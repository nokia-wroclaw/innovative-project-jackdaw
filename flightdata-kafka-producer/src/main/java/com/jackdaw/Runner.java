package com.jackdaw;

import com.jackdaw.producer.FlightDataKafkaProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Runner {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("/workdir/topic.properties"));
        FlightDataKafkaProducer producer = new FlightDataKafkaProducer(props.get("fileName").toString(),props.get("topicName").toString());
        Thread thread = new Thread(producer);
        thread.start();
    }
}
