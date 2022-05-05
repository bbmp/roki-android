package com.robam.roki.utils;

import android.util.SparseArray;

import com.robam.common.pojos.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 14807 on 2018/3/14.
 */

public class MotherListUtils {

    /**
     * 获取 source arrayList中的指定数量的随机集合
     * @param arrayList 源集合
     * @param number 指定的长度
     * @return 得到的随机集合
     */
    public static  ArrayList<Recipe> randomElement(ArrayList<Recipe> arrayList, int number){
        int size = arrayList.size() ;
        if(number > size){
            number = size;
//            throw new RuntimeException("number can't be greater than arrayList's size");
        }

        if(number == size){
            return arrayList ;
        }

        ArrayList<Recipe> _arrayList = new ArrayList<>(number) ;
        SparseArray<Recipe> sparseArray = new SparseArray<>(number);
        Random random = new Random();
        while (sparseArray.size() < number){
            int i = random.nextInt(size) ;
            sparseArray.put(i,arrayList.get(i));
        }

        for (int i = 0;i < sparseArray.size();i++){
            int key = sparseArray.keyAt(i) ;
            _arrayList.add(sparseArray.get(key)) ;
        }

        return _arrayList ;
    }
}
