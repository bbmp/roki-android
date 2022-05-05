package com.robam.roki.ui.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.legent.Callback;
import com.legent.events.ScreenPowerChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.services.ScreenPowerService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.events.OvenOtherEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.DeviceSendCommand;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.view.RecipeDetailAutoView;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.utils.DeviceSelectUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.SendCommandUtils;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by yinwei on 2017/11/27.
 * 自动菜谱old
 */

public class RecipeCookPage extends AbsDUIPage {
    //    @InjectView(R.id.recipe_quit)
//    ImageView recipeQuit;
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
    String guid;
    Long id;
    /**
     * 横竖屏切换
     */
    boolean is_vh_switching = false;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
//        cookSteps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
//        guid = getActivity().getIntent().getStringExtra("Guid");
//        iDevice = Plat.deviceService.lookupChild(guid);
//        View view = inflater.inflate(R.layout.recipe_auto_cooking, container, false);
//        ButterKnife.inject(RecipeCookPage.this, view);
//        initView();
//        return view;
//    }


    RecipeDetailAutoView recipeDetailView;
    DeviceSendCommand deviceSendCommand;//发送命令
    int prestep;
    private short totalSec = 0;
    private short currentSec = -1;
    boolean isHeatRecipe;

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    next();
                    break;
                case 15:
                    totalSec = 0;
                    currentSec = 0;
                    String dc = cookSteps.get(step).getDc();
                    LogUtils.i("202011131701", "cookSteps:" + cookSteps);
                    LogUtils.i("202011131701", "dc:" + dc);
                    isHeatRecipe = cookSteps.get(step).getNeedPreHeat();
                    if (dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        step -= 1;
                        next();

                    } else {
                        runSelectDevice(dc);
                    }

