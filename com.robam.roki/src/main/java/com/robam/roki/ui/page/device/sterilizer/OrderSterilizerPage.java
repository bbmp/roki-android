package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.SteriSmartParams;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.SerilizerDialog;
import com.robam.roki.utils.DialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/10/18.
 * 预约峰谷电
 */

public class OrderSterilizerPage extends BasePage implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public String guid;
    String title;
    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.ser_order_name)
    TextView serOrderName;
    @InjectView(R.id.chkIsInternalDays)
    CheckBoxView chkIsInternalDays;
    @InjectView(R.id.tv_minute)
    TextView tvMinute;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.restore)
    TextView restore;

    private final int WEEK = 1;
    private final int HOUR = 2;

    AbsSterilizer absSterilizer;
    @InjectView(R.id.txt1)
    TextView txt1;
    @InjectView(R.id.txt2)
    TextView txt2;
    @InjectView(R.id.txt3)
    TextView txt3;


    List<DeviceConfigurationFunctions> mDate;

    SerilizerDialog serilizerDialog;

    List<String> weekList = new ArrayList<>();
    List<String> hourList = new ArrayList<>();
    short defaultWeek;
    int defaultHour;
    String desc;
    String[] txtDesc;
    String res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDate = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable("settingParam");
        View view = inflater.inflate(R.layout.serilizer_order_page, container, false);
        ButterKnife.inject(this, view);
        absSterilizer = Plat.deviceService.lookupChild(guid);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (guid == null) {
            return;
        }
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
    }

    private void initView() {
        JSONObject job = null;
        try {
            res = mDate.get(1).functionName;
            job = new JSONObject(mDate.get(0).functionParams);
            title = mDate.get(0).functionName;
            JSONArray weekArray = job.getJSONObject("param").getJSONObject("week").getJSONArray("value");
            for (int i = 0; i < weekArray.length(); i++) {
                weekList.add(String.valueOf(weekArray.get(i)));
            }
            String temp = job.getJSONObject("param").getJSONObject("defaultWeek").getString("value");
            switch (temp) {
                case SerilizerParam.weekOne:
                    defaultWeek = 0;
                    break;
                case SerilizerParam.weekTwo:
                    defaultWeek = 1;
                    break;
                case SerilizerParam.weekThree:
                    defaultWeek = 2;
                    break;
                case SerilizerParam.weekFour:
                    defaultWeek = 3;
                    break;
                case SerilizerParam.weekFive:
                    defaultWeek = 4;
                    break;
                case SerilizerParam.weekSix:
                    defaultWeek = 5;
                    break;
                case SerilizerParam.weekSun:
                    defaultWeek = 6;
                    break;
                default:
                    break;
            }
            JSONArray hourArray = job.getJSONObject("param").getJSONObject("hour").getJSONArray("value");
            String hourTemp = job.getJSONObject("param").getJSONObject("defaultHour").getString("value");
            for (int i = 0; i < hourArray.length(); i++) {
                if (hourTemp != null) {
                    if (hourTemp.equals(String.valueOf(hourArray.get(i)))) {
                        defaultHour = i;
                    }
                }
                hourList.add(String.valueOf(hourArray.get(i)));
            }

            desc = job.getJSONObject("param").getJSONObject("desc").getString("value");
            txtDesc = desc.split("button");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        serOrderName.setText(title);
        txt1.setText(txtDesc[0]);
        txt2.setText(txtDesc[1]);
        txt3.setText(txtDesc[2]);
        restore.setText(res);
        absSterilizer.getSteriPVConfig(new Callback<SteriSmartParams>() {
            @Override
            public void onSuccess(SteriSmartParams steriSmartParams) {
                refresh(steriSmartParams);
                setListener();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }


    void refresh(SteriSmartParams steriSmartParams) {
        if (steriSmartParams == null)
            return;
        chkIsInternalDays.setChecked(steriSmartParams.IsInternalDays);
        if (!steriSmartParams.IsInternalDays) {
            tvMinute.setTextColor(getResources().getColor(R.color.c44));
            time.setTextColor(getResources().getColor(R.color.c44));
        } else {
            tvMinute.setTextColor(getResources().getColor(R.color.c11));
            time.setTextColor(getResources().getColor(R.color.c11));
            if (steriSmartParams.WeeklySteri_week >= 1 && steriSmartParams.WeeklySteri_week <= 7) {
                tvMinute.setText(String.valueOf(steriSmartParams.WeeklySteri_week));
            }
            time.setText(steriSmartParams.PVCTime + ":00");
        }
    }

    void setListener() {
        if (!this.isAdded()) return;
        chkIsInternalDays.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        try {
            setSteriSmartParams("set");
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    void setSteriSmartParams(final String sing) {
        if (absSterilizer == null) return;
        final SteriSmartParams ssp = new SteriSmartParams();
        final boolean IsInternalDays = chkIsInternalDays.isChecked();
        ssp.IsInternalDays = IsInternalDays;
        ssp.IsWeekSteri = IsInternalDays;

        if (!TextUtils.isEmpty(tvMinute.getText())) {
            short InternalDays = Short.parseShort(tvMinute.getText().toString().trim());
            ssp.InternalDays = 7;
            ssp.WeeklySteri_week = InternalDays;
        }

        if (!TextUtils.isEmpty(time.getText())) {
            String txtPVCTimeStr = time.getText().toString().trim();
            Short PVCTime = Short.parseShort(txtPVCTimeStr.substring(0, txtPVCTimeStr.length() - 3));
            ssp.PVCTime = PVCTime;
        }
        absSterilizer.setSteriPVConfig(ssp, new VoidCallback() {
            @Override
            public void onSuccess() {
                if ("recovry".equals(sing)) {
                    ToastUtils.showShort(R.string.device_sterilizer_recover_succeed);

                } else {
                    ToastUtils.showShort(R.string.device_sterilizer_succeed);

                }

                refresh(ssp);
                chkIsInternalDays.setOnCheckedChangeListener(OrderSterilizerPage.this);


            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }


    @OnClick({R.id.mode_back, R.id.tv_minute, R.id.time, R.id.restore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_minute:
                serilizerDialog = new SerilizerDialog(cx, weekList, "", "", defaultWeek);
                serilizerDialog.show(serilizerDialog);
                serilizerDialog.setListener(new SerilizerDialog.PickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(Object index1) {
                        for (int i = 0; i < weekList.size(); i++) {
                            if (weekList.get(i).equals(index1)) {
                                tvMinute.setText((i + 1) + "");
                                setSteriSmartParams("set");
                            }
                        }
                    }
                });
                break;
            case R.id.time:
                serilizerDialog = new SerilizerDialog(cx, hourList, "", "", defaultHour);
                serilizerDialog.show(serilizerDialog);
                serilizerDialog.setListener(new SerilizerDialog.PickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(Object index1) {
                        time.setText((String) index1);
                        setSteriSmartParams("set");
                    }
                });
                break;
            case R.id.restore:
                restore();
                break;
        }
    }

    private List<String> getTimeHour() {

        return hourList;
    }

    private void restore() {
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
                recovery();
            }
        });
    }

    private void recovery() {
        chkIsInternalDays.setOnCheckedChangeListener(null);
        refresh(new SteriSmartParams());
        setSteriSmartParams("recovry");
    }


    @Override
    public void onClick(View v) {

    }
}