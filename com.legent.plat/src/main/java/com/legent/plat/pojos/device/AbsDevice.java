package com.legent.plat.pojos.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.common.base.Strings;
import com.legent.plat.BuildConfig;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceStatusChangedEvent;
import com.legent.plat.io.device.DeviceCommander;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgCallback;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceService;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;


abstract public class AbsDevice extends AbsKeyPojo<String> implements IDevice {

    protected final static int MAX_ERROR_COUNT = 4;

    protected String id, bid, name;
    // 周定钧
    protected String dc, dp, dt,disPlayType,categoryName,categoryEnglishName,categoryIconUrl;
    protected int ver;
    protected boolean isConnected;
    protected boolean valid;
    protected int errorCountOnCheck;
    protected IDevice parent;
    //    protected DeviceCommander dc = Plat.commander;
    protected DeviceCommander dcMqtt = Plat.dcMqtt;
    protected boolean hardIsConnected;//代表 硬件上报的连接状态  具体看协议41返回指令

    @Override
    public boolean isHardIsConnected() {
        return hardIsConnected;
    }

    public AbsDevice(SubDeviceInfo devInfo) {
        this.valid = true;
        this.id = devInfo.guid;
        this.bid = devInfo.bid;
        this.name = devInfo.name;
        this.ver = (short) devInfo.ver;
        //周定钧
        this.dc = devInfo.dc;
        this.dp = devInfo.dp;
        this.dt = devInfo.dt;
        this.categoryName = devInfo.categoryName;
        this.categoryEnglishName = devInfo.categoryEnglishName;
        this.categoryIconUrl = devInfo.categoryIconUrl;
        this.disPlayType = devInfo.displayType;
        this.hardIsConnected = devInfo.isConnected;
        if (Strings.isNullOrEmpty(this.name)) {
//            if (IDeviceType.RJCZ.equals(this.dc)){
//                this.name = "集成灶";
//            } else
            if (DeviceTypeManager.getInstance().getDeviceType(id) != null) {
                this.name = DeviceTypeManager.getInstance().getDeviceType(id).getName();
            }
        }

        initStatus();
    }

    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);
//        dc.initIO(id);
        if (dcMqtt != null) {
            dcMqtt.initIO(id);
        }

    }

    @Override
    public void dispose() {
        super.dispose();
//        dc.disposeIO(id);
        if (dcMqtt != null) {
            dcMqtt.initIO(id);
        }

    }

    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DeviceGuid getGuid() {
        return DeviceGuid.newGuid(id);
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceTypeManager.getInstance().getDeviceType(id);
    }

    @Override
    public String getBid() {
        return bid;
    }

    @Override
    public int getVersion() {
        return ver;
    }

    @Override
    public String getDc() {
        return dc;
    }

    @Override
    public String getDispalyType() {
        return disPlayType;
    }

    @Override
    public void setDc(String dc) {
        this.dc = dc;
    }

    @Override
    public void setDt(String dt) {
        this.dt = dt;
    }

    @Override
    public void setDp(String dp) {
        this.dp = dp;
    }

    @Override
    public void setDisplayType(String displayType) {
        this.disPlayType = displayType;
    }


    public static int getMaxErrorCount() {
        return MAX_ERROR_COUNT;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String getCgEngName() {
        return categoryEnglishName;
    }

    @Override
    public String getCgIconUrl() {
        return categoryIconUrl;
    }

    public void setCategoryEnglishName(String categoryEnglishName) {
        this.categoryEnglishName = categoryEnglishName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }

    public void setCategoryIconUrl(String categoryIconUrl) {
        this.categoryIconUrl = categoryIconUrl;
    }

    @Override
    public String getDt() {
        return dt;
    }

    @Override
    public String getDp() {
        return dp;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }





    @Override
    public synchronized void setConnected(boolean connected) {


        LogUtils.i("20170830 test1 before","errorcount::"+errorCountOnCheck+" conencted::"+connected+" isCon::"+isConnected);
        errorCountOnCheck = connected ? 0 : errorCountOnCheck;
        if (Plat.DEBUG)
            LogUtils.i("20170830 test1 after","errorcount::"+errorCountOnCheck+" conencted::"+connected+" isCon::"+isConnected);
        if (isConnected == connected)
            return;

        isConnected = connected;
        if (!isConnected) {
            initStatus();
        }
        postEvent(new DeviceConnectionChangedEvent(this, isConnected));
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("post DeviceConnectionChangedEvent:%s", isConnected));
        }
    }




    @Override
    public boolean getValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public IDevice getParent() {
        return parent;
    }

    @Override
    public void setParent(IDevice parent) {
        this.parent = parent;
    }

    @Override
    public void onPolling() {
    }

    private static final String TAG = "AbsDevice";
    //设置false  循环在不现在
    @Override
    public void onCheckConnection() {
        Log.e(TAG,"onCheckConnection test1");
        if (isConnected){
            errorCountOnCheck++;
            if (errorCountOnCheck >= MAX_ERROR_COUNT){
                //增加一个时间检测，如果4
//                if (System.currentTimeMillis()-currentTime>8000) {
                    setConnected(true);
                    Log.e(TAG, "onCheckConnection test1 false");
                    writeLog();
//                }
            }
        }

    }


