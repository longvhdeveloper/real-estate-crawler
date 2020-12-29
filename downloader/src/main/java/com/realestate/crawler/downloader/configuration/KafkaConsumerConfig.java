package com.realestate.crawler.downloader.configuration;

import com.realestate.crawler.downloader.message.AbstractMessage;
import com.realestate.crawler.downloader.message.DownloadDetailUrlMessage;
import com.realestate.crawler.downloader.message.DownloadStarterUrlMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.topic.downloadStarterDLT}")
    private String downloadStarterDLT;

    public ConsumerFactory<String, DownloadStarterUrlMessage> downloadStarterUrlConsumerFactory() {

        JsonDeserializer<DownloadStarterUrlMessage> deserializer = new JsonDeserializer<>(DownloadStarterUrlMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDownloadStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DownloadStarterUrlMessage> downloadStarterUrlKafkaListenerContainerFactory(
            KafkaTemplate<String, AbstractMessage> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, DownloadStarterUrlMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(downloadStarterUrlConsumerFactory());

//        factory.setRetryTemplate(kafkaRetry());
//        factory.setRecoveryCallback(retryContext -> {
//            ConsumerRecord consumerRecord = (ConsumerRecord) retryContext.getAttribute("record");
//            log.info("Recovery is called for message {} ", consumerRecord.value());
//            kafkaTemplate.send(downloadStarterDLT, (DownloadStarterUrlMessage) consumerRecord.value());
//            return Optional.empty();
//        });

        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(5 * 1000L, 1)));

        return factory;
    }

    public ConsumerFactory<String, DownloadDetailUrlMessage> downloadDetailUrlConsumerFactory() {

        JsonDeserializer<DownloadDetailUrlMessage> deserializer = new JsonDeserializer<>(DownloadDetailUrlMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupDownloadStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DownloadDetailUrlMessage> downloadDetailUrlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DownloadDetailUrlMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(downloadDetailUrlConsumerFactory());
        return factory;
    }

    public RetryTemplate kafkaRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(10 * 1000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
