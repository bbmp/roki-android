package com.robam.roki.ui.activity3.device.steamoven;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.roki.R;
import com.robam.roki.manage.ThreadPoolManager;
import com.robam.roki.ui.PageArgumentKey;

import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DeviceModeSelectActivity;

import com.robam.roki.ui.activity3.device.base.DeviceMoreActivity;
import com.robam.roki.ui.activity3.device.base.other.ActivityManager;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.SortUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.base.table.LocalRecipeEntity;
import com.robam.roki.ui.activity3.device.dishwasher.DishWorkActivity;
import com.robam.roki.ui.activity3.device.fan.FanLinkageActivity;
import com.robam.roki.ui.activity3.device.fan.FanMoreActivity;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuntionAdapter;
import com.robam.roki.ui.activity3.device.steamoven.adapter.RvSteamOvenFuncAdapter;
import com.robam.roki.ui.activity3.device.steamoven.bean.RecipeOrderBean;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.LocalCookbook620Activity;

import java.io.Serializable;
import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 一体机功能界面
 */
public final class SteamOvenFunctionActivity<SteamOven extends AbsSteameOvenOneNew> extends DeviceBaseActivity implements RvSteamOvenFuncAdapter.FuntionOnClickListener {
    private SteamOven steamOven;
    /**
     * 功能区adapter
     */
    private RecyclerView rvFunction;
    /**
     * 功能区adapter
     */
    private RvSteamOvenFuncAdapter adapter;

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {

        if (steamOven == null || !Objects.equal(steamOven.getID(), event.pojo.getID())) {
            return;
        }
        this.steamOven = (SteamOven) event.pojo;
        if (!ActivityManager.getInstance().getTopActivity().equals(this) || !loadSuccess) {
            return;
        }
        //设置在线状态

        setConnectedState(!steamOven.isConnected());
        skipAction();
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_steamoven_function;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        adapter = new RvSteamOvenFuncAdapter();
        rvFunction.setAdapter(adapter);
    }

    /**
     * 数据参数获取成功
     *
     * @param deviceResponse
     */
    @Override
    public void onLoadData(Reponses.DeviceResponse deviceResponse) {
        if (deviceResponse == null) {
            toast("设备参数为空");
            return;
        }
        if (mDevice instanceof AbsSteameOvenOneNew) {
            steamOven = (SteamOven) mDevice;
        }
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
        setScollBgImg(adapter , bgImgUrl);

        //主功能区数据
        MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
        //主功能排序
//        mainFuncList = SortUtils.funSort(mainFuncList, 0);

        //其他功能区
        OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
        //获取第一模块并排序
        List<DeviceConfigurationFunctions> otherFun1 = SortUtils.funSort(deviceConfigurationFunctions, 1);
        //获取第二模块并排序
        List<DeviceConfigurationFunctions> otherFun2 = SortUtils.funSort(deviceConfigurationFunctions, 2);

        adapter.addData(mainFuncList);
        adapter.addData(otherFun1);
        adapter.addData(otherFun2);
        //添加点击事件
        adapter.addFuntionOnClickListener(this);

        //隐藏功能区
        HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
        List<DeviceConfigurationFunctions> hideFuncs = hideFunc.deviceConfigurationFunctions;
        if ("CQ920".equals(mDevice.getDt())){
            saveRecipe(hideFuncs);
        }else {
            //原始界面加载完成
            loadSuccess = true;
        }
    }



    /**
     * 数据参数获取失败
     */
    @Override
    public void onFail() {
        toast("设备参数请求失败");
        finish();
    }

