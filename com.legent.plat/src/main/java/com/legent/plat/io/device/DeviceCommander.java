package com.legent.plat.io.device;

import android.content.Context;
import android.util.Log;

import com.legent.Callback2;
import com.legent.LogTags;
import com.legent.io.IOWatcher;
import com.legent.io.channels.IChannel;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.IMsgCallback;
import com.legent.io.senders.AbsSenderWithWatchdog;
import com.legent.io.senders.IMsgSyncDecider;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceConnectedNoticEvent;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.io.device.mqtt.MqttChannel;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgKeys;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.io.device.msg.PushMsg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;

/**
 * Created by sylar on 15/7/23.
 */
public class DeviceCommander extends AbsCommander implements IDeviceCommander {

    protected static final String TAG = LogTags.TAG_IO;
    private static DeviceCommander instance = new DeviceCommander();

//    synchronized public static DeviceCommander getInstance() {
//        return instance;
//    }

    private DeviceCommander() {

    }

    public DeviceCommander(IChannel channel) {
        this.channel = channel;

    }

    protected boolean isLog_sync, isLog_async;
    protected boolean isMqtt;
    protected IChannel channel;
    protected IAppMsgMarshaller msgMarshaller;
    protected IAppNoticeReceiver noticeReceiver;
    protected IMsgSyncDecider syncDecider;
    protected Sender sender = new Sender();
    protected Watcher watcher = new Watcher();


    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        this.cx = cx;
        //this.channel = Plat.channel;
        this.msgMarshaller = Plat.appMsgMarshaller;
        this.syncDecider = Plat.appMsgSyncDecider;
        this.noticeReceiver = Plat.appNoticeReceiver;
        isMqtt = (channel instanceof MqttChannel);


        this.sender.init(cx);
        this.sender.setMsgSyncDecider(new PlatMsgSyncDecider(syncDecider));
        if (channel != null){
            this.channel.setWatcher(watcher);
        }

