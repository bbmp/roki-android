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
                msg.putOpt(MsgParams.cookerSwitchStatus,//????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerModel,//??????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerFire,//????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.setCookerTemp,//????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerTemp,//????????????
                        MsgUtils.getShort(payload,offset++));
                        offset++;

                msg.putOpt(MsgParams.cookerTimingSwitch,//????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerTimingTime,//????????????
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerHeatingTime,//??????????????????
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerCurrentAction,//????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipePerformStatus,//??????????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipeCode,//??????????????????
                        MsgUtils.getInt(payload,offset++));
                offset++;
                offset++;
                offset++;

                msg.putOpt(MsgParams.cookerRecipeStepCode,//????????????????????????
                        MsgUtils.getShort(payload[offset++]));

                msg.putOpt(MsgParams.cookerRecipeStepRemainTime,//??????????????????????????????
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerRecipeCookingTotalTime,//?????????????????????
                        MsgUtils.getShort(payload,offset++));
                offset++;

                msg.putOpt(MsgParams.cookerAlarmCode,//??????????????????
                        MsgUtils.getShort(payload[offset++]));

                //??????wifi???????????????
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
