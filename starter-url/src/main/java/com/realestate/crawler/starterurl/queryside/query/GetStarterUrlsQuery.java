package com.realestate.crawler.starterurl.queryside.query;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetStarterUrlsQuery {
    private long dataSourceId;
    private int status;
}
