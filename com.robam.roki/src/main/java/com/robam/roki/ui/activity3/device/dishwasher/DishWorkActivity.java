package com.robam.roki.ui.activity3.device.dishwasher;

import android.view.View;
import android.widget.Button;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.activity3.device.base.DeviceWorkActivity;
import com.robam.roki.ui.activity3.device.base.adapter.bean.WorkModeBean;
import com.robam.roki.ui.activity3.device.base.table.DishModeEntity;
import com.robam.roki.ui.activity3.device.base.view.WorkTimeView;
import com.robam.roki.ui.activity3.device.dishwasher.bean.DishModeEnum;
import com.robam.roki.utils.DialogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DishWorkActivity extends DeviceWorkActivity {

    AbsDishWasher dishWasher ;
    /**
     * 中间显示状态的view
     */
    private WorkTimeView workTimeView;

    /**
     * 洗碗机报警
     * @param event
     */
    @Subscribe
    public void onEvent(DishWasherAlarmEvent event) {
        if (mDevice == null || !Objects.equal(mDevice.getID(), event.washer.getID())) {
            return;
        }
        this.dishWasher = event.washer;

    }

    /**
     * 洗碗机状态变更事件
     * @param event
     */
    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (mDevice == null || !Objects.equal(mDevice.getID(), event.pojo.getID())) {
            return;
        }
        this.dishWasher = event.pojo;
        if(loadSuccess){
            setData(dishWasher);
        }
    }


    /**
     * 加载数据
     */
    @Override
    protected void dealData() {
        super.dealData();
        if (mDevice != null && mDevice instanceof AbsDishWasher){
            dishWasher = (AbsDishWasher) mDevice;
             workTimeView = new WorkTimeView(this);
             setProgressMax(dishWasher.SetWorkTimeValue);
             setProgressCur(dishWasher.SetWorkTimeValue);
            setData(dishWasher);
        }
    }

    /**
     * 这是处理数据
     * @param dishWasher
     */
    private void setData( AbsDishWasher dishWasher) {
        //工作模式
        short dishWasherWorkMode = dishWasher.DishWasherWorkMode;
        DishModeEntity dishMode = LitePal.where("mode = ? and dt = ?", dishWasherWorkMode + "", mDevice.getDt()).findFirst(DishModeEntity.class);
        if (dishMode == null) {
            return;
        }
        //设置的工作时间
        int SetWorkTimeValue = dishWasher.SetWorkTimeValue;
        //当前水温
        int CurrentWaterTemperatureValue = dishWasher.CurrentWaterTemperatureValue;
        String auxString = "";
        if (dishWasher.LowerLayerWasher == 1){
            auxString = "下层洗";
        }else if (dishWasher.EnhancedDryStatus == 1){
            auxString = "加强干燥";
        }else if (dishWasher.AutoVentilation == 1){
            auxString = "干燥换气";
        }
        List<WorkModeBean> workModeBeans = new ArrayList<>();
        WorkModeBean workModeBean = new WorkModeBean();
        workModeBean.key = "模式" ;
        workModeBean.value = dishMode.name;
        workModeBean.aux = auxString;
        workModeBeans.add(workModeBean);
        if (dishWasherWorkMode != 9 && dishWasherWorkMode != 10){
            workModeBean = new WorkModeBean();
            workModeBean.key = "温度" ;
            workModeBean.value = dishMode.temp+"";
            workModeBean.unit = "℃";
            workModeBeans.add(workModeBean);
        }
        workModeBean = new WorkModeBean();
        workModeBean.key = "时间" ;
        workModeBean.value = String.valueOf(SetWorkTimeValue);
        workModeBean.unit = "min";
        workModeBeans.add(workModeBean);
        setTopMode(workModeBeans);

        switch (dishWasher.powerStatus) {
            case DishWasherStatus.working://工作中
            case DishWasherStatus.pause://暂停中
                //工作中
                if (dishWasher.AppointmentRemainingTime == DishWasherStatus.appointmentSwitchOff){
                    workState() ;
                }else {
                    //预约中
                    orderState();
                }
                break;
            case DishWasherStatus.end://完成
            case DishWasherStatus.off://关机
                finish();
                break;
            default:
                break;
        }


    }

    /**
     * 工作中 暂停中
     */
    private void  workState(){
        if (dishWasher.DishWasherRemainingWorkingTime  < 60){
            workTimeView.setTvMinu(dishWasher.DishWasherRemainingWorkingTime);
        }else {
            int hour = dishWasher.DishWasherRemainingWorkingTime / 60;
            int minu = dishWasher.DishWasherRemainingWorkingTime % 60;
            workTimeView.setTvHourMinu(hour  , minu);
        }
        setProgressCur( dishWasher.DishWasherRemainingWorkingTime);
        //设置进度条暂停
        if (dishWasher.powerStatus == DishWasherStatus.working){
            setProgressStart();
        }else {
            setProgressPause();
        }
        if (dishWasher.DishWasherWorkMode != 10){
            setMessage("剩余工作时间"
                    , workTimeView
                    , dishWasher.powerStatus == DishWasherStatus.working  ? "工作中" : "暂停中");
        }else {
            workTimeView.setMessage("等待换气中");
            setMessageCentre(workTimeView);
        }


        setButtonText("结束工作" , dishWasher.powerStatus == DishWasherStatus.working  ?"暂停" :"继续");
    }

    /**
     * 预约中
     */
    private void orderState(){
        if (dishWasher.AppointmentRemainingTime  < 60){
            workTimeView.setTvMinu(dishWasher.AppointmentRemainingTime);
        }else {
            int hour = dishWasher.AppointmentRemainingTime / 60;
            int minu = dishWasher.AppointmentRemainingTime % 60;
            workTimeView.setTvHourMinu(hour  , minu);
        }
        setMessage("预约剩余时间" , workTimeView , "预约中");
        setButtonText("结束预约" , "立即启动");
    }

    /**
     * 左按钮点击事件
     * @param btnLeft
     */
    @Override
    protected void onControlLeftClick(Button btnLeft) {
//        toast("left");
        finishWork();
    }

    /**
     * 右边按钮点击事件
     * @param btnRight
     */
    @Override
    protected void onControlRightClick(Button btnRight) {
//        toast("right");
        pauseAndContinueWork();
    }

    /**
     * 一个按钮的点击事件
     * @param one
     */
    @Override
    protected void onControlOneClick(Button one) {
        toast("one");
    }

    /**
     * 结束工作
     */
    private void finishWork() {
        IRokiDialog closedialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
                dishWasher.setDishWasherStatusControl(DishWasherStatus.off, null);
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }

    /**
     * 开始或者暂停
     */
    private void pauseAndContinueWork() {
        if (dishWasher.powerStatus == DishWasherStatus.working) {
            if (dishWasher.AppointmentRemainingTime == DishWasherStatus.appointmentSwitchOff){
                dishWasher.setDishWasherStatusControl(DishWasherStatus.pause, null);
            }else {
                //预约中直接开始
                dishWasher.setDishWasherStatusControl(DishWasherStatus.working, null);
            }

        } else {
            dishWasher.setDishWasherStatusControl(DishWasherStatus.working, null);
        }
    }
}
