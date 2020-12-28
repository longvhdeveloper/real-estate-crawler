package com.realestate.crawler.downloader.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DownloadDetailUrlCommand implements ICommand {
    private String url;
}
