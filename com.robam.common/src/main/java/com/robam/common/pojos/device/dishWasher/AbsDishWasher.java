package com.robam.common.pojos.device.dishWasher;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.events.DishWasherLackRinseResetEvent;
import com.robam.common.events.DishWasherLackSaltResetEvent;
import com.robam.common.events.DishWasherOpenDoorResetEvent;
import com.robam.common.events.DishWasherSwitchControlResetEvent;
import com.robam.common.events.DishWasherWorkCompleteResetEvent;
import com.robam.common.events.DishWasherWorkModeResetEvent;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import java.io.Serializable;

public class AbsDishWasher extends AbsDeviceHub implements IDishWasher, Serializable {
    public short powerStatus;
    public short StoveLock;
    public short DishWasherWorkMode;
    public short DishWasherRemainingWorkingTime;
    public short LowerLayerWasher;
    public short EnhancedDryStatus;
    public short AppointmentSwitchStatus;
    public short AutoVentilation;
    public short AppointmentTime;
    public short AppointmentRemainingTime;
    public short RinseAgentPositionValue;
    public short SaltFlushValue;
    public short DishWasherFanSwitch;
    public short DoorOpenState;
    public short LackRinseStatus;
    public short LackSaltStatus;
    public short AbnormalAlarmStatus;
    public short ArgumentNumber;
    public short CurrentWaterTemperatureValue;
    public short SetWorkTimeValue;
    public short powerConsumption;
    public short waterConsumption;
    public short alarmId;
    protected short terminalType = TerminalType.getType();
    //事件
    static final public short Event_DishWasher_Power_Control_Reset = 10;//电源状态控制事件
    static final public short Event_DishWasher_Work_Mode_Reset = 11;//工作模式调整事件
    static final public short Event_DishWasher_Work_Complete_Reset = 12;//工作完成事件
    static final public short Event_DishWasher_Lack_Rinse_Reset = 13;//却漂洗剂事件
    static final public short Event_DishWasher_Lack_Salt_Reset = 14;//缺盐事件
    static final public short Event_DishWasher_Open_Door_Reset = 15;//开门事件



    public AbsDishWasher(DeviceInfo devInfo) {
        super(devInfo);
    }


