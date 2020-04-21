package io.xeyes.conf.core;

import io.xeyes.conf.core.core.XEyesConfLocalCacheConf;
import io.xeyes.conf.core.exception.XEyesConfException;
import io.xeyes.conf.core.listener.XEyesConfListener;
import io.xeyes.conf.core.listener.XEyesConfListenerFactory;

/**
 * XEyesConf 配置中心的客户端
 * 提供相应的API获取特定的配置属性，并提供对特定属性值是否变化的监听
 */
public class XEyesConfClient {

	/**
	 * 从内存中获取属性值
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static String get(String key, String defaultVal) {
		return XEyesConfLocalCacheConf.get(key, defaultVal);
	}

	/**
	 * get conf (string)
	 *
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * get conf (boolean)
	 *
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Boolean.valueOf(value);
	}

	/**
	 * get conf (short)
	 *
	 * @param key
	 * @return
	 */
	public static short getShort(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Short.valueOf(value);
	}

	/**
	 * get conf (int)
	 *
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Integer.valueOf(value);
	}

	/**
	 * get conf (long)
	 *
	 * @param key
	 * @return
	 */
	public static long getLong(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Long.valueOf(value);
	}

	/**
	 * get conf (float)
	 *
	 * @param key
	 * @return
	 */
	public static float getFloat(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Float.valueOf(value);
	}

	/**
	 * get conf (double)
	 *
	 * @param key
	 * @return
	 */
	public static double getDouble(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new XEyesConfException("config key [" + key + "] does not exist");
		}
		return Double.valueOf(value);
	}

	/**
	 * add listener with xeyes conf change
	 *
	 * @param key
	 * @param XEyesConfListener
	 * @return
	 */
	public static boolean addListener(String key, XEyesConfListener XEyesConfListener){
		return XEyesConfListenerFactory.addListener(key, XEyesConfListener);
	}
	
}
