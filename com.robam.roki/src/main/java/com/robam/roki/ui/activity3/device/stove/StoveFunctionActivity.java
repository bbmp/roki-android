package com.robam.roki.ui.activity3.device.stove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DevicePMenuActivity;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.SortUtils;
import com.robam.roki.ui.activity3.device.base.other.StoveHeadSelect;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuntionAdapter;
import com.robam.roki.ui.activity3.device.steamoven.adapter.RvSteamOvenFuncAdapter;
import com.robam.roki.ui.activity3.device.stove.adapter.RvStoveFuncAdapter;
import com.robam.roki.ui.dialog.StoveQuickOffFireDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 灶具功能界面
 */
public final class StoveFunctionActivity<StoveCur extends Stove> extends DeviceBaseActivity implements RvStoveFuncAdapter.FuntionOnClickListener {
    private StoveCur stove;
    /**
     * 功能区adapter
     */
    private RecyclerView rvFunction;
    /**
     * 功能区adapter
     */
    private RvStoveFuncAdapter adapter;

    //快速关火弹窗
    StoveHeadSelect mStoveHeadSelect;
    // 无人锅
    private Pot pot = null;
    List<DeviceConfigurationFunctions> deviceConfigurationFunctions;

    /**
     * 上显现变更通知
     *
     * @param event
     */
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.device.getID()))
            return;
        //设置设备状态
        setConnectedState(!event.isConnected);
        if (!event.isConnected) {
            toast(R.string.device_new_connected);

        }
    }

    /**
     * 设备状态变更
     *
     * @param event
     */
    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20200611", "FanStatusChangedEvent:::" + event.pojo.toString());
        if (stove == null || !Objects.equal(stove.getID(), event.pojo.getID())) {
            return;
        }
        setConnectedState(!event.pojo.isConnected());
        //数据未加载完毕
        if (!loadSuccess) {
            return;
        }
        stove = (StoveCur) event.pojo;
        //更新主功能区数据
        adapter.notify(stove);
        //快速关火弹出窗状态
        mStoveHeadSelect.setStatus(stove);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_stove_function;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        adapter = new RvStoveFuncAdapter();
        rvFunction.setAdapter(adapter);
        ImageView ivDeviceSwitch = findViewById(R.id.iv_device_switch);
        ivDeviceSwitch.setVisibility(View.GONE);
        mStoveHeadSelect = new StoveHeadSelect(getActivity());
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
        if (mDevice instanceof Stove) {
            stove = (StoveCur) mDevice;
        }
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
        setDeviceBgImg(bgImgUrl);

        //主功能区数据
        BackgroundFunc mainFunc = deviceResponse.modelMap.backgroundFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
        //主功能排序
