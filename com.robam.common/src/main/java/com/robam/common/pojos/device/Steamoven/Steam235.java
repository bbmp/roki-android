package com.robam.common.pojos.device.Steamoven;

import android.util.Log;

import com.legent.plat.Plat;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.events.SteamFinishEvent;
import com.robam.common.events.SteamPowerEvent;
import com.robam.common.events.SteamTempResetEvent;
import com.robam.common.events.SteamTimeResetEvent;
import com.robam.common.events.SteamWaterBoxEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

public class Steam235 extends AbsSteamoven implements ISteamoven {
    public Steam235(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        LogUtils.i("20170306msg", "msg:" + msg.getDeviceGuid());
        setConnected(true);
        try {
            int key = msg.getID();
            LogUtils.i("20180926", "key:" + key);
            if (Plat.DEBUG)
                Log.e("key_steam226", key + " tostring:" + msg.toString());
            switch (key) {
                case MsgKeys.SteamOven_Noti:
                    // TODO 处理事件
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    LogUtils.i("20180926", "eventId:" + eventId + " eventParam:" + eventParam);
                    switch (eventId) {
                        case Event_Steam_Power:
                            postEvent(new SteamPowerEvent(Steam235.this, 1 == eventParam));
                            break;
                        case Event_Steam_Time_Reset:
                            postEvent(new SteamTimeResetEvent(Steam235.this, eventParam));
                            break;
                        case Event_Steam_Temp_Reset:
                            postEvent(new SteamTempResetEvent(Steam235.this, eventParam));
                            break;
                        case Event_Steam_Cookbook_Reset:
                            LogUtils.i("20171207", "除垢" + Event_Steam_Cookbook_Reset);
                            postEvent(new SteamCleanResetEvent(Steam235.this, eventParam));
                            break;
                        case Event_Steam_Finish:
                            LogUtils.i("20171207", "226finish:" + Event_Steam_Finish);
                            postEvent(new SteamFinishEvent(Steam235.this, eventParam));
                            break;
                        case Event_Steam228_Finish:
                            LogUtils.i("20171207", "228finish:" + Event_Steam228_Finish);
                            postEvent(new SteamFinishEvent(Steam235.this, eventParam));
                            break;
                        case Event_Steam228_WaterBox:
                            postEvent(new SteamWaterBoxEvent(Steam235.this, eventParam));
                            break;
                        default:
                            break;
                    }

                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    LogUtils.i("20180115", "alarmId:" + alarmId);
                    postEvent(new SteamAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    Steam235.this.oldstatus = status;
                    Steam235.this.childLock = (short) msg.optInt(MsgParams.SteamLock);
                    Steam235.this.status = (short) msg.optInt(MsgParams.SteamStatus);
                    Steam235.this.alarm = (short) msg.optInt(MsgParams.SteamAlarm);
                    Steam235.this.mode = (short) msg.optInt(MsgParams.SteamMode);
                    Steam235.this.temp = (short) msg.optInt(MsgParams.SteamTemp);
                    Steam235.this.time = (short) msg.optInt(MsgParams.SteamTime);
                    Steam235.this.doorState = (short) msg.optInt(MsgParams.SteamDoorState);
                    Steam235.this.tempSet = (short) msg.optInt(MsgParams.SteamTempSet);
                    Steam235.this.timeSet = (short) msg.optInt(MsgParams.SteamTimeSet);
                    //新增 zdj
                    Steam235.this.orderTime_min = (short) msg.optInt(MsgParams.OrderTime_value_min);
                    Steam235.this.orderTime_hour = (short) msg.getInt(MsgParams.OrderTime_value_hour);
                    Steam235.this.steamLight = (short) msg.optInt(MsgParams.SteamLight);
                    Steam235.this.recipeId = (short) msg.optInt(MsgParams.SteamRecipeId);
                    Steam235.this.recipeStep = (short) msg.optInt(MsgParams.SteamRecipeStep);
                    Steam235.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    Steam235.this.waterboxstate = (short) msg.optInt(MsgParams.SteamOvenWaterBoxValue);
                    Steam235.this.currentStage = (short) msg.optInt(MsgParams.SteamOvenCurrentStageValue);
                    Steam235.this.steamAutoRecipeModeValue = (short) msg.optInt(MsgParams.SteamAutoRecipeModeValue);
                    Steam235.this.descaleModeStageValue = (short) msg.optInt(MsgParams.DescaleModeStageValue);
                    Steam235.this.WeatherDescalingValue = (short) msg.optInt(MsgParams.WeatherDescalingValue);
                    onStatusChanged();
                    if (Plat.DEBUG)
                        Log.i("command::" + MsgKeys.GetSteamOvenStatus_Rep + "steam_st", "childLock:" + childLock + " status:" + status + " alarm:" + alarm +
                                " mode:" + mode + " tempSet:" + tempSet + " temp:" + temp + " timeSet:" + timeSet + " time:" + time
                                + " doorState:" + doorState + " orderTime_min:" + orderTime_min + " orderTime_hour:" + orderTime_hour
                                + " steamLight:" + steamLight + " waterboxstate:" + waterboxstate);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
