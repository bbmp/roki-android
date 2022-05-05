package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import java.nio.ByteBuffer;

/**
 * Created by as on 2017-08-09.
 */

public class MicroMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        if (msg!=null&&msg.getTopic().contains("RM509")) {
            switch (key) {
                case MsgKeys.setMicroWaveStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveStatus);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveKindsAndHeatCold_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveMode);
                    buf.put(b);
                    int value = msg.optInt(MsgParams.MicroWaveWeight);
                    /*byte[] targets = new byte[4];
                    targets[0] = (byte) (value & 0xff);// 最低位
                    targets[1] = (byte) ((value >> 8) & 0xff);// 次低位
                    targets[2] = (byte) ((value >> 16) & 0xff);// 次高位
                    targets[3] = (byte) (value >>> 24);// 最高位,无符号右移。*/
                    b = (byte) (value & 0xFF);//rent 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((value >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveProModeHeat_Req://140设置微波炉专业模式加热
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.MicroWaveMode);
                    buf.put(b);
                    int time = msg.optInt(MsgParams.MicroWaveTime);
                    b = (byte) (time & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWavePower);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveLinkedCook_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveStepState);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode1);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode2);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode3);
                    buf.put(b);
                    int time1 = msg.optInt(MsgParams.MicroWaveLinkTime1);
                    b = (byte) (time1 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time1 >> 8) & 0xFF);
                    buf.put(b);
                    int time2 = msg.optInt(MsgParams.MicroWaveLinkTime2);
                    b = (byte) (time2 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time2 >> 8) & 0xFF);
                    buf.put(b);
                    int time3 = msg.optInt(MsgParams.MicroWaveLinkTime3);
                    b = (byte) (time3 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time3 >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower1);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower2);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower3);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveLight_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveLight);
                    buf.put(b);
                    break;

                case MsgKeys.getMicroWaveStatus_Req://微波炉状态查询
                    break;
            }
        } else {//526
               /* LogUtils.i("20170531","526");
                LogUtils.i("20170531", " key " + key);*/
            switch (key) {
                case MsgKeys.setMicroWaveStatus_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveStatus);
                    buf.put(b);
                    break;
                //新增去味指令
                case MsgKeys.setMicroWaveClean_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveMode);
                    buf.put(b);
                    int microTime = msg.optInt(MsgParams.MicroWaveTime);
                    b = (byte) (microTime & 0xFF);
                    buf.put(b);
                    b = (byte) ((microTime >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWavePower);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);

                    byte[] data = new byte[buf.position()];
                    System.arraycopy(buf.array(), 0, data, 0, data.length);
                    break;
                case MsgKeys.setMicroWaveKindsAndHeatCold_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveMode);
                    buf.put(b);
                    int value = msg.optInt(MsgParams.MicroWaveWeight);
                    /*byte[] targets = new byte[4];
                    targets[0] = (byte) (value & 0xff);// 最低位
                    targets[1] = (byte) ((value >> 8) & 0xff);// 次低位
                    targets[2] = (byte) ((value >> 16) & 0xff);// 次高位
                    targets[3] = (byte) (value >>> 24);// 最高位,无符号右移。*/
                    b = (byte) (value & 0xFF);//rent 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((value >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveProModeHeat_Req://140设置微波炉专业模式加热
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());

                    b = (byte) msg.optInt(MsgParams.MicroWaveMode);
                    buf.put(b);
                    int time = msg.optInt(MsgParams.MicroWaveTime);
                    b = (byte) (time & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWavePower);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    /////////新增
                    int microRecipeId = msg.optInt(MsgParams.MicroWaveRecipe);
                    b = (byte) (microRecipeId & 0xFF);
                    buf.put(b);
                    b = (byte) ((microRecipeId >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroRecipeStep);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.ArgumentNumber);
                    buf.put(b);
                    ////////新增
                    break;
                case MsgKeys.setMicroWaveLinkedCook_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveStepState);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode1);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode2);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkdMode3);
                    buf.put(b);
                    int time1 = msg.optInt(MsgParams.MicroWaveLinkTime1);
                    b = (byte) (time1 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time1 >> 8) & 0xFF);
                    buf.put(b);
                    int time2 = msg.optInt(MsgParams.MicroWaveLinkTime2);
                    b = (byte) (time2 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time2 >> 8) & 0xFF);
                    buf.put(b);
                    int time3 = msg.optInt(MsgParams.MicroWaveLinkTime3);
                    b = (byte) (time3 & 0xFF);//rent add 低位在前，高位在后
                    buf.put(b);
                    b = (byte) ((time3 >> 8) & 0xFF);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower1);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower2);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveLinkPower3);
                    buf.put(b);
                    b = (byte) msg.optInt(MsgParams.MicroWaveRestartNow);
                    buf.put(b);
                    break;
                case MsgKeys.setMicroWaveLight_Req:
                    str = msg.optString(MsgParams.UserId);
                    buf.put(str.getBytes());
                    b = (byte) msg.optInt(MsgParams.MicroWaveLight);
                    buf.put(b);
                    break;

                case MsgKeys.getMicroWaveStatus_Req://微波炉状态查询
                    break;
            }
        }
    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws Exception {
        int offset = 0;
        if (msg!=null&&msg.getTopic().contains("RM509")) {
            switch (key) {
                case MsgKeys.setMicroWaveStates_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveKindsAndHeatCold_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveProModeHeat_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveLight_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getMicroWaveStatus_Rep://状态查询回应
                    msg.putOpt(MsgParams.MicroWaveStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWavePower,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveWeight,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveStepState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveSettime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveError,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.MicroWave_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset, 10));
                    break;
                case MsgKeys.getMicroWaveAlarm_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
            }
        } else {//微波炉526指令
            switch (key) {
                case MsgKeys.setMicroWaveStates_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveClean_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveKindsAndHeatCold_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveProModeHeat_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.setMicroWaveLight_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.getMicroWaveStatus_Rep://状态查询回应
                    msg.putOpt(MsgParams.MicroWaveStatus,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveLight,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveMode,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWavePower,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveWeight,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveTime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveDoorState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveStepState,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.MicroWaveSettime,
                            MsgUtils.getShort(payload, offset++));
                    offset++;
                    msg.putOpt(MsgParams.MicroWaveError,
                            MsgUtils.getShort(payload[offset++]));
                    break;
                case MsgKeys.MicroWave_Noti:
                    msg.putOpt(MsgParams.EventId,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.EventParam,
                            MsgUtils.getShort(payload[offset++]));
                    msg.putOpt(MsgParams.UserId,
                            MsgUtils.getString(payload, offset, 10));
                    break;
                case MsgKeys.getMicroWaveAlarm_Rep:
                    msg.putOpt(MsgParams.RC,
                            MsgUtils.getShort(payload[offset++]));
                    break;
            }
        }
    }
}
