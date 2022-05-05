package com.robam.roki.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.VoidCallback2;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/8/26.
 */

public class ThemeListViewItem extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private Context cx;
    private RecipeTheme theme;
    @InjectView(R.id.theme_listitem_img)
    ImageView theme_listitem_img;
    @InjectView(R.id.theme_listitem_title)
    TextView theme_listitem_title;
    ArrayList<String> timeDataList = new ArrayList<>();

    public ThemeListViewItem(Context context) {
        super(context);
        this.cx = context;
        init();
    }


    public ThemeListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        init();
    }

    public ThemeListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cx = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_theme_listview_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            setOnClickListener(this);
            setOnLongClickListener(this);
        }
        getThemeHttpData();
    }

    private void getThemeHttpData() {
        CookbookManager.getInstance().getThemeRecipes_new(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                if (recipeThemes == null || recipeThemes.size() == 0) {
                    return;
                }
                for (int i  = 0; i < recipeThemes.size();i++){
                    String insertTimeDate = recipeThemes.get(i).insertTimeDate;
                    timeDataList.add(insertTimeDate);
                }
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void loadData(RecipeTheme theme) {
        this.theme = theme;
        ImageUtils.displayImage(getContext(), theme.imageUrl, theme_listitem_img);
        theme_listitem_title.setText(theme.name);
    }

    /**
     * 点击进入详情
     */
    @Override
    public void onClick(View v) {
        if (theme == null) return;
        if ("1".equals(theme.type)) {
            Bundle bundle = new Bundle();
            theme.isCollect = true ;
            bundle.putSerializable(PageArgumentKey.Theme, theme);
            bundle.putStringArrayList(PageArgumentKey.timeDataList,timeDataList);
            UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bundle);
        } else {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.WebTitle, "主题菜谱");
            bd.putString(PageArgumentKey.Url, theme.activity);
            UIService.getInstance().postPage(PageKey.WebClient, bd);

        }
    }

    /**
     * 长按删除
     */
    @Override
    public boolean onLongClick(View v) {
        onDelete(new VoidCallback2() {
            @Override
            public void onCompleted() {
                ProgressDialogHelper.setRunning(MainActivity.activity, true);
                StoreService.getInstance().setCancelThemeCollect(theme.id, new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            ToastUtils.show("已取消收藏", Toast.LENGTH_SHORT);
                            EventUtils
                                    .postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ThemeFavoriteChange));
                        }
                        ProgressDialogHelper.setRunning(MainActivity.activity, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("取消收藏失败", Toast.LENGTH_SHORT);
                        ProgressDialogHelper.setRunning(MainActivity.activity, false);
                    }
                });
            }
        });
        return true;
    }

    void onDelete(final VoidCallback2 callback) {
        DialogHelper.newDialog_OkCancel(MainActivity.activity, "确认删除此项?", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (callback != null) {
                                callback.onCompleted();
                            }
                        }
                    }
                }
        ).show();
    }
}
