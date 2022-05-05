package com.robam.common.services;

import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.services.AbsService;
import com.legent.services.TaskService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove.StoveHead;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.util.StoveSendCommandUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static com.robam.common.Utils.getDefaultStove;


abstract public class AbsCookTaskService extends AbsService implements IAbsCookTaskInterface {

    protected AbsFan fan;
    protected Stove stove;
    protected StoveHead stoveHead;
    protected List<CookStep> steps = new ArrayList<>();
    protected long startTime, endTime;

    protected boolean isRunning, isPause;
    protected int stepIndex, remainTime;
    protected ScheduledFuture<?> future;
    protected boolean isPreview = false;
    protected CookStep step;
    protected Long recipeId;
    private List<CookStep> mStepList;

    private int num;

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunnung(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public boolean isPause() {
        return isPause;
    }

    @Override
    public int getStepCount() {
        return steps != null ? steps.size() : 0;
    }

    @Override
    public int getStepIndex() {
        return stepIndex;
    }

    @Override
    public int getRemainTime() {
        return remainTime;
    }

    StoveSendCommandUtils stoveUtils;

    /**
     * 启动烧菜
     *
     * @param
     */
    @Override
    public void start(StoveHead stoveHead, ArrayList<CookStep> steps, Long id) {
        if (isRunning)
            return;
        this.stoveHead = stoveHead;
        this.startTime = Calendar.getInstance().getTimeInMillis();
        this.steps = steps;
        this.recipeId = id;
        if (stoveHead != null) {
            stove = stoveHead.parent;
            fan = (AbsFan) stove.getParent();
        } else {
            fan = Utils.getDefaultFan();
        }
        stepIndex = -1;
        isRunning = true;
        stoveUtils = new StoveSendCommandUtils(fan, stove, stoveHead, steps, 0);
        LogUtils.i("20190610", "start_stoveUtils:" + stoveUtils);
        next();
        onShowCookingView();
//          onStart();
    }

    abstract protected void onStart();

    public void setStep(int pos) {
        LogUtils.i("20190516", "pos:" + pos);
        this.stepIndex = pos;
        stepIndex -= 1;
        next();

    }

    /**
     * 执行下一步工序
     *
     * @return
     */
    @Override
    public synchronized void next() {
        //是否最后一个步骤
        boolean isLastStep = stepIndex + 1 == steps.size();
        LogUtils.i("isLastStep", "isLastStep:" + isLastStep);
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
        } else {
            // 中间步骤
            stepIndex++;
            try {
                step = DaoHelper.getDao(CookStep.class).queryForId(steps.get(stepIndex).getID());
                LogUtils.i("20190522", "step:" + step + " stepIndex:" + stepIndex);
                if (StringUtils.isNullOrEmpty(step.dc)){
                    stop();
                    setIsRunnung(false);
                }else {
                    setCommand(step);
                }

                onNext();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    abstract protected void onNext();

    /**
     * 烧菜完成
     */
    @Override
    public void stop() {
        LogUtils.i("20190521", "isPreview:" + isPreview + "stepIndex:" + stepIndex + " isRunning:" + isRunning);
        if (isPreview)
            return;
        this.endTime = Calendar.getInstance().getTimeInMillis();
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);
        stopCountdown();
        onStoped();
        this.stove = null;
        this.stepIndex = -1;
        isRunning = false;
    }

    protected void onStoped() {

    }


    /**
     * 下发控制指令并计时
     */
    public void setCommand(CookStep step) {
        setStepParams(step);
        if (null != getDefaultStove()) {
            if (stove != null) {
                startCountdown(step.getParamByCodeName(stove.getDp(), "needTime"));
            }
        }
    }

    /*************************
     * .
     * 计时控制 开和关
     *************************/

    abstract protected void startCountdown(final int needTime);

    protected void stopCountdown() {
        if (!isRunning)
            return;

        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
    // -----------------------------------------------------------------------------------------------------


    /*************************
     * 设备控制
     *********************************/
    protected void setStepParams(final CookStep step) {
        LogUtils.i("20190610", " setStepParams：" + step.dc);
        TaskService.getInstance().postUiTask(new Runnable() {

            @Override
            public void run() {
                try {
                    stoveUtils.setCookStep(step);
                    stoveUtils.setStep(stepIndex);
                    stoveUtils.onStart();
                } catch (Exception e) {
                    if (Plat.DEBUG)
                        LogUtils.i("20170407", "AbsCookTaskService postUiTask" + e.getMessage());
                }

            }
        }, 500);
    }

    /**
     * 失败次数
     */
    protected short failureNum;

    /**
     * 设置烟机档位
     *
     * @param level
     */
    protected void setFanLevel(final int level) {
        LogUtils.i("20190522", "AbsCookTask setFanLevel level:" + level);
        if (fan != null) {
            fan.setFanLevel((short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("isLastStep", "下发成功 fanlevel:" + level);
                    failureNum = 0;
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("isLastStep", "下发失败 :" + t.toString());
                    if (failureNum > 3) {
                        failureNum = 0;
                        return;
                    }
                    failureNum++;
                    setFanLevel(level);
                }
            });
        }
    }

    /**
     * 设置灶具状态
     *
     * @param status
     */
    protected void setStoveStatus(final int status, final VoidCallback callback) {
        LogUtils.i("20171011", "status::" + stove);
        if (stove != null && stoveHead != null) {
            LogUtils.i("20171011", "status");
            stove.setStoveStatus(true, stoveHead.ihId, (short) status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                    if (callback != null)
                        callback.onSuccess();
                }

                @Override
                public void onFailure(Throwable t) {
                    if (failureNum > 5) {
                        failureNum = 0;
                        if (callback != null)
                            callback.onFailure(t);
                        return;
                    }
                    failureNum++;
                    setStoveStatus(status, callback);
                }
            });
        }
    }

    /**
     * 设置灶具档位，设置前观察灶具状态
     *
     * @param level
     */
    protected void setStoveLevel(final int level) {
        if (stove != null && stoveHead != null) {
            if (stoveHead.status == StoveStatus.Off) {
                setStoveStatus(StoveStatus.StandyBy, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        setStoveLevel2(level);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        setStoveLevel(level);
                    }
                });
                return;
            }
            setStoveLevel2(level);
        }
    }

    /**
     * 下发灶具档位
     *
     * @param level
     */
    private void setStoveLevel2(final int level) {

        if (stove != null && !stove.isConnected())
            ToastUtils.show("灶具已离线", Toast.LENGTH_SHORT);
        if (stove != null && stoveHead != null) {
            stove.setStoveLevel(true, stoveHead.ihId, (short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20170614", "cishu::" + failureNum);
                    if (failureNum > 3) {
                        failureNum = 0;
                        return;
                    }
                    failureNum++;
                    setStoveLevel2(level);
                }
            });
        }
    }

    protected void onShowCookingView() {
        // TODO
    }

//    protected void onAskAtEnd() {
//        Context cx = UIService.getInstance().getTop().getActivity();
//        String message = "已到最后一步,是否退出？";
//        DialogHelper.newDialog_OkCancel(cx, null, message,
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dlg, int witch) {
//                        if (witch == DialogInterface.BUTTON_POSITIVE) {
//                            stop();
//                        }
//                    }
//                }).show();
//    }

}
