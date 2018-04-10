package com.jackdaw.producer;

import com.jackdaw.avro.flights.Flight;
import com.jackdaw.avro.flights.FlightSituation;
import com.jackdaw.avro.flights.FlightType;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class FlightDataKafkaProducer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaProducer.class);

    private final String topicName;
    private final KafkaProducer<Long, Flight> producer;
    private final String fileName;

    public FlightDataKafkaProducer(String fileName, String topicName) throws IOException {
        InputStream input = new FileInputStream("/workdir/flightdata-kafka-producer.properties");
        Properties props = new Properties();
        props.load(input);
        producer = new KafkaProducer<>(props);
        this.fileName = fileName;
        this.topicName=topicName;
    }

    @Override
    public void run() {
        long lineCount = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                this.sendMessage(lineCount, createFlight(line.split(",")));
                Thread.sleep(1000);
            }

        } catch (InterruptedException | IOException e) {
            LOG.error("Failed to open file {}", fileName, e);
        }
    }

    private void sendMessage(Long key, Flight value) {
        try {
            producer.send(new ProducerRecord<>(topicName, key, value)).get();
            LOG.info("Sent message: ({}, {})", key, value);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }

    private Flight createFlight(String[] splitMessage) {
        if (splitMessage.length != 18) {
            throw new IllegalArgumentException("Array size different than 18");
        } else {
            Flight record = new Flight();
            int index = 0;
            for (String data : splitMessage) {
                if (index == 2) {
                    record.put(index, FlightType.valueOf(data));
                } else if (index == 7) {
                    record.put(index, FlightSituation.valueOf(data));
                } else if (index >= 15) {
                    record.put(index, Double.parseDouble(data));
                } else {
                    record.put(index, data);
                }
                ++index;
            }
            return record;
        }
    }


}
