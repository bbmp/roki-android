package com.robam.roki.ui.activity3.device.hidkit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.HidKitHomeOtherParams;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvConSuctionAdapter;
import com.robam.roki.ui.activity3.device.hidkit.adapter.RvOtherFuncAdapter;
import com.robam.roki.ui.page.device.hidkit.HidKitHomeOtherPage;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/13
 * desc   : 藏宝盒功能二级界面
 */
public final class HidKitOtherActivity extends DeviceBaseFuntionActivity {


    private RecyclerView rvFunction;
    private RvOtherFuncAdapter rvOtherFuncAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hidkit_other_func;
    }

    @Override
    protected void initView() {

        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        rvFunction.addItemDecoration(new VerticalItemDecoration(16 , this));
        rvOtherFuncAdapter = new RvOtherFuncAdapter();
        rvFunction.setAdapter(rvOtherFuncAdapter);

    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);

        try {
            String functionParams = deviceConfigurationFunction.functionParams;
            HidKitHomeOtherParams hidKitHomeOtherParams = JsonUtils.json2Pojo(functionParams, HidKitHomeOtherParams.class);
            List<HidKitHomeOtherParams.StepsBean> steps = hidKitHomeOtherParams.getSteps();
            rvOtherFuncAdapter.addData(steps);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}