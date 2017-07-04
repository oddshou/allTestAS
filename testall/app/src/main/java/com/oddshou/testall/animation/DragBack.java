package com.oddshou.testall.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.oddshou.testall.Logger;
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
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dragHelperLayout.setBackgroundColor(Color.GRAY);

        container.addView(dragHelperLayout);

        Button btn = getTextView("hello world");
        dragHelperLayout.addView(btn, 0);

        Button btn2 = getTextView("hello world2");
        dragHelperLayout.addView(btn2);
        Button btn3 = getTextView("hello world3");
        dragHelperLayout.addView(btn3);

    }

    @NonNull
    private Button getTextView(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setBackgroundResource(R.drawable.button_selector);
        btn.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(200, 100);
        layoutParam.setMargins(10, 10, 10, 10);
        btn.setLayoutParams(layoutParam);

        return btn;
    }

    public class DragHelperLayout2 extends GridLayout{

        private final ViewDragHelper mDragger;

        ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
            View captureView;
            Point capturePoint = new Point();
            @Override
            public boolean tryCaptureView(View child, int pointerId)
            {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx)
            {
                //返回child 最终要定的位置
//                    Logger.i(TAG, "clampViewPositionHorizontal: " + left, "oddshou");
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy)
            {
//                    Logger.i(TAG, "clampViewPositionVertical: " + top, "oddshou");
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                Logger.i(TAG, "onViewReleased: " + xvel + "  " + yvel, "oddshou");
                mDragger.settleCapturedViewAt(capturePoint.x, capturePoint.y);
                invalidate();
//                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public void onViewDragStateChanged(int state) {

                Logger.i(TAG, "onViewDragStateChanged: " + state, "oddshou");
                super.onViewDragStateChanged(state);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                Logger.i(TAG, "onViewCaptured: " + activePointerId, "oddshou");
                captureView = capturedChild;
                capturePoint.x = capturedChild.getLeft();
                capturePoint.y = capturedChild.getTop();

                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                Logger.i(TAG, "onEdgeTouched: "+ edgeFlags + "  " + pointerId, "oddshou");
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Logger.i(TAG, "onEdgeLock: " +edgeFlags, "oddshou");
                return super.onEdgeLock(edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Logger.i(TAG, "onEdgeDragStarted: " + edgeFlags + " " + pointerId, "oddshou");
                super.onEdgeDragStarted(edgeFlags, pointerId);
            }

            @Override
            public int getOrderedChildIndex(int index) {
                //这个方法决定的child的层级，从后往前找
                Logger.i(TAG, "getOrderedChildIndex: " + index, "oddshou");
                return super.getOrderedChildIndex(index);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                int i = /*getMeasuredWidth() - child.getMeasuredWidth()*/ getMeasuredWidth() - child.getLeft();
                Logger.i(TAG, "getViewHorizontalDragRange: " + i, "oddshou");
                return i;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                int i = getMeasuredHeight() - child.getMeasuredHeight();
                Logger.i(TAG, "getViewVerticalDragRange: " + getMeasuredHeight() + " - " + child.getMeasuredHeight(), "oddshou");

                return i;
            }
        };

        public DragHelperLayout2(Context context) {
            super(context);
            mDragger = ViewDragHelper.create(this, 1.0f, callback);
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

        @Override
        public void computeScroll() {
            if (mDragger.continueSettling(true)) {
                invalidate();
            }
            super.computeScroll();
        }
    }
}
