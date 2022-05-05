package com.robam.common.pojos.device.Steamoven;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.events.SteamFinishEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.events.SteamPowerEvent;
import com.robam.common.events.SteamTempResetEvent;
import com.robam.common.events.SteamTimeResetEvent;
import com.robam.common.events.SteamWaterBoxEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

/**
 * Created by Rosicky on 15/12/14.
 */
public class AbsSteamoven extends AbsDeviceHub implements ISteamoven {
    static final public short Event_Steam_Power = 10;
    static final public short Event_Steam_Time_Reset = 11;
    static final public short Event_Steam_Temp_Reset = 12;
    static final public short Event_Steam_Cookbook_Reset = 13;
    static final public short Event_Steam_Finish = 21;
    static final public short Event_Steam228_Finish = 15;
    static final public short Event_Steam228_WaterBox = 16;

    static final public short Event_Steam_Alarm_ok = 0;
    static final public short Event_Steam_Alarm_water = 1;
    static final public short Event_Steam_Alarm_temp = 2;
    static final public short Event_Steam_Alarm_door = 3;

    //s226故障指令
    static final public short Event_Steam226_Alarm_lack_water = 1;
    static final public short Event_Steam226_Alarm_heat = 3;
    static final public short Event_Steam228_Alarm_door = 8;
    static final public short Event_Steam226_Alarm_sensor = 5;
    static final public short Event_Steam226_Alarm_communication_fault = 6;
    static final public short Event_Steam228_Alarm_communication_fault = 7;
    static final public short Steam_Door_Open = 0;
    static final public short Steam_Door_Close = 1;


    public short childLock = 0;
    public short status = SteamStatus.Off;
    public short oldstatus = SteamStatus.Off;
    public short tempSet = -1;
    public short timeSet = -1;
    public short mode = 0;
    public short alarm = 0;
    public short temp = 0; // 当前温度
    public short time = 0; // 当前剩余时间
    public short doorState = 0;
    public short orderTime_min = 0;
    public short orderTime_hour = 0;
    public short steamLight = 0;

    public short recipeId;//菜谱ID
    public short recipeStep;//菜谱步骤
    public short argument;//参数个数
    public short waterboxstate;//水箱状况 0开 1关
    public short currentStage;
    protected short terminalType = TerminalType.getType();
    public short steamAutoRecipeModeValue;
    public short descaleModeStageValue;
    public short WeatherDescalingValue;
    public short SetTime2_value;
    public short alarmId;

    public AbsSteamoven(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            Log.i("steam_st_onpoll", "ID:" + getID() + " onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.GetSteamOvenStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged() {
        LogUtils.i("202007010", "ID:" + getID() + "childLock:" + childLock + " status:" + status + " alarm:" + alarm +
                " mode:" + mode + " tempSet:" + tempSet + " temp:" + temp + " timeSet:" + timeSet + " time:" + time
                + " doorState:" + doorState + " orderTime_min:" + orderTime_min + " orderTime_hour:" + orderTime_hour
                + " steamLight:" + steamLight + " waterboxstate:" + waterboxstate + "recipeId::" + recipeId+"recipeStep::" + recipeStep);

        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("SteamOven onStatusChanged. isConnected:%s level:%s", isConnected, status));
        }

