package com.robam.common.pojos.device.fan;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanLevelEvent;
import com.robam.common.events.FanLightEvent;
import com.robam.common.events.FanPlateRemoveEvent;
import com.robam.common.events.FanPowerEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.FanTimingCompletedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.SmartParams;

/**
 * Created by Administrator on 2016/4/11.
 */
public class AbsFanPAD extends AbsFan implements IFan {

    static final public short PowerLevel_0 = 0;
    static final public short PowerLevel_1 = 1;
    static final public short PowerLevel_2 = 2;
    static final public short PowerLevel_3 = 3;
    static final public short PowerLevel_6 = 6;

    static final public short Event_Power = 10;
    static final public short Event_TimingCompleted = 11;
    static final public short Event_Level = 12;
    static final public short Event_Light = 13;
    static final public short Event_CleanNotic = 14;
    static final public short Event_CleanLock = 15;
    static final public short Event_PlateRemove = 16;


    protected short terminalType = TerminalType.getType();
    public SmartParams smartParams = new SmartParams();
    public short status;
    public short level;
    public short timeLevel;
    public short timeWork;
    public boolean light;
    public boolean clean;


    public AbsFanPAD(DeviceInfo devInfo) {
        super(devInfo);
    }


    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------


    @Override
    public void onPolling() {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.GetFanStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsgBySerial(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("Fan onStatusChanged. isConnected:%s level:%s", isConnected, level));
        }

        postEvent(new FanStatusChangedEvent(AbsFanPAD.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.FanEvent_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);

                    switch (eventId) {
                        case Event_Power:
                            postEvent(new FanPowerEvent(this, 1 == eventParam));
                            break;
                        case Event_Light:
                            postEvent(new FanLightEvent(this, 1 == eventParam));
                            break;
                        case Event_Level:
                            postEvent(new FanLevelEvent(this, eventParam));
                            break;
                        case Event_TimingCompleted:
                            postEvent(new FanTimingCompletedEvent(this,eventParam));
                            break;
                        case Event_CleanNotic:
                            postEvent(new FanCleanNoticEvent(this));
                            break;
                        case Event_CleanLock:
                            postEvent(new FanCleanLockEvent(this,eventParam));
                            break;
                        case Event_PlateRemove:
                            postEvent(new FanPlateRemoveEvent(this,eventParam));
                    }

                    break;
                case MsgKeys.GetFanStatus_Rep:

                    AbsFanPAD.this.status = (short) msg.optInt(MsgParams.FanStatus);
                    AbsFanPAD.this.level = (short) msg.optInt(MsgParams.FanLevel);
                    AbsFanPAD.this.light = msg.optBoolean(MsgParams.FanLight);
                    AbsFanPAD.this.clean = msg.optBoolean(MsgParams.NeedClean);
                    AbsFanPAD.this.timeWork=(short) msg.optInt(MsgParams.FanTime);

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
    public boolean isHardIsConnected() {
        return false;
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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.status = (short) resMsg.optInt(MsgParams.FanStatus);
                    AbsFanPAD.this.level = (short) resMsg.optInt(MsgParams.FanLevel);
                    AbsFanPAD.this.light = resMsg.optBoolean(MsgParams.FanLight);
                    AbsFanPAD.this.clean = resMsg.optBoolean(MsgParams.NeedClean);
                    AbsFanPAD.this.timeWork = (short)resMsg.optInt(MsgParams.FanTime);   //增加预约时间 by zhaiyuanyi

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public String getFanModel() {
//        return null;
//    }

    @Override
    public void setFanStatus(final short status, VoidCallback callback) {
        Log.i("ergergergerg","absfunpad");
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanStatus, status);

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.status = status;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanLevel(final short level, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanLevel_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.level = level;
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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.light = light;

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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.level = level;
                    AbsFanPAD.this.light = light;

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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback));
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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setFanTimeWork(final short level, final short time,
                               VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetFanTimeWork_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);             // 控制端类型区分 by zhaiyuanyi
//            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.FanLevel, level);
            msg.putOpt(MsgParams.FanTime, time);

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.timeLevel = level;
                    AbsFanPAD.this.timeWork = time;

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

            sendMsgBySerial(msg, new RCMsgCallback(callback) {
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

                    if (smartParams.TimingVentilationPeriod == 255)
                        smartParams.TimingVentilationPeriod = 3;
                    if (smartParams.WeeklyVentilationDate_Week == 255)
                        smartParams.WeeklyVentilationDate_Week = 1;
                    if (smartParams.WeeklyVentilationDate_Hour == 255)
                        smartParams.WeeklyVentilationDate_Hour = 12;
                    if (smartParams.WeeklyVentilationDate_Minute == 255)
                        smartParams.WeeklyVentilationDate_Minute = 30;

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

            sendMsgBySerial(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsFanPAD.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    // -------------------------------------------------------------------------------
    // protected
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
    }

}

