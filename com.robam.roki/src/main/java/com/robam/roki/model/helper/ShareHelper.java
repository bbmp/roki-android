package com.robam.roki.model.helper;

import static com.legent.utils.EventUtils.postEvent;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.mob.tools.utils.UIHandler;
import com.robam.common.events.WxCodeShareEvent;
import com.robam.roki.R;

import java.util.HashMap;

import cn.sharesdk.dingding.friends.Dingding;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by sylar on 15/6/3.
 */
public class ShareHelper {

    //---------------------------------------------------------------------------------
    // shareWeb
    //---------------------------------------------------------------------------------

    static public void shareWebByWechat(Context cx, String title, String text, String url, String imgUrl) {
        shareWebFromUrl(cx, Wechat.NAME, title, text, url, imgUrl);
    }

    static public void shareWebByWechatMoments(Context cx, String title, String text, String url, String imgUrl) {
        shareWebFromUrl(cx, WechatMoments.NAME, title, text, url, imgUrl);
    }

    static public void shareWebByQQ(Context cx, String title, String text, String url, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setImageUrl(imgUrl);

        share(cx, QQ.NAME, sp);
    }
    //新浪微博链接
    static public void shareSinaWeibo(Context cx,String title,String text,String url,String imgUrl){
        Platform.ShareParams sp = new Platform.ShareParams();
        //图片
//        sp.setTitle(title);
//        sp.setTitleUrl(url);
//        sp.setText(text);
//        sp.setImageUrl(imgUrl);
//        share(cx, SinaWeibo.NAME, sp);

        //链接
        sp.setTitle(title);
        sp.setText(text);
        sp.setImageUrl(imgUrl);
        sp.setImageData(null);
        sp.setUrl(url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        share(cx, SinaWeibo.NAME, sp);

    }


    //新浪微博图片本地
    static public void shareSinaWeiboImg(Context cx,String title,String imgPath){
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setImagePath(imgPath);
        share(cx, SinaWeibo.NAME, sp);
    }


    static public void shareDingding(Context cx,String title,String imgPath,String webUrl,String desc){


        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(desc);
        sp.setImagePath(imgPath);
        sp.setImageUrl(imgPath);
        sp.setImageData(null);
        sp.setUrl(webUrl);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        share(cx, Dingding.NAME, sp);
    }

    //qq空间
    public static void shareQzone(Context cx, String title, String text, String url, String imgUrl) {
        LogUtils.i("20180518","imgurl:::"+imgUrl);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setImageUrl(imgUrl);
//        share(cx, QZone.NAME, sp);
    }

    //qq空间图片本地
    public static void shareQzoneImg(Context cx, String title,String imgPath,String text) {
        LogUtils.i("20180518","imgurl:::"+imgPath);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(text);
        sp.setImageUrl(imgPath);
//        share(cx, QZone.NAME, sp);
    }

    static public void shareWebByQQ(Context cx, String title, String path) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setImagePath(path);
        sp.setTitle(title);
        share(cx, QQ.NAME, sp);
    }

    static public void shareWebByWechatLocal(Context cx, String title, String text, String url, String imgPath) {
        shareWebFromUrlLocal(cx, Wechat.NAME, title, text, url, imgPath);
    }

    static public void shareWebByWechatMomentsLocal(Context cx, String title, String text, String url, String imgPath) {
        shareWebFromUrlLocal(cx, WechatMoments.NAME, title, text, url, imgPath);
    }

