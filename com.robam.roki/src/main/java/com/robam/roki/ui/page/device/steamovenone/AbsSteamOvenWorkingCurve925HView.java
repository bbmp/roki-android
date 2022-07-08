package com.robam.roki.ui.page.device.steamovenone;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsSteamOvenWorkingCurve925HView extends FrameLayout {


    Context cx;
    AbsSteameOvenOne steameOven;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.tv_temperature)
    TextView tvTemperature;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.btn_screen_switch)
    Button btnScreenSwitch;
    @InjectView(R.id.rl_top)
    LinearLayout rlTop;
    @InjectView(R.id.tv_work_surplus_time_con)
    TextView tvWorkSurplusTimeCon;
    @InjectView(R.id.tv_work_surplus_time)
    TextView tvWorkSurplusTime;
    @InjectView(R.id.ll_btn)
    LinearLayout llBtn;
    @InjectView(R.id.tv_status_name)
    TextView tvStatusName;
    @InjectView(R.id.img_curve_mark)
    ImageView imgCurveMark;
    @InjectView(R.id.tv_curve_mark)
    TextView tvCurveMark;
    @InjectView(R.id.ll_curve_mark)
    LinearLayout llCurveMark;
    @InjectView(R.id.btn_suspend)
    Button btnSuspend;
    @InjectView(R.id.btn_finish)
    Button btnFinish;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;
    @InjectView(R.id.cook_chart)
    LineChart cookChart;
    private DynamicLineChartManager dm;
    private PayLoadCookBook mPayLoadCookBook;
    List<Entry> entryList, drawList;
    private boolean isSuspend = false;

    public AbsSteamOvenWorkingCurve925HView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsSteamOvenWorkingCurve925HView(Context context, PayLoadCookBook mPayLoadCookBook) {
        super(context);
        this.cx = context;
        this.mPayLoadCookBook = mPayLoadCookBook;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_steamoven_925_working_h_page, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        dm = new DynamicLineChartManager(cookChart, cx);
//        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_deep), null, true);
//        dm.initLineDataSet("烹饪曲线+1", getResources().getColor(R.color.line_chart_deep), null, true);
        if(mPayLoadCookBook==null){
            return;
        }
        String temperature = mPayLoadCookBook.curveCookbookDto.temperatureCurveParams;
        temperature = temperature.substring(1, temperature.indexOf("}"));
        List<LineChartDataBean> dataBeanList = ChartDataReviseUtil.curveDataToLine(temperature);

        entryList = new ArrayList<>(); //数据集合
        drawList = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            entryList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
            if (dataBeanList.get(i).isBigPoint) {
                drawList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
            }
        }

    }

    @OnClick({R.id.iv_back,R.id.btn_screen_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
//            case R.id.ll_voice_long:
//                addEntry();
//                break;
//            case R.id.btn_suspend:
//                if (isSuspend) {
//                    isSuspend = false;
//                } else {
//                    isSuspend = true;
//                }
//                break;
//            case R.id.btn_finish:
//                dm.setLowLimitLine(20, "室温");
//                dm.setHightLimitLine(80, "高温", Color.YELLOW);
//                dm.setLeftLimitLine(40, "预热完成");
////                dm.addExtraLine("预热完成+1", getResources().getColor(R.color.line_chart_deep), singleLine);
//                break;
            case R.id.btn_screen_switch:
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

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {

        //预热   工作
        if ((event.pojo.workState == SteamOvenOneWorkStatus.PreHeat &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) || (event.pojo.workState == SteamOvenOneWorkStatus.Working &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            steameOven = (AbsSteameOvenOne) event.pojo;
//            new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue)
            drawPoint(steameOven);
        }
    }

    List<LineChartDataBean> dataBeanList = new ArrayList<>();

    public void drawPoint(AbsSteameOvenOne steameOven) {
        LineChartDataBean bean = new LineChartDataBean();
        bean.yValue = steameOven.temp;
        if (dataBeanList.isEmpty()) {
            bean.xValue = 0;
        } else {
            bean.xValue = (dataBeanList.size()-1 +3);
        }
        if (steameOven.workState == SteamOvenOneWorkStatus.PreHeat &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            bean.stepName = "预热中";
        } else if ((steameOven.workState == SteamOvenOneWorkStatus.Working &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            bean.stepName = "工作中";
        }
        dataBeanList.add(bean);
//        pageAddEntry(dataBeanList);
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
