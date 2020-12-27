package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.command.NextStarterUrl01Command;
import com.realestate.crawler.extractor.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractStarterUrl01Service;
import com.realestate.crawler.extractor.message.DownloadStarterUrlMessage;
import com.realestate.crawler.extractor.producer.IProducer;
import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.Starterurl;
import com.realestate.crawler.proto.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NextStarterUrl01CommandHandler implements ICommandHandler {

    private final IStarterUrlRepository starterUrlRepository;
    private final ExtractStarterUrl01Service extractStarterUrl01Service;
    private IProducer producer;

    @Autowired
    public NextStarterUrl01CommandHandler(IStarterUrlRepository starterUrlRepository,
                                          ExtractStarterUrl01Service extractStarterUrl01Service,
                                          IProducer producer) {
        this.starterUrlRepository = starterUrlRepository;
        this.extractStarterUrl01Service = extractStarterUrl01Service;
        this.producer = producer;
    }

    @Override
    public boolean handler(ICommand command) {

        NextStarterUrl01Command nextStarterUrl01Command = (NextStarterUrl01Command) command;

        Starterurl starterUrl = starterUrlRepository.findById(nextStarterUrl01Command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Starter url with id " + nextStarterUrl01Command.getId() + " is not exist."));

        if (!isStarterUrlEnabled(starterUrl)) {
            log.warn("Starter URL {} is disabled", starterUrl.getUrl());
            return false;
        }

        int currentPageNumber = extractStarterUrl01Service.getCurrentPageNumber(starterUrl);

        log.info("===== CURRENT PAGE: {}", currentPageNumber);

        // TODO to keep test small data
        int limit = 3;
        if (currentPageNumber > 0 && currentPageNumber < limit) {
            String nextStarterUrl = starterUrl.getUrl().replace("/p" + currentPageNumber, "");
            int nextPageNumber = currentPageNumber + 1;
            nextStarterUrl = nextStarterUrl + "/p" + nextPageNumber;
            log.info("==== NEXT URL {}", nextStarterUrl);
            createStarterUrl(nextStarterUrl, starterUrl);
            sendToDownloadStarterUrl(nextStarterUrl, starterUrl);
            return true;
        }

        return false;
    }

    private void createStarterUrl(String url, Starterurl starterurl) {
        log.info("Create next starter url......");
        starterUrlRepository.create(CreateStaterUrl.newBuilder()
                .addUrls(Url.newBuilder()
                        .setUrlString(url)
                        .build())
                .setDataSourceId(starterurl.getDataSourceId())
                .build());
    }

    private void sendToDownloadStarterUrl(String url, Starterurl starterurl) {
        String topic = getTopic(starterurl);
        producer.send(topic, new DownloadStarterUrlMessage(starterurl.getDataSourceId(), url));
    }

    private boolean isStarterUrlEnabled(Starterurl starterUrl) {
        return starterUrl.getStatus() == 1;
    }

    private String getTopic(Starterurl starterUrl) {
        return "download-starter-" + starterUrl.getDataSourceId();
    }
}
