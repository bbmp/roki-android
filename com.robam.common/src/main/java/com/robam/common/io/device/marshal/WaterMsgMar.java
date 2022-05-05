package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.IRokiFamily;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class WaterMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        if (Utils.getDefaultWaterPurifier() != null && IRokiFamily.RJ312.equals(Utils.getDefaultWaterPurifier().getGuid().getDeviceTypeId())){
            switch (key) {
                case MsgKeys.GetWaterPurifiyStatus_Req:
                      /*  b = (byte) msg.optInt(MsgParams.TerminalType);
                        Log.i("water-statusreq", "key:" + key + " payload:" + Byte.toString(b));
                        buf.put(b);*/
                    break;
                case MsgKeys.SetWaterPurifiyWorking_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.WaterPurifiyStatus);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.WaterPurifierKettelCount);
                    buf.put(b);
                    break;
                default:
                    break;
            }
        }else{
            switch (key) {
                case MsgKeys.GetWaterPurifiyStatus_Req:
                       /* b = (byte) msg.optInt(MsgParams.TerminalType);
                        Log.i("water-statusreq", "key:" + key + " payload:" + Byte.toString(b));
                        buf.put(b);*/
                    break;
                case MsgKeys.SetWaterPurifiyWorking_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.WaterPurifiyStatus);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.WaterPurifierKettelCount);
                    buf.put(b);
                    break;

                case MsgKeys.SetWaterPurifiySmart_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    if (msg.optInt(MsgParams.ArgumentNumber) > 0){
                        b= (byte) msg.optInt(MsgParams.SetWaterPurifierSystemKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetWaterPurifierSystemLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetSetWaterPurifierSystemValue);
                        buf.put(b);
                        b= (byte) msg.optInt(MsgParams.setWaterPurifierPowerSavingKey);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetWaterPurifierPowerSavingLength);
                        buf.put(b);
                        b = (byte) msg.optInt(MsgParams.SetWaterPurifierPowerSavingValue);
                        buf.put(b);
                    }
                    break;
                case MsgKeys.getWaterPurifierStatusSmart_Req://读取净水器智能设定
                       /* b = (byte) msg.optInt(MsgParams.TerminalType);
                        buf.put(MsgUtils.toByte(b));*/
                    break;

                default:
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        if (Utils.getDefaultWaterPurifier() != null && IRokiFamily.RJ312.equals(Utils.getDefaultWaterPurifier().getGuid().getDeviceTypeId())){
            switch (key) {
                case MsgKeys.GetWaterPurifiyStatus_Rep://状态查询回应
                    msg.putOpt(MsgParams.WaterPurifiyStatus, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierAlarm, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterQualityBefore, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterQualityAfter, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterWorkTime, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterCleand, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilterStatus_pp, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_cto, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_ro1, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_ro2, MsgUtils.getShort(payload[offset++]));

                    msg.putOpt(MsgParams.WaterFilter_time_pp, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_cto, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_ro1, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_ro2, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterEveryDay, MsgUtils.getShort(payload, offset++));
                    break;
                case MsgKeys.GetWaterPurifiyFiliter_Rep://工作事件上报
                    msg.putOpt(MsgParams.WaterPurifierSwitch, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierClean, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierWash, MsgUtils.getShort(payload, offset++));
                    msg.putOpt(MsgParams.WaterPurifierFiliter, MsgUtils.getShort(payload, offset++));
                    msg.putOpt(MsgParams.WaterPurifierDayReport, MsgUtils.getShort(payload, offset++));
                    break;
                case MsgKeys.GetWaterPurifiyAlarm_Rep://报警事件上报
                    msg.putOpt(MsgParams.WaterPurifierAlarm, MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetWaterPurifiyWorking_Rep://制水回复
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                default:
                    break;
            }
        }else{
            switch (key) {
                case MsgKeys.GetWaterPurifiyStatus_Rep://状态查询回应
                    msg.putOpt(MsgParams.WaterPurifiyStatus, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierAlarm, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterQualityBefore, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterQualityAfter, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterWorkTime, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterCleand, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilterStatus_pp, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_cto, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_ro1, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterFilterStatus_ro2, MsgUtils.getShort(payload[offset++]));

                    msg.putOpt(MsgParams.WaterFilter_time_pp, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_cto, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_ro1, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterFilter_time_ro2, MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.WaterEveryDay, MsgUtils.getShort(payload, offset++));
                    offset++;
                    offset++;
                    offset++;
                    offset++;
                    offset++;
                    offset++;
                    short argumentQulity = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber,argumentQulity);
                    while(argumentQulity>0) {
                        short argument_key = MsgUtils.getShort(payload[offset++]);
                        switch (argument_key) {
                            case 1:
                                msg.putOpt(MsgParams.WaterCurrentQuilityKey, argument_key);
                                msg.putOpt(MsgParams.WaterCurrentQuilityLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.WaterCurrentQuilityValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argumentQulity--;
                    }
                    break;
                case MsgKeys.GetWaterPurifiyFiliter_Rep://工作事件上报
                    msg.putOpt(MsgParams.WaterPurifierSwitch, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierClean, MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.WaterPurifierWash, MsgUtils.getShort(payload, offset++));
                    msg.putOpt(MsgParams.WaterPurifierFiliter, MsgUtils.getShort(payload, offset++));
                    msg.putOpt(MsgParams.WaterPurifierDayReport, MsgUtils.getShort(payload, offset++));
                    break;
                case MsgKeys.GetWaterPurifiyAlarm_Rep://报警事件上报
                    msg.putOpt(MsgParams.WaterPurifierAlarm, MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.SetWaterPurifiyWorking_Rep://制水回复
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;

                case MsgKeys.getWaterPurifiySmart_Rep://智能设定回应
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getWaterPurifierStatusSmart_Rep://读取净水器智能设定回应
                    short argument = MsgUtils.getShort(payload[offset++]);
                    msg.putOpt(MsgParams.ArgumentNumber, argument);
                    while(argument>0){
                        short argument_key=MsgUtils.getShort(payload[offset++]);
                        switch (argument_key){
                            case 1:
                                msg.putOpt(MsgParams.SetWaterPurifierSystemKey,argument_key);
                                msg.putOpt(MsgParams.SetWaterPurifierSystemLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetSetWaterPurifierSystemValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            case 2:
                                msg.putOpt(MsgParams.setWaterPurifierPowerSavingKey,argument_key);
                                msg.putOpt(MsgParams.SetWaterPurifierPowerSavingLength,
                                        MsgUtils.getShort(payload[offset++]));
                                msg.putOpt(MsgParams.SetWaterPurifierPowerSavingValue,
                                        MsgUtils.getShort(payload[offset++]));
                                break;
                            default:
                                break;
                        }
                        argument--;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
