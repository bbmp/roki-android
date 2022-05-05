package com.robam.roki.ui.page;

import android.app.Dialog;
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
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.OvenUserAction;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.R.id.oven028_main_img_switch;

/**
 * Created by yinwei on 2017/8/17.
 */

public class DeviceOven028MainPage extends BasePage {

    @InjectView(R.id.imgHead)
    ImageView imgHead;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.txtRecipe)
    TextView txtRecipe;
    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;
    @InjectView(R.id.oven028_btn)
    Button oven028Btn;
    @InjectView(R.id.oven028_profession_txt)
    TextView oven028ProfessionTxt;
    @InjectView(R.id.ovn028_profession_img)
    ImageView ovn028ProfessionImg;
    @InjectView(R.id.oven028_profession)
    RelativeLayout oven028Profession;
    @InjectView(R.id.oven028_expert_txt)
    TextView oven028ExpertTxt;
    @InjectView(R.id.oven028_expert)
    RelativeLayout oven028Expert;
    @InjectView(R.id.oven028_assist_txt)
    TextView oven028AssistTxt;
    @InjectView(R.id.oven028_assist)
    RelativeLayout oven028Assist;
    @InjectView(oven028_main_img_switch)
    ImageView oven028MainImgSwitch;
    @InjectView(R.id.oven028_main_ll_switch)
    LinearLayout oven028MainLlSwitch;
    @InjectView(R.id.oven028_main_fram_switch)
    FrameLayout oven028MainFramSwitch;


    //
    String guid;
    View contentView;
    AbsOven oven028;
    LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven028 = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven028_control, container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    void init() {

    }

    @OnClick({R.id.oven028_return, R.id.oven028_btn, R.id.oven028_profession, R.id.oven028_expert,
            R.id.oven028_assist, R.id.oven028_main_fram_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oven028_return://返回
                UIService.getInstance().popBack();
                break;
            case R.id.oven028_btn://最近使用
                //   ToastUtils.show("最近使用", Toast.LENGTH_SHORT);
                onclick_recently_user();
                break;
            case R.id.oven028_profession://专业模式
                onclick_profession();
                break;
            case R.id.oven028_expert://专家模式
                onclick_expert();
                break;
            case R.id.oven028_assist://辅助
                onclick_assist();
                break;
            case R.id.oven028_main_fram_switch://开关
                onclick_switch();
                break;
          /* *//* case R.id.test://测试多段烹饪
                //onclick_test();*//*
                break;*/

        }
    }

  /*  private void onclick_test() {
        oven028.setOvenMoreMode((short)0,(short) 5,(short) 1,(short) 7,(short)2,
                (short)1,(short)20,(short)20,(short)1,(short)20,(short)20,
                new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20171030","成功");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20171030","失败："+t.getStackTrace());
            }
        });
    }*/

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven028 == null || !Objects.equal(oven028.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.GONE : View.VISIBLE);
        if (!event.isConnected) {
            // closeAllDialog();当断网的时候可关闭所有的对话框
        }
        boolean isConnect = event.isConnected;
        LogUtils.i("20171017", "isCpnnect::" + isConnect);
        ovenstatus_change(isConnect);
    }

    Dialog dialog = null;

    //最近使用
    private void onclick_recently_user() {
        if (!checkStateAndConnect()) {
            return;
        }
        dialog = Helper.newOven028RecentlyUseDialog(cx, new Callback2<OvenUserAction>() {

            @Override
            public void onCompleted(OvenUserAction ovenUserAction) {
                if (ovenUserAction.getMode() == 9) {
                    sendExp(ovenUserAction);
                } else {
                    sendProfession(ovenUserAction);
                }
            }
        }, oven028);
    }

    //专业模式指令
    private void sendProfession(OvenUserAction ovenUserAction) {
        oven028.setOvenRunMode(ovenUserAction.getMode(), ovenUserAction.getTimeCook(), ovenUserAction.getTemperature()
                , (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.show("指令下发成功", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                    }
                });
    }

    //exp模式指令下发
    private void sendExp(OvenUserAction ovenUserAction) {
        oven028.setOvenRunMode((short) 9, ovenUserAction.getTimeCook(), ovenUserAction.getTemperUp(),
                (short) 0, (short) 0, (short) 0, (short) 1, ovenUserAction.getTemperDown(), (short) 255, (short) 255, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.show("指令下发成功", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                    }
                });
    }

    //专业模式
    private void onclick_profession() {
        if (!checkStateAndConnect()) {
            return;
        }
        //  ToastUtils.show("专业模式",Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven028.getID());
        UIService.getInstance().postPage(PageKey.DeviceOven028ProfessionalSetting, bd);
    }

    //专家模式
    private void onclick_expert() {
        if (!checkStateAndConnect()) {
            return;
        }
        //  ToastUtils.show("专家模式",Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven028.getID());
        UIService.getInstance().postPage(PageKey.DeviceOven026ExpSetting, bd);
    }

    //辅助模式
    private void onclick_assist() {
        if (!checkStateAndConnect()) {
            return;
        }
        //    ToastUtils.show("辅助模式",Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven028.getID());
        UIService.getInstance().postPage(PageKey.DeviceOven028Assist, bd);
    }

    //开关
    private void onclick_switch() {
        if (!oven028.isConnected())
            return;
        if (oven028.status == OvenStatus.Off || oven028.status == OvenStatus.Wait) {
            setOvenStatus(OvenStatus.On);
        } else if (oven028.status == OvenStatus.On) {
            setOvenStatus(OvenStatus.Off);
        }
    }

    void setOvenStatus(final short state) {
        oven028.setOvenStatus(state, null);
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        LogUtils.i("20171019", "event::" + event.pojo.getID());
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven028Main)) {
            return;
        }
        if (oven028 == null || !Objects.equal(oven028.getID(), event.pojo.getID()))
            return;
        if (oven028.status == OvenStatus.Wait) {
            ovenstatus_change(false);
        } else if (oven028.status == OvenStatus.Off) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            ovenstatus_change(false);
        } else if (oven028.status == OvenStatus.Working || oven028.status == OvenStatus.Pause
                || oven028.status == OvenStatus.Order || oven028.status == OvenStatus.PreHeat) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, oven028.getID());
            bundle.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceOven028Working, bundle);
        } else {
            ovenstatus_change(true);
        }
        if (oven028.status != OvenStatus.AlarmStatus) {

        }
    }

    @Subscribe
    public void onEvent(OvenAlarmEvent event) {
        if (!PageKey.DeviceOven028Main.equals(UIService.getInstance().getTop().getCurrentPageKey()))
            return;

        switch (event.alarmId) {
            case AbsOven.Event_Oven_Heat_Fault:
                // showDialog("错误：加热故障", event.alarmId);
                break;
            case AbsOven.Event_Oven_Alarm_Senor_Fault:
                // showDialog("错误：传感器故障", event.alarmId);
                break;
            case AbsOven.Event_Oven_Communication_Fault:
                //  showDialog("错误：通信故障", event.alarmId);
                break;
            default:
                break;
        }
    }

    private boolean checkStateAndConnect() {
        if (!oven028.isConnected()) {
            ToastUtils.show("设备与网络断开连接", Toast.LENGTH_SHORT);
            return false;
        }

        if (oven028.status == OvenStatus.AlarmStatus) {
            ToastUtils.show("设备处于报警中，无法工作", Toast.LENGTH_SHORT);
            return false;
        }

        if (oven028.status == OvenStatus.Off || oven028.status == OvenStatus.Wait) {
            StartNotice();
            return false;
        } else {
            return true;
        }
    }

    void StartNotice() {
      /*  if (blackPromptDialog != null && blackPromptDialog.isShowing())
            return;
        blackPromptDialog = BlackPromptDialog.show(cx, R.layout.dialog_oven026_startnotice);
        blackPromptDialog.show();*/
        ToastUtils.show("请先开启设备", Toast.LENGTH_SHORT);
    }

    private void ovenstatus_change(boolean isOn) {
        oven028Btn.setBackgroundResource(isOn ? R.mipmap.img_common_recently_use_yellow : R.mipmap.img_common_recently_use_gray);
        oven028Btn.setTextColor(cx.getResources().getColor(isOn ? R.color.yellow_use : R.color.device_oven_gray));
        oven028Profession.setBackgroundResource(isOn ? R.mipmap.img_device_oven028_main_profession_circle_white : R.mipmap.img_device_oven028_main_professtion_circle_gray);
        oven028ProfessionTxt.setTextColor(cx.getResources().getColor(isOn ? R.color.white : R.color.device_oven_gray));
        ovn028ProfessionImg.setImageResource(isOn ? R.mipmap.img_deivce_oven028_main_professtion_yellow : R.mipmap.img_device_oven028_main_professtion_gray);
        oven028Expert.setBackgroundResource(isOn ? R.mipmap.img_device_oven028_circle_white : R.mipmap.img_device_oven028_circle_gray);
        oven028ExpertTxt.setTextColor(cx.getResources().getColor(isOn ? R.color.white : R.color.device_oven_gray));
        oven028Assist.setBackgroundResource(isOn ? R.mipmap.img_device_oven028_circle_white : R.mipmap.img_device_oven028_circle_gray);
        oven028AssistTxt.setTextColor(cx.getResources().getColor(isOn ? R.color.white : R.color.device_oven_gray));
      /*  oven028MainFramSwitch.setBackgroundResource(isOn ? R.mipmap.ic_device_oven_leanline_yellow : R.mipmap.ic_device_oven_leanline_white);
        oven028MainImgSwitch.setImageResource(isOn ? R.mipmap.ic_device_oven_started : R.mipmap.ic_device_oven_start_white );
        oven028MainTvSwitch.setTextColor(isOn ? r.getColor(R.color.c02):r.getColor(R.color.white));
        oven028MainTvSwitch.setText(isOn ? R.string.device_oven_switch_on : R.string.device_oven_switch_off);*/
        oven028MainImgSwitch.setImageResource(R.mipmap.btn_power_open);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
