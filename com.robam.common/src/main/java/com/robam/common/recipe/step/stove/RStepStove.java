package com.robam.common.recipe.step.stove;


import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.VoidCallback4;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.common.recipe.step.AbsRStepCook;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;
import com.robam.common.recipe.step.inter.callback.IStepCallback_Stove;

import java.util.ArrayList;

import static com.legent.plat.constant.IPlatRokiFamily.R9B39;
/**
 * Created by as on 2017-07-13.
 */

public abstract class RStepStove extends AbsRStepCook {

    Short mSHeadId;

    Stove[] mStoves;

    Stove[] stoves;

    public RStepStove(int cookIns, ArrayList<CookStep> cookSteps, int stepIndex, IStepCallback callback) {
        super(cookIns, cookSteps, stepIndex, callback);
    }

    @Override
    protected void setDeviceCommand(final VoidCallback4 callback2) {
        if (stoves[0] != null)
            setFanParam(mCookStep.getParamByCodeName(stoves[0].getDc(), stoves[0].getDp(), paramCode.FAN_LEVEL).getValue());
        else {
            setFanParam(mCookStep.getParamByCodeName(null, paramCode.FAN_LEVEL));
            if (callback2 != null)
                callback2.onSuccess();
        }
        if (stoves[0] != null) {
            Log.i("20171109", "getDeviceType:" + mStoves[0].getDeviceType().getID() + " stoves[0].isLock:" + stoves[0].isLock);
            if (IRokiFamily.R9B39.equals(mStoves[0].getDeviceType().getID()) && stoves[0].isLock) {
                stoves[0].setStoveLock(!stoves[0].isLock, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        setStoveparam(mCookStep.getParamByCodeName(stoves[0].getDc(), stoves[0].getDp(), paramCode.STOVE_LEVEL).getValue(),
                                new VoidCallback4() {
                                    @Override
                                    public void onSuccess() {
                                        if (callback2 != null)
                                            callback2.onSuccess();
                                    }

                                    @Override
                                    public void onFailure(Object error) {
                                        if (callback2 != null)
                                            callback2.onFailure(error);
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback2.onFailure(2);
                    }
                });
            } else {
                LogUtils.i("20180408","ddd:"+paramCode.STOVE_LEVEL);
                setStoveparam(mCookStep.getParamByCodeName(stoves[0].getDc(), stoves[0].getDp(), paramCode.STOVE_LEVEL).getValue(),
                        new VoidCallback4() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20180414","setDeviceCommand:");
                                if (callback2 != null)
                                    callback2.onSuccess();
                            }

                            @Override
                            public void onFailure(Object error) {
                                if (callback2 != null)
                                    callback2.onFailure(error);
                            }
                        });
            }
        }
    }

    /**
     * 设置烟机档位
     *
     * @param level
     */
    private void setFanParam(final int level) {
        IFan fan = Utils.getDefaultFan();
        if (fan != null) {
            fan.setFanLevel((short) level, null);
        }
    }

    /**
     * 设置灶具档位，设置前观察灶具状态
     *
     * @param level
     */
    protected void setStoveparam(final int level, final VoidCallback4 callback2) {
        if (mStoves[0] != null) {
            setStoveLevel(level, callback2);
//            if (mStoves[0].getHeadById(mSHeadId).status == StoveStatus.Off) {
//                setStoveStatus(StoveStatus.StandyBy, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        mFailureNum = 0;
//                        setStoveLevel(level, callback2);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        if (mFailureNum > 3) {
//                            mFailureNum = 0;
//                            return;
//                        }
//                        mFailureNum++;
//                        setStoveparam(level, callback2);
//                    }
//                });
//                return;
//            } else {
//
//            }
        }
    }

    /**
     * 设置灶具状态
     *
     * @param status
     */
    protected void setStoveStatus(final int status, final VoidCallback callback) {
        if (mStoves[0] != null) {
            mStoves[0].setStoveStatus(true, mSHeadId, (short) status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mFailureNum = 0;
                    if (callback != null)
                        callback.onSuccess();
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > 5) {
                        mFailureNum = 0;
                        callback.onFailure(t);
                        return;
                    }
                    mFailureNum++;
                    setStoveStatus(status, callback);
                }
            });
        }
    }

    protected void setStoveLevel(final int level, final VoidCallback4 callback2) {
        if (stoves[0] != null && stoves[0].getHeadById(mSHeadId) != null) {
            LogUtils.i("20180414","headId::"+mSHeadId+"status::"+stoves[0].getHeadById(mSHeadId).status);
            if (stoves[0].getHeadById(mSHeadId).status == StoveStatus.Off) {
                short status = StoveStatus.StandyBy;
                if (IRokiFamily.R9B39.equals(mStoves[0].getDeviceType().getID())){
                     status = StoveStatus.Working;
                }
                LogUtils.i("20180414","status::"+status);
                setStoveStatus(status, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mFailureNum = 0;
                        if (IRokiFamily.R9B39.equals(mStoves[0].getDeviceType().getID())){
                            try {
                                Thread.sleep(5600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setStoveLevel2(level, callback2);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (mFailureNum > 5) {
                            mFailureNum = 0;
                            callback2.onFailure(1);
                            return;
                        }
                        mFailureNum++;
                        setStoveLevel(level, callback2);
                    }
                });
            } else {
                setStoveLevel2(level, callback2);
            }
        }
    }

    private void setStoveLevel2(final int level, final VoidCallback4 callback4) {
        if (stoves[0] == null || stoves[0].getHeadById(mSHeadId) == null)
            return;

        if (!stoves[0].isConnected()) {
            ToastUtils.show("灶具已离线", Toast.LENGTH_SHORT);
        }


        stoves[0].setStoveLevel(true, mSHeadId, (short) level, new VoidCallback() {

            @Override
            public void onSuccess() {
                LogUtils.i("20180414","stoveLevel2::succc");
                mFailureNum = 0;
                callback4.onSuccess();
            }

            @Override
            public void onFailure(Throwable t) {
                if (mFailureNum > 5) {
                    mFailureNum = 0;
                    callback4.onFailure(1);
                    return;
                }
                LogUtils.i("20180414","stoveLevel2::fail");
                mFailureNum++;
                setStoveLevel2(level, callback4);
            }
        });
    }


  /*  */
    /**
     * 下发灶具档位
     *
     * @param level
     *//*
    private void setStoveLevel(final int level, final VoidCallback4 callback2) {
        if (stoves[0] != null) {
            Log.i("20171110", "level:" + level);
            stoves[0].setStoveLevel(true, mSHeadId, (short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    Log.i("stove_sts", "灶具下发档位成功：" + level);
                    mFailureNum = 0;
                    if (callback2 != null)
                        callback2.onSuccess();
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("stove_sts", "灶具下发档位失败：" + mFailureNum);
                    if (mFailureNum > 3) {
                        mFailureNum = 0;
                        callback2.onFailure(1);
                        return;
                    }
                    mFailureNum++;
                    setStoveLevel(level, callback2);
                }
            });
        }
    }*/

    boolean isPause;

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (!mIsCounting)
            return;
        if (mStoves[0] != null) {
            mFailureNum = 0;
            mStoves[0].getHeadById(mSHeadId).pause(new VoidCallback() {

                @Override
                public void onSuccess() {
                    isPause = true;
                    mFailureNum = 0;
                    stopCountdown();
                    mServiceCallback.onPause(null);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                        mFailureNum = 0;
                        mServiceCallback.onPause(new RecipeException.RecipeStepPauseOrRestoreException(1));
                    }
                    mFailureNum++;
                    try {
                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                    pause();
                }
            });
        }
    }

    /**
     * 从暂停状态恢复
     */
    @Override
    public void restore() {
        if (mIsCounting)
            return;
        if (mStoves[0] != null) {
            mFailureNum = 0;
            mStoves[0].getHeadById(mSHeadId).restore(new VoidCallback() {
                @Override
                public void onSuccess() {
                    isPause = false;
                    mFailureNum = 0;
                    startCountdown(mRemainTime);
                    mServiceCallback.onRestore(null);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (mFailureNum > MAX_SENDPARAM_NUM) {
                        mFailureNum = 0;
                        mServiceCallback.onPause(new RecipeException.RecipeStepPauseOrRestoreException(1));
                    }
                    mFailureNum++;
                    try {
                        Thread.sleep(MAX_SENDPARAM_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                    restore();
                }
            });
        }
    }

    @Override
    protected void closeDevice() {
        if (stoves[0] != null) {
            short status = stoves[0].getHeadById(mSHeadId).status;
            short level = stoves[0].getHeadById(mSHeadId).level;
            if (status == StoveStatus.StandyBy
                    || status == StoveStatus.Off) {
                return;
            }
            setFanParam(0);
            //关闭设备
            stoves[0].setStoveStatus(true, mSHeadId, StoveStatus.StandyBy, null);
        }
    }

    @Override
    protected void closeRRQZ() {
        if (stoves[0] != null) {
            short status = stoves[0].getHeadById(mSHeadId).status;
            short level = stoves[0].getHeadById(mSHeadId).level;
            if (status == StoveStatus.StandyBy
                    || status == StoveStatus.Off) {
                return;
            }
            //关闭烟机
            setFanParam(0);
            //关闭设备
            stoves[0].setStoveStatus(true, mSHeadId, StoveStatus.Off, null);
        }
    }

    /**
     * 倒计时完成
     */
    @Override
    protected void onCountComplete() {
        super.onCountComplete();
        if (stoves[0] != null) {
            short status = stoves[0].getHeadById(mSHeadId).status;
            short level = stoves[0].getHeadById(mSHeadId).level;
            if (status == StoveStatus.StandyBy
                    || status == StoveStatus.Off) {
                return;
            }
            short setlevel = Stove.PowerLevel_0;
            if (R9B39.equals(stoves[0].getStoveModel()) || IRokiFamily.R9B37.equals(stoves[0].getStoveModel())) {
                setlevel = Stove.PowerLevel_1;
            } else {
                setlevel = Stove.PowerLevel_0;
            }

            setStoveparam(setlevel, new VoidCallback4() {

                @Override
                public void onSuccess() {
                    mServiceCallback.onRefresh();
                }

                @Override
                public void onFailure(Object error) {

                }
            });
        }
    }

    @Override
    protected void onException(int index) {
    }

    @Override
    protected void onWarn(int alarmId) {
    }

    /**
     * 报警恢复
     */
    @Override
    protected void onWarnRecovery(int alarmId) {

    }


    /*@Override
    public void onEvent(DeviceConnectionChangedEvent event) {
        super.onEvent(event);
        LogUtils.i("20180413","stove_isConnect");
        mServiceCallback.onDisconnect(event.device);
    }*/

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        Log.i("stove_sts", "----------: mWaitFlag:" + mWaitFlag + " IFPREFLAG:" + IFPREFLAG + " mIsPolling:" + mIsPolling
                + " status:" + stoves[0].getHeadById(mSHeadId).status);
        if (!mIsPolling || event == null || event.pojo == null ||
                !Objects.equal(stoves[0].getID(), event.pojo.getID()))
            return;
        if (stoves[0] == null)
            return;
        mServiceCallback.onPolling(stoves[0]);
        Log.i("stove_sts", "00000000000: mWaitFlag:" + mWaitFlag + " IFPREFLAG:" + IFPREFLAG + "  " + stoves[0].getHeadById(mSHeadId).status);
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (stoves[0].getHeadById(mSHeadId).status == StoveStatus.Working)) {
            startCountdown(getNeedTime());
            IFPREFLAG = 2;
            mWaitFlag = false;
            isPause = false;
            Log.i("stove_sts", "开始倒计时");
            return;
        }
        if (mWaitFlag && (IFPREFLAG == 0 || IFPREFLAG == 1) && (stoves[0].getHeadById(mSHeadId).status == StoveStatus.Off)) {
//            refreshInit();
//            Log.i("stove_sts", "s重新初始化");
            LogUtils.i("20180412","tttttttt");
            return;
        }
        if (IFPREFLAG == 2 && (stoves[0].getHeadById(mSHeadId).status == StoveStatus.Off
                || stoves[0].getHeadById(mSHeadId).status == StoveStatus.StandyBy)) {
            refreshInit();
            Log.i("stove_sts", "设备关闭了");
        }
        if (IFPREFLAG == 2 && stoves[0].getHeadById(mSHeadId).status == StoveStatus.Working) {

            LogUtils.i("20180412","ggggggggg");
            short minlevel = (IRokiFamily.R9B39.equals(stoves[0].getStoveModel()) || IRokiFamily.R9B37.equals(stoves[0].getStoveModel())) ? Stove.PowerLevel_1 : Stove.PowerLevel_0;
            if (isPause) {
                if (stoves[0].getHeadById(mSHeadId).level > minlevel) {
                    isPause = false;
                    startCountdown(mRemainTime);
                } else {
                    stopCountdown();
                    mServiceCallback.onPause(null);
                }
            } else {
                isPause = false;
                startCountdown(mRemainTime);
            }
        }
    }

    @Override
    protected void addDevice(String id, String... headId) {
        if (headId != null && headId.length > 0) {
            this.mSHeadId = Short.parseShort(headId[0]);
        } else {
            this.mSHeadId = 0;
        }
        if (this.mStoves == null)
            this.mStoves = new Stove[1];
        this.mStoves[0] = DeviceService.getInstance().lookupChild(id);
        addDeviceRecipeMap(stoves[0].getID(), mRecipeInstance_Index);
    }

    @Override
    protected IDevice getDevice() {
        return this.mStoves[0];
    }

    @Override
    protected int getNeedTime() {
        return mCookStep.getParamByCodeName(stoves[0] != null ? stoves[0].getDp() : null,
                paramCode.NEED_TIME);
    }
}
