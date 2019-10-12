package com.cellgroup.cellapp.ui.today.topic;

import android.net.Uri;

import com.cellgroup.cellapp.ui.SingleFragmentActivity;
import com.cellgroup.cellapp.ui.login.InitializeUserFragment;

import androidx.fragment.app.Fragment;

public class TopicActivity extends SingleFragmentActivity implements InitializeUserFragment.OnFragmentInteractionListener {
    @Override
    protected Fragment createFragment() {
        return TopicFragment.newInstance(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
