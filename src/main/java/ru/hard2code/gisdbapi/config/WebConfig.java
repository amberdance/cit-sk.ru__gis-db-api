package ru.hard2code.gisdbapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.rest.api-prefix:/api}")
    private String restApiPrefix;


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(restApiPrefix, HandlerTypePredicate.forAnnotation(RestController.class));
    }


}
