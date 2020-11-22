package com.example.tiingostock.network.pojos;

import java.util.HashMap;
import java.util.Map;

public class StoredFavorites {

    private String companyName;
    private String companyTicker;
    private Double companyStockValue;
    private Double companyStockValueChange;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTicker() { return companyTicker; }
    public void setCompanyTicker(String companyTicker) { this.companyTicker = companyTicker; }

    public Double getCompanyStockValue() { return companyStockValue; }
    public void setCompanyStockValue(Double companyStockValue) { this.companyStockValue = companyStockValue; }

    public Double getCompanyStockValueChange() { return companyStockValueChange; }
    public void setCompanyStockValueChange(Double companyStockValueChange) { this.companyStockValueChange = companyStockValueChange; }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
