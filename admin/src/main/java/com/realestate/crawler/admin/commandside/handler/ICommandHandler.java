package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.ICommand;

public interface ICommandHandler {

    boolean handler(ICommand command);
}
