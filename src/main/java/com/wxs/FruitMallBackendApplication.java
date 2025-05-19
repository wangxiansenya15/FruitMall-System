package com.wxs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.wxs.dao") // 扫描 Mapper 接口
@ComponentScan(basePackages = {"com.wxs.controller", "com.wxs.service", "com.wxs.config", "com.wxs.util"})
public class FruitMallBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitMallBackendApplication.class, args);
    }

}
