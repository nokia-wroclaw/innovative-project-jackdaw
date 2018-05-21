package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONSerializerTest {
    private JSONSerializer serializer;

    @Before
    public void setUp() {
        serializer = new JSONSerializer();
    }

    @Test
    public void getGeoJSON() {
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

        assertEquals(serializer.getGeoJSON(flight), expectedValue);
    }
}