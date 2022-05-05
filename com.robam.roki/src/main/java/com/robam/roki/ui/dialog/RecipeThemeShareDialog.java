package com.robam.roki.ui.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.legent.utils.graphic.BitmapUtils;
import com.legent.utils.graphic.ImageUtils;
import com.legent.utils.qrcode.QrUtils;
import com.robam.common.pojos.RecipeTheme;
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
 * Created by sylar on 15/6/15.
 */
public class RecipeThemeShareDialog extends AbsDialog {
    private static RecipeThemeShareDialog dialog;
    public static final String TEXT = "长按识别二维码可获取菜谱详情";
    public static final String LINK_URL = "http://h5.myroki.com/#/themeSharing?themeId=%s";
    @InjectView(R.id.layout)
    LinearLayout layout;
    @InjectView(R.id.ll_copy_url)
    LinearLayout llCopyUrl;
    @InjectView(R.id.ll_fc)
    LinearLayout llFc;

    private Bitmap resultBitmap;
    private Bitmap newLogbitmap;
    RecipeTheme theme;
    Bitmap encodeBitmap;
    Bitmap maxBitmap;
    int padding = 12;
    private String img_path;
    private String link_url;

    static public void show(Context cx, RecipeTheme theme) {
        if (dialog != null)
            dialog.dismiss();
        dialog = new RecipeThemeShareDialog(cx, theme);
        dialog.show();
    }

