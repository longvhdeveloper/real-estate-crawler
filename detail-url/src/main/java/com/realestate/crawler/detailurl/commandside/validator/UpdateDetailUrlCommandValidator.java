package com.realestate.crawler.detailurl.commandside.validator;

import com.realestate.crawler.detailurl.commandside.command.ICommand;
import com.realestate.crawler.detailurl.commandside.command.UpdateHtmlContentCommand;
import org.springframework.stereotype.Component;

@Component
public class UpdateDetailUrlCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        UpdateHtmlContentCommand updateHtmlContentCommand = (UpdateHtmlContentCommand) command;

        if (updateHtmlContentCommand.getId() <= 0) {
            return false;
        }

        if (updateHtmlContentCommand.getHtml().isEmpty()) {
            return false;
        }

        return true;
    }
}
