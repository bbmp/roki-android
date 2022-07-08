package com.robam.roki.ui.page.device.steamovenone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.RepiceEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.SteamOvenModelFunction620Params;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.utils.DialogUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsSteamOvenWorking620View extends FrameLayout {
    private final Context cx;
    private List<DeviceConfigurationFunctions> bgFunList;
    String localRecipeParams;


    List<DeviceConfigurationFunctions> localCookbookList = null;
    boolean isSpecial;
    public AbsSteamOvenWorking620View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public void setCookbookList( List<DeviceConfigurationFunctions> localCookbookList){
        this.localCookbookList =localCookbookList;
    }

    public AbsSteamOvenWorking620View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOneNew steameOvenOne) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        initView();
    }

    public AbsSteamOvenWorking620View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOneNew steameOvenOne, String localRecipeParams) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        this.localRecipeParams = localRecipeParams;
        initView();
    }

    public AbsSteamOvenWorking620View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOneNew steameOvenOne, String localRecipeParams, boolean isSpecial) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        this.localRecipeParams = localRecipeParams;
        this.isSpecial = isSpecial;
        initView();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    private void setTopInVisible(){
        if (SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.WEIBO
                ||SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.FURE
                ||SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.JIEDONG
                ||SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.WEIBOZHENG
                ||SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.WEIBOKAO){
            mTvTemp.setVisibility(View.GONE);
            mTvTempContent.setVisibility(View.GONE);
        }else{
            mTvTemp.setVisibility(View.VISIBLE);
            mTvTempContent.setVisibility(View.VISIBLE);
        }
    }



    private void updateUI() {
        //菜谱模式
        txtFinishName.setText("结束");
        mTvDeviceModelName.setText(steameOvenOne.getDt());
        fl_pause.setVisibility(View.VISIBLE);
        setTopInVisible();


        if (steameOvenOne.recipeId != 0) {
            setRecipeMode();
            ll_rika_z.setVisibility(View.GONE);
            tv_recipe.setVisibility(View.VISIBLE);
        } else {
            tv_recipe.setVisibility(View.GONE);
            ll_rika_z.setVisibility(View.VISIBLE);
            //运行模式
            steamRunModel = steameOvenOne.mode;
            SteamOvenModeEnum match = SteamOvenModeEnum.match(steamRunModel);
            String value = match.getValue();
            mTvModel.setText("模式");

//            101 001+100
            if (match == SteamOvenModeEnum.CHUGOU) {
                mTvModel.setVisibility(View.INVISIBLE);
                mTvTemp.setVisibility(View.INVISIBLE);
                mTvTime.setVisibility(View.INVISIBLE);
                ll_rika_z.setVisibility(View.VISIBLE);
                fl_pause.setVisibility(View.GONE);
                tv_recipe.setVisibility(View.GONE);
                tv_recipe.setText("");
                mFlRunStop.setVisibility(View.INVISIBLE);
                mTvTempContent.setVisibility(View.VISIBLE);
                mTvModelContent.setText("");

                mTvTimeContent.setText("");
                mTvModel.setText("");
                mTvTempContent.setText("除垢");
                fl_add_steam.setVisibility(View.GONE);
                mTvWorkDec.setText("");



                //工作状态
                short steamWorkStatus = steameOvenOne.workState;
                Log.e("除垢","工作:"+steamWorkStatus);
                if (steamWorkStatus == IntegStoveStatus.workState_preheat||
                steamWorkStatus==IntegStoveStatus.workState_work) {
                    startAnimation();
                    mTvWorkDec.setText("工作中");
                    mTvWorkStateName.setTextSize(29);
                    mTvWorkStateName.setText("");

                } else if (steamWorkStatus == IntegStoveStatus.workState_work_time_out||
                        steamWorkStatus==IntegStoveStatus.workState_preheat_time_out) {
                    //工作暂停
                    stopAnimation();
                    mTvWorkDec.setText(R.string.device_stop);
                    mTvWorkStateName.setTextSize(29);
                    mTvWorkStateName.setText("");
                }
                return;
            }
            mTvModel.setVisibility(View.VISIBLE);
            mTvTemp.setVisibility(View.VISIBLE);
            mTvTime.setVisibility(View.VISIBLE);
            if ( SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.WEIBOZHENG
                    ||SteamOvenModeEnum.match(steamRunModel)==SteamOvenModeEnum.WEIBOKAO){
                mTvTemp.setVisibility(View.GONE);
                mTvTempContent.setVisibility(View.GONE);
            }else{
                mTvTemp.setVisibility(View.VISIBLE);
                mTvTempContent.setVisibility(View.VISIBLE);
            }
            mTvModelContent.setText(value);
            mTvTempContent.setText(steameOvenOne.setUpTemp + "℃");
            String setTime = (steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)/60==0?"":(((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)/60)+"min");

            String sec=(steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)%60==0?"":
                    ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)%60+"s");

            mTvTimeContent.setText(setTime +sec);
            if (SteamOvenHelper.isShowSteam(steamRunModel)) {
                tv_steam.setVisibility(View.VISIBLE);
                tv_steam_content.setVisibility(View.VISIBLE);
                tv_steam_content.setText(SteamOvenHelper.getSteamContent(steameOvenOne.steam));
            } else {
                tv_steam.setVisibility(View.GONE);
                tv_steam_content.setVisibility(View.GONE);
            }
            //是否多段
            if (SteamOvenHelper.isMult(steameOvenOne.sectionNumber)&&!steameOvenOne.getGuid().getGuid().contains("920")) {
                mLlMult.setVisibility(View.VISIBLE);
                setMultShow(steameOvenOne.sectionNumber);
            } else {
                mLlMult.setVisibility(View.GONE);
            }

        }
        mFlRunStop.setVisibility(View.VISIBLE);
        //工作状态
        short steamWorkStatus = steameOvenOne.workState;
        //结束按钮
        GlideApp.with(cx).load( finishUrl)
                .placeholder(R.mipmap.img_run_stop)
                .error(R.mipmap.img_run_stop)
                .into(iv_finish);

        if (steamWorkStatus == IntegStoveStatus.workState_work) {
            startAnimation();
            mTvWorkStateName.setText(R.string.work_remaining_time);
            mTvWorkStateName.setTextSize(16);
            outTime = steameOvenOne.restTimeH * 256 + steameOvenOne.restTime;
            String time = TimeUtils.secToHourMinSec(outTime);
            //菜谱模式
            if (steameOvenOne.recipeId != 0){
                time = TimeUtils.secToHourMinSec(steameOvenOne.totalRemainSecondsH * 256 + steameOvenOne.totalRemainSeconds);
            }
            Log.e("时间--",steameOvenOne.totalRemainSecondsH+"----"+steameOvenOne.totalRemainSeconds);
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            tv_pause.setText("暂停");

            GlideApp.with(cx).load(pauseUrl)
                        .placeholder(R.drawable.ic_pause_ytj)
                        .error(R.drawable.ic_pause_ytj)
                        .into(iv_pause);

            //是否可以加蒸汽
            setSteam();
        } else if (steamWorkStatus == IntegStoveStatus.workState_work_time_out) {
            //工作暂停
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_stop);
            mTvWorkStateName.setTextSize(16);
            mTvWorkStateName.setVisibility(View.VISIBLE);
            String time = TimeUtils.secToHourMinSec(steameOvenOne.restTimeH * 256 +
                    steameOvenOne.restTime);
            //菜谱模式
            if (steameOvenOne.recipeId != 0){
                time = TimeUtils.secToHourMinSec(steameOvenOne.totalRemainSecondsH * 256
                        + steameOvenOne.totalRemainSeconds);
            }
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            mTvWorkDec.setVisibility(View.VISIBLE);
            tv_pause.setText("继续");

            GlideApp.with(cx).load(R.drawable.ic_start_ytj)
                    .placeholder(R.drawable.ic_start_ytj)
                    .error(R.drawable.ic_start_ytj)
                    .into(iv_pause);
