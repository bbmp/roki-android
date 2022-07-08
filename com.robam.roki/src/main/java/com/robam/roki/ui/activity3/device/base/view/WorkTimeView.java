package com.robam.roki.ui.activity3.device.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robam.roki.R;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/07/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WorkTimeView extends FrameLayout {

    private TextView tvHour;
    private TextView tvUnitHour;
    private TextView tvMinu;
    private TextView tvUnitMinu;

    public WorkTimeView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_device_work_time, null);
        tvHour = view.findViewById(R.id.tv_hour);
        tvUnitHour = view.findViewById(R.id.tv_unit_hour);
        tvMinu = view.findViewById(R.id.tv_minu);
        tvUnitMinu = view.findViewById(R.id.tv_unit_minu);
        addView(view);
    }

    public void setTvHourMinu(int hour , int minu) {
        tvHour.setText(String.valueOf(hour));
        tvMinu.setText(String.valueOf(minu));
        tvHour.setVisibility(VISIBLE);
        tvUnitHour.setVisibility(VISIBLE);
        tvMinu.setVisibility(VISIBLE);
        tvUnitMinu.setVisibility(VISIBLE);
    }

    /**
     * 只设置分钟时候 隐藏小时
     * @param minu
     */
    public void setTvMinu(int minu) {
        tvMinu.setText(String.valueOf(minu) );
        tvHour.setVisibility(GONE);
        tvUnitHour.setVisibility(GONE);
    }

    /**
     * 设置中间提示message
     * @param message
     */
    public void setMessage(String message){
        tvHour.setText(message );
        tvUnitHour.setVisibility(GONE);
        tvUnitMinu.setVisibility(GONE);
        tvUnitHour.setVisibility(GONE);
    }
}
