package com.example.tiingostock.ui.homepage;

//TODO: Change app logo, name and implement Splash screen

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiingostock.R;
import com.example.tiingostock.network.connectivity.StockDataSourceImpl;
import com.example.tiingostock.network.pojos.AutocompleteResponseItem;
import com.example.tiingostock.network.pojos.StoredFavorites;
import com.example.tiingostock.network.retrofit.TiingoAPIRetrofitService;
import com.example.tiingostock.repository.StockRepositoryImpl;
import com.example.tiingostock.ui.helpers.FavoritesRecyclerViewItem;
import com.example.tiingostock.ui.stockdetails.StockDetailsActivity;
import com.google.gson.Gson;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.Section;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

@SuppressWarnings("ALL")
public class HomePageActivity extends AppCompatActivity{

    TextView textFooter;
    private final TiingoAPIRetrofitService tiingoAPIRetrofitService = new TiingoAPIRetrofitService();
    private final StockDataSourceImpl stockDataSource = new StockDataSourceImpl(tiingoAPIRetrofitService);
    private final StockRepositoryImpl stockRepository = new StockRepositoryImpl(this, stockDataSource);
    private final HomePageActivityViewModelFactory viewModelFactory = new HomePageActivityViewModelFactory(stockRepository);
    private HomePageActivityViewModel viewModel;
    private LiveData<List<AutocompleteResponseItem>> autocompleteDataObserver;
    GroupAdapter groupieAdapter = new GroupAdapter();
    RecyclerView recyclerViewFavorites;
    Gson gson = new Gson();
    Section section = new Section();
    SharedPreferences.Editor editor;
    List<FavoritesRecyclerViewItem> favoritesRecyclerViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textFooter = findViewById(R.id.text_footer);
        viewModel = new ViewModelProvider(this, viewModelFactory)
                .get(HomePageActivityViewModel.class);
        SharedPreferences sharedPreferences = HomePageActivity.this.getSharedPreferences(("favorites"), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bindUI();

        recyclerViewFavorites = findViewById(R.id.recyclerview_favorites);
        recyclerViewFavorites.setAdapter(groupieAdapter);

        favoritesRecyclerViewItems = new ArrayList<>();
        sharedPreferences.getAll().forEach((item, value) -> {
            favoritesRecyclerViewItems.add(new FavoritesRecyclerViewItem(gson.fromJson((String) value, StoredFavorites.class), this));
        });
        editor.apply();
        section.addAll(favoritesRecyclerViewItems);
        groupieAdapter.add(section);
        setItemHelperRecyclerView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    public void setItemHelperRecyclerView(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(favoritesRecyclerViewItems, fromPosition, toPosition);
                groupieAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = groupieAdapter.getItem(viewHolder.getAdapterPosition());
                section.remove(item);
                TextView textViewToRemove = viewHolder.itemView.findViewById(R.id.favorites_text_company_ticker);
                editor.remove(textViewToRemove.getText().toString());
                editor.apply();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(HomePageActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.color_swipe_left))
                        .addActionIcon(R.drawable.delete_button)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlag = makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT);

                int swipeFlag = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE,
                        ItemTouchHelper.LEFT);

                return makeMovementFlags(dragFlag, swipeFlag);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewFavorites);
    }

    public void bindUI(){

        setTypeFaceForTextView();
        textFooter.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiingo.com/"));
            startActivity(browserIntent);
        });
        setDate();
    }

    public void setDate(){
        TextView dateTextView = findViewById(R.id.date_text_view);
        dateTextView.setTypeface(null, Typeface.BOLD);
        String currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
        dateTextView.setText(currentDate);
    }

    public void setTypeFaceForTextView(){
        TextView titlePortfolio = findViewById(R.id.title_portfolio);
        titlePortfolio.setTypeface(null, Typeface.BOLD);
        TextView valueNetworth = findViewById(R.id.value_networth);
        valueNetworth.setTypeface(null, Typeface.BOLD);
        TextView titleFavorites = findViewById(R.id.title_favorites);
        titleFavorites.setTypeface(null, Typeface.BOLD);
        textFooter.setTypeface(null, Typeface.ITALIC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_item, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
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
                Intent intent = new Intent(HomePageActivity.this, StockDetailsActivity.class);
                intent.putExtra("ticker", query.split(" ")[0]);
                startActivity(intent);
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
                         Observer<List<AutocompleteResponseItem>> namedObserver = data -> {
                            MatrixCursor cursor = new MatrixCursor(sAutocompleteColNames);
                            for (int i = 0; i < Math.min(5, data.size()); i++){
                                String term = data.get(i).getTicker() + " - " + data.get(i).getName();
                                String[] row = new String[]{String.valueOf(i), term};
                                cursor.addRow(row);
                            }
                            searchView.getSuggestionsAdapter().changeCursor(cursor);
                        };

                        autocompleteDataObserver = viewModel.getAutocompleteData(newText);
                        autocompleteDataObserver.observe(HomePageActivity.this, namedObserver);

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