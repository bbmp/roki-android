package com.robam.roki.utils.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataDescriptBean;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.robam.roki.R;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.request.bean.RecipeCurveSuccessBean;
import com.robam.roki.ui.floating.FloatingService;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.ui.view.LineChartShowView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态折线图管理器
 */
public class DynamicLineChartManager {
    private LineChart lineChart;
    //    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private YAxis yAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private Context context;

    private boolean isShowLastMarkView = false;

    //一条曲线
    public DynamicLineChartManager(LineChart mLineChart, Context mContext) {
        this.lineChart = mLineChart;
        this.context = mContext;
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        yAxis = lineChart.getAxisLeft();
        initLineChart();
//        initLineDataSet(name, color);

    }

    /**
     * 初始化LineChar图表对象
     */
    private void initLineChart() {

        // 不显示数据描述
        lineChart.getDescription().setEnabled(false);
        //禁止x轴y轴同时进行缩放
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setEnabled(false);//关闭右侧Y轴
        lineChart.setDrawGridBackground(false);
        lineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。
        lineChart.setScaleEnabled(true);// 是否可以缩放
//        lineChart.setDragOffsetX(100);
        /***折线图例 标签 设置***/
        Legend legend = lineChart.getLegend();
        //设置显示类型，LINE(线) CIRCLE(圆) SQUARE(方) EMPTY(无)  等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.EMPTY);
        legend.setTextSize(0f);
        legend.setEnabled(false);
        //描述显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        setXYAxis();

        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        //添加一个空的 LineData
        lineData = new LineData(lineDataSets);
        lineChart.setData(lineData);
//        //设置在曲线图中显示的最大数量
//        lineChart.setVisibleXRangeMaximum(7);
    }

    /**
     * 设置XY轴数据样式
     */
    private void setXYAxis() {
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);

        //X轴显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return timeList.get((int) value % timeList.size());
                if (value <= 0) {
                    return "";
                } else if (value % 60 == 0) {
                    return (int) value / 60 + "min";
                } else {
                    return (int) value + "s";
                }
            }
        });
        //是否绘制y轴网格线  }
        yAxis.setDrawGridLines(false);
        yAxis.setAxisLineColor(Color.TRANSPARENT);
        yAxis.setGridColor(Color.parseColor("#FFD8D7E4"));
        yAxis.setLabelCount(5);
        //保证Y轴从0开始，不然会上移一点
        yAxis.setAxisMinimum(0f);

        //Y轴显示格式
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value == 0) {
                    return 0 + "";
                } else {
                    return (int) value + "℃";
                }
            }
        });
//        rightAxis.setAxisMinimum(0f);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.TRANSPARENT);//设置X轴颜色
        xAxis.setDrawAxisLine(false);//是否绘制X轴
        xAxis.setDrawGridLines(false);//是否绘制X轴网格线
        xAxis.setGridColor(Color.parseColor("#FFD8D7E4"));
        //图表第一个和最后一个label数据不超出左边和右边的Y轴
        xAxis.setAvoidFirstLastClipping(true);
    }


    /**
     * 初始化折线(一条线)
     * <p>
     * lineDataSet 是针对本条曲线的配置对象
     *
     * @param name         曲线名称
     * @param color        颜色
     * @param list         曲线列表数据
     * @param isDrawFilled 是否填充
     */
    public void initLineDataSet(String name, int color, List list, boolean isDrawFilled) {
        if (list == null || list.isEmpty()) {
            lineDataSet = new LineDataSet(null, name);
        } else {
            lineDataSet = new LineDataSet(list, name);
        }
        //曲线宽度
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        //是否禁用点击高亮线
        lineDataSet.setHighlightEnabled(true);
        //显示折线上的值
        lineDataSet.setDrawValues(false);
        //圆圈大小
        lineDataSet.setCircleRadius(2.5f);
        //是否画圆点
        lineDataSet.setDrawCircles(false);
        //设置曲线值的圆点是实心还是空心
//        lineDataSet.setDrawCircleHole(true);
        //设置曲线填充
        lineDataSet.setDrawFilled(isDrawFilled);
        //设置填充
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_fill);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(context.getResources().getColor(R.color.line_chart_easy));
        }
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(10f);

