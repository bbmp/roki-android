package com.robam.roki.ui.activity3.device.fan;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.robam.base.BaseDialog;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.DeviceApi;
import com.robam.roki.net.request.bean.LinkageConfig;
import com.robam.roki.net.request.param.SetLinkageConfigParam;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvDeviceSelectAdapter;
import com.robam.roki.ui.activity3.device.fan.adapter.RvLinkageAdapter;
import com.robam.roki.utils.DeviceSelectUtils;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/08
 * desc   : 烟机联动功能
 */
public final class FanLinkageActivity extends DeviceBaseFuntionActivity implements
        RvLinkageAdapter.OnLinkageChangeLinstener , OnRequestListener {
    /**
     * 烟机
     */
    private AbsFan fan ;
    private RecyclerView rvFunction;
    /**
     * 联动adapter
     */
    private RvLinkageAdapter rvLinkageAdapter;
    private Pot pot;
    /**
     * 无人锅设备的状态
     */
    private short mPotBurningWarnSwitch = 1;
    /**
     * 烟机智能设定状态
     */
    private SmartParams smartParams ;
    /**
     * 蒸烤一体机联动状态
     */
    private SetLinkageConfigParam stemOvenconfig;
    /**
     * 设备选择dialog
     */
    private BaseDialog deviceDialog;
    private DeviceApi deviceApi;
    private String targetDeviceName;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fan_linkage;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(16, getContext()));
        rvLinkageAdapter = new RvLinkageAdapter();
        rvFunction.setAdapter(rvLinkageAdapter);
        //添加adapter监听
        rvLinkageAdapter.addOnLinkageChangeLinstener(this);
        rvLinkageAdapter.addChildClickViewIds(R.id.tv_child_device);
        rvLinkageAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter baseQuickAdapter, @NonNull View view, int position) {
                DeviceConfigurationFunctions item = rvLinkageAdapter.getItem(position);
                if (item.functionCode.equals("steamingRoastLinkage")) {
                    selectSteamOvenOne();
                }
            }
        });
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);
        //获取配置的子功能
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = deviceConfigurationFunction.subView.subViewModelMap.
                subViewModelMapSubView.deviceConfigurationFunctions;
        if (mDevice instanceof AbsFan){
            fan = (AbsFan) mDevice ;
            rvLinkageAdapter.setNewInstance(deviceConfigurationFunctions ,fan);
            //读取烟机智能设定状态
            redSmartConfig();

            IDevice childPot = fan.getChildPot();
            if (childPot != null && childPot instanceof Pot){
                pot = (Pot) childPot;
                //查询无人锅设定状态
                getFanPotLinkValue();
            }

            deviceApi = new DeviceApi(this);
            deviceApi.getLinkageConfig(Plat.accountService.getCurrentUserId() , fan.getGuid().getGuid() );
        }

    }

    /**
     * 读取烟机设定状态
     */
    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                FanLinkageActivity.this.smartParams = smartParams;
                rvLinkageAdapter.setSmart(smartParams);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    /**
     * 查询无人锅智能设定
     */

    private void getFanPotLinkValue() {
        pot.getFanPotLink(new Callback() {
            @Override
            public void onSuccess(Object o) {
                 mPotBurningWarnSwitch = pot.mPotBurningWarnSwitch;
                 rvLinkageAdapter.setPotParam(mPotBurningWarnSwitch);

            }

            @Override
            public void onFailure(Throwable t) {
                toast(t.getMessage());
                rvLinkageAdapter.setPotParam((short) 1);
            }
        });
    }

    @Override
    public void linkChange(boolean checked, DeviceConfigurationFunctions func, int linkState) {
        switch (func.functionCode) {
            case "stovelinkage":
                //烟灶联动
                if (linkState == RvLinkageAdapter.MAIN_LINK){
                    //烟灶联动
                    stovelinkage(checked);
                }else if (linkState == RvLinkageAdapter.SEC_LINK){
                    //自动匹配风量
                    stoveVolume(checked);
                }

                break;
            case "potLinkage":
                //烟锅联动
                if (linkState == RvLinkageAdapter.MAIN_LINK){
                    //烟锅联动
                    potLinkage(checked);
                }else if (linkState == RvLinkageAdapter.SEC_LINK){
                    //自动匹配风量
                    potVolume(checked);
                }

                break;
            case "steamingRoastLinkage":
                //烟灶联动
                if (linkState == RvLinkageAdapter.MAIN_LINK){
                    //烟灶联动
                    steamingRoastLinkage(checked);
                }else if (linkState == RvLinkageAdapter.SEC_LINK){
                    //自动匹配风量
                    steamingRoastVolume(checked);
                }

                break;
        }
    }

    /**
     * 烟灶联动
     * @param bool
     */
    public void stovelinkage(boolean bool){
        fan.setPowerLinkageSwitch( bool ? (short) 1 : (short) 0, (short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");
                smartParams.IsPowerLinkage  = bool ;
                //更新状态
                rvLinkageAdapter.setSmart(smartParams);
            }

            @Override
            public void onFailure(Throwable t) {
                toast(t.getMessage());
            }
        });
    }

    /**
     * 烟灶联动（自动匹配风量）
     * @param bool
     */
    public void stoveVolume(boolean bool){
        fan.setFanStoveAuto(bool ? (short) 1 : (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");
            }

            @Override
            public void onFailure(Throwable t) {
                toast(t.getMessage());
            }
        });
    }

    /**
     * 烟锅联动
     * @param bool
     */
    public void potLinkage(boolean bool){
        if (bool ){
            mPotBurningWarnSwitch |= 0x02;
        }else {
            mPotBurningWarnSwitch &= ~0x02;

        }
        pot.setFanPotLink(mPotBurningWarnSwitch, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");
                rvLinkageAdapter.setPotParam(mPotBurningWarnSwitch);
            }

            @Override
            public void onFailure(Throwable t) {
                rvLinkageAdapter.setPotParam(mPotBurningWarnSwitch);
            }
        });
    }

    /**
     * 烟锅联动（自动匹配风量）
     * @param bool
     */
    public void potVolume(boolean bool){
        if (bool ){
            mPotBurningWarnSwitch |= 0x04;
        }else {
            mPotBurningWarnSwitch &= ~0x04;
        }
        pot.setFanPotLink(mPotBurningWarnSwitch, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");
                rvLinkageAdapter.setPotParam(mPotBurningWarnSwitch);
            }

            @Override
            public void onFailure(Throwable t) {
                rvLinkageAdapter.setPotParam(mPotBurningWarnSwitch);
            }
        });
    }

    /**
     * 烟蒸烤联动
     * @param bool
     */
    public void steamingRoastLinkage(boolean bool){
        if (stemOvenconfig == null){
            stemOvenconfig = new SetLinkageConfigParam() ;
        }
        stemOvenconfig.enabled = bool ;
        stemOvenconfig.deviceGuid = fan.getGuid().getGuid() ;
        deviceApi.setLinkageConfig(stemOvenconfig);
    }

    /**
     * 烟蒸烤联动（自动匹配风量）
     * @param bool
     */
    public void steamingRoastVolume(boolean bool){
        if (stemOvenconfig == null){
            stemOvenconfig = new SetLinkageConfigParam() ;
        }
        if (stemOvenconfig.targetGuid == null || stemOvenconfig.targetGuid.length() == 0){
            toast("请选择蒸烤一体机设备");
            return ;
        }
        stemOvenconfig.doorOpenEnabled = bool ;
        stemOvenconfig.deviceGuid = fan.getGuid().getGuid() ;
        deviceApi.setLinkageConfig(stemOvenconfig);
    }

    /**
     * 选择蒸烤一体机
     */
    public void selectSteamOvenOne(){
        List<IDevice> list = Plat.deviceService.queryAll();
        List<IDevice> iDevices = DeviceSelectUtils.getInstance().deviceLinkageSteamOvenSelect(list);
        if (iDevices == null || iDevices.size() == 0) {
            toast("当前没有支持烟蒸烤联动的一体机设备");
            return;
        }
        deviceDialog = new BaseDialog(this);
        deviceDialog.setContentView(R.layout.dialog_device_select);
        deviceDialog.setCanceledOnTouchOutside(true);
        deviceDialog.setGravity(Gravity.BOTTOM);
        deviceDialog.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) deviceDialog.findViewById(R.id.rv_device);
        rvDevice.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL , false));
        //设置功能区块间距
        rvDevice.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        //device adapter
        RvDeviceSelectAdapter rvDeviceSelectAdapter = new RvDeviceSelectAdapter();
        rvDevice.setAdapter(rvDeviceSelectAdapter);
        //设置数据
        rvDeviceSelectAdapter.addData(iDevices);
        deviceDialog.show();
        rvDeviceSelectAdapter.addChildClickViewIds(R.id.rb_select);
        rvDeviceSelectAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter baseQuickAdapter, @NonNull View view, int i) {
                if (view.getId() == R.id.rb_select){
                    IDevice item = rvDeviceSelectAdapter.getItem(i);
                    if (view instanceof AppCompatCheckBox){
                        AppCompatCheckBox cbBox = (AppCompatCheckBox) view;
                        if (cbBox.isChecked()){
                            rvDeviceSelectAdapter.setSelectGuid(item);
                        }else {
                            rvDeviceSelectAdapter.setSelectGuid(null);
                        }
                    }
                }
            }
        });

        deviceDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel){
                    deviceDialog.dismiss();
                }else if (view.getId() == R.id.btn_complete){
                    bindFanSteamOven(rvDeviceSelectAdapter.getSelectGuid());
                    deviceDialog.dismiss();
                }
            }
        } ,R.id.btn_complete, R.id.btn_cancel);
    }

    /**
     * 绑定设备
     * @param device
     */
    private void bindFanSteamOven(IDevice device){
        if(device == null){
            toast("请选择设备");
            return;
        }
        if (stemOvenconfig == null){
            stemOvenconfig = new SetLinkageConfigParam() ;
        }
        stemOvenconfig.targetGuid = device.getGuid().getGuid() ;
        targetDeviceName = device.getDispalyType();
        deviceApi.setLinkageConfig(stemOvenconfig);
    }




    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {
        if (requestId == DeviceApi.getLinkageConfigCode){
            toast("获取烟机联动配置失败");
        }else if (requestId == DeviceApi.setLinkageConfigCode){
            toast("绑定失败");
        }
    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {
        if (requestId == DeviceApi.getLinkageConfigCode){
            if (paramObject instanceof LinkageConfig) {
                LinkageConfig linkageConfig = (LinkageConfig) paramObject;
                if (linkageConfig.payload != null){
                    stemOvenconfig = linkageConfig.payload;
                    rvLinkageAdapter.setSteamingRoast(stemOvenconfig);
                }

            }
        }else if (requestId == DeviceApi.setLinkageConfigCode){
            toast("设置成功");
            if (stemOvenconfig != null){
                if (targetDeviceName != null) {
                    stemOvenconfig.targetDeviceName = targetDeviceName ;
                }
                rvLinkageAdapter.setSteamingRoast(stemOvenconfig);
            }
        }
    }
}