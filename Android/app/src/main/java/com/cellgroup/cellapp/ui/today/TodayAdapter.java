package com.cellgroup.cellapp.ui.today;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Topic;
import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;
import com.cellgroup.cellapp.ui.today.document.StepPagerActivity;
import com.cellgroup.cellapp.ui.today.todayAllTopic.TodayAllTopicsTopicItemHolder;
import com.cellgroup.cellapp.ui.today.topic.TopicActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderCallBackDelegate {

    private boolean mShouldRecieveUserInput;
    private Context activity;
    private Context getActivity(){
        return activity;
    }
    private List<Topic> topics;
    private List<Doc> docs;

    public TodayAdapter(Context activity){
        this.activity = activity;
    }

    public void onUpdateData(){
        this.topics = DataManager.shared.getAllTopics();
        this.docs = DataManager.shared.getSortedDocuments(true);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.topics = DataManager.shared.getAllTopics();
        this.docs = DataManager.shared.getSortedDocuments(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        switch (viewType) {
            case 0:
                return new TodayGroupTitleHolder(layoutInflater, parent, this);
            case 1:
                return new TodayItemHolder(layoutInflater, parent, this);
            default:
                return new TodayTopicContainerHolder(layoutInflater, parent, this, activity);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass().isAssignableFrom(TodayItemHolder.class)) {
            TodayItemHolder todayItemHolder = (TodayItemHolder) holder;
            Doc doc = docs.get(position - 3);
            todayItemHolder.bind(doc, getActivity());

        } else if (holder.getClass().isAssignableFrom(TodayGroupTitleHolder.class)) {
            TodayGroupTitleHolder todayGroupTitleHolder = (TodayGroupTitleHolder) holder;
            if (position == 0) {
                todayGroupTitleHolder.bind("Topics", getActivity());
            } else {
                todayGroupTitleHolder.bind("Recently Viewed", getActivity());
            }
        } else if (holder.getClass().isAssignableFrom(TodayTopicContainerHolder.class)) {
            TodayTopicContainerHolder todayTopicContainerHolder = (TodayTopicContainerHolder) holder;
            todayTopicContainerHolder.bind(activity);
        }
        mShouldRecieveUserInput = true;
    }

    @Override
    public int getItemCount() {
        return 1 + docs.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 2) {
            return 0;
        } else if (position == 1){
            return 2;
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
