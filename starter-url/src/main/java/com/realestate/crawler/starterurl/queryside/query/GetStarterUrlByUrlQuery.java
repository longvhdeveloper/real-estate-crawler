package com.realestate.crawler.starterurl.queryside.query;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetStarterUrlByCheckSumQuery {
    private long dataSourceId;
    private String checkSum;
}
