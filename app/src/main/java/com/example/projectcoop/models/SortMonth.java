package com.example.projectcoop.models;

public class SortMonth {
    private int mon;
    private int year;
    private Long moneyValue;
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int month) {
        this.mon = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Long moneyValue) {
        this.moneyValue = moneyValue;
    }
}
