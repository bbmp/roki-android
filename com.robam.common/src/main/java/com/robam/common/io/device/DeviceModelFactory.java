package com.robam.common.io.device;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.Oven016;
import com.robam.common.pojos.device.Oven.Oven026;
import com.robam.common.pojos.device.Oven.Oven028;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.pojos.device.Oven.Oven075;
import com.robam.common.pojos.device.Oven.OvenHK906;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.common.pojos.device.Steamoven.Steam226;
import com.robam.common.pojos.device.Steamoven.Steam228;
import com.robam.common.pojos.device.Steamoven.Steam275;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.common.pojos.device.Stove.OtherStove;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove9B30C;
import com.robam.common.pojos.device.Stove.Stove9B37;
import com.robam.common.pojos.device.Stove.Stove9B39;
import com.robam.common.pojos.device.Stove.StoveHI704;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierJ312;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierJ320;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierJ321;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.fan.Fan5610;
import com.robam.common.pojos.device.fan.Fan5910;
import com.robam.common.pojos.device.fan.Fan5910S;
import com.robam.common.pojos.device.fan.Fan66A2;
import com.robam.common.pojos.device.fan.Fan66A2H;
import com.robam.common.pojos.device.fan.Fan68A0;
import com.robam.common.pojos.device.fan.Fan8229;
import com.robam.common.pojos.device.fan.Fan8230;
import com.robam.common.pojos.device.fan.Fan8230C;
import com.robam.common.pojos.device.fan.Fan8230S;
import com.robam.common.pojos.device.fan.Fan8231S;
import com.robam.common.pojos.device.fan.Fan8700;
import com.robam.common.pojos.device.fan.Fan9700;
import com.robam.common.pojos.device.fan.OtherFan;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM509;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.Rika90B8X;
import com.robam.common.pojos.device.rika.Rika90B8Z;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenC906;


/**
 * Created by 14807 on 2018/1/19.
 */

public class DeviceModelFactory {

    public static IDevice generateModelByTypeAndCategory(SubDeviceInfo deviceInfo) {

        IDevice iDevice = generateParticularModelByType(deviceInfo);
        if (iDevice == null) {
            iDevice = generateCommonModelByDeviceCategory(deviceInfo);
        }
        return iDevice;
    }

