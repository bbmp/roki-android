package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.robam.roki.ui.PageArgumentKey;

import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;


public class AccountSafePage extends BasePage implements View.OnClickListener {

    private ImageView mIvSetttingAccount;
    private RelativeLayout mRlUpdatePasswords;
    private RelativeLayout mRlUpdatePhoneNumber;
    private RelativeLayout mRlLogOff;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_safe_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mIvSetttingAccount.setOnClickListener(this);
        mRlUpdatePasswords.setOnClickListener(this);
        mIvSetttingAccount.setOnClickListener(this);
        mRlUpdatePhoneNumber.setOnClickListener(this);
        mRlLogOff.setOnClickListener(this);
        user = Plat.accountService.getCurrentUser();
//        showUser(user);
    }

    @Override
    public void onClick(View v) {
        Bundle bd = new Bundle();
        bd.putParcelable(PageArgumentKey.User, user);
        switch (v.getId()){
            case R.id.iv_settting_account:
                UIService.getInstance().popBack();
                break;
            case R.id.rl_update_passwords:
                UIService.getInstance().postPage(PageKey.UserModifyPwd, bd);//修改密码
                break;
            case R.id.rl_update_phone_number:
                UIService.getInstance().postPage(PageKey.UserModifyPhone, bd);//修改手机
                break;
            case R.id.rl_log_off:
                UIService.getInstance().postPage(PageKey.UserCancelAccount,bd);//注销账号
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View view) {
        mIvSetttingAccount = (ImageView) view.findViewById(R.id.iv_settting_account);
        mRlUpdatePasswords = (RelativeLayout) view.findViewById(R.id.rl_update_passwords);
        mRlUpdatePhoneNumber = (RelativeLayout) view.findViewById(R.id.rl_update_phone_number);
        mRlLogOff = (RelativeLayout) view.findViewById(R.id.rl_log_off);
    }
}