package com.oddshou.testall.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by win7 on 2016/8/29.
 */
public class CircleProgressButton extends Button {

    Paint paint,textpaint;
    RectF area;
    int value = 100;
    LinearGradient shader;

    public CircleProgressButton(Context context) {
        super(context);
        init();
    }

    public CircleProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setProgress(int value){
        this.value = value;
        invalidate();
    }

    public void init() {
        paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        textpaint = new Paint();
        textpaint.setTextSize(50f);
        textpaint.setColor(Color.WHITE);
        area = new RectF(100, 100, 500, 500);

        shader =new LinearGradient(0, 0, 400, 0, new int[] {
                Color.BLUE, Color.WHITE}, null,
                Shader.TileMode.CLAMP);
        paint.setShader(shader);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
//        canvas.drawColor(Color.GRAY);
        canvas.drawArc(area, 120, 360*value/100 , false, paint);
        canvas.drawText(value+"%", 270, 290, textpaint);
    }
}