    static public void shareWebByQQLocal(Context cx, String title, String text, String url, String imgPath) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setImagePath(imgPath);
        share(cx, QQ.NAME, sp);
    }


    static public void shareWebFromMemory(Context cx, String platKey, String title, String text, String url, Bitmap bmp) {

        Platform.ShareParams sp = getSP(title, text, url);
        sp.setImageData(bmp);
        share(cx, platKey, sp);
    }

    static public void shareWebFromUrl(Context cx, String platKey, String title, String text, String url, String imgUrl) {

        Platform.ShareParams sp = getSP(title, text, url);
        sp.setImageUrl(imgUrl);
        share(cx, platKey, sp);
    }

    static public void shareWebFromUrl(Context cx, String platKey, String title, String text, Bitmap bitmap, String imgUrl) {

        Platform.ShareParams sp = getSP(title, text, bitmap);
        sp.setImageUrl(imgUrl);
        sp.setImageData(bitmap);
        share(cx, platKey, sp);
    }

    static public void shareWebFromUrlLocal(Context cx, String platKey, String title, String text, String url, String imgPath) {

        Platform.ShareParams sp = getSP(title, text, url);
        sp.setImagePath(imgPath);

        share(cx, platKey, sp);
    }

    static public void shareWebFromLocal(Context cx, String platKey, String title, String text, String url, String imgPath) {

        Platform.ShareParams sp = getSP(title, text, url);
        sp.setImagePath(imgPath);
        sp.setFilePath(imgPath);
        share(cx, platKey, sp);
    }

    //---------------------------------------------------------------------------------
    // shareText
    //---------------------------------------------------------------------------------

    static public void shareText(Context cx, String platKey, String title, String text) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setTitle(title);
        sp.setText(text);
        share(cx, platKey, sp);
    }

    //---------------------------------------------------------------------------------
    // shareImage
    //---------------------------------------------------------------------------------

    static public void shareImageFromMemory(Context cx, String platKey, Bitmap bmp) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImageData(bmp);
        share(cx, platKey, sp);
    }

    static public void shareImageFromLocal(Context cx, String platKey, String imgPath) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(imgPath);
        share(cx, platKey, sp);
    }

    static public void shareImageFromWeb(Context cx, String platKey, String imgUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImageUrl(imgUrl);
        share(cx, platKey, sp);
    }

    static public void shareMiniprogram(Context cx, String platKey, String title, String imgUrl
            , String url, String wxPath,String desc) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setUrl(url);
        sp.setImageUrl(imgUrl);
        sp.setTitle(title);
        sp.setText(desc);
        sp.setWxWithShareTicket(true);
        sp.setWxMiniProgramType(0);
        sp.setWxUserName("gh_be1f78aea5cd");
        sp.setWxPath(wxPath);
        sp.setShareType(Platform.SHARE_WXMINIPROGRAM);
        share(cx, platKey, sp);
    }

    static public void shareImageFromWeb(Context cx, String platKey, Bitmap bitmap) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImageData(bitmap);
        share(cx, platKey, sp);
    }

    //---------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------

    static public void share(Context cx, String platKey, Platform.ShareParams sp) {
        Platform plat = getPlat(platKey);
        plat.setPlatformActionListener(new PlatListener(cx));
        plat.share(sp);

    }

    static public Platform getPlat(String platKey) {
        Platform plat = ShareSDK.getPlatform(platKey);
        Preconditions.checkNotNull(plat, "not support plat:" + platKey);
        return plat;
    }

    //---------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------

    static Platform.ShareParams getSP(String title, String text, String url) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(text);
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(url);

        return sp;
    }

    static Platform.ShareParams getSP(String title, String text, Bitmap bitmap) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(text);
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setImageData(bitmap);

        return sp;
    }



    static class PlatListener implements PlatformActionListener, Handler.Callback {

        Context cx;

        public PlatListener(Context cx) {
            this.cx = cx;
        }

        public void onComplete(Platform plat, int action,
                               HashMap<String, Object> res) {
            Message msg = new Message();
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = plat;
            UIHandler.sendMessage(msg, this);
        }

        public void onCancel(Platform plat, int action) {
            Message msg = new Message();
            msg.arg1 = 3;
            msg.arg2 = action;
            msg.obj = plat;
            UIHandler.sendMessage(msg, this);
        }

        public void onError(Platform plat, int action, Throwable t) {
            t.printStackTrace();

            Log.e("分享错误",t.toString()+"---");
            Message msg = new Message();
            msg.arg1 = 2;
            msg.arg2 = action;
            msg.obj = t;
            UIHandler.sendMessage(msg, this);
        }


        @Override
        public boolean handleMessage(Message msg) {
            String text = null;
            switch (msg.arg1) {
                case 1: {//钉钉  微博  QQ 分享成功后回调
//                    ToastUtils.showShort("分享成功");
                    postEvent(new WxCodeShareEvent());
                }
                break;
                case 2: {
                    // 失败
                    if ("WechatClientNotExistException".equals(msg.obj.getClass().getSimpleName())) {
                        ToastUtils.showShort(cx.getString(R.string.wechat_client_inavailable));
                    } else if ("WechatTimelineNotSupportedException".equals(msg.obj.getClass().getSimpleName())) {
                        ToastUtils.showShort(cx.getString(R.string.wechat_client_inavailable));

                    } else {

                        ToastUtils.showShort(((Throwable)msg.obj).toString()+"");
                    }
                }
                break;
                case 3: {
                    // 取消
//                    ToastUtils.showShort("取消分享");
                }
                break;
            }

            return false;
        }
    }

    //---------------------------------------------------------------------------------
    //
    //---------------------------------------------------------------------------------

    static boolean shareFromQQLogin = false;

    static public void shareByLocalImage(Context cx, String platKey, String title, String text,
                                         String webUrl, String imgPath) {
        share(cx, platKey, false, title, text, webUrl, imgPath, null, null, null, null, null, null);
    }

    static public void shareByWebImage(Context cx, String platKey, String title, String text,
                                       String webUrl, String imgUrl) {
        share(cx, platKey, false, title, text, webUrl, null, imgUrl, null, null, null, null, null);
    }

