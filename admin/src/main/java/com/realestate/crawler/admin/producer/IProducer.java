package com.realestate.crawler.admin.producer;

import com.realestate.crawler.admin.message.AbstractMessage;

public interface IProducer {
    void send(String message);

    void send(String topic, AbstractMessage message);
}
