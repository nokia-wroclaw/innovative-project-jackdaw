package com.jackdaw.consumer;

import com.jackdaw.consumer.model.StockData;

import java.util.List;

public class CalculatorUtil {

    private CalculatorUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateSimpleMovingAverage(List<StockData> stockDataList) {
        double movingAverage = 0;
        for (StockData sd : stockDataList) {
            movingAverage += sd.getAverage();
        }
        return movingAverage / stockDataList.size();
    }
}
