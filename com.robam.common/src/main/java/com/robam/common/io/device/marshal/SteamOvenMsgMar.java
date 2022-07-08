package com.robam.common.io.device.marshal;


import android.util.Log;

import com.legent.io.msgs.collections.BytesMsg;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class SteamOvenMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        Log.e("结果",key+"----");
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.getDeviceAttribute_Req:
                buf.put((byte) 0);
                break;

            case MsgKeys.setDeviceAttribute_Req:
                int type = msg.optInt(MsgParamsNew.type);
                switch (type) {
                    //专业模式 单一 设置
                    case 0:
                        byte number = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(number);
                        //一体机电源控制
                        byte powerCtrlKey = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey);
                        byte powerCtrlLength = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength);
                        byte powerCtrlKeyValue = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue);
                        //一体机工作控制
                        byte SteameOvenStatus_Key = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key);
                        byte SteameOvenStatus_Length = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length);
                        byte SteameOvenStatus = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus);
                        //预约时间
                        byte OrderTime_key = (byte) msg.optInt(MsgParamsNew.setOrderMinutesKey);
                        buf.put(OrderTime_key);
                        byte OrderTime_length = (byte) msg.optInt(MsgParamsNew.setOrderMinutesLength);
                        buf.put(OrderTime_length);
                        byte OrderTime = (byte) msg.optInt(MsgParamsNew.setOrderMinutes01);
                        buf.put(OrderTime);
                        if (OrderTime_length>1){
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes02));
                        }
                        if (OrderTime_length>2){
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes03));
                            buf.put((byte)0);
                        }

                        //总段数
                        byte steameOvenTotalNumberOfSegments_Key = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
                        buf.put(steameOvenTotalNumberOfSegments_Key);
                        byte steameOvenTotalNumberOfSegments_Length = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
                        buf.put(steameOvenTotalNumberOfSegments_Length);
                        byte steameOvenTotalNumberOfSegments_Value = (byte) msg.optInt(MsgParamsNew.sectionNumber);
                        buf.put(steameOvenTotalNumberOfSegments_Value);

                        //模式
                        byte SteameOvenMode_Key = (byte) msg.optInt(MsgParamsNew.modeKey);
                        buf.put(SteameOvenMode_Key);
                        byte SteameOvenMode_Length = (byte) msg.optInt(MsgParamsNew.modeLength);
                        buf.put(SteameOvenMode_Length);
                        byte SteameOvenMode = (byte) msg.optInt(MsgParamsNew.mode);
                        buf.put(SteameOvenMode);
                        //上温度
                        byte SteameOvenSetTemp_Key = (byte) msg.optInt(MsgParamsNew.setUpTempKey);
                        buf.put(SteameOvenSetTemp_Key);
                        byte SteameOvenSetTemp_Length = (byte) msg.optInt(MsgParamsNew.setUpTempLength);
                        buf.put(SteameOvenSetTemp_Length);
                        byte SteameOvenSetTemp = (byte) msg.optInt(MsgParamsNew.setUpTemp);
                        buf.put(SteameOvenSetTemp);

                        if (msg.has(MsgParamsNew.setDownTempKey)){
                            byte SteameOvenSetDownTemp_Key = (byte) msg.optInt(MsgParamsNew.setDownTempKey);
                            buf.put(SteameOvenSetDownTemp_Key);
                            byte SteameOvenSetDownTemp_Length = (byte) msg.optInt(MsgParamsNew.setDownTempLength);
                            buf.put(SteameOvenSetDownTemp_Length);
                            byte SteameOvenSetDownTemp = (byte) msg.optInt(MsgParamsNew.setDownTemp);
                            buf.put(SteameOvenSetDownTemp);
                        }
                        //时间
                        byte SteameOvenSetTime_Key = (byte) msg.optInt(MsgParamsNew.setTimeKey);
                        buf.put(SteameOvenSetTime_Key);
                        byte SteameOvenSetTime_Length = (byte) msg.optInt(MsgParamsNew.setTimeLength);
                        buf.put(SteameOvenSetTime_Length);
                        byte SteameOvenSetTime = (byte) msg.optInt(MsgParamsNew.setTime0b);
                        buf.put(SteameOvenSetTime);
                        if (SteameOvenSetTime_Length>1)
                            buf.put((byte) msg.optInt(MsgParamsNew.setTime1b));

                        //蒸汽量
                        if ( msg.has(MsgParamsNew.steam)) {
                            byte steamKey = (byte) msg.optInt(MsgParamsNew.steamKey);
                            buf.put(steamKey);
                            byte steamLength = (byte) msg.optInt(MsgParamsNew.steamLength);
                            buf.put(steamLength);
                            byte steam = (byte) msg.optInt(MsgParamsNew.steam);
                            buf.put(steam);
                        }
                        break;
                    //单属性设置
                    case 1:
