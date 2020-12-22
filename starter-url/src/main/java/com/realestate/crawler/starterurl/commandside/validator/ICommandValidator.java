package com.realestate.crawler.starterurl.commandside.validator;

import com.realestate.crawler.starterurl.commandside.command.ICommand;

public interface ICommandValidator {

    boolean isValid(ICommand command);
}
