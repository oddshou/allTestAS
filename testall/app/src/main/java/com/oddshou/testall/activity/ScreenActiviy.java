package com.oddshou.testall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.oddshou.testall.Logger;
import com.oddshou.testall.R;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;


/**
 * Created by win7 on 2017/5/24.
 */

public class ScreenActiviy extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        String screenInfo = displayMetrics.toString() + " densityDpi= " + displayMetrics.densityDpi;
        Logger.i(TAG, "onCreate: " + screenInfo + "com.oddshou.testall.activity.ScreenActiviy", "oddshou");

        String note = "layout选择 计算公式：min(height, width)/density " + " densityDpi计算公式： density * 160 ";
        String maxdp = "屏幕最大宽度： width/density 单位 dp";
        ((TextView)findViewById(R.id.textView2)).setText(screenInfo + "\n\n" + note + "\n\n" + maxdp);

        /**
         *
         * 总结： 最小屏幕宽度选择依据 屏幕宽高的pix/desity 1080/3.0 == 360
         *  densityDpi 依据 160 * 3.0 = 480，对应xxhdpi
         *
         */

//小米4c
//DisplayMetrics{density=3.0, width=1080, height=1920, scaledDensity=3.0, xdpi=449.704, ydpi=447.412} densityDpi= 480

    }


}
