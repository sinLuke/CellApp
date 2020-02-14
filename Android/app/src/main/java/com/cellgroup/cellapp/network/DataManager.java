package com.cellgroup.cellapp.network;

import android.content.Context;

import com.cellgroup.cellapp.AppDelegate;
import com.cellgroup.cellapp.models.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

public class DataManager {
    private Map<String, Topic> topics;
    private Map<String, Doc> docs;
    private Map<String, Step> steps;
    private Map<String, AnimationBackground> animationBackgrounds;
    private Map<String, AnimationItem> animationItems;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static DataManager shared = null;

    private CollectionReference topicRef;
    private CollectionReference docRef;
    private CollectionReference stepRef;
    private CollectionReference animationBackgroundRef;
    private CollectionReference animationItemRef;

    public DataManager(Context activity){
        topics = new HashMap<>();
        docs = new HashMap<>();
        steps = new HashMap<>();
        animationBackgrounds = new HashMap<>();
        animationItems = new HashMap<>();
    }

    public void downloadData(Context activity){
        topicRef = firebaseFirestore.collection("TOPIC");
        docRef = firebaseFirestore.collection("DOCUMENT");
        stepRef = firebaseFirestore.collection("STEP");
        animationBackgroundRef = firebaseFirestore.collection("ANIMATION_BACKGROUND");
        animationItemRef = firebaseFirestore.collection("ANIMATION_ITEM");
        downloadTopics(activity);
    }

    public void finishDownloads(Context activity){
        AppDelegate.shared.sharedUserManager.updateUserHistory(activity);
    }

    public void downloadTopics(final Context activity){
        topicRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String id = document.getId();
                                Topic topic = new Topic(data, id);
                                if (!topic.hidden) {
                                    DataManager.this.topics.put(id, topic);
                                }
                            }
                            DataManager.this.downloadDocs(activity);
                        } else {
                            NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Topics", activity);
                        }
                    }
                });
    }

    public void downloadDocs(final Context activity){
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            Doc doc = new Doc(data, id);
                            if (!doc.hidden) {
                                DataManager.this.docs.put(id, doc);
                                doc.topic.get().addDoc(doc);
                            }

                        } catch (Exception e) {

                        }

                    }
                    DataManager.this.downloadSteps(activity);
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Documents", activity);
                }
            }
        });
    }

    public void downloadSteps(final Context activity){
        stepRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            Step step = new Step(data, id);
                            if (!step.hidden) {
                                DataManager.this.steps.put(id, step);
                                step.doc.get().addStep(step);
                            }

                        } catch (Exception e) {

                        }
                    }
                    DataManager.this.downloadAnimationBackgrounds(activity);
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Steps", activity);
                }
            }
        });
    }

    public void downloadAnimationBackgrounds(final Context activity){
        animationBackgroundRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            AnimationBackground animationBackground = new AnimationBackground(data, id);
                            DataManager.this.animationBackgrounds.put(id, animationBackground);
                            animationBackground.step.get().addAnimationBackground(animationBackground);
                        } catch (Exception e) {

                        }

                    }
                    DataManager.this.downloadAnimationItems(activity);
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Animation Backgrounds", activity);
                }
            }
        });
    }

    public void downloadAnimationItems(final Context activity){
        animationItemRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();

                        try {
                            AnimationItem animationItem = new AnimationItem(data, id);
                            DataManager.this.animationItems.put(id, animationItem);
                            animationItem.step.get().addAnimationItem(animationItem);
                        } catch (Exception e) {

                        }
                    }
                    DataManager.this.finishDownloads(activity);
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Animation Items", activity);
                }
            }
        });
    }

    public Topic getTopicByID(String id) {
        return this.topics.get(id);
    }

    public Doc getDocumentByID(String id) {
        return this.docs.get(id);
    }

    public Doc getDocumentByStep(String id) {
        Step step = getStepByID(id);
        return this.getDocumentByID(step.id);
    }

    public Step getStepByID(String id) {
        return this.steps.get(id);
    }

    public List<Topic> getAllTopics() {
        return new ArrayList(topics.values());
    }

    public int getTopicsCount() {
        return topics.size();
    }
    public int getRecentDocumentsCount() {
        List<Doc> sortedDoc = new ArrayList();
        for (Doc doc : this.docs.values()) {
            if (doc.existInUserHistory()) {
                sortedDoc.add(doc);
            }
        }
        return sortedDoc.size();
    }

    public List<Doc> getSortedDocuments(final boolean sortByTime){
        List<Doc> sortedDoc = new ArrayList();
        for (Doc doc: this.docs.values()) {
            DocumentCompleteRate rate = doc.getCompletionRate();
            if (doc.existInUserHistory() && !rate.isFinished()) {
                sortedDoc.add(doc);
            }
        }
        sortedDoc.sort(new Comparator<Doc>() {
            @Override
            public int compare(Doc lhs, Doc rhs) {
                if (sortByTime) {
                    return rhs.getLastViewDate().compareTo(lhs.getLastViewDate());
                } else {
                    if (lhs.getCompletionRate().rate > rhs.getCompletionRate().rate) {
                        return -1;
                    } else if (lhs.getCompletionRate().rate < rhs.getCompletionRate().rate) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        return sortedDoc;
    }

}
