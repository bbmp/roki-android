package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Material;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.RecipeDifficultyView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by Rosicky on 16/1/20.
 *
 * 烤蒸微的菜谱Banner
 */
public class RecipeBanner2Page extends BasePage {
    @InjectView(R.id.pager)
    ViewPager pager;

    ExtPageAdapter adapter;

    Ordering<AbsRecipe> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<AbsRecipe, Comparable>() {
        @Override
        public Comparable apply(AbsRecipe absRecipe) {
            return absRecipe.id;
        }
    }).reverse();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        long bookId = getArguments().getLong(PageArgumentKey.BookId);

        View view = inflater.inflate(R.layout.page_recipe_banner, container, false);
        ButterKnife.inject(this, view);

        adapter = new ExtPageAdapter();
        pager.setAdapter(adapter);
        loadData(bookId);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.imgReturn)
    public void onClickBack() {
        UIService.getInstance().popBack();
    }

    @OnPageChange(R.id.pager)
    public void onPageSelected(int position) {

        ViewHolder vh = (ViewHolder) adapter.getPage(position).getTag();
        if (vh != null) {
            vh.refresh();
        }
    }

    void loadData(final long bookId) {
        ProgressDialogHelper.setRunning(cx, true);
        CookbookManager.getInstance().getRecommendCookbooks(new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> tinyBooks) {
                ProgressDialogHelper.setRunning(cx, false);
                buildViews(bookId, tinyBooks);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    void buildViews(final long bookId, List<Recipe> books) {
        if (books == null || books.size() == 0) return;

        List<View> views = Lists.newArrayList();
        LayoutInflater inflater = LayoutInflater.from(cx);
        ViewHolder vh;
        View view;
        int index = 0;
        Recipe book;

        books = ordering.sortedCopy(books);
        for (int i = 0; i < books.size(); i++) {
            book = books.get(i);
            view = inflater.inflate(R.layout.view_recipe_banner_item, null);
            vh = new ViewHolder(view, book);
            view.setTag(vh);
            views.add(view);

            if (book.id == bookId) {
                index = i;
                if (index == 0) {
                    vh.refresh();
                }
            }
        }
        adapter.loadViews(views);
        pager.setCurrentItem(index, true);
    }


    class ViewHolder {
        @InjectView(R.id.imgRecipe)
        ImageView imgRecipe;
        @InjectView(R.id.txtTitle)
        TextView txtTitle;
        @InjectView(R.id.txtDesc)
        TextView txtDesc;
        @InjectView(R.id.difficultyView)
        RecipeDifficultyView difficultyView;
        @InjectView(R.id.txtSlaveMaterial)
        TextView txtSlaveMaterial;
        @InjectView(R.id.txtMainMaterial)
        TextView txtMainMaterial;

        Recipe recipe;

        ViewHolder(View view, Recipe recipe) {
            ButterKnife.inject(this, view);
            this.recipe = recipe;
            initView();
        }

        @OnClick(R.id.txtDetail)
        public void onClickDetail() {
            if (recipe != null) {
                RecipeDetailPage.show(recipe.id,RecipeDetailPage.RecipeBanner2Page);
            }
        }

        void refresh() {
            imgRecipe.setImageDrawable(null);
            if (recipe.isNewest()) {
                showData(recipe);
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().getCookbookById(recipe.id, new Callback<Recipe>() {
                    @Override
                    public void onSuccess(Recipe book) {
                        ProgressDialogHelper.setRunning(cx, false);
                        showData(book);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showThrowable(t);
                    }
                });
            }
        }

        void showData(Recipe book) {
            if (book == null) return;
            this.recipe = book;
            ImageUtils.displayImage(cx, book.imgPoster, imgRecipe);
            txtTitle.setText(book.name);
            txtDesc.setText(book.desc);
            difficultyView.setDifficulty(book.difficulty);

            if (book.materials == null) return;
            txtMainMaterial.setText(getMaterialString(book.materials.getMain()));
            txtSlaveMaterial.setText(getMaterialString(book.materials.getAccessory()));
        }

        void initView() {
            imgRecipe.setImageDrawable(null);
            txtTitle.setText(null);
            txtDesc.setText(null);
            txtMainMaterial.setText(null);
            txtSlaveMaterial.setText(null);
            difficultyView.setDifficulty(0);
        }

        private String getMaterialString(List<Material> list) {
            StringBuilder sb = new StringBuilder();
            if (list == null || list.size() == 0) {
                return sb.toString();
            }

            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).toString()).append("、");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
    }

}