//                           byte categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
//                           buf.put(categoryCode);
                        byte argumentNumber = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber);
                        //一体机工作控制
                        byte workCtrlKey = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(workCtrlKey);
                        byte workCtrlLength = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(workCtrlLength);
                        byte workCtrl = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(workCtrl);
                        break;
                    //多段模式设置
                    case 2:
//                           byte numberOfCategory2 = (byte) msg.optInt(MsgParams.categoryCode);
//                           buf.put(numberOfCategory2);
                        byte number2 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(number2);
                        //一体机电源控制
                        byte powerCtrlKey2 = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey2);
                        byte powerCtrlLength2 = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength2);
                        byte powerCtrlKeyValue2 = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue2);
                        //一体机工作控制
                        byte SteameOvenStatus_Key2 = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key2);
                        byte SteameOvenStatus_Length2 = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length2);
                        byte SteameOvenStatus2 = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus2);

                        //总段数
                        byte steameOvenTotalNumberOfSegments_Key2 = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
                        buf.put(steameOvenTotalNumberOfSegments_Key2);
                        byte steameOvenTotalNumberOfSegments_Length2 = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
                        buf.put(steameOvenTotalNumberOfSegments_Length2);
                        int sectionNumberMulti = msg.optInt(MsgParamsNew.sectionNumber);

                        byte steameOvenTotalNumberOfSegments_Value2 = (byte) sectionNumberMulti == 1 ?  (byte) 0 :(byte) sectionNumberMulti;
                        buf.put(steameOvenTotalNumberOfSegments_Value2);

                        if (sectionNumberMulti > 0) {
                            for (int i = 0; i < sectionNumberMulti; i++) {
                                //模式
                                byte modeKey = (byte) msg.optInt(MsgParamsNew.modeKey + i);
                                buf.put(modeKey);
                                byte modeLength = (byte) msg.optInt(MsgParamsNew.modeLength + i);
                                buf.put(modeLength);
                                byte mode = (byte) msg.optInt(MsgParamsNew.mode + i);
                                buf.put(mode);
                                //上温度
                                byte setUpTempKey = (byte) msg.optInt(MsgParamsNew.setUpTempKey + i);
                                buf.put(setUpTempKey);
                                byte setUpTempLength = (byte) msg.optInt(MsgParamsNew.setUpTempLength + i);
                                buf.put(setUpTempLength);
                                byte setUpTemp = (byte) msg.optInt(MsgParamsNew.setUpTemp + i);
                                buf.put(setUpTemp);

                                //下温度
                                 byte SteameOvenSetDownTemp_Key = (byte) msg.optInt(MsgParamsNew.setDownTempKey+ i);
                                 buf.put(SteameOvenSetDownTemp_Key);
                                 byte SteameOvenSetDownTemp_Length = (byte) msg.optInt(MsgParamsNew.setDownTempLength+ i);
                                 buf.put(SteameOvenSetDownTemp_Length);
                                 byte SteameOvenSetDownTemp = (byte) msg.optInt(MsgParamsNew.setDownTemp+ i);
                                 buf.put(SteameOvenSetDownTemp);


                                //时间
                                byte setTimeKey = (byte) msg.optInt(MsgParamsNew.setTimeKey + i);
                                buf.put(setTimeKey);
                                byte setTimeLength = (byte) msg.optInt(MsgParamsNew.setTimeLength + i);
                                buf.put(setTimeLength);
                                byte setTime = (byte) msg.optInt(MsgParamsNew.setTime0b + i);
                                buf.put(setTime);
                                if (setTimeLength>1){
                                    buf.put((byte) msg.optInt(MsgParamsNew.setTime1b + i));
                                }
                                //蒸汽量

                                byte steamKey2 = (byte) msg.optInt(MsgParamsNew.steamKey + i);
                                buf.put(steamKey2);
                                byte steamLength2 = (byte) msg.optInt(MsgParamsNew.steamLength + i);
                                buf.put(steamLength2);
                                byte steam2 = (byte) msg.optInt(MsgParamsNew.steam + i);
                                buf.put(steam2);
                                Log.e("结果蒸汽",steam2+"----");


                            }
                        }

                        break;
                    case 3:
