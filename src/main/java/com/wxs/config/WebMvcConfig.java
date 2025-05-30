package com.wxs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${avatar.upload-dir}")
    private String avatarUploadDir;

    @Value("${product.upload-dir}")
    private String productUploadDir;

    @Value("${banner.upload-dir}")
    private String bannerUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 头像资源
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + avatarUploadDir + "/");

        // 商品图片资源
        registry.addResourceHandler("/product/**")
                .addResourceLocations("file:" + productUploadDir + "/");

        // 轮播图资源
        registry.addResourceHandler("/banner/**")
                .addResourceLocations("file:" + bannerUploadDir + "/");
    }

}