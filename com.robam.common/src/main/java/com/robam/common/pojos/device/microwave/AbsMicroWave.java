package com.robam.common.pojos.device.microwave;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveHeadModeEvent;
import com.robam.common.events.MicroWavePowerEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.events.MicroWaveSwitchEvent;
import com.robam.common.events.MicroWaveTimeEvent;
import com.robam.common.events.MicroWaveWeightEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.UserAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by WZTCM on 2015/12/17.
 */
public class AbsMicroWave extends AbsDeviceHub implements iMicroWave,Serializable {

    public static final short Event_MicroWave_Switch = 10;   //微波炉运行状态调整事件
    public static final short Event_MicroWave_HeatMode = 11; //微波炉加热模式调整事件（参考状态查询）
    public static final short Event_MicroWave_Weight = 12;   //微波炉重量调整事件
    public static final short Event_MicroWave_Power = 13;    //微波炉功率调整事件
    public static final short Event_MicroWave_Time = 14;     //微波率加热时间调整事件

    public short state; // 开关状态
    public short light; // 照明灯状态
    public short mode;  // 模式
    public short power; // 功率
    public short weight;// 重量
    public short time;  // 工作时间
    public short doorState;// 门阀状态 0 门关 1门开
    public short step;//阶段
    public short setTime;//设置时间
    public short error;
    public short preState = -1;

    protected short terminalType = TerminalType.getType();


