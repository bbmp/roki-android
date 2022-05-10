package com.robam.roki.ui.page;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.DeviceWorkMsg;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.SteamOvenSensorBrokeDialog;
import com.robam.roki.ui.dialog.SteamOvenStartWorkDialog;
import com.robam.roki.ui.dialog.SteamOvenWarningDialog;
import com.robam.roki.ui.view.HomeRecipeView32;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/9.
 */
public class DeviceSteamOvenPage extends HeadPage {

    Steam209 steam;

    @InjectView(R.id.imgContent)
    ImageView imgContent;//蒸汽炉中间图标-关闭img
    @InjectView(R.id.txtContent)
    TextView txtContext;//蒸汽炉中间字 蒸
    @InjectView(R.id.relClean)
    RelativeLayout relClean;//自洁 RelativeLayout
    @InjectView(R.id.txtClean)
    TextView txtClean;//自洁 文字
    @InjectView(R.id.relSterilize)
    RelativeLayout relSterilize;//杀菌 RelativeLayout
    @InjectView(R.id.txtSterilize)
    TextView txtSterilize;//杀菌文字
    @InjectView(R.id.imgSwitch)
    ImageView imgSwitch;//关闭 图标
    @InjectView(R.id.txtSwitch)
    TextView txtSwitch;//关闭文字
    /*@InjectView(R.id.linSwitch)
    RelativeLayout linSwitch;//关闭 RelativeLayout*/
    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;//断网提示 控件
    @InjectView(R.id.imgLeanline)
    ImageView imgLeanLine;//关闭 倾斜线
    @InjectView(R.id.imgContentCircle)
    ImageView imgContentCircle;//开启状态 圆圈
    View contentView;
    Resources resources;
    static boolean isConnect = true;
    static final int PollStatus = 100;
    private SteamOvenWarningDialog dlg;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PollStatus:
                    Log.i("DeviceSteamOvenPage:", steam.status + "");
                    if (steam.status == SteamStatus.Working || steam.status == SteamStatus.Pause) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, steam.getID());
                        UIService.getInstance().postPage(PageKey.DeviceSteamOvenWorking, bundle);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private IRokiDialog switchDialog;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("20170307","guid:"+guid);
        steam = Plat.deviceService.lookupChild(guid);
        LogUtils.i("20170307","steam:"+steam.getGuid());
        contentView = inflater.inflate(R.layout.page_device_steamoven,
                container, false);
        ButterKnife.inject(this, contentView);
        disconnectHintView.setVisibility(View.INVISIBLE);
        resources = getResources();
