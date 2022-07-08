package com.robam.roki.ui.page.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.events.WxCode2Event;
import com.robam.common.events.WxCodeEvent;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.youzan.sdk.YouzanSDK;


/**
 * desc : 账户与安全
 *
 * @author hxw
 */
public class MineAccAndSecPage extends MyBasePage<MainActivity> {
    /**
     * 修改密码
     */
    private RelativeLayout stbChangePassword;
    /**
     * 绑定手机号
     */
    private RelativeLayout stbBindPhone;
    /**
     * 绑定微信
     */
    private RelativeLayout stbBingWx;
    /**
     * 注销账户
     */
    private RelativeLayout stbLogoff;

    private TextView tvPhone;
    private TextView tvBindWx;
    private TextView tvSetPwd;
    private ImageView ivBack;
    /**
     * 用户信息
     */
    private User user;
    /**
     * 微信绑定
     */
    private String code;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_acc_and_sec;
    }

    @Override
    protected void initView() {
//        setTitle(R.string.setting_acc_and_sec);
//        getTitleBar().setOnTitleBarListener(this);
        stbChangePassword = findViewById(R.id.stb_change_password);
        stbBindPhone = findViewById(R.id.stb_bind_phone);
        stbBingWx =  findViewById(R.id.stb_bing_wx);
        stbLogoff = findViewById(R.id.stb_logoff);
        tvPhone = findViewById(R.id.tv_phone);
        tvBindWx = findViewById(R.id.tv_bind_wx);
        tvSetPwd = findViewById(R.id.tv_set_pwd);
        ivBack = findViewById(R.id.img_back);
        setOnClickListener(stbChangePassword, stbBindPhone, stbBingWx, stbLogoff, ivBack);
    }

    @Override
    protected void initData() {
        user = Plat.accountService.getCurrentUser();
//        setUser(user);
        getUser();
    }
    /**
     * 获取用户信息3.7
     */
    public  void getUser() {
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getUser2(Plat.accountService.getCurrentUserId(), new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                ProgressDialogHelper.setRunning(cx, false);
                MineAccAndSecPage.this.user = user ;
                setUser(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage());
            }
        });
    }

    public void setUser(User user) {
        tvPhone.setText(user.getPhone());
        tvBindWx.setText(StringUtil.isEmpty(user.wxNickname) ? "未绑定" : user.wxNickname);
        if (user.hasPassword()){
        }else {
            tvSetPwd.setText("未设置");
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bd = new Bundle();
        bd.putParcelable(PageArgumentKey.User, user);
        if (view.equals(stbChangePassword)) {
            if (user.hasPassword()){
                UIService.getInstance().postPage(PageKey.MineChangePasswordPage, bd);
            }else {
                UIService.getInstance().postPage(PageKey.MineForgetPasswordPage, bd);
            }
        } else if (view.equals(stbBindPhone)) {
            UIService.getInstance().postPage(PageKey.MineBindPhonePage, bd);
        } else if (view.equals(stbBingWx)) {
            if (!StringUtil.isEmpty(user.wxNickname)) {
                unBindWx();
            } else {
                sendWxMessage();
            }
        } else if (view.equals(stbLogoff)) {
            UIService.getInstance().postPage(PageKey.MineCancelAccountPage, bd);
        } else if (view.equals(ivBack)) {
            UIService.getInstance().popBack();
        }
    }

    /**
     * 微信认证
     */
    private void sendWxMessage() {

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_bind";
        MobApp.mWxApi.sendReq(req);
    }

    @Subscribe
    public void onEvent(WxCode2Event event) {
        if ("wx_bind".equals(event.state)){
            code = event.code;
            bindWx(code ,"" ,"");
        }

    }



    private void bindWx(String code , String token ,String openId) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.bind3r(code, token, new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                ToastUtils.show("绑定成功");

                tvBindWx.setText(user.wxNickname);
                ProgressDialogHelper.setRunning(cx, false);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage());
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * 解绑微信
     */
    private void unBindWx() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
        dialog.setTitleText(R.string.unbind_wx);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Plat.accountService.unbind3rd(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        tvBindWx.setText("未绑定");
                        ToastUtils.show("解绑成功");
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(t.getMessage());
                    }
                });
            }
        });

    }

    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();
    }

}
