package com.robam.roki.service;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.Utils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepTip;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.StoveCookTaskService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.OpenStoveDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.robam.common.Utils.getDefaultFan;
import static com.robam.common.Utils.getDefaultStove;

/**
 * Created by as on 2017-04-07.
 */

public class MobileStoveCookTaskService extends StoveCookTaskService {

    static private MobileStoveCookTaskService instance = new MobileStoveCookTaskService();

    private final int FIRSH_SEK_TIME = 3000;
    List<Long> TimeList = new ArrayList<>();

    public void setRunning(boolean running) {
        isRunning = running;
    }

    protected boolean isPreview = false;

    synchronized public static MobileStoveCookTaskService getInstance() {
        synchronized (MobileStoveCookTaskService.class) {
            if (instance == null) {
                instance = new MobileStoveCookTaskService();
            }
        }
        return instance;
    }

    int timemobile;
    private MobileStoveCookTaskService() {
        SpeechManager.getInstance().init(Plat.app);
    }

    @Override
    public void dispose() {
        super.dispose();
        SpeechManager.getInstance().dispose();
    }

    @Override
    protected void onShowCookingView() {
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putInt(PageArgumentKey.RecipeStepIndex, stepIndex);
        bd.putBoolean(PageArgumentKey.isRunning,isRunning);
        UIService.getInstance().postPage(PageKey.RecipeCooking, bd);
    }


    @Override
    public void start(Stove.StoveHead stoveHead, Recipe book, String str) {
        this.stoveHead = stoveHead;
        stepIndex = Integer.parseInt(str);
        if (Plat.DEBUG)
            LogUtils.i("20170911","stepindex:"+stepIndex);
        if (stepIndex != 0){
            stepIndex-=1;
            next();
        }
        if (stoveHead == null) {
            stepIndex = 0;
            isPreview = true;
            onShowCookingView();
            return;
        } else
            isPreview = false;
        if (isRunning)
            return;
    }


    @Override
    public void start(Stove.StoveHead stoveHead, ArrayList<CookStep> steps,Long id) {
        this.stoveHead = stoveHead;
        this.steps = steps;
        this.startTime = Calendar.getInstance().getTimeInMillis();
        if (stoveHead != null) {
            stove = stoveHead.parent;
            fan = (AbsFan) stove.getParent();
        } else {
            fan = getDefaultFan();
            stove = Utils.getDefaultStove();
        }
        stepIndex = -1;
        isRunning = true;
        isPreview = false;
        next();
        onShowCookingView();
        onStart();
    }

    private Stove.StoveHead tempStoveHead;

    @Subscribe
    private void onEvent(StoveStatusChangedEvent event) {
        Stove stove = event.pojo;
        if (Plat.DEBUG)
             LogUtils.i("2017222","stovestatus:"+stove.rightHead.status);
        if (!stove.getParent().isConnected()){
            ToastUtils.show("烟机已离线",Toast.LENGTH_SHORT);
        }
        if (!stove.isConnected()){
            ToastUtils.show("灶具已离线",Toast.LENGTH_SHORT);
        }
        if (stove.rightHead==stoveHead && stove.rightHead.status == StoveStatus.Off){
            stoveHead.status = stove.rightHead.status;
            stopCountdown();
            timemobile = remainTime;
            next(stepIndex);
            if (Plat.DEBUG)
                LogUtils.i("20170913","rightHead");
        }else if (stove.leftHead==stoveHead&& stove.leftHead.status == StoveStatus.Off){
            stoveHead.status = stove.leftHead.status;
            stopCountdown();
            timemobile = remainTime;
            next(stepIndex);
            if (Plat.DEBUG)
               LogUtils.i("20170913","leftHead");
        }

    }

    @Override
    public void next() {
        if (Plat.DEBUG)
           LogUtils.i("20170911","next:");

        if (stoveHead!=null){
            stove = stoveHead.parent;
            if (!stove.isConnected()){
                ToastUtils.show("灶具已离线",Toast.LENGTH_SHORT);
                return;
            }
        }


        if (isPreview)
            return;
        //是否最后一个步骤
        boolean isLastStep = stepIndex + 1 == steps.size();
        if (isLastStep) {
            //是否仍在倒计时
            boolean isCountdown = remainTime > 0;
            if (isCountdown) {
                // 仍在倒计时
                if (Plat.DEBUG)
                    LogUtils.i("20171011","isCountdown");
//                onAskAtEnd();
            } else {
                // 倒计时完成
                if (Plat.DEBUG){
                    LogUtils.i("20171011","isCountdown");
                }
                stepIndex++;
                stop();
            }
        } else {
            // 中间步骤
            stepIndex++;
            remainTime=0;
            try{
             steps =  DaoHelper.getDao(Recipe.class).queryForId(recipeId).getJs_cookSteps();
            }catch (SQLException e){
                e.printStackTrace();
            }

            CookStep step = steps.get(stepIndex);
            isNeedDc(step,stepIndex);
            if (Plat.DEBUG){
                LogUtils.i("20170911","wo zou le ");
            }
        }
    }

