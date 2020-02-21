package com.cellgroup.cellapp.ui.today.topic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.ui.login.InitializeUserFragment;

public class TopicFragment extends Fragment {

    private RecyclerView topicRecyclerView;
    private TopicAdapter adapter;
    private boolean mShouldRecieveUserInput;

    public static TopicFragment newInstance(Context activity) {
        TopicFragment fragment = new TopicFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        topicRecyclerView = v.findViewById(R.id.today_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);

        topicRecyclerView.setLayoutManager(layoutManager);
        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mShouldRecieveUserInput = true;
        updateUI();
    }

    private void updateUI(){
        if (adapter == null) {
            adapter = new TopicAdapter(getActivity());
            topicRecyclerView.setAdapter(adapter);
            getActivity().setTitle(AppState.shared.getCurrentTopic().TOPIC_NAME);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}