package com.robam.roki.ui.page;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.UserGenderView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.PickImageHelperTwo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class UserInfoPage extends BasePage {
    @InjectView(R.id.genderBoyView)
    UserGenderView genderBoyView;
    @InjectView(R.id.genderGirlView)
    UserGenderView genderGirlView;
    @InjectView(R.id.imgFigure)
    ImageView imgFigure;
    @InjectView(R.id.edtName)
    TextView txtName;
    @InjectView(R.id.txtPhone)
    TextView txtPhone;
    @InjectView(R.id.txtEmail)
    TextView txtEmail;
    @InjectView(R.id.txtPwd)
    TextView txtPwd;

    User user;
    PickImageHelperTwo pickHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_user_info, container, false);
        ButterKnife.inject(this, view);

        user = Plat.accountService.getCurrentUser();
        showUser(user);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Subscribe
    public void onEvent(UserUpdatedEvent event) {
        showUser(user);
    }

    @OnClick(R.id.imgFigure)
    public void onClickFigure() {
        if (pickHelper == null) {
            pickHelper = new PickImageHelperTwo(activity, pickCallback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.CAMERA);
            if (selfPermission == 0){
                pickHelper.showPickDialog("设置头像");
            }else {
                PermissionsUtils.checkPermission(cx,Manifest.permission.CAMERA,PermissionsUtils.CODE_USER_INFO_SHARE);
            }
        }else {
            pickHelper.showPickDialog("设置头像");
        }

    }


    @OnClick({R.id.edtName, R.id.txtPhone, R.id.txtEmail, R.id.txtPwd})
    public void onClick(View view) {
        Bundle bd = new Bundle();
        bd.putParcelable(PageArgumentKey.User, user);

        switch (view.getId()) {
            case R.id.edtName:
                UIService.getInstance().postPage(PageKey.UserModifyName, bd);
                break;
            case R.id.txtPwd:
                UIService.getInstance().postPage(PageKey.UserModifyPwd, bd);
                break;
            case R.id.txtPhone:
                UIService.getInstance().postPage(PageKey.UserModifyPhone, bd);
                break;
            case R.id.txtEmail:
                UIService.getInstance().postPage(PageKey.UserModifyEmail, bd);
                break;
        }
    }

    @OnClick(R.id.txtLogout)
    public void onClickLogout() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
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

    @OnClick(R.id.genderBoyView)
    public void onClickGenderBoy(UserGenderView view) {

        final boolean gender = view.isChecked();
        setGender(gender);
    }

    @OnClick(R.id.genderGirlView)
    public void onClickGenderGirl(UserGenderView view) {

        final boolean gender = !view.isChecked();
        setGender(gender);
    }

    void showUser(User user) {

        txtName.setText(Strings.isNullOrEmpty(user.nickname) ? user.phone : user.nickname);
        txtEmail.setText(Strings.isNullOrEmpty(user.email) ? "请设置邮箱" : user.email);
        txtPhone.setText(Strings.isNullOrEmpty(user.phone) ? "请设置手机" : user.phone);
        txtPwd.setText(user.hasPassword() ? "修改密码" : "请设置密码");

        showGender(user.gender);
        showFigure(user.figureUrl);
    }

    void showGender(boolean gender) {
        genderGirlView.setSelected(gender);
        genderBoyView.setSelected(!gender);
    }

    void showFigure(String figure) {
        if (Strings.isNullOrEmpty(figure))
            imgFigure.setImageResource(R.mipmap.ic_user_default_figure);
        else
            ImageUtils.displayImage(cx, figure, imgFigure, Helper.DisplayImageOptions_UserFace);
    }

    void setGender(final boolean gender) {
        CloudHelper.updateUser(user.id, user.nickname, user.phone, user.email, gender, new VoidCallback() {
            @Override
            public void onSuccess() {
                user.gender = gender;
                showGender(gender);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }


    PickImageHelperTwo.PickCallbackTwo pickCallback = new PickImageHelperTwo.PickCallbackTwo() {

        @Override
        public void onPickComplete(Bitmap bmp) {
            if (bmp == null)
                return;

            String strFace = User.figure2String(bmp);

            ProgressDialogHelper.setRunning(cx, true);
            Plat.accountService.updateFigure(user.id, strFace,
                    new Callback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            ProgressDialogHelper.setRunning(cx, false);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ProgressDialogHelper.setRunning(cx, false);
                            ToastUtils.showThrowable(t);
                        }
                    });
        }
    };


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionsUtils.CODE_USER_INFO_SHARE == requestCode){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    pickHelper.showPickDialog("设置头像");
                }
            }
        }

    }
}
