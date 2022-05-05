package com.robam.common.pojos.device.microwave;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
public class MicrowaveUtils {
    /**
     * 设置时间范围 微波 烧烤 组合
     *
     * @return
     */
    public static List<Integer> create90TimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 5; i < 180; i = i + 5) {
            list.add(i);
        }//5秒到3分钟每次加5秒
        for (int i = 180; i < 600; i = i + 30) {
            list.add(i);
        }//3分钟到10分钟每次加30秒
        for (int i = 600; i < 1800; i = i + 60) {
            list.add(i);
        }//10分钟到30分钟每次加1分钟
        for (int i = 1800; i <= 5400; i = i + 300) {
            list.add(i);
        }//30分钟到90分钟每次加5分钟
        return list;
    }
    public static List<Integer> create30TimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 5; i < 180; i = i + 5) {
            list.add(i);
        }//5秒到3分钟每次加5秒
        for (int i = 180; i < 600; i = i + 30) {
            list.add(i);
        }//3分钟到10分钟每次加30秒
        for (int i = 600; i <= 1800; i = i + 60) {
            list.add(i);
        }//10分钟到30分钟每次加1分钟
        return list;
    }

    public static List<Integer> create526R30TimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i <60; i = i + 5) {
            list.add(i);
        }//5秒到3分钟每次加5秒
        return list;
    }

    public static List<Integer> create526R5SecTimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 5; i < 60; i = i + 5) {
            list.add(i);
        }//5s跳
        return list;
    }

    public static List<Integer> create526R0SecTimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 1; i = i + 1) {
            list.add(i);
        }//0s
        return list;
    }

    public static List<Integer> create526R30SecTimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 60; i = i+30 ) {
            list.add(i);
        }//30s跳
        return list;
    }

    public static List<Integer> create30TimeMinList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i <= 30; i = i +1) {
            list.add(i);
        }
        return list;
    }

    public static List<Integer> create526R90TimeList() {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i <= 30; i = i + 1) {
            list.add(i);
        }
        for (int i = 35; i <=90;i= i+5) {
            list.add(i);
        }
        return list;
    }

    /**
     * 微波火力范围
     */
    public static List<Integer> createMicroFireList(){
        List<Integer> list1 = Lists.newArrayList();
        list1.add(Integer.valueOf(MicroWaveFire.LOW_FIRE));
        list1.add(Integer.valueOf(MicroWaveFire.UNFREEZE_FIRE));
        list1.add(Integer.valueOf(MicroWaveFire.MIDDLE_LOW_FIRE));
        list1.add(Integer.valueOf(MicroWaveFire.MIDDLE_FIRE));
        list1.add(Integer.valueOf(MicroWaveFire.MIDDLE_HIGH_FIRE));
        list1.add(Integer.valueOf(MicroWaveFire.HIGH_FIRE));
        return list1;
    }

    /**
     * 烧烤 火力范围
     */
    public static List<Integer> createBarbecueFireList(){
        List<Integer> list1 = Lists.newArrayList();
        list1.add(Integer.valueOf(MicroWaveFire.BARBECUE_LOW));
        list1.add(Integer.valueOf(MicroWaveFire.BARBECUE_MIDDLE));
        list1.add(Integer.valueOf(MicroWaveFire.BARBECUE_HIGH));
        return list1;
    }
    /**
     * 组合加热 火力范围
     */
    public static List<Integer> createCombineFireList(){
        List<Integer> list1 = Lists.newArrayList();
        list1.add(Integer.valueOf(MicroWaveFire.COMBINE_HEATING_LOW));
        list1.add(Integer.valueOf(MicroWaveFire.COMBINE_HEATING_MIDDLE));
        list1.add(Integer.valueOf(MicroWaveFire.COMBINE_HEATING_HIGH));
        return list1;
    }


    /**
     * 重量 范围
     */
    public static List<Integer> createWeightList(short start,short end,short step){
        List<Integer> list1 = Lists.newArrayList();
        for (int i=start;i<=end;i=i+step){
            list1.add(Integer.valueOf(i+""));
        }
        return list1;
    }

}
