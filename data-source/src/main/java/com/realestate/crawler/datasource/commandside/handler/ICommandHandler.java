package com.realestate.crawler.datasource.commandside.handler;

import com.realestate.crawler.datasource.commandside.command.ICommand;

public interface ICommandHandler {
    boolean handler(ICommand command);
}
