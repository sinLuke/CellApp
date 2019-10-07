package com.cellgroup.cellapp.ui.today;

import android.content.Context;
import android.graphics.Color;
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

import java.lang.ref.WeakReference;

import androidx.recyclerview.widget.RecyclerView;

public class TodayItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mainTextView;
    private TextView secondaryTextView;
    private TextView middleTextView;
    private ImageView disclosureImage;
    private ViewHolderCallBackDelegate callBackDelegate;

    private WeakReference<Doc> doc;
    private WeakReference<Topic> topic;

    private int type = 0;

    public TodayItemHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.list_item_setting, parent, false));
        callBackDelegate = pCallBackDelegate;

        mainTextView = itemView.findViewById(R.id.setting_list_main_title);
        secondaryTextView = itemView.findViewById(R.id.setting_list_secondary_title);
        middleTextView = itemView.findViewById(R.id.setting_list_middle_title);
        disclosureImage = itemView.findViewById(R.id.image_disclosure);
        itemView.setOnClickListener(this);
    }

    public void bind(Doc doc, Topic topic, Context activity) {
        setType(0);
        mainTextView.setText(activity.getText(R.string.setting_change_display_name));
        secondaryTextView.setText(UserManager.getCurrentUser().getDisplayName());

        itemView.setBackgroundColor(Color.TRANSPARENT);

        if (doc != null) {
            this.doc = new WeakReference<>(doc);
            mainTextView.setText(doc.DOCUMENT_NAME);
            secondaryTextView.setText(String.format("%d Steps", doc.steps.size()));
        }

        if (topic != null) {
            this.topic = new WeakReference<>(topic);
            mainTextView.setText(topic.TOPIC_NAME);
            secondaryTextView.setText(String.format("%d Documents", topic.docs.size()));
        }
    }

    private void setType(int type) {
        mainTextView.setText("");
        secondaryTextView.setText("");
        middleTextView.setText("");
        if (type == 0) {
            mainTextView.setVisibility(View.VISIBLE);
            secondaryTextView.setVisibility(View.VISIBLE);
            middleTextView.setVisibility(View.GONE);
            disclosureImage.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            mainTextView.setVisibility(View.VISIBLE);
            secondaryTextView.setVisibility(View.GONE);
            middleTextView.setVisibility(View.GONE);
            disclosureImage.setVisibility(View.VISIBLE);
        } else {
            mainTextView.setVisibility(View.GONE);
            secondaryTextView.setVisibility(View.GONE);
            middleTextView.setVisibility(View.VISIBLE);
            disclosureImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (callBackDelegate.holderShouldRecieveUserInput()) {
            itemView.setBackgroundColor(Color.GRAY);
            if (this.doc != null && this.doc.get() != null) {
                callBackDelegate.holderDidCallSendingObject(this, this.doc.get());
            } else if (this.topic != null && this.topic.get() != null) {
                callBackDelegate.holderDidCallSendingObject(this, this.topic.get());
            }
        }
    }
}