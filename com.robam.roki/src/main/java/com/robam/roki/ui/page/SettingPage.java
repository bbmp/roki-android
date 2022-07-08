package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.DataCleanManagerUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.youzan.sdk.YouzanSDK;


public class SettingPage extends BasePage implements OnClickListener{


    private ImageView mIvSetttingBack;
    private RelativeLayout mRlAccountSafe;
    private RelativeLayout mRlClearCache;
    private TextView mAccoutLogout;
    private TextView mCacheZise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settting_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mIvSetttingBack.setOnClickListener(this);
        mRlAccountSafe.setOnClickListener(this);
        mRlClearCache.setOnClickListener(this);
        mAccoutLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settting_back:
                UIService.getInstance().popBack();
                break;
            case R.id.rl_account_safe:
                UIService.getInstance().postPage(PageKey.AccountSafe);
                break;
            case R.id.rl_clear_cache:
                DataCleanManagerUtils.clearAppCache(getContext());
                ToastUtils.show("缓存清除成功", Toast.LENGTH_SHORT);
                mCacheZise.setText(DataCleanManagerUtils.getCacheSize(getContext()));
                break;
            case R.id.tv_accout_logout:
                accout_logout_setting();
                break;
        }

    }

    private void accout_logout_setting() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialog.setTitleText(R.string.user_out_app);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PreferenceUtils.setBool("logout",false);
                Plat.accountService.logout(null);
                YouzanSDK.userLogout(cx);
                UIService.getInstance().popBack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View view) {
        mIvSetttingBack = (ImageView) view.findViewById(R.id.iv_settting_back);
        mRlAccountSafe = (RelativeLayout) view.findViewById(R.id.rl_account_safe);
        mRlClearCache = (RelativeLayout) view.findViewById(R.id.rl_clear_cache);
        mAccoutLogout = (TextView) view.findViewById(R.id.tv_accout_logout);
        mCacheZise = (TextView) view.findViewById(R.id.tv_cahce_size);
        mCacheZise.setText(DataCleanManagerUtils.getCacheSize(getContext()));
    }
}