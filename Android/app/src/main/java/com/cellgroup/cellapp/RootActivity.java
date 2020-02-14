package com.cellgroup.cellapp;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class RootActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        AppDelegate.shared.currentScreen = this.getClass().getName();
    }
}
