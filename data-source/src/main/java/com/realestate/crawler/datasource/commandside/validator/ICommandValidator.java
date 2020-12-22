package com.realestate.crawler.datasource.commandside.validator;

import com.realestate.crawler.datasource.commandside.command.ICommand;

public interface ICommandValidator {
    boolean isValid(ICommand command);
}
