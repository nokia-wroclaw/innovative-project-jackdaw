package com.jackdaw;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.consumer.FlightDataKafkaConsumer;
import okhttp3.*;
import org.apache.avro.Schema;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Runner {
    private final static MediaType SCHEMA_CONTENT =
            MediaType.parse("application/vnd.schemaregistry.v1+json");

    public static void main(String[] args) throws IOException {

        Properties topicProperties = new Properties();
        Properties schemaProps = new Properties();
        final OkHttpClient client = new OkHttpClient();
        final Schema schema = Flight.getClassSchema();

        topicProperties.load(new FileInputStream("/volume/topic.properties"));
        schemaProps.load(new FileInputStream("/volume/flightdata-kafka-consumer.properties"));

        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schema.toString()))
                .url(schemaProps.getProperty("schema.registry.url") +"/subjects/Flight/versions")
                .build();

        client.newCall(request).execute();

        FlightDataKafkaConsumer consumer = new FlightDataKafkaConsumer(topicProperties.get("topicName").toString(),
                                                                       topicProperties.get("fileName").toString());
        consumer.run();
    }
}
