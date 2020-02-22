package com.cellgroup.cellapp.models;

import com.cellgroup.cellapp.network.DataManager;
import com.cellgroup.cellapp.network.NetworkManager;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.lang.StrictMath.abs;

public class AnimationItem {

    public static int tolerant = 0;

    public String id;

    public String DESCRIPTION;

    public URL IMAGE_URL;
    public double START_POSITION_X;
    public double START_POSITION_Y;
    public double END_POSITION_X;
    public double END_POSITION_Y;
    public int SIZE;

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
        Object _DESCRIPTION = data.get("DESCRIPTION");
        Object _SIZE = data.get("SIZE");

        if (_IMAGE_URL != null) {
            this.IMAGE_URL = new URL((String) _IMAGE_URL);
        } else {
            throw new Exception(id + ": URL not exist");
        }

        if (_STEP_ID != null) {
            String stepID = (String) _STEP_ID;
            DataManager dataManager = DataManager.shared;
            Step step = dataManager.getStepByID(stepID);
            this.step = new WeakReference<Step>(step);
        } else {
            throw new Exception("step id not exist");
        }

        if (_START_POSITION_X != null) {
            if (_START_POSITION_X.getClass().isAssignableFrom(Double.class)){
                this.START_POSITION_X = (double) _START_POSITION_X;
            } else if (_START_POSITION_X.getClass().isAssignableFrom(Long.class)){
                this.START_POSITION_X = (double) (long) _START_POSITION_X;
            } else {
                throw new Exception("START_POSITION_X no valid value" + _START_POSITION_X.getClass());
            }
        }

        if (_START_POSITION_Y != null) {

            if (_START_POSITION_Y.getClass().isAssignableFrom(Double.class)){
                this.START_POSITION_Y = (double) _START_POSITION_Y;
            } else if (_START_POSITION_Y.getClass().isAssignableFrom(Long.class)){
                this.START_POSITION_Y = (double) (long) _START_POSITION_Y;
            } else {
                throw new Exception("START_POSITION_Y no valid value" + _START_POSITION_Y.getClass());
            }
        }
        if (_END_POSITION_X != null) {

            if (_END_POSITION_X.getClass().isAssignableFrom(Double.class)){
                this.END_POSITION_X = (double) _END_POSITION_X;
            } else if (_END_POSITION_X.getClass().isAssignableFrom(Long.class)){
                this.END_POSITION_X = (double) (long) _END_POSITION_X;
            } else {
                throw new Exception("END_POSITION_X no valid value" + _END_POSITION_X.getClass());
            }
        }

        if (_END_POSITION_Y != null) {

            if (_END_POSITION_Y.getClass().isAssignableFrom(Double.class)){
                this.END_POSITION_Y = (double) _END_POSITION_Y;
            } else if (_END_POSITION_Y.getClass().isAssignableFrom(Long.class)){
                this.END_POSITION_Y = (double) (long) _END_POSITION_Y;
            } else {
                throw new Exception("END_POSITION_Y no valid value" + _END_POSITION_Y.getClass());
            }
        }

        if (_SIZE != null) {

            if (_SIZE.getClass().isAssignableFrom(Double.class)){
                this.SIZE = (int) (double) _SIZE;
            } else if (_SIZE.getClass().isAssignableFrom(Long.class)){
                this.SIZE = (int) (long) _SIZE;
            } else if (_SIZE.getClass().isAssignableFrom(Integer.class)){
                this.SIZE = (int) (int) _SIZE;
            } else {
                throw new Exception("SIZE no valid value" + _SIZE.getClass());
            }
        }

        if (_DESCRIPTION != null) {
            this.DESCRIPTION = (String) _DESCRIPTION;
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
        this.DESCRIPTION = "";
    }
}
