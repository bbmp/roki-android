package com.robam.roki.ui.page.device.water;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.roki.R;
import com.robam.roki.model.bean.ReplaceFilterCoreParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.WaterFilterReplacementAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/11/6.
 */

public class WaterPurifierFilterReplacementPage extends BasePage {

    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private WaterFilterReplacementAdapter mWaterFilterReplacementAdapter;
    private String mFunctionParams;
    private List<ReplaceFilterCoreParams> mCoreParamses;
    private String mTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            DeviceConfigurationFunctions replaceFilterCoreList = (DeviceConfigurationFunctions) bundle.getSerializable(PageArgumentKey.List);
            mTitle = replaceFilterCoreList.functionName;
            mFunctionParams = replaceFilterCoreList.functionParams;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_filter_replacement, container, false);
        ButterKnife.inject(this, view);
        mTvTitle.setText(mTitle);
        initData();
        return view;
    }

    private void initData() {

        try {
            mCoreParamses = JsonUtils.json2List(mFunctionParams, ReplaceFilterCoreParams.class);
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        mWaterFilterReplacementAdapter = new WaterFilterReplacementAdapter(cx, mCoreParamses);
        mRecyclerView.setAdapter(mWaterFilterReplacementAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }
}
