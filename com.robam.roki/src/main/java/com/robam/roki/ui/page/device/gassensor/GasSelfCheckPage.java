package com.robam.roki.ui.page.device.gassensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/5/31.
 */

public class GasSelfCheckPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.title)
    RelativeLayout title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gas_self_check, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.iv_back)
    public void onClickBack(){
        UIService.getInstance().popBack();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
