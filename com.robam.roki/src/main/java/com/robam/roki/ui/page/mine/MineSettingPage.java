package com.robam.roki.ui.page.mine;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginEvent;
import com.legent.ui.UIService;
import com.legent.utils.DataCleanManagerUtils;
import com.legent.utils.api.PreferenceUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.utils.DialogUtil;
import com.youzan.sdk.YouzanSDK;


/**
 * desc : 设置界面
 *
 * @author hxw
 */
public class MineSettingPage extends MyBasePage<MainActivity> {
    /**
     * 账户与安全
     */
    private RelativeLayout stbSettingAccAndSec;
    /**
     * 清楚缓存
     */
    private RelativeLayout stbClearCache;
    /**
     * 退出账户
     */
    private Button btnLogout;
    private TextView tvCache;
    private ImageView ivBack;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_setting;
    }

    @Override
    protected void initView() {
        tvCache = findViewById(R.id.tv_cache);
        ivBack = findViewById(R.id.img_back);
        stbSettingAccAndSec = findViewById(R.id.stb_setting_acc_and_sec);
        stbClearCache = findViewById(R.id.stb_clear_cache);
        btnLogout = findViewById(R.id.btn_logout);
        setOnClickListener(stbSettingAccAndSec ,stbClearCache ,btnLogout, ivBack);
    }

    @Override
    protected void initData() {
        tvCache.setText(DataCleanManagerUtils.getCacheSize(cx));
        if(!Plat.accountService.isLogon()){
            btnLogout.setText("立即登录");
        }else {
            btnLogout.setText(getResources().getString(R.string.setting_logout));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnLogout)){
            if (Plat.accountService.isLogon()){
                logout();
            }else {
                CmccLoginHelper.getInstance().toLogin();
            }

        }else if(view.equals(stbSettingAccAndSec)){
            if (Plat.accountService.isLogon()) {
                UIService.getInstance().postPage(PageKey.MineAccAndSecPage);
            } else {
                if (CmccLoginHelper.getInstance().isGetPhone) {
                    CmccLoginHelper.getInstance().loginAuth();
                } else {
                    CmccLoginHelper.getInstance().login();
                }
            }
        }else if(view.equals(stbClearCache)){
            DataCleanManagerUtils.clearAppCache(getContext());
            ToastUtils.show("缓存清除成功");
            tvCache.setText(DataCleanManagerUtils.getCacheSize(getContext()));
        } else if (view.equals(ivBack)) {
            UIService.getInstance().popBack();
        }
    }


    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        if (activity instanceof MainActivity) {
            UIService.getInstance().popBack();
        } else {
            MainActivity.start(activity);
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
        dialog.setTitleText(R.string.user_out_app);
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
                PreferenceUtils.setBool("logout",false);
                Plat.accountService.logout(null);
                UIService.getInstance().popBack();
            }
        });
    }
    @Subscribe
    public void onEvent(UserLoginEvent event) {
        if(!Plat.accountService.isLogon()){
            btnLogout.setText("立即登录");
        }else {
            btnLogout.setText(getResources().getString(R.string.setting_logout));
        }
    }
}
