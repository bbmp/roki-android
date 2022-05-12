package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.utils.PickImageHelperTwo;


public class VerifyMobilePhonePage extends BasePage implements View.OnClickListener {
    private static final String TAG = "VerifyMobilePhonePage";


    private ImageView mIvCancelSetttingBack;
    private TextView mTvPhoneNumber;
    private TextView mTxtUnregisterGetcode;
    private EditText mEdtUnregisterCode;
    private TextView mTxtUnregisterConfirm;
    private User user;
    private PickImageHelperTwo pickHelper;
    private String verifyCode;
    private String phone;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_mobile_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        user = Plat.accountService.getCurrentUser();
        mTvPhoneNumber.setText(Strings.isNullOrEmpty(user.phone) ? "无法查询到绑定手机号" : user.phone);
        mIvCancelSetttingBack.setOnClickListener(this);
        mTxtUnregisterConfirm.setOnClickListener(this);
        mTxtUnregisterGetcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel_settting_back:
                UIService.getInstance().popBack();
                break;
            case R.id.txt_unregister_getcode:
                getCode(user.phone);
                break;
            case R.id.txt_unregister_confirm:
                LogUtils.i(TAG, "unregisterConfirm");
                try {
                    String code = mEdtUnregisterCode.getText().toString();
                    Preconditions.checkState(Objects.equal(this.verifyCode, code), getCodeDesc() + cx.getString(R.string.weixin_login_mismatch));
                    LogUtils.i(TAG, "user.id:" + user.id + " user.phone:" + user.phone + " verifyCode:" + verifyCode + " currentUserId:" + Plat.accountService.getCurrentUserId());
                    Plat.accountService.unRegistAccount(user.getID(), user.phone, verifyCode, new Callback<Reponses.UnregisterResponse>() {

                        @Override
                        public void onSuccess(Reponses.UnregisterResponse unregisterResponse) {
                            LogUtils.i(TAG, "onSuccess:" + unregisterResponse.toString());
                            ToastUtils.show(unregisterResponse.msg, Toast.LENGTH_SHORT);
                            if (unregisterResponse.rc == 0) {
                                PreferenceUtils.setBool("logout", false);
                                PreferenceUtils.clear();
                                Plat.accountService.logout(null);

                                UIService.getInstance().popBack();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i(TAG, "onFailure:" + t.toString());
                        }
                    });
                } catch (Exception e) {
                    ToastUtils.showException(e);
                }


                break;
        }
    }

    private void getCode(final String phone) {
        this.phone = phone;
        LogUtils.i(TAG, "phone:" + phone);
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {
                    mTxtUnregisterGetcode.setBackgroundColor(getResources().getColor(R.color.c01));
                    verifyCode = getVerifyCodeReponse.verifyCode;
                    LogUtils.i(TAG, "verifyCode:" + verifyCode);
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.showShort(getCodeDesc() + cx.getString(R.string.weixin_login_send_msg));
                    startCountdown();
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(err);
            }
        });
    }

    private String getCodeDesc() {
        return cx.getString(R.string.weixin_login_verification_code);
    }


    private void startCountdown() {
        stopCountdown();

        mTxtUnregisterGetcode.setEnabled(false);
        countDownTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                if (VerifyMobilePhonePage.this.isDetached()) return;

                mTxtUnregisterGetcode.post(new Runnable() {
                    @Override
                    public void run() {
                        if (VerifyMobilePhonePage.this.isAdded() && !VerifyMobilePhonePage.this.isDetached()) {
                            mTxtUnregisterGetcode.setText(String.format(getCodeDesc() + "(%s)", millisUntilFinished / 1000));
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                mTxtUnregisterGetcode.setEnabled(true);
                mTxtUnregisterGetcode.setText(cx.getString(R.string.weixin_login_regain) + getCodeDesc());
            }
        };

        countDownTimer.start();
    }

    void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void initView(View view) {
        mIvCancelSetttingBack = (ImageView) view.findViewById(R.id.iv_cancel_settting_back);
        mTvPhoneNumber = (TextView) view.findViewById(R.id.tv_phone_number);
        mTxtUnregisterGetcode = (TextView) view.findViewById(R.id.txt_unregister_getcode);
        mEdtUnregisterCode = (EditText) view.findViewById(R.id.edt_unregister_code);
        mTxtUnregisterConfirm = (TextView) view.findViewById(R.id.txt_unregister_confirm);
    }

}