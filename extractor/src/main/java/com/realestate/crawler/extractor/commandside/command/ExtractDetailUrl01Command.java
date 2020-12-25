package com.realestate.crawler.extractor.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExtractDetailUrl01Command implements ICommand {
    private long id;
}
