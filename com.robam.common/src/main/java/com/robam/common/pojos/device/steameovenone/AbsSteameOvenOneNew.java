package com.robam.common.pojos.device.steameovenone;


import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import android.util.Log;
import android.widget.Toast;
import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.IntegStoveAlarmEvent;
import com.robam.common.events.NewSteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneWaterChangesEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;

import org.json.JSONException;

import java.util.Map;

public class AbsSteameOvenOneNew extends AbsSteameOvenOne{
    public short faultCode;
    public short rotateSwitch;
    public short waterBoxState;

    public short recipeSetMinutes;
    public short curTexmp2;
    public short curSteamTotalNeedHours;
    public short cookedTime;

    public short sectionNumber;
    public short mode;
    public short restTime;
    public short UserID;


    public short waterBoxLow;
    public short chugouType;
    public short totalRemainSecondsH;
    public short totalRemainSeconds;
    //剩余总时间和
    public int totalRemain;
    public int restTimeH;
    public short curTemp;

    public short curSectionNbr;
    public short steamState;
    public short descaleFlag;
    public short waterLevelState;

    public short orderLeftMinutes;
    public short orderMinutesLength;
    public short orderRightMinutes;
    public short orderLeftMinutes1;
    public short orderRightMinutes1;

    public int orderLeftTime ;

    public short doorState;
    public short curTemp2;
    public short curSteamTotalHours;
    public short setDownTemp;

    public String recipteName ;
    /**
     * 菜谱id 菜谱名对应map
     */
    public Map<String, String> cookMap ;
    /**
     * 菜谱id 是否需要水对应map
     */
    public Map<String, String> cookNeedWaterMap ;
    public String workingAddSteamUrl;



    public AbsSteameOvenOneNew(DeviceInfo devInfo) {
        super(devInfo);
    }


