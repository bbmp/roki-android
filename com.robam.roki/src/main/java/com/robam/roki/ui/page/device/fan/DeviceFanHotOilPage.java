package com.robam.roki.ui.page.device.fan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.wave.MnScaleBar;
import com.robam.roki.ui.view.wave.MyWaveView;
import com.robam.roki.utils.TestDatas;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * PS: 健康热油.
 */
public class DeviceFanHotOilPage extends BasePage {

    @InjectView(R.id.mwv_left)
    MyWaveView myWaveViewLeft;
    @InjectView(R.id.mwv_right)
    MyWaveView myWaveViewRight;
    @InjectView(R.id.scale_bar)
    MnScaleBar mnScaleBar;
    @InjectView(R.id.tv_left_temp)
    TextView tvLeftTemp;
    @InjectView(R.id.tv_right_temp)
    TextView tvRightTemp;
    @InjectView(R.id.iv_back)
    ImageView ivBack;

    private AbsFan fan;

    List<DeviceConfigurationFunctions> mList;

    @OnClick(R.id.iv_back)
    public void onClick() {
        UIService.getInstance().popBack();
    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {

        if (fan == null || !Objects.equals(fan.getDt(), event.pojo.getDt())){
            return;
        }
        if (fan == null || !Objects.equals(fan.getGuid(), event.pojo.getGuid())){
            return;
        }
        fan = event.pojo;
        int temperatureReportOne = fan.temperatureReportOne;
        int temperatureReportTwo = fan.temperatureReportTwo;

        LogUtils.i("20191031000", "temperatureReportOne:::" + temperatureReportOne);

        myWaveViewLeft.startProgress(temperatureReportOne * 7);
        myWaveViewRight.startProgress(temperatureReportTwo * 7);

        tvLeftTemp.setText(temperatureReportOne + "℃");
        tvRightTemp.setText(temperatureReportTwo + "℃");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_hot_oil, container, false);
        ButterKnife.inject(this, view);
        initData();

        return view;
    }

    private void initData() {
        if (mList == null || mList.size() == 0) return;
        for (int i = 0; i < mList.size(); i++) {
            if ("hotOil".equals(mList.get(i).functionCode)) {
                List<DeviceConfigurationFunctions> hotOilList = mList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                if (hotOilList == null || hotOilList.size() == 0) return;
                for (int j = 0; j < hotOilList.size(); j++) {
                    if (hotOilList.get(j).functionCode.equals("temperature")) {
                        String functionParams = hotOilList.get(j).functionParams;
                        try {
                            JSONObject jsonObject = new JSONObject(functionParams);
                            JSONObject param = jsonObject.getJSONObject("param");
                            JSONArray tempJson = param.getJSONObject("temp").getJSONArray("value");
                            JSONObject desc = param.getJSONObject("desc");

                            //将json转换成Map集合
                            Map<String, String> parse = (Map<String, String>) JSON.parse(desc.toString());


                            List<Integer> temp = new ArrayList<>();
                            for (int k = 0; k < tempJson.length(); k++) {
                                Integer tem = (Integer) tempJson.get(k);
                                temp.add(tem);
                            }

                            List<Integer> tempList = TestDatas.createModeDataTemp(temp);
                            //Activity中往自定义View中传值（实现要点：在自定义View中创建set方法）
                            mnScaleBar.setMax(tempList);
                            mnScaleBar.setMaps(parse);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }


            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }


}
