package com.robam.roki.ui.adapter3;

import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.CookStep;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 菜谱详情烹饪步骤adapter
 */
public class RvRecipeSteps3Adapter extends BaseQuickAdapter<CookStep, BaseViewHolder> {
    private int index  = 0;
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(40, 0));

    public RvRecipeSteps3Adapter() {
        super(R.layout.item_cook_step_position);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CookStep item) {
        if (item != null) {
            holder.setText(R.id.tv_step_position , item.order +"");
            TextView textView = (TextView) holder.getView(R.id.tv_step_position);
            if (holder.getLayoutPosition() == index){
                textView.setTextSize(28);
            }else {
                textView.setTextSize(16);
            }
        }
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }
}
