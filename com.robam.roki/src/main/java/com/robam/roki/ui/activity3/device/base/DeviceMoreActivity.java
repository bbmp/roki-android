package com.robam.roki.ui.activity3.device.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.j256.ormlite.stmt.query.In;
import com.robam.base.BaseActivity;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.activity3.EditDeviceNameActivity;
import com.robam.roki.ui.activity3.device.base.adapter.RvDeviceMoreAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.RvWorkModeAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.bean.WorkModeBean;
import com.robam.roki.ui.activity3.device.base.view.MyCircleProgress;
import com.robam.roki.utils.DialogUtil;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/23
 * desc   : 设备更多界面
 */
public abstract class DeviceMoreActivity extends DeviceBaseFuntionActivity {


    private RecyclerView rvMoreFunction;
    private RvDeviceMoreAdapter rvDeviceMoreAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_more;
    }

    @Override
    protected void initView() {

        rvMoreFunction = findViewById(R.id.rv_more_function);
        rvMoreFunction.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false ));
        rvDeviceMoreAdapter = new RvDeviceMoreAdapter();
        rvMoreFunction.setAdapter(rvDeviceMoreAdapter);
        rvDeviceMoreAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                String more = rvDeviceMoreAdapter.getItem(i);
                handleClick(more);
            }
        });
    }

    protected  void handleClick(String more){
        Intent intent = new Intent();
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        switch (more){
            case "设备名称":
                setDeviceName();
                break;
            case "留言咨询":
                intent.setClass(this, DeviceChatActivity.class);
                startActivity(intent);
                break;
            case "一键售后":
                callAfterSale();
                break;
            case "产品信息":
                intent.setClass(this, DeviceInfoActivity.class);
                startActivity(intent);
                break;
            default:
                otherOnClick(more);
                break;
        }
    }

    /**
     * 设置设备名称
     */
    private void setDeviceName() {
        Intent intent = new Intent(this, EditDeviceNameActivity.class);
        intent.putExtra(EditDeviceNameActivity.EDIT_BD , bundle);
        startActivityForResult(intent, new OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                if (resultCode == Activity.RESULT_OK){
                    mDevice.setName(data.getStringExtra(EditDeviceNameActivity.DEVICE_NAME));
                    rvDeviceMoreAdapter.setDevice(mDevice);
                }
            }
        });
    }

    /**
     * 一键售后
     */
    public void callAfterSale() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_00);
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
                Uri uri = Uri.parse(String.format("tel:%s", getString(R.string.after_sale_phone)));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        setTitle("更多");
        //设置对应设备
        rvDeviceMoreAdapter.setDevice(mDevice);
        //设置adapter数据
        rvDeviceMoreAdapter.addData(initMoreData());
    }

    /**
     * 更多数据
     * @return
     */

    protected abstract List<String> initMoreData();

    /**
     * 其他非公共点击事件
     * @param more
     */
    protected abstract void otherOnClick(String more);
}