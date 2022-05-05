package com.robam.roki.ui.page.device.rika;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Created by 14807 on 2018/6/7.
 */

public class RikaDeviceMorePage extends BasePage {


    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;
    private AbsMoreAdapter mMoreAdapter;
    public List<DeviceMoreBean> mDatas = new ArrayList<>();
    String mGuid;
    protected String mUrl;
    protected String mTitle;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mUrl = bd == null ? null : bd.getString(PageArgumentKey.Url);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGuid == null) {
            return;
        }
        IDevice iDevice = Plat.deviceService.lookupChild(mGuid);

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
                initItemClick(v, position);
            }
        });
        mRecyclerViewMore.setAdapter(mMoreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewMore.setLayoutManager(linearLayoutManager);
        mRecyclerViewMore.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));

    }

    private void initData() {

        DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
        deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
        deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
        deviceMoreBean.setType(1);

        DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
        deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
        deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
        deviceMoreBean1.setType(1);

        DeviceMoreBean deviceMoreBean2 = new DeviceMoreBean();
        deviceMoreBean2.setName(cx.getString(R.string.fan_dialog_setting_product_information));
        deviceMoreBean2.setImageRes(R.mipmap.img_product_information);
        deviceMoreBean2.setType(1);


        DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
        deviceMoreBean3.setName(cx.getString(R.string.fan_oil_dismantling_title));
        deviceMoreBean3.setImageRes(R.mipmap.img_net_oil_removal);
        deviceMoreBean3.setType(1);


        mDatas.add(deviceMoreBean);
        mDatas.add(deviceMoreBean1);
        mDatas.add(deviceMoreBean2);
        mDatas.add(deviceMoreBean3);
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
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                break;
            case 3:
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.Url, mUrl);
                bundle.putSerializable(PageArgumentKey.title, mTitle);
                UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bundle);
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
