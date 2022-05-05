package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.Steamoven.SteamMode026;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceExpNumWheel;
import com.robam.roki.ui.view.DeviceNumWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.pojos.device.Steamoven.SteamMode026.VERGETABLE;

/**
 * Created by rent on 2015/12/25.
 */
public class Steam226PauseSettingDialog extends AbsDialog {
    public static Steam226PauseSettingDialog dlg;
    Context cx;
    Steam226PauseSettingDialog.PickListener callback;
    short model;
//    @InjectView(R.id.dialog_oven026_pausesetting_title)
//    TextView oven026_title;
    @InjectView(R.id.oven026_pausesetting_uptemp)
    DeviceExpNumWheel oven026_pausesetting_uptemp;
    @InjectView(R.id.oven026_pausesetting_downtemp)
    DeviceExpNumWheel oven026_pausesetting_downtemp;
    @InjectView(R.id.oven026_pausesetting_min)
    DeviceExpNumWheel oven026_pausesetting_min;

    public Steam226PauseSettingDialog(Context context, short model, Steam226PauseSettingDialog.PickListener callback) {
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
            case SteamMode026.FISH://鱼
                initTempUp(getList2(SteamMode026.FISH), 15);
                initMin(getList3(SteamMode026.FISH), 20);
//                oven026_title.setText(new String("鱼类"));
                break;
            case SteamMode026.EGG://蛋
                initTempUp(getList2(SteamMode026.EGG), 10);
                initMin(getList3(SteamMode026.EGG), 15);
//                oven026_title.setText(new String("蛋类"));
                break;
            case SteamMode026.TENDON://蹄筋
                initTempUp(getList2(SteamMode026.TENDON), 10);
                initMin(getList3(SteamMode026.TENDON), 40);
//                oven026_title.setText(new String("蹄筋"));
                break;
            case VERGETABLE://蔬菜
                initTempUp(getList2(VERGETABLE), 10);
                initMin(getList3(VERGETABLE), 25);
//                oven026_title.setText(new String("蔬菜"));
                break;
            case SteamMode026.MEAT://五花肉
                initTempUp(getList2(SteamMode026.MEAT), 10);
                initMin(getList3(SteamMode026.MEAT), 40);
//                oven026_title.setText(new String("肉"));
                break;
            case SteamMode026.STEAMEDBREAD://馒头
                initTempUp(getList2(SteamMode026.STEAMEDBREAD), 60);
                initMin(getList3(SteamMode026.STEAMEDBREAD), 25);
//                oven026_title.setText(new String("面食"));
                break;
            case SteamMode026.RICE://米饭
                initTempUp(getList2(SteamMode026.RICE), 15);
                initMin(getList3(SteamMode026.RICE), 20);
//                oven026_title.setText(new String("米饭"));
                break;
            case SteamMode026.STRONGSTEAM://强蒸
                initTempUp(getList2(SteamMode026.STRONGSTEAM), 0);
                initMin(getList3(SteamMode026.STRONGSTEAM), 40);
//                oven026_title.setText(new String("强力蒸"));
                break;
            case SteamMode026.STERILIZATION://杀菌
                initTempUp(getList2(SteamMode026.STERILIZATION), 0);
                initMin(getList3(SteamMode026.STERILIZATION), 25);
//                oven026_title.setText(new String("杀菌"));
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
            callback.onConfirm((Integer) oven026_pausesetting_uptemp.getSelectedTag(), (Integer) oven026_pausesetting_downtemp.getSelectedTag(),
                    (Integer) oven026_pausesetting_min.getSelectedTag());
        }

    }


    /*设置各种模式温度范围*/
    protected List<Integer> getList2(int index) {
        List<Integer> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == SteamMode026.VERGETABLE || index == SteamMode026.RICE) {
            for (int i = 85; i <= 100; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.TENDON || index == SteamMode026.MEAT) {
            for (int i = 90; i <= 100; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.STEAMEDBREAD) {
            for (int i = 35; i <= 100; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.STRONGSTEAM) {
            list.add(105);
        } else if (index == SteamMode026.STERILIZATION) {
            list.add(100);
        }
        return list;
    }

    /*设置各种模式时间范围*/
    protected List<Integer> getList3(int index) {
        List<Integer> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == VERGETABLE || index == SteamMode026.STEAMEDBREAD) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.TENDON || index == SteamMode026.STRONGSTEAM) {
            for (int i = 20; i <= 90; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.MEAT) {
            for (int i = 5; i <= 90; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.RICE) {
            for (int i = 15; i <= 90; i++) {
                list.add(i);
            }
        } else if (index == SteamMode026.STERILIZATION) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        }
        return list;
    }

    public interface PickListener {
        void onCancel();

        void onConfirm(Integer uptemp, Integer downtemp, Integer min);
    }

    private Steam226PauseSettingDialog.PickListener listener;

}

