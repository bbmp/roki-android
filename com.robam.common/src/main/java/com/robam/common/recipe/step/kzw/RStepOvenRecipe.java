package com.robam.common.recipe.step.kzw;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.VoidCallback4;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by as on 2017-07-17.
 */

public class RStepOvenRecipe extends RStepKZWRecipe {
    private int mode;
    private int temp;
    private int time;
    private int tempup;
    private int tempbelow;
    private int ovenUplayerTemp;
    private int ovenUplayerTime;
    private int ovenDownLayerTemp;
    private int ovenDownLayerTime;
    AbsOven oven;

    protected AbsOven getOven() {
        if (this.oven == null)
            this.oven = (AbsOven) getDevice();
        return this.oven;
    }

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps 菜谱步骤 不能为空
     * @param stepindex 菜谱步骤号
     * @param callback2
     */
    public RStepOvenRecipe(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        super(cookIns, cookSteps, stepindex, callback2);
        EventUtils.regist(RStepOvenRecipe.this);
    }

    @Override
    public void dispose() {
        try {
            EventUtils.unregist(RStepOvenRecipe.this);
        } catch (Exception e) {

        }
    }


    @Override
    void setDeviceParam(VoidCallback4 callback2) {
        paramCode code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_MODE);
        mode = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_TEMP);
        if (code != null)
            temp = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_TIME);
        if (code != null)
            time = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_TEMPUP);
        if (code != null)
            tempup = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_TEMPBELOW);
        if (code != null)
            tempbelow = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_UPLAYERTIME);
        if (code != null)
            ovenUplayerTime = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_UPLAYERTEMP);
        if (code != null)
            ovenUplayerTemp = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_DOWNLAYERTEMP);
        if (code != null)
            ovenDownLayerTemp = code.value;
        code = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.OVEN_DOWNLAYERTIME);
        if (code != null)
            ovenDownLayerTime = code.value;
        LogUtils.i("20180413", "mode::" + mode);
        AbsOven oven = (AbsOven) getDevice();
        if (IRokiFamily.RR039.equals(oven.getDt())) {
            setParam039((short) mode, (short) time, (short) temp, callback2);
        } else if (IRokiFamily.RR026.equals(oven.getDt()) || IRokiFamily.RR016.equals(oven.getDt())) {
            if (mode == 9) {
                temp = tempup;
                setParam026016((short) mode, (short) time, (short) temp, (short) tempbelow, callback2);
            } else {
                setParam026016((short) mode, (short) time, (short) temp, (short) 0, callback2);
            }

        }
    }


    @Override
    protected int getNeedTime() {
        mCookStep.getParamByCodeName(getOven() != null ? getOven().getDp() : null,
                paramCode.OVEN_TIME);
        return 0;
    }

    @Override
    public void setrun() {
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
        }
        if (!mWaitFlag)
            return;
        AbsOven oven = (AbsOven) getDevice();
        mFailureNum = 0;
        oven.setOvenStatus(OvenStatus.Working, new VoidCallback() {
            @Override
            public void onSuccess() {
                mFailureNum = 0;
                mServiceCallback.onStart(new Object());
            }

            @Override
            public void onFailure(Throwable t) {
                if (mFailureNum > MAX_SENDPARAM_NUM) {
                    mFailureNum = 0;
                    mServiceCallback.onStart(new RecipeException.RecipeStepPreStartException(1));
                    return;
                }
                mFailureNum++;
                try {
                    Thread.sleep(MAX_SENDPARAM_INTERVAL);
                } catch (InterruptedException e) {

                }
                setrun();
            }
        });
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        Log.i("oven_st", "----------: mWaitFlag:" + mWaitFlag + " IFPREFLAG:" + IFPREFLAG + " mIsPolling:" + mIsPolling);
        if (!mIsPolling || event == null || event.pojo == null ||
                !Objects.equal(getDevice().getID(), event.pojo.getID()))
            return;
        AbsOven oven = (AbsOven) getDevice();
        if (oven == null)
            return;
        mServiceCallback.onPolling(oven);
        Log.i("oven_st", "00000000000: mWaitFlag:" + mWaitFlag + " IFPREFLAG:" + IFPREFLAG);
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (oven.status == OvenStatus.Working ||
                oven.status == OvenStatus.PreHeat)) {
            if (true || IRokiFamily.RR039.equals(oven.getGuid().getDeviceTypeId())) {
                if (oven.time < (oven.setTime * 60)) {
                    Log.i("20171106", "startCountdown 1");
                    startCountdown(oven.time);
                } else {
                    onCount(oven.setTime * 60);
                }
            }
            IFPREFLAG = 2;
            mWaitFlag = false;
            Log.i("oven_st", "111111");
            return;
        }
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (oven.status == OvenStatus.Wait
                || oven.status == OvenStatus.Off || oven.status == OvenStatus.SelfTest
                || oven.status == OvenStatus.Order)) {
            Log.i("oven_st", "正在等待");
            return;
        }
        if (IFPREFLAG == 2 && (oven.status == OvenStatus.Order ||
                oven.status == OvenStatus.Off || oven.status == OvenStatus.Wait ||
                oven.status == OvenStatus.On || oven.status == OvenStatus.SelfTest)) {
            Log.i("20171106", "stopCountdown 2");
            refreshInit();
            Log.i("oven_st", "重新初始化");
            return;
        }
        if (IFPREFLAG == 2 && (oven.status == OvenStatus.Pause || oven.status == OvenStatus.AlarmStatus
                || oven.alarm != 255)) {
            Log.i("20171106", "stopCountdown 1");
            stopCountdown();
            mServiceCallback.onPause(null);
            Log.i("oven_st", "暂停或报警");
            return;
        }
        if (IFPREFLAG == 2 && (oven.status == OvenStatus.Working || oven.status == OvenStatus.PreHeat)) {
            if (true || IRokiFamily.RR039.equals(oven.getGuid().getDeviceTypeId())) {
                if (oven.time < (oven.setTime * 60)) {
                    Log.i("20171106", "startCountdown 2");
                    startCountdown(oven.time);
                } else {
                    onCount(oven.setTime * 60);
                }
            }
            Log.i("oven_st", "工作中");
            return;
        }
