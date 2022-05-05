package com.robam.common.recipe.step.kzw;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.VoidCallback4;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by as on 2017-07-25.
 */

public class RStepMicroRecipe extends RStepKZWRecipe {
    protected int power;
    protected int time;
    protected int mode;
    protected int weight;
    AbsMicroWave microWave;

    protected AbsMicroWave getMicroWave() {
        if (this.microWave == null)
            this.microWave = (AbsMicroWave) getDevice();
        return this.microWave;
    }

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps    菜谱步骤 不能为空
     * @param stepindex 菜谱步骤号
     * @param callback2
     */
    public RStepMicroRecipe(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        super(cookIns, cookSteps, stepindex, callback2);
    }


    @Override
    void setDeviceParam(VoidCallback4 callback2) {
        paramCode param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.MICRO_MODE);
        if (param != null)
            mode = param.value;
        param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.MICRO_TIME);
        if (param != null)
            time = param.value;
        param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.MICRO_POWER);
        if (param != null)
            power = param.value;
        param = mCookStep.getParamByCodeName(getDevice().getDc(), getDevice().getDp(), paramCode.MICRO_WEIGHT);
        if (param != null)
            weight = param.value;
        setParam((short) mode, (short) time, (short) power, callback2);
    }


    @Override
    protected int getNeedTime() {
        mCookStep.getParamByCodeName(getMicroWave() != null ? getMicroWave().getDp() : null,
                paramCode.MICRO_TIME);
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
        AbsMicroWave microWave = getMicroWave();
        mFailureNum = 0;
        microWave.setMicroWaveState(MicroWaveStatus.Run, new VoidCallback() {
            @Override
            public void onSuccess() {
                mFailureNum = 0;
                mServiceCallback.onStart((short) 0);
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
    public void onEvent(MicroWaveStatusChangedEvent event) {
        if (!isSetRun)
            return;
        if (!mIsPolling && microWave.state == MicroWaveStatus.Run) {
            mIsPolling = true;
            mWaitFlag = true;
            mServiceCallback.onStart(null);
        }
        if (!mIsPolling || event == null || event.pojo == null ||
                !Objects.equal(getDevice().getID(), event.pojo.getID()))
            return;
        AbsMicroWave microWave = getMicroWave();
        if (microWave == null)
            return;
        mServiceCallback.onPolling(microWave);
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && microWave.state == MicroWaveStatus.Run) {
            startCountdown(microWave.time);
            IFPREFLAG = 2;
            mWaitFlag = false;
            return;
        }
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (microWave.state == MicroWaveStatus.Wait
                || microWave.state == MicroWaveStatus.RunFinish)) {
            refreshInit();
            return;
        }
        if (IFPREFLAG == 2 && (microWave.state == MicroWaveStatus.Wait ||
                microWave.state == MicroWaveStatus.Setting && microWave.state == MicroWaveStatus.RunFinish)) {
            refreshInit();
            return;
        }
        if (IFPREFLAG == 2 && (microWave.state == MicroWaveStatus.Pause || microWave.state == MicroWaveStatus.Alarm)) {
            stopCountdown();
            mServiceCallback.onPause(null);
            return;
        }
        if (IFPREFLAG == 2 && microWave.state == MicroWaveStatus.Run) {
            startCountdown(microWave.time);
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

    @Override
    public void pause() {
        Log.i("20171109", "pause: mIsCounting  " + mIsCounting);
        if (!mIsCounting)
            return;
        mFailureNum = 0;
        (getMicroWave()).setMicroWaveState(MicroWaveStatus.Pause, new VoidCallback() {
            @Override
            public void onSuccess() {
                Log.i("20171109", "pause: onSuccess  ");
                mIsCounting = false;
                mFailureNum = 0;
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("20171109", "pause: onFailure  " + mFailureNum);
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
        (getMicroWave()).setMicroWaveState(MicroWaveStatus.Run, new VoidCallback() {
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
        Log.i("20171109", "closeDevice:" + mIsPolling);
        if (!mIsPolling || StringUtils.isNullOrEmpty(this.mGuid)) {
            mServiceCallback.onClose(0);
            return;
        }

        if (microWave != null) {
            Log.i("20171109", "microWave.setMicroWaveState(MicroWaveStatus.Wait:" + microWave.state);
            if (microWave.state==MicroWaveStatus.Run){
                microWave.setMicroWaveState(MicroWaveStatus.Pause, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        microWave.setMicroWaveState(MicroWaveStatus.Wait, new VoidCallback() {
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

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("下发指令失败", Toast.LENGTH_SHORT);
                    }
                });
            }

        }
    }

    @Override
    protected void onCountComplete() {
        mServiceCallback.onComplete(0);
//        IDevice device = getDeviceByID(mGuid);
//        AbsMicroWave microWave = (AbsMicroWave) device;
//        mFailureNum = 0;
//        microWave.setMicroWaveState(MicroWaveStatus.Wait, new VoidCallback() {
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
        mode = 0;
        power = 0;
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
    protected boolean isSetRun;

    protected void setParam(final short mode_, final short time_, final short temp_, final VoidCallback4 callback) {
        mFailureNum = 0;
        Log.i("20171109", "mode:" + mode_ + " time_" + time_ + " temp:" + temp_);
        getMicroWave().setMicroWaveProModeHeat(mode_, time_, temp_,
                (short) IFPREFLAG, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("20171109", "onSuccess ");
                        isSetRun = true;
                        mFailureNum = 0;
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("20171109", "onFailure ");
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
                        setParam(mode_, time_, temp_, callback);
                    }
                });
    }
}