    private void next(int stepIndex){
        CookStep step = steps.get(stepIndex);
        if (Plat.DEBUG)
           LogUtils.i("20170911","stepIndex:::..."+stepIndex);
        if(!stove.isConnected()){
           ToastUtils.show("灶具离线",Toast.LENGTH_SHORT);
        }
        boolean isLastStep = stepIndex + 1 == steps.size();
        if (isLastStep) {
            //是否仍在倒计时
            boolean isCountdown = remainTime > 0;
            if (isCountdown) {
                // 仍在倒计时
//                onAskAtEnd();
            } else {
                // 倒计时完成
                stepIndex++;
                stop();
            }
        }else{
            isNeedDc(step,stepIndex);
        }

    }

    OpenStoveDialog dlg;
    private void isNeedDc(CookStep step,int stepIndex){
        if (Plat.DEBUG){
            LogUtils.i("20170911","retime:"+remainTime);
            LogUtils.i("20170911","step:"+step.getDc());
        }
        if ("RRQZ".equals(step.getDc())){//需要设备
            //LogUtils.i("20170911","stoveHead::"+stoveHead.status);
            if(stoveHead==null){//没有关联的炉头
              //  LogUtils.i("20170911","没有炉头index:"+stepIndex);
                isExitStoveHead(stepIndex,step);
            }else{//有
                if (stoveHead.status!=0&&stoveHead.status!=3){
                //    LogUtils.i("20170911","有炉头且是开着的");
                    setCommand(step);
                    onNext();
                }else{
                 //   LogUtils.i("20170911","有炉头且关着的");
                  //  ToastUtils.show("请在灶具上开启",Toast.LENGTH_SHORT);
                    FragmentActivity activity = UIService.getInstance().getMain().getActivity();
                    cx = activity;
                    if (dlg!=null&&dlg.isShowing()){
                        return;
                    }
                   // dlg=OpenStoveDialog.showDialog(cx,recipeId,stepIndex,stoveHead);
                    setCommand(step);
                    stopCountdown();
                }
            }
        }else{//不需要设备
          //  LogUtils.i("20170911","不需要设备");
            stopCountdown();
            setFanLevel(0);
            LogUtils.i("20190522", "setFanLevel 3:");
            if(stoveHead==null){//是否有关联的炉头

            }else{//有
                if (stoveHead.status!=0){
               //     LogUtils.i("20170911","我关机了");
                    setStoveStatus(StoveStatus.Off, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            stoveHead.status =0;
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            //setStoveLevel(level);
                        }
                    });
                    setFanLevel(0);
                    LogUtils.i("20190522", "setFanLevel 4:");
                }
            }
        }
    }

    @Override
    public void pause() {
        if (steps!=null){
            LogUtils.i("20171024","stepindex:"+stepIndex);
            if (!"RRQZ".equals(steps.get(stepIndex).getDc()))
                return;
        }
        super.pause();
    }

    @Override
    public void stop() {
        timemobile=0;
        TimeList.clear();
        super.stop();
    }
    @Override
    public void back() {
      //  LogUtils.i("20170911","back");
        if(stoveHead!=null){
            stove = stoveHead.parent;
        }
        if (stepIndex == 0 || isPreview)
            return;
        stepIndex--;
        remainTime=0;
        Recipe recipe =null;
        try {
            recipe = DaoHelper.getDao(Recipe.class).queryForId(recipeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (recipe != null){
            if (stepIndex < 0){
                stepIndex = 0;
            }
            steps = recipe.getJs_cookSteps();
            CookStep step = steps.get(stepIndex);
       //     LogUtils.i("20170911","stove!=null");
            isNeedDc(step,stepIndex);
        }
    }

    @Override
    public void setCommand(final CookStep step) {
        if (stoveHead == null){
            if (null != getDefaultFan()){
                if ("RRQZ".equals(step.getDc())){
                    LogUtils.i("20170911","fanGearrrtttttt"+getDefaultFan().getDp());

                    String dp = step.getjs_PlatformCodes().get(0).platCode;
                    startCountdown(step.getParamByCodeName(dp, "needTime"));
                    int fanGear = step.getParamByCodeName(dp, "fanGear");
                      //  LogUtils.i("20170911","fanGearrrtttttt"+fanGear);
                    setFanLevel(fanGear);
                }else{
                    stopCountdown();
                    setFanLevel(0);
                }

            }
            return;
        }
        short status = stoveHead.getStatus();
     //   LogUtils.i("20170911","stove.status:"+status+"steindex::"+stepIndex);
            if (stepIndex == 0 && status == StoveStatus.StandyBy){
                delaySet(step);
            }else if (StoveStatus.Working == status){
                setStepParams(step);
            }else if (StoveStatus.StandyBy == status){
                delaySet(step);
            }

        int timetemp=step.getParamByCodeName(getDefaultStove().getDp(), "needTime");
      //  LogUtils.i("20170914","timetemp:"+timetemp+" timemobile:"+timemobile+" step::"+stepIndex);
      //  LogUtils.i("20170911","timemobile::"+timemobile);
        if (timemobile!=0 && null!=getDefaultStove()){
            startCountdown(timemobile);
            timemobile=0;
        }else if(null != getDefaultStove()){
            startCountdown(timetemp);
        }

    }

    private void delaySet(final CookStep step){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(FIRSH_SEK_TIME);
                    setStepParams(step);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onStoped() {
        super.onStoped();
        timemobile=0;
        UIService.getInstance().popBack();
    }

//    @Override
//    protected void onAskAtEnd() {
//        super.onAskAtEnd();
//    }

    @Override
    protected void onCount(int count) {
        super.onCount(count);
      /*  if (stepIndex+1==steps.size()&&count<=0){
            stop();
            return;
        }*/
        CookStep step = steps.get(stepIndex);
        List<CookStepTip> tips = step.getJs_tips();
       /* for (int i = 0; i <tips.size() ; i++) {
            LogUtils.i("2017108",tips.get(i).toString());
        }*/
        if (tips != null && tips.size() > 0) {
           // LogUtils.i("2017108","step.needTime:"+step.needTime+"count::"+count);
            int lostTime = step.needTime - count;
            for (CookStepTip tip : tips) {
            //    LogUtils.i("2017108","tip:"+tip.time+"losttime::"+lostTime);
                if ((tip.time+1) == lostTime) {
                    speak(tip.prompt);
                }
            }
        }
    }

    private void speak(final String text) {

        TaskService.getInstance().postUiTask(new Runnable() {

            @Override
            public void run() {
                SpeechManager.getInstance().startSpeaking(text);
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestore() {
        super.onRestore();
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    private void isExitStoveHead(int index,CookStep step){
        if (stove == null || !stove.isConnected()) {
           /* if ("RRQZ".equals(step.getDc())){
                NoStoveDialog.showNotic(cx,book,stepIndex);//未检测到灶具
            }*/
            //if (stepIndex!=0)
            ToastUtils.show("未检测到灶具", Toast.LENGTH_SHORT);
            setCommand(step);
        } else {
            boolean isOffLeft = stove.leftHead != null && stove.leftHead.status == StoveStatus.Off;
            boolean isOffRight = stove.rightHead != null && stove.rightHead.status == StoveStatus.Off;

            if (isOffLeft != isOffRight) {
                //有一个是开机状态
                stoveHead = (!isOffLeft ? stove.leftHead : stove.rightHead);
                setCommand(step);
               // ToastUtils.show("开始工作12", Toast.LENGTH_SHORT);
            } else {
                //左右灶状态一样
                if (isOffLeft) {
                    //都是关机状态
                    //等待开火，请在灶具上开启
                    FragmentActivity activity = UIService.getInstance().getMain().getActivity();
                    cx = activity;
                   // LogUtils.i("20170911","都是关机的状态:"+index);
                  //  ChooseStoveByWaitDialog.showDialog(cx,book,index);
                } else {
                    //都是开机状态
                    boolean isStandByLeft = stove.leftHead != null && stove.leftHead.status == StoveStatus.StandyBy;
                    boolean isStandByRight = stove.rightHead != null && stove.rightHead.status == StoveStatus.StandyBy;
                    if (isStandByLeft != isStandByRight) {
                        //其中有一个是待机状态
                       // startCook(isStandByLeft ? stove.leftHead : stove.rightHead, book);
                        stoveHead = (isStandByLeft ? stove.leftHead : stove.rightHead);
                        setCommand(step);
                     //   ToastUtils.show("开始工作45", Toast.LENGTH_SHORT);
                    } else {
                        //都是待机或都是工作中时，需要选择R,L
                        FragmentActivity activity = UIService.getInstance().getMain().getActivity();
                        cx = activity;
                        stoveHead = (isStandByLeft ? stove.leftHead : stove.rightHead);
                      //  ChooseStoveByManualDialog.showSelectStove(cx, book,index);
                    }
                }
            }
        }
    }
}
