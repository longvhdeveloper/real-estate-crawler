package com.realestate.crawler.downloader.commandside.handler;

import com.realestate.crawler.downloader.commandside.command.DownloadStarterUrlCommand;
import com.realestate.crawler.downloader.commandside.command.ICommand;
import com.realestate.crawler.downloader.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.downloader.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.downloader.commandside.service.DownloadService;
import com.realestate.crawler.downloader.message.ExtractStarterUrlMessage;
import com.realestate.crawler.downloader.producer.IProducer;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.Starterurl;
import com.realestate.crawler.proto.UpdateHtmlContentStarterUrl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DownloadTopicStarterUrlCommandHandler implements ICommandHandler {

    private final IDataSourceRepository dataSourceRepository;
    private final IStarterUrlRepository starterUrlRepository;

    private final DownloadService downloadService;

    private final IProducer producer;

    @Value("${spring.kafka.topic.extractStarter}")
    private String extractStarterTopic;

    @Autowired
    public DownloadTopicStarterUrlCommandHandler(IDataSourceRepository dataSourceRepository,
                                                 IStarterUrlRepository starterUrlRepository,
                                                 DownloadService downloadService,
                                                 IProducer producer) {
        this.dataSourceRepository = dataSourceRepository;
        this.starterUrlRepository = starterUrlRepository;
        this.downloadService = downloadService;
        this.producer = producer;
    }

    @Override
    public boolean handler(ICommand command) {
        DownloadStarterUrlCommand downloadStarterUrlCommand = (DownloadStarterUrlCommand) command;

        Datasource datasource = dataSourceRepository.get(downloadStarterUrlCommand.getDataSourceId()).orElseThrow(
                () -> new IllegalArgumentException("Data source with id " + downloadStarterUrlCommand.getDataSourceId() + " is not exist"));

        if (!isDataSourceEnabled(datasource)) {
            log.warn("Data source with id {} is disabled", datasource.getId());
            return false;
        }

        Optional<Starterurl> optional = starterUrlRepository.getStarterUrlByUrl(downloadStarterUrlCommand.getDataSourceId(),
                downloadStarterUrlCommand.getUrl());

        if (optional.isEmpty()) {
            log.warn("Starter Url {} is not exist", downloadStarterUrlCommand.getUrl());
            return false;
        }

        Starterurl starterurl = optional.get();

        if (!isStarterUrlEnabled(starterurl)) {
            log.warn("Starter Url {} is disabled", downloadStarterUrlCommand.getUrl());
            return false;
        }

        Optional<Document> documentOptional = downloadService.downloadWebPage(starterurl.getUrl());
        if (documentOptional.isPresent()) {
            log.info("Starting update HTML content starter url");
            starterUrlRepository.updateHtmlContent(UpdateHtmlContentStarterUrl.newBuilder()
                    .setId(starterurl.getId())
                    .setHtmlContent(documentOptional.get().html())
                    .build());

            sendToExtractorStarterUrl(starterurl);

            return true;
        }

        return false;
    }

    private boolean isDataSourceEnabled(Datasource datasource) {
        return datasource.getStatus() == 1;
    }

    private boolean isStarterUrlEnabled(Starterurl starterurl) {
        return starterurl.getStatus() == 1;
    }

    private void sendToExtractorStarterUrl(Starterurl starterurl) {
        producer.send(extractStarterTopic, ExtractStarterUrlMessage.builder().id(starterurl.getId()).build());
    }
}
