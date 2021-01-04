package com.realestate.crawler.downloader.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class DownloadStarterUrlDLTConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    public DownloadStarterUrlDLTConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "dltGroup", topics = "${spring.kafka.topic.downloadStarterDLT}")
    public void listen(Object message) throws ExecutionException, InterruptedException, JsonProcessingException {
        ConsumerRecord consumerRecord = (ConsumerRecord) message;
        DownloadStarterUrlMessage downloadStarterUrlMessage = objectMapper.readValue(consumerRecord.value().toString(), DownloadStarterUrlMessage.class);

        log.info("Download starter message DLT: {}", downloadStarterUrlMessage);
    }
}
