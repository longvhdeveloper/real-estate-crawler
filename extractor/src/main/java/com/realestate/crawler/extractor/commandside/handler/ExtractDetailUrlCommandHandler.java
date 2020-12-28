package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.ExtractDetailUrlCommand;
import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.repository.DetailUrlCommandRepository;
import com.realestate.crawler.extractor.commandside.repository.PropertyCommandRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractDetailUrlService;
import com.realestate.crawler.extractor.entity.PropertyParseItem;
import com.realestate.crawler.proto.CreateProperty;
import com.realestate.crawler.proto.Detailurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ExtractDetailUrlCommandHandler implements ICommandHandler {

    private final PropertyCommandRepository propertyRepository;
    private final DetailUrlCommandRepository detailUrlRepository;
    private final ExtractDetailUrlService extractDetailUrlService;

    @Autowired
    public ExtractDetailUrlCommandHandler(PropertyCommandRepository propertyRepository,
                                          DetailUrlCommandRepository detailUrlRepository,
                                          ExtractDetailUrlService extractDetailUrlService) {
        this.propertyRepository = propertyRepository;
        this.detailUrlRepository = detailUrlRepository;
        this.extractDetailUrlService = extractDetailUrlService;
    }

    @Override
    public boolean handle(ICommand command) {

        try {
            ExtractDetailUrlCommand extractDetailUrlCommand = (ExtractDetailUrlCommand) command;

            Detailurl detailurl = detailUrlRepository.findById(extractDetailUrlCommand.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Detail url with id " + extractDetailUrlCommand.getId() + " is not exist."));

            if (!isDetailUrlEnabled(detailurl)) {
                log.warn("Detail url {} is disabled", detailurl.getUrl());
                return false;
            }

            if (isHtmlContentEmpty(detailurl)) {
                log.warn("Detail url {} is empty html content", detailurl.getUrl());
                return false;
            }

//        PropertyParseItem propertyParseItem = extractDetailUrlService.parse(detailurl.getHtmlContent());
            PropertyParseItem propertyParseItem =
                    CompletableFuture.supplyAsync(() -> {
                        log.info("THREAD NAME: {}", Thread.currentThread().getName());
                        return extractDetailUrlService.parse(detailurl.getHtmlContent());
                    }).get();
            boolean result = propertyRepository.create(CreateProperty.newBuilder()
                    .setName(propertyParseItem.getName())
                    .setPrice(propertyParseItem.getPrice())
                    .setArea(propertyParseItem.getArea())
                    .setAddress(propertyParseItem.getAddress())
                    .setDescription(propertyParseItem.getDescription())
                    .setUrl(detailurl.getUrl())
                    .build());

            if (!result) {
                log.warn("CAN NOT INSERT URL {}", detailurl.getUrl());
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private boolean isDetailUrlEnabled(Detailurl detailUrl) {
        return detailUrl.getStatus() == 1;
    }

    private boolean isHtmlContentEmpty(Detailurl detailUrl) {
        return detailUrl.getHtmlContent().isEmpty();
    }
}
