package com.robam.roki.ui.activity3;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.agent.ASREngine;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;
import com.robam.roki.ui.adapter3.RvRecipeSteps2Adapter;
import com.robam.roki.ui.adapter3.RvRecipeSteps3Adapter;
import com.robam.roki.ui.adapter3.RvRecipeStepsAdapter;
import com.robam.roki.ui.bean3.SpeechBean;
import com.robam.roki.utils.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *    desc   : 语音菜谱
 *    @author r210190
 */
public final class SpeechCookActivity extends AppActivity {
    private static final String TAG = "SpeechCookActivity";

    public static String RECIPE = "RECIPE";
    public static String NEXT = "下一步";
    public static String LAST = "上一步";
    public static String LAST_2 = "上一部";
    public static String AGAIN = "再来一次";
    /**
     * 图片
     */
    private RecyclerView rvCookImage;
    /**
     * position
     */
    private RecyclerView rvCookPosition;
    /**
     * 步骤文字说明
     */
    private RecyclerView rvCookDesc;
    /**
     * 菜谱数据
     */
    private Recipe recipe;
    /**
     * 图片 视频adapter
     */
    private RvRecipeStepsAdapter rvRecipeStepsAdapter;
    /**
     * 文字说明adapter
     */
    private RvRecipeSteps2Adapter rvRecipeSteps2Adapter;
    /**
     * position adapter
     */
    private RvRecipeSteps3Adapter rvRecipeSteps3Adapter;
    private int mAuthCount = 0;

    private ImageView ivTishi;
    private ImageView ivFangxiang;
    private ImageView ivYuyin;
    private CheckBox cbYinliang;
    /**
     * 语音识别
     */
    private ASREngine asrEngine;
    /**
     * 语音播报
     */
    private TTSEngine ttsEngine;
    public static void start(Context context , Recipe recipe) {
        Intent intent = new Intent(context, SpeechCookActivity.class);
        intent.putExtra(RECIPE ,recipe) ;
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return R.layout.activity_speech_cook_2;
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return R.layout.activity_speech_cook;
        }else {
            return R.layout.activity_speech_cook;
        }

    }

    @Override
    protected boolean isStatusBarEnabled() {
        return false;
    }


    @Override
    protected void initView() {
        rvCookImage = (RecyclerView) findViewById(R.id.rv_cook_image);
        rvCookPosition = (RecyclerView) findViewById(R.id.rv_cook_position);
        rvCookDesc = (RecyclerView) findViewById(R.id.rv_cook_desc);
        ivTishi = (ImageView) findViewById(R.id.iv_tishi);
        ivFangxiang = (ImageView) findViewById(R.id.iv_fangxiang);
        ivYuyin = (ImageView) findViewById(R.id.iv_yuyin);
        cbYinliang = (CheckBox) findViewById(R.id.cb_yinliang);
        setOnClickListener(ivTishi , ivFangxiang ,ivYuyin );
        cbYinliang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        rvCookImage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rvCookPosition.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCookDesc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        rvRecipeStepsAdapter = new RvRecipeStepsAdapter();
        rvRecipeSteps2Adapter = new RvRecipeSteps2Adapter();
        rvRecipeSteps3Adapter = new RvRecipeSteps3Adapter();
        rvCookImage.setAdapter(rvRecipeStepsAdapter);
        rvCookDesc.setAdapter(rvRecipeSteps2Adapter);
        rvCookPosition.setAdapter(rvRecipeSteps3Adapter);
        rvRecipeSteps3Adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                rvRecipeSteps3Adapter.setIndex(position);
                rvCookImage.scrollToPosition(position);
                rvCookDesc.scrollToPosition(position);
                rvRecipeStepsAdapter.notifyDataSetChanged();
                speak(recipe.js_cookSteps.get(position).desc);
            }
        });

