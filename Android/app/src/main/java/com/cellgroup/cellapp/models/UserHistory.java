package com.cellgroup.cellapp.models;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserHistory {
    public String id;

    public WeakReference<Step> step;
    public Boolean finished;
    public Date lastViewd;

    public UserHistory(){

    };

    public UserHistory(Map<String, Object> data, String id) throws Exception {

        this.id = id;

        Object _finish = data.get("finish");
        Object _lastViewed = data.get("lastViewed");
        Object _stepID = data.get("stepID");;

        if (_finish != null) {
            this.finished = (Boolean) _finish;
        } else {
            throw new Exception();
        }

        if (_lastViewed != null) {
            this.lastViewd = new Date((long) _lastViewed);
        }else {
            throw new Exception();
        }

        if (_stepID != null) {
            String stepID = (String) _stepID;
            DataManager dataManager = NetworkManager.shared.data;
            Step step = dataManager.getStepByID(stepID);
            this.step = new WeakReference<Step>(step);
        } else {
            throw new Exception();
        }
    }
}
