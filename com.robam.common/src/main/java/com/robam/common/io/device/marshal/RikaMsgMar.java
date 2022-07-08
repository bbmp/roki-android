package com.robam.common.io.device.marshal;


import android.util.Log;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by 14807 on 2018/1/25.
 */
public class RikaMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) {
        try {

            boolean bool;
            byte b;
            String str;
            switch (key) {

                case MsgKeys.readIntelligentInteractiveModeSetting_Req:

                    break;
                case MsgKeys.readDeviceStatus_Req:
                    break;
                case MsgKeys.setDeviceRunStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    byte numberOfCategory = (byte) msg.optInt(MsgParams.numberOfCategory);
                    buf.put(numberOfCategory);

                    while (numberOfCategory > 0) {
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
                                            b = (byte) msg.optInt(MsgParams.rikaFanWorkStatus);
                                            buf.put(b);
                                            break;
                                        case '2':
                                            b = (byte) msg.optInt(MsgParams.rikaFanPower);
                                            buf.put(b);
                                            break;
                                        case '3':
                                            b = (byte) msg.optInt(MsgParams.rikaFanLight);
                                            buf.put(b);
                                            break;
                                        case '4':
                                            b = (byte) msg.optInt(MsgParams.rikaFanCleaningRemind);
                                            buf.put(b);
                                            break;
                                    }
                                    break;
                                case 'C':
                                    byte sterilHeader_Key = (byte) msg.optInt(MsgParams.sterilHeader_Key);
                                    buf.put(sterilHeader_Key);
                                    byte sterilHeader_Leng = (byte) msg.optInt(MsgParams.sterilHeader_Leng);
                                    buf.put(sterilHeader_Leng);
                                    switch (sterilHeader_Key) {
                                        case '1':
                                            b = (byte) msg.optInt(MsgParams.sterilWorkStatus);
                                            buf.put(b);
                                            int sterilWorkModelTime = msg.optInt(MsgParams.sterilWorkModelTime);
                                            buf.put((byte) ((sterilWorkModelTime >> 8) & 0Xff));
                                            buf.put((byte) (sterilWorkModelTime & 0Xff));

                                            int steriOrderTime = msg.optInt(MsgParams.sterilOrderTime);
                                            buf.put((byte) ((steriOrderTime >> 8) & 0Xff));
                                            buf.put((byte) (steriOrderTime & 0Xff));

                                            b = (byte) msg.optInt(MsgParams.sterilWarmDishTemp);
                                            buf.put(b);
                                            break;
                                        case '2':
                                            b = (byte) msg.optInt(MsgParams.sterilLockStatus);
                                            buf.put(b);
                                            break;
                                    }
                                    break;
                                case 'D':
                                    byte steamHeader_Key = (byte) msg.optInt(MsgParams.steamHeader_Key);
                                    buf.put(steamHeader_Key);
                                    byte steamHeader_Leng = (byte) msg.optInt(MsgParams.steamHeader_Leng);
                                    buf.put(steamHeader_Leng);
                                    switch (steamHeader_Key) {
                                        case '1':
                                            b = (byte) msg.optInt(MsgParams.steamWorkStatus);
                                            buf.put(b);
                                            break;
                                        case '2':
                                            b = (byte) msg.optInt(MsgParams.steamRunModel);
                                            buf.put(b);
                                            b = (byte) msg.optInt(MsgParams.steamSetTemp);
                                            buf.put(b);
                                            b = (byte) msg.optInt(MsgParams.steamSetTime);
                                            buf.put(b);
                                            break;
                                        case '3':
                                            int recipeId = msg.optInt(MsgParams.steamRecipeId);
                                            buf.put((byte) (recipeId & 0Xff));
                                            buf.put((byte) ((recipeId >> 8) & 0Xff));
                                            b = (byte) msg.optInt(MsgParams.steamRecipeStep);
                                            buf.put(b);
                                            break;
                                        case '6':
                                            b = (byte) msg.optInt(MsgParams.steamLightState);
                                            buf.put(b);
                                            break;
                                    }
                                    break;
                                case 'E':
                                    if (argumentNumber > 1) {
                                        buf.put((byte) 49);
                                        buf.put((byte) 1);
                                        buf.put((byte) 4);
                                    }
                                    byte steamOvenHeader_Key = (byte) msg.optInt(MsgParams.steamOvenHeader_Key);
                                    buf.put(steamOvenHeader_Key);
                                    byte steamOvenHeader_Leng = (byte) msg.optInt(MsgParams.steamOvenHeader_Leng);
                                    buf.put(steamOvenHeader_Leng);
                                    switch (steamOvenHeader_Key) {
                                        case '1':
                                            b = (byte) msg.optInt(MsgParams.steamOvenWorkStatus);
                                            buf.put(b);
                                            break;
                                        case '2':
                                            int model = msg.optInt(MsgParams.steamOvenRunModel);
                                            boolean flag = false;
                                            if (model == 9 || model == 14  || model == 19 || model == 24 || model == 18) {
                                                flag = true;
                                            }
                                            b = (byte) model;
                                            buf.put(b);
                                            if (flag) {
                                                b = (byte) msg.optInt(MsgParams.steamOvenSetTemp);
                                                buf.put(b);
                                                b = (byte) msg.optInt(MsgParams.steamOvenSetTime);
                                                buf.put(b);
                                            } else {
                                                b = (byte) msg.optInt(MsgParams.steamOvenSetTime);
                                                buf.put(b);
                                                b = (byte) msg.optInt(MsgParams.steamOvenSetTemp);
                                                buf.put(b);
                                            }
                                            b = (byte) msg.optInt(MsgParams.steamOvenPreFlag);
                                            buf.put(b);
                                            break;
                                        case '3':
                                            int recipeId = msg.optInt(MsgParams.steamOvenRecipeId);
                                            buf.put((byte) (recipeId & 0Xff));
                                            buf.put((byte) ((recipeId >> 8) & 0Xff));
                                            b = (byte) msg.optInt(MsgParams.steamOvenRecipeId);
                                            buf.put(b);
                                            break;
                                        case '4':
                                            int orderTime = msg.optInt(MsgParams.steamOvenOrderTime);
                                            buf.put((byte) (orderTime & 0Xff));
                                            buf.put((byte) ((orderTime >> 8) & 0Xff));
                                            break;
                                        case '5':
                                            b = (byte) msg.optInt(MsgParams.steamOvenLockStatus);
                                            buf.put(b);
                                            break;
                                        case '6':
                                            b = (byte) msg.optInt(MsgParams.steamOvenLight);
                                            buf.put(b);
                                            break;
                                        case '7':
                                            b = (byte) msg.optInt(MsgParams.steamOvenWaterStatus);
                                            buf.put(b);
                                            break;
                                    }
                                    break;
                            }
                        }
                        numberOfCategory--;
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
                case MsgKeys.readIntelligentInteractiveModeSetting_Rep:
                    short numberCategory = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.numberOfCategory, numberCategory);
                    while (numberCategory > 0) {
                        short categoryCode = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.categoryCode, categoryCode);
                        short arg = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.ArgumentNumber, arg);

                        while (arg > 0) {

                            switch (categoryCode) {

                                case 'A':
                                    short fanHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                                    short fan_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Leng, fan_Length);

                                    switch (fanHeader_Key) {

                                        case '1':
                                            if (fan_Length > 0) {
                                                short fanAndStoveSwitchLinkageStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanAndStoveSwitchLinkage,
                                                        fanAndStoveSwitchLinkageStatus == 1);

                                                short fanPowerSwitchLinkage = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanPowerSwitchLinkage,
                                                        fanPowerSwitchLinkage == 1);

                                                short fanTimeDelayShutdownSwitch = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanTimeDelayShutdownSwitch,
                                                        fanTimeDelayShutdownSwitch == 1);

                                                short fanDelaySwitchTime = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanDelaySwitchTime,
                                                        fanDelaySwitchTime);

                                                short fanCleaningPromptSwitch = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanCleaningPromptSwitch,
                                                        fanCleaningPromptSwitch == 1);

                                                short fanOpenRegularVentilation = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanOpenRegularVentilation,
                                                        fanOpenRegularVentilation == 1);

                                                short fanRegularVentilationIntervalTime = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanRegularVentilationIntervalTime,
                                                        fanRegularVentilationIntervalTime);

                                                short fanOpenWeeklyVentilation = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanOpenWeeklyVentilation,
                                                        fanOpenWeeklyVentilation == 1);

                                                short WeeklyVentilationDate_Week = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.WeeklyVentilationDate_Week,
                                                        WeeklyVentilationDate_Week);

                                                short WeeklyVentilationDate_Hour = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.WeeklyVentilationDate_Hour,
                                                        WeeklyVentilationDate_Hour);

                                                short WeeklyVentilationDate_Minute = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.WeeklyVentilationDate_Minute,
                                                        WeeklyVentilationDate_Minute);
                                                LogUtils.i("20180719", "fanAndStoveSwitchLinkageStatus:" + fanAndStoveSwitchLinkageStatus + " fanPowerSwitchLinkage:" + fanPowerSwitchLinkage
                                                        + " fanTimeDelayShutdownSwitch:" + fanTimeDelayShutdownSwitch + " fanDelaySwitchTime:" + fanDelaySwitchTime
                                                        + " fanCleaningPromptSwitch:" + fanCleaningPromptSwitch + " fanOpenRegularVentilation:" +
                                                        fanOpenRegularVentilation + " fanRegularVentilationIntervalTime:" + fanRegularVentilationIntervalTime +
                                                        " fanOpenWeeklyVentilation:" + fanOpenWeeklyVentilation + " WeeklyVentilationDate_Week:" + WeeklyVentilationDate_Week +
                                                        " WeeklyVentilationDate_Hour:" + WeeklyVentilationDate_Hour + " WeeklyVentilationDate_Minute:" + WeeklyVentilationDate_Minute);

                                            }
                                            break;

                                        case '2':

                                            if (fan_Length > 0) {
                                                short fanTimeForAwhileSwitch = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanTimeForAwhileSwitch,
                                                        fanTimeForAwhileSwitch);
                                                short fanTimeForAwhile = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.fanTimeForAwhile,
                                                        fanTimeForAwhile);

                                            }

                                            break;
                                    }


                                case 'B':
                                    break;
                                case 'C':

                                    break;

                                case 'D':


                                    break;
                            }

                            arg--;
                        }

                        numberCategory--;
                    }

                    break;
                case MsgKeys.readDeviceStatus_Rep:
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
                                case 'A'://烟机
                                    short fanHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                                    LogUtils.i("20180330", "fanHeader_Key:" + fanHeader_Key);
                                    short fan_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanHeader_Leng, fan_Length);
                                    LogUtils.i("20180330", " fan_Length:" + fan_Length);
                                    switch (fanHeader_Key) {

                                        case '1':

                                            if (fan_Length > 0) {
                                                short rikaFanWorkStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.rikaFanWorkStatus,
                                                        rikaFanWorkStatus);

                                                short rikaFanPower = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.rikaFanPower,
                                                        rikaFanPower);

                                                short rikaFanLight = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.rikaFanLight,
                                                        rikaFanLight);

                                                short rikaFanCleaningRemind = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.rikaFanCleaningRemind,
                                                        rikaFanCleaningRemind);

                                                LogUtils.i("20180330", "20180330" + "rikaFanWorkStatus:" + rikaFanWorkStatus
                                                        + " rikaFanPower:" + rikaFanPower + " rikaFanLight:" + rikaFanLight + " rikaFanCleaningRemind:"
                                                        + rikaFanCleaningRemind);
                                            }
                                            break;
                                        case '2':

                                            break;
                                        case '3':
