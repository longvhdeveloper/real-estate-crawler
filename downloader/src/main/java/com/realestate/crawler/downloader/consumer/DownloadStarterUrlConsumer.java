package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadStarterUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicStarterUrlCommandHandler;
import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DownloadStarterUrlConsumer {

    private final DownloadTopicStarterUrlCommandHandler handler;

    @Autowired
    public DownloadStarterUrlConsumer(DownloadTopicStarterUrlCommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${spring.kafka.topic.downloadStarter}",
            containerFactory = "downloadStarterUrlKafkaListenerContainerFactory")
    public void listen(DownloadStarterUrlMessage message) {

        log.info("Received download starter url message: {}", message);
        handler.handler(DownloadStarterUrlCommand.builder()
                .dataSourceId(message.getDatasourceId())
                .url(message.getStarterUrl())
                .build());
    }
}
