package com.robam.roki.ui.page;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.plat.Plat;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.PageBack2Event;
import com.legent.plat.events.PageBackEvent;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.view.RecipeDetailAuto2View;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.InjectView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by yinwei on 2017/11/27.
 * ????????????old
 */

public class RecipeCookNoDevicePage extends AbsDUIPage {
    //    @InjectView(R.id.recipe_quit)
//    ImageView recipeQuit;

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(999, 0));
    @InjectView(R.id.view_add)
    FrameLayout viewAdd;


    /**
     * ??????
     */
    private ImageView ivTishi;
    /**
     * ??????
     */
    private ImageView ivFangxiang;
    /**
     * ??????
     */
    private CheckBox ivYuyin;
    /**
     * ??????
     */
    private CheckBox cbYinliang;
    /**
     * ROKI??????
     */
    private TextView tvRokiMessage;
    /**
     * ROKI??????
     */
    private ImageView ivRoki;

    ArrayList<CookStep> cookSteps;
    Long id;


    RecipeDetailAuto2View recipeDetailView;
    int prestep;
    /**
     * ???????????????
     */
    boolean is_vh_switching = false;

    boolean isError = false;

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
//            if (isError){
                LogUtils.i(TAG ,"??????????????????");
                setStep("??????????????????");
//                isError = false ;
//            }
            handler.postDelayed(this, 10000);
        }
    };
    private Recipe recipe;
    private String imgLarge;
//    private TTSEngine ttsEngine;
    /**
     * ??????????????????
     */
    private boolean isDuiError = false;

    private void nextStep() {
        onSpeakClick(step);
        recipeDetailView.onfresh(step);
//        recipeDetailView.onfreshNoDeviceView(step);
    }


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
//        StatusBarUtils.setTextDark(getContext(), true);
//        StatusBarUtils.setColor(cx, Color.WHITE);
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
                getActivity().getIntent().putExtra("ivYuyin", ivYuyin.isChecked());
                getActivity().getIntent().putExtra("cbYinliang", cbYinliang.isChecked());
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

        try {
//            DDS.getInstance().doAuth();
            asrEngine = DDS.getInstance().getAgent().getASREngine();
            //???????????????????????????
            SpeechManager.getInstance().init(Plat.app);
        } catch (Exception e) {
            e.printStackTrace();
        }

//         ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//        try {
//            ttsEngine.setMode( TTSEngine.CLOUD);
//            ttsEngine.setSpeaker("gqlanfp");
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }

        handler.postDelayed(runnable, 10000);
//        speakSearchRecipe();

    }

    @Override
    protected void initData() {
        id = getActivity().getIntent().getLongExtra("RecipeID", 0);
        cookSteps = (ArrayList<CookStep>) getActivity().getIntent().getSerializableExtra("list");
        recipe = (Recipe) getActivity().getIntent().getSerializableExtra("recipe");
        step = getActivity().getIntent().getIntExtra("step", 0);
        is_vh_switching = getActivity().getIntent().getBooleanExtra("is_vh_switching", false);
        imgLarge = getActivity().getIntent().getStringExtra("imgLarge");

        boolean isIvYuyin = getActivity().getIntent().getBooleanExtra("ivYuyin", true);
        ivYuyin.setChecked(isIvYuyin);
        boolean isCbYinliang = getActivity().getIntent().getBooleanExtra("cbYinliang", true);
        cbYinliang.setChecked(isCbYinliang);

        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_12);
        recipeDetailView = new RecipeDetailAuto2View(cx, cookSteps, null, getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1, step);
        viewAdd.addView(recipeDetailView);
        onClickCardView();
        if (!is_vh_switching) {
            onSpeakClick(step);
        }
    }


    public void onClickCardView() {
        recipeDetailView.recipeStepShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (step == position) {
//                    stepToNext2(position);
                } else {
                    stepToNext(position);
                }
            }
        });
    }


    //??????
