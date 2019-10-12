package com.cellgroup.cellapp.ui.today.document;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Step;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class StepPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Step> steps;

    private Button nextButton;
    private Button prevButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_pager);

        viewPager = findViewById(R.id.step_view_page);
        steps = new ArrayList<>(AppState.shared.getCurrentDoc().steps.values());

        setTitle(AppState.shared.getCurrentDoc().DOCUMENT_NAME);

        steps.sort(new Comparator<Step>() {
            @Override
            public int compare(Step step, Step t1) {
                if (step.PAGE_NUMBER > t1.PAGE_NUMBER) {
                    return 1;
                }
                if (step.PAGE_NUMBER < t1.PAGE_NUMBER) {
                    return -1;
                }
                return 0;
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Step step = steps.get(position);
                return StepFragment.newInstance(step);
            }

            @Override
            public int getCount() {
                return steps.size();
            }
        });

    }


}
