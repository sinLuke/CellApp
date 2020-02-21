package com.cellgroup.cellapp.ui.today.todayAllTopic;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.ColorLong;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodayAllTopicsTopicItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mainTextView;
    private TextView secondaryTextView;
    private ImageView topicImage;
    private ViewHolderCallBackDelegate callBackDelegate;

    private WeakReference<Topic> topic;

    private int type = 0;

    public TodayAllTopicsTopicItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.list_today_topic_item, parent, false));
        callBackDelegate = pCallBackDelegate;

        mainTextView = itemView.findViewById(R.id.today_topic_item_main_title);
        secondaryTextView = itemView.findViewById(R.id.today_topic_item_secondary_title);
        topicImage = itemView.findViewById(R.id.image_today_topic_item);

        itemView.setOnClickListener(this);

    }

    public void bind(Topic pTopic, Context activity) {
        itemView.setBackgroundColor(Color.TRANSPARENT);
        this.topic = new WeakReference<>(pTopic);
        mainTextView.setText(pTopic.TOPIC_NAME);
        secondaryTextView.setText(String.format("%d Documents", pTopic.docs.size()));
        if (pTopic.IMAGE_URL != null) {
            Picasso
                    .get()
                    .load(pTopic.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(topicImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                }
            });

            Picasso.Builder builder = new Picasso.Builder(activity);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        if (callBackDelegate.holderShouldRecieveUserInput()) {
            itemView.setBackgroundColor(Color.LTGRAY);
            if (this.topic != null && this.topic.get() != null) {
                callBackDelegate.holderOnClickSendingObject(this, this.topic.get());
            }
        }
    }
}
