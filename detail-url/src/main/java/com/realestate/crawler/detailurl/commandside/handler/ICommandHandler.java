package com.realestate.crawler.detailurl.commandside.handler;


import com.realestate.crawler.detailurl.commandside.command.ICommand;

public interface ICommandHandler {
    boolean handler(ICommand command);
}
