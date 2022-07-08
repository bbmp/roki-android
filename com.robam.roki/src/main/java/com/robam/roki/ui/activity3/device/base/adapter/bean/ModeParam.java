package com.robam.roki.ui.activity3.device.base.adapter.bean;

import org.litepal.annotation.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/23
 *     desc   : 设备模式参数
 *     version: 1.0
 * </pre>
 */
public class ModeParam {

    /**
     * 协议对应code
     */
    public int code;
    /**
     * 模式名
     */
    public String name;

    /**
     * 默认温度
     */
    public int defTemp;
    /**
     * 模式时间
     */
    public int defTime;
    /**
     * 最小温度
     */
    public int minTemp;
    /**
     * 最大温度
     */
    public int maxTemp;
    /**
     * 最小时间
     */
    public int minTime;
    /**
     * 最大时间
     */
    public int maxTime;

    /**
     * 可设置最小蒸汽量
     */
    public int minSteam ;
    /**
     * 可设置最大蒸汽量
     */
    public int maxSteam ;
    /**
     * 模式默认蒸汽量
     */
    public int defSteam ;
    /**
     * 描述
     */
    public String desc ;


    public int getDefDownTemp(int selectTemp){
        return selectTemp - 20 > minTemp ? selectTemp - 20 : minTemp ;
    }

    /**
     * 默认温度index
     * @return
     */
    public int getDefTempIndex(){
        return defTemp - minTemp ;
    }

    /**
     * 默认时间index
     * @return
     */
    public int getDefTimeIndex(){
        return defTime - minTime ;
    }

    /**
     * 默认蒸汽量index
     * @return
     */
    public int getDefSteamIndex(){
        return defSteam - minSteam ;
    }


    /**
     * 获取modo温度范围
     * @return
     */
    public List<Integer> getTempData(){
        ArrayList<Integer> tempData = new ArrayList<>();
        for (int i = minTemp ; i <= maxTemp ; i ++ ){
            tempData.add(i);
        }
        return tempData ;
    }

    /**
     * 获取modo下管温度范围
     * @param selectTemp 选择的上温度
     * @return
     */
    public  List<Integer> getDownTempData(int selectTemp){
        int temp1 = selectTemp - 20 < minTemp ? minTemp : selectTemp - 20  ;
        int temp2 = selectTemp + 20 > maxTemp ? maxTemp : selectTemp + 20  ;
        ArrayList<Integer> tempData = new ArrayList<>();
        for (int i = temp1 ; i <= temp2 ; i ++ ){
            tempData.add(i);
        }
        return tempData ;
    }


    /**
     * 获取时间范围
     * @return
     */
    public  List<Integer> getTimeData(){
        ArrayList<Integer> tempData = new ArrayList<>();
        for (int i = minTime ; i <= maxTime ; i ++ ){
            tempData.add(i);
        }
        return tempData ;
    }

    /**
     * 获取蒸汽范围
     * @return
     */
    public  List<Integer> getSteamData(){
        ArrayList<Integer> tempData = new ArrayList<>();
        for (int i = minSteam ; i <= maxSteam ; i ++ ){
            tempData.add(i);
        }
        return tempData ;
    }

    public List<ModeSingleParam> getSingleParams(){
        ArrayList<ModeSingleParam> modeSingleParams = new ArrayList<>();
        if (name.equals("澎湃蒸") || name.equals("加湿烤") || code == 22 || code == 23 || code == 24){
            ModeSingleParam  singleParam = new ModeSingleParam() ;
            singleParam.name = "蒸汽量" ;
            singleParam.unit = "" ;
            singleParam.datas = getSteamData() ;
            modeSingleParams.add(singleParam);

            ModeSingleParam  singleParam1 = new ModeSingleParam() ;
            singleParam1.name = "温度" ;
            singleParam1.unit = "℃" ;
            singleParam1.datas = getTempData() ;
            singleParam1.defIndex = getDefTempIndex() ;
            modeSingleParams.add(singleParam1);
        } else if (name.equals("EXP") || name.equals("专业烤") ){
            ModeSingleParam  singleParam = new ModeSingleParam() ;
            singleParam.name = "上温度" ;
            singleParam.unit = "℃" ;
            singleParam.datas = getTempData() ;
            singleParam.defIndex = getDefTempIndex() ;
            modeSingleParams.add(singleParam);

            ModeSingleParam  singleParam1 = new ModeSingleParam() ;
            singleParam1.name = "下温度" ;
            singleParam1.unit = "℃" ;
            singleParam1.datas = getDownTempData(defTemp)  ;
            modeSingleParams.add(singleParam1);
        }else {
            ModeSingleParam  singleParam = new ModeSingleParam() ;
            singleParam.name = "温度" ;
            singleParam.unit = "℃" ;
            singleParam.datas = getTempData() ;
            singleParam.defIndex = getDefTempIndex() ;
            modeSingleParams.add(singleParam);
        }

        ModeSingleParam  singleParam = new ModeSingleParam() ;
        singleParam.name = "时间" ;
        singleParam.unit = "min" ;
        singleParam.datas = getTimeData() ;
        singleParam.defIndex = getDefTimeIndex() ;
        modeSingleParams.add(singleParam);
        return modeSingleParams ;
    }
}
