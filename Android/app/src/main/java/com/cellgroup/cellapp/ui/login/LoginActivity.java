package com.cellgroup.cellapp.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cellgroup.cellapp.R;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity implements LoginFragment.OnFragmentInteractionListener {
    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.no_back, Toast.LENGTH_SHORT).show();;
    }
}
