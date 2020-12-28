package com.realestate.crawler.property.commandside.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreatePropertyCommand implements ICommand {
    private String name;
    private String price;
    private String area;
    private String address;
    private String description;
    private String url;
}
