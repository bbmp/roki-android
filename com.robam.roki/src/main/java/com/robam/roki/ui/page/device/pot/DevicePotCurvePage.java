package com.robam.roki.ui.page.device.pot;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.RCReponse;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.model.bean.PotOilTempParams;
import com.robam.roki.request.bean.RecipeCurveSuccessBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.curve.RecipeSuccessActivity;
import com.robam.roki.ui.view.CirclePercentView;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 无人锅曲线入口页面
 */

public class DevicePotCurvePage extends BasePage {

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
    LineChart cook_chart;
    @InjectView(R.id.btn_finish)
    Button btn_finish;
    @InjectView(R.id.btn_screen_switch)
    Button btn_screen_switch;

    private DynamicLineChartManager dm;
    private String mGuid = null;
    private String temperatureCurveParams;
    ArrayList<Entry> entryList = new ArrayList<>();
    private long curveId;
    private boolean isLoadCurveData = false;
    private int HeadId;//0左 1右灶
    Stove mStove;
    private String searchRecipeKeyWord;
    //说话点位
    List<Entry> appointList = new ArrayList<>();
    ArrayList<RecipeCurveSuccessBean.StepList> operationList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pot_curve, container, false);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        curveId = bd == null ? 0 : bd.getLong(PageArgumentKey.curveId);
        HeadId = bd == null ? 0 : bd.getInt(PageArgumentKey.HeadId);
        ButterKnife.inject(this, view);
        initData();
//        initRecordEvent();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initData() {
        mTvDeviceModelName.setText("记录烹饪曲线");

        dm = new DynamicLineChartManager(cook_chart, cx);
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, true);
        dm.setChartAttribute(false, false);
        cook_chart.notifyDataSetChanged();

//        img_add_step.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_chart_time.setText((int) (cook_chart.getHighestVisibleX() + cook_chart.getLowestVisibleX()) / 2 + "");
//            }
//        });
//        cook_chart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    tv_chart_time.setText((int) (cook_chart.getHighestVisibleX() + cook_chart.getLowestVisibleX()) / 2 + "");
//                }
//                return false;
//            }
//        });

        //上一次的高亮线
        Highlight[] highlightsOld = new Highlight[1];
        //开启值转高亮线
        cook_chart.valuesToHighlight();
        cook_chart.post(new Runnable() {
            @Override
            public void run() {
                //折线图点的标记
                LineChartMarkView mv = new LineChartMarkView(cx, cook_chart.getXAxis().getValueFormatter());
                mv.setChartView(cook_chart);
                cook_chart.setMarker(mv);
                //通过触摸生成高亮线
//                Highlight h = mLineChartStudy.getHighlightByTouchPoint(mLineChartStudy.getCenter().x,mLineChartStudy.getCenter().y);
                Highlight h = cook_chart.getHighlightByTouchPoint(cook_chart.getRight(), cook_chart.getTop());
                cook_chart.highlightValue(h, true);
            }
        });
        //图标刷新
        cook_chart.invalidate();
        cook_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                highlightsOld[0] = h;
            }

            @Override
            public void onNothingSelected() {
                //高亮线非选中状态
                cook_chart.highlightValues(highlightsOld);
            }
        });

    }

    public void drawPoint(float temp) {
        LineChartDataBean bean = new LineChartDataBean();
        bean.yValue = temp;
//        bean.xValue = stoveHead.time;
        if (entryList.isEmpty()) {
            bean.xValue = 0;
        } else {
            bean.xValue = (entryList.get(entryList.size() - 1).getX() + 2);
        }

//        dataBeanList.add(bean);
        entryList.add(new Entry(bean.xValue, bean.yValue));
        pageAddEntry(entryList);
    }

    public void pageAddEntry(List<Entry> entryList) {
        if (entryList.size() > 2) {
            RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
            if (operationList.size() == 0) {
                bean.setDescription("开始烹饪");
                bean.setMarkTemp((int) (entryList.get(0).getY()) + "");
                bean.setMarkTime((int) (entryList.get(0).getX()) + "");
                operationList.add(0, bean);
                appointList.add(new Entry(entryList.get(0).getX(), 2));
            } else if (operationList.size() == 1) {
                bean.setDescription("结束烹饪");
                bean.setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
                bean.setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
                appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 2));
                operationList.add(bean);
            } else {
                for (int i = 0; i < operationList.size(); i++) {
                    if (operationList.get(i).getDescription().equals("结束烹饪")) {
                        operationList.get(i).setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
                        operationList.get(i).setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
                        appointList.get(i).setX(entryList.get(entryList.size() - 1).getX());
                        break;
                    }
                }
            }
            dm.lineDataAppointSet(appointList);

        }

        //添加数据点
        new Thread(() -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dm.addEntryWithMV(entryList.get(entryList.size() - 1));
                Highlight h = cook_chart.getHighlightByTouchPoint(cook_chart.getRight(), cook_chart.getTop());
                cook_chart.highlightValue(h, true);
            }
        })).start();

    }

    Pot pot;
    int result;

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        pot = (Pot) event.pojo;
        //  event.pojo.tempUp
        int number = (int) ((1 + Math.random() * (2)));
        result += number;
        if (result > 60)
            return;
        drawPoint((short) result);
