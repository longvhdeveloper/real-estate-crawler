package com.realestate.crawler.extractor.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PropertyParseItem {
    private String name;
    private String price;
    private String area;
    private String description;
    private String address;
}
