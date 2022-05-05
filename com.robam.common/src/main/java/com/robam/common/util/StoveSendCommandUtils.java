package com.robam.common.util;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;

import java.util.ArrayList;

/**
 * Created by yinwei on 2017/12/25.
 */

public class StoveSendCommandUtils implements IsendCommand {

    protected AbsFan fan;
    protected Stove stove;
    protected Stove.StoveHead stoveHead;
    private ArrayList<CookStep> cookSteps;
    public int step;
    private CookStep cookStep;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int level = msg.what;
            setStoveLevel(level);
        }
    };

    public StoveSendCommandUtils(AbsFan fan, Stove stove, Stove.StoveHead stoveHead, ArrayList<CookStep> cookSteps, int step) {
        this.fan = fan;
        this.stove = stove;
        this.stoveHead = stoveHead;
        this.step = step;
        this.cookSteps = cookSteps;

    }

    public void setStep(int step) {
        LogUtils.i("20190522", "step");
        this.step = step;
    }

    public void setCookStep(CookStep cookStep) {
        LogUtils.i("20190522", "cookStep");
        this.cookStep = cookStep;
    }

    @Override
    public void onStart() {
       /* try {
            cookStep = DaoHelper.getDao(CookStep.class).queryForId(cookSteps.get(step).getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        String ste = null;
        if (null != stove) {
            ste = stove.getDp();
        }

        int fanlevel = cookStep.getParamByCodeName(ste, "fanGear");
        int stovelevel = cookStep.getParamByCodeName(ste, "stoveGear");
        LogUtils.i("20190522", "fanlevel:" + fanlevel + " stovelevel:" + stovelevel + "step:" + step);
        setFanLevel(fanlevel);
        if (null != stove)
            setStoveLevel(stovelevel);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onPreSend() {

    }

    @Override
    public void onFinish() {
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);

    }

    @Override
    public void onRestart() {

    }

    protected void setFanLevel(final int level) {
        LogUtils.i("20190522", "Stove setFanLevel level:" + level);
        if (fan != null) {
            if (!fan.isConnected()) {
                ToastUtils.show("烟机已离线", Toast.LENGTH_SHORT);
                return;
            }
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

    protected short failureNum;

    private void setStoveLevel(final int level) {
        if (stove != null && !stove.isConnected())
            ToastUtils.show("灶具已离线", Toast.LENGTH_SHORT);
        if (stove != null && stoveHead != null) {
            stove.setStoveLevel(true, stoveHead.ihId, (short) level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    failureNum = 0;
                    mHandler.removeMessages(level);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (failureNum > 3) {
                        failureNum = 0;
                        return;
                    }
                    mHandler.sendEmptyMessageDelayed(level, 5000);
                    failureNum++;
                }
            });
        }
    }

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
}
