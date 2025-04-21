package io.bvb.smarthealthcare.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartHealthcareBackendApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartHealthcareBackendApplication.class);

    public static void main(String[] args) {
        LOGGER.info("**** Starting SmartHealthcareBackendApplication  ****");
        SpringApplication.run(SmartHealthcareBackendApplication.class, args);
    }

}
