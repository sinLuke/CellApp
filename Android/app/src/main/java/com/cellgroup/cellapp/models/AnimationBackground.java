package com.cellgroup.cellapp.models;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;

public class AnimationBackground {
    public String id;

    public URL IMAGE_URL;
    public WeakReference<Step> step;

    public AnimationBackground() {
        defaultInit();
    }

    public AnimationBackground(Map<String, Object> data, String id) throws Exception {

        this.id = id;

        defaultInit();
        Object _IMAGE_URL = data.get("IMAGE_URL");
        Object _STEP_ID = data.get("STEP_ID");

        if (_IMAGE_URL != null) {
            try {
                this.IMAGE_URL = new URL((String) _IMAGE_URL);
            } catch (Exception e) {
                throw new Exception();
            }
        }

        if (_STEP_ID != null) {
            String stepID = (String) _STEP_ID;
            DataManager dataManager = NetworkManager.shared.data;
            Step step = dataManager.getStepByID(stepID);
            this.step = new WeakReference<Step>(step);
        } else {
            throw new Exception();
        }
    }

    private void defaultInit() {
        this.id = "0";
        this.IMAGE_URL = null;
        this.step = null;
    }
}
