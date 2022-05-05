package com.robam.roki.ui.page;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.RecipeDetailAutoView;
import com.robam.roki.ui.view.RecipeDetailView;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.utils.DeviceSelectUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.common.util.StoveSendCommandUtils;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/12/21.
 */

public class RecipeRRQZPage extends AbsDUIPage {
    @InjectView(R.id.recipe_quit)
    ImageView recipeQuit;
    @InjectView(R.id.view_add)
    FrameLayout viewAdd;
    /**
     * 提示
     */
    private ImageView ivTishi;
    /**
     * 方向
     */
    private ImageView ivFangxiang;
    /**
     * 语音
     */
    private CheckBox ivYuyin;
    /**
     * 音量
     */
    private CheckBox cbYinliang;
    /**
     * ROKI提示
     */
    private TextView tvRokiMessage;
    /**
     * ROKI图标
     */
    private ImageView ivRoki;

    ArrayList<CookStep> cookSteps;
    Recipe recipe;
    IDevice iDevice;
    IDevice stoveIdevice;
    String guid;
    Long id;

    AbsFan fan;
    Stove stove;
    Stove.StoveHead stoveHeadId;
    int stoveflag;
    int step = 0;

    RecipeDetailAutoView recipeDetailView;
    StoveSendCommandUtils stoveUtils;
    IRokiDialog dialog;
    IRokiDialog dialog1;
    /**
     * 横竖屏切换
     */
    boolean is_vh_switching = false;

    boolean isError  = false;
    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isError){
                LogUtils.i(TAG ,"重新唤醒识别");
                setStep("重新唤醒识别");
                isError = false ;
            }
            myHandler.postDelayed(this, 5000);
        }
    };
    private String imgLarge;
    private TTSEngine ttsEngine;

    @Override
    protected int getLayoutId() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return R.layout.recipe_auto_cooking_h;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return R.layout.recipe_auto_cooking;
        }
        return R.layout.recipe_auto_cooking;
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setTextDark(getContext(), true);
        StatusBarUtils.setColor(cx, getResources().getColor(R.color.white));
        getTitleBar().setOnTitleBarListener(this);
        ivTishi = (ImageView) findViewById(R.id.iv_tishi);
        ivFangxiang = (ImageView) findViewById(R.id.iv_fangxiang);
        ivYuyin = (CheckBox) findViewById(R.id.iv_yuyin);
        cbYinliang = (CheckBox) findViewById(R.id.cb_yinliang);
        tvRokiMessage = (TextView) findViewById(R.id.tv_roki_message);
        ivRoki = (ImageView) findViewById(R.id.iv_roki);

