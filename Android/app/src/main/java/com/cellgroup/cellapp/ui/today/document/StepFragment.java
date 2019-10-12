package com.cellgroup.cellapp.ui.today.document;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.models.Doc;
import com.cellgroup.cellapp.models.Step;
import com.cellgroup.cellapp.network.UserManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import androidx.fragment.app.Fragment;

public class StepFragment extends Fragment {

    private Step step;

    private TextView stepDocumentTitle;
    private TextView stepText;
    private ImageView stepImage;

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
        updateUI();

        return v;
    }

    private void updateUI() {
        String docName = "Unknown";
        Doc doc = step.doc.get();
        if (doc != null) {
            docName = step.doc.get().DOCUMENT_NAME;
        }
        stepDocumentTitle.setText(String.format("%s: Step:%d", docName, step.PAGE_NUMBER));
        stepText.setText(step.TEXT);
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

        }
    }
}
