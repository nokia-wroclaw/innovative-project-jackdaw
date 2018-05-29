package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

class FlightCreator {
    private static final int flightSymbolIndex = 0;
    private static final int airlineIndex = 1;
    private static final int flightTypeIndex = 2;
    private static final int timeTypeIndex = 3;
    private static final int timeIndex = 4;
    private static final int flightSituationIndex = 5;
    private static final int codeJustificationIndex = 6;
    private static final int originAirportIndex = 7;
    private static final int originCityIndex = 8;
    private static final int originStateIndex = 9;
    private static final int originCountryIndex = 10;
    private static final int destinationAirportIndex = 11;
    private static final int destinationCityIndex = 12;
    private static final int destinationStateIndex = 13;
    private static final int destinationCountryIndex = 14;
    private static final int destinationLongitudeIndex = 15;
    private static final int destinationLatitudeIndex = 16;
    private static final int originLongitudeIndex = 17;
    private static final int originAltitudeIndex = 18;


    Optional<Flight> createFlight(String[] splitMessage) {
        Flight.Builder builder = Flight.newBuilder();
        final int expectedArraySize = 19;
        if (splitMessage.length != expectedArraySize) {
            return Optional.empty();
        } else {
            builder.setFlightSymbol(splitMessage[flightSymbolIndex]);
            builder.setAirline(splitMessage[airlineIndex]);
            builder.setFlightType(FlightType.valueOf(splitMessage[flightTypeIndex]));
            builder.setTimeType(TimeType.valueOf(splitMessage[timeTypeIndex]));
            builder.setTime(splitMessage[timeIndex]);
            builder.setFlightSituation(FlightSituation.valueOf(splitMessage[flightSituationIndex]));
            builder.setCodeJustification(splitMessage[codeJustificationIndex]);
            builder.setOriginAirport(splitMessage[originAirportIndex]);
            builder.setOriginCity(splitMessage[originCityIndex]);
            builder.setOriginState(splitMessage[originStateIndex]);
            builder.setOriginCountry(splitMessage[originCountryIndex]);
            builder.setDestinationAirport(splitMessage[destinationAirportIndex]);
            builder.setDestinationCity(splitMessage[destinationCityIndex]);
            builder.setDestinationState(splitMessage[destinationStateIndex]);
            builder.setDestinationCountry(splitMessage[destinationCountryIndex]);
            builder.setDestinationLongitude(NumberUtils.createDouble(splitMessage[destinationLongitudeIndex]));
            builder.setDestinationLatitude(NumberUtils.createDouble(splitMessage[destinationLatitudeIndex]));
            builder.setOriginLongitude(NumberUtils.createDouble(splitMessage[originLongitudeIndex]));
            builder.setOriginAltitude(NumberUtils.createDouble(splitMessage[originAltitudeIndex]));
        }
        return Optional.of(builder.build());
    }
}

