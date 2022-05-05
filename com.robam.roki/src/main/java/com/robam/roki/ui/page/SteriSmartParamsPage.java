package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.common.pojos.device.Sterilizer.SteriSmartParams;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Gu on 2016/2/29.
 */
public class SteriSmartParamsPage extends HeadPage implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Steri829 sterilizer;
    @InjectView(R.id.txtInternalDays)
    TextView txtInternalDays;
    @InjectView(R.id.chkIsInternalDays)
    CheckBoxView chkIsInternalDays;
    private final int WEEK = 1;
    private final int HOUR = 2;
    @InjectView(R.id.txtPVCTime)
    TextView txtPVCTime;
    private IRokiDialog mSheTimeDialog = null;

    Handler mHandelr = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WEEK:
                    setWeekData((String) msg.obj);
                    break;
                case HOUR:
                    setHourData((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_steri_smart_params, null);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        sterilizer = (Steri829) bundle.getSerializable(PageArgumentKey.steri829);
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.txtRestore)
    public void onClickRestore() {

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText(R.string.title_leave_factory);
        dialog.setContentText(R.string.regain_leave_factory_setting);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
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
        setSteriSmartParams();
    }

    void refresh(SteriSmartParams steriSmartParams) {
        Log.i("sterilizer.refresh", steriSmartParams.toString());
        if (steriSmartParams == null)
            return;
        chkIsInternalDays.setChecked(steriSmartParams.IsInternalDays);
        if (steriSmartParams.WeeklySteri_week>=1&&steriSmartParams.WeeklySteri_week<=7){
            txtInternalDays.setText(String.valueOf(steriSmartParams.WeeklySteri_week));
        }

        txtPVCTime.setText(steriSmartParams.PVCTime + ":00");
        if (!steriSmartParams.IsInternalDays) {
            txtInternalDays.setOnClickListener(null);
            txtInternalDays.setTextColor(r.getColor(R.color.c03));
            txtPVCTime.setOnClickListener(null);
            txtPVCTime.setTextColor(r.getColor(R.color.c03));
        } else {
            txtInternalDays.setOnClickListener(SteriSmartParamsPage.this);
            txtInternalDays.setTextColor(r.getColor(R.color.c07));
            txtPVCTime.setOnClickListener(SteriSmartParamsPage.this);
            txtPVCTime.setTextColor(r.getColor(R.color.c07));
        }

    }

    void setSteriSmartParams() {
        if (sterilizer == null) return;
        SteriSmartParams ssp = new SteriSmartParams();
        final boolean IsInternalDays = chkIsInternalDays.isChecked();

        ssp.IsInternalDays = IsInternalDays;
        //   boolean IsWeekSteri = chkIsWeek.isChecked();
        ssp.IsWeekSteri = IsInternalDays;

        if (!TextUtils.isEmpty(txtInternalDays.getText())){
            short InternalDays = Short.parseShort(txtInternalDays.getText().toString().trim());
            ssp.InternalDays = 7;
            ssp.WeeklySteri_week=InternalDays;
        }

//        int weekDayIndex = Arrays.asList(week).indexOf(txtWeek.getText().toString());
//        ssp.WeeklySteri_week = (short) (weekDayIndex + 1);

        if (!TextUtils.isEmpty(txtPVCTime.getText())){
            String txtPVCTimeStr = txtPVCTime.getText().toString().trim();
            Short PVCTime = Short.parseShort(txtPVCTimeStr.substring(0, txtPVCTimeStr.length() - 3));
            ssp.PVCTime = PVCTime;

        }
        sterilizer.setSteriPVConfig(ssp, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort("设置成功");
                if (!IsInternalDays) {
                    txtInternalDays.setOnClickListener(null);
                    txtInternalDays.setTextColor(r.getColor(R.color.c03));
                    txtPVCTime.setOnClickListener(null);
                    txtPVCTime.setTextColor(r.getColor(R.color.c03));
                } else {
                    txtInternalDays.setOnClickListener(SteriSmartParamsPage.this);
                    txtInternalDays.setTextColor(r.getColor(R.color.c07));
                    txtPVCTime.setOnClickListener(SteriSmartParamsPage.this);
                    txtPVCTime.setTextColor(r.getColor(R.color.c07));
                }
                chkIsInternalDays.setOnCheckedChangeListener(SteriSmartParamsPage.this);
            }

            @Override
            public void onFailure(Throwable t) {
                initData();
                ToastUtils.showThrowable(t);
            }
        });
    }

    void setListener() {
        if (!this.isAdded()) return;
        chkIsInternalDays.setOnCheckedChangeListener(this);
    }

    private void initData() {
        if (sterilizer == null) return;
          sterilizer.getSteriPVConfig(new Callback<SteriSmartParams>() {
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        try {
            setSteriSmartParams();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtInternalDays:
                showListSelectDialog(txtInternalDays, getWeekList());
                break;
            case R.id.txtPVCTime:
                showListSelectDialog(txtPVCTime, getTimeHour());
                break;

            default:
                break;
        }
    }
    //显示底部弹框
    void showListSelectDialog(final View parent, List<String> list) {
        mSheTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        switch (parent.getId()) {

            case R.id.txtInternalDays:
              //"设置周几";
                mSheTimeDialog.setWheelViewData(null, list, null, false, 0, 3, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message message = mHandelr.obtainMessage();
                        message.what = WEEK;
                        message.obj = contentCenter;
                        mHandelr.sendMessage(message);
                    }
                },null);
                mSheTimeDialog.show();
                break;
            case R.id.txtPVCTime:
               //"设置时钟";
                mSheTimeDialog.setWheelViewData(null, list, null, false, 0, 8, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message message = mHandelr.obtainMessage();
                        message.what = HOUR;
                        message.obj = contentCenter;
                        mHandelr.sendMessage(message);
                    }
                },null);
                mSheTimeDialog.show();
                break;
        }

    }
    //设置时间
    private void setHourData(String content) {
        txtPVCTime.setTag(HOUR);
        canAndOkBtnListener(txtPVCTime,content);
    }

    //设置星期
    private void setWeekData(String content) {
        txtInternalDays.setTag(WEEK);
        canAndOkBtnListener(txtInternalDays,content);

    }

    private void canAndOkBtnListener(final TextView view, final String content){

        LogUtils.i("20170919","content:"+content);
        String newContent = null;
        if (content.equals("周一")){
            newContent = "1";
        }else if ("周二".equals(content)) {
            newContent = "2";
        }else if ("周三".equals(content)) {
            newContent = "3";
        }else if ("周四".equals(content)) {
            newContent = "4";
        }else if ("周五".equals(content)) {
            newContent = "5";
        }else if ("周六".equals(content)) {
            newContent = "6";
        }else if ("周日".equals(content)) {
            newContent = "7";
        }
        final String finalNewContent = newContent;
        mSheTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSheTimeDialog != null && mSheTimeDialog.isShow()){
                    mSheTimeDialog.dismiss();
                    int  tag = (int) view.getTag();

                    LogUtils.i("20170919","tag:"+tag);
                    switch (tag){
                        case WEEK:
                            txtInternalDays.setText(finalNewContent);
                            setSteriSmartParams();
                            break;
                        case HOUR:
                            txtPVCTime.setText(content);
                            setSteriSmartParams();
                            break;
                    }
                }
            }
        });

        mSheTimeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
    }

    //星期
    private List<String> getWeekList(){
        List<String> mWeekList = new ArrayList<>();
        for (int i = 0; i <= 7; i ++){
            switch (i){
                case 1:
                    mWeekList.add("周一");
                    break;
                case 2:
                    mWeekList.add("周二");
                    break;
                case 3:
                    mWeekList.add("周三");
                    break;
                case 4:
                    mWeekList.add("周四");
                    break;
                case 5:
                    mWeekList.add("周五");
                    break;
                case 6:
                    mWeekList.add("周六");
                    break;
                case 7:
                    mWeekList.add("周日");
                    break;
            }
        }
        return mWeekList;
    }
    //小时
    private List<String> getTimeHour(){
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i <= 24; i ++){
            if (i > 0 && i <= 6){
                hourList.add(i+":00");
            }
            if (i == 22 || i == 23 || i == 24){
                hourList.add(i+":00");
            }
        }
        return hourList;
    }

}
