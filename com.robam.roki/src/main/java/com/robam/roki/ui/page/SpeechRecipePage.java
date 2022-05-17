package com.robam.roki.ui.page;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UI;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.MessageBean;
import com.robam.roki.observer.DuiUpdateObserver;
import com.robam.roki.ui.adapter.DialogAdapter;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.view.RecordButton;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static butterknife.ButterKnife.*;


/**
 * @author wwq
 * des：语音搜索界面
 */
public class SpeechRecipePage extends AbsDUIPage {

    private static final String TAG = "SpeechRecipePage";

    private RecordButton mRecordButton;

    @InjectView(R.id.iv_back_speak_recipe)
    ImageView backSpeakRecipe;

    @InjectView(R.id.iv_recipe_speaking)
    ImageView ivRecipeSpeaking;

    @InjectView(R.id.tv_press_speak_note)
    TextView tvPressSpeakNote;

    @InjectView(R.id.rv_speak_content)
    RecyclerView rvSpeakContent;
    private String searchRecipeKeyWord;
    /**
     * 当前消息容器
     */
    private LinkedList<MessageBean> mMessageData = new LinkedList<>();


    public static SpeechRecipePage newInstance(String param1, String param2) {
        SpeechRecipePage fragment = new SpeechRecipePage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        StatusBarUtils.setColor(cx, getResources().getColor(R.color.transparent));
//        StatusBarUtils.setTextDark(cx, false);
//        View view = inflater.inflate(R.layout.fragment_speech_recipe_page, container, false);
//        inject(this, view);
//        mRecordButton = view.findViewById(R.id.btn_record);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        mDialogAdapter = new DialogAdapter(mMessageData);
//        // 必须在setAdapter之后调用才生效
//        rvSpeakContent.setAdapter(mDialogAdapter);
//        rvSpeakContent.setLayoutManager(linearLayoutManager);
//        initRecordEvent();
//        initDuiView();
////        initDuiReceiver();
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_speech_recipe_page;
    }

    @Override
    protected void initView() {
        super.initView();
//        StatusBarUtils.setColor(cx, getResources().getColor(R.color.transparent));
//        StatusBarUtils.setTextDark(cx, false);
    }

    @Override
    protected void initData() {
        mRecordButton = findViewById(R.id.btn_record);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mDialogAdapter = new DialogAdapter(mMessageData);
        // 必须在setAdapter之后调用才生效
        rvSpeakContent.setAdapter(mDialogAdapter);
        rvSpeakContent.setLayoutManager(linearLayoutManager);
        initRecordEvent();
        initDuiView();
    }

    private void initRecordEvent() {
        //设置触摸延迟时间，区分点击还是长按，单位是毫秒
        mRecordButton.setTouchDelay(300);
        //设置最大录制时间，单位为毫秒
        mRecordButton.setRecordTime(9000);
        //设置最小录制时间，单位为毫秒
        mRecordButton.setMinRecordTime(2000);
        mRecordButton.setRecordButtonListener(new RecordButton.RecordButtonListener() {
            @Override
            public void onClick() {
                Log.e(TAG, "onClick: ");
//                Toast.makeText(getContext(), "点击了按钮", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick() {
                Log.e(TAG, "onLongClick: ");
                MessageBean messageBean = new MessageBean();
                messageBean.setType(MessageBean.TYPE_INPUT);
                messageBean.setText(". . . . . .");
//                mMessageData.add(mMessageList.get(0));
                mMessageData.add(messageBean);
                rvSpeakContent.smoothScrollToPosition(mMessageData.size());
                mDialogAdapter.notifyDataSetChanged();
                speakSearchRecipe();
            }

            @Override
            public void onLongClickFinish(int result) {
                try {
                    stopSpeech();
                    LogUtils.i(TAG, "onLongClickFinish: " + result);
                } catch (Exception e) {
                    LogUtils.i(TAG, "DDSNotInitCompleteException " + e.toString());
                }
                switch (result) {
                    case RecordButton.NORMAL:
//                        noSpeech();
//                        Toast.makeText(getContext(), "长按结束", Toast.LENGTH_SHORT).show();
                        break;
                    case RecordButton.RECORD_SHORT:
//                        Toast.makeText(getContext(), "录制时间过短", Toast.LENGTH_SHORT).show();
                        break;
                        case RecordButton.RECORD_SUCCESS:
                            noSpeech();
                            break;
                    default:
                }
            }
        });
    }

