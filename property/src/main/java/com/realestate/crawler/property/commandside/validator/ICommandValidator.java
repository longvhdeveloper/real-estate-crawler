package com.realestate.crawler.property.commandside.validator;

import com.realestate.crawler.property.commandside.command.ICommand;

public interface ICommandValidator {

    boolean isValid(ICommand command);
}
