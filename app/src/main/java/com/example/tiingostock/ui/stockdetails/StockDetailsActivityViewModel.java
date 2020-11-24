package com.example.tiingostock.ui.stockdetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyNewsResponse;
import com.example.tiingostock.network.pojos.CompanyStockDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyStockHistoryResponse;
import com.example.tiingostock.repository.StockRepositoryImpl;

import java.util.List;

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

    public LiveData<CompanyStockDetailsResponse> getCompanyStockDetails(String ticker){
        return this.stockRepository.getCompanyStockDetails(ticker);
    }

    public LiveData<List<CompanyNewsResponse>> getCompanyNewsDetails(String ticker){
        return this.stockRepository.getCompanyNewsDetails(ticker);
    }

    public LiveData<List<CompanyStockHistoryResponse>> getCompanyStockHistoryDetails(String ticker){
        return this.stockRepository.getCompanyStockHistoryDetails(ticker);
    }
}
