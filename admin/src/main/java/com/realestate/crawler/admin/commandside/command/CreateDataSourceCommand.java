package com.realestate.crawler.admin.commandside.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateDataSourceCommand implements ICommand {
    private String name;
    private String url;
}
