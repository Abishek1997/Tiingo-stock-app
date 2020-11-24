package com.example.tiingostock.ui.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tiingostock.R;
import com.example.tiingostock.network.pojos.StoredFavorites;
import com.example.tiingostock.ui.stockdetails.StockDetailsActivity;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class FavoritesRecyclerViewItem extends Item {

    private final StoredFavorites storedFavorites;
    private final Context context;

    public FavoritesRecyclerViewItem(StoredFavorites storedFavorites, Context context){
        this.storedFavorites = storedFavorites;
        this.context = context;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView textCompanyTicker = viewHolder.itemView.findViewById(R.id.favorites_text_company_ticker);
        TextView textCompanyName = viewHolder.itemView.findViewById(R.id.favorites_text_company_name);
        TextView textCompanyStockValue = viewHolder.itemView.findViewById(R.id.favorites_text_stock_value);
        ImageView imageCompanyChangeTrend = viewHolder.itemView.findViewById(R.id.favorites_image_value_change);
        TextView textCompanyStockValueChange = viewHolder.itemView.findViewById(R.id.favorites_text_company_stock_value_change);
        ImageView imageRight = viewHolder.itemView.findViewById(R.id.favorites_arrow);

        textCompanyTicker.setText(this.storedFavorites.getCompanyTicker());
        textCompanyName.setText(this.storedFavorites.getCompanyName());
        textCompanyStockValue.setText(String.format("%.2f", this.storedFavorites.getCompanyStockValue()));
        textCompanyStockValueChange.setText(String.format("%.2f $", this.storedFavorites.getCompanyStockValueChange()));

        if (this.storedFavorites.getCompanyStockValueChange() < 0){
            textCompanyStockValueChange.setTextColor(context.getColor(R.color.color_negative_change));
            imageCompanyChangeTrend.setImageResource(R.drawable.ic_baseline_trending_down_24);
        } else{
            textCompanyStockValueChange.setTextColor(context.getColor(R.color.color_positive_change));
            imageCompanyChangeTrend.setImageResource(R.drawable.ic_twotone_trending_up_24);
        }

        imageRight.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra("ticker", storedFavorites.getCompanyTicker());
            context.startActivity(intent);
        });
    }

    @Override
    public int getLayout() {
        return R.layout.favorites_recycler_item;
    }
}
