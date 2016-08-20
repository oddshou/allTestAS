package com.oddshou.testall.activity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oddshou.testall.R;
import com.oddshou.testall.receiver.NetworkReceiver;

public class NetwrokLogActivity extends AppCompatActivity implements NetworkReceiver.WifiListener {

    private EditText editLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netwrok_log);

        Button btnClear = (Button) findViewById(R.id.btnClear);
        editLog = (EditText) findViewById(R.id.editLog);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLog.setText("");
            }
        });
        NetworkReceiver.setWifiConnectListen(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkReceiver.removeWifiListener(this);
    }

    @Override
    public void networkChange(int currentNetwork, NetworkInfo networkInfo, String msg) {
        editLog.setText(editLog.getText() + "\n" + msg);

        switch (currentNetwork) {
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
}
