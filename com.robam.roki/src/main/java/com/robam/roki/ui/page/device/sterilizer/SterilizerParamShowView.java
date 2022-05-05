package com.robam.roki.ui.page.device.sterilizer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/10/16.
 */

public class SterilizerParamShowView extends FrameLayout {

    @InjectView(R.id.txt_wd)
    TextView txtWd;
    @InjectView(R.id.txt_sd)
    TextView txtSd;
    @InjectView(R.id.txt_o)
    TextView txtO;
    @InjectView(R.id.txt_xj)
    TextView txtXj;
    @InjectView(R.id.param)
    LinearLayout param;

    @InjectView(R.id.tv_title_wd)
    TextView tvTitleWd;
    @InjectView(R.id.tv_title_sd)
    TextView tvTitleSd;
    @InjectView(R.id.tv_title_o)
    TextView tvTitleO;
    @InjectView(R.id.tv_title_xj)
    TextView tvTitleXj;

    public SterilizerParamShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SterilizerParamShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SterilizerParamShowView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context cx) {
        View view = LayoutInflater.from(cx).inflate(R.layout.sterilizere_param_show, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void upDataView(short wd,short sd,short oo,short xj){
        txtWd.setText(String.valueOf(wd));
        txtSd.setText(String.valueOf(sd));
        txtO.setText(String.valueOf(oo));
        txtXj.setText(String.valueOf(xj));


    }

    public void upDataView(String wd,String sd,String oo,String xj){
        txtWd.setText(wd);
        txtSd.setText(sd);
        txtO.setText(oo);
        txtXj.setText(xj);


    }

    public void upDataViewName(String wdName,String sdName,String ooName,String xjName){
        if (!"".equals(wdName)&&wdName!=null) {
            tvTitleWd.setText(wdName);
        }
        if (!"".equals(sdName)&&sdName!=null) {
            tvTitleSd.setText(sdName);
        }
        if (!"".equals(ooName)&&ooName!=null) {
            tvTitleO.setText(ooName);
        }
        if (!"".equals(xjName)&&xjName!=null) {
            tvTitleXj.setText(xjName);
        }




    }


}
