package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.ExtractStarterUrlCommand;
import com.realestate.crawler.extractor.commandside.handler.ExtractStarterUrlCommandHandler;
import com.realestate.crawler.extractor.message.ExtractStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class ExtractStarterUrlConsumer {

    private final ExtractStarterUrlCommandHandler handler;

    @Autowired
    public ExtractStarterUrlConsumer(ExtractStarterUrlCommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${spring.kafka.topic.extractStarter}",
            containerFactory = "extractStarterUrlKafkaListenerContainerFactory")
    public void listen(ExtractStarterUrlMessage message) throws ExecutionException, InterruptedException {

        log.info("Received download starter url message: {}", message);
        CompletableFuture.runAsync(() -> handler.handle(ExtractStarterUrlCommand.builder()
                .id(message.getId())
                .build())).get();
    }
}
