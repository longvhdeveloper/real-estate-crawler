package com.realestate.crawler.extractor.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtractStarterUrlMessage extends AbstractMessage {
    private long id;
}
