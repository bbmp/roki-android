package com.robam.roki.ui.page;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.common.base.Strings;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.roki.ui.PageArgumentKey.Url;

/**
 * Created by sylar on 15/6/4.
 */
public class WebClientPage extends HeadPage {
    @InjectView(R.id.webView)
    protected ExtWebView webView;
    String _signkey="B9FAFDD1-BA4F-4AF5-A8D4-1440F7863001";
    String url;
    long userId;

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View view = layoutInflater.inflate(R.layout.page_webclient, viewGroup, false);
        ButterKnife.inject(this, view);
        initPage(getArguments());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        ButterKnife.reset(this);
    }

    protected void initPage(Bundle bd) {
        url = bd == null ? null : bd.getString(Url);
        final String webTitle = bd == null ? null : bd.getString(PageArgumentKey.WebTitle);
        LogUtils.i("20171024","webTitle:"+webTitle);
        titleBar.setTitle(webTitle);
        addNavLeft(bd);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new callMethodFromAndroidLister(cx), "wu");
        webView.setCallback(new ExtWebView.Callback() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {

                LogUtils.i("20171024","title:"+title);

                if (Strings.isNullOrEmpty(webTitle)) {
                    titleBar.setTitle(title);
                } else {
                    titleBar.setTitle(webTitle);
                }
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode,
                                        String description, String failingUrl) {
                ToastUtils.showShort(description);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                     url = request.getUrl().toString();
                }
                //判断用户单击的是那个超连接
                String tag = "tel";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Intent mIntent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse(mobile);
                    mIntent.setData(data);
                    //Android6.0以后的动态获取打电话权限
                    if (ActivityCompat.checkSelfPermission(cx, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    }else{
                        //申请权限
                        ActivityCompat.requestPermissions((Activity) cx, new String[]{Manifest.permission.CALL_PHONE},1);
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.loadUrl(url);
    }



 public class callMethodFromAndroidLister{
    private Context context;
    String cookBookId;
    long userid=Plat.accountService.getCurrentUserId();
    boolean isEmpty = Plat.deviceService.isEmpty();
    public callMethodFromAndroidLister(Context context){
        this.context=context;
    }

    @JavascriptInterface
    public void cookDetail(String cookBookId){
        Bundle bundle = new Bundle();
        bundle.putLong(PageArgumentKey.BookId,Long.parseLong(cookBookId));
        bundle.putString(PageArgumentKey.entranceCode, RecipeRequestIdentification.RECIPE_H5);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bundle);
    }

    @JavascriptInterface
    public void isLogIn(String s){
        if("true".equals(s)){
            DialogHelper.newDialog_OkCancel(cx, "您还未登录，快去登录吧~", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        UIService.getInstance().postPage(PageKey.UserLogin);
                    }
                }
            }).show();
        }

    }

    @JavascriptInterface
    public void isClick(String reportUrl){
        String number;
        if (!isEmpty){
            number="1";
        }else{
            number="0";
        }
        StringBuilder s=new StringBuilder();
        s.append("{");
        s.append("\""+"url"+"\""+":");
        s.append("\""+reportUrl+"\""+",");
        s.append("\""+"isbuy"+"\""+":"+"\""+number+"\""+",");
        s.append("\""+"openId"+"\""+":"+userid+"}");

        CloudHelper.reportLog(Plat.appGuid, 1,s.toString(),null);





    }
}

    void addNavLeft(Bundle bd) {

    }






}
