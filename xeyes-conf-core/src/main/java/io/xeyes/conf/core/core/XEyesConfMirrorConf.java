package io.xeyes.conf.core.core;

import io.xeyes.conf.core.exception.XEyesConfException;
import io.xeyes.conf.core.util.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * XEyes 配置中心的镜像配置，镜像数据存在于本地文件中
 *
 */
public class XEyesConfMirrorConf {
    private static Logger logger = LoggerFactory.getLogger(XEyesConfMirrorConf.class);

    private static String mirrorFile = null;

    public static void init(String mirrorFileParam){
        // valid
        if (mirrorFileParam==null || mirrorFileParam.trim().length()==0) {
            throw new XEyesConfException("xeyes-conf mirrorFileParam can not be empty");
        }

        mirrorFile = mirrorFileParam;
    }

    /**
     * 从文件中读取镜像配置
     *
     * @return
     */
    public static Map<String, String> readConfMirror(){
        Properties mirrorProp = PropUtil.loadFileProp(mirrorFile);
        if (mirrorProp!=null && mirrorProp.stringPropertyNames()!=null && mirrorProp.stringPropertyNames().size()>0) {
            Map<String, String> mirrorConfData = new HashMap<>();
            for (String key: mirrorProp.stringPropertyNames()) {
                mirrorConfData.put(key, mirrorProp.getProperty(key));
            }
            return mirrorConfData;
        }
        return null;
    }

    /**
     * 把配置的镜像内容写到本地文件中
     *
     * @param mirrorConfDataParam
     */
    public static void writeConfMirror(Map<String, String> mirrorConfDataParam){
        Properties properties = new Properties();
        for (Map.Entry<String, String> confItem: mirrorConfDataParam.entrySet()) {
            properties.setProperty(confItem.getKey(), confItem.getValue());
        }

        // write mirror file
        PropUtil.writeFileProp(properties, mirrorFile);
    }

}
