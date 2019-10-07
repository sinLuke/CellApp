package com.cellgroup.cellapp.ui.today;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;

import androidx.recyclerview.widget.RecyclerView;

public class TodayGroupTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView groupTitle;

    public TodayGroupTitleHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate) {
        super(inflater.inflate(R.layout.list_group_title_setting, parent, false));
        groupTitle = itemView.findViewById(R.id.setting_group_title_text);
    }

    public void bind(String text, Context context) {
        if (text != null && text != "") {
            groupTitle.setText(text);
        } else {
            groupTitle.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        return;
    }
}