//        if (IFPREFLAG == 2 && oven.alarm != 255) {
//            onWarn(oven.alarm);
//            mWaitFlag = true;
//            Log.i("oven_st", "666666666");
//            return;
//        }
//        if (mWaitFlag && IFPREFLAG == 2 && oven.alarm == 255) {
//            mWaitFlag = false;
//            onWarnRecovery(oven.alarm);
//            Log.i("oven_st", "77777777777");
//            return;
//        }
    }


    @Override
    public void pause() {
        Log.i("20171106", "mIsCounting:" + mIsCounting + " this.mGuid:" + this.mGuid);
        AbsOven oven = (AbsOven) getDevice();
        if (oven == null)
            return;
        if (!mIsCounting && !(oven.status == OvenStatus.PreHeat || oven.status == OvenStatus.Working))
            return;
        mFailureNum = 0;
        ((AbsOven) getDevice()).setOvenStatus(OvenStatus.Pause, new VoidCallback() {
            @Override
            public void onSuccess() {
                Log.i("20171106", "suc:" + mFailureNum);
                mIsCounting = false;
                mFailureNum = 0;
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("20171106", "onFailure");
                if (mFailureNum > MAX_SENDPARAM_NUM) {
                    mFailureNum = 0;
                    mServiceCallback.onPause(new RecipeException.RecipeStepPauseOrRestoreException(1));
                    return;
                }
                mFailureNum++;
                pause();

            }
        });
    }

    @Override
    public void restore() {
        if (mIsCounting)
            return;
        mFailureNum = 0;
        ((AbsOven) getDevice()).setOvenStatus(OvenStatus.Working, new VoidCallback() {
            @Override
            public void onSuccess() {
                mIsCounting = true;
                mFailureNum = 0;
            }

            @Override
            public void onFailure(Throwable t) {
                if (mFailureNum > MAX_SENDPARAM_NUM) {
                    mFailureNum = 0;
                    mServiceCallback.onRestore(new RecipeException.RecipeStepPauseOrRestoreException(1));
                    return;
                }
                mFailureNum++;
                restore();

            }
        });
    }

    @Override
    protected void closeDevice() {
        if (!mIsPolling || StringUtils.isNullOrEmpty(this.mGuid)) {
            mServiceCallback.onClose(0);
            return;
        }
        if (oven != null)
            oven.setOvenStatus(OvenStatus.Off, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    mServiceCallback.onClose(0);
                    mIsPolling = false;
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                        mFailureNum = 0;
                        mServiceCallback.onClose(1);
                    }
                    try {
                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                    mFailureNum++;
                    closeDevice();
                }
            });
        return;
    }


    @Override
    protected void onCountComplete() {
        mServiceCallback.onComplete(0);
//        oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                mFailureNum = 0;
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (mFailureNum > MAX_SENDPARAM_NUM) {
//                    mFailureNum = 0;
//                    mServiceCallback.onComplete(1);
//                }
//                try {
//                    Thread.sleep(MAX_SENDPARAM_INTERVAL);
//                } catch (InterruptedException e) {
//                }
//                mFailureNum++;
//            }
//        });
    }

    @Override
    public void refreshInit() {
        super.refreshInit();
        mFailureNum = 0;
        mode = 0;
        time = 0;
        temp = 0;
    }

    @Override
    protected Map<String, Object> prerun() {
        return null;
    }

    protected void setParam026016(final short mode_, final short time_, final short temp_,
                                  final short tempbelow,
                                  final VoidCallback4 callback) {
        if (getOven().status == OvenStatus.Wait || getOven().status == OvenStatus.Off) {
            getOven().setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    LogUtils.i("20189999", "mode_::" + mode_ + "temp:" + temp_ + "tempbelow:" + tempbelow);
                    getOven().setOvenRunMode(AbsOven.getExpMode(mode_), (short) (time_ / 60), temp_,
                            (short) IFPREFLAG, (short) 0, (short) 0, (short) 1, tempbelow, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    mFailureNum = 0;
                                    callback.onSuccess();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                                        mFailureNum = 0;
                                        callback.onFailure(new RecipeException.RecipeStepStartException(1));
                                        return;
                                    }
                                    mFailureNum++;
                                    try {
                                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                                    } catch (InterruptedException e) {
                                    }
                                    setParam026016(mode_, time_, temp_, tempbelow, callback);
                                }
                            });
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                        mFailureNum = 0;
                        callback.onFailure(new RecipeException.RecipeStepStartException(1));
                        return;
                    }
                    mFailureNum++;
                    try {
                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                    setParam026016(mode_, time_, temp_, tempbelow, callback);
                }
            });
        } else {
            getOven().setOvenRunMode(AbsOven.getExpMode(mode_), (short) (time_ / 60), temp_,
                    (short) IFPREFLAG, (short) 0, (short) 0, (short) 1, tempbelow, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            mFailureNum = 0;
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            if (mFailureNum > MAX_SENDPARAM_NUM) {
                                mFailureNum = 0;
                                callback.onFailure(new RecipeException.RecipeStepStartException(1));
                                return;
                            }
                            mFailureNum++;
                            try {
                                Thread.sleep(MAX_SENDPARAM_INTERVAL);
                            } catch (InterruptedException e) {
                            }
                            setParam026016(mode_, time_, temp_, tempbelow, callback);
                        }
                    });
        }
    }

    protected void setParam039(final short mode_, final short time_, final short temp_, final VoidCallback4 callback) {
        if (getOven().status == OvenStatus.Wait || getOven().status == OvenStatus.Off) {
            getOven().setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    getOven().setOvenRecipeParams(mode_,
                            (short) (time_ / 60), temp_, (short) IFPREFLAG, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.i("123123", "success");
                                    mFailureNum = 0;
                                    callback.onSuccess();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.i("123123", "onFailure");
                                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                                        mFailureNum = 0;
                                        callback.onFailure(new RecipeException.RecipeStepStartException(1));
                                        return;
                                    }
                                    mFailureNum++;
                                    try {
                                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                                    } catch (InterruptedException e) {
                                    }
                                    setParam039(mode_, time_, temp_, callback);
                                }
                            });
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                        mFailureNum = 0;
                        callback.onFailure(new RecipeException.RecipeStepStartException(1));
                        return;
                    }
                    mFailureNum++;
                    try {
                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                    setParam039(mode_, time_, temp_, callback);
                }
            });
        } else {
            getOven().setOvenRecipeParams(mode_,
                    (short) (time_ / 60), temp_, (short) IFPREFLAG, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            mFailureNum = 0;
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            if (mFailureNum > MAX_SENDPARAM_NUM) {
                                mFailureNum = 0;
                                callback.onFailure(new RecipeException.RecipeStepStartException(1));
                                return;
                            }
                            mFailureNum++;
                            try {
                                Thread.sleep(MAX_SENDPARAM_INTERVAL);
                            } catch (InterruptedException e) {
                            }
                            setParam039(mode_, time_, temp_, callback);
                        }
                    });
        }
    }

}
