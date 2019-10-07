package com.cellgroup.cellapp.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cellgroup.cellapp.AppDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NetworkManager {

    private DocumentReference docRef;
    private Context context;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static NetworkManager shared;
    public DataManager data;

    private NetworkManager (Context c) {
        context = c.getApplicationContext();
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

                        shared = new NetworkManager(c);
                        shared.callBackCurrentOnlineDatabaseVersion();
                    } else {
                        AppDelegate.shared.applicationDidReportException("Can't detect the online database version");
                    }
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }

    public void callBackCurrentOnlineDatabaseVersion() {
        downloadData();
    }

    public void downloadData(){
        data = new DataManager();
    }

    public void networkManagerDidDownloadData(String ErrorMessage){
        if (ErrorMessage != null) {
            AppDelegate.shared.applicationDidReportException(ErrorMessage);
        } else {
            AppDelegate.shared.DatabseDidCheckingUpdates(shared);
        }
    }
}
