package com.realestate.crawler.extractor.commandside.handler;


import com.realestate.crawler.extractor.commandside.command.ICommand;

public interface ICommandHandler {

    boolean handle(ICommand command);
}
