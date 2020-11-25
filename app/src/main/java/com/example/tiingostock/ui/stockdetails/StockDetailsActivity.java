package com.example.tiingostock.ui.stockdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiingostock.R;
import com.example.tiingostock.network.connectivity.StockDataSourceImpl;
import com.example.tiingostock.network.pojos.CompanyDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyNewsResponse;
import com.example.tiingostock.network.pojos.CompanyStockDetailsResponse;
import com.example.tiingostock.network.pojos.CompanyStockHistoryResponse;
import com.example.tiingostock.network.pojos.StoredFavorites;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;
import com.example.tiingostock.repository.StockRepositoryImpl;
import com.example.tiingostock.ui.helpers.DialogBox;
import com.example.tiingostock.ui.helpers.NewsRecyclerViewItem;
import com.example.tiingostock.ui.helpers.TradeDialogBox;
import com.google.gson.Gson;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


//TODO: Implement portfolio, market open/close strategies

@SuppressWarnings({"MalformedFormatString", "rawtypes"})
public class StockDetailsActivity extends AppCompatActivity {

    private final TiingoAPIRetrofitService tiingoAPIRetrofitService = new TiingoAPIRetrofitService();
    private final StockDataSourceImpl stockDataSource = new StockDataSourceImpl(tiingoAPIRetrofitService);
    private final StockRepositoryImpl stockRepository = new StockRepositoryImpl(this, stockDataSource);
    private final StockDetailsActivityViewModelFactory viewModelFactory = new StockDetailsActivityViewModelFactory(stockRepository);
    private StockDetailsActivityViewModel viewModel;
    private String ticker = "";
    private SharedPreferences sharedPreferences;
    ImageView imageBookmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = StockDetailsActivity.this.getSharedPreferences(("favorites"), Context.MODE_PRIVATE);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        TextView textPortfolio = findViewById(R.id.text_portfolio_title);

        viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(StockDetailsActivityViewModel.class);
        ticker = Objects.requireNonNull(getIntent().getExtras()).getString("ticker");
        toolbarTitle.setTypeface(null, Typeface.BOLD);
        textPortfolio.setTypeface(null, Typeface.BOLD);

        imageBookmark = findViewById(R.id.image_bookmark);

        if (sharedPreferences.contains(ticker)){
            imageBookmark.setImageResource(R.drawable.ic_baseline_star_24);
        } else{
            imageBookmark.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
        bindUI();
    }

