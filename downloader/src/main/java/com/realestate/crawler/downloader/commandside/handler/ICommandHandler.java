package com.realestate.crawler.downloader.commandside.handler;

import com.realestate.crawler.downloader.commandside.command.ICommand;

public interface ICommandHandler {

    boolean handler(ICommand command);
}
