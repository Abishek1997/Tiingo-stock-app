package com.example.tiingostock.ui.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiingostock.R;

import java.util.Objects;

public class DialogBox {

    public void showDialog(Activity activity, String imageURL, String title, String url) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_box);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView imgDialog = dialog.findViewById(R.id.image_dialog);
        TextView textDialog = dialog.findViewById(R.id.title_dialog);

        textDialog.setText(title);
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(activity).load(imageURL).apply(requestOptions).into(imgDialog);

        LinearLayout mDialogNo = dialog.findViewById(R.id.linearLayout_twitter);
        mDialogNo.setOnClickListener(v -> {
            String urlToSet = "https://twitter.com/intent/tweet?text=Check out this link: " + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToSet));
            activity.startActivity(browserIntent);
            dialog.cancel();
        });

        LinearLayout mDialogOk = dialog.findViewById(R.id.linearLayout_chrome);
        mDialogOk.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(browserIntent);
            dialog.cancel();
        });

        dialog.show();
    }
}
