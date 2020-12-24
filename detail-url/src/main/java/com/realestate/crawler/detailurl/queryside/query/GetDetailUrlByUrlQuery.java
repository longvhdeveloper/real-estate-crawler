package com.realestate.crawler.detailurl.queryside.query;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDetailUrlByUrlQuery {
    private String url;
}
