package com.crombucket.storagemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class StorageManagerApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(StorageManagerApplicationMain.class, args);
	}

}
