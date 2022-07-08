package com.robam.roki.ui.adapter3;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class Rv620StepAdapter extends BaseQuickAdapter<RecipeStepBean, BaseViewHolder> {

    private int selectPosition = -1 ;
    String[] strings = new String[]{"一" , "二"  ,"三" ,"四" ,"五" ,"六" ,"七" };
    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;

    public Rv620StepAdapter() {
        super(R.layout.item_d620_step);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecipeStepBean item) {
        if (item != null){
            SettingBar stbStep = (SettingBar)holder.getView(R.id.stb_step);
            SteamOvenModeEnum match = SteamOvenModeEnum.match(item.getWork_mode());
            if ("EXP".equals(item.getFunction_name())){
                stbStep.setRightText(item.getFunction_name() + " " + item.getTemperature()  + "℃ " + " " + item.getTemperature2()  + "℃ " + item.getTime() + "min");
            }else if ("澎湃蒸".equals(item.getFunction_name())){
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


//            stbStep.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            mPosX = event.getX();
//                            mPosY = event.getY();
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            mCurPosX = event.getX();
//                            mCurPosY = event.getY();
//
//                            break;
//                        case MotionEvent.ACTION_UP:
////                            if (mCurPosY - mPosY > 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向下滑動
////
////                            } else if (mCurPosY - mPosY < 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向上滑动
////                            }
//                            if (mCurPosX - mPosX > 0
//                                    && (Math.abs(mCurPosX - mPosX) > 3)) {
//                                //向右滑動
////                                ToastUtils.showShort("向右滑动");
//                                if (onLeftTouchListener != null){
//                                    onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
//                                    return true;
//                                }
//
//                            } else if (mCurPosX - mPosX < 0
//                                    && (Math.abs(mCurPosX - mPosX) > 3)) {
//                                //向左滑动
////                                ToastUtils.showShort("向左滑动");
//                                if (onLeftTouchListener != null){
//                                    onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
//                                    return true;
//                                }
//                            }
//                            break;
//                    }
//                    return false;
//                }
//            });
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
