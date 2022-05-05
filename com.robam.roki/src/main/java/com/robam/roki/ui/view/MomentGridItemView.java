package com.robam.roki.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnLongClick;

public class MomentGridItemView extends FrameLayout {

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

    Context cx;
    int smallHeight, middleHeight;
    CookAlbum album;

    public MomentGridItemView(Context context) {
        super(context);
        computeHeight();
        init(context);
    }

    @OnLongClick(R.id.layout)
    public boolean onLongClick(View view) {
        onDelete();
        return true;
    }


    void init(Context cx) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_moment_grid_item,
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

    void showData(CookAlbum album, final boolean isSmallSize) {

        this.album = album;
        imgAlbum.getLayoutParams().height = isSmallSize ? smallHeight : middleHeight;

        txtDesc.setText(album.desc);
        txtPraiseCount.setText(String.format("点赞(%s)", album.praiseCount));
        imgAlbum.setImageDrawable(null);
        ImageUtils.displayImage(cx, album.imgUrl, imgAlbum);

        CookbookManager.getInstance().getCookbookById(album.bookId, new Callback<Recipe>() {
            @Override
            public void onSuccess(Recipe cookbook) {
                txtName.setText(cookbook.name);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }


    void onDelete() {
        if (!UiHelper.checkAuthWithDialog(getContext(),PageKey.UserLogin)) return;

        final Context cx = getContext();

        DialogHelper.newDialog_OkCancel(getContext(), "确认删除此项?", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

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
                        }
                    }
                }
        ).show();
    }
}
