package com.cellgroup.cellapp.ui.today.todayAllTopic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.cellgroup.cellapp.ui.today.TodayGroupTitleHolder;
import com.cellgroup.cellapp.ui.today.TodayItemHolder;
import com.cellgroup.cellapp.ui.today.document.StepPagerActivity;
import com.cellgroup.cellapp.ui.today.topic.TopicActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class TodayAllTopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderCallBackDelegate {

    private boolean mShouldRecieveUserInput;
    private List<Topic> topics;
    private Context activity;
    private Context getActivity(){
        return activity;
    }

    public TodayAllTopicsAdapter(Context activity){
        this.activity = activity;
        this.topics = DataManager.shared.getAllTopics();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        return new TodayAllTopicsTopicItemHolder(layoutInflater, parent, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TodayAllTopicsTopicItemHolder todayAllTopicsTopicItemHolder = (TodayAllTopicsTopicItemHolder) holder;
        Topic topic = topics.get(position);
        todayAllTopicsTopicItemHolder.bind(topic, getActivity());
        mShouldRecieveUserInput = true;
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    @Override
    public void holderDidCallFromItemPosition(RecyclerView.ViewHolder holder, int position) {
        return;
    }

    @Override
    public void holderDidCallSendingObject(RecyclerView.ViewHolder holder, Object object) {
        if (object.getClass().isAssignableFrom(Doc.class)) {
            Doc doc = (Doc) object;
            AppState.shared.setCurrentDoc(doc);
            Intent i = new Intent(getActivity(), StepPagerActivity.class);
            getActivity().startActivity(i);
        } else if (object.getClass().isAssignableFrom(Topic.class)) {
            Topic topic = (Topic) object;
            AppState.shared.setCurrentTopic(topic);
            Intent i = new Intent(getActivity(), TopicActivity.class);
            getActivity().startActivity(i);
        }
        mShouldRecieveUserInput = false;
    }

    @Override
    public boolean holderShouldRecieveUserInput() {
        return mShouldRecieveUserInput;
    }
}