    @Override
    public void onPolling() {
        try {
            if (Plat.DEBUG) {
                Msg reqMsg = newReqMsg(MsgKeys.setDishWasherStatus);
                reqMsg.putOpt(MsgParams.TerminalType, terminalType);
                sendMsg(reqMsg, null);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
        }
        if (Plat.DEBUG) {

        }
        postEvent(new DisherWasherStatusChangeEvent(AbsDishWasher.this));
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            switch (key) {
                case MsgKeys.getDishWasherStatus:
                    AbsDishWasher.this.powerStatus = (short) msg.optInt(MsgParams.powerStatus);
                    AbsDishWasher.this.StoveLock = (short) msg.optInt(MsgParams.StoveLock);
                    AbsDishWasher.this.DishWasherWorkMode = (short) msg.optInt(MsgParams.DishWasherWorkMode);
                    AbsDishWasher.this.DishWasherRemainingWorkingTime = (short) msg.optInt(MsgParams.DishWasherRemainingWorkingTime);

                    AbsDishWasher.this.LowerLayerWasher = (short) msg.optInt(MsgParams.LowerLayerWasher);//下层洗
                    AbsDishWasher.this.EnhancedDryStatus = (short) msg.optInt(MsgParams.EnhancedDryStatus);
                    AbsDishWasher.this.AppointmentSwitchStatus = (short) msg.optInt(MsgParams.AppointmentSwitchStatus);
                    AbsDishWasher.this.AutoVentilation = (short) msg.optInt(MsgParams.AutoVentilation);
                    AbsDishWasher.this.AppointmentTime = (short) msg.optInt(MsgParams.AppointmentTime);
                    AbsDishWasher.this.AppointmentRemainingTime = (short) msg.optInt(MsgParams.AppointmentRemainingTime);
                    AbsDishWasher.this.RinseAgentPositionValue = (short) msg.optInt(MsgParams.RinseAgentPositionValue);
                    AbsDishWasher.this.SaltFlushValue = (short) msg.optInt(MsgParams.SaltFlushValue);
                    AbsDishWasher.this.DishWasherFanSwitch = (short) msg.optInt(MsgParams.DishWasherFanSwitch);
                    AbsDishWasher.this.DoorOpenState = (short) msg.optInt(MsgParams.DoorOpenState);

                    AbsDishWasher.this.LackRinseStatus = (short) msg.optInt(MsgParams.LackRinseStatus);

                    AbsDishWasher.this.LackSaltStatus = (short) msg.optInt(MsgParams.LackSaltStatus);

                    AbsDishWasher.this.AbnormalAlarmStatus = (short) msg.optInt(MsgParams.AbnormalAlarmStatus);

                    AbsDishWasher.this.ArgumentNumber = (short) msg.optInt(MsgParams.ArgumentNumber);

                    AbsDishWasher.this.CurrentWaterTemperatureValue = (short) msg.optInt(MsgParams.CurrentWaterTemperatureValue);

                    AbsDishWasher.this.SetWorkTimeValue = (short) msg.optInt(MsgParams.SetWorkTimeValue);

                    postEvent(new DisherWasherStatusChangeEvent(AbsDishWasher.this));


                    break;
                case MsgKeys.getAlarmEventReport:
                    AbsDishWasher.this.alarmId = (short) msg.optInt(MsgParams.DishWasherAlarm);

                    postEvent(new DishWasherAlarmEvent(AbsDishWasher.this, alarmId));
                    break;

                case MsgKeys.getEventReport:
                    short eventId = (short) msg.optInt(MsgParams.EventId);

                    switch (eventId) {
                        case Event_DishWasher_Power_Control_Reset:

                            postEvent(new DishWasherSwitchControlResetEvent(AbsDishWasher.this, (short) msg.optInt(MsgParams.EventParam)));
                            break;
                        case Event_DishWasher_Work_Mode_Reset:
                            postEvent(new DishWasherWorkModeResetEvent(AbsDishWasher.this, (short) msg.optInt(MsgParams.EventParam)));
                            break;
                        case Event_DishWasher_Work_Complete_Reset:
                            AbsDishWasher.this.powerConsumption = (short) msg.optInt(MsgParams.powerConsumption);
                            AbsDishWasher.this.waterConsumption = (short) msg.optInt(MsgParams.waterConsumption);
                            postEvent(new DishWasherWorkCompleteResetEvent(AbsDishWasher.this, powerConsumption,waterConsumption));
                            break;
                        case Event_DishWasher_Lack_Rinse_Reset:
                            postEvent(new DishWasherLackRinseResetEvent(AbsDishWasher.this, (short) msg.optInt(MsgParams.EventParam)));
                            break;
                        case Event_DishWasher_Lack_Salt_Reset:
                            postEvent(new DishWasherLackSaltResetEvent(AbsDishWasher.this, (short) msg.optInt(MsgParams.EventParam)));
                            break;
                        case Event_DishWasher_Open_Door_Reset:
                            postEvent(new DishWasherOpenDoorResetEvent(AbsDishWasher.this, (short) msg.optInt(MsgParams.EventParam)));
                            break;

                    }
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
    public void setDishWasherStatusControl(final short status, VoidCallback voidCallback) {
            try{
                Msg msg = newReqMsg(MsgKeys.setDishWasherPower);
                msg.putOpt(MsgParams.UserId, getSrcUser());
                msg.putOpt(MsgParams.PowerMode, status);
                sendMsg(msg,new RCMsgCallbackWithVoid(voidCallback){
                    @Override
                    protected void afterSuccess(Msg resMsg) {
                        AbsDishWasher.this.powerStatus=status;
                        onStatusChanged();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
    }



    @Override
    public void setDishWasherLock(final short status, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDishWasherChildLock);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.StoveLock, status);
            sendMsg(msg,new RCMsgCallbackWithVoid(callback){
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsDishWasher.this.StoveLock=status;
                    onStatusChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //设置洗碗机工作模式
    public void setDishWasherWorkMode(final short workMode, final short bottomWasherSwitch,
                                      final short autoVentilation,final short enhancedDrySwitch,
                                      short appointmentSwitch,short appointmentTime,
                                      VoidCallback voidCallback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDishWasherWorkMode);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.DishWasherWorkMode, workMode);
            msg.putOpt(MsgParams.LowerLayerWasher, bottomWasherSwitch);
            msg.putOpt(MsgParams.AutoVentilation, autoVentilation);
            msg.putOpt(MsgParams.EnhancedDrySwitch, enhancedDrySwitch);
            msg.putOpt(MsgParams.AppointmentSwitch, appointmentSwitch);
            msg.putOpt(MsgParams.AppointmentTime, appointmentTime);
            sendMsg(msg,new RCMsgCallbackWithVoid(voidCallback){
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsDishWasher.this.DishWasherWorkMode=workMode;
                    AbsDishWasher.this.LowerLayerWasher=bottomWasherSwitch;
                    AbsDishWasher.this.AutoVentilation=autoVentilation;
                    onStatusChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void userSet(final short arg, final short flushSalt, final short rinse, VoidCallback voidCallback) {
            try {
                Msg msg = newReqMsg(MsgKeys.setDishWasherUserOperate);
                msg.putOpt(MsgParams.TerminalType, terminalType);
                msg.putOpt(MsgParams.ArgumentNumber, arg);
                msg.putOpt(MsgParams.SaltFlushValue, flushSalt);
                msg.putOpt(MsgParams.RinseAgentPositionValue, rinse);
                sendMsg(msg,new RCMsgCallbackWithVoid(voidCallback){
                    @Override
                    protected void afterSuccess(Msg resMsg) {
                        AbsDishWasher.this.SaltFlushValue=flushSalt;
                        AbsDishWasher.this.RinseAgentPositionValue=rinse;
                        onStatusChanged();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
