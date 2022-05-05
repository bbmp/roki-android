package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 2017/8/17.
 * 即将开始工作，倒计时 3 2 1
 */
public class DialogType_07 extends BaseDialog {


    private TextView mTvCountDown;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public DialogType_07(Context context) {
        super(context);
    }


    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_07, null);
        mTvCountDown = rootView.findViewById(R.id.tv_count_down);
        createDialog(rootView);
        initTask();
    }

    private void initTask() {

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mTvCountDown.setText(msg.arg1 + "");
                if (msg.arg1 <= 0) {
                    mTimer.cancel();
                    mTimerTask.cancel();
                    dismiss();
                }
            }
        };

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            int count = 3;

            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.arg1 = count;
                mHandler.sendMessage(msg);
                count -= 1;
            }
        };

        mTimer.schedule(mTimerTask, 0, 1000);

    }

    @Override
    public void bindAllListeners() {
    }


}
