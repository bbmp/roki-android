package com.robam.roki.ui.page.device.dishWasher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.ExtWebView;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 快速指南
 */
public class QuickGuidePage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.webView)
    ExtWebView webView;
    String guid;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quick_guide_page, container, false);
        ButterKnife.inject(this, view);
        initPage(getArguments());
        return view;
    }

    @OnClick(R.id.iv_back)
    public void onClickView() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        ButterKnife.reset(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initPage(Bundle bd) {
        guid = bd.getString(PageArgumentKey.Guid);
        url = bd.getString(PageArgumentKey.Url);
        title = bd.getString(PageArgumentKey.title);
        tvTitle.setText(title);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


}
