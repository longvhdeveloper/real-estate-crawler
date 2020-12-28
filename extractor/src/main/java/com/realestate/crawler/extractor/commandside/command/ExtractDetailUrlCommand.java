package com.realestate.crawler.extractor.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExtractDetailUrlCommand implements ICommand {
    private long id;
}
