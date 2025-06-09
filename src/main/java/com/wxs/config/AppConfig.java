package com.wxs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// Spring配置类

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.wxs.controller", "com.wxs.service", "com.wxs.util"})
public class AppConfig {
}