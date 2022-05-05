package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * Created by Dell on 2018/9/6.
 */

public class SteamMsgNewMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;

       if (msg!=null){//蒸箱226
            switch (key) {
                case MsgKeys.setSteamTime_Req://设置蒸汽炉工作时间
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.SteamTime);
                    buf.put(b);
                    if (msg.getTopic().contains("RS209")){
                        break;
                    }
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    break;
                case MsgKeys.setSteamTemp_Req://设置蒸汽炉工作温度
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.SteamTemp);
                    buf.put(b);
                    if (msg.getTopic().contains("RS209")){
                        break;
                    }
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
                    if (msg.getTopic().contains("RS209")){
                        break;
                    }
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
                    if (msg.getTopic().contains("RS209")){
                        break;
                    }
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
                    if (msg.getTopic().contains("RS209")){
                        break;
                    }
                    int recipeId = msg.optInt(MsgParams.OrderTime);
                    buf.put((byte) (recipeId & 0Xff));
                    buf.put((byte) ((recipeId >> 8) & 0Xff));
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
                case MsgKeys.SetLocalRecipeReq:
                    LogUtils.i("202011051518","111-------------");
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.setSteamAutoMode);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.SteamTimeSet);
                    buf.put(b);

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);

                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.OrderTime_key)==1) {
                            b = (byte) msg.optInt(MsgParams.OrderTime_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                            buf.put(b);
                        }
                        if (msg.optInt(MsgParams.SetTime_H_key)==2) {
                            b = (byte) msg.optInt(MsgParams.SetTime_H_key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTime_H_length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.SetTime_H_Value);
                            buf.put(b);



                        }
                    }
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        if (msg!=null) {//226
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
                    if ((offset+1)==payload.length){
                        break;
                    }
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
                            case 3:
                                msg.putOpt(MsgParams.SteamAutoRecipeModeKey,
                                        argument_key);
                                msg.putOpt(MsgParams.SteamAutoRecipeModeLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SteamAutoRecipeModeValue,
                                        MsgUtils.getShort(payload[offset++]));

                                break;
                            case 4:
                                msg.putOpt(MsgParams.DescaleModeStageKey,
                                        argument_key);
                                msg.putOpt(MsgParams.DescaleModeStageLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.DescaleModeStageValue,
                                        MsgUtils.getShort(payload[offset++]));

                                break;
                            case 5:
                                msg.putOpt(MsgParams.WeatherDescalingKey,
                                        argument_key);
                                msg.putOpt(MsgParams.WeatherDescalingLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.WeatherDescalingValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 6:
                                LogUtils.i("202011051343","argument_key:::"+argument_key);
                                LogUtils.i("202011051343"," MsgUtils.getShort(payload[offset++]):::"+ MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetTime2_key,
                                        argument_key);
                                msg.putOpt(MsgParams.SetTime2_length,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetTime2_value,
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
                    LogUtils.i("20180925","here is run :");
                    msg.putOpt(MsgParams.AlarmId,
                            MsgUtils.getShort(payload[offset++]));
                    if ((offset+1)==payload.length){
                        break;
                    }
                    msg.putOpt(MsgParams.ArgumentNumber,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SteamOven_Noti:
                    LogUtils.i("20180925","here is run :");
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset++, 10));
                    offset+=10;
                    LogUtils.i("20180925","offset:"+offset+"total::"+payload.length);
                    short argumentBao = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,
                            argumentBao);
                    if ((offset+1)==payload.length){
                        break;
                    }
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
                case MsgKeys.GetLocalRecipeRep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                default:
                    break;

            }
        }
    }
}
