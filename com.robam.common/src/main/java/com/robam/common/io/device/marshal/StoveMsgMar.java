package com.robam.common.io.device.marshal;

import com.google.common.collect.Lists;
import com.j256.ormlite.stmt.query.In;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.Stove.Stove;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class StoveMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;

        //组装
        // 电磁灶
        switch (key) {
            case MsgKeys.GetStoveStatus_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                break;
            case MsgKeys.SetStoveStatus_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhStatus);
                buf.put(b);

                break;
            case MsgKeys.SetStoveLevel_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                //
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                //
                bool = msg.optBoolean(MsgParams.IsCook);
                buf.put(bool ? (byte) 1 : (byte) 0);
                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);
                //
                b = (byte) msg.optInt(MsgParams.IhLevel);
                buf.put(b);
                break;
            case MsgKeys.SetStoveShutdown_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                b = (byte) msg.optInt(MsgParams.IhId);
                buf.put(b);

                //
                s = (short) msg.optInt(MsgParams.IhTime);
                buf.putShort(s);
                break;
            case MsgKeys.SetStoveLock_Req:
                //
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);

                //
                bool = msg.optBoolean(MsgParams.StoveLock);
                buf.put(bool ? (byte) 1 : (byte) 0);

                break;

            case MsgKeys.SetPowerOn_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b= (byte) msg.optInt(MsgParams.AutoPowerOff);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;

            case MsgKeys.setTimePowerOff_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.StoveLevel);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.AutoPowerOffTime);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                break;


            /**
             * //  msg.putOpt(MsgParams.AutoTemporaryKey, i);
             *     msg.putOpt(MsgParams.AutoTemporaryLength, 6);
             *     msg.putOpt(MsgParams.AutoTemporaryControlWay, controlWay);
             *     msg.putOpt(MsgParams.AutoTemporaryControlTap, tap);
             *     msg.putOpt(MsgParams.AutoTemporaryControlTemp, temp);
             *     msg.putOpt(MsgParams.AutoTemporaryControlTime, time);
             */
            case MsgKeys.setAutoTemporaryStep_Look_Rep:
                b= (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.headOrderNumber);
                buf.put(b);
                b= (byte) msg.optInt(MsgParams.ArgumentNumber);
                buf.put(b);
                 ArrayList<Short> controlWay= (ArrayList<Short>) msg.opt(MsgParams.AutoTemporaryControlWay);
                ArrayList<Short> tap = (ArrayList<Short>) msg.opt(MsgParams.AutoTemporaryControlTap);
                ArrayList<Integer> temp = (ArrayList<Integer>) msg.opt(MsgParams.AutoTemporaryControlTemp);
                ArrayList<Integer> time = (ArrayList<Integer>) msg.opt(MsgParams.AutoTemporaryControlTime);
                for (int i=0;i<controlWay.size();++i){
                    b= (byte) (i+1);
                    buf.put(b);
                    b= 6;
                    buf.put(b);
                    b= controlWay.get(i).byteValue();
                    buf.put(b);
                    b= tap.get(i).byteValue();
                    buf.put(b);


                    final short lowTemp = temp.get(i) > 255 ? (short) (temp.get(i).intValue() & 0Xff):(short)(temp.get(i)).intValue();
                    buf.put((byte)lowTemp);
                    if (temp.get(i)>255){
                        short highTemp = (short) ((temp.get(i) >> 8) & 0Xff);
                        buf.put((byte)highTemp);
                    }else{
                        buf.put((byte) 0);
                    }
//                    b= temp.get(i).byteValue();
//                    buf.put(b);



                    final short lowTime = time.get(i) > 255 ? (short) (time.get(i).intValue() & 0Xff):(short)(time.get(i)).intValue();
                    buf.put((byte)lowTime);
                    if (temp.get(i)>255){
                        short highTime = (short) ((time.get(i) >> 8) & 0Xff);
                        buf.put((byte)highTime);
                    }else{
                        buf.put((byte) 0);
                    }

                }
                break;
            case MsgKeys.setPowerOff_Look_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;


            case MsgKeys.setAutoTemporarySetting_Look_Rep:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.headOrderNumber);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.ControlInstruction);
                buf.put(b);

                break;
            default:
                break;
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        // 电磁灶
        switch (key) {
            case MsgKeys.setAutoTemporarySetting_Look_Res:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setAutoTemporaryStep_Look_Res:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));

                break;

            case MsgKeys.GetStoveStatus_Rep:
                int num = MsgUtils.getShort(payload[offset++]);
                msg.putOpt(MsgParams.IhNum, num);
                msg.putOpt(MsgParams.StoveLock,
                        MsgUtils.getShort(payload[offset++]) == 1);

                List<Stove.StoveHead> list = Lists.newArrayList();
                for (short i = 0; i < num; i++) {
                    Stove.StoveHead head = new Stove.StoveHead(i);
                    head.status = MsgUtils.getShort(payload[offset++]);
                    head.level = MsgUtils.getShort(payload[offset++]);
                    head.time = MsgUtils.getShort(payload, offset);
                    offset += 2;
                    head.alarmId = MsgUtils.getShort(payload[offset++]);
                    list.add(head);
                }
                //params长度
                short paramsLen = MsgUtils.getShort(payload[offset++]);
                //9w851工作时间
                for (short i = 0; i < paramsLen; i++) {
                    short paramsKey = MsgUtils.getShort(payload[offset++]);
                    short valueLen;
                    short workTime;
                    if (paramsKey == 65) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        offset += valueLen;
                    } else if (paramsKey == 66) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        offset += valueLen;
                    } else if (paramsKey == 67) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        list.get(0).workTime = MsgUtils.getShort(payload, offset++);
                        offset += 1;
                    } else if (paramsKey == 68) {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        list.get(0).workTime = MsgUtils.getShort(payload, offset++);
                        offset += 1;
                    }else if (paramsKey==69){
                        valueLen = MsgUtils.getShort(payload[offset++]);
//                        short leftTemp = MsgUtils.getShort(payload[offset++]);
//                        msg.putOpt(MsgParams.LeftTemp,
//                                leftTemp);

                        msg.putOpt(MsgParams.LeftTemp,
                                MsgUtils.getFloat(payload, offset++));
                        offset += 3;
                    }else if (paramsKey==70){
                        valueLen = MsgUtils.getShort(payload[offset++]);
//                        short rightTemp = MsgUtils.getShort(payload[offset++]);
//                        msg.putOpt(MsgParams.RightTemp,
//                                rightTemp);
                        msg.putOpt(MsgParams.RightTemp,
                                MsgUtils.getFloat(payload, offset++));
                        offset += 3;
                    }
                    else {
                        valueLen = MsgUtils.getShort(payload[offset++]);
                        offset += valueLen;
                    }
                }
                msg.putOpt(MsgParams.StoveHeadList, list);
                break;
            case MsgKeys.SetStoveStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLevel_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveShutdown_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));

                break;
            case MsgKeys.SetStoveLock_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.StoveAlarm_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveEvent_Noti:
                msg.putOpt(MsgParams.IhId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventId,
                        MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.EventParam,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.StoveTemp_Noti:        //灶具温度参数上报
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset));
                break;
            case MsgKeys.SetPowerOn_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setTimePowerOff_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setPowerOff_Look_Rep:
                msg.putOpt(MsgParams.AutoPowerOff,MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.AutoPowerOffTimeForOne,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForTwo,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForThree,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForFour,MsgUtils.getShort(payload,offset++));
                offset++;
                msg.putOpt(MsgParams.AutoPowerOffTimeForFive,MsgUtils.getShort(payload,offset++));
                offset++;
                break;


            default:
                break;
        }
    }
}
