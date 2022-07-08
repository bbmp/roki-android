package com.robam.roki.ui.page.device.fan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceFanStoveLinkage;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DialogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 变速巡航
 */
public class DeviceFanCruisePage extends MyBasePage<MainActivity> {

    AbsFan fan;
    private List<DeviceConfigurationFunctions> mDates;
    private List<String> mMinute;
    private ImageView ivBack;
    private TextView tvDeviceModelName;
    private TextView tvFanName;
    private CheckBoxView cbCruise;
    private TextView tvFanFolding;
    private TextView tvRecovery;
    /**
     * 手动设置
     */
    private boolean set = false ;
    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        LogUtils.i("20200611", "FanStatusChangedEvent:::" + event.pojo.toString());
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID()))
            return;
        if (event.pojo instanceof AbsFan){
            if (set){
                set = false ;
                return;
            }
            fan = (AbsFan) event.pojo;
            cbCruise.setChecked(fan.cruise == 1);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_fan_cruise;
    }

    @Override
    protected void initView() {
        
        ivBack = findViewById(R.id.iv_back);
        tvDeviceModelName = findViewById(R.id.tv_device_model_name);
        tvFanName = findViewById(R.id.tv_fan_name);
        cbCruise = findViewById(R.id.cb_cruise);
        tvFanFolding = findViewById(R.id.tv_fan_folding);
        tvRecovery = findViewById(R.id.tv_recovery);
//        cbCruise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });
        cbCruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fan == null) {
                    return;
                }
                if (!fan.isConnected()){
                    ToastUtils.showShort(R.string.device_connected);
                    return;
                }
                boolean checked = cbCruise.isChecked();
                set = true ;
                fan.setCruise(checked ? 1 : 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        fan.cruise = (short) (checked ? 1 : 0);
                        ToastUtils.showShort(R.string.device_sterilizer_succeed);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIService.getInstance().popBack();
            }
        });
    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        mDates = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        initDate(mDates);
        cbCruise.setChecked(fan.cruise == 1);
    }


    private void initDate(List<DeviceConfigurationFunctions> dates) {

        if (dates == null || dates.size() == 0) return;
        for (int i = 0; i < dates.size(); i++) {
            if ("smokeStoveAutoPower".equals(dates.get(i).functionCode)) {
                String title = dates.get(i).functionName;
                tvDeviceModelName.setText(title);
                tvFanName.setText(title);
                List<DeviceConfigurationFunctions> functions = dates.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    String functionParams = functions.get(0).functionParams;
                    try {
                        DeviceFanStoveLinkage fanStoveLinkage = JsonUtils.json2Pojo(functionParams, DeviceFanStoveLinkage.class);
                        String tips = fanStoveLinkage.getParam().getTips().getValue();
                        tvFanFolding.setText(tips);
                        String value = fanStoveLinkage.getParam().getDesc().getValue();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

//    private void subString(String value) {
//        String[] strings = value.split("button");
//        for (int i = 0; i < strings.length; i++) {
//            if (0 == i) {
//                mFroneDesc = strings[i];
//            } else if (1 == i) {
//                mAfterDesc = strings[i];
//            }
//        }
//    }


    public void onViewClicked() {

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
            }
        });
    }


}
