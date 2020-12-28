package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.GetNextStarterUrlCommand;
import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractStarterUrlService;
import com.realestate.crawler.extractor.message.DownloadStarterUrlMessage;
import com.realestate.crawler.extractor.producer.IProducer;
import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.Starterurl;
import com.realestate.crawler.proto.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NextStarterUrlCommandHandler implements ICommandHandler {

    private final StarterUrlCommandRepository starterUrlRepository;
    private final ExtractStarterUrlService extractStarterUrlService;
    private final IProducer producer;

    @Value("${spring.kafka.topic.downloadStarter}")
    private String downloadStarterTopic;

    @Autowired
    public NextStarterUrlCommandHandler(StarterUrlCommandRepository starterUrlRepository,
                                        ExtractStarterUrlService extractStarterUrlService,
                                        IProducer producer) {
        this.starterUrlRepository = starterUrlRepository;
        this.extractStarterUrlService = extractStarterUrlService;
        this.producer = producer;
    }

    @Override
    public boolean handle(ICommand command) {

        GetNextStarterUrlCommand getNextStarterUrlCommand = (GetNextStarterUrlCommand) command;

        Starterurl starterUrl = starterUrlRepository.findById(getNextStarterUrlCommand.getId())
                .orElseThrow(() -> new IllegalArgumentException("Starter url with id " + getNextStarterUrlCommand.getId() + " is not exist."));

        if (!isStarterUrlEnabled(starterUrl)) {
            log.warn("Starter URL {} is disabled", starterUrl.getUrl());
            return false;
        }

        int currentPageNumber = extractStarterUrlService.getCurrentPageNumber(starterUrl);

        log.info("===== CURRENT PAGE: {}", currentPageNumber);

        // TODO to keep test small data
        int limit = 5;
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
        producer.send(downloadStarterTopic, new DownloadStarterUrlMessage(starterurl.getDataSourceId(), url));
    }

    private boolean isStarterUrlEnabled(Starterurl starterUrl) {
        return starterUrl.getStatus() == 1;
    }
}
