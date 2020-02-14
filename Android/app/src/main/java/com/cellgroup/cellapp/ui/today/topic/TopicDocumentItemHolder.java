package com.cellgroup.cellapp.ui.today.topic;

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
import com.cellgroup.cellapp.models.DocumentCompleteRate;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.cellgroup.cellapp.ui.today.todayAllTopic.TodayAllTopicsAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicDocumentItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mainTextView;
    private TextView secondaryTextView;
    private ImageView documentImage;
    private ViewHolderCallBackDelegate callBackDelegate;

    private WeakReference<Doc> doc;

    public TopicDocumentItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.cell_topic_document_item, parent, false));
        callBackDelegate = pCallBackDelegate;

        mainTextView = itemView.findViewById(R.id.topic_doc_item_main_title);
        secondaryTextView = itemView.findViewById(R.id.topic_doc_item_secondary_title);
        documentImage = itemView.findViewById(R.id.image_topic_document_item);
        itemView.setOnClickListener(this);
    }

    public void bind(Doc doc, Context activity) {
        DocumentCompleteRate rate = doc.getCompletionRate();
        mainTextView.setText(activity.getText(R.string.setting_change_display_name));
        secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());

        itemView.setBackgroundColor(Color.TRANSPARENT);

        if (doc != null) {
            this.doc = new WeakReference<>(doc);
            mainTextView.setText(doc.DOCUMENT_NAME);
            secondaryTextView.setText(String.format("%d Steps, %d%% Complete", doc.steps.size(), (int) (rate.rate*100)));
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
    public void onClick(View view) {
        if (callBackDelegate.holderShouldRecieveUserInput()) {
            itemView.setBackgroundColor(Color.LTGRAY);
            if (this.doc != null && this.doc.get() != null) {
                callBackDelegate.holderDidCallSendingObject(this, this.doc.get());
            }
        }
    }
}
