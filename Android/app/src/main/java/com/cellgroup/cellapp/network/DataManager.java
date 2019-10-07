package com.cellgroup.cellapp.network;

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
    private Map<String, List<Doc>> documents;
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private CollectionReference topicRef;
    private CollectionReference docRef;
    private CollectionReference stepRef;
    private CollectionReference animationBackgroundRef;
    private CollectionReference animationItemRef;

    public DataManager(){
        topics = new HashMap<>();
        docs = new HashMap<>();
        steps = new HashMap<>();
        animationBackgrounds = new HashMap<>();
        animationItems = new HashMap<>();
        downloadData();
    }

    public void downloadData(){
        topicRef = firebaseFirestore.collection("TOPIC");
        docRef = firebaseFirestore.collection("DOCUMENT");
        stepRef = firebaseFirestore.collection("STEP");
        animationBackgroundRef = firebaseFirestore.collection("ANIMATION_BACKGROUND");
        animationItemRef = firebaseFirestore.collection("ANIMATION_ITEM");
        downloadTopics();
    }

    public void finishDownloads(){
        NetworkManager.shared.networkManagerDidDownloadData(null);
    }

    public void downloadTopics(){
        topicRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String id = document.getId();
                                Topic topic = new Topic(data, id);
                                DataManager.this.topics.put(id, topic);
                            }
                            DataManager.this.downloadDocs();
                        } else {
                            NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Topics");
                        }
                    }
                });
    }

    public void downloadDocs(){
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            Doc doc = new Doc(data, id);
                            DataManager.this.docs.put(id, doc);
                            doc.topic.get().addDoc(doc);
                        } catch (Exception e) {

                        }

                    }
                    DataManager.this.downloadSteps();
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Documents");
                }
            }
        });
    }

    public void downloadSteps(){
        stepRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String id = document.getId();
                        try {
                            Step step = new Step(data, id);
                            DataManager.this.steps.put(id, step);
                            step.doc.get().addStep(step);
                        } catch (Exception e) {

                        }
                    }
                    DataManager.this.downloadAnimationBackgrounds();
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Steps");
                }
            }
        });
    }

    public void downloadAnimationBackgrounds(){
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
                    DataManager.this.downloadAnimationItems();
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Animation Backgrounds");
                }
            }
        });
    }

    public void downloadAnimationItems(){
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
                    DataManager.this.finishDownloads();
                } else {
                    NetworkManager.shared.networkManagerDidDownloadData("Error When Downloading Animation Items");
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
        for (Doc doc: this.docs.values()) {
            if (doc.existInUserHistory()) {
                sortedDoc.add(doc);
            }
        }
        return sortedDoc.size();
    }

    public List<Doc> getDocumentsForTopic(String topicID) {
        return documents.get(topicID);
    }

    public List<Doc> getSortedDocuments(final boolean sortByTime){
        List<Doc> sortedDoc = new ArrayList();
        for (Doc doc: this.docs.values()) {
            if (doc.existInUserHistory()) {
                sortedDoc.add(doc);
            }
        }
        sortedDoc.sort(new Comparator<Doc>() {
            @Override
            public int compare(Doc lhs, Doc rhs) {
                if (sortByTime) {
                    return lhs.getLastViewDate().compareTo(rhs.getLastViewDate());
                } else {
                    if (lhs.getCompletionRate().rate > rhs.getCompletionRate().rate) {
                        return 1;
                    } else if (lhs.getCompletionRate().rate < rhs.getCompletionRate().rate) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        return sortedDoc;
    }

}
