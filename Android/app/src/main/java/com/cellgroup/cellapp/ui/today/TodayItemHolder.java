package com.cellgroup.cellapp.ui.today;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import androidx.recyclerview.widget.RecyclerView;

public class TodayItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mainTextView;
    private TextView secondaryTextView;
    private ImageView documentImage;
    private ViewHolderCallBackDelegate callBackDelegate;

    private WeakReference<Doc> doc;
    private WeakReference<Topic> topic;

    public TodayItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.cell_today_recent_document_item, parent, false));
        callBackDelegate = pCallBackDelegate;

        mainTextView = itemView.findViewById(R.id.today_recent_doicument_main_title);
        secondaryTextView = itemView.findViewById(R.id.today_recent_doicument_secondary_title);
        documentImage = itemView.findViewById(R.id.image_today_recent_document);
        itemView.setOnClickListener(this);
    }

    public void bind(Topic topic, Context activity) {
        mainTextView.setText(activity.getText(R.string.setting_change_display_name));
        if (UserManager.getCurrentUser() != null) {
            secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());
        }

        itemView.setBackgroundColor(Color.TRANSPARENT);
        if (topic != null) {
            this.topic = new WeakReference<>(topic);
            mainTextView.setText(topic.TOPIC_NAME);
            secondaryTextView.setText(String.format("%d Documents", topic.docs.size()));
        }

        if (topic.IMAGE_URL != null) {
            Picasso
                    .get()
                    .load(topic.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(documentImage, new Callback() {
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
    public void bind(Doc doc, Context activity) {
        mainTextView.setText(activity.getText(R.string.setting_change_display_name));
        secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());
        itemView.setBackgroundColor(Color.TRANSPARENT);
        if (doc != null) {
            this.doc = new WeakReference<>(doc);
            mainTextView.setText(doc.DOCUMENT_NAME);
            secondaryTextView.setText(String.format("%d Steps", doc.steps.size()));
        }

        if (doc.IMAGE_URL != null) {
            Picasso
                    .get()
                    .load(doc.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(documentImage, new Callback() {
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
            if (this.doc != null && this.doc.get() != null) {
                callBackDelegate.holderOnClickSendingObject(this, this.doc.get());
            } else if (this.topic != null && this.topic.get() != null) {
                callBackDelegate.holderOnClickSendingObject(this, this.topic.get());
            }
        }
    }
}