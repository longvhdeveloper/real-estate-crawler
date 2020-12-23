package com.realestate.crawler.admin.commandside.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CreateStarterUrlCommand implements ICommand {
    private long dataSourceId;
    private List<String> urls;
}
