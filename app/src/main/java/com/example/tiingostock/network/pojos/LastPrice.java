package com.example.tiingostock.network.pojos;

import java.util.HashMap;
import java.util.Map;

public class LastPrice {

    private String timestamp;
    private Object bidSize;
    private String lastSaleTimestamp;
    private Double low;
    private Object bidPrice;
    private Double prevClose;
    private String quoteTimestamp;
    private Double last;
    private Object askSize;
    private Integer volume;
    private Object lastSize;
    private String ticker;
    private Double high;
    private Object mid;
    private Object askPrice;
    private Double open;
    private Double tngoLast;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getBidSize() {
        return bidSize;
    }

    public void setBidSize(Object bidSize) {
        this.bidSize = bidSize;
    }

    public String getLastSaleTimestamp() {
        return lastSaleTimestamp;
    }

    public void setLastSaleTimestamp(String lastSaleTimestamp) {
        this.lastSaleTimestamp = lastSaleTimestamp;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Object getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Object bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(Double prevClose) {
        this.prevClose = prevClose;
    }

    public String getQuoteTimestamp() {
        return quoteTimestamp;
    }

    public void setQuoteTimestamp(String quoteTimestamp) {
        this.quoteTimestamp = quoteTimestamp;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Object getAskSize() {
        return askSize;
    }

    public void setAskSize(Object askSize) {
        this.askSize = askSize;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Object getLastSize() {
        return lastSize;
    }

    public void setLastSize(Object lastSize) {
        this.lastSize = lastSize;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Object getMid() {
        return mid;
    }

    public void setMid(Object mid) {
        this.mid = mid;
    }

    public Object getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Object askPrice) {
        this.askPrice = askPrice;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getTngoLast() {
        return tngoLast;
    }

    public void setTngoLast(Double tngoLast) {
        this.tngoLast = tngoLast;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}