//                           byte numberOfCategory3 = (byte) msg.optInt(MsgParams.categoryCode);
//                           buf.put(numberOfCategory3);
                        byte number3 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(number3);
                        //一体机电源控制
                        byte powerCtrlKey3 = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey3);
                        byte powerCtrlLength3 = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength3);
                        byte powerCtrlKeyValue3 = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue3);
                        //一体机工作控制
                        byte SteameOvenStatus_Key3 = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key3);
                        byte SteameOvenStatus_Length3 = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length3);
                        byte SteameOvenStatus3 = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus3);

                        byte recipeIdKey = (byte) msg.optInt(MsgParamsNew.recipeIdKey);
                        buf.put(recipeIdKey);
                        byte recipeIdLength = (byte) msg.optInt(MsgParamsNew.recipeIdLength);
                        buf.put(recipeIdLength);
                        byte recipeId = (byte) msg.optInt(MsgParamsNew.recipeId);
                        buf.put(recipeId);
                        if (recipeIdLength>1){
                            buf.put((byte) msg.optInt(MsgParamsNew.recipeId01));
                        }

                        byte recipeSetMinutesKey = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesKey);
                        buf.put(recipeSetMinutesKey);
                        byte recipeSetMinutesLength = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesLength);
                        buf.put(recipeSetMinutesLength);
                        byte recipeSetMinutes = (byte) msg.optInt(MsgParamsNew.recipeSetMinutes);
                        buf.put(recipeSetMinutes);
                        if (msg.optInt(MsgParamsNew.recipeSetMinutesLength) > 1){
                            byte recipeSetMinutesH = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesH);
                            buf.put(recipeSetMinutesH);
                        }

//                           byte sectionNumberKey = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
//                           buf.put(sectionNumberKey);
//                           byte sectionNumberLength = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
//                           buf.put(sectionNumberLength);
//                           byte sectionNumber = (byte) msg.optInt(MsgParamsNew.sectionNumber);
//                           buf.put(sectionNumber);
                        break;

                    case 4:
//                           byte categoryCode4 = (byte) msg.optInt(MsgParams.categoryCode);
//                           buf.put(categoryCode4);
                        byte argumentNumber4 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber4);
                        //烟机电源
                        byte fan_powerCtrlKey = (byte) msg.optInt(MsgParamsNew.fan_powerCtrlKey);
                        buf.put(fan_powerCtrlKey);
                        byte fan_powerCtrlLength = (byte) msg.optInt(MsgParamsNew.fan_powerCtrlLength);
                        buf.put(fan_powerCtrlLength);
                        byte fan_powerCtrl = (byte) msg.optInt(MsgParamsNew.fan_powerCtrl);
                        buf.put(fan_powerCtrl);

                        byte fan_gearKey = (byte) msg.optInt(MsgParamsNew.fan_gearKey);
                        buf.put(fan_gearKey);
                        byte fan_gearLength = (byte) msg.optInt(MsgParamsNew.fan_gearLength);
                        buf.put(fan_gearLength);
                        byte fan_gear = (byte) msg.optInt(MsgParamsNew.fan_gear);
                        buf.put(fan_gear);
                    case 5:
                        byte argumentNumber5 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber5);


                        byte powerCtrlKey5 = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey5);
                        byte powerCtrlLength5 = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength5);
                        byte powerCtrlKeyValue5 = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue5);
                        //一体机工作控制
                        byte SteameOvenStatus_Key5 = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key5);
                        byte SteameOvenStatus_Length5 = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length5);
                        byte SteameOvenStatus5 = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus5);

                        //段数
                        byte sectionNumberKey5 = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
                        buf.put(sectionNumberKey5);
                        byte sectionNumberLength5 = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
                        buf.put(sectionNumberLength5);
                        byte sectionNumber5 = (byte) msg.optInt(MsgParamsNew.sectionNumber);
                        buf.put(sectionNumber5);

                        //微波等级

                        byte microWaveLevelCtrlKey = (byte) msg.optInt(MsgParamsNew.microWaveLevelCtrlKey);
                        buf.put(microWaveLevelCtrlKey);
                        byte microWaveLevelLength = (byte) msg.optInt(MsgParamsNew.microWaveLevelLength);
                        buf.put(microWaveLevelLength);
                        byte microWaveLevelCtrl = (byte) msg.optInt(MsgParamsNew.microWaveLevelCtrl);
                        buf.put(microWaveLevelCtrl);
                        if (microWaveLevelLength>1){
                            buf.put((byte) msg.optInt(MsgParamsNew.microWaveLevelCtrl01));
                        }


