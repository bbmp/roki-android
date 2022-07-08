package com.robam.roki.utils;

import com.aispeech.kernel.Abs;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinwei on 2017/12/6.
 */

public class DeviceSelectUtils {

    List<IDevice> list =null;
   private static class DeviceSelect{
        private static final DeviceSelectUtils INSTANCE = new DeviceSelectUtils();
   }

    private DeviceSelectUtils(){
         list = Plat.deviceService.queryAll();
        for (int i = 0; i < list.size(); i++) {
            LogUtils.i("20171207","list:"+list.get(i).getDc());
        }
    }

    public static final DeviceSelectUtils getInstance(){
        return DeviceSelect.INSTANCE;
    }

    public void setList(List<IDevice> list){
        this.list = list;
    }

   public List<IDevice> deviceIDev(List<String> listDc){
       list = Plat.deviceService.queryAll();
       List<IDevice> listTemp = new ArrayList<>();
      /* List<String> listDevice = new ArrayList<>();
       for (int i = 0; i < list.size(); i++) {
           listDevice.add(list.get(i).getDc());
       }
       for (int i = 0; i < listDc.size(); i++) {
           if ("RDKX".equals(listDc.get(i))||"RZQL".equals(listDc.get(i))){
              if (listDevice.contains(listDc.get(i))){
                  for (int j = 0; j < list.size(); j++) {
                      if (listDc.get(i).equals(list.get(j).getDc())){
                          listTemp.add(list.get(j));
                      }
                  }
                  return listTemp;
              }else{
                  if (listDevice.contains("RZKY")){
                      for (int j = 0; j < list.size(); j++) {
                          if ("RZKY".equals(list.get(j).getDc())){
                              listTemp.add(list.get(j));
                          }
                      }
                  }
                  return listTemp;
              }

           }else{
               for (int j = 0; j < list.size(); j++) {
                   if (listDc.get(i).equals(list.get(j).getDc())){
                       listTemp.add(list.get(j));
                   }
               }
               return listTemp;
           }
       }*/
       if (listDc!=null){
           for (int i = 0; i < listDc.size(); i++) {
               for (int j = 0; j <list.size() ; j++) {
                  // LogUtils.i("20171221","deviceIDev:::"+list.get(j).getDc()+"  "+listDc.get(i).toString());
                   if (listDc.get(i).equals(list.get(j).getDc())){
                       listTemp.add(list.get(j));
                      // LogUtils.i("20171221","listTemp:::"+listTemp.toString());
                   }
               }
           }
       }

       return listTemp;
   }

    /**
     * 根据菜谱参数 筛选支持该菜谱品类的设备
     * @param list
     * @param platformCodeList
     * @return
     */
    public List<IDevice> deviceIDevPlatformCode(List<IDevice> list , List<String> platformCodeList){
        List<IDevice> iDevices = new ArrayList<>();
        for (IDevice iDevice : list) {
            if (platformCodeList.contains(iDevice.getDp())){
                iDevices.add(iDevice);
            }
        }
        return iDevices;
    }
    /**
     * 根据菜谱参数 筛选支持该菜谱品类的设备 (灶具)
     * @param list
     * @param platformCodeList
     * @return
     */
    public List<Stove> deviceIDevPlatformCode2(List<Stove> list , List<String> platformCodeList){
        List<Stove> stoves = new ArrayList<>();
        for (Stove stove : list) {
            if (platformCodeList.contains(stove.getDp())){
                stoves.add(stove);
            }
        }
        return stoves;
    }
    /**
     * 筛选支持联动的蒸烤一体机
     * @param list
     * @return
     */
    public List<IDevice> deviceLinkageSteamOvenSelect(List<IDevice> list ){
        List<IDevice> iDevices = new ArrayList<>();
        for (IDevice iDevice : list) {
            if ("CQ920".equals(iDevice.getDt())){
                iDevices.add(iDevice);
            }
        }
        return iDevices;
    }

    public Pot[] getPot(){
        Pot[] defaultPot = Utils.getDefaultPot();
        return defaultPot ;
    }

    public AbsFan getDeviceFan(String dt){
        List<AbsFan> fans = Utils.getFan();
        for (int i = 0; i <fans.size() ; i++) {
            if (dt.equals(fans.get(i).getDt())){
                return fans.get(i);
            }
        }
        return null;
    }

    public IDevice device(String dt){
        if (list!=null){
            for (int i = 0; i <list.size() ; i++) {
                if (dt.equals(list.get(i).getDt())){
                    return list.get(i);
                }
            }
        }
        return null;
    }

    public List<String> dcSubString(List<Dc> dclist){
        List<String> listTemp = new ArrayList<>();
        for (int i = 0; i <dclist.size() ; i++) {
            if (dclist.get(i).getDc().contains("||")){
                String[] str = dclist.get(i).getDc().split("\\|\\|");
                for (int j = 0; j < str.length; j++) {
                    listTemp.add(str[j]);
                }

            }else{
               listTemp.add(dclist.get(i).getDc());
            }
        }
        return listTemp;
    }

    public List<String> dcSubString(String dc) {
        List<String> listTemp = new ArrayList<>();

        if (dc.contains("||")) {
            String[] str = dc.split("\\|\\|");
            for (int j = 0; j < str.length; j++) {
                listTemp.add(str[j]);
            }

        } else {
            listTemp.add(dc);
        }

        return listTemp;
    }

    public String timeStardard(int time){
        String timeMin=null;
        String timeSec=null;
        if (time/60<10){
            timeMin = "0"+time/60;
        }else {
            timeMin = time/60+"";
        }
        if (time%60<10){
            timeSec = "0"+time%60;
        }else{
            timeSec = time%60+"";
        }
        String str = timeMin+":"+timeSec;
        return str;
    }


}
