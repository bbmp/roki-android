package com.robam.roki.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback2;
import com.legent.plat.events.ClickRecipeEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.services.CookbookManager;
import com.robam.common.ui.UiHelper;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.helper3.DeviceNameHelper;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RecipeGridItemVIew extends FrameLayout {
    final float Small_Height_Scale = 251 / 640f;
    final float Middle_Height_Scale = 320 / 640f;

    @InjectView(R.id.imgDish)
    ImageView imgDish;
    @InjectView(R.id.imgSrcLogo)
    ImageView imgSrcLogo;
    @InjectView(R.id.edtName)
    TextView txtName;
    @InjectView(R.id.txtCollectCount)
    TextView txtCollectCount;

    @InjectView(R.id.tv_view_count)
    TextView tvViewCount;

    Context cx;
    AbsRecipe book;
    int modelType;
    int smallHeight, middleHeight;
//    private static RequestOptions options = new RequestOptions()
//            .centerCrop()
//            .placeholder(R.mipmap.img_default) //预加载图片
//            .error(R.mipmap.img_default) //加载失败图片
//            .priority(Priority.HIGH) //优先级
//            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
//            .transform(new RoundedCornersTransformation(50, 10, RoundedCornersTransformation.CornerType.TOP)); //圆角

    private static RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存
            .transform(new CenterCrop(), new MaskTransformation(R.mipmap.icon_roki_recipe_bg)); //圆角

    public RecipeGridItemVIew(Context context, int modelType) {
        super(context);
        computeHeight();
        this.modelType = modelType;
        init(context);
    }

    @OnClick(R.id.layout)
    public void onClickDetail() {
        clickLayout();
    }

    public void clickLayout() {
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, book.id);

        if (book.isRoki()) {
            onShowDetail((Recipe) book,modelType);
        } else {
            Recipe3rd r = (Recipe3rd) book;
            bd.putString(PageArgumentKey.Url, r.detailUrl);
            bd.putLong(PageArgumentKey.BookId, r.id);
            bd.putString(PageArgumentKey.WebTitle, r.name);
            UIService.getInstance().postPage(PageKey.RecipeDetail3rd, bd);
        }
    }

    @OnLongClick(R.id.layout)
    public boolean onLongClick(View view) {

        switch (modelType) {
            case RecipeGridView.Model_Favority:


                onDelete(new VoidCallback2() {
                    @Override
                    public void onCompleted() {
                        ((Recipe) book).collected = true;
                        UiHelper.onFavority((Recipe) book,null,null);
                    }
                });


                return true;
            default:
                return true;
        }

    }

    void init(Context cx) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_grid_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    void computeHeight() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(getContext());
        smallHeight = (int) (screenWidth * Small_Height_Scale);
        middleHeight = (int) (screenWidth * Middle_Height_Scale);
    }

     public void showData(AbsRecipe book, final boolean isSmallSize) {
         this.book = book;
         imgDish.getLayoutParams().height = isSmallSize ? middleHeight : middleHeight;
//         txtName.setText(book.name.length() > 6 ? book.name.substring(0, 5) + "..." : book.name);
         txtName.setText(book.name);
         imgDish.setImageDrawable(null);
//         txtCollectCount.setText("收藏" + NumberUtil.converString(book.collectCount));
//         tvViewCount.setText("浏览" + NumberUtil.converString(book.viewCount));
         tvViewCount.setText("" + NumberUtil.converString(book.viewCount));
         imgSrcLogo.setImageDrawable(null);
         imgDish.setImageDrawable(null);



         Drawable drawable1 = UiUtils.getResources().getDrawable(R.drawable.hot, null);
         drawable1.setBounds(0, 0, 35, 35);
         tvViewCount.setCompoundDrawables(drawable1, null, null, null);


         List<Dc> dcs = ((Recipe)book).getJs_dcs();
         if (dcs != null && dcs.size() != 0) {
             Drawable drawable = UiUtils.getResources().getDrawable(DeviceNameHelper.getIcon(dcs), null);
             drawable.setBounds(0, 0, 35, 35);
             txtCollectCount.setCompoundDrawables(drawable, null, null, null);
             txtCollectCount.setText(DeviceNameHelper.getDeviceName2(dcs));

         } else {

         }

         if (book.isRoki()) {
             LogUtils.i("20190214", "Recipe:" + book.type);
             Recipe b = (Recipe) book;
//             ImageUtils.displayImage(b.imgMedium == null ? b.imgSmall : b.imgMedium, imgDish);
             GlideApp.with(getContext())
                     .load(RecipeUtils.getMediumRecipeImgUrl(b))
                     .apply(maskOption)
                     .into(imgDish);
             imgSrcLogo.setImageResource(R.mipmap.ic_recipe_roki_logo);
             imgSrcLogo.setVisibility(GONE);
         } else {
             LogUtils.i("20190214", "Recipe3rd:" + book.type);
             Recipe3rd b = (Recipe3rd) book;
             GlideApp.with(getContext())
                     .load(RecipeUtils.getMediumRecipeImgUrl(b))
                     .apply(maskOption)
                     .into(imgDish);

             RecipeProvider provider = b.getProvider();
             if (provider != null) {
                 ImageUtils.displayImage(getContext(), provider.logoUrl, imgSrcLogo);
             }
         }
    }

    void onDelete(final VoidCallback2 callback) {
        DialogHelper.newDialog_OkCancel(getContext(), "确认删除此项?", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (callback != null) {
                                if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
                                    callback.onCompleted();
                                }
                            }
                        }
                    }
                }
        ).show();
    }

    void onShowDetail(Recipe recipe, final int modelType) {

        if (recipe.isNewest()) {
            if (modelType == RecipeGridView.Model_Favority){
                RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_FAVORITY);
            }else if (modelType == RecipeGridView.Model_Search){
                RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_SEARCE);
            }else if(modelType == RecipeGridView.Model_History){
                RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_ALL);
            }else {
                RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_SORTED);
            }
        } else {
            ProgressDialogHelper.setRunning(cx, true);

            LogUtils.i("20180830"," recipe.id:" + recipe.id);

            CookbookManager.getInstance().getCookbookById(recipe.id, new Callback<Recipe>() {
                @Override
                public void onSuccess(Recipe recipe) {
                    ProgressDialogHelper.setRunning(cx, false);
                    if (modelType == RecipeGridView.Model_Favority){
                        RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_FAVORITY);
                    }else if (modelType == RecipeGridView.Model_Search){
                        RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_SEARCE);
                    }else if(modelType == RecipeGridView.Model_History){
                        RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_ALL);
                    }else {
                        RecipeDetailPage.show(null,recipe.id,RecipeDetailPage.unKnown, RecipeRequestIdentification.RECIPE_SORTED);
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.showShort("菜谱不存在或已下架");
                }
            });
        }
    }


}
