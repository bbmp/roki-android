package com.robam.roki.ui.adapter3;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hjq.toast.ToastUtils;
import com.robam.common.pojos.CurveCookbookDto;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.roki.R;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;

/**
 * 烹饪曲线列表adapter
 */
public class CookLineListAdapter extends BaseQuickAdapter<PayLoadCookBook, BaseViewHolder> {

    public CookLineListAdapter() {
        super(R.layout.item_cook_line);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, PayLoadCookBook item) {
        if (item != null){
            holder.setText(R.id.tv_title , item.curveCookbookDto.name);
            TextView tv_show = (TextView)holder.getView(R.id.tv_show);
            tv_show.setOnClickListener(v -> {
                ToastUtils.show("分享");
            });
            TextView tv_check_edit = (TextView)holder.getView(R.id.tv_check_edit);
            tv_check_edit.setOnClickListener(v -> {
                ToastUtils.show("查看编辑");
            });
            TextView tv_cook = (TextView)holder.getView(R.id.tv_cook);
            tv_cook.setOnClickListener(v -> {
                ToastUtils.show("烹饪");
            });
        }
    }


}
