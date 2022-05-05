package com.robam.roki.utils;

/**
 * Created by Administrator on 2017/8/23.
 */

public class RemoveManOrsymbolUtil {


    /**
     * 去除字符串中的非数字部分
     * @param str
     * @return
     */
    public static String  getRemoveString(String str){
        String tmpStr="";
        if(str.length()>0){

            for(int i=0;i<str.length();i++){
                String tmp = ""+str.charAt(i);
                if((tmp).matches("[0-9.]")){
                    tmpStr+=tmp;
                }
            }
        }
        return tmpStr;
    }


}
