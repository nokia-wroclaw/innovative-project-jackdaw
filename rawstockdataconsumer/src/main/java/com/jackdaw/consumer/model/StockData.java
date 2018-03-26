package com.jackdaw.consumer.model;

public class StockData {

    private String date;
    private double low;
    private double high;
    private double open;
    private double close;
    private double average;

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
