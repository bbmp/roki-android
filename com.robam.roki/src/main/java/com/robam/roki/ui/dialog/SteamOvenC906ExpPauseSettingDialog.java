package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceExpNumWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2015/12/25.
 */
public class SteamOvenC906ExpPauseSettingDialog extends AbsDialog {
    public static SteamOvenC906ExpPauseSettingDialog dlg;
    Context cx;
    PickListener callback;
    short model;
    @InjectView(R.id.oven026_pausesetting_uptemp)
    DeviceExpNumWheel c906_pausesetting_uptemp;
    @InjectView(R.id.oven026_pausesetting_downtemp)
    DeviceExpNumWheel c906_pausesetting_downtemp;
    @InjectView(R.id.oven026_pausesetting_min)
    DeviceExpNumWheel oven026_pausesetting_min;
    @InjectView(R.id.ll_exp_model_fg)
    LinearLayout llExpModelFg;

    public SteamOvenC906ExpPauseSettingDialog(Context context, short model, PickListener callback) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.cx = context;
        this.callback = callback;
        this.model = model;
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven026_pausesetting;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    private void init() {
        switch (model) {
            case SteamOvenOneModel.EXP:
                llExpModelFg.setVisibility(View.VISIBLE);
                ArrayList<Integer> list = Lists.newArrayList();
                for (int i = 100; i <= 200; i++) {
                    list.add(i);
                }
                initTempUp(list, 60);

                ArrayList<Integer> list2 = Lists.newArrayList();
                for (int i = 140; i <= 180; i++) {
                    list2.add(i);
                }
                initTempDown(list2, 0);

                ArrayList<Integer> list1 = Lists.newArrayList();
                for (int i = 1; i <= 120; i++) {
                    list1.add(i);
                }
                initMin(list1, 19);
                break;
            default:
                break;
        }
    }

    public void initTempUp(List<Integer> list, int defaultPos) {
        if (list == null) return;
        c906_pausesetting_uptemp.setVisibility(View.VISIBLE);
        c906_pausesetting_uptemp.setData(list);
        c906_pausesetting_uptemp.setDefault(defaultPos);
        c906_pausesetting_uptemp.setUnit("℃");
        c906_pausesetting_uptemp.setOnSelectListener(new DeviceExpNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                List<?> list = getDownTempRange(item);
                c906_pausesetting_downtemp.setData(list);
                c906_pausesetting_downtemp.setDefault(0);
            }

            @Override
            public void selecting(int index, Object item) {

            }
        });
    }

    //根据选中的上管温度设置下管温度范围
    protected List<?> getDownTempRange(Object item) {
        if (item == null) {
            return null;
        }
        List<Integer> list = Lists.newArrayList();
        Integer wv1SelectValue = (Integer) item;
        if (wv1SelectValue <= 120) {
            for (int i = 100; i <= wv1SelectValue + 20; i++) {
                list.add(i);
            }
            return list;
        }
        if (wv1SelectValue > 120 && wv1SelectValue <= 180) {

            for (int i = wv1SelectValue - 20; i <= wv1SelectValue + 20; i++) {
                list.add(i);
            }
            return list;
        }

        if (wv1SelectValue > 180 && wv1SelectValue <= 200) {
            for (int i = wv1SelectValue - 20; i <= wv1SelectValue + 20; i++) {
                if (i <= 200) {
                    list.add(i);
                }
            }
            return list;
        }

        if (wv1SelectValue >= 200) {
            for (int i = wv1SelectValue - 20; i <= 200; i++) {
                list.add(i);
            }
            return list;
        }
        return list;
    }

    public void initTempDown(List<Integer> list, int defaultPos) {
        if (list == null) return;
        c906_pausesetting_downtemp.setVisibility(View.VISIBLE);
        c906_pausesetting_downtemp.setData(list);
        c906_pausesetting_downtemp.setDefault(defaultPos);
        c906_pausesetting_downtemp.setUnit("℃");
    }

    public void initMin(List<Integer> list, int defaultPos) {
        if (list == null) return;
        oven026_pausesetting_min.setVisibility(View.VISIBLE);
        oven026_pausesetting_min.setData(list);
        oven026_pausesetting_min.setDefault(defaultPos);
        oven026_pausesetting_min.setUnit("分");
    }

    @OnClick(R.id.oven026_pausesetting_close)
    public void OnCloseClick() {
        if (this != null && this.isShowing()) {
            this.dismiss();
        }
    }

    @OnClick(R.id.oven026_pausesetting_confirm)
    public void OnStartClick() {
        if (callback != null) {
            callback.onConfirm((Integer) c906_pausesetting_uptemp.getSelectedTag(), (Integer) c906_pausesetting_downtemp.getSelectedTag(),
                    (Integer) oven026_pausesetting_min.getSelectedTag());

        }
    }


    /*设置各种模式温度范围*/
    protected List<Integer> getList2(int index) {
        List<Integer> list = Lists.newArrayList();
        if (SteamOvenOneModel.EXP == index) {
            for (int i = 100; i <= 200; i++) {
                list.add(i);
            }
        }
        return list;
    }


    public interface PickListener {
        void onCancel();

        void onConfirm(Integer uptemp, Integer downtemp, Integer min);
    }

    private PickListener listener;

}

