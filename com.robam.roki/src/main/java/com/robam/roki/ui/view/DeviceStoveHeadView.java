package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.legent.ui.ext.dialogs.NumberDialog;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove.StoveHead;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceStoveHeadView extends FrameLayout {

    @InjectView(R.id.txtClock)
    TextView txtClock;
    @InjectView(R.id.txtLevel)
    TextView txtLevel;
    @InjectView(R.id.imgUp)
    ImageView imgUp;
    @InjectView(R.id.imgDown)
    ImageView imgDown;
    @InjectView(R.id.imgCornerLeft)
    ImageView imgCornerLeft;
    @InjectView(R.id.imgCornerRight)
    ImageView imgCornerRight;
    @InjectView(R.id.divTransparent)
    RelativeLayout divTransparent;
    @InjectView(R.id.switchView)
    DeviceSwitchView switchView;

    Stove stove;
    StoveHead head;

    public DeviceStoveHeadView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceStoveHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceStoveHeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void loadData(StoveHead head) {
        this.head = head;
        this.stove = head.parent;

        boolean isLeft = head.ihId == StoveHead.LEFT_ID;
        imgCornerLeft.setVisibility(!isLeft ? GONE : VISIBLE);
        imgCornerRight.setVisibility(isLeft ? GONE : VISIBLE);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_stove_head,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    @OnClick(R.id.txtClock)
    public void onClickClock() {

        if (!checkConnection()) return;
        if (!checkIsPowerOn()) return;
        if (head.level < Stove.PowerLevel_1)
            return;

        NumberDialog.show(getContext(), "设置倒计时", 0, 99, 0, new NumberDialog.NumberSeletedCallback() {
            @Override
            public void onNumberSeleted(int i) {
                int seconds = 60 * i;
                setTimeShutDown((short) seconds);
            }
        });

    }

    @OnClick(R.id.switchView)
    public void onClickSwitch() {

        setStatus();
    }

    @OnClick(R.id.imgUp)
    public void onClickUp() {

        if (head.status == StoveStatus.StandyBy) {
            setLevel(5);
        } else {
            int level = head.level;
            if (level < 9) {
                level++;
                setLevel(level);
            }
        }
    }

    @OnClick(R.id.imgDown)
    public void onClickDown() {

        if (head.status == StoveStatus.StandyBy) {
            setLevel(5);
        } else {
            int level = head.level;
            if (level > 1) {
                level--;
                setLevel(level);
            }
        }
    }

    public void refresh() {

        if (head == null || !stove.isConnected()) {
            setViewValid(false);
            setViewEnable(false);
            showStaus(false);
        } else {
            setViewValid(true);
            showStaus(true);

            if (stove.isLock) {
                setViewEnable(false);
                switchView.setEnabled(true);
            } else {
                setViewEnable(true);
            }
        }
    }

    void setViewValid(boolean isValid) {

        imgUp.setSelected(isValid);
        imgDown.setSelected(isValid);
        txtClock.setSelected(isValid);
        txtLevel.setSelected(isValid);

        showLevel(0);
        showCountdownTime((short) 0);
    }

    void setViewEnable(boolean isEnable) {
        imgUp.setEnabled(isEnable);
        imgDown.setEnabled(isEnable);
        txtClock.setEnabled(isEnable);
        switchView.setEnabled(isEnable);
    }

    void showStaus(boolean valid) {
        if (valid) {
            boolean isOn = head.status != StoveStatus.Off;
            switchView.setStatus(isOn);
            showLevel(head.level);
            showCountdownTime(head.time);
            divTransparent.setVisibility(stove.isLock ? VISIBLE : GONE);
        } else {
            switchView.setStatus(false);
            showLevel(0);
            showCountdownTime((short) 0);
            divTransparent.setVisibility(GONE);
        }
    }

    void showLevel(int level) {
        String str = "--";
        if (level >= 1 && level <= 9) {
            str = String.valueOf(level);
        }
        txtLevel.setText(str);
    }

    void showCountdownTime(short time) {
        String strTime = time <= 0 ? "计时" : UiHelper.second2String(time);
        txtClock.setText(strTime);
    }


    void setTimeShutDown(final short seconds) {

        stove.setStoveShutdown(head.ihId, seconds, new VoidCallback() {

            @Override
            public void onSuccess() {
                showCountdownTime(seconds);
                ToastUtils.showShort("倒计时已设置");
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    void setStatus() {

        if (!checkConnection()) return;
        if (!checkIsPowerOn()) return;

        short status = (head.status == StoveStatus.Off) ? StoveStatus.StandyBy
                : StoveStatus.Off;

        stove.setStoveStatus(false, head.ihId, status, new VoidCallback() {

            @Override
            public void onSuccess() {
                refresh();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    void setLevel(int level) {
        if (!checkConnection()) return;
        if (!checkIsPowerOn()) return;

        stove.setStoveLevel(false, head.ihId, (short) level,
                new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        refresh();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });
    }


    boolean checkConnection() {
        if (!stove.isConnected()) {
            ToastUtils.showShort(R.string.stove_invalid_error);
            return false;
        } else {
            return true;
        }
    }

    boolean checkIsPowerOn() {
        if (head.status != StoveStatus.Off)
            return true;
        else {
            ToastUtils.showShort(R.string.stove_off_hint);
            return false;
        }
    }

}
