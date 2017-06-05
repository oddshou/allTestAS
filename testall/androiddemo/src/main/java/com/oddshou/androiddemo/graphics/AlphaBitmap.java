/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oddshou.androiddemo.graphics;

import com.oddshou.androiddemo.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;

public class AlphaBitmap extends GraphicsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        private Bitmap mBitmap;
        private Bitmap mBitmap2;
        private Bitmap mBitmap3;
        private Shader mShader;

        /**
         * 说明： 在这个bitmap中绘制的颜色都是无效的
         *
         * 绘制三种方式的bitmap，第一种直接将bitmap对象绘制出来
         * 第二种，获取bitmap的extractAlpha，绘制一个和所去bitmap形状一样的图片
         * 第三种，创建bitmap进行绘制，并通过xfermode 和图形的alpha值来绘制文字，最后通过设置画笔 shader 绘制出来，文字颜色是图片背景色
         *
         *
         * @param bm
         */
        private static void drawIntoBitmap(Bitmap bm) {
            float x = bm.getWidth();
            float y = bm.getHeight();
            Canvas c = new Canvas(bm);
            Paint p = new Paint();
            p.setAntiAlias(true);

            p.setAlpha(0x80);
            p.setColor(Color.GREEN);
            c.drawCircle(x/2, y/2, x/2, p);

            p.setAlpha(0x30);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            p.setTextSize(60);
            p.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = p.getFontMetrics();
            c.drawText("AlphaAlphaAlphaAlphaAlpha", x/2, (y-fm.ascent)/2, p);
        }

        public SampleView(Context context) {
            super(context);
            setFocusable(true);

            InputStream is = context.getResources().openRawResource(R.drawable.app_sample_code);
            mBitmap = BitmapFactory.decodeStream(is);
            mBitmap2 = mBitmap.extractAlpha();
            mBitmap3 = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
            drawIntoBitmap(mBitmap3);

            mShader = new LinearGradient(0, 0, 100, 70, new int[] {
                                         /*Color.RED, */Color.GREEN, Color.BLUE },
                                         null, Shader.TileMode.MIRROR);

            //关闭硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.GRAY);

            Paint p = new Paint();
            p.setAntiAlias(true);
            float y = 10;

            canvas.drawBitmap(mBitmap, 10, y, p);
            y += mBitmap.getHeight() + 10;
            canvas.drawBitmap(mBitmap2, 10, y, p);
            y += mBitmap2.getHeight() + 10;
//            p.setColor(Color.RED);
            p.setShader(mShader);
//            canvas.drawCircle(110, y + 100,100, p);
//            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.drawBitmap(mBitmap3, 10, y, p);
        }
    }
}