    @Override
    public void onPolling() {
        try {
            //普通一体机
            Msg reqMsg = newReqMsg(MsgKeys.getDeviceAttribute_Req);
            reqMsg.putOpt(MsgParams.categoryCode , 0);
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    @Override
    public void onStatusChanged() {
//       super.onStatusChanged();

//        Log.e("结果",toString());
//        postEvent(new NewSteamOvenOneAlarmEvent(AbsSteameOvenOneNew.this , (short)11));

        Log.e(TAG,toString());
      postEvent(new SteamOvenOneStatusChangedEvent(AbsSteameOvenOneNew.this));
    }

    @Override
    public String toString() {
        return "打印"+"powerState:"+ powerState+"-----workState:"+workState;
    }
    public  boolean isWater(SteamOvenModeEnum modeEnum){
        if (modeEnum == SteamOvenModeEnum.XIANNENZHENG
                || modeEnum == SteamOvenModeEnum.YIYANGZHENG
                || modeEnum == SteamOvenModeEnum.GAOWENZHENG
                || modeEnum == SteamOvenModeEnum.WEIBOZHENG
                || modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
                || modeEnum == SteamOvenModeEnum.JIASHIBEIKAO
                || modeEnum == SteamOvenModeEnum.SHAJUN
                || modeEnum == SteamOvenModeEnum.JIEDONG
                || modeEnum == SteamOvenModeEnum.FAJIAO
                ||modeEnum==SteamOvenModeEnum.SHOUDONGJIASHIKAO
        ){
            return true ;
        }
        return false ;
    }
    /**
     * 一体机自动菜谱
     * @param recipeId
     * @param recipeSetMinutes
     * @param callback
     */
    public void setSteamOvenAutoRecipe(int recipeId, int recipeSetMinutes , VoidCallback callback) {

        if (this.descaleFlag==1){
            if (isWater(SteamOvenModeEnum.match(mode))) {
                ToastUtils.show("水箱缺水",Toast.LENGTH_LONG);
                return;
            }

        }
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(ArgumentNumber, 4);
            msg.putOpt(MsgParamsNew.type , 3) ;
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //菜谱id

            msg.putOpt(MsgParamsNew.recipeIdKey, 17);
            msg.putOpt(MsgParamsNew.recipeIdLength, 1);
            short recipeIdTemp = recipeId > 255 ? (short) (recipeId & 0Xff):(short)recipeId;
            if (recipeId<=255){
                msg.putOpt(MsgParamsNew.recipeId, recipeIdTemp);
            }else{
                msg.putOpt(MsgParamsNew.recipeIdKey, 17);
                msg.putOpt(MsgParamsNew.recipeIdLength, 2);
                short lowPower = (short)(recipeIdTemp & 0xff);
                msg.putOpt(MsgParamsNew.recipeId, lowPower);
                short highPower = (short) ((recipeIdTemp >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.recipeId01, highPower);
            }

//                msg.putOpt(MsgParamsNew.recipeId, recipeId);

            //菜谱时间

            msg.putOpt(MsgParamsNew.recipeSetMinutesKey, 18);
            msg.putOpt(MsgParamsNew.recipeSetMinutesLength, 1);
            short SetMinutesTemp = recipeSetMinutes > 255 ? (short) (recipeSetMinutes & 0Xff):(short)recipeSetMinutes;
            if (recipeSetMinutes<=255){
                msg.putOpt(MsgParamsNew.recipeSetMinutes, SetMinutesTemp);
            }else{
                msg.putOpt(MsgParamsNew.recipeSetMinutesLength, 2);
                short time = (short)(recipeSetMinutes & 0xff);
                msg.putOpt(MsgParamsNew.recipeSetMinutes, time);
                short highTime = (short) ((recipeSetMinutes >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.recipeSetMinutesH, highTime);
            }

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "AbsSteameOvenOneNew";

    /**
     * 复热
     * @param mode
     * @param power
     * @param weight
     * @param orderTime
     * @param callback
     */
    public void setReMicroRunModel(short mode,int power,int weight,int orderTime,VoidCallback callback){
        Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
        try {
            msg.putOpt(ArgumentNumber, 7);
            msg.putOpt(MsgParamsNew.type,6);
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 0) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //微波控制档位
            msg.putOpt(MsgParamsNew.microWaveLevelCtrlKey,107);
            msg.putOpt(MsgParamsNew.microWaveLevelLength, 1);

            short powerTemp = power > 255 ? (short) (power & 0Xff):(short)power;
            if (powerTemp<=255){
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl, powerTemp);

            }else {
                msg.putOpt(MsgParamsNew.microWaveLevelCtrlKey, 107);
                msg.putOpt(MsgParamsNew.microWaveLevelLength, 2);
                short lowPower = (short)(power & 0xff);
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl, lowPower);
                short highPower = (short) ((power >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl01, highPower);
            }

            //预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);

//            short orderTimerTemp = orderTime > 255 ? (short) (orderTime & 0Xff):(short)orderTime;
            if (orderTime<=255){
                msg.putOpt(MsgParamsNew.setOrderMinutes01, orderTime);

            }else if (orderTime<=65535){
                msg.putOpt(MsgParamsNew.setOrderMinutesKey, 104);
                msg.putOpt(MsgParamsNew.setOrderMinutesLength, 2);
                short lowOrderTime = (short)(orderTime & 0xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes01, lowOrderTime);
                short highOrderTime = (short) ((orderTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes02, highOrderTime);
            }else{
                msg.putOpt(MsgParamsNew.setOrderMinutesKey, 104);
                msg.putOpt(MsgParamsNew.setOrderMinutesLength, 3);
                short lowOrderTime = (short)(orderTime & 0xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes01, lowOrderTime);
                short highOrderTime = (short) ((orderTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes02, highOrderTime);
                short highPower1 = (short) ((power >> 16) & 0Xffff);
                msg.putOpt(MsgParamsNew.setOrderMinutes03, highPower1);
            }

            //255*255*1+255*1+num


            //微波控制重量
            msg.putOpt(MsgParamsNew.microWaveWeightCtrlKey,108);
            msg.putOpt(MsgParamsNew.microWaveWeightLength, 1);
            msg.putOpt(MsgParamsNew.microWaveWeightCtrl, weight);


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {

                @Override
                public void onSuccess(Msg resMsg) {
                 Log.e(TAG,"sendMsg");
                }

                @Override
                public void onFailure(Throwable t) {
                }



            });
        }catch (Exception e){



        }

    }


    //微热
    public void setMicroRunModel(short mode,int
            setTime,short microLevel,int orderTime,VoidCallback callback){
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(ArgumentNumber, 7);
            msg.putOpt(MsgParamsNew.type,5);
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 0) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //微波控制档位
            msg.putOpt(MsgParamsNew.microWaveLevelCtrlKey,107);
            msg.putOpt(MsgParamsNew.microWaveLevelLength, 1);

            short lowMicroLevel = microLevel > 255 ? (short) (microLevel & 0Xff):(short)microLevel;
            if (lowMicroLevel<=255){
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl, lowMicroLevel);
            }else{
                msg.putOpt(MsgParamsNew.microWaveLevelCtrlKey, 107);
                msg.putOpt(MsgParamsNew.microWaveLevelLength, 2);
                short LowMicroLevel = (short)(microLevel & 0xff);
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl, LowMicroLevel);
                short highMicroLevel = (short) ((microLevel >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.microWaveLevelCtrl01, highMicroLevel);
            }



//预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);

             short lowTime = orderTime > 255 ? (short) (orderTime & 0Xff):(short)orderTime;
            if (setTime<=255){
                msg.putOpt(MsgParamsNew.setOrderMinutes01, lowTime);
            }else{
                msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                msg.putOpt(MsgParamsNew.setOrderMinutesLength, 2);
                short time = (short)(orderTime & 0xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                short highTime = (short) ((orderTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
            }


            //设置时间
            msg.putOpt(MsgParamsNew.setTimeKey, 104);
            msg.putOpt(MsgParamsNew.setTimeLength, 1);

            lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            if (setTime<=255){
                msg.putOpt(MsgParamsNew.setTime0b, lowTime);
            }else{
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 2);
                short time = (short)(setTime & 0xff);
                msg.putOpt(MsgParamsNew.setTime0b, time);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setTime1b, highTime);
            }

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void addStream(short steamFlow,VoidCallback callback){
        Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
        try {
            msg.putOpt(ArgumentNumber, 3);
            //一体机电源控制
            msg.putOpt(MsgParamsNew.type, 7);
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //
            msg.putOpt(MsgParamsNew.steamKey, 16);
            msg.putOpt(MsgParamsNew.steamLength, 1);
            msg.putOpt(MsgParamsNew.steam, steamFlow);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        }catch (JSONException e){

        }

    }
    /**
     * 设置一体机参数 （单模式）
     * @param mode 模式
     * @param setTime 时间
     * @param setTemp 温度
     * @param orderTime 预约时间
     * @param callback
     */
    public void setSteameOvenOneRunModeExp(final short mode,  int setTime,
                                           final short setTemp, final short setDownTemp,
                                           int orderTime, short steamFlow ,VoidCallback callback) {


        if (this.descaleFlag==1){
            if (isWater(SteamOvenModeEnum.match(mode))) {
                ToastUtils.show("水箱缺水",Toast.LENGTH_LONG);
                return;
            }


        }
        SteamOvenModeEnum modeEnum= SteamOvenModeEnum.match(mode);
        if (modeEnum == SteamOvenModeEnum.XIANNENZHENG
                || modeEnum == SteamOvenModeEnum.YIYANGZHENG
                || modeEnum == SteamOvenModeEnum.GAOWENZHENG
                || modeEnum == SteamOvenModeEnum.WEIBOZHENG
                || modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
                || modeEnum == SteamOvenModeEnum.JIASHIBEIKAO
                || modeEnum == SteamOvenModeEnum.SHAJUN
                || modeEnum == SteamOvenModeEnum.JIEDONG
                || modeEnum == SteamOvenModeEnum.FAJIAO
        ){
            if (waterLevelState==1) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_LONG);
                return;
            }
        }
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            if (steamFlow==0){
                msg.putOpt(ArgumentNumber, 8);
            }else {
                msg.putOpt(ArgumentNumber, 9);
            }
            msg.putOpt(MsgParamsNew.type , 0) ;
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);

            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);

            msg.putOpt(MsgParamsNew.powerCtrl, 1);

            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);

