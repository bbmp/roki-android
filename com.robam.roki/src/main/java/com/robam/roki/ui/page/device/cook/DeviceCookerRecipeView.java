package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.utils.StringUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.KuCookingSteps;
import com.robam.common.pojos.PayLoadKuF;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.DeviceCookerApdater;
import com.robam.roki.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robam.roki.R.id.reco_name;

/**
 * Created by Dell on 2018/6/11.
 */

public class DeviceCookerRecipeView extends FrameLayout {

    @InjectView(reco_name)
    TextView recoName;
    @InjectView(R.id.recook_time)
    TextView recookTime;
    @InjectView(R.id.step_desc)
    RecyclerView stepDesc;
    @InjectView(R.id.status_show)
    TextView statusShow;


    DeviceCookerApdater deviceCookerAdapter;
    Context cx;

    List<KuCookingSteps> stepList = new ArrayList<>();
    int pos = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    deviceCookerAdapter.setSelect(pos);
                    stepDesc.smoothScrollToPosition(pos);
                    break;
            }
        }
    };

    AbsCooker absCooker;
    String name;

    public DeviceCookerRecipeView(Context context, AbsCooker absCooker, List<KuCookingSteps> list, String name) {
        super(context);
        this.cx = context;
        this.absCooker = absCooker;
        this.stepList = list;
        this.name = name;
        initView(context, null);
    }

    public DeviceCookerRecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.device_cooker_re_detail, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initRecipe();
    }


    private void initRecipe() {
        recoName.setText(name);
        deviceCookerAdapter = new DeviceCookerApdater(cx, stepList);
        stepDesc.setAdapter(deviceCookerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        stepDesc.setLayoutManager(linearLayoutManager);
        deviceCookerAdapter.setSelect(pos);
        // timer = new Timer();
        // setLoop();
    }

    public void upDateStep(PayLoadKuF payLoadKuF) {
        recoName.setText(payLoadKuF.name);
        this.stepList = payLoadKuF.kuCookingStepses;
        deviceCookerAdapter.upData(stepList);
    }

    int preStep = -1;

    public void upDate(AbsCooker absCooker) {
        if (absCooker.recipeTimeLeft == 0) {
            recookTime.setText("--");
        } else {
            String timeStr = TimeUtils.timeToStr(absCooker.recipeTimeLeft);
            recookTime.setText(timeStr);
        }
        short step = absCooker.recipeStepId;
        deviceCookerAdapter.setSelect(step - 1);
        if (preStep != (step - 1)) {
            preStep = step - 1;
            stepDesc.scrollToPosition(step - 1);
            onSpeakClick(step - 1);
        }
        switch (absCooker.recipeStatus) {
            case 0:
                break;
            case 1:
                statusShow.setText("烹饪中");
                break;
            case 2:
                statusShow.setText("暂停中");
                break;
            case 3:
                statusShow.setText("已结束");
                break;
        }
    }


    //语音识别
    public void onSpeakClick(final int step) {
        //  LogUtils.i("20171223","step::"+cookStepTemp.get(step).desc);
        if (StringUtils.isNullOrEmpty(stepList.get(step).description))
            return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SpeechManager.getInstance().startSpeaking(stepList.get(step).description);
            }
        }, 100);
    }


}
