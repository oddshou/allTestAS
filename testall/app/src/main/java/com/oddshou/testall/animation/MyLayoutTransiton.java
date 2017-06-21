package com.oddshou.testall.animation;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by win7 on 2017/6/20.
 */

public class MyLayoutTransiton extends LayoutTransition {



//    public void addChild(ViewGroup parent, View child) {
//        addChild(parent, child, true);
//    }


    public MyLayoutTransiton() {
        super();
    }

    @Override
    public void addChild(ViewGroup parent, View child) {
        super.addChild(parent, child);
    }

    @Override
    public void showChild(ViewGroup parent, View child, int oldVisibility) {
        super.showChild(parent, child, oldVisibility);
    }

    @Override
    public void removeChild(ViewGroup parent, View child) {
        super.removeChild(parent, child);
    }
}
