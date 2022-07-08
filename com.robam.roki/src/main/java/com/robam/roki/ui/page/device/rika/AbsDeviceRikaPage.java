package com.robam.roki.ui.page.device.rika;

import static com.robam.common.pojos.device.rika.RikaStatus.SUBSET_OFF_COUNT;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.plat.pojos.device.SubView;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceTimeRemindEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModeName;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.BackgroundFuncAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.MainFuncAdapter;
import com.robam.roki.ui.adapter.OtherFuncAdapter;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by 14807 on 2018/1/23.
 * 集成烟机
 */

public class AbsDeviceRikaPage<Rika extends AbsRika> extends DeviceCatchFilePage {

    Rika rika;
    String mGuid;
    String mDeviceCategory;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_off_line_text)
    TextView mTvOffLineText;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_switch)
    ImageView mIvDeviceSwitch;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout mLayoutBg;
    @InjectView(R.id.rv_background_func)
    RecyclerView mBackgroundFuncRecyclerview;
    @InjectView(R.id.rv_main_func)
    GridView mGvMainFunc;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    private String mOilUrl;
    private String mTitle;
    private IRokiDialog mCloseDialog;
    private String mViewBackgroundImg;
    private OtherFuncAdapter mOtherFuncAdapter;
    private BackgroundFuncAdapter mBackgroundFuncAdapter;
    private MainFuncAdapter mMainFuncAdapter;
    private List<DeviceConfigurationFunctions> mMainFuncDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mBackgroundFuncDeviceConfigurationFunctions;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mOtherFuncAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            mTvDeviceModelName.setText(name);
        }
    }

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        LogUtils.i("20180417", " Connected:" + event.pojo.isConnected());
        if (rika == null || !Objects.equal(rika.getID(), event.pojo.getID()))
            return;
        rika = (Rika) event.pojo;
        //子设备离线累计计数 一体机
        if(rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_NOT){
            rika.steamOvenStatusOffTotal++;
        }else {
            rika.steamOvenStatusOffTotal=0;
        }
        //子设备离线累计计数 蒸箱
        if(rika.steamWorkStatus == RikaStatus.STEAM_NOT){
            rika.steamStatusOffTotal++;
        }else {
            rika.steamStatusOffTotal=0;
        }
        //子设备离线累计计数 消毒柜
        if(rika.sterilWorkStatus == RikaStatus.STERIL_NOT){
            rika.sterilStatusOffTotal++;
        }else {
            rika.sterilStatusOffTotal=0;
        }

        if (!rika.isConnected()) {
            mTvOffLineText.setVisibility(View.VISIBLE);
        } else {
            mTvOffLineText.setVisibility(View.GONE);
        }
        showOilDialog();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (rika == null || !Objects.equal(rika.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.device_new_connected);
            mTvOffLineText.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceCategory = bd == null ? null : bd.getString(PageArgumentKey.deviceCategory);
        rika = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_rika, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {
//        if (obj == null) return;
        try {

            if (mDevice instanceof AbsRika) {
                rika = (Rika) mDevice;
            }
            LogUtils.i("20180815", " mVersion:" + mVersion);
            if (!rika.isConnected()) {
                ToastUtils.showLong(R.string.device_new_connected);
                if (mTvOffLineText != null) {
                    mTvOffLineText.setVisibility(View.VISIBLE);
                }
            }
            ModelMap modelMap = obj.modelMap;
//            mTvDeviceModelName.setText(obj.title);
            mTvDeviceModelName.setText(rika.getName() == null ||  rika.getName().equals(rika.getCategoryName()) ? rika.getDispalyType() : rika.getName());
            mViewBackgroundImg = obj.viewBackgroundImg;
            LogUtils.i("20180815", " mViewBackgroundImg:" + mViewBackgroundImg);
            Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);
            //档位，炉头，模式
            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
            mBackgroundFuncDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
            mBackgroundFuncAdapter = new BackgroundFuncAdapter(cx, rika, mBackgroundFuncDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    //TODO
                }
            });

            mBackgroundFuncRecyclerview.setAdapter(mBackgroundFuncAdapter);
            LinearLayoutManager backgroundFuncLinerLayout = new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL
                    , false);
            mBackgroundFuncRecyclerview.setLayoutManager(backgroundFuncLinerLayout);
            mBackgroundFuncRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.HORIZONTAL_LIST));

            //主功能区
            OtherFunc otherFunc = modelMap.otherFunc;
            mDeviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
            if (mOtherFuncAdapter == null) {
                mOtherFuncAdapter = new OtherFuncAdapter(cx, rika, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        otherFuncItemEvent(view);
                    }
                });
            }
            mRecyclerview.setAdapter(mOtherFuncAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                    (cx, LinearLayoutManager.VERTICAL, false);
            mRecyclerview.setLayoutManager(linearLayoutManager);
            mRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));
            //油网区
            MainFunc mainFunc = modelMap.mainFunc;
            mMainFuncDeviceConfigurationFunctions = mainFunc.deviceConfigurationFunctions;
            if (mMainFuncDeviceConfigurationFunctions == null || mMainFuncDeviceConfigurationFunctions.size() == 0)
                return;
            for (int i = 0; i < mMainFuncDeviceConfigurationFunctions.size(); i++) {
                if ("oilNetworkDetection".equals(mMainFuncDeviceConfigurationFunctions.get(i).functionCode)) {
                    SubView subView = mMainFuncDeviceConfigurationFunctions.get(i).subView;
                    if (subView != null) {
                        title = subView.title;
                        List<DeviceConfigurationFunctions> subViewList = subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        if (subViewList == null || subViewList.size() == 0) return;
                        for (int j = 0; j < subViewList.size(); j++) {
                            mOilUrl = subViewList.get(j).subViewName;
                            mTitle = subViewList.get(j).functionName;
                        }
                    } else if ("clearOilCup".equals(mMainFuncDeviceConfigurationFunctions.get(i).functionCode)) {
                        SubView subView2 = mMainFuncDeviceConfigurationFunctions.get(i).subView;
                        if (subView2 != null) {
                            title = subView2.title;
                            List<DeviceConfigurationFunctions> subViewList = subView2
                                    .subViewModelMap
                                    .subViewModelMapSubView
                                    .deviceConfigurationFunctions;
                            if (subViewList == null || subViewList.size() == 0) return;
                            for (int j = 0; j < subViewList.size(); j++) {
                                mOilUrl = subViewList.get(j).subViewName;
                                mTitle = subViewList.get(j).functionName;
                            }
                        }
                    }
                }
                mMainFuncAdapter = new MainFuncAdapter(cx, rika, title, mMainFuncDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {

                        String tag = (String) view.getTag();
                        LogUtils.i("20201113", "tag:" + tag);
                        if ("oilNetworkDetection".equals(tag)) {
                            Bundle bd = new Bundle();
                            bd.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                            bd.putSerializable(PageArgumentKey.Bean, rika);
                            bd.putSerializable(PageArgumentKey.tag, "mainFunc");
                            bd.putSerializable(PageArgumentKey.time, rika.cleaningUseTime);
                            bd.putSerializable(PageArgumentKey.title, mTitle);
                            UIService.getInstance().postPage(PageKey.DeviceRikaOilDetection, bd);
                        } else if ("clearOilCup".equals(tag)) {
                            ToastUtils.showLong(R.string.dialog_oil_cleaning_text);
                        }
                    }
                });
                mGvMainFunc.setAdapter(mMainFuncAdapter);
            }
            mHandler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otherFuncItemEvent(View view) {
        String tag = view.getTag().toString();
        if (mDeviceConfigurationFunctions == null || mDeviceConfigurationFunctions.size() == 0)
            return;
        switch (tag) {
            case RikaModeName.SMOKE_AIR_VOLUME://烟机风量
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                Bundle bundleVolume = new Bundle();
                bundleVolume.putString(PageArgumentKey.Guid, mGuid);
                bundleVolume.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleVolume.putSerializable(PageArgumentKey.RIKA, rika);
                UIService.getInstance().postPage(PageKey.DeviceRikaFanAirVolume, bundleVolume);
                break;
            case RikaModeName.STEAMING_MODE://蒸模式
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.steamStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steam_invalid_error);
                    return;
                }
