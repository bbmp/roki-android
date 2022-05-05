package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.SmartParamsWaterPurifier;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SmartWaterPurifierParamsPage extends HeadPage implements CompoundButton.OnCheckedChangeListener{
    @InjectView(R.id.chkIsPowerLinkage)
    CheckBoxView chkIsPowerLinkage;//联动设置 开关
    @InjectView(R.id.water321_txt)
    TextView timeShow;
    @InjectView(R.id.smart_time_set)
    ImageView timeSet;
    @InjectView(R.id.water_time_set)
    LinearLayout water_time_set;
    AbsWaterPurifier waterPurifier;
    private boolean recovery_flag;
    private IRokiDialog mWaterTimeDialog = null;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  1:
                    setSmartDaata((String) msg.obj);
                 break;
            }
        }
    };



    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.page_waterpurifier_smart_params, viewGroup, false);
        ButterKnife.inject(this, view);
        waterPurifier= Utils.getDefaultWaterPurifier();
        initData();
        return view;
    }

    void initData() {
       // chkIsPowerLinkage.setOnCheckedChangeListener(this);
       // refresh(new SmartParamsWaterPurifier());
        if (waterPurifier==null)return;
        waterPurifier.getWaterPurifierSmartStatus(new Callback<SmartParamsWaterPurifier>(){

            @Override
            public void onSuccess(SmartParamsWaterPurifier smartParamsWaterPurifier) {
               // LogUtils.i("20170644","ggg");
               // LogUtils.i("20170704","smartParamsWaterPurifier::"+smartParamsWaterPurifier.PowerSaving+"::;;"+smartParamsWaterPurifier.WaterSystem_minte);
                refresh(smartParamsWaterPurifier);
                setListener();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton chkBox, boolean isChecked) {
        try {
            LogUtils.i("20170524","chkBox:"+chkBox+":::isChecked"+isChecked);
            if(recovery_flag)return;
           if (chkBox == chkIsPowerLinkage) {//联动
               setSmartParams();
           }
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    @OnClick(R.id.water_time_set)
    public void onClickTimeSet(){
        //DialogWaterPurifierSmartTimeSet.show(cx);
        mWaterTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        mWaterTimeDialog.setWheelViewData(null, getSmartListData(), null, false, 0, 2, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 1;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        mWaterTimeDialog.setCanceledOnTouchOutside(true);
        mWaterTimeDialog.show();

    }

    private void setSmartDaata(String data) {
        final List<String> smartListData = new ArrayList<>();
        if (data.contains(StringConstantsUtil.STRING_MINUTES)){
            String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
            smartListData.add(removeString);
        }
        mWaterTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWaterTimeDialog != null && mWaterTimeDialog.isShow()){
                    mWaterTimeDialog.dismiss();
                }
                if (smartListData.size() == 0 || smartListData == null){
                    return;
                }
                timeShow.setText(smartListData.get(smartListData.size()-1)+"");
                setSmartParams();

            }
        });

    }

    //智能节流数据
    private List<String> getSmartListData() {
        List<String> smartList = new ArrayList<>();
        for (int i = 10 ;i <= 60; i = i + 10 ){
            smartList.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return smartList;
    }

    void setSmartParams() {
        if (waterPurifier == null) {
            return;
        }

        final SmartParamsWaterPurifier sp = new SmartParamsWaterPurifier();
        sp.PowerSaving = (short) (chkIsPowerLinkage.isChecked() ? 0 : 1);
        String timeSmart = timeShow.getText().toString();
      //  LogUtils.i("20170524","timeSmart::"+timeSmart);
        PreferenceUtils.setInt(PageArgumentKey.ShutDownDelay,Integer.parseInt(timeSmart));
        sp.WaterSystem_minte = Short.parseShort(timeSmart);
        short min= Short.parseShort(timeSmart);
        if (min==10){
            sp.WaterSystem_minte=1;
        }else if (min==20){
            sp.WaterSystem_minte=2;
        }else if(min==30){
            sp.WaterSystem_minte=3;
        }else if (min==40){
            sp.WaterSystem_minte=4;
        }else if (min==50){
            sp.WaterSystem_minte=5;
        }else if (min==60){
            sp.WaterSystem_minte=6;
        }

        waterPurifier.setSmartConfig(sp, (short) 2, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("201706044","dddd");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    void setListener() {
        if (!this.isAdded()) return;
        chkIsPowerLinkage.setOnCheckedChangeListener(this);
    }

    void refresh(SmartParamsWaterPurifier smartwaterParams) {
        if (smartwaterParams == null) return;
        short temp=0;
        chkIsPowerLinkage.setChecked((smartwaterParams.PowerSaving == (short) 0));
        LogUtils.i("20170644","ee"+smartwaterParams.WaterSystem_minte);
        if (smartwaterParams.WaterSystem_minte==1){
            temp=10;
        }else if(smartwaterParams.WaterSystem_minte==2){
            temp=20;
        }else if(smartwaterParams.WaterSystem_minte==3){
            temp=30;
        }else if(smartwaterParams.WaterSystem_minte==4){
            temp=40;
        }else if(smartwaterParams.WaterSystem_minte==5){
            temp=50;
        }else if(smartwaterParams.WaterSystem_minte==6){
            temp=60;
        }
        timeShow.setText(temp+"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