        openChannel();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.sender.dispose();
        this.channel.dispose();
    }


    public void setSyncLogEnable(boolean enable) {
        isLog_sync = enable;
        sender.setSyncLogEnable(enable);
    }

    public void setAsyncLogEnable(boolean enable) {
        isLog_async = enable;
        sender.setAsyncLogEnable(enable);
    }

    void openChannel() {
        if (!isMqtt || Plat.isValidAppGuid()) {
            this.channel.init(cx);
            this.channel.open(null);
        }
    }

    // ====================================================================================================================

    public IChannel getChannel() {
        return channel;
    }

    public IAppMsgMarshaller getMsgMarshaller() {
        return msgMarshaller;
    }

    public IMsgSyncDecider getMsgSyncDecider() {
        return syncDecider;
    }

    public IAppNoticeReceiver getNoticReceiver() {
        return noticeReceiver;
    }

    public void sendMsg(String deviceId, Msg msg) {
        sender.sendMsg(deviceId, msg);
    }

    public void asyncSend(String deviceId, Msg msg, IMsgCallback<Msg> callback) {
        if (Plat.DEBUG)
            LogUtils.i("20170527", "deviceId::+" + deviceId);
        sender.asyncSend(deviceId, msg, callback);
    }

    // ====================================================================================================================

    @Override
    public void initIO(String deviceId) {
        if (channel != null && (channel instanceof MqttChannel)) {
            if (Plat.DEBUG){
                LogUtils.i("20170326", "channel:" + deviceId);
                LogUtils.i("20170326","rrrrr:"+channel.isConnected());
            }

            MqttChannel.getInstance().regist(deviceId);
        }
    }

    @Override
    public void disposeIO(String deviceId) {
        if (channel != null && (channel instanceof MqttChannel)) {
            MqttChannel.getInstance().unregist(deviceId);
        }
    }


    // ====================================================================================================================

    void onReceived(Msg msg) throws Exception {
        int key = msg.getID();
        String deviceId = msg.getSource().getGuid();

        // 匹配同步消息
        boolean isSync = sender.match(deviceId, msg);
        if (isSync) return;

        if (isLog_async) {
            Log.d(TAG, String.format("recv msg:\n%s", msg));
        }

        // 设备通知消息
        DeviceInfo device;
        switch (key) {
            case MsgKeys.CloudPush_Noti:
                if (Plat.DEBUG)
                    LogUtils.i("20170324", "msg:" + MsgKeys.CloudPush_Noti);
                // 云端推送消息
                if (Plat.DEBUG)
                    Log.d(TAG, String.format("recv CloudPush_Noti:\n%s", msg));
                onReceivedPushMsgNotic(msg);
                break;

            case MsgKeys.DeviceActivated_Noti:
                if (Plat.DEBUG) {
                    LogUtils.i("2020031308", "msg:" + MsgKeys.DeviceActivated_Noti);
                }
                LogUtils.i("2020031308", "msg:" + MsgKeys.DeviceActivated_Noti);
                // EasyLink 完成通知
                Log.d(TAG, String.format("recv DeviceActivated_Noti:\n%s", msg));
                device = (DeviceInfo) msg.opt(MsgParams.DeviceInfo);
                onDeviceActivatedNotic(device);
                break;

            case MsgKeys.DeviceConnected_Noti:
                if (Plat.DEBUG)
                    LogUtils.i("20180711", "msg:" + MsgKeys.DeviceConnected_Noti);
                // 设备上线或变更通知
                Log.d(TAG, String.format("recv DeviceConnected_Noti:\n%s", msg));
                device = (DeviceInfo) msg.opt(MsgParams.DeviceInfo);
                onDeviceConnectedNotic(device);
                break;
            case MsgKeys.SubDeviceChanged_Noti:
                if (!IAppType.RKPAD.equals(Plat.appType))
                    break;
                //灶具设备变更
                if (Plat.DEBUG)
                    Log.d(TAG, String.format("recv DeviceChanged_Noti:\n%s", msg));
                if (FanChange_callback != null)
                    FanChange_callback.onCompleted(null);
                break;
            default:
                if (Plat.DEBUG)
                    LogUtils.i("20170324", "msg:" + msg);
                onReceivedMsg(msg);
                break;
        }
    }

    private void onReceivedMsg(Msg msg) {
        String devGuid = msg.getSource().getGuid();
        LogUtils.i("20170326--onReceivedMsg", "ee:" + devGuid);
        IDevice dev = Plat.deviceService.lookupChild(devGuid);
        if (dev != null) {
            dev.onReceivedMsg(msg);
            LogUtils.i("20170326", "msg:" + msg.toString());
        }
    }

    private void onReceivedPushMsgNotic(Msg msg) throws Exception {

        String json = msg.getString(MsgParams.PushContent);
        PushMsg pMsg = PushMsg.newPushMsg(json);

        if (noticeReceiver != null && pMsg != null) {
            noticeReceiver.onReceivedPushMsg(pMsg);
        }
    }

    private void onDeviceConnectedNotic(DeviceInfo device) {
        EventUtils.postEvent(new DeviceConnectedNoticEvent(device));
    }

    private void onDeviceActivatedNotic(DeviceInfo device) {
        EventUtils.postEvent(new DeviceFindEvent(device));
    }

    // ====================================================================================================================

    class Sender extends AbsSenderWithWatchdog<Msg> {
        @Override
        protected void sendMsg(String deviceId, Msg msg) {
            if (Plat.LOG_FILE_ENABLE) {
                LogUtils.logFIleWithTime(String.format("send msg:%s", msg.toString()));
            }
            if (Plat.DEBUG)
            LogUtils.i("20170727", "Sendermsg:" + msg);
            LogUtils.i("20190215", "Sendermsg826:"+deviceId+"  " + msg);
            channel.send(msg, null);
        }
    }

    class Watcher implements IOWatcher {

        @Override
        public void onConnectionChanged(boolean isConnected) {

        }

        @Override
        public void onMsgReceived(IMsg msg) {

            if (Plat.LOG_FILE_ENABLE) {
                LogUtils.logFIleWithTime(String.format("recv msg:%s", msg.toString()));
            }

            Msg m = (Msg) msg;
            if (Plat.DEBUG)
                LogUtils.i("20170727", "Recvmsg:" + msg);
            try {
                onReceived(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ====================================================================================================================
    private Callback2 FanChange_callback;

    public void setFanChangeCallBack(Callback2 callback2) {
        this.FanChange_callback = callback2;
    }
}
