package com.robam.common.util;

import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.Utils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

import java.util.List;

/**
 * Created by Dell on 2018/2/3.
 */

public class DeviceSelUtils {

    private static class singleInstance{
        private static final DeviceSelUtils instance = new DeviceSelUtils();
    }

    private DeviceSelUtils(){

    }

    public static final DeviceSelUtils getInsatance(){
        return singleInstance.instance;
    }

    public AbsOven getDKX(IDevice iDevice){
        List<AbsOven> ovenList = Utils.getOven();
        for(AbsOven oven :ovenList){
            if (iDevice.getGuid().equals(oven.getGuid())){
                return oven;
            }
        }
        return null;
    }

    public AbsSteamoven getZQL(IDevice iDevice){
        List<AbsSteamoven> steamovens = Utils.getSteam();
        for (AbsSteamoven steam :steamovens){
            if (iDevice.getGuid().equals(steam.getGuid())){
                return steam;
            }
        }
        return null;
    }

    public AbsMicroWave getWBL(IDevice iDevice){
        List<AbsMicroWave> microWaves = Utils.getMicrowave();
        for(AbsMicroWave microWave : microWaves){
            if (iDevice.getGuid().equals(microWave.getGuid())){
                return microWave;
            }
        }
        return null;
    }

    //public AbsSteameOvenOne getZKY(IDevice iDevice){
    //   if ("RC906".equals(iDevice.getDt())){
    //       return Utils.getDefaultSteameOven();
    //   }
    //    return null;
    //}

    public AbsSteameOvenOne getZKY(IDevice iDevice){
        AbsSteameOvenOne defaultSteameOven = Utils.getDefaultSteameOven();
        assert defaultSteameOven != null;
        if ((defaultSteameOven.getDt()).equals(iDevice.getDt())) {
            return Utils.getDefaultSteameOven();
        }
        return null;
    }
}
