package com.robam.common.pojos.device.fan;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.SmartParams;

/**
 * Created by as on 2017-01-14.
 */

public class Fan8230S extends Fan8230 {
    private short fry_switch;
    private short fry_time;

    public Fan8230S(DeviceInfo devInfo) {
        super(devInfo);
    }

//    @Override
//    public String getFanModel() {
//        return IRokiFamily._8230S;
//    }

    @Override
    public void getSmartConfig(final Callback<SmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {

                    smartParams.IsPowerLinkage = resMsg
                            .optBoolean(MsgParams.IsPowerLinkage);
                    smartParams.IsLevelLinkage = resMsg
                            .optBoolean(MsgParams.IsLevelLinkage);
                    smartParams.IsShutdownLinkage = resMsg
                            .optBoolean(MsgParams.IsShutdownLinkage);
                    smartParams.ShutdownDelay = (short) resMsg
                            .optInt(MsgParams.ShutdownDelay);
                    smartParams.IsNoticClean = resMsg
                            .optBoolean(MsgParams.IsNoticClean);

                    //
                    smartParams.IsTimingVentilation = resMsg
                            .optBoolean(MsgParams.IsTimingVentilation);
                    smartParams.TimingVentilationPeriod = (short) resMsg
                            .optInt(MsgParams.TimingVentilationPeriod);
                    //
                    smartParams.IsWeeklyVentilation = resMsg
                            .optBoolean(MsgParams.IsWeeklyVentilation);
                    smartParams.WeeklyVentilationDate_Week = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Week);
                    smartParams.WeeklyVentilationDate_Hour = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Hour);
                    smartParams.WeeklyVentilationDate_Minute = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Minute);
                    smartParams.R8230S_Switch = (short) resMsg.optInt(MsgParams.R8230SFrySwitch);
                    smartParams.R8230S_Time = (short) resMsg.optInt(MsgParams.R8230SFryTime);
                    if (smartParams.TimingVentilationPeriod == 255)
                        smartParams.TimingVentilationPeriod = 3;
                    if (smartParams.WeeklyVentilationDate_Week == 255)
                        smartParams.WeeklyVentilationDate_Week = 1;
                    if (smartParams.WeeklyVentilationDate_Hour == 255)
                        smartParams.WeeklyVentilationDate_Hour = 12;
                    if (smartParams.WeeklyVentilationDate_Minute == 255)
                        smartParams.WeeklyVentilationDate_Minute = 30;
                    if (Plat.DEBUG)
                        LogUtils.i("20162016", "开关: " + fry_switch + " 时间: " + fry_time);
                    Helper.onSuccess(callback, smartParams);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSmartConfig(final SmartParams smartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            // msg.putOpt(MsgParams.UserId, getSrcUser());                   //增加 userid
            msg.putOpt(MsgParams.IsPowerLinkage, smartParams.IsPowerLinkage);
            msg.putOpt(MsgParams.IsLevelLinkage, smartParams.IsLevelLinkage);
            msg.putOpt(MsgParams.IsShutdownLinkage, smartParams.IsShutdownLinkage);
            msg.putOpt(MsgParams.ShutdownDelay, smartParams.ShutdownDelay);
            msg.putOpt(MsgParams.IsNoticClean, smartParams.IsNoticClean);
            msg.putOpt(MsgParams.IsTimingVentilation, smartParams.IsTimingVentilation);
            msg.putOpt(MsgParams.TimingVentilationPeriod, smartParams.TimingVentilationPeriod);
            msg.putOpt(MsgParams.IsWeeklyVentilation, smartParams.IsWeeklyVentilation);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Week, smartParams.WeeklyVentilationDate_Week);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Hour, smartParams.WeeklyVentilationDate_Hour);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Minute, smartParams.WeeklyVentilationDate_Minute);
            msg.putOpt(MsgParams.ArgumentNumber, (short) 1);
            msg.putOpt(MsgParams.Key, (short) 1);
            msg.putOpt(MsgParams.Length, (short) 2);
            msg.putOpt(MsgParams.R8230SFrySwitch, smartParams.R8230S_Switch);
            msg.putOpt(MsgParams.R8230SFryTime, smartParams.R8230S_Time);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20162016", "设置成功  " + resMsg.toString());
                    Fan8230S.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
