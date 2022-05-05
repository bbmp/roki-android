package com.robam.roki.ui.page.device.dishWasher;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.PayloadBean;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.OvenCommonAdapter;
import com.robam.roki.ui.page.device.oven.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsDishWasherFirstView extends FrameLayout {
    @InjectView(R.id.dish_washer_offline_txt)
    TextView dishWasherOfflineTxt;
    @InjectView(R.id.dish_washer_func)
    MyGridView dishWasherFunc;
    @InjectView(R.id.dish_washer_func_show)
    RecyclerView dishWasherFuncShow;


    @InjectView(R.id.tv_show_data_top)
    TextView tvShowDataTop;
    @InjectView(R.id.tv_show_data_bottom)
    TextView tvShowDataBottom;
    @InjectView(R.id.ll_show_data_top)
    LinearLayout llShowDataTop;


    @InjectView(R.id.ll_water)
    LinearLayout llWater;
    @InjectView(R.id.iv_water)
    ImageView ivWater;
    @InjectView(R.id.tv_water)
    TextView tvWater;
    @InjectView(R.id.ll_salt)
    LinearLayout llSalt;
    @InjectView(R.id.iv_salt)
    ImageView ivSalt;
    @InjectView(R.id.tv_salt)
    TextView tvSalt;
    @InjectView(R.id.ll_rinse)
    LinearLayout llRinse;
    @InjectView(R.id.iv_rinse)
    ImageView ivRinse;
    @InjectView(R.id.tv_rinse)
    TextView tvRinse;


    Context cx;
    private List<DeviceConfigurationFunctions> mainList;
    private List<DeviceConfigurationFunctions> otherList;
    private List<DeviceConfigurationFunctions> bgFunList;
    AbsDishWasher washer;
    OvenCommonAdapter ovenCommonAdapter;
    private List<DeviceConfigurationFunctions> backgroundList;
    private Reponses.GetHistoryDataResponse historyDataResponse;
    String wxappletParams;


    public AbsDishWasherFirstView(@NonNull Context context) {
        super(context);
        this.cx = context;
        initView();
    }

    public AbsDishWasherFirstView(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList
            , List<DeviceConfigurationFunctions> bgFunList, AbsDishWasher washer) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.bgFunList = bgFunList;
        this.washer = washer;
        initView();
    }

    public AbsDishWasherFirstView(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList
            , List<DeviceConfigurationFunctions> bgFunList, AbsDishWasher washer, Reponses.GetHistoryDataResponse historyDataResponse) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.bgFunList = bgFunList;
        this.washer = washer;
        this.historyDataResponse = historyDataResponse;
        initView();
    }

    public AbsDishWasherFirstView(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList
            , List<DeviceConfigurationFunctions> bgFunList, AbsDishWasher washer, Reponses.GetHistoryDataResponse historyDataResponse, String wxappletParams) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.bgFunList = bgFunList;
        this.washer = washer;
        this.historyDataResponse = historyDataResponse;
        this.wxappletParams = wxappletParams;
        EventUtils.regist(this);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.dish_washer_first_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        ovenCommonAdapter = new OvenCommonAdapter(cx, mainList, otherList, washer);
        dishWasherFuncShow.setAdapter(ovenCommonAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(cx, 1);
        dishWasherFuncShow.setLayoutManager(layoutManager);
        ovenCommonAdapter.setGridViewOnclickLister(new OvenCommonAdapter.GridViewOnclick() {
            @Override
            public void onGridClick(String pos) {
                if (onClickMainListener != null) {
                    onClickMainListener.onClickMain(pos);
                }
            }
        });
        ovenCommonAdapter.setItemViewOnclickLister(new OvenCommonAdapter.ItemViewOnclick() {
            @Override
            public void onItemClick(String pos) {
                if (onClickMainListener != null) {
                    onClickMainListener.onClickOther(pos);
                }
            }
        });


        showBacData();

    }

    @OnClick({R.id.ll_salt, R.id.ll_rinse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_salt:
                for (int i = 0; i < backgroundList.size(); i++) {
                    if ((backgroundList.get(i).functionCode).equals(DishWasherName.softSaltWater)) {
                        String title = backgroundList.get(i).functionName;
                        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = backgroundList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0)
                            return;
                        String params = null;
                        String url = null;
                        for (int j = 0; j < deviceConfigurationFunctions.size(); j++) {
                            if ("descText".equals(deviceConfigurationFunctions.get(j).functionCode)) {
                                params = deviceConfigurationFunctions.get(j).functionParams;
                            }
                            if ("buyLink".equals(deviceConfigurationFunctions.get(j).functionCode)) {
                                url = deviceConfigurationFunctions.get(j).functionParams;
                            }
                        }
                        Bundle bd = new Bundle();
                        bd.putSerializable(PageArgumentKey.Bean, washer);
                        bd.putSerializable(PageArgumentKey.Url, url);
                        bd.putSerializable(PageArgumentKey.title, title);
                        bd.putSerializable(PageArgumentKey.text, params);
                        bd.putSerializable(PageArgumentKey.tag, "2");
                        bd.putSerializable(PageArgumentKey.wxparams, wxappletParams);
                        UIService.getInstance().postPage(PageKey.SpecialState, bd);
                    }
                }

                break;
            case R.id.ll_rinse:
                for (int i = 0; i < backgroundList.size(); i++) {
                    if ((backgroundList.get(i).functionCode).equals(DishWasherName.bleaching)) {
                        String title = backgroundList.get(i).functionName;
                        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = backgroundList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0)
                            return;
                        String params = null;
                        String url = null;
                        for (int j = 0; j < deviceConfigurationFunctions.size(); j++) {
                            if ("descText".equals(deviceConfigurationFunctions.get(j).functionCode)) {
                                params = deviceConfigurationFunctions.get(j).functionParams;
                            }
                            if ("buyLink".equals(deviceConfigurationFunctions.get(j).functionCode)) {
                                url = deviceConfigurationFunctions.get(j).functionParams;
                            }
                        }
                        Bundle bd = new Bundle();
                        bd.putSerializable(PageArgumentKey.Bean, washer);
                        bd.putSerializable(PageArgumentKey.Url, url);
                        bd.putSerializable(PageArgumentKey.title, title);
                        bd.putSerializable(PageArgumentKey.text, params);
                        bd.putSerializable(PageArgumentKey.tag, "3");
                        bd.putSerializable(PageArgumentKey.wxparams, wxappletParams);
                        UIService.getInstance().postPage(PageKey.SpecialState, bd);

                    }

                }


                break;
        }

    }

    private void showBacData() {
        if (backgroundList != null) {
            backgroundList.clear();
        }
        backgroundList = new ArrayList<>();
        llRinse.setVisibility(INVISIBLE);
        llSalt.setVisibility(INVISIBLE);
        llWater.setVisibility(INVISIBLE);

        for (int i = 0; i < bgFunList.size(); i++) {
            //缺水
            if (bgFunList.get(i).functionCode.equals(DishWasherName.hydropenia)) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = bgFunList.get(i);
                Glide.with(cx).load(deviceConfigurationFunctions.backgroundImg).into(ivWater);
                tvWater.setText(deviceConfigurationFunctions.functionName);
            }

        }

        for (int i = 0; i < bgFunList.size(); i++) {
            if (bgFunList.get(i).functionCode.equals(DishWasherName.statistics)) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = bgFunList.get(i);
                backgroundList.add(deviceConfigurationFunctions);
            }

        }
        //水软盐不足
        for (int i = 0; i < bgFunList.size(); i++) {
            if (bgFunList.get(i).functionCode.equals(DishWasherName.softSaltWater)) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = bgFunList.get(i);
                backgroundList.add(deviceConfigurationFunctions);
                Glide.with(cx).load(deviceConfigurationFunctions.backgroundImg).into(ivSalt);
                tvSalt.setText(deviceConfigurationFunctions.functionName);
            }
        }
        //漂白剂不足
        for (int i = 0; i < bgFunList.size(); i++) {
            if (bgFunList.get(i).functionCode.equals(DishWasherName.bleaching)) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = bgFunList.get(i);
                backgroundList.add(deviceConfigurationFunctions);
                Glide.with(cx).load(deviceConfigurationFunctions.backgroundImg).into(ivRinse);
                tvRinse.setText(deviceConfigurationFunctions.functionName);

            }
        }


        if (historyDataResponse == null) {
            return;
        }
        PayloadBean payloadBean = historyDataResponse.payload;
        if (payloadBean != null) {
            double totalWaterSaved = payloadBean.totalWaterSaved;
            double lastWaterCost = payloadBean.lastWaterCost;
            double lastPowerCost = payloadBean.lastPowerCost;
            for (int i = 0; i < backgroundList.size(); i++) {
                if (backgroundList.get(i).functionCode.equals(DishWasherName.statistics)) {
                    String params = backgroundList.get(i).functionParams;
                    String[] split = params.split("<br>");
                    String s1 = split[0];
                    String s2 = split[1];
                    String[] buttons1 = s1.split("Button");
                    String[] buttons2 = s2.split("Button");

                    String button0 = buttons1[0];//上次工作耗水
                    String button1 = buttons1[1];//L，耗电
                    String button2 = buttons1[2];//kW•h。

                    String button3 = buttons2[0];//洗碗机已累计为您省水大约
                    String button4 = buttons2[1];//L
                    tvShowDataTop.setText(String.format("%s%s%s%s%s", button0, lastWaterCost, button1, lastPowerCost, button2));
                    tvShowDataBottom.setText(String.format("%s%s%s", button3, totalWaterSaved, button4));

                }
            }
        } else {
            llShowDataTop.setVisibility(INVISIBLE);

        }


    }

    public void setUpData(List<DeviceConfigurationFunctions> moreList) {
        ovenCommonAdapter.upMoreView(moreList);

    }

    public void removeMoreView() {
        ovenCommonAdapter.removeMoreView();
    }

    public interface OnClickMain {
        void onClickMain(String str);

        void onClickOther(String str);
    }


    public void disConnect(boolean isCon) {
        if (isCon) {
            dishWasherOfflineTxt.setVisibility(View.VISIBLE);
            ovenCommonAdapter.setDisCon(true);
            llShowDataTop.setVisibility(INVISIBLE);

        } else {
            dishWasherOfflineTxt.setVisibility(View.INVISIBLE);
            ovenCommonAdapter.setDisCon(false);
            llShowDataTop.setVisibility(VISIBLE);
        }
    }

    public OnClickMain onClickMainListener;

    public void setOnClickMainListener(OnClickMain onClickMainListener) {
        this.onClickMainListener = onClickMainListener;
    }

    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (washer == null || !Objects.equal(washer.getID(), event.pojo.getID()))
            return;
        short LackSaltStatus = washer.LackSaltStatus;//缺盐状态
        short LackRinseStatus = washer.LackRinseStatus;//缺漂洗剂状态
        short AbnormalAlarmStatus = washer.AbnormalAlarmStatus;//进水异常
        if (LackSaltStatus == 1) {
            LogUtils.i("202007020123", "缺盐");
            llSalt.setVisibility(VISIBLE);
        } else {
            llSalt.setVisibility(INVISIBLE);
        }
        if (LackRinseStatus == 1) {
            LogUtils.i("202007020123", "缺漂洗剂");
            llRinse.setVisibility(VISIBLE);
        } else {
            llRinse.setVisibility(INVISIBLE);
        }
        if (AbnormalAlarmStatus == 1) {
            LogUtils.i("202007020123", "进水异常");
            llWater.setVisibility(VISIBLE);
        } else {
            llWater.setVisibility(INVISIBLE);
        }


    }

}
