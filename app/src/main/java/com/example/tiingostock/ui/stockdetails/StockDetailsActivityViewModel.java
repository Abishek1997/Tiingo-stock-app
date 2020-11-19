package com.example.tiingostock.ui.stockdetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.repository.StockRepositoryImpl;

public class StockDetailsActivityViewModel extends ViewModel {

    private final StockRepositoryImpl stockRepository;
    public StockDetailsActivityViewModel(
            StockRepositoryImpl stockRepository
    ){
        this.stockRepository = stockRepository;
    }

    public LiveData<CompanyDetailsResponse> getCompanyDetails(String ticker){
        return this.stockRepository.getCompanyDetails(ticker);
    }
}
