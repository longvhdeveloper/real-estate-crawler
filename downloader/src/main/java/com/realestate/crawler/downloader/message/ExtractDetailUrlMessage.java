package com.realestate.crawler.downloader.message;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExtractDetailUrlMessage extends AbstractMessage {
    private long id;
}
