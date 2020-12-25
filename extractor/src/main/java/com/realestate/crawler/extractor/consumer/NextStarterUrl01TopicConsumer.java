package com.realestate.crawler.extractor.consumer;

import com.realestate.crawler.extractor.commandside.command.NextStarterUrl01Command;
import com.realestate.crawler.extractor.commandside.handler.NextStarterUrl01CommandHandler;
import com.realestate.crawler.extractor.message.NextStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NextStarterUrl01TopicConsumer {

    private final NextStarterUrl01CommandHandler handler;

    @Autowired
    public NextStarterUrl01TopicConsumer(NextStarterUrl01CommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${kafka.topic.nextStarter01}",
            containerFactory = "nextStarterUrlKafkaListenerContainerFactory")
    public void listen(NextStarterUrlMessage message) {

        log.info("Next starter url 01 message: {}", message);

        handler.handler(NextStarterUrl01Command.builder().id(message.getId()).build());
    }
}
