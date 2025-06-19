package com.example.customcakes_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //spring 서버가 동작하는 루트(cisVue)
        String basePath = System.getProperty("user.dir").replace("\\", "/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + basePath + "/upload/images/");
        System.out.println("정적 리소스 등록됨: " + basePath + "/upload/images/");

        /*
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/Users/yunjeongha/projectexample/upload/images/");
         */
    }
}