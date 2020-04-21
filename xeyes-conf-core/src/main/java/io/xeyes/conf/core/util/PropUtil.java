package io.xeyes.conf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;

/**
 * Properties 工具类
 */
public class PropUtil {

    private static Logger logger = LoggerFactory.getLogger(PropUtil.class);

    /**
     * load prop
     *
     * @param propertyFileName disk path when start with "file:", other classpath
     * @return
     */
    public static Properties loadProp(String propertyFileName) {

        // disk path
        if (propertyFileName.startsWith("file:")) {
            propertyFileName = propertyFileName.substring("file:".length());
            return loadFileProp(propertyFileName);
        } else {
            return loadClassPathProp(propertyFileName);
        }
    }

    public static Properties loadClassPathProp(String propertyFileName) {

        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName);
            if (in == null) {
                return null;
            }

            Properties prop = new Properties();
            prop.load(new InputStreamReader(in, "utf-8"));
            return prop;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }


    public static Properties loadFileProp(String propertyFileName) {
        InputStream in = null;
        try {
            // load file location, disk
            File file = new File(propertyFileName);
            if (!file.exists()) {
                return null;
            }

            URL url = new File(propertyFileName).toURI().toURL();
            in = new FileInputStream(url.getPath());
            if (in == null) {
                return null;
            }

            Properties prop = new Properties();
            prop.load(new InputStreamReader(in, "utf-8"));
            return prop;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }


    /**
     * write prop to disk
     *
     * @param properties
     * @param filePathName
     * @return
     */
    public static boolean writeFileProp(Properties properties, String filePathName){
        FileOutputStream fileOutputStream = null;
        try {

            // mk file
            File file = new File(filePathName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            // write data
            fileOutputStream = new FileOutputStream(file, false);
            properties.store(new OutputStreamWriter(fileOutputStream, "utf-8"), null);
            //properties.store(new FileWriter(filePathName), null);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
