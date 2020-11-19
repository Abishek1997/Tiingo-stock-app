package com.example.tiingostock.ui.stockdetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiingostock.R;
import com.example.tiingostock.network.connectivity.StockDataSourceImpl;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;
import com.example.tiingostock.repository.StockRepositoryImpl;

import java.util.Objects;

public class StockDetailsActivity extends AppCompatActivity {

    private final TiingoAPIRetrofitService tiingoAPIRetrofitService = new TiingoAPIRetrofitService();
    private final StockDataSourceImpl stockDataSource = new StockDataSourceImpl(tiingoAPIRetrofitService);
    private final StockRepositoryImpl stockRepository = new StockRepositoryImpl(this, stockDataSource);
    private final StockDetailsActivityViewModelFactory viewModelFactory = new StockDetailsActivityViewModelFactory(stockRepository);
    private StockDetailsActivityViewModel viewModel;
    private String ticker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(StockDetailsActivityViewModel.class);
        ticker = Objects.requireNonNull(getIntent().getExtras()).getString("ticker");
        bindUI();
    }

    public void bindUI(){

        LiveData<CompanyDetailsResponse> companyDetailsObserver = viewModel.getCompanyDetails(ticker);

        Observer<CompanyDetailsResponse> namedObserver = (Observer<CompanyDetailsResponse>) data -> {
        };
        companyDetailsObserver.observe(this, namedObserver);
    }
}