//        ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
        try {
//            ttsEngine.setMode( TTSEngine.LOCAL);
//            ttsEngine.setSpeaker("gqlanfp");
            asrEngine = DDS.getInstance().getAgent().getASREngine();
            SpeechManager.getInstance().init(Plat.app);//初始化語音播放組件
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivTishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesageDialog();
            }
        });
        ivFangxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("step", step);
                getActivity().getIntent().putExtra("is_vh_switching", true);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        cbYinliang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    SpeechManager.getInstance().stopSpeaking();
                    speech();
                }
            }
        });
        ivYuyin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    speakSearchRecipe();
                } else {
                    try {
                        asrEngine.startListening(null);
                        asrEngine.stopListening();
                    } catch (DDSNotInitCompleteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        myHandler.postDelayed(runnable, 5000);
    }

    @Override
    protected void initData() {
        speakSearchRecipe();
        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
        cookSteps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
        recipe = (Recipe)getActivity().getIntent().getSerializableExtra("recipe");
        stoveflag = getActivity().getIntent().getIntExtra("stoveHeadId", 0);
        imgLarge = getActivity().getIntent().getStringExtra("imgLarge");
        LogUtils.i("20171213", "stoveFlag::" + stoveflag + "cookSteps::" + cookSteps.toString());
        LogUtils.i("20171127", "id:" + id);
        guid = getActivity().getIntent().getStringExtra("Guid");
        step = getActivity().getIntent().getIntExtra("step", 0);
        is_vh_switching = getActivity().getIntent().getBooleanExtra("is_vh_switching", false);
        iDevice = Plat.deviceService.lookupChild(guid);
        fan = (AbsFan) iDevice;
        stoveIdevice = fan.getChildStove();
        stove = (Stove) stoveIdevice;
        if (stove != null) {
            if (stoveflag == 0) {
                stoveHeadId = stove.leftHead;
            } else {
                stoveHeadId = stove.rightHead;
            }
        } else {
            stoveIdevice = fan.getChildStove();
            stove = (Stove) stoveIdevice;
        }

        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
        dialog1 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_13);

        //  LogUtils.i("20171127","recipe--init:"+recipe);
//        recipeDetailView = new RecipeDetailAutoView(cx, cookSteps, stoveIdevice);
        recipeDetailView = new RecipeDetailAutoView(cx, cookSteps, stoveIdevice, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1);
        viewAdd.addView(recipeDetailView);
//        onSpeakClick(0);
        stoveUtils = new StoveSendCommandUtils(fan, stove, stoveHeadId, cookSteps, step);
        // init();
        next();
        onClickCardView();
    }

    int currentTime;
    int totalTime;
    CookStep cookStep;

    private void next() {
        if (!stove.isConnected()) {
            ToastUtils.show("灶具已离线", Toast.LENGTH_SHORT);
            return;
        }

        cookStep = null;
        currentTime = 0;
        totalTime = 0;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        try {
            cookStep = DaoHelper.getDao(CookStep.class).queryForId(cookSteps.get(step).getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (cookStep == null) {
            ToastUtils.show("数据获取异常", Toast.LENGTH_SHORT);
        }
        if (cookStep.getDc().equals("")) {

            if (!is_vh_switching) {
                shutDownDevice();
                onSpeakClick(step);
            }
            recipeDetailView.onfreshNoDeviceView(step);
            is_vh_switching = false;
            return;
        }
        LogUtils.i("20171228--stove", "stoveheadId:" + stoveHeadId.getStatus());
        if (stoveHeadId.getStatus() == 0 || stoveHeadId.getStatus() == 3) {
            int s = 0;
            if (stoveflag == 0) {
                s = R.string.recipe_auto_fire_tips_content_left;
            } else {
                s = R.string.recipe_auto_fire_tips_content_right;
            }
            fireTip(s);
        } else {
            if (!is_vh_switching) {
                stoveUtils.setCookStep(cookStep);
                stoveUtils.setStep(step);
                stoveUtils.onStart();
                recipeDetailView.onfresh(step);
                onSpeakClick(step);
            }
            is_vh_switching = false;
            totalTime = cookStep.getParamByCodeName(stove.getDp(), "needTime");
            LogUtils.i("20171228", "totalTime::" + totalTime);
            currentTime = totalTime;
            startCountdown(currentTime);
        }
    }

    Timer timer;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (recipeDetailView == null){
                        return;
                    }
                    if (currentTime < 0)
                        return;
                    recipeDetailView.onfreshView((short) 15, step, (short) currentTime, (short) totalTime, false, 0);
                    if (currentTime < 10) {
                        if ((step + 1) != recipeDetailView.adapter.getCount())
                            recipeDetailView.onfreshViewForNext((short) 0, step + 1, (short) currentTime);
                    }
                    if (currentTime == 0) {
                        step += 1;
                        LogUtils.i("20171228", "step:" + (step + 1) + "totalStep:" + recipeDetailView.cardAdapter.getCount());
                        if (step == recipeDetailView.cardAdapter.getCount()) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            shutDownDevice();
                            cookFinish();
                            //  shutDownDevice();
                            //如果是最后一步
                            return;
                        } else {
                            recipeDetailView.onfresh(step);
                            onSpeakClick(step);
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            next();
                        }
                    }
                    currentTime--;
                    break;
                case 2:
                    if (recipeDetailView == null){
                        return;
                    }
                    step += 1;
                    if (step == recipeDetailView.cardAdapter.getCount()) {
                        return;
                    }
                    next();
                    break;
                default:
                    break;
            }
        }
    };

    public void startCountdown(int time) {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                myHandler.sendEmptyMessage(1);
            }
        }, 0, 1000);
    }

    private void cookFinish() {
        dialog.setTitleText(R.string.recipe_auto_cook_finish);
        dialog.setContentText(R.string.recipe_auto_cook_finish_txt);
        dialog.show();
        dialog.setOkBtn("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIService.getInstance().popBack();
            }
        });
        dialog.setCancelBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void fireTip(int s) {
        dialog1.setTitleText(R.string.recipe_auto_fire_tips);
        dialog1.setContentText(s);
        dialog1.show();
        dialog1.setCancelBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //步骤切换对话框
    public void stepToNext(int pos) {
        final int position = pos;
        dialog.setTitleText(R.string.recipe_auto_steptonext);
        dialog.setContentText(R.string.recipe_auto_steptonext_context);
        dialog.show();
        dialog.setCancelBtn(R.string.recipe_auto_enter, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (step == recipeDetailView.recipeStepShow.getCount()) {
                    prestep = step - 1;
                } else {
                    prestep = step;
                }
                recipeDetailView.onfreshViewRevert(prestep);
                step = position;
                recipeDetailView.onfresh(step);
                next();
                dialog.dismiss();
                //myHandler.sendEmptyMessage(1);
            }
        });
        dialog.setOkBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 语音切换步骤
     *
     * @param pos
     */
    public void stepToNext2(int pos) {

        if (step == recipeDetailView.recipeStepShow.getCount()) {
            prestep = step - 1;
        } else {
            prestep = step;
        }
        recipeDetailView.onfreshViewRevert(prestep);
        step = pos;
        recipeDetailView.onfresh(step);
        next();
    }

    @OnClick(R.id.recipe_quit)
    public void QuickWork() {
        dialog.setTitleText(R.string.is_stop_cook_recipe_out);
        dialog.setContentText(R.string.recipe_finish_work_context);
        dialog.show();
        dialog.setOkBtn(R.string.finish_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shutDownDevice();
                UIService.getInstance().popBack();
            }
        });
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void quickWork() {
        shutDownDevice();
        UIService.getInstance().popBack();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.show("灶具已离线，连接后才可进行自动烹饪", Toast.LENGTH_SHORT);
        }
    }


    boolean flag = true;

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20180227", "step::" + step);
        if (stoveHeadId == event.pojo.leftHead && event.pojo.leftHead.status != 0) {
            if (dialog1 != null && dialog1.isShow())
                dialog1.dismiss();

            if (flag) {
                flag = false;
                return;
            }

            if (cookStep.getDc().equals("")) {
                return;
            }

            if (TextUtils.equals(IRokiFamily.RQZ02, event.pojo.getDp())) {
                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {
                    if (TextUtils.equals(IRokiFamily.R9B39, event.pojo.getDt())) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if ((step - 1) < 0) {

                        } else {
                            recipeDetailView.onfreshViewRevert(step - 1);
                        }

                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                next();
                            }
                        }, 4000);
                    }
                }
            } else {
                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    LogUtils.i("20180228", "here is run");
                    if ((step - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(step - 1);
                    }
                    next();
                }
            }
            if (stoveHeadId.getStatus() == 0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                ToastUtils.show("灶具已关闭", Toast.LENGTH_SHORT);
            }
        }
        if (stoveHeadId == event.pojo.rightHead && event.pojo.rightHead.status != 0) {
            if (dialog1 != null && dialog1.isShow()) {
                dialog1.dismiss();
            }

            if (flag) {
                flag = false;
                return;
            }

            if (cookStep.getDc().equals("")) {
                return;
            }

            if (TextUtils.equals(IRokiFamily.RQZ02, event.pojo.getDp())) {
                if (TextUtils.equals(IRokiFamily.R9B39, event.pojo.getDt())) {
                    if (stoveHeadId.getStatus() == 1) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if ((step - 1) < 0) {

                        } else {
                            recipeDetailView.onfreshViewRevert(step - 1);
                        }
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                next();
                            }
                        }, 4000);
                    }
                }
            } else {
                if (stoveHeadId.getStatus() == 1) {
               /* if (timer!=null){
                    timer.cancel();
                    timer=null;
                }*/
                    LogUtils.i("20180228", "here is run");

                    if ((step - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(step - 1);
                    }
                    next();
                }
            }


            if (stoveHeadId.getStatus() == 0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                ToastUtils.show("灶具已关闭", Toast.LENGTH_SHORT);
            }

        }
    }

    int prestep;
    int press = 0;

    public void onClickCardView() {
        recipeDetailView.recipeStepShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i("20180102", "stepCard::" + step + " posi:::" + position + " stoveHeadId.getStatus():" + stoveHeadId.getStatus());
                if (step == position && cookStep.getDc().equals("")) {
                    if ((step + 1) == recipeDetailView.cardAdapter.getCount()) {
                        cookFinish();
                    } else {
                        if (stoveHeadId.getStatus() == 0 || stoveHeadId.getStatus() == 3) {
                            int s = 0;
                            if (stoveflag == 0) {
                                s = R.string.recipe_auto_fire_tips_content_left;
                            } else {
                                s = R.string.recipe_auto_fire_tips_content_right;
                            }
                            fireTip(s);
                        } else {
                            recipeDetailView.onfreshViewRevert(step);
                            myHandler.sendEmptyMessage(2);
                        }
                    }
                }

                if (step != position) {
//                    press++;
//                    if (press == 2) {
//                        press = 0;
                    stepToNext(position);
//                    }
                }
            }
        });
    }

    private void shutDownDevice() {
        stoveUtils.onFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        SpeechManager.getInstance().dispose();
        myHandler.removeCallbacks(runnable);
    }

    @Override
    public void onLeftClick(View view) {
        super.onLeftClick(view);
        showFloatingWindow();
    }

    @Override
    public void onRightClick(View view) {
        QuickWork();
    }

    /**
     * 提示
     */
    private void mesageDialog() {
//        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_27);
//        dialog.show();
//
//        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        BaseDialog baseDialog = new BaseDialog(cx);
        baseDialog.setContentView( cx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.common_dialog_recipe_type_27 : R.layout.common_dialog_recipe_type_27_h);
        baseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
            }
        } , R.id.common_dialog_ok_btn);
        baseDialog.show();
    }


    private static final String TAG = "RecipeCookPage";

    public static String RECIPE = "RECIPE";
    public static String NEXT = "下一步";
    public static String LAST = "上一步";
    public static String LAST_2 = "上一部";
    public static String AGAIN = "再来一次";
    public static String AGAIN_2 = "再说一遍";
    public static String PAUSE = "暂停工作";
    public static String END = "结束烹饪";
    public static String END_2 = "退出烹饪";
    /**
     * 语音识别
     */
    private ASREngine asrEngine;

    /**
     * 语音识别回调
     */
    private ASREngine.Callback callback = new ASREngine.Callback() {
        @Override
        public void beginningOfSpeech() {
            LogUtils.i(TAG, "beginningOfSpeech:");
        }

        @Override
        public void endOfSpeech() {
            LogUtils.i(TAG, "endOfSpeech:");
        }

        @Override
        public void bufferReceived(byte[] bytes) {
            LogUtils.i(TAG, "bufferReceived:" + bytes);
        }

        @Override
        public void partialResults(String s) {
            LogUtils.i(TAG, "partialResults:" + s);
            SpeechBean speechBean = new Gson().fromJson(s, SpeechBean.class);
            if (StringUtil.isEmpty(speechBean.getText()) && StringUtil.isEmpty(speechBean.getVar())) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speech();
                    }
                });

            }
        }

        @Override
        public void finalResults(String s) {
            LogUtils.i(TAG, "finalResults:" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String text = jsonObject.get("text").toString();
                setStep(text);
            } catch (JSONException e) {
                e.printStackTrace();
                setStep("唤醒");
            }
        }

        @Override
        public void error(String s) {
            LogUtils.i(TAG, "error:" + s);
            speech() ;
        }

        @Override
        public void rmsChanged(float v) {
            LogUtils.i(TAG, "rmsChanged:" + v);
        }
    };

    private void setStep(String speek) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (NEXT.equals(speek)) {
                        if (step == recipeDetailView.adapter.getCount() - 1) {
                            speech();
                            QuickWork();
                            return;
                        }
                        int position = step + 1;
                        stepToNext2(position);
                    } else if (LAST.equals(speek) || LAST_2.equals(speek)) {
                        if (step == 0) {
                            onSpeakClick("已经是第一步");
//                            speech();
                            return;
                        }
                        int position = step - 1;
                        stepToNext2(position);
                    } else if (AGAIN.equals(speek) || AGAIN_2.equals(speek)) {
//                    again();
//                    stepToNext2(step);
                        onSpeakClick(step);
                    } else if (END.equals(speek) || END_2.equals(speek)) {
                        quickWork();
                    } else {
                        speech();
                    }
                }catch (Exception e){
                    e.getMessage();
                    speech();
                }

            }
        });
    }

    /**
     * 语音识别
     */
    private void speakSearchRecipe() {
        try {
            if (ivYuyin.isChecked()) {
                if (asrEngine != null) {
                    asrEngine.startListening(callback);
                } else {
                    asrEngine = DDS.getInstance().getAgent().getASREngine();
                    asrEngine.startListening(callback);
                }
            }
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "DDSNotInitCompleteException " + e.toString());
        }
    }

    /**
     * 重新唤醒语音
     */
    private void speech() {
        tvRokiMessage.setText("你说，ROKI正在听...");
        if (ivRoki != null) {
            Glide.with(getActivity())
                    .load(R.drawable.ic_discern)
                    .into(ivRoki);
        }
        speakSearchRecipe();

    }

    //语音播报
    public void onSpeakClick(final int step) {
        if (!cbYinliang.isChecked()) {
            speech();
            return;
        }

        if (StringUtils.isNullOrEmpty(recipeDetailView.cookStepTemp.get(step).desc))
            return;
        try {
            onSpeakClick(recipeDetailView.cookStepTemp.get(step).desc);
//            tvRokiMessage.setText("ROKI正在说话，请稍后...");
//            Glide.with(getActivity())
//                    .load(R.drawable.ic_play)
//                    .into(ivRoki);
//            SpeechManager.getInstance().startSpeaking2(recipeDetailView.cookStepTemp.get(step).desc, new SpeechManager.SpeakComple() {
//                @Override
//                public void comple() {
//                    setStep("");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            setStep("唤醒");
        }
    }

    //语音播报
    public void onSpeakClick(final String message) {
        if (!cbYinliang.isChecked()) {
            speech();
            return;
        }

        try {
            tvRokiMessage.setText("ROKI正在说话，请稍后...");
            Glide.with(getActivity())
                    .load(R.drawable.ic_play)
                    .into(ivRoki);
            try {
                SpeechManager.getInstance().startSpeaking2(message, new SpeechManager.SpeakComple() {
                    @Override
                    public void comple() {
                        setStep("唤醒");
                    }
                });
//                if (ttsEngine == null){
//                    ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//                    ttsEngine.setMode( TTSEngine.LOCAL);
//                    ttsEngine.setSpeaker("gqlanfp");
//                }
//                ttsEngine.speak(message, 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
//                ttsEngine.setListener(new TTSEngine.Callback() {
//                    @Override
//                    public void beginning(String s) {
//                        LogUtils.i("TTSEngine" , "beginning--------" + s );
//                    }
//
//                    @Override
//                    public void received(byte[] bytes) {
//                        LogUtils.i("TTSEngine" , "received--------" + Arrays.toString(bytes));
//                    }
//
//                    @Override
//                    public void end(String s, int i) {
//                        LogUtils.i("TTSEngine" , "end--------" + s );
//                        setStep("唤醒");
//                    }
//
//                    @Override
//                    public void error(String s) {
//                        LogUtils.i("TTSEngine" , "error--------" + s );
//                        setStep("唤醒");
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
                setStep("唤醒");
            }
        } catch (Exception e) {
            e.printStackTrace();
            speech();
        }

    }

    public void showFloatingWindow() {
        Bundle bd = new Bundle();;
        bd.putSerializable("list", cookSteps);
        bd.putSerializable("stoveHeadId", stoveflag);
        EventUtils.postEvent(new FloatHelperEvent(id, bd, step, recipe!= null ? recipe.imgLarge : imgLarge, guid, 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        speech();
    }
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeSpeakBack".equals(event.getPageName())){
            QuickWork();
        }
    }
}
