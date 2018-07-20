package com.lfyt.mobile.android.logsampleapp;

import android.app.Application;

public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        L.start();
    }
}