    /**
     * 根据设备类型创建设备对象
     *
     * @param deviceInfo
     * @return
     */
    private static IDevice generateParticularModelByType(SubDeviceInfo deviceInfo) {
        Preconditions.checkState(!Strings.isNullOrEmpty(deviceInfo.guid), "guid is null or empty");
        String guid = deviceInfo.guid;
        LogUtils.i("20180612", "dt   deviceInfo:" + deviceInfo);
        if (deviceInfo instanceof DeviceInfo) {
            DeviceInfo devInfo = (DeviceInfo) deviceInfo;
            if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9700)) {
                return new Fan9700(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R8230)) {
                return new Fan8230(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._8230S)) {
                return new Fan8230S(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._8230C)) {
                return new Fan8230C(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._8231S)) {
                return new Fan8231S(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R5910)) {
                return new Fan5910(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._5910S)) {
                return new Fan5910S(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R8700)) {
                return new Fan8700(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R8229)) {
                return new Fan8229(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R66A2)) {
                return new Fan66A2(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily._66A2H)) {
                return new Fan66A2H(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R5610)) {
                return new Fan5610(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R68A0)) {
                return new Fan68A0(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS209)) {
                return new Steam209(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS226)) {
                return new Steam226(devInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily.HS906)){
                return new Steam228(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS228)) {
                return new Steam228(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RS275)) {
                return new Steam275(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR829)) {
                return new Steri829(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR039)) {
                return new Oven039(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR026)) {
                return new Oven026(devInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily.HK906)){
                return new OvenHK906(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR016)) {
                return new Oven016(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR028)) {
                return new Oven028(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR075)) {
                return new Oven075(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ312)) {
                return new WaterPurifierJ312(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ321)) {
                return new WaterPurifierJ321(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ320)) {
                return new WaterPurifierJ320(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RM509)) {
                return new MicroWaveM509(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RM526)) {
                return new MicroWaveM526(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RC906)) {
                return new SteamOvenC906(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RR826)) {
                return new Steri826(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RIKA_Z)) {
                return new Rika90B8Z(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RIKA_X)) {
                return new Rika90B8X(devInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.KDC01)){
                return new AbsCooker(devInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily.R0003)){
                return new GasSensor(devInfo);
            }else if(DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily.WB755)){
                return new AbsDishWasher(devInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.KC306)) {
                return new AbsHidKit(devInfo);
            }else{
                return null;
            }
        } else {
            if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9W70)) {
                return new Stove(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9W851)) {
                return new Stove(deviceInfo);
            }  else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.HI704)){
                return new StoveHI704(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B39)) {
                return new Stove9B39(deviceInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily._9B30C)){
                return new Stove9B30C(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B37)) {
                return new Stove9B37(deviceInfo);
            } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R0001)) {
                return new Pot(deviceInfo);
            }else if (DeviceTypeManager.getInstance().isInDeviceType(guid,IRokiFamily.R0003)){
                return new GasSensor(deviceInfo);
            } else {
                //TODO
                return null;
            }
        }
    }

    /**
     * 根据设备品类创建设备模型对象
     *
     * @param deviceInfo
     * @return
     */
    private static IDevice generateCommonModelByDeviceCategory(SubDeviceInfo deviceInfo) {
        String deviceCategory = deviceInfo.getDc();
        IDevice iDevice = null;
        if (deviceInfo instanceof DeviceInfo) {
            DeviceInfo devInfo = (DeviceInfo) deviceInfo;
            switch (deviceCategory) {
                case IDeviceType.RYYJ:
                    iDevice = new OtherFan(devInfo);

                    break;
                case IDeviceType.RXDG:
                    iDevice = new Steri826(devInfo);
                    break;
                case IDeviceType.RZQL:
                    iDevice = new AbsSteamoven(devInfo);
                    break;
                case IDeviceType.RWBL:
                    iDevice = new AbsMicroWave(devInfo);
                    break;
                case IDeviceType.RJSQ:
                    iDevice = new AbsWaterPurifier(devInfo);
                    break;
                case IDeviceType.RDKX:
                    iDevice = new AbsOven(devInfo);
                    break;
                case IDeviceType.RZKY:
                    iDevice = new AbsSteameOvenOne(devInfo);
                    break;
                case IDeviceType.RIKA:
                    iDevice = new AbsRika(devInfo);
                    break;
                case IDeviceType.RJCZ:
                    iDevice = new AbsIntegratedStove(devInfo);
                    break;
                case IDeviceType.RCBH:
                    iDevice = new AbsHidKit(devInfo);
                    break;
                case IDeviceType.RPOT:
                    iDevice = new Pot(deviceInfo);
                    break;
                case IDeviceType.RDCZ:
                    iDevice = new Stove(deviceInfo);
                    break;
                case IDeviceType.RRQZ:
                    iDevice = new Stove(deviceInfo);
                    break;
                case IDeviceType.RQCG:
                    iDevice = new GasSensor(deviceInfo);
                    break;
                case IDeviceType.RXWJ:
                    iDevice =  new AbsDishWasher(devInfo);
                    break;
            }
        } else {
            switch (deviceCategory) {
                case IDeviceType.RPOT:
                    iDevice = new Pot(deviceInfo);
                    break;
                case IDeviceType.RDCZ:
                case IDeviceType.RRQZ:
                    iDevice = new OtherStove(deviceInfo);
                    break;
                case IDeviceType.RQCG:
                    iDevice = new GasSensor(deviceInfo);
                    break;
            }
        }
        return iDevice;
    }

}
