package com.realestate.crawler.starterurl.commandside.validator;

import com.realestate.crawler.starterurl.commandside.command.ICommand;
import com.realestate.crawler.starterurl.commandside.command.UpdateHtmlContentCommand;
import org.springframework.stereotype.Component;

@Component
public class UpdateHtmlContentCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        UpdateHtmlContentCommand updateHtmlContentCommand = (UpdateHtmlContentCommand) command;

        if (updateHtmlContentCommand.getId() <= 0) {
            return false;
        }

        if (updateHtmlContentCommand.getHtmlContent().isEmpty()) {
            return false;
        }

        return true;
    }
}
