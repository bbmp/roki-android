package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.R.id.text_desc;

/**
 * Created by Dell on 2018/10/19.
 */

public class SerilizerDialog<T> extends AbsDialog {

    @InjectView(R.id.cannel)
    TextView cannel;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.wv1)
    WheelView wv1;
    @InjectView(text_desc)
    TextView textDesc;

    List<T> temp;
    String str;
    String desc;
    String btnName;
    int defaultPos;

    public interface PickListener<T> {
        void onCancel();

        void onConfirm(T index1);
    }

    private PickListener listener;

    public void setListener(PickListener listener) {
        this.listener = listener;
    }

    public SerilizerDialog(Context context,List<T> temp,String str,String desc,int defaultPos) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.temp = temp;
        this.str = str;
        this.desc = desc;
        this.defaultPos = defaultPos;
        initData();
    }

    public SerilizerDialog(Context context,List<T> temp,String str,String desc,int defaultPos,String btnName) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.temp = temp;
        this.str = str;
        this.desc = desc;
        this.defaultPos = defaultPos;
        this.btnName = btnName;
        confirm.setText(btnName);
        initData();
    }

    int indexSelectTemp;

    private void initData() {
        LogUtils.i("20181105","desc::"+desc);

        textDesc.setText(desc);
        wv1.setDefaultPosition(defaultPos);
        indexSelectTemp = defaultPos;
        wv1.setAdapter(new TimeAdapter<T>(temp, str));
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtils.i("20180609","index:"+index);
                indexSelectTemp = index;
            }
        });
    }

    @Override
    protected int getViewResId() {
        return R.layout.serilizer_data_select_dialog;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick({R.id.cannel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cannel:
                if (this!=null&&this.isShowing()){
                    this.dismiss();
                    if (listener!=null){
                        listener.onCancel();
                    }

                }
                break;
            case R.id.confirm:
                if (listener!=null){
                    listener.onConfirm(temp.get(indexSelectTemp));
                }
                this.dismiss();
                break;
        }
    }

    public void show(SerilizerDialog absSetting) {
        Window win = absSetting.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        WindowManager.LayoutParams wl = win.getAttributes();
        // wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = displayMetrics.widthPixels;
        wl.height =(int) (displayMetrics.heightPixels * 0.45);
        win.setAttributes(wl);
        absSetting.show();
        absSetting.setCanceledOnTouchOutside(true);
    }


}
