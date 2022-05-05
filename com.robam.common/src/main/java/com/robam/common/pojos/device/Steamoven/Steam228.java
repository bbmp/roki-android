package com.robam.common.pojos.device.Steamoven;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.SteamUserAction;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yinwei on 2017/9/7.
 */

public class Steam228 extends AbsSteamoven implements ISteamoven {

    public Steam228(DeviceInfo devInfo) {
        super(devInfo);
    }

    long userid= Plat.accountService.getCurrentUserId();

    @Override
    public void setSteamCookMode(final short model, final short workTemp, final short workTime, final short preFlag, final short recipeId, final short recipeStep, final short argumentNumber, final short orderTimeKey, final short orderTimeLength, final short orderTimeMin, final short orderTimeHour, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SetMeum, model);
            msg.putOpt(MsgParams.SteamTemp, workTemp);
            msg.putOpt(MsgParams.SteamTime, workTime);
            msg.putOpt(MsgParams.PreFlag, preFlag);
            msg.putOpt(MsgParams.SteamRecipeId, recipeId);
            msg.putOpt(MsgParams.SteamRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            msg.putOpt(MsgParams.OrderTime_key, orderTimeKey);
            msg.putOpt(MsgParams.OrderTime_length, orderTimeLength);
            msg.putOpt(MsgParams.OrderTime_value_min, orderTimeMin);
            msg.putOpt(MsgParams.OrderTime_value_hour, orderTimeHour);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    String strTime = getCurrentTime();
                    SteamUserAction steamUserAction = new SteamUserAction(userid,strTime,model,workTemp,workTime);
                    steamUserAction.save2db();
                    tempSet = workTemp;
                    timeSet = workTime;
                    mode = model;
                    status = -100;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日,HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
