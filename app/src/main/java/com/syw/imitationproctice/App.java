package com.syw.imitationproctice;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author: Shui
 * @data: 2018/6/2
 * @description:
 */

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
