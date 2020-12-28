package com.realestate.crawler.downloader.commandside.handler;

import com.realestate.crawler.downloader.commandside.command.DownloadDetailUrlCommand;
import com.realestate.crawler.downloader.commandside.command.ICommand;
import com.realestate.crawler.downloader.commandside.repository.IDetailUrlRepository;
import com.realestate.crawler.downloader.commandside.service.DownloadService;
import com.realestate.crawler.downloader.message.ExtractDetailUrlMessage;
import com.realestate.crawler.downloader.producer.IProducer;
import com.realestate.crawler.proto.Detailurl;
import com.realestate.crawler.proto.UpdateHtmlContentDetailUrl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DownloadTopicDetailUrlCommandHandler implements ICommandHandler {

    private final IDetailUrlRepository detailUrlRepository;
    private final DownloadService downloadService;
    private final IProducer producer;

    @Value("${spring.kafka.topic.extractDetail}")
    private String extractDetailTopic;

    @Autowired
    public DownloadTopicDetailUrlCommandHandler(IDetailUrlRepository detailUrlRepository,
                                                DownloadService downloadService, IProducer producer) {
        this.detailUrlRepository = detailUrlRepository;
        this.downloadService = downloadService;
        this.producer = producer;
    }

    @Override
    public boolean handler(ICommand command) {
        DownloadDetailUrlCommand downloadDetailUrlCommand = (DownloadDetailUrlCommand) command;

        Optional<Detailurl> optional = detailUrlRepository.findByUrl(downloadDetailUrlCommand.getUrl());

        if (optional.isEmpty() || optional.get().getUrl().isEmpty()) {
            throw new IllegalArgumentException("Detail url " + downloadDetailUrlCommand.getUrl() + " is not exist");
        }

        Detailurl detailUrl = optional.get();

        if (!isDetailUrlEnabled(detailUrl)) {
            log.warn("Detail url {} is disabled", detailUrl.getUrl());
            return false;
        }

        Optional<Document> documentOptional = downloadService.downloadWebPage(detailUrl.getUrl());
        if (documentOptional.isPresent()) {
            log.info("Starting update HTML content detail url");

            detailUrlRepository.updateHtmlContent(UpdateHtmlContentDetailUrl.newBuilder()
                    .setId(detailUrl.getId())
                    .setHtmlContent(documentOptional.get().html())
                    .build());

            sendToExtractDetailUrl(detailUrl);
            return true;
        }

        return false;
    }

    private void sendToExtractDetailUrl(Detailurl detailUrl) {
        producer.send(extractDetailTopic, ExtractDetailUrlMessage.builder().id(detailUrl.getId()).build());
    }

    private boolean isDetailUrlEnabled(Detailurl detailUrl) {
        return detailUrl.getStatus() == 1;
    }
}
