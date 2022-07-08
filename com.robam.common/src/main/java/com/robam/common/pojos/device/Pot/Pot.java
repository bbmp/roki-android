package com.robam.common.pojos.device.Pot;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.PotAlarmEvent;
import com.robam.common.events.PotDotEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by as on 2017-04-06.
 */

public class Pot extends AbsDevice implements IPot, Serializable {
    protected short terminalType = TerminalType.getType();
    public float tempUp = 0;
    public short potStatus;
    public short mPotBurningWarnSwitch = 3;
    public byte mDrySwitch;
    public byte mFanPotSwitch;
    //电机模式 0,1,2,0xff(255故障)
    public short potESPMode;
    //无人锅绑定炉头
    public short Pot_BindHead;
    //无人锅锅盖状态  1: 取下 2:盖上
    public short Pot_LisState;
    //是否打点{Key:3,Len:1,Val=0不打点/1打 点
    public short Pot_IsDot;

    public Pot(SubDeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG)
                LogUtils.i("pot_polling", "温控锅pot onPolling  " + getID());
            Msg reqMsg = newReqMsg(MsgKeys.GetPotTemp_Req);
            reqMsg.putOpt(MsgParams.TerminalType, terminalType);
            reqMsg.setIsFan(true);
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long lasterReciverTemple, lasterReciverTemple12;

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            LogUtils.i("20190423", "key:" + key);
            if (Plat.DEBUG)
                 switch (key) {
                    case MsgKeys.ActiveTemp_Rep:
                        tempUp = (float) (Math.round((float) msg.optDouble(MsgParams.Pot_Temp) * 100)) / 100;
                        Pot_IsDot = (short) msg.optInt(MsgParams.Pot_IsDot);
                       if(Pot_IsDot>0){//打点
                           postEvent(new PotDotEvent());
                       }
                        long curTime = System.currentTimeMillis();
                        if (curTime - lasterReciverTemple < 800)
                            break;
                        lasterReciverTemple = curTime;
                        onStatusChanged();
                        break;
                    case MsgKeys.PotKey_Report:
                        short Pot_keybood = (short) msg.optInt(MsgParams.Pot_keybood);

                        break;

                    case MsgKeys.PotAlarm_Report:
                        short alarmId = (short) msg.optInt(MsgParams.AlarmId);
                        EventUtils.postEvent(new PotAlarmEvent(this, alarmId));
                        break;
                    case MsgKeys.SetPotTemp_Rep:
                        tempUp = (float) (Math.round((float) msg.optDouble(MsgParams.Pot_Temp) * 100)) / 100;
                        potStatus = (short) msg.optInt(MsgParams.Pot_status);
                        potESPMode = (short) msg.optInt(MsgParams.Pot_ESPMode);
                        Pot_BindHead = (short) msg.optInt(MsgParams.Pot_BindHead);
                        Pot_LisState  = (short) msg.optInt(MsgParams.Pot_LisState);
                        LogUtils.i("20190423", "tempUp:" + tempUp + " potStatus:" + potStatus);
                        onStatusChanged();
                        break;
                     case MsgKeys.POT_INTERACTION_Rep:
                      short rc = (short) msg.optInt(MsgParams.RC);
                         break;
                    default:
                        long curTime12 = System.currentTimeMillis();
                        if (curTime12 - lasterReciverTemple12 < 4000)
                            break;
                        lasterReciverTemple12 = curTime12;
//                    ToastUtils.showShort("温控锅上报异常指令：" + key);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged() {
        postEvent(new PotStatusChangedEvent(Pot.this));
    }

