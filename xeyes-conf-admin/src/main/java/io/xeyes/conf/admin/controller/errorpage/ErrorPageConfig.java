package io.xeyes.conf.admin.controller.errorpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryCustomizer;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 *
 */
@Configuration
public class ErrorPageConfig {

    @Autowired ServerProperties serverProperties;

    @Bean
    public WebServerFactoryCustomizer containerCustomizer() {
        return new ServletWebServerFactoryCustomizer(serverProperties) {
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {

                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/static/html/500.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/html/500.html");

                factory.addErrorPages(error404Page, error500Page);
            }
        };
    }

}