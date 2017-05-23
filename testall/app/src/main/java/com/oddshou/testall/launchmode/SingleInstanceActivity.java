
package com.oddshou.testall.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.oddshou.testall.Logger;
import com.oddshou.testall.R;

public class SingleInstanceActivity extends Activity {
    private static final String TAG = "SingleInstanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchmode);
        ((TextView)findViewById(R.id.textView1)).setText(TAG);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.e(TAG, "onNew Intent " + this.getClass().getName());
    }

    public void doClick(View v){
        IntentTest.doIntent(this, Integer.valueOf((String)v.getTag()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i(TAG, "onStop: " + this.getClass().getName(), "oddshou");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy: " + this.getClass().getName(), "oddshou");
    }
}
