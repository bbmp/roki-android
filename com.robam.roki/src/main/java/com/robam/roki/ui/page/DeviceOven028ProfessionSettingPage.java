package com.robam.roki.ui.page;

import android.os.Bundle;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026OrderTimeDialog;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/17.
 */

public class DeviceOven028ProfessionSettingPage extends AbsDeviceOvenProfessionSetPage {
    @Override
    public void initData() {
        super.initData();
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        modeKingMap.put("快热", (short) 0);
        modeKingMap.put("风焙烤", (short) 1);
        modeKingMap.put("焙烤", (short) 2);
        modeKingMap.put("底加热", (short) 3);
        modeKingMap.put("风扇烤", (short) 4);
        modeKingMap.put("强烤烧", (short) 5);
        modeKingMap.put("烤烧", (short) 6);
        modeKingMap.put("快速加热",(short) 10);
        modeKingMap.put("煎烤",(short) 11);
        modeKingMap.put("果蔬烘干",(short) 12);
        modeKingMap.put("保温",(short)15);
    }

    @Override
    protected void setDefaultValue(int index,Object item) {
        List<?> list1 = getList2(item);
        List<?> list2 = getList3(item);
        temWheel.setData(list1);
        timeWheel.setData(list2);
        int def1 = 0, def2 = 0;
        if (index == 0) {
            def1 = 150;
            def2 = 29;
        } else if (index == 1) {
            def1 = 150;
            def2 = 29;
        } else if (index == 2) {
            def1 = 110;
            def2 = 29;
        } else if (index == 3) {//底加热
            def1 = 110;
            def2 = 29;
        } else if (index == 4) {
            def1 = 170;
            def2 = 29;
        } else if (index == 5) {
            def1 = 130;
            def2 = 29;
        } else if (index == 6) {
            def1 = 130;
            def2 = 29;
        }else if (index == 7){
            def1 = 130;
            def2 = 0;
        }else if (index == 8){
            def1 = 100;
            def2 = 29;
        }else if (index == 9){
            def1 = 10;
            def2 = 29;
        }else if (index == 10){
            def1 = 20;
            def2 = 0;
        }
        handler.sendEmptyMessage(0x03);
        temWheel.setDefault(def1);
        timeWheel.setDefault(def2);
    }

    /*设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if (s.equals("底加热")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if ("快热".equals(s)|| "风焙烤".equals(s)||"焙烤".equals(s)|| "风扇烤".equals(s)||
                "烤烧".equals(s)||"强烤烧".equals(s)) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if ("快速预热".equals(s) || "煎烤".equals(s)) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if ("果蔬烘干".equals(s)){
            for (int i = 50; i <=80 ; i++) {
                list.add(i);
            }
        }else if ("保温".equals(s)){
            for (int i = 40; i <=90 ; i++) {
                list.add(i);
            }
        }
        return list;
    }

    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals("快热") || s.equals("风焙烤") || s.equals("风扇烤")
                || s.equals("烤烧") || s.equals("强烤烧") || s.equals("焙烤") || s.equals("底加热")
                ||"煎烤".equals(s)||"果蔬烘干".equals(s)
                ) {
            for (int i = 1; i <= 120; i++) {
                list.add(i);
            }
        }else if ("快速预热".equals(s)){
            list.add(90);
        }else if ("保温".equals(s)){
            list.add(120);
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }

        /*if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
            ovenWarningDialog.dismiss();
            ovenWarningDialog = null;
        }*/

        alarmModel();//警报
        compareMode();
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        oven.setOvenRunMode(model, time, temp, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                goToWorking();
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    Oven026OrderTimeDialog orderTimeDialog;
    short model = 0;

    private void goToWorking(){
        UIService.getInstance().popBack();
        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.Guid, oven.getID());
        bundle.putShort("from", (short) 0);
        UIService.getInstance().postPage(PageKey.DeviceOven028Working, bundle);
    }

    private void compareMode(){
        if ("快热".equals(modeWheel.getSelectedText())) {
            model = 1;
        } else if ("风焙烤".equals(modeWheel.getSelectedText())) {
            model = 2;
        } else if ("焙烤".equals(modeWheel.getSelectedText())) {
            model = 3;
        } else if ("底加热".equals(modeWheel.getSelectedText())) {
            model = 4;
        } else if ("风扇烤".equals(modeWheel.getSelectedText())) {
            model = 6;
        } else if ("烤烧".equals(modeWheel.getSelectedText())) {
            model = 7;
        } else if ("强烤烧".equals(modeWheel.getSelectedText())) {
            model = 8;
        } else if ("快速预热".equals(modeWheel.getSelectedText())){
            model = 10;
        } else if ("煎烤".equals(modeWheel.getSelectedText())){
            model = 11;
        }else if ("果蔬烘干".equals(modeWheel.getSelectedText())){
            model = 12;
        }else if ("保温".equals(modeWheel.getSelectedText())){
            model = 15;
        }
    }

    private void alarmModel(){
        if (oven.status == OvenStatus.AlarmStatus) {
            switch (oven.alarm) {
                case AbsOven.Event_Oven_Heat_Fault:
                    //   showDialog("错误：加热故障", oven.alarm);
                    break;
                case AbsOven.Event_Oven_Alarm_Senor_Fault:
                    //   showDialog("错误：传感器故障", oven.alarm);
                    break;
                case AbsOven.Event_Oven_Communication_Fault:
                    //    showDialog("错误：通信故障", oven.alarm);
                    break;
                default:
                    break;
            }
            return;
        }
    }
}
