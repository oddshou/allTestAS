package com.oddshou.testall.activity.launchmode;

import android.content.Context;
import android.content.Intent;

/**
 * Created by win7 on 2017/5/22.
 */

public class IntentTest {


    public static void doIntent(Context context, int tag) {
        switch (tag) {
            case 10: //back
                context.startActivity(new Intent(context, SingleTaskActivity.class));
                break;
            case 11:
                context.startActivity(new Intent(context, SingleInstanceActivity.class));
                break;
            case 12:
                Intent intent = new Intent(context, LaunchModeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
