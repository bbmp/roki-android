package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.VoidCallback;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceExpNumWheel;
import com.robam.roki.ui.view.DeviceWaterNumWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/23.
 */

public class WaterPurifierSetPeopleDialog extends AbsDialog {
    @InjectView(R.id.waterpurifier_confirm)
    TextView waterpurifier_confirm;
    @InjectView(R.id.water_set_familyPeople)
    DeviceExpNumWheel water_set_familyPeople;

    static WaterPurifierSetPeopleDialog dlgSetPeople;
    String memberCount;
    @InjectView(R.id.tv_cancel)
    TextView mTvCancel;
    private String guid;
    protected Boolean flag;

    public WaterPurifierSetPeopleDialog(Context context, String guid) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.guid = guid;
        LogUtils.i("gudfd", "guid44" + guid);
        init();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_waterpurifier_set_people;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    private void init() {
        water_set_familyPeople.setData(generateModelWheelData(20, 1));
        water_set_familyPeople.setDefault(2);
        water_set_familyPeople.setUnit("人");
        water_set_familyPeople.setOnSelectListener(new DeviceExpNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                LogUtils.i("20170321", "qq:" + water_set_familyPeople.getSelectedTag());
                memberCount = String.valueOf(water_set_familyPeople.getSelectedTag());
            }

            @Override
            public void selecting(int index, Object item) {
                //memberCount=String.valueOf( water_set_familyPeople.getSelectedTag());
            }
        });
    }

    private List<Integer> generateModelWheelData(int max, int start) {
        List<Integer> list = Lists.newArrayList();
        for (int i = start; i < max; i++) {
            list.add(i);
        }
        return list;
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        if (dlgSetPeople != null && dlgSetPeople.isShowing()){
            dlgSetPeople.dismiss();
        }
    }

    //向服务器上设置家庭成员数
    private class SetFamilyMember implements Runnable {

        @Override
        public void run() {
            StoreService.getInstance().setFamilyMember(memberCount, guid, new VoidCallback() {
                @Override
                public void onSuccess() {

                    Toast.makeText(cx, "设置成功", Toast.LENGTH_SHORT).show();
                    flag = true;
                    listener.onConfirm(flag, memberCount);
                    // dlgSetPeople.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                    //LogUtils.i("20170610","t:"+t.getMessage());
                }
            });

        }
    }

    static public WaterPurifierSetPeopleDialog show(Context cx, String guid) {
        dlgSetPeople = new WaterPurifierSetPeopleDialog(cx, guid);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        Window win = dlgSetPeople.getWindow();

        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int) (dm.heightPixels * 0.4);
        win.setAttributes(lp);
        dlgSetPeople.show();
        return dlgSetPeople;

    }

    //确定
    @OnClick(R.id.waterpurifier_confirm)
    public void onClickConfirm() {
        memberCount = String.valueOf(water_set_familyPeople.getSelectedTag());
        if (listener != null) {
            new Thread(new SetFamilyMember()).start();
        }


        if (dlgSetPeople != null && dlgSetPeople.isShowing())
            dlgSetPeople.dismiss();
    }


    public interface PickListener {

        void onConfirm(Boolean flag, String memberCount);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
