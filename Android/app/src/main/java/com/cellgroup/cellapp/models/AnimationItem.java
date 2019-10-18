package com.cellgroup.cellapp.models;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AnimationItem {
    public String id;

    public URL IMAGE_URL;
    public int START_POSITION_X;
    public int START_POSITION_Y;
    public int END_POSITION_X;
    public int END_POSITION_Y;

    public WeakReference<Step> step;
    public Map<String, Doc> docs;

    public AnimationItem(){
        defaultInit();
    };

    public AnimationItem(Map<String, Object> data, String id) throws Exception {
        defaultInit();

        this.id = id;

        Object _STEP_ID = data.get("STEP_ID");
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _START_POSITION_X = data.get("START_POSITION_X");
        Object _START_POSITION_Y = data.get("START_POSITION_Y");
        Object _END_POSITION_X = data.get("END_POSITION_X");
        Object _END_POSITION_Y = data.get("END_POSITION_Y");

        if (_IMAGE_URL != null) {
            try {
                this.IMAGE_URL = new URL((String) _IMAGE_URL);
            } catch (Exception e) {
                throw new Exception();
            }
        }

        if (_STEP_ID != null) {
            String stepID = (String) _STEP_ID;
            DataManager dataManager = DataManager.shared;
            Step step = dataManager.getStepByID(stepID);
            this.step = new WeakReference<Step>(step);
        } else {
            throw new Exception();
        }

        if (_START_POSITION_X != null) {
            this.START_POSITION_X = (int) (long) _START_POSITION_X;
        }

        if (_START_POSITION_Y != null) {
            this.START_POSITION_Y = (int) (long) _START_POSITION_Y;
        }
        if (_END_POSITION_X != null) {
            this.END_POSITION_X = (int) (long) _END_POSITION_X;
        }

        if (_END_POSITION_Y != null) {
            this.END_POSITION_Y = (int) (long) _END_POSITION_Y;
        }

    }

    private void defaultInit(){
        this.id = "0";
        this.IMAGE_URL = null;
        this.docs = new HashMap<>();
        this.step = null;
        this.START_POSITION_X = 0;
        this.START_POSITION_Y = 0;
        this.END_POSITION_X = 0;
        this.END_POSITION_Y = 0;
    }
}
