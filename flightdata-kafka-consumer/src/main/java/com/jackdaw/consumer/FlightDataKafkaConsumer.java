package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class FlightDataKafkaConsumer {
    private final Consumer<Long, Flight> consumer;
    private final GeoJSONKafkaProducer geojsonProducer;
    private boolean isRunning = true;

    public FlightDataKafkaConsumer(Consumer<Long, Flight> consumer,
                                   GeoJSONKafkaProducer geojsonProducer) {
        this.consumer = consumer;
        this.geojsonProducer = geojsonProducer;
    }

    public void run() {
        while (isRunning) {
            ConsumerRecords<Long, Flight> records = consumer.poll(1000);

            send(records);
        }
    }

    void send(ConsumerRecords<Long, Flight> records) {
        for (ConsumerRecord<Long, Flight> record : records) {
            Long key = record.key();
            Flight flight = record.value();

            geojsonProducer.sendMessage(key, flight);

            consumer.commitSync();
        }
    }
}
