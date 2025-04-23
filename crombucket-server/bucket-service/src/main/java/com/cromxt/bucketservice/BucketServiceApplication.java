package com.cromxt.bucketservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

@SpringBootApplication(exclude = {ReactiveSecurityAutoConfiguration.class})
public class BucketServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BucketServiceApplication.class, args);
    }
}
