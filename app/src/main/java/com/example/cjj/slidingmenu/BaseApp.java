package com.example.cjj.slidingmenu;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by CJJ on 2016/8/26.
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
