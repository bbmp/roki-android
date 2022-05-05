package com.robam.common.io.device;

import com.legent.io.msgs.IMsg;
import com.legent.io.senders.AbsMsgSyncDecider;
import com.legent.plat.io.device.IAppMsgSyncDecider;
import com.legent.plat.io.device.msg.Msg;
import com.robam.common.Utils;

public class RokiMsgSyncDecider implements IAppMsgSyncDecider {

    FanDecider fanDecider = new FanDecider();
    StoveDecider stoveDecider = new StoveDecider();
    PotDecider potDecider = new PotDecider();
    GasSensorDecider gasSensorDecider = new GasSensorDecider();
    SteriDecider steriDecider = new SteriDecider();

    SteamOvenDecider steamOvenDecider = new SteamOvenDecider();
    MicroWaveDecider microWaveDecider = new MicroWaveDecider();
    OvenDecider ovenDecider = new OvenDecider();
    WaterPurifierDecider waterPurifier = new WaterPurifierDecider();
    SteamOvenOneDecider steamOvenOneDecider = new SteamOvenOneDecider();
    RokiDecider mRokiDecider = new RokiDecider();
    CookerDecider mCookerDecider = new CookerDecider();
    DishWasherDecider dishWasherDecider=new DishWasherDecider();
    HidKitDecider hidKitDecider = new HidKitDecider();
    /**
     * 新协议
     */
    NewCommonTreatyDecider newCommonTreatyDecider = new NewCommonTreatyDecider();

    @Override
    public long getSyncTimeout() {
        return 3000;
    }

    @Override
    public int getPairsKey(IMsg msg) {
        int res = 0;
        Msg m = (Msg) msg;
        String devGuid = m.getDeviceGuid().getGuid();
        if (Utils.isFan(devGuid)) {
            res = fanDecider.getPairsKey(msg);
        } else if (Utils.isStove(devGuid)) {
            res = stoveDecider.getPairsKey(msg);
        }else if (Utils.isPot(devGuid)) {
            res = potDecider.getPairsKey(msg);
        }else if(Utils.isGasSensor(devGuid)){
            res = gasSensorDecider.getPairsKey(msg);
        } else if (Utils.isSterilizer(devGuid)) {
            res = steriDecider.getPairsKey(msg);
        } else if (Utils.isSteam(devGuid)) {
            res = steamOvenDecider.getPairsKey(msg);
        } else if (Utils.isMicroWave(devGuid)) {
            res = microWaveDecider.getPairsKey(msg);
        } else if (Utils.isOven(devGuid)) {
            res = ovenDecider.getPairsKey(msg);
        } else if (Utils.isWaterPurifier(devGuid)) {
            res = waterPurifier.getPairsKey(msg);
        } else if (Utils.isSteamOvenMsg(devGuid)) {
            res = steamOvenOneDecider.getPairsKey(msg);
        } else if (Utils.isRikaMsg(devGuid)) {
            res = mRokiDecider.getPairsKey(msg);
        }else if (Utils.isCooker(devGuid)){
            res = mCookerDecider.getPairsKey(msg);
        }else if (Utils.isDishWasher(devGuid)){
            res=dishWasherDecider.getPairsKey(msg);
        }else if (Utils.isHidKitMsg(devGuid)) {
            res = hidKitDecider.getPairsKey(msg);
        }else if (Utils.isRJCZMsg(devGuid)){
            res = newCommonTreatyDecider.getPairsKey(msg);
        }
        return res;
    }

