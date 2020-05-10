package io.xeyes.conf.core.factory;

import io.xeyes.conf.core.core.XEyesConfLocalCacheConf;
import io.xeyes.conf.core.core.XEyesConfMirrorConf;
import io.xeyes.conf.core.core.XEyesConfRemoteConf;
import io.xeyes.conf.core.listener.XEyesConfListenerFactory;
import io.xeyes.conf.core.listener.impl.BeanRefreshXEyesConfListener;

/**
 * XEyes 配置中心的基础工厂类
 *
 */
public class XEyesConfBaseFactory {


	/**
	 * 配置中心基类工厂的初始化方法
	 *
	 * @param adminAddress
	 * @param env
	 */
	public static void init(String adminAddress, String env, String accessToken, String mirrorFile) {
		// init
		// init remote util
		XEyesConfRemoteConf.init(adminAddress, env, accessToken);
		// init mirror util
		XEyesConfMirrorConf.init(mirrorFile);
		// init cache + thread, cycle refresh + monitor
		XEyesConfLocalCacheConf.init();

		// listener all key change
		XEyesConfListenerFactory.addListener(null, new BeanRefreshXEyesConfListener());

	}

	/**
	 * destory
	 */
	public static void destroy() {
		XEyesConfLocalCacheConf.destroy();
	}

}
