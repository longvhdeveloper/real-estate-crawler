package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.ExtractStarterUrlCommand;
import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.repository.DataSourceCommandRepository;
import com.realestate.crawler.extractor.commandside.repository.DetailUrlCommandRepository;
import com.realestate.crawler.extractor.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractStarterUrlService;
import com.realestate.crawler.extractor.message.DownloadDetailUrlMessage;
import com.realestate.crawler.extractor.message.NextStarterUrlMessage;
import com.realestate.crawler.extractor.producer.IProducer;
import com.realestate.crawler.proto.CreateDetailUrl;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.Detailurl;
import com.realestate.crawler.proto.Starterurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ExtractStarterUrlCommandHandler implements ICommandHandler {

    private final StarterUrlCommandRepository starterUrlRepository;
    private final DataSourceCommandRepository dataSourceRepository;
    private final DetailUrlCommandRepository detailUrlRepository;
    private final ExtractStarterUrlService extractStarterUrlService;
    private IProducer producer;

    @Value("${spring.kafka.topic.downloadDetail}")
    private String downloadDetailUrlTopic;

    @Value("${spring.kafka.topic.nextStarter}")
    private String getNextStarterUrlTopic;

    @Autowired
    public ExtractStarterUrlCommandHandler(StarterUrlCommandRepository starterUrlRepository,
                                           DataSourceCommandRepository dataSourceRepository,
                                           DetailUrlCommandRepository detailUrlRepository,
                                           ExtractStarterUrlService extractStarterUrlService,
                                           IProducer producer) {
        this.starterUrlRepository = starterUrlRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.detailUrlRepository = detailUrlRepository;
        this.extractStarterUrlService = extractStarterUrlService;
        this.producer = producer;
    }

    @Override
    public boolean handle(ICommand command) {
        try {
            ExtractStarterUrlCommand extractStarterUrlCommand = (ExtractStarterUrlCommand) command;

            Starterurl starterUrl = starterUrlRepository.findById(extractStarterUrlCommand.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Starter url with id " + extractStarterUrlCommand.getId() + " is not exist."));

            if (!isStarterUrlEnabled(starterUrl)) {
                log.warn("Starter URL {} is disabled", starterUrl.getUrl());
                return false;
            }

            if (isStarterUrlHtmlContentEmpty(starterUrl)) {
                log.warn("Starter URL {} is empty html", starterUrl.getUrl());
                return false;
            }
            CompletableFuture.runAsync(() -> processStarterUrl(starterUrl)).get();
            CompletableFuture.runAsync(() -> sendToNextStarterUrl(starterUrl)).get();

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    private void processStarterUrl(Starterurl starterUrl) {
        try {
            List<String> urlGetDetailUrls = extractStarterUrlService.extractStarterUrlGetDetailUrls(starterUrl);

            Datasource datasource = dataSourceRepository.get(starterUrl.getDataSourceId()).orElseThrow(
                    () -> new IllegalArgumentException("Data source with id " + starterUrl.getDataSourceId() + " is not exist"));

            for (String detailUrlString : urlGetDetailUrls) {
                if (detailUrlString.startsWith("/")) {
                    detailUrlString = datasource.getUrl().concat(detailUrlString);
                }

                String finalDetailUrlString = detailUrlString;
                CompletableFuture.runAsync(() -> processDetailUrl(starterUrl, finalDetailUrlString)).get();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void processDetailUrl(Starterurl starterUrl, String url) {
        Optional<Detailurl> optional = detailUrlRepository.findByUrl(url);
        if (optional.isPresent() && !optional.get().getUrl().isEmpty()) {
            log.warn("Detail URL {} is exist", url);
            return;
        }
        detailUrlRepository.create(CreateDetailUrl.newBuilder()
                .setDataSourceId(starterUrl.getDataSourceId())
                .setUrl(url)
                .build());

        sendToDownloadDetailUrl(starterUrl, url);
    }

    private void sendToDownloadDetailUrl(Starterurl starterUrl, String url) {
        producer.send(downloadDetailUrlTopic, DownloadDetailUrlMessage.builder().url(url).build());
    }

    private void sendToNextStarterUrl(Starterurl starterUrl) {
        producer.send(getNextStarterUrlTopic, new NextStarterUrlMessage(starterUrl.getId()));
    }

    private boolean isStarterUrlEnabled(Starterurl starterUrl) {
        return starterUrl.getStatus() == 1;
    }

    private boolean isStarterUrlHtmlContentEmpty(Starterurl starterUrl) {
        return starterUrl.getHtmlContent().isEmpty();
    }
}
