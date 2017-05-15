package com.oddshou.testall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.oddshou.testall.R;
import com.oddshou.testall.jniClass.JniUtils;


public class JniActivity extends AppCompatActivity {

    private Button mBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        mBtn = (Button) findViewById(R.id.btnJni);
        JniUtils jniUtils = new JniUtils();
        mBtn.setText(jniUtils.getFirstMethed()/* + jniUtils.add(1, 2)*/);
    }
}
