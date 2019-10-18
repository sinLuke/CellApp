package com.cellgroup.cellapp.models;

import android.util.Log;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.network.CreateUserHistoryCallBackDelegate;
import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.network.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class UserHistory {
    public String id;

    public WeakReference<Step> step;
    public Boolean finished;
    public Date lastViewd;

    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public UserHistory(){

    };

    public static void createUserHistory(final Step step, final CreateUserHistoryCallBackDelegate createUserHistoryCallBackDelegate){

        Map<String, Object> docData = new HashMap<>();
        docData.put("finish", true);
        docData.put("lastViewed", (new Date()).getTime());
        docData.put("stepID", step.id);
        docData.put("userID", UserManager.getCurrentUser().getUid());
        firebaseFirestore.collection("UserHistory").add(docData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> pTask) {
                if (pTask.isSuccessful()) {
                    DocumentReference document = pTask.getResult();

                    UserHistory newUserHistory = new UserHistory();
                    newUserHistory.id = document.getId();
                    newUserHistory.finished = true;
                    newUserHistory.lastViewd = new Date();
                    newUserHistory.step = new WeakReference<>(step);

                    createUserHistoryCallBackDelegate.handler(newUserHistory);
                } else {
                    AppDelegate.shared.applicationDidReportException("Firestore get failed with " + pTask.getException());
                }
            }
        });
    }

    public UserHistory(Map<String, Object> data, String id) throws Exception {

        this.id = id;

        Object _finish = data.get("finish");
        Object _lastViewed = data.get("lastViewed");
        Object _stepID = data.get("stepID");;

        if (_finish != null) {
            this.finished = (Boolean) _finish;
        } else {
            Log.d("new User History Exception", "_finish != null");
            throw new Exception();
        }

        if (_lastViewed != null) {
            this.lastViewd = new Date((long) _lastViewed);
        }else {
            Log.d("new User History Exception", "_lastViewed != null");
            throw new Exception();
        }

        if (_stepID != null) {
            String stepID = (String) _stepID;
            Step step = DataManager.shared.getStepByID(stepID);
            this.step = new WeakReference<Step>(step);
        } else {
            Log.d("new User History Exception", "stepID != null");
            throw new Exception();
        }
    }
}
