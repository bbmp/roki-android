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
import com.robam.common.pojos.device.fan.Fan9700;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class FanCtr9700View extends FrameLayout implements UIListeners.IFanCtrView {

    Fan9700 fan;

    @InjectView(R.id.imgLight)
    ImageView imgLight;

    @InjectViews({R.id.gear1, R.id.gear2, R.id.gear3, R.id.gear6})
    List<DeviceFanGearView> gearViews;


    public FanCtr9700View(Context context) {
        super(context);
        init(context, null);
    }

    public FanCtr9700View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_9700,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            gearViews.get(0).setTag(AbsFan.PowerLevel_1);
            gearViews.get(1).setTag(AbsFan.PowerLevel_2);
            gearViews.get(2).setTag(AbsFan.PowerLevel_3);
            gearViews.get(3).setTag(AbsFan.PowerLevel_6);
        }
    }

    @OnClick(R.id.imgLight)
    public void onClickLight() {
        setLight();
    }

    @OnClick({R.id.gear1, R.id.gear2, R.id.gear3, R.id.gear6})
    public void onClickGear(View v) {
        short level = (Short) v.getTag();
        if (level == fan.level) {
            setLevel(AbsFan.PowerLevel_0);
        } else {
            setLevel(level);
        }
    }

    @Override
    public void attachFan(IFan fan) {
        Preconditions.checkState(fan instanceof Fan9700, "attachFan error:not 9700");
        this.fan = (Fan9700) fan;
    }

    @Override
    public void onRefresh() {
        if (fan == null)
            return;

        imgLight.setSelected(fan.light);
        showLevel(fan.level);
    }

    void showLevel(int level) {
        for (DeviceFanGearView v : gearViews) {
            v.setSelected(false);
        }

        if (level == AbsFan.PowerLevel_0)
            return;

        for (DeviceFanGearView v : gearViews) {
            if ((Short) v.getTag() == level) {
                v.setSelected(true);
                break;
            }
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
