package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yinwei on 2017/8/11.
 */

public class RecipeLiveListNewPage extends BasePage {
    @InjectView(R.id.weblive)
    WebView weblive;

    String url = "http://develop.h5.myroki.com/#/video";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_live_new, container, false);
        ButterKnife.inject(this, view);
        setiingWebToJS();
        init(url);
        return view;
    }

    private void init(String url) {
        weblive.loadUrl(url);
    }

    private void setiingWebToJS() {
        WebSettings settings = weblive.getSettings();
        settings.setJavaScriptEnabled(true);
        weblive.setWebChromeClient(new WebChromeClient());
    }
}

