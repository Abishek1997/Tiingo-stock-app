package com.example.tiingostock.network.connectivity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDataSourceImpl implements StockDataSource {

    private final TiingoAPIRetrofitService tiingoAPIRetrofitService;
    private final MutableLiveData<List<AutocompleteResponseItem>> _downloadedAutocompleteData = new MutableLiveData<>();

    public StockDataSourceImpl(
        TiingoAPIRetrofitService tiingoAPIRetrofitService
    ){
        this.tiingoAPIRetrofitService = tiingoAPIRetrofitService;
    }

    @Override
    public void fetchAutocompleteData(String ticker) {
        TiingoAPIRetrofitService
                .getInstance()
                .getRetrofitInterface()
                .getAutoCompleteAsync(ticker)
                .enqueue(new Callback<List<AutocompleteResponseItem>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<AutocompleteResponseItem>> call, @NotNull Response<List<AutocompleteResponseItem>> response) {
                        _downloadedAutocompleteData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AutocompleteResponseItem>> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public LiveData<List<AutocompleteResponseItem>> getAutoCompleteData() {
        return _downloadedAutocompleteData;
    }
}
