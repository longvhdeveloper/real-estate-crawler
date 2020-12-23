package com.realestate.crawler.downloader.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DownloadStarterUrlCommand implements ICommand {
    private long dataSourceId;
    private String url;
}
