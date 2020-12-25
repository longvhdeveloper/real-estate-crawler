package com.realestate.crawler.property.commandside.handler;

import com.realestate.crawler.property.commandside.command.ICommand;

public interface ICommandHandler {

    boolean handler(ICommand command);
}
