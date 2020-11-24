package com.example.tiingostock.repository;

import androidx.lifecycle.LiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyNewsResponse;
import com.example.tiingostock.network.pojos.CompanyStockDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyStockHistoryResponse;

import java.util.List;

public interface StockRepository {
    LiveData<List<AutocompleteResponseItem>> getAutocompleteData(String input);

    LiveData<CompanyDetailsResponse> getCompanyDetails(String ticker);

    LiveData<CompanyStockDetailsResponse> getCompanyStockDetails(String ticker);

    LiveData<List<CompanyNewsResponse>> getCompanyNewsDetails(String ticker);

    LiveData<List<CompanyStockHistoryResponse>> getCompanyStockHistoryDetails(String ticker);
}
