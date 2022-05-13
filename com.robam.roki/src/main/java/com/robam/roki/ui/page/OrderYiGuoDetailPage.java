//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.legent.Callback;
//import com.legent.plat.Plat;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.security.MD5Utils;
//import com.robam.common.io.cloud.Reponses;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by Administrator on 2017/2/20.
// */
//
//public class OrderYiGuoDetailPage extends BasePage {
//    View contentView;
//    @InjectView(R.id.ww)
//    WebView webView;
//    @InjectView(R.id.order_return)
//    ImageView orderReturn;
//    final String _signkey="B9FAFDD1-BA4F-4AF5-A8D4-1440F7836001";
//   String urlWeb;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        contentView = inflater.inflate(R.layout.orderyiguodetail, container, false);
//        ButterKnife.inject(this, contentView);
//        init();
//        return contentView;
//    }
//
//    public void init() {
//
//        StoreService.getInstance().getYiGuoUrl(new Callback<Reponses.GetYiGuoUrlResponse>() {
//            @Override
//            public void onSuccess(Reponses.GetYiGuoUrlResponse getYiGuoUrlResponse) {
//                if (getYiGuoUrlResponse != null) {
//                    LogUtils.i("20170222", "getYiGuoUrlResponse:" + getYiGuoUrlResponse.images.get(0).getContent());
//                    String localurl=getYiGuoUrlResponse.images.get(0).getContent();
//                    long userid=Plat.accountService.getCurrentUserId();
//                    boolean isEmpty = Plat.deviceService.isEmpty();
//                    String number;
//                    LogUtils.i("20170222","account::"+userid+"isEmpty:::"+isEmpty);
//                    if (!isEmpty){
//                        number="1";
//                    }else{
//                        number="0";
//                    }
//                        String string1="isbuy="+number+"&"+"openId="+userid+"&"+"key="+_signkey;
//                        String urlStr2= MD5Utils.Md5(string1);
//                        LogUtils.i("20170222","urlstr:::"+urlStr2);
//                        urlWeb=localurl+"isbuy="+number+"&"+"openId="+userid+"&"+"sign="+urlStr2;
//                        LogUtils.i("20170222","urlWeb:"+urlWeb);
//                        webView.loadUrl(urlWeb);
//
//                } else {
//                    ToastUtils.show("网络访问异常", Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.show("网络访问异常", Toast.LENGTH_SHORT);
//                t.printStackTrace();
//            }
//        });
//
//
//    }
//
//
//    @OnClick(R.id.order_return)
//    public void onClickRetrun() {
//        UIService.getInstance().popBack();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        webView.destroy();
//        ButterKnife.reset(this);
//    }
//
//}
