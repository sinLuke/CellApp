package com.cellgroup.cellapp;

import android.app.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.cellgroup.cellapp.network.DataManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class AppDelegate {

    private int RC_SIGN_IN = 0;

    public static AppDelegate shared = new AppDelegate();

    private Activity currentActivity;

    private void AppDelegate(){return;}

    public void applicationDidlaunched(Activity activity) {

    }

    public void applicationShouldRequireLogin(Activity activity) {
        currentActivity = activity;
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        currentActivity = activity;
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == activity.RESULT_OK) {
                applicationDidFinisheLogin(activity);
            } else {
                Snackbar.make(activity.findViewById(R.id.initial_content), R.string.login_unsuccessful, Snackbar.LENGTH_LONG).show();
                this.applicationShouldRequireLogin(activity);
            }
        }
    }

    public void applicationDidFinisheLogin(Activity activity) {
        DataManager.shared.checkOnlineDatabaseVersion(activity);
    }

    public void applicationMoveToMainScreen() {

        Intent i = new Intent(currentActivity, MainActivity.class);
        currentActivity.startActivity(i);
    }

    public void DatabseDidFinishCheckingUpdates(SQLiteDatabase db){
        applicationMoveToMainScreen();
    }
}