//                if (rika.steamWorkStatus == RikaStatus.STEAM_NOT) {
//                    ToastUtils.showShort(R.string.steam_invalid_error);
//                    return;
//                }
                DeviceConfigurationFunctions proFunctions = null;
                String steamTitle = null;
                for (DeviceConfigurationFunctions deviceConfigurationFunctions : mDeviceConfigurationFunctions) {
                    if (TextUtils.equals(deviceConfigurationFunctions.functionCode, tag)) {
                        proFunctions = deviceConfigurationFunctions;
                        steamTitle = deviceConfigurationFunctions.functionName;
                    }
                }
                if (proFunctions == null) {
                    return;
                }
                Bundle bundleSteamModel = new Bundle();
                bundleSteamModel.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleSteamModel.putSerializable(PageArgumentKey.code, tag);
                bundleSteamModel.putSerializable(PageArgumentKey.title, steamTitle);
                bundleSteamModel.putSerializable(PageArgumentKey.RIKA, rika);
                bundleSteamModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                if (rika.steamWorkStatus == RikaStatus.STEAM_RUN || rika.steamWorkStatus == RikaStatus.STEAM_STOP
                        || rika.steamWorkStatus == RikaStatus.STEAM_PREHEAT) {
                    bundleSteamModel.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaWork, bundleSteamModel);
                } else {
                    bundleSteamModel.putString(PageArgumentKey.tag, "select");
                    UIService.getInstance().postPage(PageKey.DeviceModelSelected, bundleSteamModel);
                }
                break;
            case RikaModeName.STEAMING_ROAST_MODE://蒸烤模式
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.steamOvenStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
                    return;
                }
