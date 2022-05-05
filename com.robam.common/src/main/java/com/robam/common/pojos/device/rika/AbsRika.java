package com.robam.common.pojos.device.rika;


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
import com.robam.common.events.RikaAlarmEvent;
import com.robam.common.events.RikaFanCleanNoticEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSteamOvenWorkEvent;
import com.robam.common.events.RikaSteamWorkEvent;
import com.robam.common.events.RikaSterilizerChildLockEvent;
import com.robam.common.events.RikaSterilizerWorkFinishEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;

import java.io.Serializable;


/**
 * Created by 14807 on 2018/1/23.
 */

public class AbsRika extends AbsDeviceHub implements IRika, Serializable {


    public short numberOfCategory;
    public short rikaFanWorkStatus;
    public short rikaFanPower;
    public short rikaFanLight;
    public short rikaFanCleaningRemind;
    public short waitTimeValue;
    public short damperLeft;//左风门
    public short damperRight;
    public int cleaningUseTime;//清洗时间
    public short headNumber;
    public short lockStatus;
    public short stoveHeadLeftWorkStatus;//左灶具炉头工作状态
    public short stoveHeadLeftPower;
    public short stoveHeadLeftAlarmStatus;
    public short stoveHeadRightWorkStatus;
    public short stoveHeadRightPower;
    public short stoveHeadRightAlarmStatus;
    public short sterilWorkStatus = 255;//消毒柜工作状态
    public short sterilLockStatus;
    public int sterilWorkTimeLeft;
    public short sterilDoorLockStatus = 255;//消毒柜门锁状态
    public short sterilAlarmStatus;
    public short sterilRemoveOilySoiled; //消毒柜清除油渍
    public short sterilExhaustFanStatus;//消毒柜排风机状态
    public short steamLockStatus;
    public short steamWorkStatus = 255;//蒸汽炉工作状态
    public short steamAlarmStatus;
    public short steamRunModel;
    public short steamWorkTemp;
    public short steamWorkRemainingTime;//蒸汽炉工作剩余时间
    public short steamDoorState;
    public short steamSetTemp;
    public short steamSetTime;
    public short steamLightState;//蒸汽炉照明灯
    public short steamRemoveOilySoiled;//蒸汽炉清除油渍
    public short steamRecipeId;//蒸汽炉菜谱ID
    public short steamRecipeStep;
    public short steamWaterSwitchStatus;
    public short steamWasteWaterExcessive;//蒸汽炉废水位超标
    public short steamWasteWaterStatus;//抽废水状态
    public short steamExhaustFanStatus;
    public short steamOvenLockStatus;//一体机童锁
    public short steamOvenWorkStatus;//一体机工作状态
    public short steamOvenAlarmStatus;//一体机报警状态
    public short steamOvenRunModel;//一体机运行模式
    public short steamOvenWorkTemp;//一体机工作温度
    public short steamOvenTimeWorkRemaining;//一体机工作剩余时间
    public short steamOvenDoorState;//一体机门状态
    public short steamOvenSetTemp;//一体机设置温度
    public short steamOvenSetTime;//一体机设置时间
    public short steamOvenPreFlag;//一体机预设置时间
    public short steamOvenOrderTime;//一体机预约时间
    public short steamOvenLight;//一体机灯关
    public short steamOvenTotalNumber;//一体机总步鄹
    public short steamOvenCurrentNumber;//一体机当前步鄹
    public short steamOvenAutomaticRecipe;//一体机自动菜谱
    public short steamOvenCleanOil;//一体机是否需要清油
    public short steamOvenAbolishWaterOverproof;//废水水位超标
    public short steamOvenAbolishWaterRemind;//抽废水提醒
    public short steamOvenAbolishWaterTake;//抽废水状态
    public short steamOvenAbolishWaterSwitchStatus;//抽废水开关

    protected short terminalType = TerminalType.getType();
    RikaAlarmCodeBean mRikaAlarmCodeBean = null;
    RikaSmartParams smartParams = new RikaSmartParams();