    /**
     * 点击事件
     * @param func
     */
    @Override
    public void onClick(DeviceConfigurationFunctions func) {


        Intent intent = new Intent();
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, steamOven.getGuid().getGuid());
        bd.putSerializable(DeviceBaseFuntionActivity.FUNCTION, func);
        String functionCode = func.functionCode;
        toast(functionCode);
        switch (functionCode){
            case FuncCodeKey.STEAMINGMODE:
            case FuncCodeKey.ROASTMODEL:
            case FuncCodeKey.AIRFRYMODE:
            case FuncCodeKey.FZMODE:
            case FuncCodeKey.ADDSTEAMROAST:
//                startActivity(new Intent(this,SteamOvenWorkActivity.class));
                intent.setClass(this, SteamCurveWorkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;

            case FuncCodeKey.MULTISTEPMODE:
                intent=new Intent(getContext(),NewMutilCookBook4Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case FuncCodeKey.LOCALCOOK:
//                Intent intent=new Intent();
                intent.setClass(getContext(), LocalCookbook620Activity.class);
                intent.putExtra(LocalCookbook620Activity.getBdLocalCookbook(),bd);
                getContext().startActivity(intent);
//                List<DeviceConfigurationFunctions> localCookbookList = null;
//                String localCookbookTitle = null;
//                for (int i = 0; i < otherList.size(); i++) {
//                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.localCookbook)) {
//                        localCookbookList = otherList.get(i)
//                                .subView
//                                .subViewModelMap
//                                .subViewModelMapSubView
//                                .deviceConfigurationFunctions;
//                        localCookbookTitle = otherList.get(i).subView.title;
//                    }
//                }
//                Bundle bdLocalCookbook = new Bundle();
//                bdLocalCookbook.putString(PageArgumentKey.Guid, mGuid);
//                bdLocalCookbook.putSerializable(PageArgumentKey.List, (Serializable) func.subView
//                        .subViewModelMap
//                        .subViewModelMapSubView
//                        .deviceConfigurationFunctions);
//                bdLocalCookbook.putString(PageArgumentKey.title, localCookbookTitle);
//                bdLocalCookbook.putString(PageArgumentKey.descaling, needDescalingParams);
//                bdLocalCookbook.putString(PageArgumentKey.dt, mSteamOvenOneNew.getDt());
////                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
////                    UIService.getInstance().postPage(PageKey.Local610Cookbook, bdLocalCookbook);
////                }else {
//
//                if (mSteamOvenOneNew.getGuid().getGuid().contains("920")){
//                    Intent intent=new Intent(getContext(), LocalCookbook620Activity.class);
//                    intent.putExtra(LocalCookbook620Activity.getBdLocalCookbook(),bdLocalCookbook);
//                    getContext().startActivity(intent);
//                }else {
//                    UIService.getInstance().postPage(PageKey.LocalCookbook620Page, bdLocalCookbook);
//                }
                break;
            case FuncCodeKey.AUTOCOOKING:
                break;
            default:


                toast("功能未实现");
                break;


        }
    }

    /**
     * 更多
     */
    @Override
    protected void toMore() {
        super.toMore();

        Intent intent = new Intent(this , SteamMoreActivity.class);
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        startActivity(intent);

    }

    /**
     * 关机
     */
    @Override
    protected void onSwitch() {
        super.onSwitch();

    }

    /**
     * 查询一体机状态跳转意图
     */
    private void skipAction() {
        //当前界面不是置于顶部 不处理
       if (steamOven.workState != IntegStoveStatus.workState_free ){
           Intent intent = new Intent(this, SteamOvenWorkActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
           startActivity(intent);
       }
    }

    /**
     * 保存菜谱
     */
    private void saveRecipe(List<DeviceConfigurationFunctions> hideFuncs) {

        if (PreferenceUtils.getBool("recipe_" + mDevice.getDt() , false)){
            loadSuccess = true ;
            return;
        }
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                DeviceConfigurationFunctions func = hideFuncs.get(2);
                if ("recipe_order".equals(func.functionCode)){
                    RecipeOrderBean recipeOrderBean = new Gson().fromJson(func.functionParams, RecipeOrderBean.class);
                    for (LocalRecipeEntity localRecipeEntity : recipeOrderBean.localRecipe) {
                        localRecipeEntity.dt = mDevice.getDt();
                        localRecipeEntity.save() ;
                    }
                    PreferenceUtils.setBool("recipe_" + mDevice.getDt() , true);
                    loadSuccess = true ;
                }else {
                    loadSuccess = true ;
                }
            }
        });

    }

}