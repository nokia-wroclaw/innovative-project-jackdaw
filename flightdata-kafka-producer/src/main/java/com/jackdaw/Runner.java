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
    private final static MediaType SCHEMA_CONTENT =
            MediaType.parse("application/vnd.schemaregistry.v1+json");

    public static void main(String[] args) throws IOException {

        Properties topicProperties = new Properties();
        Properties schemaProps = new Properties();
        final OkHttpClient client = new OkHttpClient();
        final Schema schema = Flight.getClassSchema();

        topicProperties.load(new FileInputStream("/volume/topic.properties"));
        schemaProps.load(new FileInputStream("/volume/flightdata-kafka-producer.properties"));

        Request request = new Request.Builder()
                .post(RequestBody.create(SCHEMA_CONTENT, schema.toString()))
                .url(schemaProps.getProperty("schema.registry.url") + "/subjects/Flight/versions")
                .build();

        client.newCall(request).execute();
        InputStream input = new FileInputStream("/volume/flightdata-kafka-producer.properties");
        Properties props = new Properties();
        props.load(input);

        FlightDataKafkaProducer producer = new FlightDataKafkaProducer(topicProperties.get("fileName").toString(),
                topicProperties.get("topicName").toString(),
                new KafkaProducer<>(props));
        producer.runProducer();
    }
}
