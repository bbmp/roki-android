package com.robam.roki.listener;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View.OnClickListener;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.ui.dialog.CoreDialog;

import java.util.List;


/**
 *弹框接口
 */
public interface IRokiDialog {
    /**
     * 设置标题
     * @param titleStrId 资源文件中的id
     */
    void setTitle(int titleStrId);

    /**
     * 设置标题
     * @param titleStr 字符串类型要展示的标题
     */
    void setTitle(CharSequence titleStr);

    /**
     * 设置标题内容
     * @param titleStrId 资源文件中的id
     */
    void setTitleText(int titleStrId);

    /**
     * 设置标题内容
     * @param titleStr 字符串类型要展示的标题
     */
    void setTitleText(CharSequence titleStr);


    /**
     * 设置报警代码
     * @param titleAralmCodeStrId 资源文件中的id
     */
    void setTitleAralmCodeText(int titleAralmCodeStrId);
    /**
     * 设置报警代码
     * @param titleAralmCodeStr 字符串类型要展示的报警代码
     */
    void setTitleAralmCodeText(CharSequence titleAralmCodeStr);

    /**
     * 设置对话框文本内容
     * @param contentStrId 资源文件中的id
     */
    void setContentText(int contentStrId);
    /**
     * 设置对话框文本内容
     * @param contentStr 字符串类型要展示的文本内容
     */
    void setContentText(CharSequence contentStr);


    /**
     * @param textId 资源id
     * @param cancelClickListener 取消（或其他）回调监听
     */
    void setCancelBtn(int textId, OnClickListener cancelClickListener);

    /**
     * @param text 展示的按钮文本
     * @param cancelClickListener 取消（或其他）回调监听
     */
    void setCancelBtn(CharSequence text, OnClickListener cancelClickListener);

    /**
     * 设置取消键字体颜色
     * @param color 颜色值
     */
    void setCanBtnTextColor(int color);

    /**
     * @param textId 资源id
     * @param okClickListener 确定（或其他）回调监听
     */
    void setOkBtn(int textId, OnClickListener okClickListener);
    /**
     * @param text 展示的按钮文本
     * @param okClickListener 确定（或其他）回调监听
     */
    void setOkBtn(CharSequence text, OnClickListener okClickListener);


    /**
     * 触摸对话框外部监听事件
     * @param canceledOnTouchOutside 如果为true在点击对话框外部时可以关闭对话框，false不可以关闭
     */
    void setCanceledOnTouchOutside(boolean canceledOnTouchOutside);

    /**
     * 设置滚轮数据根据设备所用到的dialog类型传入相对应的数据，不用则根据需要传入的参数设置为null或者0
     * @param listFrnot   前部滚轮数据源
     * @param listCenter  中部滚轮数据源
     * @param listRear    尾部滚轮数据源
     * @param isLoop      是否无限循环滚轮如果是 true 代表循环 false 不循环
     * @param froneIndex  前部滚轮数据初始化索引
     * @param centerIndex 中部滚轮数据初始化索引
     * @param rearIndex   尾部滚轮数据初始化索引
     * @param onItemSelectedListenerFrone   前部滚轮数据监听器
     * @param onItemSelectedListenerCenter  中部滚轮数据监听器
     * @param onItemSelectedListenerRear    尾部滚轮数据监听器
     */
    void setWheelViewData(List<String> listFrnot,List<String> listCenter,List<String> listRear,
                          boolean isLoop,int froneIndex,int centerIndex,int rearIndex,
                          OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                          OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                          OnItemSelectedListenerRear onItemSelectedListenerRear);

    void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                          List<DeviceConfigurationFunctions> listFunctions, boolean isLoop,
                          int froneIndex, int centerIndex, int rearIndex,
                          OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                          OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                          OnItemSelectedListenerRear onItemSelectedListenerRear);

    void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                          String desc, boolean isLoop,
                          int froneIndex, int centerIndex, int rearIndex,
                          OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                          OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                          OnItemSelectedListenerRear onItemSelectedListenerRear);

    /**
     * 设置初始化任务数据
     * @param randomNumber  传入的随机数
     * @param readyTime     进度开始停留时间
     * @param timingTime    定时器任务的时间间隔
     */
    void setInitTaskData(int randomNumber,int readyTime,long timingTime);

    /**
     *对话框取消监听器
     * @param listener
     */
    void setOnDismissListener(DialogInterface.OnDismissListener listener);

    /**
     * 对话框显示监听器
     * @param listener
     */
    void setOnShowListener(DialogInterface.OnShowListener listener);

    /**
     * 设置弹框显示时间
     * @param time 以秒为单位
     */
    void setToastShowTime(int time);

    void setProgress(int por);

    /**
     * 设置对话框是否可取消
     * @param b
     */
    void setCancelable(boolean b);

    /**
     * true 显示 false 不显示
     * @return
     */
    boolean isShow();

    /**
     * 对话框显示功能
     */
    void show();

    /**
     * 对话框关闭功能
     */
    void dismiss();

    /**
     * 获取对话框本身
     * @return
     */
    CoreDialog getCoreDialog();

}