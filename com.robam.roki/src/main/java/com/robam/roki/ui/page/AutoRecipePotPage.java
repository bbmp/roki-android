package com.robam.roki.ui.page;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.PlotRecipeNextEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.Utils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.RuleCookTaskService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.service.MobPotOneKeyCookTaskService;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.RecipeDetailAutoView;
import com.robam.roki.ui.view.RecipeDetailView;
import com.robam.roki.ui.view.RecipeParamShowView;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.utils.DeviceSelectUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import regulation.dto.StatusResult;

/**
 * Created by Dell on 2018/5/22.
 */

public class AutoRecipePotPage extends MyBasePage<RecipeActivity> {


    ArrayList<CookStep> steps;
    String guid;

    int stoveflag;
    Long id;
    IDevice iDevice;
    public ExtPageAdapter adapter;
    public RecipeParamShowView recipeParamShowView;

    Pot[] pot;
    int mStepIndex;
    Stove stove;
    Stove.StoveHead stoveHeadId;

    int totalSize;
    AbsFan fan;
    MobPotOneKeyCookTaskService mobPotCookTaskService;
    IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警
    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
    RecipeDetailAutoView recipeDetailView;
    @InjectView(R.id.view_add)
    FrameLayout viewAdd;
    @InjectView(R.id.recipe_quit)
    ImageView recipeQuit;
    private IRokiDialog mDoubleClickSwitchDialog;

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
    /**
     * 横竖屏切换
     */
    boolean is_vh_switching = false;

    boolean isError  = false;

    Handler handler=new Handler();
    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isError){
                LogUtils.i(TAG ,"重新唤醒识别");
                setStep("重新唤醒识别");
                isError = false ;
            }
            handler.postDelayed(this, 5000);
        }
    };
    private TTSEngine ttsEngine;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LogUtils.i("20180522", "here is run");
//        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
//        steps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
//        guid = getActivity().getIntent().getStringExtra("Guid");
//        iDevice = Plat.deviceService.lookupChild(guid);
//        stoveflag = getActivity().getIntent().getIntExtra("stoveHeadId", 0);
//        fan = (AbsFan) iDevice;
//        stove = fan.getChildStove();
//        if (stoveflag == 0) {
//            stoveHeadId = stove.leftHead;
//        } else {
//            stoveHeadId = stove.rightHead;
//        }
//        StatusBarUtils.setTextDark(cx ,true);
//        View view = inflater.inflate(R.layout.recipe_auto_cooking, container, false);
//        ButterKnife.inject(AutoRecipePotPage.this, view);
//        initView();
//        startCook_Fan(stoveflag);
//        return view;
//    }

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
        getTitleBar().setOnTitleBarListener(this);
        ivTishi = (ImageView) findViewById(R.id.iv_tishi);
        ivFangxiang = (ImageView) findViewById(R.id.iv_fangxiang);
        ivYuyin = (CheckBox) findViewById(R.id.iv_yuyin);
        cbYinliang = (CheckBox) findViewById(R.id.cb_yinliang);
        tvRokiMessage = (TextView) findViewById(R.id.tv_roki_message);
        ivRoki = (ImageView) findViewById(R.id.iv_roki);
        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
        dialog1 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_13);
        StatusBarUtils.setTextDark(cx ,true);


        ivFangxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("step", mStepIndex);
                getActivity().getIntent().putExtra("is_vh_switching", true);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        ivTishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesageDialog();
            }
        });

