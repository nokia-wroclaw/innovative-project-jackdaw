package com.jackdaw;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.producer.FlightDataKafkaProducer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Runner {
    private static final MediaType SCHEMA_CONTENT =
            MediaType.parse("application/vnd.schemaregistry.v1+json");

    public static void main(String[] args) throws IOException {

        Properties topicProperties = new Properties();
        Properties producerProperties = new Properties();
        final OkHttpClient client = new OkHttpClient();
        final Schema schema = Flight.getClassSchema();

        try (FileInputStream fileInputStream =
                     new FileInputStream("/volume/topic.properties")) {
            topicProperties.load(fileInputStream);
        }

        try (FileInputStream fileInputStream =
                     new FileInputStream("/volume/flightdata-kafka-producer.properties")) {
            producerProperties.load(fileInputStream);
        }

        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schema.toString()))
                .url(producerProperties.getProperty("schema.registry.url") + "/subjects/Flight/versions")
                .build();

        client.newCall(request).execute();

        FlightDataKafkaProducer producer = new FlightDataKafkaProducer(topicProperties.get("fileName").toString(),
                topicProperties.get("topicName").toString(),
                new KafkaProducer<>(producerProperties));
        producer.runProducer();
    }
}
