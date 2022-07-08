package com.robam.roki.ui.page.device.water;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/11/7.
 */

public class WaterPurifierFilterAboutExpirePage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_filter_change)
    TextView mTvFilterChange;
    @InjectView(R.id.iv_center_img)
    ImageView mIvCenterImg;
    @InjectView(R.id.tv_filter_expire_name)
    TextView mTvFilterExpireName;
    @InjectView(R.id.tv_filter_expire_dec)
    TextView mTvFilterExpireDec;
    private List<DeviceConfigurationFunctions> mExpireList;
    private List<DeviceConfigurationFunctions> mAboutexpireList;
    private AbsWaterPurifier mWaterPurifier;
    private String singAboutExpire;
    private String singExpire;
    private String mFroneDesc;
    private String mCenterDesc;
    private String mFunctionName;
    private String mFunctionParams;
    private DeviceConfigurationFunctions mFunctions;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mWaterPurifier = (AbsWaterPurifier) bundle.getSerializable(PageArgumentKey.Bean);
            mExpireList = (List<DeviceConfigurationFunctions>) bundle.getSerializable(PageArgumentKey.List);
            mAboutexpireList = (List<DeviceConfigurationFunctions>) bundle.getSerializable(PageArgumentKey.mAboutexpireList);
            singAboutExpire = bundle.getString(PageArgumentKey.singAboutExpire);
            singExpire = bundle.getString(PageArgumentKey.singExpire);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_filter_about_expire, container, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {

        LogUtils.i("20181107", "singAboutExpire:" + singAboutExpire);
        LogUtils.i("20181107", "singExpire:" + singExpire);

        if (!TextUtils.isEmpty(singAboutExpire)) {

            if (mAboutexpireList != null && mAboutexpireList.size() > 0) {
                for (int i = 0; i < mAboutexpireList.size(); i++) {
                    if ("ReplaceFilterCore".equals(mAboutexpireList.get(i).functionCode)) {
                        mFunctions = mAboutexpireList.get(i);
                    } else if ("filterElementDueState".equals(mAboutexpireList.get(i).functionCode)) {
                        mTvDeviceModelName.setText(mAboutexpireList.get(i).functionName);
                        String backgroundImg = mAboutexpireList.get(i).backgroundImg;
                        Glide.with(cx).load(backgroundImg).into(mIvCenterImg);
                        String functionParams = mAboutexpireList.get(i).functionParams;

                        try {
                            JSONObject jsonObject = new JSONObject(functionParams);
                            String title = jsonObject.optString("title");
                            String msg = jsonObject.optString("msg");
                            subString(msg);
                            mTvFilterExpireName.setText(title);
                            String filterNum = getFilterNum(singAboutExpire);
                            mTvFilterExpireDec.setText(singAboutExpire + mFroneDesc + filterNum + mCenterDesc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(singExpire)) {
            if (mExpireList != null && mExpireList.size() > 0) {

                for (int i = 0; i < mExpireList.size(); i++)
                    if ("ReplaceFilterCore".equals(mExpireList.get(i).functionCode)) {
                        mFunctions = mAboutexpireList.get(i);
                    } else if ("filterCoreExpires".equals(mExpireList.get(i).functionCode)) {
                        mTvDeviceModelName.setText(mExpireList.get(i).functionName);
                        String backgroundImg = mExpireList.get(i).backgroundImg;
                        Glide.with(cx).load(backgroundImg).into(mIvCenterImg);
                        String functionParams = mExpireList.get(i).functionParams;

                        try {
                            JSONObject jsonObject = new JSONObject(functionParams);
                            String title = jsonObject.optString("title");
                            String msg = jsonObject.optString("msg");
                            subString(msg);
                            mTvFilterExpireName.setText(title);
                            mTvFilterExpireDec.setText(singExpire + mFroneDesc);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }

    }

    private String getFilterNum(String singExpire) {
        switch (singExpire) {
            case "1":

                return mWaterPurifier.filter_state_pp + "";
            case "2":

                return mWaterPurifier.filter_state_cto + "";
            case "3":

                return mWaterPurifier.filter_state_ro1 + "";
            case "4":

                return mWaterPurifier.filter_state_ro2 + "";
        }
        return "";
    }

    private void subString(String value) {

        String[] strings = value.split("button");
        for (int i = 0; i < strings.length; i++) {
            if (1 == i) {
                mFroneDesc = strings[i];
            } else if (2 == i) {
                mCenterDesc = strings[i];
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.tv_filter_change)
    public void onMTvFilterChangeClicked() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PageArgumentKey.List, mFunctions);
        UIService.getInstance().postPage(PageKey.WaterPurifierFilterReplacement, bundle);
    }


}
