package com.robam.roki.ui.dialog;

import static com.robam.roki.ui.dialog.RecipeThemeShareDialog.TEXT;
import static com.robam.roki.ui.dialog.RecipeThemeShareDialog.encodeAsBitmap;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.common.base.Objects;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.legent.utils.graphic.BitmapUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.model.helper.ShareHelper;
import com.robam.roki.utils.StringUtil;

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
 * Created by sylar on 15/6/15.
 */
public class H5ActivityShareDialog extends AbsDialog {
    @SuppressLint("StaticFieldLeak")
    public static H5ActivityShareDialog h5ActivityShareDialog;
    @InjectView(R.id.layout)
    LinearLayout layout;
    @InjectView(R.id.ll_copy_url)
    LinearLayout ll_copy_url;


     String shareTitle;
     String shareUrl ;
     String shareimgUrl ;
     String shareDesc;

    static public void show(Context cx,String url,String imgUrl,String title,String text) {
        h5ActivityShareDialog = new H5ActivityShareDialog(cx,  url, imgUrl, title, text);
        h5ActivityShareDialog.show();
    }
    static public void show(Context cx,String url,String imgUrl,String title,String text,String pageFrom) {
        h5ActivityShareDialog = new H5ActivityShareDialog(cx,  url, imgUrl, title, text,pageFrom);
        h5ActivityShareDialog.show();
    }
    private Bitmap resultBitmap;
    private Bitmap newLogbitmap;
    Bitmap encodeBitmap;
    Bitmap maxBitmap;
    int padding = 12;
    private String img_path;
    private String pageFrom;
    public H5ActivityShareDialog(Context cx, String url,String imgUrl,String title,String text) {
        super(cx, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx, this);
        this.shareUrl = url;
        this.shareTitle = title;
        this.shareimgUrl = imgUrl;
        this.shareDesc = text;
    }
    public H5ActivityShareDialog(Context cx, String url,String imgUrl,String title,String text,String pageFrom) {
        super(cx, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx, this);
        this.shareUrl = url;
        this.shareTitle = title;
        this.shareimgUrl = imgUrl;
        this.shareDesc = text;
        this.pageFrom = pageFrom;
        if(pageFrom!=null&&pageFrom.equals("common_act")){
            ll_copy_url.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_h5_activity_share;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);

    }


    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    @OnClick(R.id.imgMoment)
    public void onClickMoment() {
        try {
            if (!TextUtils.isEmpty(img_path)) {
                share(WechatMoments.NAME, img_path);
            }else {
                shareImg(WechatMoments.NAME);
            }
        }catch (Exception e){
            ToastUtils.showShort("??????????????????????????????");
            dismiss();
        }
    }

    @OnClick(R.id.imgFriend)
    public void onClickWechat() {
        try {
            if (!TextUtils.isEmpty(img_path)) {
                share(Wechat.NAME,img_path);
            }else {
                shareImg(Wechat.NAME);
            }
        }catch (Exception e){
            ToastUtils.showShort("??????????????????????????????");
            dismiss();
        }

    }

    @OnClick(R.id.imgQQ)
    public void onClickQQ() {
        try {
            if (!TextUtils.isEmpty(img_path)) {
                share(QQ.NAME,img_path);
            }else {
                shareImg(QQ.NAME);
            }
        }catch (Exception e){
            ToastUtils.showShort("??????????????????????????????");
            dismiss();
        }

    }

    @OnClick({R.id.imgCopyUrl , R.id.imgQQzone , R.id.img_weibo , R.id.ll_fc,R.id.img_Dingding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
             case R.id.img_weibo:
                shareImg(SinaWeibo.NAME);
                if (!TextUtils.isEmpty(img_path))
                    share(SinaWeibo.NAME, img_path);
                break;
           case R.id.imgQQzone:
               if (!TextUtils.isEmpty(img_path)) {
                   share(QQ.NAME, img_path);
               } else {
                   shareImg(QQ.NAME);
               }
                break;
            case R.id.imgCopyUrl:
                copyUrl();
                break;
            case R.id.ll_fc:
                if (onFcListener != null){
                    onFcListener.onFcClick();
                }
                dismiss();
                break;
            case R.id.img_Dingding:
                if (!TextUtils.isEmpty(img_path)) {
                    share(Dingding.NAME, img_path);
                }else{
                    shareImg(Dingding.NAME);
                }
                break;
            default:
                break;
        }
    }