    public RecipeThemeShareDialog(Context cx, RecipeTheme theme) {
        super(cx, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx, this);
        this.theme = theme;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cookbook_theme_share;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
//        llCopyUrl.setVisibility(View.INVISIBLE);
//        llFc.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    @OnClick(R.id.imgMoment)
    public void onClickMoment() {
        shareImg(WechatMoments.NAME);
        if (!TextUtils.isEmpty(img_path))
            share(WechatMoments.NAME, img_path);
    }

    @OnClick(R.id.imgFriend)
    public void onClickWechat() {
        shareImg(Wechat.NAME);
        if (!TextUtils.isEmpty(img_path))
            share(Wechat.NAME, img_path);
    }

    @OnClick(R.id.imgQQ)
    public void onClickQQ() {
        shareImg(QQ.NAME);
        if (!TextUtils.isEmpty(img_path))
            share(QQ.NAME, img_path);
    }


    @OnClick({R.id.imgCopyUrl , R.id.img_weibo , R.id.imgQQzone,R.id.img_Dingding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_weibo:
                shareImg(SinaWeibo.NAME);
                if (!TextUtils.isEmpty(img_path)) {
                    share(SinaWeibo.NAME, img_path);
                }
                break;
            case R.id.imgQQzone:
                shareImg(QQ.NAME);
                if (!TextUtils.isEmpty(img_path)) {
                    share(QQ.NAME, img_path);
                }
                break;
            case R.id.imgCopyUrl:
//                if (!TextUtils.isEmpty(img_path)) {
//                    share(QQ.NAME, img_path);
                    copyUrl();
//                }
                break;
            case R.id.img_Dingding:
                shareImg(Dingding.NAME);
                if (!TextUtils.isEmpty(img_path)) {
                    share(Dingding.NAME, img_path);
                }
                break;
            default:
                break;
        }
    }

    private void shareImg(final String platKey) {
        String imgUrl = theme.imageUrl;
        newLogbitmap = compressBitmap(R.mipmap.img_share_r, 25, 25);
        link_url = String.format(LINK_URL,
                theme.id);
        encodeBitmap = encodeAsBitmap(link_url);
        Bitmap beicaibackground = BitmapFactory.decodeResource(cx.getResources(), R.mipmap.u17);
        final Bitmap backBitmap = BitmapUtils.zoomBySize(beicaibackground, 310, 103);
        if (platKey.equals(Dingding.NAME)){
            ImageUtils.loadImage(cx, imgUrl, new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {

                    maxBitmap = BitmapUtils.zoomBySize(bitmap, 310, 310);

                    if (maxBitmap != null) {

                        Canvas canvas = new Canvas(maxBitmap);
                        canvas.drawBitmap(maxBitmap, new Matrix(), null);
//                    int x = width / 2 - newLogbitmap.getWidth() / 2; log在中间
//                    int y = height - newLogbitmap.getHeight() - padding;
                        int x = maxBitmap.getWidth() - newLogbitmap.getWidth() - padding;
                        int y = maxBitmap.getHeight() - newLogbitmap.getHeight() / 2 - backBitmap.getHeight();
                        canvas.drawBitmap(maxBitmap, x, y, null); //写入点的x、y坐标
                        img_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img.png";
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(img_path);
                            maxBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    share(platKey, img_path);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable drawable) {

                }
            });
        }else {
            ImageUtils.loadImage(cx, imgUrl, new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {

                    maxBitmap = BitmapUtils.zoomBySize(bitmap, 310, 310);

                    if (maxBitmap != null) {
                        Canvas canvasBackBitmap = new Canvas(backBitmap);
                        canvasBackBitmap.drawBitmap(backBitmap, new Matrix(), null);
                        canvasBackBitmap.drawBitmap(encodeBitmap, 0, 3, null);
                        Paint paintRecipeName = new Paint();
                        paintRecipeName.setColor(0xff000000);
                        paintRecipeName.setAntiAlias(true);
                        paintRecipeName.setTextSize(16);
                        canvasBackBitmap.drawText(theme.name, 100, 46, paintRecipeName);
                        Paint paintText = new Paint();
                        paintText.setColor(0xff6d6d6d);
                        paintText.setTextSize(12);
                        paintText.setAntiAlias(true);
                        canvasBackBitmap.drawText(TEXT, 100, 73, paintText);
                        resultBitmap = BitmapUtils.add2Bitmap(maxBitmap, backBitmap);
                        Canvas canvas = new Canvas(resultBitmap);
                        canvas.drawBitmap(resultBitmap, new Matrix(), null);
//                    int x = width / 2 - newLogbitmap.getWidth() / 2; log在中间
//                    int y = height - newLogbitmap.getHeight() - padding;
                        int x = resultBitmap.getWidth() - newLogbitmap.getWidth() - padding;
                        int y = resultBitmap.getHeight() - newLogbitmap.getHeight() / 2 - backBitmap.getHeight();
                        canvas.drawBitmap(newLogbitmap, x, y, null); //写入点的x、y坐标
                        img_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "img.png";
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(img_path);
                            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    share(platKey, img_path);
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
        final String title = theme.name;
        String imageUrl = theme.imageUrl;
        final String wxPath = "pages/themDetail/themDetail?themeId=" + theme.getID();
        if (Objects.equal(platKey, WechatMoments.NAME)) {
            ShareHelper.shareImageFromLocal(cx, platKey, path);
        } else if (Objects.equal(platKey, Wechat.NAME)) {
            ShareHelper.shareMiniprogram(cx, platKey, title,imageUrl,link_url,wxPath,"");
        } else if (Objects.equal(platKey, QQ.NAME)) {
            ShareHelper.shareWebByQQ(cx, "", path);
        } else if (Objects.equal(platKey, SinaWeibo.NAME)) {
            ShareHelper.shareSinaWeiboImg(cx, title, path);
        } else if (Objects.equal(platKey, Dingding.NAME)) {
            ShareHelper.shareDingding(cx, title, path,link_url,theme.description);
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
    protected Bitmap compressBitmap(int id, double newWidth, double newHeight) {
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

    /**
     * 生成二维码的方法
     *
     * @param str
     * @return
     */
    protected static Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        bitmap = QrUtils.create2DCode(str);

//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 100, 100);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            bitmap = barcodeEncoder.createBitmap(result);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException iae) {
//            return null;
//        }
        return bitmap;
    }
    //将连接copy到剪切板
    private void copyUrl() {
        if (theme == null){
            ToastUtils.showShort("获取主题失败，请检查网络");
            dismiss();
            return;
        }
        link_url = String.format(LINK_URL,
                theme.id);
        if(!TextUtils.isEmpty(link_url)){
            ClipboardManager mClipboardManager =(ClipboardManager)cx.getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboardManager.setText(link_url);
        }
        this.dismiss();
        if (link_url != null) {
            Toast.makeText(cx,R.string.share_copy_url, Toast.LENGTH_LONG).show();
        }
    }
}
