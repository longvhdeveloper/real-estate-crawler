package com.realestate.crawler.downloader.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DownloadDetailUrlMessage {
    private String url;
}
