package com.robam.roki.ui.page.device;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/1.
 */

public abstract class AbsDeviceMorePage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;

    private AbsMoreAdapter mMoreAdapter;
    public List<DeviceMoreBean>mDatas=new ArrayList<>();
    public String mGuid;
    protected String mUrl;
    public List<DeviceConfigurationFunctions> hideFunctions;
    public abstract void subItemOnClick(View v, int position);

    public abstract void subInitData();
    public IDevice iDevice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        iDevice = Plat.deviceService.lookupChild(mGuid);
        mUrl = bd == null ? null : bd.getString(PageArgumentKey.Url);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        hideFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        LogUtils.i("20180601", "mguid:33:" + mGuid);
        View view = inflater.inflate(R.layout.page_device_more, container, false);
        ButterKnife.inject(this, view);
        initData();
        initView();
        return view;
    }

    public void initData() {
        subInitData();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (iDevice==null) {
            return;
        }

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

    public void initItemClick(View v, int position) {
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

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


}
