package com.cellgroup.cellapp.ui.today.document;

import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cellgroup.cellapp.AppState;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.RootActivity;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.sharedUI.ViewPagerDelegate;
import com.cellgroup.cellapp.ui.sharedUI.ViewPagerWithDelegate;
import com.google.android.material.snackbar.Snackbar;

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

public class StepPagerActivity extends RootActivity implements ViewPagerDelegate, StepFragmentDelegate {

    private ViewPagerWithDelegate viewPager;
    private List<Step> steps;

    private Button nextButton;
    private Button prevButton;
    private TextView stepNumberText;
    private String thisAnswer;

    @Override
    public boolean pageViewShouldContinueScrolling(ViewPagerWithDelegate viewPager) {
        int current = viewPager.getCurrentItem();
        return !(steps.get(current).isQuiz() || steps.get(current).isAnimation());
    }

    @Override
    public void didFinishStepCorrectly(Step step) {
        AppState.shared.setStepFinished(step);
        if (step.isQuiz() || step.animationItems.size() > 0) {
            StepPagerActivity.this.nextButton.setVisibility(View.VISIBLE);
            StepPagerActivity.this.nextButton.setBackground(getResources().getDrawable(R.drawable.bk_corner_radius_blue));
            StepPagerActivity.this.nextButton.setTextColor(getResources().getColor(R.color.label_dark));
        }
    }

    private void goToNextStep(){
        final int newPage = viewPager.getCurrentItem() + 1;
        if (newPage >= steps.size()) {

            CharSequence text;

            text = AppState.shared.getCurrentDoc().DOCUMENT_NAME + " Complete!";
            AppState.shared.setDocFinished(AppState.shared.getCurrentDoc());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    StepPagerActivity.this.finish();
                }
            }, 1000);
            Snackbar sb = Snackbar.make(viewPager, text, Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(getResources().getColor(R.color.systemYellow, null));
            sb.show();
            StepPagerActivity.this.nextButton.setEnabled(false);
            StepPagerActivity.this.prevButton.setEnabled(false);
        }
        viewPager.setCurrentItem(newPage);
    }

    public void updateButtons(int position){

        Step step = steps.get(position);

        if (position == step.PAGE_NUMBER) {
            stepNumberText.setText((position + 1) + "/" + step.doc.get().steps.size());
        } else {
            stepNumberText.setText("(" + step.PAGE_NUMBER + ") " + (position + 1) + "/" + step.doc.get().steps.size());
        }

        StepPagerActivity.this.nextButton.setVisibility(View.VISIBLE);
        StepPagerActivity.this.prevButton.setVisibility(View.VISIBLE);
        StepPagerActivity.this.prevButton.setBackground(getResources().getDrawable(R.drawable.bk_corner_white_fill));
        StepPagerActivity.this.prevButton.setTextColor(getResources().getColor(R.color.label));

        if (steps.get(position).isQuiz() || steps.get(position).isAnimation()) {
            StepPagerActivity.this.nextButton.setVisibility(View.GONE);
        }

        if (position == 0) {
            StepPagerActivity.this.prevButton.setVisibility(View.GONE);
        }

        if (position == steps.size() - 1) {
            StepPagerActivity.this.nextButton.setBackground(getResources().getDrawable(R.drawable.bk_corner_radius_blue));
            StepPagerActivity.this.nextButton.setText(R.string.step_button_next_finish);
            StepPagerActivity.this.nextButton.setTextColor(getResources().getColor(R.color.label_dark));
        } else {
            StepPagerActivity.this.nextButton.setBackground(getResources().getDrawable(R.drawable.bk_corner_white_fill));
            StepPagerActivity.this.nextButton.setText(R.string.step_button_next);
            StepPagerActivity.this.nextButton.setTextColor(getResources().getColor(R.color.label));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_pager);

        viewPager = findViewById(R.id.step_view_page);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        stepNumberText = findViewById(R.id.step_number_text);

        steps = new ArrayList<>(AppState.shared.getCurrentDoc().steps.values());

        viewPager.delegate = this;

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

        int initialPosition = 0;

        Step lastViewedStep = AppState.shared.getCurrentDoc().getLastViewStep();
        if (lastViewedStep != null) {
            initialPosition = steps.indexOf(lastViewedStep) + 1;
            if (initialPosition >= steps.size() || initialPosition < 0) {
                initialPosition = 0;
            }
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Step step = steps.get(position);
                StepFragment sf = StepFragment.newInstance(step);
                sf.delegate = StepPagerActivity.this;
                return sf;
            }



            @Override
            public int getCount() {
                return steps.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPagerWithDelegate.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateButtons(position);
                AppState.shared.setCurrentStep(steps.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                StepPagerActivity.this.nextButton.setEnabled(state == 0);
                StepPagerActivity.this.prevButton.setEnabled(state == 0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextStep();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPage = viewPager.getCurrentItem() - 1;
                if (newPage >= 0 && newPage < steps.size()) {
                    viewPager.setCurrentItem(newPage);
                }
            }
        });

        Step currentStep = AppState.shared.getCurrentStep();

        if (currentStep == null || currentStep.doc.get() != AppState.shared.getCurrentDoc()) {
            updateButtons(initialPosition);
            viewPager.setCurrentItem(initialPosition);
        } else {
            int position = steps.indexOf(currentStep);
            updateButtons(position);
            viewPager.setCurrentItem(position);
        }
    }
}
