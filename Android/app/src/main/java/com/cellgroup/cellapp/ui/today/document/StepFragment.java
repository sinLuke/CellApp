package com.cellgroup.cellapp.ui.today.document;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.network.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class StepFragment extends Fragment {

    private Step step;

    private TextView stepDocumentTitle;
    private TextView stepText;
    private LinearLayout linearLayout;
    private ImageView stepImage;
    private List<Button> choiceButtons;
    private Button submitButton;
    private EditText answerText;
    private TextView answerLabel;
    private TextView answerExplain;

    public StepFragmentDelegate delegate;

    public StepFragment(){

    }

    static public StepFragment newInstance(Step step) {
        StepFragment stepFragment = new StepFragment();
        stepFragment.step = step;
        return stepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step, container, false);
        stepDocumentTitle = v.findViewById(R.id.step_document_title);
        stepText = v.findViewById(R.id.step_text);
        stepImage = v.findViewById(R.id.step_image);
        submitButton = v.findViewById(R.id.submit_button);
        answerText = v.findViewById(R.id.step_answer_text);
        answerLabel = v.findViewById(R.id.step_answer_label);
        linearLayout = v.findViewById(R.id.step_layout);
        answerExplain = v.findViewById(R.id.step_answer_explain_text);
        this.choiceButtons = new ArrayList<Button>();
        updateUI();

        return v;
    }

    private void answerQuestionBy(String answer){

        CharSequence text;

        int color;

        if (step.isAnswerCorrect(answer)) {
            text = "Correct!";
            answerExplain.setVisibility(View.VISIBLE);
            answerLabel.setVisibility(View.VISIBLE);
            answerText.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            delegate.didFinishStepCorrectly(step);
            removeButtons();
            color = R.color.systemGreen;
        } else {
            answerExplain.setVisibility(View.GONE);
            answerLabel.setVisibility(View.GONE);
            text = answer + " is not correct, try again ...";
            color = R.color.systemRed;
        }

        Snackbar sb = Snackbar.make(linearLayout, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(getResources().getColor(color, null));
        sb.show();
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

    public void updateUI() {

        String docName = "Unknown";
        Doc doc = step.doc.get();
        if (doc != null) {
            docName = step.doc.get().DOCUMENT_NAME;
        }
        stepDocumentTitle.setText(String.format("%s: Step:%d", docName, step.PAGE_NUMBER));
        stepText.setText(step.TEXT);
        answerLabel.setText("The answer is: " + step.ANSWER);
        answerExplain.setText(step.ANSWER_EXPLANATION);

        answerExplain.setVisibility(View.GONE);
        answerLabel.setVisibility(View.GONE);
        answerText.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestionBy(answerText.getText().toString());
            }
        });

        if (step.IMAGE_URL != null) {
            Picasso
                    .get()
                    .load(step.IMAGE_URL.toString())
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).into(stepImage, new Callback() {
                @Override
                public void onSuccess() {
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
                removeButtons();
                this.choiceButtons = new ArrayList<Button>();
                for (final String choice : step.CHOICES) {

                    Button b = (Button) getLayoutInflater().inflate(R.layout.shared_choice_button, null);
                    b.setText(choice);
                    linearLayout.addView(b);

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
            }
        } else if (step.isInteractive()) {

        } else {
            delegate.didFinishStepCorrectly(step);
        }
    }
}
