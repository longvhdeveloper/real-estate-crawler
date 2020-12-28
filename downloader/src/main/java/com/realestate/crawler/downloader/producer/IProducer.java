package com.realestate.crawler.downloader.producer;


import com.realestate.crawler.downloader.message.AbstractMessage;

public interface IProducer {
    void send(String message);

    void send(String topic, AbstractMessage message);
}
