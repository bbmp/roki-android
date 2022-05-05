package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/30.
 * 中部文本内容
 */

public class DialogType_09 extends BaseDialog {

    private TextView mTvAlarmContent;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public DialogType_09(Context context) {
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
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_09, null);
        mTvAlarmContent = rootView.findViewById(R.id.tv_alarm_content);
        createDialog(rootView);
    }

    @Override
    public void setContentText(CharSequence contentStr) {
        mTvAlarmContent.setText(contentStr);
    }

    @Override
    public void setContentText(int contentStrId) {
        mTvAlarmContent.setText(contentStrId);
    }

    @Override
    public void setToastShowTime(final int time) {
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
               if (msg.arg1 == 0){
                   mTimer.cancel();
                   mTimerTask.cancel();
                   dismiss();
               }
            }
        };

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            int totalTime = time;
            @Override
            public void run() {
                totalTime -= 1;
                Message message = mHandler.obtainMessage();
                message.arg1 = totalTime;
                mHandler.sendMessage(message);
            }
        };

        mTimer.schedule(mTimerTask,0,1000);

    }

    @Override
    public void bindAllListeners() {}
}
