package com.robam.common.io.device.marshal;

import com.google.common.collect.Lists;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class SteamMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        if (msg!= null && (msg.getTopic().contains("RS209"))) {
            switch (key) {
                case MsgKeys.setSteamTime_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamMode);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);

                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    break;
            }
        }else if (msg!=null && (msg.getTopic().contains("RS226")||msg.getTopic().contains("RS275")||msg.getTopic().contains("HS906"))){//蒸箱226
            switch (key) {
                case MsgKeys.setSteamTime_Req://设置蒸汽炉工作时间
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req://设置蒸汽炉工作温度
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req://蒸汽炉烹饪模式
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SetMeum);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    int ovenRecipeId = msg.optInt(MsgParams.SteamRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.OrderTime_key) == 1) {
                            b = (byte) msg.optInt(MsgParams.OrderTime_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    int steamRecipeId = msg.optInt(MsgParams.SteamRecipeId);
                    b = (byte) (steamRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((steamRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.OrderTime_key) == 1) {
                            b = (byte) msg.optInt(MsgParams.OrderTime_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.OrderTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamOven_Recipe_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SteamRecipeKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.SteamRecipeUniqueKey) == 2) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.SteamRecipeConcreteKey) == 3) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTemp);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTime);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.SetSteamLightReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamWaterTankPOPReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
            }
        }else if (IRokiFamily.RS228.equals(msg.getDeviceGuid().getGuid().substring(0,5))){
            switch (key) {
                case MsgKeys.setSteamTime_Req://设置蒸汽炉工作时间
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req://设置蒸汽炉工作温度
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamMode_Req://蒸汽炉烹饪模式
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SetMeum);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    int ovenRecipeId = msg.optInt(MsgParams.SteamRecipeId);
                    b = (byte) (ovenRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.OrderTime_key) == 1) {
                            b = (byte) msg.optInt(MsgParams.OrderTime_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.setSteamProMode_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.PreFlag);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeId);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.SteamRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.OrderTime_key) == 1) {
                            b = (byte) msg.optInt(MsgParams.OrderTime_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.GetSteamOvenStatus_Req:
                    break;
                case MsgKeys.setSteamStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamStatus);
                    buf.put(b);
                    buf.put(ByteUtils.getBytes(msg.optInt(MsgParams.OrderTime), ByteOrder.LITTLE_ENDIAN));
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamOven_Recipe_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.SteamRecipeKey) == 1) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeValue);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.SteamRecipeUniqueKey) == 2) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeUniqueValue);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.SteamRecipeConcreteKey) == 3) {
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteKey);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamRecipeConcreteLength);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTemp);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SteamTime);
                            buf.put(b);
                        }
                    }
                    break;
                case MsgKeys.SetSteamLightReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamLight);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.SetSteamWaterTankPOPReq:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    break;
                default:
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        if (msg != null && msg!= null && (msg.getTopic().contains("RS209"))) {
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));

                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));

                    break;
                case MsgKeys.setSteamStatus_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    break;
            }
        } else if (msg!=null && (msg.getTopic().contains("RS226")||msg.getTopic().contains("RS275")||msg.getTopic().contains("HS906"))
                ){//226
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                  /*  List<String> list1 = Lists.newArrayList();
                        for (int i = 0; i < payload.length; i++) {
                            String hex = Integer.toHexString(payload[i] & 0xff);
                            if (hex.length() == 1) {
                                hex = "0" + hex;
                            }
                            list1.add(hex);
                        }
                       LogUtils.i("steam_st", list1.toString());*/
                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamRecipeStep,
                            MsgUtils.getShort(payload[offset++]));

                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SteamOvenWaterBoxKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenWaterBoxLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenWaterBoxValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.setSteamStatus_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:

                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:

                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    short argumentBao = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argumentBao);
                    while (argumentBao > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.setSteamModeSendKey,
                                        argument_key);
                                msg.putOpt(MsgParams.setSteamModeSendLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.setSteamModeSendValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.setSteamTemptureSendKey,
                                        argument_key);
                                msg.putOpt(MsgParams.setSteamTemptureSendLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.setSteamTemptureSendValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 3:
                                msg.putOpt(MsgParams.setSteamTimeSendKey,
                                        argument_key);
                                msg.putOpt(MsgParams.setSteamTimeSendLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.setSteamTimeSendValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argumentBao--;
                    }
                    break;

                case MsgKeys.GetSteamRecipeRep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

            }
        }else if (IRokiFamily.RS228.equals((msg.getDeviceGuid().getGuid()).substring(0,5))){
            LogUtils.i("20171121","key:"+key);
            switch (key) {
                case MsgKeys.setSteamTime_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamTemp_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamMode_Rep:
                    List<String> list = Lists.newArrayList();
                    for (int i = 0; i < payload.length; i++) {
                        String hex = Integer.toHexString(payload[i] & 0xff);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        list.add(hex);
                    }
                    LogUtils.i("20171121steam_st", "list::"+list.toString());
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setSteamProMode_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.GetSteamOvenStatus_Rep:
                    msg.putOpt(MsgParams.SteamLock,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamAlarm,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTemp,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTempSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamTimeSet,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_hour,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.OrderTime_value_min,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SteamRecipeId,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.SteamRecipeStep,
                            MsgUtils.getShort(payload[offset++]));

                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argument);
                    //取可变参数值
                    while (argument > 0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.SteamOvenWaterBoxKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenWaterBoxLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenWaterBoxValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.SteamOvenCurrentStageKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamOvenCurrentStageLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamOvenCurrentStageValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                case MsgKeys.setSteamStatus_Rep:
                    List<String> list1 = Lists.newArrayList();
                    for (int i = 0; i < payload.length; i++) {
                        String hex = Integer.toHexString(payload[i] & 0xff);
                        if (hex.length() == 1) {
                            hex = "0" + hex;
                        }
                        list1.add(hex);
                    }
                    LogUtils.i("20171121steam_st", "list111::"+list1.toString());
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOvenAlarm_Noti:
//                        List<String> list1 = Lists.newArrayList();
//                        for (int i = 0; i < payload.length; i++) {
//                            String hex = Integer.toHexString(payload[i] & 0xff);
//                            if (hex.length() == 1) {
//                                hex = "0" + hex;
//                            }
//                            list1.add(hex);
//                        }
//                        LogUtils.i("steam_st", list1.toString());
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
                       /* List<String> list1 = Lists.newArrayList();
                        for (int i = 0; i < payload.length; i++) {
                            String hex = Integer.toHexString(payload[i] & 0xff);
                            if (hex.length() == 1) {
                                hex = "0" + hex;
                            }
                            list1.add(hex);
                        }
                        LogUtils.i("8888888", list1.toString());*/
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.GetSteamRecipeRep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                default:
                    break;

            }
        }
    }
}
