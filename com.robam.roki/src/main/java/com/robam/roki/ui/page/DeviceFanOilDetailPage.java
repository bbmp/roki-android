package com.robam.roki.ui.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/9.
 * 油网拆卸
 */

public class DeviceFanOilDetailPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.webView)
    ExtWebView mWebView;
    String url;
    @InjectView(R.id.tv_oil_detection)
    TextView mTvOilDetection;
    private String guid;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        url = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Url);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_oil_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvOilDetection.setText(title);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {

        UIService.getInstance().popBack();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (guid == null) {
            return;
        }
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice.getDt() == null) {
            return;
        }
        FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
        firebaseAnalytics.setCurrentScreen(getActivity(), iDevice.getDt(), null);

    }
}