//                           //微波重量
//                           byte microWaveWeightCtrlKey = (byte) msg.optInt(MsgParamsNew.microWaveWeightCtrlKey);
//                           buf.put(microWaveWeightCtrlKey);
//                           byte microWaveWeightLength = (byte) msg.optInt(MsgParamsNew.microWaveWeightLength);
//                           buf.put(microWaveWeightLength);
//                           byte microWaveWeightCtrl = (byte) msg.optInt(MsgParamsNew.microWaveWeightCtrl);
//                           buf.put(microWaveWeightCtrl);

                        //预定时间
                        byte orderTime5 = (byte) msg.optInt(MsgParamsNew.setOrderMinutesKey);
                        buf.put(orderTime5);
                        byte orderTime_Length5 = (byte) msg.optInt(MsgParamsNew.setOrderMinutesLength);
                        buf.put(orderTime_Length5);
                        buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes01));
                        if (orderTime_Length5>1) {
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes02));
                        }
                        if (orderTime_Length5>2) {
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes03));
                        }

                        //时间
                        byte SteameOvenSetTime_Key5 = (byte) msg.optInt(MsgParamsNew.setTimeKey);
                        buf.put(SteameOvenSetTime_Key5);
                        byte SteameOvenSetTime_Length5 = (byte) msg.optInt(MsgParamsNew.setTimeLength);
                        buf.put(SteameOvenSetTime_Length5);
                        byte SteameOvenSetTime5 = (byte) msg.optInt(MsgParamsNew.setTime0b);
                        buf.put(SteameOvenSetTime5);
                        if (SteameOvenSetTime_Length5>1)
                            buf.put((byte) msg.optInt(MsgParamsNew.setTime1b));

                        break;
                    case 6:
                        byte argumentNumber6 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber6);

                        byte powerCtrlKey6 = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey6);
                        byte powerCtrlLength6 = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength6);
                        byte powerCtrlKeyValue6 = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue6);

                        //一体机工作控制
                        byte SteameOvenStatus_Key6 = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key6);
                        byte SteameOvenStatus_Length6 = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length6);
                        byte SteameOvenStatus6 = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus6);

                        //模式
                        byte mode6 = (byte) msg.optInt(MsgParamsNew.mode);
                        buf.put(mode6);
                        byte modeLength6 = (byte) msg.optInt(MsgParamsNew.modeLength);
                        buf.put(modeLength6);
                        byte modeLengthValue6 = (byte) msg.optInt(MsgParamsNew.modeKey);
                        buf.put(modeLengthValue6);
                        //段数
                        byte sectionNumberKey6 = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
                        buf.put(sectionNumberKey6);
                        byte sectionNumberLength6 = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
                        buf.put(sectionNumberLength6);
                        byte sectionNumber6 = (byte) msg.optInt(MsgParamsNew.sectionNumber);
                        buf.put(sectionNumber6);

                        //微波等级

                        byte microWaveLevelCtrlKey6 = (byte) msg.optInt(MsgParamsNew.microWaveLevelCtrlKey);
                        buf.put(microWaveLevelCtrlKey6);
                        byte microWaveLevelLength6 = (byte) msg.optInt(MsgParamsNew.microWaveLevelLength);
                        buf.put(microWaveLevelLength6);
                        byte microWaveLevelCtrl6 = (byte) msg.optInt(MsgParamsNew.microWaveLevelCtrl);
                        buf.put(microWaveLevelCtrl6);
                        if (microWaveLevelLength6>1){
                            buf.put( (byte) msg.optInt(MsgParamsNew.microWaveLevelCtrl01));
                        }

                        //微波重量
                        byte microWaveWeightCtrlKey6 = (byte) msg.optInt(MsgParamsNew.microWaveWeightCtrlKey);
                        buf.put(microWaveWeightCtrlKey6);
                        byte microWaveWeightLength6 = (byte) msg.optInt(MsgParamsNew.microWaveWeightLength);
                        buf.put(microWaveWeightLength6);
                        byte microWaveWeightCtrl6 = (byte) msg.optInt(MsgParamsNew.microWaveWeightCtrl);
                        buf.put(microWaveWeightCtrl6);


                        //预约时间
                        byte SteamOvenSetTime_Key6 = (byte) msg.optInt(MsgParamsNew.setOrderMinutesKey);
                        buf.put(SteamOvenSetTime_Key6);
                        byte SteamOvenSetTime_Length6 = (byte) msg.optInt(MsgParamsNew.setOrderMinutesLength);
                        buf.put(SteamOvenSetTime_Length6);
                        buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes01));
                        if (SteamOvenSetTime_Length6>1) {
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes02));
                        }
                        if (SteamOvenSetTime_Length6>2) {
                            buf.put((byte) msg.optInt(MsgParamsNew.setOrderMinutes03));
                        }

                        break;

                    //加蒸汽
                    case 7:
                        byte argumentNumber7 = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber7);
                        byte powerCtrlKey7 = (byte) msg.optInt(MsgParamsNew.powerCtrlKey);
                        buf.put(powerCtrlKey7);
                        byte powerCtrlLength7 = (byte) msg.optInt(MsgParamsNew.powerCtrlLength);
                        buf.put(powerCtrlLength7);
                        byte powerCtrlKeyValue7 = (byte) msg.optInt(MsgParamsNew.powerCtrl);
                        buf.put(powerCtrlKeyValue7);

                        //一体机工作控制
                        byte SteameOvenStatus_Key7 = (byte) msg.optInt(MsgParamsNew.workCtrlKey);
                        buf.put(SteameOvenStatus_Key7);
                        byte SteameOvenStatus_Length7 = (byte) msg.optInt(MsgParamsNew.workCtrlLength);
                        buf.put(SteameOvenStatus_Length7);
                        byte SteameOvenStatus7 = (byte) msg.optInt(MsgParamsNew.workCtrl);
                        buf.put(SteameOvenStatus7);
                        //蒸汽
                        byte steamKey7 = (byte) msg.optInt(MsgParamsNew.steamKey);
                        buf.put(steamKey7);
                        byte steamLength7 = (byte) msg.optInt(MsgParamsNew.steamLength);
                        buf.put(steamLength7);
                        byte steam7 = (byte) msg.optInt(MsgParamsNew.steam);
                        buf.put(steam7);


                        break;
                    default:
                        break;

                }

                break;


            case MsgKeys.setSteameOvenStatusControl_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenPowerOnStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenOrderTime_hour);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.getSteameOvenStatus_Req:


                break;
            case MsgKeys.setSteameOvenBasicMode_Req:

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetTime);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
                buf.put(b);
                int recipeId = msg.optInt(MsgParams.SteameOvenRecipeId);
                buf.put((byte) (recipeId & 0Xff));
                buf.put((byte) ((recipeId >> 8) & 0Xff));
                b = (byte) msg.optInt(MsgParams.SteameOvenRecipesteps);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetDownTemp);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OrderTime_value_min);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.OrderTime_value_hour);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                    if (msg.optInt(MsgParams.time_H_key) == 1) {
                        b = (byte) msg.optInt(MsgParams.time_H_key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.time_H_length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.time_H_Value);
                        buf.put(b);
                    }
                }
                break;
            case MsgKeys.setTheRecipe_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenPreFlag);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                    if (msg.optInt(MsgParams.SteameOvenRecipeId) == 1) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeId);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeValue);
                        buf.put(b);
                    }
                    if (msg.optInt(MsgParams.SteameOvenRecipeTotalsteps) == 2) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalsteps);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenRecipeTotalstepsValue);
                        buf.put(b);
                    }

                    if (msg.optInt(MsgParams.SteameOvenRecipesteps) == 3) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenMode);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTime);
                        buf.put(b);
                    }


                }
                break;
            case MsgKeys.setSteameOvenFloodlight_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenLight);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;

            case MsgKeys.setSteameOvenWater_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteameOvenWaterStatus);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.setSteameOvensteam_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;
            case MsgKeys.setSteameOvenMultistageCooking_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                int stepCount = msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
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

                    if (msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key) == 2) {
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
                        buf.put(b);
                    }
                    for (int i = 1; i <= stepCount; i++) {


                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Length + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenMode + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp + i);
                        int time = msg.optInt(MsgParams.SteameOvenTime + i);
                        buf.put(b);
                        if (time > 255) {
                            buf.put((byte) 0);
                            short low = (short) (time & 0Xff);
                            short high = (short) ((time >> 8) & 0Xff);
                            buf.put((byte) low);
                            buf.put((byte) high);
                        } else {
                            b = (byte) msg.optInt(MsgParams.SteameOvenTime + i);
                            buf.put(b);
                        }
                    }

                }

                break;
            case MsgKeys.setSteameOvenAutoRecipeMode610_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                int stepSize = msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
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

                    if (msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key) == 2) {
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.steameOvenTotalNumberOfSegments_Value);
                        buf.put(b);
                    }
                    for (int i = 1; i <= stepSize; i++) {
                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Key + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenSectionOfTheStep_Length + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenMode + i);
                        buf.put(b);

                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp + i);
                        buf.put(b);
                        buf.put((byte) 0);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTime + i);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTemp2 + i);
                        buf.put((byte) 0);
                        buf.put(b);
                        buf.put((byte) 0);
                        b = (byte) msg.optInt(MsgParams.SteameOvenTime2 + i);
                        buf.put(b);
                        buf.put((byte) 0);
                        Log.i("bbbbb", buf.toString());
                    }

                }

                break;
            //新增 2020年2月15日 16:00:19
            case MsgKeys.setSteameOvenAutoRecipeMode_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.SteamOvenAutoRecipeMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.SteameOvenSetTime);
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
                    if (msg.optInt(MsgParams.SetTime_H_key) == 2) {
                        b = (byte) msg.optInt(MsgParams.SetTime_H_key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTime_H_length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetTime_H);
                        buf.put(b);
                    }
                }


                break;
            default:
                break;
        }
    }

    private static final String TAG = "unmarshaller";
    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        Log.e(TAG,"unmarshaller"+"===");
        if (key==191){
            Log.e(TAG,"unmarshaller"+"===");
        }
        switch (key) {
            /**
             * 属性查询响应
             */

            case MsgKeys.getDeviceAttribute_Rep:
//                short numberOfCategory = MsgUtils.getShort(payload[offset++]);
//                msg.putOpt(MsgParams.numberOfCategory, numberOfCategory);
//
//                short categoryCode = MsgUtils.getShort(payload[offset++]);
//                msg.putOpt(MsgParams.categoryCode, categoryCode);

                String mu="";
                for (byte b : payload) {
                    mu+=b+" ";
                }
                Log.e("接收",mu);
                short arg = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, arg);
                while (arg > 0) {
                    short key620 = MsgUtils.getShort(payload[offset++]);
                    short steamOvenHeader_Length = MsgUtils.getShort(payload[offset++]);
//                    Log.e("结果",key620+"------"+steamOvenHeader_Length+"---");
                    switch (key620){
                        case 1:
                            short powerState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.powerState, powerState);
                            break;
                        case 2:
                            short powerCtrl = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.powerCtrl, powerCtrl);
                            break;
                        case 3:
                            short workState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.workState, workState);
                            break;
                        case 6:
                            short orderLeftMinutes = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.orderLeftMinutes, orderLeftMinutes);
                            msg.putOpt(MsgParamsNew.orderMinutesLength,steamOvenHeader_Length);

                            byte[]  orderTimes = new byte[steamOvenHeader_Length];
                            for (int i = 0 ; i < steamOvenHeader_Length ; i ++ ){
                                short orderLeftMinute = MsgUtils.getShort(payload[offset]);
                                orderTimes[i] = (byte) orderLeftMinute ;
                                offset ++ ;
                            }
                            int orderLeftTime = ByteUtils.byteToInt2(orderTimes);
                            msg.putOpt(MsgParamsNew.orderLeftTime, orderLeftTime);
                            offset -= steamOvenHeader_Length ;
