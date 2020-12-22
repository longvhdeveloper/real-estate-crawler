package com.realestate.crawler.starterurl.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateHtmlContentCommand implements ICommand {
    private long id;
    private String htmlContent;
}
