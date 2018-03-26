package com.jackdaw;

import com.jackdaw.consumer.RawStockDataConsumerBuilder;
import com.jackdaw.consumer.datastructures.ShiftingList;
import com.jackdaw.consumer.datastructures.StockData;
import com.jackdaw.consumer.thread.StockdataFileWriter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;

public class StockDataConsumer {

    public static void main(String[] args) throws InterruptedException {
        int maxSize = Integer.parseInt(args[0]);
        String topic = args[1];
        String outputPath = args[2];
        List<StockData> stockDataList = new ShiftingList(maxSize);
        StockdataFileWriter stockdataFileWriter = new StockdataFileWriter(stockDataList, outputPath, true);
        Thread thread = new Thread(stockdataFileWriter);
        thread.start();
        runConsumer(stockDataList, topic, stockdataFileWriter);
    }

    static void runConsumer(List<StockData> stockDataList, String topic, StockdataFileWriter stockdataFileWriter) {
        final Consumer<Long, String> consumer = RawStockDataConsumerBuilder.createConsumer(topic);

        final int giveUp = 100;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            synchronized (stockDataList) {
                consumerRecords.forEach(record -> {
                /*
                message-format: Date,Open,High,Low,Close,Volume,OpenInt
                 */
                    String[] splittedMessage = record.value().split(",");
                    stockDataList.add(new StockData(splittedMessage[0],
                            Double.parseDouble(splittedMessage[1]),
                            Double.parseDouble(splittedMessage[2]),
                            Double.parseDouble(splittedMessage[3]),
                            Double.parseDouble(splittedMessage[4])));
                    stockDataList.notify();
                });
            }

            consumer.commitAsync();
        }
        consumer.close();
        stockdataFileWriter.setRunning(false);
        //TODO remove after making sure everything works
        System.out.println("DONE");
    }
}


