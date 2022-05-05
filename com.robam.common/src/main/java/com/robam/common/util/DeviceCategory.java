package com.robam.common.util;

import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.Oven016;
import com.robam.common.pojos.device.Oven.Oven026;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.common.pojos.device.Steamoven.Steam226;
import com.robam.common.pojos.device.Stove.Stove9B37;
import com.robam.common.pojos.device.Stove.Stove9B39;

/**
 * Created by Dell on 2018/1/11.
 */

public class DeviceCategory {

    public static AbsDevice device(IDevice iDevice){
        if (IRokiFamily.RR016.equals(iDevice.getDt())){
            return (Oven016)iDevice;
        }else if (IRokiFamily.RR026.equals(iDevice.getDt())){
            return (Oven026)iDevice;
        }else if (IRokiFamily.RR039.equals(iDevice.getDt())){
            return (Oven039)iDevice;
        }else if (IRokiFamily.RR028.equals(iDevice.getDt())){
            //return (Oven028)iDevice;
        }else if (IRokiFamily.RS209.equals(iDevice.getDt())){
            return (Steam209)iDevice;
        }else if (IRokiFamily.RS226.equals(iDevice.getDt())){
            return (Steam226)iDevice;
        }else if (IRokiFamily.RS228.equals(iDevice.getDt())){
          //  return (Steam228)iDevice;
        }else if (IRokiFamily.R9B12.equals(iDevice.getDt())){
          //  return (Stove9B12)iDevice;
        }else if (IRokiFamily.R9B37.equals(iDevice.getDt())){
            return (Stove9B37)iDevice;
        }else if (IRokiFamily.R9B39.equals(iDevice.getDt())){
            return (Stove9B39)iDevice;
        }else if (IRokiFamily.R9W70.equals(iDevice.getDt())){
            //return (Stove)
        }else if (IRokiFamily.R5610.equals(iDevice.getDt())){

        }else if (IRokiFamily.R5910.equals(iDevice.getDt())){

        }else if (IRokiFamily.R8229.equals(iDevice.getDt())){

        }else if (IRokiFamily.R8230.equals(iDevice.getDt())){

        }else if (IRokiFamily.R8700.equals(iDevice.getDt())){

        }else if (IRokiFamily.R9700.equals(iDevice.getDt())){

        }else if (IRokiFamily.RM509.equals(iDevice.getDt())){

        }else if (IRokiFamily.RM526.equals(iDevice.getDt())){

        }else if (IRokiFamily._66A2H.equals(iDevice.getDt())){

        }else if (IRokiFamily._5910S.equals(iDevice.getDt())){

        }else if (IRokiFamily._8230C.equals(iDevice.getDt())){

        }else if (IRokiFamily._8231S.equals(iDevice.getDt())){

        }else if (IRokiFamily._8230S.equals(iDevice.getDt())){

        }else{
            return null;
        }
        return null;

    }
}
