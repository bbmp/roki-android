package com.robam.common.recipe.step.kzw;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.VoidCallback4;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkModelStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;



/**
 * Created by as on 2017-07-17.
 */

public class RStepSteamOvenOneRecipe extends RStepKZWRecipe {
    private int mode;
    private int temp;
    private int time;
    private int tempup;
    private int tempbelow;
    AbsSteameOvenOne steameOvenOne;

    protected AbsSteameOvenOne getAbsSteamOven() {
        if (this.steameOvenOne == null)
            this.steameOvenOne = (AbsSteameOvenOne) getDevice();
        return this.steameOvenOne;
    }

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps    菜谱步骤 不能为空
     * @param stepindex 菜谱步骤号
     * @param callback2
     */
    public RStepSteamOvenOneRecipe(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        super(cookIns, cookSteps, stepindex, callback2);
        EventUtils.regist(RStepSteamOvenOneRecipe.this);
    }

    @Override
    public void dispose() {
        try {
            EventUtils.unregist(RStepSteamOvenOneRecipe.this);
        } catch (Exception e) {

        }
    }


    @Override
    void setDeviceParam(VoidCallback4 callback2) {
        IDevice device = getAbsSteamOven();
        paramCode param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_MODE);
        mode = param.value;
        param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_TEMP);
        if (param != null)
            temp = param.value;
        param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_TIME);
        if (param != null)
            time = param.value;
        param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_UP);
        if (param != null)
            tempup = param.value;
        param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_BELOW);
        if (param != null)
            tempbelow = param.value;
        if (mode == SteamOvenOneModel.EXP)
            temp = tempup;

        setSteamovenOneRun((short) mode, (short) (time/60), (short) temp, (short) tempbelow, callback2);
    }


    @Override
    protected int getNeedTime() {
        IDevice device = getAbsSteamOven();
        paramCode param = mCookStep.getParamByCodeName(device.getDc(), device.getDp(), paramCode.STEAMOVENONE_TIME);
        return param.value;
    }

    public void setSteamovenOneRun(final short mode, final short time, final short tempup, final short tempdn, final VoidCallback4 callback) {
        final VoidCallback cb = new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20180404","success");
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
            }
        };
        if (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    if (mode != SteamOvenOneWorkModelStatus.ExpModel){
                        steameOvenOne.setSteameOvenOneRunMode(mode, time, tempup, (short) 0,
                                 tempdn, (short) 255, (short) 255, cb);
                    } else{
                        steameOvenOne.setSteameOvenOneRunMode(mode,
                                time, tempup, (short) 0,
                                tempdn, (short) 255, (short) 255, cb);
                    }

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
                }
            });
        } else {
            if (mode != SteamOvenOneWorkModelStatus.ExpModel){
                steameOvenOne.setSteameOvenOneRunMode(mode,
                         time, tempup, (short) 0,
                        (short) 0, (short) 255, (short) 255, cb);
            } else{
                steameOvenOne.setSteameOvenOneRunMode(mode,
                        time, tempup, (short) 0,
                        tempdn, (short) 255, (short) 255, cb);
            }

        }
    }


    @Override
    public void setrun() {

    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        Log.i("steamovenone_st_cook", "----------: mWaitFlag:" + mWaitFlag + " IFPREFLAG:" + IFPREFLAG + " mIsPolling:" + mIsPolling);
        if (!mIsPolling || event == null || event.pojo == null ||
                !Objects.equal(getDevice().getID(), event.pojo.getID()))
            return;
        AbsSteameOvenOne steameOvenOne = (AbsSteameOvenOne) getDevice();
        if (steameOvenOne == null)
            return;
        mServiceCallback.onPolling(steameOvenOne);
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                && steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            if (steameOvenOne.leftTime < (steameOvenOne.setTime * 60)) {
                startCountdown(steameOvenOne.leftTime);
            } else {
                onCount(steameOvenOne.setTime * 60);
            }
            IFPREFLAG = 2;
            mWaitFlag = false;
            return;
        }
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off
                || steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait || (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                && steameOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus))) {
            Log.i("steamovenone_st_cook", "正在等待");
            return;
        }
        if (IFPREFLAG == 2 && (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off
                || steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait || (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                && steameOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus
                && steameOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.Pause
                && steameOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.TimeDisplay))) {
            Log.i("steamovenone_st_cook", "stopCountdown 2");
            refreshInit();
            Log.i("steamovenone_st_cook", "重新初始化");
            return;
        }
        if (IFPREFLAG == 2 && (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                && steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause)) {
            Log.i("steamovenone_st_cook", "stopCountdown 1");
            stopCountdown();
            mServiceCallback.onPause(null);
            Log.i("steamovenone_st_cook", "暂停或报警");
            return;
        }
        if (IFPREFLAG == 2 && (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                && steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            LogUtils.i("20180414","lefttime:"+steameOvenOne.leftTime+"settime:"+(steameOvenOne.setTime * 60));
            if (steameOvenOne.leftTime < (steameOvenOne.setTime * 60)) {
                startCountdown(steameOvenOne.leftTime);
            } else {
                onCount(steameOvenOne.setTime * 60);
            }
            Log.i("steamovenone_st_cook", "工作中");
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
        AbsSteameOvenOne steameOvenOne = (AbsSteameOvenOne) getDevice();
       /* if (oven == null)
            return;*/
        LogUtils.i("20180414","powerStatus:"+steameOvenOne.powerStatus+" powerOnStatus:"+
                steameOvenOne.powerOnStatus+" miscount:"+mIsCounting);
        if (!mIsCounting &&!(steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                &&steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus
                &&steameOvenOne.worknStatus == SteamOvenOneWorkStatus.PreHeat))
            return;
        mFailureNum = 0;
        LogUtils.i("20180414","pause");
        steameOvenOne.setSteameOvenStatus_pause(new VoidCallback() {
            @Override
            public void onSuccess() {
                mIsCounting = false;
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
        AbsSteameOvenOne steameOvenOne = (AbsSteameOvenOne) getDevice();
       /* if (oven == null)
            return;*/
        if (!mIsCounting && !(steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause))
            return;
        LogUtils.i("20180414","pause-restore");
        mFailureNum = 0;
        steameOvenOne.setSteameOvenStatus_rerun(new VoidCallback() {
            @Override
            public void onSuccess() {
                mIsCounting = false;
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
        LogUtils.i("20180414","close");
        AbsSteameOvenOne steameOvenOne = (AbsSteameOvenOne) getDevice();
        if (steameOvenOne != null)
            steameOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
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


}
