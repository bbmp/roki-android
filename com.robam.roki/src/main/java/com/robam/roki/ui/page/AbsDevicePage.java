package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceCtrlRecipeView;

import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by yinwei on 2017/8/30.
 */

public abstract class  AbsDevicePage extends BasePage {
    TextView oven_title;
    DeviceCtrlRecipeView oven_recipe_listview;
    RelativeLayout oven_normal_ctrl;
    public String guid;
    View contentView;
    LayoutInflater inflater;

    protected  abstract void initView();

   protected abstract void remoteOperation();

    protected abstract  void errorModel();

    protected abstract void disconnectModel();

    protected abstract void workModel();

    protected abstract void orderModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.abs_device_oven_page, container, false);
        oven_title = contentView.findViewById(R.id.oven_title);
        oven_recipe_listview = contentView.findViewById(R.id.oven_recipe_listview);
        oven_normal_ctrl = contentView.findViewById(R.id.oven_normal_ctrl);
        ButterKnife.inject(this, contentView);
        initView();
        return contentView;
    }



    //菜谱收藏事件操作
    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        if (event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeFavoriteChange
                || event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeDetailBackMyCollect
                || event.flag == HomeRecipeViewEvent.RecipeFavoriteChange) {
            oven_recipe_listview.onPullDownToRefresh(null);
        }
    }

    //返回键
    @OnClick(R.id.oven_return)
    public void OnClickReturn() {
        UIService.getInstance().returnHome();
    }

    //菜谱专属与菜谱收藏
    View initRecipeListView() {
        View view = inflater.inflate(R.layout.page_device_oven_listview_show, null, false);
        view.findViewById(R.id.oven_person_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("我的专属菜谱暂未上线", Toast.LENGTH_SHORT);
            }
        });
        view.findViewById(R.id.oven_person_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
            }
        });
        return view;
    }


}
