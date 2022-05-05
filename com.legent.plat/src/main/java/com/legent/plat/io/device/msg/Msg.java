package com.legent.plat.io.device.msg;

import com.legent.io.msgs.AbsMsg;
import com.legent.plat.Plat;
import com.legent.plat.io.device.mqtt.IMqttMsg;
import com.legent.plat.io.device.mqtt.Topic;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.utils.LogUtils;

public class Msg extends AbsMsg implements IMqttMsg {

    protected DeviceGuid source, target;
    protected boolean incoming;
    //当前消息是否为烟机发出
    protected boolean isFan = false;

    public boolean getIsFan() {
        return isFan;
    }

    public void setIsFan(boolean isFan) {
        this.isFan = isFan;
    }


    private Msg(int msgKey) {
        this.msgKey = msgKey;
    }

    @Override
    public String getTopic() {
        return getTag();
    }

    /**
     * 获取消息的方向
     * @return true-收到的消息 false-发出去的消息
     */
    public boolean isIncoming() {
        return incoming;
    }

    public DeviceGuid getSource() {
        return source;
    }

    public DeviceGuid getTarget() {
        return target;
    }

    public DeviceGuid getDeviceGuid() {
        return incoming ? source : target;
    }

    /**
     * 构造一个待发送的消息
     */
    static public Msg newRequestMsg(int msgKey, String targetGuid) {
        Msg msg = new Msg(msgKey);
        msg.incoming = false;
        msg.source = getAppGuid();
        msg.target = DeviceGuid.newGuid(targetGuid);
        msg.setTag(Topic.newUnicastTopic(targetGuid).toString());
        return msg;
    }

    /**
     * 构造一个接收的消息
     */
    static public Msg newIncomingMsg(int msgKey, String sourceGuid) {
        Msg msg = new Msg(msgKey);
        msg.incoming = true;
        msg.source = DeviceGuid.newGuid(sourceGuid);
        msg.target = getAppGuid();
        LogUtils.i("20170531","sourceGuid：："+sourceGuid);
        msg.setTag(Topic.newUnicastTopic(sourceGuid).toString());
        return msg;
    }

    static DeviceGuid getAppGuid() {
        if (Plat.appGuid == null) {
            return DeviceGuid.newGuid(DeviceGuid.ZeroGuid);
        } else {
            return DeviceGuid.newGuid(Plat.appGuid);
        }
    }
}
