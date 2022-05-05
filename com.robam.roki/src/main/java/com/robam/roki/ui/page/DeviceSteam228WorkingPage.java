package com.robam.roki.ui.page;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.OnClick;

/**
 * Created by yinwei on 2017/9/6.
 */

public class DeviceSteam228WorkingPage extends AbsSteamWorkPage {

    public void initView(){
        steam226_working_img_light_circle.setVisibility(View.GONE);
        steam226_working_img_light.setVisibility(View.GONE);
        steam228_opentank.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (steam226 == null || !Objects.equal(steam226.getID(), event.pojo.getID()) || timer != null || training_lock)
            return;
        if (steam226.waterboxstate==0){
            steam228_opentank.setImageResource(R.mipmap.steam_tank_yellow);
        }else{
            steam228_opentank.setImageResource(R.mipmap.img_steam228_watertank_white);
        }
        if (steam226.status == SteamStatus.Working || steam226.status == SteamStatus.PreHeat) {
            if (iRokiDialog!=null&&iRokiDialog.isShow()){
                iRokiDialog.dismiss();
            }
            if (dialogByType!=null&&dialogByType.isShow()){
                dialogByType.dismiss();
            }
            signDialog = 0;
            setWorkMode();
        } else if (steam226.status == SteamStatus.Pause) {
            signDialog += 1;
            setPauseMolde();
        } else if (steam226.status == SteamStatus.Order) {
            setOrderMolde();
        } else if (steam226.status == SteamStatus.Wait || steam226.status == SteamStatus.Off || steam226.status == SteamStatus.On) {
            if (iRokiDialog!=null&&iRokiDialog.isShow()){
                iRokiDialog.dismiss();
            }
            if (closedialog!=null&&closedialog.isShow()){
                closedialog.dismiss();
            }
            LogUtils.i("20180116","isRunningForeGround:"+isRunningForeground());
            if (isRunningForeground())
                back();
        } else if (steam226.status == SteamStatus.AlarmStatus) {
            signDialog += 1;
            setPauseMolde();
            UIService.getInstance().popBack();
        }
        if (steam226.status != SteamStatus.AlarmStatus) {
            if (iRokidialogAlarm != null && iRokidialogAlarm.isShow())
                iRokidialogAlarm.dismiss();
           // steam226_working_img_wateryeild.setImageDrawable(r.getDrawable(R.mipmap.ic_steam226_water_write));
        }
    }

     public void initModel() {
        switch (steam226.mode) {
            case 17://鲜嫩蒸
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_common_xianneizheng_work);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.freshsteam));
                break;
            case 16://营养蒸
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_yingyangzheng);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.nutritive));
                break;
            case 13://强力蒸
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_qiangli_white);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.strongsteam));
                break;
            case 15://快蒸慢炖
                steam226_working_img_circledown.setImageResource(R.mipmap.img_steam228_fast_steam_slow_steam_working);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.fast_steam_slow_steam));
                break;
            case 19://保温
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906ovenwork_baowen_white);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.keeptempture));
                break;
            case 9://解冻
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_jiedong_white);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.unfreeze));
                break;
            case 18://发酵
                steam226_working_img_circledown.setImageResource(R.mipmap.img_steam228_fajiao);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.ferment));
                break;
            case 14://杀菌
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_shajun_white);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.sterilization));
                break;
            case 20://除垢
                steam226_working_img_circledown.setImageResource(R.mipmap.device_steam_228_clean);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.clean));
                break;
            case 21://干燥
                steam226_working_img_circledown.setImageResource(R.mipmap.steam228_working_dry);
                steam226_working_tv_circleabove.setText(cx.getResources().getString(R.string.dry));
                break;
            default:
                /*steam226_working_tv_circleabove.setVisibility(View.GONE);
                steam226_working_tv_circledown.setVisibility(View.VISIBLE);
                steam226_working_tv_circledown.setText(new String("自定义"));
                steam226_working_img_circledown.setVisibility(View.GONE);
                steam226_working_fra_middle.setClickable(false);*/
                break;
        }
    }
    //返回
    public void back() {
        if (from == 1) {
            UIService.getInstance().popBack();
           // UIService.getInstance().returnHome();
         /*   Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, steam226.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteam228, bd);*/
        } else {
            UIService.getInstance().popBack();
        }
    }

    //点击打开水箱
    @OnClick(R.id.steam228_opentank)
    public void onClickWaterTank(){
        if (checkWaterOut_Dialog()){
            return;
        }else{
            openSteamTank();
        }

    }

    boolean checkWaterOut_Dialog() {
        if (steam226.waterboxstate == 0) {
            dialogByType.setContentText(R.string.device_alarm_water_out);
            if (!dialogByType.isShow()){
                dialogByType.show();
                return true;
            }
            return true;
        }

        return false;
    }

    private void openSteamTank(){
        //if (!checkConnection()) return;
        if (steam226.alarm!=0) return;
        if (steam226.status==SteamStatus.Working||steam226.status==SteamStatus.PreHeat||
                steam226.status==SteamStatus.Order){
            ToastUtils.show("不可操控，请先暂停工作",Toast.LENGTH_SHORT);
            return;
        }
        steam226.setSteamWaterTankPOP(new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show(cx.getResources().getString(R.string.opentanksuccess), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void Stean026Fault_Dialog(short type) {

        if (steam226PauseSettingDialog != null && steam226PauseSettingDialog.isShowing()) {
            steam226PauseSettingDialog.dismiss();
            steam226PauseSettingDialog = null;
        }

        View view = LayoutInflater.from(cx).inflate(R.layout.dialog_steam226_falut, null, false);
        view.findViewById(R.id.dialog_fault_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAllDialog();
                UIService.getInstance().postPage(PageKey.SaleService);
            }
        });
        TextView textView = view.findViewById(R.id.steam226_tv_error_text);
        TextView tv = view.findViewById(R.id.steam226_tv_error);
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                textView.setText("加热故障");
                tv.setText("错误 : E03");
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                textView.setText("传感器故障");
                tv.setText("错误 : E05");
                break;
            case AbsSteamoven.Event_Steam228_Alarm_communication_fault:
                textView.setText("通信故障");
                tv.setText("错误 : E06");
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                textView.setText("通信故障");
                tv.setText("错误 : E06");
                break;
        }

    }
}
