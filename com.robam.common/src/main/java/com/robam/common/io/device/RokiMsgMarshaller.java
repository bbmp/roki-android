package com.robam.common.io.device;

import com.legent.plat.io.device.AbsPlatProtocol;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.msg.Msg;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.marshal.CookerMsgMar;
import com.robam.common.io.device.marshal.DishWasherMsgMar;
import com.robam.common.io.device.marshal.FanMsgMar;
import com.robam.common.io.device.marshal.GasSensorMsgMar;
import com.robam.common.io.device.marshal.HidkitMsgMar;
import com.robam.common.io.device.marshal.IntegratedStoveMsgMar;
import com.robam.common.io.device.marshal.MicroMsgMar;
import com.robam.common.io.device.marshal.OvenNewMsgMar;
import com.robam.common.io.device.marshal.PotMsgMar;
import com.robam.common.io.device.marshal.RikaMsgMar;
import com.robam.common.io.device.marshal.SteamMsgNewMar;
import com.robam.common.io.device.marshal.SteamOvenMsgMar;
import com.robam.common.io.device.marshal.SterMsgMar;
import com.robam.common.io.device.marshal.SterNewMsgMar;
import com.robam.common.io.device.marshal.StoveMsgMar;
import com.robam.common.io.device.marshal.WaterMsgMar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RokiMsgMarshaller implements IAppMsgMarshaller {

    static public final int BufferSize = AbsPlatProtocol.BufferSize;
    static public final ByteOrder BYTE_ORDER = AbsPlatProtocol.BYTE_ORDER;

    //将设备通讯消息模型序列化成字节流
    @Override
    public byte[] marshal(Msg msg) throws Exception {
        int key = msg.getID();
        ByteBuffer buf = ByteBuffer.allocate(BufferSize).order(BYTE_ORDER);
        if (isStoveMsg(msg)) {//灶具
            StoveMsgMar.marshaller(key, msg, buf);
        } else if (isFanMsg(msg)) {//烟机
            FanMsgMar.marshaller(key, msg, buf);
        }else if (isGasSensor(msg)){//燃气传感器
            GasSensorMsgMar.marshaller(key,msg,buf);
        } else if (isSterilizer(msg)) {//消毒柜
            SterMsgMar.marshaller(key, msg, buf);
        } else if (isSteamMsg(msg)) {//蒸箱
            SteamMsgNewMar.marshaller(key, msg, buf);
        } else if (isMicroWaveMsg(msg)) {//微波炉
            MicroMsgMar.marshaller(key, msg, buf);
        } else if (isOvenMsg(msg)) {//烤箱
            OvenNewMsgMar.marshaller(key, msg, buf);
        } else if (isWaterPurifier(msg)) {//状态查询请求
            WaterMsgMar.marshaller(key, msg, buf);//净水器
        } else if (isPotMsg(msg)) {//温控过
            PotMsgMar.marshaller(key, msg, buf);
        } else if (isSteamOvenMsg(msg)) {//一体机
            SteamOvenMsgMar.marshaller(key, msg, buf);
        }else if (isRikaMsg(msg)){//Rika
            RikaMsgMar.marshaller(key,msg,buf);
        }else if (isCookerMsg(msg)){//灶具
            CookerMsgMar.marshaller(key,msg,buf);
        }else if (isDishWasher(msg)){//洗碗机
            DishWasherMsgMar.marshaller(key,msg,buf);
        }else if (isHidKitMsg(msg)) {//藏宝盒
            HidkitMsgMar.marshaller(key, msg, buf);
        }
        else if (isRJCZMsg(msg)){//集成灶
            IntegratedStoveMsgMar.marshaller(key,msg,buf);
        }

        byte[] data = new byte[buf.position()];
        System.arraycopy(buf.array(), 0, data, 0, data.length);
        buf.clear();
        return data;
    }

    //将字节流反序列化成设备通讯消息模型
    @Override
    public void unmarshal(Msg msg, byte[] payload) throws Exception {

        int key = msg.getID();
        if (isStoveMsg(msg)) {
            StoveMsgMar.unmarshaller(key, msg, payload);
        } else if (isFanMsg(msg)) {
            FanMsgMar.unmarshaller(key, msg, payload);
        }else if (isGasSensor(msg)){//燃气传感器
            GasSensorMsgMar.unmarshaller(key,msg,payload);
        } else if (isSterilizer(msg)) {
            SterMsgMar.unmarshaller(key, msg, payload);
        } else if (isOvenMsg(msg)) {
            OvenNewMsgMar.unmarshaller(key, msg, payload);
        } else if (isSteamMsg(msg)) {
            SteamMsgNewMar.unmarshaller(key, msg, payload);
        } else if (isMicroWaveMsg(msg)) {
            MicroMsgMar.unmarshaller(key, msg, payload);
        } else if (isWaterPurifier(msg)) {//净水器
            WaterMsgMar.unmarshaller(key, msg, payload);
        } else if (isPotMsg(msg)) {
            PotMsgMar.unmarshaller(key, msg, payload);
        } else if (isSteamOvenMsg(msg)) {
            SteamOvenMsgMar.unmarshaller(key, msg, payload);
        }else if (isRikaMsg(msg)){
            RikaMsgMar.unmarshaller(key,msg,payload);
        }else if (isCookerMsg(msg)){
            CookerMsgMar.unmarshaller(key,msg,payload);
        }else if (isDishWasher(msg)){//洗碗机
            DishWasherMsgMar.unmashaller(key,msg,payload);
        }else if (isHidKitMsg(msg)) {//藏宝盒
            HidkitMsgMar.unmashaller(key, msg, payload);
        }    else if (isRJCZMsg(msg)){//集成灶
            IntegratedStoveMsgMar.unmarshaller(key,msg,payload);
        }
    }

    private boolean isCookerMsg(Msg msg){
        return Utils.isCooker(msg.getDeviceGuid().getGuid());
    }

    private boolean isHidKitMsg(Msg msg) {
        return Utils.isHidKitMsg(msg.getDeviceGuid().getGuid());
    }

    private boolean isDishWasher(Msg msg){
        return Utils.isDishWasher(msg.getDeviceGuid().getGuid());
    }

    private boolean isStoveMsg(Msg msg) {
        return Utils.isStove(msg.getDeviceGuid().getGuid());
    }

    private boolean isGasSensor(Msg msg){
        return Utils.isGasSensor(msg.getDeviceGuid().getGuid());
    }
    //判断是否为油烟机
    private boolean isFanMsg(Msg msg) {
        return Utils.isFan(msg.getDeviceGuid().getGuid());
    }

    private boolean isSterilizer(Msg msg) {
        return Utils.isSterilizer(msg.getDeviceGuid().getGuid());
    }

    private boolean isSteamMsg(Msg msg) {
        return Utils.isSteam(msg.getDeviceGuid().getGuid());
    }

    private boolean isMicroWaveMsg(Msg msg) {
        return Utils.isMicroWave(msg.getDeviceGuid().getGuid());
    }

    private boolean isOvenMsg(Msg msg) {
        return Utils.isOven(msg.getDeviceGuid().getGuid());
    }

    //判断是否为净水器
    private boolean isWaterPurifier(Msg msg) {
        return Utils.isWaterPurifier(msg.getDeviceGuid().getGuid());
    }

    private boolean isPotMsg(Msg msg) {
        return Utils.isPot(msg.getDeviceGuid().getGuid());
    }

    private boolean isSteamOvenMsg(Msg msg) {
        return Utils.isSteamOvenMsg(msg.getDeviceGuid().getGuid());
    }

    private boolean isRikaMsg(Msg msg) {
        return Utils.isRikaMsg(msg.getDeviceGuid().getGuid());
    }

    private boolean isRJCZMsg(Msg msg) {
        return Utils.isRJCZMsg(msg.getDeviceGuid().getGuid());
    }
}
