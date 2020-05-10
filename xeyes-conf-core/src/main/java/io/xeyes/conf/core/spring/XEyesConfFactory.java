package io.xeyes.conf.core.spring;

import io.xeyes.conf.core.XEyesConfClient;
import io.xeyes.conf.core.annotation.XEyesConf;
import io.xeyes.conf.core.exception.XEyesConfException;
import io.xeyes.conf.core.factory.XEyesConfBaseFactory;
import io.xeyes.conf.core.listener.impl.BeanRefreshXEyesConfListener;
import io.xeyes.conf.core.util.FieldReflectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * XEyesConf 配置中心的Spring工厂类
 *
 */
public class XEyesConfFactory extends InstantiationAwareBeanPostProcessorAdapter
		implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware {

	private static Logger logger = LoggerFactory.getLogger(XEyesConfFactory.class);

	private String adminAddress;
	private String env;
	private String accessToken;
	private String mirrorFile;

	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setMirrorFile(String mirrorFile) {
        this.mirrorFile = mirrorFile;
    }


	/**
	 * 重写方法，初始化完成之后调用
	 */
	@Override
	public void afterPropertiesSet() {
		// 在工厂Bean初始化完成后，调用XEyesConfBaseFactory的初始化方法
		XEyesConfBaseFactory.init(adminAddress, env, accessToken, mirrorFile);
	}

	/**
	 * 重写方法
	 */
	@Override
	public void destroy() {
		// 析构时调用基类工厂的释放方法
		XEyesConfBaseFactory.destroy();
	}

	/**
	 * 重写方法，实例化完成之后调用
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
		// 1、Annotation('@XEyesConf')：resolves conf + watch
		if (!beanName.equals(this.beanName)) {

			ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					if (field.isAnnotationPresent(XEyesConf.class)) {
						String propertyName = field.getName();
						XEyesConf xEyesConf = field.getAnnotation(XEyesConf.class);

						String confKey =  xEyesConf.value();
						String confValue = XEyesConfClient.get(confKey,  xEyesConf.defaultValue());

						// resolves placeholders
						BeanRefreshXEyesConfListener.BeanField beanField = new BeanRefreshXEyesConfListener.BeanField(beanName, propertyName);
						refreshBeanField(beanField, confValue, bean);

						// watch
						if ( xEyesConf.callback()) {
							BeanRefreshXEyesConfListener.addBeanField(confKey, beanField);
						}
					}
				}
			});
		}

		return super.postProcessAfterInstantiation(bean, beanName);
	}

	/**
	 * 重写方法，实例化完成之后，初始化完成之前，用于修改属性
	 * @param pvs
	 * @param pds
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
		// 2、XML('$XEyesConf{...}')：resolves placeholders + watch
		if (!beanName.equals(this.beanName)) {

			PropertyValue[] pvArray = pvs.getPropertyValues();
			for (PropertyValue pv : pvArray) {
				if (pv.getValue() instanceof TypedStringValue) {
					String propertyName = pv.getName();
					String typeStringVal = ((TypedStringValue) pv.getValue()).getValue();
					assert typeStringVal != null;
					if (xmlKeyValid(typeStringVal)) {
						// object + property
						String confKey = xmlKeyParse(typeStringVal);
						String confValue = XEyesConfClient.get(confKey, "");
						// resolves placeholders
						BeanRefreshXEyesConfListener.BeanField beanField = new BeanRefreshXEyesConfListener.BeanField(beanName, propertyName);
						//refreshBeanField(beanField, confValue, bean);
						Class propClass = String.class;
						for (PropertyDescriptor item: pds) {
							if (beanField.getProperty().equals(item.getName())) {
								propClass = item.getPropertyType();
							}
						}
						Object valueObj = FieldReflectionUtil.parseValue(propClass, confValue);
						pv.setConvertedValue(valueObj);
						// watch
						BeanRefreshXEyesConfListener.addBeanField(confKey, beanField);
					}
				}
			}
		}

		return super.postProcessPropertyValues(pvs, pds, bean, beanName);
	}

	/**
	 * 根据相应的属性刷新Bean
	 * @param beanField
	 * @param value
	 * @param bean
	 */
	public static void refreshBeanField(final BeanRefreshXEyesConfListener.BeanField beanField, final String value, Object bean){
		if (bean == null) {
			// 已优化：启动时禁止使用，getBean 会导致Bean提前初始化，风险较大；
			bean = io.xeyes.conf.core.spring.XEyesConfFactory.beanFactory.getBean(beanField.getBeanName());
		}
		if (bean == null) {
			return;
		}

		BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

		// property descriptor
		PropertyDescriptor propertyDescriptor = null;
		PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
		if (propertyDescriptors.length > 0) {
			for (PropertyDescriptor item: propertyDescriptors) {
				if (beanField.getProperty().equals(item.getName())) {
					propertyDescriptor = item;
				}
			}
		}

		// refresh field: set or field
		if (propertyDescriptor!=null && propertyDescriptor.getWriteMethod() != null) {
			// support mult data types
			beanWrapper.setPropertyValue(beanField.getProperty(), value);
			logger.info("xeyes-conf, refreshBeanField[set] success, {}#{}:{}",
					beanField.getBeanName(), beanField.getProperty(), value);
		} else {

			final Object finalBean = bean;
			ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field fieldItem) throws IllegalArgumentException, IllegalAccessException {
					if (beanField.getProperty().equals(fieldItem.getName())) {
						try {
							Object valueObj = FieldReflectionUtil.parseValue(fieldItem.getType(), value);

							fieldItem.setAccessible(true);
							fieldItem.set(finalBean, valueObj);		// support multi data types

							logger.info(">>>>>>>>>>> xxl-conf, refreshBeanField[field] success, {}#{}:{}",
									beanField.getBeanName(), beanField.getProperty(), value);
						} catch (IllegalAccessException e) {
							throw new XEyesConfException(e);
						}
					}
				}
			});
		}
	}


	/**
	 * register beanDefinition If Not Exists
	 *
	 * @param registry
	 * @param beanClass
	 * @param beanName
	 * @return
	 */
	public static boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, Class<?> beanClass, String beanName) {

		// default bean name
		if (beanName == null) {
			beanName = beanClass.getName();
		}
		// avoid beanName repeat
		if (registry.containsBeanDefinition(beanName)) {
			return false;
		}

		String[] beanNameArr = registry.getBeanDefinitionNames();
		for (String beanNameItem : beanNameArr) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(beanNameItem);
			// avoid className repeat
			if (Objects.equals(beanDefinition.getBeanClassName(), beanClass.getName())) {
				return false;
			}
		}

		BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
		registry.registerBeanDefinition(beanName, annotationProcessor);
		return true;
	}


	private static final String placeholderPrefix = "$XEyesConf{";
	private static final String placeholderSuffix = "}";

	/**
	 * valid xml
	 *
	 * @param originKey
	 * @return
	 */
	private static boolean xmlKeyValid(String originKey){
		boolean start = originKey.startsWith(placeholderPrefix);
		boolean end = originKey.endsWith(placeholderSuffix);
		return start && end;
	}

	/**
	 * parse xml
	 *
	 * @param originKey
	 * @return
	 */
	private static String xmlKeyParse(String originKey){
		if (xmlKeyValid(originKey)) {
			// replace by xeyes-conf
			String key = originKey.substring(placeholderPrefix.length(), originKey.length() - placeholderSuffix.length());
			return key;
		}
		return null;
	}

	private String beanName;

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	private static BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		XEyesConfFactory.beanFactory = beanFactory;
	}

}