//
                                            if (fan_Length > 0) {
                                                short waitTimeValue = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.waitTimeValue,
                                                        waitTimeValue);
                                                LogUtils.i("20180330", " waitTimeValue:" + waitTimeValue);
                                            }
                                            break;
                                        case '4':
                                            if (fan_Length > 0) {
                                                short damperLeft = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.damperLeft, damperLeft);
                                                short damperRight = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.damperRight, damperRight);

                                                LogUtils.i("20180330", " damperLeft:" + damperLeft + " damperRight:" + damperRight);
                                            }
                                            break;
                                        case '5':

                                            if (fan_Length > 0) {

                                                int min = MsgUtils.getShort(payload[offset++]);
                                                int hour = MsgUtils.getShort(payload[offset++]);
                                                int newMin = (short) (min + hour * 60);
                                                msg.putOpt(MsgParams.cleaningUseTime, newMin);
                                            }
                                            break;

                                        default:
                                            break;

                                    }

                                    break;

                                case 'B'://灶具
                                    short stoveHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Key, stoveHeader_Key);
                                    LogUtils.i("20180330", "stoveHeader_Key:" + stoveHeader_Key);
                                    short stoveHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveHeader_Leng, stoveHeader_Length);
                                    LogUtils.i("20180330", " stoveHeader_Length:" + stoveHeader_Length);
                                    switch (stoveHeader_Key) {

                                        case '1':
                                            if (stoveHeader_Length > 0) {
                                                short headNumber = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.headNumber, headNumber);
                                                short lockStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.lockStatus, lockStatus);

                                                LogUtils.i("20180330", " headNumber:" + headNumber + " lockStatus:" + lockStatus);
                                            }
                                            break;

                                        case '2':
                                            if (stoveHeader_Length > 0) {
                                                short stoveHeadLeftWorkStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadLeftWorkStatus, stoveHeadLeftWorkStatus);
                                                short stoveHeadLeftPower = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadLeftPower, stoveHeadLeftPower);
                                                short stoveHeadLeftAlarmStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadLeftAlarmStatus, stoveHeadLeftAlarmStatus);

                                                LogUtils.i("20180330", " stoveHeadLeftWorkStatus:" + stoveHeadLeftWorkStatus +
                                                        " stoveHeadLeftPower:" + stoveHeadLeftPower + " stoveHeadLeftAlarmStatus:" + stoveHeadLeftAlarmStatus);
                                            }
                                            break;
                                        case '3':
                                            if (stoveHeader_Length > 0) {
                                                short stoveHeadRightWorkStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadRightWorkStatus, stoveHeadRightWorkStatus);
                                                short stoveHeadRightPower = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadRightPower, stoveHeadRightPower);
                                                short stoveHeadRightAlarmStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.stoveHeadRightAlarmStatus, stoveHeadRightAlarmStatus);

                                                LogUtils.i("20180330", " stoveHeadRightWorkStatus:" + stoveHeadRightWorkStatus +
                                                        " stoveHeadRightPower:" + stoveHeadRightPower + " stoveHeadRightAlarmStatus:" + stoveHeadRightAlarmStatus);
                                            }

                                            break;
                                    }