//        onRefresh();
        setPageTitle("");
        switchDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    private void initView() {

    }



    @OnClick(R.id.linSwitch)
    public void onClickSwitch() {
        short status = (steam.status == SteamStatus.Off || steam.status == SteamStatus.Wait) ? SteamStatus.On : SteamStatus.Off;
        setStatus(status);
    }

    private void setStatus(short status) {
        if (!checkConnection()) {
            return;
        }
//
        steam.setSteamStatus(status, new VoidCallback() {
            @Override
            public void onSuccess() {
                boolean isOn = steam.isConnected() && steam.status != SteamStatus.Off;
                setSwitch(steam.status);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    @OnClick(R.id.relClean)
    public void onClickClean() {//自洁
        if (!steam.isConnected()) {
            ToastUtils.showShort(R.string.steam_invalid_error);
            return;
        }
        if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {
            gatingToast();
            return;
        }
        if (steam.status == SteamStatus.On) {
            DeviceWorkMsg msg = new DeviceWorkMsg();
            msg.setType("自洁");
            msg.setTime(String.valueOf(10));
            msg.setTemperature(String.valueOf(100));
            SteamOvenStartWorkDialog.show(getContext(), msg, steam);
        } else {
            openDeviceDialog();
        }
    }

    private void gatingToast() {
        switchDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        switchDialog.setContentText(R.string.device_alarm_gating_content);
        switchDialog.show();
        return;
    }

    //提示打开设备
    private void openDeviceDialog() {
        switchDialog.setContentText(R.string.open_device);
        switchDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        switchDialog.show();
    }

    @OnClick(R.id.relSterilize)
    public void onClickSterilize() {//杀菌
        if (!steam.isConnected()) {
            ToastUtils.showShort(R.string.steam_invalid_error);
            return;
        }
        if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {
            gatingToast();
            return;
        }
        if (steam.status == SteamStatus.On) {
            DeviceWorkMsg msg = new DeviceWorkMsg();
            msg.setType("杀菌");
            msg.setTime(String.valueOf(60));
            msg.setTemperature(String.valueOf(100));
            SteamOvenStartWorkDialog.show(getContext(), msg, steam);
        } else {
            openDeviceDialog();
        }
    }

    @OnClick(R.id.imgContent)
    public void onClickContext() {//蒸
        if (!steam.isConnected()) {
            ToastUtils.showShort(R.string.steam_invalid_error);
            return;
        }
        if (steam.status == SteamStatus.On) {
            if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steam.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteamOvenSetting, bundle);
            }
        } else {
            openDeviceDialog();
        }
    }

    @OnClick(R.id.txtRecipe)
    public void onClickRecipe() {
        getActivity().onBackPressed();
        HomeRecipeView32.recipeCategoryClick(DeviceType.RZQL);
        //ToastUtils.show(new String("即将开放，敬请期待"), Toast.LENGTH_SHORT);
    }

    private void setSwitch(int status) {
        if (status == SteamStatus.Wait || status == SteamStatus.Off || !isConnect) {
            imgContent.setImageResource(R.mipmap.img_steamoven_unopen1);
            txtContext.setTextColor(resources.getColor(R.color.c19));
            relClean.setBackgroundDrawable(resources.getDrawable(R.mipmap.img_steamoven_circle_close));
            txtClean.setTextColor(resources.getColor(R.color.c19));
            relSterilize.setBackgroundDrawable(resources.getDrawable(R.mipmap.img_steamoven_circle_close));
            txtSterilize.setTextColor(resources.getColor(R.color.c19));
            imgContentCircle.setVisibility(View.GONE);
        } else {
            imgContent.setImageResource(R.mipmap.img_steamoven_work);
            txtContext.setTextColor(resources.getColor(R.color.c14));
            relClean.setBackgroundDrawable(resources.getDrawable(R.mipmap.img_steamoven_circle_open_small));
            txtClean.setTextColor(resources.getColor(R.color.c14));
            relSterilize.setBackgroundDrawable(resources.getDrawable(R.mipmap.img_steamoven_circle_open_small));
            txtSterilize.setTextColor(resources.getColor(R.color.c14));
            imgContentCircle.setVisibility(View.VISIBLE);
        }

        if (status == SteamStatus.Wait || !isConnect) {
            imgSwitch.setImageResource(R.mipmap.ic_device_switch_normal);
            txtSwitch.setTextColor(resources.getColor(R.color.c19));
            txtSwitch.setText("已关闭");
            imgLeanLine.setImageResource(R.mipmap.img_steamoven_leanline_gray);
        } else if (status == SteamStatus.Off) {
            imgSwitch.setImageResource(R.mipmap.img_steamoven_switch_open);
            txtSwitch.setTextColor(resources.getColor(R.color.c14));
            txtSwitch.setText("已关闭");
            imgLeanLine.setImageResource(R.mipmap.img_steamoven_leanline_white);
        } else if (status == SteamStatus.On) {
            imgSwitch.setImageResource(R.mipmap.img_switch_yellow);
            txtSwitch.setTextColor(resources.getColor(R.color.home_bg));
            txtSwitch.setText("已开启");
            imgLeanLine.setImageResource(R.mipmap.img_steamoven_leanline_yellow);
        }
    }

    private boolean checkConnection() {
        if (!steam.isConnected()) {
            ToastUtils.showShort(R.string.steam_invalid_error);
            return false;
        } else {
            return true;
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.GONE : View.VISIBLE);
        isConnect = event.isConnected;
        setSwitch(steam.status);
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID()))
            return;
        Log.i("SteamOvenStatust", "status:" + steam.status + " alarm:" + steam.alarm);
        switch (steam.status) {
            case SteamStatus.Off:
                setSwitch(steam.status);
                break;
            case SteamStatus.On:
                setSwitch(steam.status);
                break;
            case SteamStatus.Wait:
                setSwitch(steam.status);
                break;
            case SteamStatus.Working:
                //handler.sendEmptyMessage(PollStatus);
                Log.i("DeviceSteamOvenPage", steam.getID() + "SteamStatus.Working");
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, steam.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteamWorking, bd);
                break;
            case SteamStatus.Pause:
                //handler.sendEmptyMessage(PollStatus);
                Log.i("DeviceSteamOvenPage", steam.getID() + "SteamStatus.Pause");
                Bundle b = new Bundle();
                b.putString(PageArgumentKey.Guid, steam.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteamWorking, b);
                break;
        }

    }

    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        Log.e("event.alarm", String.valueOf(event.alarmId));
        Log.e("steam.alarm", String.valueOf(steam.alarm));
        alarmEventDispatch(event.alarmId);
    }

    private void alarmEventDispatch(int status) {
        Log.i("steam0", "status:" + steam.status + " alarm:" + steam.alarm);
        if (!PageKey.DeviceSteamOven.equals(UIService.getInstance().getTop().getCurrentPageKey()))
            return;
        switch (status) {

            case 3://门控开关故障
                Log.e("event.alarm--3", "3");
                if (dlg == null || !dlg.isShowing()) {
                    gatingShowDialog(Steam209.Steam_Door_Open);
                }
                break;
        }
    }

    //门控提示
    private void gatingShowDialog(short type) {
        switch (type) {
            case Steam209.Steam_Door_Open:
                IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
                dialogByType.setContentText(R.string.device_alarm_gating_content);
                dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
                dialogByType.show();
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
