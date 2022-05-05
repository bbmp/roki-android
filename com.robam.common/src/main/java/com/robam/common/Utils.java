package com.robam.common;

import android.content.Context;

import com.google.common.base.Objects;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;

import java.util.ArrayList;
import java.util.List;

import static com.legent.plat.Plat.deviceService;

public class Utils {

    static public boolean hasRokiDevice() {
        return getDefaultFan() != null;
    }

    static public AbsFan getDefaultFan() {
        if (Plat.appType.equals(IAppType.RKPAD)) { // 对于
            IDevice device = deviceService.getDefault();
            return (AbsFan) device;
        } else {
            List<AbsDevice> list = deviceService.queryDevices();
            if (list != null && list.size() > 0) {
                for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                    if (device instanceof AbsFan && device.isConnected()) { // 对于pad默认设备是处于连接状态的
                        return (AbsFan) device;
                    }
                }
                //如果没有处于连接状态的烟机，还是走原来的代码。   获取默认设备这边的代码后续是要优化的。 基本逻辑: default 的 设备需要页面上人工手动选择。 下面所有的逻辑相同
                for (AbsDevice device : list) {
                    if (device instanceof AbsFan) {
                        return (AbsFan) device;
                    }
                }
            }
            AbsFan absFan = null;
            return absFan;
        }
    }

    static public List<AbsFan> getFan() {
        List<AbsFan> listFan = new ArrayList<>();
        List<AbsDevice> list = deviceService.queryDevices();
        for (AbsDevice device : list) {
            if (device instanceof AbsFan) {
                listFan.add((AbsFan) device);
            }
        }
        return listFan;
    }

    static public AbsWaterPurifier getDefaultWaterPurifier() {
        List<AbsDevice> list = deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsWaterPurifier && device.isConnected()) {
                    return (AbsWaterPurifier) device;
                }
            }

            for (AbsDevice device : list) {
                if (device instanceof AbsWaterPurifier) {
                    return (AbsWaterPurifier) device;
                }
            }

            return null;
        }
    }

    static public AbsSteamoven getDefaultSteam() {
        List<AbsDevice> list = deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven && device.isConnected()) {
                    return (AbsSteamoven) device;
                }
            }

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven) {
                    return (AbsSteamoven) device;
                }
            }

            return null;
        }
    }

    static public AbsSteameOvenOne getDefaultSteameOven() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsSteameOvenOne && device.isConnected()) {
                    return (AbsSteameOvenOne) device;
                }
            }

            for (AbsDevice device : list) {
                if (device instanceof AbsSteameOvenOne) {
                    return (AbsSteameOvenOne) device;
                }
            }
            return null;
        }
    }

    static public AbsRika getDefaultAbsRika() {
        List<AbsDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsRika && device.isConnected()) {
                    return (AbsRika) device;
                }
            }

            for (AbsDevice device : list) {
                if (device instanceof AbsSteameOvenOne) {
                    return (AbsRika) device;
                }
            }
            return null;
        }
    }

    static public List<AbsSteamoven> getSteam() {
        List<AbsDevice> list = deviceService.queryDevices();
        List<AbsSteamoven> listSteam = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsSteamoven) {
                    listSteam.add((AbsSteamoven) device);
                }
            }

            return listSteam;
        }
    }


    static public Stove getDefaultStove() {
        AbsFan fan = getDefaultFan();
        Stove stove = null;
        if (fan != null) {
            if (Plat.appType.equals(IAppType.RKPAD)) {
                for (IDevice device : fan.getChildList()) {
                    if (device instanceof Stove) {
                        return (Stove) device;
                    }
                }
            } else {
                for (IDevice device : fan.getChildList()) {
                    if (device instanceof Stove) {
                        if (device.isConnected()) {
                            return (Stove) device;
                        }
                    }
                }
                for (IDevice device : fan.getChildList()) {
                    if (device instanceof Stove) {
                        return (Stove) device;
                    }
                }
            }
        }
        return stove;
    }

    final public static Pot[] pots = new Pot[1];

    static public Pot[] getDefaultPot() {
        AbsFan fan = getDefaultFan();
        if (IAppType.RKPAD.equals(Plat.appType)) {
            if (!StringUtils.isNullOrEmpty(Plat.getPotGuid())) {
                pots[0] = fan.getChild(Plat.getPotGuid());
                return pots;
            } else {
                pots[0] = null;
                return pots;
            }
        } else {
            pots[0] = null;
            if (fan != null) {
                for (IDevice device : fan.getChildList()) {
                    if (device != null && device instanceof Pot) {
                        if (pots[0] == null)
                            pots[0] = (Pot) device;
                        if (device.isHardIsConnected()) {
                            pots[0] = (Pot) device;
                            return pots;
                        }
                    }
                }
            }
            return pots;
        }
    }

    static public GasSensor getDefaultGasSensor() {
        AbsFan fan = getDefaultFan();
        GasSensor gasSensor = null;
        if (fan != null) {
            for (IDevice device : fan.getChildList()) {
                if (device != null && device instanceof GasSensor) {
                    if (gasSensor == null) {
                        gasSensor = (GasSensor) device;
                    }
                    if (device.isHardIsConnected()) {
                        gasSensor = (GasSensor) device;
                        return gasSensor;
                    }
                }
            }
        }
        return gasSensor;
    }

    static public AbsOven getDefaultOven() {
        List<AbsDevice> list = deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven && device.isConnected()) {
                    return (AbsOven) device;
                }
            }

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven) {
                    return (AbsOven) device;
                }
            }

            return null;
        }
    }

    static public List<AbsOven> getOven() {
        List<AbsDevice> list = deviceService.queryDevices();
        List<AbsOven> listOven = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return null;
        } else {
 /*           for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven && device.isConnected()) {
                    listOven.add((AbsOven) device);
                }
            }*/

            for (AbsDevice device : list) {
//                AbsDevice device = list.get(i);
                if (device instanceof AbsOven) {
                    listOven.add((AbsOven) device);
                }
            }

            return listOven;
        }
    }


    static public AbsMicroWave getDefaultMicrowave() {
        List<AbsDevice> list = deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsMicroWave) {
                    return (AbsMicroWave) device;
                }
            }
            return null;
        }
    }

    static public List<AbsMicroWave> getMicrowave() {
        List<AbsDevice> list = deviceService.queryDevices();
        List<AbsMicroWave> listMicroWave = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            for (AbsDevice device : list) {
                if (device instanceof AbsMicroWave) {
                    listMicroWave.add((AbsMicroWave) device);
                }
            }
            return listMicroWave;
        }
    }


    static public AbsSterilizer getDefaultSterilizer() {
        List<IDevice> list = deviceService.queryAll();
        for (IDevice device : list) {
            if (device instanceof AbsSterilizer && device.isConnected())
                return (AbsSterilizer) device;
        }

        for (IDevice device : list) {
            if (device instanceof AbsSterilizer)
                return (AbsSterilizer) device;
        }

        return null;
    }

    static public AbsHidKit getDefaultHidKit() {
        List<IDevice> list = deviceService.queryAll();
        for (IDevice device : list) {
            if (device instanceof AbsHidKit && device.isConnected())
                return (AbsHidKit) device;
        }

        for (IDevice device : list) {
            if (device instanceof AbsHidKit)
                return (AbsHidKit) device;
        }

        return null;
    }

    static public String getDeviceModel(DeviceType dt) {
        if (IRokiFamily._8230S.equals(dt.getID()) || IRokiFamily._8231S.equals(dt.getID()) ||
                IRokiFamily._8230C.equals(dt.getID()) || IRokiFamily._66A2H.equals(dt.getID()) ||
                IRokiFamily._5910S.equals(dt.getID())) {
            String res = dt.getID();
            return res;
        } else {
            String res = dt.getID();
            return res.substring(1);
        }
    }

    static public boolean isMobApp() {
        boolean isMob = Objects.equal(Plat.app.getPackageName(),
                "com.robam.roki");
        return isMob;
    }

    static public boolean isCooker(String guid) {
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.KDC01);
    }

    static public boolean isDishWasher(String guid){
        IDevice iDevice = deviceService.lookupChild(guid);
        if (iDevice instanceof AbsDishWasher) {
            String dc = iDevice.getDc();
            if (IDeviceType.RXWJ.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RXWJ,dc);
            }
        }

        return false;
        //return DeviceTypeManager.getInstance().isInDeviceCategory(guid,IRokiFamily.WB755);
    }


    static public boolean isStove(String guid) {//判断是否为灶具 by zhaiyuanyi
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof Stove) {
            String dc = iDevice.getDc();
            if (IDeviceType.RDCZ.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RDCZ, dc);
            } else if (IDeviceType.RRQZ.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RRQZ, dc);
            }
        }
        return false;
    }

    static public boolean isFan(String guid) {//判断是否为烟机 @author lixin
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsFan) {
            String dc = iDevice.getDc();
            if (IDeviceType.RYYJ.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RYYJ, dc);
            }
        }
        return false;
    }

    static public boolean isSterilizer(String guid) {//判断是否为消毒柜 by zhaiyuanyi
        LogUtils.i("20181025", "guid:" + guid);
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsSterilizer) {
            String dc = iDevice.getDc();
            if (IDeviceType.RXDG.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RXDG, dc);
            }
        }
        return false;
    }

    static public boolean isSteam(String guid) {//判断是否为蒸汽炉 by Rosicky
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsSteamoven) {
            String dc = iDevice.getDc();
            if (IDeviceType.RZQL.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RZQL, dc);
            }
        }
        return false;
    }

    static public boolean isMicroWave(String guid) {//判断是否为微波炉
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsMicroWave) {
            String dc = iDevice.getDc();
            if (IDeviceType.RWBL.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RWBL, dc);
            }
        }
        return false;
    }

    static public boolean isOven(String guid) {//判断是否为电烤箱 by Linxiaobin
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsOven) {
            String dc = iDevice.getDc();
            if (IDeviceType.RDKX.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RDKX, dc);
            }
        }
        return false;

    }

    static public boolean isGasSensor(String guid) {
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R0003);
    }


    static public boolean isWaterPurifier(String guid) {//判断是否为RR372净水器
        return DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ312) || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ321) || DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.RJ320);
    }

    static public boolean isPot(String guid) {//判断是否为温控锅R0001

        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof Pot) {
            String dc = iDevice.getDc();
            if (IDeviceType.RZNG.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RZNG, dc);
            }
        }
        return false;
    }

    static public boolean isSteamOvenMsg(String guid) {//判断是否为蒸考一体机C906
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsSteameOvenOne) {
            String dc = iDevice.getDc();
            if (IDeviceType.RZKY.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RZKY, dc);
            }
        }
        return false;
    }


    static public boolean isRikaMsg(String guid) {//判断是否为RIKA
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsRika) {
            String dc = iDevice.getDc();
            if (IDeviceType.RIKA.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RIKA, dc);
            }
        }
        return false;
    }

    /**
     * 判断是否为集成灶
     * @param guid
     * @return
     */
    static public boolean isRJCZMsg(String guid) {
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsIntegratedStove) {
            String dc = iDevice.getDc();
            if (IDeviceType.RJCZ.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RJCZ, dc);
            }
        }
        return false;
    }

//    static public boolean isDishWasher(String guid){//判断是否为洗碗机
//        IDevice iDevice = deviceService.lookupChild(guid);
//
//    }

    static public boolean isHidKitMsg(String guid) {//判断是否为藏宝盒
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsHidKit) {
            String dc = iDevice.getDc();
            if (IDeviceType.RCBH.equals(dc)) {
                return DeviceTypeManager.getInstance().isInDeviceCategory(IDeviceType.RCBH, dc);
            }
        }
        return false;
    }

    static public byte whichFan(String guid) {
        if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9700) ||
                DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R8700)) {
            return 1;
        } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R8229)) {
            return 2;
        } else {
            return 0;
        }
    }

    static public byte whichStove(String guid) {
        if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9W70)) {
            return 1;
        } else if (DeviceTypeManager.getInstance().isInDeviceType(guid, IRokiFamily.R9B39)) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
