package com.robam.roki.ui.activity3.device.base;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeParamPickerAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.bean.ModeParam;
import com.robam.roki.ui.activity3.device.base.adapter.bean.ModeSingleParam;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.mdialog.PickerLayoutManager;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.TimeUtils;
import com.robam.widget.layout.SettingBar;
import com.robam.widget.view.SwitchButton;

import java.text.ParseException;
import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/23
 * desc   : 设备模式选择
 */
public abstract   class DeviceModeSelectActivity extends DeviceBaseFuntionActivity {

    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 模式adapter
     */
    private RecyclerView rvMode;

    /**
     * 预约布局
     */
    private LinearLayout llOrder;
    /**
     * 是否预约
     */
    private SwitchButton sbOrderSwitch;



    private RvModeAdapter rvModeAdapter;


    private LinearLayout llParam1;
    private TextView tvParamDes1;
    private TextView tvParamUnit1;
    private LinearLayout llParam2;
    private TextView tvParamDes2;
    private TextView tvParamUnit2;
    private LinearLayout llParam3;
    private TextView tvParamDes3;
    private TextView tvParamUnit3;
    private RecyclerView rvModeParam1;
    private RecyclerView rvModeParam2;
    private RecyclerView rvModeParam3;
    private PickerLayoutManager manager;
    private RvModeParamPickerAdapter rvHoursAdapter;
    private PickerLayoutManager manager2;
    private RvModeParamPickerAdapter rvHours2Adapter;
    private PickerLayoutManager manager3;
    private RvModeParamPickerAdapter rvHours3Adapter;
    private TextView tvModeDesc;