            if (orderTime==0){
                msg.putOpt(MsgParamsNew.workCtrl, 1);
            }else {
                msg.putOpt(MsgParamsNew.workCtrl, 3);
            }

            //预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);


            final short lowOrderTime = orderTime > 255 ? (short) (orderTime & 0Xff):(short)orderTime;
            if (orderTime<=255){
                msg.putOpt(MsgParamsNew.setOrderMinutes01, lowOrderTime);
            }else{
                if (orderTime<=(256*256)&&orderTime>255) {
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 2);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                }else if (orderTime<=255*255*255&&orderTime>255*255){
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 3);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                    short time1 = (short) ((orderTime >> 16) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes03, time1);
                }
            }
//            msg.putOpt(MsgParamsNew.setOrderMinutes, orderTime);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 1) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //温度上温度
            msg.putOpt(MsgParamsNew.setUpTempKey  , 102);
            msg.putOpt(MsgParamsNew.setUpTempLength, 1);
            msg.putOpt(MsgParamsNew.setUpTemp, setTemp);

            //温度上温度
            msg.putOpt(MsgParamsNew.setDownTempKey  , 103);
            msg.putOpt(MsgParamsNew.setDownTempLength, 1);
            msg.putOpt(MsgParamsNew.setDownTemp, setDownTemp);
            //时间
            setTime*=60;
            msg.putOpt(MsgParamsNew.setTimeKey, 104);
            msg.putOpt(MsgParamsNew.setTimeLength, 1);

            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            if (setTime<=255){
                msg.putOpt(MsgParamsNew.setTime0b, lowTime);
            }else{
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 2);
                short time = (short)(setTime & 0xff);
                msg.putOpt(MsgParamsNew.setTime0b, time);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setTime1b, highTime);
            }


            //蒸汽量
            if (steamFlow!=0) {
                msg.putOpt(MsgParamsNew.steamKey, 106);
                msg.putOpt(MsgParamsNew.steamLength, 1);
                msg.putOpt(MsgParamsNew.steam, steamFlow);
            }


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 设置一体机参数 （单模式）
     * @param mode 模式
     * @param setTime 时间
     * @param setTemp 温度
     * @param orderTime 预约时间
     * @param callback
     */
    public void setSteameOvenOneRunMode920(final short mode,  int setTime,
                                        final short setTemp,
                                        int orderTime, short steamFlow ,VoidCallback callback) {


        if (this.descaleFlag==1){
            if (isWater(SteamOvenModeEnum.match(mode))) {
                ToastUtils.show("水箱缺水",Toast.LENGTH_LONG);
                return;
            }


        }
        SteamOvenModeEnum modeEnum= SteamOvenModeEnum.match(mode);
        if (modeEnum == SteamOvenModeEnum.XIANNENZHENG
                || modeEnum == SteamOvenModeEnum.YIYANGZHENG
                || modeEnum == SteamOvenModeEnum.GAOWENZHENG
                || modeEnum == SteamOvenModeEnum.WEIBOZHENG
                || modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
                || modeEnum == SteamOvenModeEnum.JIASHIBEIKAO
                || modeEnum == SteamOvenModeEnum.SHAJUN
                || modeEnum == SteamOvenModeEnum.JIEDONG
                || modeEnum == SteamOvenModeEnum.FAJIAO
        ){
            if (waterLevelState==1) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_LONG);
                return;
            }
        }
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            if (steamFlow==0){
                msg.putOpt(ArgumentNumber, 7);
            }else {
                msg.putOpt(ArgumentNumber, 8);
            }
            msg.putOpt(MsgParamsNew.type , 0) ;
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);

            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);

            msg.putOpt(MsgParamsNew.powerCtrl, 1);

            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);

            if (orderTime==0){
                msg.putOpt(MsgParamsNew.workCtrl, 1);
            }else {
                msg.putOpt(MsgParamsNew.workCtrl, 3);
            }

            //预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);


            final short lowOrderTime = orderTime > 255 ? (short) (orderTime & 0Xff):(short)orderTime;
            if (orderTime<=255){
                msg.putOpt(MsgParamsNew.setOrderMinutes01, lowOrderTime);
            }else{
                if (orderTime<=(256*256)&&orderTime>255) {
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 2);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                }else if (orderTime<=255*255*255&&orderTime>255*255){
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 3);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                    short time1 = (short) ((orderTime >> 16) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes03, time1);
                }
            }
