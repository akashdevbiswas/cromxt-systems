package com.cromxt.bucket;

import com.cromxt.bucket.constants.FileVisibility;
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
public class BucketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucketApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(Environment environment, ApplicationContext context) {
        return args -> {
            String basePath = environment.getProperty("BUCKET_CONFIG_DISK_PATH", String.class);
            assert basePath != null;

            for (FileVisibility visibility : FileVisibility.values()) {
                File directory = new File(basePath + File.separator + visibility.getAccessType());
                if (!directory.exists()) {
                    if (!directory.mkdirs()) {
                        log.error("Unable to create the public directory");
                        SpringApplication.exit(context, () -> 1);
                    }
                }

            }

        };
    }

}
