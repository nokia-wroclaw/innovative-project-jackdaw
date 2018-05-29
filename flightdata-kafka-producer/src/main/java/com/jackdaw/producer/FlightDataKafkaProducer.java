package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FlightDataKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaProducer.class);

    private final String topicName;
    private final Producer<Long, Flight> producer;
    private final String inputFileName;
    private final FlightCreator flightCreator;
    private final DataValidator dataValidator;

    public FlightDataKafkaProducer(String inputFileName, String topicName, Producer<Long, Flight> producer) {
        this.producer = producer;
        this.inputFileName = inputFileName;
        this.topicName = topicName;
        this.dataValidator = new DataValidator();
        this.flightCreator = new FlightCreator();
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
                if (dataValidator.isDataValid(splitMessage) && dataValidator.flightHappened(splitMessage[5])) {
                    Optional<Flight> record = flightCreator.createFlight(splitMessage);
                    if (record.isPresent()) {
                        this.sendMessage(lineCount, record.get());
                    }
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }

    }

    void sendMessage(Long key, Flight value) {
        try {
            producer.send(new ProducerRecord<>(topicName, key, value)).get();
            LOG.info("Sent message: ({}, {})", key, value);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }


}
