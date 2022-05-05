package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;
import com.robam.roki.model.helper.ShareHelper;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.robam.roki.ui.PageArgumentKey.timeType;

/**
 * Created by yinwei on 2017/1/12.
 */

public class WaterPurifierShareDialog extends AbsDialog {
    @InjectView(R.id.tv_close)
    TextView tvClose;
    @InjectView(R.id.layout)
    LinearLayout layout;
    @InjectView(R.id.ll_copy_url)
    LinearLayout llCopyUrl;
    private String guid;
    private long userId;
    static Context mContext;

    static public void show(Context cx, String guid) {
        mContext = cx;
        WaterPurifierShareDialog dlg = new WaterPurifierShareDialog(cx, guid);
        dlg.show();
    }

    public WaterPurifierShareDialog(Context context, String guid) {
        super(context, R.style.Theme_Dialog_kit_share);
        ViewUtils.setBottmScreen(cx, this);
        this.guid = guid;
        this.userId = Plat.accountService.getCurrentUserId();
        LogUtils.i("userId", "userId" + Plat.accountService.getCurrentUserId());
        LogUtils.i("userId", "userId" + guid);
        LogUtils.i("userId", "userId" + timeType);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cookbook_share;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        llCopyUrl.setVisibility(View.GONE);
    }


    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    @OnClick(R.id.imgMoment)
    public void onClickMoment() {

        share(WechatMoments.NAME);
    }

    @OnClick(R.id.imgFriend)
    public void onClickWechat() {
        share(Wechat.NAME);
    }

    @OnClick(R.id.imgQQ)
    public void onClickQQ() {
        share(QQ.NAME);
    }

    void share(final String platKey) {
        this.dismiss();
        final Context cx = getContext();
        final String title = "我的饮水量";
        final String text = "今天你达标了吗？";
        final String webUrl = String.format("http://h5.myroki.com/#/waterPurifier?userId=%s&guId=%s",
                userId, guid);
        String imgLocalPath = "water_share.jpg";
        InputStream inputStream = null;
        try {
            inputStream = cx.getApplicationContext().getAssets().open(imgLocalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtils.writeFile("/sdcard/18a23712384.jpg", inputStream);
        String imgPath = "/sdcard/18a23712384.jpg";
        if (Objects.equal(platKey, WechatMoments.NAME)) {
            //ShareHelper.shareWebByWechatMoments(cx, title, text, webUrl, imgUrl);
            ShareHelper.shareWebByWechatMomentsLocal(cx, title, text, webUrl, imgPath);
        } else if (Objects.equal(platKey, Wechat.NAME)) {
            //  ShareHelper.shareWebByWechat(cx, title, text,  webUrl, imgUrl);
            ShareHelper.shareWebByWechatLocal(cx, title, text, webUrl, imgPath);
        } else if (Objects.equal(platKey, QQ.NAME)) {
            //ShareHelper.shareWebByQQ(cx, title,text,  webUrl,imgUrl);
            ShareHelper.shareWebByQQLocal(cx, title, text, webUrl, imgPath);
        } else if (Objects.equal(platKey, SinaWeibo.NAME)) {
            ShareHelper.shareQzoneImg(cx, title, imgPath, text);
        } /*else if (Objects.equal(platKey, QZone.NAME)) {
            LogUtils.i("20180518","imgpath::"+imgPath);
            ShareHelper.shareQzoneImg(cx, title, imgPath, text);
            //ShareHelper.shareWebByQQLocal(cx, title, text, webUrl, imgPath);
        }*/

    }

    @OnClick({R.id.imgCopyUrl, R.id.ll_copy_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
          /*  case R.id.img_weibo:
                share(SinaWeibo.NAME);
                break;*/
          /*  case R.id.imgQQzone:
                share(QZone.NAME);
                break;*/

        }
    }


}