//        lineDataSet.setDrawCircles(true);//是否绘制两个点之间的圆点
//        lineDataSet.setDrawCirclesLast(true);//只绘制最后一个圆点
        //todo 模拟设置园点绘制位置和是否实心，1实心 2空心
//        List<Entry> appointList = new ArrayList<>();
//        appointList.add(new Entry(0,1));
//        appointList.add(new Entry(10,1));
//        appointList.add(new Entry(20,1));
//        appointList.add(new Entry(30,2));
//        appointList.add(new Entry(40,2));
//        lineDataSet.setAppointList(appointList);
////        //todo 指定位置显示值
//        List<Entry> specialValueList = new ArrayList<>();
//        specialValueList.add(new Entry(0,0,"烹饪开始"));
//        specialValueList.add(new Entry(10,0,"预热完成"));
//        lineDataSet.setSpecialValueList(specialValueList);

        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSets.add(lineDataSet);
//        if(lineDataSet.getEntries().size()>0){
//            yAxis.setAxisMaxValue(lineDataSet.getYMax()+50);
//        }else{
//            yAxis.setAxisMaxValue(50);
//        }
        lineData.notifyDataChanged();
        lineChart.invalidate();

    }

    public void initLineDataSet(String name, int color, List list, boolean isDrawFilled, int dragOffsetX) {
        lineChart.setDragOffsetX(dragOffsetX);
        if (list == null || list.isEmpty()) {
            lineDataSet = new LineDataSet(null, name);
        } else {
            lineDataSet = new LineDataSet(list, name);
        }
        //曲线宽度
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        //是否禁用点击高亮线
        lineDataSet.setHighlightEnabled(true);
        //显示折线上的值
        lineDataSet.setDrawValues(false);
        //圆圈大小
        lineDataSet.setCircleRadius(2.5f);
        //是否画圆点
//        lineDataSet.setDrawCircles(true);
        //设置曲线值的圆点是实心还是空心
//        lineDataSet.setDrawCircleHole(true);
        //设置曲线填充
        lineDataSet.setDrawFilled(isDrawFilled);
        //设置填充
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_fill);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(context.getResources().getColor(R.color.line_chart_easy));
        }
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(10f);

//        lineDataSet.setDrawCircles(true);//是否绘制两个点之间的圆点
//        lineDataSet.setDrawCirclesLast(true);//只绘制最后一个圆点
        //todo 模拟设置园点绘制位置和是否实心，1实心 2空心
//        List<Entry> appointList = new ArrayList<>();
//        appointList.add(new Entry(0,1));
//        appointList.add(new Entry(10,1));
//        appointList.add(new Entry(20,1));
//        appointList.add(new Entry(30,2));
//        appointList.add(new Entry(40,2));
//        lineDataSet.setAppointList(appointList);
////        //todo 指定位置显示值
//        List<Entry> specialValueList = new ArrayList<>();
//        specialValueList.add(new Entry(0,0,"烹饪开始"));
//        specialValueList.add(new Entry(10,0,"预热完成"));
//        lineDataSet.setSpecialValueList(specialValueList);

        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSets.add(lineDataSet);
        lineData.notifyDataChanged();
        lineChart.invalidate();

    }

    public void lineDataAppointSet(List<Entry> appointList) {
        lineDataSet.setDrawCircles(true);//是否绘制两个点之间的圆点
//        lineDataSet.setDrawCirclesLast(true);//只绘制最后一个圆点
        lineDataSet.setAppointList(appointList);
        lineData.notifyDataChanged();
        lineChart.invalidate();
    }

    /**
     * 添加一条额外得垂直线
     *
     * @param name
     * @param color
     * @param list
     */
    public void addExtraLine(String name, int color, List list) {
        if (list == null || list.isEmpty()) {
            lineDataSet = new LineDataSet(null, name);
        } else {
            lineDataSet = new LineDataSet(list, name);
        }
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        //显示折线上的值
        lineDataSet.setDrawValues(true);
        //设置曲线填充
        lineDataSet.setDrawFilled(false);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        //设置曲线填充
        lineDataSet.setDrawFilled(false);
        //设置填充
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.chart_fill);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(context.getResources().getColor(R.color.line_chart_easy));
        }
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(15f);
        lineDataSet.setValueTextColor(color);
