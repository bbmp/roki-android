package com.robam.roki.ui.page;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.ShareRecipePictureEvent;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.PickImageHelperTwo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RecipeShowPage extends HeadPage {

    @InjectView(R.id.txtContent)
    EditText txtContent;

    @InjectView(R.id.imgContent)
    ImageView imgContent;

    long bookId;
    Bitmap bmp;

    @Override
    public View onCreateContentView(LayoutInflater inflater,
                                    ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        if (bd != null) {
            bookId = bd.getLong(PageArgumentKey.BookId);
            bmp = bd.getParcelable(PageArgumentKey.Bitmap);
        }

        View view = inflater.inflate(R.layout.page_recipe_show_cooking, container, false);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            imgContent.setImageBitmap(bmp);


        }
        return view;
    }

    @OnClick(R.id.txtConfirm)
    public void onClick() {
        try {
            onShare();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    void onShare() {
        String text = txtContent.getText().toString();
        Preconditions.checkState(!Strings.isNullOrEmpty(text), "忘了写点什么...");
        Preconditions.checkState(text.length() < 30, "您写太多了");
        Preconditions.checkNotNull(bmp, "无图无真相");

        CookbookManager.getInstance().submitCookAlbum(bookId, bmp, text,
                new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("分享成功");
                        UIService.getInstance().popBack();
                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeShowMomnet));
                        EventUtils.postEvent(new ShareRecipePictureEvent());
                        RecipeDetailPage.show(bookId, RecipeDetailPage.SharePage);

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                        UIService.getInstance().popBack();
                    }
                });
    }


    static public void showCooking(Activity atv, final long bookId) {
        PickImageHelperTwo.PickCallbackTwo callback = new PickImageHelperTwo.PickCallbackTwo() {

            @Override
            public void onPickComplete(Bitmap bmp) {

                Bundle bd = new Bundle();
                bd.putLong(PageArgumentKey.BookId, bookId);
                bd.putParcelable(PageArgumentKey.Bitmap, bmp);
                UIService.getInstance().postPage(PageKey.RecipeShow, bd);
            }

            @Override
            public void onPickComplete(String bmp) {

            }
        };
        PickImageHelperTwo helper = new PickImageHelperTwo(atv, callback);
//        helper.setPickSize(600, 400);
        helper.showPickDialog("分享我的菜");

    }

}
