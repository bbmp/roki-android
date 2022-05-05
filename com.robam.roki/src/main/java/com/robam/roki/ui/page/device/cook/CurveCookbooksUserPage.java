package com.robam.roki.ui.page.device.cook;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.common.collect.Maps;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.roki.R;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.ui.view.recipeclassify.GlideImageLoader;
import com.robam.roki.utils.chart.DynamicLineChartManager;
//import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 烹饪曲线应用
 */

public class CurveCookbooksUserPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
//    @InjectView(R.id.recipethemebanner)
//    Banner banner;
    @InjectView(R.id.tv_voice_long)
    TextView tvVoiceLong;
    @InjectView(R.id.ll_voice_long)
    LinearLayout llVoiceLong;
    @InjectView(R.id.cook_chart)
    LineChart cookChart;
    @InjectView(R.id.btn_suspend)
    Button btnSuspend;
    @InjectView(R.id.btn_finish)
    Button btnFinish;

    private PayLoadCookBook mPayLoadCookBook;
    List<Entry> entryList;
    private List<Integer> list = new ArrayList<>();
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private LineData lineData;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private YAxis yAxis;

    private boolean isSuspend = false;
    private List<Integer> xValue = new ArrayList<>();
    private List<Integer> yValue = new ArrayList<>();

    private DynamicLineChartManager dm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mPayLoadCookBook = bd == null ? null : (PayLoadCookBook) bd.getSerializable("Item");
        View view = inflater.inflate(R.layout.curve_cook_books_user_page, container, false);
        ButterKnife.inject(this, view);
        dm = new DynamicLineChartManager(cookChart, cx);
        initData();
        initView();
        return view;
    }

    private void initData() {
        String temperature = mPayLoadCookBook.curveCookbookDto.temperatureCurveParams;
        temperature = temperature.substring(1, temperature.indexOf("}"));
        String str[] = temperature.split(",");
        Map<String, String> map = Maps.newHashMap();
        for (int i = 0; i < str.length; i++) {
            map.put(str[i].split(":")[0].trim(), str[i].split(":")[1].trim());
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            xValue.add(Integer.parseInt(entry.getKey().replace("\"", "")));
            yValue.add(Integer.parseInt(entry.getValue().replace("\"", "")));
        }
        Collections.sort(xValue);
        Collections.sort(yValue);
    }

    private void initView() {
        List<String> imageUrls = new ArrayList<String>();
        if (mPayLoadCookBook != null) {
            for (int i = 0; i < mPayLoadCookBook.curveCookbookPrepareStepDtos.size(); i++) {
                imageUrls.add(mPayLoadCookBook.curveCookbookPrepareStepDtos.get(i).image);
            }
//            banner.setImages(imageUrls)
//                    .setImageLoader(new GlideImageLoader())
//                    .start();
//            banner.setDelayTime(3500);
        }

        entryList = new ArrayList<>(); //数据集合
//        for (int i = 0; i < 40; i++) {
//            list.add((int) (Math.random() * 150) + 80);
//            entryList.add(new Entry(i, list.get(i)));
//        }
        for (int i = 0; i < xValue.size(); i++) {
            entryList.add(new Entry(xValue.get(i), yValue.get(i)));
        }
        dm.initLineDataSet("烹饪曲线",getResources().getColor(R.color.line_chart_easy),entryList,false);

        singleLine.add(new Entry(100, 0));
        singleLine.add(new Entry(100, 150));
    }
    List<Entry> singleLine=new ArrayList<>();
    @OnClick({R.id.iv_back, R.id.ll_voice_long, R.id.btn_suspend, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.ll_voice_long:
                addEntry();
                break;
            case R.id.btn_suspend:
                if (isSuspend) {
                    isSuspend = false;
                } else {
                    isSuspend = true;
                }
                break;
            case R.id.btn_finish:
                dm.setLowLimitLine(20,"室温");
                dm.setHightLimitLine(80,"高温",Color.YELLOW);
                dm.setLeftLimitLine(40,"预热完成");
                dm.addExtraLine("预热完成+1",getResources().getColor(R.color.line_chart_deep),singleLine);


                break;
        }
    }

    //按钮点击添加数据
    public void addEntry() {

        dm.initLineDataSet("烹饪曲线+1",getResources().getColor(R.color.line_chart_deep),null,true);

        final int[] i = {0};
        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i[0] < entryList.size() - 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ((Activity) cx).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("run:", "run: " + i[0]);
                            if (!isSuspend) {
//                                lineDataSet.addEntry(entryList.get(i[0]));
//                                cookChart.invalidate();
                                dm.addEntry(entryList.get(i[0]),1);
                                i[0]++;
                            }

                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
