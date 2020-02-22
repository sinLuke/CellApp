package com.cellgroup.cellapp.ui.today.document;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cellgroup.cellapp.Print;
import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.AnimationItem;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.network.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class StepFragment extends Fragment {

    private Step step;
    private DisplayMetrics metrics;
    private TextView stepText;
    private LinearLayout linearLayout;
    private ImageView stepImage;
    private List<Button> choiceButtons;
    private Map<String, TextView> animationItemTags;
    private Map<String, ImageView> animationItems;
    private Button submitButton;
    private EditText answerText;
    private TextView answerLabel;
    private TextView answerExplain;
    private RelativeLayout relativeCanvas;

    private float currentX = 0;
    private float currentY = 0;

    private boolean stepIsFinished;

    public StepFragmentDelegate delegate;

    public StepFragment(){

    }

    static public StepFragment newInstance(Step step) {
        StepFragment stepFragment = new StepFragment();
        stepFragment.step = step;
        return stepFragment;
    }

    private void setStepFinished(boolean yes) {
        stepIsFinished = yes;
        removeButtons();
        updateUI();
        delegate.didFinishStepCorrectly(step);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step, container, false);
        stepText = v.findViewById(R.id.step_text);
        stepImage = v.findViewById(R.id.step_image);
        submitButton = v.findViewById(R.id.submit_button);
        answerText = v.findViewById(R.id.step_answer_text);
        answerLabel = v.findViewById(R.id.step_answer_label);
        linearLayout = v.findViewById(R.id.step_layout);
        answerExplain = v.findViewById(R.id.step_answer_explain_text);
        relativeCanvas = v.findViewById(R.id.step_answer_interactive_canvas);
        metrics = getActivity().getResources().getDisplayMetrics();
        this.choiceButtons = new ArrayList<Button>();

        updateUI();
        return v;
    }

    private void answerQuestionBy(String answer){

        CharSequence text;

        int color;

        if (step.isAnswerCorrect(answer)) {
            text = "Correct!";

            setStepFinished(true);

            color = R.color.systemGreen;
        } else {

            text = answer + " is not correct, try again ...";
            color = R.color.systemRed;
        }

        Snackbar sb = Snackbar.make(linearLayout, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(getResources().getColor(color, null));
        sb.show();
        updateUI();
    }

    private void removeButtons(){
        if (this.choiceButtons != null) {
            for (Button b : this.choiceButtons) {
                b.setVisibility(View.GONE);
                linearLayout.removeView(b);
            }
            this.choiceButtons = new ArrayList<Button>();
        }
    }

    private void removeAnimatedItems(){
        if (this.animationItemTags != null) {
            for (TextView t : this.animationItemTags.values()) {
                t.setVisibility(View.GONE);
                linearLayout.removeView(t);
            }
            this.animationItemTags = new HashMap<String, TextView>();
        }

        if (this.animationItems != null) {
            for (ImageView i : this.animationItems.values()) {
                i.setVisibility(View.GONE);
                relativeCanvas.removeView(i);
            }
            this.animationItems = new HashMap<String, ImageView>();
        }
    }

    private void showAnimation() {
        ValueAnimator animation = ValueAnimator.ofFloat(0.0f, 1.0f);
        animation.setDuration(500);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float progress = (float) animation.getAnimatedValue();

                Print.print(progress);
                Print.print(StepFragment.this.animationItems.keySet());

                for (String id: StepFragment.this.animationItems.keySet()) {
                    ImageView i = StepFragment.this.animationItems.get(id);
                    AnimationItem item = StepFragment.this.step.animationItems.get(id);

                    if (item == null || i == null) {
                        continue;
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) i.getLayoutParams();
                    int imageStartX = (int) (item.START_POSITION_X* 0.01 * relativeCanvas.getWidth());
                    int imageStartY = (int) (item.START_POSITION_Y * 0.01 * relativeCanvas.getHeight());
                    int imageEndX = (int) (item.END_POSITION_X * 0.01 * relativeCanvas.getWidth());
                    int imageEndY = (int) (item.END_POSITION_Y * 0.01 * relativeCanvas.getHeight());
                    params.leftMargin = (int) (progress*imageEndX + (1 - progress)*imageStartX);
                    params.topMargin = (int) (progress*imageEndY + (1 - progress)*imageStartY);
                    i.setLayoutParams(params);
                }
            }
        });

        animation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                setStepFinished(true);
            }
        });
        animation.start();
    }

    private void updateAnimationItems(){
        for (final AnimationItem item : step.animationItems.values()) {

            AnimationItemImageView i = new AnimationItemImageView(getContext(), item, (float) relativeCanvas.getWidth(), (float) relativeCanvas.getHeight());

            int imageX;
            int imageY;

            if (stepIsFinished) {
                imageX = (int) (item.END_POSITION_X * 0.01 * relativeCanvas.getWidth());
                imageY = (int) (item.END_POSITION_Y * 0.01 * relativeCanvas.getHeight());
            } else {
                imageX = (int) (item.START_POSITION_X * 0.01 * relativeCanvas.getWidth());
                imageY = (int) (item.START_POSITION_Y * 0.01 * relativeCanvas.getHeight());
            }


            RelativeLayout.LayoutParams paramsForImage = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (stepImage.getHeight() * 0.01 * item.SIZE)

                    );

            i.setAdjustViewBounds(true);
            paramsForImage.leftMargin = imageX;
            paramsForImage.topMargin = imageY;

            i.setLayoutParams(paramsForImage);

//                i.getLayoutParams().height = (int) 0.01 * item.SIZE * relativeCanvas.getHeight();

            Picasso
                    .get()
                    .load(item.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(i, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                }
            });

            relativeCanvas.addView(i);
            animationItems.put(item.id, i);
            if (step.isAnimation() && step.isInteractive()) {
                TextView t = new TextView(getContext());
                t.setText(" - " + item.DESCRIPTION);

                LinearLayout.LayoutParams paramsForTag = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                paramsForTag.setMargins((int) metrics.density * 32, 0, (int) metrics.density * 32, 0);
                t.setLayoutParams(paramsForTag);

                linearLayout.addView(t);
                this.animationItemTags.put(item.id, t);
            }
        }
    }

    public void updateUI() {

        Print.print("updateUI" + step.PAGE_NUMBER);

        removeButtons();
        removeAnimatedItems();

        String docName = "Unknown";
        Doc doc = step.doc.get();
        if (doc != null) {
            docName = step.doc.get().DOCUMENT_NAME;
        }
        stepText.setText(step.TEXT);
        answerLabel.setText("The answer is: " + step.ANSWER);
        answerExplain.setText(step.ANSWER_EXPLANATION);

        submitButton.setVisibility(View.VISIBLE);

        if (stepIsFinished) {
            if (step.isQuiz()) {
                answerExplain.setVisibility(View.VISIBLE);
                answerLabel.setVisibility(View.VISIBLE);
            } else {
                answerExplain.setVisibility(View.GONE);
                answerLabel.setVisibility(View.GONE);
            }


        } else {
            answerExplain.setVisibility(View.GONE);
            answerLabel.setVisibility(View.GONE);

        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (step.isQuiz() && !step.isMuiltipleChoice()) {
                    answerQuestionBy(answerText.getText().toString());
                }
                if (step.isAnimation() && !step.isInteractive()) {
                    showAnimation();
                }
            }
        });

        if (step.IMAGE_URL != null) {

            final WeakReference<StepFragment> weakThis = new WeakReference(this);

            Picasso
                    .get()
                    .load(step.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(stepImage, new Callback() {
                @Override
                public void onSuccess() {
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            StepFragment referenceThis = weakThis.get();
                            if (referenceThis != null) {
                                referenceThis.updateAnimationItems();
                            }
                        }
                    };
                    mainHandler.post(myRunnable);
                }

                @Override
                public void onError(Exception e) {
                }
            });

            Picasso.Builder builder = new Picasso.Builder(getActivity());
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            stepImage.setVisibility(View.VISIBLE);
        } else {
            stepImage.setVisibility(View.GONE);
        }

        if (step.isQuiz()) {
            if (step.isMuiltipleChoice()) {
                answerText.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                this.choiceButtons = new ArrayList<Button>();
                for (final String choice : step.CHOICES) {

                    Button b = (Button) getLayoutInflater().inflate(R.layout.shared_choice_button, null);
                    b.setText(choice);
                    linearLayout.addView(b);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins((int) metrics.density * 32, 0, (int) metrics.density * 32, 0);
                    b.setLayoutParams(params);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            answerQuestionBy(choice);
                        }
                    });
                    this.choiceButtons.add(b);
                }
            } else {
                answerText.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
                submitButton.setText("Submit");
            }
        } else {
            this.animationItemTags = new HashMap<String, TextView>();
            this.animationItems = new HashMap<String, ImageView>();


            if (step.isInteractive()) {

                answerText.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
            } else {
                if (step.isAnimation()) {
                    answerText.setVisibility(View.GONE);
                    submitButton.setText("Show Animation");
                    delegate.didFinishStepCorrectly(step);
                } else {
                    answerText.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                    delegate.didFinishStepCorrectly(step);
                }

            }
        }
    }
}
