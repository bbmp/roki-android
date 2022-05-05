package com.robam.common.pojos.device.Oven;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenAutoModeAdjustResetEvent;
import com.robam.common.events.OvenLightResetEvent;
import com.robam.common.events.OvenRunModeResetEvent;
import com.robam.common.events.OvenSpitRotateResetEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.events.OvenSwitchControlResetEvent;
import com.robam.common.events.OvenTempResetEvent;
import com.robam.common.events.OvenTimeResetEvent;
import com.robam.common.events.OvenWorkBottomFinishEvent;
import com.robam.common.events.OvenWorkFinishEvent;
import com.robam.common.events.OvenWorkTopFinishEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.UserId;

/**
 * Created by linxiaobin on 2015/12/27.
 * 电烤箱
 */
public class AbsOven extends AbsDeviceHub implements IOven {

    static final public short Event_Oven_Switch_Control_Reset = 10; //    10	烤箱开关控制事件
    static final public short Event_Oven_Run_Mode_Reset = 11;       //    11	烤箱烧烤运行模式调整件
    static final public short Event_Oven_Spit_Rotate_Reset = 12;    //    12	烤箱烤叉旋转事件调整
    static final public short Event_Oven_Light_Reset = 13;          //    13	烤箱灯光调整事件
    static final public short Event_Oven_Temp_Reset = 14;           //    14	烤箱运行温度调整事件
    static final public short Event_Oven_Time_Reset = 15;           //    15	烤箱运行时间调整事件
    //16烤箱工作结束事件

    //新增by 周定钧
    static final public short Event_Oven_Work_Finish_Reset = 22;

    static final public short Event_Oven_Auto_Mode_Adjust_Reset = 17;//   17	烤箱自动模式调整事件

    //新增
    //上层工作完成事件
    static final public short Event_Oven_Top_Work_Finish_Reset = 27;
    //下层工作完成事件
    static final public short Event_Oven_Bottom_Work_Finish_Reset = 28;


    static final public short Event_Oven_Alarm_ok = 255;
    static final public short Event_Oven_Heat_Fault = 03;
    static final public short Event_Oven_Alarm_Senor_Fault = 05;
    static final public short Event_Oven_Communication_Fault = 06;
    static final public short Event_Oven_fan_Fault = 07;
    static final public short Event_Oven_Alarm_Senor_Short = 00;
    static final public short Event_Oven_Alarm_Senor_Open = 01;
    static final public short Event_Oven039_Alarm_Senor_Fault = 02;
    public short status;
    public short runP;
    public short alarm = 0;
    public short temp; // 当前温度
    public short time; // 当前剩余时
    public int hTime; // 当前剩余时
    public short light;//灯光控制
    public short revolve;//烤叉旋转
    public short setTemp;
    public short setTime;
    public short autoMode;//自动模式
    public short recipeId;//菜谱ID
    public short recipeStep;//菜谱步骤
    public short argument;//参数个数
    public short setTempDownValue;//设置下温度
    public short currentTempDownValue;//实时下温度
    public short currentStageValue;//当前步骤
    public short PlatInsertStatueValue = 0;//挡板插入状态
    public short SetTemp2Value;//分区模式 下层温度
    public short SetTime2Value;//分区模式 下层时间
    public short TempValue;
    public short LeftTime2Value;
    public short orderTime_min;//预约时间 min
    public short orderTime_hour;//预约时间 hour
    protected short terminalType = TerminalType.getType();
    public short moreTotalValue;//多段的总数
    //新增
    public short status2Values;//下层状态
    public short setTimeHValve;//高八位时间