//                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_NOT) {
//                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
//                    return;
//                }
                proFunctions = null;
                String steamRoastTitle = null;
                for (DeviceConfigurationFunctions deviceConfigurationFunctions : mDeviceConfigurationFunctions) {
                    if (TextUtils.equals(deviceConfigurationFunctions.functionCode, tag)) {
                        proFunctions = deviceConfigurationFunctions;
                        steamRoastTitle = deviceConfigurationFunctions.functionName;
                    }
                }
                if (proFunctions == null) {
                    return;
                }
                Bundle bundleSteamOvenModel = new Bundle();
                bundleSteamOvenModel.putString(PageArgumentKey.functionParams, proFunctions.functionParams);
                bundleSteamOvenModel.putSerializable(PageArgumentKey.RIKA, rika);
                bundleSteamOvenModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                bundleSteamOvenModel.putString(PageArgumentKey.title, steamRoastTitle);
                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
                        || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                    bundleSteamOvenModel.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaYWork, bundleSteamOvenModel);
                } else {
                    UIService.getInstance().postPage(PageKey.DeviceRikaModeSelect, bundleSteamOvenModel);
                }

//                bundleSteamOvenModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
//                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
//                        || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
//                    bundleSteamOvenModel.putString(PageArgumentKey.tag, "home");
//                    UIService.getInstance().postPage(PageKey.DeviceRikaSteamOvenWork, bundleSteamOvenModel);
//                } else {
//                    bundleSteamOvenModel.putString(PageArgumentKey.tag, "select");
//                    UIService.getInstance().postPage(PageKey.DeviceModelSelected, bundleSteamOvenModel);
//                }
                break;
            case RikaModeName.CLEANING_AND_DISINFECTION://保洁消毒
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.sterilStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steri_invalid_error);
                    return;
                }
//                if (rika.sterilWorkStatus == RikaStatus.STERIL_NOT) {
//                    ToastUtils.showShort(R.string.steri_invalid_error);
//                    return;
//                }

                if (rika.sterilWorkStatus == RikaStatus.STERIL_ALARM) {
                    ToastUtils.showShort(R.string.device_alarm_status);
                    return;
                }

               /* if (rika.sterilDoorLockStatus == 0 && rika.sterilWorkStatus == RikaStatus.STERIL_ON) {
                    ToastUtils.showShort(R.string.device_alarm_rika_E1);
                    return;
               }*/

                Bundle bundleSterilizerModel = new Bundle();
                bundleSterilizerModel.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleSterilizerModel.putSerializable(PageArgumentKey.RIKA, rika);
                bundleSterilizerModel.putSerializable(PageArgumentKey.code, tag);
                bundleSterilizerModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                if (rika.sterilWorkStatus == RikaStatus.STERIL_DISIDFECT || rika.sterilWorkStatus == RikaStatus.STERIL_CLEAN
                        || rika.sterilWorkStatus == RikaStatus.STERIL_DRYING || rika.sterilWorkStatus == RikaStatus.STERIL_PRE
                        || rika.sterilWorkStatus == RikaStatus.STERIL_DEGERMING || rika.sterilWorkStatus == RikaStatus.STERIL_INDUCTION_STERILIZATION
                        || rika.sterilWorkStatus == RikaStatus.STERIL_INTELLIGENT_DETECTION || rika.sterilWorkStatus == RikaStatus.STERIL_WARM_DISH
                        || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_DRYING
                        || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_CLEAN || rika.sterilWorkStatus == RikaStatus.STERIL_COER_DISIDFECT) {
                    bundleSterilizerModel.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaXWork, bundleSterilizerModel);
                } else {
                    bundleSterilizerModel.putString(PageArgumentKey.tag, "select");
                    UIService.getInstance().postPage(PageKey.DeviceModelSelected, bundleSterilizerModel);
                }
                break;
            case RikaModeName.CLEANING_AND_DISINFECTION_NEW:
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.steamOvenStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
                    return;
                }
