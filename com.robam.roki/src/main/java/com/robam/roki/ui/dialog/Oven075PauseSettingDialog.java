package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceExpNumWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/26.
 */

public class Oven075PauseSettingDialog extends AbsDialog {
    public static Oven075PauseSettingDialog dlg;
    Context cx;
    Oven075PauseSettingDialog.PickListener callback;
    short autoModel;
    short model;
    //    @InjectView(R.id.dialog_oven026_pausesetting_title)
//    TextView oven026_title;
    @InjectView(R.id.oven026_pausesetting_uptemp)
    DeviceExpNumWheel oven026_pausesetting_uptemp;
    @InjectView(R.id.oven026_pausesetting_downtemp)
    DeviceExpNumWheel oven026_pausesetting_downtemp;
    @InjectView(R.id.oven026_pausesetting_min)
    DeviceExpNumWheel oven026_pausesetting_min;
    @InjectView(R.id.ll_exp_model_fg)
    LinearLayout llExpModelFg;

    public Oven075PauseSettingDialog(Context context, short autoModel, short model, PickListener callback) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.cx = context;
        this.callback = callback;
        this.autoModel = autoModel;
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
        LogUtils.i("20161027", "automodel:" + autoModel + " model:" + model);
        switch (model) {
            case OvenMode.KUAIRE:
                initTempUp(getList2(OvenMode.KUAIRE), 150);
                initMin(init1to120_min(), 49);
//                    oven026_title.setText(new String("快热"));
                break;
            case OvenMode.FENGBEIKAO:
                initTempUp(getList2(OvenMode.FENGBEIKAO), 140);
                initMin(init1to120_min(), 59);
//                    oven026_title.setText(new String("风焙烤"));
                break;
            case OvenMode.BEIKAO:
                initTempUp(getList2(OvenMode.BEIKAO), 100);
                initMin(init1to120_min(), 59);
//                    oven026_title.setText(new String("焙烤"));
                break;
            case OvenMode.DIJIARE:
                initTempUp(getList2(OvenMode.DIJIARE), 110);
                initMin(init1to120_min(), 49);
//                    oven026_title.setText(new String("底加热"));
                break;
            case OvenMode.FENGSHANKAO:
                initTempUp(getList2(OvenMode.FENGSHANKAO), 160);
                initMin(init1to120_min(), 59);
//                    oven026_title.setText(new String("风扇烤"));
                break;
            case OvenMode.KAOSHAO:
                initTempUp(getList2(OvenMode.KAOSHAO), 130);
                initMin(init1to120_min(), 49);
//                    oven026_title.setText(new String("烤烧"));
                break;
            case OvenMode.QIANGKAOSHAO:
                initTempUp(getList2(OvenMode.QIANGKAOSHAO), 140);
                initMin(init1to120_min(), 39);
//                    oven026_title.setText(new String("强烤烧"));
                break;
            case OvenMode.EXP:
                llExpModelFg.setVisibility(View.VISIBLE);
                ArrayList<Integer> list = Lists.newArrayList();
                for (int i = 100; i <= 250; i++) {
                    list.add(i);
                }
                initTempUp(list, 60);

                ArrayList<Integer> list2 = Lists.newArrayList();
                for (int i = 130; i <= 190; i++) {
                    list2.add(i);
                }
                initTempDown(list2, 0);

                ArrayList<Integer> list1 = Lists.newArrayList();
                for (int i = 1; i <= 90; i++) {
                    list1.add(i);
                }
                initMin(list1, 19);
//                    oven026_title.setText(new String("EXP"));
                break;

            default:
                break;
        }
    }

    public void initTempUp(List<Integer> list, int defaultPos) {
        if (list == null) return;
        oven026_pausesetting_uptemp.setVisibility(View.VISIBLE);
        oven026_pausesetting_uptemp.setData(list);
        oven026_pausesetting_uptemp.setDefault(defaultPos);
        oven026_pausesetting_uptemp.setUnit("℃");
        oven026_pausesetting_uptemp.setOnSelectListener(new DeviceExpNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                List<?> list = getDownTempRange(item);
                oven026_pausesetting_downtemp.setData(list);
                oven026_pausesetting_downtemp.setDefault(0);
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
        if (wv1SelectValue <= 130) {
            for (int i = 100; i <= wv1SelectValue + 30; i++) {
                list.add(i);
            }
            return list;
        }
        if (wv1SelectValue > 130 && wv1SelectValue <= 220) {

            for (int i = wv1SelectValue - 30; i <= wv1SelectValue + 30; i++) {
                list.add(i);
            }
            return list;
        }

        if (wv1SelectValue >= 220) {
            for (int i = wv1SelectValue - 30; i <= 250; i++) {
                list.add(i);
            }
            return list;
        }
        return list;
    }

    public void initTempDown(List<Integer> list, int defaultPos) {
        if (list == null) return;
        oven026_pausesetting_downtemp.setVisibility(View.VISIBLE);
        oven026_pausesetting_downtemp.setData(list);
        oven026_pausesetting_downtemp.setDefault(defaultPos);
        oven026_pausesetting_downtemp.setUnit("℃");
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
//        ToastUtils.show(oven026_pausesetting_uptemp.getSelectedTag() + "/"
//                + oven026_pausesetting_downtemp.getSelectedTag() + "/" + oven026_pausesetting_min.getSelectedTag(), Toast.LENGTH_SHORT);
        if (callback != null) {
            callback.onConfirm((Integer) oven026_pausesetting_uptemp.getSelectedTag(), (Integer) oven026_pausesetting_downtemp.getSelectedTag(),
                    (Integer) oven026_pausesetting_min.getSelectedTag());
            this.dismiss();
        }

    }




    List<Integer> initDefault90(){
        ArrayList<Integer> list = Lists.newArrayList();
        list.add(90);
        return list;
    }

    List<Integer> init1to120_min() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 1; i <= 90; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init5to90_min() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 5; i <= 90; i++) {
            list.add(i);
        }
        return list;
    }

    /*设置各种模式温度范围*/
    protected List<Integer> getList2(int index) {
        List<Integer> list = Lists.newArrayList();
        if (OvenMode.DIJIARE == index) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (OvenMode.KUAIRE == index || OvenMode.KAOSHAO == index) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if (OvenMode.FENGBEIKAO == index || OvenMode.BEIKAO == index || OvenMode.FENGSHANKAO == index) {
            for (int i = 60; i <= 250; i++) {
                list.add(i);
            }
        } else if (OvenMode.QIANGKAOSHAO == index) {
            for (int i = 40; i <= 250; i++) {
                list.add(i);
            }
        }
        return list;
    }

//    static public Oven026PauseSettingDialog show(Context context, short autoModel, short model, Callback callback) {
//        dlg = new Oven026PauseSettingDialog(context, autoModel, model, callback);
//        Window win = dlg.getWindow();
//        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
//        WindowManager.LayoutParams wl = win.getAttributes();
//        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        win.setAttributes(wl);
//        dlg.show();
//        return dlg;
//    }

    public interface PickListener {
        void onCancel();

        void onConfirm(Integer uptemp, Integer downtemp, Integer min);
    }

    private PickListener listener;

}
