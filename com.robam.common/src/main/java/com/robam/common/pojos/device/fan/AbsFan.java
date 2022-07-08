package com.robam.common.pojos.device.fan;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeleteChildDeviceEvent;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanOilCupCleanEvent;
import com.robam.common.events.FanPlateRemoveEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.FanTimingCompletedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.SmartParams;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.WeeklyVentilationDate_Hour;
import static com.robam.common.io.device.MsgParams.WeeklyVentilationDate_Minute;
import static com.robam.common.io.device.MsgParams.WeeklyVentilationDate_Week;

public class AbsFan extends AbsDeviceHub implements IFan, Serializable {

    static final public short PowerLevel_0 = 0;
    static final public short PowerLevel_1 = 1;
    static final public short PowerLevel_2 = 2;
    static final public short PowerLevel_3 = 3;
    static final public short PowerLevel_4 = 4;
    static final public short PowerLevel_5 = 5;
    static final public short PowerLevel_6 = 6;

    static final public short Event_Power = 10;
    static final public short Event_TimingCompleted = 11;
    static final public short Event_Level = 12;
    static final public short Event_Light = 13;
    static final public short Event_CleanNotic = 14;
    static final public short Event_CleanLock = 15;
    static final public short Event_PlateRemove = 16;
    static final public short Event_OilCup = 20;


    protected short terminalType = TerminalType.getType();
    public SmartParams smartParams = new SmartParams();
    public short status;
    public short prestatus;
    public short level;
    public short prelevel;
    public short timeLevel;
    public short timeWork;
    public short waitTime;
    public short aerialDetection;
    public short oilCup;
    public short smartSmokeStatus;
    public boolean light;
    public boolean clean;
    public short wifi;
    public short argument;
    public short backsmoke = 255;//回烟
    public short braiseAlarm;
    public short leftStoveBraiseAlarm;
    public short rightStoveBraiseAlarm;
    public int temperatureReportOne;
    public int temperatureReportTwo;
    public int ventilationRemainingTime;
    public short stoveLinkageRemainingTime;
    public short periodicallyRemainingTime;
    public short presTurnOffRemainingTime;
    public short overTempProtectStatus;
    private short remindTime;
    public short eventId;
    public short eventParam;
    /**
     * 智感恒吸
     */
    public short cruise;


