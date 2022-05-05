package com.robam.roki.ui.page.device.gassensor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/5/31.
 */

public class GasAlarmSensorView extends FrameLayout {

    @InjectView(R.id.alarm_desc)
    TextView alarmDesc;
    @InjectView(R.id.alarm_deal)
    TextView alarmDeal;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String str = (String) msg.obj;
                    String[] str1 = str.split("b");
                    alarmDesc.setText(str1[0]);
                    alarmDeal.setText(str1[1]);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };

    public GasAlarmSensorView(Context context) {
        super(context);
        initView(context, null);
    }

    public GasAlarmSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.gas_alarm_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    //str1是描述,str2是button上的文字描述，str1在前不能变
    protected void upDate(String str1,String str2){
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = str1+"b"+str2;
        handler.sendMessage(msg);
    }

    @OnClick(R.id.alarm_deal)
    public void onClickAlarm(){
      //  ToastUtils.show("如何处理？", Toast.LENGTH_SHORT);
        if (onDealClickLister!=null){
            if ("如何处理？".equals(alarmDeal.getText().toString())){
                onDealClickLister.dealClick(0);
            }else{//拨打售后
                onDealClickLister.dealClick(1);
            }

        }
    }

    public OnDealClick onDealClickLister;

    public void setOnDealClickLister(OnDealClick onDealClickLister){
        this.onDealClickLister = onDealClickLister;
    }

    public interface OnDealClick{
        void dealClick(int num);
    }
}
