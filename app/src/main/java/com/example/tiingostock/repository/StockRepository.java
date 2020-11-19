package com.example.tiingostock.repository;

import androidx.lifecycle.LiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;

import java.util.List;

public interface StockRepository {
    LiveData<List<AutocompleteResponseItem>> getAutocompleteData(String input);
}
