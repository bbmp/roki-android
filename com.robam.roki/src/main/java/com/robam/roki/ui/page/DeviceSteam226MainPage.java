package com.robam.roki.ui.page;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
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
import com.robam.common.pojos.SteamUserAction;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamMode026;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
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

/**
 * Created by Administrator on 2016/11/2.
 */

public class DeviceSteam226MainPage extends BasePage {
    @InjectView(R.id.disconnectHintView)
    View disconnectHintView;
    @InjectView(R.id.steam226_main_imgContent)//中间圆圈
            ImageView steam226_main_imgContent;
    @InjectView(R.id.steam226_main_imgContent_text)
    TextView steam226_main_imgContent_text;
    @InjectView(R.id.steam226_main_translate)
    LinearLayout steam226_main_translate;//杀菌
    @InjectView(R.id.steam226_main_translate_text)
    TextView steam226_main_translate_text;
    @InjectView(R.id.steam226_main_openwatertank)//开启水箱
            LinearLayout steam226_main_openwatertank;
    @InjectView(R.id.steam226_main_openwatertank_text)
    TextView steam226_main_openwatertank_text;
    @InjectView(R.id.steam226_main_img_switch)//开关图标
            ImageView steam226_main_img_switch;
    @InjectView(R.id.mic_txtRecipe)
            LinearLayout mic_txtRecipe;
    @InjectView(R.id.steam226_btn)
    Button steam226_btn;
    String guid;
    AbsSteamoven steam226;
    LayoutInflater inflater;
    View contentView;
    @InjectView(R.id.steam226_main_open_water_text)
    TextView mSteam226MainOpenWaterText;
//    private Tracker mTracker;
    IRokiDialog iRokidialogAlarm = null;
    private IRokiDialog dialogByType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mTracker = MobApp.getTracker();
        Bundle bd = getArguments();
//        mTracker.setScreenName("RS226");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("20170307", "guid:" + guid);
        steam226 = Plat.deviceService.lookupChild(guid);
        LogUtils.i("20170307", "steam226:" + steam226.getGuid());
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_steam226_main, container, false);
        ButterKnife.inject(this, contentView);
        init();
        iRokidialogAlarm = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    void init() {
        disconnectHintView.setVisibility(View.INVISIBLE);
        if (IRokiFamily.RS275.equals(steam226.getDt())){
            steam226_btn.setVisibility(View.VISIBLE);
        }else{
            steam226_btn.setVisibility(View.GONE);
        }
    }

