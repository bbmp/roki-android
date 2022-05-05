package com.robam.common.pojos.device.Sterilizer;


import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SterFinishEvent;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.io.Serializable;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public class Steri826 extends AbsSterilizer implements ISteri826, Serializable {


    public short setSteriTem;
    public short oldstatus;
    public short isChildLock;
    public short doorLock;
    public short AlarmStautus;
    public short steriReminderTime;
    public short steriSecurityLock;
    public short SteriDrying;
    public short argumentNumber;
    public short SteriCleanTime;
    public short SteriDisinfectTime, work_left_time_l, work_left_time_h;
    public short temp, hum, germ, ozone;
    short weeklySteri_week;
    private static int times = 0;

    public SteriSmart826Params steriSmart826Params = new SteriSmart826Params();


    public Steri826(DeviceInfo devInfo) {
        super(devInfo);
    }


    @Override
    public void onPolling() {
        super.onPolling();

        try {

            Msg reqMsg = newReqMsg(MsgKeys.GetSteriStatus_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsg(reqMsg, null);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
        postEvent(new SteriStatusChangedEvent(Steri826.this));

    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.SteriAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    LogUtils.i("20191204666", "alarmId::" + alarmId);
                    postEvent(new SteriAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteriStatus_Rep:
                    oldstatus = status;
                    Steri826.this.status = (short) msg.optInt(MsgParams.SteriStatus);
                    Steri826.this.isChildLock = (short) msg.optInt(MsgParams.SteriLock);
                    Steri826.this.work_left_time_l = (short) msg.optInt(MsgParams.SteriWorkLeftTimeL);
                    Steri826.this.doorLock = (short) msg.optInt(MsgParams.SteriDoorLock);
                    Steri826.this.AlarmStautus = (short) msg.optInt(MsgParams.SteriAlarmStatus);
                    Steri826.this.temp = (short) msg.optInt(MsgParams.SteriParaTem);
                    Steri826.this.hum = (short) msg.optInt(MsgParams.SteriParaHum);
                    Steri826.this.germ = (short) msg.optInt(MsgParams.SteriParaGerm);
                    Steri826.this.ozone = (short) msg.optInt(MsgParams.SteriParaOzone);
                    Steri826.this.argumentNumber = (short) msg.optInt(MsgParams.ArgumentNumber);
                    if (argumentNumber > 0) {
                        // 预约剩余时间
                        Steri826.this.steriReminderTime = (short) msg.optInt(MsgParams.SteriReminderTime);
                        //停止工作时是否进入安全锁
                        Steri826.this.steriSecurityLock = (short) msg.optInt(MsgParams.SteriSecurityLock);
                        LogUtils.i("201911256", "steriReminderTime:::" + steriReminderTime);
                    }
                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriPVConfig_Rep:
                    break;
                case MsgKeys.SteriEvent_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    postEvent(new SterFinishEvent(Steri826.this, eventId, eventParam));
                    break;
                default:


                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteriPower(final short status, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setSteriPower(final short status, short powerTime, short argumentNumber,
                              VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);
            msg.putOpt(MsgParams.SteriTime, powerTime);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            /*if (argumentNumber > 0) {
                if (setSteriTem != 0){
                    msg.putOpt(MsgParams.Key,(short)1);
                    msg.putOpt(MsgParams.Length,(short)1);
                    msg.putOpt(MsgParams.warmDishTempValue, setSteriTem);
                }
            }*/
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setSteriPower2(final short status, short powerTime,
                               VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);
            msg.putOpt(MsgParams.SteriTime, powerTime);
            /*if (argumentNumber > 0) {
                if (setSteriTem != 0){
                    msg.putOpt(MsgParams.Key,(short)1);
                    msg.putOpt(MsgParams.Length,(short)1);
                    msg.putOpt(MsgParams.warmDishTempValue, setSteriTem);
                }
            }*/
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //855的预约消毒、烘干、母婴
    public void setSteriPower2(short mode, short min, short argumentNumber, short setSteriTem, short orderTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, mode);
            msg.putOpt(MsgParams.SteriTime, min);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                if (setSteriTem != 0) {
                    msg.putOpt(MsgParams.Key, (short) 1);
                    msg.putOpt(MsgParams.Length, (short) 1);
                    msg.putOpt(MsgParams.warmDishTempValue, setSteriTem);
                }

                if (orderTime != 0) {
                    msg.putOpt(MsgParams.Key, (short) 2);
                    msg.putOpt(MsgParams.Length, (short) 2);
                    msg.putOpt(MsgParams.SteriReserveTime, orderTime);
                }
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void setSteriLock(final short isChildLock, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriLock_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriLock, isChildLock);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.isChildLock = isChildLock;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriDrying(final short SteriDrying, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriDrying_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriDryingTime, SteriDrying);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.work_left_time_l = SteriDrying;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteriClean(final short SteriCleanTime, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriClean_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriCleanTime, SteriCleanTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.SteriCleanTime = SteriCleanTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //消毒（请求）
    @Override
    public void setSteriDisinfect(final short SteriDisinfectTime, VoidCallback voidCallback) {
        LogUtils.i("20171122","SteriDisinfectTime:"+SteriDisinfectTime);
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriDisinfect_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriDisinfectTime, SteriDisinfectTime);

            LogUtils.i("20171122","msg:"+msg.getID());
            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20171122","resMsg:"+resMsg.getID());
                    Steri826.this.SteriDisinfectTime = SteriDisinfectTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


   /* public void getSteriPVConfig826(final Callback<SteriSmart826Params> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallback(callback) {
                protected void afterSuccess(Msg resMsg) {
                    steriSmart826Params.IsInternalDays = resMsg.optInt(MsgParams.SteriSwitchDisinfect) == 1 ? true : false;
                    steriSmart826Params.InternalDays = (short) resMsg.optInt(MsgParams.SteriInternalDisinfect);
                    steriSmart826Params.IsWeekSteri = (boolean) resMsg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    steriSmart826Params.WeeklySteri_week = (short) resMsg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    steriSmart826Params.PVCTime = (short) resMsg.optInt(MsgParams.SteriPVDisinfectTime);
                    //onStatusChanged();
                    if (steriSmart826Params.InternalDays == 255)
                        steriSmart826Params.InternalDays = 3;
                    if (weeklySteri_week == 255)
                        weeklySteri_week = 3;
                    if (steriSmart826Params.PVCTime == 255)
                        steriSmart826Params.PVCTime = 20;
                    Helper.onSuccess(callback, steriSmart826Params);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


   /* public void setSteriPVConfig826(final SteriSmart826Params steriSmart826Params, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriSwitchDisinfect, steriSmart826Params.IsInternalDays);
            msg.putOpt(MsgParams.SteriInternalDisinfect, steriSmart826Params.InternalDays);
            msg.putOpt(MsgParams.SteriSwitchWeekDisinfect, steriSmart826Params.IsWeekSteri);
            msg.putOpt(MsgParams.SteriWeekInternalDisinfect, steriSmart826Params.WeeklySteri_week);
            msg.putOpt(MsgParams.SteriPVDisinfectTime, steriSmart826Params.PVCTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri826.this.steriSmart826Params = steriSmart826Params;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/






}
