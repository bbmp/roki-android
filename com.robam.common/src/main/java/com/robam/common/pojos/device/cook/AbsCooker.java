package com.robam.common.pojos.device.cook;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.AbsCookerAlarmEvent;
import com.robam.common.events.CookerParamReportEvent;
import com.robam.common.events.CookerStatusChangeEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import org.json.JSONException;

/**
 * Created by Dell on 2018/5/25.
 */

public class AbsCooker extends AbsDeviceHub {

    protected short terminalType = TerminalType.getType();

    public AbsCooker(DeviceInfo devInfo) {
        super(devInfo);
    }

    public short powerStatus;
    public short mode;
    public short currentFire;//当前火力
    public short setTempture;//设置温度
    public short currentTempture;//当前温度
    public short timerPower;//时间使能
    public short time;//定时时间
    public short heatTime;//加热时间
    public short currentAction;//当前动作
    public short recipeStatus;//菜谱执行状态
    public int recipeId;//当前菜谱id
    public short recipeStepId;//当前菜谱步骤编号
    public short wifiVersion;//wifi版本号

    public short recipeTimeLeft;//菜谱当前步骤剩余时间
    public short recipeTotalTime;//菜谱烹饪总时长
    public short alarm;//报警故障代码


    public short voice;
    public short voiceCon;

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                Log.i("oven_st", "ID:" + getID() + " onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.deviceStatusQuery_Req);
            // 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged() {
        LogUtils.i("absCooker", "powerStatus" + powerStatus + " mode:" + mode + " currentFire::" + currentFire
                + " setTempture:" + setTempture + " currentTempture:" + currentTempture + "timerPower:" + timerPower +
                "time:" + time + "heatTime::" + heatTime + " currentAction:" + currentAction + " recipeStatus:" +
                recipeStatus + "recipeId:" + recipeId + "recipeStepId:" + recipeStepId +
                " recipeTimeLeft:" + recipeTimeLeft + " recipeTotalTime:" + recipeTotalTime
                + "alarm:" + alarm
        );
        postEvent(new CookerStatusChangeEvent(AbsCooker.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        int key = msg.getID();
        LogUtils.i("20180830","key:"+key);
        switch (key) {
            case MsgKeys.deviceStatusQuery_Rep:
                powerStatus = (short) msg.optInt(MsgParams.cookerSwitchStatus);
                mode = (short) msg.optInt(MsgParams.cookerModel);
                currentFire = (short) msg.optInt(MsgParams.cookerFire);
                setTempture = (short) msg.optInt(MsgParams.setCookerTemp);
                currentTempture = (short) msg.optInt(MsgParams.cookerTemp);
                timerPower = (short) msg.optInt(MsgParams.cookerTimingSwitch);
                time = (short) msg.optInt(MsgParams.cookerTimingTime);
                heatTime = (short) msg.optInt(MsgParams.cookerHeatingTime);
                currentAction = (short) msg.optInt(MsgParams.cookerCurrentAction);
                recipeStatus = (short) msg.optInt(MsgParams.cookerRecipePerformStatus);
                recipeId =  msg.optInt(MsgParams.cookerRecipeCode);
                recipeStepId = (short) msg.optInt(MsgParams.cookerRecipeStepCode);
               // reTempTure = (short) msg.optInt(MsgParams.cookerRecipeStepTargetTemp);
                recipeTimeLeft = (short) msg.optInt(MsgParams.cookerRecipeStepRemainTime);
                recipeTotalTime = (short) msg.optInt(MsgParams.cookerRecipeCookingTotalTime);
                alarm = (short) msg.optInt(MsgParams.cookerAlarmCode);
                wifiVersion = (short) msg.optInt(MsgParams.wifiVersion);
                onStatusChanged();
                break;
            case MsgKeys.DeviceAlarm:
                short alarmId = (short) msg.optInt(MsgParams.cookerAlarmCode);
                LogUtils.i("20180830","alarmId:::"+alarmId);
                postEvent(new AbsCookerAlarmEvent(AbsCooker.this,alarmId));
                break;

            case MsgKeys.DeviceWorkReport:
                short eventId = (short) msg.optInt(MsgParams.paramEvent);
                LogUtils.i("20180830","eventId:"+eventId);
                postEvent(new CookerParamReportEvent(AbsCooker.this,eventId));
                switch (eventId){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                }
                break;

        }
    }


    //130设置电磁灶信息查询
    public void setCookerInfoLook(final VoidCallback callback) {
        Msg msg = newReqMsg(MsgKeys.setDeviceInformationQuery_Req);//待修改
        sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
            protected void afterSuccess(Msg resMsg) {
                voice = (short) resMsg.optInt(MsgParams.cookerVoiceMode);
                voiceCon = (short) resMsg.optInt(MsgParams.cookerVoiceLevel);
                LogUtils.i("20180806","voice:"+voice+" voiceCon::"+voiceCon);
            }
        });
    }

    //132设置电磁炉工作状态
    public void setCookerWorkStatus(short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceWorkStatus_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerSwitchStatus, status);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //134设置电磁炉温度
    public void setCookerTempTure(short tempTure, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceTemp_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.setCookerTemp, tempTure);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //136设置电磁炉火力
    public void setCookerFire(short Fire, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceFire_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerFire, Fire);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //138设置电磁炉定时关机工作
    public void setTimerOff(short timeStatus, int time, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceShutdownWork_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerTimingSwitch, timeStatus);
            msg.putOpt(MsgParams.cookerTimingTime, time);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //140设置电磁炉菜谱工作回复
    public void setRecipeCookerWork(int recipeId, VoidCallback callback) {
        try {
            LogUtils.i("20180813","recipeId:"+recipeId);
            Msg msg = newReqMsg(MsgKeys.setDeviceRecipeWork_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerRecipeCode, recipeId);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //142设置电磁炉设定信息
    public void setSetCookerInfo(short boMode, short voiceG, VoidCallback callback) {
        try {
            LogUtils.i("20180806","boMode::"+boMode+" voiceG:"+voiceG);
            Msg msg = newReqMsg(MsgKeys.setDeviceSetInformation_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerVoiceMode, boMode);
            msg.putOpt(MsgParams.cookerVoiceLevel,voiceG);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //144设置电磁炉简易智能默认温度
    public void setCookerAction(short action, short tempTure, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setActionTempTure_Req);//待修改
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.cookerAction, action);
            msg.putOpt(MsgParams.tempTureAll, tempTure);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
