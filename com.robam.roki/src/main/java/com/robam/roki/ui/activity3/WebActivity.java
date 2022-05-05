package com.robam.roki.ui.activity3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter3.RvRecipeSteps2Adapter;
import com.robam.roki.ui.adapter3.RvRecipeSteps3Adapter;
import com.robam.roki.ui.adapter3.RvRecipeStepsAdapter;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.page.WebClientNewPage;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.robam.roki.ui.PageArgumentKey.Url;


/**
 *    desc   : 隐私协议
 *    @author r210190
 */
public final class WebActivity extends AppActivity {
    private static final String TAG = "SpeechCookActivity";
    private ExtWebView webView;
    private ImageView ivBack;
    private String url;

    public static void start(Context context , String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(PageArgumentKey.Url ,url);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
            return R.layout.page_new_webclient;

    }


    @Override
    protected void initView() {
        webView = (ExtWebView) findViewById(R.id.webView);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        setOnClickListener(ivBack);
        StatusBarUtils.setColor(this , getResources().getColor(R.color.white));
        StatusBarUtils.setTextDark(this ,true);
    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra(PageArgumentKey.Url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setCallback(new ExtWebView.Callback() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {

                LogUtils.i("20171024", "title:" + title);

            }

            @Override
            public void onReceivedError(WebView webView, int errorCode,
                                        String description, String failingUrl) {
                ToastUtils.showShort(description);
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        if (ivBack.equals(view)){
            finish();
        }
    }

}