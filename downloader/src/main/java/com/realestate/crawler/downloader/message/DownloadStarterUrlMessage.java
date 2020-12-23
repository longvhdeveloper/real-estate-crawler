package com.realestate.crawler.downloader.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DownloadStarterUrlMessage extends AbstractMessage {
    private long datasourceId;
    private String starterUrl;
}
