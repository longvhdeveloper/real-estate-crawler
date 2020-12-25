package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.ExtractStarterUrl01Command;
import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.extractor.commandside.repository.IDetailUrlRepository;
import com.realestate.crawler.extractor.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractStarterUrl01Service;
import com.realestate.crawler.extractor.message.DownloadDetailUrlMessage;
import com.realestate.crawler.extractor.message.NextStarterUrlMessage;
import com.realestate.crawler.extractor.producer.IProducer;
import com.realestate.crawler.proto.CreateDetailUrl;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.Detailurl;
import com.realestate.crawler.proto.Starterurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ExtractStarterUrl01CommandHandler implements ICommandHandler {

    private final IStarterUrlRepository starterUrlRepository;
    private final IDataSourceRepository dataSourceRepository;
    private final IDetailUrlRepository detailUrlRepository;
    private final ExtractStarterUrl01Service extractStarterUrl01Service;
    private IProducer producer;

    @Autowired
    public ExtractStarterUrl01CommandHandler(IStarterUrlRepository starterUrlRepository,
                                             IDataSourceRepository dataSourceRepository,
                                             IDetailUrlRepository detailUrlRepository,
                                             ExtractStarterUrl01Service extractStarterUrl01Service,
                                             IProducer producer) {
        this.starterUrlRepository = starterUrlRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.detailUrlRepository = detailUrlRepository;
        this.extractStarterUrl01Service = extractStarterUrl01Service;
        this.producer = producer;
    }

    @Override
    public boolean handler(ICommand command) {
        ExtractStarterUrl01Command extractStarterUrl01Command = (ExtractStarterUrl01Command) command;

        Starterurl starterUrl = starterUrlRepository.findById(extractStarterUrl01Command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Starter url with id " + extractStarterUrl01Command.getId() + " is not exist."));

        if (!isStarterUrlEnabled(starterUrl)) {
            log.warn("Starter URL {} is disabled", starterUrl.getUrl());
            return false;
        }

        if (isStarterUrlHtmlContentEmpty(starterUrl)) {
            log.warn("Starter URL {} is empty html", starterUrl.getUrl());
            return false;
        }
        processStarterUrl(starterUrl);
        sendToNextStarterUrl(starterUrl);

        return true;
    }

    private void processStarterUrl(Starterurl starterUrl) {
        List<String> urlGetDetailUrls = extractStarterUrl01Service.extractStarterUrlGetDetailUrls(starterUrl);

        Datasource datasource = dataSourceRepository.get(starterUrl.getDataSourceId()).orElseThrow(
                () -> new IllegalArgumentException("Data source with id " + starterUrl.getDataSourceId() + " is not exist"));

        for (String detailUrlString : urlGetDetailUrls) {
            if (detailUrlString.startsWith("/")) {
                detailUrlString = datasource.getUrl().concat(detailUrlString);
            }

            processDetailUrl(starterUrl, detailUrlString);
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
        String topic = getDownloadDetailTopic(starterUrl);
        producer.send(topic, DownloadDetailUrlMessage.builder().url(url).build());
    }

    private void sendToNextStarterUrl(Starterurl starterUrl) {
        String topic = getNextStarterUrlTopic(starterUrl);
        producer.send(topic, new NextStarterUrlMessage(starterUrl.getId()));
    }

    private boolean isStarterUrlEnabled(Starterurl starterUrl) {
        return starterUrl.getStatus() == 1;
    }

    private boolean isStarterUrlHtmlContentEmpty(Starterurl starterUrl) {
        return starterUrl.getHtmlContent().isEmpty();
    }

    private String getDownloadDetailTopic(Starterurl starterUrl) {
        return "download-detail-" + starterUrl.getDataSourceId();
    }

    private String getNextStarterUrlTopic(Starterurl starterUrl) {
        return "next-starter-url-" + starterUrl.getDataSourceId();
    }
}
