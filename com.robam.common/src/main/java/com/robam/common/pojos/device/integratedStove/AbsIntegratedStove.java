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
import com.legent.utils.ByteUtils;
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
    public short steamOvenWorkStatus;//一体机工作状态
    public short steamOvenRunModel;//一体机运行模式
    public short steamOvenWorkTemp;//一体机工作温度
    public short steamOvenTimeWorkRemaining;//一体机工作剩余时间

    public short steamOvenCleanOil;//一体机是否需要清油

    public short terminalType = TerminalType.getType();
    IntegStoveAlarmCodeBean mRikaAlarmCodeBean = null;
    IntegStoveSmartParams smartParams = new IntegStoveSmartParams();

    private int childCategoryCode ;

/*-----------------------------------------------------一体机工作参数-------------------------------------------------------------------*/
    /**
     * 电源状态
     */
    public short powerState ;
    /**
     * 电源控制
     */
    public short  powerCtrl  ;
    /**
     * 工作状态
     */
    public short  workState  ;
    /**
     * 工作控制
     */
    public short  workCtrl   ;
    /**
     * 剩余预约时间
     */
    public short  orderLeftMinutes ;
    /**
     * 故障码
     */
    public short  faultCode ;
    /**
     * 旋转烤开关
     */
    public short  rotateSwitch  ;
    /**
     * 水箱状态
     */
    public short  waterBoxState  ;
    /**
     * 水位状态
     */
    public short  waterLevelState  ;
    /**
     * 门状态
     */
    public short  doorState  ;
    /**
     * 加蒸汽工作状态
     */
    public short  steamState  ;
    /**
     * 菜谱编号
     */
    public short  recipeId ;
    /**
     * 菜谱设置总时间
     */
    public short  recipeSetMinutes  ;
    /**
     * 当前温度 上温度
     */
    public short  curTemp  ;
    /**
     * 当前温度 下温度
     */
    public short  curTemp2 ;
    /**
     * 剩余总时间
     */
    public short  totalRemainSeconds ;
    public short  totalRemainSecondsH ;
    /**
     * 除垢请求标识
     */
    public short  descaleFlag  ;
    /**
     * 当前蒸模式累计工作时间
     */
    public short  curSteamTotalHours  ;
    /**
     * 蒸模式累计需除垢时间
     */
    public short  curSteamTotalNeedHours  ;
    /**
     * 实际运行时间
     */
    public short  cookedTime  ;
    /**
     * 除垢状态
     */
    public short  chugouType  ;
    /**
     * 当前段数/段序
     */
    public short  curSectionNbr ;
    /**
     * 设置段数
     */
    public short  sectionNumber  ;
    /**
     * 首段模式
     */
    public short  mode  ;
    /**
     * 首段设置的上温度
     */
    public short  setUpTemp  ;
    /**
     * 首段设置的下温度
     */
    public short  setDownTemp ;
    /**
     * 首段设置的时间
     */
    public short  setTime  ;
    public short  setTimeH  ;
    /**
     * 首段剩余的时间
     */
    public short  restTime  ;
    public short  restTimeH  ;
    /**
     * 首段蒸汽量
     */
    public short  steam  ;

    /**
     * 第二段模式
     */
    public short  mode1  ;
    /**
     * 2段设置的上温度
     */
    public short  setUpTemp1 ;
    /**
     * 2段设置的下温度
     */
    public short  setDownTemp1 ;
    /**
     * 2段设置的时间
     */
    public short  setTime1  ;
    /**
     * 2段剩余的时间
     */
    public short  restTime1  ;
    /**
     * 2段蒸汽量
     */
    public short  steam1  ;


    /**
     * 3段模式
     */
    public short  mode2  ;
    /**
     * 3段设置的上温度
     */
    public short  setUpTemp2  ;
    /**
     * 3段设置的下温度
     */
    public short  setDownTemp2 ;
    /**
     * 3段设置的时间
     */
    public short  setTime2  ;
    /**
     * 3段剩余的时间
     */
    public short  restTime2  ;
    /**
     * 3段蒸汽量
     */
    public short  steam2  ;

    /*-----------------------------------------------------烟机工作参数-------------------------------------------------------------------*/
    /**
     * 电源状态 烟机
     */
    public short  fan_powerState ;
    /**
     * 档位 烟机
     */
    public short  fan_gear ;
    /**
     * 灯开关 烟机
     */
    public short  fan_lightSwitch  ;
    /**
     * 烟灶联动开关 烟机
     */
    public short  fan_stove_linkage  ;

    /*---------------------------------------------------------灶具--------------------------------------------------------------*/
    /**
     * 电源状态 灶具
     */
    public short  stove_powerState ;

    /**
     * 电源控制 灶具
     */
    public short  stove_powerCtrl ;

    /**
     * 炉头数量 灶具
     */
    public short  stove_head_num  ;
    /**
     * 炉头类型 灶具
     */
    public short  stove_head_type ;
    /**
     * 功率档位 灶具
     */
    public short  stove_gear  ;

    /**
     * 童锁 灶具
     */
    public short  stove_v_chip ;
    /**
     * 菜谱ID 灶具
     */
    public short  repice_id ;
    /**
     * 灶具温度  灶具
     */
    public short  stove_temp  ;
    /**
     * 已工作时间 灶具
     */
    public short  stove_time  ;


    /**
     * 报警状态 灶具
     */
    public short  stove_faultCode  ;

    /*---------------------------------第二个炉头----------------------------------------*/
    /**
     * 电源状态 灶具
     */
    public short  stove_powerState2 ;

    /**
     * 电源控制 灶具
     */
    public short  stove_powerCtrl2 ;
    /**
     * 炉头类型 灶具
     */
    public short  stove_head_type2  ;
    /**
     * 功率档位 灶具
     */
    public short  stove_gear2  ;

    /**
     * 童锁 灶具
     */
    public short  stove_v_chip2  ;
    /**
     * 菜谱ID 灶具
     */
    public short  repice_id2  ;
    /**
     * 灶具温度  灶具
     */
    public short  stove_temp2  ;
    /**
     * 已工作时间 灶具
     */
    public short  stove_time2  ;

    /**
     * 报警状态 灶具
     */
    public short  stove_faultCode2  ;

    //-------------------------------接口返回业务数据-----------------------------------------
    /*------------------------菜谱对应----------------------------*/
    /**
     * 菜谱id 菜谱名对应map
     */
    public Map<String, String> cookMap ;
    /**
     * 菜谱id 是否需要水对应map
     */
    public Map<String, String> cookNeedWaterMap ;
    /**
     * 图标暂停
     */
    public String workingPauseUrl ;
    public String workingPauseHUrl ;
    /**
     * 结束
     */
    public String finishUrl ;
    /**
     * 加蒸汽
     */
    public String workingAddSteamUrl ;


    /**
     * 子设备需要单独查询 ，每次查询变更需要查询的子设备
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
                //设备品类
                short category = (short) msg.optInt(MsgParams.categoryCode);
                //故障码
                short fanAlarmCode = (short) msg.optInt(MsgParamsNew.faultCode);
                this.faultCode = fanAlarmCode ;
                postEvent(new IntegStoveAlarmEvent(AbsIntegratedStove.this , fanAlarmCode ,category));
//                SteamOvenFaultEnum fault = SteamOvenFaultEnum.match(faultCode);
//                if (fault )
//                switch (fault){
//                    //缺水故障
//                    case LACKWATER:
//                        this.waterLevelState = 1;
//                        break;
//                }

                postEvent(new IntegStoveAlarmEvent(AbsIntegratedStove.this , fanAlarmCode ,category));
//                onStatusChanged();
                break;
            case MsgKeys.getDeviceEventReport:
                //设备品类
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
                            //缺水事件移至故障表
//                            case WATER:
//                                this.waterLevelState = 1;
//                                break;
                            case START_WORK:
//                                this.workState = IntegStoveStatus.workState_work;
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



    //Rika蒸设置烟灶联动
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
     * 关闭烟机风量 、开启
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
     * 设置烟机单一状态
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
            //烟机状态控制
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
     * 一体机自动菜谱
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
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //菜谱id
            msg.putOpt(MsgParamsNew.recipeId, recipeId);
            msg.putOpt(MsgParamsNew.recipeIdKey, 17);
            msg.putOpt(MsgParamsNew.recipeIdLength, 1);
            //菜谱时间
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

            //总段数
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
     * 设置一体机参数 （单模式）
     * @param mode 模式
     * @param setTime 时间
     * @param setTemp 温度
     * @param orderTime 预约时间
     * @param callback
     */
    public void setSteameOvenOneRunMode(final short mode, final short setTime,
                                        final short setTemp,
                                        int orderTime, short steamFlow ,VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.setDeviceAttribute_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());

            msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
            //参数个数
                msg.putOpt(ArgumentNumber, 9);

            msg.putOpt(MsgParamsNew.type , 0) ;
            //一体机电源控制
            msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
            msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
            msg.putOpt(MsgParamsNew.powerCtrl, 1);
            //一体机工作控制
            msg.putOpt(MsgParamsNew.workCtrlKey, 4);
            msg.putOpt(MsgParamsNew.workCtrlLength, 1);
            msg.putOpt(MsgParamsNew.workCtrl, 1);
            //预约时间
            msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
            byte[] orderTimeBytes = ByteUtils.intToBytes2(orderTime);
            msg.putOpt(MsgParamsNew.setOrderMinutesLength, orderTimeBytes.length);
            msg.putOpt(MsgParamsNew.setOrderMinutes, orderTime);
            //段数
            msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
            msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
            msg.putOpt(MsgParamsNew.sectionNumber, 0) ;
            //模式
            msg.putOpt(MsgParamsNew.modeKey, 101) ;
            msg.putOpt(MsgParamsNew.modeLength, 1) ;
            msg.putOpt(MsgParamsNew.mode, mode) ;
            //温度上温度
            msg.putOpt(MsgParamsNew.setUpTempKey  , 102);
            msg.putOpt(MsgParamsNew.setUpTempLength, 1);
            msg.putOpt(MsgParamsNew.setUpTemp, setTemp);
            //时间
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
            //蒸汽量
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
    /**
     * 单独设置一体机单一状态 开始 暂停等
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

    public void setCookMap(Map<String, String> cookMap) {
        this.cookMap = cookMap;
    }
    public void setCookNeedWaterMap(Map<String, String> cookMap) {
        this.cookNeedWaterMap = cookMap;
    }
}






