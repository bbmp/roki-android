package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;


/**
 * 工作完成
 */
public class DialogType_FinishWork extends BaseDialog {

    private Button btn_finish;
    private Button btn_cancel;


    public DialogType_FinishWork(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_finish_work, null);
        btn_finish = rootView.findViewById(R.id.btn_finish);
        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        createDialog(rootView);
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }


    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
        setOnCancelClickListener(cancelOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        setOnOkClickListener(okOnClickListener);
    }


    @Override
    public void bindAllListeners() {

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });

    }

    public static DialogType_FinishWork createDialogType_FinishWork(Context context) {
        DialogType_FinishWork rokiDialog = null;

        rokiDialog = new DialogType_FinishWork(context );

        return rokiDialog;
    }


}
