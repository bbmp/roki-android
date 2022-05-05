package com.robam.roki.ui.page;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.events.SteamWaterBoxEvent;
import com.robam.common.pojos.SteamUserAction;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


import static com.robam.roki.R.id.steam228_open_watertank;


/**
 * Created by yinwei on 2017/8/31.
 */

public class DeviceSteam228MainPage extends BasePage {
    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;
    @InjectView(R.id.steam228_main_waternotice)
    LinearLayout steam228MainWaternotice;
    @InjectView(R.id.steam228_btn)
    Button steam228_btn;
    @InjectView(R.id.steam_main_imgContent)
    ImageView steam_main_imgContent;//圆环
    @InjectView(R.id.steam228_img)
    ImageView steam228_img;
    @InjectView(R.id.steam228_txt)
    TextView  steam228_txt;
    @InjectView(R.id.steam228_steam)
    RelativeLayout steam228Steam;
    @InjectView(steam228_open_watertank)
    ImageView steam228OpenWatertank;
    @InjectView(R.id.steam228_assist)
    RelativeLayout steam228Assist;
    @InjectView(R.id.steam228_assist_child)
    ImageView steam228_assist_child;
    @InjectView(R.id.steam228_assit_txt)
    TextView steam228_assit_txt;
    @InjectView(R.id.steam228_main_img_switch)
    ImageView steam228MainImgSwitch;
    @InjectView(R.id.steam228_main_ll_switch)
    LinearLayout steam228MainLlSwitch;
    @InjectView(R.id.steam_switch)
    FrameLayout steamSwitch;

