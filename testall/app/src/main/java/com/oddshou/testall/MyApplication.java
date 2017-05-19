package com.oddshou.testall;

import android.app.Application;
import android.content.res.Configuration;

import nfyg.hskj.hsgamesdk.GameSDKManager;

import static android.content.ContentValues.TAG;

/**
 * Created by win7 on 2016/12/6.
 */

public class MyApplication extends Application {
    //自定义application中执行

    @Override
    public void onCreate() {
        super.onCreate();
        GameSDKManager instance = GameSDKManager.getInstance();
        //初始化
        instance.initWithApp(this);
        //设置显示快捷方式
        instance.showShortcut(true);

        Logger.i(TAG, "onCreate: " + this.getClass().getName(), "oddshou");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.i(TAG, "onTerminate: " + "com.oddshou.testall.MyApplication", "oddshou");
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.i(TAG, "onLowMemory: " + "com.oddshou.testall.MyApplication", "oddshou");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.i(TAG, "onTrimMemory: " + "com.oddshou.testall.MyApplication", "oddshou");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.i(TAG, "onConfigurationChanged: " + "com.oddshou.testall.MyApplication", "oddshou");
    }
}
