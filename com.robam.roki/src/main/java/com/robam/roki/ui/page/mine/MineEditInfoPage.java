package com.robam.roki.ui.page.mine;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.ui.ext.BaseActivity;
import com.robam.base.BaseDialog;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.ui.UI;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.EditDeviceNameActivity;
import com.robam.roki.ui.activity3.EditUserNameActivity;
import com.robam.roki.ui.activity3.device.base.DeviceInfoActivity;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.mdialog.DateDialog;
import com.robam.roki.ui.mdialog.MenuDialog;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.PickImageHelperTwo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * desc : 用户个人信息界面
 *
 * @author hxw
 */
public class MineEditInfoPage extends MyBasePage<MainActivity> {

    /**
     * 头像
     */
    private RelativeLayout stbUserPhoto;
    /**
     * 头像图片
     */
    private AppCompatImageView ivPersonPhoto;
    /**
     * 用户名
     */
    private RelativeLayout stbUserName;
    private TextView tvName;
    /**
     * 性别
     */
    private RelativeLayout stbUserSex;
    private TextView tvGender;
    /**
     * 生日
     */
    private RelativeLayout stbUserBirthday;
    private TextView tvBirth;

    private ImageView ivBack;
    /**
     * 用户信息
     */
    User user;
    /**
     * 头像处理帮助类（原逻辑见UserInfoPage）
     */
    PickImageHelperTwo pickHelper;
    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_edit_info;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.img_back);
        stbUserPhoto = findViewById(R.id.stb_user_photo);
        ivPersonPhoto = findViewById(R.id.iv_person_photo);
        stbUserName = findViewById(R.id.stb_user_name);
        tvName = findViewById(R.id.tv_name);
        stbUserSex = findViewById(R.id.stb_user_sex);
        tvGender = findViewById(R.id.tv_gender);
        stbUserBirthday = findViewById(R.id.stb_user_birthday);
        tvBirth = findViewById(R.id.tv_birth);
        setOnClickListener(stbUserPhoto, stbUserName, stbUserSex, stbUserBirthday, ivBack);
    }

    @Override
    protected void initData() {
//        user = Plat.accountService.getCurrentUser();
//        showUser(user);
        getUser();
    }

    private void showUser(User user) {
        tvName.setText(Strings.isNullOrEmpty(user.name) ? user.phone : user.name);
        if ("1".equals(user.sex)){
            tvGender.setText("男");
        }else if("2".equals(user.sex)){
            tvGender.setText("女");
        }else {
            tvGender.setText("点击选择");
        }
        tvBirth.setText(null == user.birthday ?  "点击选择" : DateUtil.date2String(user.birthday ,DateUtil.PATTERN_DATE));
        if(null == user.birthday){
            tvBirth.setText("点击选择");
        }else {
            tvBirth.setText(DateUtil.date2String(user.birthday ,DateUtil.PATTERN_DATE));
        }
        showFigure(user.figureUrl);
    }

    /**
     * 设置头像
     * @param figure
     */
    void showFigure(String figure) {
        if (Strings.isNullOrEmpty(figure)) {
            ivPersonPhoto.setImageResource(R.mipmap.ic_user_default_figure);
        } else {
            ImageUtils.displayImage(figure, ivPersonPhoto, Helper.DisplayImageOptions_UserFace);
        }
    }
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.equals(stbUserSex)) {
            selectSex();
        }else if (view.equals(stbUserBirthday)){
            selectDate();
        }else if (view.equals(stbUserName)){
//            UIService.getInstance().postPage(PageKey.MineEditUserNamePage);
            Intent intent = new Intent(getAttachActivity(), EditUserNameActivity.class);
            startActivityForResult(intent, new BaseActivity.OnActivityCallback() {
                @Override
                public void onActivityResult(int resultCode, @Nullable Intent data) {
                    if (resultCode == Activity.RESULT_OK){
                        tvName.setText(data.getStringExtra(EditDeviceNameActivity.DEVICE_NAME));
                    }
                }
            });
        }else if (view.equals(stbUserPhoto)){
            if (pickHelper == null) {
                pickHelper = new PickImageHelperTwo(activity, pickCallback);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.CAMERA);
                if (selfPermission == 0){
                    pickHelper.showPickDialog("");
                }else {
                    PermissionsUtils.checkPermission(cx,Manifest.permission.CAMERA,PermissionsUtils.CODE_USER_INFO_SHARE);
                }
            }else {
                pickHelper.showPickDialog("");
            }