//        ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
        try {
            asrEngine = DDS.getInstance().getAgent().getASREngine();
            SpeechManager.getInstance().init(Plat.app);//初始化語音播放組件
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.postDelayed(runnable, 5000);
        speakSearchRecipe();
    }

    @Override
    protected void initData() {
        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
        steps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
        guid = getActivity().getIntent().getStringExtra("Guid");
        iDevice = Plat.deviceService.lookupChild(guid);
        stoveflag = getActivity().getIntent().getIntExtra("stoveHeadId", 0);
        is_vh_switching = getActivity().getIntent().getBooleanExtra("is_vh_switching", false);
        mStepIndex = getActivity().getIntent().getIntExtra("step", 0);
        fan = (AbsFan) iDevice;
        stove = fan.getChildStove();
        if (stoveflag == 0) {
            stoveHeadId = stove.leftHead;
        } else {
            stoveHeadId = stove.rightHead;
        }


        LogUtils.i("20190610", " initView");
        totalSize = steps.size();
        mobPotCookTaskService = MobPotOneKeyCookTaskService.getInstance();
        if (steps != null) {
            recipeDetailView = new RecipeDetailAutoView(cx, steps, stove ,  getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1);
            viewAdd.addView(recipeDetailView);
            //recipeDetailView.onSpeakClick(0);
        }
        onClickCardView();
        if (is_vh_switching){
            recipeDetailView.onfresh(mStepIndex);
        }else {
            startCook_Fan(stoveflag);
        }

    }

    IRokiDialog dialog;
    IRokiDialog dialog1;
    int press = 0;

    public void onClickCardView() {
        recipeDetailView.recipeStepShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                LogUtils.i("20190523", "mStepIndex:" + mStepIndex + " position:" + position);
                if (mStepIndex == position) {
                    if ((mStepIndex + 1) == recipeDetailView.cardAdapter.getCount()) {
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
//                            recipeDetailView.onfreshViewRevert(mStepIndex);
//                            onRefresh();
//                            mobPotCookTaskService.next();
//                            press = 0;
                            if (mDoubleClickSwitchDialog != null)
                                return;
                            int position2 = position +1 ;
                            mDoubleClickSwitchDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
                            mDoubleClickSwitchDialog.setTitleText(R.string.step_switcher);
                            mDoubleClickSwitchDialog.setCanceledOnTouchOutside(false);
                            mDoubleClickSwitchDialog.setContentText(R.string.step_switcher_content);
                            mDoubleClickSwitchDialog.setOkBtn(R.string.can_btn, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDoubleClickSwitchDialog.dismiss();
                                    mDoubleClickSwitchDialog = null;
                                }
                            });
                            mDoubleClickSwitchDialog.setCancelBtn(R.string.ok_btn_in, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDoubleClickSwitchDialog.dismiss();
                                    if (stoveHeadId.getStatus() == StoveStatus.Working) {
                                        if (StringUtils.isNullOrEmpty(steps.get(position2).dc)){
                                            mobPotCookTaskService.stop();
                                            mobPotCookTaskService.setIsRunnung(false);
                                            recipeDetailView.onfreshViewRevert(position2 - 1);
                                            recipeDetailView.onfreshNoDeviceView(position2);
                                            mStepIndex = position2;
                                        }else {
                                            mobPotCookTaskService.setStep(position2);
                                            if (position2 > mStepIndex) {
                                                mStepIndex = position2;
                                                runStep(pot[0].tempUp, null);
                                            } else {
                                                runStep(pot[0].tempUp, null);
                                                currentStep(mStepIndex);
                                                mStepIndex = position2;
                                            }
                                            isFinish = true;
                                            recipeDetailView.onfresh(mStepIndex);
                                        }

                                        onSpeakClick(mStepIndex);
                                    } else {

                                        if (stoveHeadId.getStatus() == 0 || stoveHeadId.getStatus() == 3) {
                                            int s = 0;
                                            if (stoveflag == 0) {
                                                s = R.string.recipe_auto_fire_tips_content_left;
                                            } else {
                                                s = R.string.recipe_auto_fire_tips_content_right;
                                            }
                                            fireTip(s);
                                            if (position2 > mStepIndex) {
                                                mStepIndex = position2;
                                                runStep(pot[0].tempUp, null);
                                            } else {
                                                runStep(pot[0].tempUp, null);
                                                currentStep(mStepIndex);
                                                mStepIndex = position2;
                                            }
                                            isFinish = true;
                                            recipeDetailView.onfresh(mStepIndex);
                                            onSpeakClick(mStepIndex);
                                        }
                                    }
                                    mDoubleClickSwitchDialog = null;
                                }
                            });
                            mDoubleClickSwitchDialog.show();
                        }
                    }
                }
                else {
//                    press++;
//                    if (press == 2) {
//                        press = 0;
                        if (mDoubleClickSwitchDialog != null)
                            return;
                        mDoubleClickSwitchDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
                        mDoubleClickSwitchDialog.setTitleText(R.string.step_switcher);
                        mDoubleClickSwitchDialog.setCanceledOnTouchOutside(false);
                        mDoubleClickSwitchDialog.setContentText(R.string.step_switcher_content);
                        mDoubleClickSwitchDialog.setOkBtn(R.string.can_btn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDoubleClickSwitchDialog.dismiss();
                                mDoubleClickSwitchDialog = null;
                            }
                        });
                        mDoubleClickSwitchDialog.setCancelBtn(R.string.ok_btn_in, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDoubleClickSwitchDialog.dismiss();
                                if (stoveHeadId.getStatus() == StoveStatus.Working) {
                                    if (StringUtils.isNullOrEmpty(steps.get(position).dc)){
                                        mobPotCookTaskService.stop();
                                        mobPotCookTaskService.setIsRunnung(false);
                                        recipeDetailView.onfreshViewRevert(position - 1);
                                        recipeDetailView.onfreshNoDeviceView(position);
                                        mStepIndex = position;
                                    }else {
                                        mobPotCookTaskService.setStep(position);
                                        if (position > mStepIndex) {
                                            mStepIndex = position;
                                            runStep(pot[0].tempUp, null);
                                        } else {
                                            runStep(pot[0].tempUp, null);
                                            currentStep(mStepIndex);
                                            mStepIndex = position;
                                        }
                                        isFinish = true;
                                        recipeDetailView.onfresh(mStepIndex);
                                    }

                                    onSpeakClick(mStepIndex);
                                } else {

                                    if (stoveHeadId.getStatus() == 0 || stoveHeadId.getStatus() == 3) {
                                        int s = 0;
                                        if (stoveflag == 0) {
                                            s = R.string.recipe_auto_fire_tips_content_left;
                                        } else {
                                            s = R.string.recipe_auto_fire_tips_content_right;
                                        }
                                        fireTip(s);
                                        if (position > mStepIndex) {
                                            mStepIndex = position;
                                            runStep(pot[0].tempUp, null);
                                        } else {
                                            runStep2(pot[0].tempUp, null);
                                            currentStep(mStepIndex);
                                            mStepIndex = position;
                                        }
                                        isFinish = true;
                                        recipeDetailView.onfresh(mStepIndex);
                                        onSpeakClick(mStepIndex);
                                    }
                                }
                                mDoubleClickSwitchDialog = null;
                            }
                        });
                        mDoubleClickSwitchDialog.show();
