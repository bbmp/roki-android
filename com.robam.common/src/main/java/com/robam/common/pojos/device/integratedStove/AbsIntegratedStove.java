package com.robam.common.pojos.device.integratedStove;


import android.util.Log;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.io.RCMsgCallback;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.events.IntegStoveAlarmEvent;
import com.robam.common.events.IntegStoveEvent;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.RikaAlarmEvent;
import com.robam.common.events.RikaFanCleanNoticEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSteamOvenWorkEvent;
import com.robam.common.events.RikaSteamWorkEvent;
import com.robam.common.events.RikaSterilizerChildLockEvent;
import com.robam.common.events.RikaSterilizerWorkFinishEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.rika.IRika;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;
import com.robam.common.pojos.device.rika.RikaSmartParams;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;

import java.io.Serializable;
import java.util.Map;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.UserId;


/**
 * Created by 14807 on 2018/1/23.
 */

public class AbsIntegratedStove extends AbsDeviceHub implements IintegStove, Serializable {
    public short rikaFanWorkStatus;
    public short rikaFanPower;
    public short rikaFanLight;
    public short rikaFanCleaningRemind;
    public short waitTimeValue;
    public short damperLeft;//?????????
    public short damperRight;
    public int cleaningUseTime;//????????????
    public short headNumber;
    public short lockStatus;
    public short stoveHeadLeftWorkStatus;//???????????????????????????
    public short stoveHeadLeftPower;
    public short stoveHeadLeftAlarmStatus;
    public short stoveHeadRightWorkStatus;
    public short stoveHeadRightPower;
    public short stoveHeadRightAlarmStatus;
    public short sterilWorkStatus = 255;//?????????????????????
    public short sterilLockStatus;
    public int sterilWorkTimeLeft;
    public short sterilDoorLockStatus = 255;//?????????????????????
    public short sterilAlarmStatus;
    public short sterilRemoveOilySoiled; //?????????????????????
    public short sterilExhaustFanStatus;//????????????????????????
    public short steamLockStatus;
    public short steamWorkStatus = 255;//?????????????????????
    public short steamAlarmStatus;
    public short steamRunModel;
    public short steamWorkTemp;
    public short steamWorkRemainingTime;//???????????????????????????
    public short steamDoorState;
    public short steamSetTemp;
    public short steamSetTime;
    public short steamLightState;//??????????????????
    public short steamRemoveOilySoiled;//?????????????????????
    public short steamRecipeId;//???????????????ID
    public short steamRecipeStep;
    public short steamWaterSwitchStatus;
    public short steamWasteWaterExcessive;//????????????????????????
    public short steamWasteWaterStatus;//???????????????
    public short steamExhaustFanStatus;
    public short steamOvenWorkStatus;//?????????????????????
    public short steamOvenRunModel;//?????????????????????
    public short steamOvenWorkTemp;//?????????????????????
    public short steamOvenTimeWorkRemaining;//???????????????????????????

    public short steamOvenCleanOil;//???????????????????????????

    public short terminalType = TerminalType.getType();
    IntegStoveAlarmCodeBean mRikaAlarmCodeBean = null;
    IntegStoveSmartParams smartParams = new IntegStoveSmartParams();

    private int childCategoryCode ;

/*-----------------------------------------------------?????????????????????-------------------------------------------------------------------*/
    /**
     * ????????????
     */
    public short powerState ;
    /**
     * ????????????
     */
    public short  powerCtrl  ;
    /**
     * ????????????
     */
    public short  workState  ;
    /**
     * ????????????
     */
    public short  workCtrl   ;
    /**
     * ??????????????????
     */
    public short  orderLeftMinutes ;
    /**
     * ?????????
     */
    public short  faultCode ;
    /**
     * ???????????????
     */
    public short  rotateSwitch  ;
    /**
     * ????????????
     */
    public short  waterBoxState  ;
    /**
     * ????????????
     */
    public short  waterLevelState  ;
    /**
     * ?????????
     */
    public short  doorState  ;
    /**
     * ?????????????????????
     */
    public short  steamState  ;
    /**
     * ????????????
     */
    public short  recipeId ;
    /**
     * ?????????????????????
     */
    public short  recipeSetMinutes  ;
    /**
     * ???????????? ?????????
     */
    public short  curTemp  ;
    /**
     * ???????????? ?????????
     */
    public short  curTemp2 ;
    /**
     * ???????????????
     */
    public short  totalRemainSeconds ;
    public short  totalRemainSecondsH ;
    /**
     * ??????????????????
     */
    public short  descaleFlag  ;
    /**
     * ?????????????????????????????????
     */
    public short  curSteamTotalHours  ;
    /**
     * ??????????????????????????????
     */
    public short  curSteamTotalNeedHours  ;
    /**
     * ??????????????????
     */
    public short  cookedTime  ;
    /**
     * ????????????
     */
    public short  chugouType  ;
    /**
     * ????????????/??????
     */
    public short  curSectionNbr ;
    /**
     * ????????????
     */
    public short  sectionNumber  ;
    /**
     * ????????????
     */
    public short  mode  ;
    /**
     * ????????????????????????
     */
    public short  setUpTemp  ;
    /**
     * ????????????????????????
     */
    public short  setDownTemp ;
    /**
     * ?????????????????????
     */
    public short  setTime  ;
    public short  setTimeH  ;
    /**
     * ?????????????????????
     */
    public short  restTime  ;
    public short  restTimeH  ;
    /**
     * ???????????????
     */
    public short  steam  ;

