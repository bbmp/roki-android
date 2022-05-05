package com.robam.common.pojos.device.WaterPurifier;

import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.WaterPurifiyAlarmEvent;
import com.robam.common.events.WaterPurifiyFiliterEvent;
import com.robam.common.events.WaterPurifiyStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.SmartParamsWaterPurifier;

import java.io.Serializable;

import static com.robam.common.io.device.MsgParams.SetSetWaterPurifierSystemValue;

/**
 * Created by Rent on 16/05/25.
 */
public class AbsWaterPurifier extends AbsDeviceHub implements IWaterPurifier ,Serializable{
    public short preStatus = WaterPurifierStatus.None;//之前状态
    public short status = WaterPurifierStatus.None;//默认关闭状态
    public short model = WaterPurifierModel.None;//默认模式 一壶
    public short alarm = WaterPurifierAlarm.Kettel_Ok;//默认水壶正常
    public short work_time;//已经工作时间
    public short water_cleaend;//已经净化的水
    public short filter_state_pp;//滤芯状态 pp
    public short filter_state_cto;//滤芯状态 cto
    public short filter_state_ro1;//滤芯状态 ro1
    public short filter_state_ro2;//滤芯状态 ro2
    public short amount_water_every_day;//每日饮水量
    public short input_tds;//原水质
    public short output_tds;//净化后的水质(纯水水质)
    protected short terminalType = TerminalType.getType();
    public short count;
    public short filter_time_pp;//滤芯剩余时间
    public short filter_time_cto;
    public short filter_time_ro1;
    public short filter_time_ro2;
    public short argument;//参数个数
    public short argumentQulity;
    public short waterCleanQulity;
    public SmartParamsWaterPurifier smartParams = new SmartParamsWaterPurifier();

