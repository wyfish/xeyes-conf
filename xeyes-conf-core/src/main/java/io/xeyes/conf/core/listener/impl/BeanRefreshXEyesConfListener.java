package io.xeyes.conf.core.listener.impl;

import io.xeyes.conf.core.listener.XEyesConfListener;
import io.xeyes.conf.core.spring.XEyesConfFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * XEyes 配置中心监听器接口的实现类
 *
 */
public class BeanRefreshXEyesConfListener implements XEyesConfListener {

    /**
     * 内瓿静态类(object + field)
     */
    public static class BeanField{
        private String beanName;
        private String property;

        public BeanField() {
        }

        public BeanField(String beanName, String property) {
            this.beanName = beanName;
            this.property = property;
        }

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    // key : object-field[]
    private static Map<String, List<BeanField>> key2BeanField = new ConcurrentHashMap<String, List<BeanField>>();

    /**
     *
     * @param key
     * @param beanField
     */
    public static void addBeanField(String key, BeanField beanField){
        List<BeanField> beanFieldList = key2BeanField.computeIfAbsent(key, k -> new ArrayList<>());
        for (BeanField item: beanFieldList) {
            if (item.getBeanName().equals(beanField.getBeanName()) && item.getProperty().equals(beanField.getProperty())) {
                return; // avoid repeat refresh
            }
        }
        beanFieldList.add(beanField);
    }


    /**
     * 监听类的实现方法
     * @param key
     * @param value
     * @throws Exception
     */
    @Override
    public void onChange(String key, String value) throws Exception {
        List<BeanField> beanFieldList = key2BeanField.get(key);
        if (beanFieldList!=null && beanFieldList.size()>0) {
            for (BeanField beanField: beanFieldList) {
                XEyesConfFactory.refreshBeanField(beanField, value, null);
            }
        }
    }
}
