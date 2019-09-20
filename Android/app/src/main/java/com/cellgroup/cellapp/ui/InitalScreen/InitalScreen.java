package com.cellgroup.cellapp.ui.InitalScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.cellgroup.cellapp.AppDelegate;

import com.google.firebase.auth.*;
import com.cellgroup.cellapp.R;


public class InitalScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inital_screen);
        AppDelegate.shared.applicationShouldRequireLogin(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppDelegate.shared.handleActivityResult(requestCode, resultCode, data, this);

    }
}