    /**
     * ???????????????
     */
    public short  mode1  ;
    /**
     * 2?????????????????????
     */
    public short  setUpTemp1 ;
    /**
     * 2?????????????????????
     */
    public short  setDownTemp1 ;
    /**
     * 2??????????????????
     */
    public short  setTime1  ;
    /**
     * 2??????????????????
     */
    public short  restTime1  ;
    /**
     * 2????????????
     */
    public short  steam1  ;


    /**
     * 3?????????
     */
    public short  mode2  ;
    /**
     * 3?????????????????????
     */
    public short  setUpTemp2  ;
    /**
     * 3?????????????????????
     */
    public short  setDownTemp2 ;
    /**
     * 3??????????????????
     */
    public short  setTime2  ;
    /**
     * 3??????????????????
     */
    public short  restTime2  ;
    /**
     * 3????????????
     */
    public short  steam2  ;

    /*-----------------------------------------------------??????????????????-------------------------------------------------------------------*/
    /**
     * ???????????? ??????
     */
    public short  fan_powerState ;
    /**
     * ?????? ??????
     */
    public short  fan_gear ;
    /**
     * ????????? ??????
     */
    public short  fan_lightSwitch  ;
    /**
     * ?????????????????? ??????
     */
    public short  fan_stove_linkage  ;

    /*---------------------------------------------------------??????--------------------------------------------------------------*/
    /**
     * ???????????? ??????
     */
    public short  stove_powerState ;

    /**
     * ???????????? ??????
     */
    public short  stove_powerCtrl ;

    /**
     * ???????????? ??????
     */
    public short  stove_head_num  ;
    /**
     * ???????????? ??????
     */
    public short  stove_head_type ;
    /**
     * ???????????? ??????
     */
    public short  stove_gear  ;

    /**
     * ?????? ??????
     */
    public short  stove_v_chip ;
    /**
     * ??????ID ??????
     */
    public short  repice_id ;
    /**
     * ????????????  ??????
     */
    public short  stove_temp  ;
    /**
     * ??????????????? ??????
     */
    public short  stove_time  ;


    /**
     * ???????????? ??????
     */
    public short  stove_faultCode  ;

    /*---------------------------------???????????????----------------------------------------*/
    /**
     * ???????????? ??????
     */
    public short  stove_powerState2 ;

    /**
     * ???????????? ??????
     */
    public short  stove_powerCtrl2 ;
    /**
     * ???????????? ??????
     */
    public short  stove_head_type2  ;
    /**
     * ???????????? ??????
     */
    public short  stove_gear2  ;

    /**
     * ?????? ??????
     */
    public short  stove_v_chip2  ;
    /**
     * ??????ID ??????
     */
    public short  repice_id2  ;
    /**
     * ????????????  ??????
     */
    public short  stove_temp2  ;
    /**
     * ??????????????? ??????
     */
    public short  stove_time2  ;

    /**
     * ???????????? ??????
     */
    public short  stove_faultCode2  ;

    //-------------------------------????????????????????????-----------------------------------------
    /*------------------------????????????----------------------------*/
    /**
     * ??????id ???????????????map
     */
    public Map<String, String> cookMap ;
    /**
     * ??????id ?????????????????????map
     */
    public Map<String, String> cookNeedWaterMap ;
    /**
     * ????????????
     */
    public String workingPauseUrl ;
    public String workingPauseHUrl ;
    /**
     * ??????
     */
    public String finishUrl ;
    /**
     * ?????????
     */
    public String workingAddSteamUrl ;


    /**
     * ??????????????????????????? ?????????????????????????????????????????????
     */
    private  int getChildDevice(){
        if (childCategoryCode == IntegratedStoveConstant.FAN){
            childCategoryCode = IntegratedStoveConstant.STOVE ;
            return childCategoryCode ;
        } else if (childCategoryCode == IntegratedStoveConstant.STOVE) {
            childCategoryCode = IntegratedStoveConstant.STEAME_OVEN_ONE ;
            return childCategoryCode ;
        }else {
            childCategoryCode = IntegratedStoveConstant.FAN ;
            return childCategoryCode ;
        }
    }


