package com.realestate.crawler.detailurl.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDetailUrlCommand implements ICommand {
    private long dataSourceId;
    private String url;
}
