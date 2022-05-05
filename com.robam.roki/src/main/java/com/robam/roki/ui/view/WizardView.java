package com.robam.roki.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WizardView extends FrameLayout {


    @InjectView(R.id.imgBg)
    ImageView imgBg;
    @InjectView(R.id.tv_login_register)
    TextView loginRegister;
    @InjectView(R.id.txtLogin)
    TextView tvtLogin;
    @InjectView(R.id.txtStroll)
    TextView txtStroll;

    @InjectView(R.id.cb_accpet_roki_privacy)
    CheckBox cbAcceptRokiPrivacy;
    Context mContext;
    FragmentActivity activity ;
    private boolean isCheck = false;

    public WizardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs, null);
    }

    public WizardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    public WizardView(Context context, String images) {
        super(context);
        init(context, null, images);
    }
    public WizardView(Context context, String images ,FragmentActivity activity) {
        super(context);
        this.activity = activity ;
        init(context, null, images);
    }
    public WizardView(Context context) {
        super(context);
        init(context, null, null);
    }

    public WizardView(Context context , FragmentActivity activity) {
        super(context);
        this.activity = activity ;
        init(context, null, null);
    }
    @OnClick(R.id.txtLogin)
    public void onClickLogin() {//登录

//        if (isCheck) {
//            UserActivity.start((Activity) getContext());
//            startLogin();
            MainActivity.start((Activity) getContext());
            PreferenceUtils.setBool(PageArgumentKey.IsFirstUse, false);
//        } else {
//            ToastUtils.show("请阅读并同意协议及隐私政策", Toast.LENGTH_SHORT);
//        }
    }
    /**
     * 指向登录界面
     */
    private void  startLogin(){
        CmccLoginHelper instance = CmccLoginHelper.getInstance();
        instance.initSdk(activity );
        instance.getPhnoeInfo();
    }

    /**
     * @param event
     */
    @Subscribe
//    public void onEvent(WxCodeEvent event) {
//        LoginPageHelper.getInstance().onEvent(event);
//    }
    @OnClick(R.id.txtStroll)//随便逛逛
    public void onClickStroll() {
//        if (isCheck) {
            PreferenceUtils.setBool(PageArgumentKey.IsFirstUse, false);
            MainActivity.start((Activity) getContext());
//        } else {
//            ToastUtils.show("请阅读并同意协议及隐私政策", Toast.LENGTH_SHORT);
//        }
    }


    private void init(Context cx, AttributeSet attrs, String imagesUrl) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_wizard, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        ImageUtils.displayImage(cx, imagesUrl, imgBg);
        tvtLogin.setBackground(getResources().getDrawable(R.drawable.shape_button_background_selected));
//        txtStroll.setBackground(getResources().getDrawable(R.drawable.shape_button_background_selected));
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
        loginRegister.setMovementMethod(LinkMovementMethod.getInstance());
        loginRegister.setText(spannableStringBuilder);
        avoidHintColor(loginRegister);
        cbAcceptRokiPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
//                if (isChecked) {
//                    tvtLogin.setBackground(getResources().getDrawable(R.drawable.shape_button_background));
//                    txtStroll.setBackground(getResources().getDrawable(R.drawable.shape_button_background));
//                } else {
//                    tvtLogin.setBackground(getResources().getDrawable(R.drawable.shape_button_background_selected));
//                    txtStroll.setBackground(getResources().getDrawable(R.drawable.shape_button_background_selected));
//                }
            }
        });

    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView)
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

}
