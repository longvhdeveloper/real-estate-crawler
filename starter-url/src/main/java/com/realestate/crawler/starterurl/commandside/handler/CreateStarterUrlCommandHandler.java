package com.realestate.crawler.starterurl.commandside.handler;

import com.realestate.crawler.starterurl.commandside.command.CreateStaterUrlCommand;
import com.realestate.crawler.starterurl.commandside.command.ICommand;
import com.realestate.crawler.starterurl.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.starterurl.commandside.validator.CreateStaterUrlCommandValidator;
import com.realestate.crawler.starterurl.entity.StarterUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateStarterUrlCommandHandler implements ICommandHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CreateStaterUrlCommandValidator validator;
    private final StarterUrlCommandRepository starterUrlCommandRepository;

    @Autowired
    public CreateStarterUrlCommandHandler(CreateStaterUrlCommandValidator validator,
                                          StarterUrlCommandRepository starterUrlCommandRepository) {
        this.validator = validator;
        this.starterUrlCommandRepository = starterUrlCommandRepository;
    }

    @Override
    public boolean handler(ICommand command) {
        if (!validator.isValid(command)) {
            return false;
        }

        CreateStaterUrlCommand createStaterUrlCommand = (CreateStaterUrlCommand) command;

        List<String> urlsFilter = createStaterUrlCommand.getUrls().stream().filter(url -> !url.isEmpty()).collect(Collectors.toList());

        for (final String url : urlsFilter) {
            addStaterUrl(url, createStaterUrlCommand.getDataSourceId());
        }

        return true;
    }

    private void addStaterUrl(String url, long dataSourceId) {
        StarterUrl starterUrl = new StarterUrl(url, dataSourceId);

        if (isStaterUrlExist(starterUrl)) {
            logger.warn("Starter url {} is exist in create", url);
            return;
        }

        starterUrlCommandRepository.save(starterUrl);
    }

    private boolean isStaterUrlExist(StarterUrl starterUrl) {
        return starterUrlCommandRepository.findByCheckSum(starterUrl.getCheckSumUrl(),
                starterUrl.getDataSourceId()).isPresent();
    }
}
