package com.jackdaw.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    private static final String TOPIC_NAME = "test";
    private static final String FILE_NAME = "src/main/resources/a.us.txt";
    private static final String HOST_NAME = "kafka:9092";

    private final org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;
    private final Boolean isAsync;

    public KafkaProducer(Boolean isAsync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", HOST_NAME);
        props.put("client.id", "DemoProducer");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
        this.isAsync = isAsync;
    }

    private void sendMessage(String key, String value) {
        long startTime = System.currentTimeMillis();
        if (isAsync) {
            sendMessageAsynchronously(key, value, startTime);
        } else {
            sendMessageSynchronously(key, value);
        }
    }

    private void sendMessageSynchronously(String key, String value) {
        try {
            producer.send(new ProducerRecord<>(TOPIC_NAME, key, value)).get();
            LOG.info("Sent message: ({}, {})", key, value);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("", e);
        }
    }

    private void sendMessageAsynchronously(String key, String value, long startTime) {
        producer.send(new ProducerRecord<>(TOPIC_NAME, key),
                new DemoCallBack(startTime, key, value));
    }

    private void sendToKafka() {
        int lineCount = 0;

        //Construct BufferedReader from InputStreamReader
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            //skip first line (names of columns)
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                this.sendMessage(lineCount + "", line);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public static void main(String[] args) {
        KafkaProducer kafkaProducer = new KafkaProducer(false);
        kafkaProducer.sendToKafka();
    }

}

