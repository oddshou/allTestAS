package com.oddshou.testall.animation;

import android.animation.AnimatorSet;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oddshou.testall.R;

/**
 * Created by win7 on 2017/6/15.
 */

public class RadialGradientView extends Activity {


    private static final String TAG = "RadialGradientView";
    private TestView mTestview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestview = new TestView(this);
        setContentView(R.layout.activity_container);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        container.addView(mTestview, 200, 100);


        mTestview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RadialGradientView.this, "你点我了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class TestView extends View implements ValueAnimator.AnimatorUpdateListener{
        Paint mGradientPaint;
        private static final int START_COLOR = 0X00FFFFFF;
        private static final int END_COLOR = 0xFF58FAAC;
        float mTouchx;
        float mTouchy;
        float mRadius;

        public int getBalpha() {
            return balpha;
        }

        public void setBalpha(int balpha) {
            this.balpha = balpha;
        }

        int balpha = 255;

        public float getMRadius() {
            return mRadius;
        }

        public void setMRadius(float mRadius) {
            this.mRadius = mRadius;
        }


        AnimatorSet animationSet;

        public TestView(Context context) {
            super(context);
            mGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            setBackgroundColor(Color.GREEN);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_DOWN) {
                return super.onTouchEvent(event);
            }

            mTouchx = event.getX();
            mTouchy = event.getY();
            RadialGradient radialGradient = new RadialGradient(mTouchx, mTouchy, 150,
                    0x00dddddd, 0x00FFFFFF, Shader.TileMode.CLAMP);
//            mGradientPaint.setShader(radialGradient); //shader暂不使用
            mGradientPaint.setColor(Color.GRAY);
//            Logger.i(TAG, "onTouchEvent: " + mTouchx + " mTouchy " + mTouchy, "oddshou");
            startAnimation();
            return super.onTouchEvent(event);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mGradientPaint.setAlpha(balpha);
            canvas.drawCircle(mTouchx, mTouchy, mRadius, mGradientPaint);
        }

        private void createAnimation() {
            if (animationSet == null) {
                animationSet = new AnimatorSet();
                ValueAnimator animation1 = ObjectAnimator.ofFloat(this, "mRadius", 0, 200);
                animation1.setDuration(300);
                animation1.setInterpolator(new LinearInterpolator());
                animation1.addUpdateListener(this);

                ValueAnimator animator2 = ObjectAnimator.ofInt(this, "balpha", 255, 0);
                animator2.setDuration(300);
                animator2.setInterpolator(new DecelerateInterpolator());
                animationSet.play(animation1).with(animator2);


            }
        }

        public void startAnimation(){
            //构建动画，执行动画
            createAnimation();
            animationSet.start();
        }
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    }

}
