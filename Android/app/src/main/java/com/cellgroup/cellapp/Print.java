package com.cellgroup.cellapp;

import android.util.Log;

public class Print {
    public static <T> void print(T item) {
        Log.d("print", item.toString());
    }
    public static <T, U> void print(T tag, U item) {
        Log.d(tag.toString(), item.toString());
    }
}