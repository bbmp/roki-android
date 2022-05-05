package com.legent.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.legent.events.ConnectionModeChangedEvent;
import com.legent.events.WifiConnectEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.api.NetworkUtils;

public class ConnectivtyService extends AbsService {
    static final String TAG = "roki_rent1";
    static final String CONNECTIVITY_CHANGE = ConnectivityManager.CONNECTIVITY_ACTION;

    public final static int ConnectionMode_Unknown = -1;
    public final static int ConnectionMode_Broken = 0;
    public final static int ConnectionMode_Wifi = 1;
    public final static int ConnectionMode_Mobil = 2;

    static private ConnectivtyService instance = new ConnectivtyService();

    synchronized static public ConnectivtyService getInstance() {
        return instance;
    }

    public ConnectivityManager getCm() {
        return cm;
    }

    private ConnectivityManager cm;

    private ConnectivtyService() {

    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
        cm = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
        cx.registerReceiver(receiver, new IntentFilter(CONNECTIVITY_CHANGE));
        registerWIFI(cx);
    }

    private void registerWIFI(Context cx) {
        IntentFilter mWifiFilter = new IntentFilter();
        mWifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        cx.registerReceiver(mWifiConnectReceiver, mWifiFilter);
    }

    private BroadcastReceiver mWifiConnectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Wifi onReceive action = " + intent.getAction());
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int message = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                Log.i(TAG, "liusl wifi onReceive msg=" + message);
                switch (message) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.i(TAG, "WIFI_STATE_DISABLED");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.i(TAG, "WIFI_STATE_DISABLING");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.i(TAG, "WIFI_STATE_ENABLED");
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.i(TAG, "WIFI_STATE_ENABLING");
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.i(TAG, "WIFI_STATE_UNKNOWN");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void dispose() {
        super.dispose();
        cx.unregisterReceiver(receiver);
    }

    public void checkConnectivty() {
        int mode = ConnectionMode_Broken;
        NetworkInfo netInfo;
        boolean useWifi = false;
        boolean useMobile = false;
        try {
            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netInfo != null) {
                State wifiState = netInfo.getState();
                useWifi = (wifiState == State.CONNECTED);
            }

            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (netInfo != null) {
                State mobileState = netInfo.getState();
                useMobile = (mobileState == State.CONNECTED);
            }

            if (useWifi) {
                mode = ConnectionMode_Wifi;
                String localIP = NetworkUtils.getLocalIp();
                String gateIP = NetworkUtils.getGatewayIpAddress(cx);

                Log.d(TAG, String.format("[wifi] Gate:%s Local:%s", gateIP,
                        localIP));
                EventUtils.postEvent(new WifiConnectEvent(gateIP, localIP));
            } else if (useMobile) {
                mode = ConnectionMode_Mobil;
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            ConnectionModeChangedEvent event = new ConnectionModeChangedEvent(mode);
            EventUtils.postEvent(event);
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnectivty();
        }
    };

}
