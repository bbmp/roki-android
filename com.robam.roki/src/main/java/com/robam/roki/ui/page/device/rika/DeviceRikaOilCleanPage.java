package com.robam.roki.ui.page.device.rika;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.ui.view.RoundImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/27.
 * RIKA油网拆洗页面
 */

public class DeviceRikaOilCleanPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_round)
    RoundImageView mIvRound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_oil_clean, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }
}
