package com.cellgroup.cellapp.network;

import java.util.HashMap;
import java.util.Map;

public class FirestoreCoder {
    public static int getDataFromDBVersion(Map<String, Object> data) {
        Object _db_version = data.get("value");
        if (_db_version != null) {
            int db_version = (int) (long) _db_version;
            return db_version;
        } else {
            return 0;
        }
    }
    public static int getDataFromUser(Map<String, Object> data) {
        Object _user_scope = data.get("user_scope");
        if (_user_scope != null) {
            int user_scope = (int) (long) _user_scope;
            return user_scope;
        } else {
            return 0;
        }
    }
    public static Map<String, Object> setDataByUser(int user_scope) {
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("user_scope", user_scope);

        return newUser;
    }
}
