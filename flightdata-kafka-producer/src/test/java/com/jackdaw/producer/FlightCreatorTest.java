package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlightCreatorTest {

    private FlightCreator flightCreator;
    private String[] validRecord;

    @Before
    public void setUp() {
        flightCreator = new FlightCreator();
        validRecord = new String[]{"test", "test", "Internacional", "departureEstimate", "test", "Realizado",
                "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "0.0", "0.0", "0.0", "0.0"};
    }

    @Test
    public void flightObjectIsConstructedCorrectly() {
        //given
        Flight flight = new Flight("test", "test", FlightType.Internacional, TimeType.departureEstimate, "test",
                FlightSituation.Realizado, "test", "test", "test", "test", "test", "test", "test", "test", "test",
                0.0, 0.0, 0.0, 0.0);
        //when
        Optional<Flight> result = flightCreator.createFlight(validRecord);
        //then
        assertTrue(result.isPresent());
        assertEquals(flight, result.get());
    }
}
