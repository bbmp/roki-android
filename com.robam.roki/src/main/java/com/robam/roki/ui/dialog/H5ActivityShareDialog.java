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
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.hjq.toast.ToastUtils;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.legent.utils.graphic.BitmapUtils;
import com.legent.utils.graphic.ImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.robam.common.Utils;
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
            ToastUtils.show("数据获取失败，请重试");
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
            ToastUtils.show("数据获取失败，请重试");
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
            ToastUtils.show("数据获取失败，请重试");
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
            ToastUtils.show("获取链接失败，请检查网络");
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

        newLogbitmap = compressBitmap(R.mipmap.img_share_r, Utils.dip2px(getContext(), 25), Utils.dip2px(getContext(), 25));
        //viewUrl 分享链接
//        String viewUrl = book.getViewUrl();
        //link_url 分享链接
//        link_url = String.format(viewUrl, book.id);
        //分享链接生成二维码
        encodeBitmap = encodeAsBitmap(getContext(), shareUrl);

        if (StringUtil.isEmpty(imgUrl)){
            share(platKey, null);
            return;
        }
        if (platKey.equals(Dingding.NAME)){
            ImageUtils.loadImage(imgUrl, new ImageLoadingListener() {


                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap loadedImage) {
                    maxBitmap = loadedImage;
                    if (maxBitmap == null) {
                        share(platKey, null);
                        return;
                    }
                    //modify by wang 22/04/19
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
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }else {
            ImageUtils.loadImage(imgUrl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    maxBitmap = loadedImage;
                    if (maxBitmap == null) {
                        share(platKey, null);
                        return;
                    }
                    //modify by wang 22/04/19
                    if (maxBitmap != null) {
                        Bitmap beicaibackground = BitmapFactory.decodeResource(cx.getResources(), R.mipmap.u17);
                        final Bitmap backBitmap = BitmapUtils.zoomBySize(beicaibackground, maxBitmap.getWidth(), Utils.dip2px(getContext(), 100));

                        Canvas canvasBackBitmap = new Canvas(backBitmap);
                        canvasBackBitmap.drawBitmap(backBitmap, new Matrix(), null);
                        canvasBackBitmap.drawBitmap(encodeBitmap, 0, 0, null);
                        Paint paintRecipeName = new Paint();
                        paintRecipeName.setColor(0xff000000);
                        paintRecipeName.setAntiAlias(true);
                        paintRecipeName.setTextSize(Utils.sp2px(getContext(), 14));
                        //改动
                        if(shareTitle!=null){
                            canvasBackBitmap.drawText(shareTitle, Utils.dip2px(getContext(), 100), Utils.dip2px(getContext(), 46), paintRecipeName);
                        }
                        Paint paintText = new Paint();
                        paintText.setColor(0xff6d6d6d);
                        paintText.setTextSize(Utils.sp2px(getContext(), 9));
                        paintText.setAntiAlias(true);
                        canvasBackBitmap.drawText(TEXT, Utils.dip2px(getContext(), 100), Utils.dip2px(getContext(), 73), paintText);
                        resultBitmap = BitmapUtils.add2Bitmap(maxBitmap, backBitmap);
                        Canvas canvas = new Canvas(resultBitmap);
                        canvas.drawBitmap(resultBitmap, new Matrix(), null);
                        int x = resultBitmap.getWidth() - newLogbitmap.getWidth() - padding;
                        int y = resultBitmap.getHeight() - newLogbitmap.getHeight() / 2 - backBitmap.getHeight();
                        canvas.drawBitmap(newLogbitmap, x, y, null); //写入点的x、y坐标
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
                public void onLoadingCancelled(String imageUri, View view) {

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
     * 图片压缩
     *
     * @param id        要操作的图片的大小
     * @param newWidth  图片指定的宽度
     * @param newHeight 图片指定的高度
     * @return
     */
    protected  Bitmap compressBitmap(int id, double newWidth, double newHeight) {
        // 获得原图
        Bitmap beforeBitmap = BitmapFactory.decodeResource(
                cx.getResources(), id);
        // 图片原有的宽度和高度
        float beforeWidth = beforeBitmap.getWidth();
        float beforeHeight = beforeBitmap.getHeight();

        // 计算宽高缩放率
        float scaleWidth = 0;
        float scaleHeight = 0;
        if (beforeWidth > beforeHeight) {
            scaleWidth = ((float) newWidth) / beforeWidth;
            scaleHeight = ((float) newHeight) / beforeHeight;
        } else {
            scaleWidth = ((float) newWidth) / beforeHeight;
            scaleHeight = ((float) newHeight) / beforeWidth;
        }
        // 矩阵对象
        Matrix matrix = new Matrix();
        // 缩放图片动作 缩放比例
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建一个新的Bitmap 从原始图像剪切图像
        Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,
                (int) beforeWidth, (int) beforeHeight, matrix, true);
        return afterBitmap;
    }

    OnFcListener onFcListener ;

    public interface OnFcListener{
        /**
         * 浮窗点击回调
         */
        void onFcClick();
    }

    public void addOnFcListener(OnFcListener onFcListener){
        this.onFcListener = onFcListener ;
    }
}
