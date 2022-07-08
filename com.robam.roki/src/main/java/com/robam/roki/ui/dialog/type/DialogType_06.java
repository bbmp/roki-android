package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.legent.ContextIniter.cx;

/**
 * Created by  on 2017/8/16.
 * 正在连接，数字百分比
 */
public class DialogType_06 extends BaseDialog {


    private TextView mTvPro;
    private Timer mTimer;
    private Timer mTimerCenter;
    private Timer mTimerAfter;

    private TimerTask mTimerTask;
    private TimerTask mTimerTaskCenter;
    private TimerTask mTimerTaskAfter;

    Handler mHandlerAfter = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTvPro.setText(msg.arg1 + "%");
            if (msg.arg1 >= 100) {
                mTimerAfter.cancel();
                mTimerTaskAfter.cancel();
                mTimerAfter = null;
                mTimerTaskAfter = null;
                if (mAnimationDrawable != null && mAnimationDrawable.isRunning()){
                    mAnimationDrawable.stop();
                    mAnimationDrawable = null;
                }
                dismiss();

            }
        }
    };


    Handler mHandlerCenter = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            try {
                mTvPro.setText(msg.arg1 + "%");

                final int randomNumber = msg.arg2;
                LogUtils.i("20180321", "msg.arg_222:" + msg.arg1);
                if (msg.arg1 > 88 && msg.arg1 < 100) {
                    mTimerCenter.cancel();
                    mTimerTaskCenter.cancel();
                    mTimerCenter = null;
                    mTimerTaskCenter = null;
                    mTimerAfter = new Timer();
                    mTimerTaskAfter = new TimerTask() {
                        int result = msg.arg1;

                        @Override
                        public void run() {
                            result += randomNumber;
                            Message message = mHandlerAfter.obtainMessage();
                            message.arg1 = result;
                            mHandlerAfter.sendMessage(message);
                        }
                    };
                    mTimerAfter.schedule(mTimerTaskAfter, 0, 18000);
                }
            }catch (Exception e){
                e.getMessage();
            }

        }
    };
    private ImageView mImageDot;
    private ImageView mMImageDot;
    private AnimationDrawable mAnimationDrawable;


    public DialogType_06(Context context) {
        super(context);

    }

    @Override
    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.dialog_bg_aim_aount, rootView, true);
            mDialog.setPosition(Gravity.CENTER, 0, 0);

        }
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(cx).inflate(R.layout.common_dialog_layout_type_06, null);
        mTvPro = rootView.findViewById(R.id.tv_pro);
        mMImageDot = rootView.findViewById(R.id.img_dot);
        mMImageDot.setBackgroundResource(R.drawable.animation_conmmion_dot);
        mAnimationDrawable = (AnimationDrawable) mMImageDot.getBackground();
        if (mAnimationDrawable != null){
            mAnimationDrawable.start();
        }
        createDialog(rootView);

    }

    @Override
    public void bindAllListeners() {
    }


    @Override
    public void setInitTaskData(final int randomNumber, final int readyTime, final long timingTime) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                mTvPro.setText(msg.arg1 + "%");
                LogUtils.i("20180321", "msg.arg_111:" + msg.arg1);
                if (msg.arg1 >= 10) {
                    mTimer.cancel();
                    mTimerTask.cancel();
                    mTimer = null;
                    mTimerTask = null;
                    mTimerCenter = new Timer();
                    mTimerTaskCenter = new TimerTask() {
                        int result = msg.arg1;

                        @Override
                        public void run() {
                            result += randomNumber;
                            Message message = mHandlerCenter.obtainMessage();
                            message.arg1 = result;
                            message.arg2 = randomNumber;
                            mHandlerCenter.sendMessage(message);
                        }
                    };
                    mTimerCenter.schedule(mTimerTaskCenter, 0, 135);

                }
            }
        };

        mTimer = new Timer();
        mTimerTask = new TimerTask() {

            int result;

            @Override
            public void run() {
                result += randomNumber;
                Message message = mHandler.obtainMessage();
                message.arg1 = result;
                mHandler.sendMessage(message);
            }

        };

        mTimer.schedule(mTimerTask, readyTime, 500);

    }


}
