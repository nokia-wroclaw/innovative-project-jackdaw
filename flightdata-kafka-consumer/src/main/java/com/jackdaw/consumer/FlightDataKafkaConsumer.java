package com.jackdaw.consumer;

import com.jackdaw.avro.flights.Flight;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class FlightDataKafkaConsumer
{
    private static final Logger LOG = LoggerFactory.getLogger(FlightDataKafkaConsumer.class);

    private final Consumer<Long, Flight> consumer;
    private final GeoJSONKafkaProducer geojsonProducer;

    public FlightDataKafkaConsumer(Consumer <Long, Flight> consumer,
                                   GeoJSONKafkaProducer geojsonProducer)
    {
        this.consumer = consumer;
        this.geojsonProducer = geojsonProducer;
    }

    public void run()
    {
        while (true)
        {
            ConsumerRecords<Long, Flight> records = consumer.poll(1000);

            send(records);
        }
    }

    void send(ConsumerRecords<Long, Flight> records)
    {
        for (ConsumerRecord<Long, Flight> record : records)
        {
            Long key = record.key();
            Flight flight = record.value();

            geojsonProducer.sendMessage(key, flight);

            consumer.commitSync();
        }
    }
}
