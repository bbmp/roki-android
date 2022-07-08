package com.robam.roki.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.base.BaseDialog;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.ui.BleRssiDevice;
import com.robam.roki.R;
import com.robam.roki.ui.adapter3.RvDeviceBluetoothAdapter;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class IanSend {
// 广播地址
    private static final String BROADCAST_IP = "232.10.11.13";// 广播IP

    private static final int BROADCAST_INT_PORT = 53147; // 不同的port对应不同的socket发送端和接收端

    MulticastSocket ms;// 用于接收广播信息

    DatagramSocket sender;// 数据流套接字 相当于码头，用于发送信息

    InetAddress broadAddress;// 广播地址

    private Activity activity;
    private Context cx;

    /**
     * 蓝牙设备adapter
     */
    private RvDeviceBluetoothAdapter rvDeviceAdapter;
    /**
     * dialog
     */
    private BaseDialog baseDialog;

    public IanSend(Activity activity, Context context) {
        try {
            // 初始化\
            this.activity = activity;
            this.cx = context;
            ms = new MulticastSocket(BROADCAST_INT_PORT);
            broadAddress = InetAddress.getByName(BROADCAST_IP);
            sender = new DatagramSocket();
        } catch (Exception e) {
        }
    }

    Timer t = new Timer();
    private final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                ms.receive(dp);
                String received_data = new String(dp.getData(), dp.getOffset(), dp.getLength(), StandardCharsets.UTF_8);
                Log.e("yidao", "run: "+ received_data );
                JSONObject jsonObject = new JSONObject(received_data);
                String guid = jsonObject.optString("guid");
                //TODO 是否showDialog
                if (Plat.deviceService.lookupChild(guid) == null) {
                    if (onDevicelistener != null) {
                        onDevicelistener.onDeviceGuid(guid);
                    }
                }
//                showDialog(guid);
//                addDevice(guid);
            } catch (Exception e)
            {
            }
        }
    };
    private void showDialog(String guid){
        if(baseDialog == null) {
            baseDialog = new BaseDialog(activity);
            baseDialog.setContentView(R.layout.dialog_device_wifi_610);
            baseDialog.setCanceledOnTouchOutside(true);
            baseDialog.setGravity(Gravity.BOTTOM);
            baseDialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
            baseDialog.show();
            baseDialog.findViewById(R.id.btn_add_device).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDevice(guid);
                }
            });
        }else {
            if (baseDialog.isShowing()) {

            }else {
                baseDialog.show();
            }
        }

    }
    private void addDevice(String guid) {
        DeviceInfo info = new DeviceInfo();
        info.ownerId = Plat.accountService.getCurrentUserId();
        info.name = DeviceTypeManager.getInstance().getDeviceType(
                guid).getName();
        info.guid = guid;
        Plat.deviceService.addWithBind(info.guid, info.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("添加完成");
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(info));
                        UIService.getInstance().returnHome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }

    public void join() {
        try {
            ms.joinGroup(broadAddress); // 加入到组播地址，这样就能接收到组播信息
            t.scheduleAtFixedRate(task, 100, 1000);
        } catch (Exception e) {
        }
    }
    public void close() {
        try {
            ms.close();
            t.cancel();
        } catch (Exception e) {
        }
    }

    OnDevicelistener onDevicelistener ;

    public void addOnDevicelistener(OnDevicelistener onDevicelistener) {
        this.onDevicelistener = onDevicelistener;
    }

    public interface OnDevicelistener{
        public void onDeviceGuid(String guid);
    }
}