package com.jackdaw.producer;

import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.commons.lang3.EnumUtils;

class DataValidator {

    boolean flightHappened(String flightSituation) {
        return flightSituation.equals(FlightSituation.Realizado.toString());
    }

    boolean isDataValid(String[] splitMessage) {
        final int flightTypeStringPosition = 2;
        final int timeTypeStringPosition = 3;
        final int flightSituationStringPosition = 5;
        return isFlightTypeValid(splitMessage[flightTypeStringPosition]) &&
                isTimeTypeValid(splitMessage[timeTypeStringPosition]) &&
                isFlightSituationValid(splitMessage[flightSituationStringPosition]);
    }

    private boolean isFlightSituationValid(String situation) {
        return EnumUtils.isValidEnum(FlightSituation.class, situation);
    }

    private boolean isFlightTypeValid(String flightType) {
        return EnumUtils.isValidEnum(FlightType.class, flightType);
    }

    private boolean isTimeTypeValid(String timeType) {
        return EnumUtils.isValidEnum(TimeType.class, timeType);
    }
}
