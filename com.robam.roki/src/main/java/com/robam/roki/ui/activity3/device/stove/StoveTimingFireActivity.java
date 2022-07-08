package com.robam.roki.ui.activity3.device.stove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.adapter.StoveTimingAdapter;

import java.util.List;

public class StoveTimingFireActivity extends DeviceBaseFuntionActivity {

    private RecyclerView recyclerView;
    Stove stove;
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    StoveTimingAdapter mStoveTimingAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stove_timing_fire;
    }

    @Override
    protected void initView() {

        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.iv_device_switch).setVisibility(View.GONE);
        findViewById(R.id.iv_device_more).setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
//        super.initData();

        Bundle mBundle = getIntent().getBundleExtra(BUNDLE);
        if(mBundle == null){
            return;
        }
        stove = mBundle == null ? null : (Stove) mBundle.getSerializable(PageArgumentKey.Bean);
        String title = mBundle == null ? null : mBundle.getString(PageArgumentKey.text);
        setTitle(title);
//        mDeviceConfigurationFunctions = mBundle == null ? null :
//                (List<DeviceConfigurationFunctions>) mBundle.getSerializable(PageArgumentKey.List);
//        mStoveTimingAdapter = new StoveTimingAdapter(getContext(), stove, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//            }
//        });
//        recyclerView.setAdapter(mStoveTimingAdapter);
//        LinearLayoutManager StoveQuicklyLinerLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL
//                , false);
//        recyclerView.setLayoutManager(StoveQuicklyLinerLayout);
    }

    @Override
    protected void dealData() {


    }

}