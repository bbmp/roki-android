package com.robam.common.pojos.device.steameovenone;


import android.util.Log;

import com.legent.VoidCallback;
import com.legent.io.msgs.collections.BytesMsg;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.NewSteamOvenOneAlarm2Event;
import com.robam.common.events.NewSteamOvenOneAlarmEvent;
import com.robam.common.events.OvenOtherEvent;
import com.robam.common.events.SteamOvenOneAddSteamEvent;
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneAutomaticModelEvent;
import com.robam.common.events.SteamOvenOneBegainWorkSteamEvent;
import com.robam.common.events.SteamOvenOneDescalingEvent;
import com.robam.common.events.SteamOvenOneLightResetEvent;
import com.robam.common.events.SteamOvenOneOvenRunModeResetEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneSwitchControlResetEvent;
import com.robam.common.events.SteamOvenOneWaterChangesEvent;
import com.robam.common.events.SteamOvenOneWorkFinishEvent;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import java.util.List;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.SteameOvenOrderTime_hour;
import static com.robam.common.io.device.MsgParams.UserId;


/**
 * Created by zhoudingjun on 2017/6/13.
 * 烤蒸一体机
 */

public class AbsSteameOvenOne extends AbsDeviceHub implements ISteamOvenOne {


    static final public short Event_SteameOven_Switch_Control_Reset = 10;       //开关控制事件
    static final public short Event_SteameOven_run_model_Control_Reset = 11;    //运行模式调整事件
    static final public short Event_SteameOven_Light_Reset = 13;                //灯光调整事件
    static final public short Event_SteameOven_work_finish_Reset = 14;          //工作完成事件
    static final public short Event_SteameOven_automatic_model_Reset = 15;      //自动模式调整事件
    static final public short Event_SteameOven_water_changes_Reset = 16;        //水箱更改事件
    static final public short Event_SteameOven_descaling_Reset = 17;            //除垢提醒事件
    static final public short Event_SteameOven_add_steam_Reset = 18;            //加蒸汽事件
    static final public short Event_SteameOven_open_door_Reset = 19;            //开门事件
    static final public short Event_SteameOven_open_begain_work_Reset = 20;            //开始工作事件


    public short powerStatus;
    public short worknStatus;
    public short powerOnStatus;
    public short alarm = 0;
    public short temp; // 当前温度
    public short time; // 当前剩余时
    public short light;//灯光控制
    public short leftTime;//烤叉旋转
    public int unShortLeftTime;
    public short workModel;//工作模式
    public short setTemp;
    public short setTime;
    public short setTimeH;
    public short ordertime_min;
    public short ordertime_hour;
    public short order_left_time_min;
    public short order_left_time_hour;
    public short WaterStatus;
    public short autoMode;//自动模式
    public short CpStep;//自动模式介
    public short recipeId;//菜谱ID
    public short recipeStep;//菜谱步骤
    public short argument;//参数个数
    public short SectionOfTheStep;//多段烹饪步（906）
    public short steam;//蒸汽
    public short setTempDownValue;//设置下温度
    public short currentTempDownValue;//实时下温度
    public short setTempUpValue;//设置上温度
    public short currentTempUpValue;//实时温度
    protected short terminalType = TerminalType.getType();
    public short multiSumStep;//多段总段数（906）
    public short modelType;//模式类型
    public short CpStepValue;
    public short SteamValue;
    public short SteamOvenAutoRecipeModeValue;
    public short AutoRecipeModeValue;

    public short setSteameOvenBasicMode;
    public short SteameOvenSetTemp;
    public short SteameOvenSetTime;
    public short SteameOvenSetDownTemp;
    public short SteameOvenCpMode;
    public short SteameOvenRevolve;
    public short SteameOvenWaterChanges;
    public short SteameOvenLight;
    public short SteameOvenWorkComplete;
    public short SteameOvenSteam;
    public short setSteameOvenSwitchControl;
    public short SteamOvenAutoRecipeMode;
    public short MultiStepCookingStepsValue;//总步骤（其他机型）
    public short MultiStepCurrentStepsValue;//当前步骤（其他机型）
    public short SteameOvenPreFlagValue;
    public short SteameOvenPreFlag;//(906)
    public short weatherDescalingValue;//是否需要除垢
    public short eventId;
    public short doorStatusValue;//是否开门

