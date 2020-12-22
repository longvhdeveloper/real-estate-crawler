package com.realestate.crawler.datasource.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDataSourceCommand implements ICommand {

    private String name;
    private String url;
}
