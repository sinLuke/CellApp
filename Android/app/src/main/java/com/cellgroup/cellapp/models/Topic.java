package com.cellgroup.cellapp.models;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Topic {

    public String id;

    public URL IMAGE_URL;
    public String TOPIC_NAME;
    public Date created;
    public Map<String, Doc> docs;
    public Boolean hidden = false;

    public Topic(){
        defaultInit();
    };

    public Topic(Map<String, Object> data, String id) {
        defaultInit();

        this.id = id;

        if (data.get("hidden") != null) {
            this.hidden = (Boolean) data.get("hidden") == true;
        }

        Object _TOPIC_NAME = data.get("TOPIC_NAME");
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _CREATED = data.get("created");;

        if (_TOPIC_NAME != null) {
            this.TOPIC_NAME = (String) _TOPIC_NAME;
        }

        if (_IMAGE_URL != null) {
            try { this.IMAGE_URL =  new URL((String) _IMAGE_URL);
            } catch (Exception e) {

            }
        }

        if (_CREATED != null) {
            this.created = new Date((long) _CREATED);
        }
    }

    public void addDoc(Doc doc) {
        this.docs.put(doc.id, doc);
    }

    private void defaultInit(){
        this.id = "0";
        this.TOPIC_NAME = "New Topic";
        this.IMAGE_URL = null;
        this.created = new Date();
        this.docs = new HashMap<>();
    }
}
