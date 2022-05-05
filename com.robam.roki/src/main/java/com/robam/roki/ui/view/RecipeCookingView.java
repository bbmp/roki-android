package com.robam.roki.ui.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.Utils;
import com.robam.common.events.CookCountdownEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.dialog.CookPreviewDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RecipeCookingView extends FrameLayout {

    final double Height_Scale = 0.69;

    @InjectView(R.id.progressView)
    RecipeCookingProgressView progressView;
    @InjectView(R.id.txtStepIndex)
    TextView txtStepIndex;
    @InjectView(R.id.txtStepCount)
    TextView txtStepCount;
    @InjectView(R.id.txtStepTime)
    TextView txtStepTime;
    @InjectView(R.id.txtStepDesc)
    TextView txtStepDesc;
    @InjectView(R.id.imgRecipe)
    ImageView imgRecipe;
    @InjectView(R.id.fanStatusView)
    RecipeCookingFanStatusView fanStatusView;
    @InjectView(R.id.stoveStatusView)
    RecipeCookingStoveStatusView stoveStatusView;
    @InjectView(R.id.previewView)
    RecipeCookingPreviewView previewView;
    @InjectView(R.id.materialsView)
    RecipeCookingMaterialsView materialsView;


    Recipe book;
    List<CookStep> cookSteps;
    CookStep currentStep;
    int stepIndex;
    UIListeners.CookingNextCallback callback;

    public RecipeCookingView(Context cx, Recipe book, int stepIndex, UIListeners.CookingNextCallback callback) {
        super(cx);
        this.book = book;
        this.stepIndex = stepIndex;
        this.callback = callback;
        this.cookSteps = book.getJs_cookSteps();
        if (stepIndex < cookSteps.size()) {
            currentStep = cookSteps.get(stepIndex);
        }

        init(cx);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            refresh();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        refresh();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    @Subscribe
    public void onEvent(CookCountdownEvent event) {
        if (event == null)
            return;
        if (stepIndex != event.stepIdnex)
            return;

        onCountdown(event.remainTime);
    }

    @OnClick(R.id.previewView)
    public void onClickPreview() {
        CookPreviewDialog.show(getContext(), cookSteps.size(), stepIndex + 1, cookSteps.get(stepIndex + 1).desc);
    }

    void init(Context cx) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_cooking,
                this, true);
        setDoubleTap(view);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            txtStepDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
            computeHeight();
            restoreView();
            refresh();
        }
    }

    void refresh() {
        if (currentStep == null) {
            return;
        }

        imgRecipe.setImageDrawable(null);
        ImageUtils.displayImage(getContext(), currentStep.imageUrl, imgRecipe);
        Stove stove = Utils.getDefaultStove();
        txtStepIndex.setText(String.valueOf(stepIndex + 1));
        txtStepCount.setText(String.format("/%s", cookSteps.size()));
        txtStepDesc.setText(currentStep.desc);
        txtStepTime.setText(second2String(currentStep.needTime));
        if (stove!=null){
            fanStatusView.setLevel((short) currentStep.getParamByCodeName(stove.getDp(),"fanGear"));
        }else{
            if (currentStep!=null){
                String dp = currentStep.getjs_PlatformCodes().get(0).platCode;
                fanStatusView.setLevel((short) currentStep.getParamByCodeName(dp,"fanGear"));
            }
        }

        if ("RRQZ".equals(currentStep.getDc())){
            if (stove!=null){
                stoveStatusView.setLevel((short) currentStep.getParamByCodeName(stove.getDp(),"stoveGear"));
            }else{
                stoveStatusView.setLevel(currentStep.stoveLevel);
            }
        }else{
                stoveStatusView.setVisibility(View.INVISIBLE);
        }

        LogUtils.i("20171026","sssss::"+(stepIndex == cookSteps.size() - 1) );
       previewView.setVisibility(stepIndex == cookSteps.size() - 1 ? INVISIBLE
                : VISIBLE);
        if (stepIndex==cookSteps.size()-1){
           // previewView.setVisibility(VISIBLE);
          //  progressView
        }else{
            progressView.setValue(0, 0);
        }
        materialsView.loadData(currentStep);
    }

    void restoreView() {
        previewView.setVisibility(stepIndex != cookSteps.size() - 1 ? VISIBLE : GONE);
        progressView.setOnCookingStepFinishedCallback(onStepFinishedCallback);
        txtStepIndex.setText(null);
        txtStepCount.setText(null);
        txtStepDesc.setText(null);
        progressView.setValue(0, 0);
        imgRecipe.setImageDrawable(null);
        materialsView.loadData(null);
    }

    void computeHeight() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(getContext());
        int height = screenWidth * 320 / 333;

        ViewGroup.LayoutParams lp = imgRecipe.getLayoutParams();
        lp.height = height;
        imgRecipe.setLayoutParams(lp);
    }

    void onCountdown(int remainTime) {

        txtStepTime.setText(second2String(remainTime));
        txtStepTime.setVisibility(remainTime <= 0 ? GONE : VISIBLE);
        int lostTime = currentStep.needTime - remainTime;
        float percent = 1f * lostTime / currentStep.needTime;
        progressView.setValueBySeconds(currentStep.needTime, percent);
        txtStepDesc.setVisibility(percent >= 1 ? GONE : VISIBLE);
        materialsView.onTipTime(lostTime);
    }

    void setDoubleTap(View view) {
        final GestureDetector gd = new GestureDetector(getContext(), new OnDoubleClick());
        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }

    String second2String(int seconds) {
        int minute = seconds / 60;
        int second = seconds % 60;

        if (second < 100 * 60)
            return String.format("%02d:%02d", minute, second);
        else
            return String.format("%03d:%02d", minute, second);
    }


    RecipeCookingProgressView.OnCookingStepFinishedCallback onStepFinishedCallback = new RecipeCookingProgressView.OnCookingStepFinishedCallback() {
        @Override
        public void onFinished() {
            if (callback != null) {
                callback.onClickNext();
            }
        }
    };

    class OnDoubleClick extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            MobileStoveCookTaskService.getInstance().pause();
            return super.onDoubleTap(e);
        }
    }

}
