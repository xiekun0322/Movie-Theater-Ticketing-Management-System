package com.maoyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieTicketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTicketingApplication.class, args);
    }
}
