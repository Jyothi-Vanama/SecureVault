package com.securevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SecurevaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurevaultApplication.class, args);
    }

}