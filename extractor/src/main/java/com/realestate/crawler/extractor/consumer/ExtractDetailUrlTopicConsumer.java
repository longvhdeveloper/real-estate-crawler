package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.ExtractDetailUrlCommand;
import com.realestate.crawler.extractor.commandside.handler.ExtractDetailUrlCommandHandler;
import com.realestate.crawler.extractor.message.ExtractDetailUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractDetailUrlTopicConsumer {

    private final ExtractDetailUrlCommandHandler commandHandler;

    @Autowired
    public ExtractDetailUrlTopicConsumer(ExtractDetailUrlCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @KafkaListener(topics = "${spring.kafka.topic.extractDetail}",
            containerFactory = "extractDetailUrlKafkaListenerContainerFactory")
    public void listen(ExtractDetailUrlMessage message) {

        log.info("Received download detail url message: {}", message);
        commandHandler.handler(ExtractDetailUrlCommand.builder().id(message.getId()).build());
    }
}
