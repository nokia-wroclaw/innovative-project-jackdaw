package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class FlightDataKafkaProducerTests {
    @Mock
    private MockProducer<Long, Flight> producer;
    private FlightDataKafkaProducer flightDataProducer;
    private String[] validRecord;
    private String topic = "";


    @Before
    public void setUp() throws IOException {
        producer = new MockProducer<>(true, new LongSerializer(), null);
        flightDataProducer = new FlightDataKafkaProducer("", topic, producer);
        validRecord = new String[]{"test", "test", "Internacional", "departureEstimate", "test", "Realizado",
                "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "0.0", "0.0", "0.0", "0.0"};
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThrowsExceptionWhenStringArrayIsEmpty() {
        //given
        String[] emptyArray = new String[0];
        // when
        flightDataProducer.createFlight(emptyArray);
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidFlightType() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[2] = "Invalid";
        //when
        boolean result = flightDataProducer.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidTimeType() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[3] = "Invalid";
        //when
        boolean result = flightDataProducer.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidFlightSituation() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[5] = "Invalid";
        //when
        boolean result = flightDataProducer.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void validRecordPassesDataValidation() {
        //when
        boolean result = flightDataProducer.isDataValid(validRecord);
        //then
        assertTrue(result);
    }

    @Test
    public void flightObjectIsConstructedCorrectly() {
        //given
        Flight flight = new Flight("test", "test", FlightType.Internacional, TimeType.departureEstimate, "test",
                FlightSituation.Realizado, "test", "test", "test", "test", "test", "test", "test", "test", "test",
                0.0, 0.0, 0.0, 0.0);
        //when
        Flight result = flightDataProducer.createFlight(validRecord);
        //then
        assertEquals(flight, result);
    }

    @Test
    public void consumerStopsWhenBufferedReaderReturnsNull() throws IOException {
        //given
        BufferedReader br = Mockito.mock(BufferedReader.class);
        when(br.readLine()).thenReturn(",,Internacional,departureEstimate,,Realizado,,,,,,,,,0.0,0.0,0.0,0.0")
                .thenReturn(null);
        //when
        flightDataProducer.runProducer(br);
        //expect
        verify(br, times(2)).readLine();
    }

    @Test
    public void validRecordIsSentCorrectly() {
        //given
        Flight flight = flightDataProducer.createFlight(validRecord);
        Long index = 1L;
        List<ProducerRecord<Long, Flight>> expected = Collections.singletonList(
                new ProducerRecord<>(topic, index, flight));
        //when
        flightDataProducer.sendMessage(index,flight);
        List<ProducerRecord<Long, Flight>> history = producer.history();
        //then
        assertEquals("Sent didn't match expected", expected, history);
    }

}
