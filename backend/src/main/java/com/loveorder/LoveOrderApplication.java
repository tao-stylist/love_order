package com.loveorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 情侣点餐系统 - 启动类
 * Love Order Backend Application
 */
@SpringBootApplication
@EnableTransactionManagement
public class LoveOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoveOrderApplication.class, args);
        System.out.println("========================================");
        System.out.println("  情侣点餐系统后端启动成功！");
        System.out.println("  接口文档: http://localhost:8080/swagger-ui.html");
        System.out.println("========================================");
    }
}
