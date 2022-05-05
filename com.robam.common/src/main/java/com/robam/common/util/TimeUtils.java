package com.robam.common.util;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rent on 2016/7/28.
 */

public class TimeUtils {
    /**
     * 传入秒，设置min和s
     */
    public static TextView setMinAndSec(TextView view, long sec) {
        short min_ = (short) (sec / 60);
        short sec_ = (short) (sec % 60);
        if (sec_ < 10)
            view.setText(min_ + ":0" + sec_);
        else
            view.setText(min_ + ":" + sec_);
        return view;
    }

    public static TextView setMinAndSecByUnity(TextView view, long sec) {
        short min_ = (short) (sec / 60);
        short sec_ = (short) (sec % 60);
        if (min_ == 0) {
            view.setText(sec_ + "秒");
        } else {
            if (sec_ == 0) {
                view.setText(min_ + "分钟");
            } else {
                view.setText(min_ + "分钟" + sec_ + "秒");
            }
        }
        return view;
    }


}
