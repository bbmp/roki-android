package com.robam.roki.ui.activity3.device.steamoven.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeStepBean;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class RvSteamAddStepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {

    private int selectPosition = -1 ;
    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"五" ,"六" ,"七" };
    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;

    public RvSteamAddStepAdapter() {
        super(R.layout.item_steam_add_step);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeStepBean item) {
        if (item != null){

            TextView tv_number = (TextView)holder.getView(R.id.tv_number);
            TextView tv_item_name = (TextView)holder.getView(R.id.tv_item_name);
            tv_number.setText(holder.getLayoutPosition()+1+"");
            if ("EXP".equals(item.getFunction_name())){
                tv_item_name.setText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
            }
            else if ("澎湃蒸".equals(item.getFunction_name() )){
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
                tv_item_name.setText(item.getFunction_name() + " " +steam + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            }
            else {
                tv_item_name.setText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
            }

//            SettingBar stbStep = (SettingBar)holder.getView(R.id.stb_step);
//            SteamOvenModeEnum match = SteamOvenModeEnum.match(item.getWork_mode());
//            if ("EXP".equals(item.getFunction_name())){
//                stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
//            }else if (SteamOvenModeEnum.ZHIKONGZHENG == match){
//                stbStep.setRightText(item.getFunction_name() + " " + ( item.getSteam_flow() == 2 ? "中" : "大") + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
//            }else {
//                stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + item.getTime() + "min");
//            }

//            stbStep.setLeftText("第" + strings[holder.getLayoutPosition()] +"段");
            View v_del = holder.getView(R.id.tv_del);
            if (holder.getLayoutPosition() == selectPosition){
//                holder.setVisible(R.id.tv_del , holder.getLayoutPosition() == selectPosition ? true : false);
                v_del.setVisibility(View.VISIBLE);
            }else {
                v_del.setVisibility(View.GONE);
            }
            addChildClickViewIds(R.id.ll_item ,R.id.tv_del);


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
