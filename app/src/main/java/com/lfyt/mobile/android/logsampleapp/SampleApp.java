package com.lfyt.mobile.android.logsampleapp;

import android.app.Application;

import java.util.Date;

public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        L.start();
        L.D(this, "The Application Has Started");
        L.D(this, "The Current Time is %d", (int) new Date().getTime());
    }
}