        postEvent(new SteamOvenStatusChangedEvent(AbsSteamoven.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        setConnected(true);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.SteamOven_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    LogUtils.i("2020070603", "eventId:::" + eventId);
                    LogUtils.i("2020070603", "eventParam:::" + eventParam);
                    switch (eventId) {
                        case Event_Steam_Power:
                            postEvent(new SteamPowerEvent(AbsSteamoven.this, 1 == eventParam));
                            break;
                        case Event_Steam_Time_Reset:
                            postEvent(new SteamTimeResetEvent(AbsSteamoven.this, eventParam));
                            break;
                        case Event_Steam_Temp_Reset:
                            postEvent(new SteamTempResetEvent(AbsSteamoven.this, eventParam));
                            break;
                        case Event_Steam_Cookbook_Reset:
                            postEvent(new SteamCleanResetEvent(AbsSteamoven.this, eventParam));
                            break;
                        case Event_Steam_Finish:
                            postEvent(new SteamFinishEvent(AbsSteamoven.this, eventParam));
                            break;
                        case Event_Steam228_Finish:
                            LogUtils.i("2020072306", "事件结束id:::" + eventId);
                            LogUtils.i("2020072306", "事件结束参数:::" + eventParam);
                            postEvent(new SteamFinishEvent(AbsSteamoven.this, eventParam));
                            break;
                        case Event_Steam228_WaterBox:
                            postEvent(new SteamWaterBoxEvent(AbsSteamoven.this, eventParam));
                            break;
                        default:
                            break;
                    }

                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
                    alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    postEvent(new SteamAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    AbsSteamoven.this.childLock = (short) msg.optInt(MsgParams.SteamLock);
                    AbsSteamoven.this.status = (short) msg.optInt(MsgParams.SteamStatus);
                    AbsSteamoven.this.oldstatus = status;
                    AbsSteamoven.this.alarm = (short) msg.optInt(MsgParams.SteamAlarm);
                    AbsSteamoven.this.mode = (short) msg.optInt(MsgParams.SteamMode);
                    AbsSteamoven.this.temp = (short) msg.optInt(MsgParams.SteamTemp);
                    AbsSteamoven.this.time = (short) msg.optInt(MsgParams.SteamTime);
                    AbsSteamoven.this.doorState = (short) msg.optInt(MsgParams.SteamDoorState);
                    AbsSteamoven.this.tempSet = (short) msg.optInt(MsgParams.SteamTempSet);
                    AbsSteamoven.this.timeSet = (short) msg.optInt(MsgParams.SteamTimeSet);
                    AbsSteamoven.this.orderTime_min = (short) msg.optInt(MsgParams.OrderTime_value_min);
                    AbsSteamoven.this.orderTime_hour = (short) msg.getInt(MsgParams.OrderTime_value_hour);
                    AbsSteamoven.this.steamLight = (short) msg.optInt(MsgParams.SteamLight);
                    AbsSteamoven.this.recipeId = (short) msg.optInt(MsgParams.SteamRecipeId);
                    AbsSteamoven.this.recipeStep = (short) msg.optInt(MsgParams.SteamRecipeStep);
                    AbsSteamoven.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    AbsSteamoven.this.waterboxstate = (short) msg.optInt(MsgParams.SteamOvenWaterBoxValue);
                    AbsSteamoven.this.currentStage = (short) msg.optInt(MsgParams.SteamOvenCurrentStageValue);
                    AbsSteamoven.this.steamAutoRecipeModeValue = (short) msg.optInt(MsgParams.SteamAutoRecipeModeValue);
                    AbsSteamoven.this.descaleModeStageValue = (short) msg.optInt(MsgParams.DescaleModeStageValue);
                    AbsSteamoven.this.WeatherDescalingValue = (short) msg.optInt(MsgParams.WeatherDescalingValue);
                    AbsSteamoven.this.SetTime2_value = (short) msg.optInt(MsgParams.SetTime2_value);
                    onStatusChanged();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }

    @Override
    public String getSteamovenModel() {
        return null;
    }

    @Override
    public void setSteamWorkTime(final short time, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTime, time);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.time = (short) (((short) 60) * time);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteamWorkTemp(final short temp, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamTemp_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTemp, temp);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.temp = temp;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteamWorkMode(final short cookbook, final short temp, final short time, short preFlag, VoidCallback callback) {
        try {
            if ("RS209".equals(this.getDt())){
                Msg msg = newReqMsg(MsgKeys.setSteamMode_Req);
                msg.putOpt(MsgParams.TerminalType, terminalType);
                msg.putOpt(MsgParams.UserId, getSrcUser());
                msg.putOpt(MsgParams.SteamTemp, temp);
                msg.putOpt(MsgParams.SteamTime, time);
                msg.putOpt(MsgParams.SteamMode, cookbook);
                msg.putOpt(MsgParams.PreFlag, 0);

                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsSteamoven.this.time = (short) (((short) 60) * time);
                        AbsSteamoven.this.temp = temp;
                        AbsSteamoven.this.mode = cookbook;
                        onStatusChanged();
                    }
                });
            }else {

                setSteamCookMode(cookbook,temp,time,preFlag,(short)0,(short)0,(short)0,
                        (short)0,(short)0,(short)0,(short)0,callback);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置专业蒸箱模式
     */
    @Override
    public void setSteamProMode(final short time, final short temp, VoidCallback callback) {
        setSteamProMode(time, temp, 0, callback);
    }

    public void setSteamProMode(final short time, final short temp, int preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamProMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTemp, temp);
            msg.putOpt(MsgParams.SteamTime, time);
            msg.putOpt(MsgParams.PreFlag, preflag);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20180416", "success");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getSteamStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteamOvenStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.oldstatus = status;
                    AbsSteamoven.this.status = (short) resMsg.optInt(MsgParams.SteamStatus);
                    AbsSteamoven.this.mode = (short) resMsg.optInt(MsgParams.SteamMode);
                    AbsSteamoven.this.alarm = (short) resMsg.optInt(MsgParams.SteamAlarm);
                    AbsSteamoven.this.temp = (short) resMsg.optInt(MsgParams.SteamTemp);
                    AbsSteamoven.this.time = (short) resMsg.optInt(MsgParams.SteamTime);
                    AbsSteamoven.this.doorState = (short) resMsg.optInt(MsgParams.SteamDoorState);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteamStatus(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamStatus_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamStatus, status);
            msg.putOpt(MsgParams.OrderTime, (short) 0);
            msg.putOpt(MsgParams.ArgumentNumber, (short) 0);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------全新电蒸箱指令

    /**
     * 设置电蒸箱水箱弹出
     *
     * @param callback
     */
    public void setSteamWaterTankPOP(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteamWaterTankPOPReq);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置电蒸箱工作时间
     *
     * @param time
     * @param argumentNumber
     * @param callback
     */
    public void setSteamWorkTime(final short time, final short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            ////----
            msg.putOpt(MsgParams.SteamTime, time);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.time = (short) (((short) 60) * time);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置电蒸箱工作温度
     *
     * @param temp
     * @param argumentNumber
     * @param callback
     */
    public void setSteamWorkTemp(final short temp, final short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamTemp_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTemp, temp);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.temp = temp;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setSteamCookModule(short cmd, final short model, final short workTemp,
                                   final short workTime, final short preFlag,
                                   final short recipeId, final short recipeStep,
                                   final short argumentNumber,
                                   final short orderTimeKey,
                                   final short orderTimeLength,
                                   final short orderTimeMin,
                                   final short orderTimeHour,
                                   VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
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
                    LogUtils.i("20171121", "success" + AbsSteamoven.this.timeSet);
                    AbsSteamoven.this.tempSet = workTemp;
                    AbsSteamoven.this.timeSet = workTime;
                    AbsSteamoven.this.mode = model;
//                    AbsSteamoven.this.status = -100;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            LogUtils.i("20171121", "e:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 电蒸箱烹饪模式
     *
     * @param workTemp
     * @param workTime
     * @param preFlag
     * @param recipeId
     * @param recipeStep
     * @param argumentNumber
     * @param orderTimeKey
     * @param orderTimeLength
     * @param orderTimeHour
     * @param orderTimeMin
     * @param callback
     */
    public void setSteamCookMode(final short model, final short workTemp, final short workTime,
                                 final short preFlag, final short recipeId, final short recipeStep,
                                 final short argumentNumber, final short orderTimeKey,
                                 final short orderTimeLength, final short orderTimeMin,
                                 final short orderTimeHour, VoidCallback callback) {
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
                    LogUtils.i("20171121", "success" + AbsSteamoven.this.timeSet);
                    AbsSteamoven.this.tempSet = workTemp;
                    AbsSteamoven.this.timeSet = workTime;
                    AbsSteamoven.this.mode = model;
//                    AbsSteamoven.this.status = -100;
                    onStatusChanged();

                }
            });
        } catch (Exception e) {
            LogUtils.i("20171121", "e:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 专业模式设置
     *
     * @param temp
     * @param time
     * @param preflag
     * @param recipeId
     * @param recipeStep
     * @param argumentNumber
     * @param orderTimeKey
     * @param orderTimeLength
     * @param orderTimeHour
     * @param orderTimeMin
     * @param callback
     */
    public void setSteamProMode(final short temp, final short time, int preflag, final short recipeId, final short recipeStep, final short argumentNumber, final short orderTimeKey, final short orderTimeLength, final short orderTimeHour, final short orderTimeMin, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamProMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTemp, temp);
            ///
            msg.putOpt(MsgParams.SteamTime, time);
            msg.putOpt(MsgParams.PreFlag, preflag);
            msg.putOpt(MsgParams.SteamRecipeId, recipeId);
            msg.putOpt(MsgParams.SteamRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            msg.putOpt(MsgParams.OrderTime_key, orderTimeKey);
            msg.putOpt(MsgParams.OrderTime_length, orderTimeLength);
            msg.putOpt(MsgParams.OrderTime_value_hour, orderTimeHour);
            msg.putOpt(MsgParams.OrderTime_value_min, orderTimeMin);

            LogUtils.i("SteamOven", "setSteam226Mode setTemp:" + msg.toString());
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.time = (short) (((short) 60) * time);
                    AbsSteamoven.this.temp = temp;
                    LogUtils.i("SteamOven", "setSteam226Mode setTemp:" + temp);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("SteamOven", "setSteamProMode 方法发生异常:" + e.toString());
        }
    }

    /**
     * 设置电蒸箱状态
     *
     * @param status
     * @param orderTime
     * @param argumentNumber
     * @param callback
     */
    public void setSteamStatus(final short status, final short orderTime, final short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteamStatus_Req);
//            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamStatus, status);
            msg.putOpt(MsgParams.OrderTime, orderTime);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteamoven.this.oldstatus = AbsSteamoven.this.status;
                    AbsSteamoven.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * zdj add
     * 电蒸箱菜谱设置 目前指令不明确，等到做菜谱的时候再完善
     */
    public void setSteamRecipe(short ArgumentNumber, short recipeId, short SteamRecipeTotalStep, short recipeStep, final short setTemp, final short setTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteamOven_Recipe_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            msg.putOpt(MsgParams.SteamRecipeId, recipeId);
            msg.putOpt(MsgParams.SteamRecipeTotalStep, SteamRecipeTotalStep);
            msg.putOpt(MsgParams.SteamRecipeStep, recipeStep);
            msg.putOpt(MsgParams.SteamTemp, setTemp);
            msg.putOpt(MsgParams.SteamTime, setTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置电蒸箱照明灯
     *
     * @param light
     * @param argumentNumber
     * @param callback
     */
    public void setSteamLight(final short light, final short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteamLightReq);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamLight, light);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置电蒸箱自动模式
     */
    public void setSteamAutoMode(short autoMode, final short setTime, short ArgumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetLocalRecipeReq);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.setSteamAutoMode, autoMode);
            msg.putOpt(MsgParams.SteamTimeSet, setTime);
            msg.putOpt(MsgParams.ArgumentNumber, 0);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, 0);
                msg.putOpt(MsgParams.OrderTime_value_hour, 0);
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSteamAutoMode(short autoMode, final short setTime1, short shortTime2, short ArgumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetLocalRecipeReq);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.setSteamAutoMode, autoMode);
            msg.putOpt(MsgParams.SteamTimeSet, setTime1);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.SetTime_H_key, 2);
                msg.putOpt(MsgParams.SetTime_H_length, 1);
                msg.putOpt(MsgParams.SetTime_H_Value, shortTime2);
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
