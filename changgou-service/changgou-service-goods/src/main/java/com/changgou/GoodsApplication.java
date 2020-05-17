package com.changgou;

import entity.IdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.dao" })
@EnableSwagger2
public class GoodsApplication {
    @Value("${workerId}")
    private Integer workerId;

    @Value("${datacenterId}")
    private Integer datacenterId;

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(workerId,datacenterId);
    }
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }
}
