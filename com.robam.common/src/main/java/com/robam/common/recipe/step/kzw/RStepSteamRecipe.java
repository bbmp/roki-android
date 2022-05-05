package com.robam.common.recipe.step.kzw;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.VoidCallback4;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by as on 2017-07-21.
 */

public class RStepSteamRecipe extends RStepKZWRecipe {
    protected int temp;
    protected int time;
    AbsSteamoven steam;

    protected AbsSteamoven getSteam() {
        if (this.steam == null)
            this.steam = (AbsSteamoven) getDevice();
        return this.steam;
    }

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps    菜谱步骤 不能为空
     * @param stepindex 菜谱步骤号
     * @param callback2
     */
    public RStepSteamRecipe(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        super(cookIns, cookSteps, stepindex, callback2);
    }

    @Override
    protected void closeDevice() {
        if (!mIsPolling || StringUtils.isNullOrEmpty(this.mGuid)) {
            mServiceCallback.onClose(0);
            return;
        }

        if (steam != null){
            LogUtils.i("20180330","off");
            steam.setSteamStatus(SteamStatus.Off, new VoidCallback() {

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
        }
    }


    @Override
    void setDeviceParam(VoidCallback4 callback2) {
        paramCode param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.STEAM_TEMP);
        if (param != null)
            temp = param.value;
        param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.STEAM_TIME);
        if (param != null)
            time = param.value;
        setParam((short) time, (short) temp, callback2);
    }

    @Override
    protected int getNeedTime() {
        mCookStep.getParamByCodeName(getSteam() != null ? getSteam().getDp() : null, paramCode.STEAM_TIME);
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
        AbsSteamoven steamoven = getSteam();
        mFailureNum = 0;
        steamoven.setSteamStatus(SteamStatus.Working, new VoidCallback() {
            @Override
            public void onSuccess() {
                mFailureNum = 0;
                mServiceCallback.onStart(new RecipeException.RecipeStepPreStartException(0));
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
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!mIsPolling || event == null || event.pojo == null ||
                !Objects.equal(getDevice().getID(), event.pojo.getID()))
            return;
        AbsSteamoven steamoven = getSteam();
        if (steamoven == null)
            return;
        mServiceCallback.onPolling(steamoven);
        Log.i("20171106", "---------->IFPREFLAG:" + IFPREFLAG + " mWaitFlag:" + mWaitFlag + " steamoven.status:" + steamoven.status);
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (steamoven.status == SteamStatus.Working ||
                steamoven.status == SteamStatus.PreHeat)) {
            Log.i("20171106", "11111:");
//            if (steam.time < (steam.timeSet * 60)) {
//                startCountdown(steam.time);
//            } else {
//                onCount(steam.timeSet * 60);
//            }
            IFPREFLAG = 2;
            mWaitFlag = false;
            return;
        }
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (steamoven.status == SteamStatus.Wait
                || steamoven.status == SteamStatus.Off || steamoven.status == SteamStatus.Order)) {
            Log.i("20171106", "22222:");
            refreshInit();
            return;
        }
        if (IFPREFLAG == 2 && (steamoven.status == SteamStatus.Order ||
                steamoven.status == SteamStatus.Off || steamoven.status == SteamStatus.Wait ||
                steamoven.status == SteamStatus.On)) {
            Log.i("20171106", "333333:");
            refreshInit();
            return;
        }
        if (IFPREFLAG == 2 && (steamoven.status == SteamStatus.Pause || steamoven.status == SteamStatus.AlarmStatus)) {
            Log.i("20171106", "44444:");
            stopCountdown();
            mServiceCallback.onPause(null);
            return;
        }
        if (IFPREFLAG == 2 && (steamoven.status == SteamStatus.Working ||
                steamoven.status == SteamStatus.PreHeat)) {
            Log.i("20171106", "555555:"+"steam.time:::"+steam.time+" oldtime::"+oldtime);

            if (steam.time < oldtime) {
                startCountdown(steam.time);
            } else {
                onCount(steam.time);
            }
            oldtime = steam.time;
            return;
        }
//        if (IFPREFLAG == 2 && steamoven.alarm != 255) {
//            onWarn(steamoven.alarm);
//            mWaitFlag = true;
//            return;
//        }
//        if (mWaitFlag && IFPREFLAG == 2 && steamoven.alarm == 255) {
//            mWaitFlag = false;
//            onWarnRecovery(steamoven.alarm);
//            return;
//        }
    }

    private short oldtime = 0;

    @Override
    public void pause() {
        if (!mIsCounting && !(steam.status == SteamStatus.PreHeat || steam.status == SteamStatus.Working))
            return;
        mFailureNum = 0;
        getSteam().setSteamStatus(SteamStatus.Pause, new VoidCallback() {
            @Override
            public void onSuccess() {
                mFailureNum = 0;
            }

            @Override
            public void onFailure(Throwable t) {
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
        getSteam().setSteamStatus(SteamStatus.Working, new VoidCallback() {
            @Override
            public void onSuccess() {
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
    protected void onCountComplete() {
        mServiceCallback.onComplete(0);
//        IDevice device = getDeviceByID(mGuid);
//        AbsSteamoven steamoven = (AbsSteamoven) device;
//        mFailureNum = 0;
//        steamoven.setSteamStatus(SteamStatus.On, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                mFailureNum = 0;
//                mServiceCallback.onComplete(0);
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
        time = 0;
        temp = 0;
    }

    @Override
    protected Map<String, Object> prerun() {
        return null;
    }

    /**
     * 下发 参数 注意：下发时间为分钟
     *
     * @param time_
     * @param temp_
     * @param callback
     */
    protected void setParam(final short time_, final short temp_, final VoidCallback4 callback) {
        if (getSteam().status == OvenStatus.Wait || getSteam().status == OvenStatus.Off) {
            getSteam().setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    getSteam().setSteamProMode((short) (time_ / 60), temp_,
                            (short) IFPREFLAG, new VoidCallback() {
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
                                    setParam(time_, temp_, callback);
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
                    setParam(time_, temp_, callback);
                }
            });
        } else {
            getSteam().setSteamProMode((short) (time_ / 60), temp_,
                    (short) IFPREFLAG, new VoidCallback() {
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
                            setParam(time_, temp_, callback);
                        }
                    });
        }
    }
}
