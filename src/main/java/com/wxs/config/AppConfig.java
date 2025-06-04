package com.wxs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Spring配置类

@Configuration
@ComponentScan(basePackages = {"com.wxs.controller", "com.wxs.service", "com.wxs.config", "com.wxs.util"})
public class AppConfig {
}
