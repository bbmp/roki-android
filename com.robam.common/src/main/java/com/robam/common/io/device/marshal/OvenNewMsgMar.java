package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * Created by Dell on 2018/7/16.
 */

public class OvenNewMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        byte b;
        String str;
        switch (key) {
            case MsgKeys.setOvenStatusControl_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.OvenStatus);
                buf.put(b);
                break;
            case MsgKeys.setOvenQuickHeat_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenAirBaking_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenToast_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenBottomHeat_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenUnfreeze_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenAirBarbecue_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenBarbecue_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenStrongBarbecue_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.setOvenSpitRotateLightControl_Req:
                //此指令待确定
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenRevolve);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.OvenLight);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                break;
            case MsgKeys.getOvenStatus_Req:
                break;
            //新增烤箱指令
            case MsgKeys.SetOven_RunMode_Req:

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);


                int ovenRecipeId = msg.optInt(MsgParams.OvenRecipeId);
                b = (byte) (ovenRecipeId & 0xFF);
                buf.put(b);
                b = (byte) ((ovenRecipeId >> 8) & 0xFF);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);

                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {

                    if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                        b = (byte) msg.optInt(MsgParams.SetTempDownKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTempDownLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTempDownValue);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.OrderTime_key) == 2) {
                        b = (byte) msg.optInt(MsgParams.OrderTime_key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.Rotatebarbecue) == 3) {
                        b = (byte) msg.optInt(MsgParams.Rotatebarbecue);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.RotatebarbecueLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.RotatebarbecueValue);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.SetTemp2Key) == 4) {
                        b = (byte) msg.optInt(MsgParams.SetTemp2Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTemp2Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTemp2Value);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.SetTime2Key) == 5) {
                        b = (byte) msg.optInt(MsgParams.SetTime2Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTime2Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTime2Value);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.SetTimeHKey) == 6) {
                        b = (byte) msg.optInt(MsgParams.SetTimeHKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTimeHLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTimeHValue);
                        buf.put(b);
                    }

                }
                break;

            case MsgKeys.SetSteamOven_Recipe_Req:
                //此指令并未使用过，即存在差异按照最新的028来
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.PreFlag);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenRecipeId);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenRecipeTotalStep);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                buf.put(b);
                break;

            case MsgKeys.Set_Oven_Auto_Mode_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.ovenAutoMode);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.OvenSetTime);
                buf.put(b);

                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);

                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {//可变长参数判断
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
            case MsgKeys.Set_Oven_Light_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenLight);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.Set_Oven_More_Cook://164
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.OvenPreFlag);
                buf.put(b);
                int ovenRecipe028Id = msg.optInt(MsgParams.OvenRecipeId);
                b = (byte) (ovenRecipe028Id & 0xFF);
                buf.put(b);
                b = (byte) ((ovenRecipe028Id >> 8) & 0xFF);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OvenRecipeStep);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {//可变长参数判断
                    if (msg.optInt(MsgParams.OvenStagekey) == 1) {
                        b = (byte) msg.optInt(MsgParams.OvenStagekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStageLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStageValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep1Modekey) == 2) {
                        b = (byte) msg.optInt(MsgParams.OvenStep1Modekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1ModeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1ModeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep1SetTempkey) == 3) {
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTempkey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTempLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTempValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep1SetTimekey) == 4) {
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTimekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTimeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep1SetTimeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep2Modekey) == 5) {
                        b = (byte) msg.optInt(MsgParams.OvenStep2Modekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2ModeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2ModeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep2SetTempkey) == 6) {
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTempkey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTempLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTempValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep2SetTimekey) == 7) {
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTimekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTimeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep2SetTimeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep3Modekey) == 8) {
                        b = (byte) msg.optInt(MsgParams.OvenStep3Modekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3ModeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3ModeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep3SetTempkey) == 9) {
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTempkey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTempLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTempValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.OvenStep2SetTimekey) == 10) {
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTimekey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTimeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OvenStep3SetTimeValue);
                        buf.put(b);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        switch (key) {
            case MsgKeys.setOvenStatusControl_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenQuickHeat_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenAirBaking_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenAirBarbecue_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenToast_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenBottomHeat_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenUnfreeze_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenStrongBarbecue_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenBarbecue_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setOvenSpitRotateLightControl_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            case MsgKeys.getOvenStatus_Rep:
                //烤箱工作状态
                msg.putOpt(MsgParams.OvenStatus,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱警告
                msg.putOpt(MsgParams.OvenAlarm,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱运行状态
                msg.putOpt(MsgParams.OvenRunP,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱工作当前温度
                msg.putOpt(MsgParams.OvenTemp,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱烤叉旋转
                msg.putOpt(MsgParams.OvenRevolve,
                        MsgUtils.getShort(payload[offset++]));
                //剩余时间
                msg.putOpt(MsgParams.OvenTime,
                        MsgUtils.getShort(payload, offset++));
                offset++;

                //烤箱灯光
                msg.putOpt(MsgParams.OvenLight,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱设置温度
                msg.putOpt(MsgParams.OvenSetTemp,
                        MsgUtils.getShort(payload[offset++]));
                //烤箱设置时间
                msg.putOpt(MsgParams.OvenSetTime,
                        MsgUtils.getShort(payload[offset++]));

                if ((offset + 1) == payload.length) {
                    break;
                }
                msg.putOpt(MsgParams.OrderTime_value_min,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.OrderTime_value_hour,
                        MsgUtils.getShort(payload[offset++]));
                //026
                //烤箱自动模式
                msg.putOpt(MsgParams.ovenAutoMode,
                        MsgUtils.getShort(payload[offset++]));
                //菜谱ID
                msg.putOpt(MsgParams.OvenRecipeId,
                        MsgUtils.getShort(payload, offset++));
                offset++;

                //菜谱步骤
                msg.putOpt(MsgParams.OvenRecipeStep,
                        MsgUtils.getShort(payload[offset++]));


                short argument = MsgUtils.getShort(payload[offset++]);
                //参数个数
                msg.putOpt(MsgParams.ArgumentNumber,
                        argument);

                //取可变参数值
                while (argument > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);

                    switch (argument_key) {
                        //设置下温度EXP
                        case 1:
                            msg.putOpt(MsgParams.SetTempDownKey,
                                    argument_key);
                            msg.putOpt(MsgParams.SetTempDownLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SetTempDownValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //下温度
                        case 2:
                            msg.putOpt(MsgParams.CurrentTempDownKey,
                                    argument_key);
                            msg.putOpt(MsgParams.CurrentTempDownLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.CurrentTempDownValue,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        //当前步骤
                        case 3:

                            msg.putOpt(MsgParams.CurrentStatusKey,
                                    argument_key);
                            msg.putOpt(MsgParams.CurrentStatusLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.CurrentStatusValue,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        //挡板插入状态
                        case 9:
                            msg.putOpt(MsgParams.PlatInsertStatuekey,
                                    argument_key);
                            msg.putOpt(MsgParams.PlatInsertStatueLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.PlatInsertStatueValue,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        //设置下层温度
                        case 5:
                            msg.putOpt(MsgParams.SetTemp2Key,
                                    argument_key);
                            msg.putOpt(MsgParams.SetTemp2Length,
                                    MsgUtils.getShort(payload[offset++]));
                            short aShort = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.SetTemp2Value, aShort);
                            break;
                        //设置下层时间
                        case 6:
                            msg.putOpt(MsgParams.SetTime2Key,
                                    argument_key);
                            msg.putOpt(MsgParams.SetTime2Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SetTime2Value,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 7:
                            msg.putOpt(MsgParams.TempKey,
                                    argument_key);
                            msg.putOpt(MsgParams.TempLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.TempValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 8:
                            msg.putOpt(MsgParams.LeftTime2Key,
                                    argument_key);
                            msg.putOpt(MsgParams.LeftTime2Length,
                                    MsgUtils.getShort(payload[offset++]));


                            int anInt = MsgUtils.getShort(payload, offset++);
                            msg.putOpt(MsgParams.LeftTime2Value,anInt);
                            offset++;

                            break;
                        case 10:
                            msg.put(MsgParams.status2Key,
                                    argument_key);
                            msg.put(MsgParams.status2Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.status2Values,
                                    MsgUtils.getShort(payload[offset++]));

                            break;
                        case 11:
                            msg.put(MsgParams.SetTimeHKey,
                                    argument_key);
                            msg.put(MsgParams.SetTimeHLength,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.SetTimeHValue,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        default:
                            break;
                    }
                    argument--;
                }
                break;

            case MsgKeys.OvenAlarm_Noti:
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                if ((offset + 1) == payload.length) {
                    break;
                }
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));

                break;
            //153
            case MsgKeys.Oven_Noti:
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.UserId,
                        MsgUtils.getString(payload, offset++, 10));
                offset += 10;

                if ((offset + 1) == payload.length) {
                    break;
                }
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                if (msg.optInt(MsgParams.SetTempDownKey) == 1) {
                    msg.putOpt(MsgParams.SetTempDownKey,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SetTempDownLength,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.SetTempDownValue,
                            MsgUtils.getShort(payload[offset++]));

                }
                break;

            //新增烤箱指令
            case MsgKeys.GetOven_RunMode_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.GetOven_Recipe_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.Get_Oven_Auto_Mode_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.Get_Oven_Light_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.Get_Oven_More_Cook:
                msg.putOpt(MsgParams.ArgumentNumber,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:
                break;
        }
    }
}
