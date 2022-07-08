package com.robam.roki.ui.adapter3;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import org.jetbrains.annotations.NotNull;

import static com.robam.roki.utils.UiUtils.getResources;

/**
 * @author r210190
 * 自定义加载状态
 */
public class CustomLoadMoreHomeView extends BaseLoadMoreView {

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder baseViewHolder) {
        TextView tvThemeTellRoki = (TextView) baseViewHolder.findView(R.id.tv_theme_tell_roki);
        tvThemeTellRoki.setText("加载更多");
        return tvThemeTellRoki;
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder baseViewHolder) {
        TextView tvThemeTellRoki = (TextView) baseViewHolder.findView(R.id.tv_theme_tell_roki);
        tvThemeTellRoki.setText("---我是有底线的---");
//        tvThemeTellRoki.setTextColor(Color.GRAY);
//        String tellRokiText = tvThemeTellRoki.getText().toString().trim();
//        int start = 15;
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//        ssb.append(tellRokiText);
//        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.roki_sub_color)), start, start + 6, 0);
//        tvThemeTellRoki.setText(ssb);
//        tvThemeTellRoki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UIService.getInstance().postPage(PageKey.TellRoki);
//            }
//        });
        return tvThemeTellRoki;
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder baseViewHolder) {
        TextView tvThemeTellRoki = (TextView) baseViewHolder.findView(R.id.tv_theme_tell_roki);
        tvThemeTellRoki.setText("数据请求失败");
        return tvThemeTellRoki;
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder baseViewHolder) {
        TextView tvThemeTellRoki = (TextView) baseViewHolder.findView(R.id.tv_theme_tell_roki);
        tvThemeTellRoki.setText("加载中......");
        return tvThemeTellRoki;
    }

    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foot_tell_roki, viewGroup, false);
    }
}
