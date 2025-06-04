package com.wxs;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync  // 开启异步功能
@MapperScan("com.wxs.dao") // 扫描 Mapper 接口
@Slf4j
public class FruitMallBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitMallBackendApplication.class, args);

        log.info("=============================水果商城后端启动成功=============================");
    }


}
