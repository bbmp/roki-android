package com.robam.roki.ui.view.networkoptimization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
 * Created by yinwei on 2017/8/4.
 */

public class ZJCantFinishPage extends HeadPage {

    String guidName;
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        guidName=getArguments().getString("strDeviceName");
        View view = inflater.inflate(R.layout.view_zjcannotfinish_connect,container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.call)
    public void onClickCall() {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+95105855));
        startActivity(intent);
    }

    @OnClick(R.id.try_again)
    public void onClickAgain(){
       // UIService.getInstance().postPage(PageKey.DeviceWifiConnect);
        UIService.getInstance().popBack();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
