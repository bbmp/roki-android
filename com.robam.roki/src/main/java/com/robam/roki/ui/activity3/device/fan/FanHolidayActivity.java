package com.robam.roki.ui.activity3.device.fan;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceFanVentilation;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvConSuctionAdapter;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.widget.view.SwitchButton;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/13
 * desc   : 假日模式
 */
public final class FanHolidayActivity extends DeviceBaseFuntionActivity {
    /**
     * 烟机
     */
    private AbsFan fan ;
    /**
     * 天数
     */
    private TextView tvDay;
    /**
     * 周几
     */
    private TextView tvWeek;
    /**
     * 时间
     */
    private TextView tvTime;
    private TextView tvFuncName;
    private SwitchButton sbHoliday;
    private TextView tvFuncDesc;
    private List<String> mHours;
    private List<String> mMinute;
    private List<String> mDays;
    private List<String> mWeek;
    private String functionName;
    /**
     * 假日模式功能参数
     */
    private DeviceFanVentilation fanVentilation;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fan_holiday;
    }

    @Override
    protected void initView() {
        tvDay = findViewById(R.id.tv_day);
        tvWeek = findViewById(R.id.tv_week);
        tvTime = findViewById(R.id.tv_time);
        tvFuncName = findViewById(R.id.tv_func_name);
        sbHoliday = findViewById(R.id.sb_holiday);
        tvFuncDesc = findViewById(R.id.tv_func_desc);
        setOnClickListener(tvDay , tvWeek , tvTime);
        sbHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fan.smartParams.IsTimingVentilation = sbHoliday.isChecked();
                fan.smartParams.IsWeeklyVentilation = sbHoliday.isChecked();
                setSmartParam();
            }
        });
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        functionName = deviceConfigurationFunction.functionName;
        setTitle(functionName);
        tvFuncName.setText(functionName);
        try {
            String functionParams =  deviceConfigurationFunction.subView.subViewModelMap.
                    subViewModelMapSubView.deviceConfigurationFunctions.get(0).functionParams;
            if (functionParams == null) {
                toast("功能参数未配置");
                return;
            }
             fanVentilation = JsonUtils.json2Pojo(functionParams, DeviceFanVentilation.class);
            tvFuncDesc.setText(fanVentilation.getParam().getTitle().getValue());

            List<Integer> hourSection = fanVentilation.getParam().getHour().getValue();
            List<Integer> minuteSection = fanVentilation.getParam().getMinute().getValue();
            mHours = HelperFanData.getTimeHour2(hourSection);
            mMinute = HelperFanData.getTimeMinute2(minuteSection);

            List<Integer> daySection = fanVentilation.getParam().getDay().getValue();
            mDays = HelperFanData.getListDay(daySection);
            mWeek = fanVentilation.getParam().getWeek().getValue();

        } catch (Exception e) {
            toast("功能参数配置错误");
            return;
        }

        if(mDevice instanceof AbsFan){
            fan = (AbsFan) mDevice ;
            //读取烟机设定状态
            redSmartConfig();
        }
    }

    /**
     * 读取烟机设定状态
     */
    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(SmartParams smartParams) {
//                toast("读取成功");
               loadData(smartParams);
            }

            @Override
            public void onFailure(Throwable t) {
                toast("数据获取失败");
                tvDay.setText("-");
                tvWeek.setText("-");
                tvTime.setText("-");
                tvDay.setEnabled(false);
                tvWeek.setEnabled(false);
                tvTime.setEnabled(false);
            }
        });
    }

    private  void loadData(SmartParams smartParams){
        sbHoliday.setChecked(smartParams.IsTimingVentilation && smartParams.IsWeeklyVentilation);
        tvDay.setText(smartParams.TimingVentilationPeriod+"");
        tvWeek.setText(mWeek.get(smartParams.WeeklyVentilationDate_Week - 1));

        tvTime.setText(( smartParams.WeeklyVentilationDate_Hour > 10 ? "" : "0") + smartParams.WeeklyVentilationDate_Hour+":" + ( smartParams.WeeklyVentilationDate_Minute > 10 ? "" : "0") + smartParams.WeeklyVentilationDate_Minute);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == tvDay){
            new DialogUtils(this).holidayDialog(functionName, mDays,(fan.smartParams.TimingVentilationPeriod - 1) , new DialogUtils.OnSelectListener() {
                @Override
                public void onSelectPosition(int  position) {
                    int day = position + 1;
                    fan.smartParams.TimingVentilationPeriod = (short) day ;
                    setSmartParam();
                }
            });
        }else if (view == tvWeek){
            new DialogUtils(this).holidayDialog(functionName, mWeek,  fan.smartParams.WeeklyVentilationDate_Week -1 ,new DialogUtils.OnSelectListener() {
                @Override
                public void onSelectPosition(int position) {
                    int week = position +1;
                    fan.smartParams.WeeklyVentilationDate_Week = (short) week ;
                    setSmartParam();
                }
            });
        }else if (view == tvTime){
            new DialogUtils(this).timeSelect(fan.smartParams.WeeklyVentilationDate_Hour ,fan.smartParams.WeeklyVentilationDate_Minute , new DialogUtils.OnSelectListener() {
                @Override
                public void onSelectHourMinu(int hour, int minu) {
                    fan.smartParams.WeeklyVentilationDate_Hour = (short) hour ;
                    fan.smartParams.WeeklyVentilationDate_Minute = (short) minu ;
                    setSmartParam();
                }
            });
        }

    }

    private void setSmartParam(){


        fan.setSmartConfig(fan.smartParams, new VoidCallback() {

            @Override
            public void onSuccess() {
                toast(R.string.device_sterilizer_succeed);
                loadData(fan.smartParams);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }
}