package com.robam.roki.utils;

import android.util.Log;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.plat.events.PlotRecipeNextEvent;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RuleStream;
import com.robam.common.pojos.Rules;
import com.robam.common.pojos.device.Stove.Stove;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import regulation.dto.CurrentRunningStepInfo;
import regulation.dto.CurrentStepRulesInfo;
import regulation.dto.RuleSingleInfo;
import regulation.dto.RuleStreamInfo;
import regulation.dto.StatusResult;
import regulation.service.CheckRuleServiceImp;

/**
 * Created by Dell on 2018/8/31.
 */

public class PotRuleUtils {
    double workingtime;
    Recipe recipe;
    ArrayList<CookStep> cookSteps;
    int stepIndex;
    Stove stove;

    public PotRuleUtils(Recipe recipe, ArrayList<CookStep> cookSteps,int stepIndex,Stove stove){
        this.recipe = recipe;
        this.cookSteps = cookSteps;
        this.stepIndex = stepIndex;
        this.stove = stove;
    }

    LinkedList<Double> temps = new LinkedList<Double>();

    public interface AbsCookTaskServiceCallBack {
        void onCompleted(PlotRecipeNextEvent event);
    }

    public void RecipeTempUpEvent(float temp, AbsCookTaskServiceCallBack callBack) {
        if (temps == null)
            temps = new LinkedList<Double>();
        if (temps.size() > 350) {
            temps = new LinkedList<Double>(temps.subList(0, 200));
        }
        temps.add(0, (double) temp);
        CurrentRunningStepInfo stepInfo = new CurrentRunningStepInfo();
        stepInfo.setReportPeriod(1.0D);
        stepInfo.setTemperatureBuffer(temps);
        stepInfo.setCookbookId((int) recipe.id);
        CookStep step = cookSteps.get(stepIndex);
        if (step != null)
            stepInfo.setCurrentStepNo(step.order);
        stepInfo.setTotalStepNo(cookSteps.size());
        stepInfo.setElapseTime(workingtime);
        CurrentStepRulesInfo rulesInfo = new CurrentStepRulesInfo();
        List<Rules> rules = step.getRulesByCodeName(stove != null ? stove.getDp() : null);
        List<RuleStreamInfo> t_rules = Lists.newArrayList();
        String ruleCode_t = "";
        if (step != null && rules != null && rules.size() > 0) {
            if (Plat.DEBUG)
                LogUtils.out("没步骤：" + step.toString());
            for (Rules rule : rules) {
                RuleStreamInfo t_ruleStreams = new RuleStreamInfo();
                List<RuleStream> ruleStreams = rule.getRuleStreams();
                List<RuleSingleInfo> t_ruleStream = Lists.newArrayList();
                if (ruleStreams != null && ruleStreams.size() > 0) {
                    for (RuleStream ruleStream : ruleStreams) {
                        RuleSingleInfo t_info = new RuleSingleInfo();
                        if (PlotRecipeNextEvent.RULE_CODE_OVERTIME.equals(ruleStream.ruleCode)
                                || PlotRecipeNextEvent.RULE_CODE_UP_THRESHOLD.equals(ruleStream.ruleCode)
                                || PlotRecipeNextEvent.RULE_CODE_DOWN_THRESHOLD.equals(ruleStream.ruleCode)
                                || PlotRecipeNextEvent.RULE_CODE_TEMPERATURE_TREND.equals(ruleStream.ruleCode)
                                || PlotRecipeNextEvent.RULE_CODE_ACCELERATE_TREND.equals(ruleStream.ruleCode)
                                || PlotRecipeNextEvent.RULE_CODE_SECTION_TEMP.equals(ruleStream.ruleCode)) {
                            ruleCode_t = "auto";
                        }
                        t_info.setRuleCode(ruleStream.ruleCode);
                        t_info.setRuleType(ruleStream.ruleType);
                        t_info.setRuleValue(ruleStream.ruleValue);
                        t_info.setDurationTime(ruleStream.durationTime);
                        t_info.setEffectTime(ruleStream.effectTime);
                        t_ruleStream.add(t_info);
                    }
                }
                t_ruleStreams.setRuleStream(t_ruleStream);
                t_rules.add(t_ruleStreams);
            }
        }
        rulesInfo.setRules(t_rules);
        CheckRuleServiceImp checkRuleServiceImp = new CheckRuleServiceImp();
        StatusResult statusResult = checkRuleServiceImp.checkRule(rulesInfo, stepInfo);
        try {
            if (Plat.DEBUG) {
                Log.e("plotchecker", JsonUtils.pojo2Json(rulesInfo));
                Log.e("plotchecker", JsonUtils.pojo2Json(stepInfo));
            }
        } catch (Exception e) {
            Log.e("plotchecker", "errot:" + e.getMessage());
        }
        if (Plat.DEBUG)
            LogUtils.i("plotchecker ", statusResult.getFinished() + " -->" + ruleCode_t);
        if (!"auto".equals(ruleCode_t))
            statusResult.setFinished(true);
        callBack.onCompleted(new PlotRecipeNextEvent(ruleCode_t, statusResult, stepIndex));
    }

}
