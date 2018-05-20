package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlightDataKafkaConsumerTest {
    private MockConsumer<Long, Flight> consumer;
    private MockProducer<Long, String> producer;

    @Before
    public void setUp() {
        consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        producer = new MockProducer<>(true, new LongSerializer(), new StringSerializer());
    }

    @Test
    public void send() {

    }
}