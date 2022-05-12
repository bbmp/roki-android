package com.robam.roki.ui.page;

import android.app.Activity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.ShareRecipePictureEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepDetails;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.view.RecipeCookingView;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by sylar on 15/6/14.
 */
public class RecipeCookingPage extends BasePage {

    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.imgReturn)
    ImageView imgBack;

    Recipe book;
    int stepIndex;
    protected List<CookStep> steps;
    ExtPageAdapter adapter;
    MobileStoveCookTaskService cts = MobileStoveCookTaskService.getInstance();
    private IRokiDialog dialog;
    List<CookStepDetails> stepDetailses= new ArrayList<>();
    private long startInMillis;
    private long upInMillis;
    private boolean isLastStep;
    private long backStartInMillis;
    boolean isRunbing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_recipe_cooking, container, false);
        ButterKnife.inject(this, view);
        imgBack.setVisibility(View.INVISIBLE);

        Bundle bd = getArguments();
        long bookId = bd.getLong(PageArgumentKey.BookId);
        stepIndex = bd.getInt(PageArgumentKey.RecipeStepIndex, 0);
        isRunbing = bd.getBoolean(PageArgumentKey.isRunning);
        if (stepIndex==-1)
            stepIndex=0;
        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        adapter = new ExtPageAdapter();
        pager.setAdapter(adapter);
        initData(bookId);
        startInMillis = Calendar.getInstance().getTimeInMillis();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick(R.id.imgReturn)
    public void onClickBack() {
        if (stepIndex > 0) {
            pager.setCurrentItem(stepIndex - 1);
        }
    }

    @OnClick(R.id.imgClose)
    public void onClickClose() {
        boolean running = cts.isRunning();

        if (running){
            onCookingExit();
        }else{
            if (dialog == null || dialog.isShow()) return;
            dialog.setTitleText(R.string.close_work);
            dialog.setContentText(R.string.is_close_work);
            dialog.show();
            dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onCookingStop();
                }
            });
        }
    }

    @OnPageChange(R.id.pager)
    public void onPageSelected(int position) {
        if (position > stepIndex) {
            onCookingNext();
            if (isRunbing){
                nextCookStepControl();
            }
        } else if (position < stepIndex) {
            onCookingBack();
            if (steps.size() == position+2){
                if (isRunbing){
                    nextCookStepControl();
                }
            }
            if (isRunbing){
                upCookStepControl();
            }
        }
        stepIndex = position;
        imgBack.setVisibility(stepIndex != 0 ? View.VISIBLE : View.INVISIBLE);
        adapter.getPage(position).invalidate();
    }

    public void upCookStepControl(){
        long min = 0;

        if (upInMillis != 0){
            upInMillis = 0;
        }
        if (backStartInMillis != 0){
            long endInMillis = Calendar.getInstance().getTimeInMillis();
            min = (endInMillis - backStartInMillis) / 1000;
        }
        backStartInMillis = Calendar.getInstance().getTimeInMillis();
        CookStepDetails stepDetail = new CookStepDetails();

        CookStep step = steps.get(stepIndex);
        if (RecipeUtils.isPlotRecipe(book)){
            stepDetail.scheduledTime = -1;
        }else {
            stepDetail.scheduledTime = step.needTime;
        }

        int stepNum = stepIndex;
        stepDetail.stepNo = (stepNum + 1);
        if (min <= 3){
        }else if (min >= 7200){
            cts.stop();
        }else {
            stepDetail.actualTime = (int) min;
            if (min < step.needTime){
                stepDetail.actionType="SKIPPED";
            }else if (min > step.needTime){
                stepDetail.actionType="NORMAL";
            }else if (min >= stepDetail.actualTime -2 && min <= stepDetail.actualTime + 2 ){
                stepDetail.actionType="NORMAL";
            }
            stepDetailses.add(stepDetail);
            LogUtils.i("20171020","min:"+ min+" stepNo:"+stepDetail.stepNo+ " actionType:"+
                    stepDetail.actionType+" scheduledTime:"+stepDetail.scheduledTime);
        }
    }

    public void nextCookStepControl() {
        long min = 0;
        if (backStartInMillis != 0){
            backStartInMillis = 0;
        }
        if (upInMillis != 0){
            long endInMillis = Calendar.getInstance().getTimeInMillis();
            min = (endInMillis - upInMillis) / 1000;
        }
        upInMillis = Calendar.getInstance().getTimeInMillis();

        CookStep step = null;
        if (stepIndex != 0 ){
            step = steps.get(stepIndex);
        }
        if (stepIndex == 0 || isLastStep){
            step = steps.get(stepIndex);
        }
        CookStepDetails stepDetail = new CookStepDetails();
        if (RecipeUtils.isPlotRecipe(book)){
            stepDetail.scheduledTime = -1;
        }else {
            stepDetail.scheduledTime = step.needTime;
        }
        int stepNum = stepIndex;
        stepDetail.stepNo = stepNum+1;
        if (min <= 3){
        }else if (min >= 7200){
            cts.stop();
        }else {
            stepDetail.actualTime = (int) min;
            if (min < step.needTime){
                stepDetail.actionType="SKIPPED";
            }else if (min > step.needTime){
                stepDetail.actionType="NORMAL";
            }else if(min >= stepDetail.actualTime -2 && min <= stepDetail.actualTime + 2 ){
                stepDetail.actionType="NORMAL";
            }

                stepDetailses.add(stepDetail);
            if (Plat.DEBUG) {
                LogUtils.i("20171024","min:"+ min+" stepNo:"+stepDetail.stepNo+ " actionType:"+
                        stepDetail.actionType+" scheduledTime:"+stepDetail.scheduledTime+" size:"+stepDetailses.size());
            }
        }
    }



    void initData(long bookId) {

        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getCookbookById(bookId, Reponses.CookbookResponse.class,
                new RetrofitCallback<Reponses.CookbookResponse>() {

            @Override
            public void onSuccess(Reponses.CookbookResponse cookbookResponse) {
                if (null != cookbookResponse) {
                    ProgressDialogHelper.setRunning(cx, false);
                    initPages(cookbookResponse.cookbook);
                }
            }

            @Override
            public void onFaild(String err) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showShort(err);
            }

        });

    }

    void initPages(Recipe cookbook) {
        if (null == cookbook)
            return;
        book = cookbook;
        List<View> views = Lists.newArrayList();
        List<CookStep> list =  book.getJs_cookSteps();
        steps=list;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                RecipeCookingView view = new RecipeCookingView(cx, book, i, pageNextCallback);
                views.add(view);
            }
        }
        adapter.loadViews(views);
        pager.setCurrentItem(stepIndex, true);
        if (isRunbing){
            nextCookStepControl();
        }

    }


    UIListeners.CookingNextCallback pageNextCallback = new UIListeners.CookingNextCallback() {

        @Override
        public void onClickNext() {
            LogUtils.i("20171020","pageNextCallback");
            boolean isLast = stepIndex + 1 == adapter.getCount();
            if (isLast) {
                onCookingFinished();
            } else {
                pager.setCurrentItem(stepIndex + 1, true);
            }
        }
    };

    void onCookingBack() {
        cts.back();
    }

    void onCookingNext() {
        cts.next();
    }

    void onCookingPause() {
        cts.pause();
    }

    void onCookingStop() {
        if (isRunbing){
            nextCookStepControl();
            cookStepData();
        }
        cts.stop();
        cts.setRunning(false);
    }
    //烧菜步骤数据
    private void cookStepData() {
        boolean isNotCountdown = cts.getRemainTime() <= 0;
        for (CookStepDetails step: stepDetailses){
            LogUtils.i("20171025","step:"+step);
        }
        isLastStep = stepIndex + 1 == adapter.getCount();
        boolean isBroken = !(isLastStep && isNotCountdown);
        AbsFan fan = Utils.getDefaultFan();
        long endMillis = Calendar.getInstance().getTimeInMillis();
        if (null != fan){
            CookbookManager.getInstance().addCookingLog_New(fan.getID(), book, steps.size(), IAppType.RKDRD, startInMillis, endMillis,
                    isBroken, stepDetailses, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20170902","t:"+ t);
                        }
                    });
            upInMillis = 0;
            backStartInMillis = 0;
        }

    }
    //晒厨艺
    void onCookingFinished() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText(R.string.cooking_finish);
        dialog.setContentText(R.string.cooking_complete_content);
        dialog.show();
        dialog.setOkBtn(R.string.bask_in_cooking, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity atv = UIService.getInstance().getMain().getActivity();
                RecipeShowPage.showCooking(atv, book.getID());
                dialog.dismiss();
               // onCookingStop();
            }
        });
        dialog.setCanBtnTextColor(R.color.White_60);
        dialog.setCancelBtn(R.string.refused_to, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCookingStop();
            }
        });

    }

    @Subscribe
    public void onEvent(ShareRecipePictureEvent event){
        onCookingStop();
    }

    void onCookingExit() {
        boolean isLast = stepIndex + 1 == adapter.getCount();
        if (isLast && cts.getRemainTime() <= 0) {
            onCookingFinished();
        } else {
            if (dialog == null || dialog.isShow()) return;
            dialog.setTitleText(R.string.close_work);
            dialog.setContentText(R.string.is_close_work);
            dialog.show();
            dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onCookingStop();
                }
            });
        }
    }

}
