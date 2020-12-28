package com.realestate.crawler.detailurl.commandside.validator;


import com.realestate.crawler.detailurl.commandside.command.ICommand;

public interface ICommandValidator {

    boolean isValid(ICommand command);
}
