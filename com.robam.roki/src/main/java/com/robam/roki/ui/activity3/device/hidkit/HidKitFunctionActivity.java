package com.robam.roki.ui.activity3.device.hidkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.services.RestfulService;
import com.legent.utils.FileUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.model.bean.HidKitUpdateBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.SortUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.FanLinkageActivity;
import com.robam.roki.ui.activity3.device.fan.FanMoreActivity;
import com.robam.roki.ui.activity3.device.hidkit.adapter.RvHidKitFuntionAdapter;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 藏宝盒功能界面
 */
public final class HidKitFunctionActivity<HidKit extends AbsHidKit> extends DeviceBaseActivity implements RvHidKitFuntionAdapter.FuntionOnClickListener {
    private HidKit hidKit;
    private RecyclerView rvFunction;
    private RvHidKitFuntionAdapter adapter;
    private String KC306_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KC306.json";
    private String KM310_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KM310.json";
    private String KC306_fileName = "KC306.json";
    private String KM310_fileName = "KM310.json";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fan_function;
    }

    @Override
    protected void initView() {
        hideCloseSw();
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        adapter = new RvHidKitFuntionAdapter();
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
        if (mDevice instanceof AbsHidKit) {
            hidKit = (HidKit) mDevice;
            adapter.setHidKit(hidKit);
        }
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
//        setDeviceBgImg(bgImgUrl);
        setScollBgImg(adapter , bgImgUrl);
        //主功能区数据
        MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
//        //主功能排序
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
        loadSuccess = true;
    }

    @Override
    public void onFail() {
        toast("设备参数请求失败");
        finish();
    }


    @Override
    public void onClick(DeviceConfigurationFunctions func) {
        String functionCode = func.functionCode;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, hidKit.getGuid().getGuid());
        bd.putSerializable(DeviceBaseFuntionActivity.FUNCTION, func);
        Intent intent = new Intent();
        switch (functionCode) {
            case FuncCodeKey.ONLYATHOME:
                intent.setClass(this, HidKitSmartHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case FuncCodeKey.STORYVIDEO:
            case FuncCodeKey.ENCYCLOPEDIAS:
            case FuncCodeKey.KNOWLEDGE:
            case FuncCodeKey.LIFEASSISTANT:
                intent.setClass(this, HidKitOtherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            default:
                toast("暂未实现此功能");
                break;
        }
    }

    @Override
    protected void toMore() {
        super.toMore();
        Intent intent = new Intent(this , HidkitMoreActivity.class);
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        startActivity(intent);
    }



}