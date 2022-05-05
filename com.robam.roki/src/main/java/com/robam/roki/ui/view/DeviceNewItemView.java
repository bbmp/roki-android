package com.robam.roki.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.events.AbsEvent;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.SteriStatus;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierStatus;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.cook.CookerStatus;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceNewItemView extends FrameLayout {

    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtModel)
    TextView txtModel;
    IDevice device;
    @InjectView(R.id.iv_offline_prompt)
    ImageView mIvOfflinePrompt;
    @InjectView(R.id.iv_alram)
    ImageView ivAlarm;

    Context cx;

    public void setImageDevice(String imgurl) {
        Glide.with(cx).load(imgurl).into(imgDevice);
    }

    private CountDownTimer countDownTimer = new CountDownTimer(1000 * 2, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //秒转化成 00:00形式一

        }

        @Override
        public void onFinish() {
            Log.e("结果",System.currentTimeMillis()+"---"+currentTime);
////        mIvOfflinePrompt.setImageResource(img_deviceId);
//            if (System.currentTimeMillis()-currentTime>1000){
////           currentTime=System.currentTimeMillis();
//
//            }else {
//
//            }

            mIvOfflinePrompt.setImageResource(img_deviceId);
        }
    };

    private int img_deviceId;
    public boolean getSelect(){
        return layout.isSelected();
    }

    public void setImageDeviceOfflinePromptNull() {
        countDownTimer.cancel();
        mIvOfflinePrompt.setImageDrawable(null);
    }


    long currentTime=System.currentTimeMillis();
    public void setImageDeviceOfflinePrompt(int img_deviceId) {
        this.img_deviceId=img_deviceId;
//        mIvOfflinePrompt.setImageResource(img_deviceId);
        Glide.with(getContext()).asGif().load(R.drawable.m_smart_loading2).into(mIvOfflinePrompt);
        countDownTimer.start();
    }

    public void setImgDeviceAlarm(int img) {
        ivAlarm.setVisibility(View.VISIBLE);
        ivAlarm.setImageResource(img);
    }

    public void setTxtDeviceName(String deviceName) {

        LogUtils.i("20171028", "deviceName:" + deviceName);
        txtTitle.setText(deviceName);

    }

    public void setTxtDeviceDesc(String deviceDesc) {
        txtDesc.setText(deviceDesc);
    }

    public void setTxtModel(String deviceModel) {
        if (!TextUtils.isEmpty(deviceModel)) {
            txtModel.setText(deviceModel);
        }
    }

    public void setDevice(IDevice device) {
        this.device = device;
    }

    public DeviceNewItemView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceNewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceNewItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        layout.setSelected(selected);
    }

    void init(Context cx, AttributeSet attrs) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_item,
                this, true);
        if (!view.isInEditMode()) {
            ScreenAdapterTools.getInstance().loadView(view);
            ButterKnife.inject(this, view);
        }
    }


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {


        if (device instanceof AbsFan) {
            List<IDevice> childList = ((AbsFan) device).getChildList();
            for (int i = 0; i < childList.size(); i++) {
                LogUtils.i("20180308", "connected:" + childList.get(i).getDeviceType());

            }
        }

    }


    //点击事件
    @OnClick(R.id.layout)
    public void onClickLayout() {
        layout();
    }

    public void layout() {
        if (device == null) return;
        String alarm = PreferenceUtils.getString("alarm", null);
        LogUtils.i("20190614", "alarm:" + alarm);
//        if (TextUtils.isEmpty(alarm)) {
//            ToastUtils.showLong(R.string.not_alarm_file);
//            return;
//        }
        LogUtils.i("20180105", "device:" + device.getID() + "device:" + device.getDt());
        String pageKey = null;
        if (device instanceof AbsFan) {
            LogUtils.i("20170212", "device:" + ((AbsFan) device).getChild());
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R8230))
                pageKey = PageKey.DeviceFan8230;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R9700))
                pageKey = PageKey.DeviceFan9700;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R8700))
                pageKey = PageKey.DeviceFan8700;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R66A2))
                pageKey = PageKey.DeviceFan66A2;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._66A2H))
                pageKey = PageKey.DeviceFan66A2H;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R8229))
                pageKey = PageKey.DeviceFan8229;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R5910))
                pageKey = PageKey.DeviceFan5910;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._5910S))
                pageKey = PageKey.DeviceFan5910S;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R5610))
                pageKey = PageKey.DeviceFan5610;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._8230S))
                pageKey = PageKey.DeviceFan8230s;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._8231S))
                pageKey = PageKey.DeviceFan8231s;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._8230C))
                pageKey = PageKey.DeviceFan8230c;
            else {
                pageKey = PageKey.DeviceFanOther;
            }
            UIService.getInstance().postPage(pageKey, bd);
        } else if (device instanceof Pot) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            pageKey = PageKey.AbsDevicePot;
            UIService.getInstance().postPage(pageKey, bd);
        } else if (device instanceof Stove) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            switch (device.getDt()) {
                case IRokiFamily.R9B37:
                    pageKey = PageKey.Device9B37;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
                case IRokiFamily.R9B39:
                    pageKey = PageKey.Device9B39;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
                case IRokiFamily._9B30C:
                    pageKey = PageKey.Device9B30C;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
                case IRokiFamily.R9W70:
                    pageKey = PageKey.DeviceStove;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
                case IRokiFamily.R9W851:
                    pageKey = PageKey.DeviceStove;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
                default:
                    pageKey = PageKey.DeviceStove;
                    bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RRQZ);
                    UIService.getInstance().postPage(pageKey, bd);
                    break;
            }
        } else if (device instanceof GasSensor) {//传感器
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceName, device.getDispalyType());
            pageKey = PageKey.DeviceGasSensor;
            UIService.getInstance().postPage(pageKey, bd);
            //蒸汽炉
        } else if (device instanceof AbsSteamoven) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());

            switch (((AbsSteamoven) device).status) {
                case SteamStatus.Working:
                case SteamStatus.Order:
                case SteamStatus.PreHeat:
                case SteamStatus.Pause:
                    bd.putInt(PageArgumentKey.from, 2);
                    UIService.getInstance().postPage(PageKey.SteamSub, bd);
                    break;
                default:
                    bd.putInt(PageArgumentKey.from, 1);
                    UIService.getInstance().postPage(PageKey.SteamSub, bd);
                    break;
            }


        } else if (device instanceof AbsOven) {
            LogUtils.i("20180105", "AbsOven:" + device.getDt());
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            switch (((AbsOven) device).status) {
                case OvenStatus.Working:
                case OvenStatus.Order:
                case OvenStatus.PreHeat:
                case OvenStatus.Pause:
                    bd.putInt(PageArgumentKey.from, 2);
                    UIService.getInstance().postPage(PageKey.AbsOven, bd);
                    break;
                default:
                    bd.putInt(PageArgumentKey.from, 1);
                    UIService.getInstance().postPage(PageKey.AbsOven, bd);
                    break;
            }

        } else if (device instanceof AbsSterilizer) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            boolean layoutSelected = layout.isSelected();
            bd.putBoolean("layout", layoutSelected);
            switch (((AbsSterilizer) device).status) {
                default:
                    bd.putInt(PageArgumentKey.from, 0);
                    UIService.getInstance().postPage(PageKey.DeviceSterilizer, bd);
                    break;
            }
        } else if (device instanceof AbsMicroWave) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());

            switch (((AbsMicroWave) device).state) {
                case MicroWaveStatus.Pause:
                case MicroWaveStatus.Run:
                    bd.putInt(PageArgumentKey.from, 2);
                    UIService.getInstance().postPage(PageKey.AbsDeviceMicroWave, bd);
                    break;
                default:
                    bd.putInt(PageArgumentKey.from, 1);
                    UIService.getInstance().postPage(PageKey.AbsDeviceMicroWave, bd);
                    break;

            }

        } else if (device instanceof AbsWaterPurifier) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.Guid, device.getID());
            if (IRokiFamily.RJ312.equals(device.getDt())) {
                UIService.getInstance().postPage(PageKey.AbsDeviceWaterPurifier, bd);
            } else if (IRokiFamily.RJ320.equals(device.getDt())) {
                UIService.getInstance().postPage(PageKey.AbsDeviceWaterPurifier, bd);
            } else {
                UIService.getInstance().postPage(PageKey.AbsDeviceWaterPurifier, bd);
            }
        } else if (device instanceof AbsSteameOvenOne) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            LogUtils.i("202010241056", "id:::" + device.getID());
            LogUtils.i("202012071728", "device.isConnected():::" + device.isConnected());
            if (!device.isConnected()) {
                bd.putShort(PageArgumentKey.from, (short) 1);
                UIService.getInstance().postPage(PageKey.AbsDeviceSteamOvenOne, bd);

            }else{
                switch (((AbsSteameOvenOne) device).powerOnStatus) {
                    case SteamOvenOnePowerOnStatus.WorkingStatus:
                    case SteamOvenOnePowerOnStatus.Order:
                    case SteamOvenOnePowerOnStatus.Pause:
                        bd.putShort(PageArgumentKey.from, (short) 2);
                        UIService.getInstance().postPage(PageKey.AbsDeviceSteamOvenOne, bd);
                        break;
                    default:
                        bd.putShort(PageArgumentKey.from, (short) 1);
                        UIService.getInstance().postPage(PageKey.AbsDeviceSteamOvenOne, bd);
                        break;
                }
            }



        } else if (device instanceof AbsRika) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RIKA);
            if (IRokiFamily.RIKAZ.equals(device.getDp()) || IRokiFamily.RIKAX.equals(device.getDp()) || IRokiFamily.RIKAY.equals(device.getDp())) {
                UIService.getInstance().postPage(PageKey.AbsRikaDevice, bd);
            }else if (device.getDp().contains(IRokiFamily.RJCZ)){
                UIService.getInstance().postPage(PageKey.AbsRikaDevice, bd);
            }
        } else if (device instanceof AbsIntegratedStove) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RJCZ);
           if (device.getDc().contains(IRokiFamily.RJCZ)){
                UIService.getInstance().postPage(PageKey.AbsIntegratedStovePage, bd);
            }
        } else if (device instanceof AbsCooker) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceCategory, IDeviceType.KZNZ);
            if (!device.isConnected()) {
                bd.putInt(PageArgumentKey.From, 10);
                UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                return;
            }
            if (IRokiFamily.KDC01.equals(device.getDt())) {
                switch (((AbsCooker) device).powerStatus) {
                    case 0:
                        bd.putInt(PageArgumentKey.From, 0);
                        UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                        break;
                    case 1:
                        bd.putInt(PageArgumentKey.From, 1);
                        UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                        break;
                    case 3:
                        bd.putInt(PageArgumentKey.From, 3);
                        UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                        break;
                    case 4:
                        bd.putInt(PageArgumentKey.From, 4);
                        UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                        break;
                    default:
                        bd.putInt(PageArgumentKey.From, 10);
                        UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
                        break;
                }
            }
        } else if (device instanceof AbsDishWasher) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RXWJ);

            switch (((AbsDishWasher) device).powerStatus) {
                case 0://关机
                case 1://待机
                    bd.putInt(PageArgumentKey.From, 1);
                    UIService.getInstance().postPage(PageKey.AbsDishWasher, bd);
                    break;
                case 2://工作中
                case 3://暂停
                case 4://结束
                    bd.putInt(PageArgumentKey.From, 0);
                    UIService.getInstance().postPage(PageKey.AbsDishWasher, bd);
                    break;
                default:
                    break;

            }
        } else if (device instanceof AbsHidKit) {

            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, device.getID());
            bd.putString(PageArgumentKey.deviceCategory, IDeviceType.RCBH);
            UIService.getInstance().postPage(PageKey.AbsHidKitDevicePage, bd);

        }
    }

    @Subscribe
    public void onEvent(AbsEvent event) {
        if (event.pojo == null) {
            return;
        }
        if (event.pojo instanceof AbsFan) {
            if (!Objects.equal(((AbsFan) event.pojo).getID(), device.getID()))
                return;
            boolean isOn = ((AbsFan) event.pojo).isConnected() && ((AbsFan) event.pojo).status != FanStatus.Off;
            this.setSelected(isOn);
        } else if (event.pojo instanceof Stove) {
            if (!Objects.equal(((Stove) event.pojo).getID(), device.getID()))
                return;
            boolean isOnLeft = ((Stove) event.pojo).leftHead.status != StoveStatus.Off;
            boolean isOnRight = ((Stove) event.pojo).rightHead.status != StoveStatus.Off;
            this.setSelected(((Stove) event.pojo).isConnected() && (isOnLeft || isOnRight));
        } else if (event.pojo instanceof Pot) {
            if (!Objects.equal(((Pot) event.pojo).getID(), device.getID()))
                return;
            float tempUp = ((Pot) event.pojo).tempUp;
            if (tempUp < 50) {
                this.setSelected(false);
            } else {
                this.setSelected(true);
            }

        } else if (event.pojo instanceof AbsSteamoven) {
            if (!Objects.equal(((AbsSteamoven) event.pojo).getID(), device.getID()))
                return;
            if (!((AbsSteamoven) event.pojo).isConnected()) {
                setSelected(false);
                return;
            }
            switch ((((AbsSteamoven) event.pojo).status)) {
                case SteamStatus.Working:
                    setSelected(true);
                    break;
                case SteamStatus.Off:
                    setSelected(false);
                    break;
                case SteamStatus.On:
                    setSelected(true);
                    break;
                case SteamStatus.Pause:
                    setSelected(true);
                    break;
                case SteamStatus.Wait:
                    setSelected(false);
                    break;
                case SteamStatus.PreHeat:
                    setSelected(true);
                    break;
                case SteamStatus.Order:
                    setSelected(true);
                    break;
                default:
                    break;
            }
        } else if (event.pojo instanceof GasSensor) {
            if (!Objects.equal(((GasSensor) event.pojo).getID(), device.getID()))
                return;
            if (!((GasSensor) event.pojo).isConnected()) {
                setSelected(false);
                return;
            }
            setSelected(true);
        } else if (event.pojo instanceof AbsOven) {
            if (!Objects.equal(((AbsOven) event.pojo).getID(), device.getID()))
                return;
            if (!((AbsOven) event.pojo).isConnected()) {
                setSelected(false);
                return;
            }

            if ("RQ035".equals(((AbsOven) event.pojo).getDt())) {
                if ((OvenStatus.Off == ((AbsOven) event.pojo).status
                        && OvenStatus.Off == ((AbsOven) event.pojo).status2Values)
                        || OvenStatus.Wait == ((AbsOven) event.pojo).status) {
                    setSelected(false);
                } else {
                    setSelected(true);
                }

            } else {
                if (OvenStatus.Off == ((AbsOven) event.pojo).status || OvenStatus.Wait == ((AbsOven) event.pojo).status) {
                    setSelected(false);
                } else {
                    setSelected(true);
                }
            }

        } else if (event.pojo instanceof AbsCooker) {
            if (!Objects.equal(((AbsCooker) event.pojo).getID(), device.getID()))
                return;
            if (!((AbsCooker) event.pojo).isConnected()) {
                setSelected(false);
                return;
            }
            if (CookerStatus.off == ((AbsCooker) event.pojo).powerStatus || CookerStatus.wait == ((AbsCooker) event.pojo).powerStatus) {
                setSelected(false);
            } else {
                setSelected(true);
            }


        } else if (event.pojo instanceof AbsSterilizer) {
            if (!Objects.equal(((AbsSterilizer) event.pojo).getID(), device.getID()))
                return;
            boolean isOn = ((AbsSterilizer) event.pojo).isConnected() && ((AbsSterilizer) event.pojo).status != SteriStatus.Off;
            setSelected(isOn);
        } else if (event.pojo instanceof AbsMicroWave) {
            if (!Objects.equal(((AbsMicroWave) event.pojo).getID(), device.getID()))
                return;
            if (!(((AbsMicroWave) event.pojo).isConnected())) {
                setSelected(false);
            } else {
                setSelected(true);
            }
        } else if (event.pojo instanceof AbsWaterPurifier) {
            if (!Objects.equal(((AbsWaterPurifier) event.pojo).getID(), device.getID())) {
                return;
            }

            if (!(((AbsWaterPurifier) event.pojo).isConnected())) {
                setSelected(false);
                return;
            }
            switch ((((AbsWaterPurifier) event.pojo).status)) {
                case WaterPurifierStatus.Purify:
                    setSelected(true);
                    break;
                case WaterPurifierStatus.Off:
                    setSelected(false);
                    break;
                case WaterPurifierStatus.On:
                    setSelected(false);
                    break;
                case WaterPurifierStatus.Wash:
                    setSelected(true);
                    break;
                case WaterPurifierStatus.Wait:
                    setSelected(false);
                    break;
                case WaterPurifierStatus.AlarmStatus:
                    setSelected(false);
                    break;
                default:
                    break;
            }

        } else if (event.pojo instanceof AbsSteameOvenOne) {

            if (!Objects.equal(((AbsSteameOvenOne) event.pojo).getID(), device.getID())) {
                return;
            }

            if (!(((AbsSteameOvenOne) event.pojo).isConnected())) {
                setSelected(false);
                return;
            }


            if (SteamOvenOnePowerStatus.Off == ((AbsSteameOvenOne) event.pojo).powerStatus) {
                setSelected(false);
            } else {
                setSelected(true);
            }

        } else if (event.pojo instanceof AbsRika) {
            if (!Objects.equal(((AbsRika) event.pojo).getID(), device.getID())) {
                return;
            }

            if (!(((AbsRika) event.pojo).isConnected())) {
                setSelected(false);
                return;
            }
            if (IRokiFamily.RIKAX.equals(((AbsRika) event.pojo).getDp())) {
                short rikaFanWorkStatus = ((AbsRika) event.pojo).rikaFanWorkStatus;
                short rikaFanPower = ((AbsRika) event.pojo).rikaFanPower;
                short sterilWorkStatus = ((AbsRika) event.pojo).sterilWorkStatus;
                short stoveHeadLeftWorkStatus = ((AbsRika) event.pojo).stoveHeadLeftWorkStatus;
                short stoveHeadRightWorkStatus = ((AbsRika) event.pojo).stoveHeadRightWorkStatus;
                if (RikaStatus.FAN_ON == rikaFanWorkStatus && rikaFanPower != 0
                        || RikaStatus.FAN_AWAIT == rikaFanWorkStatus || RikaStatus.FAN_DELAY_OFF == rikaFanWorkStatus
                        || RikaStatus.STOVE_WORK == stoveHeadLeftWorkStatus || RikaStatus.STOVE_WORK == stoveHeadRightWorkStatus
                        || RikaStatus.STERIL_ON == sterilWorkStatus || RikaStatus.STERIL_DISIDFECT == sterilWorkStatus
                        || RikaStatus.STERIL_CLEAN == sterilWorkStatus || RikaStatus.STERIL_DRYING == sterilWorkStatus
                        || RikaStatus.STERIL_PRE == sterilWorkStatus || RikaStatus.STERIL_DEGERMING == sterilWorkStatus
                        || RikaStatus.STERIL_INTELLIGENT_DETECTION == sterilWorkStatus || RikaStatus.STERIL_INDUCTION_STERILIZATION == sterilWorkStatus
                        || RikaStatus.STERIL_WARM_DISH == sterilWorkStatus || RikaStatus.STERIL_APPOINATION == sterilWorkStatus
                        || RikaStatus.STERIL_APPOINATION_DRYING == sterilWorkStatus || RikaStatus.STERIL_APPOINATION_CLEAN == sterilWorkStatus
                        || RikaStatus.STERIL_COER_DISIDFECT == sterilWorkStatus) {
                    setSelected(true);
                } else {
                    setSelected(false);
                }

            } else if (IRokiFamily.RIKAZ.equals(((AbsRika) event.pojo).getDp())) {

                short steamWorkStatus = ((AbsRika) event.pojo).steamWorkStatus;
                short rikaFanWorkStatus = ((AbsRika) event.pojo).rikaFanWorkStatus;
                short rikaFanPower = ((AbsRika) event.pojo).rikaFanPower;
                short stoveHeadLeftWorkStatus = ((AbsRika) event.pojo).stoveHeadLeftWorkStatus;
                short stoveHeadRightWorkStatus = ((AbsRika) event.pojo).stoveHeadRightWorkStatus;
                if (RikaStatus.FAN_ON == rikaFanWorkStatus && rikaFanPower != 0
                        || RikaStatus.STEAM_RUN == steamWorkStatus || RikaStatus.STEAM_STOP == steamWorkStatus
                        || RikaStatus.STEAM_PREHEAT == steamWorkStatus || RikaStatus.FAN_AWAIT == rikaFanWorkStatus
                        || RikaStatus.FAN_DELAY_OFF == rikaFanWorkStatus
                        || RikaStatus.STOVE_WORK == stoveHeadLeftWorkStatus || RikaStatus.STOVE_WORK == stoveHeadRightWorkStatus) {
                    setSelected(true);
                } else {
                    setSelected(false);
                }
            } else if (IRokiFamily.RIKAY.equals(((AbsRika) event.pojo).getDp())) {

                short steamOvenWorkStatus = ((AbsRika) event.pojo).steamOvenWorkStatus;
                short rikaFanWorkStatus = ((AbsRika) event.pojo).rikaFanWorkStatus;
                short rikaFanPower = ((AbsRika) event.pojo).rikaFanPower;
                if (RikaStatus.FAN_ON == rikaFanWorkStatus && rikaFanPower != 0
                        || RikaStatus.STEAMOVEN_RUN == steamOvenWorkStatus || RikaStatus.STEAMOVEN_STOP == steamOvenWorkStatus
                        || RikaStatus.STEAMOVEN_PREHEAT == steamOvenWorkStatus || RikaStatus.FAN_AWAIT == rikaFanWorkStatus
                        || RikaStatus.FAN_DELAY_OFF == rikaFanWorkStatus ){
                    setSelected(true);
                } else {
                    setSelected(false);
                }
            }

        } else if (event.pojo instanceof AbsIntegratedStove){
            if (!Objects.equal(((AbsIntegratedStove) event.pojo).getID(), device.getID())) {
                return;
            }
            if ( ((AbsIntegratedStove) event.pojo).isConnected() && SteamOvenHelper.isIntegratedStoveWork((AbsIntegratedStove)event.pojo)){
                setSelected(true);
                return;
            }else {
                setSelected(false);
                return;
            }
        }
        else if (event.pojo instanceof AbsDishWasher) {
            if (!Objects.equal(((AbsDishWasher) event.pojo).getID(), device.getID())) {
                return;
            }

            if (!(((AbsDishWasher) event.pojo).isConnected())) {
                setSelected(false);
                return;
            }
            if (DishWasherStatus.off == ((AbsDishWasher) event.pojo).powerStatus ||
                    //DishWasherStatus.wait == ((AbsDishWasher) event.pojo).powerStatus ||
                    DishWasherStatus.end == ((AbsDishWasher) event.pojo).powerStatus) {
                setSelected(false);
            } else {
                setSelected(true);
            }

        } else {
            return;
        }
    }


}
