package com.cellgroup.cellapp;

import android.app.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Network;
import android.widget.Toast;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

public class AppDelegate {

    private int RC_SIGN_IN = 0;

    public static AppDelegate shared = new AppDelegate();

    private Activity currentActivity;
    private NetworkManager sharedNetworkManager;
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

    public void applicationDidInitializeUser(Activity activity, UserManager user) {
        DatabseWillCheckingUpdates(activity);
    }

    public void DatabseWillCheckingUpdates(Activity activity){
        NetworkManager.getDataManager(activity);
    }

    //receive DataManager callback
    public void DatabseDidCheckingUpdates(NetworkManager pSharedNetworkManager){
        applicationMoveToMainScreen();
        sharedNetworkManager = pSharedNetworkManager;
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

    public NetworkManager getSharedNetworkManager(){
        return sharedNetworkManager;
    }
}