package com.robam.common.pojos.device.Sterilizer;



import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;


/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public class AbsSterilizer extends AbsDeviceHub implements ISterilizer {



    short weeklySteri_week;
    private static int times = 0;


    public short status;
    protected short terminalType = TerminalType.getType();

    public AbsSterilizer(DeviceInfo devInfo) {
        super(devInfo);
    }

    public SteriSmartParams steriSmartParams = new SteriSmartParams();
    // -------------------------------------------------------------------------------
    // IDevice
    // -------------------------------------------------------------------------------

    @Override
    public void onPolling() {
        super.onPolling();
    }

    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
    }

    // -------------------------------------------------------------------------------
    // ISterilizer
    // -------------------------------------------------------------------------------
    @Override
    public void pause() {

    }

    @Override
    public void restore() {
    }


    @Override
    public String getSterilizerModel() {
        return null;
    }



    @Override
    public void getSteriPVConfig(final Callback<SteriSmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.GetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);

            sendMsg(msg, new RCMsgCallback(callback) {
                protected void afterSuccess(Msg resMsg) {
                    steriSmartParams.IsInternalDays = resMsg.optInt(MsgParams.SteriSwitchDisinfect) == 1;
                    steriSmartParams.InternalDays = (short) resMsg.optInt(MsgParams.SteriInternalDisinfect);
                    steriSmartParams.IsWeekSteri = resMsg.optBoolean(MsgParams.SteriSwitchWeekDisinfect);
                    steriSmartParams.WeeklySteri_week = (short) resMsg.optInt(MsgParams.SteriWeekInternalDisinfect);
                    steriSmartParams.PVCTime = (short) resMsg.optInt(MsgParams.SteriPVDisinfectTime);
                    //onStatusChanged();
                    if (steriSmartParams.InternalDays == 255)
                        steriSmartParams.InternalDays = 3;
                    if (steriSmartParams.WeeklySteri_week == 255)
                        steriSmartParams.WeeklySteri_week = 3;
                    if (steriSmartParams.PVCTime == 255)
                        steriSmartParams.PVCTime = 20;

                    Helper.onSuccess(callback, steriSmartParams);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteriPVConfig(final SteriSmartParams steriSmartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.SetSteriPVConfig_Req);
            msg.put(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.SteriSwitchDisinfect, steriSmartParams.IsInternalDays);
            msg.putOpt(MsgParams.SteriInternalDisinfect, steriSmartParams.InternalDays);
            msg.putOpt(MsgParams.SteriSwitchWeekDisinfect, steriSmartParams.IsWeekSteri);
            msg.putOpt(MsgParams.SteriWeekInternalDisinfect, steriSmartParams.WeeklySteri_week);
            msg.putOpt(MsgParams.SteriPVDisinfectTime, steriSmartParams.PVCTime);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                   AbsSterilizer.this.steriSmartParams = steriSmartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
