package io.xeyes.conf.core.listener;

/**
 * XEyes 配置中心的监听器类
 *
 */
public interface XEyesConfListener {

    /**
     * 当程序启动或者配置发生变化时调用
     * @param key
     * @param value
     * @throws Exception
     */
    public void onChange(String key, String value) throws Exception;

}
