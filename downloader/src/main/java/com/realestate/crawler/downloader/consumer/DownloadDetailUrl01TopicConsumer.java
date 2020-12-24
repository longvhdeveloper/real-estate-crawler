package com.realestate.crawler.downloader.consumer;

import com.realestate.crawler.downloader.commandside.command.DownloadDetailUrlCommand;
import com.realestate.crawler.downloader.commandside.handler.DownloadTopicDetailUrl01CommandHandler;
import com.realestate.crawler.downloader.message.DownloadDetailUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DownloadDetailUrl01TopicConsumer {

    private final DownloadTopicDetailUrl01CommandHandler handler;

    @Autowired
    public DownloadDetailUrl01TopicConsumer(DownloadTopicDetailUrl01CommandHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${kafka.topic.downloadDetail01}",
            containerFactory = "downloadDetailUrlKafkaListenerContainerFactory")
    public void listen(DownloadDetailUrlMessage message) {

        log.info("Received download detail url 01 message: {}", message);

        handler.handler(DownloadDetailUrlCommand.builder().url(message.getUrl()).build());
    }
}
