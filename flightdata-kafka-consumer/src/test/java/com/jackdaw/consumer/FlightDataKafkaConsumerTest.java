package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class FlightDataKafkaConsumerTest {
    private MockConsumer<Long, Flight> consumer;
    private MockProducer<Long, String> producer;
    private JSONSerializer serializer;

    @Before
    public void setUp() {
        consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        producer = new MockProducer<>(true, new LongSerializer(), new StringSerializer());
        serializer = new JSONSerializer();
    }

    @Test
    public void send() {
        final Flight flight = new Flight("test",
                "test",
                FlightType.Internacional,
                TimeType.departureEstimate,
                "test",
                FlightSituation.Realizado,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                0.0,
                0.0,
                0.0,
                0.0);

        final Long expectedKey = 0L;
        final String expectedValue = "{\n" +
                "    \"features\": [{\n" +
                "        \"geometry\": {\n" +
                "            \"coordinates\": [\n" +
                "                0,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"type\": \"Point\"\n" +
                "        },\n" +
                "        \"type\": \"Feature\",\n" +
                "        \"properties\": {\n" +
                "            \"stateOrigin\": \"test\",\n" +
                "            \"color\": \"#7a7579\",\n" +
                "            \"stateFlight\": \"Realizado\",\n" +
                "            \"companyAerial\": \"test\",\n" +
                "            \"codeTypeLine\": \"Internacional\",\n" +
                "            \"cityOrigin\": \"test\",\n" +
                "            \"timeType\": \"Expected departure\",\n" +
                "            \"codeJustification\": \"test\",\n" +
                "            \"cityDestination\": \"test\",\n" +
                "            \"airportOrigin\": \"test\",\n" +
                "            \"time\": \"test\",\n" +
                "            \"countryOrigin\": \"test\",\n" +
                "            \"airportDestination\": \"test\",\n" +
                "            \"countryDestination\": \"test\",\n" +
                "            \"flights\": \"test\",\n" +
                "            \"stateDestination\": \"test\"\n" +
                "        }\n" +
                "    }],\n" +
                "    \"type\": \"FeatureCollection\"\n" +
                "}";

        GeoJSONKafkaProducer geojsonProducer = new GeoJSONKafkaProducer(producer, serializer, "Test_Producer");

        consumer.assign(Collections.singletonList(new TopicPartition("Test", 0)));
        HashMap<TopicPartition, Long> beginningOffsets = new HashMap<>();
        beginningOffsets.put(new TopicPartition("Test", 0), 0L);
        consumer.updateBeginningOffsets(beginningOffsets);
        consumer.addRecord(new ConsumerRecord<>("Test", 0, 0L, 0L, flight));

        ConsumerRecords<Long, Flight> records = consumer.poll(1000);

        FlightDataKafkaConsumer cons = new FlightDataKafkaConsumer(consumer, geojsonProducer);
        cons.send(records);

        List<ProducerRecord<Long, String>> history = producer.history();
        List<ProducerRecord<Long, String>> expected = Collections.singletonList(
                new ProducerRecord<>("Test_Producer", expectedKey, expectedValue));

        Assert.assertEquals("Sent didn't match expected", expected, history);
    }
}