package com.example.tiingostock.ui.homepage;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiingostock.repository.StockRepository;
import com.example.tiingostock.repository.StockRepositoryImpl;

import org.jetbrains.annotations.NotNull;

public class HomePageActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final StockRepositoryImpl stockRepository;

    public HomePageActivityViewModelFactory(
            StockRepositoryImpl stockRepository
    ) {
        this.stockRepository = stockRepository;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomePageActivityViewModel.class)) {
            return (T) new HomePageActivityViewModel(stockRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}