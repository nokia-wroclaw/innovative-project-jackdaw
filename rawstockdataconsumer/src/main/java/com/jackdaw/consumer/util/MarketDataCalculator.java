package com.jackdaw.consumer.util;

import com.jackdaw.consumer.datastructures.StockData;

import java.util.List;

public class MarketDataCalculator {

    public static double calculateMovingAverage(List<StockData> list){
        double movingAverage=0;
        for(StockData sd : list){
            movingAverage+=sd.getAverage();
        }
        return movingAverage/list.size();
    }
}
