package com.robam.roki.ui.activity3.device.fan;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvConSuctionAdapter;

/**
 * author : huxw
 * time   : 2022/06/13
 * desc   : 智感恒吸
 */
public final class FanConSuctionActivity extends DeviceBaseFuntionActivity {
    /**
     * 烟机
     */
    private AbsFan fan ;
    private RecyclerView rvFunction;
    /**
     * 恒吸功能adapter（防止以后加入其他功能 用统一列表）
     */
    private RvConSuctionAdapter rvConSuctionAdapter;


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
        rvConSuctionAdapter = new RvConSuctionAdapter();
        rvFunction.setAdapter(rvConSuctionAdapter);
        rvConSuctionAdapter.addOnLinkageChangeLinstener(new RvConSuctionAdapter.OnLinkageChangeLinstener() {
            @Override
            public void linkChange(boolean checked, DeviceConfigurationFunctions func) {
                setConSuction(checked);
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
        rvConSuctionAdapter.addData(deviceConfigurationFunction);
        if(mDevice instanceof AbsFan){
            fan = (AbsFan) mDevice ;
            rvConSuctionAdapter.setFan(fan);
        }
    }

    private void setConSuction(boolean checked){
        if (fan == null){
            toast("设备获取失败");
            return;
        }
        fan.setCruise(checked ? 1 : 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                fan.cruise = (short) (checked ? 1 : 0);
                rvConSuctionAdapter.setFan(fan);
                toast(R.string.device_sterilizer_succeed);
            }

            @Override
            public void onFailure(Throwable t) {
                toast(t.getMessage());
            }
        });
    }
}