package com.robam.common.io.device.marshal;


import android.util.Log;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.pojos.device.integratedStove.IntegratedStoveConstant;

import java.nio.ByteBuffer;

/**
 * Created by 14807 on 2018/1/25.
 */
public class IntegratedStoveMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) {
        try {

            boolean bool;
            byte b;
            String str;
            switch (key) {
                //查询设备状态 190
                case MsgKeys.getDeviceAttribute_Req:
                    buf.put((byte) 1);
                    buf.put((byte) msg.optInt(MsgParams.categoryCode));
                    buf.put((byte) 0);
                    break;
                //设置属性 192
                case MsgKeys.setDeviceAttribute_Req:
//                    str = msg.optString(MsgParams.UserId);
//                    buf.put(str.getBytes());
                    //判断设置的是什么属性
                    int type = msg.optInt(MsgParamsNew.type);
                    switch (type) {
                        //专业模式 单一 设置
                        case 0:
                            byte numberOfCategory = (byte) msg.optInt(MsgParams.categoryCode);
                            buf.put(numberOfCategory);
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
                            byte OrderTime = (byte) msg.optInt(MsgParamsNew.setOrderMinutes);
                            buf.put(OrderTime);
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
                            //时间
                            byte SteameOvenSetTime_Key = (byte) msg.optInt(MsgParamsNew.setTimeKey);
                            buf.put(SteameOvenSetTime_Key);
                            byte SteameOvenSetTime_Length = (byte) msg.optInt(MsgParamsNew.setTimeLength);
                            buf.put(SteameOvenSetTime_Length);
                            byte SteameOvenSetTime = (byte) msg.optInt(MsgParamsNew.setTime);
                            buf.put(SteameOvenSetTime);
                            //高八位
                            if (msg.optInt(MsgParamsNew.setTimeH) != 0) {
                                byte SteameOvenSetTimeH = (byte) msg.optInt(MsgParamsNew.setTimeH);
                                buf.put(SteameOvenSetTimeH);
                            }
                            //蒸汽量
                            byte steamKey = (byte) msg.optInt(MsgParamsNew.steamKey);
                            buf.put(steamKey);
                            byte steamLength = (byte) msg.optInt(MsgParamsNew.steamLength);
                            buf.put(steamLength);
                            byte steam = (byte) msg.optInt(MsgParamsNew.steam);
                            buf.put(steam);
                            break;
                        //单属性设置
                        case 1:
                            byte categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
                            buf.put(categoryCode);
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
                            byte numberOfCategory2 = (byte) msg.optInt(MsgParams.categoryCode);
                            buf.put(numberOfCategory2);
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
//                            //预约时间
//                            byte OrderTime_key = (byte) msg.optInt(MsgParamsNew.setOrderMinutesKey);
//                            buf.put(OrderTime_key);
//                            byte OrderTime_length = (byte) msg.optInt(MsgParamsNew.setOrderMinutesLength);
//                            buf.put(OrderTime_length);
//                            byte OrderTime = (byte) msg.optInt(MsgParamsNew.setOrderMinutes);
//                            buf.put(OrderTime);
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
                                    //时间
                                    byte setTimeKey = (byte) msg.optInt(MsgParamsNew.setTimeKey + i);
                                    buf.put(setTimeKey);
                                    byte setTimeLength = (byte) msg.optInt(MsgParamsNew.setTimeLength + i);
                                    buf.put(setTimeLength);
                                    byte setTime = (byte) msg.optInt(MsgParamsNew.setTime + i);
                                    buf.put(setTime);
                                    //蒸汽量
                                    byte steamKey2 = (byte) msg.optInt(MsgParamsNew.steamKey + i);
                                    buf.put(steamKey2);
                                    byte steamLength2 = (byte) msg.optInt(MsgParamsNew.steamLength + i);
                                    buf.put(steamLength2);
                                    byte steam2 = (byte) msg.optInt(MsgParamsNew.steam + i);
                                    buf.put(steam2);
                                }
                            }

                            break;
                        case 3:
                            byte numberOfCategory3 = (byte) msg.optInt(MsgParams.categoryCode);
                            buf.put(numberOfCategory3);
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

                            byte recipeSetMinutesKey = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesKey);
                            buf.put(recipeSetMinutesKey);
                            byte recipeSetMinutesLength = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesLength);
                            buf.put(recipeSetMinutesLength);
                            byte recipeSetMinutes = (byte) msg.optInt(MsgParamsNew.recipeSetMinutes);
                            buf.put(recipeSetMinutes);
                            if (msg.optInt(MsgParamsNew.recipeSetMinutesH) > 0){
                                byte recipeSetMinutesH = (byte) msg.optInt(MsgParamsNew.recipeSetMinutesH);
                                buf.put(recipeSetMinutesH);
                            }

                            byte sectionNumberKey = (byte) msg.optInt(MsgParamsNew.sectionNumberKey);
                            buf.put(sectionNumberKey);
                            byte sectionNumberLength = (byte) msg.optInt(MsgParamsNew.sectionNumberLength);
                            buf.put(sectionNumberLength);
                            byte sectionNumber = (byte) msg.optInt(MsgParamsNew.sectionNumber);
                            buf.put(sectionNumber);
                            break;

                        case 4:
                            byte categoryCode4 = (byte) msg.optInt(MsgParams.categoryCode);
                            buf.put(categoryCode4);
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
                        default:
                            break;

                    }


                    break;
                case MsgKeys.setDeviceIntelligentInteractiveModel_Req://设置设备智能互动模式
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    short numberCategory = (byte) msg.optInt(MsgParams.numberOfCategory);
                    buf.put((byte) numberCategory);

                    while (numberCategory > 0) {
                        byte categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
                        buf.put(categoryCode);
                        byte argumentNumber = (byte) msg.optInt(MsgParams.ArgumentNumber);
                        buf.put(argumentNumber);
                        if (argumentNumber > 0) {
                            switch (categoryCode) {
                                case 'A':
                                    byte fanHeader_Key = (byte) msg.optInt(MsgParams.fanHeader_Key);
                                    buf.put(fanHeader_Key);
                                    byte fanHeader_Leng = (byte) msg.optInt(MsgParams.fanHeader_Leng);
                                    buf.put(fanHeader_Leng);
                                    switch (fanHeader_Key) {
                                        case '1':
                                            bool = msg.optBoolean(MsgParams.fanAndStoveSwitchLinkage);
                                            buf.put(bool ? (byte) 1 : (byte) 0);
                                            bool = msg.optBoolean(MsgParams.fanPowerSwitchLinkage);
                                            buf.put(bool ? (byte) 1 : (byte) 0);
                                            bool = msg.optBoolean(MsgParams.fanTimeDelayShutdownSwitch);
                                            buf.put(bool ? (byte) 1 : (byte) 0);

                                            b = (byte) msg.optInt(MsgParams.fanDelaySwitchTime);
                                            buf.put(b);

                                            bool = msg.optBoolean(MsgParams.fanCleaningPromptSwitch);
                                            buf.put(bool ? (byte) 1 : (byte) 0);

                                            bool = msg.optBoolean(MsgParams.fanOpenRegularVentilation);
                                            buf.put(bool ? (byte) 1 : (byte) 0);

                                            b = (byte) msg.optInt(MsgParams.fanRegularVentilationIntervalTime);
                                            buf.put(b);

                                            bool = msg.optBoolean(MsgParams.fanOpenWeeklyVentilation);
                                            buf.put(bool ? (byte) 1 : (byte) 0);

                                            b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Week);
                                            buf.put(b);

                                            b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Hour);
                                            buf.put(b);

                                            b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Minute);
                                            buf.put(b);

                                            break;
                                        case '2':
                                            b = (byte) msg.optInt(MsgParams.fanTimeForAwhileSwitch);
                                            buf.put(b);
                                            b = (byte) msg.optInt(MsgParams.fanTimeForAwhile);
                                            buf.put(b);
                                            break;

                                    }
                                    break;

                                case 'C':
                                    byte steamHeader_Key = (byte) msg.optInt(MsgParams.steamHeader_Key);
                                    buf.put(steamHeader_Key);
                                    byte steamHeader_Leng = (byte) msg.optInt(MsgParams.steamHeader_Leng);
                                    buf.put(steamHeader_Leng);
                                    switch (steamHeader_Key) {
                                        case '1':
                                            b = (byte) msg.optInt(MsgParams.steamWorkStatus);
                                            buf.put(b);
                                            break;

                                    }
                                    break;
                            }

                        }

                        numberCategory--;
                    }
                    break;
                case MsgKeys.setRikaOveAutoRecipe_Req:
                    byte categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
                    buf.put(categoryCode);
                    byte argumentNumber = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(argumentNumber);
                    if (argumentNumber > 0) {
                        switch (categoryCode) {
                            case 'A':

                                break;
                            case 'B':

                                break;
                            case 'C':

                                break;
                            case 'D':

                                break;
                            case 'E':
                                byte header_Key = (byte) msg.optInt(MsgParams.steamOvenHeader_Key);
                                buf.put(header_Key);
                                byte header_Leng = (byte) msg.optInt(MsgParams.steamOvenHeader_Leng);
                                buf.put(header_Leng);
                                switch (header_Key) {
                                    case '1':
                                        b = (byte) msg.optInt(MsgParams.steamOvenModel);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.stemOvenTime);
                                        buf.put(b);
                                        break;

                                }
                                break;
                        }
                    }
                    break;
                case MsgKeys.setRikaOvenMultiStep_Req:
                    categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
                    buf.put(categoryCode);
                    argumentNumber = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(argumentNumber);
                    if (argumentNumber > 0) {
                        switch (categoryCode) {
                            case 'A':

                                break;
                            case 'B':

                                break;
                            case 'C':

                                break;
                            case 'D':

                                break;
                            case 'E':
                                byte header_Key = (byte) msg.optInt(MsgParams.steamOvenHeader_Key);
                                buf.put(header_Key);
                                byte header_Leng = (byte) msg.optInt(MsgParams.steamOvenHeader_Leng);
                                buf.put(header_Leng);
                                switch (header_Key) {
                                    case '1':
                                        b = (byte) msg.optInt(MsgParams.steamOvenModelOne);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.steamOvenTempOne);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.steamOvenTimeOne);
                                        buf.put(b);
                                        break;

                                }
                                byte header_Key1 = (byte) msg.optInt(MsgParams.steamOvenHeader_Key1);
                                buf.put(header_Key1);
                                byte header_Leng1 = (byte) msg.optInt(MsgParams.steamOvenHeader_Leng1);
                                buf.put(header_Leng1);
                                switch (header_Key1) {
                                    case '2':
                                        b = (byte) msg.optInt(MsgParams.steamOvenModelTwo);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.steamOvenTempTwo);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.steamOvenTimeTwo);
                                        buf.put(b);
                                        break;

                                }
                                break;
                        }
                    }
                    break;
                case MsgKeys.setRikaIntelSet_Req:
                    categoryCode = (byte) msg.optInt(MsgParams.categoryCode);
                    buf.put(categoryCode);
                    argumentNumber = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(argumentNumber);
                    if (argumentNumber > 0) {
                        switch (categoryCode) {
                            case 'A':

                                break;
                            case 'B':

                                break;
                            case 'C':

                                break;
                            case 'D':

                                break;
                            case 'E':
                                byte header_Key = (byte) msg.optInt(MsgParams.steamOvenHeader_Key);
                                buf.put(header_Key);
                                byte header_Leng = (byte) msg.optInt(MsgParams.steamOvenHeader_Leng);
                                buf.put(header_Leng);
                                switch (header_Key) {
                                    case '1':
                                        b = (byte) msg.optInt(MsgParams.fanSteamOvenLinkage);
                                        buf.put(b);
                                        b = (byte) msg.optInt(MsgParams.fanSteamOvenDelayShutdownSwitch);
                                        buf.put(b);
                                        break;

                                }
                                break;
                        }
                    }
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) {
        try {

            int offset = 0;
            switch (key) {
                //查询集成灶状态返回值
                case MsgKeys.getDeviceAttribute_Rep:
                    short numberOfCategory = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.numberOfCategory, numberOfCategory);

                    while (numberOfCategory > 0) {

                        short categoryCode = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.categoryCode, categoryCode);
                        short arg = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.ArgumentNumber, arg);
                        LogUtils.i("20180330", "categoryCode:" + categoryCode + " ArgumentNumber:" + arg);
                        while (arg > 0) {
                            switch (categoryCode) {
                                //烟机
                                case IntegratedStoveConstant.FAN:
                                    short fanHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                                    LogUtils.i("20180330", "fanHeader_Key:" + fanHeader_Key);
                                    short fan_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Leng, fan_Length);
                                    LogUtils.i("20180330", " fan_Length:" + fan_Length);
                                    switch (fanHeader_Key) {
                                        case 1:
                                            short fan_powerState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.fan_powerState,
                                                    fan_powerState);
                                            break;
                                        case 3:
                                            short fan_gear = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.fan_gear,
                                                    fan_gear);
                                            break;
                                        case 4:
                                            short fan_lightSwitch = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.fan_lightSwitch,
                                                    fan_lightSwitch);
                                            break;
                                        case 5:
                                            short fan_stove_linkage = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.fan_stove_linkage,
                                                    fan_stove_linkage);
                                            break;
                                        default:
                                            offset += fan_Length;
                                            break;

                                    }
                                    break;
                                //灶具
                                case IntegratedStoveConstant.STOVE:
                                    short stoveHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Key, stoveHeader_Key);
                                    short stoveHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Leng, stoveHeader_Length);
                                    switch (stoveHeader_Key) {

                                        case 1:
                                            short stove_powerState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_powerState, stove_powerState);
                                            break;
                                        case 3:
                                            short stove_head_num = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_head_num, stove_head_num);
                                            break;

                                        case 4:
                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            break;
                                        case 5:
                                            short stove_gear = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_gear, stove_gear);
                                            break;
                                        case 6:
                                            short stove_v_chip = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_v_chip, stove_v_chip);
                                            break;
                                        case 7:
                                            short repice_id = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.repice_id, repice_id);
                                            break;
                                        case 8:
                                            short stove_temp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_temp, stove_temp);
                                            offset++;
                                            break;
                                        case 9:
                                            short stove_time = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_time, stove_time);
                                            offset++;
                                            break;
                                        case 10:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            break;
                                        case 11:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            offset++;
                                            break;
                                        case 12:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            offset++;
                                            break;
                                        case 13:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            break;
                                        case 129:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            short stove_powerState2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_powerState2, stove_powerState2);
                                            break;
                                        case 130:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                        case 131:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset += stoveHeader_Length;
                                            break;
                                        case 132:
                                            short stove_head_type2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_head_type2, stove_head_type2);
                                            break;
                                        case 133:
                                            short stove_gear2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_gear2, stove_gear2);
                                            break;
                                        case 134:
                                            short stove_v_chip2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_v_chip2, stove_v_chip2);
                                            break;
                                        case 135:
                                            short repice_id2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.repice_id2, repice_id2);
                                            break;
                                        case 136:
                                            short stove_temp2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_temp2, stove_temp2);
                                            offset++;
                                            break;
                                        case 137:
                                            short stove_time2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.stove_time2, stove_time2);
                                            offset++;
                                            break;
                                        case 138:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            break;
                                        case 139:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            offset++;
                                            break;
                                        case 140:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            offset++;
                                            break;
                                        case 141:
