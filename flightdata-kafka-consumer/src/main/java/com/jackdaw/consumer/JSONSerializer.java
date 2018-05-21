package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.TimeType;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONSerializer {

    public String getGeoJSON(Flight flight) {
        JSONObject featureCollection = new JSONObject();
        JSONArray featureList = new JSONArray();
        JSONObject feature = new JSONObject();
        JSONObject geometry = getCorrectGeometry(flight);
        JSONObject properties = getProperties(flight);

        featureCollection.put(TYPE, FEATURE_COLLECTION);
        feature.put(TYPE, FEATURE);
        feature.put(GEOMETRY, geometry);
        feature.put(PROPERTIES, properties);
        featureList.put(feature);
        featureCollection.put(FEATURES, featureList);

        return featureCollection.toString(4);
    }

    private JSONObject getCorrectGeometry(Flight flight) {
        if (flight.getTimeType() == TimeType.arrivalEstimate) {
            return getLineString(flight);
        }
        return getPoint(flight);
    }

    private JSONObject getPoint(Flight flight) {
        JSONObject point = new JSONObject();
        JSONArray coordinates = new JSONArray();

        if (flight.getTimeType() == TimeType.arrivalReal) {
            coordinates.put(flight.getDestinationLatitude());
            coordinates.put(flight.getDestinationLongitude());
        } else {
            coordinates.put(flight.getOriginAltitude());
            coordinates.put(flight.getOriginLongitude());
        }

        point.put(TYPE, POINT);
        point.put(COORDINATES, coordinates);

        return point;
    }

    private JSONObject getLineString(Flight flight) {
        JSONObject lineString = new JSONObject();
        JSONArray coordinatesArray = new JSONArray();
        JSONArray coordinatesOrigin = new JSONArray();
        JSONArray coordinatesDest = new JSONArray();

        coordinatesOrigin.put(flight.getOriginAltitude());
        coordinatesOrigin.put(flight.getOriginLongitude());
        coordinatesDest.put(flight.getDestinationLatitude());
        coordinatesDest.put(flight.getDestinationLongitude());
        coordinatesArray.put(coordinatesOrigin);
        coordinatesArray.put(coordinatesDest);

        lineString.put(TYPE, LINE_STRING);
        lineString.put(COORDINATES, coordinatesArray);

        return lineString;
    }

    private String getColor(Flight flight) {
        switch (flight.getTimeType()) {
            case departureEstimate: {
                return "#7a7579";
            }
            case departureReal: {
                return "#11bb11";
            }
            case arrivalEstimate: {
                return "#ef9115";
            }
            case arrivalReal: {
                return "#cc350c";
            }
            default:
                return null;
        }
    }

    private String getTimeType(Flight flight) {
        switch (flight.getTimeType()) {
            case departureEstimate: {
                return "Expected departure";
            }
            case departureReal: {
                return "Real departure";
            }
            case arrivalEstimate: {
                return "Expected arrival";
            }
            case arrivalReal: {
                return "Real arrival";
            }
            default:
                return null;
        }
    }

    private JSONObject getProperties(Flight flight) {
        JSONObject properties = new JSONObject();

        properties.put(COLOR, getColor(flight));
        properties.put(FLIGHTS, flight.getFlightSymbol());
        properties.put(COMPANY_AERIAL, flight.getAirline());
        properties.put(CODE_TYPE_LINE, flight.getFlightType().name());
        properties.put(TIME_TYPE, getTimeType(flight));
        properties.put(TIME, flight.getTime());
        properties.put(STATE_FLIGHT, flight.getFlightSituation().name());
        properties.put(CODE_JUSTIFICATION, flight.getCodeJustification());
        properties.put(AIRPORT_ORIGIN, flight.getOriginArport());
        properties.put(CITY_ORIGIN, flight.getOriginCity());
        properties.put(STATE_ORIGIN, flight.getOriginState());
        properties.put(COUNTRY_ORIGIN, flight.getOriginCountry());
        properties.put(AIRPORT_DESTINATION, flight.getDestinationAirport());
        properties.put(CITY_DESTINATION, flight.getDestinationCity());
        properties.put(STATE_DESTINATION, flight.getDestinationState());
        properties.put(COUNTRY_DESTINATION, flight.getDestinationCountry());

        return properties;
    }

    private static final String TYPE = "type";
    private static final String FEATURE_COLLECTION = "FeatureCollection";
    private static final String FEATURES = "features";
    private static final String FEATURE = "Feature";
    private static final String GEOMETRY = "geometry";
    private static final String POINT = "Point";
    private static final String LINE_STRING = "LineString";
    private static final String COORDINATES = "coordinates";
    private static final String PROPERTIES = "properties";
    private static final String COLOR = "color";
    private static final String FLIGHTS = "flights";
    private static final String COMPANY_AERIAL = "companyAerial";
    private static final String CODE_TYPE_LINE = "codeTypeLine";
    private static final String TIME_TYPE = "timeType";
    private static final String TIME = "time";
    private static final String STATE_FLIGHT = "stateFlight";
    private static final String CODE_JUSTIFICATION = "codeJustification";
    private static final String AIRPORT_ORIGIN = "airportOrigin";
    private static final String CITY_ORIGIN = "cityOrigin";
    private static final String STATE_ORIGIN = "stateOrigin";
    private static final String COUNTRY_ORIGIN = "countryOrigin";
    private static final String AIRPORT_DESTINATION = "airportDestination";
    private static final String CITY_DESTINATION = "cityDestination";
    private static final String STATE_DESTINATION = "stateDestination";
    private static final String COUNTRY_DESTINATION = "countryDestination";
}