    public AbsWaterPurifier(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public String getWaterModel() {
        return null;
    }


    @Override
    public void onPolling() {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.GetWaterPurifiyStatus_Req);
            // reqMsg.putOpt(MsgParams.TerminalType, terminalType);   // 控制端类型区分
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        try {
            int key = msg.getID();
            Log.i("water-key-onReceivedMsg", key + "");
            switch (key) {
                case MsgKeys.GetWaterPurifiyStatus_Rep://状态查询回应
                    AbsWaterPurifier.this.preStatus = AbsWaterPurifier.this.status;
                    AbsWaterPurifier.this.status = (short) msg.optInt(MsgParams.WaterPurifiyStatus);
                    AbsWaterPurifier.this.alarm = (short) msg.optInt(MsgParams.WaterPurifierAlarm);
                    AbsWaterPurifier.this.input_tds = (short) msg.optInt(MsgParams.WaterQualityBefore);
                    AbsWaterPurifier.this.output_tds = (short) msg.optInt(MsgParams.WaterQualityAfter);
                    AbsWaterPurifier.this.work_time = (short) msg.optInt(MsgParams.WaterWorkTime);
                    AbsWaterPurifier.this.water_cleaend = (short) msg.optInt(MsgParams.WaterCleand);
                    AbsWaterPurifier.this.filter_state_pp = (short) msg.optInt(MsgParams.WaterFilterStatus_pp);
                    AbsWaterPurifier.this.filter_state_cto = (short) msg.optInt(MsgParams.WaterFilterStatus_cto);
                    AbsWaterPurifier.this.filter_state_ro1 = (short) msg.optInt(MsgParams.WaterFilterStatus_ro1);
                    AbsWaterPurifier.this.filter_state_ro2 = (short) msg.optInt(MsgParams.WaterFilterStatus_ro2);
                    //滤芯剩余时间轮询过来的值
                    AbsWaterPurifier.this.filter_time_pp = (short) msg.optInt(MsgParams.WaterFilter_time_pp);
                    AbsWaterPurifier.this.filter_time_cto = (short) msg.optInt(MsgParams.WaterFilter_time_cto);
                    AbsWaterPurifier.this.filter_time_ro1 = (short) msg.optInt(MsgParams.WaterFilter_time_ro1);
                    AbsWaterPurifier.this.filter_time_ro2 = (short) msg.optInt(MsgParams.WaterFilter_time_ro2);

                    AbsWaterPurifier.this.amount_water_every_day = (short) msg.optInt(MsgParams.WaterEveryDay);
                    //制水水质
                    AbsWaterPurifier.this.argumentQulity = (short) msg.optInt(MsgParams.ArgumentNumber);
                    if (AbsWaterPurifier.this.argumentQulity > 0) {
                        waterCleanQulity = (short) msg.optInt(MsgParams.WaterCurrentQuilityValue);
                        LogUtils.i("20170706", " waterCleanQulity:::" + waterCleanQulity);
                    }
                    onStatusChanged();
                    break;
                case MsgKeys.GetWaterPurifiyFiliter_Rep://滤芯到期
                    short filiter = (short) msg.optInt(MsgParams.WaterPurifierFiliter);
                    LogUtils.i("20181121", "filiter:" + filiter);
                    postEvent(new WaterPurifiyFiliterEvent(AbsWaterPurifier.this, filiter));
                    break;
                case MsgKeys.GetWaterPurifiyAlarm_Rep://报警
                    short alarmId = (short) msg.optInt(MsgParams.WaterPurifierAlarm);
                    LogUtils.i("20181121", "alarmid:" + alarmId);
                    postEvent(new WaterPurifiyAlarmEvent(AbsWaterPurifier.this, alarmId));
                case MsgKeys.SetWaterPurifiyWorking_Rep://制水回复
                    Log.i("water-setPur", "rep:" + msg.optInt(MsgParams.RC));
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isHardIsConnected() {
        return false;
    }


    @Override
    public void onStatusChanged() {
        if (Plat.LOG_FILE_ENABLE) {
            LogUtils.logFIleWithTime(String.format("Oven onStatusChanged. isConnected:%s level:%s", isConnected, status));
        }
        Log.i("water-cg", " DT:" + this.getDt() + " :preStatus:" + preStatus + " status:" + status + " alarm:" + alarm + " input_tds:" + input_tds + " output_tds:" + output_tds +
                " work_time:" + work_time + " water_cleaend:" + water_cleaend + " pp:" + filter_state_pp + " cto:" + filter_state_cto + " ro1" + filter_state_ro1
                + " ro2" + filter_state_ro2 + " filter_time_pp:" + filter_time_pp + " filter_time_cto:" + filter_time_cto + " filter_time_ro1:" + filter_time_ro1 + " filter_time_ro2:" + filter_time_ro2 + "waterCleanQulity:" +
                waterCleanQulity
        );
        postEvent(new WaterPurifiyStatusChangedEvent(AbsWaterPurifier.this));
    }

    /**
     * 设置开始制水
     *
     * @param
     * @param callback
     */
    public void setWaterPurifier(final short kettelCount, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetWaterPurifiyWorking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.WaterPurifiyStatus, 3);
            msg.putOpt(MsgParams.WaterPurifierKettelCount, kettelCount);
            count = kettelCount;
            Log.i("water-setPur", "kettelCount:" + kettelCount);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    //onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //智能设定
    public void setSmartConfig(final SmartParamsWaterPurifier smartParams, short ArgumentNumber, VoidCallback callback) {
        try {
            LogUtils.i("20170709", "smartParams:::" + smartParams.PowerSaving + "    min::::" + smartParams.WaterSystem_minte);
            Msg msg = newReqMsg(MsgKeys.SetWaterPurifiySmart_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());                   //增加 userid
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.SetWaterPurifierSystemKey, (short) 1);
                msg.putOpt(MsgParams.SetWaterPurifierSystemLength, (short) 1);
                msg.putOpt(SetSetWaterPurifierSystemValue, smartParams.WaterSystem_minte);
                msg.putOpt(MsgParams.setWaterPurifierPowerSavingKey, (short) 2);
                msg.putOpt(MsgParams.SetWaterPurifierPowerSavingLength, (short) 1);
                msg.putOpt(MsgParams.SetWaterPurifierPowerSavingValue, smartParams.PowerSaving);
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsWaterPurifier.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 设置冲洗功能
     */
    public void setRemoteIrrigation(final short kettelCount, VoidCallback callback){
        try {
            Msg msg = newReqMsg(MsgKeys.SetWaterPurifiyWorking_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.WaterPurifiyStatus, 4);
            msg.putOpt(MsgParams.WaterPurifierKettelCount, kettelCount);
            count = kettelCount;
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWaterPurifierSmartStatus(final Callback<SmartParamsWaterPurifier> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.getWaterPurifierStatusSmart_Req);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsWaterPurifier.this.argument = (short) resMsg.optInt(MsgParams.ArgumentNumber);
                    if (AbsWaterPurifier.this.argument > 0) {
                        LogUtils.i("20170644", "smartParams:" + (short) resMsg.optInt(SetSetWaterPurifierSystemValue));
                        smartParams.PowerSaving = (short) resMsg.optInt(MsgParams.SetWaterPurifierPowerSavingValue);
                        smartParams.WaterSystem_minte = (short) resMsg.optInt(SetSetWaterPurifierSystemValue);
                        Helper.onSuccess(callback, smartParams);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }
}