//                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_NOT) {
//                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
//                    return;
//                }
//                if (rika.sterilWorkStatus == RikaStatus.STERIL_NOT) {
//                    ToastUtils.showShort(R.string.steri_invalid_error);
//                    return;
//                }
//
//                if (rika.sterilWorkStatus == RikaStatus.STERIL_ALARM) {
//                    ToastUtils.showShort(R.string.device_alarm_status);
//                    return;
//                }

               /* if (rika.sterilDoorLockStatus == 0 && rika.sterilWorkStatus == RikaStatus.STERIL_ON) {
                    ToastUtils.showShort(R.string.device_alarm_rika_E1);
                    return;
               }*/

                List<DeviceConfigurationFunctions> deviceConfigurationFunctions1 = null;
                String cleanTitle = null;
                for (DeviceConfigurationFunctions deviceConfigurationFunctions : mDeviceConfigurationFunctions) {
                    if (TextUtils.equals(deviceConfigurationFunctions.functionCode, tag)) {
                        deviceConfigurationFunctions1 = deviceConfigurationFunctions
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        cleanTitle = deviceConfigurationFunctions.functionName;
                    }
                }
                if (deviceConfigurationFunctions1 == null) {
                    return;
                }
                bundleSterilizerModel = new Bundle();
                bundleSterilizerModel.putSerializable(PageArgumentKey.List, (Serializable) deviceConfigurationFunctions1);
                bundleSterilizerModel.putSerializable(PageArgumentKey.RIKA, rika);
                bundleSterilizerModel.putString(PageArgumentKey.title, cleanTitle);
                bundleSterilizerModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
                        || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                    bundleSterilizerModel.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaYWork, bundleSterilizerModel);
                } else {
                    UIService.getInstance().postPage(PageKey.DeviceRikaMode2Select, bundleSterilizerModel);
                }
//                bundleSterilizerModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
//                if (rika.sterilWorkStatus == RikaStatus.STERIL_DISIDFECT || rika.sterilWorkStatus == RikaStatus.STERIL_CLEAN
//                        || rika.sterilWorkStatus == RikaStatus.STERIL_DRYING || rika.sterilWorkStatus == RikaStatus.STERIL_PRE
//                        || rika.sterilWorkStatus == RikaStatus.STERIL_DEGERMING || rika.sterilWorkStatus == RikaStatus.STERIL_INDUCTION_STERILIZATION
//                        || rika.sterilWorkStatus == RikaStatus.STERIL_INTELLIGENT_DETECTION || rika.sterilWorkStatus == RikaStatus.STERIL_WARM_DISH
//                        || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_DRYING
//                        || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_CLEAN || rika.sterilWorkStatus == RikaStatus.STERIL_COER_DISIDFECT) {
//                    bundleSterilizerModel.putString(PageArgumentKey.tag, "home");
//                    UIService.getInstance().postPage(PageKey.DeviceRikaXWork, bundleSterilizerModel);
//                } else {
//                    bundleSterilizerModel.putString(PageArgumentKey.tag, "select");
//                    UIService.getInstance().postPage(PageKey.DeviceModelSelected, bundleSterilizerModel);
//                }
                break;
            case "timeReminding"://计时提醒


                break;
            case "oilNetworkDetection"://油网
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                Bundle bundleOil = new Bundle();
                bundleOil.putString(PageArgumentKey.Guid, mGuid);
                bundleOil.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleOil.putSerializable(PageArgumentKey.Bean, rika);
                bundleOil.putSerializable(PageArgumentKey.tag, "other");
                bundleOil.putSerializable(PageArgumentKey.title, title);
                bundleOil.putSerializable(PageArgumentKey.time, rika.cleaningUseTime);
                UIService.getInstance().postPage(PageKey.DeviceRikaOilDetection, bundleOil);
                break;
            case "smokeCookerSteamingLinkage"://烟灶蒸联动
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }


                Bundle bundleLinkage = new Bundle();
                bundleLinkage.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLinkage.putSerializable(PageArgumentKey.RIKA, rika);
                UIService.getInstance().postPage(PageKey.DeviceRikaFanStoveLinkage, bundleLinkage);
                break;
            case "steamCookbook":
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                Bundle bundleSteamCookbook = new Bundle();
                bundleSteamCookbook.putString(PageArgumentKey.RecipeId, IDeviceType.RZQL);
                bundleSteamCookbook.putSerializable(PageArgumentKey.RIKA, rika);
                bundleSteamCookbook.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
