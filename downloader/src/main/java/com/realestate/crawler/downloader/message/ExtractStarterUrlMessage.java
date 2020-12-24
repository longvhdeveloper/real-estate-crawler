package com.realestate.crawler.downloader.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractStarterUrlMessage extends AbstractMessage{
    private long id;
}
