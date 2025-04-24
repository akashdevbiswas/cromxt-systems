package com.crombucket.clusterrouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ClusterManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClusterManagerApplication.class, args);
	}
}
