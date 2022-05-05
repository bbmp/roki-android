package com.robam.common.io.device.marshal;

import android.util.Log;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * 新协议
 */
public class NewTreatyMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            //todo 原蒸烤一体机内容，需要替换
//            case MsgKeys.setSteameOvenStatusControl_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteameOvenStatus);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenPowerOnStatus);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenOrderTime_hour);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                break;
//            case MsgKeys.getSteameOvenStatus_Req:
//
//
//                break;
//            case MsgKeys.setSteameOvenBasicMode_Req:
//
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteameOvenMode);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenSetTemp);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenSetTime);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
//                buf.put(b);
//                int recipeId = msg.optInt(MsgParams.SteameOvenRecipeId);
//                buf.put((byte) (recipeId & 0Xff));
//                buf.put((byte) ((recipeId >> 8) & 0Xff));
//                b = (byte) msg.optInt(MsgParams.SteameOvenRecipesteps);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenSetDownTemp);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                    if (msg.optInt(MsgParams.time_H_key) == 1) {
//                        b = (byte) msg.optInt(MsgParams.time_H_key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.time_H_length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.time_H_Value);
//                        buf.put(b);
//                    }
//                }
//                break;
//            case MsgKeys.setTheRecipe_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                    if (msg.optInt(MsgParams.SteameOvenRecipeId) == 1) {
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeId);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeLength);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeValue);
//                        buf.put(b);
//                    }
//                    if (msg.optInt(MsgParams.SteameOvenRecipeTotalsteps) == 2) {
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalsteps);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsLength);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsValue);
//                        buf.put(b);
//                    }
//
//                    if (msg.optInt(MsgParams.SteameOvenRecipesteps) == 3) {
//                        b = (byte) msg.optInt(MsgParams.SteameOvenMode);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTime);
//                        buf.put(b);
//                    }
//
//
//                }
//                break;
//            case MsgKeys.setSteameOvenFloodlight_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteameOvenLight);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                break;
//
//            case MsgKeys.setSteameOvenWater_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteameOvenWaterStatus);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                break;
//            case MsgKeys.setSteameOvensteam_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                break;
//            case MsgKeys.setSteameOvenMultistageCooking_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                int stepCount = msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
//                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                    if (msg.optInt(MsgParams.OrderTime_key) == 1) {
//                        b = (byte) msg.optInt(MsgParams.OrderTime_key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
//                        buf.put(b);
//                    }
//
//                    if (msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key) == 2) {
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
//                        buf.put(b);
//                    }
//                    for (int i = 1; i <= stepCount; i++) {
//
//
//                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Length + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenMode + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp + i);
//                        int time = msg.optInt(MsgParams.SteameOvenTime + i);
//                        buf.put(b);
//                        if (time > 255) {
//                            buf.put((byte) 0);
//                            short low = (short) (time & 0Xff);
//                            short high = (short) ((time >> 8) & 0Xff);
//                            buf.put((byte) low);
//                            buf.put((byte) high);
//                        } else {
//                            b = (byte) msg.optInt(MsgParams.SteameOvenTime + i);
//                            buf.put(b);
//                        }
//                    }
//
//                }
//
//                break;
//            case MsgKeys.setSteameOvenAutoRecipeMode610_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                int stepSize = msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
//                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                    if (msg.optInt(MsgParams.OrderTime_key) == 1) {
//                        b = (byte) msg.optInt(MsgParams.OrderTime_key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
//                        buf.put(b);
//                    }
//
//                    if (msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key) == 2) {
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
//                        buf.put(b);
//                    }
//                    for (int i = 1; i <= stepSize; i++) {
//                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Length + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenMode + i);
//                        buf.put(b);
//
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp + i);
//                        buf.put(b);
//                        buf.put((byte) 0);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTime + i);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp2 + i);
//                        buf.put((byte) 0);
//                        buf.put(b);
//                        buf.put((byte) 0);
//                        b = (byte) msg.optInt(MsgParams.SteameOvenTime2 + i);
//                        buf.put(b);
//                        buf.put((byte) 0);
//                        Log.i("bbbbb", buf.toString());
//                    }
//
//                }
//
//                break;
//            //新增 2020年2月15日 16:00:19
//            case MsgKeys.setSteameOvenAutoRecipeMode_Req:
//                str = msg.optString(MsgParams.UserId);
//                buf.put(str.getBytes());
//                b = (byte) msg.optInt(MsgParams.SteamOvenAutoRecipeMode);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.SteameOvenSetTime);
//                buf.put(b);
//                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
//                buf.put(b);
//                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
//                    if (msg.optInt(MsgParams.OrderTime_key) == 1) {
//                        b = (byte) msg.optInt(MsgParams.OrderTime_key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
//                        buf.put(b);
//                    }
//                    if (msg.optInt(MsgParams.SetTime_H_key) == 2) {
//                        b = (byte) msg.optInt(MsgParams.SetTime_H_key);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SetTime_H_length);
//                        buf.put(b);
//                        b = (byte) msg.optInt(MsgParams.SetTime_H);
//                        buf.put(b);
//                    }
//                }
//
//                break;
//            default:
//                break;
        }
    }
    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        //todo 原蒸烤一体机内容，新协议需替换
        switch (key) {
            case MsgKeys.setSteameOvenStatusControl_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.getSteameOvenStatus_Rep://一体机状态查询回应

                msg.putOpt(MsgParams.SteameOvenStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenPowerOnStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenWorknStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenAlarm,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenMode,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenLeftTime,
                        MsgUtils.getShort(payload, offset++));
                offset++ ;
                msg.putOpt(MsgParams.SteameOvenLight,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenWaterStatus,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.SteameOvenSetTemp,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.SteameOvenSetTime,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.SteameOvenOrderTime_min,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenOrderTime_hour,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.SteameOvenRecipeId,
                        MsgUtils.getShort(payload[offset++]));
                offset++;

                msg.putOpt(MsgParams.SteameOvenRecipesteps,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.SteameOvenSetDownTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenDownTemp,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSteam,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenPreFlag,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.SteameOvenModelType,//菜谱种类
                        MsgUtils.getShort(payload[offset++]));

                short argument = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument);

                while (argument > 0) {
                    short argumentKey = MsgUtils.getShort(payload[offset++]);
                    switch (argumentKey) {
                        case 3:
                            msg.putOpt(MsgParams.CpStepKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.CpStepLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.CpStepValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 4:
                            msg.putOpt(MsgParams.SteamKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.SteamLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteamValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 5:
                            msg.putOpt(MsgParams.MultiStepCookingStepsKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.MultiStepCookingStepsLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.MultiStepCookingStepsValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 6:
                            msg.putOpt(MsgParams.SteamOvenAutoRecipeMode,
                                    argumentKey);
                            msg.putOpt(MsgParams.SteamOvenAutoRecipeModeLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.AutoRecipeModeValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 7:
                            msg.putOpt(MsgParams.MultiStepCurrentStepsKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.MultiStepCurrentStepsLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.MultiStepCurrentStepsValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 8:
                            msg.putOpt(MsgParams.SteameOvenPreFlagKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.SteameOvenPreFlagLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenPreFlagValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 9:
                            msg.putOpt(MsgParams.weatherDescalingKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.weatherDescalingLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.weatherDescalingValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 10:
                            msg.putOpt(MsgParams.doorStatusKey,
                                    argumentKey);
                            msg.putOpt(MsgParams.doorStatusLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.doorStatusValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 11:
                            msg.putOpt(MsgParams.time_H_key,
                                    argumentKey);
                            msg.putOpt(MsgParams.time_H_length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.time_H_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 12:
                            offset++ ;
                            msg.putOpt(MsgParams.SteameOvenLeftMin,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenLeftHours,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                    }
                    argument--;
                }

                break;
            case MsgKeys.SteameOvenAlarm_Noti:
                msg.putOpt(MsgParams.SteameOvenAlarm,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SteameOven_Noti://工作事件上报
                short eventId = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.EventId, eventId);
                msg.putOpt(MsgParams.UserId, MsgUtils.getString(payload, offset, 10));
                offset += 10;
                short arg = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, arg);
                while (arg > 0) {
                    short arg_key = MsgUtils.getShort(payload[offset++]);
                    switch (arg_key) {
                        //设置的基本模式
                        case 1:
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_Key,
                                    arg_key);
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.setSteameOvenBasicMode_value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //设置的温度
                        case 2:
                            msg.putOpt(MsgParams.SteameOvenSetTemp,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetTemp_Length,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetTemp_Value,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            break;
                        //设置的时间
                        case 3:
                            msg.putOpt(MsgParams.SteameOvenSetTime,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetTime_Length,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetTime_Value,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            break;
                        //设置的下温度
                        case 4:
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp_Lenght,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            msg.putOpt(MsgParams.SteameOvenSetDownTemp_Vaue,
                                    MsgUtils.getShort(payload[offset++]));
//                            offset++;
                            break;
                        //自动模式
                        case 5:
                            msg.putOpt(MsgParams.SteameOvenCpMode,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenCpMode_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenCpMode_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //烤叉旋转
                        case 6:
                            msg.putOpt(MsgParams.SteameOvenRevolve,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenRevolve_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenRevolve_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //水箱更改
                        case 7:
                            msg.putOpt(MsgParams.SteameOvenWaterChanges,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenWaterChanges_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenWaterChanges_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //照明灯开关
                        case 8:
                            msg.putOpt(MsgParams.SteameOvenLight,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenLight_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenLight_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //工作完成参数
                        case 9:

                            msg.putOpt(MsgParams.SteameOvenWorkComplete,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenWorkComplete_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            short s = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.SteameOvenWorkComplete_Value,
                                    s);
                            break;
                        //加蒸汽
                        case 10:
                            msg.putOpt(MsgParams.SteameOvenSteam,
                                    arg_key);
                            msg.putOpt(MsgParams.SteameOvenSteam_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteameOvenSteam_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //开关事件参数
                        case 11:
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl,
                                    arg_key);
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl_Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.setSteameOvenSwitchControl_Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //新增一体机自动菜谱上报
                        case 12:
                            msg.putOpt(MsgParams.SteamOvenAutoRecipeMode,
                                    arg_key);
                            msg.putOpt(MsgParams.SteamOvenAutoRecipeModeLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SteamOvenAutoRecipeModeValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                    }
                    arg--;
                }

                break;
            case MsgKeys.setSteameOvenBasicMode_Rep://一体机基本模式回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setTheRecipe_Rep://一体机菜谱设置回
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenFloodlight_Rep://一体机照明灯回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenWater_Rep://水箱回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvensteam_Rep://一体机加蒸汽回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenMultistageCooking_Rep://一体机多段烹饪回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenAutoRecipeMode610_Rep://一体机610多段烹饪回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setSteameOvenAutoRecipeMode_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:

                break;
        }
    }

}
