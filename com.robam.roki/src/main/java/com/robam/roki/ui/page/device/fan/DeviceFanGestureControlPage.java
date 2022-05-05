package com.robam.roki.ui.page.device.fan;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.FanGestureControlAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by RuanWei on 2019/9/26.
 * 手势控制
 */

public class DeviceFanGestureControlPage extends BasePage {
    AbsFan fan;

    private String mTag;

    @InjectView(R.id.iv_back)
    ImageView ivBack;

    @InjectView(R.id.tv_ges_control)
    TextView tvGesControl;

    @InjectView(R.id.rv_gesture)
    RecyclerView rvGesture;

    List<DeviceConfigurationFunctions> mList = new ArrayList<>();
    FanGestureControlAdapter fanGestureControlAdapter;
    private List<DeviceConfigurationFunctions> gestureList;
    private boolean isChecked;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_gesture_control, container, false);
        ButterKnife.inject(this, view);
        tvGesControl.setText(title);
        initData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        mTag = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.tag);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);

    }

    private void initData() {
        if (mList == null || mList.size() == 0) return;
        for (int i = 0; i < mList.size(); i++) {
            if ("gestureControl".equals(mList.get(i).functionCode)) {
                gestureList = mList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }
        redSmartConfig();




    }


    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                if (smartParams.gesture == (short) 0) {
                    isChecked = false;
                } else if (smartParams.gesture == (short) 1) {
                    isChecked = true;
                }
                setAdapter();


            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });


    }

    private void setAdapter() {
        fanGestureControlAdapter = new FanGestureControlAdapter(cx, gestureList, isChecked, fan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false);
        if (layoutManager!=null) {
            if (rvGesture!=null) {
                rvGesture.setLayoutManager(layoutManager);
                rvGesture.setAdapter(fanGestureControlAdapter);
            }

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_back})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }


}
