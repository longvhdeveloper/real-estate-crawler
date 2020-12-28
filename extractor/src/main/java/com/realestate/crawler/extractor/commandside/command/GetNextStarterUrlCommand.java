package com.realestate.crawler.extractor.commandside.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetNextStarterUrlCommand implements ICommand {
    private long id;
}
