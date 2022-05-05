package com.robam.roki.ui.page;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/25.
 * 产品信息页面
 */

public class DeviceInformationPage extends BasePage {

    @InjectView(R.id.ll_information)
    LinearLayout mLInformation;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.ll_user)
    LinearLayout mLlUser;
    AbsDeviceHub deviceHub;
    AbsDevice absDevice;
    String guid;
    private IDevice mIDevice;

    String id;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        guid = bd.getString(PageArgumentKey.Guid);
        mIDevice = Plat.deviceService.lookupChild(guid);
        if (IDeviceType.RRQZ.equals(mIDevice.getDc()) || IDeviceType.RDCZ.equals(mIDevice.getDc())
                || IDeviceType.RZNG.equals(mIDevice.getDc())) {
            mIDevice = Plat.deviceService.lookupChild(guid);
        } else if ("R0003".equals(guid.substring(0, 5))) {
            absDevice = Plat.deviceService.lookupChild(guid);
        } else {
            deviceHub = Plat.deviceService.lookupChild(guid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_information, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIDevice==null) {
            return;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDeviceData();
        initUserDate();
    }

    //设备信息
    private void initDeviceData() {
        List<IDevice> devices = Lists.newArrayList();
        if (IDeviceType.RRQZ.equals(mIDevice.getDc()) || IDeviceType.RDCZ.equals(mIDevice.getDc())
                || IDeviceType.RZNG.equals(mIDevice.getDc())) {
            devices.add(mIDevice);
            id = mIDevice.getID();
        } else if (IRokiFamily.R0003.equals(guid.substring(0, 5))) {
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
        mLInformation.removeAllViews();
        for (final IDevice device : devices) {
            LayoutInflater inflater = LayoutInflater.from(cx);
            if (inflater == null)
                return;
            View view = inflater.inflate(R.layout.item_information_device_page, null, false);
            ScreenAdapterTools.getInstance().loadView(view);
            TextView tvDcName = view.findViewById(R.id.tv_dc_name);
            TextView tvModel = view.findViewById(R.id.tv_model);
            TextView tvCoding = view.findViewById(R.id.tv_coding);
            TextView tvVersion = view.findViewById(R.id.tv_firmware_version);
            tvModel.setText(device.getDispalyType());
            tvCoding.setText(device.getBid());
            tvVersion.setText(String.valueOf(device.getVersion()));
            tvDcName.setText(device.getCategoryName());
//            deviceCategoryName(tvDcName, device.getDc());
            mLInformation.addView(view);
        }
    }

    //用户信息
    private void initUserDate() {
        long ownerId = Plat.accountService.getCurrentUserId();
        Plat.deviceService.getDeviceUsers(ownerId, id, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if (users == null || users.size() == 0) return;
                LayoutInflater inflater = LayoutInflater.from(cx);
                mLlUser.removeAllViews();
                for (final User user : users) {
                    if (inflater == null)
                        return;
                    View view = inflater.inflate(R.layout.item_information_user_page, null, false);
                    ScreenAdapterTools.getInstance().loadView(view);
                    TextView tvName = view.findViewById(R.id.tv_user_name);
                    TextView tvDesc = view.findViewById(R.id.tv_user_desc);
                    tvName.setText(user.name);
                    tvDesc.setText(user.phone);
                    mLlUser.addView(view);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    //根据品类设置Name
    private void deviceCategoryName(TextView dcName, String dc) {
        switch (dc) {
            case IDeviceType.RYYJ:
                dcName.setText(R.string.fan_dc_name);
                break;
            case IDeviceType.RRQZ:
            case IDeviceType.RDCZ:
                dcName.setText(R.string.stove_dc_name);
                break;
            case IDeviceType.RQCG:
                dcName.setText(R.string.gas_dc_name);
                break;
            case IDeviceType.RXDG:
                dcName.setText(R.string.sterilizer_dc_name);
                break;
            case IDeviceType.RDKX:
                dcName.setText(R.string.oven_dc_name);
                break;
            case IDeviceType.RZQL:
                dcName.setText(R.string.steam_dc_name);
                break;
            case IDeviceType.RWBL:
                dcName.setText(R.string.microwave_dc_name);
                break;
            case IDeviceType.RZKY:
                dcName.setText(R.string.steamOvenOne_dc_name);
                break;
            case IDeviceType.RJSQ:
                dcName.setText(R.string.waterPurifier_dc_name);
                break;
            case IDeviceType.RIKA:
                dcName.setText(R.string.rika_zh_name);
                break;
            case IDeviceType.RXWJ:
                dcName.setText(R.string.dishWash_dc_name);
                break;
            case IDeviceType.RPOT:
                dcName.setText(R.string.pot_dc_name);
                break;
        }
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
