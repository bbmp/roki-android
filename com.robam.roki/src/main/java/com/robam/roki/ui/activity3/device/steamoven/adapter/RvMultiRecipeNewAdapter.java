package com.robam.roki.ui.activity3.device.steamoven.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.Plat;
import com.robam.common.io.cloud.Reponses;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.activity3.device.fan.adapter.RvSteamRecipeStepAdapter;
import com.robam.roki.ui.adapter3.Rv610RecipeStepAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.dialog.ImageShareDialog;
import com.robam.roki.ui.mdialog.RecipteMutiShareDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * 多段菜谱adapter
 */
public class RvMultiRecipeNewAdapter extends BaseQuickAdapter<Reponses.multiRecipeList, BaseViewHolder> {
    String[] strings = new String[]{"0", "一", "二", "三", "四", "四", "四", "四", "四", "四"};
    private int selectPosition = -1;

    private int delPosition = -1;

    public RvMultiRecipeNewAdapter() {
        super(R.layout.item_new_multi_recipe);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, Reponses.multiRecipeList item) {
        ImageView iv_select = holder.getView(R.id.iv_select);
        List<RecipeStepBean> recipeStepList = new ArrayList<>();
        holder.getView(R.id.txt_share).setOnClickListener(v -> {
            String url="https://h5.myroki.com/distkitch/index.html#/lineViewsFromRokiApp?avatar="
                    +Plat.accountService.getCurrentUser().figureUrl+"&name="+Plat.accountService.getCurrentUser().name+"&id="
                    + item.id;
                RecipteMutiShareDialog mRecipeMultipleShareDialog = new RecipteMutiShareDialog(getContext(), item.name, recipeStepList, url,
                        item.deviceGuid);
                mRecipeMultipleShareDialog.create();
                mRecipeMultipleShareDialog.show();


                ImageShareDialog.show(getContext(), url, item.name, item.id
                ).setDialog(mRecipeMultipleShareDialog).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mRecipeMultipleShareDialog.dismiss();
                    }
                });



        });
        if (item != null) {


//            holder.getView(R.id.txt_share).setVisibility(View.INVISIBLE);

            if (holder.getLayoutPosition() == selectPosition) {
                iv_select.setImageResource(R.mipmap.privacy_selected_3x);
            } else {
                iv_select.setImageResource(R.mipmap.icon_choice_multi_nomal);
            }

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
                    listItem.setTime(item.multiStepDtoList.get(i).getTime(item.deviceGuid));
                    listItem.setSteam_flow(Integer.parseInt(item.multiStepDtoList.get(i).steamQuantity));
                    listItem.setTemperature(Integer.parseInt(item.multiStepDtoList.get(i).temperature));
                    listItem.setTemperature2(Integer.parseInt(item.multiStepDtoList.get(i).downTemperature));
//                    listItem.setWork_mode();
                    recipeStepList.add(listItem);
                    totaTime += item.multiStepDtoList.get(i).getTime(item.deviceGuid);
                }
            }

//            SettingBar stbStep = (SettingBar) holder.getView(R.id.stb_step_top);
//            stbStep.setLeftText(item.name);
//            stbStep.setRightText("共" + strings[recipeStepList.size()] + "段" + " " + totaTime + "min");
////            addChildClickViewIds(R.id.stb_step_top , R.id.tv_del);
//            addChildClickViewIds(R.id.tv_del);
//            stbStep.getLeftView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onSelectListener != null) {
//                        onSelectListener.onSelect(holder.getLayoutPosition());
//                    }
//                }
//            });
            TextView tv_item_name = holder.getView(R.id.tv_item_name);
            tv_item_name.setText(item.name);
            TextView tv_right = holder.getView(R.id.tv_right);
            tv_right.setText(totaTime + "min");
            addChildClickViewIds(R.id.tv_right);
            RecyclerView rvSteamStep = (RecyclerView) holder.getView(R.id.rv_sub_steam_step);
            rvSteamStep.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            RvSteamRecipeStepAdapter rvSteamStepAdapter = new RvSteamRecipeStepAdapter();
            rvSteamStep.setAdapter(rvSteamStepAdapter);
            rvSteamStepAdapter.addData(recipeStepList);

            rvSteamStepAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(holder.getLayoutPosition());
                    }
                }
            });

            rvSteamStepAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                    if (onLeftTouchListener != null) {
                        onLeftTouchListener.onLeftTouch(holder.getLayoutPosition());
                    }
                    return false;
                }
            });

            View v_del = holder.getView(R.id.tv_del);
            if (holder.getLayoutPosition() == delPosition) {
                v_del.setVisibility(View.VISIBLE);
            } else {
                v_del.setVisibility(View.GONE);
            }

        }
    }

    public void setSelectPosition(int selectPosition) {
        if (this.selectPosition == selectPosition) {
            this.selectPosition = -1;
        } else {
            this.selectPosition = selectPosition;
        }

        notifyDataSetChanged();
    }

    public void setDelPosition(int delPosition) {
        if (this.delPosition == delPosition) {
            this.delPosition = -1;
            notifyDataSetChanged();
        } else {
            this.delPosition = delPosition;
            notifyDataSetChanged();
        }

    }

    public void setRightDelPosition(int delPosition) {
        if (this.delPosition == delPosition) {
            this.delPosition = -1;
            notifyDataSetChanged();
        }

    }

    OnSelectListener onSelectListener;

    public interface OnSelectListener {
        void onSelect(int position);
    }

    public void addOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnLeftTouchListener {
        void onLeftTouch(int position);
    }

    Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener;

    public void addOnLeftTouchListener(Rv610StepAdapter.OnLeftTouchListener onLeftTouchListener) {
        this.onLeftTouchListener = onLeftTouchListener;
    }

    public int getSelectPosition() {
        return selectPosition;
    }


}