//            iv_pause.setImageResource(R.drawable.ic_start_ytj);
            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_preheat) {
            startAnimation();
            mTvWorkStateName.setText(steameOvenOne.curTemp + "℃");
            mTvWorkStateName.setTextSize(16);
            mTvWorkDec.setText(R.string.device_preheating);
            mTvWorkDec.setTextSize(30);
            tv_pause.setText("暂停");
            GlideApp.with(cx).load( pauseUrl)
                    .placeholder(R.drawable.ic_pause_ytj)
                    .error(R.drawable.ic_pause_ytj)
                    .into(iv_pause);
//            iv_pause.setImageResource(R.drawable.ic_pause_ytj);
            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_preheat_time_out) {
            //预热暂停
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_stop);
            mTvWorkStateName.setTextSize(16);
            String time = TimeUtils.secToHourMinSec(steameOvenOne.restTimeH * 256 + steameOvenOne.restTime);
            //菜谱模式
            if (steameOvenOne.recipeId != 0){
                time = TimeUtils.secToHourMinSec(steameOvenOne.totalRemainSecondsH * 256 + steameOvenOne.totalRemainSeconds);
            }
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            mFlRunStop.setVisibility(View.VISIBLE);
            mTvWorkDec.setVisibility(View.VISIBLE);
            tv_pause.setText("继续");
            GlideApp.with(cx).load(R.drawable.ic_start_ytj)
                    .placeholder(R.drawable.ic_start_ytj)
                    .error(R.drawable.ic_start_ytj)
                    .into(iv_pause);

            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_complete) {
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_finish);
            mTvWorkStateName.setTextSize(26);
            mTvWorkDec.setVisibility(View.GONE);
            mFlRunStop.setVisibility(View.GONE);
            fl_pause.setVisibility(View.GONE);
            fl_add_steam.setVisibility(View.INVISIBLE);
        }else if (steamWorkStatus==IntegStoveStatus.workState_order){ //预约
            mTvWorkDec.setText("预约中");
            mTvWorkDec.setTextSize(30);
            String time =null;
            if (steameOvenOne.orderMinutesLength==2) {
                time=TimeUtils.secToHourMinSec(steameOvenOne.orderLeftMinutes + steameOvenOne.orderRightMinutes * 256);
            }else if (steameOvenOne.orderMinutesLength==3){
                time=TimeUtils.secToHourMinSec(steameOvenOne.orderLeftMinutes1* 256* 256 + steameOvenOne.orderRightMinutes * 256+steameOvenOne.orderLeftMinutes);
            }else if (steameOvenOne.orderMinutesLength==4){
                time=TimeUtils.secToHourMinSec(steameOvenOne.orderRightMinutes1* 256* 256*256+steameOvenOne.orderLeftMinutes1* 256* 256 +
                        steameOvenOne.orderRightMinutes * 256+
                        steameOvenOne.orderLeftMinutes);
            }
            mTvWorkStateName.setText("剩余时间"+time);
            mTvWorkStateName.setTextSize(16);
            SteamOvenModeEnum match = SteamOvenModeEnum.match(steamRunModel);
            String value = match.getValue();
            mTvModelContent.setText(value);
            mTvTempContent.setText(steameOvenOne.setUpTemp + "℃");

            String setTime = (steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)/60==0?"":(((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)/60)+"min");
            String sec=(steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)%60==0?"":
                    ((steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)%60+"s");

            mTvTimeContent.setText(setTime+sec);

//            GlideApp.with(cx).load(finishUrl)
//                    .placeholder(R.mipmap.img_run_stop)
//                    .error(R.mipmap.img_run_stop)
//                    .into(iv_pause);
            GlideApp.with(cx).load(R.drawable.ic_start_ytj)
                    .placeholder(R.drawable.ic_start_ytj)
                    .error(R.drawable.ic_start_ytj)
                    .into(iv_pause);

            tv_pause.setText("立即启动");

        }

          //判断是否可以加蒸汽

        setTopInVisible();
        setSteam();

        if (SteamOvenModeEnum.match(steameOvenOne.mode)==SteamOvenModeEnum.EXP){
            tv_steam_content.setVisibility(View.VISIBLE);
            tv_steam.setVisibility(View.VISIBLE);
            tv_steam.setText("上温度");
            tv_steam_content.setText(steameOvenOne.setUpTemp + "℃");
            mTvTemp.setText("下温度");
            mTvTempContent.setText(steameOvenOne.setDownTemp+ "℃");
        }else {
            mTvTempContent.setText(steameOvenOne.setUpTemp + "℃");
        }
        if (steameOvenOne.getDt().contains("920")){
            fl_add_steam.setVisibility(View.INVISIBLE);
        }

        Log.e("模式",steamWorkStatus+"---");
    }


    private void setSteam(){
        if (steameOvenOne.steamState==2 && outTime>=2*60&&steameOvenOne.sectionNumber<=1) {
            fl_add_steam.setVisibility(View.VISIBLE);
            if (steamUrl != null) {
                GlideApp.with(getContext()).load(steamUrl)
                        .placeholder(R.drawable.icon_add_steam)
                        .error(R.drawable.icon_add_steam)
                        .into(iv_add_steam);
            }else {
                GlideApp.with(getContext()).load(R.drawable.icon_add_steam).into(iv_add_steam);
            }
        } else {
            fl_add_steam.setVisibility(View.INVISIBLE);
        }
        //如果是本地菜谱就消失
        if (steameOvenOne.recipeId!=0){
            fl_add_steam.setVisibility(View.INVISIBLE);
        }
    }

    String finishUrl;
    String pauseUrl;
    String steamUrl;
    public void initView(){
        View view = LayoutInflater.from(cx).inflate(R.layout.page_stream_620_work, this, true);
        ScreenAdapterTools.getInstance().loadView(view);


        for (DeviceConfigurationFunctions deviceConfigurationFunctions : bgFunList) {
            if (deviceConfigurationFunctions.subView!=null&&
                    deviceConfigurationFunctions.subView.subViewModelMap!=null&&
                    deviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView!=null&&
                    deviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions!=null){
            for (DeviceConfigurationFunctions deviceConfigurationFunction :
                    deviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions) {
                if (deviceConfigurationFunction.functionCode.equals("finish")) {
                    finishUrl = deviceConfigurationFunction.backgroundImg;
                } else if (deviceConfigurationFunction.functionCode.equals("wait")) {
                    pauseUrl = deviceConfigurationFunction.backgroundImg;
                } else if (deviceConfigurationFunction.functionCode.equals("addSteam")) {
                    steamUrl = deviceConfigurationFunction.backgroundImg;
                }
            }

            }

        }


        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        findViewById(R.id.title_bar_steam_one).setVisibility(View.GONE);
        updateUI();

    }

    AbsSteameOvenOneNew steameOvenOne;
    String tag;
    String mViewBackgroundImg;
