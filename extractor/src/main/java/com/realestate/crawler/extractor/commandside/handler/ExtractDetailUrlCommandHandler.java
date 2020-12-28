package com.realestate.crawler.extractor.commandside.handler;

import com.realestate.crawler.extractor.commandside.command.ExtractDetailUrlCommand;
import com.realestate.crawler.extractor.commandside.command.ICommand;
import com.realestate.crawler.extractor.commandside.repository.IDetailUrlRepository;
import com.realestate.crawler.extractor.commandside.repository.IPropertyRepository;
import com.realestate.crawler.extractor.commandside.service.ExtractDetailUrl01Service;
import com.realestate.crawler.extractor.entity.PropertyParseItem;
import com.realestate.crawler.proto.CreateProperty;
import com.realestate.crawler.proto.Detailurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractDetailUrlCommandHandler implements ICommandHandler {

    private final IPropertyRepository propertyRepository;
    private final IDetailUrlRepository detailUrlRepository;
    private final ExtractDetailUrl01Service extractDetailUrl01Service;

    @Autowired
    public ExtractDetailUrlCommandHandler(IPropertyRepository propertyRepository,
                                          IDetailUrlRepository detailUrlRepository,
                                          ExtractDetailUrl01Service extractDetailUrl01Service) {
        this.propertyRepository = propertyRepository;
        this.detailUrlRepository = detailUrlRepository;
        this.extractDetailUrl01Service = extractDetailUrl01Service;
    }

    @Override
    public boolean handler(ICommand command) {

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

        PropertyParseItem propertyParseItem = extractDetailUrl01Service.parse(detailurl.getHtmlContent());
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
    }

    private boolean isDetailUrlEnabled(Detailurl detailUrl) {
        return detailUrl.getStatus() == 1;
    }

    private boolean isHtmlContentEmpty(Detailurl detailUrl) {
        return detailUrl.getHtmlContent().isEmpty();
    }
}
