package com.robam.roki.ui.dialog;

import static com.legent.ContextIniter.cx;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;


public class StoveQuickOffFireDialog extends BaseDialog {

    private LinearLayout ll_choice_left;
    private LinearLayout ll_choice_right;
    private ImageView img_left_header;
    private ImageView img_right_header;
    private TextView tv_left_head_state;
    private TextView tv_right_head_state;
    private TextView tv_title;
    private TextView tv_content_text;

    private View  v_dialog_top;
    public StoveQuickOffFireDialog(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_stove_quick_off, null);
        ll_choice_left = rootView.findViewById(R.id.ll_choice_left);
        ll_choice_right = rootView.findViewById(R.id.ll_choice_right);
        img_left_header = rootView.findViewById(R.id.img_left_header);
        img_right_header = rootView.findViewById(R.id.img_right_header);
        tv_left_head_state = rootView.findViewById(R.id.tv_left_head_state);
        tv_right_head_state = rootView.findViewById(R.id.tv_right_head_state);
        tv_title = rootView.findViewById(R.id.tv_title);
        tv_content_text = rootView.findViewById(R.id.tv_content_text);
        v_dialog_top =  rootView.findViewById(R.id.v_dialog_top);
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
                if (mStove != null && mStove.leftHead.status != 2) {
//                    ToastUtils.show("左灶未开启", Toast.LENGTH_LONG);
                    return;
                }
                buttonLeft();
//                onCancelClick(v);
            }
        });

        ll_choice_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStove != null && mStove.rightHead.status != 2) {
//                    ToastUtils.show("右灶未开启", Toast.LENGTH_LONG);
                    return;
                }
                buttonRight();
//                onOkClick(v);
            }
        });

        v_dialog_top.setOnClickListener(v -> {
            dismiss();
        });
    }

    private Stove mStove;

    public void setStove(Stove stove) {
        this.mStove = stove;
    }

    public void setStatus(Stove stove) {
        setStove(stove);
        //status == 2 开火
        if (stove.leftHead.status == 2) {
            img_left_header.setImageResource(R.mipmap.ic_left_on);
            tv_left_head_state.setText("工作中");
            tv_left_head_state.setBackgroundResource(R.drawable.shape_bg_fc9d59_999);
            tv_content_text.setText("点击炉头进行关火");
        } else {
            img_left_header.setImageResource(R.mipmap.ic_left_off);
            tv_left_head_state.setText("已关火");
            tv_left_head_state.setBackgroundResource(R.drawable.shape_bg_bdbdbd_999dp);
        }
        if (stove.rightHead.status == 2) {
            img_right_header.setImageResource(R.mipmap.ic_right_on);
            tv_right_head_state.setText("工作中");
            tv_right_head_state.setBackgroundResource(R.drawable.shape_bg_fc9d59_999);
            tv_content_text.setText("点击炉头进行关火");
        } else {
            img_right_header.setImageResource(R.mipmap.ic_right_off);
            tv_right_head_state.setText("已关火");
            tv_right_head_state.setBackgroundResource(R.drawable.shape_bg_bdbdbd_999dp);
        }
        if(stove.leftHead.status == 0&&stove.rightHead.status == 0){
            tv_content_text.setText("所有炉头均已关火");
        }
    }

    //快速关火左
    private void buttonLeft() {
        Stove.StoveHead head = null;
        head = mStove.leftHead;
        if (mStove.leftHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final Stove.StoveHead finalHead = head;
            Preconditions.checkNotNull(finalHead);
            setStoveStatus(finalHead);

        }

    }
    //快速关火右
    private void buttonRight() {
        Stove.StoveHead head = null;
        head = mStove.rightHead;
        if (mStove.rightHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final Stove.StoveHead finalHead = head;
            Preconditions.checkNotNull(finalHead);
            setStoveStatus(finalHead);
        }

    }


    private void setStoveStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        if (!checkIsPowerOn(head)) return;
        short status = (head.status == StoveStatus.Off) ? StoveStatus.StandyBy
                : StoveStatus.Off;
        mStove.setStoveStatus(false, head.ihId, status, new VoidCallback() {
            @Override
            public void onSuccess() {
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    boolean checkIsPowerOn(Stove.StoveHead head) {
        if (head.status != StoveStatus.Off)
            return true;
        else {
            if (!mStove.isLock) {
                ToastUtils.showLong(R.string.device_stove_isLock);
            }
            return false;
        }
    }

    boolean checkConnection() {
        if (!mStove.isConnected()) {
            ToastUtils.showLong(R.string.device_connected);
            return false;
        } else {
            return true;
        }
    }
}
