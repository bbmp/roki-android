package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

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
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class RvIntegraRecipeStepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {
    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"四" ,"四" ,"四" ,"四" ,"四" };
    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;

    public RvIntegraRecipeStepAdapter() {
        super(R.layout.item_integra_recipe_step);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeStepBean item) {
        if (item != null){
            SettingBar stbStep = (SettingBar)holder.getView(R.id.stb_step);
            SteamOvenModeEnum match = SteamOvenModeEnum.match(item.getWork_mode());
            if ("EXP".equals(item.getFunction_name())){
                stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
            }else if ("澎湃蒸".equals(item.getFunction_name() )){
                stbStep.setRightText(item.getFunction_name() + " " + ( item.getSteam_flow() == 2 ? "中" : "大") + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            }else {
                stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            }
//            stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            stbStep.setLeftText("第" + strings[holder.getLayoutPosition()] +"段");

        }
    }

    public interface OnLeftTouchListener{
        void onLeftTouch(int position);
    }
    com.robam.roki.ui.adapter3.Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener ;

    public void addOnLeftTouchListener(Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }
}
