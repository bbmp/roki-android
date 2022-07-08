package com.robam.roki.ui.helper3;

import com.legent.plat.constant.IDeviceType;
import com.robam.common.pojos.Dc;
import com.robam.roki.R;
import com.robam.roki.utils.UiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author r210190
 */
public class DeviceNameHelper {
    public static String getDeviceName3(String dcs){
        if (dcs==null){
            return "";
        }
        StringBuilder deviceNames = new StringBuilder();
//        for (int i = 0 ; i < dcs.size() ; i ++){
//            if (i >0){
//                deviceNames.append("/");
//            }
//            if (i >= 2){
//                return deviceNames.append("...").toString();
//            }
            switch (dcs) {
                case IDeviceType.RRQZ:
                    deviceNames.append("灶具");
                    break;
                case IDeviceType.RDCZ:
                    deviceNames.append("灶具");
                    break;
                case IDeviceType.RDKX:
                    deviceNames.append("烤箱");
                    break;
                case IDeviceType.RZQL:
                    deviceNames.append("蒸箱");
                    break;

                case IDeviceType.RZKY:
                    deviceNames.append(IDeviceType.RZKY_ZN);
                    break;
                case IDeviceType.RWBL:
                    deviceNames.append(IDeviceType.RWBL_ZN);
                    break;
                default:
                    break;
            }
//        }
        return deviceNames.toString() ;
    }

    public static String getDeviceName(List<Dc> dcs){
        StringBuilder deviceNames = new StringBuilder();
        for (int i = 0 ; i < dcs.size() ; i ++){
            if (i >0){
                deviceNames.append("/");
            }
//            if (i >= 2){
//                return deviceNames.append("...").toString();
//            }
            switch (dcs.get(i).dc) {
                case IDeviceType.RRQZ:
                    deviceNames.append("灶具");
                    break;
                case IDeviceType.RDCZ:
                    deviceNames.append("灶具");
                    break;
                case IDeviceType.RDKX:
                    deviceNames.append("烤箱");
                    break;
                case IDeviceType.RZQL:
                    deviceNames.append("蒸箱");
                    break;

                case IDeviceType.RZKY:
                    deviceNames.append(IDeviceType.RZKY_ZN);
                    break;
                case IDeviceType.RWBL:
                    deviceNames.append(IDeviceType.RWBL_ZN);
                    break;
                default:
                    break;
            }
        }
        return deviceNames.toString() ;
    }

    public static String getDeviceName2(List<Dc> dcs){
        Map<Integer ,String>  deviceNameMap = new HashMap<>();
        StringBuilder deviceNames = new StringBuilder();
        for (int i = 0 ; i < dcs.size() ; i ++){
            switch (dcs.get(i).dc) {
                case IDeviceType.RRQZ:
                    deviceNameMap.put(0 ,"灶具");
                    break;
                case IDeviceType.RDCZ:
                    deviceNameMap.put(0 ,"灶具");
                    break;
                case IDeviceType.RDKX:
                    deviceNameMap.put(2,"烤箱");
                    break;
                case IDeviceType.RZQL:
                    deviceNameMap.put(3,"蒸箱");
                    break;

                case IDeviceType.RZKY:
                    deviceNameMap.put( 1,"一体机");
                    break;
                case IDeviceType.RWBL:
                    deviceNameMap.put(4 , IDeviceType.RWBL_ZN);
                    break;
                default:
                    break;
            }
        }
        int i = 0;
        while (i <= 4){
            String deviceName = deviceNameMap.get(i);
            if (deviceName != null){
                if (deviceNames.toString() != null && deviceNames.toString().length() != 0){
                    deviceNames.append("/").append(deviceName);
                }else {
                    deviceNames.append(deviceName);
                }
            }
            i ++ ;
        }
        return deviceNames.toString() ;
    }

    public static int getIcon(List<Dc> dcs){
        Map<Integer ,Integer>  deviceIcon = new HashMap<>();
        for (int i = 0 ; i < dcs.size() ; i ++){
            switch (dcs.get(i).dc) {
                case IDeviceType.RRQZ:
                    deviceIcon.put(0 ,R.drawable.ic_rqz);
                    break;
                case IDeviceType.RDCZ:
                    deviceIcon.put(0 ,R.drawable.ic_rqz);
                    break;
                case IDeviceType.RDKX:
                    deviceIcon.put(2,R.drawable.ic_dkx);
                    break;
                case IDeviceType.RZQL:
                    deviceIcon.put(3,R.drawable.ic_dzx);
                    break;

                case IDeviceType.RZKY:
                    deviceIcon.put( 1 , R.drawable.ic_ytj);
                    break;
                case IDeviceType.RWBL:
                    deviceIcon.put(4 , R.drawable.ic_wbl);
                    break;
                default:
                    break;
            }
        }
        int i = 0;
        while (i <= 4){
            if (deviceIcon.get(i) != null){
               return deviceIcon.get(i);
            }
            i ++ ;
        }
        return 0 ;
    }

}
