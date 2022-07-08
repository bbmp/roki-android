package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;


/**
 * 无人锅kp100弹窗
 */
public class DialogType_PotKP100 extends BaseDialog {

    private Button btn_finish;
    private Button btn_cancel;
    private TextView tv_title;
    private TextView tv_content_text;
    private LinearLayout ll_heard_choice;
    private LinearLayout ll_buttom;
    private ImageView img_left_head;
    private ImageView img_right_head;
    private ImageView img_change_battery;
    protected View.OnClickListener leftHeadOnClickListener;
    protected View.OnClickListener rightHeadOnClickListener;

    public DialogType_PotKP100(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_pot_kp100, null);
        btn_finish = rootView.findViewById(R.id.btn_finish);
        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        tv_title = rootView.findViewById(R.id.tv_title);
        tv_content_text= rootView.findViewById(R.id.tv_content_text);
        ll_heard_choice = rootView.findViewById(R.id.ll_heard_choice);
        ll_buttom = rootView.findViewById(R.id.ll_buttom);
        img_left_head = rootView.findViewById(R.id.img_left_head);
        img_right_head = rootView.findViewById(R.id.img_right_head);
        img_change_battery = rootView.findViewById(R.id.img_change_battery);
        createDialog(rootView);
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }

    public void onLeftHeadClick(View v) {
        if (leftHeadOnClickListener != null) leftHeadOnClickListener.onClick(v);
    }

    public void onRightHeadClick(View v) {
        if (rightHeadOnClickListener != null) rightHeadOnClickListener.onClick(v);
    }

    public void setOnLeftHeadClickListener(View.OnClickListener leftListener) {
        leftHeadOnClickListener = leftListener;
    }

    public void setOnRightHeadClickListener(View.OnClickListener rightListener) {
        rightHeadOnClickListener = rightListener;
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

        img_left_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftHeadClick(v);
            }
        });
        img_right_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightHeadClick(v);
            }
        });
    }

    public static DialogType_PotKP100 createDialogType_PotKP100(Context context) {
        DialogType_PotKP100 rokiDialog = null;

        rokiDialog = new DialogType_PotKP100(context);

        return rokiDialog;
    }

    public void choiceDefaultHead(){
        tv_title.setText("请选择默认工作的炉头");
        tv_title.setVisibility(View.VISIBLE);
        tv_content_text.setVisibility(View.GONE);
        ll_heard_choice.setVisibility(View.VISIBLE);
        img_change_battery.setVisibility(View.GONE);
        ll_buttom.setVisibility(View.VISIBLE);
        btn_finish.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
    }
    public void choiceHeadStartCook(Stove stove){
        tv_title.setText("请将食材放入无人锅，选择炉头并开启录制烹饪。");
        tv_title.setVisibility(View.VISIBLE);
        tv_content_text.setVisibility(View.GONE);
        ll_heard_choice.setVisibility(View.VISIBLE);
        img_change_battery.setVisibility(View.GONE);
        ll_buttom.setVisibility(View.VISIBLE);
        btn_finish.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
        setStatus(stove);
    }
    public void putFoodTips(){
        tv_title.setVisibility(View.GONE);
        tv_content_text.setVisibility(View.VISIBLE);
        tv_content_text.setText("请将食材放入无人锅，开启左灶");
        ll_heard_choice.setVisibility(View.GONE);
        img_change_battery.setVisibility(View.GONE);
        ll_buttom.setVisibility(View.GONE);
    }
    public void continueCook(){
        tv_title.setText("是否继续烹饪菜谱？");
        tv_title.setVisibility(View.VISIBLE);
        tv_content_text.setVisibility(View.GONE);
        ll_heard_choice.setVisibility(View.GONE);
        img_change_battery.setVisibility(View.GONE);
        ll_buttom.setVisibility(View.VISIBLE);
        btn_finish.setVisibility(View.VISIBLE);
        btn_finish.setText("继续烹饪(60s)");
        btn_cancel.setVisibility(View.VISIBLE);
        btn_cancel.setText("结束烹饪");
    }
    public void changeBatteryTips(){
        tv_title.setVisibility(View.GONE);
        ll_heard_choice.setVisibility(View.GONE);
        tv_content_text.setVisibility(View.GONE);
        img_change_battery.setVisibility(View.VISIBLE);
        Glide.with(mContext).asGif().load(R.mipmap.pot_state_common).into(img_change_battery);
        ll_buttom.setVisibility(View.VISIBLE);
        btn_finish.setVisibility(View.VISIBLE);
        btn_finish.setText("好的");
        btn_cancel.setVisibility(View.GONE);
    }


    //设置炉头状态
    private Stove mStove;
    public void setStove(Stove stove) {
        this.mStove = stove;
    }
    public void setStatus(Stove stove) {
        setStove(stove);
        //status == 2 开火
        if (stove.leftHead.status == 2) {
            img_left_head.setImageResource(R.mipmap.ic_left_on);
        } else {
            img_left_head.setImageResource(R.mipmap.ic_left_off);
        }
        if (stove.rightHead.status == 2) {
            img_right_head.setImageResource(R.mipmap.ic_right_on);
        } else {
            img_right_head.setImageResource(R.mipmap.ic_right_off);
        }

    }
}
