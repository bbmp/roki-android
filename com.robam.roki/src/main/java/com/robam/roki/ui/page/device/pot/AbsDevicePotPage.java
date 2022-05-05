package com.robam.roki.ui.page.device.pot;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.PotAlarmEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.PotOilTempParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.PotOtherFuncAdapter;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.ui.view.SportProgressView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/5/7.
 * PS: Not easy to write code, please indicate.
 */
public class AbsDevicePotPage<Device extends Pot> extends DeviceCatchFilePage {

    Device pot;
    String mGuid;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    @InjectView(R.id.tv_off_line_text)
    TextView mTvOffLineText;
    @InjectView(R.id.ll_oil_temp_name)
    TextView mLlOilTempName;
    @InjectView(R.id.tv_oil_temp_desc)
    TextView mTvOilTempDesc;
    @InjectView(R.id.ll_oil_temp_name_desc)
    LinearLayout mLlOilTempNameDesc;
    @InjectView(R.id.ll_low_power)
    LinearLayout mLlLowPower;
    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout mLayoutBg;
    @InjectView(R.id.spv)
    SportProgressView mSpv;
    @InjectView(R.id.iv_low_power)
    ImageView mIvLowPower;
    @InjectView(R.id.tv_low_power)
    TextView mTvLowPower;
    private PotOilTempParams mPotOilTempParams;
    private PotOtherFuncAdapter mPotOtherFuncAdapter;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.pojo.getID()))
            return;
        pot = (Device) event.pojo;
        mSpv.setProgress((int) event.pojo.tempUp);
        if (event.pojo.isConnected()) {
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.GONE);
            }
        }
        List<PotOilTempParams.OnLinBean> onLin = mPotOilTempParams.getOnLin();
        for (int i = 0; i < onLin.size(); i++) {
            int min = onLin.get(i).getMin();
            int max = onLin.get(i).getMax();
            if (pot.tempUp >= min && pot.tempUp <= max) {
                mLlOilTempName.setText(onLin.get(i).getStatue());
                mTvOilTempDesc.setText(onLin.get(i).getTips());
            }
        }

        if (event.pojo.potStatus == 3) {//低电量状态
            mLlLowPower.setVisibility(View.VISIBLE);
        } else {
            mLlLowPower.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(PotAlarmEvent event) {
        short alarmId = event.alarmId;
        if (alarmId == 1) {
            mLlLowPower.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.device.getID())) {
            return;
        }
        if (!event.device.isConnected()) {
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.VISIBLE);
                PotOilTempParams.OffLinBean offLin = mPotOilTempParams.getOffLin();
                mLlOilTempName.setText(offLin.getStatue());
                mTvOilTempDesc.setText(offLin.getTips());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        pot = Plat.deviceService.lookupChild(mGuid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_pot, container, false);
        ButterKnife.inject(this, view);
        initData();
        mSpv.setProgress((int) pot.tempUp);
        return view;
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {
        try {
            if (mDevice instanceof Pot) {
                pot = (Device) mDevice;
            }
            ModelMap modelMap = deviceResponse.modelMap;
            mTvDeviceModelName.setText(deviceResponse.title);
            Glide.with(cx).load(deviceResponse.viewBackgroundImg).into(mIvBg);
            //电量  温度描述
            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
            List<DeviceConfigurationFunctions> backgroundFunctions = backgroundFunc.deviceConfigurationFunctions;
            for (int i = 0; i < backgroundFunctions.size(); i++) {
                if ("lowEnergy".equals(backgroundFunctions.get(i).functionCode)) {
                    String functionName = backgroundFunctions.get(i).functionName;
                    mTvLowPower.setText(functionName);
                    String backgroundImg = backgroundFunctions.get(i).backgroundImg;
                    Glide.with(cx).load(backgroundImg).into(mIvLowPower);
                } else if ("temperatureTips".equals(backgroundFunctions.get(i).functionCode)) {
                    String functionParams = backgroundFunctions.get(i).functionParams;
                    mPotOilTempParams = JsonUtils.json2Pojo(functionParams, PotOilTempParams.class);
                }
            }

            List<PotOilTempParams.OnLinBean> onLin = mPotOilTempParams.getOnLin();
            for (int i = 0; i < onLin.size(); i++) {
                if (pot.tempUp >= onLin.get(i).getMin() && pot.tempUp <= onLin.get(i).getMax()) {
                    mLlOilTempName.setText(onLin.get(i).getStatue());
                    mTvOilTempDesc.setText(onLin.get(i).getTips());
                }
            }

            OtherFunc otherFunc = modelMap.otherFunc;
            mDeviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
            mPotOtherFuncAdapter = new PotOtherFuncAdapter(cx, pot, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    otherFuncItemEvent(view);
                }
            });
            mRecyclerview.setAdapter(mPotOtherFuncAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                    (cx, LinearLayoutManager.VERTICAL, false);
            mRecyclerview.setLayoutManager(linearLayoutManager);
            mRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));
            if (!pot.isConnected()) {
                ToastUtils.showLong(R.string.device_new_connected);
                PotOilTempParams.OffLinBean offLin = mPotOilTempParams.getOffLin();
                mLlOilTempName.setText(offLin.getStatue());
                mTvOilTempDesc.setText(offLin.getTips());
                if (mTvOffLineText != null) {
                    mTvOffLineText.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otherFuncItemEvent(View view) {

        String tag = (String) view.getTag(R.id.tag_pot_other_func_key);

        switch (tag) {
            case "automatiCooking":
                List<AbsFan> fanList = Utils.getFan();
                List<Stove> stoves = new ArrayList<>();
                for (int i = 0; i < fanList.size(); i++) {
                    if (fanList.get(i).getChildStove() != null) {
                        stoves.add((Stove) fanList.get(i).getChildStove());
                    }
                }
                if (!pot.isConnected()) {
                    ToastUtils.show(R.string.oven_dis_con, Toast.LENGTH_SHORT);
                    return;
                }

                if (stoves.size() == 0) {
                    ToastUtils.show(R.string.device_stove_not, Toast.LENGTH_SHORT);
                    return;
                }
                Bundle bundleCook = new Bundle();
                bundleCook.putString(PageArgumentKey.RecipeId, IDeviceType.RZNG);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundleCook);
                break;
            case "tobaccoPotLinkage":
                if (!pot.isConnected()) {
                    ToastUtils.showLong(R.string.device_new_connected);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.Bean, pot);
                bundle.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                UIService.getInstance().postPage(PageKey.DevicePotFanLink, bundle);
                break;
            case "dryBurningWarning":
                if (!pot.isConnected()) {
                    ToastUtils.showLong(R.string.device_new_connected);
                    return;
                }
                Bundle bundleDry = new Bundle();
                bundleDry.putSerializable(PageArgumentKey.Bean, pot);
                bundleDry.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
                UIService.getInstance().postPage(PageKey.DevicePotFanDry, bundleDry);
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, pot.getID());
        bd.putString(PageArgumentKey.pot, "pot");
        UIService.getInstance().postPage(PageKey.PotDeviceMore, bd);
    }
}
