package com.legent.utils.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.google.common.base.Strings;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public abstract class ResourcesUtils {

    final static String Encoding = "utf-8";
    static Context cx;

    public static void init(Context cx) {
        ResourcesUtils.cx = cx;
    }

    public static Uri res2Uri(int resId) {
        String pgName = cx.getPackageName();
        Uri uri = Uri.parse("android.resource://" + pgName + "/" + resId);
        return uri;
    }

    public static Uri asset2Uri(String fileName) {
        Uri uri = Uri.fromFile(new File("file:///android_asset/" + fileName));
        return uri;
    }

    /**
     * 资源转存为私有文件
     */
    @SuppressLint("WorldReadableFiles")
    public static void res2PrivateFile(int resId, String fileName) {

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = cx.getResources().openRawResource(resId);
            outputStream = cx.openFileOutput(fileName, Context.MODE_APPEND
                    | Context.MODE_WORLD_READABLE);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception ex) {
        } finally {
            inputStream = null;
            outputStream = null;
        }
    }

    /**
     * 从assets读取字符串
     */
    public static String asset2String(String fileName) {
        byte[] bytes = asset2Bytes(fileName);
        if (bytes != null)
            return EncodingUtils.getString(bytes, Encoding);
        else
            return null;
    }

    /**
     * 从assets读取字节流
     */
    public static byte[] asset2Bytes(String fileName) {

        InputStream in = null;
        byte[] buffer = null;
        try {
            in = cx.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception ex) {
            return null;
        } finally {
            in = null;
        }
    }

    /**
     * 从原始资源读取字符串
     */
    public static String raw2String(int resid) {
        return raw2String(resid, Encoding);
    }

    public static String raw2String(int resid, String chartsetName) {

        String str;
        try {
            byte[] data = raw2Bytes(resid);
            str = new String(data, chartsetName);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从原始资源读取字节流
     */
    public static byte[] raw2Bytes(int resid) {
        Resources r = cx.getResources();
        InputStream in = null;
        try {
            in = r.openRawResource(resid);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            in = null;
        }
    }

    /**
     * 若是字符串资源，则取其资源值，否则返回原始值
     *
     * @param rawString
     * @return
     */
    public static String getStringOrFromRes(String rawString) {
        if (!Strings.isNullOrEmpty(rawString)
                && rawString.startsWith("@string/")) {
            int resId = getResId(rawString);
            if (resId <= 0)
                return rawString;
            else
                return cx.getString(resId);
        } else {
            return rawString;
        }
    }

    /**
     * 根据资源名称获取资源id
     *
     * @param resourceName
     * @return
     */
    public static int getResId(String resourceName) {

        if (Strings.isNullOrEmpty(resourceName))
            return 0;

        try {
            String pn = cx.getApplicationInfo().packageName;
            String resName = resourceName;
            if (resourceName.startsWith("@")) {
                resName = resourceName.substring(1);
            }

            resName = String.format("%s:%s", pn, resName);
            Resources r = cx.getResources();
            int resID = r.getIdentifier(resName, null, null);
            return resID;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
