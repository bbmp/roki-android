package com.robam.roki.ui.view.networkoptimization;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.FindZJEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.ZjShowDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/4.
 */

public class DeviceZJWifiConnectPage extends HeadPage {
    String NetImgUrls;
    String NetTips;
    ImageView img_device;
    TextView tip1;
    TextView cannotfinishperform;
    TextView button;
    TextView tip2;
    TextView tip3;
    TextView tip4;
    ImageView img_tip2;
    String[] text;
    StringBuilder sb=new StringBuilder();
    ImageView imgTip;
    String txt;
    String strDeviceName;
    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        NetImgUrls = getArguments().getString("NetImgUrl");
        NetTips = getArguments().getString("NetTips");
        strDeviceName=getArguments().getString("strDeviceName");
        View view = layoutInflater.inflate(R.layout.view_device_zj_connect, viewGroup, false);
        img_device = view.findViewById(R.id.img_device);
        button= view.findViewById(R.id.next);
        tip1= view.findViewById(R.id.tip1);
        tip2= view.findViewById(R.id.tip2);
        tip3= view.findViewById(R.id.tip3);
        tip4= view.findViewById(R.id.tip4);
        img_tip2 = view.findViewById(R.id.img_tip2);
        imgTip= view.findViewById(R.id.img_tip);
        initView();
        cannotfinishperform = view.findViewById(R.id.cannotfinishperform);
        cannotfinishperform.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        cannotfinishperform.getPaint().setAntiAlias(true);//抗锯齿
        if (NetImgUrls != null) {
            Glide.with(getContext()).load(NetImgUrls).into(img_device);
        }
        ButterKnife.inject(this, view);
        return view;
    }
    private void initView(){
        if (NetTips != null) {
            text = NetTips.split("\n");
            if (text.length > 0) {
                button.setText(text[text.length - 1]);
                try{
                    if ("9W70".equals(strDeviceName)){
                        img_tip2.setVisibility(View.GONE);
                        tip1.setText(text[0]);
                        tip2.setText(text[1]);
                        tip4.setText(text[2]);
                    }else {
                        tip1.setText(text[0]);
                        tip2.setText(text[1]);
                        tip3.setText(text[2]);
                        tip4.setText(text[3]);
                    }
                }catch(Exception e){
                    e.getMessage();
                }
            }
        }
    }


    @OnClick(R.id.next)
    public void onClickNext() {
        LogUtils.i("20170227","txt:"+txt);
        ZjShowDialog dlg = new ZjShowDialog(cx);
        dlg.show();
        // UIService.getInstance().returnHome();
    }

    @Subscribe
    public void onEvent(FindZJEvent event) {
        LogUtils.i("20170807","event::"+event.toString());
    }

    @OnClick(R.id.cannotfinishperform)
    public void onClickCantfinishPerform() {
        LogUtils.i("20170804","str::"+strDeviceName);
        Bundle bundle=new Bundle();
        bundle.putString("strDeviceName",strDeviceName);
        if(("9B39".equals(strDeviceName))||("9W70".equals(strDeviceName))){
            UIService.getInstance().postPage(PageKey.ZJCantFinish,bundle);
        }else{
            UIService.getInstance().postPage(PageKey.CantFinish,bundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
