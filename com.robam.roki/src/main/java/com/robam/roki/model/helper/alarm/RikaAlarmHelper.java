package com.robam.roki.model.helper.alarm;


import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;

import static com.robam.roki.ui.form.MainActivity.activity;

/**
 * Created by 14807 on 2018/5/17.
 */
public class RikaAlarmHelper{
   static IRokiDialog alramOne = null;//一级报警
   static IRokiDialog alramTwo = null;//二级报警

    public static void rikaAlarmStatus(RikaAlarmCodeBean alarmCodeBean){

        alramTwo = RokiDialogFactory.createDialogByType(activity, DialogUtil.DIALOG_TYPE_02);
        alramOne = RokiDialogFactory.createDialogByType(activity, DialogUtil.DIALOG_TYPE_01);
        alramOne.setTitleText(R.string.device_rika_name);
        alramTwo.setTitleText(R.string.device_rika_name);
        fanStatus(alarmCodeBean.getFanAlarmCode());
        stoveStatus(alarmCodeBean.getStoveAlarmCode());
        steamStatus(alarmCodeBean.getSteamAlarmCode());
        sterilizerStatus(alarmCodeBean.getSterilAlarmCode());
    }

    private static void fanStatus(short fanAlarmCode) {

    }

    private static void stoveStatus(short stoveAlarmCode) {

        switch (stoveAlarmCode){

            case 0:

             break;

            case 2:

                break;

            case 3:

                break;

            case 4:

                break;

            case 5:

                break;

            default:

        }
    }

    private static void steamStatus(short steamAlarmCode) {

        switch (steamAlarmCode){
            case IRikaAlarmCode.STEAM_1 :
                alramTwo.setTitleAralmCodeText(R.string.water_common_queshui);
                alramTwo.setContentText(R.string.device_alarm_water_shortage_content);
                centerOneBtnListener();
                 break;
            case IRikaAlarmCode.STEAM_3 :
                alramOne.setTitleAralmCodeText(R.string.device_alarm_E3);
                alramOne.setContentText(R.string.device_alarm_rika_E3_content);
                makePhoneCallListenr();
                 break;
            case IRikaAlarmCode.STEAM_5 :
                alramOne.setTitleAralmCodeText(R.string.device_alarm_E5);
                alramOne.setContentText(R.string.device_alarm_rika_E5_content);
                makePhoneCallListenr();
                 break;
            case IRikaAlarmCode.STEAM_6 :
                alramOne.setTitleAralmCodeText(R.string.device_alarm_E6);
                alramOne.setContentText(R.string.device_alarm_rika_E6_content);
                makePhoneCallListenr();
                 break;
            case IRikaAlarmCode.STEAM_7 :
                alramOne.setTitleAralmCodeText(R.string.device_alarm_E7);
                alramOne.setContentText(R.string.device_alarm_rika_E7_content);
                makePhoneCallListenr();
                 break;
            case IRikaAlarmCode.STEAM_8 :
                alramOne.setTitleAralmCodeText(R.string.device_alarm_E8);
                alramOne.setContentText(R.string.device_alarm_rika_E8_content);
                makePhoneCallListenr();
                 break;
            case IRikaAlarmCode.STEAM_9 :
                LogUtils.i("20180709","alramTwo:" + alramTwo.isShow());
                alramTwo.setTitleAralmCodeText(R.string.device_alarm_E9_code);
                alramTwo.setContentText(R.string.device_alarm_rika_E9_content);
                centerOneBtnListener();
                break;

            default:
                break;

        }

    }

    private static void sterilizerStatus(short sterilAlarmCode) {

    }

    //拨打电话
    private static void makePhoneCallListenr() {
        alramOne.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alramOne.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                alramOne = null;
            }
        });
        alramOne.setCancelBtn(R.string.can_good, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alramOne = null;
            }
        });
        alramOne.setCanceledOnTouchOutside(false);
        alramOne.show();
    }

    /**
     * 缺水按钮设置
     */
    private static void centerOneBtnListener() {

        alramTwo.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alramTwo.dismiss();
            }
        });
        alramTwo.setCanceledOnTouchOutside(false);
        alramTwo.show();
    }

}
