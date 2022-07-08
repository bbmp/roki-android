package com.robam.roki.ui.activity3.device.stove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.legent.plat.io.cloud.Reponses;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;

public class StoveTimingFire extends DeviceBaseActivity {

    private RecyclerView recyclerView;

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

    }

    @Override
    public void onLoadData(Reponses.DeviceResponse deviceResponse) {

    }

    @Override
    public void onFail() {

    }
}