package com.realestate.crawler.datasource.commandside.validator;

import com.realestate.crawler.datasource.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.datasource.commandside.command.ICommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateDataSourceCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        CreateDataSourceCommand createDataSourceCommand = (CreateDataSourceCommand) command;

        if (createDataSourceCommand.getName().isEmpty()) {
            log.error("Data source name is empty.");
            return false;
        }

        if (createDataSourceCommand.getUrl().isEmpty()) {
            log.error("Data source url is empty.");
            return false;
        }

        return true;
    }
}
