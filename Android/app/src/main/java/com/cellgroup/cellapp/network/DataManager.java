package com.cellgroup.cellapp.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.database.DatabaseHelper;
import com.google.firebase.firestore.DocumentReference;

public class DataManager implements DataManagerCallBackDelegate {

    private DocumentReference docRef;
    private Context context;
    private SQLiteDatabase db;

    public static DataManager shared;

    private DataManager (Context c, int version) {
        context = c.getApplicationContext();
        db = new DatabaseHelper(context, version).getWritableDatabase();
        return;
    }

    public void checkOnlineDatabaseVersion(Context c){

    }

    public void callBackCurrentOnlineDatabaseVersion(int version) {
        db = new DatabaseHelper(context, version).getWritableDatabase();
        AppDelegate.shared.DatabseDidFinishCheckingUpdates(db);
    }



}
