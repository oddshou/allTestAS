package com.oddshou.testall.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddshou.testall.R;

/**
 * Created by win7 on 2017/6/22.
 */

public class DragBack extends Activity {
   private static final String TAG = "DragBack";
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        container = (LinearLayout) findViewById(R.id.container);
        container.setBackgroundResource(android.R.color.white);

        createView();
    }

    private void createView(){
        DragHelperLayout2 dragHelperLayout = new DragHelperLayout2(this);
        dragHelperLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        container.addView(dragHelperLayout);

        TextView btn = getTextView("hello world");
        dragHelperLayout.addView(btn, 0);

        TextView btn2 = getTextView("hello world2");
        dragHelperLayout.addView(btn2);
        TextView btn3 = getTextView("hello world3");
        dragHelperLayout.addView(btn3);

    }

    @NonNull
    private TextView getTextView(String text) {
        TextView btn = new TextView(this);
        btn.setText(text);
        btn.setBackgroundResource(R.drawable.button_selector);
        btn.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(200, 100);
        layoutParam.setMargins(10, 10, 10, 10);
        btn.setLayoutParams(layoutParam);

        return btn;
    }

    public class DragHelperLayout2 extends LinearLayout{

        private final ViewDragHelper mDragger;

        public DragHelperLayout2(Context context) {
            super(context);
            mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback()
            {
                @Override
                public boolean tryCaptureView(View child, int pointerId)
                {
                    return true;
                }

                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx)
                {
                    return left;
                }

                @Override
                public int clampViewPositionVertical(View child, int top, int dy)
                {
                    return top;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return mDragger.shouldInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mDragger.processTouchEvent(event);
            return true;
        }
    }
}
