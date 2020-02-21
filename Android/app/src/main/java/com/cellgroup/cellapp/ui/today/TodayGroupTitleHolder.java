package com.cellgroup.cellapp.ui.today;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;

import androidx.recyclerview.widget.RecyclerView;

public class TodayGroupTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView groupTitle;
    private Button showAllButton;
    private ViewHolderCallBackDelegate callBackDelegate;

    public TodayGroupTitleHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.list_group_large_title, parent, false));
        groupTitle = itemView.findViewById(R.id.setting_group_title_text);
        showAllButton = itemView.findViewById(R.id.setting_group_show_all_button);
        callBackDelegate = pCallBackDelegate;
    }

    public void bind(String text, boolean isHidden, boolean isShowAll, Context context) {
        if (text != null && text != "") {
            groupTitle.setText(text);
        } else {
            groupTitle.setText("");
        }

        if (isHidden) {
            showAllButton.setVisibility(View.INVISIBLE);
        } else {
            showAllButton.setVisibility(View.VISIBLE);
        }

        if (isShowAll) {
            showAllButton.setText("Show All");
        } else {
            showAllButton.setText("Collapse");
        }

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackDelegate.holderOnClickFromItemPosition(TodayGroupTitleHolder.this, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        return;
    }
}