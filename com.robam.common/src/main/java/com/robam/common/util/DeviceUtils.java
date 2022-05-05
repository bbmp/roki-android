package com.robam.common.util;

import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.IRokiFamily;

import java.util.List;

/**
 * Created by rent on 2016/7/16.
 */
public class DeviceUtils {
    /**
     * 判断当前账号品类 以后需要完善
     */
    public static boolean isHasCategory(String category) {
        if (StringUtils.isNullOrEmpty(category))
            return false;
        List<IDevice> list = DeviceService.getInstance().queryAll();
        if (list == null || list.size() <= 0)
            return false;
        String[] allId = IRokiFamily.all.split(",");
        if (category.equals(DeviceType.RDKX)) {//烤箱
            for (IDevice device : list) {
                if (IRokiFamily.RR039.equals(device.getDt())) {
                    return true;
                } else if (IRokiFamily.RR016.equals(device.getDt())) {
                    return true;
                } else if (IRokiFamily.RR026.equals(device.getDt())) {
                    return true;
                }else if (IRokiFamily.RR028.equals(device.getDt())){
                    return true;
                }
            }
        } else if (category.equals(DeviceType.RZQL)) {//蒸汽炉
            for (IDevice device : list) {
                if (IRokiFamily.RS209.equals(device.getDt())) {
                    return true;
                }else if (IRokiFamily.RS226.equals(device.getDt())){
                    return true;
                }else if (IRokiFamily.RS228.equals(device.getDt())){
                    return true;
                }
            }
        } else if (category.equals(DeviceType.RWBL)) {//微波炉
            for (IDevice device : list) {
                if (IRokiFamily.RM509.equals(device.getDt())) {
                    return true;
                }else if (IRokiFamily.RM526.equals(device.getDt())){
                    return true;
                }
            }
        }else if (category.equals(DeviceType.RZKY)){//蒸烤一体机
            for (IDevice device : list) {
                if (IRokiFamily.RC906.equals(device.getDt())) {
                    return true;
                }
            }
        }else if (category.equals(DeviceType.RIKA)){//RIKA
            for (IDevice device : list) {
                LogUtils.i("20180317","dt:"+device.getDt());
                if (IRokiFamily.RIKA_Z.equals(device.getDt())) {
                    return true;
                }
            }
        }
        return false;
    }
}
