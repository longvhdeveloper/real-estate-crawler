package com.realestate.crawler.property.commandside.validator;

import com.realestate.crawler.property.commandside.command.CreatePropertyCommand;
import com.realestate.crawler.property.commandside.command.ICommand;
import org.springframework.stereotype.Component;

@Component
public class CreatePropertyCommandValidator implements ICommandValidator {

    @Override
    public boolean isValid(ICommand command) {
        CreatePropertyCommand createPropertyCommand = (CreatePropertyCommand) command;

        if (createPropertyCommand.getUrl().isEmpty()) {
            return false;
        }

        if (createPropertyCommand.getName().isEmpty()) {
            return false;
        }

        return true;
    }
}
