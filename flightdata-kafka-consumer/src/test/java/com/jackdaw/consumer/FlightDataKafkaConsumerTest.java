package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class FlightDataKafkaConsumerTest {
    private MockConsumer<Long, Flight> mockConsumer;
    private GeoJSONKafkaProducer mockGeojsonProducer;

    @Before
    public void setUp() {
        mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        mockGeojsonProducer = Mockito.mock(GeoJSONKafkaProducer.class);
    }

    @Test
    public void send() {
        final int partition = 0;
        final Long offset = 0L;

        final Long expectedKey = 0L;
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

        mockConsumer.assign(Collections.singletonList(new TopicPartition("Test", 0)));
        HashMap<TopicPartition, Long> beginningOffsets = new HashMap<>();
        beginningOffsets.put(new TopicPartition("Test", partition), offset);
        mockConsumer.updateBeginningOffsets(beginningOffsets);
        mockConsumer.addRecord(new ConsumerRecord<>("Test", partition, offset, expectedKey, flight));

        ConsumerRecords<Long, Flight> records = mockConsumer.poll(1000);

        FlightDataKafkaConsumer kafkaConsumer = new FlightDataKafkaConsumer(mockConsumer, mockGeojsonProducer);
        kafkaConsumer.send(records);

        verify(mockGeojsonProducer).sendMessage(expectedKey, flight);
        verify(mockGeojsonProducer, times(1)).sendMessage(expectedKey, flight);
        verifyNoMoreInteractions(mockGeojsonProducer);
    }
}