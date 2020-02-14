package com.cellgroup.cellapp.models;

import android.util.Log;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;
import com.cellgroup.cellapp.network.UserManager;
import com.firebase.ui.auth.data.model.User;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Doc {
    public String id;

    public int DIFFICULTY;
    public String DOCUMENT_NAME;
    public URL IMAGE_URL;
    public String INTRODUCTION;
    public WeakReference<Topic> topic;
    public int available_scope;
    public Date created;
    public Map<String, Step> steps;
    public Boolean hidden = false;

    public Doc(){
        defaultInit();
    };

    public DocumentCompleteRate getCompletionRate(){
        UserManager userManager = UserManager.shared;
        return userManager.getCompleteRateOfDocument(this);
    }

    public Date getLastViewDate(){
        UserManager userManager = UserManager.shared;
        return userManager.getLastViewDateOfDocument(this);
    }

    public Step getLastViewStep(){
        UserManager userManager = UserManager.shared;
        return userManager.getLastViewStepOfDocument(this);
    }

    public boolean existInUserHistory() {
        UserManager userManager = UserManager.shared;
        return userManager.isDocumentExistInUserHistory(this);
    }
    public Doc(Map<String, Object> data, String id) throws Exception {
        defaultInit();
        this.id = id;
        if (data.get("hidden") != null) {
            this.hidden = (Boolean) data.get("hidden") == true;
        }
        Object _DIFFICULTY = data.get("DIFFICULTY");
        Object _DOCUMENT_NAME = data.get("DOCUMENT_NAME");
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _INTRODUCTION = data.get("INTRODUCTION");
        Object _TOPIC_ID = data.get("TOPIC_ID");
        Object _AVAILABLE_SCOPE = data.get("available_scope");
        Object _CREATED = data.get("created");;

        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }

        if (_DOCUMENT_NAME != null) {
            this.DOCUMENT_NAME = (String) _DOCUMENT_NAME;
        }

        if (_IMAGE_URL != null) {
            try { this.IMAGE_URL =  new URL((String) _IMAGE_URL);
            } catch (Exception e) {
                throw new Exception();
            }
        }

        if (_INTRODUCTION != null) {
            this.INTRODUCTION = (String) _INTRODUCTION;
        }

        if (_TOPIC_ID != null) {
            String topicID = (String) _TOPIC_ID;

            Log.d("AddDOC", topicID);
            DataManager dataManager = DataManager.shared;
            Topic topic = dataManager.getTopicByID(topicID);
            this.topic = new WeakReference<Topic>(topic);
        } else {
            throw new Exception();
        }

        if (_AVAILABLE_SCOPE != null) {
            this.available_scope = (int) (long) _AVAILABLE_SCOPE;
        }

        if (_CREATED != null) {
            this.created = new Date((long) _CREATED);
        }
    }

    public void addStep(Step step) {
        this.steps.put(step.id, step);
    }

    private void defaultInit(){
        this.id = "0";
        this.DIFFICULTY = 1;
        this.DOCUMENT_NAME = "New Document";
        this.IMAGE_URL = null;
        this.INTRODUCTION = "Add Introduction Here ...";
        this.topic = null;
        this.available_scope = 10;
        this.created = new Date(0);
        this.steps = new HashMap<>();
    }
}
