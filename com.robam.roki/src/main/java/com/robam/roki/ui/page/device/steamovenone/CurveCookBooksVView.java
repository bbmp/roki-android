package com.robam.roki.ui.page.device.steamovenone;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.Transformer;
import com.legent.ui.UIService;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.ui.view.recipeclassify.GlideImageLoader;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CurveCookBooksVView extends FrameLayout {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.recipethemebanner)
    Banner banner;
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
    @InjectView(R.id.img_screen)
    ImageView img_screen;

    Context cx;
    AbsSteameOvenOne steameOven;
    private DynamicLineChartManager dm;
    private PayLoadCookBook mPayLoadCookBook;
    List<Entry> entryList, drawList;
    private boolean isSuspend = false;


    public CurveCookBooksVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public CurveCookBooksVView(Context context, PayLoadCookBook mPayLoadCookBook) {
        super(context);
        this.cx = context;
        this.mPayLoadCookBook = mPayLoadCookBook;
        initView();
    }


    public static String temperature = "{\"22\":\"70-7\",\"44\":\"70-7\",\"24\":\"70-7\",\"46\":\"70-7\",\"26\":\"70-7\",\"48\":\"70-7\",\"28\":\"70-7\",\"50\":\"70-7\",\"52\":\"70-7\",\"31\":\"70-7\",\"10\":\"70-7\",\"32\":\"70-7\",\"12\":\"70-7\",\"34\":\"70-7\",\"14\":\"70-7\",\"36\":\"70-7\",\"16\":\"70-7\",\"38\":\"70-7\",\"18\":\"70-7\",\"0\":\"70-7\",\"2\":\"70-7\",\"4\":\"70-7\",\"6\":\"70-7\",\"8\":\"70-7\",\"40\":\"70-7\",\"20\":\"70-7\",\"43\":\"70-7\"}";

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.curve_cook_books_user_page_v, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        dm = new DynamicLineChartManager(cookChart, cx);

//        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_deep), null, true);
//        dm.initLineDataSet("烹饪曲线+1", getResources().getColor(R.color.line_chart_deep), null, true);

        List<String> imageUrls = new ArrayList<String>();
//        if (mPayLoadCookBook != null) {
//            for (int i = 0; i < mPayLoadCookBook.curveCookbookPrepareStepDtos.size(); i++) {
//                imageUrls.add(mPayLoadCookBook.curveCookbookPrepareStepDtos.get(i).image);
//            }
//            banner.setImages(imageUrls)
//                    .setImageLoader(new GlideImageLoader())
//                    .start();
//            banner.setDelayTime(3500);
//        }else{
//            return;
//        }

//        String temperature = mPayLoadCookBook.curveCookbookDto.temperatureCurveParams;
        temperature = temperature.substring(1, temperature.indexOf("}"));
        List<LineChartDataBean> dataBeanList = ChartDataReviseUtil.steamOvenCurveDataToLine(temperature);

        entryList = new ArrayList<>(); //数据集合
        drawList = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            entryList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
//            if (dataBeanList.get(i).isBigPoint) {
//                drawList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
//            }
        }
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
//        DynamicLineChartManagerTest DTest=new DynamicLineChartManagerTest(cookChart,"烹饪曲线测试",getResources().getColor(R.color.line_chart_easy));
//        DynamicLineChartManagerTest DTest=new DynamicLineChartManagerTest(cookChart,"烹饪曲线测试",getResources().getColor(R.color.line_chart_easy),cx,entryList, true);
//        DTest.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
        //死循环添加数据
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            DTest.addEntry((int) (Math.random() * 100));
//                            dm.addEntry((int) (Math.random() * 100));
//                        }
//                    });
//                }
//            }
//        }).start();

//        dm.setChartZ((int)entryList.get(entryList.size()-1).getX()/2);
        cookChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureMove(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Transformer transformer = cookChart.getRendererXAxis().getTransformer();
                double x = transformer.getValuesByTouchPoint(cookChart.getCenter().getX(), cookChart.getCenter().getY()).x;
                double y = transformer.getValuesByTouchPoint(cookChart.getCenter().getX(), cookChart.getCenter().getY()).y;
                String timeStr = "";
                if (x <= 0) {
                    timeStr = "0S";
                } else if (x > 0 && x < 60) {
                    timeStr = (int) x + "S";
                } else {
                    timeStr = (int) x / 60 + "min" + (int) x % 60 + "S";
                }
                tvVoiceLong.setText(timeStr);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }

    //按钮点击添加数据
    public void addEntry() {

        dm.initLineDataSet("烹饪曲线+1", getResources().getColor(R.color.line_chart_deep), null, true);

        final int[] i = {0};
        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i[0] < drawList.size() - 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("run:", "run: " + i[0]);
                            if (!isSuspend) {
//                                lineDataSet.addEntry(entryList.get(i[0]));
//                                cookChart.invalidate();
                                dm.addEntry(drawList.get(i[0]));
                                i[0]++;
                            }

                        }
                    });
                }
            }
        }).start();

    }

    @OnClick({R.id.iv_back, R.id.ll_voice_long, R.id.btn_suspend, R.id.btn_finish, R.id.img_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.ll_voice_long:
//                addEntry();
                break;
            case R.id.btn_suspend:
                if (isSuspend) {
                    isSuspend = false;
                } else {
                    isSuspend = true;
                }
                break;
            case R.id.btn_finish:
//                dm.setLowLimitLine(20, "室温");
//                dm.setHightLimitLine(80, "高温", Color.YELLOW);
//                dm.setLeftLimitLine(40, "预热完成");
//                dm.addExtraLine("预热完成+1", getResources().getColor(R.color.line_chart_deep), singleLine);
                Transformer transformer = cookChart.getRendererXAxis().getTransformer();
                double x = transformer.getValuesByTouchPoint(cookChart.getCenter().getX(), cookChart.getCenter().getY()).x;
                double y = transformer.getValuesByTouchPoint(cookChart.getCenter().getX(), cookChart.getCenter().getY()).y;

                break;
            case R.id.img_screen:
                onClickChangePage.onclickChange(0);
                break;
        }
    }

    public interface OnClickChangePage {

        void onclickChange(int VIndex);

    }

    public OnClickChangePage onClickChangePage;

    public void setOnClickChangePageLister(OnClickChangePage onClickChangePage) {
        this.onClickChangePage = onClickChangePage;
    }


    List<LineChartDataBean> dataBeanList = new ArrayList<>();

    public void drawPoint(AbsSteameOvenOne steameOven) {
        LineChartDataBean bean = new LineChartDataBean();
        bean.yValue = steameOven.temp;
        if (dataBeanList.isEmpty()) {
            bean.xValue = 0;
        } else {
            bean.xValue = (dataBeanList.size() * 3);
        }
        if (steameOven.workState == SteamOvenOneWorkStatus.PreHeat &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            bean.stepName = "预热中";
        } else if ((steameOven.workState == SteamOvenOneWorkStatus.Working &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            bean.stepName = "工作中";
        }
        dataBeanList.add(bean);
        pageAddEntry(dataBeanList);
    }

    public void pageAddEntry(List<LineChartDataBean> dataBeanList) {

        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dm.addEntry(new Entry(dataBeanList.get(dataBeanList.size() - 1).xValue, dataBeanList.get(dataBeanList.size() - 1).yValue));

                    }
                });
            }

        }).start();

    }
}
