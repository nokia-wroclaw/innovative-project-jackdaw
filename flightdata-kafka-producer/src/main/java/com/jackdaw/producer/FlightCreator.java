package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

class FlightCreator {
    private static final int FLIGHT_SYMBOL_INDEX = 0;
    private static final int AIRLINE_INDEX = 1;
    private static final int FLIGHT_TYPE_INDEX = 2;
    private static final int TIME_TYPE_INDEX = 3;
    private static final int TIME_INDEX = 4;
    private static final int FLIGHT_SITUATION_INDEX = 5;
    private static final int CODE_JUSTIFICATION_INDEX = 6;
    private static final int ORIGIN_AIRPORT_INDEX = 7;
    private static final int ORIGIN_CITY_INDEX = 8;
    private static final int ORIGIN_STATE_INDEX = 9;
    private static final int ORIGIN_COUNTRY_INDEX = 10;
    private static final int DESTINATION_AIRPORT_INDEX = 11;
    private static final int DESTINATION_CITY_INDEX = 12;
    private static final int DESTINATION_STATE_INDEX = 13;
    private static final int DESTINATION_COUNTRY_INDEX = 14;
    private static final int DESTINATION_LONGITUDE_INDEX = 15;
    private static final int DESTINATION_LATITUDE_INDEX = 16;
    private static final int ORIGIN_LONGITUDE_INDEX = 17;
    private static final int ORIGIN_ALTITUDE_INDEX = 18;


    Optional<Flight> createFlight(String[] splitMessage) {
        Flight.Builder builder = Flight.newBuilder();
        final int expectedArraySize = 19;
        if (splitMessage.length != expectedArraySize) {
            return Optional.empty();
        } else {
            builder.setFlightSymbol(splitMessage[FLIGHT_SYMBOL_INDEX]);
            builder.setAirline(splitMessage[AIRLINE_INDEX]);
            builder.setFlightType(FlightType.valueOf(splitMessage[FLIGHT_TYPE_INDEX]));
            builder.setTimeType(TimeType.valueOf(splitMessage[TIME_TYPE_INDEX]));
            builder.setTime(splitMessage[TIME_INDEX]);
            builder.setFlightSituation(FlightSituation.valueOf(splitMessage[FLIGHT_SITUATION_INDEX]));
            builder.setCodeJustification(splitMessage[CODE_JUSTIFICATION_INDEX]);
            builder.setOriginAirport(splitMessage[ORIGIN_AIRPORT_INDEX]);
            builder.setOriginCity(splitMessage[ORIGIN_CITY_INDEX]);
            builder.setOriginState(splitMessage[ORIGIN_STATE_INDEX]);
            builder.setOriginCountry(splitMessage[ORIGIN_COUNTRY_INDEX]);
            builder.setDestinationAirport(splitMessage[DESTINATION_AIRPORT_INDEX]);
            builder.setDestinationCity(splitMessage[DESTINATION_CITY_INDEX]);
            builder.setDestinationState(splitMessage[DESTINATION_STATE_INDEX]);
            builder.setDestinationCountry(splitMessage[DESTINATION_COUNTRY_INDEX]);
            builder.setDestinationLongitude(NumberUtils.createDouble(splitMessage[DESTINATION_LONGITUDE_INDEX]));
            builder.setDestinationLatitude(NumberUtils.createDouble(splitMessage[DESTINATION_LATITUDE_INDEX]));
            builder.setOriginLongitude(NumberUtils.createDouble(splitMessage[ORIGIN_LONGITUDE_INDEX]));
            builder.setOriginAltitude(NumberUtils.createDouble(splitMessage[ORIGIN_ALTITUDE_INDEX]));
        }
        return Optional.of(builder.build());
    }
}

