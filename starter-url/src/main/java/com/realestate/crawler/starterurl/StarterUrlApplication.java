package com.realestate.crawler.starterurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StarterUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterUrlApplication.class, args);
    }

}
