package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class GeoJSONKafkaProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GeoJSONKafkaProducer.class);

    private final String destinationTopicName;
    private final Producer<Long, String> producer;
    private final JSONSerializer serializer;

    public GeoJSONKafkaProducer(Producer<Long, String> producer,
                                JSONSerializer serializer,
                                String destinationTopicName) {
        this.producer = producer;
        this.serializer = serializer;
        this.destinationTopicName = destinationTopicName;
    }

    public void sendMessage(Long key, Flight value) {
        try {
            String deserialized = serializer.getGeoJSON(value);

            producer.send(new ProducerRecord<>(destinationTopicName, key, deserialized)).get();
            LOG.info("Sent message: ({}, {})", key, deserialized);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }
}
