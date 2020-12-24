package com.realestate.crawler.extractor.producer;


import com.realestate.crawler.extractor.message.AbstractMessage;

public interface IProducer {
    void send(String message);

    void send(String topic, AbstractMessage message);
}
