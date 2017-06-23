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
import android.widget.LinearLayout;
import android.widget.TextView;

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
                View captureView;
                Point capturePoint;
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
//                    mDragger.settleCapturedViewAt()
                    super.onViewReleased(releasedChild, xvel, yvel);
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
                    capturePoint.x = captureView.getLeft();
                    capturePoint.y = captureView.getTop();
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
                    Logger.i(TAG, "getOrderedChildIndex: " + index, "oddshou");
                    return super.getOrderedChildIndex(index);
                }

                @Override
                public int getViewHorizontalDragRange(View child) {
                    Logger.i(TAG, "getViewHorizontalDragRange: ", "oddshou");
                    return super.getViewHorizontalDragRange(child);
                }

                @Override
                public int getViewVerticalDragRange(View child) {
                    Logger.i(TAG, "getViewVerticalDragRange: ", "oddshou");
                    return super.getViewVerticalDragRange(child);
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
