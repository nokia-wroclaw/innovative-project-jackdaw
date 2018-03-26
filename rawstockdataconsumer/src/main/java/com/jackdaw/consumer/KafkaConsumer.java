package com.jackdaw.consumer;

import com.jackdaw.consumer.model.ShiftingList;
import com.jackdaw.consumer.model.StockData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;

public class KafkaConsumer {

    public static void main(String[] args) {
        int maxSize = Integer.parseInt(args[0]);
        String topic = args[1];
        String outputPath = args[2];
        ShiftingList<StockData> stockDataList = new ShiftingList<>(maxSize);
        DataFileWriter dataFileWriter = new DataFileWriter(stockDataList, outputPath, true);
        Thread thread = new Thread(dataFileWriter);
        thread.start();
        runConsumer(stockDataList, topic, dataFileWriter);
    }

    private static void runConsumer(List<StockData> stockDataList, String topic, DataFileWriter dataFileWriter) {
        final Consumer<Long, String> consumer = KafkaConsumerUtil.createConsumer(topic);

        final int giveUp = 100;
        int noRecordsCount = 0;

        //FIXME add break to this loop
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            //FIXME blocks should be synchronized on "private final" fields
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
                    stockDataList.notify(); //FIXME use notifyAll
                });
            }

            consumer.commitAsync();
        }
        consumer.close();
        dataFileWriter.setRunning(false);
        //TODO remove after making sure everything works
        //FIXME use logger or delete
        System.out.println("DONE");
    }
}

