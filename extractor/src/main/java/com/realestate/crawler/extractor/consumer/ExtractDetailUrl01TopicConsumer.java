package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.ExtractDetailUrl01Command;
import com.realestate.crawler.extractor.commandside.handler.ExtractDetailUrl01CommandHandler;
import com.realestate.crawler.extractor.message.ExtractDetailUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractDetailUrl01TopicConsumer {

    private final ExtractDetailUrl01CommandHandler commandHandler;

    @Autowired
    public ExtractDetailUrl01TopicConsumer(ExtractDetailUrl01CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @KafkaListener(topics = "${kafka.topic.extractDetail01}",
            containerFactory = "extractDetailUrlKafkaListenerContainerFactory")
    public void listen(ExtractDetailUrlMessage message) {

        log.info("Received download detail url 01 message: {}", message);
        commandHandler.handler(ExtractDetailUrl01Command.builder().id(message.getId()).build());
    }
}
