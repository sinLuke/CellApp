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

    private NetworkManager (Context c) {
        context = c.getApplicationContext();
        return;
    }

    public static void getNetworkManager(final Context activity){
        DocumentReference docRef = firebaseFirestore.collection("var").document("db_version");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentSnapshot document = pTask.getResult();
                    if (document.exists()) {

                        shared = new NetworkManager(activity);

                        shared.callBackCurrentOnlineDatabaseVersion(activity);
                    } else {
                        AppDelegate.shared.applicationDidReportException("Can't detect the online database version");
                    }
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }

    public void callBackCurrentOnlineDatabaseVersion(Context activity) {
        downloadData(activity);
    }

    public void downloadData(Context activity){
        DataManager.shared = new DataManager(activity);
        DataManager.shared.downloadData(activity);
    }

    public void networkManagerDidDownloadData(String ErrorMessage, Context activity){
        if (ErrorMessage != null) {
            AppDelegate.shared.applicationDidReportException(ErrorMessage);
        } else {
            AppDelegate.shared.sharedNetworkManager = shared;
            AppDelegate.shared.applicationLaunchingProcessDidFinishedCurrentTask(activity);
        }
    }
}
