package com.robam.roki.ui.page.device.fan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 过温保护
 */
public class DeviceFanOTPPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.tv_front_desc)
    TextView tvFrontDesc;
    @InjectView(R.id.tv_after_desc)
    TextView tvAfterDesc;

    @InjectView(R.id.tv_temp)
    TextView tvTemp;

    @InjectView(R.id.tv_recovery)
    TextView tvRecovery;

    @InjectView(R.id.tv_temp_protect)
    TextView tvTempProtect;

    @InjectView(R.id.tv_ds)
    TextView tvDs;

    @InjectView(R.id.cbx_temp_protect)
    CheckBoxView cbxTempProtect;

    @InjectView(R.id.tv_last_text)
    TextView tvLastText;

    @InjectView(R.id.fan_pic_show)
    ImageView mFanPicShow;

    @InjectView(R.id.fan_linkage_dec)
    LinearLayout fanLinkageDec;

    @InjectView(R.id.tv_temp_behind_text)
    TextView tvTempBehindText;

    private AbsFan fan;
    List<DeviceConfigurationFunctions> mList;

    private final int TEMP = 1;
    SmartParams sp = new SmartParams();

    @SuppressLint("HandlerLeak")
    MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TEMP:
                    setTemp((String) msg.obj);
                    break;
            }
        }
    };

    private void setTemp(final String temp) {

        if (temp.contains(cx.getString(R.string.dialog_degree_text))) {
            //去除字符串中非数字部分
            final String temps = RemoveManOrsymbolUtil.getRemoveString(temp);
            mRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRokiDialog != null && mRokiDialog.isShow()) {
                        mRokiDialog.dismiss();
                        tvTemp.setText(temps);
                        setTempParams();
                    }
                }
            });
            mRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    //设置温度
    private void setTempParams() {
        if (fan == null) {
            return;
        }
        mFanStatusComposite.IsOverTempProtectSwitch = (short) (cbxTempProtect.isChecked() ? 1 : 0);
        if (!TextUtils.isEmpty(tvTemp.getText().toString())) {
            mFanStatusComposite.IsOverTempProtectSet = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(tvTemp.getText().toString()));
        }
        List<Integer> listKey = new ArrayList<>();
        listKey.add(14);
        listKey.add(15);
        fan.setFanCombo(mFanStatusComposite, (short) 2, listKey, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                if (mFanStatusComposite.IsOverTempProtectSwitch == 1) {
                    setOnOffStatusForText(true);
                } else {
                    setOnOffStatusForText(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_otp, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        initData();
        return view;
    }


    private void initData() {
        if (mList == null || mList.size() == 0) return;

        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = null;
        for (int i = 0; i < mList.size(); i++) {
            if ("OTP".equals(mList.get(i).functionCode)) {
                deviceConfigurationFunctions = mList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }
        String functionParams = null;
        String functionName = null;
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if ("otpOnOFF".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                functionParams = deviceConfigurationFunctions.get(i).functionParams;
            }
            if ("doDef".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                functionName = deviceConfigurationFunctions.get(i).functionName;
                tvRecovery.setText(functionName);
            }

        }

        try {
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject param = jsonObject.getJSONObject("param");
            JSONArray tempArray = param.getJSONObject("temp").getJSONArray("value");
            String defaultTemp = param.getJSONObject("defaultTemp").getString("value");
            String title = param.getJSONObject("title").getString("value");
            String subTitle = param.getJSONObject("subTitle").getString("value");
            String desc = param.getJSONObject("desc").getString("value");

            List<Integer> temp = new ArrayList<>();
            for (int k = 0; k < tempArray.length(); k++) {
                Integer tem = (Integer) tempArray.get(k);
                temp.add(tem);
            }
            mTemp = HelperFanData.getTemp(temp);

            tvTitle.setText(title);
            tvTempProtect.setText(title);
            tvDs.setText(subTitle);
            String[] buttons = desc.split("button");
            String frontText = buttons[0];
            String behindText = buttons[1];

            String substring = behindText.substring(0, 2);
            tvTempBehindText.setText(substring);

            String substring1 = behindText.substring(2);
            String[] split = substring1.split("/n");
            String s1 = split[0];
            String s2 = split[1];

            tvFrontDesc.setText(frontText);
            tvAfterDesc.setText(s1);
            tvLastText.setText(s2);
            tvTemp.setText(defaultTemp);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (fan == null) {
            return;
        }
        redSmartConfig();
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;

        }
    }

    private IRokiDialog mRokiDialog;
    private List<String> mTemp;

    @OnClick(R.id.tv_temp)
    public void onTempClick() {

        if (mTemp == null || mTemp.size() == 0) return;
        if (!cbxTempProtect.isChecked()) return;

        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mTemp, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = TEMP;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();

        }

    }


    @OnClick(R.id.tv_recovery)
    public void onViewClicked() {

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText(R.string.device_default_leave_factory_setting);
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
                recovery();
            }
        });
    }

    @OnClick(R.id.cbx_temp_protect)
    public void onCbxTempProtectSwitch() {
        if (recovery_flag) {
            return;
        }

        mFanStatusComposite.IsOverTempProtectSwitch = (short) (cbxTempProtect.isChecked() ? 1 : 0);
        if (!TextUtils.isEmpty(tvTemp.getText().toString())) {
            mFanStatusComposite.IsOverTempProtectSet = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(tvTemp.getText().toString()));
        }
        List<Integer> listKey = new ArrayList<>();
        listKey.add(14);
        listKey.add(15);
        fan.setFanCombo(mFanStatusComposite, (short) 2, listKey, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                if (mFanStatusComposite.IsOverTempProtectSwitch == 1) {
                    setOnOffStatusForText(true);
                } else {
                    setOnOffStatusForText(false);
                }
                recovery_flag = false;
                redSmartConfig();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    private boolean recovery_flag;

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new FanStatusComposite());
        recoveryP();

    }


    //刷新
    private void refresh(FanStatusComposite fanStatusComposite) {
        if (fanStatusComposite == null) {
            return;
        }
        if (fanStatusComposite.IsOverTempProtectSwitch == 1) {
            cbxTempProtect.setChecked(true);
            setOnOffStatusForText(true);
        } else {
            cbxTempProtect.setChecked(false);
            setOnOffStatusForText(false);
        }
        tvTemp.setText(String.valueOf(fanStatusComposite.IsOverTempProtectSet));


    }

    private void recoveryP() {
        if (fan == null) {
            return;
        }
        mFanStatusComposite.IsOverTempProtectSwitch = (short) (cbxTempProtect.isChecked() ? 1 : 0);
        if (!TextUtils.isEmpty(tvTemp.getText().toString())) {
            mFanStatusComposite.IsOverTempProtectSet = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(tvTemp.getText().toString()));
        }

        List<Integer> listKey = new ArrayList<>();
        listKey.add(14);
        listKey.add(15);
        fan.setFanCombo(mFanStatusComposite, (short) 2, listKey, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                if (mFanStatusComposite.IsOverTempProtectSwitch == 1) {
                    setOnOffStatusForText(true);
                } else {
                    setOnOffStatusForText(false);
                }
                recovery_flag = false;
                redSmartConfig();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);

            }
        });


    }

    private void setOnOffStatusForText(boolean tf) {
        tvTemp.setTextColor(tf ? r.getColor(R.color.c11) : r.getColor(R.color.c03));
    }

    private FanStatusComposite mFanStatusComposite = new FanStatusComposite();

    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                sp = smartParams;
                mFanStatusComposite.IsOverTempProtectSwitch = (short) (smartParams.IsOverTempProtectSwitch ? 1 : 0);
                mFanStatusComposite.IsOverTempProtectSet = smartParams.IsOverTempProtectSet;
                refresh(mFanStatusComposite);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private boolean flag = true;

    @OnClick(R.id.fan_pic_show)
    public void onMFanPicShowClicked() {
        if (flag) {
            mFanPicShow.setImageResource(R.mipmap.img_8230s_expand_shang);
            fanLinkageDec.setVisibility(View.GONE);
            flag = false;
            return;
        }
        flag = true;
        mFanPicShow.setImageResource(R.mipmap.img_fan8230s_expand);
        fanLinkageDec.setVisibility(View.VISIBLE);
    }


}
