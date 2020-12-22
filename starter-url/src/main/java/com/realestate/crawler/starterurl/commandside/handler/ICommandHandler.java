package com.realestate.crawler.starterurl.commandside.handler;

import com.realestate.crawler.starterurl.commandside.command.ICommand;

public interface ICommandHandler {

    boolean handler(ICommand command);
}