//        DDS.getInstance().init(getContext().getApplicationContext(), createConfig(), mInitListener, mAuthListener);

    }

    @Override
    protected void initData() {
        recipe = (Recipe) getIntent().getSerializableExtra(RECIPE);
        if (recipe != null && recipe.js_cookSteps != null && recipe.js_cookSteps.size() != 0){
            setTitle(recipe.name);
            rvRecipeStepsAdapter.addData(recipe.js_cookSteps);
            rvRecipeSteps2Adapter.addData(recipe.js_cookSteps);
            rvRecipeSteps3Adapter.addData(recipe.js_cookSteps);
            if (recipe.js_cookSteps.size() < 6){
                rvCookPosition.setLayoutManager(new GridLayoutManager(this , recipe.js_cookSteps.size()));
            }
        }

         ttsEngine = DDS.getInstance().getAgent().getTTSEngine();
        asrEngine = DDS.getInstance().getAgent().getASREngine();
        speakSearchRecipe();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(ivTishi)){

        }else if (view.equals(ivFangxiang)){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if (view.equals(ivYuyin)){

        }else {

        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_speech_cook_2);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_speech_cook);
        }
        initView();
        initData();
    }

    /**
     * 语音识别回调
     */
    private ASREngine.Callback callback = new ASREngine.Callback(){
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
                speech() ;
            }
        }

        @Override
        public void finalResults(String s) {
            LogUtils.i(TAG, "finalResults:" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = new JSONObject(s);
                String text  = jsonObject.get("text").toString();
                if (NEXT.equals(text)){
                    next();
                }else if (LAST.equals(text) || LAST_2.equals(text)){
                    last();
                }else if (AGAIN.equals(text)){
                    again();
                }else {
                    speech() ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                speech() ;
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
    /**
     * 语音识别
     */
    private void speakSearchRecipe() {
        try {

            if (asrEngine != null){
                asrEngine.startListening(callback);
            }

//            LogUtils.i(TAG, "avatarClick");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "DDSNotInitCompleteException " + e.toString());
        }
    }

    /**
     * 重新唤醒语音
     */
    private void speech(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivYuyin.setImageResource(R.mipmap.ic_yuyinhuanxin);
                speakSearchRecipe();

            }
        });
    }
    /**
     * 播报
     * @param desc
     */
    private void speak(String desc){
        if (!cbYinliang.isChecked()){
            speech();
            return;
        }
        try {
            ttsEngine.setMode( TTSEngine.CLOUD);
            ttsEngine.setSpeaker("gqlanfp");
            ttsEngine.setListenerByProcess(new TTSEngine.CallbackOptimize() {
                @Override
                public void beginning(String s) {
                    LogUtils.i(TAG, "beginning----------- " + s);
                }

                @Override
                public void end(String s, int i) {
                    LogUtils.i(TAG, "end-------------- " + s);
                    speech();
                }

                @Override
                public void error(String s) {
                    LogUtils.i(TAG, "error-------------- " + s);
                    speech();
                }
            });
            ttsEngine.speak(desc, 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            ivYuyin.setImageResource(R.mipmap.ic_yuyin);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
            speech();
        }
    }
    /**
     * 下一步
     */
    private void next(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index = rvRecipeSteps3Adapter.getIndex();
                if (index < recipe.js_cookSteps.size()-1){
                    index ++ ;
                    rvRecipeSteps3Adapter.setIndex(index);
                    rvCookImage.scrollToPosition(index);
                    rvCookDesc.scrollToPosition(index);
                    speak(recipe.js_cookSteps.get(index).desc);
                }
            }
        });


    }

    /**
     * 上一步
     */
    private void last(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index = rvRecipeSteps3Adapter.getIndex();
                if (index > 0 ){
                    index -- ;
                    rvRecipeSteps3Adapter.setIndex(index);
                    rvCookImage.scrollToPosition(index);
                    rvCookDesc.scrollToPosition(index);
                    speak(recipe.js_cookSteps.get(index).desc);
                }
            }
        });

    }

    /**
     * 再来一次
     */
    private void again(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index = rvRecipeSteps3Adapter.getIndex();
                speak(recipe.js_cookSteps.get(index).desc);
            }
        });
    }
    /**
     * 创建DDS配置信息
     * @return
     */
    private DDSConfig createConfig() {
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


    /**
     * 执行自动授权
     */
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

    /**
     * 关闭语音识别
     */
    private void stopSpeech() {
        try {
            asrEngine.stopListening();
            DDS.getInstance().getAgent().avatarRelease();

        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onDestroy() {
        stopSpeech();
        asrEngine = null;
        super.onDestroy();
    }

}