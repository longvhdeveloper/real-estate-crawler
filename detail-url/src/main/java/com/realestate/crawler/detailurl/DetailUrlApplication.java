package com.realestate.crawler.detailurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DetailUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DetailUrlApplication.class, args);
    }

}