//        mainFuncList = SortUtils.funSort(mainFuncList, 0);

        //其他功能区
        OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
        deviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
        //获取第一模块并排序
        List<DeviceConfigurationFunctions> otherFun1 = SortUtils.funSort(deviceConfigurationFunctions, 1);
        //获取第二模块并排序
        List<DeviceConfigurationFunctions> otherFun2 = SortUtils.funSort(deviceConfigurationFunctions, 2);

        adapter.addData(mainFuncList);
        adapter.addData(otherFun1);
        adapter.addData(otherFun2);
        //添加点击事件
        adapter.addFuntionOnClickListener(this);
        //添加配置参数
        adapter.setBackgroundFunc(deviceResponse.modelMap.backgroundFunc);
        //原始界面加载完成
        loadSuccess = true;
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
     *
     * @param func
     */
    @Override
    public void onClick(DeviceConfigurationFunctions func) {
//        if(!stove.isConnected()){
//            toast(R.string.device_new_connected);
//            return;
//        }
        String functionCode = func.functionCode;
        switch (functionCode) {
            case FuncCodeKey.LEFTHEADSTATUE:
                break;
            case FuncCodeKey.RIGHTHEADSTATUE:
                break;
            case FuncCodeKey.CURVE:
                break;
            case FuncCodeKey.AUTOMOTICCOOKING:
                String params = null;
                String platformCode = "";
                for (DeviceConfigurationFunctions function : deviceConfigurationFunctions) {
                    if (function.functionCode.contains("automaticCooking")) {
                        params = function.functionParams;
                        break;
                    }
                }
                try {
                    if (params != null && !"".equals(params)) {
                        JSONObject jsonObject = new JSONObject(params);
                        platformCode = jsonObject.optString("platformCode");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.RecipeId, stove.getDc());
                bd.putString(PageArgumentKey.platformCode, platformCode);
                Intent intent = new Intent(getContext(), DevicePMenuActivity.class);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case FuncCodeKey.AUXISHUTDOWN:
//                if (stove.leftHead.status == 0 && stove.rightHead.status == 0) {
//                    ToastUtils.show("炉头均处于关闭状态", Toast.LENGTH_SHORT);
//                    return;
//                }
                mStoveHeadSelect.stoveHeadSelect(new DialogUtils.OnSelectListener() {
                    @Override
                    public void onSelectPosition(int index) {
                        if (index == 0) {
                            buttonLeft();
//                            toast("左");
                        } else if (index == 1) {
                            buttonRight();
//                            toast("右");
                        }
                    }
                });
                break;
            case FuncCodeKey.TIMEOFF:
                for (DeviceConfigurationFunctions function : deviceConfigurationFunctions) {
                    if (function.functionCode.contains("timedOff")) {
                        String functionName = function.functionName;
                        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = function
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        bd = new Bundle();
                        bd.putString(PageArgumentKey.Guid, mGuid);
//                        bd.putSerializable(PageArgumentKey.Bean, stove);
                        bd.putString(PageArgumentKey.text, functionName);
                        bd.putSerializable(PageArgumentKey.List, (Serializable) deviceConfigurationFunctions);
                        intent = new Intent(getContext(), StoveTimingFireActivity.class);
                        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                        startActivity(intent);
                        break;
                    }
                }

                break;
            default:
                toast("功能未实现");
                break;


        }
    }

    //查看父烟机下是否有无人锅
    private boolean checkPot() {
        //查询灶具父烟机下是否有无人锅
        String parentGuid = stove.getParent().getGuid().getGuid();
        AbsDeviceHub deviceHub;
        deviceHub = Plat.deviceService.lookupChild(parentGuid);
        pot = deviceHub.getChildPot();
        if (pot == null) {
            ToastUtils.show("请先添加无人锅", Toast.LENGTH_SHORT);
            return false;
        }
        if (!pot.isConnected()) {
            ToastUtils.show("无人锅已离线", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    /**
     * 更多
     */
    @Override
    protected void toMore() {
        super.toMore();
    }

    /**
     * 关机
     */
    @Override
    protected void onSwitch() {
        super.onSwitch();
        //TODO
    }

    //快速关火左
    private void buttonLeft() {
        Stove.StoveHead head = null;
        head = stove.leftHead;
        if (stove.leftHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final Stove.StoveHead finalHead = head;
            Preconditions.checkNotNull(finalHead);
            setStoveStatus(finalHead);

        }

    }

    //快速关火右
    private void buttonRight() {
        Stove.StoveHead head = null;
        head = stove.rightHead;
        if (stove.rightHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final Stove.StoveHead finalHead = head;
            Preconditions.checkNotNull(finalHead);
            setStoveStatus(finalHead);
        }

    }


    private void setStoveStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        if (!checkIsPowerOn(head)) return;
        short status = (head.status == StoveStatus.Off) ? StoveStatus.StandyBy
                : StoveStatus.Off;
        stove.setStoveStatus(false, head.ihId, status, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    boolean checkIsPowerOn(Stove.StoveHead head) {
        if (head.status != StoveStatus.Off)
            return true;
        else {
            if (!stove.isLock) {
                ToastUtils.showLong(R.string.device_stove_isLock);
            }
            return false;
        }
    }

    boolean checkConnection() {
        if (!stove.isConnected()) {
            ToastUtils.showLong(R.string.device_connected);
            return false;
        } else {
            return true;
        }
    }
}