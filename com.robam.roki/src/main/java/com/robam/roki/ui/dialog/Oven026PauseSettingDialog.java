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
 * Created by rent on 2015/12/25.
 */
public class Oven026PauseSettingDialog extends AbsDialog {
    public static Oven026PauseSettingDialog dlg;
    Context cx;
    PickListener callback;
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

    public Oven026PauseSettingDialog(Context context, short autoModel, short model, PickListener callback) {
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
        if (autoModel != 0) {
            switch (autoModel) {
                case OvenMode.BEEF:
                    initMin(init_BEEFData(), 0);
//                    oven026_title.setText(new String("牛肉"));
                    break;
                case OvenMode.BREAD:
                    initMin(init_BREADData(), 0);
//                    oven026_title.setText(new String("面包"));
                    break;
                case OvenMode.BISCUITS:
                    initMin(init_BISCUITSData(), 1);
//                    oven026_title.setText(new String("饼干"));
                    break;
                case OvenMode.CHICKENWINGS:
                    initMin(init_CHICKENWINGSData(), 2);
//                    oven026_title.setText(new String("鸡翅"));
                    break;
                case OvenMode.CAKE:
                    initMin(init_CAKEData(), 0);
//                    oven026_title.setText(new String("蛋糕"));
                    break;
                case OvenMode.PIZZA:
                    initMin(init_PIZZAData(), 0);
//                    oven026_title.setText(new String("披萨"));
                    break;
                case OvenMode.ROASTFISH:
                    initMin(init_ROASTFISHData(), 0);
//                    oven026_title.setText(new String("虾"));
                    break;
                case OvenMode.GRILLEDSHRIMP:
                    initMin(init_GRILLEDSHRIMPData(), 2);
//                    oven026_title.setText(new String("鱼"));
                    break;
                case OvenMode.SWEETPOTATO:
                    init_SWEETPOTATOData();
                    initMin(init_SWEETPOTATOData(), 0);
//                    oven026_title.setText(new String("红薯"));
                    break;
                case OvenMode.CORN:
                    initMin(init_CORNData(), 0);
//                    oven026_title.setText(new String("玉米"));
                    break;
                case OvenMode.STREAKYPORK:
                    initMin(init_STREAKYPORKData(), 5);
//                    oven026_title.setText(new String("五花肉"));
                    break;
                case OvenMode.VEGETABLES:
                    initMin(init_VEGETABLESData(), 0);
//                    oven026_title.setText(new String("蔬菜"));
                    break;
                default:
                    break;
            }
        } else {
            switch (model) {
                case OvenMode.KUAIRE:
                    initTempUp(getList2(OvenMode.KUAIRE), 150);
                    initMin(init5to90_min(), 45);
//                    oven026_title.setText(new String("快热"));
                    break;
                case OvenMode.FENGBEIKAO:
                    initTempUp(getList2(OvenMode.FENGBEIKAO), 140);
                    initMin(init5to90_min(), 55);
//                    oven026_title.setText(new String("风焙烤"));
                    break;
                case OvenMode.BEIKAO:
                    initTempUp(getList2(OvenMode.BEIKAO), 100);
                    initMin(init5to90_min(), 55);
//                    oven026_title.setText(new String("焙烤"));
                    break;
                case OvenMode.DIJIARE:
                    initTempUp(getList2(OvenMode.DIJIARE), 110);
                    initMin(init5to90_min(), 45);
//                    oven026_title.setText(new String("底加热"));
                    break;
                case OvenMode.FENGSHANKAO:
                    initTempUp(getList2(OvenMode.FENGSHANKAO), 160);
                    initMin(init5to90_min(), 55);
//                    oven026_title.setText(new String("风扇烤"));
                    break;
                case OvenMode.KAOSHAO:
                    initTempUp(getList2(OvenMode.KAOSHAO), 130);
                    initMin(init5to90_min(), 45);
//                    oven026_title.setText(new String("烤烧"));
                    break;
                case OvenMode.QIANGKAOSHAO:
                    initTempUp(getList2(OvenMode.QIANGKAOSHAO), 140);
                    initMin(init5to90_min(), 35);
//                    oven026_title.setText(new String("强烤烧"));
                    break;
                case OvenMode.EXP:
                    llExpModelFg.setVisibility(View.VISIBLE);
                    ArrayList<Integer> list = Lists.newArrayList();
                    for (int i = 50; i <= 250; i++) {
                        list.add(i);
                    }
                    initTempUp(list, 110);

                    ArrayList<Integer> list2 = Lists.newArrayList();
                    for (int i = 130; i <= 190; i++) {
                        list2.add(i);
                    }
                    initTempDown(list2, 0);

                    ArrayList<Integer> list1 = Lists.newArrayList();
                    for (int i = 5; i <= 90; i++) {
                        list1.add(i);
                    }
                    initMin(list1, 15);
//                    oven026_title.setText(new String("EXP"));
                    break;
                default:
                    break;
            }
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
        if (wv1SelectValue <= 80) {
            for (int i = 50; i <= wv1SelectValue + 30; i++) {
                list.add(i);
            }
            return list;
        }
        if (wv1SelectValue > 80 && wv1SelectValue <= 220) {

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
        }

    }

    List<Integer> init_BEEFData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 7; i <= 17; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_BREADData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 15; i <= 30; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_BISCUITSData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 16; i <= 25; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_CHICKENWINGSData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 14; i <= 20; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_CAKEData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 45; i <= 55; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_PIZZAData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 15; i <= 25; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_ROASTFISHData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 12; i <= 22; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_GRILLEDSHRIMPData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 21; i <= 31; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_SWEETPOTATOData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 45; i <= 60; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_CORNData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 35; i <= 50; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_STREAKYPORKData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 25; i <= 40; i++) {
            list.add(i);
        }
        return list;
    }

    List<Integer> init_VEGETABLESData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 20; i <= 30; i++) {
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

