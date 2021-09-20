
package com.example.projectcoop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("partnerId")
    @Expose
    private String partnerId;
    @SerializedName("timeUpdate")
    @Expose
    private String timeUpdate;
    @SerializedName("moneyTotal")
    @Expose
    private String moneyTotal;
    @SerializedName("moneyProfit")
    @Expose
    private String moneyProfit;
    @SerializedName("dataGetDraw")
    @Expose
    private DataGetDraw dataGetDraw;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public String getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(String moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public String getMoneyProfit() {
        return moneyProfit;
    }

    public void setMoneyProfit(String moneyProfit) {
        this.moneyProfit = moneyProfit;
    }

    public DataGetDraw getDataGetDraw() {
        return dataGetDraw;
    }

    public void setDataGetDraw(DataGetDraw dataGetDraw) {
        this.dataGetDraw = dataGetDraw;
    }

}
