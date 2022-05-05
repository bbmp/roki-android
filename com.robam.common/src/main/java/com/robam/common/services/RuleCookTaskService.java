package com.robam.common.services;


import com.legent.plat.events.PlotRecipeNextEvent;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.events.AbsCommonEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.RuleStream;
import com.robam.common.pojos.Rules;
import com.robam.common.util.PotRecipeRuleUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import regulation.dto.CurrentRunningStepInfo;
import regulation.dto.CurrentStepRulesInfo;
import regulation.dto.RuleSingleInfo;
import regulation.dto.RuleStreamInfo;
import regulation.dto.StatusResult;
import regulation.service.CheckRuleServiceImp;

/**
 * Created by as on 2017-04-07.
 */

public abstract class RuleCookTaskService extends AbsCookTaskService {
    double workingtime;
    LinkedList<Double> temps = new LinkedList<Double>();

    /**
     * 执行上一步工序
     *
     * @return
     */
    public void back() {
        if (stepIndex <= 0)
            return;

        stepIndex--;
        CookStep step = steps.get(stepIndex);
        LogUtils.i("20190610", " back" );
        setCommand(step);
    }


    @Override
    protected void startCountdown(final int needTime) {
        if (!isRunning)
            return;
        stopCountdown();
        remainTime = needTime;
        workingtime = 0;
        future = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                workingtime++;
            }
        }, 500, 1000, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void onNext() {
        if (temps != null)
            temps.clear();
    }

    public interface AbsCookTaskServiceCallBack {
        void onCompleted(PlotRecipeNextEvent event);
    }

    public void RecipeTempUpEvent(float temp, AbsCookTaskServiceCallBack callBack) {
        try {

            if (future == null)
                return;
            if (temps == null)
                temps = new LinkedList<Double>();
            if (temps.size() > 350) {
                temps = new LinkedList<Double>(temps.subList(0, 200));
            }
            temps.add(0, (double) temp);
            CurrentRunningStepInfo stepInfo = new CurrentRunningStepInfo();
            stepInfo.setReportPeriod(1.0D);
            stepInfo.setTemperatureBuffer(temps);
            stepInfo.setCookbookId(recipeId.intValue());
            stepInfo.setCurrentStepNo(step.order);
            stepInfo.setTotalStepNo(steps.size());
            stepInfo.setElapseTime(workingtime);
            CurrentStepRulesInfo rulesInfo = new CurrentStepRulesInfo();
            List<Rules> rules = step.getRulesByCodeName(stove.getDp());
            if (rules == null || rules.size() == 0) {
                callBack.onCompleted(new PlotRecipeNextEvent("RULE_CODE_RULE_MANUAL", null, stepIndex));
                return;
            }
            List<RuleStream> ruleStreams = rules.get(0).getRuleStreams();
            RuleStream ruleStream = ruleStreams.get(0);
            List<RuleSingleInfo> ruleSingleInfos = new ArrayList<>();
            RuleStreamInfo ruleStreamInfo = new RuleStreamInfo();
            List<RuleStreamInfo> ruleStreamInfos = new ArrayList<>();
            RuleSingleInfo singleInfo = new RuleSingleInfo();
            singleInfo.setRuleCode(ruleStream.ruleCode);
            String recipeRuleByCode = PotRecipeRuleUtils.getRecipeRuleByCode(ruleStream.ruleCode);
            singleInfo.setRuleType(ruleStream.ruleType);
            singleInfo.setRuleValue(ruleStream.ruleValue);
            singleInfo.setDurationTime(ruleStream.durationTime);
            singleInfo.setEffectTime(ruleStream.effectTime);
            ruleSingleInfos.add(singleInfo);
            ruleStreamInfo.setRuleStream(ruleSingleInfos);
            ruleStreamInfos.add(ruleStreamInfo);
            rulesInfo.setRules(ruleStreamInfos);
            CheckRuleServiceImp checkRuleServiceImp = new CheckRuleServiceImp();
            StatusResult statusResult = checkRuleServiceImp.checkRule(rulesInfo, stepInfo);
            LogUtils.i("20190428", "type:" + recipeRuleByCode + " order:" + step.order + " stepIndex:" + stepIndex);
            callBack.onCompleted(new PlotRecipeNextEvent(recipeRuleByCode, statusResult, stepIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStoped() {
        super.onStoped();
        EventUtils.postEvent(new AbsCommonEvent(AbsCommonEvent.DevicePotFinishCooking));
        SpeechManager.getInstance().dispose();
    }

//    @Override
//    protected void onAskAtEnd() {
//        super.onAskAtEnd();
//    }
}
