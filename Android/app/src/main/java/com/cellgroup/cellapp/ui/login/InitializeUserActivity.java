package com.cellgroup.cellapp.ui.login;

import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.ui.SingleFragmentActivity;

public class InitializeUserActivity extends SingleFragmentActivity implements InitializeUserFragment.OnFragmentInteractionListener {
    @Override
    public Fragment createFragment() {
        return InitializeUserFragment.newInstance();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.no_back, Toast.LENGTH_SHORT).show();
    }
}
