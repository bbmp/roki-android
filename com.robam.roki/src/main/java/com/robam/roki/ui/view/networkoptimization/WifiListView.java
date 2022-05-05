package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.legent.utils.api.WifiUtils;
import com.legent.utils.api.WifiUtils.WifiScanner;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class WifiListView extends FrameLayout {

    public interface OnWifiSetCallback {
        void onCompleted(String ssid, String pwd);
    }

    @InjectView(R.id.wifiList)
    ListView wifiList;

    Context cx;
    Adapter adapter;
    OnWifiSetCallback callback;
    WifiScanner scanner;
    WifiAuthDialog dlg;
    WifiConnectPage wifiConnectPage;
    public List<ScanResult> scanResultList=Lists.newArrayList();
    public WifiListView(Context context, WifiAuthDialog dlg,WifiConnectPage wifiConnectPage) {
        super(context);
        this.dlg=dlg;
        this.wifiConnectPage=wifiConnectPage;
        init(context, null);
    }

    public WifiListView(Context context, OnWifiSetCallback callback, WifiAuthDialog dlg,WifiConnectPage wifiConnectPage) {
        super(context);
        this.dlg=dlg;
        this.callback = callback;
        this.wifiConnectPage=wifiConnectPage;
        init(context, null);
    }

    public WifiListView(Context context, AttributeSet attrs, WifiAuthDialog dlg,WifiConnectPage wifiConnectPage) {
        super(context, attrs);
        this.wifiConnectPage=wifiConnectPage;
        this.dlg=dlg;
        init(context, attrs);
    }

    public WifiListView(Context context, AttributeSet attrs, int defStyle, WifiAuthDialog dlg,WifiConnectPage wifiConnectPage) {
        super(context, attrs, defStyle);
        this.dlg=dlg;
        this.wifiConnectPage=wifiConnectPage;
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        scanner = WifiUtils.startScan(cx, adapter.callabck);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (scanner != null) {
            scanner.stopScanning();
            scanner = null;
        }
    }

    void init(Context cx, AttributeSet attrs) {

        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.view_wifi_list,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            adapter = new Adapter();
            wifiList.setAdapter(adapter);
            wifiList.setVerticalScrollBarEnabled(true);
        }
    }

    @OnItemClick(R.id.wifiList)
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        TextView txtSsid = view.findViewById(R.id.txtSsid);
        String wifiName = txtSsid.getText().toString();
        ScanResult sr = scanResultList.get(position);
        Message  msg=new Message();
        msg.obj=sr;
        msg.what=0;
        wifiConnectPage.handler.sendMessage(msg);
         dlg.dismiss();
    }

    class Adapter extends BaseAdapter {
        String currentSsid;
        public boolean isConnected(int position) {
            ScanResult sr = scanResultList.get(position);
            if (sr == null)
                return false;
            boolean isConnected = Objects.equal(sr.SSID, currentSsid);
            return isConnected;
        }

        WifiUtils.WifiScanCallback callabck = new WifiUtils.WifiScanCallback() {

            @Override
            public void onScanWifi(List<ScanResult> scanList) {
                scanResultList.clear();
                if (scanList != null) {
                    scanResultList.addAll(scanList);
                }
                currentSsid = WifiUtils.getSSIDByNetworkId(cx);
                notifyDataSetChanged();
            }
        };

        @Override
        public int getCount() {
            return scanResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return scanResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(
                        R.layout.view_wifi_list_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            ScanResult sr = scanResultList.get(position);
            vh.showWifiInfo(sr, isConnected(position));

            return convertView;
        }

        class ViewHolder {

            @InjectView(R.id.txtSsid)
            TextView txtSsid;

            @InjectView(R.id.imgLock)
            ImageView imgLock;

            @InjectView(R.id.imgWifi)
            ImageView imgWifi;

            @InjectView(R.id.imgSelect)
            ImageView imgSelect;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            public void showWifiInfo(ScanResult sr, boolean isConnected) {
                txtSsid.setText(sr.SSID);
                imgWifi.setSelected(isConnected);

                imgSelect.setVisibility(isConnected ? VISIBLE : INVISIBLE);
                boolean isEncrypted = WifiUtils.isEncrypted(sr);
                imgLock.setVisibility(isEncrypted ? VISIBLE : GONE);

                txtSsid.setTextColor(cx.getResources().getColor(R.color.black));
            }
        }
    }

}
