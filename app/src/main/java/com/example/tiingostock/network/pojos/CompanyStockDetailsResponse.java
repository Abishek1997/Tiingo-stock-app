package com.example.tiingostock.network.pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyStockDetailsResponse {

    private LastPrice lastPrice;
    private List<DailyPrice> dailyPrice = null;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public LastPrice getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(LastPrice lastPrice) {
        this.lastPrice = lastPrice;
    }

    public List<DailyPrice> getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(List<DailyPrice> dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
