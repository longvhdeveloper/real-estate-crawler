package com.realestate.crawler.extractor.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ExtractDetailUrlMessage extends AbstractMessage {
    private long id;
}
