package com.crombucket.storagemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class StorageManagerApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(StorageManagerApplicationMain.class, args);
	}

}
