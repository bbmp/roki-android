package com.robam.common.pojos.device.Steamoven;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamPowerEvent;
import com.robam.common.events.SteamTempResetEvent;
import com.robam.common.events.SteamTimeResetEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

/**
 * Created by Administrator on 2016/11/9.
 */

public class Steam209 extends AbsSteamoven implements ISteamoven {
    public Steam209(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        setConnected(true);
        try {
            int key = msg.getID();
            Log.e("key", String.valueOf(key));
            switch (key) {
                case MsgKeys.SteamOven_Noti:
                    // TODO 处理事件
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);

                    switch (eventId) {
                        case Event_Steam_Power:
                            postEvent(new SteamPowerEvent(Steam209.this, 1 == eventParam));
                            break;
                        case Event_Steam_Time_Reset:
                            postEvent(new SteamTimeResetEvent(Steam209.this, eventParam));
                            break;
                        case Event_Steam_Temp_Reset:
                            postEvent(new SteamTempResetEvent(Steam209.this, eventParam));
                            break;
                       /* case Event_Steam_Cookbook_Reset:
                            postEvent(new SteamCleanResetEvent(Steam209.this, eventParam));
                            break;*/
                    }

                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    postEvent(new SteamAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    Steam209.this.oldstatus = status;
                    Steam209.this.status = (short) msg.optInt(MsgParams.SteamStatus);
                    Steam209.this.mode = (short) msg.optInt(MsgParams.SteamMode);
                    Steam209.this.alarm = (short) msg.optInt(MsgParams.SteamAlarm);
                    Steam209.this.doorState = (short) msg.optInt(MsgParams.SteamDoorState);
                    Steam209.this.temp = (short) msg.optInt(MsgParams.SteamTemp);
                    Steam209.this.time = (short) msg.optInt(MsgParams.SteamTime);
                    Steam209.this.tempSet = (short) msg.optInt(MsgParams.SteamTempSet);
                    Steam209.this.timeSet = (short) msg.optInt(MsgParams.SteamTimeSet);
                    if (Plat.DEBUG)
                        LogUtils.i("20170308", "209GetSteamOvenStatus_Rep:" + msg.getDeviceGuid() + "  ID: " + getID());
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
    // ISteamOven
    // -------------------------------------------------------------------------------


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
                    Steam209.this.time = (short) (((short) 60) * time);
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
                    Steam209.this.temp = temp;
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
            Msg msg = newReqMsg(MsgKeys.setSteamMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamTemp, temp);
            msg.putOpt(MsgParams.SteamTime, time);
            msg.putOpt(MsgParams.SteamMode, cookbook);
            msg.putOpt(MsgParams.PreFlag, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steam209.this.time = (short) (((short) 60) * time);
                    Steam209.this.temp = temp;
                    Steam209.this.mode = cookbook;

                    onStatusChanged();
                }
            });
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
                    Steam209.this.time = (short) (((short) 60) * time);
                    Steam209.this.temp = temp;

                    onStatusChanged();
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
                    Steam209.this.oldstatus = status;
                    Steam209.this.status = (short) resMsg.optInt(MsgParams.SteamStatus);
                    Steam209.this.mode = (short) resMsg.optInt(MsgParams.SteamMode);
                    Steam209.this.alarm = (short) resMsg.optInt(MsgParams.SteamAlarm);
                    Steam209.this.temp = (short) resMsg.optInt(MsgParams.SteamTemp);
                    Steam209.this.time = (short) resMsg.optInt(MsgParams.SteamTime);
                    Steam209.this.doorState = (short) resMsg.optInt(MsgParams.SteamDoorState);

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
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteamStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steam209.this.oldstatus = Steam209.this.status;
                    Steam209.this.status = status;
//                    AbsSteamoven.this.tempSet = (short) resMsg.optInt(MsgParams.SteamTempSet);
//                    AbsSteamoven.this.timeSet = (short) resMsg.optInt(MsgParams.SteamTimeSet);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
