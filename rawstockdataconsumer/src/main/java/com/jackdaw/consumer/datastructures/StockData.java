package com.jackdaw.consumer.datastructures;

public class StockData {

    String date;
    double low;
    double high;
    double open;
    double close;
    double average;

    public StockData(String date, double open, double high, double low, double close) {
        this.date = date;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.average = (low + high) / 2;
    }

    public double getAverage() {
        return average;
    }

    public String getDate() {
        return date;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }
}
