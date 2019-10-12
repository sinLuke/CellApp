package com.cellgroup.cellapp.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cellgroup.cellapp.R;

public class TodayFragment extends Fragment {

    private RecyclerView todayRecyclerView;
    private TodayAdapter adapter;
    private boolean mShouldRecieveUserInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        todayRecyclerView = v.findViewById(R.id.today_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        todayRecyclerView.setLayoutManager(layoutManager);
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
            adapter = new TodayAdapter(getActivity());
            todayRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}