package com.robam.roki.ui.activity3.device.dishwasher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DeviceModeSelectActivity;
import com.robam.roki.ui.activity3.device.base.DeviceWorkActivity;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeAdapter;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.dishwasher.adapter.RvDishModeAdapter;
import com.robam.roki.ui.activity3.device.dishwasher.adapter.RvDishParamPickerAdapter;
import com.robam.roki.ui.activity3.device.dishwasher.bean.ParamBean;
import com.robam.roki.ui.mdialog.PickerLayoutManager;
import com.robam.roki.utils.DateUtil;
import com.robam.widget.layout.SettingBar;
import com.robam.widget.view.SwitchButton;

import java.text.ParseException;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/29
 *     desc   : 洗碗机模式选择界面
 *     version: 1.0
 * </pre>
 */
public class DishModeSelectActivity extends DeviceBaseFuntionActivity {
    /**
     * 洗碗机
     */
    private AbsDishWasher dishWasher;
    /**
     * 模式选择
     */
    private RecyclerView rvMode;
    private TextView tvTemp;
    private TextView tvTime;
    private TextView tvWater;
    /**
     * 辅助功能选择
     */
    private RecyclerView rvModeParam;
    /**
     * 预约
     */
    private LinearLayout llOrder;
    private SwitchButton sbOrderSwitch;
    /**
     * 预约选择布局
     */
    private RelativeLayout rlOrder;
    /**
     * 预约时间
     */
    private TextView tvOrderTime;
    private Button btnComplete;
    private RvDishModeAdapter rvModeAdapter;
    private PickerLayoutManager manager;
    /**
     * 辅助模式参数选择adapter
     */
    private RvDishParamPickerAdapter rvDishParamPickerAdapter;
    /**
     * 预约时间
     */
    private String orderDate;
    /**
     * 预约分钟
     */
    private int orderMinu;
    /**
     * 选中的模式
     */
    ParamBean paramBean ;
    /**
     * 下层洗开关
     */
    int downWsshSw = 0 ;
    /**
     * 自动换气开关
     */
    int autoVentiSw = 0 ;
    /**
     * 加强干燥开关
     */
    int drySw = 0 ;

    /**
     * 洗碗机状态变更事件
     * @param event
     */
    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (mDevice == null || !Objects.equal(mDevice.getID(), event.pojo.getID())) {
            return;
        }
        this.dishWasher = event.pojo;
        if (dishWasher.powerStatus > 1){
            //跳转到工作界面
            Intent intent = new Intent(DishModeSelectActivity.this, DishWorkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dish_mode_select;
    }

    @Override
    protected void initView() {

        rvMode = findViewById(R.id.rv_mode);
        tvTemp = findViewById(R.id.tv_temp);
        tvTime = findViewById(R.id.tv_time);
        tvWater = findViewById(R.id.tv_water);
        rvModeParam = findViewById(R.id.rv_mode_param);
        llOrder = findViewById(R.id.ll_order);
        sbOrderSwitch = findViewById(R.id.sb_order_switch);
        rlOrder = findViewById(R.id.rl_order);
        tvOrderTime = findViewById(R.id.tv_order_time);
        btnComplete = findViewById(R.id.btn_complete);

        rvMode.setLayoutManager(new GridLayoutManager(this, 4));
        rvModeAdapter = new RvDishModeAdapter();
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
                .setScale(0.7f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvDishParamPickerAdapter.setIndex(position);
                    }
                })
                .build();
        rvModeParam.setLayoutManager(manager);
        rvDishParamPickerAdapter = new RvDishParamPickerAdapter(this);
        rvModeParam.setAdapter(rvDishParamPickerAdapter);
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

        setOnClickListener(btnComplete , rlOrder);
    }

    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);
        if (mDevice instanceof AbsDishWasher) {
             dishWasher = (AbsDishWasher) mDevice;
        }
        try {
            List<DeviceConfigurationFunctions> deviceConfigurationFunctions =
                    deviceConfigurationFunction.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0) {
                toast("功能参数未配置");
                return;
            }
            rvModeAdapter.addData(deviceConfigurationFunctions);
            setModeParam(rvModeAdapter.getItem(rvModeAdapter.selectPosition));
        } catch (Exception e) {
            toast("功能参数配置错误");
            return;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setModeParam(DeviceConfigurationFunctions item) {
        String functionParams = item.functionParams;
        try {
            if (functionParams != null) {
                 paramBean = null;
                paramBean = JsonUtils.json2Pojo(functionParams, ParamBean.class);
                ParamBean.WorkDesDTO workDesDTO = paramBean.work_des.get(0);
                tvTemp.setText(workDesDTO.value + workDesDTO.unit);
                ParamBean.WorkDesDTO workDesDTO2 = paramBean.work_des.get(1);
                tvTime.setText(workDesDTO2.value + workDesDTO2.unit);
                ParamBean.WorkDesDTO workDesDT03 = paramBean.work_des.get(2);
                tvWater.setText(workDesDT03.value + workDesDT03.unit);

                rvDishParamPickerAdapter.setData(paramBean.assist_func);
                rvDishParamPickerAdapter.setIndex(0);
                manager.scrollToPosition(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast("功能参数配置错误");
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnComplete){
            if (sbOrderSwitch.isChecked()){
                int ordertime_min = HelperRikaData.getMinGap(orderDate);
                int ordertime_hour = HelperRikaData.getHousGap(orderDate);
                //预约多少分钟后开始
                orderMinu = ordertime_hour * 60 + ordertime_min  ;
            }else {
                orderMinu = 0  ;
            }

            ParamBean.AssistFuncDTO item = rvDishParamPickerAdapter.getItem(rvDishParamPickerAdapter.getIndex());
            switch (item.functionName) {
                case "under_wash" :
                    downWsshSw = 1 ;
                    break ;
                case "assist_gzhq" :
                    autoVentiSw = 1 ;
                    break ;
                case "assist_jqhg" :
                    drySw = 1 ;
                    break ;
            }
            if (dishWasher.powerStatus == 0) {
                //关机状态需要先开机
                dishWasher.setDishWasherStatusControl((short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                        startWork(paramBean.model , downWsshSw ,autoVentiSw   , drySw , orderMinu == 0 ? 0 : 1 , orderMinu , 0);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else {
                startWork(paramBean.model , downWsshSw ,autoVentiSw   , drySw , orderMinu == 0 ? 0 : 1 , orderMinu , 0);
            }



        }else if(view == rlOrder){
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
        }
    }


    /**
     *
     * @param code 模式code
     * @param downWsshSw 下层洗开关
     * @param autoVentiSw 自动换气开关
     * @param drySw 加强干燥开关
     * @param orderTime 预约时间 分钟
     * @param auxFunc
     * @param orderTime
     */
    public  void startWork(int code , int downWsshSw ,int autoVentiSw  , int drySw , int orderSw , int orderTime ,  int auxFunc){
        dishWasher.setDishWasherWorkMode(code, downWsshSw, autoVentiSw, drySw, orderSw, orderTime, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
