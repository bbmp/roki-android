package com.robam.roki.ui.page;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.UserAction;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.ui.dialog.BlackPromptDialog526;
import com.robam.roki.ui.dialog.Microwave526ProfessionalDialog;
import com.robam.roki.ui.view.HomeRecipeView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/1.
 */

public class DeviceMicrowave526MainPage extends HeadPage {
    @InjectView(R.id.disconnectHintView)//断网
            View disconnectHintView;
    @InjectView(R.id.mic_llProMode)//快速加热 linear
            LinearLayout mic_llProMode;
    @InjectView(R.id.mic_imgProMode)//快速加热图片
            ImageView mic_imgProMode;
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
    @InjectView(R.id.micro526_btn)
            Button micro526_btn;
    @InjectView(R.id.mic_catalog_profession_ll)
    LinearLayout mic_catalog_profession_ll;//专业模式 Linear
    private MicroWaveM526 microWave;
    IRokiDialog rokiDialog = null;
    IRokiDialog rokiGatingDialog = null;
    private final int QUICKHEAT = 0;//快速加热
    private final int RECIPE_MODEL = 1;//菜谱
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


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        microWave = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave526_normal,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();

        rokiGatingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
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
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
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
    //门控提示
    private void gatingShow() {
        rokiGatingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        rokiGatingDialog.setContentText(R.string.device_alarm_gating_content);
        rokiGatingDialog.show();
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
                    microWave.setMicroWaveProModeHeat(MicroWaveModel.MicroWave, Short.valueOf((short) time), (short) 6, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Bundle bundle = new Bundle();
                            bundle.putString(PageArgumentKey.Guid, microWave.getID());
                            bundle.putString(PageArgumentKey.selectflag,"true");
                            bundle.putString(PageArgumentKey.MicroWavePageArg, "1");
                            UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });
    }

    //设置菜谱数据
    private void setDeviceRecipeModelData(String data, final int model) {
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
                microWave.setMicroWaveKindsAndHeatCold((short) model, Short.parseShort(removeString), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "0");
                        bundle.putString(PageArgumentKey.selectflag,"true");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });
    }


    /**
     * 最近使用点击事件
     */
    @OnClick(R.id.micro526_btn)
    public void onClickUse(){
        if(!microWave.isConnected())return;
        dialog2=Helper.newMicrowave526RecentlyUseDialog(cx, new Callback2<UserAction>() {
            @Override
            public void onCompleted(UserAction userAction) {
                if (microWave.doorState == 1) {
                    if ( rokiGatingDialog != null &&  rokiGatingDialog.isShow())
                        return;
                   gatingShow();
                    return;
                }
                if (userAction.getMode()==MicroWaveModel.Barbecue||userAction.getMode()==MicroWaveModel.ComibineHeating||
                        (userAction.getMode()==MicroWaveModel.MicroWave&&!(cx.getString(R.string.device_microwave_to_taste).equals(userAction.getName())))){
                    microWave.setMicroWaveProModeHeat(userAction.getMode(), userAction.getTimeCook(), userAction.getFire(), new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.show(R.string.device_command_success,Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.show(R.string.device_Failure_text,Toast.LENGTH_SHORT);
                        }
                    });
                }else if (MicroWaveModel.MicroWave==userAction.getMode()&&cx.getString(R.string.device_microwave_to_taste).equals(userAction.getName())){
                    microWave.setMicroWaveCleanAir(userAction.getMode(), userAction.getTimeCook(), userAction.getFire(), 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.show(R.string.device_command_success,Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.show(R.string.device_command_success,Toast.LENGTH_SHORT);
                        }
                    });
                } else{
                    microWave.setMicroWaveKindsAndHeatCold(userAction.getMode(), userAction.getWeight(), new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.show(R.string.device_command_success,Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.show(R.string.device_command_success,Toast.LENGTH_SHORT);
                        }
                    });
                }
            }
        },microWave);
    }

    /**
     * 微波炉菜谱点击
     */
    @OnClick(R.id.mic_txtRecipe)
    public void OnMicroRecipeClick() {

        if(!microWave.isConnected()){
            ToastUtils.show("微波炉已离线",Toast.LENGTH_SHORT);
            return;
        }
        UIService.getInstance().popBack().popBack();
        HomeRecipeView.recipeCategoryClick(DeviceType.RWBL);
       // ToastUtils.show(R.string.please_look_forward_opening, Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.mic_catalog_kebab_ll)//肉串
    public void onClickKebab() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Kebab);
    }

    @OnClick(R.id.mic_catalog_meat_ll)//肉
    public void onClickMeat() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Meat);
    }

    @OnClick(R.id.mic_catalog_chicken_ll)//鸡
    public void onClickChecken() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Checken);
    }

    @OnClick(R.id.mic_catalog_rice_ll)//米饭
    public void onClickRice() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Rice);
    }

    @OnClick(R.id.mic_catalog_porridge_ll)//粥
    public void onClickPorridge() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Porridge);
    }

    @OnClick(R.id.mic_catalog_milk_ll)//煲汤
    public void onClickMilk() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Milk);
    }

    @OnClick(R.id.mic_catalog_bread_ll)//面包
    public void onClickBread() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Bread);
    }

    @OnClick(R.id.mic_catalog_vegetables_ll)//蔬菜
    public void onClickVegetable() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Vegetables);
    }

    @OnClick(R.id.mic_catalog_fish_ll)//鱼
    public void onClickFish() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if(!microWave.isConnected())return;
        ModeSelection(MicroWaveModel.Fish);
    }

    @OnClick(R.id.mic_catalog_profession_ll)//专业模式
    public void onClickProfession() {
        if (microWave.doorState==1){
            gatingShow();
            return;
        }
        if (!microWave.isConnected()) return;
        dialog4 = Microwave526ProfessionalDialog.show(cx, microWave.getID());
    }
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (microWave == null || !Objects.equal(microWave.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.GONE : View.VISIBLE);
        if (!event.isConnected){
            closeAllDialog();
        }
        boolean isConnect = event.isConnected;
        ColorChange(isConnect);
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {
        switch (microWave.state) {
            case MicroWaveStatus.Run:
                closeAllDialog();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, microWave.getID());
                bundle.putString(PageArgumentKey.selectflag,"true");
                if (microWave.step == 0) {
                    UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                }
                break;
            case MicroWaveStatus.Pause:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, microWave.getID());
                bd.putString(PageArgumentKey.selectflag,"true");
                if (microWave.step == 0)
                    UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bd);
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
     * 关闭所有对话框
     */
    public void closeAllDialog(){
        if (dialog1!=null&&dialog1.isShowing()){
            dialog1.dismiss();
        }
        if (dialog2!=null&&dialog2.isShowing()){
            dialog2.dismiss();
        }
        if (dialog3!=null&&dialog3.isShowing()){
            dialog3.dismiss();
        }
        if (dialog4!=null&&dialog4.isShowing()){
            dialog4.dismiss();
        }
        if (Microwave526ProfessionalDialog.dialog1!=null&&Microwave526ProfessionalDialog.dialog1.isShowing()){
            Microwave526ProfessionalDialog.dialog1.dismiss();
        }
        if (Microwave526ProfessionalDialog.dialog2!=null&&Microwave526ProfessionalDialog.dialog2.isShowing()){
            Microwave526ProfessionalDialog.dialog2.dismiss();
        }
        if (Microwave526ProfessionalDialog.dialog3!=null&&Microwave526ProfessionalDialog.dialog3.isShowing()){
            Microwave526ProfessionalDialog.dialog3.dismiss();
        }
    }
    /**
     * 改变文案
     */
    private void ColorChange(boolean isOn) {
        disconnectHintView.setVisibility(isOn ? View.INVISIBLE : View.VISIBLE);
        micro526_btn.setBackgroundResource(isOn ? R.mipmap.img_micr0526_use : R.mipmap.img_micro526_use_gray);
        mic_imgProMode.setImageResource(isOn ? R.mipmap.ic_device_microwave526_quickeating_yellow : R.mipmap.ic_device_microwave526_quickeating_gray);
        mic_catalog_meat_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_beef : R.mipmap.ic_device_microwave526_normal_beef_gray);
        mic_catalog_chicken_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_chicken : R.mipmap.ic_device_microwave526_normal_chicken_gray);
        mic_catalog_kebab_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_noraml_kebab : R.mipmap.ic_device_microwave526_normal_kebab_gray);
        mic_catalog_rice_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_porridge :R.mipmap.ic_device_microwave526_normal_porridge_gray );
        mic_catalog_porridge_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_rice :R.mipmap.ic_device_microwave526_normal_rice_gray);
        mic_catalog_milk_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_baotang : R.mipmap.ic_device_microwave526_normal_baotang_gray);
        mic_catalog_bread_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_bread : R.mipmap.ic_device_microwave526_normal_bread_gray);
        mic_catalog_vegetables_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_vegetable : R.mipmap.ic_device_microwave526_normal_vegetable_gray);
        mic_catalog_fish_img.setImageResource(isOn ? R.mipmap.ic_device_microwave526_normal_fish : R.mipmap.ic_device_microwave526_normal_fish_gray);
        mic_catalog_profession_img.setImageResource(isOn ? R.mipmap.ic_device_microwave_normal_profession : R.mipmap.ic_device_microwave_normal_profession_gray);
        int res_color = isOn ? r.getColor(R.color.white) : r.getColor(R.color.Gray);
        txtProMode.setTextColor(res_color);
        micro526_btn.setTextColor(res_color);
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

    Dialog dialog1;
    Dialog dialog2;
    Dialog dialog3;
    Dialog dialog4;

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

    //根据菜谱模式获取对应数据
    private List<String> getModeListData(short mode) {
        List<String> list = Lists.newArrayList();
        list.clear();
        switch (mode) {
            case MicroWaveModel.Meat:
                for (int i = 200; i <= 600; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Checken:
                for (int i = 600; i <= 1200; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Kebab:
                for (int i = 100; i <= 400; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Rice:
                for (int i = 200; i <= 600; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Porridge:
                for (int i = 50; i <= 200; i = i + 50){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Vegetables:
                for (int i = 100; i <= 500; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.Fish:
                for (int i = 100; i <= 400; i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
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
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
            case MicroWaveModel.HeatingAgain://再加热
                for (int i = 200; i <= 600;i = i + 100){
                    list.add(i + StringConstantsUtil.STRING_GRAM);
                }
                break;
        }
        return list;
    }
    /**
     * 判断是否联网和门开
     */
    private boolean isConnectedOrDoorOpen() {
        if (!microWave.isConnected()) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return false;
        }

        if (microWave.doorState == 1) {
            if ( BlackPromptDialog526.dlg != null &&  BlackPromptDialog526.dlg.isShowing()) return false;
            Dialog dialog = Helper.new526BlackPromptDialog(cx,null,microWave.state);
            dialog.show();
            return false;
        }
        return true;
    }
}