    class FanDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.GetFanStatus_Req, MsgKeys.GetFanStatus_Rep);
            addPairsKey(MsgKeys.SetFanStatus_Req, MsgKeys.SetFanStatus_Rep);
            addPairsKey(MsgKeys.SetFanLevel_Req, MsgKeys.SetFanLevel_Rep);
            addPairsKey(MsgKeys.SetFanLight_Req, MsgKeys.SetFanLight_Rep);
            addPairsKey(MsgKeys.SetFanAllParams_Req, MsgKeys.SetFanAllParams_Rep);
            addPairsKey(MsgKeys.RestFanCleanTime_Req, MsgKeys.RestFanCleanTime_Rep);
            addPairsKey(MsgKeys.RestFanNetBoard_Req, MsgKeys.RestFanNetBoard_Rep);
            addPairsKey(MsgKeys.SetFanTimeWork_Req, MsgKeys.SetFanTimeWork_Rep);
            addPairsKey(MsgKeys.GetSmartConfig_Req, MsgKeys.GetSmartConfig_Rep);
            addPairsKey(MsgKeys.SetSmartConfig_Req, MsgKeys.SetSmartConfig_Rep);
            addPairsKey(MsgKeys.FanAddPot_Req, MsgKeys.FanAddPot_Rep);
            addPairsKey(MsgKeys.FanDelPot_Req, MsgKeys.FanDelPot_Rep);
            addPairsKey(MsgKeys.SetFanStatusCompose_Req, MsgKeys.SetFanStatusCompose_Rep);
            addPairsKey(MsgKeys.SetFanCleanOirCupTime_Req, MsgKeys.SetFanCleanOirCupTime_Rep);
            addPairsKey(MsgKeys.SetFanTimingRemind_Req, MsgKeys.SetFanTimingRemind_Rep);
        }
    }

    class GasSensorDecider extends AbsMsgSyncDecider{
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.GasSensor_Status_Check_Req, MsgKeys.GasSensor_Status_Check_Rep);
            addPairsKey(MsgKeys.GasSensor_SetCheckSelf_Req, MsgKeys.GasSensor_SetCheckSelf_Rep);
        }
    }

    class StoveDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.GetStoveStatus_Req, MsgKeys.GetStoveStatus_Rep);
            addPairsKey(MsgKeys.SetStoveStatus_Req, MsgKeys.SetStoveStatus_Rep);
            addPairsKey(MsgKeys.SetStoveLevel_Req, MsgKeys.SetStoveLevel_Rep);
            addPairsKey(MsgKeys.SetStoveShutdown_Req, MsgKeys.SetStoveShutdown_Rep);
            addPairsKey(MsgKeys.SetStoveLock_Req, MsgKeys.SetStoveLock_Rep);
            addPairsKey(MsgKeys.SetPowerOn_Req,MsgKeys.SetPowerOn_Rep);
            addPairsKey(MsgKeys.setTimePowerOff_Req, MsgKeys.setTimePowerOff_Rep);
            addPairsKey(MsgKeys.setPowerOff_Look_Req, MsgKeys.setPowerOff_Look_Rep);
          //  addPairsKey(MsgKeys.Set_Oven_More_Cook, MsgKeys.Get_Oven_More_Cook);
        }
    }

    class PotDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.GetPotTemp_Req, MsgKeys.SetPotTemp_Rep);
            addPairsKey(MsgKeys.SetPotCom_Req, MsgKeys.GetPotCom_Rep);
            addPairsKey(MsgKeys.SetPotSwitch_Req, MsgKeys.GetPotSwitch_Rep);
        }
    }

    class SteriDecider extends AbsMsgSyncDecider {

        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.SetSteriPowerOnOff_Req, MsgKeys.SetSteriPowerOnOff_Rep);
            addPairsKey(MsgKeys.SetSteriReserveTime_Req, MsgKeys.SetSteriReserveTime_Rep);
            addPairsKey(MsgKeys.SetSteriDrying_Req, MsgKeys.SetSteriDrying_Rep);
            addPairsKey(MsgKeys.SetSteriClean_Req, MsgKeys.SetSteriClean_Rep);
            addPairsKey(MsgKeys.SetSteriDisinfect_Req, MsgKeys.SetSteriDisinfect_Rep);
            addPairsKey(MsgKeys.SetSteriLock_Req, MsgKeys.SetSteriLock_Rep);
            addPairsKey(MsgKeys.GetSteriParam_Req, MsgKeys.GetSteriParam_Rep);
            addPairsKey(MsgKeys.GetSteriStatus_Req, MsgKeys.GetSteriStatus_Rep);
            addPairsKey(MsgKeys.GetSteriPVConfig_Req, MsgKeys.GetSteriPVConfig_Rep);
            addPairsKey(MsgKeys.SetSteriPVConfig_Req, MsgKeys.SetSteriPVConfig_Rep);

        }
    }

    class SteamOvenDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.setSteamTemp_Req, MsgKeys.setSteamTemp_Rep);
            addPairsKey(MsgKeys.setSteamMode_Req, MsgKeys.setSteamMode_Rep);
            addPairsKey(MsgKeys.setSteamProMode_Req, MsgKeys.setSteamProMode_Rep);
            addPairsKey(MsgKeys.GetSteamOvenStatus_Req, MsgKeys.GetSteamOvenStatus_Rep);
            addPairsKey(MsgKeys.setSteamStatus_Req, MsgKeys.setSteamStatus_Rep);
            addPairsKey(MsgKeys.setSteamTime_Req, MsgKeys.setSteamTime_Rep);
            addPairsKey(MsgKeys.SetSteamRecipeReq, MsgKeys.GetSteamRecipeRep);
            addPairsKey(MsgKeys.SetSteamLightReq, MsgKeys.GetSteamLightRep);
            addPairsKey(MsgKeys.SetSteamWaterTankPOPReq, MsgKeys.GetSteamWaterTankPOPRep);
            addPairsKey(MsgKeys.SetLocalRecipeReq, MsgKeys.GetLocalRecipeRep);
        }
    }

    class MicroWaveDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.setMicroWaveStatus_Req, MsgKeys.setMicroWaveStates_Rep);
            addPairsKey(MsgKeys.setMicroWaveKindsAndHeatCold_Req, MsgKeys.setMicroWaveKindsAndHeatCold_Rep);
            addPairsKey(MsgKeys.setMicroWaveProModeHeat_Req, MsgKeys.setMicroWaveProModeHeat_Rep);
            addPairsKey(MsgKeys.setMicroWaveLight_Req, MsgKeys.setMicroWaveLight_Rep);
            addPairsKey(MsgKeys.getMicroWaveStatus_Req, MsgKeys.getMicroWaveStatus_Rep);
            addPairsKey(MsgKeys.setMicroWaveLinkedCook_Req, MsgKeys.setMicroWaveLinkedCook_Rep);
            addPairsKey(MsgKeys.setMicroWaveClean_Req, MsgKeys.setMicroWaveClean_Rep);
        }
    }

    class OvenDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.setOvenStatusControl_Req, MsgKeys.setOvenStatusControl_Rep);
            addPairsKey(MsgKeys.setOvenQuickHeat_Req, MsgKeys.setOvenQuickHeat_Rep);
            addPairsKey(MsgKeys.setOvenAirBaking_Req, MsgKeys.setOvenAirBaking_Rep);
            addPairsKey(MsgKeys.setOvenAirBarbecue_Req, MsgKeys.setOvenAirBarbecue_Rep);
            addPairsKey(MsgKeys.setOvenBarbecue_Req, MsgKeys.setOvenBarbecue_Rep);
            addPairsKey(MsgKeys.setOvenBottomHeat_Req, MsgKeys.setOvenBottomHeat_Rep);
            addPairsKey(MsgKeys.setOvenSpitRotateLightControl_Req, MsgKeys.setOvenSpitRotateLightControl_Rep);
            addPairsKey(MsgKeys.setOvenStrongBarbecue_Req, MsgKeys.setOvenStrongBarbecue_Rep);
            addPairsKey(MsgKeys.setOvenBottomHeat_Req, MsgKeys.setOvenBottomHeat_Rep);
            addPairsKey(MsgKeys.setOvenUnfreeze_Req, MsgKeys.setOvenUnfreeze_Rep);
            addPairsKey(MsgKeys.setOvenToast_Req, MsgKeys.setOvenToast_Rep);
            addPairsKey(MsgKeys.getOvenStatus_Req, MsgKeys.getOvenStatus_Rep);
            addPairsKey(MsgKeys.SetOven_RunMode_Req, MsgKeys.GetOven_RunMode_Rep);
            addPairsKey(MsgKeys.SetSteamOven_Recipe_Req, MsgKeys.GetOven_Recipe_Rep);
            addPairsKey(MsgKeys.Set_Oven_Auto_Mode_Req, MsgKeys.Get_Oven_Auto_Mode_Rep);
            addPairsKey(MsgKeys.Set_Oven_Light_Req, MsgKeys.Get_Oven_Light_Rep);
            addPairsKey(MsgKeys.Set_Oven_More_Cook, MsgKeys.Get_Oven_More_Cook);
        }
    }

    class WaterPurifierDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.SetWaterPurifiyWorking_Req, MsgKeys.SetWaterPurifiyWorking_Rep);
            addPairsKey(MsgKeys.SetWaterPurifiySmart_Req, MsgKeys.getWaterPurifiySmart_Rep);
            addPairsKey(MsgKeys.getWaterPurifierStatusSmart_Req, MsgKeys.getWaterPurifierStatusSmart_Rep);
        }
    }

    class SteamOvenOneDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.setSteameOvenStatusControl_Req, MsgKeys.setSteameOvenStatusControl_Rep);
            addPairsKey(MsgKeys.getSteameOvenStatus_Req, MsgKeys.getSteameOvenStatus_Rep);
            addPairsKey(MsgKeys.setSteameOvenBasicMode_Req, MsgKeys.setSteameOvenBasicMode_Rep);
            addPairsKey(MsgKeys.setTheRecipe_Req, MsgKeys.setTheRecipe_Rep);
            //2020年3月9日 13:29:30 去掉160 161指令
