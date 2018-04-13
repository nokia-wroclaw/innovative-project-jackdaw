package com.jackdaw.consumer;

import com.jackdaw.consumer.model.ShiftingList;
import com.jackdaw.consumer.model.StockData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;

public class KafkaConsumer {

    private static final int MAX_SIZE = 10;
    private static final String TOPIC = "test";
    private static final String OUTPUT_PATH = "output.txt";

    public static void main(String[] args) throws InterruptedException {
        final ShiftingList<StockData> stockDataList = new ShiftingList<>(MAX_SIZE);
        DataFileWriter dataFileWriter = new DataFileWriter(stockDataList, OUTPUT_PATH);
        Thread thread = new Thread(dataFileWriter);
        thread.start();
        runConsumer(stockDataList, TOPIC, dataFileWriter);
        thread.join();
    }

    private static void runConsumer(List<StockData> stockDataList, String topic, DataFileWriter dataFileWriter) {
        final Consumer<String, String> consumer = KafkaConsumerUtil.createConsumer(topic);

        final int giveUp = 100;
        int noRecordsCount = 0;
        int readRecords = 0;

        while (readRecords<252) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            ++readRecords;

            consumerRecords.forEach(record -> {
                /*
                message-format: Date,Open,High,Low,Close,Volume,OpenInt
                 */
                String[] splitMessage = record.value().split(",");
                stockDataList.add(new StockData(splitMessage[0],
                        Double.parseDouble(splitMessage[1]),
                        Double.parseDouble(splitMessage[2]),
                        Double.parseDouble(splitMessage[3]),
                        Double.parseDouble(splitMessage[4])));
                ((ShiftingList) stockDataList).eventHappened();
            });

            consumer.commitAsync();
        }
        consumer.close();
        dataFileWriter.setRunning(false);
        ((ShiftingList) stockDataList).eventHappened();
    }
}

