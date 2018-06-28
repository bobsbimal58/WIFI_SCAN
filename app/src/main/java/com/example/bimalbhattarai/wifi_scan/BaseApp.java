package com.example.bimalbhattarai.wifi_scan;

import android.app.Application;

/**
 * Created by Bimal Bhattarai on 6/21/2018.
 */

public class BaseApp extends Application{
    private static BaseApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application getApp() {
        return mInstance;
    }
}
