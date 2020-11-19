package com.example.tiingostock.ui.homepage;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiingostock.R;
import com.example.tiingostock.network.connectivity.StockDataSourceImpl;
import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;
import com.example.tiingostock.repository.StockRepositoryImpl;

import java.util.List;

public class HomePageActivity extends AppCompatActivity{

    TextView textFooter;
    private final TiingoAPIRetrofitService tiingoAPIRetrofitService = new TiingoAPIRetrofitService();
    private final StockDataSourceImpl stockDataSource = new StockDataSourceImpl(tiingoAPIRetrofitService);
    private final StockRepositoryImpl stockRepository = new StockRepositoryImpl(this, stockDataSource);
    private final HomePageActivityViewModelFactory viewModelFactory = new HomePageActivityViewModelFactory(stockRepository);
    private HomePageActivityViewModel viewModel;
    private LiveData<List<AutocompleteResponseItem>> autocompleteDataObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textFooter = (TextView)findViewById(R.id.text_footer);
        viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(HomePageActivityViewModel.class);
        bindUI();
    }

    public void bindUI(){
        setTypeFaceForTextView();
        textFooter.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiingo.com/"));
            startActivity(browserIntent);
        });
    }

    public void setTypeFaceForTextView(){
        TextView dateTextView = (TextView)findViewById(R.id.date_text_view);
        dateTextView.setTypeface(null, Typeface.BOLD);
        TextView titlePortfolio = (TextView)findViewById(R.id.title_portfolio);
        titlePortfolio.setTypeface(null, Typeface.BOLD);
        TextView valueNetworth = (TextView)findViewById(R.id.value_networth);
        valueNetworth.setTypeface(null, Typeface.BOLD);
        TextView titleFavorites = (TextView)findViewById(R.id.title_favorites);
        titleFavorites.setTypeface(null, Typeface.BOLD);
        textFooter.setTypeface(null, Typeface.ITALIC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_item, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SimpleCursorAdapter suggestionAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0
        );
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null){
                    String[] sAutocompleteColNames = new String[] {
                            BaseColumns._ID,
                            SearchManager.SUGGEST_COLUMN_TEXT_1
                    };
                    if (!newText.isEmpty()){
                         Observer<List<AutocompleteResponseItem>> nameObserver = (Observer<List<AutocompleteResponseItem>>) data -> {
                            MatrixCursor cursor = new MatrixCursor(sAutocompleteColNames);
                            for (int i = 0; i < Math.min(5, data.size()); i++){
                                String term = data.get(i).getTicker() + " - " + data.get(i).getName();
                                String[] row = new String[]{String.valueOf(i), term};
                                cursor.addRow(row);
                            }
                            searchView.getSuggestionsAdapter().changeCursor(cursor);
                        };

                        autocompleteDataObserver = viewModel.getAutocompleteData(newText);
                        autocompleteDataObserver.observe(HomePageActivity.this, nameObserver);

                    } else {
                        searchView.getSuggestionsAdapter().changeCursor(null);
                    }
                }
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String term = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                cursor.close();
                searchView.setQuery(term, false);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return onSuggestionSelect(position);
            }
        });
        return true;
    }
}