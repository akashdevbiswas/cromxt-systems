package com.cromxt.bucket;

import com.cromxt.bucket.constants.FileConstants;
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

            File privateDirectory = new File(basePath + File.separator + FileConstants.PRIVATE_ACCESS.getAccessType());
            if (!privateDirectory.exists()) {
                if (!privateDirectory.mkdirs()) {
                    log.error("Unable to create the private directory");
                    SpringApplication.exit(context, () -> 1);
                }
            }

            File publicDirectory = new File(basePath + File.separator + FileConstants.PUBLIC_ACCESS.getAccessType());
            if (!publicDirectory.exists()) {
                if (!publicDirectory.mkdirs()) {
                    log.error("Unable to create the public directory");
                    SpringApplication.exit(context, () -> 1);
                }
            }

            File protectedDirectory = new File(basePath + File.separator + FileConstants.PROTECTED_ACCESS.getAccessType());
            if (!protectedDirectory.exists()) {
                if (!protectedDirectory.mkdirs()) {
                    log.error("Unable to create the protected directory");
                    SpringApplication.exit(context, () -> 1);
                }
            }

        };
    }

}
