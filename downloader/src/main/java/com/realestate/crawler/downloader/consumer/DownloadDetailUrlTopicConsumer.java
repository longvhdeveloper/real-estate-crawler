package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadDetailUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicDetailUrlCommandHandler;
import com.realestate.crawler.downloader.message.DownloadDetailUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class DownloadDetailUrlTopicConsumer {

    private final DownloadTopicDetailUrlCommandHandler handler;

    @Autowired
    public DownloadDetailUrlTopicConsumer(DownloadTopicDetailUrlCommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${spring.kafka.topic.downloadDetail}",
            containerFactory = "downloadDetailUrlKafkaListenerContainerFactory")
    public void listen(DownloadDetailUrlMessage message) throws ExecutionException, InterruptedException {

        log.info("Received download detail url message: {}", message);

        CompletableFuture.runAsync(() -> handler.handle(DownloadDetailUrlCommand.builder().url(message.getUrl()).build())).get();
    }
}
