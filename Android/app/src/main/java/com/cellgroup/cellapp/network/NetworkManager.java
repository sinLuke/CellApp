package com.cellgroup.cellapp.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cellgroup.cellapp.AppDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NetworkManager implements NetworkManagerCallBackDelegate {

    private DocumentReference docRef;
    private Context context;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static NetworkManager shared;
    private static DataManager data;

    private NetworkManager (Context c, int version) {
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
                        int db_version = FirestoreCoder.getDataFromDBVersion(document.getData());
                        if (db_version != 0) {
                            shared = new NetworkManager(c, db_version);
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
        downloadData();
    }

    public void downloadData(){

    }

    public void networkManagerDidDownloadData(){
        AppDelegate.shared.DatabseDidCheckingUpdates(shared);
    }
}
