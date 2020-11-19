package com.example.tiingostock.network.retrofit;


import com.example.tiingostock.network.pojos.AutocompleteResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


//http://10.0.2.2:8081/autocomplete?ticker=ama
public class TiingoAPIRetrofitService {
    private static TiingoAPIRetrofitService client;
    private static final String BASE_URL = "http://10.0.2.2:8081/";
    private final Retrofit retrofit;

    public TiingoAPIRetrofitService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface RetrofitInterface {
        @GET("autocomplete")
        Call<List<AutocompleteResponseItem>> getAutoCompleteAsync(@Query("ticker") String ticker);
    }

    public static TiingoAPIRetrofitService getInstance(){
        if (client == null){
            client = new TiingoAPIRetrofitService();
        }
        return client;
    }

    public RetrofitInterface getRetrofitInterface() {
        return retrofit.create(RetrofitInterface.class);
    }

}