    /**
     * 开始烹饪
     */
    private Button btnComplete;
    /**
     * 选中的模式参数
     */
    private ModeParam modeParam;
    /**
     * 预约时间
     */
    private String orderDate;
    /**
     * 预约时间 秒
     */
    private long orderTime;
    /**
     * 预约选择布局
     */
    private RelativeLayout rlOrder;
    /**
     * 预约时间
     */
    private TextView tvOrderTime;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_mode_select;
    }

    @Override
    protected void initView() {

        rvMode = findViewById(R.id.rv_mode);
        llOrder = findViewById(R.id.ll_order);
        sbOrderSwitch = findViewById(R.id.sb_order_switch);
        rlOrder = findViewById(R.id.rl_order);
        tvOrderTime = findViewById(R.id.tv_order_time);

        llParam1 = findViewById(R.id.ll_param1);
        tvParamDes1 = findViewById(R.id.tv_param_des1);
        tvParamUnit1 = findViewById(R.id.tv_param_unit1);
        llParam2 = findViewById(R.id.ll_param2);
        tvParamDes2 = findViewById(R.id.tv_param_des2);
        tvParamUnit2 = findViewById(R.id.tv_param_unit2);
        llParam3 = findViewById(R.id.ll_param3);
        tvParamDes3 = findViewById(R.id.tv_param_des3);
        tvParamUnit3 = findViewById(R.id.tv_param_unit3);

        rvModeParam1 = findViewById(R.id.rv_mode_param1);
        rvModeParam2 = findViewById(R.id.rv_mode_param2);
        rvModeParam3 = findViewById(R.id.rv_mode_param3);

        btnComplete = findViewById(R.id.btn_complete);
        tvModeDesc = findViewById(R.id.tv_mode_desc);


        rvMode.setLayoutManager(new GridLayoutManager(this, 4));
        rvModeAdapter = new RvModeAdapter();
        rvMode.setAdapter(rvModeAdapter);

        rvModeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                rvModeAdapter.setSelectPosition(i);
                DeviceConfigurationFunctions item = rvModeAdapter.getItem(i);
                setModeParam(item);
            }
        });
         manager = new PickerLayoutManager.Builder(getContext())
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvHoursAdapter.setIndex(position);
                    }
                })
                .build();
        rvModeParam1.setLayoutManager(manager);

         manager2 = new PickerLayoutManager.Builder(getContext())
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvHours2Adapter.setIndex(position);
                    }
                })
                .build();
        rvModeParam2.setLayoutManager(manager2);

        manager3 = new PickerLayoutManager.Builder(getContext())
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvHours3Adapter.setIndex(position);
                    }
                })
                .build();
        rvModeParam3.setLayoutManager(manager3);

        //是否预约
        sbOrderSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean checked) {
                rlOrder.setVisibility(checked ? View.VISIBLE : View.GONE);
                try {
                    orderDate = DateUtil.getCurrentDate1Hours();
                    tvOrderTime.setText("今天" + orderDate + "分开始烹饪");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        setOnClickListener(rlOrder,btnComplete);
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        functionName = deviceConfigurationFunction.functionName;
        setTitle(functionName);
        try {
            List<DeviceConfigurationFunctions> deviceConfigurationFunctions =
                    deviceConfigurationFunction.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0) {
                toast("功能参数未配置");
                return;
            }
            //是否支持预约
            llOrder.setVisibility(isOrder() ? View.VISIBLE : View.GONE);
            rvModeAdapter.addData(deviceConfigurationFunctions);
            setModeParam(rvModeAdapter.getItem(rvModeAdapter.selectPosition));
        } catch (Exception e) {
            toast("功能参数配置错误");
            return;
        }

    }

    /**
     * 是否支持预约
     *
     * @return
     */
    protected abstract boolean isOrder();

    /**
     * 设置选中的模式的参数
     *
     * @param func
     */
    private void setModeParam(DeviceConfigurationFunctions func) {
        String functionCode = func.functionCode;
        String funcName = func.functionName;
        toast(functionCode);
        try {
            String freshSteamedParams = func.functionParams;
            if ("EXPFunction".equals(functionCode)){
                OvenExpParamBean ovenParams = JsonUtils.json2Pojo(freshSteamedParams, OvenExpParamBean.class);
                List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                List<Integer> time = ovenParams.getParam().getSetTime().getValue();
                String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();
                String timeDefault = ovenParams.getParam().getDefaultSetTime().getValue();
                String descTips = ovenParams.getParam().getDesc().getValue();
                short mSteamModel = Short.parseShort(ovenParams.getParam().getModel().getValue());

                ModeParam modeParam = new ModeParam();
                modeParam.code = mSteamModel;
                modeParam.name = func.functionName;
                modeParam.defTemp = Integer.parseInt(tempUpDefault);
                modeParam.defTime = Integer.parseInt(timeDefault);
                modeParam.minTime = time.get(0);
                modeParam.maxTime = time.get(1);
                modeParam.minTemp = tempUp.get(0);
                modeParam.maxTemp = tempUp.get(1);
                modeParam.desc = descTips;
                List<ModeSingleParam> singlePsrams = modeParam.getSingleParams();
                setParams(singlePsrams);
                return;
            }

            SteamOvenModelFunctionParams functionParams = null;
            functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunctionParams.class);
            short mSteamModel = Short.parseShort(functionParams.getParam().getModel().getValue());
            String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
            String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
            short newDefaultTemp = (Short.parseShort(tempDefaultValue));
            short newDefaultTime = (Short.parseShort(timeDefaultValue));
            List<Integer> temps = functionParams.getParam().getSetTemp().getValue();
            List<Integer> times = functionParams.getParam().getSetTime().getValue();
            String descTips = func.msg;

             modeParam = new ModeParam();
            modeParam.code = mSteamModel;
            modeParam.name = func.functionName;
            modeParam.defTemp = newDefaultTemp;
            modeParam.defTime = newDefaultTime;
            modeParam.minTime = times.get(0);
            modeParam.maxTime = times.get(1);
            modeParam.minTemp = temps.get(0);
            modeParam.maxTemp = temps.get(1);
            modeParam.desc = descTips;
            if (functionParams.getParam().getSetSteam() != null
                    && functionParams.getParam().getSetSteam().getValue() != null
                    && functionParams.getParam().getSetSteam().getValue().size() != 0) {
                List<Integer> steams = functionParams.getParam().getSetSteam().getValue();
                modeParam.minSteam = steams.get(0);
                modeParam.maxSteam = steams.get(steams.size() - 1);
            }
            List<ModeSingleParam> singlePsrams = modeParam.getSingleParams();
            setParams(singlePsrams);
            tvModeDesc.setText(StringUtil.isBlank(modeParam.desc) ? "" : modeParam.desc);
        } catch (Exception e) {
            e.printStackTrace();
            toast("模式参数配置不正确");
        }
    }

   private void  setParams(List<ModeSingleParam> singleParams){

        if (singleParams.size() >= 2){
            ModeSingleParam singleParam = singleParams.get(0);
            tvParamDes1.setText(singleParam.name);
            tvParamUnit1.setText(singleParam.unit);
             rvHoursAdapter = new RvModeParamPickerAdapter(getContext() ,singleParam.unit );

            rvModeParam1.setAdapter(rvHoursAdapter);
            rvHoursAdapter.setData(singleParam.datas);
            manager.scrollToPosition(singleParam.defIndex);
            rvHoursAdapter.setIndex(singleParam.defIndex);

            ModeSingleParam singleParam2 = singleParams.get(1);
            tvParamDes2.setText(singleParam2.name);
            tvParamUnit2.setText(singleParam2.unit);
             rvHours2Adapter = new RvModeParamPickerAdapter(getContext(),singleParam2.unit  );


            rvModeParam2.setAdapter(rvHours2Adapter);
            rvHours2Adapter.setData(singleParam2.datas);
            manager2.scrollToPosition(singleParam2.defIndex);
            rvHours2Adapter.setIndex(singleParam2.defIndex);

        }

        if (singleParams.size() == 3){

            ModeSingleParam singleParam3 = singleParams.get(2);
            tvParamDes3.setText(singleParam3.name);
            tvParamUnit3.setText(singleParam3.unit);
            rvHours3Adapter = new RvModeParamPickerAdapter(getContext() , singleParam3.unit);
            rvModeParam3.setAdapter(rvHours3Adapter);
            rvHours3Adapter.setData(singleParam3.datas);
            manager3.scrollToPosition(singleParam3.defIndex);
            rvHours3Adapter.setIndex(singleParam3.defIndex);
            llParam3.setVisibility(View.VISIBLE);
            rvModeParam3.setVisibility(View.VISIBLE);
        }else {
            llParam3.setVisibility(View.GONE);
            rvModeParam3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == rlOrder){
            String[] split = orderDate.split(":");
            int hour = Integer.parseInt(split[0]);
            int minu = Integer.parseInt(split[1]);
            new DialogUtils(this).timeSelect(hour , minu, new DialogUtils.OnSelectListener() {
                @Override
                public void onSelectHourMinu(int hour, int minu) {
                    String hourString = hour < 10 ? "0" + hour : "" + hour;
                    String minuString = minu < 10 ? "0" + minu : "" + minu;
                    orderDate  = hourString + ":" + minuString ;
                    if (DateUtil.compareTime(DateUtil.getCurrentTime(DateUtil.PATTERN), orderDate, DateUtil.PATTERN) == 1) {
                        tvOrderTime.setText("明天" + orderDate + "分开始烹饪");
                    } else {
                        tvOrderTime.setText("今天" + orderDate + "分开始烹饪");
                    }
                }
            });
        }else if (view == btnComplete){
            if (sbOrderSwitch.isChecked()){
                int ordertime_min = HelperRikaData.getMinGap(orderDate);
                int ordertime_hour = HelperRikaData.getHousGap(orderDate);
                orderTime = ordertime_hour * 3600 + ordertime_min * 60 ;
            }else {
                orderTime = 0 ;
            }

            //加湿烤 澎湃蒸
            if (modeParam.code == 5  || modeParam.code == 22 || modeParam.code == 23 || modeParam.code == 24){//exp
                startWork(modeParam.code
                        , rvHoursAdapter.getItem(rvHoursAdapter.getIndex())
                        ,rvHours2Adapter.getItem(rvHours2Adapter.getIndex())
                        , 0
                        , rvHours3Adapter.getItem(rvHours3Adapter.getIndex())
                        , orderTime);
            }else if (modeParam.code == 14){ //exp
                startWork(modeParam.code
                        , 0
                        , rvHoursAdapter.getItem(rvHoursAdapter.getIndex())
                        , rvHours2Adapter.getItem(rvHours2Adapter.getIndex())
                        , rvHours3Adapter.getItem(rvHours3Adapter.getIndex())
                        , orderTime);
            }else {
                startWork(modeParam.code
                        , 0
                        , rvHoursAdapter.getItem(rvHoursAdapter.getIndex())
                        , 0
                        , rvHours2Adapter.getItem(rvHours2Adapter.getIndex())
                        , orderTime);
            }
//            startWork();
        }
    }

    /**
     * 开始工作
     */
    public abstract void startWork(int code , int steam ,int temp , int downUp , int time ,  long orderTime);
}