//    @InjectView(R.id.iv_back)
//    ImageView mIvBack;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.ll_rika_z)
    LinearLayout ll_rika_z;
    @InjectView(R.id.tv_recipe)
    TextView tv_recipe;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_steam)
    TextView tv_steam;
    @InjectView(R.id.tv_temp)
    TextView mTvTemp;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_model_content)
    TextView mTvModelContent;
    @InjectView(R.id.tv_steam_content)
    TextView tv_steam_content;
    @InjectView(R.id.tv_temp_content)
    TextView mTvTempContent;
    @InjectView(R.id.tv_time_content)
    TextView mTvTimeContent;
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.ll_mult)
    LinearLayout mLlMult;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout mLlRunAnimation;
    Animation circleRotateDown;
    Animation circleRotateUp;
    @InjectView(R.id.iv_run_down)
    ImageView mIvRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView mIvRunUp;
    @InjectView(R.id.tv_work_state_name)
    TextView mTvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView mTvWorkDec;
    @InjectView(R.id.fl_run_stop)
    FrameLayout mFlRunStop;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout mFlRunAndStop;

    @InjectView(R.id.fl_pause)
    FrameLayout fl_pause;
    @InjectView(R.id.iv_pause)
    ImageView iv_pause;
    @InjectView(R.id.tv_pause)
    TextView tv_pause;
    @InjectView(R.id.fl_add_steam)
    FrameLayout fl_add_steam;
    @InjectView(R.id.iv_add_steam)
    ImageView iv_add_steam;
    @InjectView(R.id.iv_finish)
    ImageView iv_finish;

    @InjectView(R.id.finish_name)

    TextView txtFinishName;

    @InjectView(R.id.tv_chugou_message)
    TextView tv_chugou_message;

    /**
     * 当前运行状态
     */
    private short mSteamWorkStatus;
    private IRokiDialog mCloseDialog;
    private List<DeviceConfigurationFunctions> mDatas;
    /**
     * 当前工作模式
     */
    private short steamRunModel;
    /**
     * 是否需要水
     */
    private String needWater = "0";
    private int outTime;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected ) {
//            ToastUtils.showLong(R.string.device_new_connected);
//            UIService.getInstance().popBack();
        }
    }
    public void inVisible(){
        if (mTvWorkStateName != null && mTvWorkDec != null){
            mTvWorkDec.setVisibility(GONE);
            mTvWorkStateName.setVisibility(GONE);
        }
    }

    public void setInVisible(){
        setVisibility(View.INVISIBLE);
    }

    protected void closeAllDialog() {
//        if (closedialog != null && closedialog.isShow()) {
//            closedialog.dismiss();
//        }


    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {


        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID())) {
            return;
        }
        steameOvenOne = (AbsSteameOvenOneNew) event.pojo;

        if (!steameOvenOne.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
//            UIService.getInstance().popBack();
            return;
        }

