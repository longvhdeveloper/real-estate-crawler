package com.realestate.crawler.downloader.producer;

import com.realestate.crawler.downloader.message.AbstractMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class ProducerImpl implements IProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaTemplate<String, AbstractMessage> messageKafkaTemplate;

    @Autowired
    public ProducerImpl(KafkaTemplate<String, String> kafkaTemplate,
                        KafkaTemplate<String, AbstractMessage> messageKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageKafkaTemplate = messageKafkaTemplate;
    }

    @Override
    public void send(String message) {
        //
    }

    @Override
    public void send(String topic, AbstractMessage message) {
        ListenableFuture<SendResult<String, AbstractMessage>> future = messageKafkaTemplate.send(topic,
                message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message.toString() + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, AbstractMessage> result) {
                System.out.println("Sent message=[" + message.toString() + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "] to topic=[" + topic + "]");
            }
        });
    }
}
