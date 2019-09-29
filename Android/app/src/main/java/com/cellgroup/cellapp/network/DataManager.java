package com.cellgroup.cellapp.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.database.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataManager implements DataManagerCallBackDelegate {

    private DocumentReference docRef;
    private Context context;
    private SQLiteDatabase db;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static DataManager shared;

    private DataManager (Context c, int version) {
        context = c.getApplicationContext();
        db = new DatabaseHelper(context, version).getWritableDatabase();
        return;
    }

    public static void getDataManager(final Context c){
        DocumentReference docRef = firebaseFirestore.collection("var").document("db_version");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot document = pTask.getResult();
                    if (document.exists()) {
                        int db_version = FirestoreCoder.getDataFromDBVersion(document.getData());
                        if (db_version != 0) {
                            shared = new DataManager(c, db_version);
                            shared.callBackCurrentOnlineDatabaseVersion(db_version);
                        }
                    } else {
                        AppDelegate.shared.applicationDidReportException("Can't detect the online database version");
                    }
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }

    public void callBackCurrentOnlineDatabaseVersion(int version) {
        db = new DatabaseHelper(context, version).getWritableDatabase();
        AppDelegate.shared.DatabseDidFinishCheckingUpdates(db, shared);
    }
}
