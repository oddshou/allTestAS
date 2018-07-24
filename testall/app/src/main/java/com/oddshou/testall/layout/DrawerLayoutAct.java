package com.oddshou.testall.layout;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.oddshou.testall.Logger;
import com.oddshou.testall.R;


/**
 * Created by oddshou on 2017/7/4.
 */

public class DrawerLayoutAct extends Activity {
    public static final String TAG = "DrawerLayoutAct";

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        MenuFragment menuFragment = new MenuFragment();

        fragmentTransaction.add(R.id.content_container, mainFragment);
        fragmentTransaction.add(R.id.left_drawer_container, menuFragment);
        fragmentTransaction.commit();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Logger.i(TAG, "onDrawerSlide: ", "oddshou");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Logger.i(TAG, "onDrawerOpened: ", "oddshou");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Logger.i(TAG, "onDrawerClosed: ", "oddshou");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Logger.i(TAG, "onDrawerStateChanged: " + newState, "oddshou");
            }
        });
    }
}