    public AbsIntegratedStove(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.getDeviceAttribute_Req);
            reqMsg.putOpt(MsgParams.categoryCode , getChildDevice());
            sendMsg(reqMsg, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged() {
        super.onStatusChanged();
        LogUtils.i("20180417", "rikaFanWorkStatus:" + rikaFanWorkStatus + " rikaFanPower:" +
                rikaFanPower + " rikaFanLight:" + rikaFanLight + " rikaFanCleaningRemind:" + rikaFanCleaningRemind + " steamLockStatus:" +
                steamLockStatus + " waitTimeValue:" + waitTimeValue + " damperLeft:" + damperLeft
                + "\n" + " damperRight:" + damperRight + " cleaningUseTime:" + cleaningUseTime + " headNumber:" + headNumber +
                " lockStatus:" + lockStatus + "\n" + " stoveHeadLeftWorkStatus:" + stoveHeadLeftWorkStatus +
                " stoveHeadLeftPower:" + stoveHeadLeftPower + " stoveHeadLeftAlarmStatus:" + stoveHeadLeftAlarmStatus +
                " stoveHeadRightWorkStatus:" + stoveHeadRightWorkStatus + " stoveHeadRightPower:" + stoveHeadRightPower +
                " stoveHeadRightAlarmStatus:" + stoveHeadRightAlarmStatus + "\n" + " sterilWorkStatus:" + sterilWorkStatus
                + " sterilLockStatus:" + sterilLockStatus + " sterilWorkTimeLeft:" + sterilWorkTimeLeft + "\n" + " sterilDoorLockStatus:" + sterilDoorLockStatus
                + " sterilAlarmStatus:" + sterilAlarmStatus + " sterilExhaustFanStatus:" + sterilExhaustFanStatus + " steamRecipeId:" + steamRecipeId + "\n" +
                " steamRecipeStep:" + steamRecipeStep + " steamWaterSwitchStatus:" + steamWaterSwitchStatus
                + " steamAlarmStatus:" + steamAlarmStatus + "\n" + " steamRunModel:" + steamRunModel + " steamWorkTemp:" + steamWorkTemp
                + " steamWorkRemainingTime:" + steamWorkRemainingTime + " steamDoorState:" + steamDoorState + "\n" + " steamSetTime:" + steamSetTime
                + " steamSetTemp:" + steamSetTemp + " steamLightState:" + steamLightState + " steamWasteWaterExcessive:" + steamWasteWaterExcessive
                + " steamWasteWaterStatus:" + steamWasteWaterStatus + " steamExhaustFanStatus:" + steamExhaustFanStatus + " steamWorkStatus:" + steamWorkStatus);

        postEvent(new IntegStoveStatusChangedEvent(AbsIntegratedStove.this));

    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        int key = msg.getID();

        switch (key) {

            case MsgKeys.getDeviceAttribute_Rep:
                short categoryCode = (short)msg.optInt(MsgParams.categoryCode);
                switch (categoryCode){
                    case IntegratedStoveConstant.FAN:
                        this.fan_powerState = (short) msg.optInt(MsgParamsNew.fan_powerState);
                        this.fan_gear = (short) msg.optInt(MsgParamsNew.fan_gear);
                        this.fan_lightSwitch = (short) msg.optInt(MsgParamsNew.fan_lightSwitch);
                        this.fan_stove_linkage = (short) msg.optInt(MsgParamsNew.fan_stove_linkage);
                        break;
                    case IntegratedStoveConstant.STOVE:
                        this.stove_powerState = (short) msg.optInt(MsgParamsNew.stove_powerState);
                        this.stove_head_num = (short) msg.optInt(MsgParamsNew.stove_head_num);
                        this.stove_head_type = (short) msg.optInt(MsgParamsNew.stove_head_type);
                        this.stove_v_chip = (short) msg.optInt(MsgParamsNew.stove_v_chip);
                        this.repice_id = (short) msg.optInt(MsgParamsNew.repice_id);
                        this.stove_temp = (short) msg.optInt(MsgParamsNew.stove_temp);
                        this.stove_time = (short) msg.optInt(MsgParamsNew.stove_time);
                        this.stove_faultCode = (short) msg.optInt(MsgParamsNew.stove_faultCode);

                        this.stove_powerState2 = (short) msg.optInt(MsgParamsNew.stove_powerState2);
                        this.stove_head_type2 = (short) msg.optInt(MsgParamsNew.stove_head_type2);
                        this.stove_gear2 = (short) msg.optInt(MsgParamsNew.stove_gear2);
                        this.stove_v_chip2 = (short) msg.optInt(MsgParamsNew.stove_v_chip2);
                        this.repice_id2 = (short) msg.optInt(MsgParamsNew.repice_id2);
                        this.stove_temp2 = (short) msg.optInt(MsgParamsNew.stove_temp2);
                        this.stove_time2 = (short) msg.optInt(MsgParamsNew.stove_time2);
                        this.stove_faultCode2 = (short) msg.optInt(MsgParamsNew.stove_faultCode2);
                        break;
                    case IntegratedStoveConstant.STEAME_OVEN_ONE:
                        this.powerState = (short) msg.optInt(MsgParamsNew.powerState);
                        this.workState = (short) msg.optInt(MsgParamsNew.workState);
                        this.orderLeftMinutes = (short) msg.optInt(MsgParamsNew.orderLeftMinutes);
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
                        this.descaleFlag = (short) msg.optInt(MsgParamsNew.descaleFlag);
                        this.curSteamTotalHours = (short) msg.optInt(MsgParamsNew.curSteamTotalHours);
                        this.curSteamTotalNeedHours = (short) msg.optInt(MsgParamsNew.curSteamTotalNeedHours);
                        this.cookedTime = (short) msg.optInt(MsgParamsNew.cookedTime);
                        this.chugouType = (short) msg.optInt(MsgParamsNew.chugouType);
                        this.curSectionNbr = (short) msg.optInt(MsgParamsNew.curSectionNbr);
                        this.sectionNumber = (short) msg.optInt(MsgParamsNew.sectionNumber);
                        if (curSectionNbr == 0 || curSectionNbr == 1 ) {
                            this.mode = (short) msg.optInt(MsgParamsNew.mode);
                            this.setUpTemp = (short) msg.optInt(MsgParamsNew.setUpTemp);
                            this.setDownTemp = (short) msg.optInt(MsgParamsNew.setDownTemp);
                            this.setTime = (short) msg.optInt(MsgParamsNew.setTime);
                            this.setTimeH = (short) msg.optInt(MsgParamsNew.setTimeH);
                            this.restTime = (short) msg.optInt(MsgParamsNew.restTime);
                            this.restTimeH = (short) msg.optInt(MsgParamsNew.restTimeH);
                            this.steam = (short) msg.optInt(MsgParamsNew.steam);
                        }else {
                            this.mode = (short) msg.optInt(MsgParamsNew.mode + curSectionNbr);
                            this.setUpTemp = (short) msg.optInt(MsgParamsNew.setUpTemp + curSectionNbr );
                            this.setDownTemp = (short) msg.optInt(MsgParamsNew.setDownTemp + curSectionNbr );
                            this.setTime = (short) msg.optInt(MsgParamsNew.setTime + curSectionNbr );
                            this.setTimeH = (short) msg.optInt(MsgParamsNew.setTimeH + curSectionNbr );
                            this.restTime = (short) msg.optInt(MsgParamsNew.restTime + curSectionNbr);
                            this.restTimeH = (short) msg.optInt(MsgParamsNew.restTimeH +curSectionNbr);
                            this.steam = (short) msg.optInt(MsgParamsNew.steam + curSectionNbr);
                        }

//                        this.mode1 = (short) msg.optInt(MsgParamsNew.mode1);
//                        this.setUpTemp1 = (short) msg.optInt(MsgParamsNew.setUpTemp1);
//                        this.setDownTemp1 = (short) msg.optInt(MsgParamsNew.setDownTemp1);
//                        this.setTime1 = (short) msg.optInt(MsgParamsNew.setTime1);
//                        this.restTime1 = (short) msg.optInt(MsgParamsNew.restTime1);
//                        this.steam1 = (short) msg.optInt(MsgParamsNew.steam1);
//
//                        this.mode2 = (short) msg.optInt(MsgParamsNew.mode2);
//                        this.setUpTemp2 = (short) msg.optInt(MsgParamsNew.setUpTemp2);
//                        this.setDownTemp2 = (short) msg.optInt(MsgParamsNew.setDownTemp2);
//                        this.setTime2 = (short) msg.optInt(MsgParamsNew.setTime2);
//                        this.restTime2 = (short) msg.optInt(MsgParamsNew.restTime2);
//                        this.steam2 = (short) msg.optInt(MsgParamsNew.steam2);
                        onStatusChanged();
                        break;
                    default:
                        break;
                }

                break;

            case MsgKeys.getDeviceAlarmEventReport:
                //????????????
                short category = (short) msg.optInt(MsgParams.categoryCode);
                //?????????
                short fanAlarmCode = (short) msg.optInt(MsgParamsNew.faultCode);
                this.faultCode = fanAlarmCode ;
                postEvent(new IntegStoveAlarmEvent(AbsIntegratedStove.this , fanAlarmCode ,category));
//                SteamOvenFaultEnum fault = SteamOvenFaultEnum.match(faultCode);
//                if (fault )
//                switch (fault){
//                    //????????????
//                    case LACKWATER:
//                        this.waterLevelState = 1;
//                        break;
//                }

                postEvent(new IntegStoveAlarmEvent(AbsIntegratedStove.this , fanAlarmCode ,category));
//                onStatusChanged();
                break;
            case MsgKeys.getDeviceEventReport:
                //????????????
                short categoryEvent = (short) msg.optInt(MsgParams.categoryCode);
                switch (categoryEvent) {
                    case IntegratedStoveConstant.FAN:
                        break;
                    case IntegratedStoveConstant.STOVE:
                        break;
                    case IntegratedStoveConstant.STEAME_OVEN_ONE:
                        short steamEventCode = (short) msg.optInt(MsgParams.steamEventCode);
                        SteamOvenEventEnum match = SteamOvenEventEnum.match(steamEventCode);
                        switch (match) {
                            //???????????????????????????
//                            case WATER:
//                                this.waterLevelState = 1;
//                                break;
                            case START_WORK:
                                this.workState = IntegStoveStatus.workState_work;
                                break;
                            case PAUSE_WORK:
                                this.workState = IntegStoveStatus.workState_work_time_out;
                                onStatusChanged();
                                break;
                            case GO_ON_WORK:
                                this.workState = IntegStoveStatus.workState_work;
                                break;
                            case COMPLETE_WORK:
                                this.workState = IntegStoveStatus.workState_complete;
                                break;
                            case STOP_WORK:
//                                this.workState = IntegStoveStatus.workState_complete;
                                onStatusChanged();
                                break;
                            case ORDER_WORK:
                                this.workState = IntegStoveStatus.workState_order;
                                break;
                            case CHUGOU:
                                this.descaleFlag = 1;
                                break;
                        }

                        postEvent(new IntegStoveEvent(AbsIntegratedStove.this ,categoryEvent , steamEventCode ));
//                        onStatusChanged();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;


        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void restore() {

    }

    @Override
    public void setFanSwitchStatus(short numberCategory, short categoryCode, short argumentNumber, short fanHeader_Key,
                                   short fanHeader_Leng, short rikaFanWorkStatus, VoidCallback callback) {
        fanSwitchStatus(numberCategory, categoryCode, argumentNumber, fanHeader_Key, fanHeader_Leng, rikaFanWorkStatus, callback);
    }

    //rika????????????????????????
    private void fanSwitchStatus(short numberCategory, short categoryCode, short argumentNumber, short fanHeader_Key, short fanHeader_Leng,
                                 short rikaFanWorkStatus, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceRunStatus_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);

            switch (fanHeader_Key) {
                case '1':
                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                    msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Leng);
                    msg.putOpt(MsgParams.rikaFanWorkStatus, rikaFanWorkStatus);
                    break;
                case '2':
                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                    msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Leng);
                    msg.putOpt(MsgParams.rikaFanPower, rikaFanWorkStatus);
                    break;
                case '3':
                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                    msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Leng);
                    msg.putOpt(MsgParams.rikaFanLight, rikaFanWorkStatus);
                    break;
                case '4':
                    msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                    msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Leng);
                    break;
                default:
                    break;
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
     * ??????????????????
     *
     * @param numberCategory
     * @param categoryCode
     * @param argumentNumber
     * @param fanHeader_Key
     * @param fanHeader_Leng
     * @param rikaFanPower
     * @param callback
     */
    @Override
    public void setCloseFanVolume(short numberCategory, short categoryCode, short argumentNumber,
                                  short fanHeader_Key, short fanHeader_Leng, short rikaFanPower, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceRunStatus_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.fanHeader_Key, fanHeader_Key);
                msg.putOpt(MsgParams.fanHeader_Leng, fanHeader_Leng);
                msg.putOpt(MsgParams.rikaFanPower, rikaFanPower);
            }
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("20180614", "e:" + e);
        }
    }

    @Override
    public void setSteamWorkStatus(short numberCategory, short categoryCode, short argumentNumber,
                                   short steamHeader_Key, short steamHeader_Leng, short steamWorkStatus, VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceRunStatus_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (categoryCode == RikaStatus.STEAM_CATEGORYCODE) {
                msg.putOpt(MsgParams.steamHeader_Key, steamHeader_Key);
                msg.putOpt(MsgParams.steamHeader_Leng, steamHeader_Leng);
                msg.putOpt(MsgParams.steamWorkStatus, steamWorkStatus);
            } else if (categoryCode == RikaStatus.STEAMQVEN_CATEGORYCODE){
                msg.putOpt(MsgParams.steamOvenHeader_Key, steamHeader_Key);
                msg.putOpt(MsgParams.steamOvenHeader_Leng, steamHeader_Leng);
                msg.putOpt(MsgParams.steamOvenWorkStatus, steamWorkStatus);
            }
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
    public void setSteamRunWorkModel(short numberCategory, short categoryCode, short argumentNumber,
                                     short steamHeader_Key, short steamHeader_Leng, short steamRunModel,
                                     short steamSetTemp, short steamSetTime, VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceRunStatus_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (categoryCode == RikaStatus.STEAM_CATEGORYCODE) {
                msg.putOpt(MsgParams.steamHeader_Key, steamHeader_Key);
                msg.putOpt(MsgParams.steamHeader_Leng, steamHeader_Leng);
                msg.putOpt(MsgParams.steamRunModel, steamRunModel);
                msg.putOpt(MsgParams.steamSetTemp, steamSetTemp);
                msg.putOpt(MsgParams.steamSetTime, steamSetTime);
            } else if (categoryCode == RikaStatus.STEAMQVEN_CATEGORYCODE){
                msg.putOpt(MsgParams.steamOvenHeader_Key, steamHeader_Key);
                msg.putOpt(MsgParams.steamOvenHeader_Leng, steamHeader_Leng);
                msg.putOpt(MsgParams.steamOvenRunModel, steamRunModel);
                msg.putOpt(MsgParams.steamOvenSetTemp, steamSetTemp);
                msg.putOpt(MsgParams.steamOvenSetTime, steamSetTime);
                msg.putOpt(MsgParams.steamOvenPreFlag, (short)0);
            }
            Log.e("20180529", "setSteamRunWorkModel: "+getSrcUser() );
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
    public void getSmartConfig(final Callback<IntegStoveSmartParams> callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.readIntelligentInteractiveModeSetting_Req);
            sendMsg(msg, new RCMsgCallback(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {

                    smartParams.fanAndStoveSwitchLinkage = resMsg.
                            optBoolean(MsgParams.fanAndStoveSwitchLinkage);
                    smartParams.fanPowerSwitchLinkage = resMsg
                            .optBoolean(MsgParams.fanPowerSwitchLinkage);

                    smartParams.fanTimeDelayShutdownSwitch = resMsg
                            .optBoolean(MsgParams.fanTimeDelayShutdownSwitch);

                    smartParams.fanDelaySwitchTime = (short) resMsg
                            .optInt(MsgParams.fanDelaySwitchTime);

                    smartParams.fanCleaningPromptSwitch = resMsg
                            .optBoolean(MsgParams.fanCleaningPromptSwitch);

                    smartParams.fanOpenRegularVentilation = resMsg
                            .optBoolean(MsgParams.fanOpenRegularVentilation);

                    smartParams.fanRegularVentilationIntervalTime = (short) resMsg
                            .optInt(MsgParams.fanRegularVentilationIntervalTime);

                    smartParams.fanOpenWeeklyVentilation = resMsg
                            .optBoolean(MsgParams.fanOpenWeeklyVentilation);

                    smartParams.WeeklyVentilationDate_Week = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Week);

                    smartParams.WeeklyVentilationDate_Hour = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Hour);
                    smartParams.WeeklyVentilationDate_Minute = (short) resMsg
                            .optInt(MsgParams.WeeklyVentilationDate_Minute);
                    LogUtils.i("20180719", "resMsg fanAndStoveSwitchLinkage:" + resMsg.optBoolean(MsgParams.fanAndStoveSwitchLinkage));
                    LogUtils.i("20180719", "smartParams fanAndStoveSwitchLinkage:" + smartParams.fanAndStoveSwitchLinkage);
                    Helper.onSuccess(callback, smartParams);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Rika?????????????????????
    @Override
    public void setSmartConfig(final IntegStoveSmartParams smartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceIntelligentInteractiveModel_Req);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, 1);
            msg.putOpt(MsgParams.categoryCode, RikaStatus.FAN_CATEGORYCODE);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.fanHeader_Key, 49);
            msg.putOpt(MsgParams.fanHeader_Leng, 11);
            msg.putOpt(MsgParams.fanAndStoveSwitchLinkage, smartParams.fanAndStoveSwitchLinkage);
            msg.putOpt(MsgParams.fanPowerSwitchLinkage, smartParams.fanPowerSwitchLinkage);
            msg.putOpt(MsgParams.fanTimeDelayShutdownSwitch, smartParams.fanTimeDelayShutdownSwitch);
            msg.putOpt(MsgParams.fanDelaySwitchTime, smartParams.fanDelaySwitchTime);
            msg.putOpt(MsgParams.fanSteamOvenLinkage, smartParams.fanSteamOvenLinkage);
            msg.putOpt(MsgParams.fanSteamOvenDelayShutdownSwitch, smartParams.fanSteamOvenDelayShutdownSwitch);
            msg.putOpt(MsgParams.fanCleaningPromptSwitch, smartParams.fanCleaningPromptSwitch);
            msg.putOpt(MsgParams.fanOpenRegularVentilation, smartParams.fanOpenRegularVentilation);
            msg.putOpt(MsgParams.fanRegularVentilationIntervalTime, smartParams.fanRegularVentilationIntervalTime);
            msg.putOpt(MsgParams.fanOpenWeeklyVentilation, smartParams.fanOpenWeeklyVentilation);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Week, smartParams.WeeklyVentilationDate_Week);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Hour, smartParams.WeeklyVentilationDate_Hour);
            msg.putOpt(MsgParams.WeeklyVentilationDate_Minute, smartParams.WeeklyVentilationDate_Minute);

            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsIntegratedStove.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * ?????????????????? ?????????
     * @param callback
     */
    public void setFanVolume(  short fan_powerCtrl , short fan_gear , VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.FAN);
            msg.putOpt(ArgumentNumber, 2);
            msg.putOpt(MsgParamsNew.type , 4) ;

            msg.putOpt(MsgParamsNew.fan_powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.fan_powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.fan_powerCtrl, fan_powerCtrl);

            msg.putOpt(MsgParamsNew.fan_gearKey, 3);
            msg.putOpt(MsgParamsNew.fan_gearLength, 1);
            msg.putOpt(MsgParamsNew.fan_gear, fan_gear);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("20180614", "e:" + e);
        }
    }

    /**
     * ????????????????????????
     * @param fanStatus
     * @param key
     * @param callback
     */
    public void setSteamOvenSmartConfig(short fanStatus,short key , VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.FAN);
            msg.putOpt(ArgumentNumber, 1);
            msg.putOpt(MsgParamsNew.type , 1) ;
            //??????????????????
            msg.putOpt(MsgParamsNew.workCtrlKey, key);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, fanStatus);
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
    public void setSterilizerWorkStatus(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                        short sterilizerHeader_Key, short sterilizerHeader_Leng,
                                        short sterilizerWorkStatus, short sterilizerWorkTime, short sterilOrderTime,
                                        short sterilizerWarmDishTemp, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.sterilHeader_Key, sterilizerHeader_Key);
                msg.putOpt(MsgParams.sterilHeader_Leng, sterilizerHeader_Leng);
                msg.putOpt(MsgParams.sterilWorkStatus, sterilizerWorkStatus);
                msg.putOpt(MsgParams.sterilWorkModelTime, sterilizerWorkTime);
                msg.putOpt(MsgParams.sterilOrderTime, sterilOrderTime);
                msg.putOpt(MsgParams.sterilWarmDishTemp, sterilizerWarmDishTemp);
            }
            LogUtils.i("20180529", "cmd:" + cmd + " UserId:" + getSrcUser() + " numberCatrgory:" + numberCategory + " categoryCode:" + categoryCode
                    + " argumentNumber:" + argumentNumber + " sterilizerHeader_Key:" +
                    sterilizerHeader_Key + " sterilizerHeader_Leng:" + sterilizerHeader_Leng +
                    " sterilizerWorkStatus:" + sterilizerWorkStatus + " sterilizerWorkTime:" + sterilizerWorkTime
                    + " sterilOrderTime:" + sterilOrderTime + " sterilizerWarmDishTemp:" + sterilizerWarmDishTemp);
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
    public void setSterilizerLockStatus(short cmd, short numberCategory, short categoryCode,
                                        short argumentNumber, short sterilizerHeader_Key,
                                        short sterilizerHeader_Leng, short sterilizerLockStatus,
                                        VoidCallback callback) {
        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.numberOfCategory, numberCategory);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.sterilHeader_Key, sterilizerHeader_Key);
                msg.putOpt(MsgParams.sterilHeader_Leng, sterilizerHeader_Leng);
                msg.putOpt(MsgParams.sterilLockStatus, sterilizerLockStatus);

            }
            LogUtils.i("20180529", "cmd:" + cmd + " UserId:" + getSrcUser() + " numberCatrgory:" + numberCategory + " categoryCode:" + categoryCode
                    + " argumentNumber:" + argumentNumber + " sterilizerHeader_Key:" +
                    sterilizerHeader_Key + " sterilizerHeader_Leng:" + sterilizerHeader_Leng + "sterilizerLockStatus:" + sterilizerLockStatus);
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
    public void setSteamOvenMultiStepCooking(short cmd, short numberCategory, short categoryCode, short argumentNumber,
                                             short steamOvenModelOne, short steamOvenTempOne, short steamOvenTimeOne,
                                             short steamOvenModelTwo, short steamOvenTempTwo, short steamOvenTimeTwo, VoidCallback callback) {


        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.steamOvenHeader_Key, (short)49);
                msg.putOpt(MsgParams.steamOvenHeader_Leng, (short)3);
                msg.putOpt(MsgParams.steamOvenModelOne, steamOvenModelOne);
                msg.putOpt(MsgParams.steamOvenTempOne, steamOvenTempOne);
                msg.putOpt(MsgParams.steamOvenTimeOne, steamOvenTimeOne);
                msg.putOpt(MsgParams.steamOvenHeader_Key1, (short)50);
                msg.putOpt(MsgParams.steamOvenHeader_Leng1, (short)3);
                msg.putOpt(MsgParams.steamOvenModelTwo, steamOvenModelTwo);
                msg.putOpt(MsgParams.steamOvenTempTwo, steamOvenTempTwo);
                msg.putOpt(MsgParams.steamOvenTimeTwo, steamOvenTimeTwo);

            }
            LogUtils.i("20180529", "cmd:" + cmd + " UserId:" + getSrcUser() + " numberCatrgory:" + numberCategory + " categoryCode:" + categoryCode
                    + " argumentNumber:" + argumentNumber +  "steamOvenModelOne:" + steamOvenModelOne + " steamOvenTempOne:"
                    + steamOvenTempOne + " steamOvenTimeOne:" + steamOvenTimeOne + " steamOvenModelTwo:" + steamOvenModelTwo +
                    " steamOvenTempTwo:" + steamOvenTempTwo + " steamOvenTimeTwo:" + steamOvenTimeTwo);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }

                @Override
                public void onFailure(Throwable t) {
                    super.onFailure(t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteamOvenAutoRecipeModel(short cmd, short numberCategory, short categoryCode,
                                            short argumentNumber, short steamOvenHeader_Key, short steamOvenHeader_Leng,
                                            short steamOvenModel, short stemOvenTime, VoidCallback callback) {

        try {
            Msg msg = newReqMsg(cmd);
            msg.putOpt(MsgParams.categoryCode, categoryCode);
            msg.putOpt(MsgParams.ArgumentNumber, argumentNumber);
            if (argumentNumber > 0) {
                msg.putOpt(MsgParams.steamOvenHeader_Key, steamOvenHeader_Key);
                msg.putOpt(MsgParams.steamOvenHeader_Leng, steamOvenHeader_Leng);
                msg.putOpt(MsgParams.steamOvenModel, steamOvenModel);
                msg.putOpt(MsgParams.stemOvenTime, stemOvenTime);
            }
            LogUtils.i("20180529", "cmd:" + cmd + " UserId:" + getSrcUser() + " numberCatrgory:" + numberCategory + " categoryCode:" + categoryCode
                    + " argumentNumber:" + argumentNumber + " steamOvenHeader_Key:" +
                    steamOvenHeader_Key + " steamOvenHeader_Leng:" + steamOvenHeader_Leng+ "   model:"+steamOvenModel+"    time:"+stemOvenTime);
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
     * ?????????????????????
     * @param recipeId
     * @param recipeSetMinutes
     * @param callback
     */
    public void setSteamOvenAutoRecipe(short recipeId, int recipeSetMinutes , VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
            msg.putOpt(ArgumentNumber, 5);
            msg.putOpt(MsgParamsNew.type , 3) ;
            //?????????????????????
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //?????????????????????
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //??????id
            msg.putOpt(MsgParamsNew.recipeId, recipeId);
            msg.putOpt(MsgParamsNew.recipeIdKey, 17);
            msg.putOpt(MsgParamsNew.recipeIdLength, 1);
            //????????????
            msg.putOpt(MsgParamsNew.recipeSetMinutes, recipeSetMinutes);
            msg.putOpt(MsgParamsNew.recipeSetMinutesKey, 18);
            msg.putOpt(MsgParamsNew.recipeSetMinutesLength, 1);
            if (recipeSetMinutes > 255){
                msg.putOpt(MsgParamsNew.recipeSetMinutesLength, 2);
                short time = (short)(recipeSetMinutes & 0xff);
                msg.putOpt(MsgParamsNew.recipeSetMinutes, time);
                short highTime = (short) ((recipeSetMinutes >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.recipeSetMinutesH, highTime);
            }

            //?????????
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 0) ;
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
     * ????????????????????? ???????????????
     * @param mode ??????
     * @param setTime ??????
     * @param setTemp ??????
     * @param orderTime ????????????
     * @param callback
     */
    public void setSteameOvenOneRunMode(final short mode, final short setTime,
                                        final short setTemp,
                                        short orderTime, short steamFlow ,VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());

            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
            msg.putOpt(ArgumentNumber, 8);
            msg.putOpt(MsgParamsNew.type , 0) ;
            //?????????????????????
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //?????????????????????
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //????????????
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);
            msg.putOpt(MsgParamsNew.setOrderMinutes, orderTime);
            //??????
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 0) ;
            //??????
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //???????????????
            msg.putOpt(MsgParamsNew.setUpTempKey  , 102);
            msg.putOpt(MsgParamsNew.setUpTempLength, 1);
            msg.putOpt(MsgParamsNew.setUpTemp, setTemp);
            //??????
            final short lowTime = setTime > 255 ? (short) (setTime & 0Xff):(short)setTime;
            if (setTime <= 255) {
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 1);
                msg.putOpt(MsgParamsNew.setTime, lowTime);
            }else {
                msg.putOpt(MsgParamsNew.setTimeKey, 104);
                msg.putOpt(MsgParamsNew.setTimeLength, 2);
                short time = (short)(setTime & 0xff);
                msg.putOpt(MsgParamsNew.setTime, time);
                short highTime = (short) ((setTime >> 8) & 0Xff);
                msg.putOpt(MsgParamsNew.setTimeH, highTime);
            }
            //?????????
            msg.putOpt(MsgParamsNew.steamKey, 106);
            msg.putOpt(MsgParamsNew.steamLength, 1);
            msg.putOpt(MsgParamsNew.steam, steamFlow);

//            if (setTime > 255) {
//                msg.putOpt(ArgumentNumber, 1);
//                msg.putOpt(MsgParams.time_H_key, 1);
//                msg.putOpt(MsgParams.time_H_length, 1);
//                short highTime = (short) ((setTime >> 8) & 0Xff);
//                msg.putOpt(MsgParams.time_H_Value, highTime);
//
//            } else {
//                msg.putOpt(ArgumentNumber, 0);
//            }

//            final long curtime = System.currentTimeMillis();
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
     * ???????????????????????????
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
    /**
     * ????????????????????????????????? ?????? ?????????
     * @param steamWorkStatus
     * @param callback
     */
    public void setSteamWorkStatus(short steamWorkStatus,short key , VoidCallback callback) {

        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
            msg.putOpt(ArgumentNumber, 1);
            msg.putOpt(MsgParamsNew.type , 1) ;
            //?????????????????????
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

    public void setCookMap(Map<String, String> cookMap) {
        this.cookMap = cookMap;
    }
    public void setCookNeedWaterMap(Map<String, String> cookMap) {
        this.cookNeedWaterMap = cookMap;
    }
}






