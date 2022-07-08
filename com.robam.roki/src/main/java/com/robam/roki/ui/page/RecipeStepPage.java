//package com.robam.roki.ui.page;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import androidx.viewpager.widget.ViewPager;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.common.collect.Lists;
//import com.google.common.eventbus.Subscribe;
//import com.legent.Callback;
//import com.legent.Callback2;
//import com.legent.VoidCallback;
//import com.legent.dao.DaoHelper;
//import com.legent.plat.Plat;
//import com.legent.plat.events.DeviceConnectionChangedEvent;
//import com.legent.plat.pojos.device.AbsDevice;
//import com.legent.plat.pojos.device.IDevice;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.adapters.ExtPageAdapter;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.DisplayUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.speech.SpeechManager;
//import com.robam.common.Utils;
//import com.robam.common.events.MicroWaveStatusChangedEvent;
//import com.robam.common.events.OvenStatusChangedEvent;
//import com.robam.common.events.SteamAlarmEvent;
//import com.robam.common.events.SteamOvenOneStatusChangedEvent;
//import com.robam.common.events.SteamOvenStatusChangedEvent;
//import com.robam.common.io.device.MsgParams;
//import com.robam.common.paramCode;
//import com.robam.common.pojos.CookStep;
//import com.robam.common.pojos.CookStepDetails;
//import com.robam.common.pojos.DeviceType;
//import com.robam.common.pojos.Recipe;
//import com.robam.common.pojos.device.IRokiFamily;
//import com.robam.common.pojos.device.Oven.AbsOven;
//import com.robam.common.pojos.device.Oven.Oven039;
//import com.robam.common.pojos.device.Oven.OvenStatus;
//import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
//import com.robam.common.pojos.device.Steamoven.Steam209;
//import com.robam.common.pojos.device.Steamoven.SteamStatus;
//import com.robam.common.pojos.device.microwave.AbsMicroWave;
//import com.robam.common.pojos.device.microwave.MicroWaveM509;
//import com.robam.common.pojos.device.microwave.MicroWaveM526;
//import com.robam.common.pojos.device.microwave.MicroWaveStatus;
//import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
//import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
//import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
//import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//import com.robam.roki.factory.RokiDialogFactory;
//import com.robam.roki.listener.IRokiDialog;
//import com.robam.roki.ui.Helper;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.RecipeUtil;
//import com.robam.roki.ui.dialog.BlackPromptConfirmAndCancleDialog;
//import com.robam.roki.ui.dialog.BlackPromptConfirmDialog;
//import com.robam.roki.ui.dialog.SteamOvenSensorBrokeDialog;
//import com.robam.roki.ui.dialog.SteamOvenWarningDialog;
//import com.robam.roki.ui.view.RecipeStepCountDownView;
//import com.robam.roki.ui.view.RecipeStepDetailView;
//import com.robam.roki.utils.DialogUtil;
//import com.robam.roki.utils.StringConstantsUtil;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
//import static android.R.attr.type;
//import static android.widget.RelativeLayout.CENTER_IN_PARENT;
//
///**
// * Created by Rent on 2016/7/13.
// */
//public class RecipeStepPage extends BasePage implements ViewPager.OnPageChangeListener {
//    private long recipeId = -100;//菜谱ID
//    // private Recipe recipe;//菜谱详情数据
//    public ExtPageAdapter adapter;
//    @InjectView(R.id.recipe_step_quit)//退出按鈕
//    public ImageView recipe_step_quit;
//    @InjectView(R.id.recipe_step_return)//返回按鈕
//    public ImageView recipe_step_return;
//    @InjectView(R.id.recipe_step_pager)//viewpager
//    public ViewPager recipe_step_pager;
//    @InjectView(R.id.recipe_step_button)
//    public RecipeStepCountDownView recipe_step_button;
//    public RecipeStepDetailView working_recipeStepDetailView;//当前RecipeStepDetailView
//    public int current_PageItemIndex;//当前viewpage index
//    public int working_PageItemIndex;//处于开始、暂停、倒计时状态，页面index
//    public int current_stepButtonStatus = -1;//步骤按钮状态
//
//    public StringBuffer noDevice_Prompt = new StringBuffer("当前帐号无相应 ");
//    private final String DEVICEOFF_STR = "设备与手机断开连接";
//    final int textsize = 20;
//    private boolean isClickParamSetButton = false;
//    String typeTemp;
//    private CookStep step;
//    private AbsDevice device;
//    private String valueName;
//    private IRokiDialog rokigatingdialog;
//    private long nextInMillis;
//    private long backStartInMillis;
//    private long startInMillis;
//    List<CookStepDetails> stepDetailses = new ArrayList<>();
//    private int stepIndex;
//    private boolean isScrolling = false;
//    private int lastValue = -1;
//    private List<CookStep> steps;
//    private AbsMicroWave microW;
//    StoreService ss = StoreService.getInstance();
//    private Recipe recipe;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        recipeId = getArguments().getLong(PageArgumentKey.RecipeId);
//        try {
//            recipe = DaoHelper.getDao(Recipe.class).queryForId(recipeId);
//            steps = recipe.getJs_cookSteps();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        LogUtils.i("20171212", "recipeId:" + recipeId);
//        if (inflater == null) {
//            inflater = LayoutInflater.from(cx);
//        }
//        View contentView = inflater.inflate(R.layout.page_recipe_step_viewpage, container, false);
//        ButterKnife.inject(this, contentView);
//        adapter = new ExtPageAdapter();//构建adapter
//        recipe_step_pager.addOnPageChangeListener(this);
//        recipe_step_pager.setAdapter(adapter);
//        recipe_step_pager.setOffscreenPageLimit(10);
//
//        // recipe = (Recipe) getArguments().getSerializable(PageArgumentKey.Recipe);
//        // type = getArguments().getString(PageArgumentKey.Guid);
//        //recipe 数据未获取
//        initView();
//        //获取菜谱中模式
//
//        for (int i = 0; i < steps.size(); i++) {
//            step = steps.get(i);
//            Map<String, paramCode> map = RecipeUtil.getRecipeDefaultStepParam(step);
//            if (map != null && map.size() != 0) {
//                setParamLinear(map);
//            }
//        }
//        device = Utils.getDefaultOven();
//        startInMillis = Calendar.getInstance().getTimeInMillis();
//        //nextCookStepControl(0);
//        return contentView;
//    }
//
//
//    private void setParamLinear(Map<String, paramCode> map) {
//
//        if (DeviceType.RDKX.equals(step.dc)) {
//            paramCode paramCode = map.get(MsgParams.OvenMode);
//            valueName = paramCode.valueName;
//            LogUtils.i("20170711", "valueName:" + valueName);
//        }
//    }
//
//    public void initView() {
//        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
//        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
//        SpeechManager.getInstance().init(Plat.app);//初始化語音播放組件
//        List<View> list = Lists.newArrayList();
//        LogUtils.i("20171212", "stepLLL" + steps);
//        if (steps != null && steps.size() > 0) {//遍历创建菜谱view，并封装adpter
//            StringBuffer stringBuffer = new StringBuffer();
//            for (int i = 0; i < steps.size(); i++) {
//              /*  if (steps.get(i).dc.equals("RRQZ")) {
//                    continue;
//                }*/
//                RecipeStepDetailView recipeStepDetailView = new RecipeStepDetailView(cx, steps.get(i), steps, i);
//                list.add(recipeStepDetailView);
//
//                //提示当前账户设备情况
//                LogUtils.i("20171115", "recipeStepDetailView.hasCategory:" + recipeStepDetailView.hasCategory);
//            }
//            adapter.loadViews(list);
//        } else {
//            exitAndToastExc("菜谱数据异常");//菜谱若无步骤，报异常并退出
//            return;
//        }
//        //初始化第一张页面
//        //设置滑动监听和viewpage当前数据
//        recipe_step_pager.setCurrentItem(0);
//        // dialogSelect();
//        initFalse();
//        RecipeStepDetailView recipeStepDetailView = (RecipeStepDetailView) adapter.getViews().get(0);
//        //   judgeDevice(recipeStepDetailView);
//        LogUtils.i("20171117", "hasCategory:" + recipeStepDetailView.hasCategory + "isUserHasDevice:" + recipeStepDetailView.isUserHasDevice
//                + "isCurrentDeviceHasPlat:" + recipeStepDetailView.isCurrentDeviceHasPlat);
//        if (recipeStepDetailView.hasCategory && recipeStepDetailView.isUserHasDevice
//                && recipeStepDetailView.isCurrentDeviceHasPlat) {//判断菜谱有无品类
//            current_stepButtonStatus = StepButton.preset;
//            setStepButton_Preset();
//        } else {
//            current_stepButtonStatus = StepButton.none;
//            setStepButton_None();
//        }
//        recipeStepDetailView.onSpeakClick();//自动阅读
//        rokigatingdialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
//    }
//
//
//    private void judgeDevice(RecipeStepDetailView recipeStepDetailView) {
//        LogUtils.i("20171117", "judgeDevice:" + recipeStepDetailView.category);
//        if (recipeStepDetailView.category.contains("||")) {
//            List<IDevice> listDeivce = recipeStepDetailView.splitMethod(recipeStepDetailView.category);
//            switch (listDeivce.size()) {
//                case 0:
//                    ToastUtils.show("当前无相应的设备", Toast.LENGTH_SHORT);
//                    break;
//                case 1:
//                    LogUtils.i("20171117", "listDevice====1");
//                    recipeStepDetailView.setCategory(listDeivce.get(0).getDc());
//                    //   recipeStepDetailView.setParamView(listDeivce.get(0),2);
//                    setSendPreMsg(recipeStepDetailView);
//                    typeTemp = recipeStepDetailView.type;
//                    break;
//                default:
//                    dialogSelect(listDeivce, recipeStepDetailView);
//                    break;
//            }
//        } else {
//            LogUtils.i("20171117", "recipeStepDetailView.cookStep.dc:" + recipeStepDetailView.cookStep.dc);
//            if (recipeStepDetailView.cookStep.dc.contains("||")) {
//                LogUtils.i("20171117", "iDeviceTemp:" + (iDeviceTemp == null));
//                recipeStepDetailView.setParamView(iDeviceTemp, 2);
//            } else {
//                // recipeStepDetailView.setParamView(null,1);
//                setSendPreMsg(recipeStepDetailView);
//            }
//           /* if (recipeStepDetailView.hasCategory && recipeStepDetailView.isUserHasDevice
//                    && recipeStepDetailView.isCurrentDeviceHasPlat) {//判断菜谱有无品类
//                current_stepButtonStatus = StepButton.preset;
//                setStepButton_Preset();
//            } else {
//                current_stepButtonStatus = StepButton.none;
//                setStepButton_None();
//            }*/
//            typeTemp = recipeStepDetailView.type;
//        }
//    }
//
//    IDevice iDeviceTemp;
//
//    private void dialogSelect(List<IDevice> listselect, final RecipeStepDetailView recipeStepDetailView) {
//        Helper.newSelectDialog(cx, listselect, new Callback<IDevice>() {
//            @Override
//            public void onSuccess(IDevice iDevice) {
//                LogUtils.i("20171115", "idvice:" + iDevice);
//                if (iDevice != null) {
//                    LogUtils.i("20171103", "iDevice::::" + iDevice.getID() + " " +
//                            iDevice.getDc() + " " + iDevice.getDt() + " " + iDevice.getDp() +
//                            "  " + iDevice.getGuid() + " " + iDevice.getName() + " "
//                    );
//                    iDeviceTemp = iDevice;
//                    typeTemp = iDevice.getDt();
//                    LogUtils.i("20171117", "typeTemp:" + typeTemp);
//                    recipeStepDetailView.setCategory(iDevice.getDc());
//                    recipeStepDetailView.setParamView(iDevice, 2);
//                    setSendPreMsg(recipeStepDetailView);
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
//
//    /**
//     * 异常退出并提示
//     */
//    private void exitAndToastExc(String string) {
//        if (string == null)
//            string = "";
//        ToastUtils.show(string, Toast.LENGTH_SHORT);
//        UIService.getInstance().popBack();
//    }
//
//    /**
//     * 返回按鈕
//     */
//    @OnClick(R.id.recipe_step_return)
//    public void onReturnClick() {
//        if (current_PageItemIndex > 0)
//            recipe_step_pager.setCurrentItem(current_PageItemIndex - 1);
//    }
//
//    /**
//     * 退出按鈕
//     */
//    @OnClick(R.id.recipe_step_quit)
//    public void onQuitClick() {
//        if (isClickParamSetButton) {
//            final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
//            dialog.setTitleText(R.string.cooking_finish);
//            dialog.setContentText(R.string.cooking_complete_content);
//            dialog.show();
//            dialog.setOkBtn(R.string.bask_in_cooking, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //nextCookStepControl(stepIndex);
//                    Activity atv = UIService.getInstance().getMain().getActivity();
//                    RecipeShowPage.showCooking(atv, recipeId);
//                    dialog.dismiss();
//                }
//            });
//            dialog.setCanBtnTextColor(R.color.gray);
//            dialog.setCancelBtn(R.string.refused_to, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //   nextCookStepControl(stepIndex);
//                    switch (current_stepButtonStatus) {
//                        case StepButton.none:
//                            dialog.dismiss();
//                            UIService.getInstance().popBack();
//                            break;
//                        default:
//                            shutDownDevice();
//                            dialog.dismiss();
//                            UIService.getInstance().popBack();
//                    }
//                }
//            });
//
//        } else {
//            final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
//            dialog.setTitleText(R.string.is_stop_cook_recipe_out);
//            dialog.show();
//            dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //nextCookStepControl(stepIndex);
//                    dialog.dismiss();
//                    UIService.getInstance().popBack();
//                }
//            });
//            dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//        }
//    }
//
//   /* //上报数据
//    private void cookSetpData() {
//        long endMillis = Calendar.getInstance().getTimeInMillis();
//        boolean isBroken = steps.size()== stepIndex + 1 ? false : true;
//        List<Dc> js_dcs = recipe.getJs_dcs();
//        String deviceGuid = null;
//        for (Dc dc: js_dcs){
//            if ((IDeviceType.RZQL.equals(dc.getName()))){
//                if (steam == null){
//                    steam = Utils.getDefaultSteam();
//                }
//                if (steam != null){
//                    deviceGuid = steam.getID();
//                }
//            }else if (IDeviceType.RDKX.equals(dc.getName())){
//                if (oven == null){
//                    oven = Utils.getDefaultOven();
//                }
//                if (oven != null){
//                    deviceGuid = oven.getID();
//                }
//            }else if (IDeviceType.RWBL.equals(dc.getName())){
//                if (microW == null){
//                    microW = Utils.getDefaultMicrowave();
//                }
//                if (microW != null){
//                    deviceGuid = microW.getID();
//                }
//            }
//        }
//        if (deviceGuid != null){
//            LogUtils.i("20170914","deviceGuid:"+deviceGuid);
//            CookbookManager.getInstance().addCookingLog_New(deviceGuid, recipe, steps.size(), IAppType.RKDRD, startInMillis, endMillis,
//                    isBroken, stepDetailses, new VoidCallback() {
//                        @Override
//                        public void onSuccess() {
//                        }
//                        @Override
//                        public void onFailure(Throwable t) {
//                        }
//                    });
//        }
//        nextInMillis =0;
//        backStartInMillis = 0;
//    }*/
//
//    short tempOff;
//
//    private void shutDownDevice() {
//        if (IRokiFamily.RC906.equals(typeTemp)) {
//            tempOff = SteamOvenOnePowerStatus.RecipeOff;
//        } else {
//            tempOff = RecipeUtil.OFF;
//        }
//        RecipeUtil.setDeviceStatusModel(cx, tempOff, RecipeStepPage.this, new Callback<Integer>() {
//            @Override
//            public void onSuccess(Integer n) {
//                if (n == 0) {
//                } else if (n == 1) {
//                } else if (n == 2) {
//                } else if (n == 3) {
//                    ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        LogUtils.i("20161024", keyCode + "///" + event.getAction());
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                return true;
//            default:
//                return super.onKeyDown(keyCode, event);
//        }
//    }
//
//    private boolean button_lock;
//
//    /**
//     * 步骤按钮点击
//     */
//    @OnClick(R.id.recipe_step_button)
//    public void onStepButtonClick() {
//        if (button_lock) return;
//        if (device != null) {
//            if ("RR039".equals(device.getDt()) && "EXP".equals(valueName)) {
//                ToastUtils.show(R.string.device_exp_model, Toast.LENGTH_SHORT);
//                return;
//            }
//        }
//
//        LogUtils.i("20171110", "current_stepButtonStatus: " + current_stepButtonStatus + " type:" + type);
//
//        switch (current_stepButtonStatus) {
//            case StepButton.preset:
//                working_recipeStepDetailView = (RecipeStepDetailView) adapter.getViews().get(current_PageItemIndex);
//                working_recipeStepDetailView.setCategory(steps.get(current_PageItemIndex).getDc());
//                LogUtils.i("20171117", "working_recipeStepDetailView:" + working_recipeStepDetailView.category);
//                judgeDevice(working_recipeStepDetailView);
//                break;
//            case StepButton.start:
//                LogUtils.i("20171110", "start:" + StepButton.start);
//                setStartCommmand();
//                break;
//            case StepButton.countDown:
//                setPauseCommand();
//                break;
//            case StepButton.pause:
//                setWorkingCommand();
//                break;
//            case StepButton.alarm:
//                ToastUtils.show("设备处于报警状态，请处理", Toast.LENGTH_SHORT);
//                break;
//            case StepButton.preheat:
//                Callback<Integer> callBack = new Callback<Integer>() {
//                    @Override
//                    public void onSuccess(Integer n) {
//                        if (n == 0) {
//                        } else if (n == 1) {
//                            ToastUtils.show("设备通讯异常", Toast.LENGTH_SHORT);
//                        } else if (n == 2) {
//
//                        } else if (n == 3) {
//                            ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                        } else if (n == 4) {
//                            ToastUtils.show("下发指令失败，请检查设备", Toast.LENGTH_SHORT);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                    }
//                };
//                if (IRokiFamily.RC906.equals(typeTemp)) {
//                    setPreSetParamZKY(working_recipeStepDetailView.type, SteamOvenOnePowerOnStatus.Pause, callBack);
//                } else {
//                    setPreSetParamNew(working_recipeStepDetailView.type, OvenStatus.Pause, callBack);
//                }
//        }
//    }
//
//
//    short tempCommand;
//    short tempPause;
//    short tempWork;
//
//    //倒计时暂停指令
//    private void setPauseCommand() {
//        if (IRokiFamily.RC906.equals(typeTemp)) {
//            LogUtils.i("20171122", "906::");
//            tempPause = 1;
//        } else {
//            tempPause = RecipeUtil.PAUSE;
//        }
//        RecipeUtil.setDeviceStatusModel(cx, tempPause, this, new Callback<Integer>() {
//            @Override
//            public void onSuccess(Integer n) {
//                if (n == 0) {
//
//                } else if (n == 1) {
//                    ToastUtils.show(R.string.device_communication_Failure_text, Toast.LENGTH_SHORT);
//
//                } else if (n == 2) {
////                           promptDialog();
////                            setInitStatus();
//                } else if (n == 3) {
//                    ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                } else if (n == 4) {
//                    ToastUtils.show(R.string.device_Throwable_text, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });
//    }
//
//    //暂停时发送工作指令
//    private void setWorkingCommand() {
//        if (IRokiFamily.RC906.equals(typeTemp)) {
//            LogUtils.i("20171122", "906::");
//            if (steamAndOven.alarm != 0) {
//                steamOvenOneAlarmStatus(steamAndOven.alarm);
//                return;
//            }
//            tempWork = 3;
//        } else {
//            tempWork = RecipeUtil.WORKING;
//        }
//        RecipeUtil.setDeviceStatusModel(cx, tempWork, this, new Callback<Integer>() {
//            @Override
//            public void onSuccess(Integer n) {
//                hasPause = false;
//                flag = 0;
//                if (n == 0) {
//                } else if (n == 1) {
//                    ToastUtils.show(R.string.device_communication_Failure_text, Toast.LENGTH_SHORT);
//                } else if (n == 2) {
////                            promptDialog();
////                            setInitStatus();
//                } else if (n == 3) {
//                    ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                } else if (n == 4) {
//                    ToastUtils.show(R.string.device_Throwable_text, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
//
//    boolean flagAlram = true;
//
//    //开始指令下发
//    private void setStartCommmand() {
//        LogUtils.i("20171122", "906::" + typeTemp);
//        if (IRokiFamily.RC906.equals(typeTemp)) {
//            if (steamAndOven != null) {
//                if (steamAndOven.alarm != 0) {
//                    steamOvenOneAlarmStatus(steamAndOven.alarm);
//                    return;
//                }
//            }
//            tempCommand = 3;
//        } else {
//            tempCommand = RecipeUtil.WORKING;
//        }
//
//        RecipeUtil.setDeviceStatusModel(cx, tempCommand, this, new Callback<Integer>() {
//            @Override
//            public void onSuccess(Integer n) {
//                LogUtils.i("20171110", "n:" + n);
//                if (n == 0) {
//                    flag906 = true;
//                    if (steamAndOven != null) {
//                        if (steamAndOven.alarm != 0) {
//                            steamOvenOneAlarmStatus(steamAndOven.alarm);
//                        }
//                    }
//
//                    setDeviceTrainLock(working_recipeStepDetailView.type, true);
//                    //预设开始约两秒之内不让其再次点击
//                    button_lock = true;
//                    myHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            button_lock = false;
//                        }
//                    }, 2000);
//                } else if (n == 1) {
//                    ToastUtils.show(R.string.device_communication_Failure_text, Toast.LENGTH_SHORT);
//                    setDeviceTrainLock(working_recipeStepDetailView.type, true);
//                } else if (n == 2) {
////                            promptDialog();
////                            setInitStatus();
//                } else if (n == 3) {
//                    ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                } else if (n == 4) {
//                    ToastUtils.show(R.string.device_Throwable_text, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//            }
//        });
//    }
//
//    //参数下发指令
//    private void setSendPreMsg(final RecipeStepDetailView working_recipeStepDetailView) {
//        if (IRokiFamily.RC906.equals(typeTemp)) {
//            if (steamAndOven != null) {
//                if (steamAndOven.alarm != 0) {
//                    steamOvenOneAlarmStatus(steamAndOven.alarm);
//                    return;
//                }
//            }
//        }
//        LogUtils.i("20171122", "here is run:" + working_recipeStepDetailView.type);
//        RecipeUtil.setDevicePreSetModel(cx, this, current_PageItemIndex, new Callback<Integer>() {
//            @Override
//            public void onSuccess(Integer n) {
//                if (n == 0) {
//                    //预设开始约两秒之内不让其再次点击
//                    button_lock = true;
//                    myHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            button_lock = false;
//                        }
//                    }, 2000);
//                    hasHeat = false;
//                    ovenHasHeat = false;
//                    ovenHasWorking = false;
//                    ovenHasPause = false;
//                    hasPause = false;
//                    hasworking = false;
//                    flag = 0;
//                    steamFlag = 0;
//                    ToastUtils.show("参数下发成功", Toast.LENGTH_SHORT);
//                    setStepButton_Start();
//                    // working_recipeStepDetailView = (RecipeStepDetailView) adapter.getViews().get(current_PageItemIndex);
//                    current_stepButtonStatus = StepButton.start;
//                    setDeviceTrainLock(working_recipeStepDetailView.type, true);
//                    working_PageItemIndex = current_PageItemIndex;
//                } else if (n == 1) {
//                    ToastUtils.show(R.string.device_Throwable_text, Toast.LENGTH_SHORT);
//                } else if (n == 2) {
//                    promptDialog();
//                } else if (n == 3) {
//                    ToastUtils.show(DEVICEOFF_STR, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.show(R.string.device_Failure_text, Toast.LENGTH_SHORT);
//            }
//        });
//    }
//
//
//    IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警
//    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
//
//    //一体机报警处理
//    private void steamOvenOneAlarmStatus(short alarm) {
//
//        if (alarm != 0) {
//            switch (alarm) {
//                case 128:
//                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
//                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm);
//                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_temp_unusual_content);
//                    centerOneBtnListener();
//                    break;
//                case 16:
//                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
//                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_alarm_water_shortage);
//                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_water_shortage_content);
//                    centerOneBtnListener();
//                    break;
//                case 8:
//                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
//                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
//                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_heat_elimination_fan_content);
//                    makePhoneCallListenr();
//                    break;
//                case 2:
//                case 4:
//                    iRokiDialogAlarmType_01.setTitleText(R.string.device_steamOvenOne_name);
//                    iRokiDialogAlarmType_01.setTitleAralmCodeText(R.string.device_alarm);
//                    iRokiDialogAlarmType_01.setContentText(R.string.device_alarm_c906_temp_sensor_content);
//                    makePhoneCallListenr();
//                    break;
//                case 1:
//                    iRokiDialogAlarmType_02.setTitleText(R.string.device_steamOvenOne_name);
//                    iRokiDialogAlarmType_02.setTitleAralmCodeText(R.string.device_steamOvenOne_warm_tips);
//                    iRokiDialogAlarmType_02.setContentText(R.string.device_alarm_c906_content);
//                    centerOneBtnListener();
//                    break;
//            }
//        }
//
//    }
//
//    private void centerOneBtnListener() {
//        iRokiDialogAlarmType_02.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iRokiDialogAlarmType_02.dismiss();
//            }
//        });
//        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
//        iRokiDialogAlarmType_02.show();
//    }
//
//    //拨打电话
//    private void makePhoneCallListenr() {
//        iRokiDialogAlarmType_01.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iRokiDialogAlarmType_01.dismiss();
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//        iRokiDialogAlarmType_01.setCancelBtn(R.string.can_good, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
//        iRokiDialogAlarmType_01.show();
//        LogUtils.i("20170918", "show:" + iRokiDialogAlarmType_01.isShow());
//    }
//
//
//    private void setPreSetParamZKY(String type, short status, final Callback<Integer> callback) {
//        AbsSteameOvenOne steameOvenOne = Utils.getDefaultSteameOven();
//        if (steameOvenOne != null) {
//            steameOvenOne.setSteameOvenStatus(steameOvenOne.powerState, status, new VoidCallback() {
//                @Override
//                public void onSuccess() {
//                    callback.onSuccess(0);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    callback.onSuccess(1);
//                }
//            });
//        }
//
//    }
//
//    private void setPreSetParamNew(String type, short status, final Callback<Integer> callback) {
//        LogUtils.i("20171116", "type::" + type);
//        List<AbsOven> oven = Utils.getOven();
//        for (int i = 0; i < oven.size(); i++) {
//            if (type != null && type.equals(oven.get(i).getDt())) {
//                oven.get(i).setRecipeOvenStatus(status, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        callback.onSuccess(0);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        callback.onSuccess(1);
//                    }
//                });
//            }
//        }
//        List<AbsSteamoven> steam = Utils.getSteam();
//        for (int i = 0; i < steam.size(); i++) {
//            if (type != null && type.equals(steam.get(i).getDt())) {
//                steam.get(i).setSteamStatus(status, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        callback.onSuccess(0);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        callback.onSuccess(1);
//                    }
//                });
//            }
//        }
//    }
//
//    /**
//     * viewpage 页面选择
//     */
//    @Override
//    public void onPageSelected(int position) {
//        stepIndex = position;
//        //nextCookStepControl(position);
//        LogUtils.i("20170914", "position:" + position);
//        if (position > 0) {//除了第一个页面外其他页面都有返回按钮
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    recipe_step_return.setVisibility(View.VISIBLE);
//                }
//            });
//        } else {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    recipe_step_return.setVisibility(View.GONE);
//                }
//            });
//        }
//        this.current_PageItemIndex = position;
//        RecipeStepDetailView recipeStepDetailView = (RecipeStepDetailView) adapter.getViews().get(position);
//        switch (current_stepButtonStatus) {
//            case StepButton.start:
//                break;
//            case StepButton.pause:
//                break;
//            case StepButton.countDown:
//                break;
//            case StepButton.preheat:
//                break;
//            case StepButton.alarm:
//                break;
//            default:
//                initFalse();
//                if (recipeStepDetailView.hasCategory && recipeStepDetailView.isUserHasDevice
//                        && recipeStepDetailView.isCurrentDeviceHasPlat) {//判断菜谱有无品类
//                    current_stepButtonStatus = StepButton.preset;
//                    setStepButton_Preset();
//                } else {
//                    current_stepButtonStatus = StepButton.none;
//                    setStepButton_None();
//                }
//                typeTemp = recipeStepDetailView.type;
//                recipeStepDetailView.recipe_step_tv_stepdetail.setVisibility(View.VISIBLE);
//                recipeStepDetailView.recipe_step_linear_complete.setVisibility(View.GONE);
//                recipeStepDetailView.onSpeakClick();//自动阅读
//                break;
//        }
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        if (isScrolling) {
//            if (lastValue > positionOffsetPixels) {
//                upCookStepControl(stepIndex);
//            } else if (lastValue < positionOffsetPixels) {
//                //nextCookStepControl(stepIndex);
//            }
//        }
//        lastValue = positionOffsetPixels;
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//        isScrolling = state == 1;
//    }
//
//    //上一步数据
//    private void upCookStepControl(int stepIndex) {
//        long min = 0;
//
//        if (backStartInMillis != 0) {
//            long endInMillis = Calendar.getInstance().getTimeInMillis();
//            min = (endInMillis - backStartInMillis) / 1000;
//        }
//        backStartInMillis = Calendar.getInstance().getTimeInMillis();
//        min = (nextInMillis - backStartInMillis) / 1000;
//        CookStepDetails stepDetail = new CookStepDetails();
//        CookStep step = steps.get(stepIndex);
//     /*  if (RecipeUtils.isPlotRecipe(recipe)){
//           stepDetail.scheduledTime = -1;
//       }else {
//           stepDetail.scheduledTime = step.needTime;
//       }*/
//        stepDetail.stepNo = (stepIndex + 1);
//
//
//        if (min <= 3) {
//            stepDetail.actionType = "NORMAL";
//        } else if (min >= 7200) {
//            UIService.getInstance().popBack();
//        } else {
//            stepDetail.actualTime = (int) min;
//            if (min < step.needTime) {
//                stepDetail.actionType = "SKIPPED";
//            } else if (min > step.needTime && step.needTime != 0 && tempPreSec * 60 > step.needTime) {
//                stepDetail.actionType = "NORMAL";
//            } else {
//                stepDetail.actionType = "NORMAL";
//            }
//            stepDetailses.add(stepDetail);
//
//            LogUtils.i("20170914", "tempPreSec:" + tempPreSec + " min:" + min + " stepNo:" + stepDetail.stepNo + " actionType:" + stepDetail.actionType + " scheduledTime:" + stepDetail.scheduledTime);
//        }
//    }
//
//   /* //下一步数据
//    private void nextCookStepControl(int stepIndex) {
//
//        long min = 0;
//        if (nextInMillis != 0){
//            long endInMillis = Calendar.getInstance().getTimeInMillis();
//            min = (endInMillis - nextInMillis) / 1000;
//        }
//        nextInMillis = Calendar.getInstance().getTimeInMillis();
//
//        CookStep step = null;
//        if (stepIndex == 0){
//            step = steps.get(stepIndex);
//        }else {
//            step = steps.get(stepIndex);
//        }
//        CookStepDetails stepDetail = new CookStepDetails();
//
//        stepDetail.scheduledTime = step.needTime;
//        int stepNum = stepIndex;
//        stepDetail.stepNo = stepNum+1;
//        if (min <= 3){
//            stepDetail.actionType="NORMAL";
//        }else if (min >= 7200){
//            UIService.getInstance().popBack();
//        }else {
//            stepDetail.actualTime = (int) min;
//            if (min < step.needTime){
//                stepDetail.actionType="SKIPPED";
//            }else if (min > step.needTime && step.needTime!= 0 && tempPreSec*60 > step.needTime){
//                stepDetail.actionType="NORMAL";
//            }else if (min >= stepDetail.actualTime -2 && min <= stepDetail.actualTime + 2 ){
//                stepDetail.actionType="NORMAL";
//            }
//            stepDetailses.add(stepDetail);
//            LogUtils.i("20170914"," min:"+ min+" stepNo:"+stepDetail.stepNo+ " actionType:"+stepDetail.actionType+" scheduledTime:"+stepDetail.scheduledTime);
//        }
//    }
//*/
//
//    /**
//     * 设置步骤按钮为开始状态
//     */
//    private void setStepButton_Start() {
//        isClickParamSetButton = true;
//        recipe_step_button.setVisibility(View.VISIBLE);
//        recipe_step_button.setStartCountDown(false);
//        recipe_step_button.setTotalSec(0);
//        recipe_step_button.setCurrentSec(0);
//        recipe_step_button.setBackgroundColor(r.getColor(R.color.c02));
//        //recipe_step_button.setText(new String("开始"));
//        recipe_step_button.addView(StepButtonText("开始"));
//    }
//
//    /**
//     * 设置步骤按钮暂停状态
//     */
//    private void setStepButton_Pause() {
//        // if (disconnect_text != null || finish_img != null)
//        recipe_step_button.removeAllViews();
//        //recipe_step_button.setText(new String("暂停中"));
//        recipe_step_button.setBackgroundColor(r.getColor(R.color.c02));
//        recipe_step_button.addView(StepButtonText("暂停中"));
//    }
//
//    /**
//     * 设置步骤按钮为预设状态
//     */
//    private void setStepButton_Preset() {
//        recipe_step_button.removeAllViews();
//        recipe_step_button.setVisibility(View.VISIBLE);
//        recipe_step_button.setStartCountDown(false);
//        recipe_step_button.setTotalSec(0);
//        recipe_step_button.setCurrentSec(0);
//        recipe_step_button.setBackgroundColor(r.getColor(R.color.c02));
//        //recipe_step_button.setText(new String("参数自动设置"));
//        recipe_step_button.addView(StepButtonText("参数自动设置"));
//    }
//
//    /**
//     * 设置步骤按钮为预热状态
//     */
//    private void setStepButton_PreHeat() {
//        recipe_step_button.removeAllViews();
//        recipe_step_button.setStartCountDown(false);
//        recipe_step_button.setTotalSec(0);
//        recipe_step_button.setCurrentSec(0);
//        recipe_step_button.setBackgroundColor(r.getColor(R.color.c02));
//        //recipe_step_button.setText(new String("预热中"));
//        recipe_step_button.addView(StepButtonText("预热中"));
//    }
//
//
//    /**
//     * 设置步骤按钮显示空
//     */
//    private void setStepButton_None() {
//        recipe_step_button.removeAllViews();
//        recipe_step_button.setStartCountDown(false);
//        recipe_step_button.setTotalSec(0);
//        recipe_step_button.setCurrentSec(0);
//        recipe_step_button.setVisibility(View.GONE);
//    }
//
//    /**
//     * 设置断网步骤按钮显示
//     */
//    private void setStepButton_ConnectionOut() {
//        recipe_step_button.removeAllViews();
//        recipe_step_button.setVisibility(View.VISIBLE);
//        recipe_step_button.setStartCountDown(false);
//        recipe_step_button.setTotalSec(0);
//        recipe_step_button.setCurrentSec(0);
//        recipe_step_button.setBackgroundColor(Color.parseColor("#1E1E1F"));
//        recipe_step_button.addView(StepButtonText_DisConnect());
//        recipe_step_button.addView(StepButtonIc_DisConnect());
//    }
//
//    ImageView finish_img;//断网结束符号
//
//    /**
//     * 设置断网    结束符号
//     */
//    private View StepButtonIc_DisConnect() {
//        if (finish_img != null)
//            return finish_img;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayUtils.dip2px(cx, 20),
//                DisplayUtils.dip2px(cx, 20));
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
//        params.setMargins(0, 0, DisplayUtils.dip2px(cx, 20), 0);
//        finish_img = new ImageView(cx);
//        finish_img.setLayoutParams(params);
//        finish_img.setImageResource(R.mipmap.ic_recipe_cooking_close);
//        finish_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        finish_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = Helper.newBlackPromptConfirmAndCancleDialog(cx, new BlackPromptConfirmAndCancleDialog.PickListener() {
//                    @Override
//                    public void onCancel() {
//                    }
//
//                    @Override
//                    public void onConfirm(int what, Object m) {
//                        setInitStatus();
//                        if (BlackPromptConfirmAndCancleDialog.dlg != null && BlackPromptConfirmAndCancleDialog.dlg.isShowing())
//                            BlackPromptConfirmAndCancleDialog.dlg.dismiss();
//                    }
//                }, R.layout.dialog_recipedisconnect_error);
//                dialog.show();
//            }
//        });
//        return finish_img;
//    }
//
//    LinearLayout disconnect_text;
//
//    /**
//     * 设置断网   文字文案
//     */
//    private View StepButtonText_DisConnect() {
//        if (disconnect_text != null)
//            return disconnect_text;
//        global_relative_layoutparam.addRule(CENTER_IN_PARENT);
//        disconnect_text = new LinearLayout(cx);
//        disconnect_text.setLayoutParams(global_relative_layoutparam);
//        LinearLayout.LayoutParams linear_layoutparam = new LinearLayout.LayoutParams(DisplayUtils.dip2px(cx, 28),
//                DisplayUtils.dip2px(cx, 23));
//        linear_layoutparam.setMargins(0, 0, DisplayUtils.dip2px(cx, 15), 0);
//
//        ImageView imageView = new ImageView(cx);
//        imageView.setLayoutParams(linear_layoutparam);
//        imageView.setImageResource(R.mipmap.ic_wifi_connect_state);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//        TextView textView = new TextView(cx);
//        textView.setText("断网");
//        textView.setTextSize(textsize);
//        textView.setTextColor(r.getColor(R.color.c02));
//        disconnect_text.addView(imageView);
//        disconnect_text.addView(textView);
//        return disconnect_text;
//    }
//
//    RelativeLayout.LayoutParams global_relative_layoutparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//    /**
//     * 设置给
//     */
//    private View StepButtonText(String str) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                recipe_step_button.removeAllViews();
//            }
//        });
//        global_relative_layoutparam.addRule(CENTER_IN_PARENT);
//        TextView textView = new TextView(cx);
//        textView.setText(str);
//        textView.setTextSize(textsize);
//        textView.setTextColor(r.getColor(R.color.white));
//        textView.setLayoutParams(global_relative_layoutparam);
//        return textView;
//    }
//
//    /**
//     * 设置最初状态
//     */
//    private void setInitStatus() {
//        if (currentSec > 0)
//            return;
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//        if (oven != null) {
//            tempPreSec = oven.setTime * 60;
//        }
//        current_stepButtonStatus = StepButton.preset;
//        totalSec = 0;
//        currentSec = 0;
//        initFalse();
//        setStepButton_Preset();
//        recipe_step_pager.setCurrentItem(current_PageItemIndex);
//        onPageSelected(current_PageItemIndex);
//        hasHeat = false;
//        hasPreHeat = false;
//        ovenHasHeat = false;
//        ovenHasPause = false;
//        ovenHasWorking = false;
//        flag906 = false;
//        flag = 0;
//    }
//
//    /**
//     * 设置烤箱指令暂停工作
//     */
//    private int totalSec;
//    private int currentSec = -1;
//    Timer timer;
//    private int tempPreSec;
//    private Handler myHandler = new Handler() {
//        @Override
//        public void handleMessage(final Message msg) {
//            super.handleMessage(msg);
//
//            if (msg.what == 10) {
//                if (currentSec < 0)
//                    return;
//                recipe_step_button.setStartCountDown(true);
//                recipe_step_button.setTotalSec(totalSec);
//                recipe_step_button.setCurrentSec(currentSec);
//                final short min = (short) (currentSec / 60);
//                final short sec = (short) (currentSec % 60);
//                if (sec < 10) {
//                    // recipe_step_button.setText(min + ":0" + sec);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recipe_step_button.addView(StepButtonText(min + ":0" + sec));
//                        }
//                    });
//
//                } else {
//                    //recipe_step_button.setText(min + ":" + sec);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recipe_step_button.addView(StepButtonText(min + ":" + sec));
//                        }
//                    });
//
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        recipe_step_button.setBackgroundColor(r.getColor(R.color.c02));
//                        recipe_step_button.setVisibility(View.VISIBLE);
//                    }
//                });
//
//                if (currentSec <= 3 && working_PageItemIndex != current_PageItemIndex) {//倒计时3秒自动切换当前页
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recipe_step_pager.setCurrentItem(working_PageItemIndex);
//                        }
//                    });
//                }
//                if (currentSec == 0) {
//                    totalSec = 0;
//                    currentSec = 0;
//                    current_stepButtonStatus = StepButton.none;
//                    //自动跳转到下一页
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            working_recipeStepDetailView.recipe_step_tv_stepdetail.setVisibility(View.GONE);
//                            working_recipeStepDetailView.recipe_step_linear_complete.setVisibility(View.VISIBLE);
//                        }
//                    });
//
//                    myHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //完成提示已完成，隱藏詳情文字
//                                    working_recipeStepDetailView.recipe_step_tv_stepdetail.setVisibility(View.VISIBLE);
//                                    working_recipeStepDetailView.recipe_step_linear_complete.setVisibility(View.GONE);
//                                    if (working_PageItemIndex != adapter.getCount() - 1) {//最后一页
//                                        int newIndex = working_PageItemIndex + 1;
//                                        recipe_step_pager.setCurrentItem(newIndex);
//                                    }
//                                }
//                            });
//                        }
//                    }, 2000);
//                    if (timer != null) {
//                        timer.cancel();
//                        timer = null;
//                    }
//                }
//                currentSec--;
//                if (currentSec == 0)
//                    initFalse();//关闭当前轮训
//
//            }
//
//        }
//    };
//
//
//    /**
//     * 轮训锁定
//     */
//    private Boolean OvenR016 = false;
//    private Boolean OvenR028 = false;
//    private Boolean OvenR026 = false;
//    private Boolean OvenR039 = false;
//    private Boolean SteamsS209 = false;
//    private Boolean SteamsS226 = false;
//    private Boolean MicroM509 = false;
//    private Boolean MicroM526 = false;
//    private Boolean OvenR075 = false;
//    private Boolean SteamsS228 = false;
//    private Boolean SteamsS275 = false;
//    private Boolean SteamAndOven906 = false;
//
//    private void initFalse() {
//        OvenR039 = false;
//        SteamsS209 = false;
//        SteamsS226 = false;
//        MicroM509 = false;
//        OvenR016 = false;
//        OvenR026 = false;
//        MicroM526 = false;
//        OvenR028 = false;
//        OvenR075 = false;
//        SteamsS275 = false;
//        SteamsS228 = false;
//        SteamAndOven906 = false;
//        //烤箱和蒸汽炉已经预热过的标志位重新设为false
//        hasPreHeat = false;
//    }
//
//    private Boolean train_lock;//对于所有的轮需使用
//
//
//    /**
//     * 恢复倒计时
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        LogUtils.i("20161024", "onResume");
//        EventUtils.regist(this);
//        train_lock = false;
//        //运行中，若页面再次resume（从桌面返回），让其按钮不能2秒内点击，屏蔽onStop()中的假暂停影响
//        button_lock = true;
//        myHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                button_lock = false;
//            }
//        }, 2000);
//    }
//
//    /**
//     * 清除倒计时
//     */
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (current_stepButtonStatus != StepButton.countDown &&
//                current_stepButtonStatus != StepButton.pause) {
//            return;
//        }
//        train_lock = true;
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//        totalSec = 0;
//        currentSec = 0;
//        current_stepButtonStatus = StepButton.pause;
//        setStepButton_Pause();
//    }
//
//    /**
//     * 设置轮训
//     */
//    private void setDeviceTrainLock(String type, boolean isTrain) {
//        LogUtils.i("20171116", "DeviceTrainLock::" + type);
//        if (IRokiFamily.RR039.equals(type)) {
//            OvenR039 = isTrain;
//        } else if (IRokiFamily.RR016.equals(type)) {
//            OvenR016 = isTrain;
//        } else if (IRokiFamily.RR026.equals(type)) {
//            OvenR026 = isTrain;
//        } else if (IRokiFamily.RS209.equals(type)) {
//            SteamsS209 = isTrain;
//        } else if (IRokiFamily.RS226.equals(type)) {
//            SteamsS226 = isTrain;
//        } else if (IRokiFamily.RM509.equals(type)) {
//            MicroM509 = isTrain;
//        } else if (IRokiFamily.RM526.equals(type)) {
//            MicroM526 = isTrain;
//        } else if (IRokiFamily.RC906.equals(type)) {
//            SteamAndOven906 = isTrain;
//        } else if (IRokiFamily.RR028.equals(type)) {
//            OvenR028 = isTrain;
//        } else if (IRokiFamily.RR075.equals(type)) {
//            OvenR075 = isTrain;
//        } else if (IRokiFamily.RS275.equals(type)) {
//            SteamsS275 = isTrain;
//        } else if (IRokiFamily.RS228.equals(type)) {
//            SteamsS228 = isTrain;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        SpeechManager.getInstance().dispose();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//        initFalse();//关闭当前轮训
//        totalSec = 0;
//        currentSec = 0;
//    }
//
//    interface StepButton {
//        int preset = 1;//预设
//        int start = 2;//开始
//        int pause = 3;//暂停
//        int countDown = 4;//倒计时
//        int none = 0;//无显示
//        int alarm = 5;//设备报警
//        int preheat = 6;//设备报警
//    }
//
//    private boolean hasPreHeat;//是否已经预热过
//    private short preStatus = -1;//是否处于预热状态
//    private short currentStatus = -2;//是否处于预热状态
//    private short preStatus1 = -1;//是否处于预热状态
//    private short currentStatus1 = -2;//是否处于预热状态
//
//    AbsSteameOvenOne steamAndOven;
//    boolean flag906 = false;
//
//    @Subscribe
//    public void onEvent(SteamOvenOneStatusChangedEvent event) {
//        if (event.pojo == null) {
//            return;
//        }
//        if (IRokiFamily.RC906.equals(event.pojo.getDt())) {
//            steamAndOven = event.pojo;
//            preStatus = currentStatus;
//            currentStatus = steamAndOven.powerOnStatus;
//            if (train_lock || !SteamAndOven906)//烤箱未开始轮训
//                return;
//            if (event.pojo.powerState == SteamOvenOnePowerStatus.Off) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (event.pojo.powerState == 2 && event.pojo.powerOnStatus == 0 && flag906 && event.pojo.alarm == 0) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (event.pojo.powerState == 2 && event.pojo.powerOnStatus == 0 && flag906 &&
//                    steamAndOven.alarm != 0) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                ovenHasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            }
//
//            if (steamAndOven.workState == SteamOvenOneWorkStatus.PreHeat) {//是否预热状态
//                setStepButton_PreHeat();
//                DeviceConnect_Lock = true;
//                current_stepButtonStatus = StepButton.preheat;
//                hasPreHeat = true;
//                ovenHasHeat = true;
//                flag = 1;
//                return;
//            }
//            LogUtils.i("20170714", "preStatus:" + preStatus + " currentStatus::" + currentStatus);
//            if (steamAndOven.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus && preStatus == currentStatus) {//判断烤箱是否持续处于工作状态
//                LogUtils.i("20170714", "oven");
//                if (flag == 0) {
//                    flag = 1;
//                    return;
//                }
//                ovenHasWorking = true;
//                ovenHasHeat = true;
//                current_stepButtonStatus = StepButton.countDown;
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = steamAndOven.setTime * 60;
//                currentSec = steamAndOven.leftTime;
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (steamAndOven.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                ovenHasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            } else if (steamAndOven.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else {
//                if (currentStatus == preStatus && currentStatus != SteamOvenOnePowerStatus.On) {
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == SteamOvenOnePowerStatus.On && steamAndOven.time == 0) {
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == SteamOvenOnePowerStatus.On && (ovenHasHeat || ovenHasWorking || ovenHasPause)) {
//                    if (ovenHasWorking) {
//                        currentSec = 0;
//                        ovenHasWorking = false;
//                        setInitStatus();
//                    } else if (ovenHasHeat) {
//                        ovenHasHeat = false;
//                        setInitStatus();
//                    } else if (ovenHasPause) {
//                        ovenHasPause = false;
//                        setInitStatus();
//                    }
//                    DeviceConnect_Lock = false;
//                }
//            }
//        }
//    }
//
//    private boolean ovenHasHeat;
//    private boolean ovenHasWorking;
//    private boolean ovenHasPause;
//    private boolean ovenHasOff = true;
//    AbsOven oven;
//    int flag = 0;
//    int steamFlag = 0;
//
//    /**
//     * 烤箱轮训
//     */
//    @Subscribe
//    public void onEvent(OvenStatusChangedEvent event) {
//        if (event.pojo == null) {
//            return;
//        }
//        if (IRokiFamily.RR016.equals(event.pojo.getDt())) {
//            oven = event.pojo;
//            preStatus = currentStatus;
//            currentStatus = oven.status;
//            if (train_lock || !OvenR016)//烤箱未开始轮训
//                return;
//            if (currentStatus == OvenStatus.Off) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (event.pojo.status == OvenStatus.On && flag906) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (oven.status == OvenStatus.PreHeat) {//是否预热状态
//                setStepButton_PreHeat();
//                DeviceConnect_Lock = true;
//                current_stepButtonStatus = StepButton.preheat;
//                hasPreHeat = true;
//                ovenHasHeat = true;
//                flag = 1;
//                return;
//            }
//            LogUtils.i("20170714", "preStatus:" + preStatus + " currentStatus::" + currentStatus);
//            if (oven.status == OvenStatus.Working && preStatus == currentStatus) {//判断烤箱是否持续处于工作状态
//                LogUtils.i("20170714", "oven");
//                if (flag == 0) {
//                    flag = 1;
//                    return;
//                }
//                ovenHasWorking = true;
//                ovenHasHeat = true;
//                current_stepButtonStatus = StepButton.countDown;
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = oven.setTime * 60;
//                currentSec = oven.time;
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (oven.status == OvenStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                ovenHasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            } else if (oven.status == OvenStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else {
//                if (currentStatus == preStatus && currentStatus != OvenStatus.On) {
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && oven.time == 0) {
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && (ovenHasHeat || ovenHasWorking || ovenHasPause)) {
//                    if (ovenHasWorking) {
//                        currentSec = 0;
//                        ovenHasWorking = false;
//                        setInitStatus();
//                    } else if (ovenHasHeat) {
//                        ovenHasHeat = false;
//                        setInitStatus();
//                    } else if (ovenHasPause) {
//                        ovenHasPause = false;
//                        setInitStatus();
//                    }
//                    DeviceConnect_Lock = false;
//                }
//
//            }
//
//        } else if (IRokiFamily.RR026.equals(event.pojo.getDt())
//                || IRokiFamily.RR075.equals(event.pojo.getDt())) {
//            if (OvenR075) {
//                oven = event.pojo;
//                if (train_lock || !OvenR075)//烤箱未开始轮训
//                    return;
//            } else {
//                oven = event.pojo;
//                if (train_lock || !OvenR026)//烤箱未开始轮训
//                    return;
//            }
//            preStatus = currentStatus;
//            currentStatus = oven.status;
//            if (currentStatus == OvenStatus.Off) {
//                currentSec = -1;
//                ovenHasHeat = false;
//                ovenHasPause = false;
//                ovenHasWorking = false;
//                setInitStatus();
//            }
//            if (event.pojo.status == OvenStatus.On && flag906) {
//                currentSec = -1;
//                setInitStatus();
//            }
//
//            if (oven.status == OvenStatus.PreHeat) {//是否预热状态
//                setStepButton_PreHeat();
//                DeviceConnect_Lock = true;
//                current_stepButtonStatus = StepButton.preheat;
//                hasPreHeat = true;
//                ovenHasHeat = true;
//                flag = 1;
//                return;
//            }
//            if (oven.status == OvenStatus.Working && preStatus == currentStatus) {//判断烤箱是否持续处于工作状态
//                //清除报警弹出框
//                if (flag == 0) {
//                    flag = 1;
//                    return;
//                }
//                ovenHasWorking = true;
//                ovenHasHeat = true;
//                current_stepButtonStatus = StepButton.countDown;
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = oven.setTime * 60;
//                currentSec = oven.time;
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (oven.status == OvenStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                ovenHasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            } else if (oven.status == OvenStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else {
//                if (currentStatus == preStatus && currentStatus != OvenStatus.On) {
//                    currentSec = 0;
//                    setInitStatus();
//                    LogUtils.i("20170427", "eve:" + oven.status);
//                    DeviceConnect_Lock = false;
//                }
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && oven.time == 0) {
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && (ovenHasHeat || ovenHasWorking || ovenHasPause)) {
//                    if (ovenHasWorking) {
//                        currentSec = 0;
//                        ovenHasWorking = false;
//                        setInitStatus();
//                    } else if (ovenHasHeat) {
//                        ovenHasHeat = false;
//                        setInitStatus();
//                    } else if (ovenHasPause) {
//                        ovenHasPause = false;
//                        setInitStatus();
//                    }
//                    DeviceConnect_Lock = false;
//                }
//            }
//
//        } else if (event.pojo.getDt().equals(IRokiFamily.RR028)) {
//            oven = event.pojo;
//            preStatus = currentStatus;
//            currentStatus = oven.status;
//            if (train_lock || !OvenR028)//烤箱未开始轮训
//                return;
//            if (currentStatus == OvenStatus.Off) {
//                currentSec = -1;
//                ovenHasHeat = false;
//                ovenHasPause = false;
//                ovenHasWorking = false;
//                setInitStatus();
//            }
//            if (event.pojo.status == OvenStatus.On && flag906) {
//                currentSec = -1;
//                setInitStatus();
//            }
//
//            if (oven.status == OvenStatus.PreHeat) {//是否预热状态
//                setStepButton_PreHeat();
//                DeviceConnect_Lock = true;
//                current_stepButtonStatus = StepButton.preheat;
//                hasPreHeat = true;
//                ovenHasHeat = true;
//                flag = 1;
//                return;
//            }
//            if (oven.status == OvenStatus.Working && preStatus == currentStatus) {//判断烤箱是否持续处于工作状态
//                //清除报警弹出框
//                if (flag == 0) {
//                    flag = 1;
//                    return;
//                }
//
//                ovenHasWorking = true;
//                ovenHasHeat = true;
//                current_stepButtonStatus = StepButton.countDown;
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = oven.setTime * 60;
//                currentSec = oven.time;
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (oven.status == OvenStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                ovenHasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            } else if (oven.status == OvenStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else {
//                if (currentStatus == preStatus && currentStatus != OvenStatus.On) {
//                    currentSec = 0;
//                    setInitStatus();
//                    LogUtils.i("20170427", "eve:" + oven.status);
//                    DeviceConnect_Lock = false;
//                }
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && oven.time == 0) {
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus == preStatus && currentStatus == OvenStatus.On && (ovenHasHeat || ovenHasWorking || ovenHasPause)) {
//                    if (ovenHasWorking) {
//                        currentSec = 0;
//                        ovenHasWorking = false;
//                        setInitStatus();
//                    } else if (ovenHasHeat) {
//                        ovenHasHeat = false;
//                        setInitStatus();
//                    } else if (ovenHasPause) {
//                        ovenHasPause = false;
//                        setInitStatus();
//                    }
//                    DeviceConnect_Lock = false;
//                }
//            }
//
//        } else {
//            Oven039 oven = (Oven039) event.pojo;
//            if (train_lock || !OvenR039)//烤箱未开始轮训
//                return;
//            if (oven.status == OvenStatus.Working) {//判断烤箱是否处于工作状态和
//                current_stepButtonStatus = StepButton.countDown;
//                LogUtils.i("20170809", "hhhhh:" + (tempPreSec == oven.time));
//                if (oven.setTemp != oven.temp && tempPreSec == oven.time) {//是否预热状态
//                    if (timer != null) {
//                        timer.cancel();
//                        timer = null;
//                    }
//                    setStepButton_PreHeat();
//                    DeviceConnect_Lock = true;
//                    totalSec = 0;
//                    return;
//                }
//                hasPreHeat = true;
//                tempPreSec = oven.time;
//                LogUtils.i("20170809", "tempPreSed::" + tempPreSec);
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = oven.setTime * 60;
//                currentSec = oven.time;
//
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (oven.status == OvenStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//            } else if (oven.status == OvenStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else if (oven.status == OvenStatus.On) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                currentSec = -1;
//                current_stepButtonStatus = StepButton.start;
//            } else {
//                setInitStatus();
//                DeviceConnect_Lock = false;
//            }
//        }
//    }
//
//
//    private boolean isSteamFirstInWorking = true;
//    private boolean hasHeat;
//    private boolean hasPause;
//    private boolean hasworking;
//    private boolean waterBox;
//
//    // Steam226 steam;
//    AbsSteamoven steam;
//    short time209 = -1;
//
//    /**
//     * 蒸箱轮训
//     */
//    @Subscribe
//    public void onEvent(SteamOvenStatusChangedEvent event) {
//
//        //LogUtils.i("20171110","dt:"+event.pojo.getDt());
//        if (IRokiFamily.RS226.equals(event.pojo.getDt()) ||
//                IRokiFamily.RS275.equals(event.pojo.getDt()) ||
//                IRokiFamily.RS228.equals(event.pojo.getDt())
//        ) {
//            if (SteamsS275) {
//                steam = event.pojo;
//                if (train_lock || !SteamsS275)//烤箱未开始轮训
//                    return;
//            } else if (SteamsS226) {
//                steam = event.pojo;
//                if (train_lock || !SteamsS226)//烤箱未开始轮训
//                    return;
//            } else if (SteamsS228) {
//                steam = event.pojo;
//                if (train_lock || !SteamsS228)//烤箱未开始轮训
//                    return;
//            }
//
//            preStatus1 = currentStatus1;
//            currentStatus1 = steam.status;
//
//           /* waterBox=checkWaterOut_Dialog();
//            if (waterBox){
//                return;
//            }*/
//            if (event.pojo.status == SteamStatus.Off) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (event.pojo.status == SteamStatus.On && flag906) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            checkWaterOut_Dialog();
//            if (steam.status == SteamStatus.PreHeat) {//是否预热状态
//                if (dlg_steam != null && dlg_steam.isShowing()) {
//                    dlg_steam.dismiss();
//                    dlg_steam = null;
//                }
//                if (tempDlg != null && tempDlg.isShowing()) {
//                    tempDlg.dismiss();
//                    tempDlg = null;
//                }
//                checkWaterOut_Dialog();
//                /*waterBox=checkWaterOut_Dialog();
//                if (waterBox)return;*/
//                setStepButton_PreHeat();
//                DeviceConnect_Lock = true;
//                current_stepButtonStatus = StepButton.preheat;
//                hasPreHeat = true;
//                hasHeat = true;
//                steamFlag = 1;
//                return;
//            } else if (steam.status == SteamStatus.Working && preStatus1 == currentStatus1) {//判断烤箱是否持续处于工作状态
//                if (steamFlag == 0) {
//                    steamFlag = 1;
//                    return;
//                }
//                //运行界面首先清除弹框
//                if (dlg_steam != null && dlg_steam.isShowing()) {
//                    dlg_steam.dismiss();
//                    dlg_steam = null;
//                }
//                if (tempDlg != null && tempDlg.isShowing()) {
//                    tempDlg.dismiss();
//                    tempDlg = null;
//                }
//                checkWaterOut_Dialog();
//              /* waterBox= checkWaterOut_Dialog();
//                if (waterBox)return;*/
//                hasworking = true;
//                hasHeat = false;
//                current_stepButtonStatus = StepButton.countDown;
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = steam.timeSet * 60;
//                currentSec = steam.time;
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (steam.status == SteamStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                steamFlag = 1;
//                checkWaterOut_Dialog();
//              /*  waterBox = checkWaterOut_Dialog();
//                if (waterBox)return;*/
//                hasPause = true;
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                if (tempDlg != null && tempDlg.isShowing()) {
//                    tempDlg.dismiss();
//                    tempDlg = null;
//                }
//                if (dlg_steam != null && dlg_steam.isShowing()) {
//                    dlg_steam.dismiss();
//                    dlg_steam = null;
//                }
//                DeviceConnect_Lock = true;
//            } else if (steam.status == SteamStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else {
//                if (currentStatus1 == preStatus1 && currentStatus1 != SteamStatus.On) {
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                LogUtils.i("20170306", "steam.time:" + steam.time);
//                if (currentStatus1 == preStatus && currentStatus1 == SteamStatus.On && steam.time == 0) {
//                    if (dlg_steam != null && dlg_steam.isShowing()) {
//                        dlg_steam.dismiss();
//                        dlg_steam = null;
//                    }
//
//                    if (tempDlg != null && tempDlg.isShowing()) {
//                        tempDlg.dismiss();
//                        tempDlg = null;
//                    }
//
//                    setInitStatus();
//                    DeviceConnect_Lock = false;
//                }
//
//                if (currentStatus1 == preStatus && currentStatus1 == SteamStatus.On && (hasHeat || hasPause || hasworking)) {
//                    if (hasworking) {
//                        currentSec = 0;
//                        hasworking = false;
//                        setInitStatus();
//                    } else if (hasPause) {
//                        hasPause = false;
//                        setInitStatus();
//                    } else if (hasHeat) {
//                        hasHeat = false;
//                        setInitStatus();
//                    }
//                    DeviceConnect_Lock = false;
//                }
//            }
//
//        } else if (IRokiFamily.RS209.equals(event.pojo.getDt())) {
//            Steam209 steam = (Steam209) event.pojo;
//            LogUtils.i("asdfghjkl", steam.status + "//" + steam.alarm);
//            if (train_lock || !SteamsS209)//烤箱未开始轮训
//                return;
//            if (event.pojo.status == SteamStatus.Off) {
//                currentSec = -1;
//                setInitStatus();
//            }
//            if (steam.status == SteamStatus.Working) {//判断蒸是否处于工作状态
//                //运行界面首先清除弹框
//                if (tempDlg != null && tempDlg.isShowing()) {
//                    tempDlg.dismiss();
//                    tempDlg = null;
//                }
//                if (dlg_steam != null && dlg_steam.isShowing()) {
//                    dlg_steam.dismiss();
//                    dlg_steam = null;
//                }
//
//                current_stepButtonStatus = StepButton.countDown;
//
//                //排除第一次轮询到的错误的蒸汽炉温度
//                if (isSteamFirstInWorking) {
//                    isSteamFirstInWorking = false;
//                    return;
//                }
//                if ((steam.timeSet * 60) == steam.time) {
//                    hasPreHeat = false;
//                }
//                if ((steam.timeSet * 60) < steam.time) {
//                    return;
//
//                }
//
//                if (((steam.timeSet * 60) == steam.time || time209 == steam.time) && !hasPreHeat) {//是否预热状态
//                    if (timer != null) {
//                        timer.cancel();
//                        timer = null;
//                    }
//                    setStepButton_PreHeat();
//                    DeviceConnect_Lock = true;
//                    return;
//                }
//                if (time209 == steam.time) {
//                    hasPreHeat = false;
//                    return;
//                }
//                time209 = steam.time;
//                hasPreHeat = true;
//
//                if (totalSec != 0)//保证开始轮训第一次获取时间
//                    return;
//                totalSec = steam.timeSet * 60;
//                currentSec = steam.time;
//                /*if (totalSec != currentSec) {
//                    currentSec = totalSec;
//                }*/
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        myHandler.sendEmptyMessage(10);
//                    }
//                }, 0, 1000);
//                DeviceConnect_Lock = false;
//            } else if (steam.status == SteamStatus.Pause) {//工作中，烤箱改变状态
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.pause;
//                setStepButton_Pause();
//                DeviceConnect_Lock = true;
//                //暂停界面首先清除弹框
//                if (tempDlg != null && tempDlg.isShowing()) {
//                    tempDlg.dismiss();
//                    tempDlg = null;
//                }
//                if (dlg_steam != null && dlg_steam.isShowing()) {
//                    dlg_steam.dismiss();
//                    dlg_steam = null;
//                }
//            } else if (steam.status == SteamStatus.AlarmStatus) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                totalSec = 0;
//                currentSec = 0;
//                current_stepButtonStatus = StepButton.alarm;
//                setStepButton_Pause();
//                DeviceConnect_Lock = false;
//            } else if (steam.status == SteamStatus.On) {
//                if (timer != null) {
//                    timer.cancel();
//                    timer = null;
//                }
//                currentSec = -1;
//                current_stepButtonStatus = StepButton.start;
//            } else {
//                setInitStatus();
//                DeviceConnect_Lock = false;
//            }
//        }
//    }
//
//    Dialog Waterout_dlg;
//
//    boolean checkWaterOut_Dialog() {
//        if (steam.waterboxstate == 1) {
//            if (Waterout_dlg != null && Waterout_dlg.isShowing()) {
//                Waterout_dlg.dismiss();
//                Waterout_dlg = null;
//            }
//            return false;
//        }
//
//        if (Waterout_dlg != null && Waterout_dlg.isShowing())
//            return true;
//        View view1 = LayoutInflater.from(cx).inflate(R.layout.dialog_steam226_water_out, null, false);
//        Waterout_dlg = Helper.newBlackPromptDialog2(cx, view1);
//        Waterout_dlg.show();
//        return true;
//    }
//
//    /**
//     * 微波炉轮训
//     */
//    @Subscribe
//    public void onEvent(MicroWaveStatusChangedEvent event) {
//        microW = event.pojo;
//        if (IRokiFamily.RM509.equals(event.pojo.getDt())) {
//            MicroWaveM509 microWave = (MicroWaveM509) event.pojo;
//            if (train_lock || !MicroM509)//未开始轮训
//                return;
//
//            MicroSetRun(microWave);
//        } else if (IRokiFamily.RM526.equals(event.pojo.getDt())) {
//            MicroWaveM526 microWave = (MicroWaveM526) event.pojo;
//            if (train_lock || !MicroM526)//烤箱未开始轮训
//                return;
//
//            MicroSetRun(microWave);
//        }
//
//    }
//
//    public void MicroSetRun(AbsMicroWave microWave) {
//        if (microWave.state == MicroWaveStatus.Run) {//判断微波炉是否处于工作状态
//            MicrowaveM509Alarm(microWave);//检测门控
//            current_stepButtonStatus = StepButton.countDown;
//            if (totalSec != 0)//保证开始轮训第一次获取时间
//            {
//                return;
//            }
//            if (microWave.setTime == 0) {
//                return;
//            }
//            totalSec = microWave.setTime;
//            currentSec = microWave.time;
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    myHandler.sendEmptyMessage(10);
//                }
//            }, 0, 1000);
//
//            DeviceConnect_Lock = false;
//        } else if (microWave.state == MicroWaveStatus.Pause || microWave.state == MicroWaveStatus.Alarm) {//工作中，微波炉改变状态
//            MicrowaveM509Alarm(microWave);//检测门控
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//            totalSec = 0;
//            currentSec = 0;
//            current_stepButtonStatus = StepButton.pause;
//            setStepButton_Pause();
//            DeviceConnect_Lock = true;
//        } else if (microWave.state == MicroWaveStatus.Setting) {
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//            currentSec = -1;
//        } else {
//            setInitStatus();
//            DeviceConnect_Lock = false;
//        }
//    }
//
//    private boolean DeviceConnect_Lock;//微波炉报警和暂停、烤蒸箱暂停预热不能接受断网事件屏蔽标志位
//
//    /**
//     * 轮训锁定
//     */
//    @Subscribe
//    public void onEvent(DeviceConnectionChangedEvent event) {
//        //LogUtils.i("recipe_status", "connect_status::" + event.isConnected);
//        if (!event.isConnected) {
//            if (currentSec < 10 || (!SteamsS209 && !SteamsS226 && !OvenR039 && !OvenR026 && !OvenR026 && !MicroM509 && !SteamAndOven906)) {//20秒内忽略 并且没有设备轮训也忽略
//                if (!DeviceConnect_Lock)
//                    return;
//            }
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//            totalSec = 0;
//            currentSec = 0;
//            current_stepButtonStatus = StepButton.pause;
//            setStepButton_ConnectionOut();
//            //setStepButton_Pause();
//        }
//    }
//
//    /**
//     * 提示框
//     */
//    private Dialog promptDialog() {
//        BlackPromptConfirmDialog dlg = Helper.newBlackPromptConfirmDialog(cx, new Callback2<Object>() {
//
//            @Override
//            public void onCompleted(Object o) {
//
//            }
//        }, R.layout.dialog_recipe_prompt);
//        dlg.setButtonText("知道了");
//        if (dlg != null && !dlg.isShowing()) {
//            dlg.show();
//        }
//        return dlg;
//    }
//
//
//    /**
//     * 微波炉报警
//     */
//    private void MicrowaveM509Alarm(AbsMicroWave microWave) {
//        if (microWave.doorState == 0) {// 门关
//            if (rokigatingdialog != null && rokigatingdialog.isShow()) {
//                rokigatingdialog.dismiss();
//            }
//        } else if (microWave.doorState == 1) {//门开
//            if (rokigatingdialog != null && rokigatingdialog.isShow()) {
//                return;
//            } else {
//                rokigatingdialog.setContentText(R.string.device_alarm_gating_content);
//                rokigatingdialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
//                rokigatingdialog.show();
//            }
//        }
//    }
//
//    /***************************
//     * 蒸箱报警
//     *****************************/
//    private SteamOvenSensorBrokeDialog tempDlg;
//    private SteamOvenWarningDialog dlg_steam = null;
//
//    @Subscribe
//    public void onEvent(SteamAlarmEvent event) {
//        if (IRokiFamily.RS209.equals(event.steam.getDt())) {
//            if (!SteamsS209) return;
//            int status = event.alarmId;
//            switch (status) {
//                case 3://门控开关故障
//                    if (rokigatingdialog != null && rokigatingdialog.isShow()) {
//                        rokigatingdialog.dismiss();
//                    }
//                    rokigatingdialog.setContentText(R.string.device_alarm_water_out);
//                    rokigatingdialog.setToastShowTime(DialogUtil.LENGTH_SHORT);
//                    rokigatingdialog.show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//
//}
