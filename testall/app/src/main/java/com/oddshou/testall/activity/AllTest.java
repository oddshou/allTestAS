package com.oddshou.testall.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.oddshou.testall.R;

/**
 * Created by win7 on 2017/6/21.
 */

public class AllTest extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        Button btn = new Button(this);
        btn.setText("hello");
//        container.addView(btn);

        container.addView(new CloneView(this, btn));


    }

    public class CloneView extends View{

        private View viewSrc;

        public CloneView(Context context) {
            super(context);
        }

        public CloneView(Context context, View view) {
            this(context);
            viewSrc = view;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            viewSrc.draw(canvas);
        }
    }
}
