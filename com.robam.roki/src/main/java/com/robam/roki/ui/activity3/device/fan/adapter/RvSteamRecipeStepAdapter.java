package com.robam.roki.ui.activity3.device.fan.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class RvSteamRecipeStepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {
//    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"四" ,"四" ,"四" ,"四" ,"四" };

    public RvSteamRecipeStepAdapter() {
        super(R.layout.item_mutil_new_recipe_step);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeStepBean item) {
        if (item != null){
            TextView tv_number = (TextView)holder.getView(R.id.tv_number);
            TextView tv_item_name = (TextView)holder.getView(R.id.tv_item_name);
            tv_number.setText(holder.getLayoutPosition()+1+"");
            if ("EXP".equals(item.getFunction_name())){
                tv_item_name.setText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
            }else if ("澎湃蒸".equals(item.getFunction_name())||"加湿烤".equals(item.getFunction_name())){

                String steam="小";
                switch (item.getSteam_flow()){
                    case 2:
                        steam="中";
                        break;
                    case 3:
                        steam="大";
                        break;
                    default:
                        steam="小";
                }
                tv_item_name.setText(item.getFunction_name() + " " +  steam +" "+ item.getTemperature()  + "℃ " + item.getTime() + "min");
            }else {
                tv_item_name.setText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            }
        }
    }

    public interface OnLeftTouchListener{
        void onLeftTouch(int position);
    }
    Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener ;

    public void addOnLeftTouchListener(Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }
}
