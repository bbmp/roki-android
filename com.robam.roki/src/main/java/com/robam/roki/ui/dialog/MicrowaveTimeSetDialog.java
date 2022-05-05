package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;

/**
 * Created by Rent on 2016/6/11.
 */
public class MicrowaveTimeSetDialog extends AbsDialog{
 /*   @InjectView(R.id.mic_quickheating_time)
    DeviceOvenTimeWheel mic_quickheating_time;*/
    Context context;
    public MicrowaveTimeSetDialog(Context cx){
        super(cx,R.style.Dialog_Microwave_professtion);
        this.context=cx;
    }
    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave_time;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        /*List<Integer> list = Lists.newArrayList();
        list.add(1);list.add(2);list.add(3);list.add(4);list.add(5);
        mic_quickheating_time.setData(list);mic_quickheating_time.setDefault(2);*/
    }

    public static MicrowaveTimeSetDialog show(Context cx){
        MicrowaveTimeSetDialog dlg=new MicrowaveTimeSetDialog(cx);
        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);
        dlg.show();
        return dlg;
    }
}
