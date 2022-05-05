package com.robam.common.pojos.device.fan;

import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.SmartParams;

/**
 * Created by zhaiyuanyi on 15/10/19.
 */
public class Fan8230 extends Fan9700 {
    public Fan8230(DeviceInfo devInfo) {
        super(devInfo);
    }

//    @Override
//    public String getFanModel() {
//        return IRokiFamily.R8230;
//    }


    @Override
    public void setSmartConfig(final SmartParams smartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            //msg.putOpt(MsgParams.UserId, getSrcUser());                   //增加 userid
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
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    Fan8230.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
