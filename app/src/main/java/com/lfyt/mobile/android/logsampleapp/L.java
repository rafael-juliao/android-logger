package com.lfyt.mobile.android.logsampleapp;


import android.app.Activity;

import com.lfyt.mobile.android.log.Logger;

public class L extends Logger {


    static void start(){
        LOG_TIME = true;
        Logger.setup("MyApp", new TagObjectInterface() {
            @Override
            public String tag(Object object) {
                return TAG(object);
            }
        });
    }

    private static String TAG(Object object) {

        if( object instanceof MyAwesomeClass ){
            return "AWESOME";
        }

        if( object instanceof Activity ){
            return "ACTIVITY";
        }

        return "APP";
    }

}
