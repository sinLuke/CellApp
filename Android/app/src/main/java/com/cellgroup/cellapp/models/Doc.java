package com.cellgroup.cellapp.models;

import java.net.URL;
import java.util.Map;

public class Doc {
    public String id;

    private int DIFFICULTY;
    private String DOCUMENT_NAME;
    private URL IMAGE_URL;
    private String INTRODUCTION;
    public Topic topic;
    private int AVALIABLE_SCOPE;
    private int created;

    public Doc(){
        defaultInit();
    };

    public Doc(Map<String, Object> data){
        Object _DIFFICULTY = data.get("DIFFICULTY");
        Object _DOCUMENT_NAME = data.get("DOCUMENT_NAME");
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _INTRODUCTION = data.get("INTRODUCTION");
        Object _TOPIC_ID = data.get("INTRODUCTION");
        Object _AVALIABLE_SCOPE = data.get("available_scope");
        Object _CREATED = data.get("created");;
        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }
        if (_DIFFICULTY != null) {
            this.DOCUMENT_NAME = (String) _DOCUMENT_NAME;
        }
        if (_DIFFICULTY != null) {
            try { this.IMAGE_URL =  new URL((String) _IMAGE_URL);
            } catch (Exception e) {
                defaultInit(this);
            }
        }
        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }
        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }
        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }
        if (_DIFFICULTY != null) {
            this.DIFFICULTY = (int) (long) _DIFFICULTY;
        }
    }

    private Void defaultInit(){
        this.DIFFICULTY = 1;
        this.DOCUMENT_NAME = "New Document";
        this.IMAGE_URL = null;
        this.INTRODUCTION = "Add Introduction Here ...";
        this.topic = null;
        this.AVALIABLE_SCOPE = 10;
        this.created = 0;
    }
}