//            addPairsKey(MsgKeys.setSteameOvenAutomaticMode_Req, MsgKeys.setSteameOvenAutomaticMode_Rep);
            addPairsKey(MsgKeys.setSteameOvenFloodlight_Req, MsgKeys.setSteameOvenFloodlight_Rep);
            addPairsKey(MsgKeys.setSteameOvensteam_Req, MsgKeys.setSteameOvensteam_Rep);
            addPairsKey(MsgKeys.setSteameOvenMultistageCooking_Req, MsgKeys.setSteameOvenMultistageCooking_Rep);
            addPairsKey(MsgKeys.setSteameOvenWater_Req, MsgKeys.setSteameOvenWater_Rep);
            addPairsKey(MsgKeys.setSteameOvenAutoRecipeMode_Req, MsgKeys.setSteameOvenAutoRecipeMode_Rep);
            addPairsKey(MsgKeys.setSteameOvenAutoRecipeMode610_Req, MsgKeys.setSteameOvenAutoRecipeMode610_Rep);

        }
    }

    class RokiDecider extends AbsMsgSyncDecider {
        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.readIntelligentInteractiveModeSetting_Req,MsgKeys.readIntelligentInteractiveModeSetting_Rep);
            addPairsKey(MsgKeys.setDeviceIntelligentInteractiveModel_Req,MsgKeys.setDeviceIntelligentInteractiveModel_Rep);
            addPairsKey(MsgKeys.setRikaIntelSet_Req,MsgKeys.setRikaIntelSet_Rep);
            addPairsKey(MsgKeys.readDeviceStatus_Req,MsgKeys.readDeviceStatus_Rep);
            addPairsKey(MsgKeys.setDeviceRunStatus_Req,MsgKeys.setDeviceRunStatus_Rep);
            addPairsKey(MsgKeys.setRikaOveAutoRecipe_Req,MsgKeys.setRikaOveAutoRecipe_Rep);
            addPairsKey(MsgKeys.setRikaOvenMultiStep_Req,MsgKeys.setRikaOvenMultiStep_Rep);
        }
    }

    class CookerDecider extends AbsMsgSyncDecider{

        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.deviceStatusQuery_Req,MsgKeys.deviceStatusQuery_Rep);
            addPairsKey(MsgKeys.setDeviceInformationQuery_Req,MsgKeys.setDeviceInformationQuery_Rep);
            addPairsKey(MsgKeys.setDeviceWorkStatus_Req,MsgKeys.setDeviceWorkStatus_Rep);
            addPairsKey(MsgKeys.setDeviceTemp_Req,MsgKeys.setDeviceTemp_Rep);
            addPairsKey(MsgKeys.setDeviceFire_Req,MsgKeys.setDeviceFire_Rep);
            addPairsKey(MsgKeys.setDeviceShutdownWork_Req,MsgKeys.setDeviceShutdownWork_Rep);
            addPairsKey(MsgKeys.setDeviceRecipeWork_Req,MsgKeys.setDeviceRecipeWork_Rep);
            addPairsKey(MsgKeys.setDeviceSetInformation_Req,MsgKeys.setDeviceSetInformation_Rep);
            addPairsKey(MsgKeys.setActionTempTure_Req,MsgKeys.setActionTempTure_Rep);

        }
    }

    class DishWasherDecider extends AbsMsgSyncDecider{

        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.setDishWasherPower,MsgKeys.getDishWasherPower);
            addPairsKey(MsgKeys.setDishWasherChildLock,MsgKeys.getDishWasherChildLock);
            addPairsKey(MsgKeys.setDishWasherWorkMode,MsgKeys.getDishWasherWorkMode);
            addPairsKey(MsgKeys.setDishWasherStatus,MsgKeys.getDishWasherStatus);
            addPairsKey(MsgKeys.setDishWasherUserOperate,MsgKeys.getDishWasherUserOperate);
        }
    }

    class HidKitDecider extends AbsMsgSyncDecider {

        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.getHidkitStatus_Req, MsgKeys.getHidkitStatus_Rep);
            addPairsKey(MsgKeys.setHidkitStatusCombined_Req, MsgKeys.setHidkitStatusCombined_Rep);
        }
    }

    /**
     * 新协议
     */
    class NewCommonTreatyDecider extends AbsMsgSyncDecider {

        @Override
        protected void initBiMap() {
            addPairsKey(MsgKeys.getDeviceAttribute_Req, MsgKeys.getDeviceAttribute_Rep);
            addPairsKey(MsgKeys.setDeviceAttribute_Req, MsgKeys.setDeviceAttribute_Rep);
        }
    }
}
