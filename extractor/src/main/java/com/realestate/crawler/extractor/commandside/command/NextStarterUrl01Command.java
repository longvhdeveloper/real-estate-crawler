package com.realestate.crawler.extractor.commandside.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NextStarterUrl01Command implements ICommand {
    private long id;
}
