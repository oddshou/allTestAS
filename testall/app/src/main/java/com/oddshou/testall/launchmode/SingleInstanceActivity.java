
package com.oddshou.testall.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.oddshou.testall.R;

public class SingleInstanceActivity extends Activity {
    private static final String TAG = "SingleInstanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchmode);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.e(TAG, "onNew Intent " + this.getClass().getName());
    }

    public void doClick(View v){
        switch (Integer.valueOf((String)v.getTag())) {
            case 10: //back
                startActivity(new Intent(this, SingleTaskActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, SingleInstanceActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, LaunchModeActivity.class));
                break;
            default:
                break;
        }
    }
}
