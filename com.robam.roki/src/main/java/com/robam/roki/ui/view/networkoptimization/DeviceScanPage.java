package com.robam.roki.ui.view.networkoptimization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.popoups.Dp2PxUtils;
import com.legent.utils.qrcode.ScanQrActivity;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.NetWorkingSteps;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/14.
 */

public class DeviceScanPage extends MyBasePage<MainActivity> {
    private final static int SCANNIN_GREQUEST_CODE = 100;
    String NetImgUrls;
    String displayType;
    String buttonTxt;
    String strDeviceName;

    @InjectView(R.id.background)
    ImageView mBackground;

    @InjectView(R.id.step_desc)
    TextView mStepDesc;

    @InjectView(R.id.tip)
    TextView tip;

    @InjectView(R.id.btn_scan)
    Button mBtnScan;
    private TextView tv_add_device_name;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.btn_scan)
    public void onClickLight() {
//        Activity atv = UIService.getInstance().getTop().getActivity();
//        Intent intent = new Intent();
//        intent.setClass(atv, ScanQrActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        if (TextUtils.equals(buttonTxt, "确定")) {
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().postPage(PageKey.WifiConnect);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_connect_scan;
    }

    @Override
    protected void initView() {
        NetImgUrls = getArguments().getString("NetImgUrl");
        displayType = getArguments().getString("displayType");
        strDeviceName = getArguments().getString("strDeviceName");
        tv_add_device_name = findViewById(R.id.tv_add_device_name);
        tv_add_device_name.setText(strDeviceName);
    }

    @Override
    protected void initData() {
        RokiRestHelper.getNetworkDeviceStepsRequest(displayType, new Callback<List<NetWorkingSteps>>() {

            @Override
            public void onSuccess(List<NetWorkingSteps> netWorkingSteps) {
                if (netWorkingSteps != null && netWorkingSteps.size() !=0){
                    Glide.with(getContext()).load(netWorkingSteps.get(0).netImgUrl).into(mBackground);
                    mBtnScan.setText(netWorkingSteps.get(0).buttonDesc);
                    buttonTxt = netWorkingSteps.get(0).buttonDesc;
                    String txt = netWorkingSteps.get(0).netRichText;
                    txt = txt.replace("background","");
                    RichText.from(txt).fix(new ImageFixCallback() {
                        @Override
                        public void onInit(ImageHolder holder) {
                            holder.setHeight(Dp2PxUtils.dp2px(getActivity(), 16));
                            holder.setWidth(Dp2PxUtils.dp2px(getActivity(), 16));
                        }

                        @Override
                        public void onLoading(ImageHolder holder) {

                        }

                        @Override
                        public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                        }

                        @Override
                        public void onImageReady(ImageHolder holder, int width, int height) {

                        }

                        @Override
                        public void onFailure(ImageHolder holder, Exception e) {

                        }
                    }).into(tip);
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
