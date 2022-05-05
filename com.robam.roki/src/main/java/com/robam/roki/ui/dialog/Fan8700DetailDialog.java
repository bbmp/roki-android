package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class Fan8700DetailDialog extends AbsDialog {
    public static Fan8700DetailDialog dlg;
    Context cx;
//    @InjectView(R.id.ll_sales_service)
//     LinearLayout llSalesService;
//    @InjectView(R.id.ll_feedback_suggestions)
//     LinearLayout llFeedbackSuggestions;
//    @InjectView(R.id.tv_sales_service_line)
//    TextView tvSalesServiceLine;
//    @InjectView(R.id.tv_feedback_suggestions_line)
//    TextView tvFeedbackSuggestionsLine;
    @InjectView(R.id.fan8700_detail_btn_cancel)
    Button fan8700DetailBtnCancel;


    public Fan8700DetailDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        init();
    }

    private void init() {
//        llSalesService.setVisibility(View.INVISIBLE);
//        llFeedbackSuggestions.setVisibility(View.INVISIBLE);
//        tvSalesServiceLine.setVisibility(View.INVISIBLE);
//        tvFeedbackSuggestionsLine.setVisibility(View.INVISIBLE);
    }


    @Override
    protected int getViewResId() {
        return R.layout.dialog_fan8700_detail;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.fan8700_detail_linear_setting)
    public void OnClickSetting() {
        if (listener != null) {
            listener.onConfirm(PickListener.SETTING);
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.fan8700_detail_linear_product)
    public void OnClickProduct() {
        if (listener != null) {
            listener.onConfirm(PickListener.PRODUCT);
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();

    }

    @OnClick(R.id.fan8700_detail_btn_cancel)//取消
    public void onClickConfirm() {
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Fan8700DetailDialog show(Context context) {
        dlg = new Fan8700DetailDialog(context);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {
        short SETTING = 0;
        short PRODUCT = 1;

        void onCancel();

        void onConfirm(short index);//0 智能设定 1产品信息
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        if (Plat.DEBUG)
            Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
