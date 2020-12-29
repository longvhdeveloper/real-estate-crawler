package com.realestate.crawler.downloader.commandside.handler;

import com.realestate.crawler.downloader.commandside.command.ICommand;

import java.io.IOException;

public interface ICommandHandler {

    boolean handle(ICommand command) throws IOException;
}
