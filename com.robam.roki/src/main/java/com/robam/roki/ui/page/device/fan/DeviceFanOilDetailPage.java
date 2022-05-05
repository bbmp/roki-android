package com.robam.roki.ui.page.device.fan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/9.
 * 油网拆卸页面
 */

public class DeviceFanOilDetailPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    String url;
    @InjectView(R.id.webView)
    ExtWebView mWebView;
    String mGuid;
    private IDevice iDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fan_oil_detail, container, false);
        Bundle bd = getArguments();
        url = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Url);
        mGuid = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Guid);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ProgressDialogHelper.setRunning(cx, true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGuid == null) {
            return;
        }
        iDevice = Plat.deviceService.lookupChild(mGuid);
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
