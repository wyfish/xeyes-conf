package io.xeyes.conf.sample.frameless.conf;

import io.xeyes.conf.core.factory.XEyesConfBaseFactory;
import io.xeyes.conf.core.util.PropUtil;

import java.util.Properties;

/**
 *
 */
public class FrameLessXEyesConfConfig {

    /**
     * init
     */
    public static void init() {
        Properties prop = PropUtil.loadProp("xeyes-conf.properties");

        XxlConfBaseFactory.init(
                prop.getProperty("xxl.conf.admin.address"),
                prop.getProperty("xxl.conf.env"),
                prop.getProperty("xxl.conf.access.token"),
                prop.getProperty("xxl.conf.mirrorfile"));
    }

    /**
     * destory
     */
    public static void destroy() {
        XEyesConfBaseFactory.destroy();
    }

}
