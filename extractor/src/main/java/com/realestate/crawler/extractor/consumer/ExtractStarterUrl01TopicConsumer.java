package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.ExtractStarterUrl01Command;
import com.realestate.crawler.extractor.commandside.handler.ExtractStarterUrl01CommandHandler;
import com.realestate.crawler.extractor.message.ExtractStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractStarterUrl01TopicConsumer {

    private final ExtractStarterUrl01CommandHandler handler;

    @Autowired
    public ExtractStarterUrl01TopicConsumer(ExtractStarterUrl01CommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${kafka.topic.extractStarter01}",
            containerFactory = "extractStarterUrlKafkaListenerContainerFactory")
    public void listen(ExtractStarterUrlMessage message) {

        log.info("Received download starter url 01 message: {}", message);
        handler.handler(ExtractStarterUrl01Command.builder()
                .id(message.getId())
                .build());
    }
}
