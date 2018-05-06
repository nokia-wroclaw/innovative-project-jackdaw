package com.jackdaw;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.consumer.FlightDataKafkaConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Runner {
    public static void main(String[] args) throws IOException {

        Properties topicProperties = new Properties();
        Properties schemaProps = new Properties();

        topicProperties.load(new FileInputStream("/volume/topic.properties"));
        schemaProps.load(new FileInputStream("/volume/flightdata-kafka-consumer.properties"));

        FlightDataKafkaConsumer consumer = new FlightDataKafkaConsumer(topicProperties.get("sourceTopicName").toString(),
                                                                       topicProperties.get("destinationTopicName").toString());
        consumer.run();
    }
}
