package com.robam.roki.ui.page;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.WizardActivity;
import com.robam.roki.utils.LoginUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.PageArgumentKey.Url;

/**
 * Created by sylar on 15/6/4.
 */
public class WebAdvertPage extends BasePage {
    @InjectView(R.id.webView)
    protected ExtWebView webView;
    String url;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    private String entranceCode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_advert, container, false);
        ButterKnife.inject(this, view);
        StatusBarUtils.setTextDark(cx ,true);
        initPage(getArguments());
        return view;
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        initPage(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//            if (null != webView) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                    webView.goBack();// 返回前一个页面
//                    return true;
//                }else {
                    MainActivity.start(activity);
//                }
//            }
        return super.onKeyDown(keyCode, event);
    }

    protected void initPage(Bundle bd) {
        url = bd == null ? null : bd.getString(Url);
        entranceCode = bd.getString(PageArgumentKey.entranceCode);
        final String webTitle = bd == null ? null : bd.getString(PageArgumentKey.WebTitle);
        LogUtils.i("20171024", "webTitle:" + webTitle);
        mTvTitle.setText(webTitle);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new callMethodFromAndroidLister(cx), "RecipeDetail");
        webView.setCallback(new ExtWebView.Callback() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {

                LogUtils.i("20171024", "title:" + title);

//                if (Strings.isNullOrEmpty(webTitle)) {
//                    mTvTitle.setText(title);
//                } else {
//                    mTvTitle.setText(webTitle);
//                }
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode,
                                        String description, String failingUrl) {
                ToastUtils.showShort(description);
            }
        });

        webView.loadUrl(url);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {

//            if (webView.canGoBack()){
//                webView.goBack();
//            }else {
                MainActivity.start(activity);
//            }
    }


    public class callMethodFromAndroidLister {
        private Context context;
        boolean isEmpty = Plat.deviceService.isEmpty();

        public callMethodFromAndroidLister(Context context) {
            this.context = context;
        }

        /**
         * 商品详情页
         */
        @JavascriptInterface
        public void toYeGo(String url) {
            if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)){
                Bundle bd = getArguments();
                bd.putString(PageArgumentKey.Url,url);
                UIService.getInstance().postPage(PageKey.YouzanOrderDetails,bd);
            }
        }

    }
}
