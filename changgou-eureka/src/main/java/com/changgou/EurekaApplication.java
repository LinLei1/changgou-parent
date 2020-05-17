package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer//开启服务
public class EurekaApplication {
    /**
     * 加载启动类
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