//    private long currentTime=System.currentTimeMillis();
    @Override
    public void onReceivedMsg(Msg msg) {
        Log.e(TAG,"onReceivedMsg test1");
        LogUtils.i("20180901","msg0000:"+msg.toString());
//        currentTime=System.currentTimeMillis();
        if (!isConnected){
            setConnected(true);
            writeLog();
        }

    }

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
//            if (curTime == 0) {
//                curTime = MAX_TIME;
//            } else {
//                //计数器，每次减一秒。
//                curTime -= 1000;
//            }
//            Message message = new Message();
//            message.what = WHAT;
//            message.obj = curTime;
//            mHandler.sendMessage(message);
        }
    };
    private Timer mTimer = new Timer();

    private  void writeLog() {
        try {
            StringBuilder deviceState = new StringBuilder();
            deviceState.append("-------------------------------------------------------\n");
            deviceState.append("[Mobile_Model]:" + Build.MODEL  + "   [Android_Ver]:" + Build.VERSION.RELEASE + "\n");
            deviceState.append("[user_id]:" + Plat.accountService.getCurrentUserId()  + "   [BID]:" + bid +  "   [GUID]:" + id + "\n");
            deviceState.append("[Net_type]:" + getAPNType(cx)  + "   [MQTT]:" + "connect"  + "   [Device_status]:" + isConnected() + "\n");
            LogUtils.uploadLogFIleWithTime(deviceState.toString());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public  String getAPNType(Context context) {
        String netType = "NoConnect";
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getSubtype();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "WIFI";// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if(nSubType == TelephonyManager.NETWORK_TYPE_LTE){
                netType = "4G";// 4G
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = "3G";// 3G
            } else {
                netType = "2G";// 2G
            }
        }
        return netType;
    }

    @Override
    public void onStatusChanged() {
        postEvent(new DeviceStatusChangedEvent(this));
    }

    // -------------------------------------------------------------------------------
    // protected
    // -------------------------------------------------------------------------------

    /**
     * 设备状态参数重置
     */
    protected void initStatus() {

    }


    final public Msg newReqMsg(short msgKey) {
        LogUtils.i("20180724","id::"+id);
        Msg msg = Msg.newRequestMsg(msgKey, id);
        return msg;
    }

    final protected void sendMsg(Msg reqMsg, MsgCallback callback) {
        //dc.asyncSend(id, reqMsg, callback);
        DeviceCommander dcMqtt = Plat.dcMqtt;
        if (null != dcMqtt){
            dcMqtt.asyncSend(id,reqMsg,callback);
        }
        //        Plat.dcMqtt.asyncSend(id, reqMsg, callback);
    }

    final protected void sendMsgBySerial(Msg reqMsg, MsgCallback callback) {
        Plat.dcSerial.asyncSend(id, reqMsg, callback);
    }

    final protected void sendMsgByMqtt(Msg reqMsg, MsgCallback callback) {
        Plat.dcMqtt.asyncSend(id, reqMsg, callback);
    }

    final public String getSrcUser() {
        long id = getCurrentUserId();
        String userId = String.valueOf(id);
        userId = Strings.padEnd(userId, 10, '\0');
        LogUtils.i("20180724","userId:"+userId);
        return userId;
    }

    /**
     * userId转换
     * @param id
     * @return
     */
    public final String getSrcUser(long id) {
        String userId = String.valueOf(id);
        userId = Strings.padEnd(userId, 10, '\0');
        LogUtils.i("20180724","userId:"+userId);
        return userId;
    }

    final protected long getCurrentUserId() {
        return Plat.accountService.getCurrentUserId();
    }

    @Override
    final protected void postEvent(Object event) {
        EventUtils.postEvent(event);
    }

    // -------------------------------------------------------------------------------
    // private
    // -------------------------------------------------------------------------------

}
