package com.oddshou.testall.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.oddshou.testall.Logger;
import com.oddshou.testall.R;

/**
 * Created by oddshou on 2017/7/3.
 */

public class DragGridLayout extends GridLayout {

    private static final int scrollSpeed = 20;
    private static final String TAG = "DragGridLayout";
    /**
     * 长按视为拖动图标
     */
    private long dragRespondTime = 500;

    /**
     * 手指按下的点坐标
     */
    private int mDownX, mDownY;

    /**
     * 手指移动的距离
     */
    private int moveX, moveY;

    /**
     * 状态栏高度
     */
    private int mStatusHeight = 0;

    /**
     * 手指按下坐标到屏幕边框的偏移值
     */
    private int mOffsetTop, mOffsetLeft;

    /**
     * 手指按下的position
     */
    private int mDragPosition = 0;

    /**
     * 手指按下的view
     */
    private View mStartDragItemView;

    /**
     * 镜像imageview组件
     */
    private ImageView mDragImageView;

    WindowManager.LayoutParams winLayoutParams;

    /**
     * 是否支持拖动界面
     */
    private boolean isDrag = false;

    /**
     * 震动
     */
    private Vibrator vibrator;

    private WindowManager windowManager;
    private static final long HOVER_TIEM = 200;
    private static final int LONG_PRESS = 2;
    private static final int HOVER =3;
    private boolean changing;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LONG_PRESS:
                    // 绘图缓存
                    mStartDragItemView.setDrawingCacheEnabled(true);
                    // 创建镜像
                    Bitmap mDragBitmap = Bitmap.createBitmap(mStartDragItemView
                            .getDrawingCache());
                    // 释放缓存
                    mStartDragItemView.destroyDrawingCache();
                    isDrag = true;
                    createDragImage(mDragBitmap, mDownX, mDownY);
                    mStartDragItemView.setBackgroundResource(R.drawable.btn_bg_dashgap);
                    break;
                case HOVER:
                    if ( !changing) {
//                        Logger.i(TAG, "dispatchTouchEvent: " + moveX + " : " + moveY, "oddshou");
                        int index = pointToPosition(moveX, moveY);
                        int indexStart = indexOfChild(mStartDragItemView);
                        if (index != -1 && index != indexStart && mCallback != null) {
                            Logger.i(TAG, "dispatchTouchEvent: " + indexStart + " : " + index, "oddshou");
                            changing = true;
                            mCallback.changeItem(index, mStartDragItemView);
                            mDragPosition = index;
                        }
                        changing = false;
                    }
                    break;
            }
        }
    };

    public DragGridLayout(Context context) {
        super(context);
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context);
    }

    public void createAnimation() {
        PropertyValuesHolder pvhTransX =
                PropertyValuesHolder.ofFloat("translationX",  -mStartDragItemView.getWidth()/2 + moveX - mStartDragItemView.getLeft(),  0);
        PropertyValuesHolder pvhTransY =
                PropertyValuesHolder.ofFloat("translationY", -mStartDragItemView.getHeight() +15 + moveY - mStartDragItemView.getTop(),  0);
        final ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(
                mStartDragItemView, pvhTransX, pvhTransY).
                setDuration(500);
        animIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animIn.start();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mOffsetLeft = (int) ev.getRawX() - mDownX;
                mOffsetTop = (int) ev.getRawY() - mDownY;

                Log.i(TAG, "手指按下ev.getX---------->" + mDownX + "  ev.getY-------->"
                        + mDownY);

                mDragPosition = pointToPosition(mDownX, mDownY);

                if (mDragPosition == -1) {
                    return super.dispatchTouchEvent(ev);
                }
                mStartDragItemView = getChildAt(mDragPosition);

                if (mStartDragItemView == null) {
                    return super.dispatchTouchEvent(ev);
                }
                // 500秒长按认为是拖动事件
                handler.removeMessages(LONG_PRESS);
                handler.sendEmptyMessageDelayed(LONG_PRESS, dragRespondTime);

                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrag && mDragImageView != null) {
                    int newMoveX = (int) ev.getX();
                    int newmoveY = (int) ev.getY();
                    updateDragImage(newMoveX, newmoveY);
                    if (Math.abs(moveX - newMoveX) < 10 && Math.abs(moveY - newmoveY) < 10) {
                        //认为没有移动
//                        moveX = newMoveX;
//                        moveY = newmoveY;
                    }else {
                        moveX = newMoveX;
                        moveY = newmoveY;
                        handler.removeMessages(HOVER);
                        handler.sendEmptyMessageDelayed(HOVER, HOVER_TIEM);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                handler.removeMessages(LONG_PRESS);
                handler.removeMessages(HOVER);
                moveX = (int) ev.getX();
                moveY = (int) ev.getY();
                if (isDrag && mDragImageView != null) {
//                handler.removeCallbacks(scrollRunnable);
                    isDrag = false;
                    mStartDragItemView.setBackgroundResource(R.drawable.button_selector);
                    windowManager.removeView(mDragImageView);
                    createAnimation();
                }
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    /**
     * 拖动时添加镜像
     *
     * @param mDragBitmap
     * @param mDownX
     * @param mDownY
     */
    private void createDragImage(Bitmap mDragBitmap, int mDownX, int mDownY) {
        // TODO Auto-generated method stub
        winLayoutParams = new WindowManager.LayoutParams();
        winLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        Log.i(TAG, "createDragImage ev.getX---------->" + mDownX
                + "  ev.getY-------->" + mDownY);
        winLayoutParams.x = -mStartDragItemView.getWidth()/2 + mDownX + mOffsetLeft;
        winLayoutParams.y = -mStartDragItemView.getHeight() + 15 + mDownY + mOffsetTop - mStatusHeight;
        winLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        winLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        winLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(mDragBitmap);
        // 添加到wm管理中
        windowManager.addView(mDragImageView, winLayoutParams);

    }

    /**
     * 拖动item界面更新
     */
    private void updateDragImage(int moveX, int moveY) {
        winLayoutParams.x = -mStartDragItemView.getWidth()/2 + moveX + mOffsetLeft;
        winLayoutParams.y = -mStartDragItemView.getHeight() +15 + moveY + mOffsetTop - mStatusHeight;
        windowManager.updateViewLayout(mDragImageView, winLayoutParams);

    }

    /**
     * Rectangle used for hit testing children
     */
    private Rect mTouchFrame;

    /**
     * 动画过程中，这个方法不准确
     * @param x
     * @param y
     * @return
     */
    public int pointToPosition(int x, int y) {
        if (changing) {
            return -1;
        }
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);

            if (child.getVisibility() == View.VISIBLE && child.getAnimation() == null) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(rect);
        statusHeight = rect.top;
        if (statusHeight == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public interface ChangeItemCallback{
        void changeItem(int indexEnd, View childView);
    }

    private ChangeItemCallback mCallback;
    public void setChangeItemCallback(ChangeItemCallback callback) {
        this.mCallback = callback;
    }

}
