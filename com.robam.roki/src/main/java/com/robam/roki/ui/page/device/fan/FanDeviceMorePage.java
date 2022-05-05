package com.robam.roki.ui.page.device.fan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/6/6.
 * 烟机更多页
 */

public class FanDeviceMorePage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;
    private AbsMoreAdapter mMoreAdapter;
    public List<DeviceMoreBean> mDatas = new ArrayList<>();
    String mGuid;
    protected String mUrl;
    private IDevice iDevice;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mUrl = bd == null ? null : bd.getString(PageArgumentKey.Url);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);


        if (mGuid == null) {
            return;
        }
        iDevice = Plat.deviceService.lookupChild(mGuid);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (iDevice == null) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_more, container, false);
        ButterKnife.inject(this, view);
        initData();
        initView();
        return view;
    }

    private void initView() {

        mMoreAdapter = new AbsMoreAdapter(cx, mDatas);
        mMoreAdapter.setOnItemRecycleClickLister(new AbsMoreAdapter.OnItemRecycleClick() {
            @Override
            public void onItemClick(View v, int position) {
                if ("8235S".equals(iDevice.getDt()) || "8236S".equals(iDevice.getDt())) {
                    initItemClick2(v, position);
                }else {
                    initItemClick(v, position);
                }

            }
        });
        mRecyclerViewMore.setAdapter(mMoreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewMore.setLayoutManager(linearLayoutManager);
        mRecyclerViewMore.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {
        if ("8235S".equals(iDevice.getDt()) || "8236S".equals(iDevice.getDt())) {
            DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
            deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
            deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
            deviceMoreBean.setType(1);
            DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
            deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
            deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
            deviceMoreBean1.setType(1);

            DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
            deviceMoreBean3.setName(cx.getString(R.string.fan_dialog_setting_product_information));
            deviceMoreBean3.setImageRes(R.mipmap.img_product_information);
            deviceMoreBean3.setType(1);

            mDatas.add(deviceMoreBean);
            mDatas.add(deviceMoreBean1);
            mDatas.add(deviceMoreBean3);
        }else{
            DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
            deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
            deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
            deviceMoreBean.setType(1);
            DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
            deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
            deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
            deviceMoreBean1.setType(1);
            DeviceMoreBean deviceMoreBean2 = new DeviceMoreBean();
            deviceMoreBean2.setName(cx.getString(R.string.fan_oil_dismantling_title));
            deviceMoreBean2.setImageRes(R.mipmap.img_net_oil_removal);
            deviceMoreBean2.setType(1);

            DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
            deviceMoreBean3.setName(cx.getString(R.string.fan_dialog_setting_product_information));
            deviceMoreBean3.setImageRes(R.mipmap.img_product_information);
            deviceMoreBean3.setType(1);

            mDatas.add(deviceMoreBean);
            mDatas.add(deviceMoreBean1);
            mDatas.add(deviceMoreBean2);
            mDatas.add(deviceMoreBean3);
        }









    }

    public void initItemClick(View v, int position) {
        switch (position) {
            case 0:
                UIService.getInstance().postPage(PageKey.Chat);
                break;
            case 1:
                callAfterSale();
                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.Url, mUrl);
                bundle.putSerializable(PageArgumentKey.title, title);
                bundle.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bundle);
                break;
            case 3:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                break;
            default:
                break;
        }

    }

    public void initItemClick2(View v, int position) {
        switch (position) {
            case 0:
                UIService.getInstance().postPage(PageKey.Chat);
                break;
            case 1:
                callAfterSale();
                break;
            case 2:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                break;
            default:
                break;
        }

    }

    private void callAfterSale() {
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

    @OnClick(R.id.iv_back)
    public void onViewClicked() {

        UIService.getInstance().popBack();
    }
}
