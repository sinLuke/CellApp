package com.cellgroup.cellapp.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    public String id;
    public int userScope;

    public UserData() { defaultInit(); }

    public UserData(Map<String, Object> data, String id) {
        this.id = id;
        defaultInit();
        Object _userScope = data.get("user_scope");
        if (_userScope != null) {
            this.userScope = (int) (long) _userScope;
        } else {
            Log.d("new User Data Exception", "_userScope != null");
        }
    }

    public Map<String, Object> synthesize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user_scope", this.userScope);
        return data;
    }

    private void defaultInit(){
        this.userScope = 10;
    }
}
