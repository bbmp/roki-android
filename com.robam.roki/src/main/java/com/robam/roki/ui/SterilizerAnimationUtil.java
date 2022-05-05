package com.robam.roki.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.robam.roki.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gu on 2015/12/28.
 */
public class SterilizerAnimationUtil {
    Context context;
    RelativeLayout rlTem,rlHum,rlGerm,rlOzone;

    private int i = 1;
    private boolean flag = true;
    private ArrayList<View> viewList = new ArrayList<View>();
    private ArrayList<View> viewList2 = new ArrayList<View>();
    private ArrayList<View> viewList3 = new ArrayList<View>();
    private ArrayList<View> viewList4 = new ArrayList<View>();
    private ArrayList<RelativeLayout.LayoutParams> paramsList = new ArrayList<RelativeLayout.LayoutParams>();
    private ArrayList<RelativeLayout.LayoutParams> paramsList2 = new ArrayList<RelativeLayout.LayoutParams>();
    private ArrayList<RelativeLayout.LayoutParams> paramsList3 = new ArrayList<RelativeLayout.LayoutParams>();
    private ArrayList<RelativeLayout.LayoutParams> paramsList4 = new ArrayList<RelativeLayout.LayoutParams>();
    private int[] ids = {R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (int i = 0; i < viewList.size(); i++) {
                paramsList.add((RelativeLayout.LayoutParams) viewList.get(i).getLayoutParams());
                paramsList2.add((RelativeLayout.LayoutParams) viewList2.get(i).getLayoutParams());
                paramsList3.add((RelativeLayout.LayoutParams) viewList3.get(i).getLayoutParams());
                paramsList4.add((RelativeLayout.LayoutParams) viewList4.get(i).getLayoutParams());
            }
            if (msg.what == 1) {
                setStepOne(paramsList);
                setStepOne(paramsList2);
                setStepOne(paramsList3);
                setStepOne(paramsList4);
            } else {
                setStepTwo(paramsList);
                setStepTwo(paramsList2);
                setStepTwo(paramsList3);
                setStepTwo(paramsList4);
            }
            for (int i = 0; i < viewList.size(); i++) {
                viewList.get(i).setLayoutParams(paramsList.get(i));
                viewList2.get(i).setLayoutParams(paramsList2.get(i));
                viewList3.get(i).setLayoutParams(paramsList3.get(i));
                viewList4.get(i).setLayoutParams(paramsList4.get(i));
            }
        }
    };

    public void setStepOne(ArrayList<RelativeLayout.LayoutParams> list){
        list.get(0).height += 5;
        list.get(1).height += 3;
        list.get(2).height += 1;
        list.get(3).height -= 2;
        list.get(4).height -= 2;
        list.get(5).height += 1;
        list.get(6).height += 3;
        list.get(7).height += 5;
    }

    public void setStepTwo(ArrayList<RelativeLayout.LayoutParams> list){
        list.get(0).height -= 5;
        list.get(1).height -= 3;
        list.get(2).height -= 1;
        list.get(3).height += 2;
        list.get(4).height += 2;
        list.get(5).height -= 1;
        list.get(6).height -= 3;
        list.get(7).height -= 5;
    }

    public SterilizerAnimationUtil(Context context, RelativeLayout rlTem, RelativeLayout rlHum, RelativeLayout rlGerm, RelativeLayout rlOzone) {
        this.context = context;
        this.rlTem = rlTem;
        this.rlHum = rlHum;
        this.rlGerm = rlGerm;
        this.rlOzone = rlOzone;
    }

    public void setAnimation(){
        View inflate = LayoutInflater.from(context).inflate(R.layout.page_animation_temperature, null, false);
        View inflate2 = LayoutInflater.from(context).inflate(R.layout.page_animation_humidity, null, false);
        View inflate3 = LayoutInflater.from(context).inflate(R.layout.page_animation_germ, null, false);
        View inflate4 = LayoutInflater.from(context).inflate(R.layout.page_animation_ozone, null, false);
        rlTem.addView(inflate);
        rlHum.addView(inflate2);
        rlGerm.addView(inflate3);
        rlOzone.addView(inflate4);

        for (int i = 0; i < ids.length; i++) {
            viewList.add(inflate.findViewById(ids[i]));
            viewList2.add(inflate2.findViewById(ids[i]));
            viewList3.add(inflate3.findViewById(ids[i]));
            viewList4.add(inflate4.findViewById(ids[i]));
        }
        Timer timer = new Timer();
        MyTask task = new MyTask();
        timer.schedule(task, 0, 100);
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            Message msg = SterilizerAnimationUtil.this.handler.obtainMessage();
            if (i % 21 == 0)
                flag = !flag;
            if (flag)
                msg.what = 1;
            else
                msg.what = 2;
            handler.sendMessage(msg);
            i++;
        }
    }

}
