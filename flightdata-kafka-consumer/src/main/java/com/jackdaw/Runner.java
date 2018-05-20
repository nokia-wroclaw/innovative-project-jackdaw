package com.jackdaw;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.consumer.FlightDataKafkaConsumer;
import com.jackdaw.consumer.GeoJSONKafkaProducer;
import com.jackdaw.consumer.JSONSerializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class Runner {
    public static void main(String[] args) throws IOException {
        Properties topicProperties = new Properties();
        Properties schemaProps = new Properties();

        topicProperties.load(new FileInputStream("/volume/topic.properties"));
        schemaProps.load(new FileInputStream("/volume/flightdata-kafka-consumer.properties"));
        schemaProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        KafkaConsumer<Long, Flight> kafkaConsumer = new KafkaConsumer<>(schemaProps);
        kafkaConsumer.subscribe(Collections.singletonList(topicProperties.get("sourceTopicName").toString()));

        KafkaProducer<Long, String> kafkaProducer = new KafkaProducer<>(schemaProps);
        GeoJSONKafkaProducer geojsonProducer = new GeoJSONKafkaProducer(kafkaProducer,
                new JSONSerializer(),
                topicProperties.get("destinationTopicName").toString());

        FlightDataKafkaConsumer consumer = new FlightDataKafkaConsumer(kafkaConsumer, geojsonProducer);
        consumer.run();
    }
}
