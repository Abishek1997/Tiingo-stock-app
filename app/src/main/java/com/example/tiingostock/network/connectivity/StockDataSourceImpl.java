package com.example.tiingostock.network.connectivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyNewsResponse;
import com.example.tiingostock.network.pojos.CompanyStockDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyStockHistoryResponse;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDataSourceImpl implements StockDataSource {

    private final MutableLiveData<List<AutocompleteResponseItem>> _downloadedAutocompleteData = new MutableLiveData<>();
    private final MutableLiveData<CompanyDetailsResponse> _downloadedCompanyDetailsData = new MutableLiveData<>();
    private final MutableLiveData<CompanyStockDetailsResponse> _downloadedCompanyStockDetailsData = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyNewsResponse>> _downloadedCompanyNewsDetailsData = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyStockHistoryResponse>> _downloadedCompanyStockHistoryData = new MutableLiveData<>();

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
    public void fetchCompanyStockDetails(String ticker) {
        TiingoAPIRetrofitService
                .getInstance()
                .getRetrofitInterface()
                .getCompanyStockDetailsAsync(ticker)
                .enqueue(new Callback<CompanyStockDetailsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<CompanyStockDetailsResponse> call, @NotNull Response<CompanyStockDetailsResponse> response) {
                        _downloadedCompanyStockDetailsData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<CompanyStockDetailsResponse> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void fetchCompanyNewsDetails(String ticker) {
        TiingoAPIRetrofitService
                .getInstance()
                .getRetrofitInterface()
                .getCompanyNewsDetailsAsync(ticker)
                .enqueue(new Callback<List<CompanyNewsResponse>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CompanyNewsResponse>> call, @NotNull Response<List<CompanyNewsResponse>> response) {
                        _downloadedCompanyNewsDetailsData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CompanyNewsResponse>> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void fetchCompanyStockHistoryDetails(String ticker) {
        TiingoAPIRetrofitService
                .getInstance()
                .getRetrofitInterface()
                .getCompanyStockHistoryDetailsAsync(ticker)
                .enqueue(new Callback<List<CompanyStockHistoryResponse>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CompanyStockHistoryResponse>> call, @NotNull Response<List<CompanyStockHistoryResponse>> response) {
                        _downloadedCompanyStockHistoryData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CompanyStockHistoryResponse>> call, @NotNull Throwable t) {
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

    @Override
    public LiveData<CompanyStockDetailsResponse> getCompanyStockDetailsData() {
        return _downloadedCompanyStockDetailsData;
    }

    @Override
    public LiveData<List<CompanyNewsResponse>> getCompanyNewsDetailsData() {
        return _downloadedCompanyNewsDetailsData;
    }

    @Override
    public LiveData<List<CompanyStockHistoryResponse>> getCompanyStockHistoryData() {
        return _downloadedCompanyStockHistoryData;
    }
}