    public AbsOven(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                Log.i("oven_st", "ID:" + getID() + " onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.getOvenStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);// 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged() {
        if (Plat.DEBUG) {
            LogUtils.i("20170210", "oven+onStatusChanged");
            if (Plat.LOG_FILE_ENABLE) {
                LogUtils.logFIleWithTime(String.format("Oven onStatusChanged. isConnected:%s level:%s", isConnected, status));
            }
        }
        postEvent(new OvenStatusChangedEvent(AbsOven.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        Log.i("AbsOven:", "msg:" + msg);
        try {
            int key = msg.getID();
            LogUtils.i("20171111", "msg:" + msg.toString());
            Log.e("key", String.valueOf(key));
            switch (key) {
                case MsgKeys.Oven_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    Log.e("eventId", String.valueOf(eventId));
                    LogUtils.i("20180801", "eventId:" + eventId + "eventPar:" + eventParam);
                    switch (eventId) {
                        case Event_Oven_Switch_Control_Reset:
                            postEvent(new OvenSwitchControlResetEvent(AbsOven.this, 1 == eventParam));
                            break;
                        case Event_Oven_Run_Mode_Reset:
                            postEvent(new OvenRunModeResetEvent(AbsOven.this, eventParam));
                            break;
                        case Event_Oven_Spit_Rotate_Reset:
                            postEvent(new OvenSpitRotateResetEvent(AbsOven.this, eventParam));
                            break;
                        case Event_Oven_Light_Reset:
                            postEvent(new OvenLightResetEvent(AbsOven.this, eventParam));
                            break;
                        case Event_Oven_Temp_Reset:
                            postEvent(new OvenTempResetEvent(AbsOven.this, eventParam));
                            break;
                        case Event_Oven_Time_Reset:
                            postEvent(new OvenTimeResetEvent(AbsOven.this, eventParam));
                            break;

                        //全腔结束事件
                        case Event_Oven_Work_Finish_Reset:
                            postEvent(new OvenWorkFinishEvent(AbsOven.this, eventParam));
                            LogUtils.i("20191217", "全腔结束事件" + eventParam);
                            break;
                        case Event_Oven_Auto_Mode_Adjust_Reset:
                            postEvent(new OvenAutoModeAdjustResetEvent(AbsOven.this, eventParam));
                            break;
                        //新增
                        //上层结束事件
                        case Event_Oven_Top_Work_Finish_Reset:
                            postEvent(new OvenWorkTopFinishEvent(AbsOven.this, eventParam));
                            LogUtils.i("20191217", "上层结束事件" + eventParam);
                            break;
                        //下层结束事件
                        case Event_Oven_Bottom_Work_Finish_Reset:
                            postEvent(new OvenWorkBottomFinishEvent(AbsOven.this, eventParam));
                            LogUtils.i("20191217", "下层结束事件" + eventParam);
                            break;
                    }

                    break;
                case MsgKeys.OvenAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    LogUtils.i("20191017", "alarmId:::" + alarmId);
                    postEvent(new OvenAlarmEvent(AbsOven.this, alarmId));
                    break;
                case MsgKeys.getOvenStatus_Rep:
                    AbsOven.this.status = (short) msg.optInt(MsgParams.OvenStatus);
                    AbsOven.this.alarm = (short) msg.optInt(MsgParams.OvenAlarm);
                    AbsOven.this.runP = (short) msg.optInt(MsgParams.OvenRunP);

                    AbsOven.this.temp = (short) msg.optInt(MsgParams.OvenTemp);
                    AbsOven.this.revolve = (short) msg.optInt(MsgParams.OvenRevolve);
                    //&0x0FFFF short溢出转无符号数据用int接受
                    AbsOven.this.hTime = ((short) msg.optInt(MsgParams.OvenTime))&0x0FFFF;
                    AbsOven.this.time = (short) msg.optInt(MsgParams.OvenTime);
                    AbsOven.this.light = (short) msg.optInt(MsgParams.OvenLight);
                    AbsOven.this.setTemp = (short) msg.optInt(MsgParams.OvenSetTemp);
                    AbsOven.this.setTime = (short) msg.optInt(MsgParams.OvenSetTime);
                    AbsOven.this.orderTime_min = (short) msg.optInt(MsgParams.OrderTime_value_min);
                    AbsOven.this.orderTime_hour = (short) msg.optInt(MsgParams.OrderTime_value_hour);
                    //026
                    AbsOven.this.autoMode = (short) msg.optInt(MsgParams.ovenAutoMode);
                    AbsOven.this.recipeId = (short) msg.optInt(MsgParams.OvenRecipeId);
                    AbsOven.this.recipeStep = (short) msg.optInt(MsgParams.OvenRecipeStep);
                    AbsOven.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    AbsOven.this.setTempDownValue = (short) msg.optInt(MsgParams.SetTempDownValue);
                    AbsOven.this.currentTempDownValue = (short) msg.optInt(MsgParams.CurrentTempDownValue);
                    AbsOven.this.currentStageValue = (short) msg.optInt(MsgParams.CurrentStatusValue);

                    AbsOven.this.PlatInsertStatueValue = (short) msg.optInt(MsgParams.PlatInsertStatueValue);
                    AbsOven.this.SetTemp2Value = (short) msg.optInt(MsgParams.SetTemp2Value);
                    AbsOven.this.SetTime2Value = (short) msg.optInt(MsgParams.SetTime2Value);
                    AbsOven.this.TempValue = (short) msg.optInt(MsgParams.TempValue);
                    AbsOven.this.LeftTime2Value = (short) msg.optInt(MsgParams.LeftTime2Value);
                    AbsOven.this.status2Values = (short) msg.optInt(MsgParams.status2Values);
                    //AbsOven.this.moreTotalValue = (short) msg.optInt(MsgParams.TotalValue);
                    AbsOven.this.setTimeHValve = (short) msg.optInt(MsgParams.SetTimeHValue);

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
    public String getOvenModel() {
        return null;
    }

    public void setOvenWorkTime(final short time, VoidCallback callback) {
        try {
            LogUtils.i("20171019", "time:" + time);
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);//待修改
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTime, time);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.time = time;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenWorkTemp(final short temp, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);////待修改
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, temp);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = temp;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenStatusControl(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenStatus, status);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    // AbsOven.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenQuickHeating(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenQuickHeat_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);

            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenTime, setTime);

            Log.i("201908051", "terminalType：" + String.valueOf(terminalType) + "  getSrcUser()：" + getSrcUser() + "  setTemp：" + String.valueOf(setTemp) + "  preflag：" + String.valueOf(preflag) + "  setTime：" + setTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenAirBaking(final short time, final short temp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenAirBaking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, temp);
            msg.putOpt(MsgParams.OvenTime, time);
            msg.putOpt(MsgParams.OvenPreFlag, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = temp;
                    AbsOven.this.time = (short) (time * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenToast(final short time, final short temp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenToast_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, temp);
            msg.putOpt(MsgParams.OvenTime, time);
            msg.putOpt(MsgParams.OvenPreFlag, 0);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = temp;
                    AbsOven.this.time = (short) (time * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenBottomHeating(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenBottomHeat_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, 0);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenUnfreeze(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenUnfreeze_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenAirBarbecue(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenAirBarbecue_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, 0);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenBarbecue(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenBarbecue_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, 0);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenStrongBarbecue(final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStrongBarbecue_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, 0);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenSpitRotateLightControl(final short revolve, final short light, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenSpitRotateLightControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenRevolve, revolve);
            msg.putOpt(MsgParams.OvenLight, light);
            msg.putOpt(MsgParams.OvenPreFlag, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.revolve = revolve;
                    AbsOven.this.light = light;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenStatus(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    // AbsOven.this.status = status;
                    LogUtils.i("20191218", "resMsg.toString()" + resMsg.toString());
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getOvenStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.getOvenStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.status = (short) resMsg.optInt(MsgParams.OvenStatus);
                    AbsOven.this.runP = (short) resMsg.optInt(MsgParams.OvenMode);
                    AbsOven.this.alarm = (short) resMsg.optInt(MsgParams.OvenAlarm);
                    AbsOven.this.temp = (short) resMsg.optInt(MsgParams.OvenTemp);
                    AbsOven.this.time = (short) resMsg.optInt(MsgParams.OvenTime);
                    AbsOven.this.light = (short) resMsg.optInt(MsgParams.OvenLight);
                    AbsOven.this.revolve = (short) resMsg.optInt(MsgParams.OvenRevolve);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------------R026指令


    /**
     * zdj add
     * 烤箱菜谱通用下发指令
     */
    public void setOvenRecipeParams(short msgKeys, final short setTime, final short setTemp, short preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(msgKeys);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("Ovens", "AbsOven-setOvenRecipeParams: " + t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * zdj add
     * 设置烤箱双温双控模式和专业模式
     */
    public void setOvenRunMode(short mode, final short setTime, final short setTempUp, short preflag,
                               short recipeId, short recipeStep, short ArgumentNumber, short SetTempDown,
                               VoidCallback callback) {
        setOvenRunMode(mode, setTime, setTempUp, preflag, recipeId, recipeStep, ArgumentNumber,
                SetTempDown, (short) 255, (short) 255, callback);
    }


    public void setOvenSubstrateRunMode(short mode, final short setTime, final short setTempUp,
                                        short preflag, short recipeId, short recipeStep,
                                        short ArgumentNumber, short SetTempDown, short setTemp2, short setTime2, VoidCallback callback) {
        setOvenSubstrateRunMode(mode, setTime, setTempUp, preflag, recipeId, recipeStep, ArgumentNumber,
                SetTempDown, (short) 255, (short) 255, setTemp2, setTime2, callback);
    }


    public void setOvenModelRunMode(short cmd, short mode, final short setTime, final short setTempUp, VoidCallback callback) {
        LogUtils.i("20180816", "cmd::" + cmd + " mode:" + mode + " setTime::" + setTime + " setTempUp::" + setTempUp);
        setOvenRunMode(cmd, mode, setTime, setTempUp, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, callback);
    }


    //烘烤DIY
    public void setOvenBakeDIYMode(short cmd, short mode, final short setTime, final short setTempUp, short barbecue, VoidCallback callback) {
        setOvenBakeDIYMode(cmd, mode, setTime, setTempUp, (short) 0, (short) 0, (short) 0, (short) 1, (short) 0, (short) 255, (short) 255, barbecue, callback);
    }


    public void setOvenEXPModelRunMode(short cmd, short mode, final short setTime, final short setTempUp, final short setTempDown, VoidCallback callback) {
        setOvenRunMode(cmd, mode, setTime, setTempUp, (short) 0, (short) 0, (short) 0, (short) 1, setTempDown, (short) 0, (short) 0, callback);
    }


    //烤箱模块化使用
    public void setOvenRunMode(
            short cmd,
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            msg.putOpt(MsgParams.OvenTime, lowTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }

            }
            if (setTime > 255) {
                msg.putOpt(MsgParams.ArgumentNumber, 1);
                msg.putOpt(MsgParams.SetTimeHKey, (short) 6);
                msg.putOpt(MsgParams.SetTimeHLength, (short) 1);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParams.SetTimeHValue, highTime);
            }
            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        //   onStatusChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //烤箱模块化使用
    public void setOvenRunMode(
            short cmd,
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            short Rotatebarbecue,
            short Set2TempDown,
            short SetTimeBottom,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }

                //新增 2019 09 09
                if (Rotatebarbecue != 0) {
                    msg.putOpt(MsgParams.Rotatebarbecue, (short) 3);
                    msg.putOpt(MsgParams.RotatebarbecueLength, (short) 3);
                    msg.putOpt(MsgParams.RotatebarbecueValue, Rotatebarbecue);//
                }


                if (SetTimeBottom != 0) {
                    msg.putOpt(MsgParams.SetTemp2Key, (short) 4);
                    msg.putOpt(MsgParams.SetTemp2Length, (short) 4);
                    msg.putOpt(MsgParams.SetTemp2Value, SetTimeBottom);
                }

                if (Set2TempDown != 0) {
                    msg.putOpt(MsgParams.SetTime2Key, (short) 5);
                    msg.putOpt(MsgParams.SetTime2Length, (short) 5);
                    msg.putOpt(MsgParams.SetTime2Value, Set2TempDown);
                }

            }

            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        //   onStatusChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //154
    public void setOvenRunMode(
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetOven_RunMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }
            }


            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOvenSubstrateRunMode(
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            final short setTemp2,
            final short setTime2,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetOven_RunMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }

                msg.putOpt(MsgParams.SetTemp2Key, (short) 4);
                msg.putOpt(MsgParams.SetTemp2Length, (short) 1);
                msg.putOpt(MsgParams.SetTemp2Value, setTemp2);

                msg.putOpt(MsgParams.SetTime2Key, (short) 5);
                msg.putOpt(MsgParams.SetTime2Length, (short) 1);
                msg.putOpt(MsgParams.SetTime2Value, setTime2);

            }


            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        AbsOven.this.SetTime2Value = setTime2;
                        AbsOven.this.SetTemp2Value = setTemp2;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * zdj add
     * 菜谱设置
     */
    public void setOvenRecipe(short ArgumentNumber, short recipeId, short OvenRecipeTotalStep, short recipeStep, short ovenMode, final short setTemp, final short setTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteamOven_Recipe_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeTotalStep, OvenRecipeTotalStep);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.OvenMode, ovenMode);
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.temp = setTemp;
                    AbsOven.this.time = (short) (setTime * 60);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * zdj add
     * 设置烤箱自动运行模式
     */
    public void setOvenAutoRunMode(short autoMode, short setTime, VoidCallback callback) {
        setOvenAutoRunMode(autoMode, setTime, (short) 0, (short) 0, (short) 0, callback);
    }

    public void setOvenAutoRunMode(short autoMode, final short setTime, short ArgumentNumber,
                                   short min, short hour, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.Set_Oven_Auto_Mode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ovenAutoMode, autoMode);
            msg.putOpt(MsgParams.OvenSetTime, setTime);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, min);
                msg.putOpt(MsgParams.OrderTime_value_hour, hour);
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.setTime = setTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * rent add
     * 设置菜谱开始指令
     */
    public void setRecipeOvenStatus(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20170314", "resMsg:" + resMsg.toString());
                    //AbsOven.this.status = status;
                    //onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
            Msg msg = newReqMsg(MsgKeys.Set_Oven_Light_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenLight, light);
            msg.putOpt(MsgParams.ArgumentNumber, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.light = light;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换026、016参数值
     *
     * @return
     */
    public static short getExpMode(int mode_) {
        short m = 0;
        switch (mode_) {
            case 9:
                m = 9;
                break;
            case 130:
                m = 1;
                break;
            case 132:
                m = 2;
                break;
            case 134:
                m = 3;
                break;
            case 136:
                m = 4;
                break;
            case 138:
                m = 5;
                break;
            case 140:
                m = 6;
                break;
            case 142:
                m = 7;
                break;
            case 144:
                m = 8;
                break;
            default:
                break;
        }
        return m;
    }


    //分区模式-上下层
    public void setOvenRunModeTopAndBottom(
            short cmd,
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,

            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            short Rotatebarbecue,
            short setTemp2,
            short setTime2,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }

                //新增 2019 09 09
                if (Rotatebarbecue != 0) {
                    msg.putOpt(MsgParams.Rotatebarbecue, (short) 3);
                    msg.putOpt(MsgParams.RotatebarbecueLength, (short) 1);
                    msg.putOpt(MsgParams.RotatebarbecueValue, Rotatebarbecue);//
                }


                if (setTemp2 != 0) {
                    msg.putOpt(MsgParams.SetTemp2Key, (short) 4);
                    msg.putOpt(MsgParams.SetTemp2Length, (short) 1);
                    msg.putOpt(MsgParams.SetTemp2Value, setTemp2);
                }

                if (setTime2 != 0) {
                    msg.putOpt(MsgParams.SetTime2Key, (short) 5);
                    msg.putOpt(MsgParams.SetTime2Length, (short) 1);
                    msg.putOpt(MsgParams.SetTime2Value, setTime2);
                }


            }

            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                LogUtils.i("20191022", "msg:" + msg.toString());
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        //onStatusChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //烘烤diy专业模式
    public void setOvenBakeDIYMode(
            short cmd,
            short mode,
            final short setTime,
            final short setTempUp,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short SetTempDown,
            short orderTime_min,
            short orderTime_hour,
            short Rotatebarbecue,
            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }

                msg.putOpt(MsgParams.Rotatebarbecue, (short) 3);
                msg.putOpt(MsgParams.RotatebarbecueLength, (short) 1);
                msg.putOpt(MsgParams.RotatebarbecueValue, Rotatebarbecue);


            }

            if (mode == 9) {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTempDownValue = SetTempDown;
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        onStatusChanged();
                    }
                });
            } else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        AbsOven.this.setTemp = setTempUp;
                        AbsOven.this.setTime = setTime;
                        //   onStatusChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //035烤箱 分区模式 组合模式
    public void setOvenCombination(short cmd,
                                   short mode,
                                   short setTemp,
                                   short setTime,
                                   short setTemp2,
                                   short setTime2,
                                   VoidCallback callback) {
        setOven035Mode(
                cmd,
                mode,
                setTemp,
                setTime,
                (short) 0,
                (short) 0,
                (short) 0,
                (short) 2,//参数个数
                setTemp2,
                setTime2,
                callback);
    }


    public void setOven035Mode(
            short cmd,
            short mode,
            final short setTemp,
            final short setTime,
            short preflag,
            short recipeId,
            short recipeStep,
            short ArgumentNumber,
            final short setTemp2,
            final short setTime2,

            VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTemp);
            msg.putOpt(MsgParams.OvenTime, setTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {

                msg.putOpt(MsgParams.SetTemp2Key, (short) 4);
                msg.putOpt(MsgParams.SetTemp2Length, (short) 1);
                msg.putOpt(MsgParams.SetTemp2Value, setTemp2);

                msg.putOpt(MsgParams.SetTime2Key, (short) 5);
                msg.putOpt(MsgParams.SetTime2Length, (short) 1);
                msg.putOpt(MsgParams.SetTime2Value, setTime2);


            }

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsOven.this.setTemp = setTemp;
                    AbsOven.this.setTime = setTime;
                    AbsOven.this.SetTime2Value = setTime2;
                    AbsOven.this.SetTemp2Value = setTemp2;
                    //onStatusChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}