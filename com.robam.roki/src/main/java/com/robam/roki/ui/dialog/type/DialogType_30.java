package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;

import java.util.List;


/**
 * 工作完成后延长烹饪时间
 */
public class DialogType_30 extends BaseDialog {

    private LinearLayout ll_choice_left;
    private LinearLayout ll_choice_right;
    private ImageView img_left_header;
    private ImageView img_right_header;
    private ImageView img_close;
    private View v_dialog_top;
    public DialogType_30(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_30, null);
        ll_choice_left = rootView.findViewById(R.id.ll_choice_left);
        ll_choice_right = rootView.findViewById(R.id.ll_choice_right);
        img_left_header = rootView.findViewById(R.id.img_left_header);
        img_right_header = rootView.findViewById(R.id.img_right_header);
        img_close = rootView.findViewById(R.id.img_close);
        v_dialog_top = rootView.findViewById(R.id.v_dialog_top);
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

        ll_choice_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (stove != null && stove.leftHead.status != 2) {
//                    ToastUtils.show("左灶未开启", Toast.LENGTH_LONG);
//                    return;
//                }
//                onCancelClick(v);
            }
        });

        ll_choice_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stove != null &&stove.rightHead.status != 2) {
                    ToastUtils.show("右灶未开启", Toast.LENGTH_LONG);
                    return;
                }
                onOkClick(v);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        v_dialog_top.setOnClickListener(v -> {
            dismiss();
        });
    }

    private Stove stove;

    public void setStove(Stove stove) {
        this.stove = stove;
    }

    public void setStatus(Stove stove) {
        setStove(stove);
        //status == 2 开火
//        if (stove.leftHead.status == 2) {
//            img_left_header.setImageResource(R.mipmap.ic_left_on);
//        } else {
//            img_left_header.setImageResource(R.mipmap.ic_left_off);
//        }
        if (stove.rightHead.status == 2) {
            img_right_header.setImageResource(R.mipmap.ic_right_on);
        } else {
            img_right_header.setImageResource(R.mipmap.ic_right_off);
        }
    }

}
