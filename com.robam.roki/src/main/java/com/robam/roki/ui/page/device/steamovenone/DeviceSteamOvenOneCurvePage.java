package com.robam.roki.ui.page.device.steamovenone;

import static com.robam.roki.ui.page.device.steamovenone.AbsDeviceSteamOvenOne620Page.isForeground;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.RCReponse;

import com.legent.plat.services.DevicePollingReceiver;
import com.legent.plat.services.DeviceService;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.NewSteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.io.cloud.Requests;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew;
import com.robam.common.util.StatusBarUtils;
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
import com.robam.roki.ui.form.SteamFinish;
import com.robam.roki.ui.page.AbsDUIPage;
import com.robam.roki.ui.page.curve.RecipeSuccessActivity;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.ui.widget.dialog.CookedFinishDialog;
import com.robam.roki.ui.widget.dialog.ICookedFinish;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 一体机曲线入口页面   SteamOvenCookCurveActivity
 */

public class DeviceSteamOvenOneCurvePage extends AbsDUIPage implements OnRequestListener {
    private final String startStr = "烹饪开始";
    private final String endStr = "烹饪结束";
    private final String preStr = "预热完成";
    private final String pointStr = "标记打点";

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.btn_suspend)
    Button btnSuspend;
    @InjectView(R.id.btn_finish)
    Button btnFinish;
    @InjectView(R.id.tv_model_content)
    TextView tvModelContent;
    @InjectView(R.id.tv_temp_content)
    TextView tvTempContent;

    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.tv_time_content)
    TextView tvTimeContent;
    @InjectView(R.id.tv_dot_one)
    TextView tvDotOne;
    @InjectView(R.id.tv_dot_two)
    TextView tvDotTwo;
    @InjectView(R.id.tv_dot_three)
    TextView tvDotThree;
    @InjectView(R.id.ll_more_step)
    LinearLayout llMoreStep;
    @InjectView(R.id.tv_work_surplus_time_con)
    TextView tvWorkSurplusTimeCon;
    @InjectView(R.id.tv_work_surplus_time)
    TextView tvWorkSurplusTime;
    @InjectView(R.id.btn_screen_switch)
    Button btnScreenSwitch;
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
    @InjectView(R.id.cook_chart)
    LineChart cookChart;

    @InjectView(R.id.tv_steam)
    TextView tv_steam;
    @InjectView(R.id.tv_steam_content)
    TextView tv_steam_content;

    private DynamicLineChartManager dm;
    private String mGuid = null;
    private String temperatureCurveParams;
    ArrayList<Entry> entryList = new ArrayList<>(); //数据集合
    private  long curveId;
    private boolean isLoadCurveData = false;
    private boolean isIntoWorking = false;
    AbsSteameOvenOneNew mAbsSteameOvenOneNew;
    private String searchRecipeKeyWord;
    int changeCont = 0;
    //说话点位
    ArrayList<Entry> appointList = new ArrayList<>();
    ArrayList<RecipeCurveSuccessBean.StepList> operationList = new ArrayList<>();
    //预热标志
    private int preheatTip = 0;
    private CurveListApi curveListApi;

    //本地计时器
    protected ScheduledFuture<?> mFuture;//计时器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            curveId = savedInstanceState.getInt(curveid,0);
        }
    }

    /**
     * 开始计时
     */
    protected void startCountdown() {
        if (mFuture != null)
            return;
        mFuture = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LogUtils.i("20220608",mAbsSteameOvenOneNew.curTemp+"");
                drawPoint(mAbsSteameOvenOneNew.curTemp);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);
    }

    /**
     * 结束计时
     */
    protected void stopCountdown() {
        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    @Override
    protected int getLayoutId() {
        Log.i("TAG", "getLayoutId");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            return R.layout.device_steamoven_curve_h_page;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            return R.layout.device_steamoven_curve_v_page;
        }
        return R.layout.device_steamoven_curve_v_page;
    }

    @Override
    protected void initView() {
        super.initView();
        EventUtils.regist(this);
        initDialog();
        LogUtils.i("TAG", "-------- initView");
        AlarmDataUtils.init(getActivity());
        StatusBarUtils.setTextDark(getContext(), true);

    }

    private void initDialog() {

        if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
            iRokiDialogAlarmType_01.dismiss();
        }
        if (iRokiDialogAlarmType_02 != null && iRokiDialogAlarmType_02.isShow()) {
            iRokiDialogAlarmType_02.dismiss();
        }
        iRokiDialogAlarmType_01 = null;
        iRokiDialogAlarmType_02 = null;
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_01);
        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_02);
    }








    String strTemp, strGear, strTime;

    private void switchScream() {
        changeCont++;
        getActivity().getIntent().putExtra("changeCont", changeCont);
        getActivity().getIntent().putExtra("isLoadCurveData", isLoadCurveData);
        Bundle bd = new Bundle();
        bd.putSerializable("entryList", entryList);
        bd.putSerializable("operationList", operationList);
        bd.putSerializable("appointList", (Serializable) appointList);
        bd.putLong(curveid,curveId);
        getActivity().getIntent().putExtras(bd);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void initData() {
        LogUtils.i("TAG", "-------- initData");
        mGuid = getActivity().getIntent().getStringExtra(PageArgumentKey.Guid);
//        curveId = getActivity().getIntent().getLongExtra(PageArgumentKey.curveId, 0);
        mAbsSteameOvenOneNew = DeviceService.getInstance().queryById(mGuid);
        tvDeviceModelName.setText("烹饪");
        curveListApi = new CurveListApi(this);

        changeCont = getActivity().getIntent().getIntExtra("changeCont", 0);
        if (changeCont > 0) {
            strTemp = getActivity().getIntent().getStringExtra("strTemp");
            strGear = getActivity().getIntent().getStringExtra("strGear");
            strTime = getActivity().getIntent().getStringExtra("strTime");
            entryList = (ArrayList<Entry>) getActivity().getIntent().getSerializableExtra("entryList");
            operationList = (ArrayList<RecipeCurveSuccessBean.StepList>) getActivity().getIntent().getSerializableExtra("operationList");
            appointList = (ArrayList<Entry>) getActivity().getIntent().getSerializableExtra("appointList");
            curveId=getActivity().getIntent().getLongExtra(curveid,0L);
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
        cookChart.post(() -> {

            LineChartMarkView mv = new LineChartMarkView(cx, cookChart.getXAxis().getValueFormatter());
            mv.setChartView(cookChart);
            cookChart.setMarker(mv);
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
        btnScreenSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchScream();
            }
        });

    }


    @OnClick(R.id.btn_suspend)
    void pause() {


        if (SteamOvenHelper.isPause(mAbsSteameOvenOneNew.workState)) {
            //需要水的模式
            if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mAbsSteameOvenOneNew.mode))) {
                if (SteamOvenHelper.isDescale(mAbsSteameOvenOneNew.descaleFlag)) {
                    ToastUtils.showShort("设备需要除垢后才能继续工作，请先除垢");
                    return;
                }
                if (!SteamOvenHelper.isWaterBoxState(mAbsSteameOvenOneNew.waterBoxState)) {
                    ToastUtils.showShort("水箱已弹出，请检查水箱状态");
                    return;
                }
                if (!SteamOvenHelper.isWaterLevelState(mAbsSteameOvenOneNew.waterLevelState)) {
                    ToastUtils.showShort("水箱缺水，请加水");
                    return;
                }
            }

        }


        mAbsSteameOvenOneNew.setSteamWorkStatus(SteamOvenHelper.isPause(mAbsSteameOvenOneNew.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (SteamOvenHelper.isPause(mAbsSteameOvenOneNew.workState)) {
                    startCountdown();
                    btnSuspend.setText("继续烹饪");
                } else {
                    btnSuspend.setText("暂停");
                    stopCountdown();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private String curveid="CURVEID";

    private int queryCont = 0;

    private void query() {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {
                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        isLoadCurveData = true;
                        queryCont++;
                        if (rcReponse.payload != null) {
                            curveId = rcReponse.payload.curveCookbookId;
                            temperatureCurveParams = rcReponse.payload.temperatureCurveParams;
                            drawCurve(rcReponse.payload.stepList);
                        } else {
                            if (queryCont < 3) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        query();
                                    }
                                }, 3000);
                            }else{
                                ToastUtils.show("获取数据失败",Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        isLoadCurveData = true;
                        queryCont++;
                        if (queryCont < 3) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    query();
                                }
                            }, 3000);
                        }else{
                            ToastUtils.show("获取数据失败",Toast.LENGTH_SHORT);
                            finish();
                        }
                    }
                });
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
        if (cookChart==null){
            return;
        }
        dm = new DynamicLineChartManager(cookChart, cx);
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
        dm.setChartAttribute(false, false);
        if (stepList != null && stepList.size() > 0) {
            appointList.clear();
            operationList.clear();
            for (int i = 0; i < stepList.size(); i++) {
                RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
                appointList.add(new Entry(Integer.parseInt(stepList.get(i).markTime), 1));
                bean.setMarkTemp(stepList.get(i).markTemp);
                bean.setMarkTime(stepList.get(i).markTime);
                bean.setMarkName(stepList.get(i).markName);
                operationList.add(bean);
            }
            //第一点
            appointList.add(0, new Entry(0, 2));
            RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
            bean.setMarkTemp((int) entryList.get(0).getY() + "");
            bean.setMarkTime((int) entryList.get(0).getX() + "");
            bean.setMarkName(startStr);
            operationList.add(0, bean);
            //最后一点
            appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 1));
            bean = new RecipeCurveSuccessBean.StepList();
            bean.setMarkTemp(entryList.get(entryList.size() - 1).getY() + "");
            bean.setMarkTime(entryList.get(entryList.size() - 1).getX() + "");
            bean.setMarkName(endStr);
            operationList.add(bean);
        }
        dm.lineDataAppointSet(appointList);
        Highlight h = cookChart.getHighlightByTouchPoint(cookChart.getRight(), cookChart.getTop());
        cookChart.highlightValue(h, true);
        cookChart.notifyDataSetChanged();
        cookChart.invalidate();
        //开始计时
        if (isLoadCurveData &&
                (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat ||
                        mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.Working)) {
            startCountdown();
        }
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
            Collections.sort(operationList, (o1, o2) -> {
                if (Integer.parseInt(o1.getMarkTime()) > Integer.parseInt(o2.getMarkTime())) {
                    return 1;
                }
                if (Integer.parseInt(o1.getMarkTime()) < Integer.parseInt(o2.getMarkTime())) {
                    return -1;
                }
                return 0;
            });
            dm.lineDataAppointSet(appointList);
        }
        //添加数据
        dm.addEntryWithMV(entryList.get(entryList.size() - 1));
        Highlight h = cookChart.getHighlightByTouchPoint(cookChart.getRight(), cookChart.getTop());
        cookChart.highlightValue(h, true);
    }

    //蒸烤一体机报警接收事件
    @Subscribe
    public void onEvent(NewSteamOvenOneAlarmEvent event) {
        boolean isNoShow = false;
        if (event.steameOvenOne instanceof AbsSteameOvenOneNew) {
            AbsSteameOvenOneNew steameOvenOne = (AbsSteameOvenOneNew) event.steameOvenOne;
            short alarms = event.alarmId;

            steamOvenOneAlarmStatus(steameOvenOne, alarms, isNoShow);
        }
    }

    private static final int ONE_ALARM = 1;
    private static final int TWO_ALARM = 2;
    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
    IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警

    public void steamOvenOneAlarmStatus(AbsSteameOvenOne steamOvenOne, short alarmId, boolean isNoShow) {
        String fileJson = PreferenceUtils.getString("alarm", null);
        LogUtils.i("202012071057", "fileJson::" + fileJson);
        String alarmCode = String.valueOf(alarmId);
        String dc = steamOvenOne.getDc();
        String dt = steamOvenOne.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);

            JSONObject steamOvenOneDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamOvenOneDc.get("deviceType");
            JSONObject steamOvenOneDt = (JSONObject) steamOvenOneDc.get(dt);
            JSONObject steamOvenOneCode = (JSONObject) steamOvenOneDt.get(alarmCode);
            Integer alertLevel = (Integer) steamOvenOneCode.get("alertLevel");
            String alertName = (String) steamOvenOneCode.get("alertName");
            String alertDescr = (String) steamOvenOneCode.get("alertDescr");
            if (isNoShow) {
                if (alertDescr.contains("缺水")) {
                    return;
                }
            }
            String alertCode = (String) steamOvenOneCode.get("alertCode");
            LogUtils.i("202012071057", "alertLevel::" + alertLevel);
            LogUtils.i("20180831", "alertLevel:" + alertLevel);
            LogUtils.i("20180831", "alertDescr:" + alertDescr);
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            } else {
                ToastUtils.show(alertDescr, Toast.LENGTH_SHORT);
            }

        } catch (JSONException e) {
            LogUtils.i("20200414000", e.getMessage());
            e.printStackTrace();

        }
    }

    public void makePhoneCallListenr() {
        iRokiDialogAlarmType_01.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn(R.string.can_good, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_01.show();
        LogUtils.i("20170918", "show:" + iRokiDialogAlarmType_01.isShow());
    }


    public void centerOneBtnListener() {
        iRokiDialogAlarmType_02.setOkBtn(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_02.dismiss();
            }
        });
        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_02.show();
    }

    public void setMultShow(short sectionNumber) {


        if (mAbsSteameOvenOneNew.sectionNumber == 2) {
            tvDotThree.setVisibility(View.INVISIBLE);
        }
        if (sectionNumber == 1) {
            tvDotOne.setBackgroundResource(R.drawable.shape_green_dot);
            tvDotTwo.setBackgroundResource(R.drawable.shape_grey_dot);
            tvDotThree.setBackgroundResource(R.drawable.shape_grey_dot);
        } else if (sectionNumber == 2) {
            tvDotOne.setBackgroundResource(R.mipmap.icon_ok);
            tvDotTwo.setBackgroundResource(R.drawable.shape_green_dot);
            tvDotThree.setBackgroundResource(R.drawable.shape_grey_dot);

        } else if (sectionNumber == 3) {
            tvDotOne.setBackgroundResource(R.mipmap.icon_ok);
            tvDotTwo.setBackgroundResource(R.mipmap.icon_ok);;
            tvDotThree.setBackgroundResource(R.drawable.shape_green_dot);;
        }
        if (mAbsSteameOvenOneNew.curSectionNbr == 1) {
            tvDotOne.setBackgroundResource(R.drawable.shape_green_dot);
            tvDotTwo.setBackgroundResource(R.drawable.shape_grey_dot);
            tvDotThree.setBackgroundResource(R.drawable.shape_grey_dot);
        } else if (mAbsSteameOvenOneNew.curSectionNbr == 2) {
            tvDotOne.setBackgroundResource(R.mipmap.icon_ok);
            tvDotTwo.setBackgroundResource(R.drawable.shape_green_dot);
            tvDotThree.setBackgroundResource(R.drawable.shape_grey_dot);
        } else if (mAbsSteameOvenOneNew.curSectionNbr == 3) {
            tvDotOne.setBackgroundResource(R.mipmap.icon_ok);
            tvDotTwo.setBackgroundResource(R.mipmap.icon_ok);;
            tvDotThree.setBackgroundResource(R.drawable.shape_green_dot);;

        }
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        LogUtils.i("20180518", event.pojo.getID());

        if (mAbsSteameOvenOneNew == null || !Objects.equal(mAbsSteameOvenOneNew.getID(), event.pojo.getID())) {
            return;
        }
        mAbsSteameOvenOneNew = (AbsSteameOvenOneNew) event.pojo;
        Log.e("结果onEvent", mAbsSteameOvenOneNew.workState + "---");

        tvModelContent.setText(SteamOvenModeEnum.match(mAbsSteameOvenOneNew.mode).getValue());


        String setTime = (mAbsSteameOvenOneNew.setTimeH * 256 + mAbsSteameOvenOneNew.setTime) / 60 == 0 ? ""
                : (((mAbsSteameOvenOneNew.setTimeH * 256 + mAbsSteameOvenOneNew.setTime) / 60) + "min");

        String sec = (mAbsSteameOvenOneNew.setTimeH * 256 + mAbsSteameOvenOneNew.setTime) % 60 == 0 ? "" :
                ((mAbsSteameOvenOneNew.setTimeH * 256 + mAbsSteameOvenOneNew.setTime) % 60 + "s");
        int outTime = mAbsSteameOvenOneNew.restTimeH * 256 + mAbsSteameOvenOneNew.restTime;

        if (outTime % 60 == 0) {
            tvWorkSurplusTimeCon.setText("剩余时间" + ((outTime / 60)));
        } else {
            tvWorkSurplusTimeCon.setText("剩余时间" + ((outTime / 60) + 1));
        }
        tvTimeContent.setText(setTime + sec);

        if (SteamOvenHelper.isMult(mAbsSteameOvenOneNew.sectionNumber)) {
            llMoreStep.setVisibility(View.VISIBLE);
            setMultShow(mAbsSteameOvenOneNew.curSectionNbr);
        } else {
            llMoreStep.setVisibility(View.INVISIBLE);
        }

        if (SteamOvenHelper.isShowSteam(mAbsSteameOvenOneNew.mode)) {
            tv_steam.setVisibility(View.VISIBLE);
            if (tv_steam_content != null)
                tv_steam_content.setVisibility(View.VISIBLE);
            tv_steam_content.setText(SteamOvenHelper.getSteamContent(mAbsSteameOvenOneNew.steam));
        } else {
            tv_steam.setVisibility(View.GONE);
            if (tv_steam_content != null)
                tv_steam_content.setVisibility(View.GONE);
        }
        if (SteamOvenModeEnum.match(mAbsSteameOvenOneNew.mode)==SteamOvenModeEnum.EXP){
            tv_steam_content.setVisibility(View.VISIBLE);
            tv_steam.setVisibility(View.VISIBLE);
            tv_steam.setText("上温度");
            tv_steam_content.setText(mAbsSteameOvenOneNew.setUpTemp + "℃");
            tvTemp.setText("下温度");
            tvTempContent.setText(mAbsSteameOvenOneNew.setDownTemp+ "℃");
            tvTempContent.setVisibility(View.VISIBLE);
        }else {
            tvTempContent.setText(mAbsSteameOvenOneNew.setUpTemp + "℃");
        }

        //预备 工作
        if (isLoadCurveData &&
                (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat ||
                        mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.Working)) {


            if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.Working) {//进入过工作状态
                isIntoWorking = true;
                if(operationList.size()>=2&&!checkPreheat()){
                    addAppoint(preStr, true);
                }

//                if (preheatTip == 1) {//从预热状态过来
//                    preheatTip++;
//                } else if (preheatTip == 0) {//直接进入工作状态
//
//                }
//                if (preheatTip == 2) {//添加预热完成点
//                    preheatTip++;
//                    addAppoint(preStr, true);
//                }
            }
            if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) {//预热
                preheatTip = 1;
            }
            tvStatusName.setText("记录中");
            btnSuspend.setText("暂停");
            startCountdown();
            //完成
        } else if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.finish) {
            if (!isForeground(cx,"RecipeSuccessActivity")){
                if(!isCookingCurveUpdateCurveState){
                    cookingCurveUpdateCurveState(curveId+"","4");
                }
            }


        } else if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.workPauseSteam

                || mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeatPause) {
            stopCountdown();
            tvStatusName.setText("暂停中");
            btnSuspend.setText("继续烹饪");

        } else if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.Off ||
                mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.Open) {
            if (isIntoWorking&&!isForeground(cx,"RecipeSuccessActivity")) {
                if(!isCookingCurveUpdateCurveState){
                    cookingCurveUpdateCurveState(curveId+"","3");
                }
            }
            EventUtils.postEvent(new SteamFinish());
            stopCountdown();
        }
    }
    //是否有预热完成点
    private boolean checkPreheat(){
        if (operationList.size()<=2)
            return false;
        for(int i = 0;i<operationList.size();i++){
            if(operationList.get(i).getMarkName().equals(preStr))
            return true;
        }
        return false;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        changeCont = 0;
    }

    void finish() {
        EventUtils.postEvent(new PageBackEvent("SteamOvenCookCurveActivityBack"));
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            switchScream();
        } else {
            EventUtils.postEvent(new PageBackEvent("SteamOvenCookCurveActivityBack1"));
            EventUtils.postEvent(new PageBackEvent("SteamOvenCookCurveActivityBack"));
        }
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.btn_finish)
    public void onFinishClicked() {
//        ToastUtils.show("烹饪结束", Toast.LENGTH_LONG);
//        Bundle bd = new Bundle();
//        bd.putLong(PageArgumentKey.curveId, curveId);
//        UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
        buttonLeft(btnFinish);
    }

    @OnClick(R.id.ll_curve_mark)
    public void onCurveMarkClicked() {
//        if (appointList.size() < 2||mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) {
//            return;
//        }
        if (appointList.size() < 2) {
            return;
        }
        ToastUtils.show("已成功标记该点", Toast.LENGTH_SHORT);
        addAppoint("", true);
    }

    //快速关火左
    private void buttonLeft(View v) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HStop();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setStop();
        }

    }

    private void HStop() {
        IRokiDialog horizontalDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_H_FINISH_WORK);
        horizontalDialog.setTitleText("确定结束烹饪吗？");
        horizontalDialog.setContentText("确定");

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
                finishWork("3");
            }
        });

        horizontalDialog.show();
    }

    private void setStop() {


        DialogType_FinishWork mDialogType_FinishWork = DialogType_FinishWork.createDialogType_FinishWork(getContext());
        mDialogType_FinishWork.bindAllListeners();
        mDialogType_FinishWork.setOnOkClickListener(v -> {
            switch (v.getId()) {
                case R.id.btn_finish:
                    mDialogType_FinishWork.dismiss();
                    finishWork("3");
                    break;
                case R.id.btn_cancel:
                    mDialogType_FinishWork.dismiss();
                    break;
                default:
                    break;
            }
        });
        mDialogType_FinishWork.show();

    }

    private void finishWork(String state) {
        if (mAbsSteameOvenOneNew == null) {
            return;
        }

        mAbsSteameOvenOneNew.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop, (short) 4, new VoidCallback() {
            @Override
            public void onSuccess() {
                Collections.sort(operationList, (o1, o2) -> {
                    if (Double.parseDouble(o1.getMarkTime()) > Double.parseDouble(o2.getMarkTime())) {
                        return 1;
                    }
                    if (Double.parseDouble(o1.getMarkTime()) < Double.parseDouble(o2.getMarkTime())) {
                        return -1;
                    }
                    return 0;
                });
                cookingCurveUpdateCurveState(curveId+"",state);

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
    private boolean isCookingCurveUpdateCurveState = false;
    private void cookingCurveUpdateCurveState(String id, String state) {
        RokiRestHelper.cookingCurveUpdateCurveState(id, state,
                new Callback<RCReponse>() {

                    @Override
                    public void onSuccess(RCReponse rcReponse) {
                        boolean isPreHeat = false;
                        if (mAbsSteameOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat) {
                            isPreHeat = true;
                        }
                        boolean finalIsPreHeat = isPreHeat;
                        if (!finalIsPreHeat) {
                            RecipeSuccessActivity.show(cx, RecipeSuccessActivity.Companion.getCURVE(), curveId, operationList,
                                    mAbsSteameOvenOneNew.getGuid().getGuid(), entryList, appointList);
                        }
//                        ToastUtils.show("烹饪完成", Toast.LENGTH_LONG);
                        stopCountdown();
                        finish();
                        isCookingCurveUpdateCurveState = true;
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
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