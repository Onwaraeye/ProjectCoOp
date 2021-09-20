package com.example.projectcoop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortMoneyProduct {
    private String product;
    private String moneyProduct;

    public SortMoneyProduct(String product, String moneyProduct) {
        this.product = product;
        this.moneyProduct = moneyProduct;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMoneyProduct() {
        return moneyProduct;
    }

    public void setMoneyProduct(String moneyProduct) {
        this.moneyProduct = moneyProduct;
    }
}
