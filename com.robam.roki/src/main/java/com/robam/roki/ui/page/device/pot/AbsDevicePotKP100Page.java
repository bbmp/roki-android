package com.robam.roki.ui.page.device.pot;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.PotAlarmEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.PotOilTempParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.PotOtherFuncAdapter;
import com.robam.roki.ui.dialog.type.DialogType_PotKP100;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.PotTempTipsUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 无人锅KP100设备页
 */
public class AbsDevicePotKP100Page<Device extends Pot> extends DeviceCatchFilePage {

    Device pot;
    String mGuid;
    @InjectView(R.id.iv_bg)
    ImageView ivBg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView ivDeviceMore;
    @InjectView(R.id.tv_off_line_text)
    TextView tvOffLineText;
    @InjectView(R.id.tv_low_power)
    TextView tvLowPower;
    @InjectView(R.id.ll_low_power)
    LinearLayout llLowPower;
    @InjectView(R.id.img_state)
    ImageView imgState;
    @InjectView(R.id.tv_temp_show)
    TextView tvTempShow;
    @InjectView(R.id.tv_operate_tips)
    TextView tvOperateTips;
    @InjectView(R.id.rl_state)
    RelativeLayout rlState;
    @InjectView(R.id.img_continue_fast)
    ImageView imgContinueFast;
    @InjectView(R.id.ll_continue_fast)
    LinearLayout llContinueFast;
    @InjectView(R.id.img_10_second_stir_fry)
    ImageView img10SecondStirFry;
    @InjectView(R.id.ll_10_second_stir_fry)
    LinearLayout ll10SecondStirFry;
    @InjectView(R.id.rl_pot_stir_fry)
    RelativeLayout rlPotStirFry;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout layoutBg;

