package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneEnum;
import com.robam.common.util.NumberUtil;
import com.robam.roki.R;
import com.robam.roki.db.model.CookingStepsModel;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.adapter3.Rv610RecipeStepAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.mdialog.MenuDialog;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author r210190
 * des 610多段菜谱adapter
 */
public class RvIntegraRecipeAdapter extends BaseQuickAdapter<Reponses.multiRecipeList, BaseViewHolder> {
    String[] strings = new String[]{"0" ,"一" , "二"  ,"三" ,"四" ,"四" ,"四" ,"四" ,"四" ,"四" };
    private int selectPosition = -1;

    private int delPosition = -1 ;

    public RvIntegraRecipeAdapter() {
        super(R.layout.item_integra_recipe);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, Reponses.multiRecipeList item) {
        if (item != null){
            if (holder.getLayoutPosition() == selectPosition ){
                holder.setVisible(R.id.iv_select  , true);
            }else {
                holder.setVisible(R.id.iv_select , false);
            }
//            List<RecipeStepBean> recipeStepList = item.getRecipeStepList();

            List<RecipeStepBean> recipeStepList = new ArrayList<>();
            int totaTime = 0;
            if (item.multiStepDtoList != null && item.multiStepDtoList.size() > 0) {

                for (int i = 0; i < item.multiStepDtoList.size(); i++) {
                    RecipeStepBean listItem = new RecipeStepBean();
                    listItem.setFunction_code(item.multiStepDtoList.get(i).modelCode);
                    listItem.setFunction_name(item.multiStepDtoList.get(i).modelName);
//                    listItem.setRecipe_id(item.id);
//                    listItem.setFunction_params();
//                    listItem.setId();
//                    listItem.setInset_time(Long.parseLong(item.multiStepDtoList.get(i).time));
                    listItem.setTime(Integer.parseInt(item.multiStepDtoList.get(i).time));
                    listItem.setSteam_flow(Integer.parseInt(item.multiStepDtoList.get(i).steamQuantity));
                    listItem.setTemperature(Integer.parseInt(item.multiStepDtoList.get(i).temperature));
                    listItem.setTemperature2(Integer.parseInt(item.multiStepDtoList.get(i).downTemperature));
//                    listItem.setWork_mode();
                    recipeStepList.add(listItem);
                    totaTime += Long.parseLong(item.multiStepDtoList.get(i).time);
                }
            }

            SettingBar stbStep = (SettingBar)holder.getView(R.id.stb_step_top);
            stbStep.setLeftText(item.name);
            int time = 0;
            for (RecipeStepBean recipeStepBean : recipeStepList) {
                time += recipeStepBean.getTime();
            }
            stbStep.setRightText( +time + "min");
//            addChildClickViewIds(R.id.stb_step_top , R.id.tv_del);
            addChildClickViewIds( R.id.tv_del);
            stbStep.getLeftView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectListener != null){
                        onSelectListener.onSelect(holder.getLayoutPosition());
                    }
                }
            });
//            addChildLongClickViewIds(R.id.stb_step_top);
            RecyclerView rv610Step = (RecyclerView) holder.getView(R.id.rv_610_step);
            rv610Step.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
//            rv610Step.setLayoutManager(new GridLayoutManager(getContext(), 2));
            RvIntegraRecipeStepAdapter rvIntegraStepAdapter = new RvIntegraRecipeStepAdapter();
            rv610Step.setAdapter(rvIntegraStepAdapter);
            rvIntegraStepAdapter.addData(recipeStepList);

            rvIntegraStepAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onSelectListener != null){
                        onSelectListener.onSelect(holder.getLayoutPosition());
                    }
                }
            });

            rvIntegraStepAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onLeftTouchListener != null){
                        onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
                    }
                    return false;
                }
            });


            View v_del = holder.getView(R.id.tv_del);
            if (holder.getLayoutPosition() == delPosition){
//                holder.setVisible(R.id.tv_del , holder.getLayoutPosition() == selectPosition ? true : false);
                v_del.setVisibility(View.VISIBLE);
            }else {
                v_del.setVisibility(View.GONE);
            }

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
    public void setDelPosition(int delPosition) {
        if (this.delPosition == delPosition){
            this.delPosition = -1 ;
            notifyDataSetChanged();
        }else {
            this.delPosition = delPosition;
            notifyDataSetChanged();
        }

    }
    public void setRightDelPosition(int delPosition) {
        if (this.delPosition == delPosition){
            this.delPosition = -1 ;
            notifyDataSetChanged();
        }

    }

    OnSelectListener onSelectListener ;
    public interface OnSelectListener{
        void onSelect(int position);
    }

    public void addOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnLeftTouchListener{
        void onLeftTouch(int position);
    }
    com.robam.roki.ui.adapter3.Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener ;

    public void addOnLeftTouchListener(Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }

    public int getSelectPosition() {
        return selectPosition;
    }


}