//                    }
                }

            }
        });
    }

    private void setStepWork(int position){
        if (stoveHeadId.getStatus() == StoveStatus.Working) {
            mobPotCookTaskService.setStep(position);
            if (position > mStepIndex) {
                mStepIndex = position;
                runStep(pot[0].tempUp, null);
            } else {
                runStep(pot[0].tempUp, null);
                currentStep(mStepIndex);
                mStepIndex = position;
            }
            isFinish = true;
            recipeDetailView.onfresh(mStepIndex);
            onSpeakClick(mStepIndex);
        } else {
            if (stoveHeadId.getStatus() == 0 || stoveHeadId.getStatus() == 3) {
                int s = 0;
                if (stoveflag == 0) {
                    s = R.string.recipe_auto_fire_tips_content_left;
                } else {
                    s = R.string.recipe_auto_fire_tips_content_right;
                }
                fireTip(s);
                if (position > mStepIndex) {
                    mStepIndex = position;
                    runStep(pot[0].tempUp, null);
                } else {
                    runStep(pot[0].tempUp, null);
                    currentStep(mStepIndex);
                    mStepIndex = position;
                }
                isFinish = true;
                recipeDetailView.onfresh(mStepIndex);
                onSpeakClick(mStepIndex);
            }
        }
    }

    private void currentStep(int stepIndex) {
        recipeDetailView.onfreshViewRevert(stepIndex);
    }

    private void fireTip(int s) {
        flag = true;
        dialog1.setTitleText(R.string.recipe_auto_fire_tips);
        dialog1.setContentText(s);
        dialog1.show();
        dialog1.setCancelBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                flag = false;
            }
        });

    }


    private void startCook_Fan(int headId) {
        if (mobPotCookTaskService.isRunning()) {
            ToastUtils.showShort("正在烧菜中");
            return;
        }

        Stove.StoveHead head = null;
        if (stove != null) {
            head = stove.getHeadById(headId);
            Context cx = getContext();
          /*  Preconditions.checkState(stove.getValid(),
                    R.string.on_stove_way);*/

            if (!stove.isConnected()) {
                ToastUtils.showShort(R.string.stove_invalid_error);
                return;
            }

        }
        LogUtils.i("20190610", " mobPotCookTaskService.start(head, steps, id);");
        mobPotCookTaskService.start(head, steps, id);
        mStepIndex = 0;
        onSpeakClick(mStepIndex);
    }

    HashMap<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null || mobPotCookTaskService == null || !mobPotCookTaskService.isRunning())
            return;
        pot = Utils.getDefaultPot();
        try {
            if (!Objects.equal(pot[0].getID(), event.pojo.getID())) {
                return;
            }

        } catch (Exception e) {
            return;
        }

        /****开始做烹饪传包****/
        LogUtils.i("20180507", "tempUp::;" + pot[0].tempUp + "   mstep::" + mStepIndex);
        mobPotCookTaskService.RecipeTempUpEvent(pot[0].tempUp, new RuleCookTaskService.AbsCookTaskServiceCallBack() {

            @Override
            public void onCompleted(PlotRecipeNextEvent event) {

                if (event == null) return;
                try {
                    if (!"auto".equals(event.getType())) {//手动
                        recipeDetailView.onfreshViewRevert(mStepIndex - 1);
                        recipeDetailView.onfreshNoDeviceView(mStepIndex);
//                        press = 0;
                    } else {//自动
//                        press = 0;
                        StatusResult statusResult = event.getStatusResult();
                        if (statusResult.getPercentEnable()) {
                            runStep(pot[0].tempUp, statusResult.getProcessingPercent());
                        } else {
                            runStep(pot[0].tempUp, statusResult.getProcessingPercent());
                        }
                        if (statusResult.getFinished()) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                            }
                            onRefresh();
                            LogUtils.i("20190610", " mobPotCookTaskService.next()");
                            mobPotCookTaskService.next();

                        } else {
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void runStep(float tempu, Double pro) {
        LogUtils.i("20190625", "mStepIndex:" + mStepIndex);
        if (mStepIndex > 0) {
            recipeDetailView.onfreshViewRevert(mStepIndex - 1);
        }
        recipeDetailView.onfreshView((short) 20, mStepIndex, (short) -1, (short) 0, false, (int) tempu);
    }
    private void runStep2(float tempu, Double pro) {
        LogUtils.i("20190625", "mStepIndex:" + mStepIndex);
        if (mStepIndex > 0) {
            recipeDetailView.onfreshViewRevert(mStepIndex - 1);
        }
        recipeDetailView.onfreshView((short) 20, mStepIndex, (short) -1, (short) 0, false, (int) tempu);
    }
    boolean isFinish = true;

    private void cookFinish() {


        dialog.setTitleText(R.string.recipe_auto_cook_finish);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentText(R.string.recipe_auto_cook_finish_txt);
        dialog.show();
        dialog.setOkBtn("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mobPotCookTaskService.setIsRunnung(false);
                UIService.getInstance().popBack();
            }
        });
        dialog.setCancelBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        if (isFinish) {
//            shutDownDevice();
//            isFinish = false;
//        }
    }

    private void shutDownDevice() {
        mobPotCookTaskService.stop();
    }

    public void onRefresh() {

        if (mStepIndex < steps.size() - 1) {
            mStepIndex += 1;
            LogUtils.i("20180410", "mStepIndex:" + mStepIndex);
            recipeDetailView.onfresh(mStepIndex);
        } else {
            LogUtils.i("20180410", "onRefresh:" + mStepIndex);
            recipeDetailView.onfreshViewRevert(mStepIndex);
            cookFinish();
        }

    }


//    @OnClick(R.id.recipe_quit)
    public void QuickWork() {
        dialog.setTitleText(R.string.is_stop_cook_recipe_out);
        dialog.setContentText(R.string.recipe_finish_work_context);
        dialog.show();
        dialog.setOkBtn(R.string.finish_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mobPotCookTaskService.stop();
                mobPotCookTaskService.setIsRunnung(false);
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

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.show("灶具已离线，连接后才可进行自动烹饪", Toast.LENGTH_SHORT);
        }
    }


    boolean flag = false;

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20190523", "mStepIndex:" + mStepIndex);

        if (dialog1 != null && !dialog1.isShow()) {
            return;
        }

        if (stoveHeadId == event.pojo.leftHead && event.pojo.leftHead.status != 0) {
            if (dialog1 != null && dialog1.isShow()) {
                dialog1.dismiss();
                flag = false;

            }

            LogUtils.i("20190523", "if StoveStatusChangedEvent:");
            if (flag) {
                return;
            }

            if (steps.get(mStepIndex).getDc().equals("")) {
                return;
            }

            if (event.pojo.getDt().equals("R9B39")) {
                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {

                    if ((mStepIndex - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(mStepIndex - 1);
                    }

                    mobPotCookTaskService.setStep(mStepIndex);
                    recipeDetailView.onfresh(mStepIndex);
                }
            } else {
                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {

                    LogUtils.i("20180228", "here is run");
                    if ((mStepIndex - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(mStepIndex - 1);
                    }
                    LogUtils.i("20190521", "left_hid_:" + " mStepIndex:" + mStepIndex);
                    mobPotCookTaskService.setStep(mStepIndex);
                    recipeDetailView.onfresh(mStepIndex);
                    runStep2(pot[0].tempUp, null);
                }
            }
            if (stoveHeadId.getStatus() == 0) {

                ToastUtils.show("灶具已关闭", Toast.LENGTH_SHORT);
            }
        }
        if (stoveHeadId == event.pojo.rightHead && event.pojo.rightHead.status != 0) {
            if (dialog1 != null && dialog1.isShow()) {
                dialog1.dismiss();
                flag = false;
            }
            LogUtils.i("20190521", "steps:" + steps.get(mStepIndex).getDc());
            if (flag) {
                return;
            }

            if (steps.get(mStepIndex).getDc().equals("")) {
                return;
            }

            if (event.pojo.getDt().equals("R9B39")) {
                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {

                    if ((mStepIndex - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(mStepIndex - 1);
                    }
                    mobPotCookTaskService.setStep(mStepIndex);
                    recipeDetailView.onfresh(mStepIndex);

                }
            } else {

                if (stoveHeadId.getStatus() != 0 && stoveHeadId.getStatus() == 1) {

                    if ((mStepIndex - 1) < 0) {

                    } else {
                        recipeDetailView.onfreshViewRevert(mStepIndex - 1);
                    }
                    LogUtils.i("20190521", "right_hid_:" + " pos:" + mStepIndex);

                    mobPotCookTaskService.setStep(mStepIndex);
                    recipeDetailView.onfresh(mStepIndex);

                }
            }
            if (stoveHeadId.getStatus() == 0) {

                ToastUtils.show("灶具已关闭", Toast.LENGTH_SHORT);
            }

        }
    }

    @Override
    public void onLeftClick(View view) {
        super.onLeftClick(view);
//        QuickWork();
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (recipeDetailView != null) {
            recipeDetailView = null;
        }
        if (mobPotCookTaskService != null) {
            mobPotCookTaskService = null;
        }
        SpeechManager.getInstance().dispose();
        handler.removeCallbacks(runnable);
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
                jsonObject = new JSONObject(s);
                String text = jsonObject.get("text").toString();
                if (NEXT.equals(text)) {
                    setStep(text);
                } else if (LAST.equals(text) || LAST_2.equals(text)) {
                    setStep(text);
                } else if (AGAIN.equals(text) || AGAIN_2.equals(text)) {
                    setStep(text);
                } else if (END.equals(text) || END_2.equals(text)) {
                    setStep(text);
                } else {
                    setStep("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                speech() ;
            }
        }

        @Override
        public void error(String s) {
            LogUtils.i(TAG, "error:" + s);
//            speech() ;
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
                if (NEXT.equals(speek)) {
                    if (mStepIndex == recipeDetailView.adapter.getCount() - 1) {
                        speech();
                        QuickWork();
                        return;
                    }
                    int position = mStepIndex + 1;
                    setStepWork(position);
                } else if (LAST.equals(speek) || LAST_2.equals(speek)) {
                    if (mStepIndex == 0) {
//                        speech();
                        onSpeakClick("已经是第一步");
                        return;
                    }
                    int position = mStepIndex - 1;
                    setStepWork(position);
                } else if (AGAIN.equals(speek) || AGAIN_2.equals(speek)) {
                    onSpeakClick(mStepIndex);
                } else if (END.equals(speek) || END_2.equals(speek)) {
                    QuickWork();
                } else {
                    speech();
                }
            }
        });
    }

    //语音播报
    public void onSpeakClick(final int mStepIndex) {
        if (!cbYinliang.isChecked()) {
//            speech();
            return;
        }

        if (StringUtils.isNullOrEmpty(recipeDetailView.cookStepTemp.get(mStepIndex).desc))
            return;
        try {
            onSpeakClick(recipeDetailView.cookStepTemp.get(mStepIndex).desc);
//            tvRokiMessage.setText("ROKI正在说话，请稍后...");
//            Glide.with(getActivity())
//                    .load(R.drawable.ic_play)
//                    .into(ivRoki);
//            SpeechManager.getInstance().startSpeaking2(recipeDetailView.cookStepTemp.get(mStepIndex).desc, new SpeechManager.SpeakComple() {
//                @Override
//                public void comple() {
//                    setStep("");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            speech();
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
            SpeechManager.getInstance().startSpeaking2(message, new SpeechManager.SpeakComple() {
                @Override
                public void comple() {
                    setStep("唤醒");
                }
            });
//            try {
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
//            } catch (DDSNotInitCompleteException e) {
//                e.printStackTrace();
//                setStep("唤醒");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            speech();
        }

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
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeSpeakBack".equals(event.getPageName())){
            QuickWork();
        }
    }
}
