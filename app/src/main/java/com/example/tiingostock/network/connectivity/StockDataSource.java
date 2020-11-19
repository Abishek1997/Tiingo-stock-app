package com.example.tiingostock.network.connectivity;

import androidx.lifecycle.LiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;

import java.util.List;

public interface StockDataSource {

    void fetchAutocompleteData(
        String ticker
    );

    void fetchCompanyDetails(
        String ticker
    );

    LiveData<List<AutocompleteResponseItem>> getAutoCompleteData();
    LiveData<CompanyDetailsResponse> getCompanyDetailsData();
}
