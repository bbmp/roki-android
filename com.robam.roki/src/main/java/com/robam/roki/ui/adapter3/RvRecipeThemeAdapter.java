package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.Callback;
import com.legent.plat.constant.IDeviceType;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.SelectThemeDetailPage;
import com.robam.roki.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des：菜谱和专题混合列表
 */
public class RvRecipeThemeAdapter extends BaseMultiItemQuickAdapter<ThemeRecipeMultipleItem, BaseViewHolder> implements LoadMoreModule {


    private static RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存
            .transform(new CenterCrop(), new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角


    public RvRecipeThemeAdapter() {
        addItemType(ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT, R.layout.list_item_card);
        addItemType(ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT, R.layout.item_recipe_thome);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, ThemeRecipeMultipleItem multiItemEntity) {
        switch (holder.getItemViewType()) {
            case ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT:


                final Recipe recipe = multiItemEntity.getRecipe();
                holder.setText(R.id.tv_recipe_name, recipe.name)
                        .setText(R.id.tv_collection_number, "收藏" + NumberUtil.converString(recipe.collectCount))
                        .setText(R.id.tv_collection_number, "")
                        .setText(R.id.tv_recipe_read_number, NumberUtil.converString(recipe.viewCount));



                ImageView imageView=(ImageView) holder.getView(R.id.iv_tag_recipe);
                GlideApp.with(getContext())
                        .load(RecipeUtils.getRecipeImgUrl(recipe))
                        .apply(maskOption)
                        .into(imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecipeDetailPage.show(recipe.id, recipe.sourceType);
                    }
                });

                TextView tvCollection = (TextView) holder.getView(R.id.tv_collection_number);
                ImageView img=holder.getView(R.id.img_collection_number);
                List<Dc> dcs = recipe.getJs_dcs();
                if (dcs != null && dcs.size() != 0) {

                    img.setImageResource(DeviceNameHelper.getIcon(dcs));
                    tvCollection.setText(DeviceNameHelper.getDeviceName2(dcs));

                } else {
//                    img.setImageDrawable(null);
                    img.setImageResource(R.drawable.hot);
                    tvCollection.setText(NumberUtil.converString(recipe.viewCount));
                    holder.setVisible(R.id.tv_recipe_read_number,false);
                    holder.setVisible(R.id.img_recipe_read_number,false);

                }
                break;
            case ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT:
//                 ThemeRecipeList themeRecipeList = multiItemEntity.getThemeRecipeList();
                RecipeTheme recipeTheme = multiItemEntity.getRecipeTheme();
//                RecipeTheme recipeTheme = themeRecipeList.getRecipeTheme();
                holder.setText(R.id.tv_theme_title, recipeTheme.name)
                        .setText(R.id.tv_theme_subname, recipeTheme.subName);

                //初始化专题recyclerView
                RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.rv_recipe_pic_list);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                //获取专题菜谱数据
                getBygetCookbookBythemeId("cn", 4, 0, recipeTheme, recyclerView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtils.show((holder.getLayoutPosition()+1) + " ");
                        SelectThemeDetailPage.show(recipeTheme, SelectThemeDetailPage.TYPE_THEME_RECIPE);
                    }
                });

                break;
            default:
                break;
        }
    }

    /**
     * 根据主题id查询下属所有菜单
     */
    private void getBygetCookbookBythemeId(String lang, long limit, final int start, final RecipeTheme recipeTheme, RecyclerView recyclerView) {

        RokiRestHelper.getCookBookBythemeId(lang, limit, start, recipeTheme.id.intValue(), new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                RvThemePicAdapter rvThemePicAdapter = new RvThemePicAdapter();
                recyclerView.setAdapter(rvThemePicAdapter);
                rvThemePicAdapter.setList(recipes);
                rvThemePicAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        SelectThemeDetailPage.show(recipeTheme, SelectThemeDetailPage.TYPE_THEME_RECIPE);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
