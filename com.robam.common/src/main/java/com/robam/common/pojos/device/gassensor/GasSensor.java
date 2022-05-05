package com.robam.common.pojos.device.gassensor;

import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.GasAlarmEvent;
import com.robam.common.events.GasStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.Oven.AbsOven;

import org.json.JSONException;

/**
 * Created by Dell on 2018/5/8.
 */

public class GasSensor extends AbsDevice {

    public short status;
    public short gasCon;
    public short argument;

    public short alarm;
    public short argumentAlarm;

    protected short terminalType = TerminalType.getType();

   /* public GasSensor(DeviceInfo devInfo) {
        super(devInfo);
    }*/

     public GasSensor(SubDeviceInfo devInfo) {
         super(devInfo);
     }


    @Override
    public void onPolling() {
        super.onPolling();
        try {
            Msg reqMsg = newReqMsg(MsgKeys.getGasSensor);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        LogUtils.i("20180514","msg1111111::"+msg.getID());
        int key = msg.getID();
        switch (key){
            case MsgKeys.GasSensor_Status_Check_Rep:
                GasSensor.this.status = (short) msg.optInt(MsgParams.GasSensorStatus);
                GasSensor.this.gasCon = (short) msg.optInt(MsgParams.GasCon);
                GasSensor.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                onStatusChanged();
                break;
            case MsgKeys.GasSensor_Status_Noti:
                break;
            case MsgKeys.GasSensor_Alarm_Noti:
                short alarm = (short) msg.optInt(MsgParams.Alarm);
                //postEvent(new GasAlarmEvent(GasSensor.this,alarm));
                LogUtils.i("20171030","alarmId:::"+alarm);
                break;
            default:
                break;
        }
    }


    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
        LogUtils.i("20180601","status:"+status+" gasCon:"+gasCon);
        postEvent(new GasStatusChangedEvent(GasSensor.this));
    }

    public void setGasSelfCheckCom(VoidCallback callback){

        try {
            Msg msg = newReqMsg(MsgKeys.GasSensor_SetCheckSelf_Req);//待修改
            msg.putOpt(MsgParams.TerminalType, terminalType);
            sendMsg(msg,new RCMsgCallbackWithVoid(callback){
                @Override
                protected void afterSuccess(Msg resMsg) {
                    super.afterSuccess(resMsg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
