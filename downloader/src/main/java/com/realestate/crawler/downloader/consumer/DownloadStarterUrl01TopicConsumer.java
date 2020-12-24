package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadStarterUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicStarterUrl01CommandHandler;
import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DownloadStarterUrl01TopicConsumer {

    private final DownloadTopicStarterUrl01CommandHandler handler;

    @Autowired
    public DownloadStarterUrl01TopicConsumer(DownloadTopicStarterUrl01CommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${kafka.topic.downloadStarter01}",
            containerFactory = "downloadStarterUrlKafkaListenerContainerFactory")
    public void listen(DownloadStarterUrlMessage message) {

        log.info("Received download starter url 01 message: {}", message);
        handler.handler(DownloadStarterUrlCommand.builder()
                .dataSourceId(message.getDatasourceId())
                .url(message.getStarterUrl())
                .build());
    }
}
