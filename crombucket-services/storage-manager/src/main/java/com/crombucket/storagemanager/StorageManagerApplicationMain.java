package com.crombucket.storagemanager;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@RequiredArgsConstructor
public class StorageManagerApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(StorageManagerApplicationMain.class, args);
	}

}
