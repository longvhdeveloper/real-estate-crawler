package com.realestate.crawler.extractor.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NextStarterUrlMessage extends AbstractMessage {
    private long id;
}
