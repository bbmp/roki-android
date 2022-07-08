package com.robam.roki.ui.page.device;

import static com.legent.plat.constant.IPlatRokiFamily.R0004;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BaseActivity;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.EditDeviceNameActivity;
import com.robam.roki.ui.activity3.device.base.DeviceInfoActivity;import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter3.RvDeviceMoreAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/1.
 */

public abstract class AbsDeviceMorePage extends MyBasePage<MainActivity> {
//    @InjectView(R.id.iv_back)
//    ImageView mIvBack;
//    @InjectView(R.id.tv_device_model_name)
//    TextView mTvDeviceModelName;
    @InjectView(R.id.tb_title)
    TitleBar tbBar;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;
    @InjectView(R.id.ll_default_head)
    LinearLayout ll_default_head;

    private RvDeviceMoreAdapter mMoreAdapter;
    public List<DeviceMoreBean>mDatas=new ArrayList<>();
    public String mGuid;
    protected String mUrl;
    public List<DeviceConfigurationFunctions> hideFunctions;
    private Bundle bd;

    public abstract void subItemOnClick(View v, int position);

    public abstract void subInitData();
    public IDevice iDevice;



    @Override
    protected int getLayoutId() {
        return R.layout.page_device_more;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         bd = getArguments();
//        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
//        iDevice = Plat.deviceService.lookupChild(mGuid);
//        mUrl = bd == null ? null : bd.getString(PageArgumentKey.Url);
//        title = bd == null ? null : bd.getString(PageArgumentKey.title);
//        hideFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
//        LogUtils.i("20180601", "mguid:33:" + mGuid);
//        View view = inflater.inflate(R.layout.page_device_more, container, false);
//        ButterKnife.inject(this, view);
//        initData();
//        initView();
//        return view;
//    }
    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewMore.setLayoutManager(linearLayoutManager);
        mMoreAdapter = new RvDeviceMoreAdapter();
        mRecyclerViewMore.setAdapter(mMoreAdapter);
        mMoreAdapter.addData(mDatas);
        mMoreAdapter.addChildClickViewIds(R.id.stb_more);
        mMoreAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter baseQuickAdapter, @NonNull View view, int position) {
                initItemClick(view, position);
            }
        });
//        mMoreAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int position) {
//
//            }
//        });

//        mMoreAdapter.setOnItemRecycleClickLister(new AbsMoreAdapter.OnItemRecycleClick() {
//            @Override
//            public void onItemClick(View v, int position) {
//                initItemClick(v, position);
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (iDevice==null) {
            return;
        }
        if (iDevice.getDt()!=null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(),iDevice.getDt()+":更多页",null);
        }

    }
    @Override
    protected void initView() {

        bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        iDevice = Plat.deviceService.lookupChild(mGuid);
        mUrl = bd == null ? null : bd.getString(PageArgumentKey.Url);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        hideFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        LogUtils.i("20180601", "mguid:33:" + mGuid);
//        View view = inflater.inflate(R.layout.page_device_more, container, false);
//        ButterKnife.inject(this, view);
        if(iDevice instanceof Pot &&iDevice.getDt().equals(R0004)){
            ll_default_head.setVisibility(View.VISIBLE);
        }


        DeviceMoreBean deviceName = new DeviceMoreBean();
        deviceName.setName("设备名称");
        deviceName.setDeviceName(iDevice.getName() == null  || iDevice.getName().equals(iDevice.getCategoryName()) ? iDevice.getDispalyType() : iDevice.getName());
        deviceName.setImageRes(R.mipmap.img_product_information);
        deviceName.setType(1);
        mDatas.add(deviceName);

        subInitData();
        tbBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View view) {
                UIService.getInstance().popBack();
            }

            @Override
            public void onTitleClick(View view) {

            }

            @Override
            public void onRightClick(View view) {

            }
        });


        if (mGuid.contains("920")){

        }





//        mRecyclerViewMore.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));
    }

    public void initItemClick(View v, int position) {
        if (position == 0){
//            bd.putSerializable(PageArgumentKey.deviceName , iDevice.getName() == null ? iDevice.getDt() : iDevice.getName());
//            UIService.getInstance().postPage(PageKey.EditDeviceNamePage , bd);
            Intent intent = new Intent(getAttachActivity(), EditDeviceNameActivity.class);
            intent.putExtra(EditDeviceNameActivity.EDIT_BD , bd);
            startActivityForResult(intent, new BaseActivity.OnActivityCallback() {
                @Override
                public void onActivityResult(int resultCode, @Nullable Intent data) {
                    if (resultCode == Activity.RESULT_OK){
                        DeviceMoreBean deviceName = new DeviceMoreBean();
                        deviceName.setName("设备名称");
                        deviceName.setDeviceName(data.getStringExtra(EditDeviceNameActivity.DEVICE_NAME));
                        deviceName.setImageRes(R.mipmap.img_product_information);
                        deviceName.setType(1);
                        mMoreAdapter.setData(0 , deviceName);
                    }
                }
            });
            return;
        }
        subItemOnClick(v, position);

    }

    public void callAfterSale() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialog.setTitleText(R.string.after_sale_phone);
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
                Uri uri = Uri.parse(String.format("tel:%s", cx.getString(R.string.after_sale_phone)));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
//
//    @OnClick(R.id.iv_back)
//    public void onViewClicked() {
//        UIService.getInstance().popBack();
//    }


}
