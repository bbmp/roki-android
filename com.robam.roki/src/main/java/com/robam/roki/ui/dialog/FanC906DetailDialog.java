package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class FanC906DetailDialog extends AbsDialog {
    public static FanC906DetailDialog dlg;
    Context cx;
    @InjectView(R.id.fan8700_detail_btn_cancel)
    Button fan8700DetailBtnCancel;
    @InjectView(R.id.ll_opinion)
    LinearLayout mLlOpinion;
    @InjectView(R.id.fan8700_detail_linear_product)
    LinearLayout mFan8700DetailLinearProduct;
    @InjectView(R.id.ll_sales_service)
    LinearLayout mLlSalesService;


    public FanC906DetailDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        init();
    }

    private void init() {

    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_c906_detail;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }


    @OnClick(R.id.fan8700_detail_linear_product)
    public void onMFan8700DetailLinearProductClicked() {

        if (listener != null) {
            listener.onConfirm(PickListener.PRODUCT);//产品信息
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.ll_sales_service)
    public void onMLlSalesServiceClicked() {
        if (listener != null) {
            listener.onConfirm(PickListener.SALE);//售后服务
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.ll_opinion)
    public void onViewClicked() {

        if (listener != null) {
            listener.onConfirm(PickListener.OPINION);//反馈意见
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.fan8700_detail_btn_cancel)//取消
    public void onClickConfirm() {
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public FanC906DetailDialog show(Context context) {
        dlg = new FanC906DetailDialog(context);
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
        short PRODUCT = 0;
        short SALE = 1;
        short OPINION = 2;

        void onCancel();

        void onConfirm(short index);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        if (Plat.DEBUG)
            Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
