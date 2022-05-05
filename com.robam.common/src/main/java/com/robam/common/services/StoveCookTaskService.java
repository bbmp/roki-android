package com.robam.common.services;

import android.content.Context;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.CookCountdownEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.ui.dialog.PauseDialog;

import java.util.concurrent.TimeUnit;

/**
 * Created by as on 2017-04-07.
 */

public abstract class StoveCookTaskService extends AbsCookTaskService implements StoveCookTaskInterface {
    protected boolean isPreview = false;
    /**
     * 启动烧菜
     *
     * @param
     */
    @Override
    public void onStart() {

    }

    /**
     * 执行下一步工序
     *
     * @return
     */
    @Override
    public void onNext() {

    }

    /**
     * 暂停
     */
    @Override
    public void pause(VoidCallback callback) {
        if (isPause || isPreview)
            return;
        isPause = true;

        if (isRunning) {
            stopCountdown();
        }

        if (fan != null) {
            fan.pause();
        }
        if (stoveHead != null) {
            stoveHead.pause(callback);
        }
        onPause();
    }

    @Override
    public void pause() {
        pause(null);
    }

    @Override
    public void onPause() {
        Context cx = UIService.getInstance().getTop().getActivity();
        if (IAppType.RKDRD.equals(Plat.appType))
            PauseDialog.show(cx, new PauseDialog.DialogCallback() {

                @Override
                public void onRestore() {
                    restore();
                }
            });
    }

    /**
     * 从暂停恢复
     */
    @Override
    public void restore(VoidCallback callback) {

        if (!isPause || isPreview)
            return;

        if (isRunning) {
            startCountdown(remainTime);
        }

        if (fan != null) {
            fan.restore();
        }
        if (stoveHead != null) {
            stoveHead.restore(callback);
        }

        onRestore();
        isPause = false;
    }

    /**
     * 从暂停恢复
     */
    @Override
    public void restore() {
        restore(null);
    }

    @Override
    public void onRestore() {
    }

    /**
     * 执行上一步工序
     *
     * @return
     */
    @Override
    public void back() {
        if (stepIndex == 0 || isPreview)
            return;

        stepIndex--;

        CookStep step = steps.get(stepIndex);
        setCommand(step);
    }

    @Override
    public void onBack() {
    }

    protected void startCountdown(final int needTime) {
        if (!isRunning)
            return;
        stopCountdown();
        remainTime = needTime;
        future = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                remainTime--;
                onCount(remainTime);

                if (remainTime <= 0) {
                    stopCountdown();
                }
            }
        }, 500, 1000, TimeUnit.MILLISECONDS);
    }

    protected void onCount(final int count) {
        LogUtils.i("20171111","count:"+count);
        EventUtils.postEvent(new CookCountdownEvent(stepIndex, count));
        if (count <= 0) {
            if (stove == null || stoveHead == null)
                return;
            if (stoveHead.status == StoveStatus.StandyBy
                    || stoveHead.status == StoveStatus.Off
                    || stoveHead.level == Stove.PowerLevel_0) {
                return;
            }
            if (IRokiFamily.R9B39.equals(stove.getStoveModel())){
                setStoveLevel(Stove.PowerLevel_1);
            }else{
                setStoveLevel(Stove.PowerLevel_0);
            }

        }
    }
}
