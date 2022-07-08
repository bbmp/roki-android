package com.robam.roki.ui.page.device.stove;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;
import static com.legent.plat.constant.IPlatRokiFamily.R0001;
import static com.legent.plat.constant.IPlatRokiFamily.R0004;
import static com.robam.roki.ui.widget.view.PlayerView.conversionTime;
import static com.robam.roki.utils.TimeUtils.getTime;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clj.fastble.BleManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.RCReponse;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CureFinishEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.request.api.CurveListApi;
import com.robam.roki.request.bean.RecipeCurveSuccessBean;
import com.robam.roki.request.param.RequestsParam;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.type.DialogType_FinishWork;
import com.robam.roki.ui.page.AbsDUIPage;
import com.robam.roki.ui.page.curve.RecipeSuccessActivity;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 灶具曲线入口页面
 */

public class DeviceStoveCurvePage extends AbsDUIPage implements OnRequestListener {
    private final String startStr = "烹饪开始";
    private final String endStr = "烹饪结束";
    private final String preStr = "预热完成";
    private final String pointStr = "标记打点";

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_cook_time)
    TextView tv_cook_time;
    @InjectView(R.id.tv_gear_name)
    TextView tv_gear_name;
    @InjectView(R.id.tv_cook_temp)
    TextView tv_cook_temp;
    @InjectView(R.id.cook_chart)
    LineChart cookChart;
    @InjectView(R.id.btn_finish)
    Button btn_finish;
    @InjectView(R.id.btn_screen_switch)
    Button btn_screen_switch;

    private DynamicLineChartManager dm;
    private String mGuid = null,mPotDt;
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
                if(mPotDt.equals(R0001)&&mStove!=null){
                    drawPoint(mStove.rightTemp);
                }else if(mPotDt.equals(R0004)){
                    drawPoint((short)pot.tempUp);
                }
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

    @Override
    protected int getLayoutId() {
        Log.i("TAG", "getLayoutId");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            return R.layout.pot_curve_h;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            return R.layout.pot_curve;
        }
        return R.layout.pot_curve;
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.i("TAG", "-------- initView");

    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i("TAG", "-------- onConfigurationChanged");
    }

    String strTemp, strGear, strTime;

    @Override
    public void initData() {
        LogUtils.i("TAG", "-------- initData");
        mGuid = getActivity().getIntent().getStringExtra(PageArgumentKey.Guid);
        curveId = getActivity().getIntent().getLongExtra(PageArgumentKey.curveId, 0);
        mTvDeviceModelName.setText("烹饪");
        curveListApi = new CurveListApi(this);
        changeCont = getActivity().getIntent().getIntExtra("changeCont", 0);
        String potGuid = getActivity().getIntent().getStringExtra(PageArgumentKey.pot);
        if(potGuid!=null){
            pot = Plat.deviceService.lookupChild(potGuid);
        }
        if(pot!=null){
            mPotDt = pot.getDt();
        }

        if (changeCont > 0) {
            strTemp = getActivity().getIntent().getStringExtra("strTemp");
            strGear = getActivity().getIntent().getStringExtra("strGear");
            strTime = getActivity().getIntent().getStringExtra("strTime");
            entryList = (ArrayList<Entry>) getActivity().getIntent().getSerializableExtra("entryList");
            operationList = (ArrayList<RecipeCurveSuccessBean.StepList>) getActivity().getIntent().getSerializableExtra("operationList");
            appointList = (ArrayList<Entry>) getActivity().getIntent().getSerializableExtra("appointList");
            isLoadCurveData = getActivity().getIntent().getBooleanExtra("isLoadCurveData", false);
        } else {
            query();
        }
        dm = new DynamicLineChartManager(cookChart, cx);
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
                LineChartMarkView mv = new LineChartMarkView(cx, cookChart.getXAxis().getValueFormatter());
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
        //横竖屏切换
        btn_screen_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCont++;
                getActivity().getIntent().putExtra("strTemp", tv_cook_temp.getText().toString());
                getActivity().getIntent().putExtra("strGear", tv_gear_name.getText().toString());
                getActivity().getIntent().putExtra("strTime", tv_cook_time.getText().toString());
                getActivity().getIntent().putExtra("changeCont", changeCont);
                getActivity().getIntent().putExtra("isLoadCurveData", isLoadCurveData);
                Bundle bd = new Bundle();
                bd.putSerializable("entryList", entryList);
                bd.putSerializable("operationList", operationList);
                bd.putSerializable("appointList", (Serializable) appointList);
                getActivity().getIntent().putExtras(bd);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
    }

    private void query() {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        isLoadCurveData = true;
                        if (rcReponse.payload != null) {
                            temperatureCurveParams = rcReponse.payload.temperatureCurveParams;
                            drawCurve(rcReponse.payload.stepList);
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        isLoadCurveData = true;
                    }
                });
    }

    private void drawCurve(List<com.robam.common.pojos.CookingCurveMarkStepList> stepList) {
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
        dm = new DynamicLineChartManager(cookChart, cx);
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


    //取温规则 一代锅+带温度的灶： 取灶的温度 R0001     二代锅+灶(普通灶，智能灶)：取锅的温度 R0004
    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20180518", event.pojo.getID());
        mStove = event.pojo;
        if (isLoadCurveData && event.pojo.rightHead.level > 0) {
            if(mPotDt.equals(R0001)){
//                drawPoint(event.pojo.rightTemp);
            }
            tv_cook_time.setText(getTime((int)entryList.get(entryList.size()-1).getX()));
            tv_gear_name.setText("F" + event.pojo.rightHead.level);
            tv_cook_temp.setText(event.pojo.rightTemp + "℃");

        }
        //关火
        if (mStove.rightHead.status == 0 &&isGoEditCurvePage) {
            cookingCurveUpdateCurveState(curveId + "", "3");
        }
    }
    //无人锅
    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo==null)
            return;
        if(mPotDt.equals(R0004)){
//            drawPoint((short) event.pojo.tempUp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        changeCont = 0;
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        stopCountdown();
//        UIService.getInstance().popBack();
    }

    @OnClick(R.id.btn_finish)
    public void onFinishClicked() {
//        ToastUtils.show("烹饪结束", Toast.LENGTH_LONG);
//        Bundle bd = new Bundle();
//        bd.putLong(PageArgumentKey.curveId, curveId);
//        UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HStop();
        }else{
            buttonLeft();
        }
    }

    @OnClick(R.id.ll_curve_mark)
    public void onCurveMarkClicked() {

        if (appointList.size() < 2) {
            return;
        }
        ToastUtils.show("已成功标记该点", Toast.LENGTH_SHORT);
        addAppoint("", true);
    }
    private void HStop() {
        if (mStove == null || mStove.rightHead == null) {
            return;
        }
        Stove.StoveHead head = mStove.rightHead;
        LogUtils.i("20180702", " leftHead:" + mStove.rightHead.level);

        IRokiDialog horizontalDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_H_FINISH_WORK);
        horizontalDialog.setTitleText("确定结束烹饪吗？");
        horizontalDialog.setContentText("确定");
        horizontalDialog.show();
        horizontalDialog.setCancelBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalDialog.dismiss();
            }
        });

        horizontalDialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalDialog.dismiss();
                if (mStove.rightHead.level == 0) {
                    ToastUtils.showShort(R.string.stove_not_open_fire);
                } else {
                    final Stove.StoveHead finalHead = head;
                    Preconditions.checkNotNull(finalHead);
                    setStatus(finalHead);
                }
            }
        });
    }
    //快速关火左
    private void buttonLeft() {
        if (mStove == null || mStove.rightHead == null) {
            return;
        }
        Stove.StoveHead head = mStove.rightHead;
        LogUtils.i("20180702", " leftHead:" + mStove.rightHead.level);


        if (mStove.rightHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final DialogType_FinishWork dialogType_finishWork = DialogType_FinishWork.createDialogType_FinishWork(cx);
            final Stove.StoveHead finalHead = head;
            dialogType_finishWork.setOkBtn(0, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogType_finishWork.dismiss();
                    Preconditions.checkNotNull(finalHead);
                    setStatus(finalHead);
                }
            });
            dialogType_finishWork.show();
        }

    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("CookCurveActivityBack".equals(event.getPageName())) {

        }
    }

    private void setStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        if (!checkIsPowerOn(head)) return;
        short status = (head.status == StoveStatus.Off) ? StoveStatus.StandyBy
                : StoveStatus.Off;
        mStove.setStoveStatus(false, head.ihId, status, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("结束烹饪", Toast.LENGTH_SHORT);
                cookingCurveUpdateCurveState(curveId + "", "3");
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("结束烹饪", Toast.LENGTH_SHORT);
                cookingCurveUpdateCurveState(curveId + "", "3");
            }
        });
    }

    boolean checkIsPowerOn(Stove.StoveHead head) {
        if (head.status != StoveStatus.Off)
            return true;
        else {
            if (!mStove.isLock) {
                ToastUtils.showLong(R.string.device_stove_isLock);
            }
            return false;
        }
    }

    boolean checkConnection() {
        if (!mStove.isConnected()) {
            ToastUtils.showLong(R.string.device_connected);
            return false;
        } else {
            return true;
        }
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
        RecipeSuccessActivity.show(cx, RecipeSuccessActivity.Companion.getCURVE(), curveId, operationList, mGuid, entryList, appointList);
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
