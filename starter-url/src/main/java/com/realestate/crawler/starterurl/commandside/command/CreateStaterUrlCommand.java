package com.realestate.crawler.starterurl.commandside.command;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateStaterUrlCommand implements ICommand {
    private List<String> urls;
    private long dataSourceId;
}