    private void copyUrl() {
        if (shareUrl == null){
            ToastUtils.show("????????????????????????????????????");
            dismiss();
            return;
        }
        if(!TextUtils.isEmpty(shareUrl)){
            ClipboardManager mClipboardManager =(ClipboardManager)cx.getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboardManager.setText(shareUrl);
        }
        this.dismiss();
        if (shareUrl != null) {
            Toast.makeText(cx, R.string.share_copy_url, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(cx, R.string.share_copy_fail_url, Toast.LENGTH_LONG).show();
        }
    }

    private void shareImg(final String platKey) {

        String imgUrl =  shareimgUrl;

        newLogbitmap = compressBitmap(R.mipmap.img_share_r, 25, 25);
        //viewUrl ????????????
//        String viewUrl = book.getViewUrl();
        //link_url ????????????
//        link_url = String.format(viewUrl, book.id);
        //???????????????????????????
        encodeBitmap = encodeAsBitmap(shareUrl);
        Bitmap beicaibackground = BitmapFactory.decodeResource(cx.getResources(), R.mipmap.u17);
        final Bitmap backBitmap = BitmapUtils.zoomBySize(beicaibackground, 310, 103);
        if (StringUtil.isEmpty(imgUrl)){
            share(platKey, null);
            return;
        }
        if (platKey.equals(Dingding.NAME)){
            ImageUtils.loadImage(cx, imgUrl, new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {

                    if (bitmap == null) {
                        share(platKey, null);
                        return;
                    }
                    maxBitmap = BitmapUtils.zoomBySize(bitmap, 300, 300);

                    if (maxBitmap != null) {
                        img_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img.png";
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(img_path);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        maxBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        share(platKey, img_path);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable drawable) {

                }
            });
        }else {
            ImageUtils.loadImage(cx, imgUrl, new CustomTarget<Bitmap>(){

                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    if (bitmap == null) {
                        share(platKey, null);
                        return;
                    }
                    maxBitmap = BitmapUtils.zoomBySize(bitmap, 300, 300);

                    if (maxBitmap != null) {
                        Canvas canvasBackBitmap = new Canvas(backBitmap);
                        canvasBackBitmap.drawBitmap(backBitmap, new Matrix(), null);
                        canvasBackBitmap.drawBitmap(encodeBitmap, 0, 3, null);
                        Paint paintRecipeName = new Paint();
                        paintRecipeName.setColor(0xff000000);
                        paintRecipeName.setAntiAlias(true);
                        paintRecipeName.setTextSize(16);
                        //??????
                        canvasBackBitmap.drawText(shareTitle, 100, 46, paintRecipeName);
                        Paint paintText = new Paint();
                        paintText.setColor(0xff6d6d6d);
                        paintText.setTextSize(12);
                        paintText.setAntiAlias(true);
                        canvasBackBitmap.drawText(TEXT, 100, 73, paintText);
                        resultBitmap = BitmapUtils.add2Bitmap(maxBitmap, backBitmap);
                        Canvas canvas = new Canvas(resultBitmap);
                        canvas.drawBitmap(resultBitmap, new Matrix(), null);
                        int x = resultBitmap.getWidth() - newLogbitmap.getWidth() - padding;
                        int y = resultBitmap.getHeight() - newLogbitmap.getHeight() / 2 - backBitmap.getHeight();
                        canvas.drawBitmap(newLogbitmap, x, y, null); //????????????x???y??????
                        img_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img.png";
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(img_path);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        share(platKey, img_path);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable drawable) {

                }
            });
        }
    }

    void share(final String platKey, String path) {

        this.dismiss();
        final Context cx = getContext();
        final String title = shareTitle;
        final String webUrl = shareUrl;
        final String imgSmall = shareimgUrl;
        final String desc = shareDesc;
        final String wxPath = "pages/cookbook/cookbook?cookbookId=";
        if (Objects.equal(platKey, WechatMoments.NAME)) {
            ShareHelper.shareWebByWechatMoments(cx, title, desc, webUrl, imgSmall);
        } else if (Objects.equal(platKey, Wechat.NAME)) {
            ShareHelper.shareWebByWechat(cx, title, desc, webUrl, imgSmall);
        } else if (Objects.equal(platKey, QQ.NAME)) {
            ShareHelper.shareWebByQQ(cx, title,desc, webUrl,imgSmall);
//            ShareHelper.shareWebByQQ(cx,"",path);
        }else if (Objects.equal(platKey,SinaWeibo.NAME)){
            ShareHelper.shareSinaWeibo(cx, title,desc, webUrl,imgSmall);
//            ShareHelper.shareSinaWeiboImg(cx,title,path);
        }else if (Objects.equal(platKey,Dingding.NAME)){
            ShareHelper.shareDingding(cx,title,path,webUrl,desc);
        }

    }

    /**
     * ????????????
     *
     * @param id        ???????????????????????????
     * @param newWidth  ?????????????????????
     * @param newHeight ?????????????????????
     * @return
     */
    protected  Bitmap compressBitmap(int id, double newWidth, double newHeight) {
        // ????????????
        Bitmap beforeBitmap = BitmapFactory.decodeResource(
                cx.getResources(), id);
        // ??????????????????????????????
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();

        // ?????????????????????
        float scaleWidth = 0;
        float scaleHeight = 0;
        if (beforeWidth > beforeHeight) {
            scaleWidth = ((float) newWidth) / beforeWidth;
            scaleHeight = ((float) newHeight) / beforeHeight;
        } else {
            scaleWidth = ((float) newWidth) / beforeHeight;
            scaleHeight = ((float) newHeight) / beforeWidth;
        }
        // ????????????
        Matrix matrix = new Matrix();
        // ?????????????????? ????????????
        matrix.postScale(scaleWidth, scaleHeight);
        // ??????????????????Bitmap ???????????????????????????
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,
                (int) beforeWidth, (int) beforeHeight, matrix, true);
        return afterBitmap;
    }

    OnFcListener onFcListener ;

    public interface OnFcListener{
        /**
         * ??????????????????
         */
        void onFcClick();
    }

    public void addOnFcListener(OnFcListener onFcListener){
        this.onFcListener = onFcListener ;
    }
}
