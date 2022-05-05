package com.legent.utils.speech;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.legent.Helper;
import com.legent.VoidCallback;

import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;

import java.util.List;

public class SpeechManager {

    public interface SpeechListener {
        void onSpeechRecognition(String text);

        void onSpeechOver();
    }

    private static String TAG = "lgSpeech";
    private static String APPID = "459b35fe";
    final private static String KEY_GRAMMAR_ABNF_ID = "KEY_GRAMMAR_ABNF_ID";
    final private static String mEngineType = SpeechConstant.TYPE_CLOUD;
    final private static String GRAMMAR_TYPE_ABNF = "abnf";

    private static SpeechManager instance = new SpeechManager();

    synchronized public static SpeechManager getInstance() {
        return instance;
    }

    // 语音识别对象
    private SpeechRecognizer mAsr;
    private SpeechSynthesizer mTts;
    private List<String> commands = Lists.newArrayList();
    private SpeechListener listener;
    private boolean isBuildGrammar;
    private boolean isInited;
    private boolean autoListen;

    private SpeechManager() {

    }

    public void init(Context cx) {
        init(cx, APPID);
    }

    public void init(Context cx, String appId) {
        if (isInited)
            return;

        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用“,”分隔。
        // 设置你申请的应用appid
        Setting.setShowLog(false);
        SpeechUtility.createUtility(cx, SpeechConstant.APPID + "=" + appId);

        mTts = SpeechSynthesizer.createSynthesizer(cx, null);
        setSpeakParams();
        mAsr = SpeechRecognizer.createRecognizer(cx, new InitListener() {

            @Override
            public void onInit(int code) {
                Log.d(TAG, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    showLog("初始化失败,错误码：" + code);
                } else {
                    setListenParams();
                }
            }
        });

        isInited = true;
    }

    public void dispose() {
        if (mAsr != null) {
            mAsr.cancel();
            mAsr.destroy();
        }

        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
    }

    public void setAutoListen(boolean autoListen) {
        this.autoListen = autoListen;
    }

    // --------------------------------------------------语音合成

