package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/9.
 */
abstract public class AbsVerifyCodePage extends HeadPage {

    @InjectView(R.id.edtPhone)
    EditText edtPhone;
    @InjectView(R.id.edtCode)
    EditText edtCode;
    @InjectView(R.id.txtConfirm)
    TextView txtConfirm;
    @InjectView(R.id.txtGetCode)
    TextView txtGetCode;

    @InjectView(R.id.verify_user_login_register)
    TextView verifyUserLoginRegister;
    @InjectView(R.id.cb_accpet_roki_privacy)
    CheckBox accpetRokiPrivacy;

    String phone, code;
    CountDownTimer timer;

    abstract void onConfirm(String phone, String verifyCode);

    protected void init(Bundle bd) {
    }

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.abs_page_verify_code, viewGroup, false);
        ButterKnife.inject(this, view);
        edtPhone.requestFocus();
        txtGetCode.setText(cx.getString(R.string.weixin_login_acquiring) + getCodeDesc());
        edtCode.setHint(cx.getString(R.string.weixin_login_please_enter) + getCodeDesc());
        onCheckChange(true);
        init(getArguments());
        initLoginRegister();
        return view;
    }

    @OnCheckedChanged(R.id.cb_accpet_roki_privacy)
    public void onCheckChange(boolean checked) {
        txtConfirm.setSelected(!checked);
        txtConfirm.setEnabled(checked);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCountdown();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.txtGetCode)
    public void onClickGetCode() {

        try {
            String phone = edtPhone.getText().toString();
            Preconditions.checkState(!Strings.isNullOrEmpty(phone), cx.getString(R.string.weixin_login_phone_num_not_null));
            Preconditions.checkState(StringUtils.isMobile(phone), cx.getString(R.string.weixin_login_not_phone_num));
            getCode(phone);
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    //确定
    @OnClick(R.id.txtConfirm)
    public void onClickConfirm() {
        try {
            if (!accpetRokiPrivacy.isChecked()) {
                ToastUtils.show(getString(R.string.accept_user_privacy_policy), Toast.LENGTH_SHORT);
                return;
            }
            String code = edtCode.getText().toString();
            Preconditions.checkState(Objects.equal(this.code, code), getCodeDesc() + cx.getString(R.string.weixin_login_mismatch));
            onConfirm(phone, code);
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }


    void getCode(final String phone) {
        this.phone = phone;
        ProgressDialogHelper.setRunning(cx, true);
        CloudHelper.getVerifyCode(phone, Reponses.GetVerifyCodeReponse.class, new RetrofitCallback<Reponses.GetVerifyCodeReponse>() {
            @Override
            public void onSuccess(Reponses.GetVerifyCodeReponse getVerifyCodeReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (null != getVerifyCodeReponse) {

                    txtGetCode.setBackgroundColor(getResources().getColor(R.color.c01));
                    code = getVerifyCodeReponse.verifyCode;;
                    ToastUtils.showShort(getCodeDesc() + cx.getString(R.string.weixin_login_send_msg));
                    startCountdown();
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showShort(err);
            }
        });

    }


    void startCountdown() {
        stopCountdown();

        txtGetCode.setEnabled(false);
        edtCode.requestFocus();

        timer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                if (AbsVerifyCodePage.this.isDetached()) return;

                txtGetCode.post(new Runnable() {
                    @Override
                    public void run() {
                        if (AbsVerifyCodePage.this.isAdded() && !AbsVerifyCodePage.this.isDetached()) {
                            txtGetCode.setText(String.format(getCodeDesc() + "(%s)", millisUntilFinished / 1000));
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                txtGetCode.setEnabled(true);
                txtGetCode.setText(cx.getString(R.string.weixin_login_regain) + getCodeDesc());
            }
        };

        timer.start();
    }

    void stopCountdown() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



    String getCodeDesc() {
        return cx.getString(R.string.weixin_login_verification_code);
    }

    private void initLoginRegister() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.roki_priviacy_agreement_title));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(R.color.c67), 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.UserNotice);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "ROKI用户协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        }, 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(R.color.c67), 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.RegisterAgreement);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "隐私协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        }, 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        verifyUserLoginRegister.setMovementMethod(LinkMovementMethod.getInstance());
        verifyUserLoginRegister.setText(spannableStringBuilder);
        avoidHintColor(verifyUserLoginRegister);
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView)
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }


}
