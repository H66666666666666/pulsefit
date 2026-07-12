package com.itbin.config;

import com.itbin.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//配置类
public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    DemoInterceptor demoInterceptor;

//    @Autowired
//    TokenInterceptor tokenInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//       registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");//拦截所有请求
//    }
}
