package com.robam.roki.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.j256.ormlite.stmt.query.In;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.ui.UiHelper;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.PublishApi;
import com.robam.roki.net.request.bean.ResultBean;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.recipedetail.DeletePublicDialog;
import com.robam.roki.ui.activity3.recipedetail.ProductDetailActivity;
import com.robam.roki.ui.extension.GlideApp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import jp.wasabeef.glide.transformations.MaskTransformation;

public class MomentGridItemView extends FrameLayout implements OnRequestListener {

    final float Small_Height_Scale = 251 / 640f;
    final float Middle_Height_Scale = 320 / 640f;

    @InjectView(R.id.imgAlbum)
    ImageView imgAlbum;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.edtName)
    TextView txtName;
    @InjectView(R.id.txtPraiseCount)
    TextView txtPraiseCount;
    @InjectView(R.id.layout)
    LinearLayout layout;


    @InjectView(R.id.imgSrcLogo)
    ImageView imgScr;

    @InjectView(R.id.ll_fabulous)
    LinearLayout llFabulous;

    Context cx;
    int smallHeight, middleHeight;
    CookAlbum album;

    private static RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.ALL); //缓存; //圆角

    public MomentGridItemView(Context context) {
        super(context);
        computeHeight();
        init(context);
    }

    @OnLongClick(R.id.layout)
    public boolean onLongClick(View view) {
//        onDelete();
        return true;
    }


    @OnClick(R.id.layout)
    public void onClick(View view) {
        Intent intent=new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.Companion.getCOOKALBUM(),album);
        getContext().startActivity(intent);

    }

    private PublishApi mPublishApi;

    View view;
    void init(Context cx) {

        view = LayoutInflater.from(cx).inflate(R.layout.view_moment_grid_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        mPublishApi=new PublishApi(this);


        llFabulous.setOnClickListener(view1 -> {


                if (imgScr.isSelected()) {

                    mPublishApi.noPraise(R.id.img_thumbs_up_publish, album.id);
                } else {

                    mPublishApi.praisePublic(R.id.img_thumbs_up_publish, album.id);
                }
        });
    }


    void computeHeight() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(getContext());
        smallHeight = (int) (screenWidth * Small_Height_Scale);
        middleHeight = (int) (screenWidth * Middle_Height_Scale);
    }

    void showData(CookAlbum album, final boolean isSmallSize) {

        this.album = album;
        imgAlbum.getLayoutParams().height = isSmallSize ? smallHeight : middleHeight;

        txtDesc.setText(album.desc);
        txtPraiseCount.setText(String.format("%s", album.praiseCount));
        imgAlbum.setImageDrawable(null);
        if (album.imgUrl == null || album.imgUrl.length() == 0){
            imgAlbum.setVisibility(GONE);
        }else {
            GlideApp.with(getContext())
                    .load(album.imgUrl)
                    .apply(maskOption)
                    .into(imgAlbum);
        }

//        ImageUtils.displayImage(album.imgUrl, imgAlbum);

        imgScr.setSelected(album.hasPraised);
        txtName.setText(album.bookName);


    }


    void onDelete() {
        if (!UiHelper.checkAuthWithDialog(getContext(),PageKey.UserLogin)) return;

        final Context cx = getContext();

        DeletePublicDialog mDeletePublicDialog= new DeletePublicDialog(getContext(), v -> {

            ProgressDialogHelper.setRunning(cx, true);
            CookbookManager.getInstance().removeCookAlbum(album.id, new VoidCallback() {
                @Override
                public void onSuccess() {
                    ProgressDialogHelper.setRunning(cx, false);
                    EventUtils
                            .postEvent(new HomeRecipeViewEvent(0));
                }

                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.showThrowable(t);
                }
            });
        });
        mDeletePublicDialog.create();
        mDeletePublicDialog.show();

    }

    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {

    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {
        if (requestId==R.id.img_thumbs_up_publish){
            if (imgScr.isSelected()){
                txtPraiseCount.setText(String.format("%s", --album.praiseCount+""));
                imgScr.setSelected(false);
            }else{
                txtPraiseCount.setText(String.format("%s", ++album.praiseCount+""));
                imgScr.setSelected(true);
            }
            album.hasPraised=imgScr.isSelected();
        }
    }
}