    public AbsSteameOvenOne(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("steamovenone_st", "ID:" + getID() + " onPolling");
            LogUtils.i("steamovenone_st", "terminalType:" + terminalType);
            Msg reqMsg = newReqMsg(MsgKeys.getSteameOvenStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged() {
        if (Plat.DEBUG) {
            LogUtils.i("steamovenone_st", "multiSumStep:" + multiSumStep + " powerStatus:" + powerStatus + " powerOnStatus:" + powerOnStatus + " worknStatus:" + worknStatus
                    + " SectionOfTheStep:" + SectionOfTheStep + " alarm:" + alarm + " workModel:" + workModel + " setTime:" + setTime + " leftTime:" + leftTime + " setTemp:" + setTemp + " temp:"
                    + temp + " setTempDownValue:" + setTempDownValue + " currentTempDownValue:" + currentTempDownValue + " light:" + light + " WaterStatus:" + WaterStatus
                    + " modelType:" + modelType);
        }
        postEvent(new SteamOvenOneStatusChangedEvent(AbsSteameOvenOne.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);

        int key = msg.getID();
        switch (key) {
            case MsgKeys.SteameOven_Noti:
                AbsSteameOvenOne.this.eventId = (short) msg.optInt(MsgParams.EventId);

                //设置的基本模式
                AbsSteameOvenOne.this.setSteameOvenBasicMode = (short) msg.optInt(MsgParams.setSteameOvenBasicMode_value);
                LogUtils.i("20200512","setSteameOvenBasicMode:::"+setSteameOvenBasicMode);
                //设置的温度
                AbsSteameOvenOne.this.SteameOvenSetTemp = (short) msg.optInt(MsgParams.SteameOvenSetTemp_Value);
                //设置的时间
                AbsSteameOvenOne.this.SteameOvenSetTime = (short) msg.optInt(MsgParams.SteameOvenSetTime);
                //设置的下温度
                AbsSteameOvenOne.this.SteameOvenSetDownTemp = (short) msg.optInt(MsgParams.SteameOvenSetDownTemp_Vaue);
                //自动模式
                AbsSteameOvenOne.this.SteameOvenCpMode = (short) msg.optInt(MsgParams.SteameOvenCpMode_Value);
                //烤叉旋转
                AbsSteameOvenOne.this.SteameOvenRevolve = (short) msg.optInt(MsgParams.SteameOvenRevolve_Value);
                //水箱更改
                AbsSteameOvenOne.this.SteameOvenWaterChanges = (short) msg.optInt(MsgParams.SteameOvenWaterChanges_Value);
                //照明灯开关
                AbsSteameOvenOne.this.SteameOvenLight = (short) msg.optInt(MsgParams.SteameOvenLight_Value);
                //工作完成参数
                AbsSteameOvenOne.this.SteameOvenWorkComplete = (short) msg.optInt(MsgParams.SteameOvenWorkComplete_Value);
                LogUtils.i("202012071709","SteameOvenWorkComplete:::"+this.SteameOvenWorkComplete);
                //加蒸汽
                AbsSteameOvenOne.this.SteameOvenSteam = (short) msg.optInt(MsgParams.SteameOvenSteam_Value);
                //开关事件参数
                AbsSteameOvenOne.this.setSteameOvenSwitchControl = (short) msg.optInt(MsgParams.setSteameOvenSwitchControl_Value);
                //新增一体机自动菜谱上报
                AbsSteameOvenOne.this.SteamOvenAutoRecipeMode = (short) msg.optInt(MsgParams.SteamOvenAutoRecipeModeValue);


                switch (eventId) {
                    case Event_SteameOven_Switch_Control_Reset://一体机开关控制事件 111
                        postEvent(new SteamOvenOneSwitchControlResetEvent(this));
                        break;
                    case Event_SteameOven_run_model_Control_Reset://一体机运行模式模式调整事件
                        postEvent(new SteamOvenOneOvenRunModeResetEvent(this));
                        break;
                    case Event_SteameOven_Light_Reset://一体机灯光调整事件  111
                        postEvent(new SteamOvenOneLightResetEvent(this));
                        break;
                    case Event_SteameOven_work_finish_Reset://一体机工作完成事件
                        postEvent(new SteamOvenOneWorkFinishEvent(this));
                        break;
                    case Event_SteameOven_automatic_model_Reset://一体机自动模式调整事件
                        postEvent(new SteamOvenOneAutomaticModelEvent(this));
                        break;
                    case Event_SteameOven_water_changes_Reset://一体机水箱更改事件
                        LogUtils.i("202010231746","eventId:::"+eventId);
                        postEvent(new SteamOvenOneWaterChangesEvent(this));
                        break;
                    case Event_SteameOven_descaling_Reset://一体机除垢提醒事件
                        postEvent(new SteamOvenOneDescalingEvent(this));
                        break;
                    case Event_SteameOven_add_steam_Reset:
                        postEvent(new SteamOvenOneAddSteamEvent(this));
                        break;
                        //开门事件
                    case Event_SteameOven_open_door_Reset:
                        postEvent(new SteamOvenOpenDoorSteamEvent(this));
                        break;
                        //开始工作事件
                    case Event_SteameOven_open_begain_work_Reset:
                        postEvent(new SteamOvenOneBegainWorkSteamEvent(this,setSteameOvenBasicMode));
                        break;
                    default:
                        postEvent(new OvenOtherEvent(this));
                        break;
                }
                break;

            case MsgKeys.SteameOvenAlarm_Noti:
                short alarmId = (short) msg.optInt(MsgParams.SteameOvenAlarm);
                LogUtils.i("20180731", " alarmId:" + alarmId);
                this.alarm = alarmId;
                short alarmCode = 255;
                if (alarmId == 4) {
                    alarmCode = 2;
                } else if (alarmId == 2) {
                    alarmCode = 1;
                } else if (alarmId == 8) {
                    alarmCode = 3;
                } else if (alarmId == 16) {
                    alarmCode = 4;
                    postEvent(new NewSteamOvenOneAlarm2Event(this, alarmCode));
                } else if (alarmId == 1) {
                    alarmCode = 0;
                } else if (alarmId == 128) {
                    alarmCode = 7;
                } else if (alarmId == 64) {
                    alarmCode = 6;
                }else if(alarmId==3){//新增水位报警
                    alarmCode = 8;
                }else if (alarmId==5){
                    alarmCode = 9;
                }else if (alarmId==32){
                    alarmCode = 5;
                }
                else if (alarmId==7){//E3警报
                    alarmCode = 11;
                }
                else if (alarmId==6){//E3警报
                    alarmCode = 10;
                }
                postEvent(new NewSteamOvenOneAlarmEvent(this, alarmCode));

                if (alarmId == 0)
                    return;
                short[] args = new short[8];
                for (short i = 7; i > -1; i--) {
                    args[7 - i] = ((alarmId & (1 << i)) == 0) ? (short) 0 : 1;
                }
                postEvent(new SteamOvenOneAlarmEvent(this, args));

                break;
            case MsgKeys.getSteameOvenStatus_Rep:
                this.powerStatus = (short) msg.optInt(MsgParams.SteameOvenStatus);
                this.powerOnStatus = (short) msg.optInt(MsgParams.SteameOvenPowerOnStatus);
                this.worknStatus = (short) msg.optInt(MsgParams.SteameOvenWorknStatus);
                this.alarm = (short) msg.optInt(MsgParams.SteameOvenAlarm);
                this.workModel = (short) msg.optInt(MsgParams.SteameOvenMode);
                this.temp = (short) msg.optInt(MsgParams.SteameOvenTemp);
                this.leftTime = ((short) msg.optInt(MsgParams.SteameOvenLeftTime));
                this.unShortLeftTime = ((short) msg.optInt(MsgParams.SteameOvenLeftTime))&0x0FFFF;
                this.light = (short) msg.optInt(MsgParams.SteameOvenLight);
                this.WaterStatus = (short) msg.optInt(MsgParams.SteameOvenWaterStatus);
                LogUtils.i("202010231742","WaterStatus:::"+WaterStatus);
                this.setTemp = (short) msg.optInt(MsgParams.SteameOvenSetTemp);
                this.setTime = (short) msg.optInt(MsgParams.SteameOvenSetTime);
                this.ordertime_min = (short) msg.optInt(MsgParams.SteameOvenOrderTime_min);
                this.ordertime_hour = (short) msg.optInt(MsgParams.SteameOvenOrderTime_hour);
                this.order_left_time_min = (short) msg.optInt(MsgParams.SteameOvenLeftMin);
                this.order_left_time_hour = (short) msg.optInt(MsgParams.SteameOvenLeftHours);
                this.recipeId = (short) msg.optInt(MsgParams.SteameOvenRecipeId);
                this.recipeStep = (short) msg.optInt(MsgParams.SteameOvenRecipesteps);
                this.setTempDownValue = (short) msg.optInt(MsgParams.SteameOvenSetDownTemp);
                this.currentTempDownValue = (short) msg.optInt(MsgParams.SteameOvenDownTemp);
                this.steam = (short) msg.optInt(MsgParams.SteameOvenSteam);
                this.multiSumStep = (short) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);//多段总步骤（906）
                this.SectionOfTheStep = (short) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key);//当前步骤（906）
                this.SteameOvenPreFlag = (short) msg.optInt(MsgParams.SteameOvenPreFlag);
                this.modelType = (short) msg.optInt(MsgParams.SteameOvenModelType);
                this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                this.CpStepValue = (short) msg.optInt(MsgParams.CpStepValue);
                LogUtils.i("202005071111","CpStepValue:::"+CpStepValue);
                this.SteamValue = (short) msg.optInt(MsgParams.SteamValue);
                this.MultiStepCookingStepsValue = (short) msg.optInt(MsgParams.MultiStepCookingStepsValue);//总步骤（其他机型）
                this.AutoRecipeModeValue = (short) msg.optInt(MsgParams.AutoRecipeModeValue);
                this.MultiStepCurrentStepsValue = (short) msg.optInt(MsgParams.MultiStepCurrentStepsValue);//当前步骤（其他机型）
                this.SteameOvenPreFlagValue = (short) msg.optInt(MsgParams.SteameOvenPreFlagValue);
                this.weatherDescalingValue = (short) msg.optInt(MsgParams.weatherDescalingValue);
                this.doorStatusValue = (short) msg.optInt(MsgParams.doorStatusValue);
                this.setTimeH = (short) msg.optInt(MsgParams.time_H_Value);
                onStatusChanged();

                LogUtils.i("20171113", "SteamOvenOne:" + this.getClass().hashCode() + " worknStatus:" + worknStatus + " powerOnStatus:" + powerOnStatus + " leftTime:" + leftTime + " alarm:" + alarm +
                        " workModel:" + workModel + " setTemp:" + setTemp + " temp:" + temp + " setTime:" + setTime + " time:" + time + " ordertime_hour:" + ordertime_hour + " ordertime_min:" + ordertime_min
                        + " autoMode:" + autoMode + " powerStatus:" + powerStatus + " light:" + light + " modelType:" + modelType + " ordertime_hour:" + ordertime_hour + " ordertime_min:" + ordertime_min);
                break;
            default:
                break;
        }

    }


    /**
     * 灯控
     *
     * @param light
     * @param callback
     */
    public void setLightControl(final short light, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenFloodlight_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenLight, light);
            msg.putOpt(ArgumentNumber, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.light = light;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 一体机状态控制
     *
     * @param status
     * @param callback
     */
    public void setSteamOvenOneStatusControl(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenPowerOnStatus, status);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.powerOnStatus = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关机
     */
    public void setSteameOvenStatus_Off(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.NoStatus, callback);
    }

    /**
     * 开机
     */
    public void setSteameOvenStatus_on(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, callback);
    }