//            selectPhoto();
        } else if (view.equals(ivBack)) {
            UIService.getInstance().popBack();
        }
    }

    /**
     * 图像处理回调
     */
    PickImageHelperTwo.PickCallbackTwo pickCallback = new PickImageHelperTwo.PickCallbackTwo() {

        @Override
        public void onPickComplete(Bitmap bmp) {
            if (bmp == null) {
                return;
            }
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
                            ToastUtils.show(t.getMessage());
                        }
                    });
        }

        @Override
        public void onPickComplete(String bmp) {

        }
    };

    /**
     * 拍照
     */
    private void selectPhoto() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("我的相册");
        new MenuDialog.Builder(cx)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        if ("拍照".equals(string)){
                            pickHelper.camera();
                        }else if ("我的相册".equals(string)){
                            pickHelper.gallery(PickImageHelperTwo.PHOTO_REQUEST_GALLERY);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }
    /**
     * 日期选择器
     */
    private void selectDate(){
        // 日期选择对话框
        new DateDialog.Builder(cx)
                .setTitle(getString(R.string.date_title))
                .setConfirm("确定")
                // 设置 null 表示不显示取消按钮
                .setCancel("取消")
                .setListener(new DateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // ToastUtils.show(year + getString(R.string.common_year) + month + getString(R.string.common_month) + day + getString(R.string.common_day));
                        String dateStr = year + "-" + month + "-" + day ;
                        Date date = DateUtil.string2Date(dateStr, DateUtil.PATTERN_DATE);
                        setUserInfo(date);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }
    /**
     * 性别选择器
     */
    private void selectSex() {
        List<String> data = new ArrayList<>();
        data.add("男");
        data.add("女");
        // 底部选择框
        new MenuDialog.Builder(cx)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        setUserInfo(string.equals("女") ? "2" :"1");
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }
    /**
     * 设置生日
     * @param birthday
     */
    private void setUserInfo(Date birthday) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updateUser(user.id, user.name, user.phone, user.email, user.gender , birthday, user.sex, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                user = Plat.accountService.getCurrentUser();
                showUser(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(t.getMessage());
            }
        });
    }
    /**
     * 设置性别
     * @param sex
     */
    private void setUserInfo(final String sex) {
        //未修改性别,直接结束,减少消耗
        if (sex.equals(user.sex) ){
            return;
        }
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updateUser(user.id, user.name, user.phone, user.email, false,  user.birthday, sex ,new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                user = Plat.accountService.getCurrentUser();
                showUser(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(t.getMessage());
            }
        });
    }


    @Subscribe
    public void onEvent(UserUpdatedEvent event) {
        user = Plat.accountService.getCurrentUser();
        showUser(user);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionsUtils.CODE_USER_INFO_SHARE == requestCode){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    pickHelper.showPickDialog("");
                }
            }
        }

    }

    /**
     * 获取用户信息3.7
     */
    public void getUser() {
        if (Plat.accountService.isLogon()) {
            ProgressDialogHelper.setRunning(cx, true);
            CloudHelper.getUser2(Plat.accountService.getCurrentUserId(), new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    ProgressDialogHelper.setRunning(cx, false);
                    Plat.accountService.onLogin(user);
                    MineEditInfoPage.this.user = user ;
                    showUser(user);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show(t.getMessage());
                }
            });
        }
    }
}
