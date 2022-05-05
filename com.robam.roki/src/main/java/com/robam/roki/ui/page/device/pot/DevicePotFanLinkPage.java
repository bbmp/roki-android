package com.robam.roki.ui.page.device.pot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DevicePotVentilationParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by 14807 on 2018/5/3.
 * 烟锅联动页面
 */

public class DevicePotFanLinkPage extends BasePage {

    Pot pot;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_pot_name)
    TextView mTvPotName;
    @InjectView(R.id.chkIsInternalDays)
    CheckBoxView mChkIsInternalDays;
    @InjectView(R.id.tv_pot_folding)
    TextView mTvPotFolding;
    private List<DeviceConfigurationFunctions> mDates;

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.pojo.getID()))
            return;
        pot = event.pojo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pot_ventilation, container, false);
        Bundle bd = getArguments();
        mDates = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        pot = bd == null ? null : (Pot) bd.getSerializable(PageArgumentKey.Bean);
        ButterKnife.inject(this, view);
        initDate(mDates);
        getFanPotLinkValue("init");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pot == null) {
            return;
        }
    }

    private void getFanPotLinkValue(final String value) {

        pot.getFanPotLink(new Callback() {
            @Override
            public void onSuccess(Object o) {
                if (value.equals("set")) {
                    ToastUtils.showShort(R.string.device_sterilizer_succeed);
                }
                if (pot.mFanPotSwitch == 1) {
                    if (mChkIsInternalDays != null) {
                        mChkIsInternalDays.setChecked(true);
                    }
                } else {
                    if (mChkIsInternalDays != null) {
                        mChkIsInternalDays.setChecked(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (value.equals("set")) {
                    ToastUtils.showShort(R.string.toast_problems_text);
                }
            }
        });

    }

    private void initDate(List<DeviceConfigurationFunctions> dates) {

        if (dates == null || dates.size() == 0) return;
        for (int i = 0; i < dates.size(); i++) {
            if ("tobaccoPotLinkage".equals(dates.get(i).functionCode)) {
                String title = dates.get(i).functionName;
                mTvDeviceModelName.setText(title);
                mTvPotName.setText(title);
                List<DeviceConfigurationFunctions> functions = dates.get(i).subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        try {
                            DevicePotVentilationParams devicePotVentilationParams = JsonUtils.json2Pojo(functionParams, DevicePotVentilationParams.class);
                            mTvPotFolding.setText(devicePotVentilationParams.getParam().getDesc().getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
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

    @OnClick(R.id.chkIsInternalDays)
    public void onMChkIsInternalDaysClicked() {

        if (pot == null) return;
        switch (pot.mPotBurningWarnSwitch) {
            case 0:
                setFanPotLinkValue(2);
                break;
            case 1:
                setFanPotLinkValue(3);
                break;
            case 2:
                setFanPotLinkValue(0);
                break;
            case 3:
                setFanPotLinkValue(1);
                break;
        }
    }

    private void setFanPotLinkValue(int value) {
        pot.setFanPotLink((short) value, new VoidCallback() {
            @Override
            public void onSuccess() {
                getFanPotLinkValue("set");
            }

            @Override
            public void onFailure(Throwable t) {
                setFanPotLinkValue(3);
            }
        });
    }


    //恢复出厂点击
    @OnClick(R.id.tv_recovery)
    public void onMTvRecoveryClicked() {

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText(R.string.title_leave_factory);
        dialog.setContentText(R.string.regain_leave_factory_setting);
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
                setFanPotLinkValue(3);
            }
        });

    }


}
