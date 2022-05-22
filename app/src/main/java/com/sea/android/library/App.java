package com.sea.android.library;

import android.app.Application;

public class App extends Application {

    public static final String TAG = "App";
    public static final String FILE_AUTHORITIES = BuildConfig.APPLICATION_ID + ".provider";

    @Override
    public void onCreate() {
        super.onCreate();
    }

}