//    private void sendAnalystics(String str) {
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory(steam226.getID().substring(0, 5))
//                .setAction(str).setLabel("Device")
//                .build());
//    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steam226 == null || !Objects.equal(steam226.getID(), event.device.getID()))
            return;
        if (event.isConnected) {
            disconnectHintView.setVisibility(View.INVISIBLE);
        } else {
            if (sterilization_dialog!=null&&sterilization_dialog.isShowing()){
                sterilization_dialog.dismiss();
            }
            disconnectHintView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceSteam226Main)) {
            return;
        }
        if (steam226 == null || !Objects.equal(steam226.getID(), event.pojo.getID()))
            return;
        if (steam226.status == SteamStatus.Wait || steam226.status == SteamStatus.Off) {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            closeAllDialog();
            setOff_Model();
        } else if (steam226.status == SteamStatus.On) {
            setOn_Model();
        } else if (steam226.status == SteamStatus.PreHeat || steam226.status == SteamStatus.Order ||
                steam226.status == SteamStatus.Working || steam226.status == SteamStatus.Pause) {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            closeAllDialog();
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, steam226.getID());
            bundle.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
        } else if (steam226.status == SteamStatus.AlarmStatus) {

        }
        if (steam226.status != SteamStatus.AlarmStatus) {
            closeAlarmDialog();
        }
    }

    //最近使用点击事件
    @OnClick(R.id.steam226_btn)
    public void OnClickRecentlyUse(){
        onclick_recently_user();
    }

    Dialog dialog=null;
    private void onclick_recently_user(){
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (!checkStateAndConnect()) return;
        if (checkWaterOut_Dialog()) return;
        dialog = Helper.newSteam228RecentlyUseDialog(cx,new Callback2<SteamUserAction>(){

            @Override
            public void onCompleted(SteamUserAction steamUserAction) {
                sendRecently(steamUserAction);
            }
        },steam226);
    }


    private void sendRecently(SteamUserAction steamUserAction){
        short n = (short) 0;
        steam226.setSteamCookMode(steamUserAction.getMode(),steamUserAction.getTemperature(),steamUserAction.getTimeCook(), n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steam226.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @OnClick(R.id.oven026_return)
    public void OnClickReturn() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.steam226_main_switch)//开关
    void OnSwitchClick() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if ("RS226".equals(steam226.getDt())){
            if (checkWaterOut_Dialog()) return;
        }
        if (steam226.status == SteamStatus.Wait || steam226.status == SteamStatus.Off)
            steam226.setSteamStatus(SteamStatus.On, null);
        else if (steam226.status == SteamStatus.On)
            steam226.setSteamStatus(SteamStatus.Off, null);
    }

    @OnClick(R.id.steam226_main_imgContent) //蒸点击事件
    void OnImgContentClick() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (!checkStateAndConnect()) return;
        if (checkWaterOut_Dialog()) return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, steam226.getID());
        if (IRokiFamily.RS275.equals(steam226.getDt())){
            UIService.getInstance().postPage(PageKey.DeviceSteam275ProfessionalSetting, bd);
        }else{
            UIService.getInstance().postPage(PageKey.DeviceSteam226ProfessionalSetting, bd);
        }
    }

    Dialog sterilization_dialog;

    @OnClick(R.id.steam226_main_translate)
    void OnSterilizationClick() {//杀菌
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (!checkStateAndConnect()) return;
        if (checkWaterOut_Dialog()) return;
        View view = null;
        if ("RS275".equals(steam226.getDt())){
             view = LayoutInflater.from(cx).inflate(R.layout.dialog_steam275_sterilization, null, false);
        }else{
             view = LayoutInflater.from(cx).inflate(R.layout.dialog_steam226_sterilization, null, false);
        }

        view.findViewById(R.id.steam226_sterilization_tvopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                short n = (short) 0;
                steam226.setSteamCookMode(SteamMode026.STERILIZATION, (short) 100, (short) 30, n, n, n, n, n, n, n, n, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (sterilization_dialog != null && sterilization_dialog.isShowing())
                            sterilization_dialog.dismiss();
                        UIService.getInstance().popBack();
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, steam226.getID());
                        bundle.putShort("from", (short) 0);
                        UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        sterilization_dialog = Helper.newBlackPromptDialog2(cx, view, R.style.Dialog_Microwave_professtion_bottom);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Window window = sterilization_dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.75);
        window.setAttributes(layoutParams);
        sterilization_dialog.show();
    }

    @OnClick(R.id.steam226_main_openwatertank)
    void OnOpenWaterClick() {//开启水箱
//        sendAnalystics("WaterTankPop");
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (!checkStateAndConnect()) return;
        if (checkWaterOut_Dialog()) return;
        steam226.setSteamWaterTankPOP(new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    void setOn_Model() {
        checkWaterOut_Dialog();
        if (steam226_btn.getVisibility()==View.VISIBLE){
            steam226_btn.setBackgroundResource(R.mipmap.img_common_recently_use_yellow);
            steam226_btn.setTextColor(cx.getResources().getColor(R.color.yellow_use));
        }
        steam226_main_imgContent.setImageResource(R.mipmap.img_steamoven_open1);
        steam226_main_imgContent_text.setTextColor(r.getColor(R.color.white));
        steam226_main_translate.setBackgroundResource(R.mipmap.img_steamoven_circle_open_small);
        steam226_main_translate_text.setTextColor(r.getColor(R.color.white));
        steam226_main_openwatertank.setBackgroundResource(R.mipmap.img_steamoven_circle_open_small);
        steam226_main_openwatertank_text.setTextColor(r.getColor(R.color.white));
        mSteam226MainOpenWaterText.setTextColor(r.getColor(R.color.white));
       // steam226_main_img_switch.setImageResource(R.mipmap.btn_power_open);

    }

    void setOff_Model() {
        if (steam226_btn.getVisibility()==View.VISIBLE){
            steam226_btn.setBackgroundResource(R.mipmap.img_common_recently_use_gray);
            steam226_btn.setTextColor(cx.getResources().getColor(R.color.device_oven_gray));
        }
        steam226_main_imgContent.setImageResource(R.mipmap.img_steamoven_unopen1);
        steam226_main_imgContent_text.setTextColor(r.getColor(R.color.c19));
        steam226_main_translate.setBackgroundResource(R.mipmap.img_steamoven_circle_close);
        steam226_main_translate_text.setTextColor(r.getColor(R.color.c19));
        steam226_main_openwatertank.setBackgroundResource(R.mipmap.img_steamoven_circle_close);
        steam226_main_openwatertank_text.setTextColor(r.getColor(R.color.c19));
        mSteam226MainOpenWaterText.setTextColor(r.getColor(R.color.c19));
       // steam226_main_img_switch.setImageResource(R.mipmap.btn_power);
    }

    boolean checkStateAndConnect() {
        if (steam226.status == OvenStatus.Off || steam226.status == OvenStatus.Wait) {
            StartNotice();
            return false;
        }
        return true;
    }

    void StartNotice() {
        if (dialogByType != null && dialogByType.isShow())
            return;
        dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
        dialogByType.setContentText(R.string.open_device);
        dialogByType.show();
    }

    boolean checkWaterOut_Dialog() {
        if (steam226.waterboxstate == 0) {
            dialogByType.setContentText(R.string.device_alarm_water_out);
            if (!dialogByType.isShow()) {
                dialogByType.show();
                return true;
            }
            return false;
        }

        return false;
    }

    public void closeAlarmDialog() {
        if (iRokidialogAlarm != null && iRokidialogAlarm.isShow())
            iRokidialogAlarm.dismiss();
    }

    void closeAllDialog() {
        closeAlarmDialog();
        if (sterilization_dialog != null && sterilization_dialog.isShowing())
            sterilization_dialog.dismiss();
    }

    private boolean checkConnection() {
        if (steam226.isConnected()) {
            return true;
        } else {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return false;
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
            default:
                cause = true;
                break;
        }
        return cause;
    }

    @Override
    public void onStop() {
        super.onStop();
        closeAllDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