    public void bindUI(){

        LiveData<CompanyDetailsResponse> companyDetailsObserver = viewModel.getCompanyDetails(ticker);
        LiveData<CompanyStockDetailsResponse> companyStockDetailsObserver = viewModel.getCompanyStockDetails(ticker);
        LiveData<List<CompanyNewsResponse>> companyNewsDetailsObserver = viewModel.getCompanyNewsDetails(ticker);
        LiveData<List<CompanyStockHistoryResponse>> companyStockHistoryDetailsObserver = viewModel.getCompanyStockHistoryDetails(ticker);

        StoredFavorites favorites = new StoredFavorites();

        Group groupLoading = findViewById(R.id.group_loading);
        Group groupReady = findViewById(R.id.group_ready);
        Observer<CompanyDetailsResponse> companyObserverCallback = data -> {
            if (data == null){
                groupLoading.setVisibility(View.VISIBLE);
                groupReady.setVisibility(View.GONE);
            }
            groupLoading.setVisibility(View.GONE);
            groupReady.setVisibility(View.VISIBLE);
            setCompanyDetailsUI(Objects.requireNonNull(data).getTicker(), data.getName());
            setCompanyAboutUI(data.getDescription());

            favorites.setCompanyName(data.getName());
            favorites.setCompanyTicker(data.getTicker());
        };


        @SuppressLint("DefaultLocale") Observer<CompanyStockDetailsResponse> companyStockDataObserverCallback = data ->{
            if (data == null){
                groupLoading.setVisibility(View.VISIBLE);
                groupReady.setVisibility(View.GONE);
                return;
            }
            groupLoading.setVisibility(View.GONE);
            groupReady.setVisibility(View.VISIBLE);
            setCompanyStockUI(Objects.requireNonNull(data).getLastPrice().getTngoLast(), data.getLastPrice().getLast() - data.getLastPrice().getPrevClose());
            setCompanyStatsUI(
                    String.format(" %.2f", data.getLastPrice().getTngoLast()), String.format(" %.2f", data.getLastPrice().getLow()),
                    data.getLastPrice().getBidPrice() == null ? " null" : String.format(" %.2f", data.getLastPrice().getBidPrice()),
                    String.format(" %.2f", data.getLastPrice().getOpen()),
                    data.getLastPrice().getMid() == null ? " null": String.format(" %.2f", data.getLastPrice().getMid()),
                    String.format(" %.2f", data.getLastPrice().getHigh()), " " + data.getLastPrice().getVolume().toString()
            );


            favorites.setCompanyStockValue(data.getLastPrice().getLast());
            favorites.setCompanyStockValueChange(data.getLastPrice().getLast() - data.getLastPrice().getPrevClose());
            setCompanyPortfolioUI();
        };

        Observer<List<CompanyNewsResponse>> companyNewsDataObserverCallback = data -> {
            if (data == null){
                groupLoading.setVisibility(View.VISIBLE);
                groupReady.setVisibility(View.GONE);
                return;
            }
            groupLoading.setVisibility(View.GONE);
            groupReady.setVisibility(View.VISIBLE);
            if (data.size() > 0) {
                setCompanyNewsUI(
                        data.get(0).getUrlToImage(),
                        data.get(0).getSource().getName(),
                        data.get(0).getPublishedAt(),
                        data.get(0).getTitle(),
                        data.get(0).getUrl()
                );
            }
            if (data.size() >= 1){
                setCompanyNewsRecyclerUI(constructNewsRecyclerViewItems(data.subList(1, data.size())));
            }

        };

        Observer<List<CompanyStockHistoryResponse>> companyStockHistoryObserverCallback = data -> {
            if (data == null){
                groupLoading.setVisibility(View.VISIBLE);
                groupReady.setVisibility(View.GONE);
                return;
            }
            groupLoading.setVisibility(View.GONE);
            groupReady.setVisibility(View.VISIBLE);
            setCompanyChartsUI(data, ticker);
        };

        companyDetailsObserver.observe(this, companyObserverCallback);
        companyStockDetailsObserver.observe(this, companyStockDataObserverCallback);
        companyNewsDetailsObserver.observe(this, companyNewsDataObserverCallback);
        companyStockHistoryDetailsObserver.observe(this, companyStockHistoryObserverCallback);
        final Boolean[] isSet = {sharedPreferences.contains(ticker)};

        imageBookmark.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();

            if (isSet[0]){
                imageBookmark.setImageResource(R.drawable.ic_baseline_star_border_24);
                isSet[0] = false;
                editor.remove(ticker);
                Toast.makeText(this, ticker + " has been removed from Favorites", Toast.LENGTH_SHORT).show();

            } else{
                imageBookmark.setImageResource(R.drawable.ic_baseline_star_24);
                isSet[0] = true;
                editor.putString(ticker, gson.toJson(favorites));
                Toast.makeText(this, ticker + " has been added to Favorites", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });
    }

    public void setCompanyDetailsUI(String ticker, String name){
        TextView textTicker = findViewById(R.id.text_ticker);
        textTicker.setText(ticker);
        textTicker.setTypeface(null, Typeface.BOLD);

        TextView textCompanyName = findViewById(R.id.text_company_name);
        textCompanyName.setText(name);
        textCompanyName.setTypeface(null, Typeface.BOLD);
    }

