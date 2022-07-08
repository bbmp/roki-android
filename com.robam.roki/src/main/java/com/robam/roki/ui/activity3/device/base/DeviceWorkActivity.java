package com.robam.roki.ui.activity3.device.base;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.RvModeParamPickerAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.RvWorkModeAdapter;
import com.robam.roki.ui.activity3.device.base.adapter.bean.ModeParam;
import com.robam.roki.ui.activity3.device.base.adapter.bean.ModeSingleParam;
import com.robam.roki.ui.activity3.device.base.adapter.bean.WorkModeBean;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.base.view.MyCircleProgress;
import com.robam.roki.ui.mdialog.PickerLayoutManager;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.StringUtil;
import com.robam.widget.layout.SettingBar;
import com.robam.widget.view.SwitchButton;

import java.text.ParseException;
import java.util.List;

import skin.support.content.res.SkinCompatResources;

/**
 * author : huxw
 * time   : 2022/06/23
 * desc   : 设备工作界面
 */
public abstract class DeviceWorkActivity extends DeviceBaseFuntionActivity {

    /**
     * 工作顶部mode显示
     */
    private RecyclerView rvMode;
    /**
     * 进度条
     */
    private MyCircleProgress cpgBar;
    private TextView tvMessageTop;
    private FrameLayout tvMessageCentre;
    private TextView tvMessageBotton;
    private Button btnLeft;
    private Button btnRight;
    /**
     * 顶部模式选择adapter
     */
    private RvWorkModeAdapter rvWorkModeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_work;
    }

    @Override
    protected void initView() {

        rvMode = findViewById(R.id.rv_mode);
        cpgBar = findViewById(R.id.cpg_bar);
        tvMessageTop = findViewById(R.id.tv_message_top);
        tvMessageCentre = findViewById(R.id.tv_message_centre);
        tvMessageBotton = findViewById(R.id.tv_message_botton);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        setOnClickListener(btnLeft, btnRight);
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        setTitle(mDevice.getDispalyType());
        rvWorkModeAdapter = new RvWorkModeAdapter();
        rvMode.setAdapter(rvWorkModeAdapter);
    }

    /**
     * 设置/更新顶部数据
     *
     * @param workModeBeans
     */
    public void setTopMode(List<WorkModeBean> workModeBeans) {
        rvMode.setLayoutManager(new GridLayoutManager(this, workModeBeans.size()));
        rvWorkModeAdapter.setNewInstance(workModeBeans);
    }

    /**
     * 设置两个按钮提示
     *
     * @param left
     * @param right
     */
    public void setButtonText(String left, String right) {
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnLeft.setText(left);
        btnRight.setText(right);
    }

    /**
     * 设置只显示一个按钮
     *
     * @param buttonText
     */
    public void setButtonOneText(String buttonText) {
        btnLeft.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(buttonText);
    }

    /**
     * 隐藏全部按钮
     */
    public void hideButton(){
        btnLeft.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);
    }
    /**
     * 设置显示数据
     *
     * @param topMessage
     * @param centre
     * @param bottMessge
     */
    public void setMessage(String topMessage, View centre, String bottMessge) {
        tvMessageTop.setVisibility(View.VISIBLE);
        tvMessageBotton.setVisibility(View.VISIBLE);
        tvMessageTop.setText(topMessage);
        tvMessageCentre.removeAllViews();
        tvMessageCentre.addView(centre);
        tvMessageBotton.setText(bottMessge);
    }

    /**
     * 设置中间显示数据
     *
     * @param centre
     */
    public void setMessageCentre(View centre) {
        tvMessageCentre.removeAllViews();
        tvMessageCentre.addView(centre);
        tvMessageTop.setVisibility(View.INVISIBLE);
        tvMessageBotton.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置进度条最大值
     *
     * @param max
     */
    public void setProgressMax(int max) {
        cpgBar.setMax(max);
    }

    /**
     * 进度条值
     *
     * @param cur
     */
    public void setProgressCur(int cur) {
        cpgBar.setCurrent(cur);
    }

    /**
     * 设置进度条暂停（主要控制颜色）
     */
    public void setProgressPause() {
        cpgBar.setColor(SkinCompatResources.getColor(getContext(), R.color.circle_color_progress));
    }

    /**
     * 设置进度条开始（主要控制颜色）
     */
    public void setProgressStart() {
        cpgBar.setColor(SkinCompatResources.getColor(getContext(), R.color.common_dialog_btn_normal_bg_blue));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnLeft) {
            onControlLeftClick(btnLeft);
        } else if (view == btnRight) {
            if (btnLeft.getVisibility() == View.GONE) {
                onControlOneClick(btnRight);
            } else {
                onControlRightClick(btnRight);
            }
        }
    }

    /**
     * 左边点击事件
     *
     * @param btnLeft
     */
    protected abstract void onControlLeftClick(Button btnLeft);

    /**
     * 右边点击事件
     *
     * @param btnRight
     */
    protected abstract void onControlRightClick(Button btnRight);

    /**
     * 一个按钮的点击事件
     *
     * @param btnRight
     */
    protected abstract void onControlOneClick(Button btnRight);
}