//                            if (steamOvenHeader_Length==2) {
//                                short orderRightMinutes = MsgUtils.getShort(payload[offset + 1]);
//                                msg.putOpt(MsgParamsNew.orderRightMinutes, orderRightMinutes);
//                            }else if (steamOvenHeader_Length==3){
//                                short orderRightMinutes = MsgUtils.getShort(payload[offset + 1]);
//                                msg.putOpt(MsgParamsNew.orderRightMinutes, orderRightMinutes);
//
//                                short orderLeftMinutes1 = MsgUtils.getShort(payload[offset + 2]);
//                                msg.putOpt(MsgParamsNew.orderLeftMinutes1, orderLeftMinutes1);
//
//
//                            }else if (steamOvenHeader_Length==4){
//                                short orderRightMinutes = MsgUtils.getShort(payload[offset + 1]);
//                                msg.putOpt(MsgParamsNew.orderRightMinutes, orderRightMinutes);
//
//                                short orderLeftMinutes1 = MsgUtils.getShort(payload[offset + 2]);
//                                msg.putOpt(MsgParamsNew.orderLeftMinutes1, orderLeftMinutes1);
//
//                                short orderRightMinutes1 = MsgUtils.getShort(payload[offset + 3]);
//                                msg.putOpt(MsgParamsNew.orderRightMinutes1, orderRightMinutes1);
//                            }
                            break;
                        case 7:
                            short faultCode = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.faultCode, faultCode);
                            break;
                        case 9:
                            short rotateSwitch = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.rotateSwitch, rotateSwitch);
                            break;
                        case 10:
                            short waterBoxState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.waterBoxState, waterBoxState);
                            break;
                        case 12:
                            short waterLevelState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.waterLevelState, waterLevelState);
                            break;
                        case 13:
                            short doorState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.doorState, doorState);
                            break;
                        case 15:
                            short steamState = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.steamState, steamState);
                            break;
                        case 17:
                            short recipeId = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.recipeId, recipeId);
                            break;
                        case 18:

