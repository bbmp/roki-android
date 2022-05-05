package com.robam.roki.ui.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.dialogs.ProgressNewDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.KitchenSourceShareDialog;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/4/11.
 */

public class KitchenKnowledgeArticlePage extends BasePage {

    @InjectView(R.id.webView)
    ExtWebView mWebView;
    private long id;
    String voideoId;
    int contentType;
    final String KIRCHEN_ACTIVE_URL = "https://h5.myroki.com/#/kitchenKnowledge/knowledgeActive?id=";
    final String KIRCHEN_VIDEO_URL = "https://h5.myroki.com/#/kitchenKnowledge/knowledgeVideo?id=";
    final String KIRCHEN_SOURCE_ACT_URL = "https://h5.myroki.com/#/chuYuanActivity";//分享链接
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mWebView.reload();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_kitchen_knowledg_article, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        if (null != bd) {
            id = bd.getLong(PageArgumentKey.Id);
            contentType = bd.getInt(PageArgumentKey.contentType);
            voideoId = bd.getString(PageArgumentKey.Url);
        }
        setiingWebToJS();
        initPage();
        return view;
    }

    private void setiingWebToJS() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ProgressNewDialogHelper.setRunning(cx, true);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressNewDialogHelper.setRunning(cx, false);
            }

        });

    }

    private void initPage() {
        boolean logon = Plat.accountService.isLogon();
        Long userId = Plat.accountService.getCurrentUserId();
        if (logon) {
            if (contentType == 0){
                mWebView.loadUrl(KIRCHEN_ACTIVE_URL + id + "&userId=" + userId);
            }else if (contentType == 1){
                mWebView.loadUrl(KIRCHEN_VIDEO_URL + id + "&videoUrl="+voideoId+"&userId=" + userId);
                LogUtils.i("20180516","voideoUrl:" + voideoId);
            }
        } else {
            if (contentType == 0){
                mWebView.loadUrl(KIRCHEN_ACTIVE_URL + id);
            }else if (contentType == 1){
                mWebView.loadUrl(KIRCHEN_VIDEO_URL + id + "&videoUrl="+voideoId);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }

    class CallMethodFromAndroidLister {
        Context context;

        public CallMethodFromAndroidLister(Context context) {
            this.context = context;
        }


        /**
         * 返回方法
         */
        @JavascriptInterface
        public void goback(boolean b) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                UIService.getInstance().popBack();
            }
        }

        /**
         * 分享文章方法
         */
        @JavascriptInterface
        public void shareActive(String id, String imgUrl, String title, String text) {
            String activeUrl = KIRCHEN_SOURCE_ACT_URL + "/articleShare?id=" + id;
            KitchenSourceShareDialog.show(cx, activeUrl, imgUrl, title, text);
        }

        /**
         * 分享视频方法
         */
        @JavascriptInterface
        public void shareVideo(String id, String url, String imgUrl, String title, String text) {
            String videoUrl = KIRCHEN_SOURCE_ACT_URL + "/videoShare?id=" + id + "&videoUrl=" + url;

            KitchenSourceShareDialog.show(cx, id, videoUrl, imgUrl, title, text);
        }

        /**
         * 跳转到菜谱详情页面
         *
         * @param id
         */
        @JavascriptInterface
        public void cookDetail(String id) {

            LogUtils.i("20170803", "id:" + id);
            long recipeId = Long.parseLong(id);
            RecipeDetailPage.show(recipeId, RecipeDetailPage.unKnown);
        }

        /**
         * 删除评论
         *
         * @param id
         */
        @JavascriptInterface
        public void deleteComment(final String id) {

            final IRokiDialog deleteCommentDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            deleteCommentDialog.setTitleText(R.string.is_delete_title);
            deleteCommentDialog.setContentText(R.string.is_delete_content);
            deleteCommentDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCommentDialog.dismiss();
                    CookbookManager.getInstance().deleteKitComment(Long.parseLong(id), new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            //删除后刷新页面
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            });
            deleteCommentDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCommentDialog.dismiss();
                }
            });
            deleteCommentDialog.show();

        }
    }
}
