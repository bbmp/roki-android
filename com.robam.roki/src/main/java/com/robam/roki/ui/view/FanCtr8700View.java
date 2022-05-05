package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.Fan8700;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FanCtr8700View extends FrameLayout implements UIListeners.IFanCtrView {

    Fan8700 fan;

    @InjectView(R.id.gearLow)
    DeviceFanGearView gearLow;
    @InjectView(R.id.gearHigh)
    DeviceFanGearView gearHigh;
    @InjectView(R.id.imgLight)
    ImageView imgLight;

    public FanCtr8700View(Context context) {
        super(context);
        init(context, null);
    }

    public FanCtr8700View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_8700,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    @OnClick(R.id.imgLight)
    public void onClickLight() {
        setLight();
    }

    @OnClick(R.id.gearLow)
    public void onClickLow() {
        if (fan.level >= AbsFan.PowerLevel_1 && fan.level < AbsFan.PowerLevel_3)
            setLevel(AbsFan.PowerLevel_0);
        else
            setLevel(AbsFan.PowerLevel_2);
    }

    @OnClick(R.id.gearHigh)
    public void onClickHigh() {
        if (fan.level == AbsFan.PowerLevel_6 || fan.level == AbsFan.PowerLevel_3)
            setLevel(AbsFan.PowerLevel_0);
        else
            setLevel(AbsFan.PowerLevel_6);
    }


    @Override
    public void attachFan(IFan fan) {
        Preconditions.checkState(fan instanceof Fan8700, "attachFan error:not 8700");
        this.fan = (Fan8700) fan;
    }

    @Override
    public void onRefresh() {
        if (fan == null)
            return;

        imgLight.setSelected(fan.light);
        showLevel(fan.level);
    }

    void showLevel(int level) {
        gearLow.setSelected(false);
        gearHigh.setSelected(false);
        gearLow.setTitle("弱", "档");
        gearHigh.setTitle("爆", "炒");

        if (level == AbsFan.PowerLevel_0)
            return;

        boolean isLow = level < AbsFan.PowerLevel_3;
        boolean isLevel3 = level == AbsFan.PowerLevel_3;
        gearLow.setSelected(isLow);
        gearHigh.setSelected(!isLow);

        if (!isLow && isLevel3) {
            gearHigh.setTitle("强", "档");
        }

    }

    void setLight() {
//        if (!checkConnection())
//            return;

        fan.setFanLight(!fan.light, new VoidCallback() {

            @Override
            public void onSuccess() {
                imgLight.setSelected(fan.light);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    void setLevel(final int level) {
//        if (!checkConnection())
//            return;

        fan.setFanLevel((short) level, new VoidCallback() {

            @Override
            public void onSuccess() {
                showLevel(level);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    boolean checkConnection() {
        if (!fan.isConnected()) {
            ToastUtils.showShort(R.string.fan_invalid_error);
            return false;
        } else {
            return true;
        }
    }

}
