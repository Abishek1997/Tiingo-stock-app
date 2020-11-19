package com.example.tiingostock.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tiingostock.network.connectivity.StockDataSourceImpl;
import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;

import java.util.List;

public class StockRepositoryImpl implements StockRepository {
    final Context context;
    private final StockDataSourceImpl stockDataSource;
    public StockRepositoryImpl(
            Context context,
            StockDataSourceImpl stockDataSource
    ){
        this.context = context;
        this.stockDataSource = stockDataSource;
    }

    public LiveData<List<AutocompleteResponseItem>> getAutocompleteData(String input){
        this.stockDataSource.fetchAutocompleteData(input);
        return this.stockDataSource.getAutoCompleteData();
    }

    @Override
    public LiveData<CompanyDetailsResponse> getCompanyDetails(String ticker) {
        this.stockDataSource.fetchCompanyDetails(ticker);
        return this.stockDataSource.getCompanyDetailsData();
    }
}
