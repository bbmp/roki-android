package com.robam.common.io.device.marshal;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.utils.ByteUtils;
import com.legent.utils.JsonUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.device.Pot.Pot;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by as on 2017-08-09.
 */

public class PotMsgMar {
    public static void marshaller(int key, Msg msg, ByteBuffer buf) throws Exception {
        boolean bool;
        byte b;
        String str;
        short s;
        switch (key) {
            case MsgKeys.GetPotTemp_Req:
                b = (byte) msg.optInt(MsgParams.TerminalType);
                buf.put(b);
                break;
            case MsgKeys.SetPotCom_Req:
                str = msg.optString(MsgParams.UserId);
                buf.put(str.getBytes());
                b = (byte) msg.optInt(MsgParams.potBurningWarnSwitch);
                buf.put(b);
                break;
            case MsgKeys.SetPotSwitch_Req:

                break;
            case MsgKeys.POT_INTERACTION_Req:
                String strMsg = msg.getString(MsgParams.interactionData);
                List<Pot.Interaction> interactions = JsonUtils.json2List(strMsg, Pot.Interaction.class);
                //设置数据个数
                buf.put((byte) interactions.size());
                //设置数据
                for (Pot.Interaction interaction : interactions) {
                    buf.put((byte) interaction.key);
                    buf.put((byte) interaction.length);
                    for (int i : interaction.value) {
                        buf.put((byte) i);
                    }
                }
                break;
            case MsgKeys.POT_P_MENU_Req:
                b = (byte) msg.optInt(MsgParams.Pot_P_Number);
                buf.put(b);
                b = (byte) msg.optInt(MsgParams.HeadId);
                buf.put(b);

                strMsg = msg.getString(MsgParams.interactionData);
                interactions = JsonUtils.json2List(strMsg, Pot.Interaction.class);
                //设置数据个数
                buf.put((byte) interactions.size());
                //设置数据
                for (Pot.Interaction interaction : interactions) {
                    buf.put((byte) interaction.key);
                    buf.put((byte) interaction.length);
                    for (int i : interaction.value) {
                        buf.put((byte) i);
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
            case MsgKeys.SetPotTemp_Rep:
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset++));
                offset++;
                offset++;
                offset++;
                msg.putOpt(MsgParams.Pot_status,
                        MsgUtils.getShort(payload[offset++]));

                //参数个数
                short count = MsgUtils.getShort(payload[offset++]);
                while (count >= 0) {
                    short valueKey = MsgUtils.getShort(payload[offset++]);
                    short valueLength = MsgUtils.getShort(payload[offset++]);
                    switch (valueKey) {
                        case 1: //无人锅电机模式
                            short Pot_ESPMode = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_ESPMode, Pot_ESPMode);
                            break;
                        case 2: //无人锅锅盖状态
                            short Pot_LisState = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_LisState, Pot_LisState);
                            break;
                        case 3: //P档菜谱值
                            short Pot_PMenuValue = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_PMenuValue, Pot_PMenuValue);
                            break;
                        case 4: //无人锅平台菜谱/曲线还原模式值  len 4
                            short Pot_PlatformMenuValue = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_PlatformMenuValue, Pot_PlatformMenuValue);
                            offset++;
                            offset++;
                            offset++;
                            break;
                        case 5: //无人锅电量
                            short Pot_ElectricValue = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_ElectricValue, Pot_ElectricValue);
                            break;
                        case 6: //无人锅模式状态
                            short Pot_ModelState = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_ModelState, Pot_ModelState);
                            break;
                        case 7: //无人锅本地记录状态
                            short Pot_LocalRecordState = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_LocalRecordState, Pot_LocalRecordState);
                            break;
                        case 8: //菜谱/曲线还原运行秒数
                            short Pot_MenuRestoreSecond = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_MenuRestoreSecond, Pot_MenuRestoreSecond);
                            offset++;
                            break;
                        case 9: //无人锅绑定炉头
                            short Pot_BindHead = MsgUtils.getShort(payload[offset++]);
                            msg.putOpt(MsgParams.Pot_BindHead, Pot_BindHead);
                            offset++;
                            break;
                        default:
                            offset += valueLength;
                            break;
                    }
                    count--;
                }

                break;
            case MsgKeys.ActiveTemp_Rep:
                msg.putOpt(MsgParams.Pot_Temp,
                        MsgUtils.getFloat(payload, offset++));
                msg.putOpt(MsgParams.Pot_IsDot,
                        MsgUtils.getFloat(payload, offset++));
                break;
            case MsgKeys.PotKey_Report:
                msg.putOpt(MsgParams.Pot_keybood,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.PotAlarm_Report:
                msg.putOpt(MsgParams.AlarmId,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.GetPotCom_Rep:
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;

            case MsgKeys.GetPotSwitch_Rep:
                msg.putOpt(MsgParams.potBurningWarnSwitch,
                        MsgUtils.getShort(payload[offset++]));
                break;
            case MsgKeys.POT_INTERACTION_Rep://154
                msg.putOpt(MsgParams.RC,
                        MsgUtils.getShort(payload[offset++]));
                break;
            default:
                break;
        }
    }
}
