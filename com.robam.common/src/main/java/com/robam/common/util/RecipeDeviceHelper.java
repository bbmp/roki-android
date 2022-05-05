package com.robam.common.util;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;

import java.util.List;

/**
 * Created by zhoudingjun on 2016/7/9.
 */
public class RecipeDeviceHelper {

    public static boolean queryIfContainDevice(String device) {
        List<String> userOwnDevice = Lists.newArrayList();
        List<IDevice> list = Plat.deviceService.queryDevices();
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RRQZ)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RRQZ);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RDKX)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RDKX);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RZQL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RZQL);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RWBL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RWBL);
            }
        }
        return (userOwnDevice.contains(device));
    }


    //周定钧
    public static boolean queryIfContainAllDevices(List<Dc> deviceList) {
        List<String> userOwnDevice = Lists.newArrayList();
        List<String> recipeNeedDevice = Lists.newArrayList();
        List<IDevice> list = Plat.deviceService.queryDevices();
        if (list == null || list.size() == 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RRQZ)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RRQZ);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RDKX)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RDKX);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RZQL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RZQL);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RWBL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RWBL);
            }
        }

        for (int i = 0; i < deviceList.size(); i++) {
            recipeNeedDevice.add(deviceList.get(i).getDc());
        }
        if (userOwnDevice == null) {
            return false;
        }

        for (String dc : recipeNeedDevice) {
            if (!userOwnDevice.contains(dc)) {
                return false;
            }
        }
        return true;
    }

    //查询出制作菜谱缺少的设备
    //周定钧
    public static List<String> queryLackDevices(List<Dc> deviceList) {
        if (deviceList == null || deviceList.size() == 0) {
            return null;
        }
        List<String> userOwnDevice = Lists.newArrayList();
        List<String> deviceLacklist = Lists.newArrayList();
        List<String> recipeNeedDevice = Lists.newArrayList();
        List<IDevice> list = Plat.deviceService.queryDevices();
        for (int i = 0; i < deviceList.size(); i++) {
            recipeNeedDevice.add(deviceList.get(i).getDc());
        }
        if (list == null) {
            return recipeNeedDevice;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RRQZ)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RRQZ);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RDKX)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RDKX);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RZQL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RZQL);
            } else if (list.get(i).getDc().equals(com.robam.common.pojos.DeviceType.RWBL)) {
                userOwnDevice.add(com.robam.common.pojos.DeviceType.RWBL);
            }
        }
        for (String device : recipeNeedDevice) {
            if (!userOwnDevice.contains(device)) {
                if (device.equals(com.robam.common.pojos.DeviceType.RDKX)) {
                    deviceLacklist.add("电烤箱");
                }
                if (device.equals(com.robam.common.pojos.DeviceType.RZQL)) {
                    deviceLacklist.add("电蒸箱");
                }
                if (device.equals(com.robam.common.pojos.DeviceType.RWBL)) {
                    deviceLacklist.add("微波炉");
                }
                if (device.equals(com.robam.common.pojos.DeviceType.RRQZ)) {
                    deviceLacklist.add("燃气灶");
                }
            }
        }
        return deviceLacklist;
    }

    //查询是否菜谱里面是否用到灶具
    public static boolean queryIfContainStove(List<Dc> deviceList) {
        if (deviceList == null || deviceList.size() == 0) {
            return true;
        } else {
            for (Dc dc : deviceList) {
                if (dc.getDc().equals(com.robam.common.pojos.DeviceType.RRQZ)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean queryIfContainDC(List<Dc> deviceList, String dc) {
        if (dc.equals(DeviceType.RRQZ)) {
            return queryIfContainStove(deviceList);
        } else {
            if (deviceList == null || deviceList.size() == 0)
                return false;
            for (Dc dc_e : deviceList)
                if (dc_e.getDc().equals(dc))
                    return true;
            return false;
        }
    }
}

