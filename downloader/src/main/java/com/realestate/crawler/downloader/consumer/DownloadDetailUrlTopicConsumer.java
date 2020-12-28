package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadDetailUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicDetailUrlCommandHandler;
import com.realestate.crawler.downloader.message.DownloadDetailUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
    public void listen(DownloadDetailUrlMessage message) {

        log.info("Received download detail url message: {}", message);

        handler.handler(DownloadDetailUrlCommand.builder().url(message.getUrl()).build());
    }
}
