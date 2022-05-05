package com.robam.common.util;


import android.util.Base64;

import com.legent.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14807 on 2018/3/27.
 */

public class SaveArrayListUtil {

    public static String SearchList2String(List<String> searchList) {
        try {
            // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 然后将得到的字符数据装载到ObjectOutputStream
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(searchList);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            String SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            // 关闭objectOutputStream
            objectOutputStream.close();
            return SceneListString;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i("20180327", "E:" + e.toString());
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static ArrayList<String> String2SearchList(String SearchListString) {
        try {
            byte[] mobileBytes = Base64.decode(SearchListString.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            ArrayList<String> SceneList = (ArrayList<String>) objectInputStream
                    .readObject();

            objectInputStream.close();

            return SceneList;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i("20180327", "IOException:" + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LogUtils.i("20180327", "ClassNotFoundException:" + e.toString());
        }
        return null;
    }

    public static ArrayList getList2ArrayList(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i));
        }

        return arrayList;
    }
}
