package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/11.
 */

public class Microwave526CleanDialog extends AbsDialog {
        @InjectView(R.id.title)
        TextView title;
        public static Microwave526CleanDialog dlg;
        Context cx;
        private View customView;
        MicroWaveM526 microWave;
        public Microwave526CleanDialog(Context context, AbsMicroWave microWave) {
            super(context,R.style.Dialog_Micro_FullScreen);
            this.cx = context;
            this.microWave= (MicroWaveM526) microWave;
            init();
        }

        @Override
        protected int getViewResId() {
            return R.layout.dialog_microwave526_clean;
        }

        @Override
        protected void initView(View view) {
            this.customView=view;
        }
        private void init() {
            ButterKnife.inject(this, customView);
        }

        //返回
        @OnClick(R.id.imgreturn)
        public void onClickBack(){
            if (dlg!=null&&dlg.isShowing()){
                dlg.dismiss();
            }
        }

        //已放入柠檬的点击事件
        @OnClick(R.id.start_cook)
        public void onClickCook(){
            if (listener != null) {
                listener.onConfirm((short) 3);
            }

            if(dlg!=null&&dlg.isShowing())
                dlg.dismiss();
        }

        static public Microwave526CleanDialog show(Context context,AbsMicroWave microWave) {
            dlg = new Microwave526CleanDialog(context,microWave);
            Window win = dlg.getWindow();
            // win.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
            WindowManager.LayoutParams wl = win.getAttributes();
            wl.width = WindowManager.LayoutParams.MATCH_PARENT;
            wl.height = WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(wl);
            dlg.show();
            return dlg;
        }

        public interface PickListener {

            void onCancel();

            void onConfirm(short fire);
        }

        private Microwave526CleanDialog.PickListener listener;

        public void setPickListener(Microwave526CleanDialog.PickListener listener) {
            this.listener = listener;
        }
}
