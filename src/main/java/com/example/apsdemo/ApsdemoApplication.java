package com.example.apsdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.example.apsdemo.admin.authority.system.mapper","com.example.apsdemo.admin.authority.security.mapper"})
public class ApsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApsdemoApplication.class, args);
    }

}
