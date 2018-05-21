package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GeoJSONKafkaProducerTest {
    private MockProducer<Long, String> mockProducer;
    private JSONSerializer mockSerializer;

    @Before
    public void setUp() {
        mockProducer = new MockProducer<>(true, new LongSerializer(), new StringSerializer());
        mockSerializer = Mockito.mock(JSONSerializer.class);
    }

    @Test
    public void sendMessage() {
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

        when(mockSerializer.getGeoJSON(flight)).thenReturn(expectedValue);
        GeoJSONKafkaProducer geojsonProducer = new GeoJSONKafkaProducer(mockProducer, mockSerializer, "Test");

        geojsonProducer.sendMessage(expectedKey, flight);

        verify(mockSerializer).getGeoJSON(flight);
        verify(mockSerializer, times(1)).getGeoJSON(flight);
        verifyNoMoreInteractions(mockSerializer);

        List<ProducerRecord<Long, String>> history = mockProducer.history();
        List<ProducerRecord<Long, String>> expected = Collections.singletonList(
                new ProducerRecord<>("Test", expectedKey, expectedValue));
        assertEquals("Sent didn't match expected", expected, history);
    }
}