    public AbsRika(DeviceInfo devInfo) {
        super(devInfo);
    }

    @Override
    public void onPolling() {
        try {
            Msg reqMsg = newReqMsg(MsgKeys.readDeviceStatus_Req);
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

        postEvent(new RikaStatusChangedEvent(AbsRika.this));

    }

    @Override
    public void onReceivedMsg(Msg msg) {
        super.onReceivedMsg(msg);
        int key = msg.getID();

        switch (key) {

            case MsgKeys.readDeviceStatus_Rep:
                this.numberOfCategory = (short) msg.optInt(MsgParams.numberOfCategory);
                this.rikaFanWorkStatus = (short) msg.optInt(MsgParams.rikaFanWorkStatus);
                this.rikaFanPower = (short) msg.optInt(MsgParams.rikaFanPower);
                this.rikaFanLight = (short) msg.optInt(MsgParams.rikaFanLight);
                this.rikaFanCleaningRemind = (short) msg.optInt(MsgParams.rikaFanCleaningRemind);
                this.waitTimeValue = (short) msg.optInt(MsgParams.waitTimeValue);
                this.damperLeft = (short) msg.optInt(MsgParams.damperLeft);
                this.damperRight = (short) msg.optInt(MsgParams.damperRight);
                this.cleaningUseTime = (short) msg.optInt(MsgParams.cleaningUseTime);
                this.headNumber = (short) msg.optInt(MsgParams.headNumber);
                this.lockStatus = (short) msg.optInt(MsgParams.lockStatus);
                this.stoveHeadLeftWorkStatus = (short) msg.optInt(MsgParams.stoveHeadLeftWorkStatus);
                this.stoveHeadLeftPower = (short) msg.optInt(MsgParams.stoveHeadLeftPower);
                this.stoveHeadLeftAlarmStatus = (short) msg.optInt(MsgParams.stoveHeadLeftAlarmStatus);
                this.stoveHeadRightWorkStatus = (short) msg.optInt(MsgParams.stoveHeadRightWorkStatus);
                this.stoveHeadRightPower = (short) msg.optInt(MsgParams.stoveHeadRightPower);
                this.stoveHeadRightAlarmStatus = (short) msg.optInt(MsgParams.stoveHeadRightAlarmStatus);
                this.sterilWorkStatus = (short) msg.optInt(MsgParams.sterilWorkStatus, -2);
                this.sterilLockStatus = (short) msg.optInt(MsgParams.sterilLockStatus);
                this.sterilWorkTimeLeft = msg.optInt(MsgParams.sterilWorkTimeLeft);
                this.sterilDoorLockStatus = (short) msg.optInt(MsgParams.sterilDoorLockStatus);
                this.sterilAlarmStatus = (short) msg.optInt(MsgParams.sterilAlarmStatus);
                this.sterilRemoveOilySoiled = (short) msg.optInt(MsgParams.sterilRemoveOilySoiled);
                this.sterilExhaustFanStatus = (short) msg.optInt(MsgParams.sterilExhaustFanStatus);
                this.steamLockStatus = (short) msg.optInt(MsgParams.steamLockStatus);
                this.steamWorkStatus = (short) msg.optInt(MsgParams.steamWorkStatus, -1);
                this.steamAlarmStatus = (short) msg.optInt(MsgParams.steamAlarmStatus);
                this.steamRunModel = (short) msg.optInt(MsgParams.steamRunModel);
                this.steamWorkTemp = (short) msg.optInt(MsgParams.steamWorkTemp);
                this.steamWorkRemainingTime = (short) msg.optInt(MsgParams.steamWorkRemainingTime);
                this.steamDoorState = (short) msg.optInt(MsgParams.steamDoorState);
                this.steamSetTemp = (short) msg.optInt(MsgParams.steamSetTemp);
                this.steamSetTime = (short) msg.optInt(MsgParams.steamSetTime);
                this.steamLightState = (short) msg.optInt(MsgParams.steamLightState);
                this.steamRemoveOilySoiled = (short) msg.optInt(MsgParams.steamRemoveOilySoiled);
                this.steamRecipeId = (short) msg.optInt(MsgParams.steamRecipeId);
                this.steamRecipeStep = (short) msg.optInt(MsgParams.steamRecipeStep);
                this.steamWaterSwitchStatus = (short) msg.optInt(MsgParams.steamWaterSwitchStatus);
                this.steamWasteWaterExcessive = (short) msg.optInt(MsgParams.steamWasteWaterExcessive);
                this.steamWasteWaterStatus = (short) msg.optInt(MsgParams.steamWasteWaterStatus);
                this.steamExhaustFanStatus = (short) msg.optInt(MsgParams.steamExhaustFanStatus);
                this.steamOvenLockStatus = (short) msg.optInt(MsgParams.steamOvenLockStatus);
                this.steamOvenWorkStatus = (short) msg.optInt(MsgParams.steamOvenWorkStatus,-3);
                this.steamOvenAlarmStatus = (short) msg.optInt(MsgParams.steamOvenAlarmStatus);
                this.steamOvenRunModel = (short) msg.optInt(MsgParams.steamOvenRunModel);
                this.steamOvenWorkTemp = (short) msg.optInt(MsgParams.steamOvenWorkTemp);
                this.steamOvenTimeWorkRemaining = (short) msg.optInt(MsgParams.steamOvenTimeWorkRemaining);
                this.steamOvenDoorState = (short) msg.optInt(MsgParams.steamOvenDoorState);
                this.steamOvenSetTemp = (short) msg.optInt(MsgParams.steamOvenSetTemp);
                this.steamOvenSetTime = (short) msg.optInt(MsgParams.steamOvenSetTime);
                this.steamOvenPreFlag = (short) msg.optInt(MsgParams.steamOvenPreFlag);
                this.steamOvenOrderTime = (short) msg.optInt(MsgParams.steamOvenOrderTime);
                this.steamOvenLight = (short) msg.optInt(MsgParams.steamOvenLight);
                this.steamOvenTotalNumber = (short) msg.optInt(MsgParams.steamOvenTotalNumber);
                this.steamOvenCurrentNumber = (short) msg.optInt(MsgParams.steamOvenCurrentNumber);
                this.steamOvenAutomaticRecipe = (short) msg.optInt(MsgParams.steamOvenAutomaticRecipe);
                this.steamOvenCleanOil = (short) msg.optInt(MsgParams.steamOvenCleanOil);

                this.steamOvenAbolishWaterOverproof = (short) msg.optInt(MsgParams.steamOvenAbolishWaterOverproof);
                this.steamOvenAbolishWaterRemind = (short) msg.optInt(MsgParams.steamOvenAbolishWaterRemind);
                this.steamOvenAbolishWaterTake = (short) msg.optInt(MsgParams.steamOvenAbolishWaterTake);
                this.steamOvenAbolishWaterSwitchStatus = (short) msg.optInt(MsgParams.steamOvenAbolishWaterSwitchStatus);
                onStatusChanged();
                break;

            case MsgKeys.alarmEventReport:
                short fanAlarmCode = (short) msg.optInt(MsgParams.fanAlarmCode);
                short stoveAlarmCode = (short) msg.optInt(MsgParams.stoveAlarmCode);
                short sterilAlarmCode = (short) msg.optInt(MsgParams.sterilAlarmCode);
                short steamAlarmCode = (short) msg.optInt(MsgParams.steamAlarmCode);
                short steamOvenAlarmCode = (short) msg.optInt(MsgParams.steamOvenAlarmCode);
                mRikaAlarmCodeBean = new RikaAlarmCodeBean(fanAlarmCode, stoveAlarmCode, sterilAlarmCode, steamAlarmCode, steamOvenAlarmCode);

                postEvent(new RikaAlarmEvent(AbsRika.this, mRikaAlarmCodeBean));
                break;

            case MsgKeys.readIntelligentInteractiveModeSetting_Req:

                break;

            case MsgKeys.eventReport:
                short categoryCode = (short) msg.optInt(MsgParams.categoryCode);
                switch (categoryCode) {

                    case 'A':
                        short fanEventCode = (short) msg.optInt(MsgParams.fanEventCode);
                        short fanEventArg = (short) msg.optInt(MsgParams.fanEventArg);
                        LogUtils.i("20180625", " fanEventCode:" + fanEventCode);
                        LogUtils.i("20180625", " fanEventArg:" + fanEventArg);
                        switch (fanEventCode) {
                            case RikaEventCode.EVENT_FAN_CLEANING_TIPS:
                                postEvent(new RikaFanCleanNoticEvent(this));
                                break;

                        }

                        break;

                    case 'B':
                        short stoveEventCode = (short) msg.optInt(MsgParams.stoveEventCode);
                        short stoveEventArg = (short) msg.optInt(MsgParams.stoveEventArg);
                        break;
                    case 'C':
                        short sterilEventCode = (short) msg.optInt(MsgParams.sterilEventCode);
                        short sterilEventArg = (short) msg.optInt(MsgParams.sterilEventArg);

                        switch (sterilEventCode) {

                            case RikaEventCode.EVENT_STERIL_WORK_FINISH:
                                postEvent(new RikaSterilizerWorkFinishEvent(this, sterilEventArg));
                                break;

                            case RikaEventCode.EVENT_STERIL_CHILD_LOCK:
                                postEvent(new RikaSterilizerChildLockEvent(this, sterilEventArg));
                                break;
                        }
                        break;
                    case 'D':
                        short steamEventCode = (short) msg.optInt(MsgParams.steamEventCode);
                        short steamEventArg = (short) msg.optInt(MsgParams.steamEventArg);
                        postEvent(new RikaSteamWorkEvent(this, steamEventCode, steamEventArg));
                        LogUtils.i("20180526", " steamEventCode:" + steamEventCode + " steamEventArg:" + steamEventArg);
                        break;
                    case 'E':
                        short steamOvenEventCode = (short) msg.optInt(MsgParams.steamOvenEventCode);
                        short steamOvenEventArg = (short) msg.optInt(MsgParams.steamOvenEventArg);
                        postEvent(new RikaSteamOvenWorkEvent(this, steamOvenEventCode, steamOvenEventArg));
                        LogUtils.i("20180526", " steamOvenEventCode:" + steamOvenEventCode + " steamOvenEventArg:" + steamOvenEventArg);
                        break;
                }

                LogUtils.i("20180515", " categoryCode:" + categoryCode);

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

    //rika烟机开关状态控制
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
                protected void afterSuccess(Msg resMsg) {
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭烟机风量
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
    public void getSmartConfig(final Callback<RikaSmartParams> callback) {
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


    //Rika蒸设置烟灶联动
    @Override
    public void setSmartConfig(final RikaSmartParams smartParams, VoidCallback callback) {
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
                    AbsRika.this.smartParams = smartParams;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setSteamOvenSmartConfig(final RikaSmartParams smartParams, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setRikaIntelSet_Req);
            msg.putOpt(MsgParams.categoryCode, RikaStatus.STEAMQVEN_CATEGORYCODE);
            msg.putOpt(MsgParams.ArgumentNumber, 1);
            msg.putOpt(MsgParams.steamOvenHeader_Key, 1);
            msg.putOpt(MsgParams.steamOvenHeader_Leng, 2);
            msg.putOpt(MsgParams.fanSteamOvenLinkage, smartParams.fanSteamOvenLinkage);
            msg.putOpt(MsgParams.fanSteamOvenDelayShutdownSwitch, smartParams.fanSteamOvenDelayShutdownSwitch);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                @Override
                protected void afterSuccess(Msg resMsg) {
                    AbsRika.this.smartParams.fanSteamOvenDelayShutdownSwitch = smartParams.fanSteamOvenDelayShutdownSwitch;
                    AbsRika.this.smartParams.fanSteamOvenLinkage = smartParams.fanSteamOvenLinkage;
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


}






