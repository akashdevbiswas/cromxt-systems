package com.cromxt.storageserver;

import com.cromxt.storageserver.constants.FileVisibility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class StorageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(Environment environment, ApplicationContext context) {
        return args -> {
            String basePath = environment.getProperty("STORAGE_SERVER_DISK_PATH", String.class);
            assert basePath != null;

            File directory = new File(basePath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    log.error("Unable to create the public directory");
                    SpringApplication.exit(context, () -> 1);
                }
            }

        };
    }

}