//    private void singleOnClick(int position) {
//        if (step == position) {
//            recipeDetailView.onfreshViewRevert(step);
//            next();
//        }
//    }


    //?????????????????????
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

                step = pos;
                recipeDetailView.onfreshViewRevert(prestep);
                nextStep();

                dialog.dismiss();
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
     * ??????
     *
     * @param pos
     */
    public void stepToNext2(int pos) {
        if (step == recipeDetailView.recipeStepShow.getCount()) {
            prestep = step - 1;
        } else {
            prestep = step;
        }

        step = pos;
        recipeDetailView.onfreshViewRevert(prestep);
        nextStep();

        dialog.dismiss();


    }

    public void quickWork() {
        dialog.setTitleText(R.string.is_stop_cook_recipe_out);
        dialog.setContentText(R.string.recipe_finish_work_context);
        dialog.show();
        dialog.setOkBtn(R.string.finish_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    UIService.getInstance().popBack();
                    requireActivity().finish();
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

    int step = 0;

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        try {
            SpeechManager.getInstance().dispose();
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onRightClick(View view) {
        quickWork();
    }

    @Override
    public void onLeftClick(View view) {
        super.onLeftClick(view);
        showFloatingWindow();
//        EventUtils.postEvent(new PageBack2Event("min", step));

    }

    /**
     * ??????
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
        LogUtils.i(TAG, "-------- onResume");
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i("RecipeCookPage", "-------- onConfigurationChanged");
    }


    private static final String TAG = "RecipeCookPage";

    public static String RECIPE = "RECIPE";
    public static String NEXT = "?????????";
    public static String LAST = "?????????";
    public static String LAST_2 = "?????????";
    public static String AGAIN = "????????????";
    public static String AGAIN_2 = "????????????";
    public static String PAUSE = "??????";
    public static String END = "??????";
    public static String END_2 = "??????";
    /**
     * ????????????
     */
    private ASREngine asrEngine;
    /**
     * ??????????????????
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
//            setStep("??????");
        }

        @Override
        public void partialResults(String s) {
            LogUtils.i(TAG, "partialResults:" + s);

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
                isDuiError = true;
                setStep("??????");
                isError = true ;
            }
//            finally {
//                setStep(text);
//            }
        }

        @Override
        public void error(String s) {
            LogUtils.i(TAG, "error:" + s);
            isError = true ;
            setStep("??????");
//            speech();

        }

        @Override
        public void rmsChanged(float v) {
            LogUtils.i(TAG, "rmsChanged:" + v);
        }
    };

    private void setStep(String speek) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (speek.contains(NEXT)) {
                            if (step == recipeDetailView.adapter.getCount() - 1) {
                                onSpeakClick("?????????????????????");
                                return;
                            }
                            int position = step + 1;
                            stepToNext2(position);
                        } else if (speek.contains(LAST) || speek.contains(LAST_2)) {
                            if (step == 0) {
                                onSpeakClick("??????????????????");
                                return;
                            }
                            int position = step - 1;
                            stepToNext2(position);
                        } else if (speek.contains(AGAIN) || speek.contains(AGAIN_2)) {
//                        speech();
                            onSpeakClick(step);
                        } else if (speek.contains(END) || speek.contains(END_2)) {
//                    quickWork();
                            UIService.getInstance().popBack();
                            requireActivity().finish();
                        } else {
                            speech();
                        }
                    } catch (Exception e) {
                        e.getMessage();
//                    speech();
                    isError = true ;
                        setStep("??????");
                    }

                }
            });
        } catch (Exception e) {
            e.getMessage();
            isError = true ;
            setStep("??????");
        }

    }

    /**
     * ????????????
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
                isError =false;
            }
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
            isError = true ;
            setStep("??????");
            LogUtils.i(TAG, "DDSNotInitCompleteException " + e.toString());
        }
    }

    /**
     * ??????????????????
     */
    private void speech() {
        tvRokiMessage.setText("?????????ROKI?????????...");
//        if (ivRoki != null){
        Glide.with(getActivity())
                .load(R.drawable.ic_discern)
                .into(ivRoki);
//        }
        speakSearchRecipe();

    }

    //????????????
    public void onSpeakClick(final int step) {
        if (!cbYinliang.isChecked()) {
            speech();
            return;
        }

        if (StringUtils.isNullOrEmpty(recipeDetailView.cookStepTemp.get(step).desc)) {
            return;
        }
        try {
//            tvRokiMessage.setText("ROKI????????????????????????...");
//            Glide.with(getActivity())
//                    .load(R.drawable.ic_play)
//                    .into(ivRoki);
            onSpeakClick(recipeDetailView.cookStepTemp.get(step).desc);
//            SpeechManager.getInstance().startSpeaking2(recipeDetailView.cookStepTemp.get(step).desc, new SpeechManager.SpeakComple() {
//                @Override
//                public void comple() {
//                    setStep("??????");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            setStep("??????");
        }
    }

    //????????????
    public void onSpeakClick(final String message) {
        if (!cbYinliang.isChecked()) {
            speech();
            return;
        }
        try {
            tvRokiMessage.setText("ROKI????????????????????????...");
            Glide.with(getActivity())
                    .load(R.drawable.ic_play)
                    .into(ivRoki);
            try {
//                if (ttsEngine == null){
//                ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//                ttsEngine.setMode(TTSEngine.LOCAL);
//                ttsEngine.setSpeaker("gqlanfp");
//                }
                isError = false;
                SpeechManager.getInstance().startSpeaking2(message, new SpeechManager.SpeakComple() {
                    @Override
                    public void comple() {
                        LogUtils.i(TAG, "??????????????????");
//                        speech();
                        setStep("??????");
                        isError = true;
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
//                        setStep("??????");
//                    }
//
//                    @Override
//                    public void error(String s) {
//                        LogUtils.i("TTSEngine" , "error--------" + s );
//                        setStep("??????");
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
                setStep("??????");
//                isError = true ;
            }
//            SpeechManager.getInstance().startSpeaking2(message, new SpeechManager.SpeakComple() {
//                @Override
//                public void comple() {
//                    setStep("");
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
            setStep("??????");
//            isError = true ;
        }
    }

    @SuppressLint("InflateParams")
    public void showFloatingWindow() {
        Bundle bd = new Bundle();
        bd.putSerializable("list", cookSteps);
//        EventUtils.postEvent(new FloatHelperEvent(id, bd, step, recipe.imgLarge));
        EventUtils.postEvent(new FloatHelperEvent(id, bd, step, recipe != null ? recipe.imgLarge : imgLarge));
    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeSpeakBack".equals(event.getPageName())){
            quickWork();
        }
    }
}
