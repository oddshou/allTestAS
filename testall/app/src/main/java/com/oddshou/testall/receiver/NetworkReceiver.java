package com.oddshou.testall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;

import com.oddshou.testall.Logger;

import java.util.ArrayList;

/**
 * Created by win7 on 2016/8/20.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkReceiver";
    private Context mContext;

    //listen for network
    private static ArrayList<WifiListener> wifiListeners = new ArrayList<WifiListener>();

    public interface WifiListener {
        void networkChange(int currentNetwork, NetworkInfo networkInfo, String msg);
    }

    public static void setWifiConnectListen(WifiListener wifiListener) {
        if (!wifiListeners.contains(wifiListener)) {
            wifiListeners.add(wifiListener);
        }
    }

    public static void removeWifiListener(WifiListener wifiListener) {
        if (wifiListeners.contains(wifiListener)) {
            wifiListeners.remove(wifiListener);
        }
    }

    private void notifyNewworkChange(int currentNetwork, NetworkInfo networkInfo, String msg) {
        for (WifiListener wifiListener :
                wifiListeners) {
            wifiListener.networkChange(currentNetwork, networkInfo, msg);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);


            NetworkInfo info = getActiveNetwork(context);
            if (null != info) {//无网络时 info值为空
                int type = info.getType();//type = 0 为mobile 状态  = 1 为 wifi 状态
                String name = info.getTypeName();
                NetworkInfo.State state = info.getState();
                if (state == NetworkInfo.State.CONNECTED) {
                    if (type == 0 || name.equals("mobile")) {//mobile状态  停止下载
                        showMsg("connected mobile", context);

                    } else if (type == 1 || name.equals("WIFI")) {
                        showMsg("connected WIFI " + info.toString(), context);
                        boolean FAILOVER_CONNECTION = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
                        boolean EXTRA_NO_CONNECTIVITY = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                        String EXTRA_REASON = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
                        NetworkInfo EXTRA_OTHER_NETWORK_INFO = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
                        String EXTRA_EXTRA_INFO = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
                        Logger.e(TAG, "onReceive: FAILOVER_CONNECTION " + FAILOVER_CONNECTION
                                +" ---EXTRA_NO_CONNECTIVITY "+ EXTRA_NO_CONNECTIVITY
                                +" ---EXTRA_REASON "+ EXTRA_REASON
                                +" ---EXTRA_OTHER_NETWORK_INFO "+ EXTRA_OTHER_NETWORK_INFO
                                +" ---EXTRA_EXTRA_INFO "+ EXTRA_EXTRA_INFO
                                , "oddshou");


                        //wifi 测试， mx4
                        //连接游戏wifi
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "花生游戏礼包专线"
                        //无网络连接普通密码wifi
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "SGV-test"
                        //密码wifi重连游戏wifi
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO [type: WIFI[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: <unknown ssid>, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "花生游戏礼包专线"
                        //无网络连接花生wifi
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "花生游戏礼包专线"
                        //游戏wifi切换至密码wifi
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO [type: WIFI[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: <unknown ssid>, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "SGV-test"

                        //游戏wifi切换至密码wifi 小米4，时间太长
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO [type: WIFI[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: <unknown ssid>, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO <unknown ssid>
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO ctnet
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO ctnet
                        //FAILOVER_CONNECTION false ---EXTRA_NO_CONNECTIVITY false ---EXTRA_REASON null ---EXTRA_OTHER_NETWORK_INFO null ---EXTRA_EXTRA_INFO "SGV-test"

                    }
                }
            } else {
                //无网络
                showMsg("connected none ", context);
            }
        }


        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                if (isConnected) {
                    String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
                    WifiInfo wifiInfo = (WifiInfo) intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
//                    showMsg("isConnected" + isConnected + " bssid: " + bssid + "----wifiinfo: " + wifiInfo, context);
//                    EXTRA_NETWORK_INFO
//                    EXTRA_BSSID
//                    EXTRA_WIFI_INFO
                    NetworkInfo EXTRA_NETWORK_INFO = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    String EXTRA_BSSID = intent.getStringExtra(WifiManager.EXTRA_BSSID);
                    WifiInfo EXTRA_WIFI_INFO = (WifiInfo)intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

                    Logger.e(TAG, "onReceive: EXTRA_NETWORK_INFO " + EXTRA_NETWORK_INFO
                                    +" ---EXTRA_BSSID "+ EXTRA_BSSID
                                    +" ---EXTRA_WIFI_INFO "+ EXTRA_WIFI_INFO
                            , "oddshou");
                    //无网络连接密码wifi
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d2, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -38, Link speed: 72Mbps, Frequency: 2412MHz, Net ID: 32, Metered hint: false, score: 60
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID 0e:69:6c:0c:58:d2 ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d2, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -38, Link speed: 72Mbps, Frequency: 2412MHz, Net ID: 32, Metered hint: false, score: 60
                    //无网络连接花生wifi
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "花生游戏礼包专线", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: 花生游戏礼包专线, BSSID: 0a:69:6c:0b:44:2e, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -58, Link speed: 65Mbps, Frequency: 2462MHz, Net ID: 31, Metered hint: false, score: 60
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "花生游戏礼包专线", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID 0a:69:6c:0b:44:2e ---EXTRA_WIFI_INFO SSID: 花生游戏礼包专线, BSSID: 0a:69:6c:0b:44:2e, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -58, Link speed: 65Mbps, Frequency: 2462MHz, Net ID: 31, Metered hint: false, score: 60
                    //花生wifi切换至密码wifi
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: <unknown ssid>, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: <unknown ssid>, BSSID: <none>, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -127, Link speed: 0Mbps, Frequency: 2462MHz, Net ID: -1, Metered hint: false, score: 0
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d2, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -40, Link speed: 72Mbps, Frequency: 2412MHz, Net ID: 32, Metered hint: false, score: 60
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID 0e:69:6c:0c:58:d2 ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d2, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -40, Link speed: 72Mbps, Frequency: 2412MHz, Net ID: 32, Metered hint: false, score: 60
                    //密码wifi切换至花生wifi
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: <unknown ssid>, roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: <unknown ssid>, BSSID: <none>, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: DISCONNECTED, RSSI: -64, Link speed: 72Mbps, Frequency: 2462MHz, Net ID: -1, Metered hint: false, score: 0
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "花生游戏礼包专线", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: 花生游戏礼包专线, BSSID: 0a:69:6c:0b:44:2e, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -66, Link speed: 72Mbps, Frequency: 2462MHz, Net ID: 33, Metered hint: false, score: 60
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "花生游戏礼包专线", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID 0a:69:6c:0b:44:2e ---EXTRA_WIFI_INFO SSID: 花生游戏礼包专线, BSSID: 0a:69:6c:0b:44:2e, MAC: 38:bc:1a:bd:7b:f0, Supplicant state: COMPLETED, RSSI: -66, Link speed: 72Mbps, Frequency: 2462MHz, Net ID: 33, Metered hint: false, score: 60

                    //无网络连接密码wifi
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID null ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d3, MAC: 10:2a:b3:2f:fd:0f, Supplicant state: COMPLETED, RSSI: -43, Link speed: 54Mbps, Frequency: 5765MHz, Net ID: 9, Metered hint: false, score: 60
                    //EXTRA_NETWORK_INFO [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: "SGV-test", roaming: false, failover: false, isAvailable: true, isConnectedToProvisioningNetwork: false] ---EXTRA_BSSID 0e:69:6c:0c:58:d3 ---EXTRA_WIFI_INFO SSID: SGV-test, BSSID: 0e:69:6c:0c:58:d3, MAC: 10:2a:b3:2f:fd:0f, Supplicant state: COMPLETED, RSSI: -43, Link speed: 54Mbps, Frequency: 5765MHz, Net ID: 9, Metered hint: false, score: 60

                } else {
                    Logger.e(TAG, "onReceive: connected false ", "oddshou");
                }
            }
        }
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo info = getActiveNetwork(context);
            //这个广播只能监测到wifi打开关闭，切换wifi，以及wifi是否可用不能表示

            int EXTRA_WIFI_STATE = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            int EXTRA_PREVIOUS_WIFI_STATE = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, -1);
//            showMsg("connected wifiChange " + wifiState, context);
            Logger.e(TAG, "onReceive: EXTRA_WIFI_STATE " + EXTRA_WIFI_STATE
                            +" ---EXTRA_PREVIOUS_WIFI_STATE "+ EXTRA_PREVIOUS_WIFI_STATE
                    , "oddshou");
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {


            }
        }

        if (WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION.equals(intent.getAction())) {
            boolean EXTRA_SUPPLICANT_CONNECTED = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            Logger.e(TAG, "onReceive: EXTRA_SUPPLICANT_CONNECTED " + EXTRA_SUPPLICANT_CONNECTED, "oddshou");
        }

    }

    private void showMsg(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Logger.e(TAG, "showMsg: " + msg, "oddshou");
        notifyNewworkChange(0, null, msg);
    }


    /**
     * 检测活动网络连接信息
     */
    public static NetworkInfo getActiveNetwork(Context context) {
        if (context == null)
            return null;
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
            return null;
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
        return aActiveInfo;
    }

}
