package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.GetNextStarterUrlCommand;
import com.realestate.crawler.extractor.commandside.handler.NextStarterUrlCommandHandler;
import com.realestate.crawler.extractor.message.NextStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class NextStarterUrlTopicConsumer {

    private final NextStarterUrlCommandHandler handler;

    @Autowired
    public NextStarterUrlTopicConsumer(NextStarterUrlCommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${spring.kafka.topic.nextStarter}",
            containerFactory = "nextStarterUrlKafkaListenerContainerFactory")
    public void listen(NextStarterUrlMessage message) throws ExecutionException, InterruptedException {

        log.info("Next starter url message: {}", message);

        CompletableFuture.runAsync(() -> handler.handle(GetNextStarterUrlCommand.builder().id(message.getId()).build())).get();
    }
}
