package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.ui.view.SlideLockView;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by rent on 2016/9/30.
 */

public class ChildLockDialog extends Dialog {
    int res;
    CallBack callBack;
    View contentView;
    TextView tv_child_lock;
    SlideLockView slideLockView;
    Vibrator mVibrator;
    AbsRika mRika;
    Context mContext;
    private interface CallBack {
        void OnPositive();

        void OnNegative();
    }

    public ChildLockDialog(Context context, int res, CallBack callBack, AbsRika rika) {
        super(context, R.style.Theme_Dialog_FullScreen);
        mContext = context;
        this.res = res;
        this.callBack = callBack;
        this.mRika = rika;
        iniiView();
    }

    void iniiView() {
        contentView = LayoutInflater.from(mContext)
                .inflate(res, null, false);
        tv_child_lock = contentView.findViewById(R.id.tv_child_lock);
        slideLockView = contentView.findViewById(R.id.slideLockView);

        setContentView(contentView);
        setListener();
    }

    public void setListener(){
        // 获取系统振动器服务
        mVibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
        slideLockView.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                // 启动震动器 100ms
                mVibrator.vibrate(100);
                mRika.setSterilizerLockStatus(MsgKeys.setDeviceRunStatus_Req, (short)1, RikaStatus.STERIL_CATEGORYCODE
                        , (short) 1, (short)50, (short)1, RikaStatus.STERIL_LOCK_OFF,new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                dismiss();
                            }
                            @Override
                            public void onFailure(Throwable t) {}
                        });
            }

        });
    }

    public static ChildLockDialog dlg;

    public static ChildLockDialog build(Context cx, int res, CallBack callBack, AbsRika rika) {
        dlg = new ChildLockDialog(cx, res, callBack, rika);
        return dlg;
    }
}
