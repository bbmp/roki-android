package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

public class DishWasherMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        byte b;
        String str;
        switch (key) {
            case MsgKeys.setDishWasherPower:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.PowerMode);
                buf.put(b);
                break;
            case MsgKeys.setDishWasherChildLock:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.StoveLock);
                buf.put(b);
                break;
            case MsgKeys.setDishWasherWorkMode:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.DishWasherWorkMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.LowerLayerWasher);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.AutoVentilation);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.EnhancedDrySwitch);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.AppointmentSwitch);
                buf.put(b);
                int appointTime = msg.optInt(MsgParams.AppointmentTime);
                buf.put((byte) (appointTime & 0Xff));
                buf.put((byte) ((appointTime >> 8) & 0Xff));

                break;
            case MsgKeys.setDishWasherStatus:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                break;
            case MsgKeys.setDishWasherUserOperate:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                str = msg.optString(MsgParams.ArgumentNumber);
                buf.put(str.getBytes());
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                    if (msg.optInt(MsgParams.SaltFlushKey) == 1) {
                        b = (byte) msg.optInt(MsgParams.SaltFlushKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SaltFlushLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SaltFlushValue);
                        buf.put(b);

                    }
                    if (msg.optInt(MsgParams.RinseAgentPositionKey) == 2) {
                        b = (byte) msg.optInt(MsgParams.RinseAgentPositionKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.RinseAgentPositionLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.RinseAgentPositionValue);
                        buf.put(b);

                    }
                }

                break;


        }
    }


    public static void unmashaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        switch (key) {
            case MsgKeys.getDishWasherPower:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.getDishWasherChildLock:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.getDishWasherWorkMode:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.getDishWasherStatus:
                msg.putOpt(MsgParams.powerStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.StoveLock,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.DishWasherWorkMode,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.DishWasherRemainingWorkingTime,
                        MsgUtils.getInt(payload, offset++));
                offset++;
                msg.putOpt(MsgParams.LowerLayerWasher,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EnhancedDryStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AppointmentSwitchStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AutoVentilation,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AppointmentTime,
                        MsgUtils.getInt(payload, offset++));
                offset++;
                msg.putOpt(MsgParams.AppointmentRemainingTime,
                    MsgUtils.getInt(payload, offset++));
                offset++;
                msg.putOpt(MsgParams.RinseAgentPositionKey,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SaltFlushValue,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.DishWasherFanSwitch,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.DoorOpenState,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.LackRinseStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.LackSaltStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AbnormalAlarmStatus,
                        MsgUtils.getShort(payload[offset++]));
                short argument = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument);
                while (argument > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    switch (argument_key) {
                        case 1:
                            msg.putOpt(MsgParams.CurrentWaterTemperatureKey,
                                    argument_key);
                            msg.putOpt(MsgParams.CurrentWaterTemperatureLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.CurrentWaterTemperatureValue,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;
                            break;
                        case 2:
                            msg.putOpt(MsgParams.SetWorkTimeKey,
                                    argument_key);
                            msg.putOpt(MsgParams.SetWorkTimelength,
                                    MsgUtils.getShort(payload[offset++]));
                            //msg.putOpt(MsgParams.SetWorkTimeValue,
                            //        MsgUtils.getInt(payload, offset++));
                            //offset++;

                            msg.putOpt(MsgParams.SetWorkTimeValue,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;
                            break;
                    }
                    argument--;
                }


                break;
            case MsgKeys.getDishWasherUserOperate:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            case MsgKeys.getAlarmEventReport:
                msg.putOpt(MsgParams.DishWasherAlarm,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));


                break;
            case MsgKeys.getEventReport:
                short aShort = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.EventId,aShort);
                if (aShort==12) {
                    msg.putOpt(MsgParams.waterConsumption,
                            MsgUtils.getInt(payload, offset++));
                    offset++;

                    msg.putOpt(MsgParams.powerConsumption,
                            MsgUtils.getInt(payload, offset++));
                }else{
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getInt(payload, offset++));
                    offset++;
                    offset++;

                }

                msg.putOpt(MsgParams.UserId,
                        MsgUtils.getString(payload, offset++, 10));
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));

                break;
        }
    }
}
