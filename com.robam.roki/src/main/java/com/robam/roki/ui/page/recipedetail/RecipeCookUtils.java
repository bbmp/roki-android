package com.robam.roki.ui.page.recipedetail;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.roki.R;

public class RecipeCookUtils {
    private RecipeCookUtils() {}

    private static class RecipeCookUtilsInstance {
        private static final RecipeCookUtils INSTANCE = new RecipeCookUtils();
    }

    public static RecipeCookUtils getInstance() {
        return RecipeCookUtilsInstance.INSTANCE;
    }
    /**
     * 是否报警
     * @param idevice
     * @return
     */
    public Boolean isAlram(IDevice idevice) {
        switch (idevice.getDc()) {
            case "RDKX":
                AbsOven deviceOven = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceOven.status == OvenStatus.AlarmStatus) {
                    return true;
                }
                break;
            case "RWBL":
                AbsMicroWave deviceMicro = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceMicro.state == MicroWaveStatus.Alarm) {
                    return true;
                }
                break;
            case "RZQL":
                AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceSteam.status == SteamStatus.AlarmStatus) {
                    return true;
                }
                break;
            case "RZKY":
                AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (steamOvenOen.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public  Boolean isOpenDoor(IDevice idevice) {
        switch (idevice.getDc()) {
            case "RZQL":
                AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceSteam.doorState == 0) {
                    return true;
                }
                break;
            case "RZKY":
                AbsSteameOvenOne deviceSteam2 = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceSteam2.doorStatusValue == 1) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public   Boolean isWaterBoxState(IDevice idevice) {
        switch (idevice.getDc()) {
            case "RZQL":
                AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (IRokiFamily.RS209.equals(deviceSteam.getDt())) {
                    return false;
                }
                if (deviceSteam.waterboxstate == 0) {
                    return true;
                }
                break;
            case "RZKY":
                AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (steamOvenOen.WaterStatus == 1 && (steamOvenOen.workModel > 12 && steamOvenOen.workModel < 23)) {
                    ToastUtils.show(R.string.device_alarm_water_out);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }



    public  Boolean isWhichDevice(IDevice idevice) {
        switch (idevice.getDc()) {
            case "RDKX":
                AbsOven deviceOven = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceOven.status == OvenStatus.Working || deviceOven.status == OvenStatus.Pause ||
                        deviceOven.status == OvenStatus.PreHeat) {
                    return true;
                }
                break;
            case "RWBL":
                AbsMicroWave deviceMicro = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceMicro.state == MicroWaveStatus.Pause || deviceMicro.state == MicroWaveStatus.Run) {
                    return true;
                }
                break;
            case "RZQL":
                AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (deviceSteam.status == SteamStatus.Pause || deviceSteam.status == SteamStatus.PreHeat ||
                        deviceSteam.status == SteamStatus.Working) {
                    return true;
                }
                break;
            case "RZKY":
                AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                if (steamOvenOen.powerOnStatus == SteamOvenOnePowerOnStatus.Pause || steamOvenOen.powerOnStatus
                        == SteamOvenOnePowerOnStatus.WorkingStatus) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

}
