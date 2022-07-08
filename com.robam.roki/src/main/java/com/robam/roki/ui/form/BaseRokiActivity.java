package com.robam.roki.ui.form;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.ui.ext.BaseActivity;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.WaterPurifiyAlarmEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierAlarm;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.StoveAlarmManager;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;

/**
 * Created by Dell on 2018/1/16.
 */

public abstract  class BaseRokiActivity extends BaseActivity {

    public IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
    public IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警

    @Override
    protected void setContentView() {
//        setContentView(com.legent.ui.R.layout.abs_activity);
//        setContentView(R.layout.abs_activity);
        super.setContentView();
        init();
    }

    public void init(){
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(BaseRokiActivity.this, DialogUtil.DIALOG_TYPE_01);
        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(BaseRokiActivity.this, DialogUtil.DIALOG_TYPE_02);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        onStoveAlarmEvent(event.stove, event.alarm);
    }

    //电烤箱报警事件
    @Subscribe
    public void onEvent(OvenAlarmEvent event) {
        LogUtils.i("2020031203","event:::"+event.alarmId);
        AbsOven oven = event.oven;
        String guid = oven.getGuid().getGuid();
        ovenAlarmStatus(event.alarmId, guid);
    }

    //净水器报警事件
    @Subscribe
    public void onEvent(WaterPurifiyAlarmEvent event) {
        iRokiDialogAlarmType_01.setTitleText(R.string.device_water_name);
        iRokiDialogAlarmType_02.setTitleText(R.string.device_water_name);
        if (Plat.DEBUG)
            LogUtils.i("20170928", "alarmId:" + event.alarmId);
        switch (event.alarmId) {
            case WaterPurifierAlarm.Water_Leak://漏水
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_water_E2_content);
                makePhoneCallListenr();
                break;
            case WaterPurifierAlarm.Water_Making://长时间制水
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_E3_content);
                centerOneBtnListener();
                break;
            case WaterPurifierAlarm.Water_None://缺水
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E1);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_E1_content);
                centerOneBtnListener();
                break;
            default:
                break;
        }
    }

    //蒸汽炉报警接收事件
    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        AbsSteamoven steam = event.steam;
        String guid = steam.getGuid().getGuid();
        SteamAlarmStatus(event.alarmId, guid);
    }

    @Subscribe
    public void onEvent(SteamOvenOneAlarmEvent event) {
        AbsSteameOvenOne steameOvenOne = event.steameOvenOne;
        String guid = steameOvenOne.getGuid().getGuid();
        short[] alarms = event.alarms;
        for (int i = 0; i < alarms.length; i++) {
            short alarm = alarms[i];
            steamOvenOneAlarmStatus(i,alarm, guid);
        }

    }

    //@Subscribe
    //public void onEvent(DishWasherAlarmEvent event){
    //    short alarmId = event.alarmId;
    //}

    @Subscribe
    public void onEvent(SteriAlarmEvent event) {
        AbsSterilizer absSterilizer = event.absSterilizer;
        String guid = absSterilizer.getGuid().getGuid();
        onSteriAlarmEvent(guid, event.alarm);
    }

    //微波炉报警接收事件
    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        onMicroWaveAlarmEvent();
    }


    private void onMicroWaveAlarmEvent() {
        iRokiDialogAlarmType_01.setTitleText(R.string.device_microwave_name);
        iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_microwave_err_content);
        makePhoneCallListenr();
    }

    //消毒柜
    private void onSteriAlarmEvent(String guid, short alarm) {

        LogUtils.i("20170918", "alarmId；" + alarm);
        LogUtils.i("20170918", "guid；" + guid);
        if (IRokiFamily.RR829.equals(guid.substring(0, 5))) {
            r829AralmDispose(alarm);
        } else  {
            r826AralmDispose(alarm);
        }
    }


    @Subscribe
    public void onEvent(SteamCleanResetEvent event) {
        ToastUtils.show("长时间使用，您需要除垢清洁蒸汽炉", Toast.LENGTH_SHORT);
    }
    //电烤箱
    private void ovenAlarmStatus(short alarmId, String guid) {

        iRokiDialogAlarmType_01.setTitleText(R.string.device_oven_name);
        iRokiDialogAlarmType_02.setTitleText(R.string.device_oven_name);
        if ((StringConstantsUtil.DEVICE_OVEN_R039.equals(guid.substring(0, 5)))) {
            oven039Alarm(alarmId);
        }  else if (StringConstantsUtil.DEVICE_OVEN_R028.equals(guid.substring(0, 5))) {
            LogUtils.i("2020031203","event:::2:::");
            oven028Alarm(alarmId);
        }else  {
            LogUtils.i("2020031203","event:::3:::");
            oven026And016Alarm(alarmId);
        }
    }






    //烤箱028报警分类
    private void oven028Alarm(short alarmId) {
        //TODO
        switch (alarmId) {
            case AbsOven.Event_Oven_Heat_Fault://加热故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E3_content);
                makePhoneCallListenr();
                break;
            case AbsOven.Event_Oven_Alarm_Senor_Fault://传感器故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E5_content);
                makePhoneCallListenr();
                break;
            case AbsOven.Event_Oven_Communication_Fault://通信故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E6_content);
                makePhoneCallListenr();
                break;
            case AbsOven.Event_Oven_fan_Fault://风机故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E7);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven028_fan_content);
                makePhoneCallListenr();
                break;
            default:
                break;
        }
    }

    //烤箱016/026报警分类
    private void oven026And016Alarm(short alarmId) {
        switch (alarmId) {
            case AbsOven.Event_Oven_Heat_Fault://加热故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E3_content);
                makePhoneCallListenr();
                break;
            case AbsOven.Event_Oven_Alarm_Senor_Fault://传感器故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E5_content);
                makePhoneCallListenr();

                break;
            case AbsOven.Event_Oven_Communication_Fault://通信故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven026_E6_content);
                makePhoneCallListenr();
                break;
            case AbsOven.Event_Oven_fan_Fault://风机故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E7);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven028_fan_content);
                makePhoneCallListenr();
                break;
            default:
                break;
        }
    }

    //烤箱039报警分类
    private void oven039Alarm(short alarmId) {
        switch (alarmId) {
            case Oven039.Event_Oven039_Alarm_Senor_Fault://加热故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven039_E3_content);
                makePhoneCallListenr();
                break;
            case Oven039.Event_Oven_Alarm_Senor_Short:// 传感器短路
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E1);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven039_E1_content);
                makePhoneCallListenr();
                break;
            case Oven039.Event_Oven_Alarm_Senor_Open://传感器开路
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_oven039_E2_content);
                makePhoneCallListenr();
                break;
            default:
                break;
        }
    }

    //消毒柜829
    private void r829AralmDispose(short alarmId) {
        iRokiDialogAlarmType_01.setTitleText(R.string.device_sterilizer_name);
        switch (alarmId) {

            case 2://紫外线灯管不工作或上层传感器不良
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_ultraviolet_E2);
                makePhoneCallListenr();
                break;

            default:
                break;
        }
    }

    //消毒柜826报警分类
    private void r826AralmDispose(short alarm) {
        iRokiDialogAlarmType_01.setTitleText(R.string.device_sterilizer_name);
        switch (alarm) {
            case 5://传感器不良
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_01.setContentText(R.string.device_steri_alarm_sensor_content);
                makePhoneCallListenr();
                break;
            case 6://通讯故障
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_01.setContentText(R.string.device_steri_alarm_fault_content);
                makePhoneCallListenr();
                break;
            default:
                break;
        }
    }

    //灶具报警分类
    private void onStoveAlarmEvent(Stove stove, StoveAlarm alarm) {
        short id = alarm.getID();
        iRokiDialogAlarmType_01.setTitleText(R.string.device_stove_name);
        iRokiDialogAlarmType_02.setTitleText(R.string.device_stove_name);
        switch (id) {
            case StoveAlarmManager.E_0:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_0);
                break;
            case StoveAlarmManager.E_2:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_2);
                break;
            case StoveAlarmManager.E_3:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_3);
                break;
            case StoveAlarmManager.E_4:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_4);
                break;
            case StoveAlarmManager.E_5:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_5);
                break;
            case StoveAlarmManager.E_6:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_6);
                break;
            case StoveAlarmManager.E_7:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_7);
                break;
            case StoveAlarmManager.E_8:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_8);
                break;
            case StoveAlarmManager.E_D:
                aLLStoveAlarmDispose(stove, StoveAlarmManager.E_D);
                break;
        }

    }

    //将所有灶具分类
    private void aLLStoveAlarmDispose(Stove stove, short alarmId) {

        LogUtils.i("20170929", "main_alarmId:" + alarmId);

        if (stove.getStoveModel().equals(IRokiFamily.R9W70)) {
            alarmDispose9w70(alarmId);
        } else if (stove.getStoveModel().equals(IRokiFamily.R9B39)) {
            alarmDispose9b39(alarmId);
        } else if (stove.getStoveModel().equals(IRokiFamily.R9B37)) {
            alarmDispose9b37(alarmId);
        }
    }

    private void alarmDispose9w70(short alarmId) {
        switch (alarmId) {
            case StoveAlarmManager.E_0:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E0);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9w70_E0_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_2:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E2_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_3:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E3_content);
                centerOneBtnListener();

                break;
            case StoveAlarmManager.E_4:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E4);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E4_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_5:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E5_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_6:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E6_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_7:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E7);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_E7_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_D:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_ED);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9w70_ED_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_9:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E9);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9w70_ED_content);
                centerOneBtnListener();
                break;
        }
    }

    private void alarmDispose9b39(short alarmId) {
        switch (alarmId) {
            case StoveAlarmManager.E_0:
                break;
            case StoveAlarmManager.E_2:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9b39_E2_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_3:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b39_E3_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_4:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E4);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b39_E4_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_6:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E7);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b39_E7_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_7:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E8);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b39_E8_content);
                makePhoneCallListenr();
                break;
        }
    }

    private void alarmDispose9b37(short alarmId) {
        LogUtils.i("20170929", "alarmId:" + alarmId);
        switch (alarmId) {
            case StoveAlarmManager.E_2:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_stove_9b37_E2_content);
                centerOneBtnListener();
                break;
            case StoveAlarmManager.E_3:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b37_E3_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_4:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E4);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b37_E4_content);
                makePhoneCallListenr();
                break;
            case StoveAlarmManager.E_5:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_stove_9b37_E5_content);
                makePhoneCallListenr();
                break;
        }

    }

    /**
     * 蒸汽炉报警分类
     *
     * @param alarmId  报警编码
     * @param deviceId 设备ID
     * @return
     */
    private boolean SteamAlarmStatus(short alarmId, String deviceId) {
        boolean cause = false;
        LogUtils.i("20170122", "alarmId:" + alarmId);
        switch (alarmId) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                fault_Dialog(AbsSteamoven.Event_Steam226_Alarm_lack_water, deviceId);
                break;
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                fault_Dialog(AbsSteamoven.Event_Steam226_Alarm_heat, deviceId);
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                fault_Dialog(AbsSteamoven.Event_Steam226_Alarm_sensor, deviceId);
                break;
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                fault_Dialog(AbsSteamoven.Event_Steam226_Alarm_communication_fault, deviceId);
                break;
            case AbsSteamoven.Event_Steam_Alarm_temp:
                fault_Dialog(AbsSteamoven.Event_Steam_Alarm_temp, deviceId);
                break;
            case AbsSteamoven.Event_Steam228_Alarm_communication_fault:
                fault_Dialog(AbsSteamoven.Event_Steam228_Alarm_communication_fault, deviceId);
                break;
            default:
                cause = true;
                break;
        }
        return cause;
    }


    private void fault_Dialog(short type, String guid) {
        if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
            return;
        }
        iRokiDialogAlarmType_01.setTitleText(R.string.device_steam_name);
        iRokiDialogAlarmType_02.setTitleText(R.string.device_steam_name);
        if (StringConstantsUtil.DEVICE_STEAM_S209.equals(guid.substring(0, 5))) {
            s209AralmDispose(type);
        }else if (StringConstantsUtil.DEVICE_STEAM_S228.equals(guid.substring(0,5))){
            s228AlarmDipose(type);
        } else {
            s226AralmDispose(type);
        }
    }

    //蒸箱S209
    private void s209AralmDispose(short type) {
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
                centerOneBtnListener();
                break;
            case AbsSteamoven.Event_Steam_Alarm_temp:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E2);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_sensor_content);
                makePhoneCallListenr();
                break;
        }
    }


    private void s228AlarmDipose(short type){
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
                centerOneBtnListener();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                iRokiDialogAlarmType_01.setTitleAralmCodeText("报警");
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_heat228_content);
                makePhoneCallListenr();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                iRokiDialogAlarmType_01.setTitleAralmCodeText("报警");
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_sensor_content);
                makePhoneCallListenr();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                iRokiDialogAlarmType_01.setTitleAralmCodeText("报警");
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_fault_content);
                makePhoneCallListenr();
                break;
            case AbsSteamoven.Event_Steam228_Alarm_communication_fault:
                iRokiDialogAlarmType_01.setTitleAralmCodeText("报警");
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_fengji_content);
                makePhoneCallListenr();
                break;
        }
    }


    //蒸箱S226
    private void s226AralmDispose(short type) {
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
                iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
                centerOneBtnListener();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E3);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_heat_content);
                makePhoneCallListenr();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E5);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_sensor_content);
                makePhoneCallListenr();
                break;
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm_E6);
                iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_fault_content);
                makePhoneCallListenr();
                break;
        }
    }

    //一体机报警处理
    private void steamOvenOneAlarmStatus(int i,short alarm, String guid) {

        if (alarm != 0){
            LogUtils.i("20171208"," i:"+ i +" alarm" + alarm);
            switch (i) {

                case 0:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_temp_unusual_content);
                    centerOneBtnListener();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
                    centerOneBtnListener();
                    break;
                case 4:
                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_heat_elimination_fan_content);
                    makePhoneCallListenr();
                    break;
                case 5:
                case 6:
                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_temp_sensor_content);
                    makePhoneCallListenr();
                    break;
                case 7:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_steamOvenOne_warm_tips);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_content);
                    centerOneBtnListener();
                    break;

            }
        }

    }


    //拨打电话
    private void makePhoneCallListenr() {
        iRokiDialogAlarmType_01.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn(R.string.can_good, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_01.show();
        LogUtils.i("20170918", "show:" + iRokiDialogAlarmType_01.isShow());
    }

    /**
     * 缺水按钮设置
     */
    private void centerOneBtnListener() {
        iRokiDialogAlarmType_02.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_02.dismiss();
            }
        });
        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_02.show();
    }

}
