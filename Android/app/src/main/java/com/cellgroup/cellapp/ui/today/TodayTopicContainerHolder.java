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
import com.cellgroup.cellapp.ui.today.todayAllTopic.TodayAllTopicsAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodayTopicContainerHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private ViewHolderCallBackDelegate callBackDelegate;

    private WeakReference<List<Topic>> topics;
    private TodayAllTopicsAdapter adapter;

    private int type = 0;

    public TodayTopicContainerHolder(LayoutInflater inflater, ViewGroup parent, ViewHolderCallBackDelegate pCallBackDelegate, Context activity) {
        super(inflater.inflate(R.layout.list_today_all_topic_container, parent, false));
        callBackDelegate = pCallBackDelegate;

        recyclerView = itemView.findViewById(R.id.today_topic_container_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
    }

    public void bind(Context activity) {
        updateUI(activity);
    }

    private void updateUI(Context activity){
        if (adapter == null) {
            adapter = new TodayAllTopicsAdapter(activity);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}