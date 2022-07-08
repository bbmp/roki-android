package com.robam.roki.ui.activity3.device.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.common.collect.Lists;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.roki.R;

import com.robam.roki.ui.page.DeviceDetailPage;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.InjectView;

/**
 * author : huxw
 * time   : 2022/06/23
 * desc   : 设备信息界面
 */
public  class DeviceInfoActivity extends DeviceBaseFuntionActivity {


    AbsDeviceHub deviceHub;
    AbsDevice absDevice;
    private String id;
    private RecyclerView deviceListView;
    private RecyclerView userListView;

    private DeviceAdapter deviceAdapter;
    private UserAdapter userAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void initView() {
        deviceListView = findViewById(R.id.deviceListView);
        userListView = findViewById(R.id.userListView);

        deviceListView.setLayoutManager(new LinearLayoutManager(getContext()));
        userListView.setLayoutManager(new LinearLayoutManager(getContext()));
        deviceAdapter = new DeviceAdapter();
        userAdapter = new UserAdapter();

        deviceListView.setAdapter(deviceAdapter);
        userListView.setAdapter(userAdapter);
    }


    @Override
    protected void dealData() {
        setTitle("设备信息");
        if (IDeviceType.RRQZ.equals(mDevice.getDc()) || IDeviceType.RDCZ.equals(mDevice.getDc())
                || IDeviceType.RZNG.equals(mDevice.getDc())) {
            mDevice = Plat.deviceService.lookupChild(mGuid);
        } else if ("R0003".equals(mGuid.substring(0, 5))) {
            absDevice = Plat.deviceService.lookupChild(mGuid);
        } else {
            deviceHub = Plat.deviceService.lookupChild(mGuid);
        }
        initDeviceData();
        initUserDate();
    }


    //设备信息
    private void initDeviceData() {
        List<IDevice> devices = Lists.newArrayList();
        if (IDeviceType.RRQZ.equals(mDevice.getDc()) || IDeviceType.RDCZ.equals(mDevice.getDc())
                || IDeviceType.RZNG.equals(mDevice.getDc())) {
            devices.add(mDevice);
            id = mDevice.getID();
        } else if (IRokiFamily.R0003.equals(mGuid.substring(0, 5))) {
            devices.add(absDevice);
            id = absDevice.getID();
        } else {
            devices.add(deviceHub);
            Stove stove = deviceHub.getChild();
            Pot pot = deviceHub.getChildPot();
            List<GasSensor> gasSensors = deviceHub.getChildGas();
            if (stove != null) {
                devices.add(stove);
            }
            if (pot != null){
                devices.add(pot);
            }
            if (gasSensors != null && gasSensors.size() > 0) {
                for (int i = 0; i < gasSensors.size(); i++) {
                    devices.add(gasSensors.get(i));
                }
            }
            id = deviceHub.getID();
        }

        deviceAdapter.setList(devices);
    }

    //用户信息
    private void initUserDate() {
        long ownerId = Plat.accountService.getCurrentUserId();
        Plat.deviceService.getDeviceUsers(ownerId, id, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if (users == null || users.size() == 0) return;
                LayoutInflater inflater = LayoutInflater.from(DeviceInfoActivity.this);

                userAdapter.setList(users);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }
    void showData(BaseViewHolder baseViewHolder, IDevice device) {
        DeviceGuid dg = device.getGuid();
        DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(dg.getGuid());

        String dispalyType = device.getDispalyType();
        baseViewHolder.setText(R.id.txtDeviceType, dispalyType);
        baseViewHolder.setText(R.id.txtDevice, device.getCategoryName() + getString(R.string.detail));
        baseViewHolder.setText(R.id.txtBid, device.getBid());
        baseViewHolder.setText(R.id.txtOtaVer, String.valueOf(device.getVersion()));

    }
    class DeviceAdapter extends BaseQuickAdapter<IDevice, BaseViewHolder> {

        public DeviceAdapter() {
            super(R.layout.view_device_detail);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, IDevice iDevice) {
            showData(baseViewHolder, iDevice);

        }
    }
    class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

        public UserAdapter() {
            super(R.layout.view_device_user_item);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, User user) {
            baseViewHolder.setText(R.id.txtUserName, user.name);
            baseViewHolder.setText(R.id.txtDesc, user.phone);
        }

    }
}