//            try {
                updateUI();
//            }catch (Exception e){
//                e.getMessage() ;
//            }

//        }

    }



    /**
     * 设置菜谱模式顶部显示
     */
    public void setRecipeMode() {

        ll_rika_z.setVisibility(View.GONE);
        tv_recipe.setVisibility(View.VISIBLE);
        JSONObject localRecipe;
        try {
            localRecipe = new JSONObject(localRecipeParams);
            JSONObject jsonObject = localRecipe.getJSONObject(steameOvenOne.recipeId + "");
            String pKey = jsonObject.getString("pKey");
            String name = jsonObject.getString("value");
            tv_recipe.setText(pKey + " " + name);
//            if (steameOvenOne.recipteName!=null){
//                for (DeviceConfigurationFunctions deviceConfigurationFunctions : localCookbookList) {
//
//                    if (deviceConfigurationFunctions.id== steameOvenOne.recipeId){
//                        tv_recipe.setText("P" + steameOvenOne.recipeId + " " + deviceConfigurationFunctions.functionName);
//                        break;
//                    }
//                }
//
//
//            }else {
//                try {
//                    RepiceEnum match = RepiceEnum.match(steameOvenOne.recipeId);
//                    tv_recipe.setText("P" + steameOvenOne.recipeId + " " + match.getValue());
//                    ll_rika_z.setVisibility(View.GONE);
//                    tv_recipe.setVisibility(View.VISIBLE);
//                }catch (Exception e){
//                    e.getMessage();
//                }
//
//            }
            if (steameOvenOne.cookNeedWaterMap != null){
                needWater = steameOvenOne.cookNeedWaterMap.get(String.valueOf(steameOvenOne.recipeId)) == null ? "0" : steameOvenOne.cookNeedWaterMap.get(String.valueOf(steameOvenOne.recipeId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置正常工作模式显示
     */
    public void setMode() {

    }


    /**
     * 设置多段状态的显示
     */
    public void setMultShow(short sectionNumber) {

        if (sectionNumber==1){
            mBtnOne.setVisibility(View.VISIBLE);
            mBtnTwo.setVisibility(View.GONE);
            mBtnThere.setVisibility(View.GONE);
        }else    if (sectionNumber == 2) {
            mBtnOne.setVisibility(View.VISIBLE);
            mBtnTwo.setVisibility(View.VISIBLE);
            mBtnThere.setVisibility(View.GONE);
        }else if (sectionNumber==3){
            mBtnOne.setVisibility(View.VISIBLE);
            mBtnTwo.setVisibility(View.VISIBLE);
            mBtnThere.setVisibility(View.VISIBLE);
        }
        if (steameOvenOne.curSectionNbr == 1) {
            mBtnOne.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
            mBtnTwo.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnThere.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
        } else if (steameOvenOne.curSectionNbr == 2) {
            mBtnOne.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnTwo.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
            mBtnThere.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
        } else if (steameOvenOne.curSectionNbr == 3) {
            mBtnOne.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnTwo.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnThere.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
        }
    }



    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            mIvRunUp.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvRunDown.startAnimation(circleRotateDown);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            mIvRunUp.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            mIvRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            mIvRunUp.clearAnimation();
        }

    }

//
//    @OnClick(R.id.iv_back)
//    public void onMIvBackClicked() {
//        UIService.getInstance().popBack();
//    }

    /**
     * 加蒸汽
     */
    @OnClick(R.id.fl_add_steam)
    public void addSteam() {
        if (outTime < 120){
            ToastUtils.showShort("当前模式最后两分钟不能加蒸汽");
            return;
        }
        if (!SteamOvenHelper.isWaterBoxState(steameOvenOne.waterBoxState)) {
            ToastUtils.showShort("水箱已弹出，请检查水箱状态");
            return;
        }
        if (!SteamOvenHelper.isWaterLevelState(steameOvenOne.waterLevelState)) {
            ToastUtils.showShort("水箱缺水，请加水");
            return;
        }


//        if (steameOvenOne.steamState != 2) {
//            ToastUtils.showShort("距离上次加蒸汽时间太近");
//            return;
//        }

        steameOvenOne.addStream((short) 1, new VoidCallback() {


            @Override
            public void onSuccess() {
                 ToastUtils.show("添加蒸汽成功", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

//        if (SteamOvenHelper.isDescale(integratedStove.descaleFlag)) {
//            ToastUtils.showShort("设备需要除垢后才能加蒸汽，请先除垢");
//            return;
//        }

//        steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model),Integer.parseInt(selectTime), Short.parseShort(selectTemp), (short) 0, (short) 0, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                LogUtils.i("202010201715", "success:下发模式成功2");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMul(code);
//                    }
//                },100);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });

//        steameOvenOne.setSteameOvenOneRunMode(SteamOvenHelper.isPause(steameOvenOne.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });
//        integratedStove.setSteamWorkStatus((short) 1, (short) 16, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                ToastUtils.showShort("加蒸汽成功");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });
    }

    /**
     * 暂停 开始
     */
    @OnClick(R.id.fl_pause)
    public void onMFlPauseClicked() {

        if ((steameOvenOne.workState==IntegStoveStatus.workState_order)) {
//            onMFlRunStopClicked();
//            setStop();/

            steameOvenOne.setSteameOvenOneRunMode(steameOvenOne.mode, (short) (steameOvenOne.setTimeH * 256 + steameOvenOne.setTime)/60,

                    steameOvenOne.setUpTemp, (byte) 0, (byte) 0, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else{
            //暂停状态
            if (SteamOvenHelper.isPause(steameOvenOne.workState)) {
                //需要水的模式
                if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(steameOvenOne.mode)) || SteamOvenHelper.isRecipeWater(needWater)) {
                    if (SteamOvenHelper.isDescale(steameOvenOne.descaleFlag)) {
                        ToastUtils.showShort("设备需要除垢后才能继续工作，请先除垢");
                        return;
                    }
                    if (!SteamOvenHelper.isWaterBoxState(steameOvenOne.waterBoxState)) {
                        ToastUtils.showShort("水箱已弹出，请检查水箱状态");
                        return;
                    }
                    if (!SteamOvenHelper.isWaterLevelState(steameOvenOne.waterLevelState)) {
                        ToastUtils.showShort("水箱缺水，请加水");
                        return;
                    }
                }
            }
            steameOvenOne.setSteamWorkStatus(SteamOvenHelper.isPause(steameOvenOne.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
//        integratedStove.setSteamWorkStatus(SteamOvenHelper.isPause(integratedStove.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });
    }

    @OnClick(R.id.fl_run_stop)
    public void onMFlRunStopClicked() {

//        if (steameOvenOne.workState==IntegStoveStatus.workState_order) {
//

//        }else {
//        LogUtils.i("20180502", " mRika:" + integratedStove.steamWorkStatus);
            setStop();
//        }
    }



    private  void setStop(){
        mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        mCloseDialog.setTitleText(R.string.close_work);
        mCloseDialog.setContentText(R.string.is_close_work);
        mCloseDialog.show();
        mCloseDialog.setOkBtn(R.string.ok_btn, v -> {
            mCloseDialog.dismiss();
            steameOvenOne.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop, (short) 4, new VoidCallback() {
                @Override
                public void onSuccess() {
//                                UIService.getInstance().postPage(PageKey.AbsRikaDevice);
//                  UIService.getInstance().popBack();
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        });
        mCloseDialog.setCancelBtn(R.string.can_btn, v -> {
            if (mCloseDialog.isShow()) {
                mCloseDialog.dismiss();
            }
        });
    }
}
