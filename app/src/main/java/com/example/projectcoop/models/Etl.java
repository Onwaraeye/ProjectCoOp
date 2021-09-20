
package com.example.projectcoop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Etl {

    @SerializedName("saleTotal")
    @Expose
    private String saleTotal;
    @SerializedName("profitTotal")
    @Expose
    private String profitTotal;

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getProfitTotal() {
        return profitTotal;
    }

    public void setProfitTotal(String profitTotal) {
        this.profitTotal = profitTotal;
    }

}
