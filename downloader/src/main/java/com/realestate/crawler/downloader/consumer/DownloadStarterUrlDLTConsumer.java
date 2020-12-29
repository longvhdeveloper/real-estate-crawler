package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class DownloadStarterUrlDLTConsumer {

    @KafkaListener(id = "dltGroup", topics = "download-starter.DLT")
    public void listen(Object message) throws ExecutionException, InterruptedException {
        ConsumerRecord consumerRecord = (ConsumerRecord) message;

        DownloadStarterUrlMessage downloadStarterUrlMessage = null;
        log.info("Download starter message DLT: {}", downloadStarterUrlMessage);
    }
}
