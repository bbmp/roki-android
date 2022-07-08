package com.robam.roki.ui.activity3.device.base.other;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.base.BaseDialog;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;

import skin.support.content.res.SkinCompatResources;

public class PotDialogUtils extends DialogUtils {

    private final BaseDialog dialog;

    public PotDialogUtils(Activity activity) {
        super(activity);
        this.activity = activity;
        dialog = new BaseDialog(activity);
    }

    public  void showBatteryReplace(View.OnClickListener onClickListener){
        dialog.setContentView(R.layout.dialog_pot_change_battery);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        View btn_complete = dialog.findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 灶头选择
     * @param stove
     */
    private TextView tv_left_head_state;
    private TextView tv_right_head_state;

    private Button btn_complete;

    private String choiceHead;
    public String getChoiceHead() {
        return choiceHead;
    }

    public void setChoiceHead(String choiceHead) {
        this.choiceHead = choiceHead;
    }

    public void stoveHeadSelect(View.OnClickListener onClickListener ){
        dialog.setContentView(R.layout.dialog_pot_choice_head);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        btn_complete = dialog.findViewById(R.id.btn_complete);
        tv_left_head_state = dialog.findViewById(R.id.tv_left_head_state);
        tv_right_head_state = dialog.findViewById(R.id.tv_right_head_state);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(getChoiceHead());
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == tv_left_head_state) {
                    setChoiceHead("left");
                    tv_left_head_state.setBackground(getResId(R.mipmap.icon_choice_selected));
                    tv_left_head_state.setTextColor(getColorSkin(R.color.white));
                    tv_right_head_state.setBackground(getResId(R.mipmap.icon_choice_common));
                    tv_right_head_state.setTextColor(getColorSkin(R.color.text_select_color));
                }else if (view == tv_right_head_state) {
                    setChoiceHead("right");
                    tv_left_head_state.setBackground(getResId(R.mipmap.icon_choice_common));
                    tv_left_head_state.setTextColor(getColorSkin(R.color.text_select_color));
                    tv_right_head_state.setBackground(getResId(R.mipmap.icon_choice_selected));
                    tv_right_head_state.setTextColor(getColorSkin(R.color.white));
                }
            }
        }, tv_left_head_state , tv_right_head_state );
    }

    private Stove mStove;

    public void setStove(Stove stove) {
        this.mStove = stove;
    }

    public void setStatus(Stove stove) {
        setStove(stove);
        //status == 2 开火
        if (stove.leftHead.status == 2) {
            tv_left_head_state.setText("左灶\n工作中");
            tv_left_head_state.setTextColor(getColorSkin(R.color.text_select_color));

        } else {
            tv_left_head_state.setText("左灶\n已关火");
            tv_left_head_state.setTextColor(getColorSkin(R.color.text_color_gray));
        }
        if (stove.rightHead.status == 2) {
            tv_right_head_state.setText("右灶\n工作中");
            tv_right_head_state.setTextColor(getColorSkin(R.color.text_select_color));
        } else {
            tv_right_head_state.setText("右灶\n已关火");
            tv_right_head_state.setTextColor(getColorSkin(R.color.text_color_gray));
        }

    }


    private int getColorSkin(int resId) {
        return SkinCompatResources.getColor(activity, resId);
    }
    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(activity, resId);
    }
}
