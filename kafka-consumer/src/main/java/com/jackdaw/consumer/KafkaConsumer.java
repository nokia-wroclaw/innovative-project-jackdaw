package com.jackdaw.consumer;

import com.jackdaw.consumer.model.ShiftingList;
import com.jackdaw.consumer.model.StockData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;

public class KafkaConsumer {

    public static void main(String[] args) throws InterruptedException {
        int maxSize = Integer.parseInt(args[0]);
        String topic = args[1];
        String outputPath = args[2];
        final ShiftingList<StockData> stockDataList = new ShiftingList<>(maxSize);
        DataFileWriter dataFileWriter = new DataFileWriter(stockDataList, outputPath);
        Thread thread = new Thread(dataFileWriter);
        thread.start();
        runConsumer(stockDataList, topic, dataFileWriter);
        thread.join();
    }

    private static void runConsumer(List<StockData> stockDataList, String topic, DataFileWriter dataFileWriter) {
        final Consumer<Long, String> consumer = KafkaConsumerUtil.createConsumer(topic);

        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

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

