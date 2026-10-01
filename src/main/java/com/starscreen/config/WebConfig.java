package com.starscreen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")     // 拦截所有请求
                .excludePathPatterns(        // 但这些放行
                        "/login",
                        "/api/user/login",
                        "/api/user/register",
                        "/",
                        "/images/**",
                        "/css/**",
                        "/js/**",
                        "/favicon.ico",
                        "/error"
                );
    }
}