//                            short recipeSetMinutes = MsgUtils.getShort(payload[offset]);
//                            msg.putOpt(MsgParamsNew.recipeSetMinutes, recipeSetMinutes);
                            byte[]  recipeTimes = new byte[steamOvenHeader_Length];
                            for (int i = 0 ; i < steamOvenHeader_Length ; i ++ ){
                                short recipeTimeMinu = MsgUtils.getShort(payload[offset]);
                                recipeTimes[i] = (byte) recipeTimeMinu ;
                                offset ++ ;
                            }
                            int recipeTime = ByteUtils.byteToInt2(recipeTimes);
                            msg.putOpt(MsgParamsNew.recipeSetMinutes, recipeTime);
                            offset -= steamOvenHeader_Length ;
                            break;
                        case 19:
                            short curTemp = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.curTemp, curTemp);
                            break;
                        case 20:
                            short curTemp2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.curTemp2, curTemp2);
                            break;

                        case 21:
                            short totalRemainSeconds = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.totalRemainSeconds, totalRemainSeconds);
                            short totalRemainSeconds2 = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.totalRemainSeconds2, totalRemainSeconds2);
                            LogUtils.i("testTime", "--------" + totalRemainSeconds + "-----" + totalRemainSeconds2);

                            //获取总剩余时间
                            byte[]  totalRemains = new byte[steamOvenHeader_Length];
                            for (int i = 0 ; i < steamOvenHeader_Length ; i ++ ){
                                short recipeTimeMinu = MsgUtils.getShort(payload[offset]);
                                totalRemains[i] = (byte) recipeTimeMinu ;
                                offset ++ ;
                            }
                            int totalRemain = ByteUtils.byteToInt2(totalRemains);
                            msg.putOpt(MsgParamsNew.totalRemain, totalRemain);
                            offset -= steamOvenHeader_Length ;
                            break;
                        case 22:
                            short descaleFlag = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.descaleFlag, descaleFlag);
                            break;
                        case 23:
                            short curSteamTotalHours = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.curSteamTotalHours, curSteamTotalHours);

                            break;
                        case 24:
                            short curSteamTotalNeedHours = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.curSteamTotalNeedHours, curSteamTotalNeedHours);

                            break;
                        case 25:
                            short cookedTime = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.cookedTime, cookedTime);
                            break;
                        case 26:
                            short chugouType = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.chugouType, chugouType);
                            break;
                        case 99:
                            short curSectionNbr = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.curSectionNbr, curSectionNbr);
                            break;
                        case 100:
                            short sectionNumber = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.sectionNumber, sectionNumber);
                            break;
                        case 101:
                            short mode = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.mode, mode);
                            Log.e("模式11",mode+"--MsgMar");
                            break;
                        case 102:
                            short setUpTemp = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setUpTemp, setUpTemp);
                            break;
                        case 103:
                            short setDownTemp = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setDownTemp, setDownTemp);
                            break;
                        case 104:
                            short setTime = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setTime, setTime);
                            short setTimeH = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.setTimeH, setTimeH);
                            break;
                        case 105:
                            short restTime = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.restTime, restTime);
                            short restTimeH = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.restTimeH, restTimeH);
                            break;
                        case 106:
                            short steam = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.steam, steam);
                            break;
                        case 111:
                            short mode1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.mode2, mode1);
                            break;
                        case 112:
                            short setUpTemp1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setUpTemp2, setUpTemp1);
                            break;
                        case 113:
                            short setDownTemp1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setDownTemp2, setDownTemp1);
                            break;
                        case 114:
                            short setTime1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setTime2, setTime1);
                            short setTimeH2 = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.setTimeH2, setTimeH2);
                            break;
                        case 115:
                            short restTime1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.restTime2, restTime1);
                            short restTimeH2 = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.restTimeH2, restTimeH2);
                            Log.e("时间--",restTime1+"---"+restTimeH2);
                            break;
                        case 116:
                            short steam1 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.steam2, steam1);
                            break;
                        case 121:
                            short mode2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.mode3, mode2);
                            break;
                        case 122:
                            short setUpTemp2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setUpTemp3, setUpTemp2);
                            break;
                        case 123:
                            short setDownTemp2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setDownTemp3, setDownTemp2);

                            break;
                        case 124:
                            short setTime2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.setTime3, setTime2);
                            short setTimeH3 = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.setTimeH3, setTimeH3);
                            break;
                        case 125:
                            short restTime2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.restTime3, restTime2);
                            short restTimeH3 = MsgUtils.getShort(payload[offset+1]);
                            msg.putOpt(MsgParamsNew.restTimeH3, restTimeH3);
                            break;
                        case 126:
                            short steam2 = MsgUtils.getShort(payload[offset]);
                            msg.putOpt(MsgParamsNew.steam3, steam2);
                            break;
                        default:
//                            offset += steamOvenHeader_Length;
                            break;
                    }
                    offset += steamOvenHeader_Length;
                    arg--;
                }

                break;
            case MsgKeys.setDeviceAttribute_Rep:
                short arg1 = MsgUtils.getShort(payload[offset++]);
//                Log.e("结果","------------"+arg1);
//                msg.putOpt(MsgParams.faultCode, arg1);

                break;

            case MsgKeys.getDeviceAlarmEventReport:
                short code = MsgUtils.getShort(payload[offset++]);

                msg.putOpt(MsgParams.faultCode, code);
                break;


            case MsgKeys.getDeviceEventReport:
                break;
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
//                            msg.putOpt(MsgParams.time_H_key,
//                                    argumentKey);
//                            msg.putOpt(MsgParams.time_H_length,
//                                    MsgUtils.getShort(payload[offset++]));
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
                arg = MsgUtils.getShort(payload[offset++]);
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
