package com.example.tiingostock.ui.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.repository.StockRepositoryImpl;

import java.util.List;

public class HomePageActivityViewModel extends ViewModel {

    private final StockRepositoryImpl stockRepository;
    public HomePageActivityViewModel(
            StockRepositoryImpl stockRepository
    ){
        this.stockRepository = stockRepository;
    }

    public LiveData<List<AutocompleteResponseItem>> getAutocompleteData(String input){
        return this.stockRepository.getAutocompleteData(input);
    }
}