    @SuppressLint("DefaultLocale")
    public void setCompanyStockUI(Double price, Double priceDiff){
        TextView textStockValue = findViewById(R.id.text_stock_value);
        TextView textStockValueChange = findViewById(R.id.text_stock_value_change);

        String stockValue = String.format("%.2f", price);

        textStockValue.setText(stockValue);
        textStockValue.setTypeface(null, Typeface.BOLD);
        textStockValueChange.setText(String.format("%.2f $", priceDiff));
        if (priceDiff < 0){
            textStockValueChange.setTextColor(getColor(R.color.color_negative_change));
        } else{
            textStockValueChange.setTextColor(getColor(R.color.color_positive_change));
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setCompanyChartsUI(List<CompanyStockHistoryResponse> items, String ticker){
        WebView webView = findViewById(R.id.webView_charts);

        JSONArray jsonArray = new JSONArray();
        items.forEach(item -> {
            JSONObject jsonObject = new JSONObject();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = null;
            try {
                date = sdf.parse(item.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long epoch = date.getTime();
            try {
                jsonObject.put("date", epoch);
                jsonObject.put("open", item.getOpen());
                jsonObject.put("close", item.getClose());
                jsonObject.put("high", item.getHigh());
                jsonObject.put("low", item.getLow());
                jsonObject.put("volume", item.getVolume());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:init('" + jsonArray +"', '" + ticker + "')");
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    public void setCompanyPortfolioUI(){

        LiveData<CompanyDetailsResponse> companyDetailsObserver = viewModel.getCompanyDetails(ticker);
        LiveData<CompanyStockDetailsResponse> companyStockDetailsObserver = viewModel.getCompanyStockDetails(ticker);

        Button tradeButton = findViewById(R.id.button_trade);
        tradeButton.setOnClickListener(v -> {
            TradeDialogBox tradeDialog = new TradeDialogBox();
            Log.d("data", Objects.requireNonNull(companyDetailsObserver.getValue()).getName());
            tradeDialog.showDialog(StockDetailsActivity.this, companyDetailsObserver.getValue().getName(), companyDetailsObserver.getValue().getTicker(),
            Objects.requireNonNull(companyStockDetailsObserver.getValue()).getLastPrice().getTngoLast());
        });
    }

    public void setCompanyStatsUI(String currentPrice, String low, String bidPrice, String openPrice, String mid, String high, String volume){
        TextView textCurrentPrice = findViewById(R.id.text_current_price_value);
        TextView textLow = findViewById(R.id.text_low_value);
        TextView textBidPrice = findViewById(R.id.text_bid_price_value);
        TextView textOpenPrice = findViewById(R.id.text_open_price_value);
        TextView textMid = findViewById(R.id.text_mid_value);
        TextView textHigh = findViewById(R.id.text_high_value);
        TextView textVolume = findViewById(R.id.text_volume_value);

        textCurrentPrice.setText(currentPrice);
        textLow.setText(low);
        textBidPrice.setText(bidPrice);
        textOpenPrice.setText(openPrice);
        textMid.setText(mid);
        textHigh.setText(high);
        textVolume.setText(volume);
    }

    public void setCompanyAboutUI(String aboutText){
        TextView textNews = findViewById(R.id.text_news_value);
        textNews.setText(aboutText);

        TextView buttonShowMore = findViewById(R.id.button_show_more_less);
        buttonShowMore.setOnClickListener(v -> {
            if (buttonShowMore.getText().toString().equalsIgnoreCase("Show more...")){
                textNews.setMaxLines(Integer.MAX_VALUE);
                buttonShowMore.setText(R.string.show_less);
            } else{
                textNews.setMaxLines(2);
                buttonShowMore.setText(R.string.show_more);
            }
        });
    }

    public void setCompanyNewsUI(String imageURL, String source, String dateTime, String title, String url){

        ImageView firstNewsImage = findViewById(R.id.image_first_news_card);
        TextView textNewsSource = findViewById(R.id.text_rv_news_source_value);
        TextView textNewsTime = findViewById(R.id.text_rv_timeline_value);
        TextView textNewsTitle = findViewById(R.id.text_rv_news_title);
        CardView cardStockNews = findViewById(R.id.card_stock_news);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this).load(imageURL).apply(requestOptions).into(firstNewsImage);
        textNewsSource.setText(source);
        textNewsTime.setText(formatDateTime(dateTime));
        textNewsTitle.setText(title);
        cardStockNews.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
        cardStockNews.setOnLongClickListener(v -> {
            DialogBox dialogBox = new DialogBox();
            dialogBox.showDialog(StockDetailsActivity.this, imageURL, title, url);
            return true;
        });
    }

    public String formatDateTime(String pastTime){
        String time;
        long now = new Date().getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = sdf.parse(pastTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert date != null;
        if (TimeUnit.MILLISECONDS.toDays(now - date.getTime()) == 0){
            if (TimeUnit.MILLISECONDS.toHours(now - date.getTime()) == 0){
                time = TimeUnit.MILLISECONDS.toMinutes(now - date.getTime()) + " minutes ago";
            } else{
                time = TimeUnit.MILLISECONDS.toHours(date.getTime() - now) + " hours ago";
            }
        } else{
            time = TimeUnit.MILLISECONDS.toDays(now - date.getTime()) + " days ago";
        }
        return time;
    }

    public List<NewsRecyclerViewItem> constructNewsRecyclerViewItems(List<CompanyNewsResponse> newsList){
        return newsList
                .stream()
                .map(item -> new NewsRecyclerViewItem(item.getUrlToImage(), item.getSource().getName(), formatDateTime(item.getPublishedAt()), item.getTitle(), StockDetailsActivity.this, item.getUrl()))
                .collect(Collectors.toList());
    }

    public void setCompanyNewsRecyclerUI(List<NewsRecyclerViewItem> items) {
        GroupAdapter groupieAdapter = new GroupAdapter();
        RecyclerView recyclerViewNews = findViewById(R.id.recyclerView_news);
        recyclerViewNews.setAdapter(groupieAdapter);

        Section section = new Section();
        section.addAll(items);
        groupieAdapter.add(section);
    }
}