//            msg.putOpt(MsgParamsNew.setOrderMinutes, orderTime);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 1) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //温度上温度
            msg.putOpt(MsgParamsNew.setUpTempKey  , 102);
            msg.putOpt(MsgParamsNew.setUpTempLength, 1);
            msg.putOpt(MsgParamsNew.setUpTemp, setTemp);
            //时间

            msg.putOpt(MsgParamsNew.setTimeKey, 104);
            msg.putOpt(MsgParamsNew.setTimeLength, 1);

            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            if (setTime<=255){
                msg.putOpt(MsgParamsNew.setTime0b, lowTime);
            }else{
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 2);
                short time = (short)(setTime & 0xff);
                msg.putOpt(MsgParamsNew.setTime0b, time);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setTime1b, highTime);
            }


            if (steamFlow!=0) {
                //蒸汽量
                msg.putOpt(MsgParamsNew.steamKey, 106);
                msg.putOpt(MsgParamsNew.steamLength, 1);
                msg.putOpt(MsgParamsNew.steam, steamFlow);
            }


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置一体机参数 （单模式）
     * @param mode 模式
     * @param setTime 时间
     * @param setTemp 温度
     * @param orderTime 预约时间
     * @param callback
     */
    public void setSteameOvenOneRunMode(final short mode,  int setTime,
                                        final short setTemp,
                                         int orderTime, short steamFlow ,VoidCallback callback) {


        if (this.descaleFlag==1){
            if (isWater(SteamOvenModeEnum.match(mode))) {
                ToastUtils.show("水箱缺水",Toast.LENGTH_LONG);
                return;
            }


        }
        SteamOvenModeEnum modeEnum= SteamOvenModeEnum.match(mode);
        if (modeEnum == SteamOvenModeEnum.XIANNENZHENG
                || modeEnum == SteamOvenModeEnum.YIYANGZHENG
                || modeEnum == SteamOvenModeEnum.GAOWENZHENG
                || modeEnum == SteamOvenModeEnum.WEIBOZHENG
                || modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
                || modeEnum == SteamOvenModeEnum.JIASHIBEIKAO
                || modeEnum == SteamOvenModeEnum.SHAJUN
                || modeEnum == SteamOvenModeEnum.JIEDONG
                || modeEnum == SteamOvenModeEnum.FAJIAO
        ){
            if (waterLevelState==1) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_LONG);
                return;
            }
        }
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            if (steamFlow==0){
                msg.putOpt(ArgumentNumber, 7);
            }else {
                msg.putOpt(ArgumentNumber, 8);
            }
            msg.putOpt(MsgParamsNew.type , 0) ;
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);

            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);

            msg.putOpt(MsgParamsNew.powerCtrl, 1);

            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);

            if (orderTime==0){
                msg.putOpt(MsgParamsNew.workCtrl, 1);
            }else {
                msg.putOpt(MsgParamsNew.workCtrl, 3);
            }

            //预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);

