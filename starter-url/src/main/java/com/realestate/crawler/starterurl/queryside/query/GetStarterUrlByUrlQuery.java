package com.realestate.crawler.starterurl.queryside.query;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetStarterUrlByUrlQuery {
    private long dataSourceId;
    private String url;
}
