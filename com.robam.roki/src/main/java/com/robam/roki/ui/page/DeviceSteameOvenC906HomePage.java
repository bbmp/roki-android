package com.robam.roki.ui.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.legent.ui.UI;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.FanC906DetailDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.StringConstantsUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2017/6/13.
 */

public class DeviceSteameOvenC906HomePage extends BasePage {

    String guid;
    AbsSteameOvenOne steameOvenC906;
    @InjectView(R.id.iv_switch_iscon)
    ImageView mIvSwitchIscon;
    @InjectView(R.id.img_return)
    ImageView mImgReturn;
    @InjectView(R.id.tv_oven_steam_recipe)
    TextView mTvOvenSteamRecipe;
    @InjectView(R.id.disconnectHintView)
    LinearLayout mDisconnectHintView;
    @InjectView(R.id.rl_oven_open)
    RelativeLayout mRlOvenOpen;
    @InjectView(R.id.rl_oven_close)
    RelativeLayout mRlOvenClose;
    @InjectView(R.id.rl_stame_open)
    RelativeLayout mRlStameOpen;
    @InjectView(R.id.rl_steam_close)
    RelativeLayout mRlSteamClose;
    @InjectView(R.id.rl_fuzhud_open)
    RelativeLayout mRlFuzhudOpen;
    @InjectView(R.id.iv_fuzhu_logo_close)
    ImageView mIvFuzhuLogoClose;
    @InjectView(R.id.rl_fuzhu_close)
    RelativeLayout mRlFuzhuClose;
    @InjectView(R.id.ll_hint_switch)
    LinearLayout mLlHintSwitch;
    @InjectView(R.id.rl_water_tank_open)
    RelativeLayout mRlWaterTankOpen;
    @InjectView(R.id.rl_water_tank_close)
    RelativeLayout mRlWaterTankClose;
    @InjectView(R.id.rl_water_tank_off_line)
    RelativeLayout mRlWaterTankOffLine;
    @InjectView(R.id.iv_switch_open)
    ImageView mIvSwitchOpen;
    @InjectView(R.id.iv_switch_close)
    ImageView mIvSwitchClose;
    @InjectView(R.id.rl_back_bufen)
    RelativeLayout mRlBackBufen;
    @InjectView(R.id.tv_setting)
    TextView mTvSetting;
    @InjectView(R.id.iv_water_close)
    ImageView mIvWaterClose;
    private IRokiDialog mRokiDialog;



    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        LogUtils.i("steamovenone_st", "steameOvenC906.getID():" + steameOvenC906.getID() + " event.device.getID():" + event.device.getID());
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.device.getID()))
            return;

        if (steameOvenC906.WaterStatus == 1 && steameOvenC906.isConnected()) {
            mRlWaterTankOpen.setVisibility(View.VISIBLE);
        } else {
            mRlWaterTankOpen.setVisibility(View.GONE);
        }
        mDisconnectHintView.setVisibility(event.isConnected ? View.INVISIBLE : View.VISIBLE);
        if (event.isConnected) {
            if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off) {
//                mIvSwitchClose.setVisibility(View.GONE);
                mIvSwitchOpen.setVisibility(View.VISIBLE);

            } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.On) {
                closeSteamOvenOneIcon();
                mIvSwitchOpen.setVisibility(View.VISIBLE);
//                mIvSwitchClose.setVisibility(View.GONE);


            }
        } else {
            openSteamOvenOneIcon();
            if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off) {
                mIvSwitchClose.setVisibility(View.VISIBLE);
                mIvSwitchOpen.setVisibility(View.GONE);
                mRlWaterTankOffLine.setVisibility(View.VISIBLE);
            } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.On) {
                mIvSwitchOpen.setVisibility(View.GONE);
                mIvSwitchClose.setVisibility(View.VISIBLE);
                mRlWaterTankOffLine.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {

        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()))
            return;

        if (steameOvenC906.isConnected()) {
            mDisconnectHintView.setVisibility(View.GONE);
        } else {
            mDisconnectHintView.setVisibility(View.VISIBLE);
        }
        LogUtils.i("20171127", "WaterStatus:" + event.pojo.WaterStatus);
        if (event.pojo.WaterStatus == 1) {
            mIvWaterClose.setImageResource(R.mipmap.device_img_water_tank_open);
        }else if(event.pojo.WaterStatus == 0){
            mIvWaterClose.setImageResource(R.mipmap.device_img_water_tank_close);
        }else if (!steameOvenC906.isConnected()){
            mIvWaterClose.setImageResource(R.mipmap.device_img_water_tank_off_line);
        }

        if (event.pojo.powerStatus == SteamOvenOnePowerStatus.Off) {
            openSteamOvenOneIcon();
        } else if (event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.Pause
                || event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.Order ||
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            if (event.pojo.workModel == SteamOvenOneModel.XIANNENZHENG ||
                    event.pojo.workModel == SteamOvenOneModel.FAXIAO || event.pojo.workModel
                    == SteamOvenOneModel.YINGYANGZHENG || event.pojo.workModel == SteamOvenOneModel.GAOWENZHENG
                    || event.pojo.workModel == SteamOvenOneModel.JIEDONG ||
                    event.pojo.workModel == SteamOvenOneModel.ZHENGQISHAJUN ||
                    event.pojo.workModel == SteamOvenOneModel.KUAIRE || event.pojo.workModel == SteamOvenOneModel.FENGPEIKAO
                    || event.pojo.workModel == SteamOvenOneModel.PEIKAO || event.pojo.workModel == SteamOvenOneModel.FENGSHAIKAO
                    || event.pojo.workModel == SteamOvenOneModel.SHAOKAO || event.pojo.workModel == SteamOvenOneModel.QIANGSHAOKAO
                    || event.pojo.workModel == SteamOvenOneModel.JIANKAO || event.pojo.workModel == SteamOvenOneModel.DIJIARE
                    || event.pojo.workModel == SteamOvenOneModel.GUOSHUHONGGAN
                    || event.pojo.workModel == SteamOvenOneModel.KUAISUYURE || steameOvenC906.workModel == SteamOvenOneModel.BAOWEN) {
                Bundle bundle = new Bundle();
                bundle.putShort("from", (short) 1);
                bundle.putString(PageArgumentKey.Guid, event.pojo.getID());
                UIService.getInstance().postPage(PageKey.DeviceOvenC906Working, bundle);
            } else if (event.pojo.workModel == SteamOvenOneModel.CHUGO || event.pojo.workModel == SteamOvenOneModel.GANZAO
                    || event.pojo.workModel == SteamOvenOneModel.QINGJIE) {
                Bundle bundle = new Bundle();
                bundle.putShort("from", (short) 1);
                bundle.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                UIService.getInstance().postPage(PageKey.DeviceOtherC906Working, bundle);
            } else if (event.pojo.workModel == SteamOvenOneModel.EXP) {
                Bundle bundle = new Bundle();
                bundle.putShort("from", (short) 1);
                bundle.putString(PageArgumentKey.Guid, event.pojo.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906ExpWorking, bundle);
            }
        } else {
            closeSteamOvenOneIcon();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
     /*   LogUtils.i("20171113", "guid:" + guid);
        LogUtils.i("20171113", "app:" + Plat.appGuid);*/
        View view = inflater.inflate(R.layout.page_device_steame_oven_c906_home, null, false);
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        ButterKnife.inject(this, view);
        initStatusShow();
        return view;
    }

    //根据状态展示图标
    private void initStatusShow() {
        if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off) {
            openSteamOvenOneIcon();
        } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.On) {
            closeSteamOvenOneIcon();
        }
        mDisconnectHintView.setVisibility(steameOvenC906.isConnected() ? View.INVISIBLE : View.VISIBLE);
        openSteamOvenOneIcon();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }


    @OnClick({R.id.img_return, R.id.tv_setting, R.id.tv_oven_steam_recipe, R.id.rl_oven_open, R.id.rl_oven_close,
            R.id.rl_stame_open, R.id.rl_steam_close, R.id.rl_fuzhud_open, R.id.iv_fuzhu_logo_close,
            R.id.iv_switch_open, R.id.iv_switch_close, R.id.rl_water_tank_close, R.id.rl_water_tank_off_line,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_setting:
                settingDevice();
                break;
            case R.id.tv_oven_steam_recipe:
                UIService.getInstance().returnHome();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.RecipeId, DeviceType.RZKY);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundle);
                break;
            case R.id.rl_oven_open:

                if (steameOvenC906.alarm == 64 && SteamOvenOnePowerOnStatus.AlarmStatus == steameOvenC906.powerOnStatus){
                    mRokiDialog.setContentText(R.string.device_c906_alarm);
                    mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                    mRokiDialog.show();
                    return;
                }
                Bundle bundleOven = new Bundle();
                bundleOven.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906Oven, bundleOven);
                break;
            case R.id.rl_oven_close:
                if (!StartNotice())return;
                break;
            case R.id.rl_stame_open:

                if (steameOvenC906.WaterStatus == 1 ){
                    mRokiDialog.setContentText(R.string.device_alarm_water_out);
                    mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                    mRokiDialog.show();
                    return;
                }
                Bundle bundleSteam = new Bundle();
                bundleSteam.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906Steame, bundleSteam);
                break;
            case R.id.rl_steam_close:
                if (!StartNotice())return;
                break;
            case R.id.rl_fuzhud_open:
                if (steameOvenC906.WaterStatus == 1 ){
                    mRokiDialog.setContentText(R.string.device_alarm_water_out);
                    mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                    mRokiDialog.show();
                    return;
                }
                Bundle bundleAssist = new Bundle();
                bundleAssist.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906Assist, bundleAssist);
                break;
            case R.id.iv_fuzhu_logo_close:
                if (!StartNotice())return;
                break;
            case R.id.rl_water_tank_off_line:
                if (!StartNotice())return;
                break;
            case R.id.rl_water_tank_close:
                LogUtils.i("20171030", "rl_water_tank_close:" + steameOvenC906.WaterStatus);
                if (!StartNotice())return;
                if (steameOvenC906.WaterStatus == 0) {
                    steameOvenC906.setSteameOvenOneWaterPop((short) 1, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20171030", "t:" + t);
                        }
                    });
                }
                break;
            case R.id.iv_switch_open:
                controlSteamOvenOneSwitch();
                break;
            case R.id.iv_switch_close:
                controlSteamOvenOneSwitch();
                break;

        }
    }

    //设置设备信息
    private void settingDevice() {

        FanC906DetailDialog dlg = FanC906DetailDialog.show(cx);
        dlg.setPickListener(new FanC906DetailDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(short index) {
                if (index == FanC906DetailDialog.PickListener.PRODUCT) {
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                    bd.putBoolean(PageArgumentKey.IfDeleteInDeviceDetail, false);
                    UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
                } else if (index == FanC906DetailDialog.PickListener.SALE) {
                    makePhoneCall();
                } else if (index == FanC906DetailDialog.PickListener.OPINION) {
                    if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)) {
                        UIService.getInstance().postPage(PageKey.Chat);
                    }
                }
            }
        });
    }

    //拨打售后电话
    private void makePhoneCall() {
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialogByType.setTitleText(StringConstantsUtil.STRING_SERVICE_PHONE);
        dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialogByType.setCanceledOnTouchOutside(false);
        dialogByType.show();

    }

    //一体机开关机控制
    private void controlSteamOvenOneSwitch() {
        if (!steameOvenC906.isConnected()) {
            IRokiDialog startNoticeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
            startNoticeDialog.setContentText(R.string.device_connected);
            startNoticeDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            startNoticeDialog.show();
        }
        if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off ) {
            steameOvenC906.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    closeSteamOvenOneIcon();
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.On ||
                steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Wait) {
            steameOvenC906.setSteameOvenStatus_Off(new VoidCallback() {
                @Override
                public void onSuccess() {
                    openSteamOvenOneIcon();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    //关机时图标控制
    private void closeSteamOvenOneIcon() {

        mIvSwitchClose.setVisibility(View.GONE);
        mIvSwitchOpen.setVisibility(View.VISIBLE);
        //烤
        mRlOvenClose.setVisibility(View.GONE);
        mRlOvenOpen.setVisibility(View.VISIBLE);
        //蒸
        mRlSteamClose.setVisibility(View.GONE);
        mRlStameOpen.setVisibility(View.VISIBLE);
        //辅助
        mRlFuzhuClose.setVisibility(View.GONE);
        mRlFuzhudOpen.setVisibility(View.VISIBLE);
        //水箱
        mRlWaterTankClose.setVisibility(View.VISIBLE);
        mRlWaterTankOffLine.setVisibility(View.GONE);
    }

    //开机时图标控制
    private void openSteamOvenOneIcon() {
        mIvSwitchOpen.setVisibility(View.VISIBLE);
        mIvSwitchClose.setVisibility(View.GONE);
        mRlOvenOpen.setVisibility(View.GONE);
        mRlOvenClose.setVisibility(View.VISIBLE);
        //蒸
        mRlSteamClose.setVisibility(View.VISIBLE);
        mRlStameOpen.setVisibility(View.GONE);
        //辅助
        mRlFuzhuClose.setVisibility(View.VISIBLE);
        mRlFuzhudOpen.setVisibility(View.GONE);
        //水箱
        if (steameOvenC906.isConnected()){
            mRlWaterTankClose.setVisibility(View.VISIBLE);
            mRlWaterTankOpen.setVisibility(View.GONE);
            mRlWaterTankOffLine.setVisibility(View.GONE);
        }else {
            mRlWaterTankClose.setVisibility(View.GONE);
            mRlWaterTankOffLine.setVisibility(View.VISIBLE);
        }


    }


    //启动时通知
    private boolean StartNotice() {
        IRokiDialog startNoticeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        if (!steameOvenC906.isConnected()) {
            startNoticeDialog.setContentText(R.string.device_connected);
            startNoticeDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            startNoticeDialog.show();
            return false;
        } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off) {
            startNoticeDialog.setContentText(R.string.open_device);
            startNoticeDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            startNoticeDialog.show();
            return false;
        }

        return true;
    }


}
