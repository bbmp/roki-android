package com.robam.common.io.device.marshal;

import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;

import org.json.JSONException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by 14807 on 2018/5/17.
 */
public class CookerMsgMar {

    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        byte b;
        String str;
        switch (key) {
            case MsgKeys.deviceStatusQuery_Req:

                break;
            case MsgKeys.setDeviceInformationQuery_Req:

                break;
            case MsgKeys.setDeviceWorkStatus_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.cookerSwitchStatus);
                buf.put(b);

                break;
            case MsgKeys.setDeviceTemp_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.setCookerTemp);
                buf.put(b);

                break;
            case MsgKeys.setDeviceFire_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.cookerFire);
                buf.put(b);

                break;
            case MsgKeys.setDeviceShutdownWork_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.cookerTimingSwitch);
                buf.put(b);
                int cookerTimingTime = msg.optInt(MsgParams.cookerTimingTime);
                buf.put((byte) (cookerTimingTime & 0Xff));
                buf.put((byte) ((cookerTimingTime >> 8) & 0Xff));
                break;
            case MsgKeys.setDeviceRecipeWork_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                buf.put(ByteUtils.getBytes(msg.optInt(MsgParams.cookerRecipeCode), ByteOrder.LITTLE_ENDIAN));
                break;
            case MsgKeys.setDeviceSetInformation_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.cookerVoiceMode);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.cookerVoiceLevel);
                buf.put(b);
                break;
            case MsgKeys.setActionTempTure_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.cookerAction);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.tempTureAll);
                buf.put(b);
                break;

        }

    }

    public static void unmarshaller(int key, Msg msg, byte[] payload) throws JSONException {
        int offset = 0;
        LogUtils.i("20180609","key:"+key);
        switch (key) {
            case MsgKeys.deviceStatusQuery_Rep:
                msg.putOpt(MsgParams.cookerSwitchStatus,//开关状态
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerModel,//模式
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerFire,//当前火力
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.setCookerTemp,//设置温度
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerTemp,//当前温度
                        MsgUtils.getShort(payload,offset++));
                        offset++;

                msg.putOpt(MsgParams.cookerTimingSwitch,//定时使能
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerTimingTime,//定时时间
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerHeatingTime,//本次加热时间
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerCurrentAction,//当前动作
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipePerformStatus,//菜谱执行状态
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipeCode,//当前菜谱编号
                        MsgUtils.getInt(payload,offset++));
                offset++;
                offset++;
                offset++;

                msg.putOpt(MsgParams.cookerRecipeStepCode,//当前菜谱步骤编号
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipeStepRemainTime,//当前菜谱步骤剩余时间
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerRecipeCookingTotalTime,//菜谱烹饪总时长
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerAlarmCode,//报警故障代码
                        MsgUtils.getShort(payload[offset++]));

                //新增wifi固件版本号
                msg.putOpt(MsgParams.wifiVersion,MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceInformationQuery_Rep:
                msg.putOpt(MsgParams.cookerVoiceMode,
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerVoiceLevel,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceWorkStatus_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceTemp_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                LogUtils.i("20180609","rc:"+msg.toString());
                break;
            case MsgKeys.setDeviceFire_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceShutdownWork_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceRecipeWork_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setDeviceSetInformation_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.setActionTempTure_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.DeviceAlarm:
                msg.putOpt(MsgParams.cookerAlarmCode,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.DeviceWorkReport:
                msg.putOpt(MsgParams.paramEvent,MsgUtils.getShort(payload[offset++]));
                msg.putOpt(MsgParams.paramEventId,MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.ReportUpdateStatus:
                msg.putOpt(MsgParams.statusCode,MsgUtils.getShort(payload[offset++]));
                break;
        }

    }


}
