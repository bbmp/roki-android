package com.robam.roki.ui.page.device.cook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.qrcode.QrUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/11/28.
 * 分享控制权
 */

public class ScanCodePage extends BasePage {

    public String mGuid;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.txt_desc)
    TextView txtDesc;
    @InjectView(R.id.img_code)
    ImageView imgCode;
    @InjectView(R.id.code_show)
    LinearLayout codeShow;
    @InjectView(R.id.text_sao)
    TextView textSao;
    @InjectView(R.id.saosao)
    LinearLayout saosao;
    private IDevice iDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        iDevice = Plat.deviceService.lookupChild(mGuid);
        LogUtils.i("20181128", "mGuid::" + mGuid);
        View view = inflater.inflate(R.layout.scan_code_page, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        Bitmap imgBit = QrUtils.create2DCode("GUID" + mGuid);
        imgCode.setImageBitmap(imgBit);
        textSao.setText("打开ROKI智能烹饪App \"扫一扫\" 获取产品控制权");
    }

    @OnClick(R.id.iv_back)
    public void reBack() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
