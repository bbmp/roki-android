package com.robam.roki.ui.adapter3;

import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class RvIntegraStepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {

    private int selectPosition = -1 ;
    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"五" ,"六" ,"七" };


    public RvIntegraStepAdapter() {
        super(R.layout.item_integra_step);
    }


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

            stbStep.setLeftText("第" + strings[holder.getLayoutPosition()] +"段");
            View v_del = holder.getView(R.id.tv_del);
            if (holder.getLayoutPosition() == selectPosition){
//                holder.setVisible(R.id.tv_del , holder.getLayoutPosition() == selectPosition ? true : false);
                v_del.setVisibility(View.VISIBLE);
            }else {
                v_del.setVisibility(View.GONE);
            }
            addChildClickViewIds(R.id.stb_step ,R.id.tv_del);

        }
    }

    public void setSelectPosition(int selectPosition) {
        if (this.selectPosition == selectPosition){
            this.selectPosition = -1 ;
        }else {
            this.selectPosition = selectPosition;
        }
        notifyDataSetChanged();
    }

    public interface OnLeftTouchListener{
        void onLeftTouch(int position);
    }
    OnLeftTouchListener onLeftTouchListener ;

    public void addOnLeftTouchListener(OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }
}
