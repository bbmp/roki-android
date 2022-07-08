package com.robam.roki.ui.page;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.RvTellRokiImageAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.helper3.UploadFileHelper;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.PickImageHelperNew;

import org.eclipse.jetty.util.MultiPartInputStream;

import java.io.File;
import java.util.ArrayList;


import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 告诉ROKI
 */
public class TellRokiPage extends MyBasePage<MainActivity> {

    private static final String TAG = "TellRokiPage";
    @InjectView(R.id.et_say_sth)
    EditText sayDescEt;
    @InjectView(R.id.et_cellphone)
    EditText cellPhoneEt;
    @InjectView(R.id.rv_tell_roki_image)
    RecyclerView rv_tell_roki_image ;
    private String mCellPhone;
    private String mSayDesc;
    private long userId = Plat.accountService.getCurrentUserId();
    private RvTellRokiImageAdapter rvTellRokiImageAdapter;
    /**
     * 头像处理帮助类（原逻辑见UserInfoPage）
     */
    PickImageHelperNew pickHelper;
    private ArrayList<File> bitmaps;
    private ArrayList<String> bitMapString;

    @Override
    protected int getLayoutId() {
        return R.layout.page_tell_roki;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL, false);
        rv_tell_roki_image.setLayoutManager(linearLayoutManager);
        rvTellRokiImageAdapter = new RvTellRokiImageAdapter();
        rvTellRokiImageAdapter.addChildClickViewIds(R.id.iv_tell_roki_camera ,R.id.iv_close);
        rv_tell_roki_image.setAdapter(rvTellRokiImageAdapter);
        rvTellRokiImageAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.iv_tell_roki_camera){
                    if (bitmaps.get(position) == null){
                        pickImage();
                    }
                }else if (view.getId() == R.id.iv_close){
                    bitmaps.remove(position);
                    if (bitMapString.size() > position){
                        bitMapString.remove(position);
                    }
                    rvTellRokiImageAdapter.setList(bitmaps);
                }
            }
        });
    }



    @Override
    protected void initData() {
        bitmaps = new ArrayList<>();
        bitmaps.add(null);
        rvTellRokiImageAdapter.setList(bitmaps);
        if (Plat.accountService.isLogon()){
            cellPhoneEt.setText(Plat.accountService.getCurrentUser().getPhone());
        }
        bitMapString = new ArrayList<>();

    }

    /**
     * 拍照
     */
    private void pickImage(){
        if (pickHelper == null) {
            pickHelper = new PickImageHelperNew(activity, pickCallback);

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
    }

    /**
     * 图像处理回调
     */
    PickImageHelperNew.PickCallbackTwo pickCallback = new PickImageHelperNew.PickCallbackTwo() {

        @Override
        public void onPickComplete(File bmp) {
            if (bmp == null) {
                return;
            }

            bitmaps.add(bitmaps.size() - 1 ,bmp);
            rvTellRokiImageAdapter.setList(bitmaps);
            upLoad(bmp);
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.img_back)
    public void onBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.btn_ok)
    public void onOkClicked() {
        mCellPhone = cellPhoneEt.getText().toString();
        mSayDesc = sayDescEt.getText().toString();
        if (!TextUtils.isEmpty(mSayDesc)) {
            if (Plat.accountService.isLogon()){
                updateToROKI(mCellPhone,  mSayDesc);
            }else {
                CmccLoginHelper.getInstance().toLogin();
            }

        } else {
            ToastUtils.show("请输入您的意见或建议");
        }

    }

    private void upLoad(File file){

        UploadFileHelper.upload("2" , file, new Callback<Reponses.UploadRepones>() {
            @Override
            public void onSuccess(Reponses.UploadRepones uploadRepones) {
                ToastUtils.show(uploadRepones.msg);
                if (uploadRepones.rc == 0){
                    bitMapString.add(uploadRepones.uuid);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void updateToROKI(String cellPhone, String sayDesc) {
        ProgressDialogHelper.setRunning(cx, true);
        userId = Plat.accountService.getCurrentUserId();
        while (bitMapString.size()<3){
            bitMapString.add("");
        }
        RokiRestHelper.submitSuggestApply(cellPhone, bitMapString.get(0), bitMapString.get(1), bitMapString.get(2), sayDesc, userId, new Callback<Reponses.SuggestApplyReponse>() {
            @Override
            public void onSuccess(Reponses.SuggestApplyReponse suggestApplyReponse) {
                ProgressDialogHelper.setRunning(cx, false);
                LogUtils.i(TAG, "onSuccess:" + suggestApplyReponse.msg);
                UIService.getInstance().postPage(PageKey.TellRokiSucess);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i(TAG, "onFailure:" + t.toString());
                com.hjq.toast.ToastUtils.show(t.getMessage());
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }
    @Subscribe
    public void onEvent(UserLoginEvent event) {
        cellPhoneEt.setText(Plat.accountService.getCurrentUser().getPhone());
    }

}
