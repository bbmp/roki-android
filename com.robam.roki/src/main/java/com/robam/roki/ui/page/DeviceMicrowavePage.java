package com.robam.roki.ui.page;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.qrcode.decoding.Intents;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.microwave.MicroWaveM509;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.common.pojos.device.microwave.MicrowaveUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.ui.dialog.MicrowaveProfessionalDialog;
import com.robam.roki.ui.view.HomeRecipeView32;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/10.
 */
public class DeviceMicrowavePage extends HeadPage {
    @InjectView(R.id.disconnectHintView)//断网
            View disconnectHintView;
    @InjectView(R.id.mic_llProMode)//快速加热 linear
            LinearLayout mic_llProMode;
    @InjectView(R.id.mic_imgProMode)//快速加热图片
            ImageView mic_imgProMode;
    @InjectView(R.id.mic_quickheat_img)//快速加热图标
            ImageView mic_quickheat_img;
    @InjectView(R.id.txtProMode)//快速加热文字
            TextView txtProMode;
    @InjectView(R.id.mic_catalog_meat_img)//肉图
            ImageView mic_catalog_meat_img;
    @InjectView(R.id.mic_catalog_meat_txt)//肉字
            TextView mic_catalog_meat_txt;
    @InjectView(R.id.mic_catalog_chicken_img)//鸡图
            ImageView mic_catalog_chicken_img;
    @InjectView(R.id.mic_catalog_chicken_txt)//鸡字
            TextView mic_catalog_chicken_txt;
    @InjectView(R.id.mic_catalog_kebab_img)//肉串图
            ImageView mic_catalog_kebab_img;
    @InjectView(R.id.mic_catalog_kebab_txt)//肉串字
            TextView mic_catalog_kebab_txt;
    @InjectView(R.id.mic_catalog_rice_img)//米饭图
            ImageView mic_catalog_rice_img;
    @InjectView(R.id.mic_catalog_rice_txt)//米饭字
            TextView mic_catalog_rice_txt;
    @InjectView(R.id.mic_catalog_porridge_img)//粥图
            ImageView mic_catalog_porridge_img;
    @InjectView(R.id.mic_catalog_porridge_txt)//粥字
            TextView mic_catalog_porridge_txt;
    @InjectView(R.id.mic_catalog_milk_img)//牛奶图
            ImageView mic_catalog_milk_img;
    @InjectView(R.id.mic_catalog_milk_txt)//牛奶字
            TextView mic_catalog_milk_txt;
    @InjectView(R.id.mic_catalog_bread_img)//面包图
            ImageView mic_catalog_bread_img;
    @InjectView(R.id.mic_catalog_bread_txt)//面包字
            TextView mic_catalog_bread_txt;
    @InjectView(R.id.mic_catalog_vegetables_img)//蔬菜图
            ImageView mic_catalog_vegetables_img;
    @InjectView(R.id.mic_catalog_vegetables_txt)//蔬菜字
            TextView mic_catalog_vegetables_txt;
    @InjectView(R.id.mic_catalog_fish_img)//鱼图
            ImageView mic_catalog_fish_img;
    @InjectView(R.id.mic_catalog_fish_txt)//鱼字
            TextView mic_catalog_fish_txt;
    @InjectView(R.id.mic_catalog_profession_img)//专业模式图片
            ImageView mic_catalog_profession_img;
    @InjectView(R.id.mic_catalog_profession_txt)//专业模式文字
            TextView mic_catalog_profession_txt;

