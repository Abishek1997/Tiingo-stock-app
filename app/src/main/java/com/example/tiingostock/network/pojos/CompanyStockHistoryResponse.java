package com.example.tiingostock.network.pojos;

import java.util.HashMap;
import java.util.Map;

public class CompanyStockHistoryResponse {

    private String date;
    private Double close;
    private Double high;
    private Double low;
    private Double open;
    private Integer volume;
    private Double adjClose;
    private Double adjHigh;
    private Double adjLow;
    private Double adjOpen;
    private Integer adjVolume;
    private Double divCash;
    private Double splitFactor;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
    }

    public Double getAdjHigh() {
        return adjHigh;
    }

    public void setAdjHigh(Double adjHigh) {
        this.adjHigh = adjHigh;
    }

    public Double getAdjLow() {
        return adjLow;
    }

    public void setAdjLow(Double adjLow) {
        this.adjLow = adjLow;
    }

    public Double getAdjOpen() {
        return adjOpen;
    }

    public void setAdjOpen(Double adjOpen) {
        this.adjOpen = adjOpen;
    }

    public Integer getAdjVolume() {
        return adjVolume;
    }

    public void setAdjVolume(Integer adjVolume) {
        this.adjVolume = adjVolume;
    }

    public Double getDivCash() {
        return divCash;
    }

    public void setDivCash(Double divCash) {
        this.divCash = divCash;
    }

    public Double getSplitFactor() {
        return splitFactor;
    }

    public void setSplitFactor(Double splitFactor) {
        this.splitFactor = splitFactor;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}