//        lineDataSet.setDrawCircles(true);//是否绘制两个点之间的圆点
//        lineDataSet.setDrawCirclesLast(true);//只绘制最后一个圆点

        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value > 0) {
                    return "预热完成";
                } else {
                    return "";
                }

            }
        });

        lineDataSets.add(lineDataSet);
        lineData.notifyDataChanged();
        lineChart.invalidate();

    }


    /**
     * 动态添加数据（一条折线图）
     * 逐点添加
     *
     * @param entry 数据点
     */
    private LineChartShowView lineChartShowView;

    public void addEntryWithMV(Entry entry) {
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        yAxis.setAxisMaxValue(lineDataSet.getYMax() + 50);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();

//        MPPointD pixels = lineChart.getTransformer(lineChart.getData().getDataSets().get(1).getAxisDependency()).
//                getPixelForValues(entry.getX(), entry.getY());
//
//        Log.d("20220113","X值:"+pixels.x);
//        Log.d("20220113","y值:"+pixels.y);
//        lineChartShowView.setX((float) pixels.x-270);
//        lineChartShowView.setY((float) pixels.y-115);
//        lineChartShowView.setTvValue((int)entry.getY()+"℃");
////        lineChartShowView.setTvValue(280+"℃");
//
//        lineChart.removeAllViews();
//        if(lineChart.getData().getEntryCount()>2){
//            lineChart.addView(lineChartShowView);
//        }

    }

    public void addEntry(Entry entry) {
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);

        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
    }

    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(int number) {

        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        //避免集合数据过多，及时清空（做这样的处理，并不知道有没有用，但还是这样做了）
//        if (timeList.size() > 11) {
//            timeList.clear();
//        }
//
//        timeList.add(df.format(System.currentTimeMillis()));

        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(1f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        hightLimit.setLabel("这是一个lable");
        yAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        yAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    public void setLeftLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(1f);
        hightLimit.setTextSize(10f);
        hightLimit.setLabel("这是一个lable");
        hightLimit.enableDashedLine(2, 2, 2);
        xAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

    /**
     * 设置图表属性
     */
    public void setChartAttribute(boolean isZoom, boolean setHighLine) {
        lineChart.setScaleEnabled(isZoom);// 是否可以缩放
        //是否禁用点击高亮线
//        lineDataSet.setHighlightEnabled(setHighLine);
        lineDataSet.setHighLightColor(Color.TRANSPARENT);
        if (lineDataSet.getEntries().size() > 0) {
            yAxis.setAxisMaxValue(lineDataSet.getYMax() + 50);
        }
//        lineChartShowView = new LineChartShowView(context, xAxis.getValueFormatter());

//        lineChart.setExtraRightOffset(100);
//        lineChart.setExtraTopOffset(50);

    }

    /**
     * 设置图表属性
     */
    public void setShowLastMarkView(boolean showLastMarkView) {
        isShowLastMarkView = showLastMarkView;
    }

    private static final String TAG = "DynamicLineChartManager";
    public void setChartZ(int dragOffsetX,int width) {
        Log.e(TAG,dragOffsetX+"--"+width+"-----"+Utils.convertPixelsToDp(width));
        lineChart.setScaleEnabled(false);// 是否可以缩放
//      lineChart.setExtraRightOffset(100);
//      lineChart.setExtraTopOffset(50);
        lineDataSet.setHighlightEnabled(false);
//        if (dragOffsetX < Utils.convertPixelsToDp(width)) {
            lineChart.setDragOffsetX(Utils.convertPixelsToDp(width)/2);
//        } else {
//            lineChart.setDragOffsetX(dragOffsetX-Utils.convertPixelsToDp(width)/2 );
//        }
    }

    public void setPointText(ArrayList<RecipeCurveSuccessBean.StepList> operationList) {
        //设置点位名称
        List<DataDescriptBean> mOperationList = new ArrayList<>();
        for (int i = 0; i < operationList.size(); i++) {
            String markName = operationList.get(i).getMarkName();
            if (markName.equals("标记打点")) {
                markName = "";
            }
            DataDescriptBean bean = new DataDescriptBean(Float.parseFloat(operationList.get(i).getMarkTime()), Float.parseFloat(operationList.get(i).getMarkTemp()), markName);
            mOperationList.add(bean);
        }
//        lineDataSet.setDrawValues(true);
        lineDataSet.setOperationList(mOperationList);

        lineData.notifyDataChanged();
        lineChart.invalidate();
    }
}
