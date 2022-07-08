package com.robam.roki.ui.activity3.device.pot;

import static com.legent.plat.constant.IPlatRokiFamily.R0001;
import static com.legent.plat.constant.IPlatRokiFamily.R0004;
import static com.robam.roki.utils.TimeUtils.getTime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.RCReponse;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CureFinishEvent;
import com.robam.common.events.PotDotEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.CookingCurveMarkStepList;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.request.api.CurveListApi;
import com.robam.roki.request.bean.RecipeCurveSuccessBean;
import com.robam.roki.request.param.RequestsParam;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.page.curve.RecipeSuccessActivity;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PotCookingCurveActivity extends DeviceBaseFuntionActivity implements OnRequestListener {

    //火力
    TextView tv_cooking_power;
    //温度
    TextView tv_cooking_temp;
    //烹饪时间
    TextView tv_cook_time,tv_cooking_time_tip;
    //横竖屏切换
    ImageView img_switch_screen;
    //标记步骤
    Button btn_sign_step;
    //持续快炒
    Button bt_continue,bt_ten_second_fry;
    //结束工作
    Button btn_complete;
    //结束工作
    LineChart cookChart;


    private DynamicLineChartManager dm;
    private String mPotDt;
    private String temperatureCurveParams;
    ArrayList<Entry> entryList = new ArrayList<>(); //数据集合
    private long curveId;
    private boolean isLoadCurveData = false;
    private boolean isGoEditCurvePage = true;
    Stove mStove;
    private Pot pot = null;
    int changeCont = 0;

    ArrayList<Entry> appointList = new ArrayList<>();
    ArrayList<RecipeCurveSuccessBean.StepList> operationList = new ArrayList<>();
    private CurveListApi curveListApi;

    String strTemp, strGear, strTime;

    private final String startStr = "烹饪开始";
    private final String endStr = "烹饪结束";
    private final String pointStr = "标记打点";

    //本地计时器
    private Timer mTimer;
    private TimerTask mTimerTask;

    /**
     * 开始计时
     */
    private void startCountdown() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                drawPoint((short)pot.tempUp);
                tv_cooking_power.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_cooking_power.setText(mStove.rightHead.level+"档");
                    }
                });
                tv_cooking_temp.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_cooking_temp.setText(pot.tempUp+"");
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 2 * 1000, 2 * 1000);
    }

    /**
     * 结束计时
     */
    private void stopCountdown() {
        //关闭定时任务
        if (mTimer != null) mTimer.cancel();
    }
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected) {
            stopCountdown();
        }
    }
    //无人锅打点
    @Subscribe
    public void onEvent(PotDotEvent event) {
        addAppoint(pointStr,true);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pot_cooking_curve;
    }

    @Override
    protected void initView() {

        tv_cooking_power =  findViewById(R.id.tv_cooking_power);
        tv_cooking_temp =  findViewById(R.id.tv_cooking_temp);
        tv_cook_time =  findViewById(R.id.tv_cook_time);
        tv_cooking_time_tip =  findViewById(R.id.tv_cooking_time_tip);
        img_switch_screen =  findViewById(R.id.img_switch_screen);
        btn_sign_step =  findViewById(R.id.btn_sign_step);
        bt_continue =  findViewById(R.id.bt_continue);
        bt_ten_second_fry =  findViewById(R.id.bt_ten_second_fry);
        btn_complete =  findViewById(R.id.btn_complete);
        cookChart =  findViewById(R.id.cook_chart);

        setOnClickListener(R.id.img_switch_screen, R.id.btn_sign_step,R.id.bt_continue, R.id.bt_ten_second_fry,R.id.btn_complete);

    }

    @Override
    protected void dealData() {
//        setTitle(mDevice.getDt());
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        curveListApi = new CurveListApi(this);
        changeCont = bundle.getInt("changeCont", 0);
        curveId = bundle.getLong(PageArgumentKey.curveId, 0);
        String stoveGuid = bundle.getString(PageArgumentKey.stove);
        mStove = Plat.deviceService.lookupChild(stoveGuid);
        pot = Plat.deviceService.lookupChild(mGuid);

        if(pot!=null){
            mPotDt = pot.getDt();
        }
        if (changeCont > 0) {
            strTemp = bundle.getString("strTemp");
            strGear = bundle.getString("strGear");
            strTime = bundle.getString("strTime");
            entryList = (ArrayList<Entry>) bundle.getSerializable("entryList");
            operationList = (ArrayList<RecipeCurveSuccessBean.StepList>) bundle.getSerializable("operationList");
            appointList = (ArrayList<Entry>) bundle.getSerializable("appointList");
            isLoadCurveData = bundle.getBoolean("isLoadCurveData", false);
        } else {
            query();
        }
        dm = new DynamicLineChartManager(cookChart, getContext());
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
        dm.setChartAttribute(false, false);
        cookChart.notifyDataSetChanged();

        //上一次的高亮线
        Highlight[] highlightsOld = new Highlight[1];
        //开启值转高亮线
        cookChart.valuesToHighlight();
        cookChart.post(new Runnable() {
            @Override
            public void run() {
                //折线图点的标记
                LineChartMarkView mv = new LineChartMarkView(getContext(), cookChart.getXAxis().getValueFormatter());
                mv.setChartView(cookChart);
                cookChart.setMarker(mv);
                //通过触摸生成高亮线
//                Highlight h = mLineChartStudy.getHighlightByTouchPoint(mLineChartStudy.getCenter().x,mLineChartStudy.getCenter().y);
//                Highlight h = cookChart.getHighlightByTouchPoint(cookChart.getRight()-100, cookChart.getTop()+100);
//                cookChart.highlightValue(h, true);
            }
        });
        //图标刷新
        cookChart.invalidate();
        cookChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                highlightsOld[0] = h;
            }

            @Override
            public void onNothingSelected() {
                //高亮线非选中状态
                cookChart.highlightValues(highlightsOld);
            }
        });


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.img_switch_screen:

                break;
            case R.id.btn_sign_step:
                addAppoint(pointStr,true);
                break;
            case R.id.bt_continue:

                break;
            case R.id.bt_ten_second_fry:

                break;
            case R.id.btn_complete:
                potStopRecordCurve();
                break;

        }
    }

    /**
     * 停止记录
     */
    private void potStopRecordCurve() {
        if (!pot.isConnected()) {
            toast(R.string.device_new_connected);
            return;
        }
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 2;
        interaction.length = 2;
        interaction.value = new int[]{1,0};
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                cookingCurveUpdateCurveState(curveId + "", "3");
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void query() {
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        isLoadCurveData = true;
                        if (rcReponse.payload != null) {
                            temperatureCurveParams = rcReponse.payload.temperatureCurveParams;
                            drawCurve(rcReponse.payload.stepList);
                        }else {
                            startCountdown();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        isLoadCurveData = true;
                    }
                });
    }

    private void drawCurve(List<CookingCurveMarkStepList> stepList) {
        List<LineChartDataBean> dataBeanList = new ArrayList<>();
        if (temperatureCurveParams != null && !temperatureCurveParams.isEmpty()) {
            String temperature = temperatureCurveParams;
            temperature = temperature.substring(1, temperature.indexOf("}"));
            dataBeanList = ChartDataReviseUtil.steamOvenCurveDataToLine(temperature);
        }
        for (int i = 0; i < dataBeanList.size(); i++) {
            entryList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
        }

        if(entryList.size()>0){
            tv_cook_time.setText(getTime((int)entryList.get(entryList.size()-1).getX()));
        }
        if (stepList != null && stepList.size() > 0) {
            appointList.clear();
            operationList.clear();
            //第一点
            appointList.add(0, new Entry(0, 2));
            RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
            bean.setMarkTemp((int) entryList.get(0).getY() + "");
            bean.setMarkTime((int) entryList.get(0).getX() + "");
            bean.setMarkName(startStr);
            operationList.add(0, bean);

            for (int i = 0; i < stepList.size(); i++) {
                appointList.add(new Entry(Integer.parseInt(stepList.get(i).markTime), 1));
                bean = new RecipeCurveSuccessBean.StepList();
                bean.setMarkTemp(stepList.get(i).markTemp);
                bean.setMarkTime(stepList.get(i).markTime);
                bean.setMarkName(stepList.get(i).markName);
                operationList.add(bean);
            }

            //最后一点
            appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 1));
            bean = new RecipeCurveSuccessBean.StepList();
            bean.setMarkTemp(entryList.get(entryList.size() - 1).getY() + "");
            bean.setMarkTime(entryList.get(entryList.size() - 1).getX() + "");
            bean.setMarkName(endStr);
            operationList.add(bean);
        }
        dm = new DynamicLineChartManager(cookChart, getContext());
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
        dm.setChartAttribute(false, false);
        Highlight h = cookChart.getHighlightByTouchPoint(cookChart.getRight(), cookChart.getTop());
        cookChart.highlightValue(h, true);
        cookChart.notifyDataSetChanged();
        cookChart.invalidate();
        dm.lineDataAppointSet(appointList);

        //开始计时
        startCountdown();
    }

    public void drawPoint(short leftTemp) {
        LineChartDataBean bean = new LineChartDataBean();
        bean.yValue = leftTemp;

        if (entryList.isEmpty()) {
            bean.xValue = 0;
        } else {
            bean.xValue = (entryList.get(entryList.size() - 1).getX() + 2);
        }

        entryList.add(new Entry(bean.xValue, bean.yValue));
        pageAddEntry(entryList);
    }

    public void pageAddEntry(List<Entry> entryList) {
        if (entryList.size() > 2) {
            if (operationList.size() == 0) {
                addAppoint(startStr, false);
            } else if (operationList.size() == 1) {
                addAppoint(endStr, false);
            } else {
                for (int i = 0; i < operationList.size(); i++) {
                    if (operationList.get(i).getMarkName().equals(endStr)) {
                        operationList.get(i).setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
                        operationList.get(i).setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
                        appointList.get(i).setX(entryList.get(entryList.size() - 1).getX());
                        break;
                    }
                }
            }
            Collections.sort(appointList, new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    if (o1.getX() > o2.getX()) {
                        return 1;
                    }
                    if (o1.getX() < o2.getX()) {
                        return -1;
                    }
                    return 0;
                }
            });
            Collections.sort(operationList, new Comparator<RecipeCurveSuccessBean.StepList>() {
                @Override
                public int compare(RecipeCurveSuccessBean.StepList o1, RecipeCurveSuccessBean.StepList o2) {
                    if (Integer.parseInt(o1.getMarkTime()) > Integer.parseInt(o2.getMarkTime())) {
                        return 1;
                    }
                    if (Integer.parseInt(o1.getMarkTime()) < Integer.parseInt(o2.getMarkTime())) {
                        return -1;
                    }
                    return 0;
                }
            });
            dm.lineDataAppointSet(appointList);
        }
        //添加数据
        dm.addEntryWithMV(entryList.get(entryList.size() - 1));
        Highlight h = cookChart.getHighlightByTouchPoint(cookChart.getRight(), cookChart.getTop());
        cookChart.highlightValue(h, true);
    }
    //添加步骤点
    private void addAppoint(String appointName, boolean iiCheck) {
        RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
        if (appointList.size() == 0) {
            appointList.add(new Entry(0, 2));
            bean.setMarkTemp((int) (entryList.get(0).getY()) + "");
            bean.setMarkTime((int) (entryList.get(0).getX()) + "");
        } else {
            appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 1));
            bean.setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
            bean.setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
        }
        dm.lineDataAppointSet(appointList);
        if (TextUtils.isEmpty(appointName)) {
            appointName = pointStr;
        }
        bean.setMarkName(appointName);
        operationList.add(bean);
        if (iiCheck) {
            cookingCurveMarkStep();
        }

    }
    private void cookingCurveMarkStep() {
        List<RequestsParam.CookingCurveMarkStepList> stepDtoList = new ArrayList<>();
        if (operationList == null || operationList.size() <= 2) {
            return;
        }
        int i = operationList.size() - 1;
        RequestsParam.CookingCurveMarkStepList ben = new RequestsParam.CookingCurveMarkStepList(curveId + "", operationList.get(i).getMarkName(), operationList.get(i).getMarkTemp(), operationList.get(i).getMarkTime());
        stepDtoList.add(ben);
        curveListApi.cookingCurveMarkStep(curveListApi.getCURVE_MARK_STEP(), curveId + "", stepDtoList);
    }

    //更新曲线状态
    private void cookingCurveUpdateCurveState(String id, String state) {
        stopCountdown();
        isGoEditCurvePage = false;
        EventUtils.postEvent(new CureFinishEvent());
        RokiRestHelper.cookingCurveUpdateCurveState(id, state,
                new Callback<RCReponse>() {

                    @Override
                    public void onSuccess(RCReponse rcReponse) {
                        goEditCurvePage();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }
    //去编辑页
    private void goEditCurvePage(){
        Collections.sort(operationList, new Comparator<RecipeCurveSuccessBean.StepList>() {
            @Override
            public int compare(RecipeCurveSuccessBean.StepList o1, RecipeCurveSuccessBean.StepList o2) {
                if (Integer.parseInt(o1.getMarkTime()) > Integer.parseInt(o2.getMarkTime())) {
                    return 1;
                }
                if (Integer.parseInt(o1.getMarkTime()) < Integer.parseInt(o2.getMarkTime())) {
                    return -1;
                }
                return 0;
            }
        });
        UIService.getInstance().popBack();
        RecipeSuccessActivity.show(getActivity(), RecipeSuccessActivity.Companion.getCURVE(), curveId, operationList, mGuid, entryList, appointList);
    }
    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {

    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {

    }
}