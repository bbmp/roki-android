package com.robam.roki.ui.activity3.device.dishwasher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeAdapter;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
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
 *     desc   : 洗碗机免打扰设置
 *     version: 1.0
 * </pre>
 */
public class DishDisturbActivity extends DeviceBaseFuntionActivity {


    private SwitchButton sbDisturb;
    private LinearLayout llDishturb;
    private TextView tvStartTime;
    private TextView tvEndTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dish_disturb;
    }

    @Override
    protected void initView() {
        sbDisturb = findViewById(R.id.sb_disturb);
        llDishturb = findViewById(R.id.ll_dishturb);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        sbDisturb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean checked) {
                llDishturb.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void dealData() {
        //设置title
        setTitle("设置免打扰");

    }



}
