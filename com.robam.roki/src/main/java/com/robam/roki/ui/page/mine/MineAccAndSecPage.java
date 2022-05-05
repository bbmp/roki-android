package com.robam.roki.ui.page.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.common.eventbus.Subscribe;
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
import com.legent.utils.api.ToastUtils;
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


/**
 * desc : 账户与安全
 *
 * @author hxw
 */
public class MineAccAndSecPage extends MyBasePage<MainActivity> {
    /**
     * 修改密码
     */
    private SettingBar stbChangePassword;
    /**
     * 绑定手机号
     */
    private SettingBar stbBindPhone;
    /**
     * 绑定微信
     */
    private SettingBar stbBingWx;
    /**
     * 绑定Apple Id
     */
    private SettingBar stbBindApple;
    /**
     * 注销账户
     */
    private SettingBar stbLogoff;
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
        setTitle(R.string.setting_acc_and_sec);
        getTitleBar().setOnTitleBarListener(this);
        stbChangePassword = (SettingBar) findViewById(R.id.stb_change_password);
        stbBindPhone = (SettingBar) findViewById(R.id.stb_bind_phone);
        stbBingWx = (SettingBar) findViewById(R.id.stb_bing_wx);
        stbBindApple = (SettingBar) findViewById(R.id.stb_bind_apple);
        stbLogoff = (SettingBar) findViewById(R.id.stb_logoff);
        setOnClickListener(stbChangePassword, stbBindPhone, stbBingWx, stbBindApple, stbLogoff);
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
                ToastUtils.showThrowable(t);
            }
        });
    }

    public void setUser(User user) {
        stbBindPhone.setRightText(user.getPhone());
        stbBingWx.setRightText(StringUtil.isEmpty(user.wxNickname) ? "未绑定" : user.wxNickname);
        if (user.hasPassword()){
        }else {
            stbChangePassword.setRightText("未设置");
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
        } else if (view.equals(stbBindApple)) {
            ToastUtils.show("安卓手机不支持Apple Id的绑定");
        } else if (view.equals(stbLogoff)) {
            UIService.getInstance().postPage(PageKey.MineCancelAccountPage, bd);
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

                stbBingWx.setRightText(user.wxNickname);
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
                        stbBingWx.setRightText("未绑定");
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
