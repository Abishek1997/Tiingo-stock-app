package com.example.tiingostock.ui.stockdetails;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiingostock.repository.StockRepositoryImpl;

import org.jetbrains.annotations.NotNull;

public class StockDetailsActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final StockRepositoryImpl stockRepository;

    public StockDetailsActivityViewModelFactory(
            StockRepositoryImpl stockRepository
    ) {
        this.stockRepository = stockRepository;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StockDetailsActivityViewModel.class)) {
            return (T) new StockDetailsActivityViewModel(stockRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
