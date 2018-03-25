package com.jackdaw.consumer.util;

import com.jackdaw.consumer.datastructures.StockData;

import java.util.List;

public class MarketDataCalculator {

    public static double calculateSimpleMovingAverage(List<StockData> stockDataList){
        double movingAverage=0;
        for(StockData sd : stockDataList){
            movingAverage+=sd.getAverage();
        }
        return movingAverage/stockDataList.size();
    }
}
