package com.example.tiingostock.ui.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.example.tiingostock.R;

import java.util.Objects;

public class TradeDialogBox {

    public void showDialog(Activity activity, String companyName, String companyTicker, Double stockValue) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.trade_dialog_box);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tradeDialogTitleText = dialog.findViewById(R.id.trade_dialog_title);
        TextView tradeDialogAmountText = dialog.findViewById(R.id.trade_dialog_available_cost);

        String titleToSet = "Trade " + companyName + " shares";
        tradeDialogTitleText.setText(titleToSet);

        String amountTextToSet = "$20000.00 available to buy " + companyTicker;
        tradeDialogAmountText.setText(amountTextToSet);

        dialog.show();
    }
}