//
//            final short lowOrderTime = orderTime > 255 ? (short) (orderTime & 0Xff):(short)orderTime;
            if (orderTime<=255){
                msg.putOpt(MsgParamsNew.setOrderMinutes01, orderTime);
            }else{
                if (orderTime<=(256*256)&&orderTime>255) {
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 2);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                }else if (orderTime<=255*255*255&&orderTime>255*255){
                    msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
                    msg.putOpt(MsgParamsNew.setOrderMinutesLength, 4);
                    short time = (short) (orderTime & 0xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes01, time);
                    short highTime = (short) ((orderTime >> 8) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes02, highTime);
                    short time1 = (short) ((orderTime >> 16) & 0Xff);
                    msg.putOpt(MsgParamsNew.setOrderMinutes03, time1);
                }
            }
//            msg.putOpt(MsgParamsNew.setOrderMinutes, orderTime);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 1) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //温度上温度
            msg.putOpt(MsgParamsNew.setUpTempKey  , 102);
            msg.putOpt(MsgParamsNew.setUpTempLength, 1);
            msg.putOpt(MsgParamsNew.setUpTemp, setTemp);
            //时间
            setTime*=60;
            msg.putOpt(MsgParamsNew.setTimeKey, 104);
            msg.putOpt(MsgParamsNew.setTimeLength, 1);

            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            if (setTime<=255){
                msg.putOpt(MsgParamsNew.setTime0b, lowTime);
            }else{
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 2);
                short time = (short)(setTime & 0xff);
                msg.putOpt(MsgParamsNew.setTime0b, time);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setTime1b, highTime);
            }


            if (steamFlow!=0) {
                //蒸汽量
                msg.putOpt(MsgParamsNew.steamKey, 106);
                msg.putOpt(MsgParamsNew.steamLength, 1);
                msg.putOpt(MsgParamsNew.steam, steamFlow);
            }


            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 蒸烤一体机多段模式
     */
    public void setSteamOvenOneMultiStepMode(Msg msg  ,VoidCallback callback) {
        try {
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceivedMsg(Msg msg) {

        super.onReceivedMsg(msg);
        Log.e(TAG,"onReceivedMsg test1");

//        if (!isConnected) {
//            Log.e(TAG,"onReceivedMsg test1111");
//            setConnected(true);
//       }

        int key = msg.getID();
        if (key==MsgKeys.getDeviceAttribute_Rep){
            this.powerState = (short) msg.optInt(MsgParamsNew.powerState);
            this.workState = (short) msg.optInt(MsgParamsNew.workState);
            this.orderLeftMinutes = (short) msg.optInt(MsgParamsNew.orderLeftMinutes);
            this.orderRightMinutes = (short) msg.optInt(MsgParamsNew.orderRightMinutes);
            this.orderLeftMinutes1 = (short) msg.optInt(MsgParamsNew.orderLeftMinutes1);
            this.orderRightMinutes1 = (short) msg.optInt(MsgParamsNew.orderRightMinutes1);
            this.orderMinutesLength= (short) msg.optInt(MsgParamsNew.orderMinutesLength);
            this.orderLeftTime= msg.optInt(MsgParamsNew.orderLeftTime);

            this.faultCode = (short) msg.optInt(MsgParamsNew.faultCode);
            this.rotateSwitch = (short) msg.optInt(MsgParamsNew.rotateSwitch);
            this.waterBoxState = (short) msg.optInt(MsgParamsNew.waterBoxState);
            this.waterLevelState = (short) msg.optInt(MsgParamsNew.waterLevelState);
            this.doorState = (short) msg.optInt(MsgParamsNew.doorState);
            this.steamState = (short) msg.optInt(MsgParamsNew.steamState);
            this.recipeId = (short) msg.optInt(MsgParamsNew.recipeId);
            this.recipeSetMinutes = (short) msg.optInt(MsgParamsNew.recipeSetMinutes);
            this.curTemp = (short) msg.optInt(MsgParamsNew.curTemp);
            this.curTemp2 = (short) msg.optInt(MsgParamsNew.curTemp2);
            this.totalRemainSeconds = (short) msg.optInt(MsgParamsNew.totalRemainSeconds);
            this.totalRemainSecondsH = (short) msg.optInt(MsgParamsNew.totalRemainSeconds2);
            this.totalRemain = msg.optInt(MsgParamsNew.totalRemain);
            this.descaleFlag = (short) msg.optInt(MsgParamsNew.descaleFlag);
            this.curSteamTotalHours = (short) msg.optInt(MsgParamsNew.curSteamTotalHours);
            this.curSteamTotalNeedHours = (short) msg.optInt(MsgParamsNew.curSteamTotalNeedHours);
            this.cookedTime = (short) msg.optInt(MsgParamsNew.cookedTime);
            this.chugouType = (short) msg.optInt(MsgParamsNew.chugouType);
            this.curSectionNbr = (short) msg.optInt(MsgParamsNew.curSectionNbr);
            this.sectionNumber = (short) msg.optInt(MsgParamsNew.sectionNumber);
            Log.e("模式",msg.optInt(MsgParamsNew.mode)+"--");
            if (curSectionNbr == 0 || curSectionNbr == 1 ) {
                this.mode = (short) msg.optInt(MsgParamsNew.mode);
                this.setUpTemp = (short) msg.optInt(MsgParamsNew.setUpTemp);
                this.setDownTemp = (short) msg.optInt(MsgParamsNew.setDownTemp);
                this.setTime = (short) msg.optInt(MsgParamsNew.setTime);
                this.setTimeH = (short) msg.optInt(MsgParamsNew.setTimeH);
                this.restTime = (short) msg.optInt(MsgParamsNew.restTime);
                this.restTimeH = (short) msg.optInt(MsgParamsNew.restTimeH);
                this.steam = (short) msg.optInt(MsgParamsNew.steam);
            }else  {
                this.mode = (short) msg.optInt(MsgParamsNew.mode + curSectionNbr);
                this.setUpTemp = (short) msg.optInt(MsgParamsNew.setUpTemp + curSectionNbr );
                this.setDownTemp = (short) msg.optInt(MsgParamsNew.setDownTemp + curSectionNbr );
                this.setTime = (short) msg.optInt(MsgParamsNew.setTime + curSectionNbr );
                this.setTimeH = (short) msg.optInt(MsgParamsNew.setTimeH + curSectionNbr );
                this.restTime = (short) msg.optInt(MsgParamsNew.restTime + curSectionNbr);
                this.restTimeH = (short) msg.optInt(MsgParamsNew.restTimeH +curSectionNbr);
                this.steam = (short) msg.optInt(MsgParamsNew.steam + curSectionNbr);
            }
            onStatusChanged();
        }

        else if (key==MsgKeys.getDeviceAlarmEventReport){
            this.faultCode = (short) msg.optInt(MsgParamsNew.faultCode);
            postEvent(new NewSteamOvenOneAlarmEvent(AbsSteameOvenOneNew.this , faultCode));
        }





    }




    /**
     * 单独设置一体机单一状态 开始 暂停等
     * @param steamWorkStatus
     * @param callback
     */
    public void setSteamWorkStatus(short steamWorkStatus,short key , VoidCallback callback) {

        try {
//            if (steamWorkStatus== IntegStoveStatus.workCtrl_stop&&key==4){
//                isAutoRecipe=false;
//            }
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);

            msg.putOpt(ArgumentNumber, 1);
            msg.putOpt(MsgParamsNew.type , 1) ;
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, key);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, steamWorkStatus);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20180417", " resMsg:" + resMsg);
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void setSteameOvenStatus(short powerStatus, short powerOnStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.headType,2);
//            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.sectionNumber,1);
            msg.putOpt(MsgParams.powerCtrlKey, powerStatus);
            msg.putOpt(MsgParams.powerCtrlLength, 1);
            msg.putOpt(MsgParams.powerCtrlKeyValue, powerOnStatus);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                public void onSuccess(Msg resMsg) {
                    super.onSuccess(resMsg);
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSteameOvenStatus_Off(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatusNew.powerCtrlKey, SteamOvenOnePowerOnStatusNew.Off, callback);
    }

    @Override
    public void setSteameOvenStatus_pause(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatusNew.workCtrlKey, SteamOvenOnePowerOnStatusNew.pause, callback);
    }

    @Override
    public void setSteameOvenStatus_on(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatusNew.powerCtrlKey, SteamOvenOnePowerOnStatusNew.Open, callback);
    }




    @Override
    public void setSteameOvenStatus_rerun(VoidCallback callback) {
        setSteameOvenStatus(SteamOvenOnePowerStatusNew.workCtrlKey,
                SteamOvenOnePowerOnStatusNew.rerun, callback);
    }

}
