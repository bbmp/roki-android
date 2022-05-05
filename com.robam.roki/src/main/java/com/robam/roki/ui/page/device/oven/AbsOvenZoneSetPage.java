package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 分区模式设置页面
 */

public class AbsOvenZoneSetPage extends BasePage {

    @InjectView(R.id.tv_title_name)
    TextView tvTitleName;

    @InjectView(R.id.wv1_top)
    WheelView wv1Top;

    @InjectView(R.id.wv2_top)
    WheelView wv2Top;

    @InjectView(R.id.text_desc_top)
    TextView textDescTop;

    @InjectView(R.id.wv1_bottom)
    WheelView wv1Bottom;

    @InjectView(R.id.wv2_bottom)
    WheelView wv2Bottom;


    @InjectView(R.id.text_desc_bottom)
    TextView textDescBottom;

    @InjectView(R.id.btn_work)
    Button btnWork;

    @InjectView(R.id.tv_bottom_name)
    TextView tvBottomName;

    @InjectView(R.id.tv_top_name)
    TextView tvTopName;


    List<Integer> tempTop;
    List<Integer> timeTop;
    int defaultTempTop;
    int defaultTimeTop;
    String str1 = "℃";//单位1
    String str2 = "分钟";//单位2


    List<Integer> tempBottom;
    List<Integer> timeBottom;
    int defaultTempBottom;
    int defaultTimeBottom;
    private int indexSelectTempTop;
    private int indexSelectTimeTop = 0;
    private int indexSelectTempBottom;
    private int indexSelectTimeBottom = 0;
    private String combination;

    protected String mGuid;
    private AbsOven oven;
    private String mode;
    private int cmd;
    private String from;


