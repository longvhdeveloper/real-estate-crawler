package com.realestate.crawler.extractor.message;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DownloadDetailUrlMessage extends AbstractMessage {
    private String url;
}
