package com.robam.roki.ui.page.device.water;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.ext.BasePage;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.roki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/7.
 * PS: Not easy to write code, please indicate.
 */
@SuppressLint("ValidFragment")
public class FilterStateTwoPage extends BasePage {

    AbsWaterPurifier mWaterPurifier;
    JSONArray mParams;
    @InjectView(R.id.iv_outline_border)
    ImageView mIvOutlineBorder;
    @InjectView(R.id.iv_filter_one)
    ImageView mIvFilterOne;
    @InjectView(R.id.tv_percent_pp)
    TextView mTvPercentPp;
    @InjectView(R.id.iv_filter_two)
    ImageView mIvFilterTwo;
    @InjectView(R.id.tv_number)
    TextView mTvNumber;
    @InjectView(R.id.tv_percent_cto)
    TextView mTvPercentCto;
    @InjectView(R.id.iv_filter_three)
    ImageView mIvFilterThree;
    @InjectView(R.id.tv_percent_ro1)
    TextView mTvPercentRo1;
    @InjectView(R.id.iv_filter_fore)
    ImageView mIvFilterFore;
    @InjectView(R.id.tv_percent_ro2)
    TextView mTvPercentRo2;
    @InjectView(R.id.tv_no)
    TextView mTvNo;
    @InjectView(R.id.tv_filter_title)
    TextView mTvFilterTitle;
    @InjectView(R.id.tv_msg)
    TextView mTvMsg;

    public FilterStateTwoPage(AbsWaterPurifier waterPurifier, JSONArray params) {
        mWaterPurifier = waterPurifier;
        mParams = params;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_filter_two, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        if (mWaterPurifier.filter_state_cto <= 10 || mWaterPurifier.filter_state_cto == 255) {
            mIvFilterTwo.setImageResource(R.mipmap.ic_bord_orang);
            mTvPercentCto.setTextColor(getResources().getColor(R.color.c66));
            mTvNumber.setTextColor(getResources().getColor(R.color.c66));
        } else {
            mIvFilterTwo.setImageResource(R.mipmap.ic_bord_blue);
            mTvPercentCto.setTextColor(getResources().getColor(R.color.c67));
            mTvNumber.setTextColor(getResources().getColor(R.color.c67));
        }
        mTvPercentPp.setText((mWaterPurifier.filter_state_pp == 255 ? 0 : mWaterPurifier.filter_state_pp) + "%");
        mTvPercentCto.setText((mWaterPurifier.filter_state_cto == 255 ? 0 : mWaterPurifier.filter_state_cto) + "%");
        mTvPercentRo1.setText((mWaterPurifier.filter_state_ro1 == 255 ? 0 : mWaterPurifier.filter_state_ro1) + "%");
        mTvPercentRo2.setText((mWaterPurifier.filter_state_ro2 == 255 ? 0 : mWaterPurifier.filter_state_ro2) + "%");
        if (mParams == null) return;
        try {
            JSONObject jsonOne = (JSONObject) mParams.get(1);
            JSONObject oneStep = (JSONObject) jsonOne.get("2");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
