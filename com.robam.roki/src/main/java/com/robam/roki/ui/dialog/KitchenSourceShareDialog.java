package com.robam.roki.ui.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.common.base.Objects;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import com.robam.roki.model.helper.ShareHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.dingding.friends.Dingding;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/7/31.
 */

public class KitchenSourceShareDialog extends AbsDialog{


      public static void show(Context cx, String url,String imgUrl,String title,String text) {
          KitchenSourceShareDialog dlg = new KitchenSourceShareDialog(cx,url,imgUrl,title,text);
          dlg.create();
          dlg.show();
      }

    public static void show(Context cx,String id,String url,String imgUrl,String title,String text) {
        KitchenSourceShareDialog dlg = new KitchenSourceShareDialog(cx,id,url,imgUrl,title,text);
        dlg.create();
        dlg.show();
    }


    @InjectView(R.id.imgFriend)
    ImageView imgFriend;
    @InjectView(R.id.imgMoment)
    ImageView imgMoment;
   /* @InjectView(R.id.img_weibo)
    ImageView imgWeibo;*/
    @InjectView(R.id.imgQQ)
    ImageView imgQQ;
   /* @InjectView(R.id.imgQQzone)
    ImageView imgQQzone;*/
    @InjectView(R.id.imgCopyUrl)
    ImageView imgCopyUrl;

    String activeUrl = null;
    String videoUrl = null;
    String imageUrl= null;
    String mTitle= null;
    String mText= null;
    String id= null;

    public KitchenSourceShareDialog(Context context) {
        super(context);
    }

    public KitchenSourceShareDialog(Context context, int theme) {
        super(context, theme);
    }

    public KitchenSourceShareDialog(Context cx, String url,String imgUrl,String title,String text) {
        super(cx, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx,this);
        this.cx = cx;
        this.activeUrl = url;
        this.imageUrl = imgUrl;
        this.mTitle = title;
        this.mText = text;
    }


    public KitchenSourceShareDialog(Context cx, String id,String url,String imgUrl,String title,String text) {
        super(cx, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx, this);
        this.cx = cx;
        this.id = id;
        this.videoUrl = url;
        this.imageUrl = imgUrl;
        this.mTitle = title;
        this.mText = text;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_share;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick({R.id.share_main,R.id.imgFriend, R.id.imgMoment, R.id.imgQQ,
            R.id.imgCopyUrl,R.id.img_Dingding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_main:
                this.dismiss();
                break;
            case R.id.imgFriend:
                share(Wechat.NAME);
                break;
            case R.id.imgMoment:
                share(WechatMoments.NAME);
                break;
           /* case R.id.img_weibo:
                share(SinaWeibo.NAME);
                break;*/
            case R.id.imgQQ:
                share(QQ.NAME);
                break;
           /* case R.id.imgQQzone:
                share(QZone.NAME);
                break;*/
            case R.id.imgCopyUrl:
                copyUrl();
                break;
            case R.id.img_Dingding:
                share(Dingding.NAME);
                break;
            default:
                break;
        }
    }
    //?????????copy????????????
    private void copyUrl() {

        if(!TextUtils.isEmpty(activeUrl)){
            ClipboardManager mClipboardManager =(ClipboardManager)cx.getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboardManager.setText(activeUrl);
        }

        if(!TextUtils.isEmpty(videoUrl)){
            ClipboardManager mClipboardManager =(ClipboardManager)cx.getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboardManager.setText(videoUrl);
        }
        this.dismiss();
        if (activeUrl != null || videoUrl != null)
        Toast.makeText(cx,getContext().getString(R.string.share_copy_url), Toast.LENGTH_LONG).show();
    }

    private void share(String platKey) {
        this.dismiss();

        String webUrl = null;
        String title = mTitle;
        String text = null;

        if (TextUtils.isEmpty(activeUrl)){
            webUrl = videoUrl;
            text = mText;
        }else{
            webUrl = activeUrl;
            text = mText;
        }
       String img = imageUrl;

        if (Objects.equal(platKey, WechatMoments.NAME)) {
            ShareHelper.shareWebByWechatMoments(cx, title, text, webUrl, img);
        } else if (Objects.equal(platKey, Wechat.NAME)) {
            ShareHelper.shareWebByWechat(cx, title, text, webUrl, img);
        } else if (Objects.equal(platKey, QQ.NAME)) {
            ShareHelper.shareWebByQQ(cx, title,text, webUrl,img);
        }else if (Objects.equal(platKey,Dingding.NAME)){

            String finalWebUrl = webUrl;
            String finalText = text;
            ImageUtils.loadImage(cx, img, new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    if (bitmap == null) {
                        ShareHelper.shareDingding(cx,title,img, finalWebUrl, finalText);
                        return;
                    }

                    if (bitmap != null) {
                        String img_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img.png";
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(img_path);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        ShareHelper.shareDingding(cx,title,img_path, finalWebUrl, finalText);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable drawable) {

                }
            });


        }


        /*else if (Objects.equal(platKey,SinaWeibo.NAME)){
            ShareHelper.shareSinaWeibo(cx,title,text,webUrl,img);
        }*/

    }

}
