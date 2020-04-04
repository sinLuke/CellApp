package com.cellgroup.cellapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.network.UserManager;
import com.cellgroup.cellapp.ui.InitalScreen.InitalScreen;
import com.cellgroup.cellapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

public class AppDelegate {

    private int RC_SIGN_IN = 0;
    private Boolean ShouldAskLoginInfomation = false;

    public static AppDelegate shared = new AppDelegate();
    public String currentScreen = InitalScreen.class.getName();

    private Context currentActivity;
    public NetworkManager sharedNetworkManager;
    public UserManager sharedUserManager;

    private boolean launched = false;

    private void AppDelegate(){return;}

    public void applicationDidlaunched(Context activity) {
        if (!launched) {
            shared.applicationLaunchingProcessDidFinishedCurrentTask(activity);
            launched = true;
        }
    }

    public void applicationLaunchingProcessDidFinishedCurrentTask(Context activity) {
        currentActivity = activity;
        FirebaseUser user = UserManager.getCurrentUser();
        if (user == null && ShouldAskLoginInfomation) {
            Intent i = new Intent(currentActivity, LoginActivity.class);
            currentActivity.startActivity(i);
            return;
        }
        if (sharedUserManager == null) {
            if (ShouldAskLoginInfomation) {
                UserManager.getUserManager(user, activity);
            } else {
                UserManager.getUserManager(activity);
            }

            return;
        }
//        if (!sharedUserManager.checkIfUserFirstLogin(activity)) {
//            Intent i = new Intent(activity, InitializeUserActivity.class);
//            activity.startActivity(i);
//            return;
//        }
        if (!sharedUserManager.checkIfUserEmailVerified(activity)) {
            sharedUserManager.verifyEmail(activity);
            return;
        }
        if (DataManager.shared == null) {
            NetworkManager.getNetworkManager(activity);
            return;
        }
        applicationMoveToScreenIfNeeded(MainActivity.class);
    }

    //handling exception
    public void applicationDidReportException(String withMessage){
        if (currentActivity != null) {
            Toast.makeText(currentActivity, withMessage, Toast.LENGTH_LONG).show();
            applicationLaunchingProcessDidFinishedCurrentTask(currentActivity);
        }
    }

    public void applicationMoveToScreenIfNeeded(Class<? extends AppCompatActivity> activityClass) {
        if (currentScreen != activityClass.getName()) {
            Intent i = new Intent(currentActivity, activityClass);
            currentActivity.startActivity(i);
        }
    }
}