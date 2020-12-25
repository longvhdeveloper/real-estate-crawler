package com.realestate.crawler.extractor.message;

import lombok.Data;

@Data
public class DownloadStarterUrlMessage extends AbstractMessage {
    private long datasourceId;
    private String starterUrl;

    public DownloadStarterUrlMessage(long datasourceId, String starterUrl) {
        super();
        this.datasourceId = datasourceId;
        this.starterUrl = starterUrl;
    }
}
