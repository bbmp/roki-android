package com.robam.roki.utils;

import com.legent.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by you on 2017/10/13.
 */

public class TestDatas {

    //烤箱模块化数据------------------------------
    public static List<Integer> createModeData(List<Integer> data){
        List<Integer> timeList = new ArrayList<>();
        if (data.size()>0){
            for (int i = data.get(0); i <=data.get(1); i+=data.get(2)) {
                timeList.add(i);
            }
        }

        return timeList;
    }


    public static List<Integer> createModeDataTemp(List<Integer> data){
        List<Integer> timeList = new ArrayList<>();
        if (data.size()>0){
            for (int i = data.get(0); i <=data.get(1); i+=data.get(2)) {
                timeList.add(i);
            }
        }

        return timeList;
    }

    public static List<Integer> createModeDataTime(List<Integer> data){
        List<Integer> timeList = new ArrayList<>();
        if (data.size()>0){
            for (int i = data.get(0); i <=data.get(1); i+=data.get(2)) {
                timeList.add(i);
            }
        }

        return timeList;
    }

    public static List<String> createModeDataTime2(List<Integer> data){
        List<String> timeList = new ArrayList<>();
        if (data.size()>0){
            for (int i = data.get(0); i <=data.get(1); i+=data.get(2)) {
                timeList.add(i+"");
            }
        }

        return timeList;
    }

    public static int createModeDefTimePosition(List<String> data  ,String defTime){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(defTime)){
                return i ;
            }
        }
        return Integer.parseInt(defTime);
    }

    //创建文本内容
    public static List<String> createDialogText(String str0,String str1,String str2,String str3,String str4){
        List<String> strList = new ArrayList<>();
        //前两个位置是单位，中间两个是取消确定，str4说明
        strList.add(str0);
        strList.add(str1);
        strList.add(str2);
        strList.add(str3);
        strList.add(str4);
        return strList;
    }

    //创建文本内容
    public static List<String> createExpDialogText(String str0,String str1,String str2,String str3){
        List<String> strList = new ArrayList<>();
        //前两个位置是单位，中间两个是取消确定，str4说明
        strList.add(str0);
        strList.add(str1);
        strList.add(str2);
        strList.add(str3);
        return strList;
    }

    //创建文本内容
    public static List<String> createDialogText(String str0,String str1){
        List<String> strList = new ArrayList<>();
        //前两个位置是单位，中间两个是取消确定，str4说明
        strList.add(str0);
        strList.add(str1);
        return strList;
    }


    //end-----------------------------------------------------------

    //电磁炉温度调节

    public static List<Integer> createTempDatas(short temp) {
        List<Integer> timeList = new ArrayList<>();
        if (temp>=0 &&temp<=30){
            for (int i = 0; i <=200; i++) {
                timeList.add(i);
            }
        }else{
            for (int i = temp-30; i <=200; i++) {
                timeList.add(i);
            }
        }
        return timeList;
    }

    public static List<Integer> createTimeDatas() {
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <=240; i+=1) {
            timeList.add(i);
        }
        return timeList;
    }

    public static List<String> createVoiceDatas(){
        List<String> str = new ArrayList<>();
        str.add("男声");
        str.add("女声");
        return str;
    }

    public static List<Integer> createVoiceContrl(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <=15 ; i++) {
            list.add(i);
        }
        return list;
    }



    public static List<Integer> createStoveData(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <=180 ; i++) {
            list.add(i);
        }
        return list;
    }

    public static List<Integer> createFire(){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <=7 ; i++) {
            list.add(i);
        }
        return list;
    }

    //创建button文本
    public static List<String> createButtonText(String str0,String str1,String str2,String str3){
        List<String> strList = new ArrayList<>();
        //规定好0就是取消的位置，1就是确定的位置,2就是wheel下的说明文字
        strList.add(str0);
        strList.add(str1);
        strList.add(str2);
        strList.add(str3);
        return strList;
    }

    public static List<Integer> createTempertureDatas(List<Integer> uptemp) {
        if (uptemp.size()==0)return null;
        List<Integer> timeList = new ArrayList<>();
        for (int i = uptemp.get(0); i <= uptemp.get(1); i+=uptemp.get(2)) {
            timeList.add(i);
        }
        return timeList;
    }

    public static List<Integer> createExpDownDatas(int upTemp,int stepC,int dev,int max,int min) {
        LogUtils.i(" View view = inflater.inflate(R.layout.page_device_more, container, false);\n" +
                "        ButterKnife.inject(this, view);\n" +
                "        initData();\n" +
                "        initView();\n" +
                "        return view;"," upTemp:"+upTemp+" stepC:"+stepC+" max:"+max+" min:"+min+"dev:"+dev);
        List<Integer> timeList = new ArrayList<>();
        if (upTemp <= dev){
            for (int i = min; i <= upTemp+stepC; i++) {
                timeList.add(i);
            }
            return timeList;
        }

        if (upTemp > dev && upTemp <= max-stepC) {

            for (int i = upTemp - stepC; i <= upTemp + stepC; i++) {
                timeList.add(i);
            }
            return timeList;
        }

        if (upTemp >= (max-stepC)) {
            for (int i = upTemp - stepC; i <= max; i++) {
                timeList.add(i);
            }
            return timeList;
        }

        return timeList;
    }

    public static List<Integer> createExpDownDatas(List<Integer> downList ,int upDate) {
        ArrayList<Integer> date = new ArrayList<>();
        for (int i :
                downList) {
            if (i > upDate -20 && i <= upDate +20){
                date.add(i);
            }
        }

        return date;
    }

    //  消毒柜
    public static List<Integer> createSerTime(int time){
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i < time; i++) {
            timeList.add(i);
        }
        return timeList;
    }

    public static List<Integer> createRikaData(List<Integer> list){

        List<Integer> listData = new ArrayList<>();
        for (int i = 1;i<= list.get(1);i+= list.get(2)){
            listData.add(i);
        }
        return listData;

    }


}
