package com.realestate.crawler.datasource.commandside.validator;

import com.realestate.crawler.datasource.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.datasource.commandside.command.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateDataSourceCommandValidator implements ICommandValidator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean isValid(ICommand command) {
        CreateDataSourceCommand createDataSourceCommand = (CreateDataSourceCommand) command;

        if (createDataSourceCommand.getName().isEmpty()) {
            logger.error("Data source name is empty.");
            return false;
        }

        if (createDataSourceCommand.getUrl().isEmpty()) {
            logger.error("Data source url is empty.");
            return false;
        }

        return true;
    }
}
