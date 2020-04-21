package io.xeyes.conf.core.listener;

import io.xeyes.conf.core.XEyesConfClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * XEyes 配置中心的监听器工厂类
 *
 */
public class XEyesConfListenerFactory {
    private static Logger logger = LoggerFactory.getLogger(XEyesConfListenerFactory.class);

    /**
     * 配置中心的属性键值与相应监听器的集合对象(ConcurrentHashMap)
     */
    private static ConcurrentHashMap<String, List<XEyesConfListener>> keyListenerRepository = new ConcurrentHashMap<>();

    /**
     * 监听器集合
     */
    private static List<XEyesConfListener> noKeyConfListener = Collections.synchronizedList(new ArrayList<XEyesConfListener>());

    /**
     * 为特定的配置属性增加监听器
     *
     * @param key   empty will listener all key
     * @param XEyesConfListener
     * @return
     */
    public static boolean addListener(String key, XEyesConfListener XEyesConfListener){
        if (XEyesConfListener == null) {
            return false;
        }
        if (key==null || key.trim().length()==0) {
            // listen all key used
            noKeyConfListener.add(XEyesConfListener);
            return true;
        } else {

            // first use, invoke and watch this key
            try {
                String value = XEyesConfClient.get(key);
                XEyesConfListener.onChange(key, value);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            // listen this key
            List<XEyesConfListener> listeners = keyListenerRepository.computeIfAbsent(key, k -> new ArrayList<>());
            listeners.add(XEyesConfListener);
            return true;
        }
    }

    /**
     * 当配置发生变化时，调用监听器相应的的方法
     * @param key
     * @param value
     */
    public static void onChange(String key, String value){
        if (key==null || key.trim().length()==0) {
            return;
        }
        List<XEyesConfListener> keyListeners = keyListenerRepository.get(key);
        if (keyListeners!=null && keyListeners.size()>0) {
            for (XEyesConfListener listener : keyListeners) {
                try {
                    listener.onChange(key, value);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (noKeyConfListener.size() > 0) {
            for (XEyesConfListener confListener: noKeyConfListener) {
                try {
                    confListener.onChange(key, value);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
