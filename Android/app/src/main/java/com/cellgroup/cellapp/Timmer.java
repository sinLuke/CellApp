package com.cellgroup.cellapp;

import android.os.Handler;

import java.lang.ref.WeakReference;


public class Timmer {

    static Timmer last = null;
    private WeakReference<TimmerDelegate> delegate;
    private int secs;

    static public Timmer getTimmer(int sec, TimmerDelegate callBackDelegate) {
        Timmer t = new Timmer();
        t.delegate = new WeakReference(callBackDelegate);
        t.secs = sec;

        if (last != null) {
            last.delegate = null;
        }

        last = t;
        return t;
    }

    public void start() {
        tick(secs);
    }

    private void tick(final int sec) {

        if (delegate == null) {
            return;
        }

        TimmerDelegate theDelegate = delegate.get();
        if (theDelegate == null) {
            return;
        }

        if (sec < 0) {
            return;
        } else {
            theDelegate.tick(sec);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tick(sec - 1);
            }
        }, 1000);
    }
}

