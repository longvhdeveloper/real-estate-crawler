package com.realestate.crawler.downloader.commandside.service;

import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DownloadStarter01TopicConsumerService {

    @KafkaListener(topics = "${kafka.topic.downloadStarter01}",
            containerFactory = "downloadStarterUrlKafkaListenerContainerFactory")
    public void listen(DownloadStarterUrlMessage message) {
        log.info("Received userReserve message: {}", message);
    }
}
