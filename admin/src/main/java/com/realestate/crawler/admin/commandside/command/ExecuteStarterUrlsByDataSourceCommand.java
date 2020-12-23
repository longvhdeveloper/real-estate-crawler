package com.realestate.crawler.admin.commandside.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ExecuteStarterUrlsByDataSourceCommand implements ICommand {
    private long id;
}
