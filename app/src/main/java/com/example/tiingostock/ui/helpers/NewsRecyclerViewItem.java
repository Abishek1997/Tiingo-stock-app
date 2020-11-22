package com.example.tiingostock.ui.helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tiingostock.R;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class NewsRecyclerViewItem  extends Item{

    private final String imageURL;
    private final String source;
    private final String timeLine;
    private final String title;
    private final Context context;
    private final String url;

    public NewsRecyclerViewItem(
        String imageURL, String source, String timeLine, String title, Context context, String url
    ){
        this.imageURL = imageURL;
        this.source = source;
        this.timeLine = timeLine;
        this.title = title;
        this.context = context;
        this.url = url;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        //TODO: Change time of news source to appropriate format
        TextView textNewsSource = viewHolder.itemView.findViewById(R.id.text_rv_news_source_value);
        TextView textNewsTitle = viewHolder.itemView.findViewById(R.id.text_rv_news_title);
        TextView textNewsTimeline = viewHolder.itemView.findViewById(R.id.text_rv_timeline_value);
        ImageView imageNews = viewHolder.itemView.findViewById(R.id.image_rv_news);
        CardView recyclerMainCard = viewHolder.itemView.findViewById(R.id.recycler_main_card);

        recyclerMainCard.setOnLongClickListener(v -> {
            DialogBox dialogBox = new DialogBox();
            dialogBox.showDialog((Activity) context, imageURL, title, url);
            return false;
        });
        textNewsSource.setText(this.source);
        textNewsTitle.setText(this.title);
        textNewsTimeline.setText(this.timeLine);

        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(imageURL).apply(requestOptions).into(imageNews);
    }

    @Override
    public int getLayout() {
        return R.layout.news_recycler_view_item;
    }
}