//                                            short stove_head_type = MsgUtils.getShort(payload[offset++]);
//                                            msg.putOpt(MsgParamsNew.stove_head_type, stove_head_type);
                                            offset++;
                                            break;
                                        default:
                                            offset += stoveHeader_Length;
                                            break;
                                    }
//

                                    break;
                                //一体机
                                case IntegratedStoveConstant.STEAME_OVEN_ONE:
                                    short steamOvenHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenHeader_Key, steamOvenHeader_Key);
                                    short steamOvenHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenHeader_Leng, steamOvenHeader_Length);
                                    switch (steamOvenHeader_Key) {
                                        case 1:
                                            short powerState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.powerState, powerState);
                                            break;
                                        case 2:
                                            short powerCtrl = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.powerCtrl, powerCtrl);
                                            break;
                                        case 3:
                                            short workState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.workState, workState);
                                            break;
                                        case 6:
                                            short orderLeftMinutes = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.orderLeftMinutes, orderLeftMinutes);
                                            break;
                                        case 7:
                                            short faultCode = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.faultCode, faultCode);
                                            break;
                                        case 9:
                                            short rotateSwitch = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.rotateSwitch, rotateSwitch);
                                            break;
                                        case 10:
                                            short waterBoxState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.waterBoxState, waterBoxState);
                                            break;
                                        case 12:
                                            short waterLevelState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.waterLevelState, waterLevelState);
                                            break;
                                        case 13:
                                            short doorState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.doorState, doorState);
                                            break;
                                        case 15:
                                            short steamState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.steamState, steamState);
                                            break;
                                        case 17:
                                            short recipeId = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.recipeId, recipeId);
                                            break;
                                        case 18:
                                            short recipeSetMinutes = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.recipeSetMinutes, recipeSetMinutes);
                                            break;
                                        case 19:
                                            short curTemp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.curTemp, curTemp);
                                            break;
                                        case 20:
                                            short curTemp2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.curTemp2, curTemp2);
                                            break;

                                        case 21:
                                            short totalRemainSeconds = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.totalRemainSeconds, totalRemainSeconds);
                                            short totalRemainSeconds2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.totalRemainSeconds2, totalRemainSeconds2);
                                            LogUtils.i("testTime", "--------" + totalRemainSeconds + "-----" + totalRemainSeconds2);
                                            break;
                                        case 22:
                                            short descaleFlag = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.descaleFlag, descaleFlag);
                                            break;
                                        case 23:
                                            short curSteamTotalHours = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.curSteamTotalHours, curSteamTotalHours);
                                            offset++;
                                            break;
                                        case 24:
                                            short curSteamTotalNeedHours = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.curSteamTotalNeedHours, curSteamTotalNeedHours);
                                            offset++;
                                            break;
                                        case 25:
                                            short cookedTime = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.cookedTime, cookedTime);
                                            break;
                                        case 26:
                                            short chugouType = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.chugouType, chugouType);
                                            break;
                                        case 99:
                                            short curSectionNbr = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.curSectionNbr, curSectionNbr);
                                            break;
                                        case 100:
                                            short sectionNumber = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.sectionNumber, sectionNumber);
                                            break;
                                        case 101:
                                            short mode = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.mode, mode);
                                            break;
                                        case 102:
                                            short setUpTemp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setUpTemp, setUpTemp);
                                            break;
                                        case 103:
                                            short setDownTemp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setDownTemp, setDownTemp);
                                            break;
                                        case 104:
                                            short setTime = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTime, setTime);
                                            short setTimeH = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTimeH, setTimeH);
                                            break;
                                        case 105:
                                            short restTime = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTime, restTime);
                                            short restTimeH = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTimeH, restTimeH);
                                            break;
                                        case 106:
                                            short steam = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.steam, steam);
                                            break;
                                        case 111:
                                            short mode1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.mode2, mode1);
                                            break;
                                        case 112:
                                            short setUpTemp1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setUpTemp2, setUpTemp1);
                                            break;
                                        case 113:
                                            short setDownTemp1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setDownTemp2, setDownTemp1);
                                            break;
                                        case 114:
                                            short setTime1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTime2, setTime1);
                                            short setTimeH2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTimeH2, setTimeH2);
                                            break;
                                        case 115:
                                            short restTime1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTime2, restTime1);
                                            short restTimeH2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTimeH2, restTimeH2);
                                            break;
                                        case 116:
                                            short steam1 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.steam2, steam1);
                                            break;
                                        case 121:
                                            short mode2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.mode3, mode2);
                                            break;
                                        case 122:
                                            short setUpTemp2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setUpTemp3, setUpTemp2);
                                            break;
                                        case 123:
                                            short setDownTemp2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setDownTemp3, setDownTemp2);

                                            break;
                                        case 124:
                                            short setTime2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTime3, setTime2);
                                            short setTimeH3 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.setTimeH3, setTimeH3);
                                            break;
                                        case 125:
                                            short restTime2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTime3, restTime2);
                                            short restTimeH3 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.restTimeH3, restTimeH3);
                                            break;
                                        case 126:
                                            short steam2 = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParamsNew.steam3, steam2);
                                            break;
                                        default:
                                            offset += steamOvenHeader_Length;
                                            break;
                                    }
                                    break;

                                default:
                                    break;
                            }

                            arg--;
                        }

                        numberOfCategory--;
                    }

                    break;
                //事件上报
                case MsgKeys.getDeviceEventReport:
                    LogUtils.i("20180515", "alarmEventReport:" + MsgKeys.alarmEventReport);
                    //设备型号
                    short categoryCodeEvent = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.categoryCode, categoryCodeEvent);
                    short event = MsgUtils.getShort(payload[offset++]);
                    switch (categoryCodeEvent) {
                        case IntegratedStoveConstant.FAN:
                            break;
                        case IntegratedStoveConstant.STOVE:
                            break;
                        case IntegratedStoveConstant.STEAME_OVEN_ONE:
                            msg.putOpt(MsgParams.steamEventCode, event);
                            break;
                        default:
                            break;
                    }
                    break;
                //报警上报
                case MsgKeys.getDeviceAlarmEventReport:
                    LogUtils.i("20180515", "alarmEventReport:" + MsgKeys.alarmEventReport);
                    //设备型号
                    short categoryCodeAlarm = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.categoryCode, categoryCodeAlarm);
                    //故障码
                    msg.putOpt(MsgParamsNew.faultCode,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.eventReport:
                    LogUtils.i("20180515", "eventReport:" + MsgKeys.eventReport);
                    short numberOfCategoryReport = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.numberOfCategory, numberOfCategoryReport);

                    while (numberOfCategoryReport > 0) {
                        short categoryCode = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.categoryCode, categoryCode);
                        short arg = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.ArgumentNumber, arg);
                        while (arg > 0) {
                            switch (categoryCode) {
                                case 'A'://烟机事件
                                    short fanHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                                    short fanHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Length);

                                    switch (fanHeader_Key) {
                                        case '1':
                                            if (fanHeader_Length > 0) {
                                                msg.putOpt(MsgParams.fanEventCode,
                                                        MsgUtils.getShort(payload[offset++]));
                                                msg.putOpt(MsgParams.fanEventArg,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;

                                    }

                                    break;

                                case 'B'://灶具事件

                                    short stoveHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Key, stoveHeader_Key);
                                    short stoveHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Leng, stoveHeader_Length);

                                    switch (stoveHeader_Key) {
                                        case '1':
                                            short stoveEventCode = MsgUtils.getShort(payload[offset++]);
                                            Log.e("eventReport", "stoveEventCode: " + stoveEventCode);
                                            short stoveEventArg = MsgUtils.getShort(payload[offset++]);
                                            Log.e("eventReport", "stoveEventArg: " + stoveEventCode);
                                            if (stoveHeader_Length > 0) {
                                                msg.putOpt(MsgParams.stoveEventCode,
                                                        stoveEventCode);
                                                msg.putOpt(MsgParams.stoveEventArg,
                                                        stoveEventArg);
                                            }
                                            break;
                                    }
                                    break;

                                case 'C'://消毒柜事件

                                    short sterilHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilHeader_Key, sterilHeader_Key);
                                    short sterilHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilHeader_Leng, sterilHeader_Length);

                                    switch (sterilHeader_Key) {
                                        case '1':
                                            if (sterilHeader_Length > 0) {
                                                msg.putOpt(MsgParams.sterilEventCode,
                                                        MsgUtils.getShort(payload[offset++]));

                                                msg.putOpt(MsgParams.sterilEventArg,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }

                                    break;

                                case 'D'://蒸汽炉事件

                                    short steamHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamHeader_Key, steamHeader_Key);
                                    short steamHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamHeader_Leng, steamHeader_Length);
                                    switch (steamHeader_Key) {
                                        case '1':
                                            if (steamHeader_Length > 0) {
                                                msg.putOpt(MsgParams.steamEventCode,
                                                        MsgUtils.getShort(payload[offset++]));

                                                msg.putOpt(MsgParams.steamEventArg,
                                                        MsgUtils.getShort(payload[offset++]));

                                            }
                                            break;
                                    }
                                    break;

                                case 'E':
                                    short steamOvenHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenHeader_Key, steamOvenHeader_Key);
                                    short steamOvenHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenHeader_Leng, steamOvenHeader_Length);
                                    switch (steamOvenHeader_Key) {
                                        case '1':
                                            if (steamOvenHeader_Length > 0) {
                                                msg.putOpt(MsgParams.steamOvenEventCode,
                                                        MsgUtils.getShort(payload[offset++]));

                                                msg.putOpt(MsgParams.steamOvenEventArg,
                                                        MsgUtils.getShort(payload[offset++]));

                                            }
                                            break;
                                    }
                                    break;

                                case 'F':

                                    break;
                            }

                            arg--;
                        }


                        numberOfCategoryReport--;
                    }
                    break;
                case MsgKeys.setRikaIntelSet_Rep:
                    short categoryCode = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.categoryCode, categoryCode);
                    short arg = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber, arg);
                    while (arg > 0) {
                        switch (categoryCode) {
                            case 'E':
                                short header_Key = MsgUtils.getShort(payload[offset++]);
                                msg.putOpt(MsgParams.steamOvenHeader_Key, header_Key);
                                short header_Length = MsgUtils.getShort(payload[offset++]);
                                msg.putOpt(MsgParams.steamOvenHeader_Leng, header_Length);
                                switch (header_Key) {
                                    case '1':
                                        if (header_Length > 0) {
                                            short fanSteamOvenLinkage = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.fanSteamOvenLinkage, fanSteamOvenLinkage);
                                            short fanSteamOvenDelayShutdownSwitch = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.fanSteamOvenDelayShutdownSwitch, fanSteamOvenDelayShutdownSwitch);
                                        }
                                        break;

                                }
                        }
                        arg--;
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
        }
    }
}