    public AbsMicroWave(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            Log.i("mic-onPolling", "onPolling");
                Msg reqMsg = newReqMsg(MsgKeys.getMicroWaveStatus_Req);
                reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
                sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("MicroWave onStatusChanged. isConnected:%s level:%s", isConnected, state));
        }
        Log.i("mic--StatusChanged", "state:" + this.state + " light:" + this.light
                + " mode:" + this.mode + " power:" + this.power + " weight:" + this.weight + " time:" + this.time +
                " doorState:" + this.doorState + " step:" + this.step + " setTime:" + this.setTime + " error:" + this.error);
        postEvent(new MicroWaveStatusChangedEvent(AbsMicroWave.this));

    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        Log.i("mic-onReceivedMsg", "msg-->key:" + msg.getID());
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.getMicroWaveAlarm_Rep:
                    short alarm = (short) msg.optInt(MsgParams.RC);
                    if (alarm == 1) {
                        postEvent(new MicroWaveAlarmEvent(AbsMicroWave.this, alarm));
                    }
                case MsgKeys.MicroWave_Noti:
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);

                    switch (eventId) {
                        case Event_MicroWave_Switch:
                            postEvent(new MicroWaveSwitchEvent(AbsMicroWave.this, eventParam));
                            break;
                        case Event_MicroWave_HeatMode:
                            postEvent(new MicroWaveHeadModeEvent(AbsMicroWave.this, eventParam));
                            break;
                        case Event_MicroWave_Weight:
                            postEvent(new MicroWaveWeightEvent(AbsMicroWave.this, eventParam));
                            break;
                        case Event_MicroWave_Power:
                            postEvent(new MicroWavePowerEvent(AbsMicroWave.this, eventParam));
                            break;
                        case Event_MicroWave_Time:
                            postEvent(new MicroWaveTimeEvent(AbsMicroWave.this, eventParam));
                            break;
                        default:
                            break;
                    }

                    break;

                case MsgKeys.getMicroWaveStatus_Rep://状态查询回应
                    this.preState = state;
                    this.state = (short) msg.optInt(MsgParams.MicroWaveStatus);
                    this.light = (short) msg.optInt(MsgParams.MicroWaveLight);
                    this.mode = (short) msg.optInt(MsgParams.MicroWaveMode);
                    this.power = (short) msg.optInt(MsgParams.MicroWavePower);
                    this.weight = (short) msg.optInt(MsgParams.MicroWaveWeight);
                    this.time = (short) msg.optInt(MsgParams.MicroWaveTime);
                    this.doorState = (short) msg.optInt(MsgParams.MicroWaveDoorState);
                    this.step = (short) msg.optInt(MsgParams.MicroWaveStepState);
                    this.setTime = (short) msg.optInt(MsgParams.MicroWaveSettime);
                    this.error = (short) msg.optInt(MsgParams.MicroWaveError);
                    onStatusChanged();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //----------------------------------- iMicroWave -----------------------------------------


    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }

    @Override
    public String getMicroWaveMode() {
        return null;
    }

    @Override
    public void setMicroWaveState(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveStatus, status);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Log.i("end", "afterSuccess");
                    AbsMicroWave.this.state = status;

                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long userid=Plat.accountService.getCurrentUserId();
    @Override
    public void setMicroWaveKindsAndHeatCold(final short mode, final short weight, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveKindsAndHeatCold_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveMode, mode);
            msg.putOpt(MsgParams.MicroWaveWeight, weight);
            msg.putOpt(MsgParams.MicroWaveRestartNow, 0);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    try {
                        String strTime = getCurrentTime();
                        UserAction user = new UserAction(userid, "非专业模式", strTime, mode, (short) 0, weight, (short) 0);
                        user.save2db();
                    } catch (Exception e) {
                    }
                    AbsMicroWave.this.mode = mode;
                    AbsMicroWave.this.weight = weight;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置微波炉专业模式140
     */
    @Override
    public void setMicroWaveProModeHeat(final short mode, final short time, final short power, VoidCallback callback) {
        setMicroWaveProModeHeat(mode, time, power, 0, callback);
    }

    public void setMicroWaveProModeHeat(final short mode, final short time, final short power, int preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveProModeHeat_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveMode, mode);
            msg.putOpt(MsgParams.MicroWaveTime, time);
            msg.putOpt(MsgParams.MicroWavePower, power);
            msg.putOpt(MsgParams.MicroWaveRestartNow, preflag);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    try {
                        String strTime = getCurrentTime();
                        UserAction user = new UserAction(userid, "专业模式", strTime, mode, power, weight, time);
                        user.save2db();
                    } catch (Exception e) {

                    }
                    AbsMicroWave.this.state = MicroWaveStatus.Setting;
                    AbsMicroWave.this.mode = mode;
                    AbsMicroWave.this.time = time;
                    AbsMicroWave.this.power = power;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去味模式
     */
    public void setMicroWaveCleanAir(final short mode, final short time, final short power, int preflag, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveClean_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveMode, mode);
            msg.putOpt(MsgParams.MicroWaveTime, time);
            msg.putOpt(MsgParams.MicroWavePower, power);
            msg.putOpt(MsgParams.MicroWaveRestartNow, preflag);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    String strTime = getCurrentTime();
                    UserAction user = new UserAction(userid, "去味", strTime, mode, power, weight, time);
                    user.save2db();
                    AbsMicroWave.this.state = MicroWaveStatus.Setting;
                    AbsMicroWave.this.mode = mode;
                    AbsMicroWave.this.time = time;
                    AbsMicroWave.this.power = power;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            if (Plat.DEBUG) {
                LogUtils.i("20170502", "e:" + e.getMessage());
            }
            e.printStackTrace();
        }
    }





    /**
     * 设置微波炉灯光
     */
    @Override
    public void setMicroWaveLight(final short state, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveStatus_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveLight, state);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsMicroWave.this.light = state;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置联动模式
     */
    @Override
    public void setMicroWaveLinkdCook(final List<LinkageStepInfo> list, VoidCallback callback) {
        if (list.size() == 0) return;
        try {
            Msg msg = newReqMsg(MsgKeys.setMicroWaveLinkedCook_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.MicroWaveStepState, list.size());
            for (LinkageStepInfo info : list) {
                switch (info.getLink_step()) {
                    case 1:
                        msg.putOpt(MsgParams.MicroWaveLinkdMode1, info.getLink_model());
                        msg.putOpt(MsgParams.MicroWaveLinkPower1, info.getLink_fire());
                        msg.putOpt(MsgParams.MicroWaveLinkTime1, info.getLink_time());
                        break;
                    case 2:
                        msg.putOpt(MsgParams.MicroWaveLinkdMode2, info.getLink_model());
                        msg.putOpt(MsgParams.MicroWaveLinkPower2, info.getLink_fire());
                        msg.putOpt(MsgParams.MicroWaveLinkTime2, info.getLink_time());
                        break;
                    case 3:
                        msg.putOpt(MsgParams.MicroWaveLinkdMode3, info.getLink_model());
                        msg.putOpt(MsgParams.MicroWaveLinkPower3, info.getLink_fire());
                        msg.putOpt(MsgParams.MicroWaveLinkTime3, info.getLink_time());
                        break;
                }
                if (info.getLink_step() == list.size()) {
                    if (list.size() == 3) break;
                    if (list.size() == 1) {
                        msg.putOpt(MsgParams.MicroWaveLinkdMode2, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkPower2, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkTime2, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkdMode3, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkPower3, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkTime3, 0);
                    } else if (list.size() == 2) {
                        msg.putOpt(MsgParams.MicroWaveLinkdMode3, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkPower3, 0);
                        msg.putOpt(MsgParams.MicroWaveLinkTime3, 0);
                    }
                }
            }
            msg.putOpt(MsgParams.MicroWaveRestartNow, 0);
            Log.i("mic--link",
                    "model1:" + msg.optInt(MsgParams.MicroWaveLinkdMode1)
                            + " fire1:" + msg.optInt(MsgParams.MicroWaveLinkPower1)
                            + " time1:" + msg.optInt(MsgParams.MicroWaveLinkTime1) +
                            " model2:" + msg.optInt(MsgParams.MicroWaveLinkdMode2)
                            + " fire2:" + msg.optInt(MsgParams.MicroWaveLinkPower2)
                            + " time2:" + msg.optInt(MsgParams.MicroWaveLinkTime2) +
                            " model3:" + msg.optInt(MsgParams.MicroWaveLinkdMode3)
                            + " fire3:" + msg.optInt(MsgParams.MicroWaveLinkPower3)
                            + " time3:" + msg.optInt(MsgParams.MicroWaveLinkTime3));
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    AbsMicroWave.this.mode = list.get(0).getLink_model();
                    AbsMicroWave.this.time = list.get(0).getLink_time();
                    AbsMicroWave.this.power = list.get(0).getLink_fire();
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日,HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
