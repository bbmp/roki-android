package com.robam.roki.ui.view.networkoptimization;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/14.
 */

public class SettingWifiPage extends HeadPage {

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.view_connect_wifi_setting, viewGroup, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.txt_setting)
    public void onClickSetting() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetWorkStateUtils.isWifi(getContext())) {
//            getActivity().onBackPressed();
            UIService.getInstance().popBack().postPage(PageKey.DeviceAdd);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
