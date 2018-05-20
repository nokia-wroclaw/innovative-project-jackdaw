package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.producer.MockProducer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Array;

public class FlightDataKafkaProducerTests {
    @Mock
    private MockProducer<Long, Flight> producer;
    private FlightDataKafkaProducer flightDataProducer;


    @Before
    public void setUp() throws IOException {
        producer = new MockProducer<>();
        flightDataProducer = new FlightDataKafkaProducer("", "", producer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThrowsExceptionWhenDataIsCorrupted() {
        //given
        String[] emptyArray = (String[]) Array.newInstance(String.class, 0);
        flightDataProducer.createFlight(emptyArray);
    }

}
