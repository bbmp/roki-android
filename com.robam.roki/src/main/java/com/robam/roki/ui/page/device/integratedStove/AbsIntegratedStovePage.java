package com.robam.roki.ui.page.device.integratedStove;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.CallAudioState;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.robam.base.BaseDialog;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.plat.pojos.device.SubView;
import com.legent.plat.pojos.device.SubViewModelMap;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceTimeRemindEvent;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.marshal.IntegratedStoveMsgMar;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveModel;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModeName;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.device.rika.CookBookTag;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.BackgroundFunc2Adapter;
import com.robam.roki.ui.adapter.BackgroundFuncAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.MainFunc2Adapter;
import com.robam.roki.ui.adapter.MainFuncAdapter;
import com.robam.roki.ui.adapter.OtherFunc2Adapter;
import com.robam.roki.ui.adapter.OtherFuncAdapter;
import com.robam.roki.ui.mdialog.MessageDialog;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by 14807 on 2018/1/23.
 * 集成灶
 */

public class AbsIntegratedStovePage<Integrated extends AbsIntegratedStove> extends DeviceCatchFilePage {

    Integrated integrated;
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
    private OtherFunc2Adapter mOtherFuncAdapter;
    private BackgroundFunc2Adapter mBackgroundFuncAdapter;
    private MainFunc2Adapter mMainFuncAdapter;
    private List<DeviceConfigurationFunctions> mMainFuncDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mBackgroundFuncDeviceConfigurationFunctions;
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    mOtherFuncAdapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    };

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            mTvDeviceModelName.setText(name);
        }
    }

    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (integrated.getID().equals(event.pojo.getID())){
            integrated = (Integrated) event.pojo ;
            if (!integrated.isConnected()) {
                mTvOffLineText.setVisibility(View.VISIBLE);
            } else {
                mTvOffLineText.setVisibility(View.GONE);
            }
            if (mBackgroundFuncAdapter != null){
                mBackgroundFuncAdapter.replaceIntegratedStove(integrated);
                mOtherFuncAdapter.replceData(event);
            }
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (integrated == null || !Objects.equal(integrated.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected ) {
            ToastUtils.showLong(R.string.device_new_connected);
            mTvOffLineText.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceCategory = bd == null ? null : bd.getString(PageArgumentKey.deviceCategory);
        integrated = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_integrated, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {
        try {

            if (mDevice instanceof AbsIntegratedStove) {
                integrated = (Integrated) mDevice;
            }
            LogUtils.i("20180815", " mVersion:" + mVersion);
            if (!integrated.isConnected()) {
                ToastUtils.showLong(R.string.device_new_connected);
                if (mTvOffLineText != null) {
                    mTvOffLineText.setVisibility(View.VISIBLE);
                }
            }
            ModelMap modelMap = obj.modelMap;
//            mTvDeviceModelName.setText(obj.title);
            mTvDeviceModelName.setText(integrated.getName() == null ||  integrated.getName().equals(integrated.getCategoryName()) ? integrated.getDispalyType() : integrated.getName());
            mViewBackgroundImg = obj.viewBackgroundImg;
            LogUtils.i("20180815", " mViewBackgroundImg:" + mViewBackgroundImg);
            Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);
            //档位，炉头，模式
            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
            mBackgroundFuncDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
            mBackgroundFuncAdapter = new BackgroundFunc2Adapter(cx, integrated, mBackgroundFuncDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    //TODO
                }
            });

            mBackgroundFuncRecyclerview.setAdapter(mBackgroundFuncAdapter);
//            LinearLayoutManager backgroundFuncLinerLayout = new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL
//                    , false);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, 4);
            mBackgroundFuncRecyclerview.setLayoutManager(gridLayoutManager);
//            mBackgroundFuncRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.HORIZONTAL_LIST));

            //主功能区
            OtherFunc otherFunc = modelMap.otherFunc;
            mDeviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
            if (mOtherFuncAdapter == null) {
                mOtherFuncAdapter = new OtherFunc2Adapter(cx, integrated, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
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
//            MainFunc mainFunc = modelMap.mainFunc;
//            mMainFuncDeviceConfigurationFunctions = mainFunc.deviceConfigurationFunctions;
//            if (mMainFuncDeviceConfigurationFunctions == null || mMainFuncDeviceConfigurationFunctions.size() == 0) {
//                return;
//            }
//            for (int i = 0; i < mMainFuncDeviceConfigurationFunctions.size(); i++) {
//                if ("oilNetworkDetection".equals(mMainFuncDeviceConfigurationFunctions.get(i).functionCode)) {
//                    SubView subView = mMainFuncDeviceConfigurationFunctions.get(i).subView;
//                    if (subView != null) {
//                        title = subView.title;
//                        List<DeviceConfigurationFunctions> subViewList = subView
//                                .subViewModelMap
//                                .subViewModelMapSubView
//                                .deviceConfigurationFunctions;
//                        if (subViewList == null || subViewList.size() == 0) {
//                            return;
//                        }
//                        for (int j = 0; j < subViewList.size(); j++) {
//                            mOilUrl = subViewList.get(j).subViewName;
//                            mTitle = subViewList.get(j).functionName;
//                        }
//                    } else if ("clearOilCup".equals(mMainFuncDeviceConfigurationFunctions.get(i).functionCode)) {
//                        SubView subView2 = mMainFuncDeviceConfigurationFunctions.get(i).subView;
//                        if (subView2 != null) {
//                            title = subView2.title;
//                            List<DeviceConfigurationFunctions> subViewList = subView2
//                                    .subViewModelMap
//                                    .subViewModelMapSubView
//                                    .deviceConfigurationFunctions;
//                            if (subViewList == null || subViewList.size() == 0) {
//                                return;
//                            }
//                            for (int j = 0; j < subViewList.size(); j++) {
//                                mOilUrl = subViewList.get(j).subViewName;
//                                mTitle = subViewList.get(j).functionName;
//                            }
//                        }
//                    }
//                }
//                mMainFuncAdapter = new MainFunc2Adapter(cx, integrated, title, mMainFuncDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
//                    @Override
//                    public void onItemClick(View view) {
//
//                        String tag = (String) view.getTag();
//                        LogUtils.i("20201113", "tag:" + tag);
//                        if ("oilNetworkDetection".equals(tag)) {
//                            Bundle bd = new Bundle();
//                            bd.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
//                            bd.putSerializable(PageArgumentKey.Bean, integrated);
//                            bd.putSerializable(PageArgumentKey.tag, "mainFunc");
//                            bd.putSerializable(PageArgumentKey.time, integrated.cleaningUseTime);
//                            bd.putSerializable(PageArgumentKey.title, mTitle);
//                            UIService.getInstance().postPage(PageKey.DeviceRikaOilDetection, bd);
//                        } else if ("clearOilCup".equals(tag)) {
//                            ToastUtils.showLong(R.string.dialog_oil_cleaning_text);
//                        }
//                    }
//                });
//                mGvMainFunc.setAdapter(mMainFuncAdapter);
//            }
//            mHandler.sendEmptyMessage(1);
            //生成本地菜谱id对应菜名map
            List<DeviceConfigurationFunctions> recipeData = null;
            for (DeviceConfigurationFunctions bean:mDeviceConfigurationFunctions
            ) {
                if (IntegStoveModel.LOCAL_COOKBOOK.equals(bean.functionCode)) {
                    recipeData = bean.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }
            Map<String, String> cookMap = new HashMap<>();
            Map<String, String> cookNeedWaterMap = new HashMap<>();
            if (recipeData!= null && recipeData.size() !=0) {
                for (DeviceConfigurationFunctions recipeDatum : recipeData) {
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = recipeDatum.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    for (DeviceConfigurationFunctions deviceConfigurationFunction : deviceConfigurationFunctions) {
                        CookBookTag cookBookTag = new CookBookTag();
                        cookBookTag.setId(deviceConfigurationFunction.id);
                        cookBookTag.setFunctionCode(deviceConfigurationFunction.functionCode);
                        cookBookTag.setFunctionName(deviceConfigurationFunction.functionName);
                        cookBookTag.setDeviceCategory(deviceConfigurationFunction.deviceCategory);
                        cookBookTag.setDeviceType(deviceConfigurationFunction.deviceType);
                        cookBookTag.setBackgroundImg(deviceConfigurationFunction.backgroundImg);
                        cookBookTag.setFunctionParams(deviceConfigurationFunction.functionParams);
                        try {
                            JSONObject jsonObject = new JSONObject(deviceConfigurationFunction.functionParams);
                            String mode = jsonObject.getJSONObject("model").getString("value");
                            cookMap.put(mode ,deviceConfigurationFunction.functionName);
                            String needWater = jsonObject.has("needWater") ?jsonObject.getString("needWater") : "0";
                            cookNeedWaterMap.put(mode ,needWater);
                        }catch (JSONException e){

                        }

                    }
                }
            }
            integrated.setCookMap(cookMap);
            integrated.setCookNeedWaterMap(cookNeedWaterMap);
           //获取工作时候子页面界面样式
           if (modelMap.hideFunc.deviceConfigurationFunctions != null){
               for (DeviceConfigurationFunctions deviceConfigurationFunction : modelMap.hideFunc.deviceConfigurationFunctions) {
                   if ( IntegStoveModel.RUN_TIME_DOWN_VIEW.equals(deviceConfigurationFunction.functionCode)){
                       List<DeviceConfigurationFunctions> deviceConfigurationFunctions = deviceConfigurationFunction.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                       if (deviceConfigurationFunctions != null){
                           for (DeviceConfigurationFunctions configurationFunction : deviceConfigurationFunctions) {
                               switch (configurationFunction.functionCode){
                                   case IntegStoveModel.WORKING_PAUSE:
                                       integrated.workingPauseUrl = configurationFunction.backgroundImg ;
                                       integrated.workingPauseHUrl = configurationFunction.backgroundImgH ;
                                       break;
                                   case IntegStoveModel.FINISH:
                                       integrated.finishUrl =  configurationFunction.backgroundImg ;
                                       break;
                                   case IntegStoveModel.WORKING_ADD_STEAM:
                                       integrated.workingAddSteamUrl =  configurationFunction.backgroundImg ;
                                       break;
                               }
                           }
                       }
                   }
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otherFuncItemEvent(View view) {
        String tag = view.getTag().toString();
        if (mDeviceConfigurationFunctions == null || mDeviceConfigurationFunctions.size() == 0) {
            return;
        }
        if (!integrated.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        switch (tag) {
            //烟机风量
            case IntegStoveModel.SMOKE_AIR_VOLUME:
                Bundle bundleVolume = new Bundle();
                bundleVolume.putString(PageArgumentKey.Guid, mGuid);
                bundleVolume.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleVolume.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                UIService.getInstance().postPage(PageKey.IntegratedStoveAirVolumePage, bundleVolume);
                break;
            //蒸烤模式
            case IntegStoveModel.STEAMING_ROAST_MODE:

                List<DeviceConfigurationFunctions> zkModeFunctions = null;
                String titleZk = null;
                Bundle bdZk = new Bundle();
                bdZk.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
                    if (RikaModeName.STEAMING_ROAST_MODE.equals(mDeviceConfigurationFunctions.get(i).functionCode)) {
                        zkModeFunctions = mDeviceConfigurationFunctions.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleZk = mDeviceConfigurationFunctions.get(i).subView.title;
                    }
                }
                Bundle bdZkMode = new Bundle();
                bdZkMode.putString(PageArgumentKey.Guid, mGuid);
                bdZkMode.putSerializable(PageArgumentKey.List, (Serializable) zkModeFunctions);
                bdZkMode.putString(PageArgumentKey.title, titleZk);
                bdZkMode.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                bdZkMode.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                //空闲和工作完成 可以进入选择界面
                if(!SteamOvenHelper.isWork(integrated.workState)){
                    UIService.getInstance().postPage(PageKey.IntegratedStoveModelSelected, bdZkMode);
                }else {
                    bdZkMode.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                    UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, bdZkMode);
                }

                break;
            case IntegStoveModel.SMOKE_COOKER_STEAMING_LINKAGE://烟灶蒸联动

                Bundle bundleLinkage = new Bundle();
                bundleLinkage.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLinkage.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                UIService.getInstance().postPage(PageKey.DeviceIntegStoveLinkagePage, bundleLinkage);
                break;

            case IntegStoveModel.LOCAL_COOKBOOK://本地自动菜谱
                Bundle bundleLocalRecipe = new Bundle();
                bundleLocalRecipe.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLocalRecipe.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                bundleLocalRecipe.putString(PageArgumentKey.Guid, mGuid);
                bundleLocalRecipe.putSerializable(PageArgumentKey.title, "本地自动菜谱");
                bundleLocalRecipe.putSerializable(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                List<DeviceConfigurationFunctions> recipeData = null;

                //空闲和工作完成 可以进入选择界面
                if(!SteamOvenHelper.isWork(integrated.workState)){
                    UIService.getInstance().postPage(PageKey.AbsIntegratedStoveLocalRecipePage, bundleLocalRecipe);
                }else {
                    UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, bundleLocalRecipe);
                }

                break;
            //烟灶蒸烤联动
            case IntegStoveModel.SMOKE_COOKER_STEAMING_ROAST_LINKAGE:
                Bundle bundleLinkageSteamOven = new Bundle();
                bundleLinkageSteamOven.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                bundleLinkageSteamOven.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                UIService.getInstance().postPage(PageKey.DeviceIntegStoveLinkagePage, bundleLinkageSteamOven);
                break;
                //辅助模式
            case IntegStoveModel.MISCELLANOUS:
                List<DeviceConfigurationFunctions> fzModeFunctions = null;
                String titleFz = null;
                Bundle bdFz = new Bundle();
                bdFz.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
                    if (RikaModeName.MISCELLANOUS.equals(mDeviceConfigurationFunctions.get(i).functionCode)) {
                        fzModeFunctions = mDeviceConfigurationFunctions.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleFz = mDeviceConfigurationFunctions.get(i).subView.title;
                    }
                }
                Bundle bdFzMode = new Bundle();
                bdFzMode.putString(PageArgumentKey.Guid, mGuid);
                bdFzMode.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                bdFzMode.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                bdFzMode.putSerializable(PageArgumentKey.List, (Serializable) fzModeFunctions);
                bdFzMode.putString(PageArgumentKey.title, titleFz);
                //空闲和工作完成 可以进入选择界面
                if(!SteamOvenHelper.isWork(integrated.workState)){
                    UIService.getInstance().postPage(PageKey.IntegratedStoveModelSelected, bdFzMode);
                }else {
                    bdFzMode.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                    UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, bdFzMode);
                }
                break;
            //多段模式
            case IntegStoveModel.MULTI_STEP_MODEL:
                List<DeviceConfigurationFunctions> multiStepModelList = null;
                String multiStepModelTitle = null;
                for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
                    if (mDeviceConfigurationFunctions.get(i).functionCode.equals(RikaModeName.MULTI_STEP_MODEL)) {
                        multiStepModelList = mDeviceConfigurationFunctions.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        multiStepModelTitle = mDeviceConfigurationFunctions.get(i).functionName;
                    }
                }
                Bundle bdMultiStepModel = new Bundle();
                bdMultiStepModel.putString(PageArgumentKey.Guid, mGuid);
                bdMultiStepModel.putSerializable(PageArgumentKey.List, (Serializable) multiStepModelList);
                bdMultiStepModel.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                bdMultiStepModel.putSerializable(PageArgumentKey.INTEGRATED_STOVE, integrated);
                bdMultiStepModel.putString(PageArgumentKey.title, multiStepModelTitle);
                List<DeviceConfigurationFunctions> multModes = new ArrayList<>();
                for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
                    if (IntegStoveModel.STEAMING_ROAST_MODE.equals(mDeviceConfigurationFunctions.get(i).functionCode) ) {
                        multModes.addAll(mDeviceConfigurationFunctions.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions);
                    }
                }
                bdMultiStepModel.putSerializable(PageArgumentKey.List, (Serializable) multModes);
                if(!SteamOvenHelper.isWork(integrated.workState)){
                    UIService.getInstance().postPage(PageKey.MultiRecipePage, bdMultiStepModel);
                }else {
                    bdMultiStepModel.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                    UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, bdMultiStepModel);
                }

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
        if (integrated == null) {
            return;
        }
        if (integrated.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), integrated.getDt(), null);
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
        if (integrated == null) {
            return;
        }
        if (!integrated.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        if (integrated != null) {
            ToolUtils.logEvent(integrated.getDt(), "关机", "roki_设备");
        }
        if (integrated.fan_powerState == IntegStoveStatus.powerState_lock_clean){
            ToastUtils.showShort("烟机清洗锁定中,无法关闭烟机");
            return;
        }
        if (integrated.fan_powerState != IntegStoveStatus.powerState_off
                || integrated.powerState != IntegStoveStatus.powerState_off
               ) {
            new MessageDialog.Builder(getActivity())
                    // 标题可以不用填写
                    .setTitle("关闭集成灶")
                    // 内容必须要填写
                    .setMessage("确定关闭集成灶烟机和一体机部分？灶具无法远程关火，请至厨房自行关闭。")
                    // 确定按钮文本
                    .setConfirm("确定")
                    // 设置 null 表示不显示取消按钮
                    .setCancel("取消")
                    // 设置点击按钮后不关闭对话框
                    //.setAutoDismiss(false)
                    .setListener(new MessageDialog.OnListener() {

                        @Override
                        public void onConfirm(BaseDialog dialog) {

                            //先关闭一体机
                            integrated.setSteamWorkStatus(IntegStoveStatus.powerCtrl_off, (short) 2, new VoidCallback() {
                                @Override
                                public void onSuccess() {
//                                UIService.getInstance().postPage(PageKey.AbsRikaDevice);
//                                    UIService.getInstance().popBack();
                                    //关闭烟机
                                    if (integrated.fan_powerState != IntegStoveStatus.powerState_off) {
                                        integrated.setFanVolume((short) 0,
                                                (short) 0, new VoidCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {

                        }
                    })
                    .show();

        } else {
            ToastUtils.showShort(R.string.device_close_text);
        }

    }


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, integrated.getID());
        bd.putString(PageArgumentKey.Url, mOilUrl);
        bd.putString(PageArgumentKey.title, mTitle);
        UIService.getInstance().postPage(PageKey.IntegratedStoveMorePage, bd);
    }

    private void DeviceOfflinePrompt() {
        ToastUtils.showShort(R.string.device_connected);
    }


}