    String guid;
    AbsSteamoven steam;
    LayoutInflater inflater;
    View contentView;
    private IRokiDialog dialogByType;
    IRokiDialog iRokidialogAlarm = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steam = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_steam228_main, container, false);
        ButterKnife.inject(this, contentView);
        init();
        iRokidialogAlarm = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    void init() {
        if (Plat.DEBUG)
         LogUtils.i("20170904","steam.isconnect::"+ steam.isConnected());
        if (steam.isConnected()){
            disconnectHintView.setVisibility(View.INVISIBLE);
        }else{
            disconnectHintView.setVisibility(View.VISIBLE);
        }
    }

    //开关机
    @OnClick(R.id.steam_switch)
    public void onAndOff(){
        onOrOff();
    }

    @OnClick(R.id.oven026_return)
    public void back(){
        UIService.getInstance().popBack();
    }

    //点击事件
    @OnClick({R.id.steam228_open_watertank, R.id.steam228_assist,
            R.id.steam228_btn,R.id.steam228_steam})
    public void onViewClicked(View view) {
        LogUtils.i("20180111","这里点击了");
        switch (view.getId()) {
            case R.id.steam228_btn://最近使用
                if (!alarmStatus(steam.alarm)){
                    ToastUtils.show("请先解除警报",Toast.LENGTH_SHORT);
                    return;
                }
                onclick_recently_user();
                break;
            case R.id.steam228_open_watertank://弹出水箱
                if (!alarmStatus(steam.alarm)){
                    ToastUtils.show("请先解除警报",Toast.LENGTH_SHORT);
                    return;
                }
                openSteamTank();
                break;
            case R.id.steam228_assist://辅助
                if (!alarmStatus(steam.alarm)){
                    ToastUtils.show("请先解除警报",Toast.LENGTH_SHORT);
                    return;
                }
                assit();
                break;
            case R.id.steam228_steam://蒸
                if (!alarmStatus(steam.alarm)){
                    ToastUtils.show("请先解除警报",Toast.LENGTH_SHORT);
                    return;
                }
                steam();
                break;
        }
    }

    Dialog dialog=null;
    private void onclick_recently_user(){
        if (!checkConnection()) return;
        if (!checkStateAndConnect()) return;
        dialog = Helper.newSteam228RecentlyUseDialog(cx,new Callback2<SteamUserAction>(){

            @Override
            public void onCompleted(SteamUserAction steamUserAction) {
                sendRecently(steamUserAction);
            }
        },steam);
    }


    @Subscribe
    public void onEvent(SteamWaterBoxEvent event){
        ToastUtils.show(R.string.device_alarm_water_out,Toast.LENGTH_SHORT);
    }

    private void sendRecently(SteamUserAction steamUserAction){
        short n = (short) 0;
        if (checkWaterOut_Dialog()) return;
        steam.setSteamCookMode(steamUserAction.getMode(),steamUserAction.getTemperature(),steamUserAction.getTimeCook(), n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steam.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam228Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //蒸的点击响应事件
    private void steam(){
        if (!checkConnection()) return;
        if (!checkStateAndConnect()) return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, steam.getID());
        UIService.getInstance().postPage(PageKey.DeviceSteam228ProfessionalSetting, bd);
    }



    //辅助模式响应事件
    private void assit(){
        if (!checkConnection()) return;
        if (!checkStateAndConnect()) return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, steam.getID());
        UIService.getInstance().postPage(PageKey.DeviceSteam228assistSetting, bd);
    }

    boolean checkStateAndConnect() {
        if (steam.status == OvenStatus.Off ) {
            StartNotice();
            return false;
        }
        return true;
    }

    void StartNotice() {
        if (dialogByType != null && dialogByType.isShow()){
            LogUtils.i("20171207","dialogByType:");
            return;
        }

        dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
        dialogByType.setContentText(R.string.open_device);
        dialogByType.show();
    }

    boolean checkWaterOut_Dialog() {
        if (steam.waterboxstate == 0) {
            LogUtils.i("20180111","waterboxstate::"+steam.waterboxstate);
            dialogByType.setContentText(R.string.device_alarm_water_out);
            if (!dialogByType.isShow()){
                dialogByType.show();
                return true;
            }
            return true;
        }

        return false;
    }

    public void closeAlarmDialog() {
        if (iRokidialogAlarm != null && iRokidialogAlarm.isShow())
            iRokidialogAlarm.dismiss();
    }

    void closeAllDialog() {
        closeAlarmDialog();
    }

        //判断连接是否建立
    private boolean checkConnection() {
        if (steam.isConnected()) {
            return true;
        } else {
            ToastUtils.show(cx.getResources().getString(R.string.networkwrong), Toast.LENGTH_SHORT);
            return false;
        }
    }

    //连网状态改变接收事件UI状态变化
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.device.getID()))
            return;
        if (event.isConnected) {
            disconnectHintView.setVisibility(View.INVISIBLE);
        } else {
            disconnectHintView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (Plat.DEBUG)LogUtils.i("20170904","ev::"+event.pojo.getID());
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceSteam228Main)) {
            return;
        }
        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID()))
            return;

        if (steam.status == SteamStatus.Off) {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
                online(false);
        } else if (steam.status == SteamStatus.On) {
                online(true);
        } else if (steam.status == SteamStatus.PreHeat || steam.status == SteamStatus.Order ||
                steam.status == SteamStatus.Working || steam.status == SteamStatus.Pause) {
         //   closeAllDialog();
            if (dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, event.pojo.getID());
            bundle.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceSteam228Working, bundle);
        } else if (steam.status == SteamStatus.AlarmStatus) {
            online(true);
        }
        if (steam.status != SteamStatus.AlarmStatus) {
            //closeAlarmDialog();
        }
    }

    private void openSteamTank(){
        if (!checkConnection()) return;
        if (!alarmStatus(steam.alarm)) return;
        if (!checkStateAndConnect()) return;
        if (checkWaterOut_Dialog()) return;
        steam.setSteamWaterTankPOP(new VoidCallback() {
            @Override
            public void onSuccess() {

                ToastUtils.show(cx.getResources().getString(R.string.opentanksuccess),Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //ui状态在线离线的变化
    private void online(boolean flag){
        if (steam.waterboxstate==0){
            steam228OpenWatertank.setImageResource(R.mipmap.steam_tank_yellow);
        }else{
            steam228OpenWatertank.setImageResource(R.mipmap.img_steam228_watertank_white);
        }
        steam228_btn.setBackgroundResource(flag ? R.mipmap.img_common_recently_use_yellow : R.mipmap.img_common_recently_use_gray);
        steam228_btn.setTextColor(cx.getResources().getColor(flag ? R.color.yellow_use : R.color.device_oven_gray));
        steam_main_imgContent.setImageResource(flag ? R.mipmap.img_common_big_circle_white : R.mipmap.img_common_big_circle_gray);
        steam228_img.setImageResource(flag ? R.mipmap.img_device_steam228_steam_white : R.mipmap.img_device_steam028_steam_gray);
        steam228_txt.setTextColor(flag ? Color.parseColor("#ffffff") : Color.parseColor("#575757"));

        steam228_assist_child.setImageResource(flag ? R.mipmap.img_common_circle_white : R.mipmap.img_common_circle_gray);
        steam228_assit_txt.setTextColor(flag ? Color.parseColor("#ffffff") : Color.parseColor("#575757"));
       // steamSwitch.setBackgroundResource(flag ? R.mipmap.ic_device_oven_leanline_yellow : R.mipmap.ic_device_oven_leanline_white);
        steam228MainImgSwitch.setImageResource(R.mipmap.btn_power_open);
    }

    //开机关机UI状态变化
    private void onOrOff(){
        if (steam.status == SteamStatus.Wait || steam.status == SteamStatus.Off){
            steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    online(true);
                }

                @Override
                public void onFailure(Throwable t) {
                    //ToastUtils.show(cx.getResources().getString(R.string.device_Failure_text),Toast.LENGTH_SHORT);
                }
            });
        } else if (steam.status == SteamStatus.On||steam.status == SteamStatus.AlarmStatus){
            steam.setSteamStatus(SteamStatus.Off, new VoidCallback() {
                @Override
                public void onSuccess() {
                    online(false);
                }

                @Override
                public void onFailure(Throwable t) {
                   // ToastUtils.show(cx.getResources().getString(R.string.device_Failure_text),Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private boolean alarmStatus(short type) {
        boolean cause = false;
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                break;
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                break;
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                break;
            case AbsSteamoven.Event_Steam228_Alarm_communication_fault:
                break;
            default:
                cause = true;
                break;
        }
        return cause;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
