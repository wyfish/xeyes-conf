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
	 * init
	 *
	 * @param adminAddress
	 * @param env
	 */
	public static void init(String adminAddress, String env, String accessToken, String mirrorFile) {
		// init
		XEyesConfRemoteConf.init(adminAddress, env, accessToken);	// init remote util
		XEyesConfMirrorConf.init(mirrorFile);			// init mirror util
		XEyesConfLocalCacheConf.init();				// init cache + thread, cycle refresh + monitor

		XEyesConfListenerFactory.addListener(null, new BeanRefreshXEyesConfListener());    // listener all key change

	}

	/**
	 * destory
	 */
	public static void destroy() {
		XEyesConfLocalCacheConf.destroy();	// destroy
	}

}
