package com.cellgroup.cellapp;

import android.app.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.cellgroup.cellapp.network.DataManager;

import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.login.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AppDelegate {

    private int RC_SIGN_IN = 0;

    public static AppDelegate shared = new AppDelegate();

    private Activity currentActivity;
    private DataManager sharedDataManager;
    private UserManager sharedUserManager;

    private void AppDelegate(){return;}

    public void applicationDidlaunched(Activity activity) {
        applicationShouldRequireLogin(activity);
    }

    //handling login
    public void applicationShouldRequireLogin(Activity activity) {

        currentActivity = activity;
        FirebaseUser user = UserManager.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(currentActivity, LoginActivity.class);
            currentActivity.startActivity(i);
        } else {
            UserManager.getUserManager(user, activity);
        }
    }

    public void applicationDidFinisheLogin(Activity activity, UserManager pSharedUserManager) {
        sharedUserManager = pSharedUserManager;
        applicationWillInitalizeUser(activity);
    }

    //Initialize User for first time login, receive UserManager callback
    public void applicationWillInitalizeUser(Activity activity) {
        sharedUserManager.checkIfUserFirstLogin(activity);
    }

    public void applicationDidFinisheInitializeUser(Activity activity) {
        DataManager.getDataManager(activity);
    }

    //receive DataManager callback
    public void DatabseDidFinishCheckingUpdates(SQLiteDatabase db, DataManager pSharedDataManager){
        applicationMoveToMainScreen();
        sharedDataManager = pSharedDataManager;
    }

    //handling exception
    public void applicationDidReportException(String withMessage){
        if (currentActivity != null) {
            Toast.makeText(currentActivity, withMessage, Toast.LENGTH_LONG).show();
            applicationShouldRequireLogin(currentActivity);
        }
    }

    public void applicationMoveToMainScreen() {
        Intent i = new Intent(currentActivity, MainActivity.class);
        currentActivity.startActivity(i);
    }

    public UserManager getSharedUserManager(){
        return sharedUserManager;
    }

    public DataManager getSharedDataManager(){
        return sharedDataManager;
    }
}