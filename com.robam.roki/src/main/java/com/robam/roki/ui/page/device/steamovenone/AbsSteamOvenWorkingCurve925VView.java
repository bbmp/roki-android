package com.robam.roki.ui.page.device.steamovenone;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.ui.view.recipeclassify.GlideImageLoader;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsSteamOvenWorkingCurve925VView extends FrameLayout {

    Context cx;
    AbsSteameOvenOne steameOven;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.rl_top)
    RelativeLayout rlTop;
    @InjectView(R.id.btn_suspend)
    Button btnSuspend;
    @InjectView(R.id.btn_finish)
    Button btnFinish;
    @InjectView(R.id.ll_bottom)
    LinearLayout llBottom;
    @InjectView(R.id.tv_model)
    TextView tvModel;
    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_model_content)
    TextView tvModelContent;
    @InjectView(R.id.tv_temp_content)
    TextView tvTempContent;
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
    @InjectView(R.id.normal_show)
    CardView normal_show;
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
    @InjectView(R.id.iv_device_switch)
    ImageView mIvDeviceSwitch;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;

    private DynamicLineChartManager dm;
    private PayLoadCookBook mPayLoadCookBook;
    private String temperatureCurveParams;
    private String curveID;
    List<Entry> entryList, drawList;

    AbsSteameOvenOne steameOvenOne;
    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> ovenRunListDown;
    private List<DeviceConfigurationFunctions> steamRunListDown;
    String localRecipeParams;

    public AbsSteamOvenWorkingCurve925VView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsSteamOvenWorkingCurve925VView(Context context, PayLoadCookBook mPayLoadCookBook, AbsSteameOvenOne steameOvenOne) {
        super(context);
        this.cx = context;
        this.mPayLoadCookBook = mPayLoadCookBook;
        this.steameOvenOne = steameOvenOne;
        initView();
    }

    public AbsSteamOvenWorkingCurve925VView(Context context, String temperatureCurveParams, AbsSteameOvenOne steameOvenOne, String curveID) {
        super(context);
        this.cx = context;
        this.temperatureCurveParams = temperatureCurveParams;
        this.steameOvenOne = steameOvenOne;
        this.curveID = curveID;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_steamoven_925_working_v_page, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        dm = new DynamicLineChartManager(cookChart, cx);
//        if (mPayLoadCookBook == null) {
//            return;
//        }
        String temperature = mPayLoadCookBook.curveCookbookDto.temperatureCurveParams;
        List<LineChartDataBean> dataBeanList = new ArrayList<>();
//        if (temperatureCurveParams == null || temperatureCurveParams.isEmpty()) {
//
//        } else {
//            String temperature = temperatureCurveParams;
//            temperature = temperature.substring(1, temperature.indexOf("}"));
//            dataBeanList = ChartDataReviseUtil.curveDataToLine(temperature);
//        }
            temperature = temperature.substring(1, temperature.indexOf("}"));
            dataBeanList = ChartDataReviseUtil.curveDataToLine(temperature);
        entryList = new ArrayList<>(); //数据集合
        drawList = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            entryList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
            if (dataBeanList.get(i).isBigPoint) {
                drawList.add(new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue));
            }
        }
        List<Entry> transList = new ArrayList<>();
        transList.addAll(entryList);
        for (int i = 0; i < Math.ceil(entryList.size() / 2); i++) {
            transList.add(0, new Entry(-entryList.get(i).getX(), 0));
            transList.add(new Entry(entryList.get(entryList.size() - 1).getX() + entryList.get(i).getX(), 0));
        }
        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, false);

    }

    @OnClick({R.id.iv_back, R.id.btn_screen_switch, R.id.iv_device_switch, R.id.btn_finish, R.id.btn_suspend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
//            case R.id.ll_voice_long:
//                addEntry();
//                break;
            case R.id.btn_suspend:
                onPauseClickView();
                break;
            case R.id.btn_finish:
                finishCooking();
                break;
            case R.id.btn_screen_switch:
                onClickChangePage.onclickChange(0);
                break;
            case R.id.iv_device_switch:
                onClickmDeviceSwitch.onClickmDeviceSwitch();
                break;
        }
    }

    //暂停工作
    public void onPauseClickView() {
        //工作中--> 暂停
        if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            cookingCurveUpdateCurveState(curveID,"2");
            steameOvenOne.setSteameOvenStatus2(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.Pause, null);
            //暂停中-->开始
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            cookingCurveUpdateCurveState(curveID,"1");
            steameOvenOne.setSteameOvenStatus2(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.WorkingStatus, null);
        }


    }

    IRokiDialog closedialog = null;

    private void finishCooking() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();

        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (steameOvenOne.powerState == SteamOvenOnePowerOnStatus.Order || steameOvenOne.powerState == SteamOvenOnePowerOnStatus.WorkingStatus) {
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, null);
                        cookingCurveUpdateCurveState(curveID,"3");
                    } else {
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.OperatingState, null);
                    }
                }
            }
        });

        closedialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }

    private void cookingCurveUpdateCurveState(String id, String state) {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurveUpdateCurveState(id, state,
                new Callback<RCReponse>() {

                    @Override
                    public void onSuccess(RCReponse rcReponse) {
                        ToastUtils.show("state:"+state,Toast.LENGTH_LONG);

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }


    public interface OnClickChangePage {

        void onclickChange(int VIndex);

    }

    public OnClickChangePage onClickChangePage;

    public void setOnClickChangePageLister(OnClickChangePage onClickChangePage) {
        this.onClickChangePage = onClickChangePage;
    }

    public interface OnClickmDeviceSwitch {

        void onClickmDeviceSwitch();

    }

    public OnClickmDeviceSwitch onClickmDeviceSwitch;

    public void setOnClickmDeviceSwitchLister(OnClickmDeviceSwitch onClickmDeviceSwitch) {
        this.onClickmDeviceSwitch = onClickmDeviceSwitch;
    }
//    List<LineChartDataBean> dataBeanList = new ArrayList<>();

    public void drawPoint(AbsSteameOvenOne steameOven) {
        LineChartDataBean bean = new LineChartDataBean();
        bean.yValue = steameOven.temp;
        if (entryList.isEmpty()) {
            bean.xValue = 0;
        } else {
            bean.xValue = (entryList.get(entryList.size() - 1).getX() + 2);
        }
        if (steameOven.workState == SteamOvenOneWorkStatus.PreHeat &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            bean.stepName = "预热中";
        } else if ((steameOven.workState == SteamOvenOneWorkStatus.Working &&
                steameOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            bean.stepName = "工作中";
        }
//        dataBeanList.add(bean);
        entryList.add(new Entry(bean.xValue, bean.yValue));
        pageAddEntry(entryList);
    }

    public void pageAddEntry(List<Entry> entryList) {

        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dm.addEntry(entryList.get(entryList.size() - 1));
                    }
                });
            }

        }).start();

    }

    public void updateStatus(AbsSteameOvenOne steameOvenOne) {
        if (steameOvenOne.AutoRecipeModeValue == 0) {
            if (steameOvenOne.workModel == SteamOvenOneModel.EXP) {
                expShow();
                commonExpMode();
            } else {
                normalShow();
                commonMode();
            }
        } else {
            localAutoRecipe();
        }

        if (steameOvenOne.workState == SteamOvenOneWorkStatus.PreHeat &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            heatMode();
        } else if (steameOvenOne.workState == SteamOvenOneWorkStatus.Working &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            runMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerStatus.On) {
            LogUtils.i("202010231621", steameOvenOne.powerOnStatus + "");
//            pauseMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
//            orderMode();
        }
    }

    private void expShow() {
        numStep();
        for (int i = 0; i < bgFunList.size(); i++) {
            if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            } else if ("roastRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                ovenRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            }
        }
    }

    //多段烹饪步鄹
    private void numStep() {
        if (steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
            llMoreStep.setVisibility(View.GONE);
            return;
        }
        if (steameOvenOne.MultiStepCookingStepsValue != 0) {
            llMoreStep.setVisibility(View.VISIBLE);
            if (steameOvenOne.MultiStepCookingStepsValue == 2) {
                tvDotThree.setVisibility(View.INVISIBLE);
            }
        } else {
            llMoreStep.setVisibility(View.GONE);
        }
        if (steameOvenOne.MultiStepCookingStepsValue == 2) {
            tvDotThree.setVisibility(View.GONE);
            switch (steameOvenOne.MultiStepCurrentStepsValue) {
                case 1:
                    tvDotOne.setBackground(getResources().getDrawable(R.drawable.shape_green_dot));
                    tvDotTwo.setBackground(getResources().getDrawable(R.drawable.shape_grey_dot));
                    break;
                case 2:
                    tvDotOne.setBackground(getResources().getDrawable(R.mipmap.icon_ok));
                    tvDotTwo.setBackground(getResources().getDrawable(R.drawable.shape_green_dot));
                    break;
            }
        } else if (steameOvenOne.MultiStepCookingStepsValue == 3) {
            switch (steameOvenOne.MultiStepCurrentStepsValue) {

                case 1:
                    tvDotOne.setBackground(getResources().getDrawable(R.drawable.shape_green_dot));
                    tvDotTwo.setBackground(getResources().getDrawable(R.drawable.shape_grey_dot));
                    tvDotThree.setBackground(getResources().getDrawable(R.drawable.shape_grey_dot));
                    break;
                case 2:
                    tvDotOne.setBackground(getResources().getDrawable(R.mipmap.icon_ok));
                    tvDotTwo.setBackground(getResources().getDrawable(R.drawable.shape_green_dot));
                    tvDotThree.setBackground(getResources().getDrawable(R.drawable.shape_grey_dot));
                    break;
                case 3:
                    tvDotOne.setBackground(getResources().getDrawable(R.mipmap.icon_ok));
                    tvDotTwo.setBackground(getResources().getDrawable(R.mipmap.icon_ok));
                    tvDotThree.setBackground(getResources().getDrawable(R.drawable.shape_green_dot));
                    break;
            }
        }

    }

    private void commonExpMode() {
//        expTempContent1.setText(steameOvenOne.setTemp + "℃");
//        expTempContent2.setText(steameOvenOne.setTempDownValue + "℃");
//        expTimeContent.setText(steameOvenOne.setTime + "min");
//        JSONObject obj = null;
//        try {
//            obj = new JSONObject(subFunList.get(0).functionParams);
//            String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
//            expModelContent.setText(param);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void commonMode() {
        tvTempContent.setText(steameOvenOne.setTemp + "℃");
        if (steameOvenOne.setTimeH > 0) {
            byte low = (byte) steameOvenOne.setTime;
            byte high = (byte) steameOvenOne.setTimeH;
            byte[] bytes = new byte[2];
            bytes[0] = high;
            bytes[1] = low;
            String time = StringUtils.bytes2Hex(bytes).trim();
            time = time.replace(" ", "");
            tvTimeContent.setText(new BigInteger(time, 16) + "min");
        } else {
            tvTimeContent.setText(steameOvenOne.setTime + "min");
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
            tvModelContent.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void normalShow() {
        try {
            numStep();
            if (steameOvenOne.AutoRecipeModeValue == 0) {
                numStep();
            }

            if (SteamOvenOneModel.FAXIAO == steameOvenOne.workModel
                    || SteamOvenOneModel.XIANNENZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.YINGYANGZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.GAOWENZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.GANZAO == steameOvenOne.workModel
                    || SteamOvenOneModel.BAOWEN == steameOvenOne.workModel
                    || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
                for (int i = 0; i < bgFunList.size(); i++) {
                    if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                        subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    } else if ("steamRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                        steamRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    }
                }

            } else {
                for (int i = 0; i < bgFunList.size(); i++) {
                    if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                        subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    } else if ("roastRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                        ovenRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    }
                }

            }


            for (int i = 0; i < subFunList.size(); i++) {
                if ("model".equals(subFunList.get(i).functionCode)) {
                    tvModel.setText(subFunList.get(i).functionName);
                    //tvTitle.setText(subFunList.get(i).functionName);
                } else if ("temperature".equals(subFunList.get(i).functionCode)) {
                    tvTemp.setText(subFunList.get(i).functionName);
                } else if ("remainingTime".equals(subFunList.get(i).functionCode)) {
                    tvTime.setText(subFunList.get(i).functionName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //本地自动菜谱模式
    private void localAutoRecipe() {
        try {
            JSONObject jsonObject = new JSONObject(localRecipeParams);
            JSONObject obj = jsonObject.getJSONObject(steameOvenOne.AutoRecipeModeValue + "");
            String pKey = obj.getString("pKey");
            String value = obj.getString("value");
//            tvLocalRecipe.setText(pKey + " " + value);
            tvModelContent.setText(pKey + " " + value);

            for (int i = 0; i < bgFunList.size(); i++) {
                if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                    subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                } else if ("steamRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                    steamRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //预热
    private void heatMode() {
        tvModelContent.setText("预热中");
    }

    //工作中
    private void runMode() {
        String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
        tvWorkSurplusTimeCon.setText("剩余时间" + minSec);
    }

}
