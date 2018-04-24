package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class FlightDataKafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaConsumer.class);

    private final String topicName;
    private final KafkaConsumer<Long, Flight> consumer;
    private final String fileName;
    private final JSONSerializer serializer = new JSONSerializer();

    public FlightDataKafkaConsumer(String topicName, String fileName) throws IOException {
        InputStream input = new FileInputStream("/volume/flightdata-kafka-consumer.properties");
        Properties props = new Properties();
        props.load(input);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        this.consumer = new KafkaConsumer<>(props);
        this.topicName = topicName;
        this.fileName = fileName;
    }

    public void run() {
        consumer.subscribe(Collections.singletonList(topicName));

        while(true) {
            ConsumerRecords<Long, Flight> records = consumer.poll(1000);

            for (ConsumerRecord<Long, Flight> record : records) {
                Long key =  record.key();
                Flight flight = record.value();

                serializer.write(fileName, flight);

                LOG.info("Recived message: ({}, {})", key, flight);

                consumer.commitSync();
            }
        }
    }

}
