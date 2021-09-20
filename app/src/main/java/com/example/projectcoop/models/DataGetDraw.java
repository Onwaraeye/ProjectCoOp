
package com.example.projectcoop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataGetDraw {

    @SerializedName("unitel")
    @Expose
    private Unitel unitel;
    @SerializedName("etl")
    @Expose
    private Etl etl;
    @SerializedName("beeline")
    @Expose
    private Beeline beeline;
    @SerializedName("laotelecom")
    @Expose
    private Laotelecom laotelecom;
    @SerializedName("garena")
    @Expose
    private Garena garena;
    @SerializedName("truemoney")
    @Expose
    private Truemoney truemoney;
    @SerializedName("lottory")
    @Expose
    private Lottory lottory;

    public Unitel getUnitel() {
        return unitel;
    }

    public void setUnitel(Unitel unitel) {
        this.unitel = unitel;
    }

    public Etl getEtl() {
        return etl;
    }

    public void setEtl(Etl etl) {
        this.etl = etl;
    }

    public Beeline getBeeline() {
        return beeline;
    }

    public void setBeeline(Beeline beeline) {
        this.beeline = beeline;
    }

    public Laotelecom getLaotelecom() {
        return laotelecom;
    }

    public void setLaotelecom(Laotelecom laotelecom) {
        this.laotelecom = laotelecom;
    }

    public Garena getGarena() {
        return garena;
    }

    public void setGarena(Garena garena) {
        this.garena = garena;
    }

    public Truemoney getTruemoney() {
        return truemoney;
    }

    public void setTruemoney(Truemoney truemoney) {
        this.truemoney = truemoney;
    }

    public Lottory getLottory() {
        return lottory;
    }

    public void setLottory(Lottory lottory) {
        this.lottory = lottory;
    }

}
