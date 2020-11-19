package com.example.tiingostock.ui.stockdetails;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
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
        TextView toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(null, Typeface.BOLD);
        bindUI();
    }

    public void bindUI(){

        LiveData<CompanyDetailsResponse> companyDetailsObserver = viewModel.getCompanyDetails(ticker);
        Group groupLoading = (Group)findViewById(R.id.group_loading);
        Group groupReady = (Group)findViewById(R.id.group_ready);
        Observer<CompanyDetailsResponse> namedObserver = (Observer<CompanyDetailsResponse>) data -> {
            if (data == null){
                groupLoading.setVisibility(View.VISIBLE);
                groupReady.setVisibility(View.GONE);
            }
            Log.d("data", Objects.requireNonNull(data).getDescription());
            groupLoading.setVisibility(View.GONE);
            groupReady.setVisibility(View.VISIBLE);
        };
        companyDetailsObserver.observe(this, namedObserver);
    }
}