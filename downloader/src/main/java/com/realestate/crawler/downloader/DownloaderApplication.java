package com.realestate.crawler.downloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DownloaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloaderApplication.class, args);
    }

}
