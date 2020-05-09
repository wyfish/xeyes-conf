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

        XEyesConfBaseFactory.init(
                prop.getProperty("xeyes.conf.admin.address"),
                prop.getProperty("xeyes.conf.env"),
                prop.getProperty("xeyes.conf.access.token"),
                prop.getProperty("xeyes.conf.mirrorfile"));
    }

    /**
     * destory
     */
    public static void destroy() {
        XEyesConfBaseFactory.destroy();
    }

}