    public AbsFan(DeviceInfo devInfo) {
        super(devInfo);
    }


    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------


    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                Log.i("fan_polling", "烟机 onPolling");
            Msg reqMsg = newReqMsg(MsgKeys.GetFanStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("Fan onStatusChanged. isConnected:%s level:%s", isConnected(), level));
        }
        //if (Plat.DEBUG)
            LogUtils.i("fan_polling_rep", " oilCup:" + oilCup + " status:" + status + " prestatus" + prestatus + " level:" + level
                    + " prelevel:" + prelevel + " timeLevel:" + timeLevel + " timeWork" + timeWork
                    + " light:" + light + " clean " + clean + " wifi " + wifi + " argument "
                    + argument + " backsmoke " + Integer.toHexString(backsmoke));


        postEvent(new FanStatusChangedEvent(AbsFan.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.FanEvent_Noti:
                    AbsFan.this.eventId = (short) msg.optInt(MsgParams.EventId);
                    AbsFan.this.eventParam = (short) msg.optInt(MsgParams.EventParam);
                    LogUtils.i("20180523", "eventId::" + eventId);
                    switch (eventId) {
                        case Event_Power:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanPowerEvent(this, 1 == eventParam));
                            }
                            break;
                        case Event_Light:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanLightEvent(this, 1 == eventParam));
                            }
                            break;
                        case Event_Level:
                            if (this.getID() != null && this.getID().equals(Utils.getDefaultFan().getID())) {
                                postEvent(new FanLevelEvent(this, eventParam));
                            }
                            break;
                        case Event_TimingCompleted:
                            postEvent(new FanTimingCompletedEvent(this, eventParam));
                            break;
                        case Event_CleanNotic://清洗提醒
                            postEvent(new FanCleanNoticEvent(this));
                            LogUtils.i("20180608", "FanCleanNoticEvent:");
                            break;
                        case Event_CleanLock://清洗锁定
                            postEvent(new FanCleanLockEvent(this, eventParam));
                            LogUtils.i("202005120111", "FanCleanLockEvent:");
                            break;
                        case Event_PlateRemove://挡风板拆除
                            postEvent(new FanPlateRemoveEvent(this, eventParam));
                            LogUtils.i("20180608", "FanPlateRemoveEvent:");
                            break;

                        case Event_OilCup:   //油杯提醒
                            LogUtils.i("20180322", "event-cup::" + Event_OilCup);
                            postEvent(new FanOilCupCleanEvent(this));
                            break;
                    }

                    break;
                case MsgKeys.GetFanStatus_Rep:
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = (short) msg.optInt(MsgParams.FanStatus);
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = (short) msg.optInt(MsgParams.FanLevel);
                    AbsFan.this.light = msg.optBoolean(MsgParams.FanLight);
                    AbsFan.this.clean = msg.optBoolean(MsgParams.NeedClean);
                    AbsFan.this.timeWork = (short) msg.optInt(MsgParams.FanTime);
                    AbsFan.this.wifi = (short) msg.optInt(MsgParams.FanWIfi);
                    AbsFan.this.argument = (short) msg.optInt(MsgParams.ArgumentNumber);
                    AbsFan.this.backsmoke = (short) msg.optInt(MsgParams.BackSmoke);
                    AbsFan.this.waitTime = (short) msg.optInt(MsgParams.waitTime);
                    AbsFan.this.aerialDetection = (short) msg.optInt(MsgParams.aerialDetection);
                    AbsFan.this.oilCup = (short) msg.optInt(MsgParams.oilCup);
                    AbsFan.this.smartSmokeStatus = (short) msg.optInt(MsgParams.smartSmokeStatus);


                    AbsFan.this.temperatureReportOne = msg.optInt(MsgParams.TemperatureReportOne);
                    AbsFan.this.temperatureReportTwo = msg.optInt(MsgParams.TemperatureReportTwo);
                    AbsFan.this.braiseAlarm = (short) msg.optInt(MsgParams.BraiseAlarm);
                    //20220505新增变速巡航
                    AbsFan.this.cruise = (short) msg.optInt(MsgParams.CRUISE);
                    byte[] args = new byte[8];
                    for (short i = 7; i > -1; i--) {
                        args[7 - i] = ((braiseAlarm & (1 << i)) == 0) ? (byte) 0 : 1;
                        leftStoveBraiseAlarm = args[7];
                        rightStoveBraiseAlarm = args[6];
                    }
                    AbsFan.this.ventilationRemainingTime = (int) msg.optInt(MsgParams.RegularVentilationRemainingTime);
                    AbsFan.this.stoveLinkageRemainingTime = (short) msg.optInt(MsgParams.FanStoveLinkageVentilationRemainingTime);
                    //判断是否接收成功 255代表没有接收成功    5916s
                    if ((short) msg.optInt(MsgParams.PeriodicallyRemindTheRemainingTime) != 255) {
                        AbsFan.this.periodicallyRemainingTime = (short) msg.optInt(MsgParams.PeriodicallyRemindTheRemainingTime);
                        if (AbsFan.this.getDt().equals("5916S")) {
                            LogUtils.i("20190826111111111", "periodicallyRemainingTime:0::" + periodicallyRemainingTime);
                        }
                        LogUtils.i("20190826", "periodicallyRemainingTime:1::" + periodicallyRemainingTime);
                    } else {
                        AbsFan.this.periodicallyRemainingTime = (short) 0;
                        LogUtils.i("20190826", "periodicallyRemainingTime:2::" + periodicallyRemainingTime);
                    }
                    AbsFan.this.presTurnOffRemainingTime = (short) msg.optInt(MsgParams.PresTurnOffRemainingTime);
                    AbsFan.this.overTempProtectStatus = (short) msg.optInt(MsgParams.OverTempProtectStatus);
                    LogUtils.i("202005120111","status:::"+status);

                    onStatusChanged();

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // -------------------------------------------------------------------------------
    // IFan
    // -------------------------------------------------------------------------------
    short savedLevel;

    @Override
    public void pause() {
        if (level > PowerLevel_0) {
            savedLevel = level;
            setFanLevel(PowerLevel_1, null);
        }
    }

    @Override
    public void restore() {
        setFanLevel(savedLevel, null);
        savedLevel = PowerLevel_0;
    }


    @Override
    public void getFanStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetFanStatus_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = (short) resMsg.optInt(MsgParams.FanStatus);
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = (short) resMsg.optInt(MsgParams.FanLevel);
                    AbsFan.this.light = resMsg.optBoolean(MsgParams.FanLight);
                    AbsFan.this.clean = resMsg.optBoolean(MsgParams.NeedClean);
                    AbsFan.this.timeWork = (short) resMsg.optInt(MsgParams.FanTime);   //增加预约时间 by zhaiyuanyi
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getFanModel() {
        return null;
    }

    @Override
    public void setFanStatus(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanStatus, status);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearOilCupAramTime(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanCleanOirCupTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prestatus = AbsFan.this.status;
                    AbsFan.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 设置烟机档位
     *
     * @param level    0、1、2、3、6档
     * @param callback
     */
    @Override
    public void setFanLevel(final short level, VoidCallback callback) {
        LogUtils.i("20190521", "level:" + level + " hashCode:" + this.hashCode());
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanLevel_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = level;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFanLight(final boolean light, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanLight_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLight, light);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.light = light;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanAllParams(final short level, final boolean light,
                                VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanAllParams_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.putOpt(MsgParams.FanLight, light);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.prelevel = AbsFan.this.level;
                    AbsFan.this.level = level;
                    AbsFan.this.light = light;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void restFanCleanTime(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.RestFanCleanTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void RestFanNetBoard(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.RestFanNetBoard_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanTimeWork(final short level, final short time,
                               VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanTimeWork_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            if (IRokiFamily.HE905.equals(getDt())) {
                // 控制端类型区分 by zhaiyuanyi
                msg.putOpt(MsgParams.UserId, getSrcUser());
            }
            msg.putOpt(MsgParams.FanLevel, level);
            msg.putOpt(MsgParams.FanTime, time);
            msg.setIsFan(true);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.timeLevel = level;
                    AbsFan.this.timeWork = time;
                    if (Plat.DEBUG)
                        LogUtils.i("20170328", "resmsg:" + resMsg.toString());
                    onStatusChanged();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getSmartConfig(final Callback<SmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    smartParams.IsPowerLinkage = resMsg.optBoolean(MsgParams.IsPowerLinkage);
                    smartParams.IsLevelLinkage = resMsg.optBoolean(MsgParams.IsLevelLinkage);
                    smartParams.IsShutdownLinkage = resMsg.optBoolean(MsgParams.IsShutdownLinkage);
                    smartParams.ShutdownDelay = (short) resMsg.optInt(MsgParams.ShutdownDelay);
                    smartParams.IsNoticClean = resMsg.optBoolean(MsgParams.IsNoticClean);
                    smartParams.IsTimingVentilation = resMsg.optBoolean(MsgParams.IsTimingVentilation);
                    smartParams.TimingVentilationPeriod = (short) resMsg.optInt(MsgParams.TimingVentilationPeriod);
                    smartParams.IsWeeklyVentilation = resMsg.optBoolean(MsgParams.IsWeeklyVentilation);
                    smartParams.WeeklyVentilationDate_Week = (short) resMsg.optInt(WeeklyVentilationDate_Week);
                    smartParams.WeeklyVentilationDate_Hour = (short) resMsg.optInt(WeeklyVentilationDate_Hour);
                    smartParams.WeeklyVentilationDate_Minute = (short) resMsg.optInt(WeeklyVentilationDate_Minute);
                    smartParams.IsOverTempProtectSwitch = (short) resMsg.optInt(MsgParams.OverTempProtectSwitch) == 1;
                    smartParams.IsOverTempProtectSet = (short) resMsg.optInt(MsgParams.OverTempProtectSet);
                    smartParams.R8230S_Switch = (short) resMsg.optInt(MsgParams.R8230SFrySwitch);
                    smartParams.R8230S_Time = (short) resMsg.optInt(MsgParams.R8230SFryTime);
                    smartParams.gesture = (short) resMsg.optInt(MsgParams.gesture);
                    //添加灶具小火，烟机自动匹配风量字段
                    smartParams.fanStoveAuto = (short) resMsg.optInt(MsgParams.fanStoveAuto);
                    Log.i("smartParams" , "-------"  +smartParams.fanStoveAuto) ;
                    if (smartParams.TimingVentilationPeriod == 255)
                        smartParams.TimingVentilationPeriod = 3;
                    if (smartParams.WeeklyVentilationDate_Week == 255)
                        smartParams.WeeklyVentilationDate_Week = 1;
                    if (smartParams.WeeklyVentilationDate_Hour == 255)
                        smartParams.WeeklyVentilationDate_Hour = 12;
                    if (smartParams.WeeklyVentilationDate_Minute == 255)
                        smartParams.WeeklyVentilationDate_Minute = 30;
                    if (smartParams.IsOverTempProtectSet == 255) {
                        smartParams.IsOverTempProtectSet = 280;
                    }

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
            if (IRokiFamily.SE638.equals(this.getDt()) || IRokiFamily.HE905.equals(this.getDt())) {
                msg.putOpt(MsgParams.UserId, getSrcUser());
            }
            //增加 userid
            msg.putOpt(MsgParams.IsPowerLinkage, smartParams.IsPowerLinkage);
            msg.putOpt(MsgParams.IsLevelLinkage, smartParams.IsLevelLinkage);
            msg.putOpt(MsgParams.IsShutdownLinkage, smartParams.IsShutdownLinkage);
            msg.putOpt(MsgParams.ShutdownDelay, smartParams.ShutdownDelay);
            msg.putOpt(MsgParams.IsNoticClean, smartParams.IsNoticClean);
            msg.putOpt(MsgParams.IsTimingVentilation, smartParams.IsTimingVentilation);
            msg.putOpt(MsgParams.TimingVentilationPeriod, smartParams.TimingVentilationPeriod);
            msg.putOpt(MsgParams.IsWeeklyVentilation, smartParams.IsWeeklyVentilation);
            msg.putOpt(WeeklyVentilationDate_Week, smartParams.WeeklyVentilationDate_Week);
            msg.putOpt(WeeklyVentilationDate_Hour, smartParams.WeeklyVentilationDate_Hour);
            msg.putOpt(WeeklyVentilationDate_Minute, smartParams.WeeklyVentilationDate_Minute);
            //msg.putOpt(MsgParams.ArgumentNumber, 1);
            //msg.putOpt(MsgParams.Length, 2);
            //msg.putOpt(MsgParams.R8230SFrySwitch, smartParams.R8230S_Switch);
            //msg.putOpt(MsgParams.R8230SFryTime, smartParams.R8230S_Time);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setRegularlyRemind(short regularSwitch, final short remindTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSmartConfig_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.TimeReminderSetSwitch, regularSwitch);
            msg.putOpt(MsgParams.TimeReminderSetTime, remindTime);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.remindTime = remindTime;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addPotDevice() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("pot_adddevice", "温控锅添加 53指令");
            Msg reqMsg = newReqMsg(MsgKeys.FanAddPot_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, new RCMsgCallbackWithVoid(new VoidCallback() {
                @Override
                public void onSuccess() {
                    if (Plat.DEBUG)
                        LogUtils.i("pot_adddevice_rep", "温控锅添加 54指令 成功");
                }

                @Override
                public void onFailure(Throwable t) {
                    if (Plat.DEBUG)
                        LogUtils.i("pot_adddevice_rep", "温控锅添加 54指令 失败");
                }
            }));
        } catch (Exception e) {
        }
    }

    // -------------------------------------------------------------------------------
    public void delPotDevice(String deviceID) {
        try {
            if (Plat.DEBUG)
                LogUtils.i("20180621", "11111111111111KAI 38指令");
            Msg reqMsg = newReqMsg(MsgKeys.FanDelPot_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.putOpt(MsgParams.DeviceId, deviceID);
            reqMsg.setIsFan(false);
            sendMsg(reqMsg, new RCMsgCallbackWithVoid(new VoidCallback() {
                @Override
                public void onSuccess() {
                    EventUtils.postEvent(new DeleteChildDeviceEvent(true));
                    LogUtils.i("20180621", "温控锅失败 38指令 成功");

                }

                @Override
                public void onFailure(Throwable t) {
                    EventUtils.postEvent(new DeleteChildDeviceEvent(false));
                    if (Plat.DEBUG)
                        LogUtils.i("20180621", "温控锅失败 38指令 失败");
                }
            }));
        } catch (Exception e) {
        }
    }


    /**
     * 设置智能烟感状态
     */
    @Override
    public void setFanSmartSmoke(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.FanFeelPowerKey, 10);
                msg.putOpt(MsgParams.FanFeelPowerLength, 1);
                msg.putOpt(MsgParams.FanFeelPower, fanStatusComposite.FanFeelPower);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190424", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190424", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时通风
     *
     * @param fanStatusComposite
     * @param argumentNumber
     * @param callback
     */
    @Override
    public void setFanTimingVentilationTime(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.TimeAirPowerKey, 6);
                msg.putOpt(MsgParams.TimeAirPowerLength, 2);
                msg.putOpt(MsgParams.TimeAirPower, fanStatusComposite.IsTimingVentilation);
                msg.putOpt(MsgParams.TimeAirPowerDay, fanStatusComposite.TimingVentilationPeriod);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 烟灶联动
     */
    @Override
    public void setPowerLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.FanStovePowerKey, 1);
                msg.putOpt(MsgParams.FanStovePowerLength, 1);
                msg.putOpt(MsgParams.FanStovePower, fanStatusComposite.IsPowerLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * y烟灶联动
     */
    public void setPowerLinkageSwitch(short isPowerLinkage, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.FanStovePowerKey, 1);
                msg.putOpt(MsgParams.FanStovePowerLength, 1);
                msg.putOpt(MsgParams.FanStovePower, isPowerLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置灶具烟机档位联动开关
     */
    @Override
    public void setLevelLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.FanPowerLinkKey, 2);
                msg.putOpt(MsgParams.FanPowerLinkLength, 1);
                msg.putOpt(MsgParams.FanPowerLink, fanStatusComposite.IsLevelLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灶具关闭后烟机延时开关
     */
    @Override
    public void setShutdownLinkageSwitch(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.StoveShutDelayKey, 3);
                msg.putOpt(MsgParams.StoveShutDelayLength, 1);
                msg.putOpt(MsgParams.StoveShutDelay, fanStatusComposite.IsShutdownLinkage);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灶具关闭后烟机延时时间
     */
    @Override
    public void setShutdownLinkageTime(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.StoveShutDelayTimeKey, 4);
                msg.putOpt(MsgParams.StoveShutDelayTimeLength, 1);
                msg.putOpt(MsgParams.StoveShutDelayTime, fanStatusComposite.ShutdownDelay);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置烟机油网清洗提示开关
     */
    @Override
    public void setFanOilCleanHintSwitch(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.FanCleanPowerKey, 5);
                msg.putOpt(MsgParams.FanCleanPowerLength, 1);
                msg.putOpt(MsgParams.FanCleanPower, fanStatusComposite.IsNoticClean);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置礼拜几通风
     */
    @Override
    public void setFanWeekDay(FanStatusComposite fanStatusComposite, short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {

                msg.putOpt(MsgParams.AirTimePowerKey, 7);
                msg.putOpt(MsgParams.AirTimePowerLength, 4);
                msg.putOpt(MsgParams.AirTimePower, fanStatusComposite.IsWeeklyVentilation);
                msg.putOpt(MsgParams.AirTimeWeek, fanStatusComposite.WeeklyVentilationDate_Week);
                msg.putOpt(MsgParams.AirTimeHour, fanStatusComposite.WeeklyVentilationDate_Hour);
                msg.putOpt(MsgParams.AirTimeMinute, fanStatusComposite.WeeklyVentilationDate_Minute);

            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置爆炒时间
     */
    @Override
    public void setFryStrongTime(FanStatusComposite fanStatusComposite, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.FryStrongTimePowerKey, 8);
            msg.putOpt(MsgParams.FryStrongTimePowerLength, 2);
            msg.putOpt(MsgParams.FryStrongTimePower, fanStatusComposite.R8230S_Switch);
            msg.putOpt(MsgParams.FryStrongTime, fanStatusComposite.R8230S_Time);
            msg.putOpt(MsgParams.gesture, fanStatusComposite.gestureControlSwitch);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置倒油杯提示开关
     */
    @Override
    public void setCupOilPower(FanStatusComposite fanStatusComposite, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.CupOilPowerKey, 9);
            msg.putOpt(MsgParams.CupOilPowerLength, 1);
            msg.putOpt(MsgParams.CupOilPower, fanStatusComposite.FanCupOilSwitch);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置防干烧提示开关
     *
     * @param fanStatusComposite
     * @param callback
     */
    @Override
    public void setProtectTipDryPower(FanStatusComposite fanStatusComposite, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.ProtectTipDryPowerKey, 11);
            msg.putOpt(MsgParams.ProtectTipDryPowerLength, 1);
            msg.putOpt(MsgParams.ProtectTipDryPower, fanStatusComposite.dryBurningPromptSwitch);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置变速巡航
     *
     * @param callback
     */
    public void setCruise(int cruise, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.CruiseKey, 17);
            msg.putOpt(MsgParams.CruiseLength, 1);
            msg.putOpt(MsgParams.CruiseValue, cruise);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置灶具最小火力烟机自动调节
     *
     * @param callback
     */
    public void setFanStoveAuto(int fanStoveAuto, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.fanStoveKey, 18);
            msg.putOpt(MsgParams.fanStoveLength, 1);
            msg.putOpt(MsgParams.fanStoveValue, fanStoveAuto);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置灶具最小火力烟机自动调节
     *
     * @param callback
     */
    public void setDelayedShut(int time, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.StoveShutDelayTimeKey, 4);
            msg.putOpt(MsgParams.StoveShutDelayTimeLength, 1);
            msg.putOpt(MsgParams.StoveShutDelayTime, time);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setProtectDryPower(FanStatusComposite fanStatusComposite, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.ProtectDryPowerKey, 12);
            msg.putOpt(MsgParams.ProtectDryPowerLength, 1);
            msg.putOpt(MsgParams.ProtectDryPower, fanStatusComposite.dryBurningSwitch);

            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置手势控制
     *
     * @param fanStatusComposite
     * @param callback
     */
    @Override
    public void setGestureControl(FanStatusComposite fanStatusComposite, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.GestureControlPowerKey, 13);
            msg.putOpt(MsgParams.GestureControlPowerLength, 1);
            msg.putOpt(MsgParams.gesture, fanStatusComposite.gestureControlSwitch);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190926", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190926", "Throwable:" + t.toString());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setGestureRecognitionSwitch(FanStatusComposite fanStatusComposite,
                                            short argumentNumber, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Rep);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.Key, 13);
                msg.putOpt(MsgParams.Length, 1);
                msg.putOpt(MsgParams.gesture, fanStatusComposite.gestureControlSwitch);
            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190926", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190926", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void setFanCombo(FanStatusComposite fanStatusComposite, short argumentNumber, List<Integer> listKey, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatusCompose_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                for (int i = 0; i < listKey.size(); i++) {
                    Integer key = listKey.get(i);
                    switch (key) {
                        case 1:
                            msg.putOpt(MsgParams.FanStovePowerKey, key);
                            msg.putOpt(MsgParams.FanStovePowerLength, 1);
                            msg.putOpt(MsgParams.FanStovePower, fanStatusComposite.IsPowerLinkage);
                            break;
                        case 2:
                            msg.putOpt(MsgParams.FanPowerLinkKey, key);
                            msg.putOpt(MsgParams.FanPowerLinkLength, 1);
                            msg.putOpt(MsgParams.FanPowerLink, fanStatusComposite.IsLevelLinkage);
                            break;
                        case 3:
                            msg.putOpt(MsgParams.StoveShutDelayKey, key);
                            msg.putOpt(MsgParams.StoveShutDelayLength, 1);
                            msg.putOpt(MsgParams.StoveShutDelay, fanStatusComposite.IsShutdownLinkage);
                            break;
                        case 4:
                            msg.putOpt(MsgParams.StoveShutDelayTimeKey, key);
                            msg.putOpt(MsgParams.StoveShutDelayTimeLength, 1);
                            msg.putOpt(MsgParams.StoveShutDelayTime, fanStatusComposite.ShutdownDelay);
                            break;
                        case 5:
                            msg.putOpt(MsgParams.FanCleanPowerKey, key);
                            msg.putOpt(MsgParams.FanCleanPowerLength, 1);
                            msg.putOpt(MsgParams.FanCleanPower, fanStatusComposite.IsNoticClean);
                            break;
                        case 6:
                            msg.putOpt(MsgParams.TimeAirPowerKey, key);
                            msg.putOpt(MsgParams.TimeAirPowerLength, 2);
                            msg.putOpt(MsgParams.TimeAirPower, fanStatusComposite.IsTimingVentilation);
                            msg.putOpt(MsgParams.TimeAirPowerDay, fanStatusComposite.TimingVentilationPeriod);

                            break;

                        case 7:
                            msg.putOpt(MsgParams.AirTimePowerKey, key);
                            msg.putOpt(MsgParams.AirTimePowerLength, 4);
                            msg.putOpt(MsgParams.AirTimePower, fanStatusComposite.IsWeeklyVentilation);
                            msg.putOpt(MsgParams.AirTimeWeek, fanStatusComposite.WeeklyVentilationDate_Week);
                            msg.putOpt(MsgParams.AirTimeHour, fanStatusComposite.WeeklyVentilationDate_Hour);
                            msg.putOpt(MsgParams.AirTimeMinute, fanStatusComposite.WeeklyVentilationDate_Minute);
                            break;
                        case 8:
                            msg.putOpt(MsgParams.FryStrongTimePowerKey, key);
                            msg.putOpt(MsgParams.FryStrongTimePowerLength, 2);
                            msg.putOpt(MsgParams.FryStrongTimePower, fanStatusComposite.R8230S_Switch);
                            msg.putOpt(MsgParams.FryStrongTime, fanStatusComposite.R8230S_Time);
                            break;
                        case 9:
                            msg.putOpt(MsgParams.CupOilPowerKey, key);
                            msg.putOpt(MsgParams.CupOilPowerLength, 1);
                            msg.putOpt(MsgParams.CupOilPower, fanStatusComposite.FanCupOilSwitch);
                            break;
                        case 10:
                            msg.putOpt(MsgParams.FanFeelPowerKey, key);
                            msg.putOpt(MsgParams.FanFeelPowerLength, 1);
                            msg.putOpt(MsgParams.FanFeelPower, fanStatusComposite.FanFeelPower);
                            break;
                        case 11:
                            msg.putOpt(MsgParams.ProtectTipDryPowerKey, key);
                            msg.putOpt(MsgParams.ProtectTipDryPowerLength, 1);
                            msg.putOpt(MsgParams.ProtectTipDryPower, fanStatusComposite.dryBurningPromptSwitch);
                            break;
                        case 12:
                            msg.putOpt(MsgParams.ProtectDryPowerKey, key);
                            msg.putOpt(MsgParams.ProtectDryPowerLength, 1);
                            msg.putOpt(MsgParams.ProtectDryPower, fanStatusComposite.dryBurningSwitch);
                            break;
                        case 13:
                            msg.putOpt(MsgParams.GestureControlPowerKey, key);
                            msg.putOpt(MsgParams.GestureControlPowerLength, 1);
                            msg.putOpt(MsgParams.gesture, fanStatusComposite.gestureControlSwitch);
                            break;
                        //新增 2020年3月24日 12:49:56 8235S
                        case 14:
                            msg.putOpt(MsgParams.OverTempProtectSwitchKey, key);
                            msg.putOpt(MsgParams.OverTempProtectSwitchLength, 1);
                            msg.putOpt(MsgParams.OverTempProtectSwitch, fanStatusComposite.IsOverTempProtectSwitch);
                            break;
                        case 15:
                            msg.putOpt(MsgParams.OverTempProtectSetKey, key);
                            msg.putOpt(MsgParams.OverTempProtectSetLength, 2);
                            msg.putOpt(MsgParams.OverTempProtectSet, fanStatusComposite.IsOverTempProtectSet);
                            break;
                        default:
                            break;
                    }
                }


            }
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190425", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190425", "Throwable:" + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置定时提醒
    public void setTimingRemind(short TimeReminderSetSwitch, final short TimeReminderSetTime, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanTimingRemind_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.TimeReminderSetSwitch, TimeReminderSetSwitch);
            msg.putOpt(MsgParams.TimeReminderSetTime, TimeReminderSetTime);
            msg.setIsFan(true);
            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsFan.this.periodicallyRemainingTime = TimeReminderSetTime;
                    LogUtils.i("20190813", "afterSuccess:" + resMsg.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                    LogUtils.i("20190813", "Throwable:" + t.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // -------------------------------------------------------------------------------

    @Override
    protected void initStatus() {
        super.initStatus();

        status = FanStatus.Off;
        level = 0;
        timeLevel = 0;
        timeWork = 0;
        light = false;
        clean = false;
        wifi = -1;
    }

}