    public void startSpeaking(String text) {
        if (mTts == null || Strings.isNullOrEmpty(text))
            return;

        stop();

        mTts.startSpeaking(text + "。", new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
                showLog("开始播放");
                if (mAsr.isListening()) {
                    mAsr.stopListening();
                }
            }

            @Override
            public void onSpeakPaused() {
                showLog("暂停播放");
            }

            @Override
            public void onSpeakResumed() {
                showLog("继续播放");
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos,
                                         String info) {
                // mPercentForBuffering = percent;
                // showTip(String.format(getString(R.string.tts_toast_format),
                // mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // mPercentForPlaying = percent;
                // showTip(String.format(getString(R.string.tts_toast_format),
                // mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (error == null) {
                    showLog("播放完成");
                } else if (error != null) {
                    showLog(error.getPlainDescription(true));
                }

                if (autoListen) {
                    restartListening();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }
        });
    }
    public void startSpeaking(String text , SynthesizerListener synthesizerListener) {
        if (mTts == null || Strings.isNullOrEmpty(text))
            return;

        stop();

        mTts.startSpeaking(text + "。", synthesizerListener);
    }

    SpeakComple  speakComple ;
    public interface SpeakComple{
        void comple();
    }
    public void startSpeaking2(String text , final SpeakComple speakComple) {
        this.speakComple =speakComple ;
        if (mTts == null || Strings.isNullOrEmpty(text))
            return;

        stop();

        mTts.startSpeaking(text + "。", new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
                showLog("开始播放");
                if (mAsr.isListening()) {
                    mAsr.stopListening();
                }
            }

            @Override
            public void onSpeakPaused() {
                showLog("暂停播放");
                if (speakComple != null){
                    speakComple.comple();
                }
            }

            @Override
            public void onSpeakResumed() {
                showLog("继续播放");
                if (speakComple != null){
                    speakComple.comple();
                }
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos,
                                         String info) {
                LogUtils.i("RecipeCook" ,"onBufferProgress" + info);
                // mPercentForBuffering = percent;
                // showTip(String.format(getString(R.string.tts_toast_format),
                // mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // mPercentForPlaying = percent;
                // showTip(String.format(getString(R.string.tts_toast_format),
                // mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (error == null) {
                    showLog("播放完成");
                    if (speakComple != null){
                        speakComple.comple();
                    }
                } else if (error != null) {
                    showLog(error.getPlainDescription(true));
                    if (speakComple != null){
                        speakComple.comple();
                    }
                }

                if (autoListen) {
                    restartListening();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                LogUtils.i("RecipeCook" ,"onEvent" );

            }
        });
    }

    public void stopSpeaking() {
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }

    // --------------------------------------------------语音识别

    public void startListening(final List<String> commands, SpeechListener l) {
        if (mAsr == null)
            return;

        this.listener = l;

        if (mAsr.isListening() || mTts.isSpeaking())
            return;

        if (isBuildGrammar) {
            restartListening();
        } else {
            buildGrammar(commands, new VoidCallback() {

                @Override
                public void onSuccess() {
                    isBuildGrammar = true;
                    restartListening();
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }

    }

    public void restartListening() {
        int ret = mAsr.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showLog("启动识别失败,错误码: " + ret);
        }
    }

    public void stopListening() {

        mAsr.stopListening();
        mAsr.cancel();
        // showLog("停止识别");
    }

    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {


        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {

            if (null != result) {
                Log.d(TAG, "recognizer result：" + result.getResultString());
                String text = JsonParser.parseOptimalResult(result
                        .getResultString());

                onParserText(text);

            } else {
                Log.d(TAG, "recognizer result : null");
            }

            if (listener != null) {
                listener.onSpeechOver();
            }
        }

        @Override
        public void onEndOfSpeech() {
            showLog("结束说话");

        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            showLog("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            Log.e(TAG, String.valueOf(error.getErrorCode()));

            if (listener != null) {
                listener.onSpeechOver();
            }

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

    };

    // --------------------------------------------------语音识别

    private void stop() {
        stopSpeaking();
        stopListening();
    }

    private void setSpeakParams() {
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        mTts.setParameter(SpeechConstant.STREAM_TYPE,
                String.valueOf(AudioManager.STREAM_MUSIC));
        // 设置合成音频保存位置(可自定义保存位置),保存在“./sdcard/iflytek.pcm”
        // 保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        // 如果不需要保存合成音频,注释该行代码
         mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
         "./sdcard/iflytek.pcm");
    }

    private void setListenParams() {
        if (mAsr != null){
            mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
            mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
            mAsr.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "0");
            mAsr.setParameter(SpeechConstant.VAD_BOS, "10000");
        }

        String grammarId = PreferenceUtils.getString(KEY_GRAMMAR_ABNF_ID, null);
        setGrammarId(grammarId);
    }

    /**
     * 云端构建语法
     */
    private void buildGrammar(final List<String> commands,
                              final VoidCallback callback) {

        this.commands.clear();
        String content = buildGrammarContent(commands);
        int ret = mAsr.buildGrammar(GRAMMAR_TYPE_ABNF, content,
                new GrammarListener() {

                    @Override
                    public void onBuildFinish(String grammarId,
                                              SpeechError error) {
                        if (error == null) {

                            SpeechManager.this.commands.addAll(commands);

                            PreferenceUtils.setString(KEY_GRAMMAR_ABNF_ID,
                                    grammarId);
                            setGrammarId(grammarId);

                            showLog("注册命令集成功：" + commands);
                            showLog("语法构建成功：" + grammarId);

                            Helper.onSuccess(callback);
                        } else {
                            showLog("语法构建失败,错误码：" + error.getErrorCode());

                            Helper.onFailure(callback, error);
                        }
                    }
                });

        if (ret != ErrorCode.SUCCESS) {
            showLog("语法构建失败,错误码：" + ret);

            Helper.onFailure(callback, new Throwable("语法构建失败,错误码：" + ret));
        }
    }

    private String buildGrammarContent(List<String> commands) {

        if (commands.size() == 0)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("#ABNF 1.0 UTF-8;\n");
        sb.append("language zh-CN;\n");
        sb.append("mode voice;\n");
        sb.append("\n");
        sb.append("root $main;\n");
        sb.append("$main = ");
        for (String cmd : commands) {
            sb.append(cmd).append("|");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(";");

        return sb.toString();
    }

    private void setGrammarId(String grammarId) {
        if (Strings.isNullOrEmpty(grammarId))
            return;
        mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
    }

    private void onParserText(final String text) {
        if (Strings.isNullOrEmpty(text))
            return;
        Log.d(TAG, text);
        if (commands.contains(text) && listener != null) {
            listener.onSpeechRecognition(text);
        }
    }

    private void showLog(final String str) {
        Log.d(TAG, str);
    }
}