    private PotOilTempParams mPotOilTempParams;
    private PotOtherFuncAdapter mPotOtherFuncAdapter;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    DialogType_PotKP100 dialogType_potKP100;

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.pojo.getID()))
            return;
        pot = (Device) event.pojo;
        updatePotState();
    }

    private void updatePotState() {
        if (pot.isConnected()) {
            if (tvOffLineText != null) {
                tvOffLineText.setVisibility(View.GONE);
            }
        } else {
            Disconnect_Model();
            return;
        }
        if (pot.potStatus == 3) {//低电量状态
            llLowPower.setVisibility(View.VISIBLE);
        } else {
            llLowPower.setVisibility(View.GONE);
        }
        //电机模式
        if (pot.potESPMode == 0) {
            imgContinueFast.setImageResource(R.mipmap.icon_continue_fast_common);
            img10SecondStirFry.setImageResource(R.mipmap.icon_10_second_common);
        } else if (pot.potESPMode == 1) {
            imgContinueFast.setImageResource(R.mipmap.icon_continue_fast_select);
            img10SecondStirFry.setImageResource(R.mipmap.icon_10_second_common);
        } else if (pot.potESPMode == 2) {
            imgContinueFast.setImageResource(R.mipmap.icon_continue_fast_common);
            img10SecondStirFry.setImageResource(R.mipmap.icon_10_second_select);
        }
        //稳定显示
        if (pot.tempUp >= 130) {
            rlState.setVisibility(View.VISIBLE);
        } else {
            rlState.setVisibility(View.INVISIBLE);
        }

        if (mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.size() > 0 && !TextUtils.isEmpty(mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.get(0).functionParams)) {

            PotTempTipsUtil.updateState(imgState, tvTempShow, tvOperateTips, mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.get(0).functionParams, pot, cx);
        }
    }


    @Subscribe
    public void onEvent(PotAlarmEvent event) {
        short alarmId = event.alarmId;
        if (alarmId == 1) {
            llLowPower.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected) {
            Disconnect_Model();
        }
    }

    private void Disconnect_Model() {
        ivBack.setImageResource(R.mipmap.icon_back);
        tvDeviceModelName.setTextColor(getResources().getColor(R.color.black));
        ivDeviceMore.setImageResource(R.mipmap.icon_more);
        Glide.with(cx).load(mDeviceResponse.viewBackgroundImg).into(ivBg);
        rlState.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        pot = Plat.deviceService.lookupChild(mGuid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_pot_kp100, container, false);
        ButterKnife.inject(this, view);
        initData();
//        dialogType_potKP100 =  DialogType_PotKP100.createDialogType_PotKP100(cx);
//        dialogType_potKP100.choiceDefaultHead();
//        dialogType_potKP100.setOnLeftHeadClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show("左灶",Toast.LENGTH_SHORT);
//            }
//        });
//        dialogType_potKP100.choiceDefaultHead();
//        dialogType_potKP100.show();

        return view;
    }

    Reponses.DeviceResponse mDeviceResponse;

    @Override
    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {
        try {
            if (mDevice instanceof Pot) {
                pot = (Device) mDevice;
            }
            mDeviceResponse = deviceResponse;
            ModelMap modelMap = deviceResponse.modelMap;
            tvDeviceModelName.setText(deviceResponse.title);
            Glide.with(cx).load(deviceResponse.viewBackgroundImg).into(ivBg);
//            setDeepTheme();
//            Glide.with(cx).asGif().load(R.mipmap.pot_state_common).into(imgState);

            //电量  温度描述
            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
            List<DeviceConfigurationFunctions> backgroundFunctions = backgroundFunc.deviceConfigurationFunctions;
            for (int i = 0; i < backgroundFunctions.size(); i++) {
                if ("lowEnergy".equals(backgroundFunctions.get(i).functionCode)) {
                    String functionName = backgroundFunctions.get(i).functionName;
                    tvLowPower.setText(functionName);
                } else if ("temperatureTips".equals(backgroundFunctions.get(i).functionCode)) {
                    String functionParams = backgroundFunctions.get(i).functionParams;
                    mPotOilTempParams = JsonUtils.json2Pojo(functionParams, PotOilTempParams.class);
                }
            }

            OtherFunc otherFunc = modelMap.otherFunc;
            mDeviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
            mPotOtherFuncAdapter = new PotOtherFuncAdapter(cx, pot, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    otherFuncItemEvent(view);
                }
            });
            recyclerview.setAdapter(mPotOtherFuncAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                    (cx, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));
            if (!pot.isConnected()) {
                ToastUtils.showLong(R.string.device_new_connected);
                PotOilTempParams.OffLinBean offLin = mPotOilTempParams.getOffLin();
                if (tvOffLineText != null) {
                    tvOffLineText.setVisibility(View.VISIBLE);
                }
                Disconnect_Model();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otherFuncItemEvent(View view) {

        String tag = (String) view.getTag(R.id.tag_pot_other_func_key);

        switch (tag) {
            case "automatiCooking":
                List<AbsFan> fanList = Utils.getFan();
                List<Stove> stoves = new ArrayList<>();
                for (int i = 0; i < fanList.size(); i++) {
                    if (fanList.get(i).getChildStove() != null) {
                        stoves.add((Stove) fanList.get(i).getChildStove());
                    }
                }
                if (!pot.isConnected()) {
                    ToastUtils.show(R.string.oven_dis_con, Toast.LENGTH_SHORT);
                    return;
                }
;
                if (stoves.size() == 0) {
                    ToastUtils.show(R.string.device_stove_not, Toast.LENGTH_SHORT);
                    return;
                }
                Bundle bundleCook = new Bundle();
                bundleCook.putString(PageArgumentKey.RecipeId, IDeviceType.RZNG);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundleCook);

//                if(dialogType_potKP100==null){
//                    dialogType_potKP100 =  DialogType_PotKP100.createDialogType_PotKP100(cx);
//                }
//                dialogType_potKP100.continueCook();
//                dialogType_potKP100.show();

                break;
            case "tobaccoPotLinkage":
                if (!pot.isConnected()) {
                    ToastUtils.showLong(R.string.device_new_connected);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.Bean, pot);
                bundle.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                UIService.getInstance().postPage(PageKey.DevicePotFanLink, bundle);

//                if(dialogType_potKP100==null){
//                    dialogType_potKP100 =  DialogType_PotKP100.createDialogType_PotKP100(cx);
//                }
//                dialogType_potKP100.changeBatteryTips();
//                dialogType_potKP100.show();

                break;
            case "dryBurningWarning":
                if (!pot.isConnected()) {
                    ToastUtils.showLong(R.string.device_new_connected);
                    return;
                }
                Bundle bundleDry = new Bundle();
                bundleDry.putSerializable(PageArgumentKey.Bean, pot);
                bundleDry.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                UIService.getInstance().postPage(PageKey.DevicePotFanDry, bundleDry);
                break;
            case "curveCooking":
                if (!pot.isConnected()) {
                    ToastUtils.showLong(R.string.device_new_connected);
                    return;
                }

                break;
            case "favoriteCooking":
//                if (!pot.isConnected()) {
//                    ToastUtils.showLong(R.string.device_new_connected);
//                    return;
//                }
                bundleDry = new Bundle();
                bundleDry.putSerializable(PageArgumentKey.Bean, pot);
                bundleDry.putString(PageArgumentKey.Guid, pot.getID());
                UIService.getInstance().postPage(PageKey.PotMyMenu, bundleDry);

                break;
        }


    }

    //设置标题栏状态栏
    private void setDeepTheme() {
        ivBg.setBackgroundResource(R.color.pot_black_bg);
        ivBack.setImageResource(R.mipmap.icon_back);
        tvDeviceModelName.setTextColor(r.getColor(R.color.white));
        ivDeviceMore.setImageResource(R.drawable.ic_more_withe);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.pot_black_bg));
            StatusBarUtils.setTextDark(getContext(), false);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            StatusBarUtils.setTextDark(getContext(), true);
        }
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();

//        if(dialogType_potKP100==null){
//            dialogType_potKP100 =  DialogType_PotKP100.createDialogType_PotKP100(cx);
//        }
//        dialogType_potKP100.choiceHeadStartCook();
//        dialogType_potKP100.show();
    }

    @OnClick(R.id.ll_continue_fast)
    public void onContinueFast() {
//        pot.potStatus
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 6;
        interaction.length = 1;
        if (pot.potESPMode == 1) {
            interaction.value = new int[]{0};
        } else {
            interaction.value = new int[]{1};
        }
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                Glide.with(cx).load(mDeviceResponse.viewBackgroundImg).into(ivBg);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

//        rlState.setVisibility(View.INVISIBLE);


    }

    @OnClick(R.id.ll_10_second_stir_fry)
    public void onTenSecondFast() {
//        rlState.setVisibility(View.VISIBLE);
//        ivBg.setImageDrawable(null);
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 6;
        interaction.length = 1;
        if (pot.potESPMode == 2) {
            interaction.value = new int[]{0};
        } else {
            interaction.value = new int[]{2};
        }
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                Glide.with(cx).load(mDeviceResponse.viewBackgroundImg).into(ivBg);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, pot.getID());
        bd.putString(PageArgumentKey.pot, "pot");
        bd.putSerializable(PageArgumentKey.Bean, pot);
        UIService.getInstance().postPage(PageKey.PotDeviceMore, bd);
//        if(dialogType_potKP100==null){
//            dialogType_potKP100 =  DialogType_PotKP100.createDialogType_PotKP100(cx);
//        }
//        dialogType_potKP100.putFoodTips();
//        dialogType_potKP100.show();
    }
}
