package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadStarterUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicStarterUrlCommandHandler;
import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void listen(DownloadStarterUrlMessage message) throws ExecutionException, InterruptedException {

        log.info("Received download starter url message: {}", message);

        CompletableFuture.runAsync(() -> handler.handle(DownloadStarterUrlCommand.builder()
                .dataSourceId(message.getDatasourceId())
                .url(message.getStarterUrl())
                .build())).get();
    }
}
