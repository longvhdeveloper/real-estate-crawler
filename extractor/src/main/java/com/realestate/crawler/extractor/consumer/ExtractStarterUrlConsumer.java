package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.ExtractStarterUrlCommand;
import com.realestate.crawler.extractor.commandside.handler.ExtractStarterUrlCommandHandler;
import com.realestate.crawler.extractor.message.ExtractStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
    public void listen(ExtractStarterUrlMessage message) {

        log.info("Received download starter url message: {}", message);
        handler.handler(ExtractStarterUrlCommand.builder()
                .id(message.getId())
                .build());
    }
}