    /**
     * 设置智能状态
     * @param potBurningWarnSwitch
     * @param callback
     */
    public void setFanPotLink(short potBurningWarnSwitch, VoidCallback callback) {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.SetPotCom_Req);
            reqMsg.putOpt(MsgParams.UserId, getSrcUser());
            reqMsg.putOpt(MsgParams.potBurningWarnSwitch, potBurningWarnSwitch);
            sendMsg(reqMsg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190508", "144 resMsg:" + resMsg);
                }
            });
        } catch (JSONException e) {
            LogUtils.i("20190508", "144 e:" + e.toString());
            e.printStackTrace();
        }

    }

    public void getFanPotLink(final Callback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.SetPotSwitch_Req);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190508", "146 resMsg:" + resMsg);
                    mPotBurningWarnSwitch = (byte) resMsg.optInt(MsgParams.potBurningWarnSwitch);
                    byte[] args = new byte[8];
                    for (short i = 7; i > -1; i--) {
                        args[7 - i] = ((mPotBurningWarnSwitch & (1 << i)) == 0) ? (byte) 0 : 1;
                        mDrySwitch = args[7];
                        mFanPotSwitch = args[6];
                    }
//                    LogUtils.i("20190508", "mPotBurningWarnSwitch:" + mPotBurningWarnSwitch);
//                    LogUtils.i("20190508", "mDrySwitch:" + mDrySwitch);
//                    LogUtils.i("20190508", "mFanPotSwitch:" + mFanPotSwitch);
                    callback.onSuccess(mPotBurningWarnSwitch);
                    Helper.onSuccess(callback, null);
                }

            });
        } catch (Exception e) {
            LogUtils.i("20190508", "146 e:" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 设置智能状态
     * @param callback
     */
    public void setInteraction(List<Interaction> data, VoidCallback callback) {
//        Interaction interaction = new Interaction();
//        interaction.key = 1 ;
//        interaction.length = 3 ;
//        interaction.value = new  int[]{1 , 3, 1 } ;
        try {
            Msg reqMsg = newReqMsg(MsgKeys.POT_INTERACTION_Req);
            reqMsg.putOpt(MsgParams.UserId, getSrcUser());
            //放入需要放松的data数据
            try {
                String jsondata = JsonUtils.pojo2Json(data);
                reqMsg.putOpt(MsgParams.interactionData, jsondata);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sendMsg(reqMsg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190508", "144 resMsg:" + resMsg);
                }
            });
        } catch (JSONException e) {
            LogUtils.i("20190508", "144 e:" + e.toString());
            e.printStackTrace();
        }
    }
    public void setInteraction(List<Interaction> data,final Callback callback) {

        try {
            Msg reqMsg = newReqMsg(MsgKeys.POT_INTERACTION_Req);
            reqMsg.putOpt(MsgParams.UserId, getSrcUser());
            //放入需要放松的data数据
            try {
                String jsondata = JsonUtils.pojo2Json(data);
                reqMsg.putOpt(MsgParams.interactionData, jsondata);

                sendMsg(reqMsg, new RCMsgCallback(callback) {
                    @Override
                    protected void afterSuccess(Msg resMsg) {
                        Helper.onSuccess(callback, resMsg);

                    }

                    @Override
                    protected void afterFailure(Msg resMsg) {
                        super.afterFailure(resMsg);
//                        try {
//                            int rc = resMsg.getInt(com.legent.plat.io.device.msg.MsgParams.RC);
//                            if(rc==1){
//                                Helper.onFailure(callback,null);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            LogUtils.i("20190508", "146 e:" + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * 无人锅温控P档菜谱下发
     * @param callback
     */
    public void setPMenu(int Pot_P_Numb,List<Interaction> data, VoidCallback callback) {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.POT_P_MENU_Req);
            reqMsg.putOpt(MsgParams.UserId, getSrcUser());
            //P序号[1B]:1~5,表示P档菜谱序号
            reqMsg.putOpt(MsgParams.Pot_P_Number, Pot_P_Numb);
            //炉头ID[1B] 0:左 1:右   9b010默认右炉头
            reqMsg.putOpt(MsgParams.HeadId, 1);
            //放入需要放松的data数据
            try {
                String jsondata = JsonUtils.pojo2Json(data);
                reqMsg.putOpt(MsgParams.interactionData, jsondata);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sendMsg(reqMsg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20190508", "144 resMsg:" + resMsg);
                }
            });
        } catch (JSONException e) {
            LogUtils.i("20190508", "144 e:" + e.toString());
            e.printStackTrace();
        }
    }
    public static class Interaction implements Serializable{
        public int key ;
        public int length ;
        public int[]  value ;
    }
}
