package com.example.tiingostock.network.connectivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDataSourceImpl implements StockDataSource {

    private final MutableLiveData<List<AutocompleteResponseItem>> _downloadedAutocompleteData = new MutableLiveData<>();
    private final MutableLiveData<CompanyDetailsResponse> _downloadedCompanyDetailsData = new MutableLiveData<>();

    public StockDataSourceImpl(
        TiingoAPIRetrofitService tiingoAPIRetrofitService
    ){
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
    public void fetchCompanyDetails(String ticker){
        TiingoAPIRetrofitService
                .getInstance()
                .getRetrofitInterface()
                .getCompanyDetailsAsync(ticker)
                .enqueue(new Callback<CompanyDetailsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<CompanyDetailsResponse> call, @NotNull Response<CompanyDetailsResponse> response) {
                        _downloadedCompanyDetailsData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<CompanyDetailsResponse> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
    @Override
    public LiveData<List<AutocompleteResponseItem>> getAutoCompleteData() {
        return _downloadedAutocompleteData;
    }

    @Override
    public LiveData<CompanyDetailsResponse> getCompanyDetailsData(){
        return _downloadedCompanyDetailsData;
    }
}
