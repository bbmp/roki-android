package com.robam.common.recipe.step;

import android.speech.tts.UtteranceProgressListener;

import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenC906;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkModelStatus;

/**
 * Created by Dell on 2018/4/2.
 */

public class DeviceStatusCheck {
    public static DeviceStatusCheck getInstance() {
        return new DeviceStatusCheck();
    }


    public boolean getStatus(String dc, String headId) {
        if ("RDKX".equals(dc)) {
            AbsOven oven = Utils.getDefaultOven();
            if (oven != null) {
                return oven.status == OvenStatus.PreHeat || oven.status == OvenStatus.Working ||
                        oven.status == OvenStatus.Pause;
            }
        } else if ("RZQL".equals(dc)) {
            AbsSteamoven steamoven = Utils.getDefaultSteam();
            if (steamoven != null) {
                LogUtils.i("20180419", "status:;" + steamoven.status);
                return steamoven.status == SteamStatus.PreHeat || steamoven.status == SteamStatus.Working ||
                        steamoven.status == SteamStatus.Pause;
            }
        } else if ("RWBL".equals(dc)) {
            AbsMicroWave microWave = Utils.getDefaultMicrowave();
            if (microWave != null) {

                return microWave.state == MicroWaveStatus.Run || microWave.state == MicroWaveStatus.Pause;
            }
        } else if ("RZKY".equals(dc)) {
            AbsSteameOvenOne absSteameOvenOne = Utils.getDefaultSteameOven();
            if (absSteameOvenOne != null) {
                return absSteameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                        absSteameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause;
            }
        } else if ("RRQZ".equals(dc)) {
            Stove stove = Utils.getDefaultStove();

            if (stove != null) {
                if (headId == "0") {
                    return stove.leftHead.status == 2;
                } else {
                    LogUtils.i("20180413", "stove__status::" + stove.rightHead.status);
                    return stove.rightHead.status == 2;
                }
            }


        }
        return false;
    }

    public boolean getDeviceConnect(String dc) {
        LogUtils.i("20180403", "dc::" + dc);
        if ("RDKX".equals(dc)) {
            AbsOven oven = Utils.getDefaultOven();
            if (oven != null) {
                return !oven.isConnected();
            }
        } else if ("RZQL".equals(dc)) {
            AbsSteamoven steamoven = Utils.getDefaultSteam();
            if (steamoven != null) {
                return !steamoven.isConnected();
            }
        } else if ("RWBL".equals(dc)) {
            AbsMicroWave microWave = Utils.getDefaultMicrowave();
            if (microWave != null) {
                return !microWave.isConnected();

            }
        } else if ("RZKY".equals(dc)) {
            AbsSteameOvenOne absSteameOvenOne = Utils.getDefaultSteameOven();
            if (absSteameOvenOne != null) {
                return !absSteameOvenOne.isConnected();
            }
        } else if ("RRQZ".equals(dc)) {
            Stove stove = Utils.getDefaultStove();
            if (stove != null) {
                return !stove.isConnected();
            }
        }
        return false;
    }

    public boolean getIsLock(String dc) {
        if ("RRQZ".equals(dc)) {
            Stove stove = Utils.getDefaultStove();
            return stove.isLock;
        }
        return false;
    }

}