    /**
     * 暂停
     */
    public void setSteameOvenStatus_pause(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.Pause, callback);
    }

    /**
     * 恢复运行
     */
    public void setSteameOvenStatus_rerun(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.WorkingStatus, callback);
    }

    /**
     * 一体机状态
     *
     * @param callback
     */
    public void setSteameOvenStatus(final short powerStatus,
                                    final short powerOnStatus, VoidCallback callback) {
        LogUtils.i("20180126", "powerStatus:::" + powerStatus + " powerON:::" + powerOnStatus);
        try {
            final long curtime = System.currentTimeMillis();
            Msg msg = newReqMsg(MsgKeys.setSteameOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenStatus, powerStatus);
            msg.putOpt(MsgParams.SteameOvenPowerOnStatus, powerOnStatus);
            msg.putOpt(SteameOvenOrderTime_hour, 255);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20180126", "success");
                    Log.i("20171023", "setStatus:" + (System.currentTimeMillis() - curtime));
                    AbsSteameOvenOne.this.powerStatus = powerStatus;
                    AbsSteameOvenOne.this.powerOnStatus = SteamOvenOnePowerOnStatus.OperatingState;
                    AbsSteameOvenOne.this.worknStatus = SteamOvenOneWorkStatus.NoStatus;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setSteameOvenStatus2(final short powerStatus,
                                    final short powerOnStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenStatus, powerStatus);
            msg.putOpt(MsgParams.SteameOvenPowerOnStatus, powerOnStatus);
            msg.putOpt(SteameOvenOrderTime_hour, 255);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public void setSteameOvenOneRunMode(short mode, final short setTime,
                                        final short setTempUp, short preflag, VoidCallback callback) {
        setSteameOvenOneRunMode(mode, setTime, setTempUp, preflag, (short) 0, (short) 0, (short) 0, callback);
    }

    /**
     * 610设备 带预约功能
     * @param mode
     * @param setTime
     * @param setTempUp
     * @param preflag
     * @param ordertime_min
     * @param ordertime_hour
     * @param callback
     */
    public void setSteameOvenOneRunMode(short mode, final short setTime,
                                        final short setTempUp, short preflag,  short ordertime_min, short ordertime_hour ,VoidCallback callback) {
        setSteameOvenOneRunMode(mode, setTime, setTempUp, preflag, (short) 0, (short) ordertime_min, (short) ordertime_hour, callback);
    }

    //154
    public void setSteameOvenOneRunMode(final short mode, final short setTime,
                                        final short setTempUp, short preflag, final short setTempDown,
                                        short orderTime_min, short orderTime_hour, VoidCallback callback) {
        try {
            Log.i("20180731", "mode:" + mode + " setTime:" + setTime + " setTempUp:" + setTempUp + " preflag:" + preflag
                    + " setTempDown:" + setTempDown + " orderTime_min:" + orderTime_min + " orderTime_hour:" + orderTime_hour);
            Msg msg = newReqMsg(MsgKeys.setSteameOvenBasicMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenMode, mode);
            msg.putOpt(MsgParams.SteameOvenSetTemp, setTempUp);
            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            msg.putOpt(MsgParams.SteameOvenSetTime, lowTime);
            msg.putOpt(MsgParams.SteameOvenPreFlag, preflag);

            msg.putOpt(MsgParams.SteameOvenRecipeId, 0);
            msg.putOpt(MsgParams.SteameOvenRecipesteps, 0);
            msg.putOpt(MsgParams.SteameOvenSetDownTemp, setTempDown);
            msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
            msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
            if (setTime > 255) {
                msg.putOpt(ArgumentNumber, 1);
                msg.putOpt(MsgParams.time_H_key, 1);
                msg.putOpt(MsgParams.time_H_length, 1);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParams.time_H_Value, highTime);

            } else {
                msg.putOpt(ArgumentNumber, 0);
            }

            final long curtime = System.currentTimeMillis();
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {

                    Log.i("20180731", "setRunMode:" + (System.currentTimeMillis() - curtime));
                    AbsSteameOvenOne.this.powerStatus = SteamOvenOnePowerStatus.On;
                    AbsSteameOvenOne.this.powerOnStatus = SteamOvenOnePowerOnStatus.WorkingStatus;
                    AbsSteameOvenOne.this.worknStatus = SteamOvenOneWorkStatus.PreHeat;
                    AbsSteameOvenOne.this.workModel = mode;
                    AbsSteameOvenOne.this.setTemp = setTempUp;
                    if (setTime > 255) {
                        AbsSteameOvenOne.this.setTime = lowTime;
                        AbsSteameOvenOne.this.setTimeH = (short) ((setTime >> 8) & 0Xff);
                    } else {
                        AbsSteameOvenOne.this.setTime = (short) setTime;
                    }
                    AbsSteameOvenOne.this.setTempDownValue = setTempDown;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 菜谱设置
     *
     * @param ArgumentNumber
     * @param recipeId
     * @param OvenRecipeTotalStep
     * @param recipeStep
     * @param ovenMode
     * @param setTemp
     * @param setTime
     * @param callback
     */
    public void setSteameOvenOneRecipe(short ArgumentNumber, short recipeId,
                                       short OvenRecipeTotalStep, short recipeStep, short ovenMode, final short setTemp,
                                       final short setTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setTheRecipe_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            msg.putOpt(MsgParams.SteameOvenRecipeId, recipeId);
            msg.putOpt(MsgParams.SteameOvenRecipeTotalsteps, OvenRecipeTotalStep);
            msg.putOpt(MsgParams.SteameOvenRecipesteps, recipeStep);
            msg.putOpt(MsgParams.SteameOvenMode, ovenMode);
            msg.putOpt(MsgParams.SteameOvenTemp, setTemp);
            msg.putOpt(MsgParams.SteameOvenTime, setTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.temp = setTemp;
                    AbsSteameOvenOne.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置自动运行模式
     */
    public void setSteameOvenOneAutoRunMode(short autoMode, short setTime, VoidCallback
            callback) {
        setSteameOvenOneAutoRunMode(autoMode, setTime, (short) 0, (short) 0, (short) 0, callback);
    }

    public void setSteameOvenOneAutoRunMode(short autoMode, final short setTime,
                                            short ArgumentNumber,
                                            short min, short hour, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenAutomaticMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.SteameOvenCpMode, autoMode);
            msg.putOpt(MsgParams.SteameOvenSetTime, setTime);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, min);
                msg.putOpt(MsgParams.OrderTime_value_hour, hour);
            }
            LogUtils.i("20161021", " autoModel:" + autoMode + " setTime:" + setTime + " ArgumentNumber:" + ArgumentNumber
                    + " min:" + min + " hour:" + hour);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.setTime = setTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //水箱控制
    public void setSteameOvenOneWaterPop(final short waterStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenWater_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteameOvenWaterStatus, waterStatus);
            msg.putOpt(ArgumentNumber, 0);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsSteameOvenOne.this.WaterStatus = waterStatus;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置一体机多段模式
     */
    public void setSteamOvenOneMultiStepMode(short ArgumentNumber, short orderTime,
                                             short TotalNumberOfSegments, short SectionOfTheStep,
                                             short SteameOvenMode, short SteameOvenTemp, short SteameOvenTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenMultistageCooking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                //总段数
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, 2);
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Length, 1);
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Value, TotalNumberOfSegments);
            }
            msg.putOpt(MsgParams.OrderTime_key, orderTime);
            msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, TotalNumberOfSegments);
            msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key, SectionOfTheStep);
            msg.putOpt(MsgParams.SteameOvenMode, SteameOvenMode);
            msg.putOpt(MsgParams.SteameOvenTemp, SteameOvenTemp);
            msg.putOpt(MsgParams.SteameOvenTime, SteameOvenTime);
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
     * 蒸烤一体机 多段模式
     *  166
     * @param ArgumentNumber
     * @param TotalNumberOfSegments
     * @param Mode1
     * @param Temp1
     * @param Time1
     * @param Mode2
     * @param Temp2
     * @param Time2
     */
    public void setSteamOvenOneMultiStepMode(short ArgumentNumber, short TotalNumberOfSegments,
                                             short Mode1, short Temp1, short Time1, short Mode2,
                                             short Temp2, short Time2, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenMultistageCooking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);

            if (ArgumentNumber > 0) {
                //总段数
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, 2);//总段数 key
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Length, 1);//总段数 length
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Value, TotalNumberOfSegments);//总段数 value

                for (int i = 1; i <= TotalNumberOfSegments; i++) {
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key + i, 2 + i);//段步骤 key
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 3);//段步骤 value
                    if (i == 1) {
                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode1);//第一段 mode
                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp1);//第一段 temp
                        msg.putOpt(MsgParams.SteameOvenTime + i, Time1);//第一段 time
                    } else if (i == 2) {
                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode2);//第二段 mode
                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp2);//第二段 temp
                        msg.putOpt(MsgParams.SteameOvenTime + i, Time2);//第二段 time
                    }
                }
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

    /**
     * 蒸烤一体机610多段模式
     *  175
     */
    public void setSteamOvenOneMultiStepMode(Msg msg  ,VoidCallback callback) {
        try {
//            Msg msg = newReqMsg(MsgKeys.setSteameOvenMultistageCooking_Req);
//            msg.putOpt(MsgParams.TerminalType, terminalType);
//            msg.putOpt(MsgParams.UserId, getSrcUser());
//            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
//
//            if (ArgumentNumber > 0) {
//                //总段数
//                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, 2);//总段数 key
//                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Length, 1);//总段数 length
//                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Value, TotalNumberOfSegments);//总段数 value
//
//                for (int i = 1; i <= TotalNumberOfSegments; i++) {
//                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key + i, 2 + i);//段步骤 key
//                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 3);//段步骤 value
//                    if (i == 1) {
//                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode1);//第一段 mode
//                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp1);//第一段 temp
//                        msg.putOpt(MsgParams.SteameOvenTime + i, Time1);//第一段 time
//                    } else if (i == 2) {
//                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode2);//第二段 mode
//                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp2);//第二段 temp
//                        msg.putOpt(MsgParams.SteameOvenTime + i, Time2);//第二段 time
//                    }
//                }
//            }

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 蒸烤一体机 本地菜谱模式
     *
     * @return
     */
    public void setLocalRecipeMode(short argumentNumber, short recipeMode, int setTime, short orderTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenAutoRecipeMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamOvenAutoRecipeMode, recipeMode);
            msg.putOpt(MsgParams.SteameOvenSetTime, setTime > 255 ? (short) (setTime & 0Xff):(short)setTime);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber == 1) {
//                msg.putOpt(MsgParams.OrderTime_key, 1);
//                msg.putOpt(MsgParams.OrderTime_length, 2);
//                msg.putOpt(MsgParams.OrderTime_value_min, orderTime);
                msg.putOpt(MsgParams.SetTime_H_key, 2);
                msg.putOpt(MsgParams.SetTime_H_length, 1);
                msg.putOpt(MsgParams.SetTime_H, (short) ((setTime >> 8) & 0Xff));
            }
            if (argumentNumber == 2) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, orderTime);
                msg.putOpt(MsgParams.OrderTime_value_hour, orderTime);
                msg.putOpt(MsgParams.SetTime_H_key, 2);
                msg.putOpt(MsgParams.SetTime_H_length, 1);
                msg.putOpt(MsgParams.SetTime_H, (short) ((setTime >> 8) & 0Xff));
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

    public void setSteamOvenOneMultiStepModeCQ908(short ArgumentNumber, short TotalNumberOfSegments,
                                                  short Mode1, short Temp1, short Time1, short Mode2,
                                                  short Temp2, short Time2, short Mode3, short Temp3,
                                                  short time3, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setSteameOvenMultistageCooking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);

            if (ArgumentNumber > 0) {
                //总段数
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, 2);//总段数 key
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Length, 1);//总段数 length
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Value, TotalNumberOfSegments);//总段数 value

                for (int i = 1; i <= TotalNumberOfSegments; i++) {
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key + i, 2 + i);//段步骤 key
                    if (Time1 > 255) {
                        msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 5);//段步骤 value
                    } else {
                        msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 3);//段步骤 value
                    }

                    if (i == 1) {
                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode1);//第一段 mode
                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp1);//第一段 temp
                        msg.putOpt(MsgParams.SteameOvenTime + i, Time1);//第一段 time
                    } else if (i == 2) {
                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode2);//第二段 mode
                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp2);//第二段 temp
                        msg.putOpt(MsgParams.SteameOvenTime + i, Time2);//第二段 time
                    } else if (i == 3) {
                        msg.putOpt(MsgParams.SteameOvenMode + i, Mode3);//第三段 mode
                        msg.putOpt(MsgParams.SteameOvenTemp + i, Temp3);//第三段 temp
                        msg.putOpt(MsgParams.SteameOvenTime + i, time3);//第三段 time

                    }

                }
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


    @Override
    public String getSteamOvenOneMode() {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }
}