//        drawPoint(event.pojo.tempUp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.ll_curve_mark)
    public void onCurveMarkClicked() {
        ToastUtils.show("添加步骤", Toast.LENGTH_SHORT);
        appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 2));
        dm.lineDataAppointSet(appointList);
        RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
        bean.setDescription("");
        bean.setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
        bean.setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
        operationList.add(bean);
    }

    @OnClick(R.id.tv_finish)
    public void onFinishClicked() {
//        ToastUtils.show("烹饪结束", Toast.LENGTH_LONG);
//        Bundle bd = new Bundle();
//        bd.putLong(PageArgumentKey.curveId, curveId);
//        UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
//        cookingCurveUpdateCurveState(curveId + "", "3");


        UIService.getInstance().popBack();

//        RecipeSuccessActivity.show(cx, RecipeSuccessActivity.Companion.getCURVE(), curveId, operationList);

        RecipeSuccessActivity.show(cx, RecipeSuccessActivity.Companion.getCURVE(), curveId, operationList,null, entryList);
    }


//    private void initRecordEvent() {
//        tv_press.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    stopSpeech();
//                }
//                return false;
//            }
//        });
//        tv_press.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                speakSearchRecipe();
//                return false;
//            }
//        });
//    }
//
//    private void speakSearchRecipe() {
//        try {
//            DDS.getInstance().getAgent().getASREngine().startListening(new ASREngine.Callback() {
//                @Override
//                public void beginningOfSpeech() {
//                    LogUtils.i("TAG", "beginningOfSpeech:");
//                }
//
//                @Override
//                public void endOfSpeech() {
//                    LogUtils.i("TAG", "endOfSpeech:");
//                }
//
//                @Override
//                public void bufferReceived(byte[] bytes) {
//                    LogUtils.i("TAG", "bufferReceived:" + bytes);
//                }
//
//                @Override
//                public void partialResults(String s) {
//                    LogUtils.i("TAG", "partialResults:" + s);
//                    SpeechBean speechBean = new Gson().fromJson(s, SpeechBean.class);
//                    if (StringUtil.isEmpty(speechBean.getText()) && StringUtil.isEmpty(speechBean.getVar())) {
//                        noSpeech();
//                    }
//
//                }
//
//                @Override
//                public void finalResults(String s) {
//                    LogUtils.i("TAG", "finalResults:" + s);
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(s);
//                        searchRecipeKeyWord = jsonObject.get("text").toString();
//                        LogUtils.i("TAG", "searchRecipeKeyWord:" + searchRecipeKeyWord);
////                        searchRecipeByWord(searchRecipeKeyWord);
//
//                        //todo 模拟设置园点绘制位置和是否实心，1实心 2空心
//                        appointList.add(new Entry(entryList.get(entryList.size() - 1).getX(), 2));
//                        dm.lineDataAppointSet(appointList);
//                        RecipeCurveSuccessBean.StepList bean = new RecipeCurveSuccessBean.StepList();
//                        bean.setDescription(searchRecipeKeyWord);
//                        bean.setMarkTemp((int) (entryList.get(entryList.size() - 1).getY()) + "");
//                        bean.setMarkTime((int) (entryList.get(entryList.size() - 1).getX()) + "");
//                        operationList.add(bean);
//
//                        stopSpeech();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void error(String s) {
//                    LogUtils.i("TAG", "error:" + s);
//                }
//
//                @Override
//                public void rmsChanged(float v) {
//                    LogUtils.i("TAG", "rmsChanged:" + v);
//                }
//            });
//            ivRecipeSpeaking.setVisibility(View.VISIBLE);
//            GlideApp.with(getContext())
//                    .asGif2()
//                    .load(R.mipmap.voice_recognition)
//                    .into(ivRecipeSpeaking);
//            LogUtils.i("TAG", "avatarClick");
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//            LogUtils.i("TAG", "DDSNotInitCompleteException " + e.toString());
//        }
//    }
//
//    /**
//     * 未识别到语音
//     */
//    private void noSpeech() {
//        try {
//            TTSEngine ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//            ttsEngine.setMode(TTSEngine.CLOUD);
//            ttsEngine.setSpeaker("gqlanfp");
//            ttsEngine.speak("roki没有听清，请再说一遍好吗？", 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 关闭语音识别
//     */
//    private void stopSpeech() {
//        try {
//            DDS.getInstance().getAgent().getASREngine().stopListening();
//            DDS.getInstance().getAgent().avatarRelease();
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ivRecipeSpeaking.setVisibility(View.INVISIBLE);
//            }
//        });
//    }
}