//                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundleSteamCookbook);
                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
                        || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                    bundleSteamCookbook.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaYWork, bundleSteamCookbook);
                } else {
                    UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundleSteamCookbook);
                }
                break;

            case "smokeStoveEliminationLinkage"://烟灶消联动
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                Bundle bundleElimination = new Bundle();
                bundleElimination.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleElimination.putSerializable(PageArgumentKey.RIKA, rika);
                UIService.getInstance().postPage(PageKey.DeviceRikaFanStoveSterilizerLinkage, bundleElimination);
                break;

            case "appointmentDisinfection"://预约消毒
                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.sterilStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steri_invalid_error);
                    return;
                }
//                if (rika.sterilWorkStatus == RikaStatus.STERIL_NOT) {
//                    ToastUtils.showShort(R.string.steri_invalid_error);
//                    return;
//                }
                Bundle appointmentElimination = new Bundle();
                appointmentElimination.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                appointmentElimination.putSerializable(PageArgumentKey.RIKA, rika);
                appointmentElimination.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                if (rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_DRYING
                        || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_CLEAN) {
                    appointmentElimination.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaXWork, appointmentElimination);
                } else {
                    appointmentElimination.putString(PageArgumentKey.tag, "appointment");
                    UIService.getInstance().postPage(PageKey.DeviceRikaAppointmentElimination, appointmentElimination);
                }

                break;


            case "localCookbook"://本地自动菜谱

                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }
                if (rika.steamOvenStatusOffTotal >= SUBSET_OFF_COUNT) {
                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
                    return;
                }
