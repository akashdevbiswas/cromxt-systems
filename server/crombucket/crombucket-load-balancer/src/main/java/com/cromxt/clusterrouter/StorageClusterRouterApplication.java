package com.cromxt.clusterrouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class StorageClusterRouterApplication {
	public static void main(String[] args) {
		SpringApplication.run(StorageClusterRouterApplication.class, args);
	}
}
