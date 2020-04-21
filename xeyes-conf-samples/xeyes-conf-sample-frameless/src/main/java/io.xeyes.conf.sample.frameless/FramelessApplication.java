package io.xeyes.conf.sample.frameless;

import io.xeyes.conf.core.XEyesConfClient;
import io.xeyes.conf.core.listener.XEyesConfListener;
import io.xeyes.conf.sample.frameless.conf.FrameLessXEyesConfConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class FramelessApplication {
    private static Logger logger = LoggerFactory.getLogger(FramelessApplication.class);

    public static void main(String[] args) {
        try {
            // start
            FrameLessXEyesConfConfig.init();

            // test listener test
            XEyesConfClient.addListener("default.key01", new XEyesConfListener(){
                @Override
                public void onChange(String key, String value) throws Exception {
                    logger.info("配置变更事件通知：{}={}", key, value);
                }
            });
            String key02Val = XEyesConfClient.get("default.key02");
            logger.info("配置获取：default.key02={}", key02Val);


            while (true) {
                TimeUnit.HOURS.sleep(1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // destory
            FrameLessXEyesConfConfig.destroy();
        }
    }

}