    List<Integer> tempDown;
    private String tempDiff;
    private String tempStart;
    private String tempMin;
    private int max;
    private int deNum1;
    private int deNum2;
    private int defaultTopTime;
    private int defaultBottomTime;
    String code;
    private long userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_oven_zone_set_page, container, false);
        Bundle bd = getArguments();
        combination = bd == null ? null : bd.getString(PageArgumentKey.combination);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? null : bd.getString(PageArgumentKey.from);
        code = bd == null ? null : bd.getString(PageArgumentKey.code);
        userId = Plat.accountService.getCurrentUserId();
        oven = Plat.deviceService.lookupChild(mGuid);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        try {
            JSONObject jsonObject1 = new JSONObject(combination);
            cmd = jsonObject1.getInt("cmd");
            mode = jsonObject1.getJSONObject("mode").getString("value");
            JSONObject jsonObject = jsonObject1.getJSONObject("combination");
            JSONObject top = jsonObject.getJSONObject("top");
            JSONObject bottom;
            try {
                bottom = jsonObject.getJSONObject("bottem");
            } catch (Exception e) {
                e.printStackTrace();
                bottom = jsonObject.getJSONObject("bottom");
            }
            String topName = top.getJSONObject("name").getString("value");
            JSONArray topMin = top.getJSONObject("minute").getJSONArray("value");
            JSONArray topTemp = top.getJSONObject("temp").getJSONArray("value");
            String topDefaultMin = top.getJSONObject("defaultMinute").getString("value");
            String topdefaultTemp = top.getJSONObject("defaultTemp").getString("value");

            String bottomName = bottom.getJSONObject("name").getString("value");
            JSONArray bottomMin = bottom.getJSONObject("minute").getJSONArray("value");
            JSONArray bottomTemp = bottom.getJSONObject("temp").getJSONArray("value");
            String bottomDefaultMin = bottom.getJSONObject("defaultMinute").getString("value");
            String bottomdefaultTemp = bottom.getJSONObject("defaultTemp").getString("value");
            //温度差
            tempDiff = jsonObject1.getJSONObject("tempDiff").getString("value");
            tempStart = jsonObject1.getJSONObject("tempStart").getString("value");
            //最小温度
            tempMin = jsonObject1.getJSONObject("tempMin").getString("value");

            tvTopName.setText(topName);
            tvBottomName.setText(bottomName);
            tvTitleName.setText("分区模式");

            //上层时间
            List<Integer> topTimes = new ArrayList<>();
            for (int i = 0; i < topMin.length(); i++) {
                Integer min = (Integer) topMin.get(i);
                topTimes.add(min);
            }

            //上层温度
            List<Integer> topTemps = new ArrayList<>();
            for (int i = 0; i < topTemp.length(); i++) {
                Integer temp = (Integer) topTemp.get(i);
                topTemps.add(temp);
            }

            tempTop = TestDatas.createModeDataTemp(topTemps);
            timeTop = TestDatas.createModeDataTime(topTimes);
            defaultTempTop = Integer.parseInt(topdefaultTemp);
            defaultTimeTop = Integer.parseInt(topDefaultMin);


            //下层时间
            List<Integer> bottomTimes = new ArrayList<>();
            for (int i = 0; i < bottomMin.length(); i++) {
                Integer min = (Integer) bottomMin.get(i);
                bottomTimes.add(min);
            }
            //下层温度
            List<Integer> bottomTemps = new ArrayList<>();
            for (int i = 0; i < bottomTemp.length(); i++) {
                Integer temp = (Integer) bottomTemp.get(i);
                bottomTemps.add(temp);
            }
            tempBottom = TestDatas.createModeDataTemp(bottomTemps);
            timeBottom = TestDatas.createModeDataTime(bottomTimes);
            defaultTempBottom = Integer.parseInt(bottomdefaultTemp);
            defaultTimeBottom = Integer.parseInt(bottomDefaultMin);


            //上层最大温度
            if (tempTop.size() > 0) {
                max = tempTop.get(tempTop.size() - 1);
            }
            deNum1 = defaultTempTop - tempTop.get(0);
            tempDown = TestDatas.createExpDownDatas(
                    tempTop.get(deNum1),
                    Integer.valueOf(tempDiff),
                    Integer.valueOf(tempStart),
                    max,
                    Integer.parseInt(tempMin));


            deNum2 = defaultTempBottom - tempDown.get(0);

            defaultTopTime = Integer.parseInt(topDefaultMin) - topTimes.get(0);
            defaultBottomTime = Integer.parseInt(bottomDefaultMin) - bottomTimes.get(0);

            setWeelView();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    boolean isLinkage = true;

    protected void setWeelView() {
        indexSelectTimeTop = defaultTopTime;
        indexSelectTimeBottom = defaultBottomTime;
        wv1Top.setDefaultPosition(deNum1);
        wv2Top.setDefaultPosition(defaultTopTime);
        wv1Bottom.setDefaultPosition(deNum2);
        wv2Bottom.setDefaultPosition(defaultBottomTime);
        indexSelectTempTop = deNum1;
        indexSelectTempBottom = deNum2;

        wv1Top.setAdapter(new TimeAdapter<Integer>(tempTop, str1));
        wv1Top.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final int index) {
                indexSelectTempTop = index;
                defaultTempTop = tempTop.get(index);
                if (tempDown != null) {
                    tempDown.clear();
                }

                tempDown = TestDatas.createExpDownDatas(tempTop.get(index), Integer.valueOf(tempDiff),
                        Integer.valueOf(tempStart), max, Integer.parseInt(tempMin));

                //根据硬件逻辑修改,第一次上滑温度值,下层跟着变,只要滑动过一次下温度值,再滑动上温度,下温度值就不会跟着改变了
                if (!isLinkage) {

                    for (int i = 0; i < tempDown.size(); i++) {
                        if (tempDown.get(i).equals(defaultTempBottom)) {
                            wv1Bottom.setDefaultPosition(i);
                            indexSelectTempBottom = i;
                        }

                    }

                }
                if (tempDown != null && wv1Bottom != null) {
                    wv1Bottom.setAdapter(new TimeAdapter<>(tempDown, str1));
                }


            }
        });


        wv2Top.setAdapter(new TimeAdapter<Integer>(timeTop, str2));
        wv2Top.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTimeTop = index;
            }
        });


        wv1Bottom.setAdapter(new TimeAdapter<>(tempDown, str1));

        wv1Bottom.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final int index) {
                isLinkage = false;
                indexSelectTempBottom = index;
                defaultTempBottom = tempDown.get(index);
                if (tempTop != null) {
                    tempTop.clear();
                }
                tempTop = TestDatas.createExpDownDatas(
                        tempDown.get(index),
                        Integer.valueOf(tempDiff),
                        Integer.valueOf(tempStart),
                        max,
                        Integer.parseInt(tempMin));

                if (tempTop != null) {
                    for (int i = 0; i < tempTop.size(); i++) {
                        if (tempTop.get(i).equals(defaultTempTop)) {
                            wv1Top.setDefaultPosition(i);
                            indexSelectTempTop = i;
                        }
                    }
                    if (wv1Top != null) {
                        wv1Top.setAdapter(new TimeAdapter(tempTop, str1));
                    }

                }


            }
        });

        wv2Bottom.setAdapter(new TimeAdapter<>(timeBottom, str2));
        wv2Bottom.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTimeBottom = index;
            }
        });
    }


    @OnClick({R.id.btn_work, R.id.mode_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_work:
                send();
                break;
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
        }
    }

    private void send() {
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom();
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                }
            });
        } else {
            sendCom();
        }


    }


    private void sendCom() {
        final int topTemp = tempTop.get(indexSelectTempTop);
        final int topTime = timeTop.get(indexSelectTimeTop);
        final int downTemp = tempDown.get(indexSelectTempBottom);
        final int downTime = timeBottom.get(indexSelectTimeBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                oven.setOvenCombination(
                        (short) cmd,
                        Short.decode(mode),
                        (short) topTemp,
                        (short) topTime,
                        (short) downTemp,
                        (short) downTime,

                        new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                sendMul(code);
                                LogUtils.i("20191102", "onSuccess:" + code);
                                //首页跳转来的
                                if (from.equals("1")) {
                                    UIService.getInstance().popBack();
                                } else {
                                    //分区列表页跳转来的
                                    UIService.getInstance().popBack().popBack();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                                LogUtils.i("20191102", t.getMessage());
                            }
                        }

                );


            }
        }, 500);


    }


    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, mGuid, code, oven.getDc(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("20200527", "getReportResponse:::" + getReportResponse.msg);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20200527", "message:::" + t.getMessage());
            }
        });
    }
}
