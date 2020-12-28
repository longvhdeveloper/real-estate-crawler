package com.realestate.crawler.detailurl.commandside.validator;

import com.realestate.crawler.detailurl.commandside.command.CreateDetailUrlCommand;
import com.realestate.crawler.detailurl.commandside.command.ICommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateDetailUrlCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        CreateDetailUrlCommand createDetailUrlCommand = (CreateDetailUrlCommand) command;

        if (createDetailUrlCommand.getDataSourceId() <= 0) {
            log.error("Data source id is less than 0.");
            return false;
        }

        if (createDetailUrlCommand.getUrl().isEmpty()) {
            log.error("Detail url is empty.");
            return false;
        }
        return true;
    }
}
