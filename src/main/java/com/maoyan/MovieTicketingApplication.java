package com.maoyan; // 声明当前类所在的包

import org.springframework.boot.SpringApplication; // 导入 Spring Boot 启动类
import org.springframework.boot.autoconfigure.SpringBootApplication; // 导入自动配置注解
import org.springframework.scheduling.annotation.EnableScheduling; // 导入定时任务开关

/**
 * 项目启动类
 * 启动后访问 http://localhost:8080/ 即可看到首页
 */
@SpringBootApplication // 开启 Spring Boot 自动配置和组件扫描
@EnableScheduling      // ★ 开启定时任务支持
public class MovieTicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTicketingApplication.class, args);
    }
}
