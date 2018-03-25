package com.jackdaw.consumer.thread;

import com.jackdaw.consumer.datastructures.StockData;
import com.jackdaw.consumer.util.MarketDataCalculator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;


public class StockdataFileWriter implements Runnable {

    private List<StockData> stockDataList;
    private String outputPath;
    private volatile boolean running;


    public StockdataFileWriter(List<StockData> stockDataList, String outputPath, boolean running) {
        this.stockDataList = stockDataList;
        this.running = running;
        this.outputPath = outputPath;
    }


    @Override
    public void run() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath, true))) {
            while (running) {
                synchronized (stockDataList) {
                    stockDataList.wait();
                    StringJoiner stringJoiner = new StringJoiner(",");
                    stringJoiner.add(stockDataList.get(stockDataList.size() - 1).getDate())
                            .add(Double.toString(MarketDataCalculator.calculateSimpleMovingAverage(stockDataList)));
                    bufferedWriter.append(stringJoiner.toString());
                    bufferedWriter.newLine();
                }
            }
        } catch (InterruptedException | IOException e) {
            //TODO add exception handling
        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }
}
