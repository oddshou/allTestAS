package com.oddshou.testall.drawable;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.oddshou.testall.R;

/**
 * Created by win7 on 2017/5/23.
 */

public class DrawableMainActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_main);
        //demo 1 , transition 效果
        final ImageView image = (ImageView) findViewById(R.id.imageView);
        Resources res = getResources();
        TransitionDrawable transition = (TransitionDrawable)
                res.getDrawable(R.drawable.transition);
        image.setImageDrawable(transition);
        transition.startTransition(5000);

    }
}
