package com.example.projectcoop.models;

public class Partner {
    private String user_id;
    private String password;
    private int month;
    private int year;

    public Partner(String user_id, String password, int month, int year) {
        this.user_id = user_id;
        this.password = password;
        this.month = month;
        this.year = year;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