                    break;
                default:
                    break;
            }

        }
    };
    boolean isError = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isError) {
                LogUtils.i(TAG, "重新唤醒识别");
                setStep("重新唤醒识别");
                isError = false;
            }
            myHandler.postDelayed(this, 5000);
        }
    };
    /**
     * 主图
     */
    private String imgLarge;
    private TTSEngine ttsEngine;

    private void next() {
        if (currentSec < 0)
            return;
        if (step == recipeDetailView.adapter.getCount()) {
            return;
        }
        recipeDetailView.onfreshView(status, step, currentSec, totalSec, false, 0);
        if (currentSec > 0 && currentSec < 8) {
            if ((step + 1) != recipeDetailView.adapter.getCount()) {
                recipeDetailView.onfreshViewForNext((short) 0, step + 1, currentSec);
            }
        }
        if (currentSec == 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            totalSec = 0;
            currentSec = 0;
            step += 1;
            LogUtils.i("20180106", "step:" + step);
            if (step == recipeDetailView.adapter.getCount() && currentSec == 0) {
                cookFinish();
                return;
            }
            if (step == recipeDetailView.adapter.getCount()) {
                return;
            } else {
                String dc = cookSteps.get(step).getDc();
                isHeatRecipe = cookSteps.get(step).getNeedPreHeat();
                if (dc.equals("")) {
                    currentSec = 0;
                    totalSec = 0;
                    recipeDetailView.onfreshNoDeviceView(step);
                    onSpeakClick(step);
                    sendCommand.setStep(step);
                    sendCommand.setiDevice(iDevice);
                    deviceSendCommand.onFinish();
                    recipeDetailView.onfresh(step);
                    return;
                } else {
                    sendCommand.setStep(step - 1);
                    sendCommand.setiDevice(iDevice);
                    deviceSendCommand.onFinish();
                    selectDevice(dc);
                }
            }
        }
        currentSec--;
    }

    private void nextStep() {
        if ((step + 1) != recipeDetailView.adapter.getCount()) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    onSpeakClick(step);
                    sendCommand.setStep(step);
                    sendCommand.setiDevice(iDevice);
                    recipeDetailView.onfresh(step);
                    setOnStart();
                }
            }, 3000);
        } else {//最后一步
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    recipeDetailView.onfreshViewRevert(step - 1);
                    onSpeakClick(step);
                    sendCommand.setStep(step);
                    sendCommand.setiDevice(iDevice);
                    recipeDetailView.onfresh(step);
                    setOnStart();
                }
            }, 3000);
        }
    }

    private void setOnStart() {
        if (IRokiFamily.RC906.equals(iDevice.getDt())) {
            if (steameOvenOne != null) {
                if (steameOvenOne.alarm != 0) {
                    steamOvenOneAlarmStatus(steameOvenOne.alarm);
                    return;
                }
            }
            deviceSendCommand.onStart();
        } else {
            deviceSendCommand.onStart();
        }

    }


    private void selectDevice(String dc) {
        if (dc.contains(iDevice.getDc())) {
            nextStep();
            return;
        }
        revertFlag();
        List<String> dcList = DeviceSelectUtils.getInstance().dcSubString(dc);
        List<IDevice> listTemp = new ArrayList<>();
        listTemp = DeviceSelectUtils.getInstance().deviceIDev(dcList);
        if (listTemp.size() > 1) {
            Helper.newDeviceSelectNewOrientailDialog(cx, listTemp, new Callback<IDevice>() {
                @Override
                public void onSuccess(IDevice iDeviceTemp) {
                    iDevice = iDeviceTemp;
                    init(iDeviceTemp);
                    recipeDetailView.setiDevice(iDevice);
                    nextStep();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            iDevice = listTemp.get(0);
            recipeDetailView.setiDevice(iDevice);
            init(iDevice);
            nextStep();
        }
    }

    private void runSelectDevice(String dc) {
        if (dc.contains(iDevice.getDc())) {
            myHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    onSpeakClick(step);
                    sendCommand.setStep(step);
                    recipeDetailView.onfresh(step);
                    setOnStart();
                }
            }, 3000);
            return;
        }
        revertFlag();
        List<String> dcList = DeviceSelectUtils.getInstance().dcSubString(dc);
        List<IDevice> listTemp = new ArrayList<>();
        listTemp = DeviceSelectUtils.getInstance().deviceIDev(dcList);
        if (listTemp.size() > 1) {
            Helper.newDeviceSelectNewOrientailDialog(cx, listTemp, new Callback<IDevice>() {
                @Override
                public void onSuccess(IDevice iDeviceTemp) {
                    runSendCommand(iDeviceTemp);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            runSendCommand(listTemp.get(0));
        }
    }

    private void runSendCommand(IDevice iDevicetemp) {
        iDevice = iDevicetemp;
        recipeDetailView.setiDevice(iDevice);
        init(iDevice);
        onSpeakClick(step);
        sendCommand.setStep(step);
        sendCommand.setiDevice(iDevice);
        recipeDetailView.onfresh(step);
        setOnStart();
    }

    SendCommandUtils sendCommand;
    IRokiDialog dialog;

    @Override
    protected int getLayoutId() {
        Log.i("RecipeCookPage", "---------");

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
                getActivity().getIntent().putExtra("currentSec", currentSec);
                getActivity().getIntent().putExtra("hasHeat", hasHeat);
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
        //初始化語音播放組件
        try {
//            ttsEngine.setMode( TTSEngine.LOCAL);
//            ttsEngine.setSpeaker("gqlanfp");
            SpeechManager.getInstance().init(Plat.app);
            asrEngine = DDS.getInstance().getAgent().getASREngine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        myHandler.postDelayed(runnable, 5000);
        speakSearchRecipe();
    }

    @Override
    protected void initData() {
        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
        cookSteps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
        guid = getActivity().getIntent().getStringExtra("Guid");
        recipe = (Recipe) getActivity().getIntent().getSerializableExtra("recipe");
        step = getActivity().getIntent().getIntExtra("step", 0);
        is_vh_switching = getActivity().getIntent().getBooleanExtra("is_vh_switching", false);
        imgLarge = getActivity().getIntent().getStringExtra("imgLarge");
        currentSec = getActivity().getIntent().getShortExtra("currentSec", (short) -1);
        hasHeat = getActivity().getIntent().getBooleanExtra("hasHeat", true);

        iDevice = Plat.deviceService.lookupChild(guid);

        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
        SpeechManager.getInstance().init(Plat.app);//初始化語音播放組件
        recipeDetailView = new RecipeDetailAutoView(cx, cookSteps, iDevice, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1);
        viewAdd.addView(recipeDetailView);
        isHeatRecipe = cookSteps.get(step).getNeedPreHeat();
        sendCommand = new SendCommandUtils(cx, step, iDevice, recipeDetailView.recipeParamShowView);
        deviceSendCommand = new DeviceSendCommand(sendCommand);
        if (is_vh_switching) {
            is_vh_switching = false;
        } else {
            onSpeakClick(step);
            deviceSendCommand.onStart();
        }


        init(iDevice);
        onClickCardView();
    }

    boolean isTrain016 = false;
    boolean isTrain039 = false;
    boolean isTrain226 = false;
    boolean isTrain209 = false;
    boolean isTrain509 = false;
    boolean isTrain526 = false;
    boolean isTrain906 = false;
    boolean is228 = true;

    private void init(IDevice iDevice) {
        switch (iDevice.getDc()) {
            case DeviceType.RDKX:
                if (IRokiFamily.RR039.equals(iDevice.getDt())) {
                    isTrain039 = true;
                } else {
                    isTrain016 = true;
                }
                break;
            case DeviceType.RZKY:
                isTrain906 = true;
                break;
            case DeviceType.RZQL:
                if (IRokiFamily.RS209.equals(iDevice.getDt())) {
                    isTrain209 = true;
                } else {
                    if (IRokiFamily.RS228.equals(iDevice.getDt())) {
                        is228 = false;
                    } else {
                        isTrain226 = true;
                    }

                }
                break;
            case DeviceType.RWBL:
                if (IRokiFamily.RM509.equals(iDevice.getDt())) {
                    isTrain509 = true;
                } else {
                    isTrain526 = true;
                }
                break;
            default:
                break;
        }

    }

    private void revertFlag() {
        isTrain016 = false;
        isTrain039 = false;
        isTrain226 = false;
        isTrain209 = false;
        isTrain509 = false;
        isTrain526 = false;
    }

    int preStep = 0;
    long firstTime = 0;
    boolean alarm = false;

    public void onClickCardView() {
        recipeDetailView.recipeStepShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (alarm) {
                    ToastUtils.show(getCategory(iDevice) + "报警，恢复后才可烹饪", Toast.LENGTH_SHORT);
                    return;
                }

//                if (firstTime == 0) {//单击
//                    LogUtils.i("202012071149","单击：：：");
//                    firstTime = System.currentTimeMillis();
                if (step == position) {
                    singleOnClick(position);
                } else {
                    stepToNext(position);
                }


//                } else {//双击
//                    if (step == position) {
//                        firstTime = 0;
//                        return;
//                    }
//                    if (System.currentTimeMillis() - firstTime < 1000) {
//                        firstTime = 0;
//                        stepToNext(position);
//
//                    } else {
//                        firstTime = 0;
//                    }
//                }

            }
        });
    }

    private String getCategory(IDevice iDevice) {
        String s = null;
        if (DeviceType.RDKX.equals(iDevice.getDc())) {
            s = "电烤箱";
        } else if (DeviceType.RZQL.equals(iDevice.getDc())) {
            s = "蒸汽炉";
        } else if (DeviceType.RWBL.equals(iDevice.getDc())) {
            s = "微波炉";
        } else if (DeviceType.RZKY.equals(iDevice.getDc())) {
            s = "一体机";
        }

        return s;
    }

    //单击
    private void singleOnClick(int position) {
        if (step == position) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    heatFlag = false;
                }
            }, 2000);
            if ("RWBL".equals(iDevice.getDc())) {//微波炉的模式和其他的不同单独处理
                if (status == MicroWaveStatus.Pause) {
                    deviceSendCommand.onRestart();
                    firstTime = 0;
                } else if (status == MicroWaveStatus.Run) {
                    deviceSendCommand.onPause();
                    firstTime = 0;
                } else if (status == MicroWaveStatus.Wait) {
                    firstTime = 0;
                    String dc = null;
                    if (step == recipeDetailView.recipeStepShow.getCount()) {
                        dc = null;
                    } else {
                        dc = cookSteps.get(step).getDc();
                    }
                    if (dc != null && dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        totalSec = 0;
                        currentSec = 0;
                        recipeDetailView.onfreshViewRevert(step);
                    }
                    next();
                }
            } else if ("RZKY".equals(iDevice.getDc())) {

                LogUtils.i("202012071149", "单击：：：RZKY");
                if (steameOvenOne != null) {
                    if (steameOvenOne.alarm != 0) {
                        steamOvenOneAlarmStatus(steameOvenOne.alarm);
                        return;
                    }
                }

                LogUtils.i("202012071149", "单击2：：：status" + status);
                if (status == OvenStatus.Pause) {
                    if (flag906) {
                        deviceSendCommand.onStart();
                    } else {
                        deviceSendCommand.onRestart();
                    }
                    firstTime = 0;
                } else if (status == OvenStatus.Working || status == OvenStatus.PreHeat) {
                    LogUtils.i("202012071149", "单击2：：：pause");
                    deviceSendCommand.onPause();
                    firstTime = 0;
                } else if (status == SteamOvenOnePowerStatus.Off) {
                    firstTime = 0;
                    String dc = null;
                    if (step == recipeDetailView.recipeStepShow.getCount()) {
                        dc = null;
                    } else {
                        dc = cookSteps.get(step).getDc();
                    }
                    if (dc != null && dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        totalSec = 0;
                        currentSec = 0;
                        recipeDetailView.onfreshViewRevert(step);
                    }
                    next();
                }

            } else if ("RZQL".equals(iDevice.getDc())) {
                if (waterBoxState) {
                    ToastUtils.show("水箱已弹出，请检查水箱状态", Toast.LENGTH_SHORT);
                    return;
                }
                if (status == OvenStatus.Pause) {
                    deviceSendCommand.onRestart();
                    firstTime = 0;
                } else if (status == OvenStatus.Working || status == OvenStatus.PreHeat) {
                    deviceSendCommand.onPause();
                    firstTime = 0;
                } else if (status == SteamStatus.On) {
                    firstTime = 0;
                    String dc = null;
                    if (step == recipeDetailView.recipeStepShow.getCount()) {
                        dc = null;
                    } else {
                        dc = cookSteps.get(step).getDc();
                    }
                    if (dc != null && dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        totalSec = 0;
                        currentSec = 0;
                        recipeDetailView.onfreshViewRevert(step);
                    }
                    next();
                } else if (status == OvenStatus.Off) {
                    firstTime = 0;
                    String dc = null;
                    if (step == recipeDetailView.recipeStepShow.getCount()) {
                        dc = null;
                    } else {
                        dc = cookSteps.get(step).getDc();
                    }
                    if (dc != null && dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        totalSec = 0;
                        currentSec = 0;
                        recipeDetailView.onfreshViewRevert(step);
                    }
//                    UIService.getInstance().popBack();
                    next();
                }
            } else {
                if (status == OvenStatus.Pause) {
                    deviceSendCommand.onRestart();
                    firstTime = 0;
                } else if (status == OvenStatus.Working || status == OvenStatus.PreHeat) {
                    deviceSendCommand.onPause();
                    firstTime = 0;
                } else if (status == OvenStatus.Off) {
                    firstTime = 0;
                    String dc = null;
                    if (step == recipeDetailView.recipeStepShow.getCount()) {
                        dc = null;
                    } else {
                        dc = cookSteps.get(step).getDc();
                    }
                    if (dc != null && dc.equals("")) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        totalSec = 0;
                        currentSec = 0;
                        recipeDetailView.onfreshViewRevert(step);
                    }
                    next();
                }
            }

        }
    }

    private void cookFinish() {
        dialog.setTitleText(R.string.recipe_auto_cook_finish);
        dialog.setContentText(R.string.recipe_auto_cook_finish_txt);
        dialog.show();
        dialog.setOkBtn("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shutDownDevice();
                UIService.getInstance().popBack();
//                SpeechManager.getInstance().dispose();
            }
        });
        dialog.setCancelBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
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

                if (step == recipeDetailView.recipeStepShow.getCount()) {
                    prestep = step - 1;
                } else {
                    prestep = step;
                }

                recipeDetailView.onfreshViewRevert(prestep);
                deviceSendCommand.onFinish();
                try {
                    Thread.sleep(1400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                step = position;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                dialog.dismiss();
                myHandler.sendEmptyMessage(15);

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
     * 语音切换
     *
     * @param pos
     */
    public void stepToNext2(int pos) {
        final int position = pos;

        if (step == recipeDetailView.recipeStepShow.getCount()) {
            prestep = step - 1;
        } else {
            prestep = step;
        }

        recipeDetailView.onfreshViewRevert(prestep);
        deviceSendCommand.onFinish();
        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        step = position;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        dialog.dismiss();
        myHandler.sendEmptyMessage(15);

    }

    //    @OnClick(R.id.recipe_quit)
    public void QuickWork() {
        dialog.setTitleText(R.string.is_stop_cook_recipe_out);
        dialog.setContentText(R.string.recipe_finish_work_context);
        dialog.show();
        dialog.setOkBtn(R.string.finish_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                try {
                    dialog.dismiss();
                    shutDownDevice();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    UIService.getInstance().popBack();
                    getActivity().finish();
//                    SpeechManager.getInstance().dispose();
                }

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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        try {
            shutDownDevice();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UIService.getInstance().popBack();
            getActivity().finish();
        }
    }

    int step = 0;
    Timer timer;
    short status;
    boolean HasPreheat = false;

    //烤箱轮训
    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        String guid = event.pojo.getGuid().getGuid();
        String iDeviceGuid = iDevice.getGuid().getGuid();
        if (iDeviceGuid.equals(guid)) {
            if (isTrain039) {
                status = event.pojo.status;
                oven039StateShow(status, event.pojo.temp, event.pojo.setTime, event.pojo.time);
            } else {
                status = event.pojo.status;
                if (recipeDetailView.recipeParamShowView.isSlice) {
                    status = event.pojo.status2Values;
                    stateShow(status, event.pojo.currentTempDownValue, event.pojo.SetTime2Value, event.pojo.LeftTime2Value);
                } else {
                    stateShow(status, event.pojo.temp, event.pojo.setTime, event.pojo.time);
                }
            }
        }
    }

    AbsSteameOvenOne steameOvenOne;
    boolean flag906 = false;

    //一体机轮训
    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }

        String guid = event.pojo.getGuid().getGuid();
        String deviceGuid = iDevice.getGuid().getGuid();
        if (guid.equals(deviceGuid)) {
            steameOvenOne = event.pojo;
            if (isTrain906) {
                short powerStatusTemp = event.pojo.powerStatus;
                short powerNoStatus = event.pojo.powerOnStatus;
                short statusTemp = event.pojo.worknStatus;
                if ((powerStatusTemp == 1 && powerNoStatus == 0 && statusTemp == 255) || (powerStatusTemp == 1 && powerNoStatus == 255 && statusTemp == 255)) {
                    status = OvenStatus.Off;
                }
                if (powerStatusTemp == 2 && powerNoStatus == 3 && statusTemp == 1) {
                    status = OvenStatus.Working;
                }
                if (powerStatusTemp == 2 && powerNoStatus == 3 && statusTemp == 0) {
                    status = OvenStatus.PreHeat;
                }

                if (powerStatusTemp == 2 && powerNoStatus == 0 && statusTemp == 255 && steameOvenOne.alarm != 0) {
                    flag906 = true;
                    status = OvenStatus.Pause;
                }
                if (powerStatusTemp == 2 && powerNoStatus == 1 && statusTemp == 255) {
                    flag906 = false;
                    status = OvenStatus.Pause;
                }
                if (powerStatusTemp == 2 && powerNoStatus == 0 && statusTemp == 255) {
                    flag906 = false;
                    status = OvenStatus.complete;
                }
                stateShow(status, event.pojo.temp, event.pojo.setTime, event.pojo.leftTime);
            }
        }

    }

    boolean waterBoxState = false;

    //蒸箱轮训
    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        String deviceGuid = iDevice.getGuid().getGuid();
        String guid = event.pojo.getGuid().getGuid();
        if (deviceGuid.equals(guid)) {
            if (isTrain226) {
                if (event.pojo.waterboxstate == 0) {
                    waterBoxState = true;
                } else {
                    waterBoxState = false;
                }
                status = event.pojo.status;
                stateShow(status, event.pojo.temp, event.pojo.timeSet, event.pojo.time);
            } else {
                status = event.pojo.status;
                steam209StateShow(status, event.pojo.temp, event.pojo.timeSet, event.pojo.time);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    //微波炉轮训
    @Subscribe
    public void onEvent(MicroWaveStatusChangedEvent event) {

        String guid = event.pojo.getGuid().getGuid();
        String deviceGuid = iDevice.getGuid().getGuid();
        if (deviceGuid.equals(guid)) {
            if (isTrain509) {
                status = event.pojo.state;
                MicroStateShow(status, (short) 0, event.pojo.setTime, event.pojo.time);
            } else {
                status = event.pojo.state;
                MicroStateShow(status, (short) 0, event.pojo.setTime, event.pojo.time);
            }
        }
    }

    boolean sreenOn = false;

    @Subscribe
    public void onEvent(ScreenPowerChangedEvent event) {
        if (event.powerStatus == ScreenPowerService.OFF) {
            sreenOn = true;
        }
    }

    private void MicroStateShow(short status, short temp, short setTime, short time) {
        switch (status) {
            case MicroWaveStatus.Run:
                if (Math.abs((currentSec - time)) >= 0 && Math.abs((currentSec - time)) <= 5) {
                    totalSec = 0;
                }
                if (sreenOn) {
                    sreenOn = false;
                    totalSec = 0;
                }
                if (totalSec != 0) {
                    return;
                }
                totalSec = setTime;
                currentSec = time;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        myHandler.sendEmptyMessage(10);
                    }
                }, 0, 1000);
                break;
            case MicroWaveStatus.Pause:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    totalSec = 0;
                }
                recipeDetailView.onfreshView(status, step, (short) 1, setTime, heatFlag, 0);
                break;
            case MicroWaveStatus.Alarm:
                break;
            default:
                break;
        }
    }


    boolean heatFlag = false;


    private void stateShow(short status, short temp, short setTime, short time) {
        alarm = false;
        switch (status) {
            case OvenStatus.PreHeat:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                totalSec = 0;
                hasHeat = true;
                recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                break;
            case OvenStatus.Working:
                //旋转 或者重新进入 不做任何操作
                if (hasHeat && isHeatRecipe) {
                    deviceSendCommand.onPause();
                    hasHeat = false;
                    is_vh_switching = false;
                    heatFlag = true;
                    return;
                }
                if (is228) {
                    if (time / 60 == setTime) {
                        return;
                    }
                    if (Math.abs((currentSec - time)) >= 0 && Math.abs((currentSec - time)) <= 5) {
                        totalSec = 0;
                    }
                } else {

                }
                if (sreenOn) {
                    sreenOn = false;
                    totalSec = 0;
                }
                if (totalSec != 0) {
                    return;
                }
                totalSec = setTime;
                currentSec = time;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        myHandler.sendEmptyMessage(10);
                    }
                }, 0, 1000);
                break;
            case OvenStatus.Pause:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    totalSec = 0;
                }
                recipeDetailView.onfreshView(status, step, temp, setTime, heatFlag, 0);
                break;
            case OvenStatus.On:
            case OvenStatus.Off:
                if (status == 2) {
                    if ("RZQL".equals(iDevice.getDc())) {
                        this.status = 9;
                    } else {
                        status = 9;
                    }
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                totalSec = 0;
                currentSec = 0;
                if ("".equals(cookSteps.get(step).getDc())) {
                    recipeDetailView.onfreshNoDeviceView(step);
                } else {
                    recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                }
//                if (UIService.getInstance().)
//                UIService.getInstance().popBack();
                break;
            case SteamStatus.AlarmStatus:
            case OvenStatus.AlarmStatus:
                alarm = true;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                totalSec = 0;
                break;
            case OvenStatus.complete:
                recipeDetailView.onfreshViewRevert(step);
                break;
            default:
                break;
        }
    }

    short time209 = -1;
    boolean hasHeat = true;
    boolean isTiming = false;

    private void steam209StateShow(short status, short temp, short setTime, short time) {
        alarm = false;
        switch (status) {
            case OvenStatus.PreHeat:

                if ((time / 60) == setTime || time209 == time) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    hasHeat = false;
                    totalSec = 0;
                    recipeDetailView.onfreshView(OvenStatus.PreHeat, step, temp, setTime, false, 0);
                }

                time209 = time;
                break;
            case OvenStatus.Working:

                if ((time / 60) == setTime || time209 == time) {
                    if (!isTiming) {
                        currentSec = time;
                        isTiming = true;
                    }

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandler.sendEmptyMessage(10);
                        }
                    }, 0, 1000);


                } else {
                    if (Math.abs((currentSec - time)) <= 5) {
                        totalSec = 0;
                    }
                    if (totalSec != 0) {
                        return;
                    }
                    totalSec = setTime;
                    currentSec = time;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandler.sendEmptyMessage(10);
                        }
                    }, 0, 1000);

                }
                time209 = time;
                break;

            case OvenStatus.Pause:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    totalSec = 0;
                }
                recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                break;
            case OvenStatus.On:
            case OvenStatus.Off:
                if (status == 2) {
                    status = 10;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                totalSec = 0;
                currentSec = 0;
                if ("".equals(cookSteps.get(step).getDc())) {
                    recipeDetailView.onfreshNoDeviceView(step);
                } else {
                    recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                }
                break;
            case SteamStatus.AlarmStatus:
                alarm = true;
                break;
            default:
                break;
        }
    }

    private void oven039StateShow(short status, short temp, short setTime, short time) {
        alarm = false;
        switch (status) {
            case OvenStatus.Working:
                if ((time / 60) == setTime) {
                    recipeDetailView.onfreshView(OvenStatus.PreHeat, step, temp, setTime, false, 0);
                } else {
                    if (Math.abs((currentSec - time)) >= 0 && Math.abs((currentSec - time)) <= 5) {
                        totalSec = 0;
                    }
                    if (totalSec != 0) {
                        return;
                    }
                    totalSec = setTime;
                    currentSec = time;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myHandler.sendEmptyMessage(10);
                        }
                    }, 0, 1000);
                }
                break;
            case OvenStatus.Pause:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    totalSec = 0;
                }
                recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                break;
            case OvenStatus.Off:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                totalSec = 0;
                currentSec = 0;
                if ("".equals(cookSteps.get(step).getDc())) {
                    recipeDetailView.onfreshNoDeviceView(step);
                } else {
                    recipeDetailView.onfreshView(status, step, temp, setTime, false, 0);
                }
                break;
            case OvenStatus.AlarmStatus:
                alarm = true;
                break;
            default:
                break;
        }
    }


    private void shutDownDevice() {
        deviceSendCommand.onFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firstTime = 0;
        SpeechManager.getInstance().dispose();
        myHandler.removeCallbacks(runnable);
    }


    IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警
    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警

    private void steamOvenOneAlarmStatus(short alarm) {

        if (alarm != 0) {
            switch (alarm) {
                case 128:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_temp_unusual_content);
                    centerOneBtnListener();
                    break;
                case 16:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
                    centerOneBtnListener();
                    break;
                case 8:
                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_heat_elimination_fan_content);
                    makePhoneCallListenr();
                    break;
                case 2:
                case 4:
                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_temp_sensor_content);
                    makePhoneCallListenr();
                    break;
                case 1:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_steamOvenOne_warm_tips);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_content);
                    centerOneBtnListener();
                    break;
                case 9:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_c906_temp_sensor_content);
                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_content);
                    centerOneBtnListener();
                    break;
                case 10:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_steamOvenOne_warm_tips);
                    iRokiDialogAlarmType_02.setContentText("高温报警");
                    centerOneBtnListener();
                    break;
                case 11:
                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_steamOvenOne_warm_tips);
                    iRokiDialogAlarmType_02.setContentText("加热故障");
                    centerOneBtnListener();
                    break;
            }
        }

    }

    private void centerOneBtnListener() {
        iRokiDialogAlarmType_02.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_02.dismiss();
            }
        });
        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_02.show();
    }


    //拨打电话
    private void makePhoneCallListenr() {
        iRokiDialogAlarmType_01.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn(R.string.can_good, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_01.show();
    }

    @Subscribe
    public void onEvent(OvenOtherEvent event) {
        if (event.pojo == null) {
            return;
        }

        String guid = event.pojo.getGuid().getGuid();
        String deviceGuid = iDevice.getGuid().getGuid();
        if (guid.equals(deviceGuid)) {
            if (event.pojo.eventId == 22) {
                if (iRokiDialogAlarmType_02 == null) {
                    iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(getActivity(), DialogUtil.DIALOG_TYPE_02);
                }
                iRokiDialogAlarmType_02.setTitleText("蒸汽提醒");
                iRokiDialogAlarmType_02.setTitleAralmCodeText("");
                iRokiDialogAlarmType_02.setContentText("无法开始工作，请移至产品端进行操作。");
                centerOneBtnListener();
                return;
            }
            if (event.pojo.eventId == 23) {
                if (iRokiDialogAlarmType_02 == null) {
                    iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(getActivity(), DialogUtil.DIALOG_TYPE_02);
                }
                iRokiDialogAlarmType_02.setTitleText("温度过高");
                iRokiDialogAlarmType_02.setTitleAralmCodeText("");
                iRokiDialogAlarmType_02.setContentText("温度过高，请打开门冷却后再开始工作。");
                centerOneBtnListener();
                return;
            }
        }
    }


    @Override
    public void onLeftClick(View view) {
        showFloatingWindow();
        super.onLeftClick(view);
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
        baseDialog.setContentView(cx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.common_dialog_recipe_type_27 : R.layout.common_dialog_recipe_type_27_h);
        baseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDialog.dismiss();
            }
        }, R.id.common_dialog_ok_btn);
        baseDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        speech();
        LogUtils.i("RecipeCookPage", "-------- onResume");
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i("RecipeCookPage", "-------- onConfigurationChanged");
    }


    private static final String TAG = "RecipeCookPage";

    public static String RECIPE = "RECIPE";
    public static String NEXT = "下一步";
    public static String LAST = "上一步";
    public static String LAST_2 = "上一部";
    public static String AGAIN = "再来一次";
    public static String AGAIN_2 = "再说一遍";
    public static String PAUSE = "暂停";
    public static String END = "结束";
    public static String END_2 = "退出";
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
//                if (NEXT.equals(text)
//                ) {
////                    int position = step + 1;
////                    stepToNext(position);
//                    setStep(text);
//                } else if (LAST.equals(text) || LAST_2.equals(text)) {
////                    int position = step - 1;
////                   stepToNext(position);
//                    setStep(text);
//                } else if (AGAIN.equals(text) || AGAIN_2.equals(text)) {
////                    again();
////                    singleOnClick(step);
//                    setStep(text);
//                } else if (text.contains(PAUSE)) {
////                    again();
////                    singleOnClick(step);
//                    setStep(text);
//                } else if (text.contains(END) || text.contains(END_2) ) {
////                    again();
////                    singleOnClick(step);
//                    setStep(text);
//                } else {
//                    setStep("");
//                }
            } catch (JSONException e) {
                e.printStackTrace();
                setStep("唤醒");
            }
        }

        @Override
        public void error(String s) {
            LogUtils.i(TAG, "error:" + s);
            setStep("唤醒");
            isError = true;
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
                            onSpeakClick("已经到最后一步");
                            return;
                        }
                        int position = step + 1;
                        stepToNext2(position);
                    } else if (speek.contains(LAST) || speek.contains(LAST_2)) {
                        if (step == 0) {
                            onSpeakClick("已经是第一步");
                            return;
                        }
                        int position = step - 1;
                        stepToNext2(position);
                    } else if (AGAIN.equals(speek) || AGAIN_2.equals(speek)) {
                        onSpeakClick(step);
                    } else if (speek.contains(PAUSE)) {
                        if (status == OvenStatus.Pause) {
                            onSpeakClick("已经暂停");
                            return;
                        }
                        singleOnClick(step);
                        speech();
                    } else if (speek.contains(END) || speek.contains(END_2)) {
                        quickWork();
                    } else {
                        speech();
                    }
                } catch (Exception e) {
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
        if (tvRokiMessage == null){
            return;
        }
        tvRokiMessage.setText("你说，ROKI正在听...");
//        if (ivRoki != null){
        Glide.with(getActivity())
                .load(R.drawable.ic_discern)
                .into(ivRoki);
//        }
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
//                    setStep("唤醒语音识别");
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
//                if (ttsEngine == null){
//                    ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//                    ttsEngine.setMode( TTSEngine.CLOUD);
//                    ttsEngine.setSpeaker("gqlanfp");
//                }

                SpeechManager.getInstance().startSpeaking2(message, new SpeechManager.SpeakComple() {
                    @Override
                    public void comple() {
                        setStep("唤醒");
                    }
                });
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
        Bundle bd = new Bundle();
        bd.putSerializable("list", cookSteps);
        EventUtils.postEvent(new FloatHelperEvent(id, bd, step, recipe != null ? recipe.imgLarge : imgLarge, guid, 1));
    }
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeSpeakBack".equals(event.getPageName())){
            QuickWork();
        }
    }
}
