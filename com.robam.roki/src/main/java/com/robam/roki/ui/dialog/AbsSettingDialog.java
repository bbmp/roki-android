package com.robam.roki.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/9.
 */

public class AbsSettingDialog<T> extends AbsDialog {

    @InjectView(R.id.cannel)
    TextView cannel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(R.id.text_desc)
    TextView txtDesc;
    @InjectView(R.id.desc_layout)
    LinearLayout descLayout;

    Context cx;
    List<T> listTemp;
    List<String> buttonList;
    int defaultNum=0;
    String str;
    public AbsSettingDialog(Context context, List<T> listTemp, List<String> liststr, int defaultNum) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.listTemp = listTemp;
        this.defaultNum = defaultNum;
        this.buttonList = liststr;
        this.str = liststr.get(0);
        initSetting();
    }

    @OnClick({R.id.cannel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cannel:
                if (this!=null&&this.isShowing()){
                    this.dismiss();
                    listener.onCancel();
                }
                break;
            case R.id.confirm:
                if (listener!=null){
                    if (listTemp.get(0) instanceof DeviceOvenDiyParams) {
                        listener.onConfirm(indexSelect);
                    } else {
                        listener.onConfirm(listTemp.get(indexSelect));
                    }
                }
                this.dismiss();
                break;
        }
    }

    public interface PickListener<T> {
        void onCancel();

        void onConfirm(T index);
    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getViewResId() {
        return R.layout.time_setting_dialog;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    int indexSelect;
    protected void initSetting() {
        cannel.setText(buttonList.get(1));
        confirm.setText(buttonList.get(2));
        txtDesc.setText(buttonList.get(3));
        if (TextUtils.isEmpty(buttonList.get(3))) {
            descLayout.setVisibility(View.GONE);
        } else {
            descLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < listTemp.size(); i++) {
            if (listTemp.get(i).equals(defaultNum)) {
                indexSelect=i;
            }
        }
        wv1.setDefaultPosition(indexSelect);

        wv1.setAdapter(new TimeAdapter<T>(listTemp, str));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20191206","index:"+index);
                indexSelect = index;
            }
        });
    }

    public void show(AbsSettingDialog absSettingDialog) {
        Window win = absSettingDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
       // wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = displayMetrics.widthPixels;
        wl.height =(int) (displayMetrics.heightPixels * 0.4);
        win.setAttributes(wl);
        absSettingDialog.show();
        absSettingDialog.setCanceledOnTouchOutside(true);
    }

}
