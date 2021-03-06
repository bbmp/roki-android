package com.robam.roki.ui.page;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.text.StringPrepParseException;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSAuthListener;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.DDSInitListener;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CallResultApiEvent;
import com.robam.common.events.ClearTextEvent;
import com.robam.common.events.QueryResultApiEvent;
import com.robam.roki.R;
import com.robam.roki.model.bean.MessageBean;
import com.robam.roki.observer.DuiCommandApiObserver;
import com.robam.roki.observer.DuiMessageObserver;
import com.robam.roki.observer.DuiNativeApiObserver;
import com.robam.roki.observer.DuiUpdateObserver;
import com.robam.roki.ui.adapter.DialogAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.helper3.TaskHelper;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.suspendedball.FloatingView;

import java.util.LinkedList;
import java.util.UUID;

import butterknife.ButterKnife;

import static com.robam.roki.observer.DuiUpdateObserver.FINISH;


public abstract class AbsDUIPage extends MyBasePage<MainActivity> implements DuiUpdateObserver.UpdateCallback,
        DuiMessageObserver.MessageCallback
//        , DuiCommandApiObserver.CallCallback
{

    public MyHandler mHandler = new MyHandler();

    private DDSReceiver mInitReceiver;
    protected static LinkedList<MessageBean> mMessageList = new LinkedList<>();// ??????????????????
    //    protected DuiNativeApiObserver mNativeApiObserver = new DuiNativeApiObserver();//???????????????????????????
    protected DuiUpdateObserver mUpdateObserver = new DuiUpdateObserver();// dds???????????????
    protected DuiMessageObserver mMessageObserver = new DuiMessageObserver();// ???????????????
    //    protected DuiCommandApiObserver mCommadApiObserver = new DuiCommandApiObserver();
    protected DialogAdapter mDialogAdapter;  // ??????UI??????????????????DialogAdapter??????
    private static final String TAG = "AbsDUIPage";
    private int mAuthCount = 0;// ????????????,????????????????????????

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventUtils.regist(this);
//        EventUtils.postEvent(this);
////        DDS.getInstance().setDebugMode(2); //????????????????????????sdk?????????????????????????????????????????????
//        DDS.getInstance().init(getContext().getApplicationContext(), createConfig(), mInitListener, mAuthListener);
//        LogUtils.i(TAG, "onCreate  DDS.getInstance().init");
////        initDuiReceiver();
//        try {
//            DDS.getInstance().doAuth();
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected int getLayoutId() {
//        return 0;
//    }

    @Override
    protected void initView() {
//        DDS.getInstance().setDebugMode(2); //????????????????????????sdk?????????????????????????????????????????????
        DDS.getInstance().init(getContext().getApplicationContext(), createConfig(), mInitListener, mAuthListener);
        LogUtils.i(TAG, "onCreate  DDS.getInstance().init");
        initDuiReceiver();
        try {
            DDS.getInstance().doAuth();

//            DDS.getInstance().getAgent().getASREngine();
//            ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
//            ttsEngine.setMode( TTSEngine.CLOUD);
//            ttsEngine.setSpeaker("gqlanfp");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    protected void initData() {
//
//    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        // ?????????????????????
        mMessageObserver.regist(this, mMessageList);
//        sendHiMessage();
        refreshTv("????????????...");
        enableWakeup();

        try {
            //?????????????????????????????????
            DDS.getInstance().getAgent().getTTSEngine().setStreamType(AudioManager.STREAM_MUSIC);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("202006110123", "onPause: ");
        mMessageObserver.unregist();
        refreshTv("????????????...");
        disableWakeup();
        super.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        mMessageList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMessageList.clear();
        Log.d(TAG, "onStop: ");

    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy: ");
        unregistObserver();
        super.onDestroy();
    }

    private void initRegister() {
        try {
            if (mUpdateObserver != null) {
                mUpdateObserver.regist(this);
            }
        } catch (Exception e) {
            e.getMessage();
        }

//        mNativeApiObserver.regist(this);
//        mCommadApiObserver.register(this);
    }

    private void unregistObserver() {
        if (mInitReceiver != null) {
            getContext().unregisterReceiver(mInitReceiver);
            mInitReceiver = null;
        }
        if (mUpdateObserver != null) {
            mUpdateObserver.unregist();
        }

//        mNativeApiObserver.unregist();
//        mCommadApiObserver.unregister();
    }

    // ??????dds????????????
    public DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // ???????????????
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278582915"); // ??????ID -- ??????
        config.addConfig(DDSConfig.K_USER_ID, "lyb");  // ??????ID -- ??????
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // ????????????????????? -- ??????
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "633eed0b2ac0b5d9862b1cb8d8f28c40");// Product Key -- ??????
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "1f10cffab30f470510d6336d9f47b4fb");// Product Secre -- ??????
        config.addConfig(DDSConfig.K_API_KEY, "6cb41d6dc2de6cb41d6dc2de6039f8cb");  // ????????????????????????????????????????????????????????? -- ??????

        return config;
    }

    @Subscribe
    public void onEvent(ClearTextEvent clearTextEvent) {
        if (clearTextEvent.isClear()) {
            if (mMessageList != null && mMessageList.size() > 0) {
                for (int i = 0; i < mMessageList.size(); i++) {
                    mMessageList.remove(i);
                }
            }
        }
    }


    // dds?????????????????????,??????init????????????
    public DDSInitListener mInitListener = new DDSInitListener() {
        @Override
        public void onInitComplete(boolean isFull) {

            LogUtils.i(TAG, "?????????????????????????????? isFull:" + isFull);
            try {
                if (isFull) {
                    getContext().sendBroadcast(new Intent("ddsdemo.intent.action.init_complete"));
                    // ??????CommandObserver,????????????DUI?????????????????????????????????????????????, ?????????CommandObserver??????????????????commands.
//                    DDS.getInstance().getAgent().subscribe(new String[]{"roki.fan.power", "roki.fan.level" + "roki.fan.light"}, mNativeApiObserver);

                    //?????????????????????????????????
                    if (mMessageList != null && mMessageList.size() > 0) {
                        LogUtils.i(TAG, "mMessageList clear size" + mMessageList.size());
                        mMessageList.clear();
                    }
                } else {
                    try {
                        TaskHelper.newFixedThreadPool(1).execute(new Runnable() {
                            @Override
                            public void run() {
                                initRegister();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "?????????????????????");
            }

        }

        @Override
        public void onError(int what, final String msg) {
            Log.e(TAG, "Init onError: " + what + ", error: " + msg);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    // dds?????????????????????,??????auth????????????
    public DDSAuthListener mAuthListener = new DDSAuthListener() {
        @Override
        public void onAuthSuccess() {
            LogUtils.i(TAG, "onAuthSuccess");
            // ?????????????????????????????????
//            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_success"));
        }

        @Override
        public void onAuthFailed(final String errId, final String error) {
            LogUtils.i(TAG, "onAuthFailed: " + errId + ", error:" + error);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext().getApplicationContext(),
                            "????????????:" + errId + ":\n" + error + "\n?????????????????????", Toast.LENGTH_LONG).show();
                }
            });
            // ?????????????????????????????????
//            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_failed"));
        }
    };


    @Override
    public void onUpdate(final int type, String result) {
        LogUtils.i(TAG, "onUpdate type:" + type + " result:" + result);
        try {
            if (type == FINISH) {
                DDS.getInstance().doAuth();
            }
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
//        setEnabled(type);
//        refreshTv(result);

    }

    protected void setEnabled(int type) {

    }

    // ??????????????????
    protected void refreshTv(final String text) {
        LogUtils.i(TAG, "refreshTv text:" + text);
    }

    protected void initDuiView() {


    }

    protected void initDuiReceiver() {
        // ??????????????????????????????????????????
        IntentFilter filter = new IntentFilter();
        filter.addAction("ddsdemo.intent.action.init_complete");
        filter.addAction("ddsdemo.intent.action.auth_failed");
        mInitReceiver = new DDSReceiver();
        getContext().registerReceiver(mInitReceiver, filter);
    }
//
//    @Override
//    public void onQueryResult(String nativeApi, String data) {
//        LogUtils.i(TAG, "nativeApi:" + nativeApi + " data:" + data);
//        EventUtils.postEvent(new QueryResultApiEvent(nativeApi, data));
//    }

//    @Override
//    public void onCallResult(String commandApi, String data) {
//        LogUtils.i(TAG, "commandApi:" + commandApi + " data:" + data);
//        EventUtils.postEvent(new CallResultApiEvent(commandApi, data));
//    }


    @Override
    public void onMessage() {
        duiNotifyDataSetChanged();
    }


    @Override
    public void onState(String state) {
        LogUtils.i(TAG, "state:" + state.toString());
        switch (state) {
            case "avatar.silence":
                refreshTv("????????????...");
                break;
            case "avatar.listening":
                refreshTv("?????????...");
                break;
            case "avatar.understanding":
                refreshTv("?????????...");
                break;
            case "avatar.speaking":
                refreshTv("???????????????...");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    class DDSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            LogUtils.i(TAG, "DDSReceiver name:" + intent.getAction());
            String name = intent.getAction();
            if (!TextUtils.isEmpty(name) && name.equals("ddsdemo.intent.action.init_complete")) {
                doAutoAuth();
            } else if (TextUtils.equals(intent.getAction(), "ddsdemo.intent.action.auth_failed")) {
                doAutoAuth();
            }
        }
    }

    //??????
    protected void enableWakeup() {
//        try {
//            if (PreferenceUtils.getBool("speech_switch", false)) {
//                DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
//            }
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }
    }

    // ????????????, ??????????????????????????????
    protected void disableWakeup() {
        try {
            DDS.getInstance().getAgent().stopDialog();
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // dds?????????????????????,???????????????????????????,??????????????????????????????
    protected void sendHiMessage() {
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            // ??????????????????
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            // ??????????????????
            minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        String hiStr = "";
        if (wakeupWords != null && minorWakeupWord != null) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], minorWakeupWord);
        } else if (wakeupWords != null && wakeupWords.length == 2) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], wakeupWords[1]);
        } else if (wakeupWords != null && wakeupWords.length > 0) {
            hiStr = getString(R.string.hi_str, wakeupWords[0]);
        }

        LogUtils.i(TAG, "histr = " + hiStr);
