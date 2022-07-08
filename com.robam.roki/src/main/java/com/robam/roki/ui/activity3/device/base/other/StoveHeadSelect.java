package com.robam.roki.ui.activity3.device.base.other;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.base.BaseDialog;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;

import skin.support.content.res.SkinCompatResources;

public class StoveHeadSelect extends DialogUtils {

    private final BaseDialog dialog;

    public StoveHeadSelect(Activity activity) {
        super(activity);
        this.activity = activity;
        dialog = new BaseDialog(activity);
    }

    /**
     * 灶头选择
     * @param stove
     */
    private ImageView img_left_head;
    private ImageView img_right_head;
    private TextView tv_left_head_state;
    private TextView tv_right_head_state;
    private TextView tv_content_text;

    public void stoveHeadSelect(OnSelectListener onSelectListener ){
        dialog.setContentView(R.layout.dialog_stove_quick_off_new);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        View left = dialog.findViewById(R.id.ll_choice_left);
        View right = dialog.findViewById(R.id.ll_choice_right);
        img_left_head = dialog.findViewById(R.id.img_left_head);
        img_right_head = dialog.findViewById(R.id.img_right_head);
        tv_left_head_state = dialog.findViewById(R.id.tv_left_head_state);
        tv_right_head_state = dialog.findViewById(R.id.tv_right_head_state);
        tv_content_text = dialog.findViewById(R.id.tv_content_text);
        View v_dialog_top =dialog.findViewById(R.id.v_dialog_top);
        v_dialog_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == left) {
                    onSelectListener.onSelectPosition(0);
                }else if (view == right) {
                    onSelectListener.onSelectPosition(1);
                }
            }
        }, left , right );
    }

    private Stove mStove;

    public void setStove(Stove stove) {
        this.mStove = stove;
    }

    public void setStatus(Stove stove) {
        setStove(stove);
        //status == 2 开火
        if (stove.leftHead.status == 2) {
            img_left_head.setImageResource(R.mipmap.img_stove_head_work);
            tv_left_head_state.setText("左灶\n工作中");
            tv_left_head_state.setTextColor(getColorSkin(R.color.text_select_color));
            tv_content_text.setText("点击炉头进行关火");

        } else {
            img_left_head.setImageResource(R.mipmap.img_stove_head_common);
            tv_left_head_state.setText("左灶\n已关火");
            tv_left_head_state.setTextColor(getColorSkin(R.color.text_color_gray));
        }
        if (stove.rightHead.status == 2) {
            img_right_head.setImageResource(R.mipmap.img_stove_head_work);
            tv_right_head_state.setText("右灶\n工作中");
            tv_right_head_state.setTextColor(getColorSkin(R.color.text_select_color));
            tv_content_text.setText("点击炉头进行关火");
        } else {
            img_right_head.setImageResource(R.mipmap.img_stove_head_common);
            tv_right_head_state.setText("右灶\n已关火");
            tv_right_head_state.setTextColor(getColorSkin(R.color.text_color_gray));
        }
        if(stove.leftHead.status == 0&&stove.rightHead.status == 0){
            tv_content_text.setText("所有炉头均已关火");
        }
    }


    private int getColorSkin(int resId) {
        return SkinCompatResources.getColor(activity, resId);
    }
    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(activity, resId);
    }
}
