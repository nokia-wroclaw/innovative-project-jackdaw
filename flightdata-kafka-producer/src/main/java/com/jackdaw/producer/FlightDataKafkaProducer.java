package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import com.jackdaw.avro.flights.TimeType;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class FlightDataKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaProducer.class);

    private final String topicName;
    private final Producer<Long, Flight> producer;
    private final String inputFileName;
    private static final int flightTypeStringPosition = 2;
    private static final int timeTypeStringPosition = 3;
    private static final int flightSituationStringPosition = 5;

    public FlightDataKafkaProducer(String inputFileName, String topicName, Producer<Long, Flight> producer) {
        this.producer = producer;
        this.inputFileName = inputFileName;
        this.topicName = topicName;
    }

    public void runProducer() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/volume/" + inputFileName)))) {
            runProducer(br);
        } catch (IOException e) {
            LOG.error("Failed to open file {}", inputFileName, e);
        }
    }

    void runProducer(BufferedReader br) {
        long lineCount = 0;
        try {
            //Omit first line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] splitMessage = line.split(",");
                if (isDataValid(splitMessage) && flightHappened(splitMessage[5])) {
                    this.sendMessage(lineCount, createFlight(splitMessage));
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }

    }

    boolean isDataValid(String[] splitMessage) {
        return isFlightTypeValid(splitMessage[flightTypeStringPosition]) &&
                isTimeTypeValid(splitMessage[timeTypeStringPosition]) &&
                isFlightSituationValid(splitMessage[flightSituationStringPosition]);
    }

    void sendMessage(Long key, Flight value) {
        try {
            producer.send(new ProducerRecord<>(topicName, key, value)).get();
            LOG.info("Sent message: ({}, {})", key, value);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }

    Flight createFlight(String[] splitMessage) {
        final int expectedArraySize = 19;
        final int coordinatesStartIndex = 15;
        if (splitMessage.length != expectedArraySize) {
            throw new IllegalArgumentException("Array size different than 19, data is corrupted");
        } else {
            Flight record = new Flight();
            int index = 0;
            for (String data : splitMessage) {
                if (index == flightTypeStringPosition) {
                    record.put(index, FlightType.valueOf(data));
                } else if (index == timeTypeStringPosition) {
                    record.put(index, TimeType.valueOf(data));
                } else if (index == flightSituationStringPosition) {
                    record.put(index, FlightSituation.valueOf(data));
                } else if (index >= coordinatesStartIndex) {
                    record.put(index, Double.parseDouble(data));
                } else {
                    record.put(index, data);
                }
                ++index;
            }
            return record;
        }
    }

    private boolean isFlightSituationValid(String situation) {
        ArrayList<String> validFlightSituationStrings = new ArrayList<>();
        for(FlightSituation x : FlightSituation.values())
            validFlightSituationStrings.add(x.toString());
        return validFlightSituationStrings.contains(situation);
    }

    private boolean isFlightTypeValid(String flightType) {
        ArrayList<String> validFlightSituationStrings = new ArrayList<>();
        for(FlightSituation x : FlightSituation.values())
            validFlightSituationStrings.add(x.toString());
        return validFlightSituationStrings.contains(flightType);
    }

    private boolean isTimeTypeValid(String timeType) {
        String[] validTimeTypeStrings = {"departureEstimate", "departureReal", "arrivalEstimate", "arrivalReal"};
        return Arrays.asList(validTimeTypeStrings).contains(timeType);
    }

    private boolean flightHappened(String flightSituation) {
        return flightSituation.equals("Realizado");
    }

}
