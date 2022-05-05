package com.robam.common.pojos.device.Oven;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenLightResetEvent;
import com.robam.common.events.OvenRunModeResetEvent;
import com.robam.common.events.OvenSpitRotateResetEvent;
import com.robam.common.events.OvenSwitchControlResetEvent;
import com.robam.common.events.OvenTempResetEvent;
import com.robam.common.events.OvenTimeResetEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class Oven039 extends AbsOven implements IOven {

    public Oven039(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        setConnected(true);
        Log.i("Oven039:", "msg:" + msg);
        try {
            int key = msg.getID();
            Log.e("key", String.valueOf(key));
            switch (key) {
                case MsgKeys.Oven_Noti:
                    // TODO 处理事件
                    short eventId = (short) msg.optInt(MsgParams.EventId);
                    short eventParam = (short) msg.optInt(MsgParams.EventParam);
                    Log.e("eventId", String.valueOf(eventId));

                    switch (eventId) {
                        case Event_Oven_Switch_Control_Reset:
                            postEvent(new OvenSwitchControlResetEvent(Oven039.this, 1 == eventParam));
                            break;
                        case Event_Oven_Run_Mode_Reset:
                            postEvent(new OvenRunModeResetEvent(Oven039.this, eventParam));
                            break;
                        case Event_Oven_Spit_Rotate_Reset:
                            postEvent(new OvenSpitRotateResetEvent(Oven039.this, eventParam));
                            break;
                        case Event_Oven_Light_Reset:
                            postEvent(new OvenLightResetEvent(Oven039.this, eventParam));
                        case Event_Oven_Temp_Reset:
                            postEvent(new OvenTempResetEvent(Oven039.this, eventParam));
                            break;
                        case Event_Oven_Time_Reset:
                            postEvent(new OvenTimeResetEvent(Oven039.this, eventParam));
                            break;
                    }

                    break;
                case MsgKeys.OvenAlarm_Noti:
                    short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                    postEvent(new OvenAlarmEvent(Oven039.this, alarmId));
                case MsgKeys.getOvenStatus_Rep:
                    Oven039.this.status = (short) msg.optInt(MsgParams.OvenStatus);
                    Oven039.this.runP = (short) msg.optInt(MsgParams.OvenRunP);
                    Oven039.this.alarm = (short) msg.optInt(MsgParams.OvenAlarm);
                    Oven039.this.revolve = (short) msg.optInt(MsgParams.OvenRevolve);
                    Oven039.this.temp = (short) msg.optInt(MsgParams.OvenTemp);
                    Oven039.this.light = (short) msg.optInt(MsgParams.OvenLight);
                    Oven039.this.time = (short) msg.optInt(MsgParams.OvenTime);
                    Oven039.this.setTemp = (short) msg.optInt(MsgParams.OvenSetTemp);
                    Oven039.this.setTime = (short) msg.optInt(MsgParams.OvenSetTime);
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
    public String getOvenModel() {
        return null;
    }

    public void setOvenWorkTime(final short time, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTime, time);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Oven039.this.time = time;

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
            Msg msg = newReqMsg(MsgKeys.setOvenStatusControl_Req);//
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenTemp, temp);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Oven039.this.temp = temp;

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
                    Oven039.this.status = status;
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

            Log.i("201908051","terminalType："+String.valueOf(terminalType)+"  getSrcUser()："+getSrcUser()+"  setTemp："+String.valueOf(setTemp)+"  preflag："+String.valueOf(preflag)+"  setTime："+setTime);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);
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
                    Oven039.this.temp = temp;
                    Oven039.this.time = (short) (time * 60);

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
                    Oven039.this.temp = temp;
                    Oven039.this.time = (short) (time * 60);

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);
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
                    Oven039.this.revolve = revolve;
                    Oven039.this.light = light;
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
                    Oven039.this.status = status;

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
                    Oven039.this.status = (short) resMsg.optInt(MsgParams.OvenStatus);
                    Oven039.this.runP = (short) resMsg.optInt(MsgParams.OvenMode);
                    Oven039.this.alarm = (short) resMsg.optInt(MsgParams.OvenAlarm);
                    Oven039.this.temp = (short) resMsg.optInt(MsgParams.OvenTemp);
                    Oven039.this.time = (short) resMsg.optInt(MsgParams.OvenTime);
                    Oven039.this.light = (short) resMsg.optInt(MsgParams.OvenLight);
                    Oven039.this.revolve = (short) resMsg.optInt(MsgParams.OvenRevolve);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    Oven039.this.temp = setTemp;
                    Oven039.this.time = (short) (setTime * 60);
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
                    Oven039.this.status = status;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}