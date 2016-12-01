package com.oddshou.testall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.oddshou.testall.R;
import com.oddshou.testall.view.TasksCompletedView;

public class ViewTestActivity extends AppCompatActivity {
    private TasksCompletedView mTasksView;

    private int mTotalProgress;
    private int mCurrentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        initVariable();
        initView();

        new Thread(new ProgressRunable()).start();


        //test RecoverButton

    }

    private void initVariable() {
        mTotalProgress = 100;
        mCurrentProgress = 0;
    }

    private void initView() {
        mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
    }

    class ProgressRunable implements Runnable {

        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
