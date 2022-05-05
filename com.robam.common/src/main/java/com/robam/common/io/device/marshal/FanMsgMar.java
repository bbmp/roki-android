package com.robam.common.io.device.marshal;

import android.text.TextUtils;
import android.util.Log;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;
import org.json.JSONException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.robam.common.io.device.MsgParams.StoveShutDelay;


/**
 * Created by as on 2017-08-09.
 */

public class FanMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        switch (key) {

            case MsgKeys.GetFanStatus_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.SetFanStatus_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanStatus);
                buf.put(b);

                break;
            case MsgKeys.SetFanLevel_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                break;
            case MsgKeys.SetFanLight_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));

                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.FanLight);
                buf.put(bool ? (byte) 1 : (byte) 0);
                break;
            case MsgKeys.SetFanAllParams_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.FanLight);
                buf.put(bool ? (byte) 1 : (byte) 0);
                break;
            case MsgKeys.RestFanCleanTime_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                break;
            case MsgKeys.RestFanNetBoard_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                break;
            case MsgKeys.SetFanTimeWork_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //增加 userid
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());

                b = (byte) msg.optInt(MsgParams.FanLevel);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.FanTime);
                buf.put(b);
                break;
            case MsgKeys.GetSmartConfig_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                break;
            case MsgKeys.SetSmartConfig_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsPowerLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsLevelLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsShutdownLinkage);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.ShutdownDelay);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.IsNoticClean);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                bool = msg.optBoolean(MsgParams.IsTimingVentilation);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.TimingVentilationPeriod);
                buf.put(b);
                //
                bool = msg.optBoolean(MsgParams.IsWeeklyVentilation);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Week);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Hour);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.WeeklyVentilationDate_Minute);
                buf.put(b);
                //
                if (IRokiFamily._8230S.equals(msg.getDeviceGuid().getDeviceTypeId())
                        || IRokiFamily._8231S.equals(msg.getDeviceGuid().getDeviceTypeId())
                        ) {
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    //
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0) {
                        if (msg.optInt(MsgParams.Key) == 1) {//变频爆炒
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFrySwitch);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.R8230SFryTime);
                            buf.put(b);
                        }

                        if (msg.optInt(MsgParams.Key) == 2) {
                            b = (byte) msg.optInt(MsgParams.Key);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.Length);
                            buf.put(b);
                            b = (byte) msg.optInt(MsgParams.CupOilPower);
                            buf.put(b);
                        }
                    }
                }
                break;
            case MsgKeys.FanAddPot_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.FanDelPot_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                String sb = msg.optString(MsgParams.DeviceId);
                buf.put(sb.getBytes());

                break;

            case MsgKeys.SetFanCleanOirCupTime_Req:

                if (IRokiFamily._8231S.equals(msg.getDeviceGuid().getDeviceTypeId())) {
                    b = (byte) msg.optInt(MsgParams.TerminalType);
                    buf.put(MsgUtils.toByte(b));

                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                }

                break;

            case MsgKeys.SetFanStatusCompose_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                int arg = msg.optInt(MsgParams.ArgumentNumber);
                int FanStovePowerKey = msg.optInt(MsgParams.FanStovePowerKey);
                int FanPowerLinkKey = msg.optInt(MsgParams.FanPowerLinkKey);
                int StoveShutDelayKey = msg.optInt(MsgParams.StoveShutDelayKey);
                int StoveShutDelayTimeKey = msg.optInt(MsgParams.StoveShutDelayTimeKey);
                int TimeAirPowerKey = msg.optInt(MsgParams.TimeAirPowerKey);
                int AirTimePowerKey = msg.optInt(MsgParams.AirTimePowerKey);
                int FanFeelPowerKey = msg.optInt(MsgParams.FanFeelPowerKey);
                int FanCleanPowerKey = msg.optInt(MsgParams.FanCleanPowerKey);
                int FryStrongTimePowerKey = msg.optInt(MsgParams.FryStrongTimePowerKey);
                int CupOilPowerKey = msg.optInt(MsgParams.CupOilPowerKey);
                int ProtectTipDryPowerKey = msg.optInt(MsgParams.ProtectTipDryPowerKey);
                int ProtectDryPowerKey = msg.optInt(MsgParams.ProtectDryPowerKey);
                int GestureControlPowerKey = msg.optInt(MsgParams.GestureControlPowerKey);

                int OverTempProtectSwitchKey = msg.optInt(MsgParams.OverTempProtectSwitchKey);
                int OverTempProtectSetKey = msg.optInt(MsgParams.OverTempProtectSetKey);

                if (arg > 0) {

                    if (FanStovePowerKey == 1) {
                        b = (byte) msg.optInt(MsgParams.FanStovePowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanStovePowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanStovePower);
                        buf.put(b);
                    }

                    if (FanPowerLinkKey == 2) {
                        b = (byte) msg.optInt(MsgParams.FanPowerLinkKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanPowerLinkLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanPowerLink);
                        buf.put(b);
                    }

                    if (StoveShutDelayKey == 3) {
                        b = (byte) msg.optInt(MsgParams.StoveShutDelayKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.StoveShutDelayLength);
                        buf.put(b);
                        b = (byte) msg.optInt(StoveShutDelay);
                        buf.put(b);
                    }

                    if (StoveShutDelayTimeKey == 4) {
                        b = (byte) msg.optInt(MsgParams.StoveShutDelayTimeKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.StoveShutDelayTimeLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.StoveShutDelayTime);
                        buf.put(b);
                    }

                    if (FanCleanPowerKey == 5) {

                        b = (byte) msg.optInt(MsgParams.Key);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.Length);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanCleanPower);
                        buf.put(b);
                    }

                    if (TimeAirPowerKey == 6) {
                        b = (byte) msg.optInt(MsgParams.TimeAirPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.TimeAirPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.TimeAirPower);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.TimeAirPowerDay);
                        buf.put(b);
                    }

                    if (AirTimePowerKey == 7) {
                        b = (byte) msg.optInt(MsgParams.AirTimePowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.AirTimePowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.AirTimePower);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.AirTimeWeek);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.AirTimeHour);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.AirTimeMinute);
                        buf.put(b);
                    }

                    if (FryStrongTimePowerKey == 8) {
                        b = (byte) msg.optInt(MsgParams.FryStrongTimePowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FryStrongTimePowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FryStrongTimePower);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FryStrongTime);
                        buf.put(b);
                    }

                    if (CupOilPowerKey == 9) {
                        b = (byte) msg.optInt(MsgParams.CupOilPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.CupOilPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.CupOilPower);
                        buf.put(b);
                    }

                    if (FanFeelPowerKey == 10) {
                        b = (byte) msg.optInt(MsgParams.FanFeelPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanFeelPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.FanFeelPower);
                        buf.put(b);
                    }

                    if (ProtectTipDryPowerKey == 11) {

                        b = (byte) msg.optInt(MsgParams.ProtectTipDryPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.ProtectTipDryPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.ProtectTipDryPower);
                        buf.put(b);
                    }

                    if (ProtectDryPowerKey == 12) {

                        b = (byte) msg.optInt(MsgParams.ProtectDryPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.ProtectDryPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.ProtectDryPower);
                        buf.put(b);
                    }

                    if (GestureControlPowerKey == 13) {
                        b = (byte) msg.optInt(MsgParams.GestureControlPowerKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.GestureControlPowerLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.gesture);
                        buf.put(b);
                    }

                    //2020年3月24日 11:35:03 新增 8235S
                    if (OverTempProtectSwitchKey == 14) {
                        b = (byte) msg.optInt(MsgParams.OverTempProtectSwitchKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OverTempProtectSwitchLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OverTempProtectSwitch);
                        buf.put(b);

                    }
                    //谨记
                    if (OverTempProtectSetKey == 15) {
                        b = (byte) msg.optInt(MsgParams.OverTempProtectSetKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.OverTempProtectSetLength);
                        buf.put(b);
                        int overTempProtectSet = msg.optInt(MsgParams.OverTempProtectSet);
                        buf.put((byte) (overTempProtectSet & 0Xff));
                        buf.put((byte) ((overTempProtectSet >> 8) & 0Xff));

                    }
                }
                break;

            case MsgKeys.SetFanTimingRemind_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(MsgUtils.toByte(b));
                b = (byte) msg.optInt(MsgParams.TimeReminderSetSwitch);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.TimeReminderSetTime);
                buf.put(b);
                break;

            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        // 油烟机
        List<Byte> byteTemp = new ArrayList<>();
        for (int i = 0; i < payload.length; i++) {
            byteTemp.add(payload[i]);
        }
        LogUtils.i("20180524", "bt::" + byteTemp.toString());
        switch (key) {            // 8700/9700为通用烟机
            case MsgKeys.GetFanStatus_Rep:
                msg.putOpt(MsgParams.FanStatus,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.FanLevel,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.FanLight,
                        MsgUtils.getShort(payload[offset++]) == 1);
                msg.putOpt(MsgParams.NeedClean,
                        MsgUtils.getShort(payload[offset++]) == 1);

                short argumentLength = (short) (payload.length - offset);

                short aValue = MsgUtils.getShort(payload[offset]);
                LogUtils.i("20200506","aValue:::"+aValue);
                //argumentLength==19 表示是8235S
                if (argumentLength==19) {
                    msg.putOpt(MsgParams.FanTime, aValue);
                }else{
                    //如果为2 说明 没有返回定时时间 直接返回的是wifi
                    if (aValue == 2) {
                        LogUtils.i("20200506","FanWIfi:::"+aValue);
                        msg.putOpt(MsgParams.FanWIfi, aValue);

                    } else {
                        //不为2 说明有定时时间 先设置定时时间
                        msg.putOpt(MsgParams.FanTime, aValue);
                    }
                }

                ////如果为2 说明 没有返回定时时间 直接返回的是wifi
                //if (aValue == 2) {
                //    LogUtils.i("20200506","FanWIfi:::"+aValue);
                //    msg.putOpt(MsgParams.FanWIfi, aValue);
                //} else {
                //    //不为2 说明有定时时间 先设置定时时间
                //    msg.putOpt(MsgParams.FanTime, aValue);
                //}
                offset++;
                LogUtils.i("20200506","argumentLength:::"+argumentLength);
                //如果<=4
                if (argumentLength <= 4) {
                    //看看第三第四个值有没有 如果有（大于0） 设置wifi的值
                    short bValue = MsgUtils.getShort(payload[offset++]);
                    if (bValue > 0) {
                        msg.putOpt(MsgParams.FanWIfi, bValue);
                    }

                } else {
                    if (msg.isIncoming()) {
                        if (TextUtils.equals(msg.getSource().getDeviceTypeId(), "68A0S") || TextUtils.equals(msg.getSource().getDeviceTypeId(), "R68A0") || TextUtils.equals(msg.getSource().getDeviceTypeId(), "8235S")) {
                            setArgument(msg, payload, offset++, true);
                        } else {
                            setArgument(msg, payload, offset++, false);
                        }
                    }

                }


                break;
            case MsgKeys.SetFanStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanLevel_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanLight_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanAllParams_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.RestFanCleanTime_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.RestFanNetBoard_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanTimeWork_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            //读取状态
            case MsgKeys.GetSmartConfig_Rep:
                msg.putOpt(MsgParams.IsPowerLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsLevelLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsShutdownLinkage,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.ShutdownDelay,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsNoticClean,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsTimingVentilation,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.TimingVentilationPeriod,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IsWeeklyVentilation,
                        1 == MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Week,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Hour,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.WeeklyVentilationDate_Minute,
                        MsgUtils.getShort(payload[offset++]));
                short argument1 = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, argument1);
                //取可变参数值
                while (argument1 > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    switch (argument_key) {
                        case 1:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.R8230SFrySwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.R8230SFryTime,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 2:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.FanCupOilSwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 3:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.FanReducePower,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 4:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.gesture,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        //新增 2020年3月24日 11:23:05 8235S 过温保护
                        case 5:
                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.OverTempProtectSwitch,
                                    MsgUtils.getShort(payload[offset++]));
                            break;
                        case 6:

                            msg.putOpt(MsgParams.Key,
                                    argument_key);
                            msg.putOpt(MsgParams.Length,
                                    MsgUtils.getShort(payload[offset++]));

                            //msg.putOpt(MsgParams.OverTempProtectSet,
                            //        MsgUtils.getShort(payload[offset++]));
                            msg.putOpt(MsgParams.OverTempProtectSet,
                                    MsgUtils.getShort(payload, offset++));
                            offset++;


                            break;
                        default:
                            break;
                    }
                    argument1--;
                }
                break;
            case MsgKeys.SetSmartConfig_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;


            case MsgKeys.SetFanCleanOirCupTime_Rep://重置烟机倒油杯计回应
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;


            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.FanEvent_Noti:
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.FanAddPot_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.FanDelPot_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.SetFanStatusCompose_Rep:
                short num = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.ArgumentNumber, num);
                if (num > 0) {
                    short argument_key = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.Key,
                            argument_key);
                    msg.putOpt(MsgParams.RC, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.RcValue,
                            MsgUtils.getShort(payload[offset++]));
                }
                break;
            case MsgKeys.SetFanTimingRemind_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            default:
                break;
        }
    }

    private static void setArgument(Msg msg, byte[] payload, int offset, boolean wifiState) throws JSONException {
        int new_offset = offset;
        if (wifiState) {
            short wifiStatusValue = MsgUtils.getShort(payload[offset++]);
            msg.putOpt(MsgParams.FanWIfi,wifiStatusValue);
            wifiState = false;
        }
        short argument = MsgUtils.getShort(payload[offset++]);
        msg.putOpt(MsgParams.ArgumentNumber, argument);

        //取可变参数值
        while (argument > 0) {
            short argument_key = MsgUtils.getShort(payload[offset++]);
            short argument_length = MsgUtils.getShort(payload[offset++]);

            //15是预先设置的一个最大值 超出这个值之后
            //5916s走
            if (argument_length >= 15 || argument_length > payload.length - offset) {
                LogUtils.i("20200507",argument_length+"");
                wifiState = true;
                break;
            }
            switch (argument_key) {
                case 1:
                    msg.putOpt(MsgParams.BackSmoke,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case 2:
                    msg.putOpt(MsgParams.waitTime,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case 3:
                    msg.putOpt(MsgParams.aerialDetection,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case 4:
                    msg.putOpt(MsgParams.oilCup,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case 5:
                    short fanFellStatus = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.smartSmokeStatus, fanFellStatus);
                    break;
                case 6:

                    msg.putOpt(MsgParams.TemperatureReportOne,
                            MsgUtils.getInt(payload, offset++));
                    offset++;
                    offset++;
                    offset++;

                    msg.putOpt(MsgParams.TemperatureReportTwo,
                            MsgUtils.getInt(payload, offset++));
                    offset++;
                    offset++;
                    offset++;

                    break;
                case 7://待写，取每一个bit的值进行判断是否是报警
                    short BraiseAlarm = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.BraiseAlarm, BraiseAlarm);
                    break;
                case 8:
                    msg.putOpt(MsgParams.RegularVentilationRemainingTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    break;

                case 9:
                    msg.putOpt(MsgParams.FanStoveLinkageVentilationRemainingTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    break;
                case 10:
                    msg.putOpt(MsgParams.PeriodicallyRemindTheRemainingTime,
                            MsgUtils.getShort(payload[offset++]));

                    //msg.putOpt(MsgParams.FanTime,
                    //        MsgUtils.getShort(payload[offset++]));
                    break;
                case 11:
                    msg.putOpt(MsgParams.PresTurnOffRemainingTime,
                            MsgUtils.getShort(payload, offset++));

                    offset++;
                    break;
                // 2020年3月24日 11:28:26 新增 8235S
                case 12:
                    msg.putOpt(MsgParams.OverTempProtectStatus,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                default:
                    break;
            }
            argument--;
        }

        if ((payload.length - offset) > 0) {
            LogUtils.i("20200507","----------");
            wifiState = true;
        }

        if (wifiState) {
            setArgument(msg, payload, new_offset, true);
        }

    }
}
