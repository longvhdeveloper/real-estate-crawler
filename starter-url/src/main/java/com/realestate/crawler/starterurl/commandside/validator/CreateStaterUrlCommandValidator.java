package com.realestate.crawler.starterurl.commandside.validator;

import com.realestate.crawler.starterurl.commandside.command.CreateStaterUrlCommand;
import com.realestate.crawler.starterurl.commandside.command.ICommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateStaterUrlCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        CreateStaterUrlCommand createStaterUrlCommand = (CreateStaterUrlCommand) command;

        if (createStaterUrlCommand.getDataSourceId() <= 0) {
            return false;
        }

        List<String> urlsFilter = createStaterUrlCommand.getUrls().stream().filter(
                url -> !url.isEmpty()).collect(Collectors.toList());

        if (urlsFilter.isEmpty()) {
            return false;
        }

        return true;
    }
}
