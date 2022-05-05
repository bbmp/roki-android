package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.robam.common.events.WaterPurifiyStatusChangedEvent;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.roki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FilterStateThreeView extends FrameLayout {

    @InjectView(R.id.iv_filter_three)
    ImageView mIvFilterThree;
    @InjectView(R.id.tv_percent_pp)
    TextView mTvPercentPp;
    @InjectView(R.id.tv_percent_cto)
    TextView mTvPercentCto;
    @InjectView(R.id.tv_percent_ro1)
    TextView mTvPercentRo1;
    @InjectView(R.id.tv_percent_ro2)
    TextView mTvPercentRo2;
    @InjectView(R.id.tv_number)
    TextView mTvNumber;
    @InjectView(R.id.tv_no)
    TextView mTvNo;
    @InjectView(R.id.tv_filter_title)
    TextView mTvFilterTitle;
    @InjectView(R.id.tv_msg)
    TextView mTvMsg;
    AbsWaterPurifier mWaterPurifier;
    JSONArray mParams;

    public FilterStateThreeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FilterStateThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilterStateThreeView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_filter_three, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setPercentData(int percent_pp, int percent_cto, int percent_ro1, int percent_ro2, JSONArray params) {
        Log.e("201903","three percent_pp:" + percent_pp + " percent_cto:" + percent_cto + " percent_ro1:"
                + percent_ro1 + " percent_ro2:" + percent_ro2);
        if (percent_ro1 <= 10) {
            mIvFilterThree.setImageResource(R.mipmap.ic_bord_orang);
            mTvPercentRo1.setTextColor(getResources().getColor(R.color.c66));
            mTvNumber.setTextColor(getResources().getColor(R.color.c66));
        } else {
            mIvFilterThree.setImageResource(R.mipmap.ic_bord_blue);
            mTvPercentRo1.setTextColor(getResources().getColor(R.color.c67));
            mTvNumber.setTextColor(getResources().getColor(R.color.c67));
        }
        mTvPercentPp.setText(percent_pp + "%");
        mTvPercentCto.setText(percent_cto + "%");
        mTvPercentRo1.setText(percent_ro1 + "%");
        mTvPercentRo2.setText(percent_ro2 + "%");
        if (params == null) return;
        try {
            JSONObject jsonOne = (JSONObject) params.get(2);
            JSONObject oneStep = (JSONObject) jsonOne.get("3");
            String no = oneStep.optString("no");
            String msg = oneStep.optString("msg");
            String title = oneStep.optString("title");
            mTvNo.setText(no + "号滤芯");
            mTvMsg.setText(msg);
            mTvFilterTitle.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
