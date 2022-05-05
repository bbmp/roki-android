package com.robam.roki.ui.page.device.fan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.robam.roki.ui.view.wave.MnScaleBar2;
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
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/2/21.
 * PS: 防干烧页面.
 */
public class DeviceFanDryCleaningPage extends BasePage {

    @InjectView(R.id.mwv_left)
    MyWaveView myWaveViewLeft;
    @InjectView(R.id.mwv_right)
    MyWaveView myWaveViewRight;
    @InjectView(R.id.scale_bar)
    MnScaleBar2 mnScaleBar;
    @InjectView(R.id.tv_left_temp)
    TextView tvLeftTemp;
    @InjectView(R.id.tv_right_temp)
    TextView tvRightTemp;

    @InjectView(R.id.tv_explain)
    TextView tvExplain;
    private AbsFan fan;

    List<DeviceConfigurationFunctions> mList;


    List<String> keyList = new ArrayList<>();
    int maxTemp = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_dry_cleaning, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }


    private void initData() {
        if (mList == null || mList.size() == 0) return;
        for (int i = 0; i < mList.size(); i++) {
            if ("dryCleaning".equals(mList.get(i).functionCode)) {
                String functionParams = mList.get(i).functionParams;
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = jsonObject.getJSONObject("param");
                    JSONArray tempJson = param.getJSONObject("temp").getJSONArray("value");
                    JSONObject desc = param.getJSONObject("desc");

                    List<Integer> temp = new ArrayList<>();
                    for (int k = 0; k < tempJson.length(); k++) {
                        Integer tem = (Integer) tempJson.get(k);
                        temp.add(tem);
                    }
                    List<Integer> tempList = TestDatas.createModeDataTemp(temp);

                    //将json转换成Map集合
                    Map<String, String> parse = (Map<String, String>) JSON.parse(desc.toString());
                    LogUtils.i("20191031444", "parse:::" + parse);
                    //Activity中往自定义View中传值（实现要点：在自定义View中创建set方法）
                    mnScaleBar.setTempList(tempList);
                    mnScaleBar.setMaps(parse);

                    Set<String> strings = parse.keySet();
                    for (String key : strings) {
                        keyList.add(key);
                    }


                    if (keyList != null) {
                        if (keyList.size() != 0) {
                            for (int i1 = 0; i1 < keyList.size(); i1++) {
                                if (maxTemp < Integer.valueOf(keyList.get(i1))) {
                                    maxTemp = Integer.valueOf(keyList.get(i1));

                                }
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {

        if (fan == null || !Objects.equals(fan.getDt(), event.pojo.getDt())) return;
        fan = event.pojo;
        int temperatureReportOne = fan.temperatureReportOne;
        int temperatureReportTwo = fan.temperatureReportTwo;

        myWaveViewLeft.startProgress(temperatureReportOne * 5);
        myWaveViewRight.startProgress(temperatureReportTwo * 5);


        tvLeftTemp.setText(temperatureReportOne + "℃");
        tvRightTemp.setText(temperatureReportTwo + "℃");


        if (maxTemp != 0) {
            if (temperatureReportOne >= maxTemp || temperatureReportTwo >= maxTemp) {
                tvExplain.setVisibility(View.VISIBLE);
            } else {
                tvExplain.setVisibility(View.INVISIBLE);
            }


        }

    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
}
