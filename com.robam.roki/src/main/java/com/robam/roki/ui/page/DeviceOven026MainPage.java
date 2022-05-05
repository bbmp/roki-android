package com.robam.roki.ui.page;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.OvenUserAction;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
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
 * Created by rent on 2016/10/08.
 */
public class DeviceOven026MainPage extends BasePage {
    String guid;
    View contentView;
    AbsOven oven026;
    LayoutInflater inflater;
    private IRokiDialog mRokiDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven026 = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven_normal2, container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    void init() {
        if (IRokiFamily.RR075.equals(oven026.getDt())) {
            txtRecipe.setVisibility(View.VISIBLE);
            oven026_main_tv1_exp.setText("专家");
            oven026_main_tv2_exp.setText("模式");
        } else {
            txtRecipe.setVisibility(View.GONE);
            oven026_main_tv1_exp.setText(R.string.temp_2);
            oven026_main_tv2_exp.setText(R.string.control_2);
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
    }

    //最近使用
    @OnClick(R.id.txtRecipe)
    public void onClickRecently() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        onclick_recently_user();
    }


    Dialog dialog = null;

    private void onclick_recently_user() {
        if (!checkStateAndConnect()) {
            return;
        }
        dialog = Helper.newOven028RecentlyUseDialog(cx, new Callback2<OvenUserAction>() {

            @Override
            public void onCompleted(OvenUserAction ovenUserAction) {
                if (oven026.alarm != 255) {
                    ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
                    return;
                }
                LogUtils.i("20180509", "mode::" + ovenUserAction.getMode() + "");
                if ("EXP".equals(ovenUserAction.getName()) && ovenUserAction.getMode() == 9) {
                    sendExp(ovenUserAction);
                }
                /*if (ovenUserAction.getMode()==9){

                }*/
                else {
                    if ("自动模式".equals(ovenUserAction.getName())) {
                        sendAutoMode(ovenUserAction);
                    } else {
                        sendProfession(ovenUserAction);
                    }

                }
            }
        }, oven026);
    }

