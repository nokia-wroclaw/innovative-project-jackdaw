package com.jackdaw.consumer.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockData)) return false;
        StockData stockData = (StockData) o;
        return Double.compare(stockData.getLow(), getLow()) == 0 &&
                Double.compare(stockData.getHigh(), getHigh()) == 0 &&
                Double.compare(stockData.getOpen(), getOpen()) == 0 &&
                Double.compare(stockData.getClose(), getClose()) == 0 &&
                Double.compare(stockData.getAverage(), getAverage()) == 0 &&
                Objects.equals(getDate(), stockData.getDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDate(), getLow(), getHigh(), getOpen(), getClose(), getAverage());
    }
}
