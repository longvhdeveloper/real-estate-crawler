package com.realestate.crawler.downloader.commandside.handler;

import com.realestate.crawler.downloader.commandside.command.DownloadStarterUrlCommand;
import com.realestate.crawler.downloader.commandside.command.ICommand;
import com.realestate.crawler.downloader.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.downloader.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.downloader.commandside.service.DownloadService;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.Starterurl;
import com.realestate.crawler.proto.UpdateHtmlContentStarterUrl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DownloadTopicStarterUrl01CommandHandler implements ICommandHandler {

    private final IDataSourceRepository dataSourceRepository;
    private final IStarterUrlRepository starterUrlRepository;

    private final DownloadService downloadService;

    public DownloadTopicStarterUrl01CommandHandler(IDataSourceRepository dataSourceRepository,
                                                   IStarterUrlRepository starterUrlRepository,
                                                   DownloadService downloadService) {
        this.dataSourceRepository = dataSourceRepository;
        this.starterUrlRepository = starterUrlRepository;
        this.downloadService = downloadService;
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
        Optional<Document> documentOptional = downloadService.downloadWebPage(starterurl.getUrl());
        if (documentOptional.isPresent()) {
            log.info("Starting update HTML content");
            starterUrlRepository.updateHtmlContent(UpdateHtmlContentStarterUrl.newBuilder()
                    .setId(starterurl.getId())
                    .setHtmlContent(documentOptional.get().html())
                    .build());
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
}
