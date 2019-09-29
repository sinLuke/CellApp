package com.cellgroup.cellapp.ui.progress;

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

import com.cellgroup.cellapp.R;

public class ProgressFragment extends Fragment {

    private ProgressModel mProgressModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mProgressModel =
                ViewModelProviders.of(this).get(ProgressModel.class);
        View root = inflater.inflate(R.layout.fragment_progress, container, false);
        final TextView textView = root.findViewById(R.id.text_progress);
        mProgressModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}