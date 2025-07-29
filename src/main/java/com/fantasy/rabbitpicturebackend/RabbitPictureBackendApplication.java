package com.fantasy.rabbitpicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.fantasy.rabbitpicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class RabbitPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitPictureBackendApplication.class, args);
    }

}
