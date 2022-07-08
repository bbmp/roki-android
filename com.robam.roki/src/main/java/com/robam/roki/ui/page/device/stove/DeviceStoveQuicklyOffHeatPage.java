package com.robam.roki.ui.page.device.stove;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.StoveQuicklyAdapter;
import com.robam.roki.ui.page.device.AbsDeviceBasePage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/21.
 * 快速关火
 */

public class DeviceStoveQuicklyOffHeatPage extends AbsDeviceBasePage {

    Stove stove;
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    String title;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StoveQuicklyAdapter mStoveQuicklyAdapter;

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.pojo.getID()))
            return;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.page_quickly_off_heat;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        ButterKnife.inject(this, root);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stove==null) {
            return;
        }
        if (stove.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), stove.getDt() + ":快速关火页", null);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bd = getArguments();
        stove = bd == null ? null : (Stove) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : bd.getString(PageArgumentKey.text);
        mTvDeviceModelName.setText(title);
        mDeviceConfigurationFunctions = bd == null ? null :
                (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mStoveQuicklyAdapter = new StoveQuicklyAdapter(cx, stove, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
            }
        });

        mRecyclerView.setAdapter(mStoveQuicklyAdapter);
        LinearLayoutManager StoveQuicklyLinerLayout = new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL
                , false);
        mRecyclerView.setLayoutManager(StoveQuicklyLinerLayout);
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

}
