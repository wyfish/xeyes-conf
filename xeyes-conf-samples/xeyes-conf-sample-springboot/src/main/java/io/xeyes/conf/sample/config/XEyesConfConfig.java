package io.xeyes.conf.sample.config;

import io.xeyes.conf.core.spring.XEyesConfFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-conf config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XEyesConfConfig {
    private Logger logger = LoggerFactory.getLogger(XEyesConfConfig.class);


    @Value("${xxl.conf.admin.address}")
    private String adminAddress;

    @Value("${xxl.conf.env}")
    private String env;

    @Value("${xxl.conf.access.token}")
    private String accessToken;

    @Value("${xeyes.conf.mirrorfile}")
    private String mirrorfile;


    @Bean
    public XEyesConfFactory xEyesConfFactory() {

        XEyesConfFactory xEyesConf = new XEyesConfFactory();
        xEyesConf.setAdminAddress(adminAddress);
        xEyesConf.setEnv(env);
        xEyesConf.setAccessToken(accessToken);
        xEyesConf.setMirrorFile(mirrorfile);

        logger.info(">>>>>>>>>>> xeyes-conf config init.");
        return xEyesConf;
    }

}