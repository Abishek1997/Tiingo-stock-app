package com.example.tiingostock.ui.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiingostock.R;
import com.example.tiingostock.network.pojos.StoredFavorites;
import com.example.tiingostock.ui.stockdetails.StockDetailsActivityViewModel;
import com.google.gson.Gson;

import java.util.Objects;

public class TradeDialogBox {
    public void showDialog(Activity activity, String companyName, String companyTicker, Double stockValue, Double stockValueChange, StockDetailsActivityViewModel stockDetailsActivityViewModel) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(("portfolio_amount"), Context.MODE_PRIVATE);
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        StoredFavorites portfolioItem = new StoredFavorites();
        Gson gson = new Gson();

        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.trade_dialog_box);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tradeDialogTitleText = dialog.findViewById(R.id.trade_dialog_title);
        TextView tradeDialogAmountText = dialog.findViewById(R.id.trade_dialog_available_cost);
        TextView tradeDialogSharePrice = dialog.findViewById(R.id.trade_dialog_share_price_text);
        EditText tradeStockCount = dialog.findViewById(R.id.trade_dialog_edit_text);
        Button buyButton = dialog.findViewById(R.id.button_buy);
        Button sellButton = dialog.findViewById(R.id.button_sell);
        TradeSuccessDialogBox successDialogBox = new TradeSuccessDialogBox();

        String titleToSet = "Trade " + companyName + " shares";
        tradeDialogTitleText.setText(titleToSet);

        final double[] availableAmount = {Double.parseDouble(sharedPreferences.getString("portfolio_amount", ""))};
        final double[] currentAmount = {0.0};
        final double[] currentShares = {0};

        String textToSet = currentShares[0] + " x " + "$ " + stockValue.toString() + "/share = $" + 0 * stockValue;
        tradeDialogSharePrice.setText(textToSet);

        tradeStockCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textToSet;
                if (s.toString().equalsIgnoreCase("")){
                    currentShares[0] = 0;

                } else{
                    currentShares[0] = Double.parseDouble(s.toString());

                }
                textToSet = currentShares[0] + " x " + "$ " + stockValue.toString() + "/share = $" + String.format("%.2f", currentShares[0] * stockValue);
                tradeDialogSharePrice.setText(textToSet);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        @SuppressLint("DefaultLocale") String amountTextToSet = "$" + String.format("%.2f", availableAmount[0]) + " available to buy " + companyTicker;
        tradeDialogAmountText.setText(amountTextToSet);

        buyButton.setOnClickListener(v -> {
            if (currentShares[0] > 0){
                currentAmount[0] = currentShares[0] * stockValue;
                if ( availableAmount[0] < currentAmount[0]) {
                    Toast.makeText(activity, "Not enough money to buy", Toast.LENGTH_SHORT).show();
                } else{
                    editor[0] = sharedPreferences.edit();
                    availableAmount[0] = availableAmount[0] - currentAmount[0];

                    portfolioItem.setCompanyTicker(companyTicker);
                    portfolioItem.setCompanyName(companyName);
                    portfolioItem.setCompanyStockValue(stockValue);
                    portfolioItem.setCompanyStockValueChange(stockValueChange);
                    portfolioItem.setShares(currentShares[0]);

                    editor[0].putString("portfolio_amount", String.valueOf(availableAmount[0]));
                    editor[0].putString(companyTicker, gson.toJson(portfolioItem));

                    editor[0].apply();

                    successDialogBox.showDialog(activity, companyTicker, String.valueOf(currentShares[0]), 0);
                    stockDetailsActivityViewModel.setPortfolioValues(currentAmount[0], currentShares[0]);
                    dialog.dismiss();
                }
            } else{
                Toast.makeText(activity, "Cannot buy 0 or less shares", Toast.LENGTH_SHORT).show();
            }
        });

        sellButton.setOnClickListener(v -> {
            if (currentShares[0] > 0){
                double availableShares = 0;
                if (sharedPreferences.contains(companyTicker)){
                     availableShares = gson.fromJson(sharedPreferences.getString(companyTicker, ""), StoredFavorites.class).getShares();
                }
                currentAmount[0] = currentShares[0] * stockValue;
                if (currentShares[0] > availableShares){
                    Toast.makeText(activity, "Not enough shares to sell", Toast.LENGTH_SHORT).show();
                } else{
                    editor[0] = sharedPreferences.edit();
                    double remainingShares = availableShares - currentShares[0];
                    if (remainingShares == 0) {
                        editor[0].remove(companyTicker);
                        editor[0].apply();
                    } else{
                        portfolioItem.setCompanyTicker(companyTicker);
                        portfolioItem.setCompanyName(companyName);
                        portfolioItem.setCompanyStockValue(stockValue);
                        portfolioItem.setCompanyStockValueChange(stockValueChange);
                        portfolioItem.setShares(remainingShares);
                    }
                    availableAmount[0] = availableAmount[0] + currentAmount[0];
                    editor[0].putString("portfolio_amount", String.valueOf(availableAmount[0]));
                    editor[0].putString(companyTicker, gson.toJson(portfolioItem));
                    editor[0].apply();

                    successDialogBox.showDialog(activity, companyTicker, String.valueOf(currentShares[0]), 1);
                    stockDetailsActivityViewModel.setPortfolioValues(remainingShares * stockValue, remainingShares);
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(activity, "Cannot sell 0 or less shares", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