    @InjectView(R.id.mic_catalog_profession_ll)
    LinearLayout mic_catalog_profession_ll;//专业模式 Linear
    private MicroWaveM509 microWave;
    IRokiDialog rokiDialog = null;
    private final int QUICKHEAT = 0;
    private final int RECIPE_MODEL = 1;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case QUICKHEAT:
                    setDeviceQuickHeatData((String) msg.obj);
                    break;
                case RECIPE_MODEL:
                    setDeviceRecipeModelData((String) msg.obj,msg.arg1);
                    break;

            }

        }
    };
    private IRokiDialog mDialogByType;


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        microWave = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave_normal,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();
        return contentView;
    }

    private void initView() {
        if(microWave!=null) {
            if (!microWave.isConnected()) {//判断是否断网
                ColorChange(false);
                return;
            }
            disconnectHintView.setVisibility(View.INVISIBLE);//断网提示gone
        }
    }



    //快热模式数据
    private List<String> getListProModeData(){
        List<String> listProMode = new ArrayList<>();
        for (int i = 1; i <= 10;i++){
            listProMode.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return listProMode;
    }

    @OnClick(R.id.mic_llProMode)//快速加热模式
    public void onClickProMode() {
        if(!isConnectedOrDoorOpen()) return;
        rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        rokiDialog.setWheelViewData(null, getListProModeData(), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = QUICKHEAT;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        },null);
        rokiDialog.show();
    }


    //设置微波炉快速加热
    private void setDeviceQuickHeatData(String data) {
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        final int time = Short.valueOf(removeString) * 60;
        rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
                if(!microWave.isConnected())return;
                microWave.setMicroWaveProModeHeat(MicroWaveModel.MicroWave,Short.valueOf((short) time) , (short) 6, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "1");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowaveNormalWorking, bundle);
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });

        rokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });


    }

    //设置菜谱数据
    private void setDeviceRecipeModelData(String data, final int model){
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
                if(!microWave.isConnected())return;
                microWave.setMicroWaveKindsAndHeatCold((short) model, Short.parseShort(removeString), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "0");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowaveNormalWorking, bundle);
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });

            }
        });


    }


    /**
     * 微波炉菜谱点击
     */
    @OnClick(R.id.mic_txtRecipe)
    public void OnMicroRecipeClick() {
        getActivity().onBackPressed();
        HomeRecipeView32.recipeCategoryClick(DeviceType.RWBL);
        //ToastUtils.show(new String("即将开放，敬请期待"), Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.mic_catalog_kebab_ll)//肉串
    public void onClickKebab() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Kebab);
    }

    @OnClick(R.id.mic_catalog_meat_ll)//肉
    public void onClickMeat() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Meat);
    }

    @OnClick(R.id.mic_catalog_chicken_ll)//鸡
    public void onClickChecken() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Checken);
    }

    @OnClick(R.id.mic_catalog_rice_ll)//米饭
    public void onClickRice() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Rice);
    }

    @OnClick(R.id.mic_catalog_porridge_ll)//粥
    public void onClickPorridge() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Porridge);
    }

    @OnClick(R.id.mic_catalog_milk_ll)//煲汤
    public void onClickMilk() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Milk);
    }

    @OnClick(R.id.mic_catalog_bread_ll)//面包
    public void onClickBread() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Bread);
    }

    @OnClick(R.id.mic_catalog_vegetables_ll)//蔬菜
    public void onClickVegetable() {
        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Vegetables);
    }

    @OnClick(R.id.mic_catalog_fish_ll)//鱼
    public void onClickFish() {

        if(!isConnectedOrDoorOpen()) return;
        ModeSelection(MicroWaveModel.Fish);
    }

    @OnClick(R.id.mic_catalog_profession_ll)//专业模式
    public void onClickProfession() {
        if(!isConnectedOrDoorOpen()) return;
        MicrowaveProfessionalDialog.show(cx, microWave.getID());
    }

    //根据菜谱模式获取对应数据
    private List<String> getModeListData(short mode) {
        List<String> list = Lists.newArrayList();
            list.clear();
        switch (mode) {
            case MicroWaveModel.Meat:
                for (int i = 200; i <= 600; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Checken:
                for (int i = 600; i <= 1200; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Kebab:
                for (int i = 100; i <= 400; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Rice:
                for (int i = 200; i <= 600; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Porridge:
                for (int i = 50; i <= 200; i = i + 50){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Vegetables:
                for (int i = 100; i <= 500; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Fish:
                for (int i = 100; i <= 400; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.Milk:
                for (int i = 1; i <= 2;i++){
                    list.add(i + StringConstantsUtil.STRING_PART);
                }
                break;
            case MicroWaveModel.Bread:
                for (int i = 1; i <= 2;i++){
                    list.add(i + StringConstantsUtil.STRING_PART);
                }
                break;
            case MicroWaveModel.Unfreeze://解冻
                for (int i = 100; i <= 3000;i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.HeatingAgain://再加热
                for (int i = 200; i <= 600;i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
        }
        return list;
    }



    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (microWave == null || !Objects.equal(microWave.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.GONE : View.VISIBLE);
        boolean isConnect = event.isConnected;
        ColorChange(isConnect);
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {
        switch (microWave.state) {
            case MicroWaveStatus.Run:
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, microWave.getID());
                if (microWave.step == 0) {
                    UIService.getInstance().postPage(PageKey.DeviceMicrowaveNormalWorking, bundle);
                } else
                    UIService.getInstance().postPage(PageKey.DeviceMicrowaveLinkageWorking, bundle);
                break;
            case MicroWaveStatus.Pause:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, microWave.getID());
                if (microWave.step == 0)
                    UIService.getInstance().postPage(PageKey.DeviceMicrowaveNormalWorking, bd);
                else
                    UIService.getInstance().postPage(PageKey.DeviceMicrowaveLinkageWorking, bd);
                break;
            case MicroWaveStatus.Alarm:
                break;
            case MicroWaveStatus.Wait:
                break;
            case MicroWaveStatus.Setting:
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        if (event.alarm == 0) return;
        IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
        if (dialogByType.isShow())return;
        dialogByType.setContentText(R.string.device_alarm_gating_content);
        dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
        dialogByType.show();

    }

    /**
     * 断网模式
     */
    private void InDisconnectMode() {
        if (is) {
            ColorChange(false);
            is = false;
        } else {
            ColorChange(true);
            is = true;
        }

    }

    private boolean is;

    /**
     * 改变文案
     */
    private void ColorChange(boolean isOn) {
        disconnectHintView.setVisibility(isOn ? View.INVISIBLE : View.VISIBLE);
        mic_imgProMode.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_quickheating_yellow : R.mipmap.ic_device_microwave_normal_quickheating_gray);
        mic_quickheat_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_heatingic_white : R.mipmap.ic_device_microwave_normal_heatingic_gray);
        mic_catalog_meat_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_meat : R.mipmap.ic_device_microwave_normal_meat_gray);
        mic_catalog_chicken_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_checken : R.mipmap.ic_device_microwave_normal_checken_gray);
        mic_catalog_kebab_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_kebab : R.mipmap.ic_device_microwave_normal_kebab_gray);
        mic_catalog_rice_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_rice : R.mipmap.ic_device_microwave_normal_rice_gray);
        mic_catalog_porridge_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_porridge : R.mipmap.ic_device_microwave_normal_porridge_gray);
        mic_catalog_milk_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_baotang : R.mipmap.ic_device_microwave_normal_baotang_gray);
        mic_catalog_bread_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_bread : R.mipmap.ic_device_microwave_normal_bread_gray);
        mic_catalog_vegetables_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_vegetables : R.mipmap.ic_device_microwave_normal_vegetables_gray);
        mic_catalog_fish_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_fish : R.mipmap.ic_device_microwave_normal_fish_gray);
        mic_catalog_profession_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_profession : R.mipmap.ic_device_microwave_normal_profession_gray);
        int res_color = isOn ? r.getColor(R.color.white) : r.getColor(R.color.Gray);
        txtProMode.setTextColor(res_color);
        mic_catalog_meat_txt.setTextColor(res_color);
        mic_catalog_chicken_txt.setTextColor(res_color);
        mic_catalog_kebab_txt.setTextColor(res_color);
        mic_catalog_rice_txt.setTextColor(res_color);
        mic_catalog_porridge_txt.setTextColor(res_color);
        mic_catalog_milk_txt.setTextColor(res_color);
        mic_catalog_bread_txt.setTextColor(res_color);
        mic_catalog_vegetables_txt.setTextColor(res_color);
        mic_catalog_fish_txt.setTextColor(res_color);
        mic_catalog_profession_txt.setTextColor(res_color);
    }

    /**
     * 各模式参数选择
     */
    private void ModeSelection(final short model) {

        if(rokiDialog != null){
            rokiDialog = null;
        }
        if (null == rokiDialog){
            rokiDialog = RokiDialogFactory.createDialogByType(cx,DialogUtil.DIALOG_TYPE_03);
        }
        rokiDialog.setWheelViewData(null, getModeListData(model), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = RECIPE_MODEL;
                message.obj = contentCenter;
                message.arg1 = model;
                mHandler.sendMessage(message);
            }
        },null);
        rokiDialog.show();
    }

    /**
     * 判断是否联网和门开
     */
    private boolean isConnectedOrDoorOpen() {
        IRokiDialog dialogByType =  RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        if (!microWave.isConnected()) {
            dialogByType.setContentText(R.string.device_connected);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
            dialogByType.show();
            return false;
        }
        if (microWave.doorState == 1) {
            dialogByType.setContentText(R.string.device_alarm_gating_content);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
            dialogByType.show();
            return false;
        }
        return true;
    }
}
