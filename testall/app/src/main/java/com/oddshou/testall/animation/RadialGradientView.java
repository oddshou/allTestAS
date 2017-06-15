package com.oddshou.testall.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.oddshou.testall.Logger;

/**
 * Created by win7 on 2017/6/15.
 */

public class RadialGradientView extends Activity {


    private static final String TAG = "RadialGradientView";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestView(this));
    }

    public class TestView extends View{
        Paint mGradientPaint;
        private static final int START_COLOR = 0X00FFFFFF;
        private static final int END_COLOR = 0xFF58FAAC;
        float mTouchx, mTouchy;

        public TestView(Context context) {
            super(context);
            mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            mGradientPaint.setShader()
            setBackgroundColor(Color.GRAY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
//            if (event.getAction() != MotionEvent.ACTION_DOWN) {
////                mTouchx = -1;
//                return super.onTouchEvent(event);
//            }

            mTouchx = event.getX();
            mTouchy = event.getY();
            RadialGradient radialGradient = new RadialGradient(mTouchx, mTouchy, 150,
                    Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
            mGradientPaint.setShader(radialGradient);
            postInvalidate();
            Logger.i(TAG, "onTouchEvent: " + mTouchx + " mTouchy " + mTouchy, "oddshou");


            return super.onTouchEvent(event);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mTouchx > 0) {
                canvas.drawCircle(mTouchx, mTouchy, 50, mGradientPaint);
            }
        }
    }


    public class MyAnimationView extends View {

        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;

        public MyAnimationView(Context context) {
            super(context);

            //背景颜色渐变动画，ObjectAnimator.ofInt，的用法

            ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", RED, BLUE);
            colorAnim.setDuration(3000);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setRepeatCount(ValueAnimator.INFINITE);
            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
            colorAnim.start();

            /**
             * 补充说明：Interpolator 控制动画的变化速率，译为插值器
             * 加减速插值器 AccelerateDecelerateInterpolator
             * 线性插值器 LinearInterpolator
             * 加速插值器 AccelerateInterpolator
             * 减速插值器 DecelerateInterpolator
             *
             */
        }

    }
}
