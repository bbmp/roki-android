package com.robam.roki.ui.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.SoftInputUtils;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.RecipeSearchOptionView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RecipeSearchNewPage extends BasePage {

    @InjectView(R.id.webView)
    ExtWebView mWebView;
    @InjectView(R.id.txtCancel)
    TextView mTxtCancel;

    public static void show() {
        UIService.getInstance().postPage(PageKey.RecipeSearchNew);
    }

//        String URL = "http://develop.h5.myroki.com/dist/index.html#/menuSearch?keyword=";
    String URL = "http://h5.myroki.com/dist/index.html#/menuSearch?keyword=";
    @InjectView(R.id.edtSearch)
    EditText edtSearch;

    Map<Integer, String> source = new HashMap<Integer, String>();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_recipe_new_search, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        String text = bundle.getString(PageArgumentKey.text);
        edtSearch.setText(text);
        onSearchWord(text);
        edtSearch.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        if (!TextUtils.isEmpty(v.getText())) {
                                                            onSearchWord(v.getText().toString());//文字自动搜索
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }
        );
        initView();
        toggleSoftInput();
        switchDiv(true);
        initData();

        return view;
    }

    private void initView() {

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
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

    @OnClick(R.id.imgSearch)
    public void onMImgSearchClicked() {
        if (!TextUtils.isEmpty(edtSearch.getText().toString())) {
            onSearchWord(edtSearch.getText().toString());
        }

    }

    @OnClick(R.id.txtCancel)
    public void onMTxtCancelClicked() {
        if (!TextUtils.isEmpty(edtSearch.getText().toString())) {
            edtSearch.setText(null);
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    public class CallMethodFromAndroidLister {
        Context context;

        public CallMethodFromAndroidLister(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void goCookDetail(String id) {
            LogUtils.i("20180201", "roki_id:" + id);
            RecipeDetailPage.show(Long.parseLong(id), RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_SEARCE);
        }

        @JavascriptInterface
        public void goXiachufang(String url) {
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Url, url);
            UIService.getInstance().postPage(PageKey.RecipeUnderKitchen, bundle);
        }

        @JavascriptInterface
        public void goDouGuo(String id) {
            LogUtils.i("20180201", "gouguo_id:" + id);
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Id, id);
            UIService.getInstance().postPage(PageKey.RecipeDouGuo, bundle);

        }

    }

    void initData() {

        final RecipeSearchOptionView.OnWordSelectedCallback optCallback = new RecipeSearchOptionView.OnWordSelectedCallback() {

            @Override
            public void onWordSelected(String word) {
                edtSearch.setText(word);
                onSearchWord(word);//文字自动搜索
            }
        };

        List<String> words = CookbookManager.getInstance().getCookingHistory();
        CookbookManager.getInstance().getHotKeysForCookbook(
                new Callback<List<String>>() {

                    @Override
                    public void onSuccess(List<String> result) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

    }


    void switchDiv(boolean isSearch) {
        try {
//            divWithoutResult.setVisibility(isSearch ? View.GONE : View.VISIBLE);
            mTxtCancel.setVisibility(isSearch ? View.VISIBLE : View.GONE);
            mWebView.setVisibility(!isSearch ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            UIService.getInstance().returnHome();
        }

    }


    private void toggleSoftInput() {
        SoftInputUtils.show(cx, edtSearch);
    }


    void onSearchWord(String word) {
        boolean isSearch = !Strings.isNullOrEmpty(word);
        if (isSearch) {
//            divWithoutResult.setVisibility(View.GONE);
            mTxtCancel.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            initUrl(word);

        }
    }

    private void initUrl(String text) {
        long userId = Plat.accountService.getCurrentUserId();
        boolean logon = Plat.accountService.isLogon();
        if (logon){
            mWebView.loadUrl(URL + text+"&userId="+userId);
        }else {
            mWebView.loadUrl(URL + text);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
