package com.jackdaw.consumer;

import com.jackdaw.consumer.model.StockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class DataFileWriter implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DataFileWriter.class);
    private final List<StockData> stockDataList;
    private final String outputPath;
    private volatile boolean running;

    DataFileWriter(List<StockData> stockDataList, String outputPath) {
        this.stockDataList = stockDataList;
        this.outputPath = outputPath;
        this.running = true;
    }

    @Override
    public void run() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath, true))) {
            while (running) {
                synchronized (stockDataList) {
                    stockDataList.wait();
                    /*Program might have come to an end, while we were waiting */
                    if (running) {
                        StringJoiner stringJoiner = new StringJoiner(",");
                        stringJoiner.add(stockDataList.get(stockDataList.size() - 1).getDate())
                                .add(Double.toString(CalculatorUtil.calculateSimpleMovingAverage(stockDataList)));
                        bufferedWriter.append(stringJoiner.toString());
                        bufferedWriter.newLine();
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            LOG.error("",e);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
