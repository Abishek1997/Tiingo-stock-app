package com.example.tiingostock.ui.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.tiingostock.R;
import java.util.Objects;

public class TradeSuccessDialogBox {
    public void showDialog(Activity activity, String ticker, String shares, int indicator){
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.trade_success_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tradeSuccessSummary = dialog.findViewById(R.id.trade_success_summary);
        String textToSet;
        if (indicator == 0){
            textToSet = "You have successfully bought " + shares + " shares of " + ticker;
        } else{
            textToSet = "You have successfully sold " + shares + " shares of  " + ticker;
        }
        tradeSuccessSummary.setText(textToSet);
        dialog.show();

        Button doneButton = dialog.findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}
