package com.cellgroup.cellapp.ui.today.topic;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.cellgroup.cellapp.ui.today.TodayGroupTitleHolder;
import com.cellgroup.cellapp.ui.today.TodayItemHolder;
import com.cellgroup.cellapp.ui.today.document.StepPagerActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderCallBackDelegate {

    private boolean mShouldRecieveUserInput;
    private Context activity;
    private Context getActivity(){
        return activity;
    }
    private Topic topic;

    public TopicAdapter(Context activity){
        this.activity = activity;
        this.topic = AppState.shared.getCurrentTopic();
    }

    public void prepareFragment(Topic topic){
        this.topic = topic;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        if (viewType == 1) {
            return new TodayItemHolder(layoutInflater, parent, this);
        } else {
            return new TodayGroupTitleHolder(layoutInflater, parent, this);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass().isAssignableFrom(TodayItemHolder.class)) {
            TodayItemHolder todayItemHolder = (TodayItemHolder) holder;
            Doc doc = new ArrayList<Doc>(topic.docs.values()).get(position - 1);
            todayItemHolder.bind(doc, null, getActivity());

        } else if (holder.getClass().isAssignableFrom(TodayGroupTitleHolder.class)) {
            TodayGroupTitleHolder todayGroupTitleHolder = (TodayGroupTitleHolder) holder;
            todayGroupTitleHolder.bind(topic.TOPIC_NAME, getActivity());
        }
        mShouldRecieveUserInput = true;
    }

    @Override
    public int getItemCount() {
        return topic.docs.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
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
            Log.d("TopicAdapter", "holderDidCallSendingObject");
            getActivity().startActivity(i);
        } else if (object.getClass().isAssignableFrom(Topic.class)) {
            Topic topic = (Topic) object;
        }
        mShouldRecieveUserInput = false;
    }

    @Override
    public boolean holderShouldRecieveUserInput() {
        return mShouldRecieveUserInput;
    }
}
