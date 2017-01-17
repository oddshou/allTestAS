package com.oddshou.testall;

import android.app.Application;

import nfyg.hskj.hsgamesdk.GameSDKManager;

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


    }
}
