package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneEnum;
import com.robam.common.util.NumberUtil;
import com.robam.roki.R;
import com.robam.roki.db.model.CookingStepsModel;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class Rv610RecipeStepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {
    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"四" ,"四" ,"四" ,"四" ,"四" };

    public Rv610RecipeStepAdapter() {
        super(R.layout.item_d610_recipe_step);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeStepBean item) {
        if (item != null){
            TextView tv_number = (TextView)holder.getView(R.id.tv_number);
            TextView tv_item_name = (TextView)holder.getView(R.id.tv_item_name);
            tv_number.setText(holder.getLayoutPosition()+1+"");
            SteamOvenModeEnum match = SteamOvenModeEnum.match(item.getWork_mode());
            if ("EXP".equals(item.getFunction_name())){
                tv_item_name.setText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
            }else if (SteamOvenModeEnum.ZHIKONGZHENG == match){
                tv_item_name.setText(item.getFunction_name() + " " + ( item.getSteam_flow() == 2 ? "中" : "大") + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
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
