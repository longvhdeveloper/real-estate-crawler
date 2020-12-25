package com.realestate.crawler.extractor.configuration;

import com.realestate.crawler.extractor.message.ExtractDetailUrlMessage;
import com.realestate.crawler.extractor.message.ExtractStarterUrlMessage;
import com.realestate.crawler.extractor.message.NextStarterUrlMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    public ConsumerFactory<String, ExtractStarterUrlMessage> extractStarterUrlConsumerFactory() {

        JsonDeserializer<ExtractStarterUrlMessage> deserializer = new JsonDeserializer<>(ExtractStarterUrlMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupExtractStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ExtractStarterUrlMessage> extractStarterUrlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ExtractStarterUrlMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(extractStarterUrlConsumerFactory());
        return factory;
    }

    public ConsumerFactory<String, ExtractDetailUrlMessage> extractDetailUrlConsumerFactory() {

        JsonDeserializer<ExtractDetailUrlMessage> deserializer = new JsonDeserializer<>(ExtractDetailUrlMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupExtractStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ExtractDetailUrlMessage> extractDetailUrlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ExtractDetailUrlMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(extractDetailUrlConsumerFactory());
        return factory;
    }

    public ConsumerFactory<String, NextStarterUrlMessage> nextStarterUrlConsumerFactory() {

        JsonDeserializer<NextStarterUrlMessage> deserializer = new JsonDeserializer<>(NextStarterUrlMessage.class);


        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupExtractStarter");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NextStarterUrlMessage> nextStarterUrlKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NextStarterUrlMessage> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(nextStarterUrlConsumerFactory());
        return factory;
    }
}