//    static public void share(Context cx, String platKey, String title, String text,
//                             String webUrl, String imgUrl) {
//        share(cx, platKey, false, title, text, webUrl, null, imgUrl, null, null, null, null, null);
//    }
//
//    static public void share(Context cx, String platKey, String title, String text,
//                             String webUrl, String imgPath, String imgUrl) {
//        share(cx, platKey, false, title, text, webUrl, imgPath, imgUrl, null, null, null, null, null);
//    }

    /**
     * ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
     * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
     * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * <p/>
     * <p/>
     * 平台配置信息有三种方式：
     * 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     */
    static public void share(Context cx, String platKey, boolean silent,
                             String title, String text, String webUrl,
                             String imgPath, String imgUrl,
                             String site, String siteUrl, String comment,
                             String venueName, String venueDesc) {
        Context context = cx;
        final OnekeyShare oks = new OnekeyShare();

        oks.setTitle(title);
        oks.setTitleUrl(webUrl);
        oks.setText(text);

        oks.setImagePath(imgPath);
        oks.setFilePath(imgPath);
        oks.setImageUrl(imgUrl);

        oks.setUrl(webUrl);
        oks.setComment(comment);
        oks.setSite(site);
        oks.setSiteUrl(siteUrl);
        oks.setVenueName(venueName);
        oks.setVenueDescription(venueDesc);
        oks.setSilent(silent);
        String theme = "classic";//CustomShareFieldsPage.getString("theme", "classic");
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        if (platKey != null) {
            oks.setPlatform(platKey);
        }
        // 令编辑页面显示为Dialog模式
//        oks.setDialogMode();

        // 在自动授权时可以禁用SSO方式
        //if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
        oks.disableSSOWhenAuthorize();

        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        //oks.setCallback(new OneKeyShareCallback());

        // 去自定义不同平台的字段内容
        //oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

        // 去除注释，演示在九宫格设置自定义的图标
//        Bitmap enableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        Bitmap disableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.sharesdk_unchecked);
//        String label = getResources().getString(R.string.app_name);
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//                String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
//                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        };
//        oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

        // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
//		oks.addHiddenPlatform(SinaWeibo.NAME);
//		oks.addHiddenPlatform(TencentWeibo.NAME);

        // 为EditPage设置一个背景的View
//        oks.setEditPageBackground(getPage());
        oks.show(context);
    }

}
