package com.example.projectcoop;

public class MonthYear {
    private int globalYear;
    private int globalMonth;

    public MonthYear() {
    }

    public MonthYear(int globalYear, int globalMonth) {
        this.globalYear = globalYear;
        this.globalMonth = globalMonth;
    }

    public int getGlobalYear() {
        return globalYear;
    }

    public void setGlobalYear(int globalYear) {
        this.globalYear = globalYear;
    }

    public int getGlobalMonth() {
        return globalMonth;
    }

    public void setGlobalMonth(int globalMonth) {
        this.globalMonth = globalMonth;
    }
}