    private void sendAutoMode(OvenUserAction ovenUserAction) {
        LogUtils.i("20180509", "mode:" + ovenUserAction.getMode());
        oven026.setOvenAutoRunMode(ovenUserAction.getMode(), ovenUserAction.getTimeCook(), new VoidCallback() {
            @Override
            public void onSuccess() {
                // ToastUtils.show("指令下发成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                // ToastUtils.show(R.string.device_Throwable_error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void sendExp(OvenUserAction ovenUserAction) {
        oven026.setOvenRunMode((short) 9, ovenUserAction.getTimeCook(), ovenUserAction.getTemperUp(),
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


    //专业模式指令
    private void sendProfession(OvenUserAction ovenUserAction) {
        oven026.setOvenRunMode(ovenUserAction.getMode(), ovenUserAction.getTimeCook(), ovenUserAction.getTemperature()
                , (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        // ToastUtils.show("指令下发成功",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                    }
                });
    }

    @OnClick(R.id.oven026_return)
    public void onClickReturn() {
        UIService.getInstance().popBack();
    }


    @OnClick(R.id.oven026_main_ll_beef)
    public void onClickMeat() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.BEEF);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_bread)
    public void onClickBread() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.BREAD);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_biscuits)
    public void onClickBiscuits() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.BISCUITS);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_chicken)
    public void onClickChicken() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.CHICKENWINGS);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_cake)
    public void onClickCake() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.CAKE);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_pizza)
    public void onClickPizza() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.PIZZA);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_shrimp)
    public void onClickShrimp() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.GRILLEDSHRIMP);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_fish)
    public void onClickFish() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.ROASTFISH);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_sweetpotato)
    public void onClickSweetPotato() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.SWEETPOTATO);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_corn)
    public void onClickCorn() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.CORN);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_wuhuarou)
    public void onClickWuhuarou() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.STREAKYPORK);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_vegetables)
    public void onClickVegetable() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        bd.putShort("model", OvenMode.VEGETABLES);
        UIService.getInstance().postPage(PageKey.DeviceOven026AutoSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_profession)
    public void onClickProfession() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        if (IRokiFamily.RR075.equals(oven026.getDt())) {
            UIService.getInstance().postPage(PageKey.DeviceOven075ProfessionalSetting, bd);
        } else {
            UIService.getInstance().postPage(PageKey.DeviceOven026ProfessionalSetting, bd);
        }

    }

    @OnClick(R.id.oven026_main_ll_exp)
    public void onClickExp() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!checkStateAndConnect())
            return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, oven026.getID());
        UIService.getInstance().postPage(PageKey.DeviceOven026ExpSetting, bd);
    }

    @OnClick(R.id.oven026_main_ll_switch)
    public void onClickSwitch() {
        if (oven026.alarm == 6) {
            ToastUtils.show("设备故障了，无法操作", Toast.LENGTH_SHORT);
            return;
        }
        if (!oven026.isConnected())
            return;
        if (oven026.status == OvenStatus.Off || oven026.status == OvenStatus.Wait) {
            setOvenStatus(OvenStatus.On);
        } else if (oven026.status == OvenStatus.On || oven026.status == OvenStatus.AlarmStatus) {
            setOvenStatus(OvenStatus.Off);
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven026 == null || !Objects.equal(oven026.getID(), event.device.getID()))
            return;
        if (event.isConnected) {
            disconnectHintView.setVisibility(View.INVISIBLE);
        } else {
            disconnectHintView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) oven026_main_panel.getLayoutParams();
            layoutParams.setMargins(DisplayUtils.dip2px(cx, 0), 0, 0, 0);
            oven026_main_panel.setLayoutParams(layoutParams);
            setWriteIcon();
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven026Main)) {
            return;
        }
        if (oven026 == null || !Objects.equal(oven026.getID(), event.pojo.getID()))
            return;
        if (oven026.status == OvenStatus.Wait || oven026.status == OvenStatus.Off) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            setWriteIcon();
        } else if (oven026.status == OvenStatus.Working || oven026.status == OvenStatus.Pause
                || oven026.status == OvenStatus.Order || oven026.status == OvenStatus.PreHeat) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, oven026.getID());
            bundle.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
        } else {
            setYellowIcon();
        }

    }

    boolean checkStateAndConnect() {
        if (!oven026.isConnected())
            return false;
        if (oven026.status == OvenStatus.Off || oven026.status == OvenStatus.Wait) {
            StartNotice();
            return false;
        }
        return true;
    }

    void setOvenStatus(final short state) {
        oven026.setOvenStatus(state, null);
    }

    void setWriteIcon() {
        oven026_main_img_beef.setImageResource(R.mipmap.ic_026ovenmain_beef_white);
        oven026_main_tv_beef.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_bread.setImageResource(R.mipmap.ic_026ovenmain_bread_white);
        oven026_main_tv_bread.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_biscuits.setImageResource(R.mipmap.ic_026ovenmain_biscuits_white);
        oven026_main_tv_biscuits.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_chicken.setImageResource(R.mipmap.ic_026ovenmain_chicken_white);
        oven026_main_tv_chicken.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_cake.setImageResource(R.mipmap.ic_026ovenmain_cake_white);
        oven026_main_tv_cake.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_pizza.setImageResource(R.mipmap.ic_026ovenmain_pizza_white);
        oven026_main_tv_pizza.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_shrimp.setImageResource(R.mipmap.ic_026ovenmain_shrimp_white);
        oven026_main_tv_shrimp.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_fish.setImageResource(R.mipmap.ic_026ovenmain_fish_white);
        oven026_main_tv_fish.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_sweetpotato.setImageResource(R.mipmap.ic_026ovenmain_sweetpotato_white);
        oven026_main_tv_sweetpotato.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_corn.setImageResource(R.mipmap.ic_026ovenmain_corn_white);
        oven026_main_tv_corn.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_wuhuarou.setImageResource(R.mipmap.ic_026ovenmain_wuhuarou_white);
        oven026_main_tv_wuhuarou.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_vegetables.setImageResource(R.mipmap.ic_026ovenmain_vegetables_white);
        oven026_main_tv_vegetables.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_exp.setImageResource(R.mipmap.ic_026ovenmain_exp_white);
        oven026_main_tv1_exp.setTextColor(r.getColor(R.color.c02));
        oven026_main_tv2_exp.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_profession.setImageResource(R.mipmap.ic_026ovenmain_profession_white);
        oven026_main_tv1_profession.setTextColor(r.getColor(R.color.c02));
        oven026_main_tv2_profession.setTextColor(r.getColor(R.color.c02));
        oven026_main_img_switch.setImageResource(R.mipmap.ic_device_oven_start_white);
        oven026_main_tv_switch.setTextColor(r.getColor(R.color.white));
        oven026_main_tv_switch.setText(cx.getString(R.string.device_close));
    }

    void setYellowIcon() {
        oven026_main_img_beef.setImageResource(R.mipmap.ic_026ovenmain_beef_yellow);
        oven026_main_tv_beef.setTextColor(r.getColor(R.color.white));
        oven026_main_img_bread.setImageResource(R.mipmap.ic_026ovenmain_bread_yellow);
        oven026_main_tv_bread.setTextColor(r.getColor(R.color.white));
        oven026_main_img_biscuits.setImageResource(R.mipmap.ic_026ovenmain_biscuits_yellow);
        oven026_main_tv_biscuits.setTextColor(r.getColor(R.color.white));
        oven026_main_img_chicken.setImageResource(R.mipmap.ic_026ovenmain_chicken_yellow);
        oven026_main_tv_chicken.setTextColor(r.getColor(R.color.white));
        oven026_main_img_cake.setImageResource(R.mipmap.ic_026ovenmain_cake_yellow);
        oven026_main_tv_cake.setTextColor(r.getColor(R.color.white));
        oven026_main_img_pizza.setImageResource(R.mipmap.ic_026ovenmain_pizza_yellow);
        oven026_main_tv_pizza.setTextColor(r.getColor(R.color.white));
        oven026_main_img_shrimp.setImageResource(R.mipmap.ic_026ovenmain_shrimp_yellow);
        oven026_main_tv_shrimp.setTextColor(r.getColor(R.color.white));
        oven026_main_img_fish.setImageResource(R.mipmap.ic_026ovenmain_fish_yellow);
        oven026_main_tv_fish.setTextColor(r.getColor(R.color.white));
        oven026_main_img_sweetpotato.setImageResource(R.mipmap.ic_026ovenmain_sweetpotato_yellow);
        oven026_main_tv_sweetpotato.setTextColor(r.getColor(R.color.white));
        oven026_main_img_corn.setImageResource(R.mipmap.ic_026ovenmain_corn_yellow);
        oven026_main_tv_corn.setTextColor(r.getColor(R.color.white));
        oven026_main_img_wuhuarou.setImageResource(R.mipmap.ic_026ovenmain_wuhuarou_yellow);
        oven026_main_tv_wuhuarou.setTextColor(r.getColor(R.color.white));
        oven026_main_img_vegetables.setImageResource(R.mipmap.ic_026ovenmain_vegetables_yellow);
        oven026_main_tv_vegetables.setTextColor(r.getColor(R.color.white));
        oven026_main_img_exp.setImageResource(R.mipmap.ic_026ovenmain_exp_yellow);
        oven026_main_tv1_exp.setTextColor(r.getColor(R.color.white));
        oven026_main_tv2_exp.setTextColor(r.getColor(R.color.white));
        oven026_main_img_profession.setImageResource(R.mipmap.ic_026ovenmain_profession_yellow);
        oven026_main_tv1_profession.setTextColor(r.getColor(R.color.white));
        oven026_main_tv2_profession.setTextColor(r.getColor(R.color.white));
        oven026_main_img_switch.setImageResource(R.mipmap.ic_device_oven_started);
        oven026_main_tv_switch.setTextColor(r.getColor(R.color.c02));
        oven026_main_tv_switch.setText(cx.getString(R.string.device_open));
    }


    void StartNotice() {
        if (mRokiDialog != null && mRokiDialog.isShow())
            return;
        mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        mRokiDialog.setContentText(R.string.open_device);
        mRokiDialog.show();
    }

    @InjectView(R.id.disconnectHintView)//断网
            LinearLayout disconnectHintView;
    @InjectView(R.id.oven026_main_panel)//面板
            LinearLayout oven026_main_panel;
    @InjectView(R.id.oven026_main_ll_beef)//牛排
            LinearLayout oven026_main_ll_beef;
    @InjectView(R.id.oven026_main_img_beef)
    ImageView oven026_main_img_beef;
    @InjectView(R.id.oven026_main_tv_beef)
    TextView oven026_main_tv_beef;
    @InjectView(R.id.oven026_main_ll_bread)//面包
            LinearLayout oven026_main_ll_bread;
    @InjectView(R.id.oven026_main_img_bread)
    ImageView oven026_main_img_bread;
    @InjectView(R.id.oven026_main_tv_bread)
    TextView oven026_main_tv_bread;
    @InjectView(R.id.oven026_main_ll_biscuits)//饼干
            LinearLayout oven026_main_ll_biscuits;
    @InjectView(R.id.oven026_main_img_biscuits)
    ImageView oven026_main_img_biscuits;
    @InjectView(R.id.oven026_main_tv_biscuits)
    TextView oven026_main_tv_biscuits;
    @InjectView(R.id.oven026_main_ll_chicken)//鸡翅
            LinearLayout oven026_main_ll_chicken;
    @InjectView(R.id.oven026_main_img_chicken)
    ImageView oven026_main_img_chicken;
    @InjectView(R.id.oven026_main_tv_chicken)
    TextView oven026_main_tv_chicken;
    @InjectView(R.id.oven026_main_ll_cake)//蛋糕
            LinearLayout oven026_main_ll_cake;
    @InjectView(R.id.oven026_main_img_cake)
    ImageView oven026_main_img_cake;
    @InjectView(R.id.oven026_main_tv_cake)
    TextView oven026_main_tv_cake;
    @InjectView(R.id.oven026_main_ll_pizza)//披萨
            LinearLayout oven026_main_ll_pizza;
    @InjectView(R.id.oven026_main_img_pizza)
    ImageView oven026_main_img_pizza;
    @InjectView(R.id.oven026_main_tv_pizza)
    TextView oven026_main_tv_pizza;
    @InjectView(R.id.oven026_main_ll_shrimp)//虾
            LinearLayout oven026_main_ll_shrimp;
    @InjectView(R.id.oven026_main_img_shrimp)
    ImageView oven026_main_img_shrimp;
    @InjectView(R.id.oven026_main_tv_shrimp)
    TextView oven026_main_tv_shrimp;
    @InjectView(R.id.oven026_main_ll_fish)//鱼
            LinearLayout oven026_main_ll_fish;
    @InjectView(R.id.oven026_main_img_fish)
    ImageView oven026_main_img_fish;
    @InjectView(R.id.oven026_main_tv_fish)
    TextView oven026_main_tv_fish;
    @InjectView(R.id.oven026_main_ll_sweetpotato)//红薯
            LinearLayout oven026_main_ll_sweetpotato;
    @InjectView(R.id.oven026_main_img_sweetpotato)
    ImageView oven026_main_img_sweetpotato;
    @InjectView(R.id.oven026_main_tv_sweetpotato)
    TextView oven026_main_tv_sweetpotato;
    @InjectView(R.id.oven026_main_ll_corn)//玉米
            LinearLayout oven026_main_ll_corn;
    @InjectView(R.id.oven026_main_img_corn)
    ImageView oven026_main_img_corn;
    @InjectView(R.id.oven026_main_tv_corn)
    TextView oven026_main_tv_corn;
    @InjectView(R.id.oven026_main_ll_wuhuarou)//五花肉
            LinearLayout oven026_main_ll_wuhuarou;
    @InjectView(R.id.oven026_main_img_wuhuarou)
    ImageView oven026_main_img_wuhuarou;
    @InjectView(R.id.oven026_main_tv_wuhuarou)
    TextView oven026_main_tv_wuhuarou;
    @InjectView(R.id.oven026_main_ll_vegetables)//蔬菜
            LinearLayout oven026_main_ll_vegetables;
    @InjectView(R.id.oven026_main_img_vegetables)
    ImageView oven026_main_img_vegetables;
    @InjectView(R.id.oven026_main_tv_vegetables)
    TextView oven026_main_tv_vegetables;
    @InjectView(R.id.oven026_main_ll_exp)//exp
            LinearLayout oven026_main_ll_exp;
    @InjectView(R.id.oven026_main_img_exp)
    ImageView oven026_main_img_exp;
    @InjectView(R.id.oven026_main_tv1_exp)
    TextView oven026_main_tv1_exp;
    @InjectView(R.id.oven026_main_tv2_exp)
    TextView oven026_main_tv2_exp;
    @InjectView(R.id.oven026_main_ll_profession)//专业模式
            LinearLayout oven026_main_ll_profession;
    @InjectView(R.id.oven026_main_img_profession)
    ImageView oven026_main_img_profession;
    @InjectView(R.id.oven026_main_tv1_profession)
    TextView oven026_main_tv1_profession;
    @InjectView(R.id.oven026_main_tv2_profession)
    TextView oven026_main_tv2_profession;
    @InjectView(R.id.oven026_main_fram_switch)//开关
            FrameLayout oven026_main_fram_switch;
    @InjectView(R.id.oven026_main_img_switch)//开关img
            ImageView oven026_main_img_switch;
    @InjectView(R.id.oven026_main_tv_switch)//开关tv
            TextView oven026_main_tv_switch;
    @InjectView(R.id.txtRecipe)
    TextView txtRecipe;
}
