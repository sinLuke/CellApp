package com.cellgroup.cellapp.ui.setting;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.ViewHolderCallBackDelegate;

import java.util.Arrays;


public class SettingFragment extends Fragment {

    private RecyclerView settingRecyclerView;
    private SettingAdapter adapter;
    private boolean mShouldRecieveUserInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        settingRecyclerView = v.findViewById(R.id.setting_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        settingRecyclerView.setLayoutManager(layoutManager);
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
            adapter = new SettingAdapter(getActivity());
            settingRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}