//

                                    break;

                                case 'C'://消毒柜

                                    short sterilHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilHeader_Key, sterilHeader_Key);
                                    short sterilHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilHeader_Leng, sterilHeader_Length);
                                    switch (sterilHeader_Key) {

                                        case '1':
                                            if (sterilHeader_Length > 0) {
                                                msg.putOpt(MsgParams.sterilWorkStatus,
                                                        MsgUtils.getShort(payload[offset++]));
                                                msg.putOpt(MsgParams.sterilLockStatus,
                                                        MsgUtils.getShort(payload[offset++]));
                                                msg.putOpt(MsgParams.sterilWorkTimeLeft,
                                                        MsgUtils.getInt(payload,offset++));
                                                offset++;
                                                offset++;
                                                offset++;
                                                msg.putOpt(MsgParams.sterilDoorLockStatus,
                                                        MsgUtils.getShort(payload[offset++]));
                                                msg.putOpt(MsgParams.sterilAlarmStatus,
                                                        MsgUtils.getShort(payload[offset++]));
                                                msg.putOpt(MsgParams.sterilRemoveOilySoiled,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }

                                            break;
                                        case '6':
                                            if (sterilHeader_Length > 0) {
                                                msg.putOpt(MsgParams.sterilExhaustFanStatus,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }

                                    break;

                                case 'D'://蒸汽炉

                                    short steamHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamHeader_Key, steamHeader_Key);
                                    short steamHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamHeader_Leng, steamHeader_Length);

                                    switch (steamHeader_Key) {
                                        case '1':
                                            if (steamHeader_Length > 0) {
                                                short steamLockStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamLockStatus, steamLockStatus);


                                                short steamWorkStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamWorkStatus, steamWorkStatus);

                                                short steamAlarmStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamAlarmStatus, steamAlarmStatus);

                                                short steamRunModel = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamRunModel, steamRunModel);

                                                short steamWorkTemp = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamWorkTemp, steamWorkTemp);

                                                msg.putOpt(MsgParams.steamWorkRemainingTime,
                                                        MsgUtils.getShort(payload, offset++));
                                                offset++;

                                                short steamDoorState = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamDoorState, steamDoorState);

                                                short steamSetTemp = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamSetTemp, steamSetTemp);

                                                short steamSetTime = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamSetTime, steamSetTime);


                                                short steamLightState = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamLightState, steamLightState);

                                                short steamRemoveOilySoiled = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamRemoveOilySoiled, steamRemoveOilySoiled);

                                            }
                                            break;
                                        case '2':
                                            if (steamHeader_Length > 0) {
                                                short steamRecipeId = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamRecipeId, steamRecipeId);
                                                offset++;

                                                short steamRecipeStep = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamRecipeStep, steamRecipeStep);
                                                LogUtils.i("20180330", "steamRecipeId:" + steamRecipeId + " steamRecipeStep:" + steamRecipeStep);
                                            }

                                            break;
                                        case '3':
                                            if (steamHeader_Length > 0) {
                                                short steamWaterSwitchStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamWaterSwitchStatus, steamWaterSwitchStatus);
                                                LogUtils.i("20180330", "steamWaterSwitchStatus:" + steamWaterSwitchStatus);
                                            }
                                            break;
                                        case '4':
                                            if (steamHeader_Length > 0) {
                                                short steamWasteWaterExcessive = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamWasteWaterExcessive, steamWasteWaterExcessive);

                                                short steamWasteWaterStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamWasteWaterStatus, steamWasteWaterStatus);
                                                LogUtils.i("20180330", "steamWasteWaterExcessive:" + steamWasteWaterExcessive + " steamWasteWaterStatus:" + steamWasteWaterStatus);
                                            }
                                            break;
                                        case '5':
                                            if (steamHeader_Length > 0) {
                                                short steamExhaustFanStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamExhaustFanStatus, steamExhaustFanStatus);
                                                LogUtils.i("20180330", "steamExhaustFanStatus:" + steamExhaustFanStatus);

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
                                            short steamOvenLockStatus = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenLockStatus, steamOvenLockStatus);


                                            short steamOvenWorkStatus = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenWorkStatus, steamOvenWorkStatus);
                                            short steamOvenAlarmStatus = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenAlarmStatus, steamOvenAlarmStatus);

                                            short steamOvenRunModel = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenRunModel, steamOvenRunModel);

                                            short steamOvenWorkTemp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenWorkTemp, steamOvenWorkTemp);
                                            short remaining = MsgUtils.getShort(payload, offset++);
                                            msg.putOpt(MsgParams.steamOvenTimeWorkRemaining,
                                                    remaining);
                                            offset++;
                                            short steamOvenDoorState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenDoorState, steamOvenDoorState);

                                            short steamOvenSetTemp = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenSetTemp, steamOvenSetTemp);

                                            short steamOvenSetTime = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenSetTime, steamOvenSetTime);

                                            msg.putOpt(MsgParams.steamOvenOrderTime,
                                                    MsgUtils.getShort(payload, offset++));
                                            offset++;

                                            short steamOvenLightState = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenLight, steamOvenLightState);

                                            short steamOvenCookStep = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenTotalNumber, steamOvenCookStep);

                                            short steamOvenCurrentNumber = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenCurrentNumber, steamOvenCurrentNumber);

                                            short steamOvenAutomaticRecipe = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenAutomaticRecipe, steamOvenAutomaticRecipe);

                                            short steamOvenCleanOil = MsgUtils.getShort(payload[offset++]);
                                            msg.putOpt(MsgParams.steamOvenCleanOil, steamOvenCleanOil);
                                            break;
                                        case '2':
                                            if (steamOvenHeader_Length > 0) {
                                                short steamOvenRecipeId = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenRecipeId, steamOvenRecipeId);
                                                offset++;

                                                short steamOvenRecipeStep = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenRecipeStep, steamOvenRecipeStep);
                                            }
                                            break;
                                        case '4':
                                            if (steamOvenHeader_Length > 0) {
                                                short steamOvenAbolishWaterOverproof = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenAbolishWaterOverproof, steamOvenAbolishWaterOverproof);
                                                short steamOvenAbolishWaterRemind = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenAbolishWaterRemind, steamOvenAbolishWaterRemind);
                                                short steamOvenAbolishWaterTake = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenAbolishWaterTake, steamOvenAbolishWaterTake);
                                                short steamOvenAbolishWaterSwitchStatus = MsgUtils.getShort(payload[offset++]);
                                                msg.putOpt(MsgParams.steamOvenAbolishWaterSwitchStatus, steamOvenAbolishWaterSwitchStatus);
                                            }
                                            break;
                                    }
                                    break;
                                case 'F':

                                    break;
                            }

                            arg--;
                        }

                        numberOfCategory--;
                    }

                    break;

                case MsgKeys.alarmEventReport:
                    LogUtils.i("20180515", "alarmEventReport:" + MsgKeys.alarmEventReport);
                    short numberOfCategoryAlarm = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.numberOfCategory, numberOfCategoryAlarm);
                    while (numberOfCategoryAlarm > 0) {
                        short categoryCode = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.categoryCode, categoryCode);
                        short arg = MsgUtils.getShort(payload[offset++]);
                        msg.putOpt(MsgParams.ArgumentNumber, arg);
                        while (arg > 0) {
                            switch (categoryCode) {
                                case 'A'://烟机报警事件
                                    short fanAlarmHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanAlarmHeader_Key, fanAlarmHeader_Key);
                                    short fanAlarmHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.fanAlarmHeader_Leng, fanAlarmHeader_Length);

                                    switch (fanAlarmHeader_Key) {
                                        case '1':
                                            if (fanAlarmHeader_Length > 0) {
                                                msg.putOpt(MsgParams.fanAlarmCode,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }

                                    break;

                                case 'B'://灶具报警事件

                                    short stoveAlarmHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveAlarmHeader_Key, stoveAlarmHeader_Key);
                                    short stoveAlarmHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.stoveAlarmHeader_Leng, stoveAlarmHeader_Length);

                                    switch (stoveAlarmHeader_Key) {
                                        case '1':
                                            if (stoveAlarmHeader_Length > 0) {
                                                msg.putOpt(MsgParams.stoveAlarmCode,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }

                                    break;

                                case 'C'://消毒柜报警事件
                                    LogUtils.i("20180517", " 消毒柜报警事件");
                                    short sterilAlarmHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilAlarmHeader_Key, sterilAlarmHeader_Key);
                                    short sterilAlarmHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.sterilAlarmHeader_Leng, sterilAlarmHeader_Length);

                                    switch (sterilAlarmHeader_Key) {
                                        case '1':
                                            if (sterilAlarmHeader_Length > 0) {
                                                msg.putOpt(MsgParams.sterilAlarmCode,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }

                                    break;

                                case 'D'://蒸汽炉报警事件
                                    LogUtils.i("20180517", " 蒸汽炉报警事件");

                                    short steamAlarmHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamAlarmHeader_Key, steamAlarmHeader_Key);
                                    short steamAlarmHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamAlarmHeader_Leng, steamAlarmHeader_Length);
                                    switch (steamAlarmHeader_Key) {
                                        case '1':
                                            if (steamAlarmHeader_Length > 0) {
                                                msg.putOpt(MsgParams.steamAlarmCode,
                                                        MsgUtils.getShort(payload[offset++]));
                                            }
                                            break;
                                    }
                                    break;

                                case 'E':
                                    LogUtils.i("20180517", " 一体机报警事件");

                                    short steamOvenAlarmHeader_Key = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenAlarmHeader_Key, steamOvenAlarmHeader_Key);
                                    short steamOvenAlarmHeader_Length = MsgUtils.getShort(payload[offset++]);
                                    msg.putOpt(MsgParams.steamOvenAlarmHeader_Leng, steamOvenAlarmHeader_Length);
                                    switch (steamOvenAlarmHeader_Key) {
                                        case '1':
                                            if (steamOvenAlarmHeader_Length > 0) {
                                                msg.putOpt(MsgParams.steamOvenAlarmCode,
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


                        numberOfCategoryAlarm--;
                    }
                    break;

                case MsgKeys.setDeviceRunStatus_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.setDeviceIntelligentInteractiveModel_Rep:
                    msg.putOpt(MsgParams.RC,
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
                                            Log.e("eventReport", "stoveEventCode: "+stoveEventCode );
                                            short stoveEventArg = MsgUtils.getShort(payload[offset++]);
                                            Log.e("eventReport", "stoveEventArg: "+stoveEventCode );
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
                        arg --;
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
        }
    }
}