//        if (!TextUtils.isEmpty(hiStr)) {
//            MessageBean bean = new MessageBean();
//            bean.setText(hiStr);
//            bean.setType(MessageBean.TYPE_OUTPUT);
//
//            if (mMessageList != null && mMessageList.size() > 0) {
//                for (int i = 0; i < mMessageList.size(); i++) {
//                    if (MessageBean.TYPE_OUTPUT == mMessageList.get(i).getType()) {
//                        mMessageList.remove(i);
//                    }
//                }
//            }
//            mMessageList.add(bean);
//            duiNotifyItemInserted();
//
//        }
    }

    protected void duiNotifyItemInserted() {


    }

    // ??????ui????????????
    protected void duiNotifyDataSetChanged() {

    }

    // ??????????????????
    private void doAutoAuth() {
        // ??????????????????5???,??????5?????????????????????,?????????????????????
        if (mAuthCount < 5) {
            try {
                DDS.getInstance().doAuth();
                mAuthCount++;
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void setStateBarFixer2() {
        if (rootView != null) {
            View mStateBarFixer = rootView.findViewById(com.legent.ui.R.id.status_bar_fix);
            if (mStateBarFixer != null) {
                mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(getActivity())));
                mStateBarFixer.setBackgroundColor(Color.WHITE);
            } else {
//                Class<? extends BasePage> aClass = getClass();
//                LogUtils.i("class_name" , aClass.getName());
//                if (!"com.robam.roki.ui.page.HomePage".equals(aClass.getName()) && !"com.robam.roki.ui.page.WelcomePage".equals(aClass.getName()) ){
//                    setMargins(rootView ,0 ,getStatusBarHeight(getActivity()) ,0 , 0 );
//                }
            }
        }
    }
}
