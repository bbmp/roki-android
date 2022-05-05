package com.robam.roki.ui.view.networkoptimization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.legent.ui.ext.HeadPage;
import com.robam.roki.R;
import com.robam.roki.utils.PermissionsUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class CantFinishPage extends HeadPage {
    @InjectView(R.id.img_btn1)
    ImageView imgNet1;
    @InjectView(R.id.img_btn2)
    ImageView imgNet2;
    @InjectView(R.id.tip1)
    TextView tip1;
    String guidName;
    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        guidName=getArguments().getString("strDeviceName");
        View view = layoutInflater.inflate(R.layout.view_cannotfinish_connect, viewGroup, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    public void initView(){
        if (guidName.contains("J312")){
            imgNet1.setImageResource(R.mipmap.img_312_network);
            imgNet2.setImageResource(R.mipmap.img_312_network);
        }
        if (TextUtils.equals(guidName, "RQCG-03")) {
            tip1.setText("1.燃气卫士只支持烟机8700、5610、66A2H\n型号,请确认连接的烟机型号是否正确。");
        }
    }

    @OnClick(R.id.call)
    public void onClickCall() {
        //用intent启动拨打电话
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
            if (selfPermission == 0) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+95105855));
                startActivity(intent);
            } else {
                PermissionsUtils.checkPermission(getContext(), Manifest.permission.CALL_PHONE, PermissionsUtils.CODE_WIFI_SSID);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+95105855));
            startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionsUtils.CODE_WIFI_SSID == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+95105855));
                    startActivity(intent);
                }
            }
        }

    }
}