    private void speakSearchRecipe() {
        try {
            DDS.getInstance().getAgent().getASREngine().startListening(new ASREngine.Callback() {
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
                        noSpeech();
                    }
                }

                @Override
                public void finalResults(String s) {
                    LogUtils.i(TAG, "finalResults:" + s);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        searchRecipeKeyWord = jsonObject.get("text").toString();
                        LogUtils.i(TAG, "searchRecipeKeyWord:" + searchRecipeKeyWord);
                        searchRecipeByWord(searchRecipeKeyWord);
                        tvPressSpeakNote.setText("按住说话");
                        stopSpeech();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(String s) {
                    LogUtils.i(TAG, "error:" + s);
                }

                @Override
                public void rmsChanged(float v) {
                    LogUtils.i(TAG, "rmsChanged:" + v);
                }
            });
            tvPressSpeakNote.setText("按住说话");
            tvPressSpeakNote.setVisibility(View.INVISIBLE);
            ivRecipeSpeaking.setVisibility(View.VISIBLE);
            GlideApp.with(getContext())
                    .asGif2()
                    .load(R.mipmap.voice_recognition)
                    .into(ivRecipeSpeaking);
            LogUtils.i(TAG, "avatarClick");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "DDSNotInitCompleteException " + e.toString());
        }
    }

    /**
     * 关闭语音识别
     */
    private void stopSpeech() {
        try {
            DDS.getInstance().getAgent().getASREngine().stopListening();
            DDS.getInstance().getAgent().avatarRelease();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecordButton.onActionUp();
                tvPressSpeakNote.setVisibility(View.VISIBLE);
                ivRecipeSpeaking.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 未识别到语音
     */
    private void noSpeech() {
        try {
            TTSEngine ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
            ttsEngine.setMode( TTSEngine.CLOUD);
            ttsEngine.setSpeaker("gqlanfp");
            ttsEngine.speak("roki没有听清，请再说一遍好吗？", 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                messageBean.setType(MessageBean.TYPE_OUTPUT);
                String message = "ROKI没有听清，请再说一遍好吗？";
                messageBean.setText(message);
                mMessageData.removeLast();
                mMessageData.add(messageBean);
                mDialogAdapter.notifyDataSetChanged();

//                tvPressSpeakNote.setText("ROKI没有听清，请再说一遍好吗？");
            }
        });
    }


    @Override
    protected void initDuiView() {
        super.initDuiView();
    }

//    @Override
//    public void onMessage() {
//        super.onMessage();
//        duiNotifyItemInserted();
//    }

    private void searchRecipeByWord(final String recipeName) {
        RokiRestHelper.getCookbooksByName(recipeName,true, true, Reponses.CookbooksResponse.class,
                new RetrofitCallback<Reponses.CookbooksResponse>() {
            @Override
            public void onSuccess(Reponses.CookbooksResponse result) {
                LogUtils.i(TAG, "onSuccess result cookbooks:" + result.cookbooks.toString() + " " + result.cookbooks3rd.toString());
                MessageBean messageBean = new MessageBean();
                if (result.cookbooks.isEmpty()) {
                    messageBean.setType(MessageBean.TYPE_OUTPUT);
                    String message = "ROKI没有找到" + searchRecipeKeyWord + ",或者换个关键词搜索一下";
                    messageBean.setText(message);
                    try {
                        TTSEngine ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
                        ttsEngine.setMode( TTSEngine.CLOUD);
                        ttsEngine.setSpeaker("gqlanfp");
                        ttsEngine.speak("roki没有找到" + searchRecipeKeyWord + ",或者换个关键词搜索一下", 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                    } catch (DDSNotInitCompleteException e) {
                        e.printStackTrace();
                    }
                } else {
                    messageBean.setText(searchRecipeKeyWord);
                    messageBean.setType(MessageBean.TYPE_WIDGET_LIST);
                    messageBean.setRecipeList(result.cookbooks);
                    try {
                        TTSEngine ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
                        ttsEngine.setMode( TTSEngine.CLOUD);
                        ttsEngine.setSpeaker("gqlanfp");
                        ttsEngine.speak("roki为您搜索到以下菜谱", 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                    } catch (DDSNotInitCompleteException e) {
                        e.printStackTrace();
                    }
                }
                mMessageData.removeLast();
                mMessageData.add(mMessageList.get(0));
                mMessageData.add(messageBean);
                rvSpeakContent.smoothScrollToPosition(mMessageData.size());
//                mMessageList.add(messageBean);
                mDialogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFaild(String err) {

                LogUtils.i(TAG, "onFailure result:" + err);
                MessageBean messageBean = new MessageBean();
                messageBean.setType(MessageBean.TYPE_OUTPUT);
                messageBean.setText("服务器响应失败，请稍后重试");
                mMessageData.removeLast();
                mMessageData.add(mMessageList.get(0));
                mMessageData.add(messageBean);
                rvSpeakContent.smoothScrollToPosition(mMessageData.size());

                mDialogAdapter.notifyDataSetChanged();
            }

        });
    }


//    @Override
//    public void onUpdate(int type, String result) {
//        super.onUpdate(type, result);
//        LogUtils.i(TAG, "type:" + type + " result:" + result);
//    }

    @Override
    protected void duiNotifyItemInserted() {
        super.duiNotifyItemInserted();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                mDialogAdapter.notifyDataSetChanged();
//                rvSpeakContent.smoothScrollToPosition(mMessageList.size());
            }
        });
    }


    @Override
    protected void duiNotifyDataSetChanged() {
        super.duiNotifyDataSetChanged();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                mDialogAdapter.notifyDataSetChanged();
//                rvSpeakContent.smoothScrollToPosition(mMessageList.size());
            }
        });
    }

    @OnClick(R.id.iv_back_speak_recipe)
    public void backSpeakRecipe() {
        UIService.getInstance().popBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        StatusBarUtils.setColor(cx, getResources().getColor(R.color.white));
//        StatusBarUtils.setTextDark(cx, true);
        EventUtils.postEvent(new PageBackEvent("SpeechRecipePage"));
    }


    /**
     * 模拟从专题详情页返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeSearchPage".equals(event.getPageName())){
            StatusBarUtils.setColor(cx, getResources().getColor(R.color.transparent));
            StatusBarUtils.setTextDark(cx, false);
        }
    }
}