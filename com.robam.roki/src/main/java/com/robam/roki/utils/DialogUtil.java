package com.robam.roki.utils;

import android.content.Context;

import com.legent.utils.LogUtils;


/**
 * 对话框类型整理
 */
public class DialogUtil {

    /**
     * 中部文本内容，底部两个按钮 居中弹出
     */
    public static final int DIALOG_TYPE_00 = 0;
    /**
     * 左上角标题，报警代码，中部文本内容，底部两个按钮 居中弹出
     */
    public static final int DIALOG_TYPE_01 = 1;
    /**
     * 左上角标题，报警代码，中部文本内容，底部一个按钮 居中弹出
     **/
    public static final int DIALOG_TYPE_02 = 2;
    /**
     * 左上角按钮,右上角按钮，中部一个滑动滚轮，从底部弹出
     **/
    public static final int DIALOG_TYPE_03 = 3;
    /**
     * 左上角按钮,右上角按钮，中部两个滑动滚轮，从底部弹出
     **/
    public static final int DIALOG_TYPE_04 = 4;
    /**
     * 左上角按钮,右上角按钮，中部三个滑动滚轮，从底部弹出
     **/
    public static final int DIALOG_TYPE_05 = 5;
    /**
     * 正在连接...，数字百分比
     **/
    public static final int DIALOG_TYPE_06 = 6;
    /**
     * 即将开始工作，倒计时 3 2 1
     **/
    public static final int DIALOG_TYPE_07 = 7;
    /**
     * 所有烟机统一弹框
     **/
    public static final int DIALOG_TYPE_08 = 8;
    /**
     * 中部文本内容,用于本项目3级报警
     **/
    public static final int DIALOG_TYPE_09 = 9;
    /**
     * 左上角标题,中部文本内容，底部两个按钮 居中弹出
     */
    public static final int DIALOG_TYPE_10 = 10;

    /**
     * 煲汤提醒专用
     **/
    public static final int DIALOG_TYPE_11 = 11;

    /**
     * 自动烹饪菜谱对话框
     **/
    public static final int DIALOG_TYPE_12 = 12;

    /**
     * 自动烹饪菜谱对话框
     **/
    public static final int DIALOG_TYPE_13 = 13;
    public static final int DIALOG_TYPE_14 = 14;
    //烟机厨房净化选择框
    public static final int DIALOG_TYPE_15 = 15;
    /**
     * 左上角标题,中部文本内容，底部一个按钮 居中弹出
     */
    public static final int DIALOG_TYPE_16 = 16;
    /**
     * 左上角按钮,右上角按钮，中部两个滑动滚轮，底部又描述，从底部弹出
     **/
    public static final int DIALOG_TYPE_17 = 17;
    /**
     * 即将开始工作，倒计时 5 4 3 2 1
     **/
    public static final int DIALOG_TYPE_18 = 18;
    public static final int DIALOG_TYPE_19 = 19;
    public static final int DIALOG_TYPE_20 = 20;
    public static final int DIALOG_TYPE_21 = 21;
    public static final int DIALOG_TYPE_22 = 22;
    public static final int DIALOG_TYPE_23 = 23;
    public static final int DIALOG_TYPE_24 = 24;
    public static final int DIALOG_TYPE_25 = 25;
    public static final int DIALOG_TYPE_26 = 1026;
    /**
     * 多段菜谱
     */
    public static final int DIALOG_TYPE_27 = 1027;
    /**
     * 选择时间
     */
    public static final int DIALOG_TYPE_TIME = 1028;

    /**
     * 藏宝盒发现新版本弹框
     */
    public static final int DIALOG_DISCOVER_NEW_VERSION = 26;
    /**
     * 藏宝盒下载新版本弹框
     */
    public static final int DIALOG_DOWN_NEW_VERSION = 27;
    /**
     * 藏宝盒更新失败弹框
     */
    public static final int DIALOG_UPDATE_FAILED = 28;

    /**
     * 藏宝盒更新完成弹框
     */
    public static final int DIALOG_UPDATE_COMPLETION = 29;

    public static final int LENGTH_SHORT = 2;
    public static final int LENGTH_CENTER = 4;
    public static final int LENGTH_LONG = 6;

    /**
     * 获取内容应该显示的宽度(px)
     */
    public static int getContentWidth(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 480;
        }
        int width = context.getResources().getDisplayMetrics().widthPixels;

        LogUtils.i("20170814", "width:" + width);

//        if (width > 720) {      // 屏幕宽度大于720px则取屏幕宽度的95%作为内容的宽度
//            width = width * 95 / 100;
//        }
        return width;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 480;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        if (context == null || context.getResources() == null || context.getResources().getDisplayMetrics() == null) {
            return 800;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dip 转 px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
