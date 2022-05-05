package com.robam.common.pojos.device.Sterilizer;

import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.io.Serializable;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public class Steri829 extends AbsSterilizer implements ISteri829,Serializable{


    public short setSteriTem;
    public short oldstatus;
    public short isChildLock;
    public boolean isDoorLock;
    public short AlarmStautus;
    public short SteriReserveTime;
    public short SteriDrying;
    public short SteriCleanTime;
    public short SteriDisinfectTime, work_left_time_l, work_left_time_h;
    public short temp, hum, germ, ozone;
    short weeklySteri_week;
    private static int times = 0;
    public SteriSmartParams steriSmartParams = new SteriSmartParams();

    public Steri829(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getSterilizerModel() {
        return IRokiFamily.RR829;
    }


    @Override
    public void onPolling() {
        super.onPolling();
        LogUtils.i("2018","onPolling");
        times++;
        if (times % 3 != 0 ) {
            try {
                Msg reqMsg = newReqMsg(MsgKeys.GetSteriStatus_Req);
                reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
                sendMsg(reqMsg, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                times = 0;
                Msg reqMsg = newReqMsg(MsgKeys.GetSteriParam_Req);
                reqMsg.put(MsgParams.TerminalType, terminalType);   // 控制端类型区分
                sendMsg(reqMsg, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
        postEvent(new SteriStatusChangedEvent(Steri829.this));
        LogUtils.i("20171231"," status:"+ status+ " isChildLock:"+isChildLock+" AlarmStautus:"+AlarmStautus
                + " work_left_time_l:"+ work_left_time_l +" temp:"+ temp+" hum:"+ hum+" germ:"+germ+" ozone:"+ozone);
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            LogUtils.i("20181029","key:"+key);
            switch (key) {
                case MsgKeys.SteriAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    LogUtils.i("20190304","alarmId:"+alarmId);
                    postEvent(new SteriAlarmEvent(this, alarmId));
                    break;
                case MsgKeys.GetSteriStatus_Rep:
                    oldstatus = status;
                    Steri829.this.status = (short) msg.optInt(MsgParams.SteriStatus);
                    Steri829.this.isChildLock = (short) msg.optInt(MsgParams.SteriLock);
                    Steri829.this.work_left_time_l = (short) msg.optInt(MsgParams.SteriWorkLeftTimeL);
                    Steri829.this.work_left_time_h = (short) msg.optInt(MsgParams.SteriWorkLeftTimeH);
                    Steri829.this.AlarmStautus = (short) msg.optInt(MsgParams.SteriAlarmStatus);

                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriParam_Rep:
                    Steri829.this.temp = (short) msg.optInt(MsgParams.SteriParaTem);
                    Steri829.this.hum = (short) msg.optInt(MsgParams.SteriParaHum);
                    Steri829.this.germ = (short) msg.optInt(MsgParams.SteriParaGerm);
                    Steri829.this.ozone = (short) msg.optInt(MsgParams.SteriParaOzone);
                    onStatusChanged();
                    break;
                case MsgKeys.GetSteriPVConfig_Rep:
                    break;
                case MsgKeys.SteriEvent_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    LogUtils.i("20181029","eventId::"+eventId+" eventParam::"+eventParam);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //状态查询（请求）
    @Override
    public void getSteriStatus(VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriStatus_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.status = (short) resMsg.optInt(MsgParams.SteriStatus);
                    Steri829.this.isChildLock = (short) resMsg.optInt(MsgParams.SteriLock);
                    Steri829.this.work_left_time_l = (short) resMsg.optInt(MsgParams.SteriWorkLeftTimeL);
                    Steri829.this.work_left_time_h = (short) resMsg.optInt(MsgParams.SteriWorkLeftTimeH);
                    Steri829.this.AlarmStautus = (short) resMsg.optInt(MsgParams.SteriAlarmStatus);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开关
    @Override
    public void setSteriPower(final short status, final VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPowerOnOff_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //预约时间
    @Override
    public void SetSteriReserveTime(final short SteriReserveTime, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriReserveTime_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriReserveTime, SteriReserveTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.SteriReserveTime = SteriReserveTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //烘干
    @Override
    public void setSteriDrying(final short SteriDrying, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriDrying_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriDryingTime, SteriDrying);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                   Steri829.this.work_left_time_l = SteriDrying;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //保洁
    @Override
    public void setSteriClean(final short SteriCleanTime, VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriClean_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriCleanTime, SteriCleanTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.SteriCleanTime = SteriCleanTime;
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
                    Steri829.this.SteriDisinfectTime = SteriDisinfectTime;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 查询消毒柜温度／湿度／细菌数（请求）
    @Override
    public void querySteriParm(VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriParam_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallbackWithVoid(voidCallback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.temp = (short) resMsg.optInt(MsgParams.SteriParaTem);
                    Steri829.this.hum = (short) resMsg.optInt(MsgParams.SteriParaHum);
                    Steri829.this.germ = (short) resMsg.optInt(MsgParams.SteriParaGerm);
                    Steri829.this.ozone = (short) resMsg.optInt(MsgParams.SteriParaOzone);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* @Override
    public void getSteriPVConfig(final Callback<SteriSmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallback(callback) {
                protected void afterSuccess(Msg resMsg) {
                    steriSmartParams.IsInternalDays = resMsg.optInt(MsgParams.SteriSwitchDisinfect) == 1 ? true : false;
                    steriSmartParams.InternalDays = (short) resMsg.optInt(MsgParams.SteriInternalDisinfect);
                    steriSmartParams.IsWeekSteri = (boolean) resMsg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    steriSmartParams.WeeklySteri_week = (short) resMsg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    steriSmartParams.PVCTime = (short) resMsg.optInt(MsgParams.SteriPVDisinfectTime);
                    //onStatusChanged();
                    if (steriSmartParams.InternalDays == 255)
                        steriSmartParams.InternalDays = 3;
                    if (steriSmartParams.WeeklySteri_week == 255)
                        steriSmartParams.WeeklySteri_week = 3;
                    if (steriSmartParams.PVCTime == 255)
                        steriSmartParams.PVCTime = 20;

                    Helper.onSuccess(callback, steriSmartParams);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/



   /* @Override
    public void setSteriPVConfig(final SteriSmartParams steriSmartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriSwitchDisinfect, steriSmartParams.IsInternalDays);
            msg.putOpt(MsgParams.SteriInternalDisinfect, steriSmartParams.InternalDays);
            msg.putOpt(MsgParams.SteriSwitchWeekDisinfect, steriSmartParams.IsWeekSteri);
            msg.putOpt(MsgParams.SteriWeekInternalDisinfect, steriSmartParams.WeeklySteri_week);
            msg.putOpt(MsgParams.SteriPVDisinfectTime, steriSmartParams.PVCTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Steri829.this.steriSmartParams = steriSmartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
