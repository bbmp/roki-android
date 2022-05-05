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
    protected static LinkedList<MessageBean> mMessageList = new LinkedList<>();// 当前消息容器
    //    protected DuiNativeApiObserver mNativeApiObserver = new DuiNativeApiObserver();//本地方法回调监听器
    protected DuiUpdateObserver mUpdateObserver = new DuiUpdateObserver();// dds更新监听器
    protected DuiMessageObserver mMessageObserver = new DuiMessageObserver();// 消息监听器
    //    protected DuiCommandApiObserver mCommadApiObserver = new DuiCommandApiObserver();
    protected DialogAdapter mDialogAdapter;  // 各种UI控件的实现在DialogAdapter类里
    private static final String TAG = "AbsDUIPage";
    private int mAuthCount = 0;// 授权次数,用来记录自动授权

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventUtils.regist(this);
//        EventUtils.postEvent(this);
////        DDS.getInstance().setDebugMode(2); //在调试时可以打开sdk调试日志，在发布版本时，请关闭
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
//        DDS.getInstance().setDebugMode(2); //在调试时可以打开sdk调试日志，在发布版本时，请关闭
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
        // 注册消息监听器
        mMessageObserver.regist(this, mMessageList);
//        sendHiMessage();
        refreshTv("等待唤醒...");
        enableWakeup();

        try {
            //更改设置音量大小为媒体
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
        refreshTv("等待唤醒...");
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

    // 创建dds配置信息
    public DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // 基础配置项
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278582915"); // 产品ID -- 必填
        config.addConfig(DDSConfig.K_USER_ID, "lyb");  // 用户ID -- 必填
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // 产品的发布分支 -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "633eed0b2ac0b5d9862b1cb8d8f28c40");// Product Key -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "1f10cffab30f470510d6336d9f47b4fb");// Product Secre -- 必填
        config.addConfig(DDSConfig.K_API_KEY, "6cb41d6dc2de6cb41d6dc2de6039f8cb");  // 产品授权秘钥，服务端生成，用于产品授权 -- 必填

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


    // dds初始状态监听器,监听init是否成功
    public DDSInitListener mInitListener = new DDSInitListener() {
        @Override
        public void onInitComplete(boolean isFull) {

            LogUtils.i(TAG, "思必驰语音初始化成功 isFull:" + isFull);
            try {
                if (isFull) {
                    getContext().sendBroadcast(new Intent("ddsdemo.intent.action.init_complete"));
                    // 注册CommandObserver,用于处理DUI平台技能配置里的客户端动作指令, 同一个CommandObserver可以处理多个commands.
//                    DDS.getInstance().getAgent().subscribe(new String[]{"roki.fan.power", "roki.fan.level" + "roki.fan.light"}, mNativeApiObserver);

                    //开唤醒，调用后才能唤醒
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
                Log.d(TAG, "思必驰广播失败");
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

    // dds认证状态监听器,监听auth是否成功
    public DDSAuthListener mAuthListener = new DDSAuthListener() {
        @Override
        public void onAuthSuccess() {
            LogUtils.i(TAG, "onAuthSuccess");
            // 发送一个认证成功的广播
//            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_success"));
        }

        @Override
        public void onAuthFailed(final String errId, final String error) {
            LogUtils.i(TAG, "onAuthFailed: " + errId + ", error:" + error);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext().getApplicationContext(),
                            "授权错误:" + errId + ":\n" + error + "\n请查看手册处理", Toast.LENGTH_LONG).show();
                }
            });
            // 发送一个认证失败的广播
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

    // 更新按钮状态
    protected void refreshTv(final String text) {
        LogUtils.i(TAG, "refreshTv text:" + text);
    }

    protected void initDuiView() {


    }

    protected void initDuiReceiver() {
        // 添加一个初始成功的广播监听器
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
                refreshTv("等待唤醒...");
                break;
            case "avatar.listening":
                refreshTv("监听中...");
                break;
            case "avatar.understanding":
                refreshTv("理解中...");
                break;
            case "avatar.speaking":
                refreshTv("播放语音中...");
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

    //唤醒
    protected void enableWakeup() {
//        try {
//            if (PreferenceUtils.getBool("speech_switch", false)) {
//                DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
//            }
//        } catch (DDSNotInitCompleteException e) {
//            e.printStackTrace();
//        }
    }

    // 关闭唤醒, 调用后将无法语音唤醒
    protected void disableWakeup() {
        try {
            DDS.getInstance().getAgent().stopDialog();
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // dds初始化成功之后,展示一个打招呼消息,告诉用户可以开始使用
    protected void sendHiMessage() {
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            // 获取主唤醒词
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            // 获取副唤醒词
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

    // 更新ui列表展示
    protected void duiNotifyDataSetChanged() {

    }

    // 执行自动授权
    private void doAutoAuth() {
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
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
