package com.example.tiingostock.network.connectivity;

import androidx.lifecycle.LiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyNewsResponse;
import com.example.tiingostock.network.pojos.CompanyStockDetailsResponse;

import java.util.List;

public interface StockDataSource {

    void fetchAutocompleteData(
        String ticker
    );

    void fetchCompanyDetails(
        String ticker
    );

    void fetchCompanyStockDetails(
        String ticker
    );

    void fetchCompanyNewsDetails(
        String ticker
    );

    LiveData<List<AutocompleteResponseItem>> getAutoCompleteData();
    LiveData<CompanyDetailsResponse> getCompanyDetailsData();
    LiveData<CompanyStockDetailsResponse> getCompanyStockDetailsData();
    LiveData<List<CompanyNewsResponse>> getCompanyNewsDetailsData();

}