//                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_NOT) {
//                    ToastUtils.showShort(R.string.steam_oven_invalid_error);
//                    return;
//                }
                Bundle bundleLocalRecipe = new Bundle();
                bundleLocalRecipe.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLocalRecipe.putSerializable(PageArgumentKey.RIKA, rika);
                bundleLocalRecipe.putString(PageArgumentKey.Guid, mGuid);
                bundleLocalRecipe.putSerializable(PageArgumentKey.title, "本地自动菜谱");
                bundleLocalRecipe.putSerializable(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
//                UIService.getInstance().postPage(PageKey.AbsRikaLocalRecipe, bundleLocalRecipe);
                if (rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
                        || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                    bundleLocalRecipe.putString(PageArgumentKey.tag, "home");
                    UIService.getInstance().postPage(PageKey.DeviceRikaYWork, bundleLocalRecipe);
                } else {
                    UIService.getInstance().postPage(PageKey.AbsRikaLocalRecipe, bundleLocalRecipe);
                }
                break;
            case RikaModeName.SMOKE_COOKER_STEAMING_ROAST_LINKAGE://烟灶蒸烤联动

                if (!rika.isConnected()) {
                    DeviceOfflinePrompt();
                    return;
                }

                Bundle bundleLinkageSteamOven = new Bundle();
                bundleLinkageSteamOven.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLinkageSteamOven.putSerializable(PageArgumentKey.RIKA, rika);
                UIService.getInstance().postPage(PageKey.DeviceRikaFanSteamOvenLinkage, bundleLinkageSteamOven);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isCountdown = PreferenceUtils.getBool("isCountdown", false);
        if (!isCountdown) {
            EventUtils.postEvent(new DeviceTimeRemindEvent());
        }
        if (rika == null) {
            return;
        }
        if (rika.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), rika.getDt(), null);
        }
    }

    private void showOilDialog() {
        RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_06);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_switch)
    public void onMIvDeviceSwitchClicked() {
        if (rika == null) return;
        if (!rika.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        if (rika != null) {
            ToolUtils.logEvent(rika.getDt(), "关机", "roki_设备");
        }

        if (rika.rikaFanWorkStatus == RikaStatus.FAN_ON
                || rika.steamWorkStatus == RikaStatus.STEAM_PREHEAT
                || rika.steamWorkStatus == RikaStatus.STEAM_PRE
                || rika.steamWorkStatus == RikaStatus.STEAM_STOP
                || rika.steamWorkStatus == RikaStatus.STEAM_RUN
                || rika.sterilWorkStatus == RikaStatus.STERIL_DISIDFECT
                || rika.sterilWorkStatus == RikaStatus.STERIL_CLEAN
                || rika.sterilWorkStatus == RikaStatus.STERIL_PRE
                || rika.sterilWorkStatus == RikaStatus.STERIL_DEGERMING
                || rika.sterilWorkStatus == RikaStatus.STERIL_INTELLIGENT_DETECTION
                || rika.sterilWorkStatus == RikaStatus.STERIL_INDUCTION_STERILIZATION
                || rika.sterilWorkStatus == RikaStatus.STERIL_WARM_DISH
                || rika.sterilWorkStatus == RikaStatus.STERIL_DRYING
                || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION
                || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_CLEAN
                || rika.sterilWorkStatus == RikaStatus.STERIL_APPOINATION_DRYING
                || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_ON
                || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP
                || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN
                || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT
                || rika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_ORDER) {
            mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            mCloseDialog.setTitleText(R.string.rika_off_work);
            String dp = rika.getDp();
            LogUtils.i("20180712", " dp:" + dp);
            if (IRokiFamily.RIKAZ.equals(rika.getDp())) {
                mCloseDialog.setContentText(R.string.rika_off_work_steam_desc);
            } else if (IRokiFamily.RIKAX.equals(rika.getDp())) {
                mCloseDialog.setContentText(R.string.rika_off_work_sterilizer_desc);
            } else if (IRokiFamily.RIKAY.equals(rika.getDp())) {
                mCloseDialog.setContentText(R.string.rika_off_work_steam_oven_one_desc);
            }
            mCloseDialog.show();
            mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCloseDialog != null && mCloseDialog.isShow()) {
                                mCloseDialog.dismiss();
                            }

                            if (rika.rikaFanWorkStatus == RikaStatus.FAN_ON) {
                                rika.setFanSwitchStatus((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 49, (short) 1,
                                        RikaStatus.FAN_OFF, new VoidCallback() {
                                            @Override
                                            public void onSuccess() {
                                                closeSteamAndSterilizer();
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                            }
                                        });
                            } else {
                                closeSteamAndSterilizer();
                            }
                        }

                    }
            );


            mCloseDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }

            );

        } else {
            ToastUtils.showShort(R.string.device_close_text);
        }

    }

    //关闭烟机成功后关闭蒸汽炉或者消毒柜
    private void closeSteamAndSterilizer() {

        if (IRokiFamily.RIKAZ.equals(rika.getDp())) {
            rika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                    (short) 49, (short) 1, RikaStatus.STEAM_OFF, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort(R.string.device_close_text);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });

        } else if (IRokiFamily.RIKAX.equals(rika.getDp())) {
            short n = 0;
            rika.setSterilizerWorkStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                    RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 49,
                    (short) 6, RikaStatus.STERIL_OFF, n, n, n, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort(R.string.device_close_text);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    }

            );
        } else if (IRokiFamily.RIKAY.equals(rika.getDp())) {
            short n = 0;
            rika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                    (short) 49, (short) 1, RikaStatus.STEAMOVEN_OFF, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort(R.string.device_close_text);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
        }
    }

    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, rika.getID());
        bd.putString(PageArgumentKey.Url, mOilUrl);
        bd.putString(PageArgumentKey.title, mTitle);
        UIService.getInstance().postPage(PageKey.RikaDeviceMore, bd);
    }

    private void DeviceOfflinePrompt() {
        ToastUtils.showShort(R.string.device_connected);
    }


}
