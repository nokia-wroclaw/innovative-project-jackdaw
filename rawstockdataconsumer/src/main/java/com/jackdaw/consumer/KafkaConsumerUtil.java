package com.jackdaw.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerUtil {

    //TODO check why it won't let me use kafka:9092
    private static final String BOOTSTRAP_SERVERS = "192.168.99.100:9092";

    private KafkaConsumerUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Consumer<Long, String> createConsumer(String topic) {

        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerUtil");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create the consumer using props.

        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }
}
