package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class FlightDataKafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaConsumer.class);

    private final String sourceTopicName;
    private final String destinationTopicName;
    private final KafkaConsumer<Long, Flight> consumer;
    private final KafkaProducer<Long, String> producer;
    private final JSONSerializer serializer = new JSONSerializer();

    public FlightDataKafkaConsumer(String sourceTopicName, String destinationTopicName) throws IOException {
        InputStream input = new FileInputStream("/volume/flightdata-kafka-consumer.properties");
        Properties props = new Properties();
        props.load(input);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        this.consumer = new KafkaConsumer<>(props);
        this.producer = new KafkaProducer<Long, String>(props);
        this.sourceTopicName = sourceTopicName;
        this.destinationTopicName = destinationTopicName;
    }

    public void run() {
        consumer.subscribe(Collections.singletonList(sourceTopicName));

        while(true) {
            ConsumerRecords<Long, Flight> records = consumer.poll(1000);

            for (ConsumerRecord<Long, Flight> record : records) {
                Long key =  record.key();
                Flight flight = record.value();

                sendMessage(key, serializer.getGeoJSON(flight));

                consumer.commitSync();
            }
        }
    }

    private void sendMessage(Long key, String value) {
        try {
            producer.send(new ProducerRecord<>(destinationTopicName, key, value)).get();
            LOG.info("Sent message: ({}, {})", key, value);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }

}
